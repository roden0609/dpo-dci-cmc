package hk.gov.cmc.persistence.maintainmessage.message;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.lang.Long;
import java.lang.String;
import java.lang.NullPointerException;
import java.util.ArrayList;

import hk.gov.cmc.persistence.connection.hpfw.CommonDBUtils;
import hk.gov.cmc.persistence.connection.hpfw.HPFW_Connection;
import hk.gov.cmc.persistence.connection.hpfw.Parameter;

public class IasUserMessage_ implements Serializable {
    private static final long serialVersionUID = 1L;
    private boolean initialized = false;

    private final static String thisTableName = "IAS_USER_MESSAGE";
    private final static String thisTableHistName = "_H";
    private boolean forUpdate = false;

    private String clientId = null;
    private String openId = null;
    private String iasMsgId = null;
    private String notiId = null;
    private String tranId = null;
    private String readInd = null;
    private String iasNotiStatus = null;
    private String iasNotiResult = null;
    private String txId = null;
    private Timestamp sentDt = null;
    private String iasDeliveryStatus = null;
    private String housekeepInd = null;
    private String deleteInd = null;
    private Timestamp createDt = null;
    private Timestamp lastModifyDt = null;
    private String createBy = null;
    private String lastModifyBy = null;

    private boolean dirty_clientId = false;
    private boolean dirty_openId = false;
    private boolean dirty_iasMsgId = false;
    private boolean dirty_notiId = false;
    private boolean dirty_tranId = false;
    private boolean dirty_readInd = false;
    private boolean dirty_iasNotiStatus = false;
    private boolean dirty_iasNotiResult = false;
    private boolean dirty_txId = false;
    private boolean dirty_sentDt = false;
    private boolean dirty_iasDeliveryStatus = false;
    private boolean dirty_housekeepInd = false;
    private boolean dirty_deleteInd = false;
    private boolean dirty_createDt = false;
    private boolean dirty_lastModifyDt = false;
    private boolean dirty_createBy = false;
    private boolean dirty_lastModifyBy = false;

    /**
     * IasUserMessage_ Contructor
     */
    public IasUserMessage_() {
        super();
    }

    /**
     * IasUserMessage_ Constructor with specify PK
     */
    public IasUserMessage_(HPFW_Connection countCon, String inclientId, String inopenId, String iniasMsgId)
            throws SQLException, NullPointerException {
        this();
        init(countCon, inclientId, inopenId, iniasMsgId, false);
    }

    public IasUserMessage_(HPFW_Connection countCon, String inclientId, String inopenId, String iniasMsgId,
            boolean forUpdate) throws SQLException, NullPointerException {
        this();
        init(countCon, inclientId, inopenId, iniasMsgId, forUpdate);
    }

    static public void delete(HPFW_Connection countCon, String whereCluase, ArrayList<Parameter> paraL)
            throws SQLException {
        if (HPFW_Connection.DIRECT.equals(countCon.getUpdateMode())
                || HPFW_Connection.DIRECT_WITH_HISTORY.equals(countCon.getUpdateMode())) {
            String sql = "delete from ";
            sql += thisTableName + " ";
            sql += whereCluase;
            countCon.executeStatement(sql, paraL);
        }

        if (HPFW_Connection.HISTORY_ONLY.equals(countCon.getUpdateMode())
                || HPFW_Connection.DIRECT_WITH_HISTORY.equals(countCon.getUpdateMode())) {
            PreparedStatement stmt = null;
            ResultSet rs = null;

            try {
                String sql = "select client_id,open_id,ias_msg_id from " + thisTableName + " " + whereCluase;
                stmt = countCon.getConnectionPtr().prepareStatement(sql);
                CommonDBUtils.setStatement(stmt, paraL);
                rs = stmt.executeQuery();

                while (rs.next()) {
                    String histsql = "insert into ";
                    histsql += thisTableName + thisTableHistName;
                    histsql += " (HIST_SEQ,HIST_action,HIST_update_type,HIST_status,HIST_server_id,last_modify_by,last_modify_dt,create_by,create_dt,client_id,open_id,ias_msg_id";
                    histsql += ") values (?,?,?,?,?,?,?,?,?,?";
                    // perpare where ...
                    histsql += ",?";
                    histsql += ",?";
                    histsql += ",?";
                    histsql += ")";
                    ArrayList<Parameter> histParaList = new ArrayList<Parameter>();
                    histParaList.add(
                            new Parameter(Parameter.String,
                                    CommonDBUtils.getSequence(countCon, thisTableName + thisTableHistName)));
                    histParaList.add(new Parameter(Parameter.String, HPFW_Connection.DELETE));
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
                    countCon.executeStatement(histsql, histParaList, true);
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
            }
        }
    }

    public void delete(HPFW_Connection countCon) throws SQLException {
        if (HPFW_Connection.DIRECT.equals(countCon.getUpdateMode())
                || HPFW_Connection.DIRECT_WITH_HISTORY.equals(countCon.getUpdateMode())) {
            String sql = "delete from ";
            sql += thisTableName;
            sql += " where 1=1 and client_id = ?  and open_id = ?  and ias_msg_id = ? ";

            ArrayList<Parameter> paraList = new ArrayList<Parameter>();

            // perpare where ...
            paraList.add(new Parameter(Parameter.String, this.clientId));
            paraList.add(new Parameter(Parameter.String, this.openId));
            paraList.add(new Parameter(Parameter.String, this.iasMsgId));
            countCon.executeStatement(sql, paraList);
        }

        if (HPFW_Connection.HISTORY_ONLY.equals(countCon.getUpdateMode())
                || HPFW_Connection.DIRECT_WITH_HISTORY.equals(countCon.getUpdateMode())) {
            String histsql = "insert into ";
            histsql += thisTableName + thisTableHistName;
            histsql += " (HIST_SEQ,HIST_action,HIST_update_type,HIST_status,HIST_server_id,last_modify_by,last_modify_dt,create_by,create_dt";
            histsql += ",ClientId";
            histsql += ",OpenId";
            histsql += ",IasMsgId";
            histsql += ") values (?,?,?,?,?,?,?,?,?";
            // perpare where ...
            histsql += ",?";
            histsql += ",?";
            histsql += ",?";
            histsql += ")";
            ArrayList<Parameter> histParaList = new ArrayList<Parameter>();
            histParaList
                    .add(new Parameter(Parameter.String,
                            CommonDBUtils.getSequence(countCon, thisTableName + thisTableHistName)));
            histParaList.add(new Parameter(Parameter.String, HPFW_Connection.DELETE));
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
            histParaList.add(new Parameter(Parameter.String, this.clientId));
            histParaList.add(new Parameter(Parameter.String, this.openId));
            histParaList.add(new Parameter(Parameter.String, this.iasMsgId));
            countCon.executeStatement(histsql, histParaList, true);
        }
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
            if (openId != null)
                sql += "open_id,";
            if (iasMsgId != null)
                sql += "ias_msg_id,";
            if (notiId != null)
                sql += "noti_id,";
            if (tranId != null)
                sql += "tran_id,";
            if (readInd != null)
                sql += "read_ind,";
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
            if (housekeepInd != null)
                sql += "housekeep_ind,";
            if (deleteInd != null)
                sql += "delete_ind,";
            sql += "create_by,last_modify_by,create_dt,last_modify_dt) values (";

            // perpare set ...
            if (clientId != null)
                sql += "?,";
            if (openId != null)
                sql += "?,";
            if (iasMsgId != null)
                sql += "?,";
            if (notiId != null)
                sql += "?,";
            if (tranId != null)
                sql += "?,";
            if (readInd != null)
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
            if (housekeepInd != null)
                sql += "?,";
            if (deleteInd != null)
                sql += "?,";
            sql += "?,?,?,?) ";

            // perpare set ...
            if (clientId != null)
                paraList.add(new Parameter(Parameter.String, this.clientId));
            if (openId != null)
                paraList.add(new Parameter(Parameter.String, this.openId));
            if (iasMsgId != null)
                paraList.add(new Parameter(Parameter.String, this.iasMsgId));
            if (notiId != null)
                paraList.add(new Parameter(Parameter.String, this.notiId));
            if (tranId != null)
                paraList.add(new Parameter(Parameter.String, this.tranId));
            if (readInd != null)
                paraList.add(new Parameter(Parameter.String, this.readInd));
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
            if (housekeepInd != null)
                paraList.add(new Parameter(Parameter.String, this.housekeepInd));
            if (deleteInd != null)
                paraList.add(new Parameter(Parameter.String, this.deleteInd));
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
            if (openId != null)
                hist_sql += "open_id,";
            if (iasMsgId != null)
                hist_sql += "ias_msg_id,";
            if (notiId != null)
                hist_sql += "noti_id,";
            if (tranId != null)
                hist_sql += "tran_id,";
            if (readInd != null)
                hist_sql += "read_ind,";
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
            if (housekeepInd != null)
                hist_sql += "housekeep_ind,";
            if (deleteInd != null)
                hist_sql += "delete_ind,";
            hist_sql += "create_by,last_modify_by,create_dt,last_modify_dt) values (?,?,?,?,?,";

            // perpare set ...
            if (clientId != null)
                hist_sql += "?,";
            if (openId != null)
                hist_sql += "?,";
            if (iasMsgId != null)
                hist_sql += "?,";
            if (notiId != null)
                hist_sql += "?,";
            if (tranId != null)
                hist_sql += "?,";
            if (readInd != null)
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
            if (housekeepInd != null)
                hist_sql += "?,";
            if (deleteInd != null)
                hist_sql += "?,";
            hist_sql += "?,?,?,?) ";
            histParaList
                    .add(new Parameter(Parameter.String,
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
            if (openId != null)
                histParaList.add(new Parameter(Parameter.String, this.openId));
            if (iasMsgId != null)
                histParaList.add(new Parameter(Parameter.String, this.iasMsgId));
            if (notiId != null)
                histParaList.add(new Parameter(Parameter.String, this.notiId));
            if (tranId != null)
                histParaList.add(new Parameter(Parameter.String, this.tranId));
            if (readInd != null)
                histParaList.add(new Parameter(Parameter.String, this.readInd));
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
            if (housekeepInd != null)
                histParaList.add(new Parameter(Parameter.String, this.housekeepInd));
            if (deleteInd != null)
                histParaList.add(new Parameter(Parameter.String, this.deleteInd));
            histParaList.add(new Parameter(Parameter.String, countCon.getLastUpdBy()));
            histParaList.add(new Parameter(Parameter.String, countCon.getLastUpdBy()));
            histParaList.add(new Parameter(Parameter.Timestamp, countCon.getLastUpTime()));
            histParaList.add(new Parameter(Parameter.Timestamp, countCon.getLastUpTime()));
            countCon.executeStatement(hist_sql, histParaList, false);
        }
    }

    public static void update(HPFW_Connection countCon, String setClause, ArrayList<Parameter> paraL,
            String whereClause,
            ArrayList<Parameter> whereParaL)
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
                String sql = "select client_id,open_id,ias_msg_id from " + thisTableName + " " + whereClause;
                stmt = countCon.getConnectionPtr().prepareStatement(sql);
                CommonDBUtils.setStatement(stmt, whereParaL);
                rs = stmt.executeQuery();

                while (rs.next()) {
                    String histsql = "insert into ";
                    histsql += thisTableName + thisTableHistName;
                    histsql += " (HIST_SEQ,HIST_action,HIST_update_type,HIST_status,HIST_server_id,last_modify_by,last_modify_dt,create_by,create_dt,client_id,open_id,ias_msg_id,"
                            + selectString;
                    histsql += ") values (?,?,?,?,?,?,?,?,?,?,?,?";
                    for (int i = 0; i < selectStringArray.length; i++)
                        histsql += ",?";
                    histsql += ")";

                    ArrayList<Parameter> histParaList = new ArrayList<Parameter>();
                    histParaList.add(
                            new Parameter(Parameter.String,
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
            if (dirty_notiId)
                sql += "noti_id = ?,";
            if (dirty_tranId)
                sql += "tran_id = ?,";
            if (dirty_readInd)
                sql += "read_ind = ?,";
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
            if (dirty_housekeepInd)
                sql += "housekeep_ind = ?,";
            if (dirty_deleteInd)
                sql += "delete_ind = ?,";

            sql += " last_modify_by = ?,last_modify_dt = ? where 1=1 and client_id = ?  and open_id = ?  and ias_msg_id = ? ";
            // perpare set ...
            if (dirty_notiId)
                paraList.add(new Parameter(Parameter.String, this.notiId));
            if (dirty_tranId)
                paraList.add(new Parameter(Parameter.String, this.tranId));
            if (dirty_readInd)
                paraList.add(new Parameter(Parameter.String, this.readInd));
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
            if (dirty_housekeepInd)
                paraList.add(new Parameter(Parameter.String, this.housekeepInd));
            if (dirty_deleteInd)
                paraList.add(new Parameter(Parameter.String, this.deleteInd));
            paraList.add(new Parameter(Parameter.String, countCon.getLastUpdBy()));
            paraList.add(new Parameter(Parameter.Timestamp, countCon.getLastUpTime()));
            // perpare where ...
            paraList.add(new Parameter(Parameter.String, this.clientId));
            paraList.add(new Parameter(Parameter.String, this.openId));
            paraList.add(new Parameter(Parameter.String, this.iasMsgId));
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
            hist_sql += "open_id,";
            hist_sql += "ias_msg_id,";
            if (dirty_notiId)
                hist_sql += "noti_id,";
            if (dirty_tranId)
                hist_sql += "tran_id,";
            if (dirty_readInd)
                hist_sql += "read_ind,";
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
            if (dirty_housekeepInd)
                hist_sql += "housekeep_ind,";
            if (dirty_deleteInd)
                hist_sql += "delete_ind,";
            hist_sql += "last_modify_by,last_modify_dt,create_by,create_dt) values (?,?,?,?,?,";

            // perpare set ...
            hist_sql += "?,";
            hist_sql += "?,";
            hist_sql += "?,";
            if (dirty_notiId)
                hist_sql += "?,";
            if (dirty_tranId)
                hist_sql += "?,";
            if (dirty_readInd)
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
            if (dirty_housekeepInd)
                hist_sql += "?,";
            if (dirty_deleteInd)
                hist_sql += "?,";
            hist_sql += "?,?,?,?) ";
            histParaList
                    .add(new Parameter(Parameter.String,
                            CommonDBUtils.getSequence(countCon, thisTableName + thisTableHistName)));
            histParaList.add(new Parameter(Parameter.String, HPFW_Connection.UPDATE));
            histParaList.add(new Parameter(Parameter.String,
                    countCon.getUpdateMode().equals(HPFW_Connection.HISTORY_ONLY) ? HPFW_Connection.DUAL
                            : HPFW_Connection.REMOTE));
            histParaList.add(new Parameter(Parameter.String, HPFW_Connection.PENDING));
            histParaList.add(new Parameter(Parameter.String, CommonDBUtils.getSERVER_ID()));

            // perpare set ...
            histParaList.add(new Parameter(Parameter.String, this.clientId));
            histParaList.add(new Parameter(Parameter.String, this.openId));
            histParaList.add(new Parameter(Parameter.String, this.iasMsgId));
            if (dirty_notiId)
                histParaList.add(new Parameter(Parameter.String, this.notiId));
            if (dirty_tranId)
                histParaList.add(new Parameter(Parameter.String, this.tranId));
            if (dirty_readInd)
                histParaList.add(new Parameter(Parameter.String, this.readInd));
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
            if (dirty_housekeepInd)
                histParaList.add(new Parameter(Parameter.String, this.housekeepInd));
            if (dirty_deleteInd)
                histParaList.add(new Parameter(Parameter.String, this.deleteInd));
            histParaList.add(new Parameter(Parameter.String, countCon.getLastUpdBy()));
            histParaList.add(new Parameter(Parameter.Timestamp, countCon.getLastUpTime()));
            histParaList.add(new Parameter(Parameter.String, countCon.getLastUpdBy()));
            histParaList.add(new Parameter(Parameter.Timestamp, countCon.getLastUpTime()));
            countCon.executeStatement(hist_sql, histParaList, true);
        }
        this.forUpdate = false;
    }

    public static ArrayList<IasUserMessage_> getResultList(HPFW_Connection countCon, String whereCluase,
            ArrayList<Parameter> paraL) throws SQLException {
        boolean needClose = countCon == null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<IasUserMessage_> result = new ArrayList<IasUserMessage_>();
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
                IasUserMessage_ obj = new IasUserMessage_();
                obj.clientId = rs.getString("client_id"); // String
                obj.openId = rs.getString("open_id"); // String
                obj.iasMsgId = rs.getString("ias_msg_id"); // String
                obj.notiId = rs.getString("noti_id"); // String
                obj.tranId = rs.getString("tran_id"); // String
                obj.readInd = rs.getString("read_ind"); // String
                obj.iasNotiStatus = rs.getString("ias_noti_status"); // String
                obj.iasNotiResult = rs.getString("ias_noti_result"); // String
                obj.txId = rs.getString("tx_id"); // String
                obj.sentDt = rs.getTimestamp("sent_dt"); // Timestamp
                obj.iasDeliveryStatus = rs.getString("ias_delivery_status"); // String
                obj.housekeepInd = rs.getString("housekeep_ind"); // String
                obj.deleteInd = rs.getString("delete_ind"); // String
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

    public void setinitialized(boolean initialized) {
        this.initialized = initialized;
    }

    public void init(HPFW_Connection countCon, final String inclientId, final String inopenId, final String iniasMsgId,
            boolean forUpdate) throws SQLException, NullPointerException {
        this.forUpdate = forUpdate;
        boolean needClose = countCon == null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql = "select * from " + thisTableName
                    + " where 1=1 and client_id = ?  and open_id = ?  and ias_msg_id = ? ";
            if (forUpdate)
                sql += "for update";
            if (countCon == null)
                countCon = HPFW_Connection.getHPFW_Connection();
            stmt = countCon.getConnectionPtr().prepareStatement(sql);
            stmt.setString(1, inclientId);
            stmt.setString(2, inopenId);
            stmt.setString(3, iniasMsgId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                clientId = rs.getString("client_id"); // String
                openId = rs.getString("open_id"); // String
                iasMsgId = rs.getString("ias_msg_id"); // String
                notiId = rs.getString("noti_id"); // String
                tranId = rs.getString("tran_id"); // String
                readInd = rs.getString("read_ind"); // String
                iasNotiStatus = rs.getString("ias_noti_status"); // String
                iasNotiResult = rs.getString("ias_noti_result"); // String
                txId = rs.getString("tx_id"); // String
                sentDt = rs.getTimestamp("sent_dt"); // Timestamp
                iasDeliveryStatus = rs.getString("ias_delivery_status"); // String
                housekeepInd = rs.getString("housekeep_ind"); // String
                deleteInd = rs.getString("delete_ind"); // String
                createDt = rs.getTimestamp("create_dt"); // Timestamp
                lastModifyDt = rs.getTimestamp("last_modify_dt"); // Timestamp
                createBy = rs.getString("create_by"); // String
                lastModifyBy = rs.getString("last_modify_by"); // String
                initialized = true;
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
    // }INIT

    /**
     * IasUserMessage_ Destroyer
     */
    protected void finalize() throws Throwable {
        clientId = null;
        openId = null;
        iasMsgId = null;
        notiId = null;
        tranId = null;
        readInd = null;
        iasNotiStatus = null;
        iasNotiResult = null;
        txId = null;
        sentDt = null;
        iasDeliveryStatus = null;
        housekeepInd = null;
        deleteInd = null;
        createDt = null;
        lastModifyDt = null;
        createBy = null;
        lastModifyBy = null;
    }

    /**
     * Get client_id
     */
    public String getClientId() {
        return clientId == null ? "" : clientId;
    }

    /**
     * Set client_id
     */
    public void setClientId(final String inClientId) {
        clientId = inClientId;
        dirty_clientId = true;
    }

    /**
     * Get open_id
     */
    public String getOpenId() {
        return openId == null ? "" : openId;
    }

    /**
     * Set open_id
     */
    public void setOpenId(final String inOpenId) {
        openId = inOpenId;
        dirty_openId = true;
    }

    /**
     * Get ias_msg_id
     */
    public String getIasMsgId() {
        return iasMsgId == null ? "" : iasMsgId;
    }

    /**
     * Set ias_msg_id
     */
    public void setIasMsgId(final String inIasMsgId) {
        iasMsgId = inIasMsgId;
        dirty_iasMsgId = true;
    }

    /**
     * Get noti_id
     */
    public String getNotiId() {
        return notiId == null ? "" : notiId;
    }

    /**
     * Set noti_id
     */
    public void setNotiId(final String inNotiId) {
        notiId = inNotiId;
        dirty_notiId = true;
    }

    /**
     * Get tran_id
     */
    public String getTranId() {
        return tranId == null ? "" : tranId;
    }

    /**
     * Set tran_id
     */
    public void setTranId(final String inTranId) {
        tranId = inTranId;
        dirty_tranId = true;
    }

    /**
     * Get read_ind
     */
    public String getReadInd() {
        return readInd == null ? "" : readInd;
    }

    /**
     * Set read_ind
     */
    public void setReadInd(final String inReadInd) {
        readInd = inReadInd;
        dirty_readInd = true;
    }

    /**
     * Get ias_noti_status
     */
    public String getIasNotiStatus() {
        return iasNotiStatus == null ? "" : iasNotiStatus;
    }

    /**
     * Set ias_noti_status
     */
    public void setIasNotiStatus(final String inIasNotiStatus) {
        iasNotiStatus = inIasNotiStatus;
        dirty_iasNotiStatus = true;
    }

    /**
     * Get ias_noti_result
     */
    public String getIasNotiResult() {
        return iasNotiResult == null ? "" : iasNotiResult;
    }

    /**
     * Set ias_noti_result
     */
    public void setIasNotiResult(final String inIasNotiResult) {
        iasNotiResult = inIasNotiResult;
        dirty_iasNotiResult = true;
    }

    /**
     * Get tx_id
     */
    public String getTxId() {
        return txId == null ? "" : txId;
    }

    /**
     * Set tx_id
     */
    public void setTxId(final String inTxId) {
        txId = inTxId;
        dirty_txId = true;
    }

    /**
     * Get sent_dt
     */
    public Timestamp getSentDt() {
        return sentDt;
    }

    /**
     * Set sent_dt
     */
    public void setSentDt(final Timestamp inSentDt) {
        sentDt = inSentDt;
        dirty_sentDt = true;
    }

    /**
     * Get ias_delivery_status
     */
    public String getIasDeliveryStatus() {
        return iasDeliveryStatus == null ? "" : iasDeliveryStatus;
    }

    /**
     * Set ias_delivery_status
     */
    public void setIasDeliveryStatus(final String inIasDeliveryStatus) {
        iasDeliveryStatus = inIasDeliveryStatus;
        dirty_iasDeliveryStatus = true;
    }

    /**
     * Get housekeep_ind
     */
    public String getHousekeepInd() {
        return housekeepInd == null ? "" : housekeepInd;
    }

    /**
     * Set housekeep_ind
     */
    public void setHousekeepInd(final String inHousekeepInd) {
        housekeepInd = inHousekeepInd;
        dirty_housekeepInd = true;
    }

    /**
     * Get delete_ind
     */
    public String getDeleteInd() {
        return deleteInd == null ? "" : deleteInd;
    }

    /**
     * Set delete_ind
     */
    public void setDeleteInd(final String inDeleteInd) {
        deleteInd = inDeleteInd;
        dirty_deleteInd = true;
    }

    /**
     * Get create_dt
     */
    public Timestamp getCreateDt() {
        return createDt;
    }

    /**
     * Set create_dt
     */
    public void setCreateDt(final Timestamp inCreateDt) {
        createDt = inCreateDt;
        dirty_createDt = true;
    }

    /**
     * Get last_modify_dt
     */
    public Timestamp getLastModifyDt() {
        return lastModifyDt;
    }

    /**
     * Set last_modify_dt
     */
    public void setLastModifyDt(final Timestamp inLastModifyDt) {
        lastModifyDt = inLastModifyDt;
        dirty_lastModifyDt = true;
    }

    /**
     * Get create_by
     */
    public String getCreateBy() {
        return createBy == null ? "" : createBy;
    }

    /**
     * Set create_by
     */
    public void setCreateBy(final String inCreateBy) {
        createBy = inCreateBy;
        dirty_createBy = true;
    }

    /**
     * Get last_modify_by
     */
    public String getLastModifyBy() {
        return lastModifyBy == null ? "" : lastModifyBy;
    }

    /**
     * Set last_modify_by
     */
    public void setLastModifyBy(final String inLastModifyBy) {
        lastModifyBy = inLastModifyBy;
        dirty_lastModifyBy = true;
    }
}
