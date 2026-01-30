package hk.gov.cmc.batch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import hk.gov.cmc.common.CmcAppPropertyNames;
import hk.gov.cmc.common.IntegrationConstants;
import hk.gov.cmc.common.RecipientIDTypeConstant;
import hk.gov.cmc.common.StatusConstants;
import hk.gov.cmc.config.CmcEnvProperties;
import hk.gov.cmc.dao.maintainmessage.notification.IasToDoItemNotiDAO;
import hk.gov.cmc.eid.client.EIDUtils;
import hk.gov.cmc.model.maintainmessage.key.IasClientIdNotiIdPair;
import hk.gov.cmc.model.maintainmessage.todoitem.IasUserToDoItem;
import hk.gov.cmc.persistence.connection.hpfw.HPFW_Connection;
import hk.gov.cmc.processor.maintainmessage.todoitem.IasToDoItemPushNotiProcessor;
import hk.gov.cmc.processor.maintainmessage.todoitem.IasToDoItemSwitchNotiIdProcessor;
import hk.gov.cmc.utils.job.JobControlUtils;

public class CmcSendIasToDoItemNotiJob {

    private static Log logger = LogFactory.getLog(CmcSendIasToDoItemNotiJob.class);
    private final static CmcEnvProperties cmcEnvProperties = new CmcEnvProperties();

    public void execute(Map<String, Object> params) throws Exception {

        logger.info("[BATCH_JOB]CmcSendIasToDoItemNotiJob - BEGIN");

        HPFW_Connection conn = null;
        JobControlUtils jobControlUtils = new JobControlUtils();
        Properties prop = cmcEnvProperties.getProperties();
        String jobControlName = prop.getProperty(CmcAppPropertyNames.JOB_CONTROL_SEND_IAS_TO_DO_ITEM_NOTI_PROPERTY_NAME);
        int iasMsgProcessBatchLimit = Integer.parseInt(prop.getProperty(CmcAppPropertyNames.IAS_MSG_PROCESS_BATCH_LIMIT_PROPERTY_NAME));

        try {
            if (jobControlUtils.lockJobControl(jobControlName)) {

                conn = HPFW_Connection.getHPFW_Connection();
                conn.begin(null, conn.getLastUpTime(), HPFW_Connection.DIRECT_WITH_HISTORY);

                IasToDoItemNotiDAO iasToDoItemNotiDAO = new IasToDoItemNotiDAO();
                IasToDoItemSwitchNotiIdProcessor iasToDoItemSwithNotiIdProcessor = new IasToDoItemSwitchNotiIdProcessor();
                IasToDoItemPushNotiProcessor iasToDoItemPushNotiProcessor = new IasToDoItemPushNotiProcessor();
                List<IasClientIdNotiIdPair> deregClientIdNotiIdPairList = new ArrayList<IasClientIdNotiIdPair>();

                EIDUtils.initialize(prop);

                logger.info("[BATCH_JOB]CmcSendIasToDoItemNotiJob - Get NotiID by OpenID with iAM Smart API - Start");
                List<IasUserToDoItem> openIdNotiIdMissList = iasToDoItemNotiDAO.getNotiIdMissingIasUserToDoItem(conn, RecipientIDTypeConstant.OPEN_ID, iasMsgProcessBatchLimit);
                logger.info("[BATCH_JOB]CmcSendIasToDoItemNotiJob - openIdNotiIdMissList.size()=" + openIdNotiIdMissList.size());
                if (openIdNotiIdMissList.size() > 0) {
                    List<IasClientIdNotiIdPair> deregClientIdNotiIdPairListByOpenID = iasToDoItemSwithNotiIdProcessor.updateIasUserToDoItemNotiIDByESOpenID(conn, true, openIdNotiIdMissList);
                    deregClientIdNotiIdPairList.addAll(deregClientIdNotiIdPairListByOpenID);
                }
                logger.info("[BATCH_JOB]CmcSendIasToDoItemNotiJob - Get NotiID by OpenID with iAM Smart API - End");


                logger.info("[BATCH_JOB]CmcSendIasToDoItemNotiJob - Switch NotiID by HKID with iAM Smart API - Start");
                List<IasUserToDoItem> hkidNotiIdMissList = iasToDoItemNotiDAO.getNotiIdMissingIasUserToDoItem(conn, RecipientIDTypeConstant.HKID, iasMsgProcessBatchLimit);
                logger.info("[BATCH_JOB]CmcSendIasToDoItemNotiJob - HKIdNotiIdMissList.size()=" + hkidNotiIdMissList.size());
                if (hkidNotiIdMissList.size() > 0) {
                    List<IasClientIdNotiIdPair> deregClientIdNotiIdPairListByHKID = iasToDoItemSwithNotiIdProcessor.updateIasUserToDoItemNotiIDByESHKID(conn, true, hkidNotiIdMissList);
                    deregClientIdNotiIdPairList.addAll(deregClientIdNotiIdPairListByHKID);
                }
                logger.info("[BATCH_JOB]CmcSendIasToDoItemNotiJob - Switch NotiID by HKID with iAM Smart API - End");


                logger.info("[BATCH_JOB]CmcSendIasToDoItemNotiJob - Push Noti with iAM Smart API - Start");
                List<IasUserToDoItem> outstandingIasUserToDoItemList = iasToDoItemNotiDAO.getOutstandingIasToDoItem(conn, IntegrationConstants.IAS_NOTI_STATUS_NEW, StatusConstants.DELETE_IND_NOT_DELETED, iasMsgProcessBatchLimit);
                logger.info("[BATCH_JOB]CmcSendIasToDoItemNotiJob - outstandingIasUserToDoItemList.size()=" + outstandingIasUserToDoItemList.size());
                iasToDoItemPushNotiProcessor.pushIasToDoItemToIAMSmart(conn, true, outstandingIasUserToDoItemList, deregClientIdNotiIdPairList);
                logger.info("[BATCH_JOB]CmcSendIasToDoItemNotiJob - Push Noti with iAM Smart API - End");

                logger.info("[BATCH_JOB]CmcSendIasToDoItemNotiJob - Push Noti with iAM Smart API - Start");
                List<IasUserToDoItem> pendingDeleteIasUserToDoItemList = iasToDoItemNotiDAO.getPendingDeleteIasToDoItem(conn, IntegrationConstants.IAS_NOTI_STATUS_SENT, StatusConstants.DELETE_IND_PENDING, iasMsgProcessBatchLimit);
                logger.info("[BATCH_JOB]CmcSendIasToDoItemNotiJob - pendingDeleteIasUserToDoItemList.size()=" + pendingDeleteIasUserToDoItemList.size());
                iasToDoItemPushNotiProcessor.pushIasToDoItemToIAMSmartForDeletion(conn, true, pendingDeleteIasUserToDoItemList, deregClientIdNotiIdPairList);
                logger.info("[BATCH_JOB]CmcSendIasToDoItemNotiJob - Push Noti with iAM Smart API - End");


                // release lock for concurrent control
                jobControlUtils.releaseJobControl(jobControlName);

            } else {
                logger.info("[CmcSendIasToDoItemNotiJob]CMC_MARS_SYN_JOB_LOCK table cannot be locked.");
            }
        } catch (Exception e) {
            logger.error("CmcSendIasToDoItemNotiJob exception", e);
            throw e;
        } finally {
            if (conn != null) {
                HPFW_Connection.close(conn);
            }
            jobControlUtils.releaseJobControl(jobControlName);
        }

        logger.info("[BATCH_JOB]CmcSendIasToDoItemNotiJob - END");
    }

}
