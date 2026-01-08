package hk.gov.cmc.dao.maintainmessage.notification;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import hk.gov.cmc.common.IntegrationConstants;
import hk.gov.cmc.common.StatusConstants;
import hk.gov.cmc.model.maintainmessage.emessage.IasUserMsg;
import hk.gov.cmc.model.maintainmessage.job.IasAssoQueueJob;
import hk.gov.cmc.model.maintainmessage.job.IasMsgStatusQueueJob;
import hk.gov.cmc.model.maintainmessage.user.IasUser;
import hk.gov.cmc.persistence.connection.hpfw.HPFW_Connection;
import hk.gov.cmc.persistence.connection.hpfw.Parameter;

public class IasNotiDAO {

    private static Log logger = LogFactory.getLog(IasNotiDAO.class);

    public IasNotiDAO() {
        super();
    }

    public List<IasUserMsg> getNotiIdMissingIasUserMessage(HPFW_Connection conn, int iasMsgProcessBatchLimit)
            throws Exception {
        logger.debug("getIasUserInfo - START");

        IasUser iasUser = null;
        ResultSet rs = null;
        ArrayList<IasUserMsg> resultList = new ArrayList<IasUserMsg>();

        try {

            ArrayList<Parameter> paraList = new ArrayList<Parameter>();

            paraList.add(new Parameter(Parameter.String, IntegrationConstants.IAS_NOTI_STATUS_NEW));
            paraList.add(new Parameter(Parameter.Integer, iasMsgProcessBatchLimit));

            rs = conn.getResultSet(SELECT_NOTI_ID_MISSING_IAS_USER_MESSAGE, paraList);

            while (rs.next()) {
                IasUserMsg iasUserMsg = new IasUserMsg();
                iasUserMsg.setClientId(rs.getString("CLIENT_ID"));
                iasUserMsg.setOpenId(rs.getString("OPEN_ID"));
                iasUserMsg.setIasMsgId(rs.getString("IAS_MSG_ID"));
                iasUserMsg.setServiceProviderId(rs.getString("SERVICE_PROVIDER_ID"));

                // Input field for "ias_opt_check_ind" is not used because "ias_show_es_set_btn" derive the same meaning. So, check "ias_show_es_set_btn" while processing message requests.
                iasUserMsg.setIasOptCheckInd(rs.getString("IAS_SHOW_ES_SET_BTN"));
                iasUserMsg.setIasOptSpId(rs.getString("IAS_OPT_SP_ID"));
                iasUserMsg.setIasAutoCreateInd(rs.getString("IAS_AUTO_CREATE_IND"));

                resultList.add(iasUserMsg);
            }

            logger.debug("getIasUserInfo - END");

            return resultList;

        } catch (Exception ex) {
            logger.error("General exception caught in getIasUserInfo", ex);
            throw ex;
        } finally {
            try {
                if (rs != null)
                    HPFW_Connection.close(rs);
            } catch (Exception ex) {
                logger.error("General exception caught in getIasUserInfo - rs.close();", ex);
            }
        }
    }

    public List<IasUserMsg> getOutstandingMessage(HPFW_Connection conn, int iasMsgProcessBatchLimit) throws Exception {
        logger.debug("getOutstandingMessage - START");

        IasUser iasUser = null;
        ResultSet rs = null;
        ArrayList<IasUserMsg> resultList = new ArrayList<IasUserMsg>();

        try {

            ArrayList<Parameter> paraList = new ArrayList<Parameter>();

            paraList.add(new Parameter(Parameter.String, IntegrationConstants.IAS_NOTI_STATUS_NEW));
            paraList.add(new Parameter(Parameter.String, StatusConstants.DELETE_IND_DELETED));
            paraList.add(new Parameter(Parameter.Integer, iasMsgProcessBatchLimit));

            rs = conn.getResultSet(SELECT_OUTSTANDING_MESSAGE, paraList);

            while (rs.next()) {
                IasUserMsg iasUserMsg = new IasUserMsg();
                iasUserMsg.setClientId(rs.getString("CLIENT_ID"));
                iasUserMsg.setOpenId(rs.getString("OPEN_ID"));
                iasUserMsg.setIasMsgId(rs.getString("IAS_MSG_ID"));
                iasUserMsg.setNotiId(rs.getString("NOTI_ID"));
                iasUserMsg.setServiceProviderId(rs.getString("SERVICE_PROVIDER_ID"));
                iasUserMsg.setSubjectEn(rs.getString("SUBJECT_EN"));
                iasUserMsg.setSubjectTc(rs.getString("SUBJECT_TC"));
                iasUserMsg.setSubjectSc(rs.getString("SUBJECT_SC"));
                iasUserMsg.setStatus(rs.getString("STATUS"));
                iasUserMsg.setMapNotiId(rs.getString("MAP_NOTI_ID"));
                iasUserMsg.setIasOptSpId(rs.getString("IAS_OPT_SP_ID"));

                resultList.add(iasUserMsg);
            }

            logger.debug("getOutstandingMessage - END");

            return resultList;

        } catch (Exception ex) {
            logger.error("General exception caught in getOutstandingMessage", ex);
            throw ex;
        } finally {
            try {
                if (rs != null)
                    HPFW_Connection.close(rs);
            } catch (Exception ex) {
                logger.error("General exception caught in getOutstandingMessage - rs.close();", ex);
            }
        }
    }

    public List<IasUserMsg> getMotherMissingVipMapList(HPFW_Connection conn, int iasMsgProcessBatchLimit)
            throws Exception {
        logger.debug("getMotherMissingVipMapList - START");

        IasUser iasUser = null;
        ResultSet rs = null;
        ArrayList<IasUserMsg> resultList = new ArrayList<IasUserMsg>();

        try {

            ArrayList<Parameter> paraList = new ArrayList<Parameter>();

            paraList.add(new Parameter(Parameter.String, IntegrationConstants.IAS_NOTI_STATUS_NEW));
            paraList.add(new Parameter(Parameter.String, StatusConstants.DELETE_IND_DELETED));
            paraList.add(new Parameter(Parameter.Integer, iasMsgProcessBatchLimit));

            rs = conn.getResultSet(SELECT_MOTHER_MISSING_MAP_RECORD, paraList);

            while (rs.next()) {
                IasUserMsg iasUserMsg = new IasUserMsg();
                iasUserMsg.setNotiId(rs.getString("NOTI_ID"));
                iasUserMsg.setIasOptSpId(rs.getString("IAS_OPT_SP_ID"));

                resultList.add(iasUserMsg);
            }

            logger.debug("getMotherMissingVipMapList - END");

            return resultList;

        } catch (Exception ex) {
            logger.error("General exception caught in getMotherMissingVipMapList", ex);
            throw ex;
        } finally {
            try {
                if (rs != null)
                    HPFW_Connection.close(rs);
            } catch (Exception ex) {
                logger.error("General exception caught in getMotherMissingVipMapList - rs.close();", ex);
            }
        }
    }

    public List<String> getTxIdFromSentIasUserMsg(HPFW_Connection conn, int IasLiveToTimeDay, int dayLimit,
            int sizeLimit)
            throws Exception
    // CMC-2023-014: Support batch size limit of iAM Smart Notification delivery status enquiry job - END
    // MyGov6-C2-012: Query iAM Smart Message result limit time setting by day - END
    {
        logger.debug("getTxIdFromSentIasUserMsg - START");

        ResultSet rs = null;
        ArrayList<String> resultList = new ArrayList<String>();

        try {

            ArrayList<Parameter> paraList = new ArrayList<Parameter>();

            paraList.add(new Parameter(Parameter.String, IntegrationConstants.IAS_NOTI_STATUS_SENT));
            paraList.add(new Parameter(Parameter.Integer, IasLiveToTimeDay));
            paraList.add(new Parameter(Parameter.Integer, dayLimit));
            paraList.add(new Parameter(Parameter.Integer, sizeLimit));

            rs = conn.getResultSet(SELECT_TX_ID_FROM_SENT_IAS_USER_MSG, paraList);

            while (rs.next()) {
                resultList.add(rs.getString("TX_ID"));
            }

            logger.debug("getTxIdFromSentIasUserMsg - END");

            return resultList;

        } catch (Exception ex) {
            logger.error("General exception caught in getTxIdFromSentIasUserMsg", ex);
            throw ex;
        } finally {
            try {
                if (rs != null)
                    HPFW_Connection.close(rs);
            } catch (Exception ex) {
                logger.error("General exception caught in getTxIdFromSentIasUserMsg - rs.close();", ex);
            }
        }
    }

    public List<String> getTxIdFromSentIasUserToDoItem(HPFW_Connection conn, int IasLiveToTimeDay, int dayLimit,
            int sizeLimit) throws Exception {
        logger.debug("getTxIdFromSentIasUserToDoItem - START");

        ResultSet rs = null;
        ArrayList<String> resultList = new ArrayList<String>();

        try {
            ArrayList<Parameter> paraList = new ArrayList<Parameter>();

            paraList.add(new Parameter(Parameter.String, IntegrationConstants.IAS_NOTI_STATUS_SENT));
            paraList.add(new Parameter(Parameter.Integer, IasLiveToTimeDay));
            paraList.add(new Parameter(Parameter.Integer, dayLimit));
            paraList.add(new Parameter(Parameter.Integer, sizeLimit));

            rs = conn.getResultSet(SELECT_TX_ID_FROM_SENT_IAS_USER_TO_DO_ITEM, paraList);
            while (rs.next()) {
                resultList.add(rs.getString("TX_ID"));
            }

            logger.debug("getTxIdFromSentIasUserToDoItem - END");
            return resultList;
        } catch (Exception ex) {
            logger.error("General exception caught in getTxIdFromSentIasUserToDoItem", ex);
            throw ex;
        } finally {
            try {
                if (rs != null) {
                    HPFW_Connection.close(rs);
                }
            } catch (Exception ex) {
                logger.error("General exception caught in getTxIdFromSentIasUserToDoItem - rs.close();", ex);
            }
        }
    }

    public boolean isIasEsNotiMapExist(HPFW_Connection conn, String notiId, String spId) throws Exception {
        logger.debug("isIasEsNotiMapExist - START");

        boolean result = false;
        ResultSet rs = null;

        try {

            ArrayList<Parameter> paraList = new ArrayList<Parameter>();

            paraList.add(new Parameter(Parameter.String, notiId));
            paraList.add(new Parameter(Parameter.String, spId));

            rs = conn.getResultSet(SELECT_IAS_ES_NOTI_MAP_BY_NOTI_ID_SP_ID, paraList);

            if (rs.next()) {
                result = true;
            }

            logger.debug("isIasEsNotiMapExist - END");

            return result;

        } catch (Exception ex) {
            logger.error("General exception caught in isIasEsNotiMapExist", ex);
            throw ex;
        } finally {
            try {
                if (rs != null)
                    HPFW_Connection.close(rs);
            } catch (Exception ex) {
                logger.error("General exception caught in isIasEsNotiMapExist - rs.close();", ex);
            }
        }
    }

    public IasUser getIasEsNotiMap(HPFW_Connection conn, String notiId, String spId) throws Exception {
        logger.debug("getIasEsNotiMap - START");

        IasUser iasUser = null;
        ResultSet rs = null;

        try {
            ArrayList<Parameter> paraList = new ArrayList<Parameter>();

            paraList.add(new Parameter(Parameter.String, notiId));
            paraList.add(new Parameter(Parameter.String, spId));

            rs = conn.getResultSet(SELECT_IAS_ES_NOTI_MAP_BY_NOTI_ID_SP_ID, paraList);

            if (rs.next()) {
                iasUser = new IasUser();
                iasUser.setNotiId(rs.getString("NOTI_ID"));
                iasUser.setOpenId(rs.getString("OPEN_ID"));
                iasUser.setHkidHashed(rs.getString("HKID_HASHED"));
                iasUser.setHkidEncrypted(rs.getString("HKID_ENCRYPTED"));
                iasUser.setOptIn(rs.getString("OPT_IN"));
            }

            logger.debug("getIasEsNotiMap - END");

            return iasUser;

        } catch (Exception ex) {
            logger.error("General exception caught in getIasEsNotiMap", ex);
            throw ex;
        } finally {
            try {
                if (rs != null)
                    HPFW_Connection.close(rs);
            } catch (Exception ex) {
                logger.error("General exception caught in getIasEsNotiMap - rs.close();", ex);
            }
        }
    }

    public List<IasMsgStatusQueueJob> getDeRegUserJobFromIasMsgStatusQueue(HPFW_Connection conn) throws Exception {
        logger.debug("getDeRegUserJobFromIasMsgStatusQueue - START");

        IasUser iasUser = null;
        ResultSet rs = null;
        ArrayList<IasMsgStatusQueueJob> resultList = new ArrayList<IasMsgStatusQueueJob>();

        try {

            ArrayList<Parameter> paraList = new ArrayList<Parameter>();

            paraList.add(new Parameter(Parameter.String, IntegrationConstants.IAS_USER_STATUS_DEREGISTERED));
            paraList.add(new Parameter(Parameter.String, IntegrationConstants.JOB_STATUS_NEW));

            rs = conn.getResultSet(SELECT_IAS_DEREG_IAS_USER_JOB, paraList);

            while (rs.next()) {
                IasMsgStatusQueueJob iasQueueJob = new IasMsgStatusQueueJob();
                iasQueueJob.setJobId(rs.getString("JOB_ID"));
                iasQueueJob.setNotiId(rs.getString("NOTI_ID"));
                iasQueueJob.setCreateDt(rs.getTimestamp("CREATE_DT"));

                resultList.add(iasQueueJob);
            }

            logger.debug("getDeRegUserJobFromIasMsgStatusQueue - END");

            return resultList;

        } catch (Exception ex) {
            logger.error("General exception caught in getDeRegUserJobFromIasMsgStatusQueue", ex);
            throw ex;
        } finally {
            try {
                if (rs != null)
                    HPFW_Connection.close(rs);
            } catch (Exception ex) {
                logger.error("General exception caught in getDeRegUserJobFromIasMsgStatusQueue - rs.close();", ex);
            }
        }
    }

    public List<IasMsgStatusQueueJob> getIasMsgStatusQueueJobs(HPFW_Connection conn, String accountStatus,
            String msgType, String jobStatus) throws Exception {
        logger.debug("getIasMsgStatusQueueJobs - START");

        ResultSet rs = null;
        ArrayList<IasMsgStatusQueueJob> resultList = new ArrayList<IasMsgStatusQueueJob>();

        try {
            ArrayList<Parameter> paraList = new ArrayList<Parameter>();
            paraList.add(new Parameter(Parameter.String, accountStatus));
            paraList.add(new Parameter(Parameter.String, msgType));
            paraList.add(new Parameter(Parameter.String, jobStatus));

            rs = conn.getResultSet(SELECT_IAS_MSG_STATUS_QUEUE, paraList);

            while (rs.next()) {
                IasMsgStatusQueueJob iasQueueJob = new IasMsgStatusQueueJob();
                iasQueueJob.setJobId(rs.getString("JOB_ID"));
                iasQueueJob.setNotiId(rs.getString("NOTI_ID"));
                iasQueueJob.setSpId(rs.getString("SP_ID"));
                iasQueueJob.setCreateDt(rs.getTimestamp("CREATE_DT"));

                resultList.add(iasQueueJob);
            }

            logger.debug("getIasMsgStatusQueueJobs - END");

            return resultList;
        } catch (Exception ex) {
            logger.error("General exception caught in getIasMsgStatusQueueJobs", ex);
            throw ex;
        } finally {
            try {
                if (rs != null)
                    HPFW_Connection.close(rs);
            } catch (Exception ex) {
                logger.error("General exception caught in getIasMsgStatusQueueJobs - rs.close();", ex);
            }
        }
    }

    public int getNumberOfNotDeletedIasUserMsgByNotiIdAndCreateDt(HPFW_Connection conn, String notiId,
            Timestamp createDt) throws Exception {
        logger.debug("getNumberOfNotDeletedIasUserMsgByNotiIdAndCreateDt - START");

        ResultSet rs = null;
        int cnt = 0;
        try {
            ArrayList<Parameter> paraList = new ArrayList<Parameter>();
            paraList.add(new Parameter(Parameter.String, notiId));
            paraList.add(new Parameter(Parameter.Timestamp, createDt));
            rs = conn.getResultSet(SELECT_NOT_DELETED_IAS_USER_MSG_BY_NOTI_ID_AND_CREATE_DT, paraList);

            while (rs.next()) {
                cnt = Integer.parseInt(rs.getString("CNT"));
            }
            logger.debug("getNumberOfNotDeletedIasUserMsgByNotiIdAndCreateDt - END");

            return cnt;
        } catch (Exception ex) {
            logger.error("General exception caught in getNumberOfNotDeletedIasUserMsgByNotiIdAndCreateDt", ex);
            throw ex;
        } finally {
            try {
                if (rs != null)
                    HPFW_Connection.close(rs);
            } catch (Exception ex) {
                logger.error(
                        "General exception caught in getNumberOfNotDeletedIasUserMsgByNotiIdAndCreateDt - rs.close();",
                        ex);
            }
        }
    }

    public void updateIasMsgJobStatus(HPFW_Connection hpfwConn, String status, String jobId) throws Exception {
        String markDeleteSqlStr = "Update IAS_MSG_STATUS_QUEUE set job_status=? where job_id=? ";

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            Connection conn = hpfwConn.getConnectionPtr();
            ps = conn.prepareStatement(markDeleteSqlStr);
            ps.setString(1, status);
            ps.setString(2, jobId);
            ps.executeUpdate();
        } catch (Exception e) {
            throw e;
        } finally {
            close(ps, rs);
        }
    }

    public List<IasAssoQueueJob> getIasAssoJobFromIasAssoQueue(HPFW_Connection conn, int iasAssoJobProcessBatchLimit)
            throws Exception {
        logger.debug("getIasAssoJobFromIasAssoQueue - START");

        IasUser iasUser = null;
        ResultSet rs = null;
        ArrayList<IasAssoQueueJob> resultList = new ArrayList<>();

        try {

            ArrayList<Parameter> paraList = new ArrayList<>();

            paraList.add(new Parameter(Parameter.String, IntegrationConstants.ASSO_STATUS_ASSOCIATE));
            paraList.add(new Parameter(Parameter.String, IntegrationConstants.JOB_STATUS_NEW));
            paraList.add(new Parameter(Parameter.Integer, iasAssoJobProcessBatchLimit));

            rs = conn.getResultSet(SELECT_IAS_ASSO_JOB, paraList);

            while (rs.next()) {
                IasAssoQueueJob iasQueueJob = new IasAssoQueueJob();
                iasQueueJob.setJobId(rs.getString("JOB_ID"));
                iasQueueJob.setClientId(rs.getString("CLIENT_ID"));
                iasQueueJob.setOpenId(rs.getString("OPEN_ID"));
                iasQueueJob.setServiceProviderId(rs.getString("SERVICE_PROVIDER_ID"));
                iasQueueJob.setCreateDt(rs.getTimestamp("CREATE_DT"));
                iasQueueJob.setIasOptSpId(rs.getString("IAS_OPT_SP_ID"));
                iasQueueJob.setIasAutoCreateInd(rs.getString("IAS_AUTO_CREATE_IND"));
                iasQueueJob.setServiceProviderNameEn(rs.getString("SERVICE_PROVIDER_NAME_EN"));
                iasQueueJob.setServiceProviderNameTc(rs.getString("SERVICE_PROVIDER_NAME_TC"));
                iasQueueJob.setServiceProviderNameSc(rs.getString("SERVICE_PROVIDER_NAME_SC"));
                iasQueueJob.setMapNotiId(rs.getString("MAP_NOTI_ID"));
                iasQueueJob.setOptIn(rs.getString("OPT_IN"));
                iasQueueJob.setAccStatus(rs.getString("ACC_STATUS"));

                resultList.add(iasQueueJob);
            }

            logger.debug("getIasAssoJobFromIasAssoQueue - END");

            return resultList;

        } catch (Exception ex) {
            logger.error("General exception caught in getIasAssoJobFromIasAssoQueue", ex);
            throw ex;
        } finally {
            try {
                if (rs != null)
                    HPFW_Connection.close(rs);
            } catch (Exception ex) {
                logger.error("General exception caught in getIasAssoJobFromIasAssoQueue - rs.close();", ex);
            }
        }
    }

    public void updateIasAssoJobStatus(HPFW_Connection hpfwConn, String status, String jobId) throws Exception {
        String markDeleteSqlStr = "Update IAS_ASSO_QUEUE set job_status=? where job_id=? ";

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            Connection conn = hpfwConn.getConnectionPtr();
            ps = conn.prepareStatement(markDeleteSqlStr);
            ps.setString(1, status);
            ps.setString(2, jobId);
            ps.executeUpdate();
        } catch (Exception e) {
            throw e;
        } finally {
            close(ps, rs);
        }
    }

    public Map<String, String> getTemplateByIdVersion(HPFW_Connection conn, String templateId, String templateVersion,
            String spId)
            throws Exception {
        logger.debug("getTemplateByIdVersion - START");

        ResultSet rs = null;

        try {

            ArrayList<Parameter> paraList = new ArrayList<Parameter>();

            paraList.add(new Parameter(Parameter.String, templateId));
            paraList.add(new Parameter(Parameter.String, templateVersion));
            paraList.add(new Parameter(Parameter.String, spId));

            rs = conn.getResultSet(SELECT_TEMPLATE_CONTENT, paraList);

            HashMap<String, String> map = null;
            if (rs.next()) {
                map = new HashMap<String, String>();
                map.put("IAS_SUBJECT_EN", rs.getString("IAS_SUBJECT_EN"));
                map.put("IAS_SUBJECT_TC", rs.getString("IAS_SUBJECT_TC"));
                map.put("IAS_SUBJECT_SC", rs.getString("IAS_SUBJECT_SC"));
                map.put("IAS_CONTENT_EN", rs.getString("IAS_CONTENT_EN"));
                map.put("IAS_CONTENT_TC", rs.getString("IAS_CONTENT_TC"));
                map.put("IAS_CONTENT_SC", rs.getString("IAS_CONTENT_SC"));
            }

            logger.debug("getTemplateByIdVersion - END");

            return map;

        } catch (Exception ex) {
            logger.error("General exception caught in getTemplateByIdVersion", ex);
            throw ex;
        } finally {
            try {
                if (rs != null)
                    HPFW_Connection.close(rs);
            } catch (Exception ex) {
                logger.error("General exception caught in getTemplateByIdVersion - rs.close();", ex);
            }
        }
    }

    public void createIasUndeliveredMessage(HPFW_Connection conn, String clientId, String openId, String iasMsgId,
            String reason, String rcptType) throws Exception {

        String InsertSQL = "INSERT INTO ias_undelivered_message " +
                "(CLIENT_ID, OPEN_ID, IAS_MSG_ID, RCPT_TYPE, STATUS, REASON, CREATE_DT, LAST_MODIFY_DT, CREATE_BY, LAST_MODIFY_BY) "
                + "VALUES (?, ?, ?, ?, ?, ?, now(3), now(3), 'CMC', 'CMC')";

        ArrayList<Parameter> paraI = new ArrayList<Parameter>();
        paraI.add(new Parameter(Parameter.String, clientId));
        paraI.add(new Parameter(Parameter.String, openId));
        paraI.add(new Parameter(Parameter.String, iasMsgId));
        paraI.add(new Parameter(Parameter.String, rcptType));
        paraI.add(new Parameter(Parameter.String, "N"));
        paraI.add(new Parameter(Parameter.String, reason));

        conn.executeStatement(InsertSQL, paraI);
    }

    public void createIasUndeliveredToDoItem(HPFW_Connection conn, String clientId, String openId, String iasToDoItemId,
            String reason, String rcptType) throws Exception {

        String InsertSQL = "INSERT INTO ias_undelivered_to_do_item " +
                "(CLIENT_ID, OPEN_ID, IAS_TO_DO_ITEM_ID, RCPT_TYPE, STATUS, REASON, CREATE_DT, LAST_MODIFY_DT, CREATE_BY, LAST_MODIFY_BY) "
                + "VALUES (?, ?, ?, ?, ?, ?, now(3), now(3), 'CMC', 'CMC')";

        ArrayList<Parameter> paraI = new ArrayList<Parameter>();
        paraI.add(new Parameter(Parameter.String, clientId));
        paraI.add(new Parameter(Parameter.String, openId));
        paraI.add(new Parameter(Parameter.String, iasToDoItemId));
        paraI.add(new Parameter(Parameter.String, rcptType));
        paraI.add(new Parameter(Parameter.String, "N"));
        paraI.add(new Parameter(Parameter.String, reason));

        conn.executeStatement(InsertSQL, paraI);
    }

    public boolean isIasUserMessageExistByNotiIdMsgIdTxId(HPFW_Connection conn, String notiId, String iasMsgId,
            String txId) throws Exception {
        logger.debug("isIasUserMessageExistByNotiIdMsgIdTxId - START");

        boolean result = false;
        ResultSet rs = null;

        try {
            ArrayList<Parameter> paraList = new ArrayList<Parameter>();

            paraList.add(new Parameter(Parameter.String, notiId));
            paraList.add(new Parameter(Parameter.String, iasMsgId));
            paraList.add(new Parameter(Parameter.String, txId));

            rs = conn.getResultSet(SELECT_IAS_USER_MESSAGE_BY_NOTI_ID_MSG_ID_TX_ID, paraList);

            while (rs.next()) {
                if (Integer.parseInt(rs.getString("CNT")) > 0)
                    result = true;
            }

            logger.debug("isIasUserMessageExistByNotiIdMsgIdTxId - END");

            return result;
        } catch (Exception ex) {
            logger.error("General exception caught in isIasUserMessageExistByNotiIdMsgIdTxId", ex);
            throw ex;
        } finally {
            try {
                if (rs != null)
                    HPFW_Connection.close(rs);
            } catch (Exception ex) {
                logger.error("General exception caught in isIasUserMessageExistByNotiIdMsgIdTxId - rs.close();", ex);
            }
        }
    }

    public boolean isIasUserToDoItemExistByNotiIdToDoItemIdTxId(HPFW_Connection conn, String notiId,
            String iasToDoItemId, String txId) throws Exception {
        logger.debug("isIasUserToDoItemExistByNotiIdToDoItemIdTxId - START");

        boolean result = false;
        ResultSet rs = null;

        try {
            ArrayList<Parameter> paraList = new ArrayList<Parameter>();

            paraList.add(new Parameter(Parameter.String, notiId));
            paraList.add(new Parameter(Parameter.String, iasToDoItemId));
            paraList.add(new Parameter(Parameter.String, txId));

            rs = conn.getResultSet(SELECT_IAS_USER_TO_DO_ITEM_BY_NOTI_ID_TO_DO_ITEM_ID_TX_ID, paraList);

            while (rs.next()) {
                if (Integer.parseInt(rs.getString("CNT")) > 0) {
                    result = true;
                }
            }

            logger.debug("isIasUserToDoItemExistByNotiIdToDoItemIdTxId - END");

            return result;
        } catch (Exception ex) {
            logger.error("General exception caught in isIasUserToDoItemExistByNotiIdToDoItemIdTxId", ex);
            throw ex;
        } finally {
            try {
                if (rs != null) {
                    HPFW_Connection.close(rs);
                }
            } catch (Exception ex) {
                logger.error("General exception caught in isIasUserToDoItemExistByNotiIdToDoItemIdTxId - rs.close();",
                        ex);
            }
        }
    }

    private void close(Statement stmt, ResultSet rs) {
        if (rs != null)
            try {
                rs.close();
            } catch (Exception ignored) {
            }
        if (stmt != null)
            try {
                stmt.close();
            } catch (Exception ignored) {
            }
    }

    private final String SELECT_NOTI_ID_MISSING_IAS_USER_MESSAGE = "select ium.CLIENT_ID, ium.OPEN_ID, im.IAS_MSG_ID, t.SERVICE_PROVIDER_ID, IFNULL(s.IAS_AUTO_CREATE_IND, 'N') AS IAS_AUTO_CREATE_IND, IFNULL(t.IAS_SHOW_ES_SET_BTN, 'N') AS IAS_SHOW_ES_SET_BTN, IFNULL(s.IAS_OPT_SP_ID, s.SERVICE_PROVIDER_ID) AS IAS_OPT_SP_ID "
            + "from IAS_MESSAGE im, IAS_USER_MESSAGE ium, CMC_TEMPLATE t, CMC_SERVICE_PROVIDER s "
            + "where im.IAS_MSG_ID = ium.IAS_MSG_ID "
            + "AND im.TEMPLATE_ID = t.TEMPLATE_ID "
            + "AND im.TEMPLATE_VERSION = t.TEMPLATE_VERSION "
            + "and t.SERVICE_PROVIDER_ID = s.SERVICE_PROVIDER_ID "
            + "AND (ium.NOTI_ID is null OR LENGTH(ium.NOTI_ID) = 0) "
            + "AND ium.IAS_NOTI_STATUS = ? LIMIT ? ";

    private final String SELECT_OUTSTANDING_MESSAGE = "select A.*, ienm.NOTI_ID as MAP_NOTI_ID from ( "
            + "select ium.CLIENT_ID, ium.OPEN_ID, im.IAS_MSG_ID, ium.NOTI_ID, t.SERVICE_PROVIDER_ID, s.IAS_OPT_SP_ID, "
            + "im.SUBJECT_EN, im.SUBJECT_TC, im.SUBJECT_SC, iuni.STATUS "
            + "from IAS_MESSAGE im, CMC_TEMPLATE t, CMC_SERVICE_PROVIDER s, IAS_USER_MESSAGE ium LEFT JOIN IAS_USER_NOTI_INFO iuni ON ium.NOTI_ID = iuni.NOTI_ID "
            + "where im.IAS_MSG_ID = ium.IAS_MSG_ID "
            + "AND im.TEMPLATE_ID = t.TEMPLATE_ID "
            + "AND im.TEMPLATE_VERSION = t.TEMPLATE_VERSION "
            + "and t.SERVICE_PROVIDER_ID = s.SERVICE_PROVIDER_ID "
            + "AND ium.IAS_NOTI_STATUS = ? "
            + "AND ium.NOTI_ID is not null "
            + "AND ium.DELETE_IND <> ? ORDER BY im.IAS_MSG_ID) A "
            + "LEFT JOIN IAS_ES_NOTI_MAP ienm ON A.NOTI_ID = ienm.NOTI_ID AND A.SERVICE_PROVIDER_ID = ienm.SERVICE_PROVIDER_ID LIMIT ? ";

    private final String SELECT_MOTHER_MISSING_MAP_RECORD = "select DISTINCT ium.NOTI_ID, s.IAS_OPT_SP_ID "
            + "from IAS_MESSAGE im, CMC_TEMPLATE t, CMC_SERVICE_PROVIDER s, IAS_USER_MESSAGE ium  "
            + "where im.IAS_MSG_ID = ium.IAS_MSG_ID "
            + "AND im.TEMPLATE_ID = t.TEMPLATE_ID "
            + "AND im.TEMPLATE_VERSION = t.TEMPLATE_VERSION "
            + "and t.SERVICE_PROVIDER_ID = s.SERVICE_PROVIDER_ID "
            + "and s.SERVICE_PROVIDER_ID <> s.IAS_OPT_SP_ID "
            + "AND ium.IAS_NOTI_STATUS = ? "
            + "AND ium.NOTI_ID is not null "
            + "AND ium.DELETE_IND <> ? "
            + "AND NOT EXISTS (SELECT NOTI_ID FROM IAS_ES_NOTI_MAP ienm where ium.NOTI_ID = ienm.NOTI_ID AND s.IAS_OPT_SP_ID = ienm.SERVICE_PROVIDER_ID)  LIMIT ? ";

    private final String SELECT_TX_ID_FROM_SENT_IAS_USER_MSG = "select distinct TX_ID "
            + "from IAS_USER_MESSAGE "
            + "where IAS_NOTI_STATUS = ? and IAS_DELIVERY_STATUS is null "
            + "and SENT_DT <= now() - interval ? DAY "
            + "and SENT_DT > now() - interval ? DAY order by SENT_DT limit ?";

    private final String SELECT_TX_ID_FROM_SENT_IAS_USER_TO_DO_ITEM = "select distinct TX_ID "
            + "from IAS_USER_TO_DO_ITEM "
            + "where IAS_NOTI_STATUS = ? and IAS_DELIVERY_STATUS is null "
            + "and IAS_NOTI_RESULT IN ('" + IntegrationConstants.SEND_NOTI_ID_RESULT_READY_TO_SEND + "', '"
            + IntegrationConstants.SEND_NOTI_ID_RESULT_SENT + "') "
            + "and SENT_DT <= now() - interval ? DAY "
            + "and SENT_DT > now() - interval ? DAY order by SENT_DT limit ?";

    private final String SELECT_IAS_ES_NOTI_MAP_BY_NOTI_ID_SP_ID = "select NOTI_ID, OPEN_ID, HKID_HASHED, HKID_ENCRYPTED, OPT_IN "
            + "from IAS_ES_NOTI_MAP  "
            + "where NOTI_ID = ? "
            + "AND SERVICE_PROVIDER_ID = ? ";

    private final String SELECT_IAS_DEREG_IAS_USER_JOB = "select JOB_ID, NOTI_ID, CREATE_DT "
            + "from IAS_MSG_STATUS_QUEUE "
            + "where ACCOUNT_STATUS = ? "
            + "AND JOB_STATUS = ? ";

    private final String SELECT_IAS_MSG_STATUS_QUEUE = "select JOB_ID, NOTI_ID, SP_ID, CREATE_DT "
            + "from IAS_MSG_STATUS_QUEUE "
            + "where ACCOUNT_STATUS = ? "
            + "AND MSG_TYPE = ? "
            + "AND JOB_STATUS = ? ";

    private final String SELECT_NOT_DELETED_IAS_USER_MSG_BY_NOTI_ID_AND_CREATE_DT = "select count(*) CNT from ias_user_message where NOTI_ID = ? and CREATE_DT < ? and DELETE_IND != 'Y'";

    private final String SELECT_IAS_ASSO_JOB = "select A.*, ienm.NOTI_ID as MAP_NOTI_ID, ienm.OPT_IN, iuni.STATUS as ACC_STATUS from "
            + "(select q.JOB_ID, q.CLIENT_ID, q.OPEN_ID, q.SERVICE_PROVIDER_ID, q.CREATE_DT, IFNULL(s.IAS_AUTO_CREATE_IND, 'N') AS IAS_AUTO_CREATE_IND, IFNULL(s.IAS_OPT_SP_ID, s.SERVICE_PROVIDER_ID) AS IAS_OPT_SP_ID, "
            + "os.SERVICE_PROVIDER_NAME_EN, os.SERVICE_PROVIDER_NAME_TC, os.SERVICE_PROVIDER_NAME_SC "
            + "from IAS_ASSO_QUEUE q, CMC_SERVICE_PROVIDER s, CMC_SERVICE_PROVIDER os "
            + "where q.ASSO_STATUS = ? "
            + "AND q.SERVICE_PROVIDER_ID = s.SERVICE_PROVIDER_ID "
            + "AND os.SERVICE_PROVIDER_ID = s.SERVICE_PROVIDER_ID "
            + "AND q.JOB_STATUS = ? ) A "
            + "LEFT JOIN IAS_ES_NOTI_MAP ienm ON A.OPEN_ID = ienm.OPEN_ID AND A.IAS_OPT_SP_ID = ienm.SERVICE_PROVIDER_ID "
            + "LEFT JOIN IAS_USER_NOTI_INFO iuni ON ienm.NOTI_ID = iuni.NOTI_ID LIMIT ?";

    private final String SELECT_TEMPLATE_CONTENT = "select t.IAS_SUBJECT_EN, t.IAS_SUBJECT_TC, t.IAS_SUBJECT_SC, t.IAS_CONTENT_EN, t.IAS_CONTENT_TC, t.IAS_CONTENT_SC "
            + "from CMC_TEMPLATE t "
            + "where t.TEMPLATE_ID = ? "
            + "and t.TEMPLATE_VERSION = ? "
            + "and t.SERVICE_PROVIDER_ID = ? "
            + "and t.STATUS = 'A' ";

    private final String SELECT_IAS_USER_MESSAGE_BY_NOTI_ID_MSG_ID_TX_ID = "SELECT COUNT(*) CNT FROM IAS_USER_MESSAGE WHERE NOTI_ID = ? and IAS_MSG_ID = ? and TX_ID = ?";

    private final String SELECT_IAS_USER_TO_DO_ITEM_BY_NOTI_ID_TO_DO_ITEM_ID_TX_ID = "SELECT COUNT(*) CNT FROM IAS_USER_TO_DO_ITEM WHERE NOTI_ID = ? and IAS_TO_DO_ITEM_ID = ? and TX_ID = ?";
}
