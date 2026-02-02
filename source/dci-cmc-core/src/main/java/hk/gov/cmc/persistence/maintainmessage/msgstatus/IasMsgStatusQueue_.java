package hk.gov.cmc.persistence.maintainmessage.msgstatus;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import hk.gov.cmc.persistence.connection.hpfw.CommonDBUtils;
import hk.gov.cmc.persistence.connection.hpfw.HPFW_Connection;
import hk.gov.cmc.persistence.connection.hpfw.Parameter;

public class IasMsgStatusQueue_ implements Serializable {
    private static final long serialVersionUID = 1L;
    private boolean initialized = false;
    private final static String thisTableName = "IAS_MSG_STATUS_QUEUE";
    private final static String thisTableHistName = "_H";
    private boolean forUpdate = false;
    private String jobId = null;
    private String notiId = null;
    private String accountStatus = null;
    private String spId = null;
    private String msgType = null;
    private String jobStatus = null;
    private Timestamp createDt = null;
    private Timestamp lastModifyDt = null;
    private String createBy = null;
    private String lastModifyBy = null;
    private boolean dirty_jobId = false;
    private boolean dirty_notiId = false;
    private boolean dirty_accountStatus = false;
    private boolean dirty_spId = false;
    private boolean dirty_msgType = false;
    private boolean dirty_jobStatus = false;
    private boolean dirty_createDt = false;
    private boolean dirty_lastModifyDt = false;
    private boolean dirty_createBy = false;
    private boolean dirty_lastModifyBy = false;

    /**
     * IasMsgStatusQueue_ Contructor
     */
    public IasMsgStatusQueue_() {
        super();
    }

    /**
     * IasMsgStatusQueue_ Constructor with specify PK
     */
    public IasMsgStatusQueue_(HPFW_Connection countCon, String injobId) throws SQLException, NullPointerException {
        this();
        init(countCon, injobId, false);
    }

    public IasMsgStatusQueue_(HPFW_Connection countCon, String injobId, boolean forUpdate)
            throws SQLException, NullPointerException {
        this();
        init(countCon, injobId, forUpdate);
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
                String sql = "select job_id from " + thisTableName + " " + whereCluase;
                stmt = countCon.getConnectionPtr().prepareStatement(sql);
                CommonDBUtils.setStatement(stmt, paraL);
                rs = stmt.executeQuery();

                while (rs.next()) {
                    String histsql = "insert into ";
                    histsql += thisTableName + thisTableHistName;
                    histsql += " (HIST_SEQ,HIST_action,HIST_update_type,HIST_status,HIST_server_id,last_modify_by,last_modify_dt,create_by,create_dt,job_id";
                    histsql += ") values (?,?,?,?,?,?,?,?,?,?";
                    // perpare where ...
                    histsql += ",?";
                    histsql += ")";
                    ArrayList<Parameter> histParaList = new ArrayList<Parameter>();
                    histParaList.add(new Parameter(Parameter.String,
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
            sql += " where 1=1 and job_id = ? ";

            ArrayList<Parameter> paraList = new ArrayList<Parameter>();

            // perpare where ...
            paraList.add(new Parameter(Parameter.String, this.jobId));
            countCon.executeStatement(sql, paraList);
        }

        if (HPFW_Connection.HISTORY_ONLY.equals(countCon.getUpdateMode())
                || HPFW_Connection.DIRECT_WITH_HISTORY.equals(countCon.getUpdateMode())) {
            String histsql = "insert into ";
            histsql += thisTableName + thisTableHistName;
            histsql += " (HIST_SEQ,HIST_action,HIST_update_type,HIST_status,HIST_server_id,last_modify_by,last_modify_dt,create_by,create_dt";
            histsql += ",JobId";
            histsql += ") values (?,?,?,?,?,?,?,?,?";
            // perpare where ...
            histsql += ",?";
            histsql += ")";
            ArrayList<Parameter> histParaList = new ArrayList<Parameter>();
            histParaList.add(new Parameter(Parameter.String,
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
            histParaList.add(new Parameter(Parameter.String, this.jobId));
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
            if (jobId != null)
                sql += "job_id,";
            if (notiId != null)
                sql += "noti_id,";
            if (accountStatus != null)
                sql += "account_status,";
            // CMC-2024-026: iAM Smart delete message by SP_ID and MSG_TYPE - BEGIN
            if (spId != null)
                sql += "sp_id,";
            if (msgType != null)
                sql += "msg_type,";
            // CMC-2024-026: iAM Smart delete message by SP_ID and MSG_TYPE - END
            if (jobStatus != null)
                sql += "job_status,";
            sql += "create_by,last_modify_by,create_dt,last_modify_dt) values (";

            // perpare set ...
            if (jobId != null)
                sql += "?,";
            if (notiId != null)
                sql += "?,";
            if (accountStatus != null)
                sql += "?,";
            // CMC-2024-026: iAM Smart delete message by SP_ID and MSG_TYPE - BEGIN
            if (spId != null)
                sql += "?,";
            if (msgType != null)
                sql += "?,";
            // CMC-2024-026: iAM Smart delete message by SP_ID and MSG_TYPE - END
            if (jobStatus != null)
                sql += "?,";
            sql += "?,?,?,?) ";

            // perpare set ...
            if (jobId != null)
                paraList.add(new Parameter(Parameter.String, this.jobId));
            if (notiId != null)
                paraList.add(new Parameter(Parameter.String, this.notiId));
            if (accountStatus != null)
                paraList.add(new Parameter(Parameter.String, this.accountStatus));
            // CMC-2024-026: iAM Smart delete message by SP_ID and MSG_TYPE - BEGIN
            if (spId != null)
                paraList.add(new Parameter(Parameter.String, this.spId));
            if (msgType != null)
                paraList.add(new Parameter(Parameter.String, this.msgType));
            // CMC-2024-026: iAM Smart delete message by SP_ID and MSG_TYPE - END
            if (jobStatus != null)
                paraList.add(new Parameter(Parameter.String, this.jobStatus));
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
            if (jobId != null)
                hist_sql += "job_id,";
            if (notiId != null)
                hist_sql += "noti_id,";
            if (accountStatus != null)
                hist_sql += "account_status,";
            // CMC-2024-026: iAM Smart delete message by SP_ID and MSG_TYPE - BEGIN
            if (spId != null)
                hist_sql += "sp_id,";
            if (msgType != null)
                hist_sql += "msg_type,";
            // CMC-2024-026: iAM Smart delete message by SP_ID and MSG_TYPE - END
            if (jobStatus != null)
                hist_sql += "job_status,";
            hist_sql += "create_by,last_modify_by,create_dt,last_modify_dt) values (?,?,?,?,?,";

            // perpare set ...
            if (jobId != null)
                hist_sql += "?,";
            if (notiId != null)
                hist_sql += "?,";
            if (accountStatus != null)
                hist_sql += "?,";
            // CMC-2024-026: iAM Smart delete message by SP_ID and MSG_TYPE - BEGIN
            if (spId != null)
                hist_sql += "?,";
            if (msgType != null)
                hist_sql += "?,";
            // CMC-2024-026: iAM Smart delete message by SP_ID and MSG_TYPE - END
            if (jobStatus != null)
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
            if (jobId != null)
                histParaList.add(new Parameter(Parameter.String, this.jobId));
            if (notiId != null)
                histParaList.add(new Parameter(Parameter.String, this.notiId));
            if (accountStatus != null)
                histParaList.add(new Parameter(Parameter.String, this.accountStatus));
            // CMC-2024-026: iAM Smart delete message by SP_ID and MSG_TYPE - BEGIN
            if (spId != null)
                histParaList.add(new Parameter(Parameter.String, this.spId));
            if (msgType != null)
                histParaList.add(new Parameter(Parameter.String, this.msgType));
            // CMC-2024-026: iAM Smart delete message by SP_ID and MSG_TYPE - END
            if (jobStatus != null)
                histParaList.add(new Parameter(Parameter.String, this.jobStatus));
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
                String sql = "select job_id from " + thisTableName + " " + whereClause;
                stmt = countCon.getConnectionPtr().prepareStatement(sql);
                CommonDBUtils.setStatement(stmt, whereParaL);
                rs = stmt.executeQuery();

                while (rs.next()) {
                    String histsql = "insert into ";
                    histsql += thisTableName + thisTableHistName;
                    histsql += " (HIST_SEQ,HIST_action,HIST_update_type,HIST_status,HIST_server_id,last_modify_by,last_modify_dt,create_by,create_dt,job_id,"
                            + selectString;
                    histsql += ") values (?,?,?,?,?,?,?,?,?,?";
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
            if (dirty_accountStatus)
                sql += "account_status = ?,";
            // CMC-2024-026: iAM Smart delete message by SP_ID and MSG_TYPE - BEGIN
            if (dirty_spId)
                sql += "sp_id = ?,";
            if (dirty_msgType)
                sql += "msg_type = ?,";
            // CMC-2024-026: iAM Smart delete message by SP_ID and MSG_TYPE - END
            if (dirty_jobStatus)
                sql += "job_status = ?,";

            sql += " last_modify_by = ?,last_modify_dt = ? where 1=1 and job_id = ? ";
            // perpare set ...
            if (dirty_notiId)
                paraList.add(new Parameter(Parameter.String, this.notiId));
            if (dirty_accountStatus)
                paraList.add(new Parameter(Parameter.String, this.accountStatus));
            // CMC-2024-026: iAM Smart delete message by SP_ID and MSG_TYPE - BEGIN
            if (dirty_spId)
                paraList.add(new Parameter(Parameter.String, this.spId));
            if (dirty_msgType)
                paraList.add(new Parameter(Parameter.String, this.msgType));
            // CMC-2024-026: iAM Smart delete message by SP_ID and MSG_TYPE - END
            if (dirty_jobStatus)
                paraList.add(new Parameter(Parameter.String, this.jobStatus));
            paraList.add(new Parameter(Parameter.String, countCon.getLastUpdBy()));
            paraList.add(new Parameter(Parameter.Timestamp, countCon.getLastUpTime()));
            // perpare where ...
            paraList.add(new Parameter(Parameter.String, this.jobId));
            countCon.executeStatement(sql, paraList);
        }

        if (HPFW_Connection.HISTORY_ONLY.equals(countCon.getUpdateMode())
                || HPFW_Connection.DIRECT_WITH_HISTORY.equals(countCon.getUpdateMode())) {
            ArrayList<Parameter> histParaList = new ArrayList<Parameter>();

            String hist_sql = "insert into ";
            hist_sql += thisTableName + thisTableHistName;
            hist_sql += " (HIST_SEQ,HIST_action,HIST_update_type,HIST_status,HIST_server_id,";

            // perpare set ...
            hist_sql += "job_id,";
            if (dirty_notiId)
                hist_sql += "noti_id,";
            if (dirty_accountStatus)
                hist_sql += "account_status,";
            // CMC-2024-026: iAM Smart delete message by SP_ID and MSG_TYPE - BEGIN
            if (dirty_spId)
                hist_sql += "sp_id,";
            if (dirty_msgType)
                hist_sql += "msg_type,";
            // CMC-2024-026: iAM Smart delete message by SP_ID and MSG_TYPE - END
            if (dirty_jobStatus)
                hist_sql += "job_status,";
            hist_sql += "last_modify_by,last_modify_dt,create_by,create_dt) values (?,?,?,?,?,";

            // perpare set ...
            hist_sql += "?,";
            if (dirty_notiId)
                hist_sql += "?,";
            if (dirty_accountStatus)
                hist_sql += "?,";
            // CMC-2024-026: iAM Smart delete message by SP_ID and MSG_TYPE - BEGIN
            if (dirty_spId)
                hist_sql += "?,";
            if (dirty_msgType)
                hist_sql += "?,";
            // CMC-2024-026: iAM Smart delete message by SP_ID and MSG_TYPE - END
            if (dirty_jobStatus)
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
            histParaList.add(new Parameter(Parameter.String, this.jobId));
            if (dirty_notiId)
                histParaList.add(new Parameter(Parameter.String, this.notiId));
            if (dirty_accountStatus)
                histParaList.add(new Parameter(Parameter.String, this.accountStatus));
            // CMC-2024-026: iAM Smart delete message by SP_ID and MSG_TYPE - BEGIN
            if (dirty_spId)
                histParaList.add(new Parameter(Parameter.String, this.spId));
            if (dirty_msgType)
                histParaList.add(new Parameter(Parameter.String, this.msgType));
            // CMC-2024-026: iAM Smart delete message by SP_ID and MSG_TYPE - END
            if (dirty_jobStatus)
                histParaList.add(new Parameter(Parameter.String, this.jobStatus));
            histParaList.add(new Parameter(Parameter.String, countCon.getLastUpdBy()));
            histParaList.add(new Parameter(Parameter.Timestamp, countCon.getLastUpTime()));
            histParaList.add(new Parameter(Parameter.String, countCon.getLastUpdBy()));
            histParaList.add(new Parameter(Parameter.Timestamp, countCon.getLastUpTime()));
            countCon.executeStatement(hist_sql, histParaList, true);
        }
        this.forUpdate = false;
    }

    public static ArrayList<IasMsgStatusQueue_> getResultList(HPFW_Connection countCon, String whereCluase,
            ArrayList<Parameter> paraL) throws SQLException {
        boolean needClose = countCon == null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<IasMsgStatusQueue_> result = new ArrayList<IasMsgStatusQueue_>();
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
                IasMsgStatusQueue_ obj = new IasMsgStatusQueue_();
                obj.jobId = rs.getString("job_id"); // String
                obj.notiId = rs.getString("noti_id"); // String
                obj.accountStatus = rs.getString("account_status"); // String
                // CMC-2024-026: iAM Smart delete message by SP_ID and MSG_TYPE - BEGIN
                obj.spId = rs.getString("sp_id"); // String
                obj.msgType = rs.getString("msg_type"); // String
                // CMC-2024-026: iAM Smart delete message by SP_ID and MSG_TYPE - END
                obj.jobStatus = rs.getString("job_status"); // String
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

    public void init(HPFW_Connection countCon, final String injobId, boolean forUpdate)
            throws SQLException, NullPointerException {
        this.forUpdate = forUpdate;
        boolean needClose = countCon == null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql = "select * from " + thisTableName + " where 1=1 and job_id = ? ";
            if (forUpdate)
                sql += "for update";
            if (countCon == null)
                countCon = HPFW_Connection.getHPFW_Connection();
            stmt = countCon.getConnectionPtr().prepareStatement(sql);
            stmt.setString(1, injobId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                jobId = rs.getString("job_id"); // String
                notiId = rs.getString("noti_id"); // String
                accountStatus = rs.getString("account_status"); // String
                // CMC-2024-026: iAM Smart delete message by SP_ID and MSG_TYPE - BEGIN
                spId = rs.getString("sp_id"); // String
                msgType = rs.getString("msg_type"); // String
                // CMC-2024-026: iAM Smart delete message by SP_ID and MSG_TYPE - END
                jobStatus = rs.getString("job_status"); // String
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
     * IasMsgStatusQueue_ Destroyer
     */
    protected void finalize() throws Throwable {
        jobId = null;
        notiId = null;
        accountStatus = null;
        jobStatus = null;
        createDt = null;
        lastModifyDt = null;
        createBy = null;
        lastModifyBy = null;
    }

    /**
     * Get job_id
     */
    public String getJobId() {
        return jobId == null ? "" : jobId;
    }

    /**
     * Set job_id
     */
    public void setJobId(final String inJobId) {
        jobId = inJobId;
        dirty_jobId = true;
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
     * Get account_status
     */
    public String getAccountStatus() {
        return accountStatus == null ? "" : accountStatus;
    }

    /**
     * Set account_status
     */
    public void setAccountStatus(final String inAccountStatus) {
        accountStatus = inAccountStatus;
        dirty_accountStatus = true;
    }

    // CMC-2024-026: iAM Smart delete message by SP_ID and MSG_TYPE - BEGIN
    /**
     * Get sp_id
     */
    public String getSpId() {
        return spId == null ? "" : spId;
    }

    /**
     * Set sp_id
     */
    public void setSpId(final String inSpId) {
        spId = inSpId;
        dirty_spId = true;
    }

    /**
     * Get msg_type
     */
    public String getMsgType() {
        return msgType == null ? "" : msgType;
    }

    /**
     * Set msg_type
     */
    public void setMsgType(final String inMsgType) {
        msgType = inMsgType;
        dirty_msgType = true;
    }
    // CMC-2024-026: iAM Smart delete message by SP_ID and MSG_TYPE - END

    /**
     * Get job_status
     */
    public String getJobStatus() {
        return jobStatus == null ? "" : jobStatus;
    }

    /**
     * Set job_status
     */
    public void setJobStatus(final String inJobStatus) {
        jobStatus = inJobStatus;
        dirty_jobStatus = true;
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
