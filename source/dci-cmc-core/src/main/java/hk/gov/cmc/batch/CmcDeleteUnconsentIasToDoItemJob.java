package hk.gov.cmc.batch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import hk.gov.cmc.common.CmcAppPropertyNames;
import hk.gov.cmc.common.IntegrationConstants;
import hk.gov.cmc.common.StatusConstants;
import hk.gov.cmc.config.CmcEnvProperties;
import hk.gov.cmc.dao.maintainmessage.notification.IasNotiDAO;
import hk.gov.cmc.dao.maintainmessage.todoitem.IasToDoItemDAO;
import hk.gov.cmc.model.maintainmessage.job.IasMsgStatusQueueJob;
import hk.gov.cmc.model.maintainmessage.todoitem.IasUserToDoItem;
import hk.gov.cmc.persistence.connection.hpfw.HPFW_Connection;
import hk.gov.cmc.persistence.connection.hpfw.Parameter;
import hk.gov.cmc.persistence.maintainmessage.todoitem.IasUserToDoItem_;
import hk.gov.cmc.utils.job.JobControlUtils;

public class CmcDeleteUnconsentIasToDoItemJob {

    private static Log logger = LogFactory.getLog(CmcDeleteUnconsentIasToDoItemJob.class);
    private final static CmcEnvProperties cmcEnvProperties = new CmcEnvProperties();

    public void execute(Map<String, Object> params) throws Exception {

        logger.info("[BATCH_JOB]CmcDeleteUnconsentIasToDoItemJob - START");

        HPFW_Connection conn = null;
        JobControlUtils jobControlUtils = new JobControlUtils();

        Properties prop = cmcEnvProperties.getProperties();
        String jobControlName = prop
                .getProperty(CmcAppPropertyNames.JOB_CONTROL_UNCONSENT_DELETE_TO_DO_ITEM_PROPERTY_NAME);
        String iasUnconsentDeleteToDoItemLimit = prop
                .getProperty(CmcAppPropertyNames.IAS_UNCONSENT_DELETE_TO_DO_ITEM_LIMIT);

        try {
            Properties localProp = new Properties();
            localProp.putAll(prop);
            if (jobControlUtils.lockJobControl(jobControlName)) {
                conn = HPFW_Connection.getHPFW_Connection();

                logger.info(
                        "[BATCH_JOB]CmcDeleteUnconsentIasToDoItemJob - Get Unconsent To-Do Item Job From IasMsgStatusQueue - Start");

                IasNotiDAO iasNotiDAO = new IasNotiDAO();
                IasToDoItemDAO iasToDoItemDAO = new IasToDoItemDAO();

                List<IasMsgStatusQueueJob> iasJobList = iasNotiDAO.getIasMsgStatusQueueJobs(conn,
                        IntegrationConstants.IAS_USER_STATUS_ACTIVE, "T", IntegrationConstants.JOB_STATUS_NEW);
                for (IasMsgStatusQueueJob iasJob : iasJobList) {

                    logger.info("[BATCH_JOB]CmcDeleteUnconsentIasToDoItemJob - Mark delete for JobId="
                            + iasJob.getJobId() + ", NotiId=" + iasJob.getNotiId()
                            + ", spId=" + iasJob.getSpId() + ", CreateDt=" + iasJob.getCreateDt()
                            + ", iasUnconsentDeleteToDoItemLimit=" + iasUnconsentDeleteToDoItemLimit);

                    conn.begin(null, conn.getLastUpTime(), HPFW_Connection.DIRECT_WITH_HISTORY);

                    List<IasUserToDoItem> iasUserToDoItemKeyList = iasToDoItemDAO
                            .getIasUserToDoItemKeyBySpIdNotiIdCreateDt(conn, iasJob.getSpId(), iasJob.getNotiId(),
                                    iasJob.getCreateDt());
                    logger.info("[BATCH_JOB]CmcDeleteUnconsentIasToDoItemJob - initial iasToDoItemNeededToDelete: "
                            + iasUserToDoItemKeyList.size());

                    int updatedCnt = 0;
                    for (IasUserToDoItem iasUserToDoItemKey : iasUserToDoItemKeyList) {
                        logger.debug("[BATCH_JOB]CmcDeleteUnconsentIasToDoItemJob - iasUserToDoItemKey - clientId: "
                                + iasUserToDoItemKey.getClientId()
                                + ", recipientId: " + iasUserToDoItemKey.getRecipientId()
                                + ", iasToDoItemId: " + iasUserToDoItemKey.getIasToDoItemId());

                        ArrayList<Parameter> paraSetList = new ArrayList<Parameter>();
                        ArrayList<Parameter> paraWhereList = new ArrayList<Parameter>();

                        paraSetList.add(new Parameter(Parameter.String, StatusConstants.DELETE_IND_DELETED));
                        paraWhereList.add(new Parameter(Parameter.String, iasUserToDoItemKey.getClientId()));
                        paraWhereList.add(new Parameter(Parameter.String, iasUserToDoItemKey.getRecipientId()));
                        paraWhereList.add(new Parameter(Parameter.String, iasUserToDoItemKey.getIasToDoItemId()));
                        IasUserToDoItem_.update(conn, "set DELETE_IND = ?", paraSetList,
                                "where DELETE_IND != 'Y' and CLIENT_ID = ? and RECIPIENT_ID = ? and IAS_TO_DO_ITEM_ID = ?",
                                paraWhereList);
                        updatedCnt++;

                        if (updatedCnt >= Integer.parseInt(iasUnconsentDeleteToDoItemLimit)) {
                            conn.commit();
                            updatedCnt = 0;
                            paraSetList.clear();
                            paraWhereList.clear();
                        }
                    }

                    conn.begin(null, conn.getLastUpTime(), HPFW_Connection.DIRECT);
                    iasNotiDAO.updateIasMsgJobStatus(conn, IntegrationConstants.JOB_STATUS_COMPLETE, iasJob.getJobId());
                    conn.commit();
                }

                logger.info(
                        "[BATCH_JOB]CmcDeleteUnconsentIasToDoItemJob - Get Unconsent To-Do Item Job From IasMsgStatusQueue - End");

                // release lock for concurrent control
                jobControlUtils.releaseJobControl(jobControlName);

            } else {
                logger.info("[CmcDeleteUnconsentIasToDoItemJob]CMC_MARS_SYN_JOB_LOCK table cannot be locked.");
            }
        } catch (Exception e) {
            logger.error("CmcDeleteUnconsentIasToDoItemJob exception", e);
            throw e;
        } finally {
            if (conn != null) {
                HPFW_Connection.close(conn);
            }
            jobControlUtils.releaseJobControl(jobControlName);
        }

        logger.info("[BATCH_JOB]CmcDeleteUnconsentIasToDoItemJob - END");
    }

}
