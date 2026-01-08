package hk.gov.cmc.persistence.ias.application;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import hk.gov.cmc.persistence.connection.hpfw.CommonDBUtils;
import hk.gov.cmc.persistence.connection.hpfw.HPFW_Connection;
import hk.gov.cmc.persistence.connection.hpfw.Parameter;

public class IasUserApplication_ implements Serializable {
    private static final long serialVersionUID = 1L;
    private boolean initialized = false;

    private final static String thisTableName = "IAS_USER_APPLICATION";
    private final static String thisTableHistName = "_H";
    private boolean forUpdate = false;
    private String clientId = null;
    private String recipientId = null;
    private String iasApplicationId = null;
    private String recipientIdType = null;
    private String hkidEncrypted = null;
    private String notiId = null;
    private String tranId = null;
    private String actionTranId = null;
    private String appRefNum = null;
    private String iasNotiStatus = null;
    private String iasNotiResult = null;
    private String txId = null;
    private Timestamp sentDt = null;
    private String iasDeliveryStatus = null;
    private String appStatus = null;
    private Timestamp appStatusUpdateDate = null;
    private String contactNum = null;
    private String contactEmail = null;
    private String miscInfo = null;
    private String operationType = null;
    private String deleteInd = null;
    private String iasDeleteNotiStatus = null;
    private String iasDeleteNotiResult = null;
    private String iasDeleteNotiTxId = null;
    private Timestamp iasDeleteNotiSentDt = null;
    private String housekeepInd = null;
    private Timestamp createDt = null;
    private Timestamp lastModifyDt = null;
    private String createBy = null;
    private String lastModifyBy = null;

    private boolean dirty_clientId = false;
    private boolean dirty_recipientId = false;
    private boolean dirty_iasApplicationId = false;
    private boolean dirty_recipientIdType = false;
    private boolean dirty_hkidEncrypted = false;
    private boolean dirty_notiId = false;
    private boolean dirty_tranId = false;
    private boolean dirty_actionTranId = false;
    private boolean dirty_appRefNum = false;
    private boolean dirty_iasNotiStatus = false;
    private boolean dirty_iasNotiResult = false;
    private boolean dirty_txId = false;
    private boolean dirty_sentDt = false;
    private boolean dirty_iasDeliveryStatus = false;
    private boolean dirty_appStatus = false;
    private boolean dirty_appStatusUpdateDate = false;
    private boolean dirty_contactNum = false;
    private boolean dirty_contactEmail = false;
    private boolean dirty_miscInfo = false;
    private boolean dirty_operationType = false;
    private boolean dirty_deleteInd = false;
    private boolean dirty_iasDeleteNotiStatus = false;
    private boolean dirty_iasDeleteNotiResult = false;
    private boolean dirty_iasDeleteNotiTxId = false;
    private boolean dirty_iasDeleteNotiSentDt = false;
    private boolean dirty_housekeepInd = false;
    private boolean dirty_createDt = false;
    private boolean dirty_lastModifyDt = false;
    private boolean dirty_createBy = false;
    private boolean dirty_lastModifyBy = false;

    public IasUserApplication_() {
        super();
    }

    public IasUserApplication_(HPFW_Connection countCon, String clientId, String recipientId, String iasApplicationId)
            throws SQLException, NullPointerException {
        this();
        init(countCon, clientId, recipientId, iasApplicationId, false);
    }

    public IasUserApplication_(HPFW_Connection countCon, String clientId, String recipientId, String iasApplicationId,
            boolean forUpdate) throws SQLException, NullPointerException {
        this();
        init(countCon, clientId, recipientId, iasApplicationId, forUpdate);
    }

    public void insert(HPFW_Connection countCon) throws SQLException {
        if (HPFW_Connection.DIRECT.equals(countCon.getUpdateMode())
                || HPFW_Connection.DIRECT_WITH_HISTORY.equals(countCon.getUpdateMode())) {
            ArrayList<Parameter> paraList = new ArrayList<Parameter>();

            String sql = "insert into ";
            sql += thisTableName;
            sql += " (";

            // perpare set ...
            if (clientId != null)
                sql += "client_id,";
            if (recipientId != null)
                sql += "recipient_id,";
            if (iasApplicationId != null)
                sql += "ias_application_id,";
            if (recipientIdType != null)
                sql += "recipient_id_type,";
            if (hkidEncrypted != null)
                sql += "hkid_encrypted,";
            if (notiId != null)
                sql += "noti_id,";
            if (tranId != null)
                sql += "tran_id,";
            if (actionTranId != null)
                sql += "action_tran_id,";
            if (appRefNum != null)
                sql += "app_ref_num,";
            if (iasNotiStatus != null)
                sql += "ias_noti_status,";
            if (iasNotiResult != null)
                sql += "ias_noti_result,";
            if (txId != null)
                sql += "tx_id,";
            if (sentDt != null)
                sql += "sent_dt,";
            if (iasDeliveryStatus != null)
                sql += "ias_delivery_status,";
            if (appStatus != null)
                sql += "app_status,";
            if (appStatusUpdateDate != null)
                sql += "app_status_update_date,";
            if (contactNum != null)
                sql += "contact_num,";
            if (contactEmail != null)
                sql += "contact_email,";
            if (miscInfo != null)
                sql += "misc_info,";
            if (operationType != null)
                sql += "operation_type,";
            if (deleteInd != null)
                sql += "delete_ind,";
            if (iasDeleteNotiStatus != null)
                sql += "ias_delete_noti_status,";
            if (iasDeleteNotiResult != null)
                sql += "ias_delete_noti_result,";
            if (iasDeleteNotiTxId != null)
                sql += "ias_delete_noti_tx_id,";
            if (iasDeleteNotiSentDt != null)
                sql += "ias_delete_noti_sent_dt,";
            if (housekeepInd != null)
                sql += "housekeep_ind,";
            sql += "create_by,last_modify_by,create_dt,last_modify_dt) values (";

            // perpare set ...
            if (clientId != null)
                sql += "?,";
            if (recipientId != null)
                sql += "?,";
            if (iasApplicationId != null)
                sql += "?,";
            if (recipientIdType != null)
                sql += "?,";
            if (hkidEncrypted != null)
                sql += "?,";
            if (notiId != null)
                sql += "?,";
            if (tranId != null)
                sql += "?,";
            if (actionTranId != null)
                sql += "?,";
            if (appRefNum != null)
                sql += "?,";
            if (iasNotiStatus != null)
                sql += "?,";
            if (iasNotiResult != null)
                sql += "?,";
            if (txId != null)
                sql += "?,";
            if (sentDt != null)
                sql += "?,";
            if (iasDeliveryStatus != null)
                sql += "?,";
            if (appStatus != null)
                sql += "?,";
            if (appStatusUpdateDate != null)
                sql += "?,";
            if (contactNum != null)
                sql += "?,";
            if (contactEmail != null)
                sql += "?,";
            if (miscInfo != null)
                sql += "?,";
            if (operationType != null)
                sql += "?,";
            if (deleteInd != null)
                sql += "?,";
            if (iasDeleteNotiStatus != null)
                sql += "?,";
            if (iasDeleteNotiResult != null)
                sql += "?,";
            if (iasDeleteNotiTxId != null)
                sql += "?,";
            if (iasDeleteNotiSentDt != null)
                sql += "?,";
            if (housekeepInd != null)
                sql += "?,";
            sql += "?,?,?,?) ";

            // perpare set ...
            if (clientId != null)
                paraList.add(new Parameter(Parameter.String, this.clientId));
            if (recipientId != null)
                paraList.add(new Parameter(Parameter.String, this.recipientId));
            if (iasApplicationId != null)
                paraList.add(new Parameter(Parameter.String, this.iasApplicationId));
            if (recipientIdType != null)
                paraList.add(new Parameter(Parameter.String, this.recipientIdType));
            if (hkidEncrypted != null)
                paraList.add(new Parameter(Parameter.String, this.hkidEncrypted));
            if (notiId != null)
                paraList.add(new Parameter(Parameter.String, this.notiId));
            if (tranId != null)
                paraList.add(new Parameter(Parameter.String, this.tranId));
            if (actionTranId != null)
                paraList.add(new Parameter(Parameter.String, this.actionTranId));
            if (appRefNum != null)
                paraList.add(new Parameter(Parameter.String, this.appRefNum));
            if (iasNotiStatus != null)
                paraList.add(new Parameter(Parameter.String, this.iasNotiStatus));
            if (iasNotiResult != null)
                paraList.add(new Parameter(Parameter.String, this.iasNotiResult));
            if (txId != null)
                paraList.add(new Parameter(Parameter.String, this.txId));
            if (sentDt != null)
                paraList.add(new Parameter(Parameter.Timestamp, this.sentDt));
            if (iasDeliveryStatus != null)
                paraList.add(new Parameter(Parameter.String, this.iasDeliveryStatus));
            if (appStatus != null)
                paraList.add(new Parameter(Parameter.String, this.appStatus));
            if (appStatusUpdateDate != null)
                paraList.add(new Parameter(Parameter.Timestamp, this.appStatusUpdateDate));
            if (contactNum != null)
                paraList.add(new Parameter(Parameter.String, this.contactNum));
            if (contactEmail != null)
                paraList.add(new Parameter(Parameter.String, this.contactEmail));
            if (miscInfo != null)
                paraList.add(new Parameter(Parameter.String, this.miscInfo));
            if (operationType != null)
                paraList.add(new Parameter(Parameter.String, this.operationType));
            if (deleteInd != null)
                paraList.add(new Parameter(Parameter.String, this.deleteInd));
            if (iasDeleteNotiStatus != null)
                paraList.add(new Parameter(Parameter.String, this.iasDeleteNotiStatus));
            if (iasDeleteNotiResult != null)
                paraList.add(new Parameter(Parameter.String, this.iasDeleteNotiResult));
            if (iasDeleteNotiTxId != null)
                paraList.add(new Parameter(Parameter.String, this.iasDeleteNotiTxId));
            if (iasDeleteNotiSentDt != null)
                paraList.add(new Parameter(Parameter.Timestamp, this.iasDeleteNotiSentDt));
            if (housekeepInd != null)
                paraList.add(new Parameter(Parameter.String, this.housekeepInd));
            paraList.add(new Parameter(Parameter.String, countCon.getLastUpdBy()));
            paraList.add(new Parameter(Parameter.String, countCon.getLastUpdBy()));
            paraList.add(new Parameter(Parameter.Timestamp, countCon.getLastUpTime()));
            paraList.add(new Parameter(Parameter.Timestamp, countCon.getLastUpTime()));
            countCon.executeStatement(sql, paraList);
        }

        if (HPFW_Connection.HISTORY_ONLY.equals(countCon.getUpdateMode())
                || HPFW_Connection.DIRECT_WITH_HISTORY.equals(countCon.getUpdateMode())) {
            ArrayList<Parameter> histParaList = new ArrayList<Parameter>();

            String hist_sql = "insert into ";
            hist_sql += thisTableName + thisTableHistName;
            hist_sql += " (HIST_SEQ,HIST_action,HIST_update_type,HIST_status,HIST_server_id,";

            // perpare set ...
            if (clientId != null)
                hist_sql += "client_id,";
            if (recipientId != null)
                hist_sql += "recipient_id,";
            if (iasApplicationId != null)
                hist_sql += "ias_application_id,";
            if (recipientIdType != null)
                hist_sql += "recipient_id_type,";
            if (hkidEncrypted != null)
                hist_sql += "hkid_encrypted,";
            if (notiId != null)
                hist_sql += "noti_id,";
            if (tranId != null)
                hist_sql += "tran_id,";
            if (actionTranId != null)
                hist_sql += "action_tran_id,";
            if (appRefNum != null)
                hist_sql += "app_ref_num,";
            if (iasNotiStatus != null)
                hist_sql += "ias_noti_status,";
            if (iasNotiResult != null)
                hist_sql += "ias_noti_result,";
            if (txId != null)
                hist_sql += "tx_id,";
            if (sentDt != null)
                hist_sql += "sent_dt,";
            if (iasDeliveryStatus != null)
                hist_sql += "ias_delivery_status,";
            if (appStatus != null)
                hist_sql += "app_status,";
            if (appStatusUpdateDate != null)
                hist_sql += "app_status_update_date,";
            if (contactNum != null)
                hist_sql += "contact_num,";
            if (contactEmail != null)
                hist_sql += "contact_email,";
            if (miscInfo != null)
                hist_sql += "misc_info,";
            if (operationType != null)
                hist_sql += "operation_type,";
            if (deleteInd != null)
                hist_sql += "delete_ind,";
            if (iasDeleteNotiStatus != null)
                hist_sql += "ias_delete_noti_status,";
            if (iasDeleteNotiResult != null)
                hist_sql += "ias_delete_noti_result,";
            if (iasDeleteNotiTxId != null)
                hist_sql += "ias_delete_noti_tx_id,";
            if (iasDeleteNotiSentDt != null)
                hist_sql += "ias_delete_noti_sent_dt,";
            if (housekeepInd != null)
                hist_sql += "housekeep_ind,";
            hist_sql += "create_by,last_modify_by,create_dt,last_modify_dt) values (?,?,?,?,?,";

            // perpare set ...
            if (clientId != null)
                hist_sql += "?,";
            if (recipientId != null)
                hist_sql += "?,";
            if (iasApplicationId != null)
                hist_sql += "?,";
            if (recipientIdType != null)
                hist_sql += "?,";
            if (hkidEncrypted != null)
                hist_sql += "?,";
            if (notiId != null)
                hist_sql += "?,";
            if (tranId != null)
                hist_sql += "?,";
            if (actionTranId != null)
                hist_sql += "?,";
            if (appRefNum != null)
                hist_sql += "?,";
            if (iasNotiStatus != null)
                hist_sql += "?,";
            if (iasNotiResult != null)
                hist_sql += "?,";
            if (txId != null)
                hist_sql += "?,";
            if (sentDt != null)
                hist_sql += "?,";
            if (iasDeliveryStatus != null)
                hist_sql += "?,";
            if (appStatus != null)
                hist_sql += "?,";
            if (appStatusUpdateDate != null)
                hist_sql += "?,";
            if (contactNum != null)
                hist_sql += "?,";
            if (contactEmail != null)
                hist_sql += "?,";
            if (miscInfo != null)
                hist_sql += "?,";
            if (operationType != null)
                hist_sql += "?,";
            if (deleteInd != null)
                hist_sql += "?,";
            if (iasDeleteNotiStatus != null)
                hist_sql += "?,";
            if (iasDeleteNotiResult != null)
                hist_sql += "?,";
            if (iasDeleteNotiTxId != null)
                hist_sql += "?,";
            if (iasDeleteNotiSentDt != null)
                hist_sql += "?,";
            if (housekeepInd != null)
                hist_sql += "?,";
            hist_sql += "?,?,?,?) ";
            histParaList.add(new Parameter(Parameter.String,
                    CommonDBUtils.getSequence(countCon, thisTableName + thisTableHistName)));
            histParaList.add(new Parameter(Parameter.String, HPFW_Connection.INSERT));
            histParaList.add(new Parameter(Parameter.String,
                    countCon.getUpdateMode().equals(HPFW_Connection.HISTORY_ONLY) ? HPFW_Connection.DUAL
                            : HPFW_Connection.REMOTE));
            histParaList.add(new Parameter(Parameter.String, HPFW_Connection.PENDING));
            histParaList.add(new Parameter(Parameter.String, CommonDBUtils.getSERVER_ID()));

            // perpare set ...
            if (clientId != null)
                histParaList.add(new Parameter(Parameter.String, this.clientId));
            if (recipientId != null)
                histParaList.add(new Parameter(Parameter.String, this.recipientId));
            if (iasApplicationId != null)
                histParaList.add(new Parameter(Parameter.String, this.iasApplicationId));
            if (recipientIdType != null)
                histParaList.add(new Parameter(Parameter.String, this.recipientIdType));
            if (hkidEncrypted != null)
                histParaList.add(new Parameter(Parameter.String, this.hkidEncrypted));
            if (notiId != null)
                histParaList.add(new Parameter(Parameter.String, this.notiId));
            if (tranId != null)
                histParaList.add(new Parameter(Parameter.String, this.tranId));
            if (actionTranId != null)
                histParaList.add(new Parameter(Parameter.String, this.actionTranId));
            if (appRefNum != null)
                histParaList.add(new Parameter(Parameter.String, this.appRefNum));
            if (iasNotiStatus != null)
                histParaList.add(new Parameter(Parameter.String, this.iasNotiStatus));
            if (iasNotiResult != null)
                histParaList.add(new Parameter(Parameter.String, this.iasNotiResult));
            if (txId != null)
                histParaList.add(new Parameter(Parameter.String, this.txId));
            if (sentDt != null)
                histParaList.add(new Parameter(Parameter.Timestamp, this.sentDt));
            if (iasDeliveryStatus != null)
                histParaList.add(new Parameter(Parameter.String, this.iasDeliveryStatus));
            if (appStatus != null)
                histParaList.add(new Parameter(Parameter.String, this.appStatus));
            if (appStatusUpdateDate != null)
                histParaList.add(new Parameter(Parameter.Timestamp, this.appStatusUpdateDate));
            if (contactNum != null)
                histParaList.add(new Parameter(Parameter.String, this.contactNum));
            if (contactEmail != null)
                histParaList.add(new Parameter(Parameter.String, this.contactEmail));
            if (miscInfo != null)
                histParaList.add(new Parameter(Parameter.String, this.miscInfo));
            if (operationType != null)
                histParaList.add(new Parameter(Parameter.String, this.operationType));
            if (deleteInd != null)
                histParaList.add(new Parameter(Parameter.String, this.deleteInd));
            if (iasDeleteNotiStatus != null)
                histParaList.add(new Parameter(Parameter.String, this.iasDeleteNotiStatus));
            if (iasDeleteNotiResult != null)
                histParaList.add(new Parameter(Parameter.String, this.iasDeleteNotiResult));
            if (iasDeleteNotiTxId != null)
                histParaList.add(new Parameter(Parameter.String, this.iasDeleteNotiTxId));
            if (iasDeleteNotiSentDt != null)
                histParaList.add(new Parameter(Parameter.Timestamp, this.iasDeleteNotiSentDt));
            if (housekeepInd != null)
                histParaList.add(new Parameter(Parameter.String, this.housekeepInd));
            histParaList.add(new Parameter(Parameter.String, countCon.getLastUpdBy()));
            histParaList.add(new Parameter(Parameter.String, countCon.getLastUpdBy()));
            histParaList.add(new Parameter(Parameter.Timestamp, countCon.getLastUpTime()));
            histParaList.add(new Parameter(Parameter.Timestamp, countCon.getLastUpTime()));
            countCon.executeStatement(hist_sql, histParaList, false);
        }
    }

    public static void update(HPFW_Connection countCon, String setClause, ArrayList<Parameter> paraL,
            String whereClause, ArrayList<Parameter> whereParaL)
            throws SQLException {
        // e.g. whereClause = "set data1 = ?, data2 = ? " ; => "data1,data2"
        String selectString = setClause.toLowerCase();
        selectString = selectString.replaceAll("set", "");
        selectString = selectString.replaceAll("=", "");
        selectString = selectString.replaceAll(" ", "");
        selectString = selectString.replaceAll("\\?", "");
        String[] selectStringArray = selectString.split(",");

        if (HPFW_Connection.DIRECT.equals(countCon.getUpdateMode())
                || HPFW_Connection.DIRECT_WITH_HISTORY.equals(countCon.getUpdateMode())) {
            ArrayList<Parameter> tempList = new ArrayList<Parameter>();
            String sql = "update ";
            sql += thisTableName + " ";
            sql += setClause + " ,last_modify_by = ?,last_modify_dt = ? ";
            sql += whereClause + " ";
            tempList.addAll(paraL);
            tempList.add(new Parameter(Parameter.String, countCon.getLastUpdBy()));
            tempList.add(new Parameter(Parameter.Timestamp, countCon.getLastUpTime()));
            tempList.addAll(whereParaL);
            countCon.executeStatement(sql, tempList);
        }

        if (HPFW_Connection.HISTORY_ONLY.equals(countCon.getUpdateMode())
                || HPFW_Connection.DIRECT_WITH_HISTORY.equals(countCon.getUpdateMode())) {
            PreparedStatement stmt = null;
            ResultSet rs = null;
            try {
                String sql = "select client_id,recipient_id,ias_application_id from " + thisTableName + " "
                        + whereClause;
                stmt = countCon.getConnectionPtr().prepareStatement(sql);
                CommonDBUtils.setStatement(stmt, whereParaL);
                rs = stmt.executeQuery();

                while (rs.next()) {
                    String histsql = "insert into ";
                    histsql += thisTableName + thisTableHistName;
                    histsql += " (HIST_SEQ,HIST_action,HIST_update_type,HIST_status,HIST_server_id,last_modify_by,last_modify_dt,create_by,create_dt,client_id,recipient_id,ias_application_id,"
                            + selectString;
                    histsql += ") values (?,?,?,?,?,?,?,?,?,?,?,?";
                    for (int i = 0; i < selectStringArray.length; i++)
                        histsql += ",?";
                    histsql += ")";

                    ArrayList<Parameter> histParaList = new ArrayList<Parameter>();
                    histParaList.add(new Parameter(Parameter.String,
                            CommonDBUtils.getSequence(countCon, thisTableName + thisTableHistName)));
                    histParaList.add(new Parameter(Parameter.String, HPFW_Connection.UPDATE));
                    histParaList.add(new Parameter(Parameter.String,
                            countCon.getUpdateMode().equals(HPFW_Connection.HISTORY_ONLY) ? HPFW_Connection.DUAL
                                    : HPFW_Connection.REMOTE));
                    histParaList.add(new Parameter(Parameter.String, HPFW_Connection.PENDING));
                    histParaList.add(new Parameter(Parameter.String, CommonDBUtils.getSERVER_ID()));
                    histParaList.add(new Parameter(Parameter.String, countCon.getLastUpdBy()));
                    histParaList.add(new Parameter(Parameter.Timestamp, countCon.getLastUpTime()));
                    histParaList.add(new Parameter(Parameter.String, countCon.getLastUpdBy()));
                    histParaList.add(new Parameter(Parameter.Timestamp, countCon.getLastUpTime()));
                    // perpare where ...
                    histParaList.add(new Parameter(Parameter.String, rs.getString(1)));
                    histParaList.add(new Parameter(Parameter.String, rs.getString(2)));
                    histParaList.add(new Parameter(Parameter.String, rs.getString(3)));
                    histParaList.addAll(paraL);
                    countCon.executeStatement(histsql, histParaList, true);
                }
            } catch (SQLException e) {
                throw e;
            } finally {
                if (paraL != null) {
                    paraL.clear();
                    paraL = null;
                }
                if (whereParaL != null) {
                    whereParaL.clear();
                    whereParaL = null;
                }

                if (rs != null) {
                    try {
                        rs.close();
                    } catch (Exception ignore) {
                    }
                    rs = null;
                }
                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (Exception ignore) {
                    }
                    stmt = null;
                }
            }
        }
    }

    public void update(HPFW_Connection countCon) throws SQLException {
        if (HPFW_Connection.DIRECT.equals(countCon.getUpdateMode())
                || HPFW_Connection.DIRECT_WITH_HISTORY.equals(countCon.getUpdateMode())) {
            ArrayList<Parameter> paraList = new ArrayList<Parameter>();

            String sql = "update ";
            sql += thisTableName;
            sql += " set ";
            // perpare set ...
            if (dirty_recipientIdType)
                sql += "recipient_id_type = ?,";
            if (dirty_hkidEncrypted)
                sql += "hkid_encrypted = ?,";
            if (dirty_notiId)
                sql += "noti_id = ?,";
            if (dirty_tranId)
                sql += "tran_id = ?,";
            if (dirty_actionTranId)
                sql += "action_tran_id = ?,";
            if (dirty_appRefNum)
                sql += "app_ref_num = ?,";
            if (dirty_iasNotiStatus)
                sql += "ias_noti_status = ?,";
            if (dirty_iasNotiResult)
                sql += "ias_noti_result = ?,";
            if (dirty_txId)
                sql += "tx_id = ?,";
            if (dirty_sentDt)
                sql += "sent_dt = ?,";
            if (dirty_iasDeliveryStatus)
                sql += "ias_delivery_status = ?,";
            if (dirty_appStatus)
                sql += "app_status = ?,";
            if (dirty_appStatusUpdateDate)
                sql += "app_status_update_date = ?,";
            if (dirty_contactNum)
                sql += "contact_num = ?,";
            if (dirty_contactEmail)
                sql += "contact_email = ?,";
            if (dirty_miscInfo)
                sql += "misc_info = ?,";
            if (dirty_operationType)
                sql += "operation_type = ?,";
            if (dirty_deleteInd)
                sql += "delete_ind = ?,";
            if (dirty_iasDeleteNotiStatus)
                sql += "ias_delete_noti_status = ?,";
            if (dirty_iasDeleteNotiResult)
                sql += "ias_delete_noti_result = ?,";
            if (dirty_iasDeleteNotiTxId)
                sql += "ias_delete_noti_tx_id = ?,";
            if (dirty_iasDeleteNotiSentDt)
                sql += "ias_delete_noti_sent_dt = ?,";
            if (dirty_housekeepInd)
                sql += "housekeep_ind = ?,";

            sql += " last_modify_by = ?,last_modify_dt = ? where 1=1 and client_id = ?  and recipient_id = ?  and ias_application_id = ? ";
            // perpare set ...
            if (dirty_recipientIdType)
                paraList.add(new Parameter(Parameter.String, this.recipientIdType));
            if (dirty_hkidEncrypted)
                paraList.add(new Parameter(Parameter.String, this.hkidEncrypted));
            if (dirty_notiId)
                paraList.add(new Parameter(Parameter.String, this.notiId));
            if (dirty_tranId)
                paraList.add(new Parameter(Parameter.String, this.tranId));
            if (dirty_actionTranId)
                paraList.add(new Parameter(Parameter.String, this.actionTranId));
            if (dirty_appRefNum)
                paraList.add(new Parameter(Parameter.String, this.appRefNum));
            if (dirty_iasNotiStatus)
                paraList.add(new Parameter(Parameter.String, this.iasNotiStatus));
            if (dirty_iasNotiResult)
                paraList.add(new Parameter(Parameter.String, this.iasNotiResult));
            if (dirty_txId)
                paraList.add(new Parameter(Parameter.String, this.txId));
            if (dirty_sentDt)
                paraList.add(new Parameter(Parameter.Timestamp, this.sentDt));
            if (dirty_iasDeliveryStatus)
                paraList.add(new Parameter(Parameter.String, this.iasDeliveryStatus));
            if (dirty_appStatus)
                paraList.add(new Parameter(Parameter.String, this.appStatus));
            if (dirty_appStatusUpdateDate)
                paraList.add(new Parameter(Parameter.Timestamp, this.appStatusUpdateDate));
            if (dirty_contactNum)
                paraList.add(new Parameter(Parameter.String, this.contactNum));
            if (dirty_contactEmail)
                paraList.add(new Parameter(Parameter.String, this.contactEmail));
            if (dirty_operationType)
                paraList.add(new Parameter(Parameter.String, this.operationType));
            if (dirty_deleteInd)
                paraList.add(new Parameter(Parameter.String, this.deleteInd));
            if (dirty_iasDeleteNotiStatus)
                paraList.add(new Parameter(Parameter.String, this.iasDeleteNotiStatus));
            if (dirty_iasDeleteNotiResult)
                paraList.add(new Parameter(Parameter.String, this.iasDeleteNotiResult));
            if (dirty_iasDeleteNotiTxId)
                paraList.add(new Parameter(Parameter.String, this.iasDeleteNotiTxId));
            if (dirty_iasDeleteNotiSentDt)
                paraList.add(new Parameter(Parameter.Timestamp, this.iasDeleteNotiSentDt));
            if (dirty_housekeepInd)
                paraList.add(new Parameter(Parameter.String, this.housekeepInd));
            paraList.add(new Parameter(Parameter.String, countCon.getLastUpdBy()));
            paraList.add(new Parameter(Parameter.Timestamp, countCon.getLastUpTime()));
            // perpare where ...
            paraList.add(new Parameter(Parameter.String, this.clientId));
            paraList.add(new Parameter(Parameter.String, this.recipientId));
            paraList.add(new Parameter(Parameter.String, this.iasApplicationId));
            countCon.executeStatement(sql, paraList);
        }

        if (HPFW_Connection.HISTORY_ONLY.equals(countCon.getUpdateMode())
                || HPFW_Connection.DIRECT_WITH_HISTORY.equals(countCon.getUpdateMode())) {
            ArrayList<Parameter> histParaList = new ArrayList<Parameter>();

            String hist_sql = "insert into ";
            hist_sql += thisTableName + thisTableHistName;
            hist_sql += " (HIST_SEQ,HIST_action,HIST_update_type,HIST_status,HIST_server_id,";

            // perpare set ...
            hist_sql += "client_id,";
            hist_sql += "recipient_id,";
            hist_sql += "ias_application_id,";
            if (dirty_recipientIdType)
                hist_sql += "recipient_id_type,";
            if (dirty_hkidEncrypted)
                hist_sql += "hkid_encrypted,";
            if (dirty_notiId)
                hist_sql += "noti_id,";
            if (dirty_tranId)
                hist_sql += "tran_id,";
            if (dirty_actionTranId)
                hist_sql += "action_tran_id,";
            if (dirty_appRefNum)
                hist_sql += "app_ref_num,";
            if (dirty_iasNotiStatus)
                hist_sql += "ias_noti_status,";
            if (dirty_iasNotiResult)
                hist_sql += "ias_noti_result,";
            if (dirty_txId)
                hist_sql += "tx_id,";
            if (dirty_sentDt)
                hist_sql += "sent_dt,";
            if (dirty_iasDeliveryStatus)
                hist_sql += "ias_delivery_status,";
            if (dirty_appStatus)
                hist_sql += "app_status,";
            if (dirty_appStatusUpdateDate)
                hist_sql += "app_status_update_date,";
            if (dirty_contactNum)
                hist_sql += "contact_num,";
            if (dirty_contactEmail)
                hist_sql += "contact_email,";
            if (dirty_miscInfo)
                hist_sql += "misc_info,";
            if (dirty_operationType)
                hist_sql += "operation_type,";
            if (dirty_deleteInd)
                hist_sql += "delete_ind,";
            if (dirty_iasDeleteNotiStatus)
                hist_sql += "ias_delete_noti_status,";
            if (dirty_iasDeleteNotiResult)
                hist_sql += "ias_delete_noti_result,";
            if (dirty_iasDeleteNotiTxId)
                hist_sql += "ias_delete_noti_tx_id,";
            if (dirty_iasDeleteNotiSentDt)
                hist_sql += "ias_delete_noti_sent_dt,";
            if (dirty_housekeepInd)
                hist_sql += "housekeep_ind,";
            hist_sql += "last_modify_by,last_modify_dt,create_by,create_dt) values (?,?,?,?,?,";

            // perpare set ...
            hist_sql += "?,";
            hist_sql += "?,";
            hist_sql += "?,";
            if (dirty_recipientIdType)
                hist_sql += "?,";
            if (dirty_hkidEncrypted)
                hist_sql += "?,";
            if (dirty_notiId)
                hist_sql += "?,";
            if (dirty_tranId)
                hist_sql += "?,";
            if (dirty_actionTranId)
                hist_sql += "?,";
            if (dirty_appRefNum)
                hist_sql += "?,";
            if (dirty_iasNotiStatus)
                hist_sql += "?,";
            if (dirty_iasNotiResult)
                hist_sql += "?,";
            if (dirty_txId)
                hist_sql += "?,";
            if (dirty_sentDt)
                hist_sql += "?,";
            if (dirty_iasDeliveryStatus)
                hist_sql += "?,";
            if (dirty_appStatus)
                hist_sql += "?,";
            if (dirty_appStatusUpdateDate)
                hist_sql += "?,";
            if (dirty_contactNum)
                hist_sql += "?,";
            if (dirty_contactEmail)
                hist_sql += "?,";
            if (dirty_miscInfo)
                hist_sql += "?,";
            if (dirty_operationType)
                hist_sql += "?,";
            if (dirty_deleteInd)
                hist_sql += "?,";
            if (dirty_iasDeleteNotiStatus)
                hist_sql += "?,";
            if (dirty_iasDeleteNotiResult)
                hist_sql += "?,";
            if (dirty_iasDeleteNotiTxId)
                hist_sql += "?,";
            if (dirty_iasDeleteNotiSentDt)
                hist_sql += "?,";
            if (dirty_housekeepInd)
                hist_sql += "?,";
            hist_sql += "?,?,?,?) ";
            histParaList.add(new Parameter(Parameter.String,
                    CommonDBUtils.getSequence(countCon, thisTableName + thisTableHistName)));
            histParaList.add(new Parameter(Parameter.String, HPFW_Connection.UPDATE));
            histParaList.add(new Parameter(Parameter.String,
                    countCon.getUpdateMode().equals(HPFW_Connection.HISTORY_ONLY) ? HPFW_Connection.DUAL
                            : HPFW_Connection.REMOTE));
            histParaList.add(new Parameter(Parameter.String, HPFW_Connection.PENDING));
            histParaList.add(new Parameter(Parameter.String, CommonDBUtils.getSERVER_ID()));

            // perpare set ...
            histParaList.add(new Parameter(Parameter.String, this.clientId));
            histParaList.add(new Parameter(Parameter.String, this.recipientId));
            histParaList.add(new Parameter(Parameter.String, this.iasApplicationId));
            if (dirty_recipientIdType)
                histParaList.add(new Parameter(Parameter.String, this.recipientIdType));
            if (dirty_hkidEncrypted)
                histParaList.add(new Parameter(Parameter.String, this.hkidEncrypted));
            if (dirty_notiId)
                histParaList.add(new Parameter(Parameter.String, this.notiId));
            if (dirty_tranId)
                histParaList.add(new Parameter(Parameter.String, this.tranId));
            if (dirty_actionTranId)
                histParaList.add(new Parameter(Parameter.String, this.actionTranId));
            if (dirty_appRefNum)
                histParaList.add(new Parameter(Parameter.String, this.appRefNum));
            if (dirty_iasNotiStatus)
                histParaList.add(new Parameter(Parameter.String, this.iasNotiStatus));
            if (dirty_iasNotiResult)
                histParaList.add(new Parameter(Parameter.String, this.iasNotiResult));
            if (dirty_txId)
                histParaList.add(new Parameter(Parameter.String, this.txId));
            if (dirty_sentDt)
                histParaList.add(new Parameter(Parameter.Timestamp, this.sentDt));
            if (dirty_iasDeliveryStatus)
                histParaList.add(new Parameter(Parameter.String, this.iasDeliveryStatus));
            if (dirty_appStatus)
                histParaList.add(new Parameter(Parameter.String, this.appStatus));
            if (dirty_appStatusUpdateDate)
                histParaList.add(new Parameter(Parameter.Timestamp, this.appStatusUpdateDate));
            if (dirty_contactNum)
                histParaList.add(new Parameter(Parameter.String, this.contactNum));
            if (dirty_contactEmail)
                histParaList.add(new Parameter(Parameter.String, this.contactEmail));
            if (dirty_miscInfo)
                histParaList.add(new Parameter(Parameter.String, this.miscInfo));
            if (dirty_operationType)
                histParaList.add(new Parameter(Parameter.String, this.operationType));
            if (dirty_deleteInd)
                histParaList.add(new Parameter(Parameter.String, this.deleteInd));
            if (dirty_iasDeleteNotiStatus)
                histParaList.add(new Parameter(Parameter.String, this.iasDeleteNotiStatus));
            if (dirty_iasDeleteNotiResult)
                histParaList.add(new Parameter(Parameter.String, this.iasDeleteNotiResult));
            if (dirty_iasDeleteNotiTxId)
                histParaList.add(new Parameter(Parameter.String, this.iasDeleteNotiTxId));
            if (dirty_iasDeleteNotiSentDt)
                histParaList.add(new Parameter(Parameter.Timestamp, this.iasDeleteNotiSentDt));
            if (dirty_housekeepInd)
                histParaList.add(new Parameter(Parameter.String, this.housekeepInd));
            histParaList.add(new Parameter(Parameter.String, countCon.getLastUpdBy()));
            histParaList.add(new Parameter(Parameter.Timestamp, countCon.getLastUpTime()));
            histParaList.add(new Parameter(Parameter.String, countCon.getLastUpdBy()));
            histParaList.add(new Parameter(Parameter.Timestamp, countCon.getLastUpTime()));
            countCon.executeStatement(hist_sql, histParaList, true);
        }
        this.forUpdate = false;
    }

    public static ArrayList<IasUserApplication_> getResultList(HPFW_Connection countCon, String whereCluase,
            ArrayList<Parameter> paraL) throws SQLException {
        boolean needClose = countCon == null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<IasUserApplication_> result = new ArrayList<IasUserApplication_>();
        String sql = "select " + thisTableName + ".* from ";
        try {
            sql += thisTableName + " ";
            sql += whereCluase;
            if (countCon == null)
                countCon = HPFW_Connection.getHPFW_Connection();
            stmt = countCon.getConnectionPtr().prepareStatement(sql);
            if (paraL != null)
                CommonDBUtils.setStatement(stmt, paraL);
            rs = stmt.executeQuery();

            while (rs.next()) {
                IasUserApplication_ obj = new IasUserApplication_();
                obj.clientId = rs.getString("client_id"); // String
                obj.recipientId = rs.getString("recipient_id"); // String
                obj.iasApplicationId = rs.getString("ias_application_id"); // String
                obj.recipientIdType = rs.getString("recipient_id_type"); // String
                obj.hkidEncrypted = rs.getString("hkid_encrypted"); // String
                obj.notiId = rs.getString("noti_id"); // String
                obj.tranId = rs.getString("tran_id"); // String
                obj.actionTranId = rs.getString("action_tran_id"); // String
                obj.appRefNum = rs.getString("app_ref_num"); // String
                obj.iasNotiStatus = rs.getString("ias_noti_status"); // String
                obj.iasNotiResult = rs.getString("ias_noti_result"); // String
                obj.txId = rs.getString("tx_id"); // String
                obj.sentDt = rs.getTimestamp("sent_dt"); // Timestamp
                obj.iasDeliveryStatus = rs.getString("ias_delivery_status"); // String
                obj.appStatus = rs.getString("app_status"); // String
                obj.appStatusUpdateDate = rs.getTimestamp("app_status_update_date"); // Timestamp
                obj.contactNum = rs.getString("contact_num"); // String
                obj.contactEmail = rs.getString("contact_email"); // String
                obj.miscInfo = rs.getString("misc_info"); // String
                obj.operationType = rs.getString("operation_type"); // String
                obj.deleteInd = rs.getString("delete_ind"); // String
                obj.iasDeleteNotiStatus = rs.getString("ias_delete_noti_status"); // String
                obj.iasDeleteNotiResult = rs.getString("ias_delete_noti_result"); // String
                obj.iasDeleteNotiTxId = rs.getString("ias_delete_noti_tx_id"); // String
                obj.iasDeleteNotiSentDt = rs.getTimestamp("ias_delete_noti_sent_dt"); // Timestamp
                obj.housekeepInd = rs.getString("housekeep_ind"); // String
                obj.createDt = rs.getTimestamp("create_dt"); // Timestamp
                obj.lastModifyDt = rs.getTimestamp("last_modify_dt"); // Timestamp
                obj.createBy = rs.getString("create_by"); // String
                obj.lastModifyBy = rs.getString("last_modify_by"); // String
                obj.initialized = true;
                result.add(obj);
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception ignore) {
                }
                rs = null;
            }

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception ignore) {
                }
                stmt = null;
            }

            if (needClose && countCon != null) {
                try {
                    HPFW_Connection.close(countCon);
                    countCon = null;
                } catch (Exception ignore) {
                }
            }
        }
        return result;
    }

    public void init(HPFW_Connection countCon, final String clientId,
            final String recipientId, final String iasApplicationId, boolean forUpdate)
            throws SQLException, NullPointerException {
        this.forUpdate = forUpdate;
        boolean needClose = countCon == null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql = "select * from " + thisTableName
                    + " where 1=1 and client_id = ?  and recipient_id = ?  and ias_application_id = ? ";
            if (forUpdate)
                sql += "for update";
            if (countCon == null)
                countCon = HPFW_Connection.getHPFW_Connection();
            stmt = countCon.getConnectionPtr().prepareStatement(sql);
            stmt.setString(1, clientId);
            stmt.setString(2, recipientId);
            stmt.setString(3, iasApplicationId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                this.clientId = rs.getString("client_id"); // String
                this.recipientId = rs.getString("recipient_id"); // String
                this.iasApplicationId = rs.getString("ias_application_id"); // String
                this.recipientIdType = rs.getString("recipient_id_type"); // String
                this.hkidEncrypted = rs.getString("hkid_encrypted"); // String
                this.notiId = rs.getString("noti_id"); // String
                this.tranId = rs.getString("tran_id"); // String
                this.actionTranId = rs.getString("action_tran_id"); // String
                this.appRefNum = rs.getString("app_ref_num"); // String
                this.iasNotiStatus = rs.getString("ias_noti_status"); // String
                this.iasNotiResult = rs.getString("ias_noti_result"); // String
                this.txId = rs.getString("tx_id"); // String
                this.sentDt = rs.getTimestamp("sent_dt"); // Timestamp
                this.iasDeliveryStatus = rs.getString("ias_delivery_status"); // String
                this.appStatus = rs.getString("app_status"); // String
                this.appStatusUpdateDate = rs.getTimestamp("app_status_update_date"); // Timestamp
                this.contactNum = rs.getString("contact_num"); // String
                this.contactEmail = rs.getString("contact_email"); // String
                this.miscInfo = rs.getString("misc_info"); // String
                this.operationType = rs.getString("operation_type"); // String
                this.deleteInd = rs.getString("delete_ind"); // String
                this.iasDeleteNotiStatus = rs.getString("ias_delete_noti_status"); // String
                this.iasDeleteNotiResult = rs.getString("ias_delete_noti_result"); // String
                this.iasDeleteNotiTxId = rs.getString("ias_delete_noti_tx_id"); // String
                this.iasDeleteNotiSentDt = rs.getTimestamp("ias_delete_noti_sent_dt"); // Timestamp
                this.housekeepInd = rs.getString("housekeep_ind"); // String
                this.createDt = rs.getTimestamp("create_dt"); // Timestamp
                this.lastModifyDt = rs.getTimestamp("last_modify_dt"); // Timestamp
                this.createBy = rs.getString("create_by"); // String
                this.lastModifyBy = rs.getString("last_modify_by"); // String
                this.initialized = true;
            } else {
                throw new java.lang.NullPointerException();
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception ignore) {
                }
                rs = null;
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception ignore) {
                }
                stmt = null;
            }
            if (needClose && countCon != null) {
                try {
                    HPFW_Connection.close(countCon);
                    countCon = null;
                } catch (Exception ignore) {
                }
            }
        }
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
        this.dirty_clientId = true;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
        this.dirty_recipientId = true;
    }

    public String getIasApplicationId() {
        return iasApplicationId;
    }

    public void setIasApplicationId(String iasApplicationId) {
        this.iasApplicationId = iasApplicationId;
        this.dirty_iasApplicationId = true;
    }

    public String getRecipientIdType() {
        return recipientIdType;
    }

    public void setRecipientIdType(String recipientIdType) {
        this.recipientIdType = recipientIdType;
        this.dirty_recipientIdType = true;
    }

    public String getHkidEncrypted() {
        return hkidEncrypted;
    }

    public void setHkidEncrypted(String hkidEncrypted) {
        this.hkidEncrypted = hkidEncrypted;
    }

    public String getNotiId() {
        return notiId;
    }

    public void setNotiId(String notiId) {
        this.notiId = notiId;
        this.dirty_notiId = true;
    }

    public String getTranId() {
        return tranId;
    }

    public void setTranId(String tranId) {
        this.tranId = tranId;
        this.dirty_tranId = true;
    }

    public String getActionTranId() {
        return actionTranId;
    }

    public void setActionTranId(String actionTranId) {
        this.actionTranId = actionTranId;
        this.dirty_actionTranId = true;
    }

    public String getAppRefNum() {
        return appRefNum;
    }

    public void setAppRefNum(String appRefNum) {
        this.appRefNum = appRefNum;
        this.dirty_appRefNum = true;
    }

    public String getIasNotiStatus() {
        return iasNotiStatus;
    }

    public void setIasNotiStatus(String iasNotiStatus) {
        this.iasNotiStatus = iasNotiStatus;
        this.dirty_iasNotiStatus = true;
    }

    public String getIasNotiResult() {
        return iasNotiResult;
    }

    public void setIasNotiResult(String iasNotiResult) {
        this.iasNotiResult = iasNotiResult;
        this.dirty_iasNotiResult = true;
    }

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
        this.dirty_txId = true;
    }

    public Timestamp getSentDt() {
        return sentDt;
    }

    public void setSentDt(Timestamp sentDt) {
        this.sentDt = sentDt;
        this.dirty_sentDt = true;
    }

    public String getIasDeliveryStatus() {
        return iasDeliveryStatus;
    }

    public void setIasDeliveryStatus(String iasDeliveryStatus) {
        this.iasDeliveryStatus = iasDeliveryStatus;
        this.dirty_iasDeliveryStatus = true;
    }

    public String getAppStatus() {
        return appStatus;
    }

    public void setAppStatus(String appStatus) {
        this.appStatus = appStatus;
        this.dirty_appStatus = true;
    }

    public Timestamp getAppStatusUpdateDate() {
        return appStatusUpdateDate;
    }

    public void setAppStatusUpdateDate(Timestamp appStatusUpdateDate) {
        this.appStatusUpdateDate = appStatusUpdateDate;
        this.dirty_appStatusUpdateDate = true;
    }

    public String getContactNum() {
        return contactNum;
    }

    public void setContactNum(String contactNum) {
        this.contactNum = contactNum;
        this.dirty_contactNum = true;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
        this.dirty_contactEmail = true;
    }

    public String getMiscInfo() {
        return miscInfo;
    }

    public void setMiscInfo(String miscInfo) {
        this.miscInfo = miscInfo;
        this.dirty_miscInfo = true;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
        this.dirty_operationType = true;
    }

    public String getDeleteInd() {
        return deleteInd;
    }

    public void setDeleteInd(String deleteInd) {
        this.deleteInd = deleteInd;
        this.dirty_deleteInd = true;
    }

    public String getIasDeleteNotiStatus() {
        return iasDeleteNotiStatus;
    }

    public void setIasDeleteNotiStatus(String iasDeleteNotiStatus) {
        this.iasDeleteNotiStatus = iasDeleteNotiStatus;
        this.dirty_iasDeleteNotiStatus = true;
    }

    public String getIasDeleteNotiResult() {
        return iasDeleteNotiResult;
    }

    public void setIasDeleteNotiResult(String iasDeleteNotiResult) {
        this.iasDeleteNotiResult = iasDeleteNotiResult;
        this.dirty_iasDeleteNotiResult = true;
    }

    public String getIasDeleteNotiTxId() {
        return iasDeleteNotiTxId;
    }

    public void setIasDeleteNotiTxId(String iasDeleteNotiTxId) {
        this.iasDeleteNotiTxId = iasDeleteNotiTxId;
        this.dirty_iasDeleteNotiTxId = true;
    }

    public Timestamp getIasDeleteNotiSentDt() {
        return iasDeleteNotiSentDt;
    }

    public void setIasDeleteNotiSentDt(Timestamp iasDeleteNotiSentDt) {
        this.iasDeleteNotiSentDt = iasDeleteNotiSentDt;
        this.dirty_iasDeleteNotiSentDt = true;
    }

    public String getHousekeepInd() {
        return housekeepInd;
    }

    public void setHousekeepInd(String housekeepInd) {
        this.housekeepInd = housekeepInd;
        this.dirty_housekeepInd = true;
    }

    public Timestamp getCreateDt() {
        return createDt;
    }

    public void setCreateDt(Timestamp createDt) {
        this.createDt = createDt;
        this.dirty_createDt = true;
    }

    public Timestamp getLastModifyDt() {
        return lastModifyDt;
    }

    public void setLastModifyDt(Timestamp lastModifyDt) {
        this.lastModifyDt = lastModifyDt;
        this.dirty_lastModifyDt = true;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
        this.dirty_createBy = true;
    }

    public String getLastModifyBy() {
        return lastModifyBy;
    }

    public void setLastModifyBy(String lastModifyBy) {
        this.lastModifyBy = lastModifyBy;
        this.dirty_lastModifyBy = true;
    }
}
