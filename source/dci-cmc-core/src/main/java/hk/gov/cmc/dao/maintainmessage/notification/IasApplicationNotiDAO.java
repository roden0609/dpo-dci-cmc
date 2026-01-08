package hk.gov.cmc.dao.maintainmessage.notification;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import hk.gov.cmc.common.IntegrationConstants;
import hk.gov.cmc.model.maintainmessage.application.IasUserApplication;
import hk.gov.cmc.persistence.connection.hpfw.HPFW_Connection;
import hk.gov.cmc.persistence.connection.hpfw.Parameter;

public class IasApplicationNotiDAO {

    private static Log logger = LogFactory.getLog(IasApplicationNotiDAO.class);

    public IasApplicationNotiDAO() {
        super();
    }

    public List<IasUserApplication> getNotiIdMissingIasUserApplication(HPFW_Connection conn,
            String recipientIdType, int iasMsgProcessBatchLimit) throws Exception {
        logger.debug("getNotiIdMissingIasUserApplication - START");

        ResultSet rs = null;
        ArrayList<IasUserApplication> resultList = new ArrayList<IasUserApplication>();

        String SELECT_NOTI_ID_MISSING_IAS_USER_APPLICATION = "select "
                + "iua.CLIENT_ID, iua.RECIPIENT_ID, iua.RECIPIENT_ID_TYPE, iua.HKID_ENCRYPTED, "
                + "iua.TRAN_ID, ia.IAS_APPLICATION_ID, "
                + "t.SERVICE_PROVIDER_ID, IFNULL(t.IAS_SHOW_ES_SET_BTN, 'N') "
                + "AS IAS_SHOW_ES_SET_BTN, IFNULL(s.IAS_OPT_SP_ID, s.SERVICE_PROVIDER_ID) AS IAS_OPT_SP_ID "
                + "from IAS_APPLICATION ia, IAS_USER_APPLICATION iua, CMC_TEMPLATE t, CMC_SERVICE_PROVIDER s "
                + "where ia.IAS_APPLICATION_ID = iua.IAS_APPLICATION_ID "
                + "AND ia.TEMPLATE_ID = t.TEMPLATE_ID "
                + "AND ia.TEMPLATE_VERSION = t.TEMPLATE_VERSION "
                + "and t.SERVICE_PROVIDER_ID = s.SERVICE_PROVIDER_ID "
                + "AND (iua.NOTI_ID is null OR LENGTH(iua.NOTI_ID) = 0) "
                + "AND iua.RECIPIENT_ID_TYPE = ? "
                + "AND iua.IAS_NOTI_STATUS = ? LIMIT ? ";

        try {

            ArrayList<Parameter> paraList = new ArrayList<Parameter>();

            paraList.add(new Parameter(Parameter.String, recipientIdType));
            paraList.add(new Parameter(Parameter.String, IntegrationConstants.IAS_NOTI_STATUS_NEW));
            paraList.add(new Parameter(Parameter.Integer, iasMsgProcessBatchLimit));

            rs = conn.getResultSet(SELECT_NOTI_ID_MISSING_IAS_USER_APPLICATION, paraList);

            while (rs.next()) {
                IasUserApplication iasUserApplication = new IasUserApplication();
                iasUserApplication.setClientId(rs.getString("CLIENT_ID"));
                iasUserApplication.setRecipientId(rs.getString("RECIPIENT_ID"));
                iasUserApplication.setRecipientIdType(rs.getString("RECIPIENT_ID_TYPE"));
                iasUserApplication.setHkidEncrypted(rs.getString("HKID_ENCRYPTED"));
                iasUserApplication.setTranId(rs.getString("TRAN_ID"));
                iasUserApplication.setIasApplicationId(rs.getString("IAS_APPLICATION_ID"));
                iasUserApplication.setServiceProviderId(rs.getString("SERVICE_PROVIDER_ID"));

                // Input field for "ias_opt_check_ind" is not used because "ias_show_es_set_btn" derive the same meaning. So, check "ias_show_es_set_btn" while processing message requests.
                iasUserApplication.setIasOptCheckInd(rs.getString("IAS_SHOW_ES_SET_BTN"));
                iasUserApplication.setIasOptSpId(rs.getString("IAS_OPT_SP_ID"));

                resultList.add(iasUserApplication);
            }

            logger.debug("getNotiIdMissingIasUserApplication - END");

            return resultList;

        } catch (Exception ex) {
            logger.error("General exception caught in getNotiIdMissingIasUserApplication", ex);
            throw ex;
        } finally {
            try {
                if (rs != null) {
                    HPFW_Connection.close(rs);
                }
            } catch (Exception ex) {
                logger.error("General exception caught in getNotiIdMissingIasUserApplication - rs.close();", ex);
            }
        }
    }

    public void createIasUndeliveredApplication(HPFW_Connection conn, String clientId, String recipientId,
            String iasApplicationId,
            String recipientIdType, String rcptType, String status, String reason) throws Exception {
        logger.debug("createIasUndeliveredApplication - START");

        String insertSQL = "INSERT IGNORE INTO ias_undelivered_application "
                + "(CLIENT_ID, RECIPIENT_ID, IAS_APPLICATION_ID, RECIPIENT_ID_TYPE, RCPT_TYPE, STATUS, REASON, CREATE_DT, LAST_MODIFY_DT, CREATE_BY, LAST_MODIFY_BY) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, now(3), now(3), 'CMC', 'CMC')";

        ArrayList<Parameter> paraList = new ArrayList<Parameter>();
        paraList.add(new Parameter(Parameter.String, clientId));
        paraList.add(new Parameter(Parameter.String, recipientId));
        paraList.add(new Parameter(Parameter.String, iasApplicationId));
        paraList.add(new Parameter(Parameter.String, recipientIdType));
        paraList.add(new Parameter(Parameter.String, rcptType));
        paraList.add(new Parameter(Parameter.String, status));
        paraList.add(new Parameter(Parameter.String, reason));

        conn.executeStatement(insertSQL, paraList);
        logger.debug("createIasUndeliveredApplication - END");
    }

    public void createIasUndeliveredApplicationWithHKIdEncrypted(HPFW_Connection conn, String clientId,
            String recipientId, String iasApplicationId,
            String recipientIdType, String hkidEncrypted, String rcptType, String status, String reason)
            throws Exception {
        logger.debug("createIasUndeliveredApplicationWithHKIdEncrypted - START");

        String insertSQL = "INSERT IGNORE INTO ias_undelivered_application "
                + "(CLIENT_ID, RECIPIENT_ID, IAS_APPLICATION_ID, RECIPIENT_ID_TYPE, HKID_ENCRYPTED, RCPT_TYPE, STATUS, REASON, CREATE_DT, LAST_MODIFY_DT, CREATE_BY, LAST_MODIFY_BY) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, now(3), now(3), 'CMC', 'CMC')";

        ArrayList<Parameter> paraList = new ArrayList<Parameter>();
        paraList.add(new Parameter(Parameter.String, clientId));
        paraList.add(new Parameter(Parameter.String, recipientId));
        paraList.add(new Parameter(Parameter.String, iasApplicationId));
        paraList.add(new Parameter(Parameter.String, recipientIdType));
        paraList.add(new Parameter(Parameter.String, hkidEncrypted));
        paraList.add(new Parameter(Parameter.String, rcptType));
        paraList.add(new Parameter(Parameter.String, status));
        paraList.add(new Parameter(Parameter.String, reason));

        conn.executeStatement(insertSQL, paraList);
        logger.debug("createIasUndeliveredApplicationWithHKIdEncrypted - END");
    }

    public List<IasUserApplication> getOutstandingIasApplication(HPFW_Connection conn, String iasNotiStatus,
            String deleteInd, int iasMsgProcessBatchLimit)
            throws Exception {
        logger.debug("getOutstandingIasApplication - START");

        ResultSet rs = null;
        ArrayList<IasUserApplication> resultList = new ArrayList<IasUserApplication>();

        String SELECT_OUTSTANDING_IAS_APPLICATION = "select B.*, C.ias_application_id as PREV_IAS_APPLICATINO_ID from ("
                +
                "	select A.*, ienm.NOTI_ID as MAP_NOTI_ID from ( " +
                "		select iua.CLIENT_ID, iua.RECIPIENT_ID, iua.OPERATION_TYPE, iua.RECIPIENT_ID_TYPE, iua.APP_STATUS, iua.APP_STATUS_UPDATE_DATE, iua.CREATE_DT, iua.APP_REF_NUM, "
                +
                "		ia.IAS_APPLICATION_ID, iua.NOTI_ID, t.SERVICE_PROVIDER_ID, s.IAS_OPT_SP_ID, " +
                "		ia.TITLE_EN, ia.TITLE_TC, ia.TITLE_SC, iuni.STATUS " +
                "		from IAS_APPLICATION ia, CMC_TEMPLATE t, CMC_SERVICE_PROVIDER s, IAS_USER_APPLICATION iua LEFT JOIN IAS_USER_NOTI_INFO iuni ON iua.NOTI_ID = iuni.NOTI_ID "
                +
                "		where ia.IAS_APPLICATION_ID = iua.IAS_APPLICATION_ID " +
                "		AND ia.TEMPLATE_ID = t.TEMPLATE_ID " +
                "		AND ia.TEMPLATE_VERSION = t.TEMPLATE_VERSION " +
                "		and t.SERVICE_PROVIDER_ID = s.SERVICE_PROVIDER_ID " +
                "		AND iua.IAS_NOTI_STATUS = ?" +
                "		AND iua.NOTI_ID is not null " +
                "		AND iua.DELETE_IND = ? ORDER BY ia.IAS_APPLICATION_ID" +
                "	) A LEFT JOIN IAS_ES_NOTI_MAP ienm ON A.NOTI_ID = ienm.NOTI_ID AND A.SERVICE_PROVIDER_ID = ienm.SERVICE_PROVIDER_ID"
                +
                ") B LEFT JOIN (select ias_application_id, app_ref_num, max(create_dt) from IAS_USER_APPLICATION where DELETE_IND = 'P' group by app_ref_num) C "
                +
                "on B.APP_REF_NUM = C.APP_REF_NUM LIMIT ?";

        try {
            ArrayList<Parameter> paraList = new ArrayList<Parameter>();
            paraList.add(new Parameter(Parameter.String, iasNotiStatus));
            paraList.add(new Parameter(Parameter.String, deleteInd));
            paraList.add(new Parameter(Parameter.Integer, iasMsgProcessBatchLimit));

            rs = conn.getResultSet(SELECT_OUTSTANDING_IAS_APPLICATION, paraList);

            while (rs.next()) {
                IasUserApplication iasUserApplication = new IasUserApplication();
                iasUserApplication.setClientId(rs.getString("CLIENT_ID"));
                iasUserApplication.setRecipientId(rs.getString("RECIPIENT_ID"));
                iasUserApplication.setOperationType(rs.getString("OPERATION_TYPE"));
                iasUserApplication.setRecipientIdType(rs.getString("RECIPIENT_ID_TYPE"));
                iasUserApplication.setAppStatus(rs.getString("APP_STATUS"));
                iasUserApplication.setAppStatusUpdateDate(rs.getTimestamp("APP_STATUS_UPDATE_DATE"));
                iasUserApplication.setIasApplicationId(rs.getString("IAS_APPLICATION_ID"));
                iasUserApplication.setNotiId(rs.getString("NOTI_ID"));
                iasUserApplication.setServiceProviderId(rs.getString("SERVICE_PROVIDER_ID"));
                iasUserApplication.setTitleEn(rs.getString("TITLE_EN"));
                iasUserApplication.setTitleTc(rs.getString("TITLE_TC"));
                iasUserApplication.setTitleSc(rs.getString("TITLE_SC"));
                iasUserApplication.setStatus(rs.getString("STATUS"));
                iasUserApplication.setMapNotiId(rs.getString("MAP_NOTI_ID"));
                iasUserApplication.setIasOptSpId(rs.getString("IAS_OPT_SP_ID"));
                iasUserApplication.setCreateDt(rs.getTimestamp("CREATE_DT"));
                iasUserApplication.setPrevIasApplicationId(rs.getString("PREV_IAS_APPLICATINO_ID"));
                resultList.add(iasUserApplication);
            }

            logger.debug("getOutstandingIasApplication - END");

            return resultList;
        } catch (Exception ex) {
            logger.error("General exception caught in getOutstandingIasApplication", ex);
            throw ex;
        } finally {
            try {
                if (rs != null) {
                    HPFW_Connection.close(rs);
                }
            } catch (Exception ex) {
                logger.error("General exception caught in getOutstandingIasApplication - rs.close();", ex);
            }
        }
    }

    public List<IasUserApplication> getPendingDeleteIasApplication(HPFW_Connection conn, String iasNotiStatus,
            String deleteInd, int iasMsgProcessBatchLimit)
            throws Exception {
        logger.debug("getPendingDeleteIasApplication - START");

        ResultSet rs = null;
        ArrayList<IasUserApplication> resultList = new ArrayList<IasUserApplication>();

        String SELECT_PENDING_DELETE_IAS_APPLICATION = "select A.*, ienm.NOTI_ID as MAP_NOTI_ID from ( " +
                "select iua.CLIENT_ID, iua.RECIPIENT_ID, iua.OPERATION_TYPE, iua.RECIPIENT_ID_TYPE, iua.APP_STATUS, iua.APP_STATUS_UPDATE_DATE, iua.CREATE_DT, "
                +
                "ia.IAS_APPLICATION_ID, iua.NOTI_ID, t.SERVICE_PROVIDER_ID, s.IAS_OPT_SP_ID, " +
                "ia.TITLE_EN, ia.TITLE_TC, ia.TITLE_SC, iuni.STATUS " +
                "from IAS_APPLICATION ia, CMC_TEMPLATE t, CMC_SERVICE_PROVIDER s, IAS_USER_APPLICATION iua LEFT JOIN IAS_USER_NOTI_INFO iuni ON iua.NOTI_ID = iuni.NOTI_ID "
                +
                "where ia.IAS_APPLICATION_ID = iua.IAS_APPLICATION_ID " +
                "AND ia.TEMPLATE_ID = t.TEMPLATE_ID " +
                "AND ia.TEMPLATE_VERSION = t.TEMPLATE_VERSION " +
                "and t.SERVICE_PROVIDER_ID = s.SERVICE_PROVIDER_ID " +
                "AND iua.IAS_NOTI_STATUS = ? " +
                "AND iua.IAS_NOTI_RESULT IN ('" + IntegrationConstants.SEND_NOTI_ID_RESULT_READY_TO_SEND + "', '"
                + IntegrationConstants.SEND_NOTI_ID_RESULT_SENT + "') " +
                "AND iua.NOTI_ID is not null " +
                "AND iua.DELETE_IND = ? ORDER BY ia.IAS_APPLICATION_ID) A " +
                "LEFT JOIN IAS_ES_NOTI_MAP ienm ON A.NOTI_ID = ienm.NOTI_ID AND A.SERVICE_PROVIDER_ID = ienm.SERVICE_PROVIDER_ID LIMIT ? ";

        try {
            ArrayList<Parameter> paraList = new ArrayList<Parameter>();

            paraList.add(new Parameter(Parameter.String, iasNotiStatus));
            paraList.add(new Parameter(Parameter.String, deleteInd));
            paraList.add(new Parameter(Parameter.Integer, iasMsgProcessBatchLimit));

            rs = conn.getResultSet(SELECT_PENDING_DELETE_IAS_APPLICATION, paraList);

            while (rs.next()) {
                IasUserApplication iasUserApplication = new IasUserApplication();
                iasUserApplication.setClientId(rs.getString("CLIENT_ID"));
                iasUserApplication.setRecipientId(rs.getString("RECIPIENT_ID"));
                iasUserApplication.setOperationType(rs.getString("OPERATION_TYPE"));
                iasUserApplication.setRecipientIdType(rs.getString("RECIPIENT_ID_TYPE"));
                iasUserApplication.setAppStatus(rs.getString("APP_STATUS"));
                iasUserApplication.setAppStatusUpdateDate(rs.getTimestamp("APP_STATUS_UPDATE_DATE"));
                iasUserApplication.setIasApplicationId(rs.getString("IAS_APPLICATION_ID"));
                iasUserApplication.setNotiId(rs.getString("NOTI_ID"));
                iasUserApplication.setServiceProviderId(rs.getString("SERVICE_PROVIDER_ID"));
                iasUserApplication.setTitleEn(rs.getString("TITLE_EN"));
                iasUserApplication.setTitleTc(rs.getString("TITLE_TC"));
                iasUserApplication.setTitleSc(rs.getString("TITLE_SC"));
                iasUserApplication.setStatus(rs.getString("STATUS"));
                iasUserApplication.setMapNotiId(rs.getString("MAP_NOTI_ID"));
                iasUserApplication.setIasOptSpId(rs.getString("IAS_OPT_SP_ID"));
                resultList.add(iasUserApplication);
            }

            logger.debug("getPendingDeleteIasApplication - END");

            return resultList;
        } catch (Exception ex) {
            logger.error("General exception caught in getPendingDeleteIasApplication", ex);
            throw ex;
        } finally {
            try {
                if (rs != null) {
                    HPFW_Connection.close(rs);
                }
            } catch (Exception ex) {
                logger.error("General exception caught in getPendingDeleteIasApplication - rs.close();", ex);
            }
        }
    }

    public List<String> getTxIdFromSentIasUserApplication(HPFW_Connection conn, int IasLiveToTimeDay, int dayLimit,
            int sizeLimit) throws Exception {
        logger.debug("getTxIdFromSentIasUserApplication - START");

        ResultSet rs = null;
        ArrayList<String> resultList = new ArrayList<String>();

        String SELECT_TX_ID_FROM_SENT_IAS_USER_APPLICATION = "select distinct TX_ID "
                + "from IAS_USER_APPLICATION "
                + "where IAS_NOTI_STATUS = ? "
                + "and IAS_NOTI_RESULT IN ('" + IntegrationConstants.SEND_NOTI_ID_RESULT_READY_TO_SEND + "', '"
                + IntegrationConstants.SEND_NOTI_ID_RESULT_SENT + "') "
                + "and IAS_DELIVERY_STATUS is null "
                + "and SENT_DT <= now() - interval ? DAY "
                + "and SENT_DT > now() - interval ? DAY order by SENT_DT limit ?";

        try {
            ArrayList<Parameter> paraList = new ArrayList<Parameter>();

            paraList.add(new Parameter(Parameter.String, IntegrationConstants.IAS_NOTI_STATUS_SENT));
            paraList.add(new Parameter(Parameter.Integer, IasLiveToTimeDay));
            paraList.add(new Parameter(Parameter.Integer, dayLimit));
            paraList.add(new Parameter(Parameter.Integer, sizeLimit));

            rs = conn.getResultSet(SELECT_TX_ID_FROM_SENT_IAS_USER_APPLICATION, paraList);
            while (rs.next()) {
                resultList.add(rs.getString("TX_ID"));
            }

            logger.debug("getTxIdFromSentIasUserApplication - END");
            return resultList;
        } catch (Exception ex) {
            logger.error("General exception caught in getTxIdFromSentIasUserApplication", ex);
            throw ex;
        } finally {
            try {
                if (rs != null) {
                    HPFW_Connection.close(rs);
                }
            } catch (Exception ex) {
                logger.error("General exception caught in getTxIdFromSentIasUserApplication - rs.close();", ex);
            }
        }
    }

    public boolean isIasUserApplicationExistByNotiIdApplicationIdTxId(HPFW_Connection conn, String notiId,
            String iasApplicationId, String txId) throws Exception {
        logger.debug("isIasUserApplicationExistByNotiIdApplicationIdTxId - START");

        boolean result = false;
        ResultSet rs = null;

        String SELECT_IAS_USER_APPLICATION_BY_NOTI_ID_APPLICATION_ID_TX_ID = "SELECT COUNT(*) CNT FROM IAS_USER_APPLICATION WHERE NOTI_ID = ? and IAS_APPLICATION_ID = ? and TX_ID = ?";

        try {
            ArrayList<Parameter> paraList = new ArrayList<Parameter>();

            paraList.add(new Parameter(Parameter.String, notiId));
            paraList.add(new Parameter(Parameter.String, iasApplicationId));
            paraList.add(new Parameter(Parameter.String, txId));

            rs = conn.getResultSet(SELECT_IAS_USER_APPLICATION_BY_NOTI_ID_APPLICATION_ID_TX_ID, paraList);

            while (rs.next()) {
                if (Integer.parseInt(rs.getString("CNT")) > 0) {
                    result = true;
                }
            }

            logger.debug("isIasUserApplicationExistByNotiIdApplicationIdTxId - END");

            return result;
        } catch (Exception ex) {
            logger.error("General exception caught in isIasUserApplicationExistByNotiIdApplicationIdTxId", ex);
            throw ex;
        } finally {
            try {
                if (rs != null) {
                    HPFW_Connection.close(rs);
                }
            } catch (Exception ex) {
                logger.error(
                        "General exception caught in isIasUserApplicationExistByNotiIdApplicationIdTxId - rs.close();",
                        ex);
            }
        }
    }

}