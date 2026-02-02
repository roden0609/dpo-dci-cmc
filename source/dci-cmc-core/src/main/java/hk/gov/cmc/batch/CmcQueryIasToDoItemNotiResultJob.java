package hk.gov.cmc.batch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import hk.gov.cmc.common.CmcAppPropertyNames;
import hk.gov.cmc.common.IntegrationConstants;
import hk.gov.cmc.config.CmcEnvProperties;
import hk.gov.cmc.dao.maintainmessage.notification.IasToDoItemNotiDAO;
import hk.gov.cmc.eid.bean.EIDResponseBean;
import hk.gov.cmc.eid.bean.StatusResultItem;
import hk.gov.cmc.eid.bean.StatusResultsBean;
import hk.gov.cmc.eid.bean.TxIdBean;
import hk.gov.cmc.eid.client.EIDUtils;
import hk.gov.cmc.persistence.connection.hpfw.HPFW_Connection;
import hk.gov.cmc.persistence.connection.hpfw.Parameter;
import hk.gov.cmc.persistence.maintainmessage.todoitem.IasUserToDoItem_;
import hk.gov.cmc.utils.job.JobControlUtils;

public class CmcQueryIasToDoItemNotiResultJob {

    private static Log logger = LogFactory.getLog(CmcQueryIasToDoItemNotiResultJob.class);
    private final static CmcEnvProperties cmcEnvProperties = new CmcEnvProperties();
    private static final String REQUSET_PARA_NAME_SIZE_LIMIT = "sizeLimit";

    public void execute(Map<String, Object> params) throws Exception {

        logger.info("[BATCH_JOB]CmcQueryIasToDoItemNotiResultJob - START");

        HPFW_Connection conn = null;
        JobControlUtils jobControlUtils = new JobControlUtils();

        Properties prop = cmcEnvProperties.getProperties();
        String jobControlName = prop.getProperty(CmcAppPropertyNames.JOB_CONTROL_GET_IAS_TO_DO_ITEM_NOTI_RESULT_PROPERTY_NAME);
        String iasGetNotiResultEndPoint = prop.getProperty(CmcAppPropertyNames.IAS_GET_NOTI_RESULT_END_POINT_PROPERTY_NAME);
        int getResultDayLimit = Integer.parseInt(prop.getProperty(CmcAppPropertyNames.GET_IAS_NOTI_RESULT_DAY_LIMIT_PROPERTY_NAME));
        int IasLiveToTimeDay = Integer.parseInt(prop.getProperty(CmcAppPropertyNames.GET_IAS_NOTI_RESULT_TIME_TO_LIVE_DAY_PROPERTY_NAME));
        int getResultSizeLimit = Integer.parseInt(prop.getProperty(CmcAppPropertyNames.GET_IAS_NOTI_RESULT_SIZE_LIMIT_PROPERTY_NAME));

        if (params != null) {
            if  (params.get(REQUSET_PARA_NAME_SIZE_LIMIT)!=null && !params.get(REQUSET_PARA_NAME_SIZE_LIMIT).equals("")) {
                getResultSizeLimit = Integer.parseInt((String)params.get(REQUSET_PARA_NAME_SIZE_LIMIT));
            }
        }
        logger.info("[BATCH_JOB]CmcQueryIasToDoItemNotiResultJob - sizeLimit="+getResultSizeLimit);
        
        try {
            Properties localProp = new Properties();
            localProp.putAll(prop);
            if (jobControlUtils.lockJobControl(jobControlName)) {
                conn = HPFW_Connection.getHPFW_Connection();
                conn.begin(null, conn.getLastUpTime(), HPFW_Connection.DIRECT_WITH_HISTORY);

                logger.info("[BATCH_JOB]CmcQueryIasToDoItemNotiResultJob - Update IAS_DELIVERY_STATUS - Start");
                IasToDoItemNotiDAO iasToDoItemNotiDAO = new IasToDoItemNotiDAO();
                logger.info("[BATCH_JOB]CmcQueryIasToDoItemNotiResultJob - IasLiveToTimeDay: " + IasLiveToTimeDay 
                    + ", getResultDayLimit: " + getResultDayLimit + ", getResultSizeLimit: " + getResultSizeLimit);
                List<String> txIdList = iasToDoItemNotiDAO.getTxIdFromSentIasUserToDoItem(conn, IasLiveToTimeDay, getResultDayLimit, getResultSizeLimit);
                logger.info("[BATCH_JOB]CmcQueryIasToDoItemNotiResultJob - TX_ID List Size: " + txIdList.size());

                boolean eIDUtilsInit=false;

                for (String txId: txIdList) {

                    logger.info("[BATCH_JOB]CmcQueryIasToDoItemNotiResultJob - txId="+txId);

                    TxIdBean txIdBean = new TxIdBean();
                    txIdBean.setTxID(txId);

                    // Call getNotificationID API to get missing NotiID from iAM Smart system by Service Provider ID and Open ID
                    if (!eIDUtilsInit) {
                        EIDUtils.initialize(prop);
                        eIDUtilsInit=true;
                    }

                    EIDResponseBean statusResultResponse = EIDUtils.doRequestQueryNotificationDeliveryStatus(iasGetNotiResultEndPoint, txIdBean);
                    if (statusResultResponse!=null) {

                        logger.debug("[BATCH_JOB]CmcQueryIasToDoItemNotiResultJob StatusResult Response Content="+statusResultResponse.getContent());

                        if (((IntegrationConstants.GET_NOTI_ID_RESULT_CODE_SUCCESS.equalsIgnoreCase(statusResultResponse.getCode())) 
                                || (IntegrationConstants.GET_NOTI_ID_RESULT_CODE_PARTIAL_SUCCESS.equalsIgnoreCase(statusResultResponse.getCode())))
                            && statusResultResponse.getContent() != null 
                            && statusResultResponse.getContent().length() > 0
                        ) {
                            StatusResultsBean statusResult = new StatusResultsBean(statusResultResponse.getContent());

                            if (statusResult!=null) {

                                ArrayList<StatusResultItem> statusResultItemList = statusResult.getStatusResults();

                                for (StatusResultItem statusResultItem: statusResultItemList) {
                                    if (iasToDoItemNotiDAO.isIasUserToDoItemExistByNotiIdToDoItemIdTxId(conn, statusResultItem.getNotificationID(), statusResultItem.getMessageID(), txId)) {
                                        // Update IAS_DELIVERY_STATUS of IAS_USER_TO_DO_ITEM
                                        ArrayList<Parameter> paraSetList = new ArrayList<Parameter>();
                                        ArrayList<Parameter> paraWhereList = new ArrayList<Parameter>();
                                        paraSetList.add(new Parameter(Parameter.String, statusResultItem.getStatus()));
                                        paraWhereList.add(new Parameter(Parameter.String, statusResultItem.getNotificationID()));
                                        paraWhereList.add(new Parameter(Parameter.String, statusResultItem.getMessageID()));
                                        paraWhereList.add(new Parameter(Parameter.String, txId));

                                        IasUserToDoItem_.update(conn, "set IAS_DELIVERY_STATUS = ?", paraSetList, "where NOTI_ID = ? and IAS_TO_DO_ITEM_ID = ? and TX_ID = ? ", paraWhereList);
                                        conn.commit();
                                    } else {
                                        logger.warn("[BATCH_JOB]CmcQueryIasToDoItemNotiResultJob IAS_USER_TO_DO_ITEM not exist. " 
                                            + "NOTI_ID: " + statusResultItem.getNotificationID() + ", IAS_MSG_ID: " + statusResultItem.getMessageID() + ", TX_ID: " + txId);
                                    }
                                }
                            }
                        } else {
                            logger.warn("[BATCH_JOB]CmcQueryIasToDoItemNotiResultJob StatusResult Fail. Code=" + statusResultResponse.getCode()+", Message=" + statusResultResponse.getMessage() + ", will retry in next run. TX_ID: " + txId);
                        }
                    } else {
                        logger.warn("[BATCH_JOB]CmcQueryIasToDoItemNotiResultJob StatusResult Fail. statusResultResponse is null, will retry in next run. TX_ID: " + txId);
                    }
                }


                logger.info("[BATCH_JOB]CmcQueryIasToDoItemNotiResultJob - Update IAS_DELIVERY_STATUS- End");

                // release lock for concurrent control
                jobControlUtils.releaseJobControl(jobControlName);

            } else {
                logger.info("[CmcQueryIasToDoItemNotiResultJob]CMC_MARS_SYN_JOB_LOCK table cannot be locked.");
            }
        } catch (Exception e) {
            logger.error("CmcQueryIasToDoItemNotiResultJob exception", e);
            throw e;
        } finally {
            if (conn != null) {
                HPFW_Connection.close(conn);
            }
            jobControlUtils.releaseJobControl(jobControlName);
        }

        logger.info("[BATCH_JOB]CmcQueryIasToDoItemNotiResultJob - END");
    }

}
