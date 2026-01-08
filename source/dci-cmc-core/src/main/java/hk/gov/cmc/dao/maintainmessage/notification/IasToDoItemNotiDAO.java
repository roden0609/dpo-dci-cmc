package hk.gov.cmc.dao.maintainmessage.notification;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import hk.gov.cmc.common.IntegrationConstants;
import hk.gov.cmc.model.maintainmessage.todoitem.IasUserToDoItem;
import hk.gov.cmc.persistence.connection.hpfw.HPFW_Connection;
import hk.gov.cmc.persistence.connection.hpfw.Parameter;

public class IasToDoItemNotiDAO {

    private static Log logger = LogFactory.getLog(IasToDoItemNotiDAO.class);

    public IasToDoItemNotiDAO() {
        super();
    }

    public List<IasUserToDoItem> getNotiIdMissingIasUserToDoItem(HPFW_Connection conn,
            String recipientIdType, int iasMsgProcessBatchLimit) throws Exception {
        logger.debug("getNotiIdMissingIasUserToDoItem - START");

        ResultSet rs = null;
        ArrayList<IasUserToDoItem> resultList = new ArrayList<IasUserToDoItem>();

        String SELECT_NOTI_ID_MISSING_IAS_USER_TO_DO_ITEM = "select "
                + "iutdi.CLIENT_ID, iutdi.RECIPIENT_ID, iutdi.RECIPIENT_ID_TYPE, iutdi.HKID_ENCRYPTED, "
                + "iutdi.TRAN_ID, itdi.IAS_TO_DO_ITEM_ID, "
                + "t.SERVICE_PROVIDER_ID, IFNULL(t.IAS_SHOW_ES_SET_BTN, 'N') "
                + "AS IAS_SHOW_ES_SET_BTN, IFNULL(s.IAS_OPT_SP_ID, s.SERVICE_PROVIDER_ID) AS IAS_OPT_SP_ID "
                + "from IAS_TO_DO_ITEM itdi, IAS_USER_TO_DO_ITEM iutdi, CMC_TEMPLATE t, CMC_SERVICE_PROVIDER s "
                + "where itdi.IAS_TO_DO_ITEM_ID = iutdi.IAS_TO_DO_ITEM_ID "
                + "AND itdi.TEMPLATE_ID = t.TEMPLATE_ID "
                + "AND itdi.TEMPLATE_VERSION = t.TEMPLATE_VERSION "
                + "and t.SERVICE_PROVIDER_ID = s.SERVICE_PROVIDER_ID "
                + "AND (iutdi.NOTI_ID is null OR LENGTH(iutdi.NOTI_ID) = 0) "
                + "AND iutdi.RECIPIENT_ID_TYPE = ? "
                + "AND iutdi.IAS_NOTI_STATUS = ? LIMIT ? ";

        try {

            ArrayList<Parameter> paraList = new ArrayList<Parameter>();

            paraList.add(new Parameter(Parameter.String, recipientIdType));
            paraList.add(new Parameter(Parameter.String, IntegrationConstants.IAS_NOTI_STATUS_NEW));
            paraList.add(new Parameter(Parameter.Integer, iasMsgProcessBatchLimit));

            rs = conn.getResultSet(SELECT_NOTI_ID_MISSING_IAS_USER_TO_DO_ITEM, paraList);

            while (rs.next()) {
                IasUserToDoItem iasUserToDoItem = new IasUserToDoItem();
                iasUserToDoItem.setClientId(rs.getString("CLIENT_ID"));
                iasUserToDoItem.setRecipientId(rs.getString("RECIPIENT_ID"));
                iasUserToDoItem.setRecipientIdType(rs.getString("RECIPIENT_ID_TYPE"));
                iasUserToDoItem.setHkidEncrypted(rs.getString("HKID_ENCRYPTED"));
                iasUserToDoItem.setTranId(rs.getString("TRAN_ID"));
                iasUserToDoItem.setIasToDoItemId(rs.getString("IAS_TO_DO_ITEM_ID"));
                iasUserToDoItem.setServiceProviderId(rs.getString("SERVICE_PROVIDER_ID"));

                // Input field for "ias_opt_check_ind" is not used because "ias_show_es_set_btn" derive the same meaning. So, check "ias_show_es_set_btn" while processing message requests.
                iasUserToDoItem.setIasOptCheckInd(rs.getString("IAS_SHOW_ES_SET_BTN"));
                iasUserToDoItem.setIasOptSpId(rs.getString("IAS_OPT_SP_ID"));

                resultList.add(iasUserToDoItem);
            }

            logger.debug("getNotiIdMissingIasUserToDoItem - END");

            return resultList;

        } catch (Exception ex) {
            logger.error("General exception caught in getNotiIdMissingIasUserToDoItem", ex);
            throw ex;
        } finally {
            try {
                if (rs != null) {
                    HPFW_Connection.close(rs);
                }
            } catch (Exception ex) {
                logger.error("General exception caught in getNotiIdMissingIasUserToDoItem - rs.close();", ex);
            }
        }
    }

    public void createIasUndeliveredToDoItem(HPFW_Connection conn, String clientId, String recipientId,
            String iasToDoItemId,
            String recipientIdType, String rcptType, String status, String reason) throws Exception {
        logger.debug("createIasUndeliveredToDoItem - START");

        String insertSQL = "INSERT IGNORE INTO ias_undelivered_to_do_item "
                + "(CLIENT_ID, RECIPIENT_ID, IAS_TO_DO_ITEM_ID, RECIPIENT_ID_TYPE, RCPT_TYPE, STATUS, REASON, CREATE_DT, LAST_MODIFY_DT, CREATE_BY, LAST_MODIFY_BY) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, now(3), now(3), 'CMC', 'CMC')";

        ArrayList<Parameter> paraList = new ArrayList<Parameter>();
        paraList.add(new Parameter(Parameter.String, clientId));
        paraList.add(new Parameter(Parameter.String, recipientId));
        paraList.add(new Parameter(Parameter.String, iasToDoItemId));
        paraList.add(new Parameter(Parameter.String, recipientIdType));
        paraList.add(new Parameter(Parameter.String, rcptType));
        paraList.add(new Parameter(Parameter.String, status));
        paraList.add(new Parameter(Parameter.String, reason));

        conn.executeStatement(insertSQL, paraList);
        logger.debug("createIasUndeliveredToDoItem - END");
    }

    public void createIasUndeliveredToDoItemWithHKIdEncrypted(HPFW_Connection conn, String clientId, String recipientId,
            String iasToDoItemId,
            String recipientIdType, String hkidEncrypted, String rcptType, String status, String reason)
            throws Exception {
        logger.debug("createIasUndeliveredToDoItemWithHKIdEncrypted - START");
        logger.info("createIasUndeliveredToDoItemWithHKIdEncrypted - clientId=" + clientId + ", recipientId="
                + recipientId + ", iasToDoItemId=" + iasToDoItemId +
                ", recipientIdType=" + recipientIdType + ", hkidEncrypted=" + hkidEncrypted + ", rcptType=" + rcptType +
                ", status=" + status + ", reason=" + reason);

        String insertSQL = "INSERT IGNORE INTO ias_undelivered_to_do_item "
                + "(CLIENT_ID, RECIPIENT_ID, IAS_TO_DO_ITEM_ID, RECIPIENT_ID_TYPE, HKID_ENCRYPTED, RCPT_TYPE, STATUS, REASON, CREATE_DT, LAST_MODIFY_DT, CREATE_BY, LAST_MODIFY_BY) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, now(3), now(3), 'CMC', 'CMC')";

        ArrayList<Parameter> paraList = new ArrayList<Parameter>();
        paraList.add(new Parameter(Parameter.String, clientId));
        paraList.add(new Parameter(Parameter.String, recipientId));
        paraList.add(new Parameter(Parameter.String, iasToDoItemId));
        paraList.add(new Parameter(Parameter.String, recipientIdType));
        paraList.add(new Parameter(Parameter.String, hkidEncrypted));
        paraList.add(new Parameter(Parameter.String, rcptType));
        paraList.add(new Parameter(Parameter.String, status));
        paraList.add(new Parameter(Parameter.String, reason));

        conn.executeStatement(insertSQL, paraList);
        logger.debug("createIasUndeliveredToDoItemWithHKIdEncrypted - END");
    }

    public List<IasUserToDoItem> getOutstandingIasToDoItem(HPFW_Connection conn, String iasNotiStatus, String deleteInd,
            int iasMsgProcessBatchLimit)
            throws Exception {
        logger.debug("getOutstandingIasToDoItem - START");

        ResultSet rs = null;
        ArrayList<IasUserToDoItem> resultList = new ArrayList<IasUserToDoItem>();

        String SELECT_OUTSTANDING_IAS_TO_DO_ITEM = "select B.*, C.TRAN_ID as PREV_TRAN_ID, C.IAS_TO_DO_ITEM_ID as PREV_IAS_TO_DO_ITEM_ID from ("
                +
                "	select A.*, ienm.NOTI_ID as MAP_NOTI_ID from (" +
                "		select iutdi.CLIENT_ID, iutdi.RECIPIENT_ID, iutdi.OPERATION_TYPE, iutdi.RECIPIENT_ID_TYPE, iutdi.ITEM_DATE, iutdi.TRAN_ID, iutdi.CREATE_DT,"
                +
                "		itdi.IAS_TO_DO_ITEM_ID, iutdi.NOTI_ID, t.SERVICE_PROVIDER_ID, s.IAS_OPT_SP_ID," +
                "		itdi.TITLE_EN, itdi.TITLE_TC, itdi.TITLE_SC, iuni.STATUS" +
                "		from IAS_TO_DO_ITEM itdi, CMC_TEMPLATE t, CMC_SERVICE_PROVIDER s, IAS_USER_TO_DO_ITEM iutdi LEFT JOIN IAS_USER_NOTI_INFO iuni ON iutdi.NOTI_ID = iuni.NOTI_ID"
                +
                "		where itdi.IAS_TO_DO_ITEM_ID = iutdi.IAS_TO_DO_ITEM_ID" +
                "		AND itdi.TEMPLATE_ID = t.TEMPLATE_ID" +
                "		AND itdi.TEMPLATE_VERSION = t.TEMPLATE_VERSION" +
                "		and t.SERVICE_PROVIDER_ID = s.SERVICE_PROVIDER_ID" +
                "		AND iutdi.IAS_NOTI_STATUS = ?" +
                "		AND iutdi.NOTI_ID is not null" +
                "		AND iutdi.DELETE_IND = ? ORDER BY itdi.IAS_TO_DO_ITEM_ID" +
                "	) A LEFT JOIN IAS_ES_NOTI_MAP ienm ON A.NOTI_ID = ienm.NOTI_ID AND A.SERVICE_PROVIDER_ID = ienm.SERVICE_PROVIDER_ID"
                +
                ") B LEFT JOIN IAS_USER_TO_DO_ITEM C ON B.TRAN_ID = C.ACTION_TRAN_ID LIMIT ?";

        try {
            ArrayList<Parameter> paraList = new ArrayList<Parameter>();

            paraList.add(new Parameter(Parameter.String, iasNotiStatus));
            paraList.add(new Parameter(Parameter.String, deleteInd));
            paraList.add(new Parameter(Parameter.Integer, iasMsgProcessBatchLimit));

            rs = conn.getResultSet(SELECT_OUTSTANDING_IAS_TO_DO_ITEM, paraList);

            while (rs.next()) {
                IasUserToDoItem iasUserToDoItem = new IasUserToDoItem();
                iasUserToDoItem.setClientId(rs.getString("CLIENT_ID"));
                iasUserToDoItem.setRecipientId(rs.getString("RECIPIENT_ID"));
                iasUserToDoItem.setOperationType(rs.getString("OPERATION_TYPE"));
                iasUserToDoItem.setRecipientIdType(rs.getString("RECIPIENT_ID_TYPE"));
                iasUserToDoItem.setItemDate(rs.getString("ITEM_DATE"));
                iasUserToDoItem.setIasToDoItemId(rs.getString("IAS_TO_DO_ITEM_ID"));
                iasUserToDoItem.setNotiId(rs.getString("NOTI_ID"));
                iasUserToDoItem.setServiceProviderId(rs.getString("SERVICE_PROVIDER_ID"));
                iasUserToDoItem.setTitleEn(rs.getString("TITLE_EN"));
                iasUserToDoItem.setTitleTc(rs.getString("TITLE_TC"));
                iasUserToDoItem.setTitleSc(rs.getString("TITLE_SC"));
                iasUserToDoItem.setStatus(rs.getString("STATUS"));
                iasUserToDoItem.setMapNotiId(rs.getString("MAP_NOTI_ID"));
                iasUserToDoItem.setIasOptSpId(rs.getString("IAS_OPT_SP_ID"));
                iasUserToDoItem.setCreateDt(rs.getTimestamp("CREATE_DT"));
                iasUserToDoItem.setPrevIasToDoItemId(rs.getString("PREV_IAS_TO_DO_ITEM_ID"));
                resultList.add(iasUserToDoItem);
            }

            logger.debug("getOutstandingIasToDoItem - END");

            return resultList;
        } catch (Exception ex) {
            logger.error("General exception caught in getOutstandingIasToDoItem", ex);
            throw ex;
        } finally {
            try {
                if (rs != null) {
                    HPFW_Connection.close(rs);
                }
            } catch (Exception ex) {
                logger.error("General exception caught in getOutstandingIasToDoItem - rs.close();", ex);
            }
        }
    }

    public List<IasUserToDoItem> getPendingDeleteIasToDoItem(HPFW_Connection conn, String iasNotiStatus,
            String deleteInd, int iasMsgProcessBatchLimit)
            throws Exception {
        logger.debug("getPendingDeleteIasToDoItem - START");

        ResultSet rs = null;
        ArrayList<IasUserToDoItem> resultList = new ArrayList<IasUserToDoItem>();

        String SELECT_PENDING_DELETE_IAS_TO_DO_ITEM = "	select A.*, ienm.NOTI_ID as MAP_NOTI_ID from (" +
                "		select iutdi.CLIENT_ID, iutdi.RECIPIENT_ID, iutdi.OPERATION_TYPE, iutdi.RECIPIENT_ID_TYPE, iutdi.ITEM_DATE, iutdi.TRAN_ID, iutdi.CREATE_DT,"
                +
                "		itdi.IAS_TO_DO_ITEM_ID, iutdi.NOTI_ID, t.SERVICE_PROVIDER_ID, s.IAS_OPT_SP_ID," +
                "		itdi.TITLE_EN, itdi.TITLE_TC, itdi.TITLE_SC, iuni.STATUS" +
                "		from IAS_TO_DO_ITEM itdi, CMC_TEMPLATE t, CMC_SERVICE_PROVIDER s, IAS_USER_TO_DO_ITEM iutdi LEFT JOIN IAS_USER_NOTI_INFO iuni ON iutdi.NOTI_ID = iuni.NOTI_ID"
                +
                "		where itdi.IAS_TO_DO_ITEM_ID = iutdi.IAS_TO_DO_ITEM_ID" +
                "		AND itdi.TEMPLATE_ID = t.TEMPLATE_ID" +
                "		AND itdi.TEMPLATE_VERSION = t.TEMPLATE_VERSION" +
                "		and t.SERVICE_PROVIDER_ID = s.SERVICE_PROVIDER_ID" +
                "		AND iutdi.IAS_NOTI_STATUS = ?" +
                "		AND iutdi.IAS_NOTI_RESULT IN ('" + IntegrationConstants.SEND_NOTI_ID_RESULT_READY_TO_SEND
                + "', '"
                + IntegrationConstants.SEND_NOTI_ID_RESULT_SENT + "') " +
                "		AND iutdi.NOTI_ID is not null" +
                "		AND iutdi.DELETE_IND = ? ORDER BY itdi.IAS_TO_DO_ITEM_ID" +
                "	) A LEFT JOIN IAS_ES_NOTI_MAP ienm ON A.NOTI_ID = ienm.NOTI_ID AND A.SERVICE_PROVIDER_ID = ienm.SERVICE_PROVIDER_ID LIMIT ?";

        try {
            ArrayList<Parameter> paraList = new ArrayList<Parameter>();

            paraList.add(new Parameter(Parameter.String, iasNotiStatus));
            paraList.add(new Parameter(Parameter.String, deleteInd));
            paraList.add(new Parameter(Parameter.Integer, iasMsgProcessBatchLimit));

            rs = conn.getResultSet(SELECT_PENDING_DELETE_IAS_TO_DO_ITEM, paraList);

            while (rs.next()) {
                IasUserToDoItem iasUserToDoItem = new IasUserToDoItem();
                iasUserToDoItem.setClientId(rs.getString("CLIENT_ID"));
                iasUserToDoItem.setRecipientId(rs.getString("RECIPIENT_ID"));
                iasUserToDoItem.setOperationType(rs.getString("OPERATION_TYPE"));
                iasUserToDoItem.setRecipientIdType(rs.getString("RECIPIENT_ID_TYPE"));
                iasUserToDoItem.setItemDate(rs.getString("ITEM_DATE"));
                iasUserToDoItem.setIasToDoItemId(rs.getString("IAS_TO_DO_ITEM_ID"));
                iasUserToDoItem.setNotiId(rs.getString("NOTI_ID"));
                iasUserToDoItem.setServiceProviderId(rs.getString("SERVICE_PROVIDER_ID"));
                iasUserToDoItem.setTitleEn(rs.getString("TITLE_EN"));
                iasUserToDoItem.setTitleTc(rs.getString("TITLE_TC"));
                iasUserToDoItem.setTitleSc(rs.getString("TITLE_SC"));
                iasUserToDoItem.setStatus(rs.getString("STATUS"));
                iasUserToDoItem.setMapNotiId(rs.getString("MAP_NOTI_ID"));
                iasUserToDoItem.setIasOptSpId(rs.getString("IAS_OPT_SP_ID"));
                resultList.add(iasUserToDoItem);
            }

            logger.debug("getPendingDeleteIasToDoItem - END");

            return resultList;
        } catch (Exception ex) {
            logger.error("General exception caught in getPendingDeleteIasToDoItem", ex);
            throw ex;
        } finally {
            try {
                if (rs != null) {
                    HPFW_Connection.close(rs);
                }
            } catch (Exception ex) {
                logger.error("General exception caught in getPendingDeleteIasToDoItem - rs.close();", ex);
            }
        }
    }

    public List<String> getTxIdFromSentIasUserToDoItem(HPFW_Connection conn, int IasLiveToTimeDay, int dayLimit,
            int sizeLimit) throws Exception {
        logger.debug("getTxIdFromSentIasUserToDoItem - START");

        ResultSet rs = null;
        ArrayList<String> resultList = new ArrayList<String>();

        String SELECT_TX_ID_FROM_SENT_IAS_USER_TO_DO_ITEM = "select distinct TX_ID "
                + "from IAS_USER_TO_DO_ITEM "
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

    public boolean isIasUserToDoItemExistByNotiIdToDoItemIdTxId(HPFW_Connection conn, String notiId,
            String iasToDoItemId, String txId) throws Exception {
        logger.debug("isIasUserToDoItemExistByNotiIdToDoItemIdTxId - START");

        boolean result = false;
        ResultSet rs = null;

        String SELECT_IAS_USER_TO_DO_ITEM_BY_NOTI_ID_TO_DO_ITEM_ID_TX_ID = "SELECT COUNT(*) CNT FROM IAS_USER_TO_DO_ITEM WHERE NOTI_ID = ? and IAS_TO_DO_ITEM_ID = ? and TX_ID = ?";

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

}