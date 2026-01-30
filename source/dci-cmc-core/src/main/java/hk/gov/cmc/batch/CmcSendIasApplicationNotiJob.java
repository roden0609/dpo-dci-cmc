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
import hk.gov.cmc.dao.maintainmessage.notification.IasApplicationNotiDAO;
import hk.gov.cmc.eid.client.EIDUtils;
import hk.gov.cmc.model.maintainmessage.application.IasUserApplication;
import hk.gov.cmc.model.maintainmessage.key.IasClientIdNotiIdPair;
import hk.gov.cmc.persistence.connection.hpfw.HPFW_Connection;
import hk.gov.cmc.processor.maintainmessage.application.IasApplicationPushNotiProcessor;
import hk.gov.cmc.processor.maintainmessage.application.IasApplicationSwitchNotiIdProcessor;
import hk.gov.cmc.utils.job.JobControlUtils;


public class CmcSendIasApplicationNotiJob {

    private static Log logger = LogFactory.getLog(CmcSendIasApplicationNotiJob.class);
    private final static CmcEnvProperties cmcEnvProperties = new CmcEnvProperties();

    public void execute(Map<String, Object> params) throws Exception {

        logger.info("[BATCH_JOB]CmcSendIasApplicationNotiJob - BEGIN");

        HPFW_Connection conn = null;
        JobControlUtils jobControlUtils = new JobControlUtils();
        Properties prop = cmcEnvProperties.getProperties();
        String jobControlName = prop.getProperty(CmcAppPropertyNames.JOB_CONTROL_SEND_IAS_APPLICATION_NOTI_PROPERTY_NAME);
        int iasMsgProcessBatchLimit = Integer.parseInt(prop.getProperty(CmcAppPropertyNames.IAS_MSG_PROCESS_BATCH_LIMIT_PROPERTY_NAME));

        try {
            if (jobControlUtils.lockJobControl(jobControlName)) {

                conn = HPFW_Connection.getHPFW_Connection();
                conn.begin(null, conn.getLastUpTime(), HPFW_Connection.DIRECT_WITH_HISTORY);

                IasApplicationNotiDAO iasAppNotiDAO = new IasApplicationNotiDAO();
                IasApplicationSwitchNotiIdProcessor iasAppSwithNotiIdProcessor = new IasApplicationSwitchNotiIdProcessor();
                IasApplicationPushNotiProcessor iasAppPushNotiProcessor = new IasApplicationPushNotiProcessor();
                List<IasClientIdNotiIdPair> deregClientIdNotiIdPairList = new ArrayList<IasClientIdNotiIdPair>();

                EIDUtils.initialize(prop);

                logger.info("[BATCH_JOB]CmcSendIasApplicationNotiJob - Get NotiID by OpenID with iAM Smart API - Start");
                List<IasUserApplication> openIdNotiIdMissList = iasAppNotiDAO.getNotiIdMissingIasUserApplication(conn, RecipientIDTypeConstant.OPEN_ID, iasMsgProcessBatchLimit);
                logger.info("[BATCH_JOB]CmcSendIasApplicationNotiJob - openIdNotiIdMissList.size()=" + openIdNotiIdMissList.size());
                if (openIdNotiIdMissList.size() > 0) {
                    List<IasClientIdNotiIdPair> deregClientIdNotiIdPairListByOpenID = iasAppSwithNotiIdProcessor.updateIasUserAppNotiIDByESOpenID(conn, true, openIdNotiIdMissList);
                    deregClientIdNotiIdPairList.addAll(deregClientIdNotiIdPairListByOpenID);
                }
                logger.info("[BATCH_JOB]CmcSendIasApplicationNotiJob - Get NotiID by OpenID with iAM Smart API - End");


                logger.info("[BATCH_JOB]CmcSendIasApplicationNotiJob - Switch NotiID by HKID with iAM Smart API - Start");
                List<IasUserApplication> hkidNotiIdMissList = iasAppNotiDAO.getNotiIdMissingIasUserApplication(conn, RecipientIDTypeConstant.HKID, iasMsgProcessBatchLimit);
                logger.info("[BATCH_JOB]CmcSendIasApplicationNotiJob - HKIdNotiIdMissList.size()=" + hkidNotiIdMissList.size());
                if (hkidNotiIdMissList.size() > 0) {
                    List<IasClientIdNotiIdPair> deregClientIdNotiIdPairListByHKID = iasAppSwithNotiIdProcessor.updateIasUserAppNotiIDByESHKID(conn, true, hkidNotiIdMissList);
                    deregClientIdNotiIdPairList.addAll(deregClientIdNotiIdPairListByHKID);
                }
                logger.info("[BATCH_JOB]CmcSendIasApplicationNotiJob - Switch NotiID by HKID with iAM Smart API - End");


                logger.info("[BATCH_JOB]CmcSendIasApplicationNotiJob - Push Noti with iAM Smart API - Start");
                List<IasUserApplication> outstandingIasUserApplicationList = iasAppNotiDAO.getOutstandingIasApplication(conn, IntegrationConstants.IAS_NOTI_STATUS_NEW, StatusConstants.DELETE_IND_NOT_DELETED, iasMsgProcessBatchLimit);
                logger.info("[BATCH_JOB]CmcSendIasApplicationNotiJob - outstandingIasUserApplicationList.size()=" + outstandingIasUserApplicationList.size());
                iasAppPushNotiProcessor.pushIasApplicationToIAMSmart(conn, true, outstandingIasUserApplicationList, deregClientIdNotiIdPairList);
                logger.info("[BATCH_JOB]CmcSendIasApplicationNotiJob - Push Noti with iAM Smart API - End");

                logger.info("[BATCH_JOB]CmcSendIasApplicationNotiJob - Push Noti with iAM Smart API - Start");
                List<IasUserApplication> pendingDeleteIasUserApplicationList = iasAppNotiDAO.getPendingDeleteIasApplication(conn, IntegrationConstants.IAS_NOTI_STATUS_SENT, StatusConstants.DELETE_IND_PENDING, iasMsgProcessBatchLimit);
                logger.info("[BATCH_JOB]CmcSendIasApplicationNotiJob - pendingDeleteIasUserApplicationList.size()=" + pendingDeleteIasUserApplicationList.size());
                iasAppPushNotiProcessor.pushIasApplicationToIAMSmartForDeletion(conn, true, pendingDeleteIasUserApplicationList, deregClientIdNotiIdPairList);
                logger.info("[BATCH_JOB]CmcSendIasApplicationNotiJob - Push Noti with iAM Smart API - End");

                // release lock for concurrent control
                jobControlUtils.releaseJobControl(jobControlName);

            } else {
                logger.info("[CmcSendIasApplicationNotiJob]CMC_MARS_SYN_JOB_LOCK table cannot be locked.");
            }
        } catch (Exception e) {
            logger.error("CmcSendIasApplicationNotiJob exception", e);
            throw e;
        } finally {
            if (conn != null) {
                HPFW_Connection.close(conn);
            }
            jobControlUtils.releaseJobControl(jobControlName);
        }

        logger.info("[BATCH_JOB]CmcSendIasApplicationNotiJob - END");
    }

}
