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
import hk.gov.cmc.dao.maintainmessage.application.IasApplicationDAO;
import hk.gov.cmc.dao.maintainmessage.notification.IasNotiDAO;
import hk.gov.cmc.dao.maintainmessage.todoitem.IasToDoItemDAO;
import hk.gov.cmc.model.maintainmessage.job.IasMsgStatusQueueJob;
import hk.gov.cmc.persistence.connection.hpfw.HPFW_Connection;
import hk.gov.cmc.persistence.connection.hpfw.Parameter;
import hk.gov.cmc.persistence.maintainmessage.application.IasUserApplication_;
import hk.gov.cmc.persistence.maintainmessage.message.IasUserMessage_;
import hk.gov.cmc.persistence.maintainmessage.todoitem.IasUserToDoItem_;
import hk.gov.cmc.utils.job.JobControlUtils;

public class CmcDeleteDeRegIasUserMsgJob {

    private static Log logger = LogFactory.getLog(CmcDeleteDeRegIasUserMsgJob.class);
    private final static CmcEnvProperties cmcEnvProperties = new CmcEnvProperties();

    public void execute(Map<String, Object> params) throws Exception {

        logger.info("[BATCH_JOB]CmcDeleteDeRegIasUserMsgJob - START");

        HPFW_Connection conn = null;
        JobControlUtils jobControlUtils = new JobControlUtils();

        Properties prop = cmcEnvProperties.getProperties();
        String jobControlName = prop
                .getProperty(CmcAppPropertyNames.JOB_CONTROL_DELETE_DEREG_IAS_USER_MSG_PROPERTY_NAME);
        String iasDeregisterUserDeleteMsgLimit = prop
                .getProperty(CmcAppPropertyNames.IAS_DEREGISTER_USER_DELETE_MSG_LIMIT);

        try {
            Properties localProp = new Properties();
            localProp.putAll(prop);
            if (jobControlUtils.lockJobControl(jobControlName)) {
                conn = HPFW_Connection.getHPFW_Connection();

                logger.info(
                        "[BATCH_JOB]CmcDeleteDeRegIasUserMsgJob - Get DeReg User Job From IasMsgStatusQueue - Start");

                IasNotiDAO iasNotiDAO = new IasNotiDAO();
                IasToDoItemDAO iasToDoItemDAO = new IasToDoItemDAO();
                IasApplicationDAO iasApplicationDAO = new IasApplicationDAO();

                // Get DeReg User Job From IasMsgStatusQueue
                List<IasMsgStatusQueueJob> iasJobList = iasNotiDAO.getDeRegUserJobFromIasMsgStatusQueue(conn);

                for (IasMsgStatusQueueJob iasJob : iasJobList) {

                    logger.info("[BATCH_JOB]CmcDeleteDeRegIasUserMsgJob - Mark delete for JobId=" + iasJob.getJobId()
                            + ", NotiId=" + iasJob.getNotiId()
                            + ", CreateDt=" + iasJob.getCreateDt() + ", iasDeregisterUserDeleteMsgLimit="
                            + iasDeregisterUserDeleteMsgLimit);

                    conn.begin(null, conn.getLastUpTime(), HPFW_Connection.DIRECT_WITH_HISTORY);

                    ArrayList<Parameter> paraSetList = new ArrayList<Parameter>();
                    ArrayList<Parameter> paraWhereList = new ArrayList<Parameter>();

                    int iasUserMsgNeededToDelete = iasNotiDAO.getNumberOfNotDeletedIasUserMsgByNotiIdAndCreateDt(conn,
                            iasJob.getNotiId(), iasJob.getCreateDt());
                    logger.info("[BATCH_JOB]CmcDeleteDeRegIasUserMsgJob - initial iasUserMsgNeededToDelete: "
                            + iasUserMsgNeededToDelete);
                    while (iasUserMsgNeededToDelete > 0) {
                        paraSetList.add(new Parameter(Parameter.String, StatusConstants.DELETE_IND_DELETED));
                        paraWhereList.add(new Parameter(Parameter.String, iasJob.getNotiId()));
                        paraWhereList.add(new Parameter(Parameter.Timestamp, iasJob.getCreateDt()));
                        paraWhereList.add(
                                new Parameter(Parameter.Integer, Integer.parseInt(iasDeregisterUserDeleteMsgLimit)));
                        IasUserMessage_.update(conn, "set DELETE_IND = ?", paraSetList,
                                "where DELETE_IND != 'Y' and NOTI_ID = ? and CREATE_DT < ? order by ias_msg_id limit ?",
                                paraWhereList);
                        conn.commit();
                        iasUserMsgNeededToDelete = iasNotiDAO.getNumberOfNotDeletedIasUserMsgByNotiIdAndCreateDt(conn,
                                iasJob.getNotiId(), iasJob.getCreateDt());
                        logger.info("[BATCH_JOB]CmcDeleteDeRegIasUserMsgJob - iasUserMsgNeededToDelete: "
                                + iasUserMsgNeededToDelete);
                    }

                    int iasToDoItemNeededToDelete = iasToDoItemDAO.getNumberOfNotDeletedIasToDoItemByNotiIdAndCreateDt(
                            conn, iasJob.getNotiId(), iasJob.getCreateDt());
                    logger.info("[BATCH_JOB]CmcDeleteDeRegIasUserMsgJob - initial iasToDoItemNeededToDelete: "
                            + iasToDoItemNeededToDelete);
                    while (iasToDoItemNeededToDelete > 0) {
                        paraSetList.add(new Parameter(Parameter.String, StatusConstants.DELETE_IND_DELETED));
                        paraWhereList.add(new Parameter(Parameter.String, iasJob.getNotiId()));
                        paraWhereList.add(new Parameter(Parameter.Timestamp, iasJob.getCreateDt()));
                        paraWhereList.add(
                                new Parameter(Parameter.Integer, Integer.parseInt(iasDeregisterUserDeleteMsgLimit)));
                        IasUserToDoItem_.update(conn, "set DELETE_IND = ?", paraSetList,
                                "where DELETE_IND != 'Y' and NOTI_ID = ? and CREATE_DT < ? order by ias_to_do_item_id limit ?",
                                paraWhereList);
                        conn.commit();
                        iasToDoItemNeededToDelete = iasToDoItemDAO.getNumberOfNotDeletedIasToDoItemByNotiIdAndCreateDt(
                                conn, iasJob.getNotiId(), iasJob.getCreateDt());
                        logger.info("[BATCH_JOB]CmcDeleteDeRegIasUserMsgJob - iasToDoItemNeededToDelete: "
                                + iasToDoItemNeededToDelete);
                    }

                    int iasApplicationNeededToDelete = iasApplicationDAO
                            .getNumberOfNotDeletedIasApplicationByNotiIdAndCreateDt(conn, iasJob.getNotiId(),
                                    iasJob.getCreateDt());
                    logger.info("[BATCH_JOB]CmcDeleteDeRegIasUserMsgJob - initial iasApplicationNeededToDelete: "
                            + iasApplicationNeededToDelete);
                    while (iasApplicationNeededToDelete > 0) {
                        paraSetList.add(new Parameter(Parameter.String, StatusConstants.DELETE_IND_DELETED));
                        paraWhereList.add(new Parameter(Parameter.String, iasJob.getNotiId()));
                        paraWhereList.add(new Parameter(Parameter.Timestamp, iasJob.getCreateDt()));
                        paraWhereList.add(
                                new Parameter(Parameter.Integer, Integer.parseInt(iasDeregisterUserDeleteMsgLimit)));
                        IasUserApplication_.update(conn, "set DELETE_IND = ?", paraSetList,
                                "where DELETE_IND != 'Y' and NOTI_ID = ? and CREATE_DT < ? order by app_ref_num limit ?",
                                paraWhereList);
                        conn.commit();
                        iasApplicationNeededToDelete = iasApplicationDAO
                                .getNumberOfNotDeletedIasApplicationByNotiIdAndCreateDt(conn, iasJob.getNotiId(),
                                        iasJob.getCreateDt());
                        logger.info("[BATCH_JOB]CmcDeleteDeRegIasUserMsgJob - iasApplicationNeededToDelete: "
                                + iasApplicationNeededToDelete);
                    }

                    conn.begin(null, conn.getLastUpTime(), HPFW_Connection.DIRECT);
                    iasNotiDAO.updateIasMsgJobStatus(conn, IntegrationConstants.JOB_STATUS_COMPLETE, iasJob.getJobId());
                    conn.commit();
                }

                logger.info("[BATCH_JOB]CmcDeleteDeRegIasUserMsgJob - Get DeReg User Job From IasMsgStatusQueue - End");

                // release lock for concurrent control
                jobControlUtils.releaseJobControl(jobControlName);

            } else {
                logger.info("[CmcDeleteDeRegIasUserMsgJob]CMC_MARS_SYN_JOB_LOCK table cannot be locked.");
            }
        } catch (Exception e) {
            logger.error("CmcDeleteDeRegIasUserMsgJob exception", e);
            throw e;
        } finally {
            if (conn != null) {
                HPFW_Connection.close(conn);
            }
            jobControlUtils.releaseJobControl(jobControlName);
        }

        logger.info("[BATCH_JOB]CmcDeleteDeRegIasUserMsgJob - END");
    }

}
