package hk.gov.cmc.persistence.report;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import hk.gov.cmc.persistence.connection.hpfw.CommonDBUtils;
import hk.gov.cmc.persistence.connection.hpfw.HPFW_Connection;
import hk.gov.cmc.persistence.connection.hpfw.MysqlCommonDBUtils;
import hk.gov.cmc.persistence.connection.hpfw.Parameter;

public class AdmRptAdhocReq_ implements Serializable {
    private static final long serialVersionUID = 1L;
    private boolean initialized = false;
    private final static String thisTableName = "adm_rpt_adhoc_req";
    private final static String thisTableHistName = "_h";
    private boolean forUpdate = false;
    private String seqId = null;
    private String rptId = null;
    private String outputFileType = null;
    private String userId = null;
    private String param = null;
    private String status = null;
    private String reqType = null;
    private String projectId = null;
    private String lastModifyBy = null;
    private Timestamp lastModifyDt = null;
    private Timestamp createDt = null;
    private String createBy = null;

    private boolean dirty_seqId = false;
    private boolean dirty_rptId = false;
    private boolean dirty_outputFileType = false;
    private boolean dirty_userId = false;
    private boolean dirty_param = false;
    private boolean dirty_status = false;
    private boolean dirty_reqType = false;
    private boolean dirty_projectId = false;
    private boolean dirty_lastModifyBy = false;
    private boolean dirty_lastModifyDt = false;
    private boolean dirty_createDt = false;
    private boolean dirty_createBy = false;

    /**
     * AdmRptAdhocReq_ Contructor
     */
    public AdmRptAdhocReq_() {
        super();
    }

    /**
     * AdmRptAdhocReq_ Constructor with specify PK
     */
    public AdmRptAdhocReq_(HPFW_Connection countCon, String inseqId) throws SQLException, NullPointerException {
        this();
        init(countCon, inseqId, false);
    }

    public AdmRptAdhocReq_(HPFW_Connection countCon, String inseqId, boolean forUpdate)
            throws SQLException, NullPointerException {
        this();
        init(countCon, inseqId, forUpdate);
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
                String sql = "select seq_id from " + thisTableName + " " + whereCluase;
                stmt = countCon.getConnectionPtr().prepareStatement(sql);
                CommonDBUtils.setStatement(stmt, paraL);
                rs = stmt.executeQuery();

                while (rs.next()) {
                    String histsql = "insert into ";
                    histsql += thisTableName + thisTableHistName;
                    histsql += " (HIST_SEQ,HIST_action,HIST_update_type,HIST_status,last_modify_by,last_modify_dt,create_by,create_dt,seq_id";
                    histsql += ") values (?,?,?,?,?,?,?,?";
                    // perpare where ...
                    histsql += ",?";
                    histsql += ")";
                    ArrayList<Parameter> histParaList = new ArrayList<Parameter>();
                    // MyGov5-01-207: Migrate DBMS to MySQL
                    // histParaList.add(new Parameter(Parameter.String,CommonDBUtils.getSequence(countCon, thisTableName + thisTableHistName))) ;
                    histParaList.add(new Parameter(Parameter.String, MysqlCommonDBUtils.getNextMachineBaseSequence()));
                    // MyGov5-01-207: Migrate DBMS to MySQL
                    histParaList.add(new Parameter(Parameter.String, HPFW_Connection.DELETE));
                    histParaList.add(new Parameter(Parameter.String,
                            countCon.getUpdateMode().equals(HPFW_Connection.HISTORY_ONLY) ? HPFW_Connection.DUAL
                                    : HPFW_Connection.REMOTE));
                    histParaList.add(new Parameter(Parameter.String, HPFW_Connection.PENDING));
                    histParaList.add(new Parameter(Parameter.String, countCon.getLastUpdBy()));
                    histParaList.add(new Parameter(Parameter.Timestamp, countCon.getLastUpTime()));
                    histParaList.add(new Parameter(Parameter.String, countCon.getLastUpdBy()));
                    histParaList.add(new Parameter(Parameter.Timestamp, countCon.getLastUpTime()));
                    // perpare where ...
                    // MyGov5-01-207: Migrate DBMS to MySQL
                    histParaList.add(new Parameter(Parameter.String, rs.getString(1)));
                    // MyGov5-01-207: Migrate DBMS to MySQL
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
            sql += " where 1=1 and seq_id = ? ";

            ArrayList<Parameter> paraList = new ArrayList<Parameter>();

            // perpare where ...
            // MyGov5-01-207: Migrate DBMS to MySQL
            // paraList.add(new Parameter(Parameter.Long, this.seqId)) ;
            paraList.add(new Parameter(Parameter.String, this.seqId));
            // MyGov5-01-207: Migrate DBMS to MySQL
            countCon.executeStatement(sql, paraList);
        }

        if (HPFW_Connection.HISTORY_ONLY.equals(countCon.getUpdateMode())
                || HPFW_Connection.DIRECT_WITH_HISTORY.equals(countCon.getUpdateMode())) {
            String histsql = "insert into ";
            histsql += thisTableName + thisTableHistName;
            histsql += " (HIST_SEQ,HIST_action,HIST_update_type,HIST_status,last_modify_by,last_modify_dt,create_by,create_dt,seq_id) values (?,?,?,?,?,?,?,?,?)";
            ArrayList<Parameter> histParaList = new ArrayList<Parameter>();
            // MyGov5-01-207: Migrate DBMS to MySQL
            // histParaList.add(new Parameter(Parameter.String,CommonDBUtils.getSequence(countCon, thisTableName + thisTableHistName))) ;
            histParaList.add(new Parameter(Parameter.String, MysqlCommonDBUtils.getNextMachineBaseSequence()));
            // MyGov5-01-207: Migrate DBMS to MySQL
            histParaList.add(new Parameter(Parameter.String, HPFW_Connection.DELETE));
            histParaList.add(new Parameter(Parameter.String,
                    countCon.getUpdateMode().equals(HPFW_Connection.HISTORY_ONLY) ? HPFW_Connection.DUAL
                            : HPFW_Connection.REMOTE));
            histParaList.add(new Parameter(Parameter.String, HPFW_Connection.PENDING));
            histParaList.add(new Parameter(Parameter.String, countCon.getLastUpdBy()));
            histParaList.add(new Parameter(Parameter.Timestamp, countCon.getLastUpTime()));
            histParaList.add(new Parameter(Parameter.String, countCon.getLastUpdBy()));
            histParaList.add(new Parameter(Parameter.Timestamp, countCon.getLastUpTime()));
            // perpare where ...
            // MyGov5-01-207: Migrate DBMS to MySQL
            histParaList.add(new Parameter(Parameter.String, this.seqId));
            // MyGov5-01-207: Migrate DBMS to MySQL
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
            if (seqId != null)
                sql += "seq_id,";
            if (rptId != null)
                sql += "rpt_id,";
            if (outputFileType != null)
                sql += "output_file_type,";
            if (userId != null)
                sql += "user_id,";
            if (param != null)
                sql += "param,";
            if (status != null)
                sql += "status,";
            if (reqType != null)
                sql += "req_type,";
            if (projectId != null)
                sql += "project_id,";
            sql += "create_by,last_modify_by,create_dt,last_modify_dt) values (";

            // perpare set ...
            if (seqId != null)
                sql += "?,";
            if (rptId != null)
                sql += "?,";
            if (outputFileType != null)
                sql += "?,";
            if (userId != null)
                sql += "?,";
            if (param != null)
                sql += "?,";
            if (status != null)
                sql += "?,";
            if (reqType != null)
                sql += "?,";
            if (projectId != null)
                sql += "?,";
            sql += "?,?,?,?) ";

            // perpare set ...
            // MyGov5-01-207: Migrate DBMS to MySQL
            // if(seqId!=null) paraList.add(new Parameter(Parameter.Long, this.seqId)) ;
            if (seqId != null)
                paraList.add(new Parameter(Parameter.String, this.seqId));
            // MyGov5-01-207: Migrate DBMS to MySQL
            if (rptId != null)
                paraList.add(new Parameter(Parameter.String, this.rptId));
            if (outputFileType != null)
                paraList.add(new Parameter(Parameter.String, this.outputFileType));
            if (userId != null)
                paraList.add(new Parameter(Parameter.String, this.userId));
            if (param != null)
                paraList.add(new Parameter(Parameter.String, this.param));
            if (status != null)
                paraList.add(new Parameter(Parameter.String, this.status));
            if (reqType != null)
                paraList.add(new Parameter(Parameter.String, this.reqType));
            if (projectId != null)
                paraList.add(new Parameter(Parameter.String, this.projectId));
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
            hist_sql += " (HIST_SEQ,HIST_action,HIST_update_type,HIST_status,";

            // perpare set ...
            if (seqId != null)
                hist_sql += "seq_id,";
            if (rptId != null)
                hist_sql += "rpt_id,";
            if (outputFileType != null)
                hist_sql += "output_file_type,";
            if (userId != null)
                hist_sql += "user_id,";
            if (param != null)
                hist_sql += "param,";
            if (status != null)
                hist_sql += "status,";
            if (reqType != null)
                hist_sql += "req_type,";
            if (projectId != null)
                hist_sql += "project_id,";
            hist_sql += "create_by,last_modify_by,create_dt,last_modify_dt) values (?,?,?,?,";

            // perpare set ...
            if (seqId != null)
                hist_sql += "?,";
            if (rptId != null)
                hist_sql += "?,";
            if (outputFileType != null)
                hist_sql += "?,";
            if (userId != null)
                hist_sql += "?,";
            if (param != null)
                hist_sql += "?,";
            if (status != null)
                hist_sql += "?,";
            if (reqType != null)
                hist_sql += "?,";
            if (projectId != null)
                hist_sql += "?,";
            hist_sql += "?,?,?,?) ";
            // MyGov5-01-207: Migrate DBMS to MySQL
            // histParaList.add(new Parameter(Parameter.String,CommonDBUtils.getSequence(countCon, thisTableName + thisTableHistName))) ;
            histParaList.add(new Parameter(Parameter.String, MysqlCommonDBUtils.getNextMachineBaseSequence()));
            // MyGov5-01-207: Migrate DBMS to MySQL
            histParaList.add(new Parameter(Parameter.String, HPFW_Connection.INSERT));
            histParaList.add(new Parameter(Parameter.String,
                    countCon.getUpdateMode().equals(HPFW_Connection.HISTORY_ONLY) ? HPFW_Connection.DUAL
                            : HPFW_Connection.REMOTE));
            histParaList.add(new Parameter(Parameter.String, HPFW_Connection.PENDING));

            // perpare set ...
            // MyGov5-01-207: Migrate DBMS to MySQL
            if (seqId != null)
                histParaList.add(new Parameter(Parameter.String, this.seqId));
            // MyGov5-01-207: Migrate DBMS to MySQL

            if (rptId != null)
                histParaList.add(new Parameter(Parameter.String, this.rptId));
            if (outputFileType != null)
                histParaList.add(new Parameter(Parameter.String, this.outputFileType));
            if (userId != null)
                histParaList.add(new Parameter(Parameter.String, this.userId));
            if (param != null)
                histParaList.add(new Parameter(Parameter.String, this.param));
            if (status != null)
                histParaList.add(new Parameter(Parameter.String, this.status));
            if (reqType != null)
                histParaList.add(new Parameter(Parameter.String, this.reqType));
            if (projectId != null)
                histParaList.add(new Parameter(Parameter.String, this.projectId));
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
                String sql = "select seq_id from " + thisTableName + " " + whereClause;
                stmt = countCon.getConnectionPtr().prepareStatement(sql);
                CommonDBUtils.setStatement(stmt, whereParaL);
                rs = stmt.executeQuery();

                while (rs.next()) {
                    String histsql = "insert into ";
                    histsql += thisTableName + thisTableHistName;
                    histsql += " (HIST_SEQ,HIST_action,HIST_update_type,HIST_status,last_modify_by,last_modify_dt,create_by,create_dt,seq_id,"
                            + selectString;
                    histsql += ") values (?,?,?,?,?,?,?,?,?";
                    for (int i = 0; i < selectStringArray.length; i++)
                        histsql += ",?";
                    histsql += ")";

                    ArrayList<Parameter> histParaList = new ArrayList<Parameter>();
                    // MyGov5-01-207: Migrate DBMS to MySQL
                    histParaList.add(new Parameter(Parameter.String, MysqlCommonDBUtils.getNextMachineBaseSequence()));
                    // MyGov5-01-207: Migrate DBMS to MySQL
                    histParaList.add(new Parameter(Parameter.String, HPFW_Connection.UPDATE));
                    histParaList.add(new Parameter(Parameter.String,
                            countCon.getUpdateMode().equals(HPFW_Connection.HISTORY_ONLY) ? HPFW_Connection.DUAL
                                    : HPFW_Connection.REMOTE));
                    histParaList.add(new Parameter(Parameter.String, HPFW_Connection.PENDING));
                    histParaList.add(new Parameter(Parameter.String, countCon.getLastUpdBy()));
                    histParaList.add(new Parameter(Parameter.Timestamp, countCon.getLastUpTime()));
                    histParaList.add(new Parameter(Parameter.String, countCon.getLastUpdBy()));
                    histParaList.add(new Parameter(Parameter.Timestamp, countCon.getLastUpTime()));
                    // perpare where ...
                    // MyGov5-01-207: Migrate DBMS to MySQL
                    histParaList.add(new Parameter(Parameter.String, rs.getString(1)));
                    // MyGov5-01-207: Migrate DBMS to MySQL
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
            if (dirty_rptId)
                sql += "rpt_id = ?,";
            if (dirty_outputFileType)
                sql += "output_file_type = ?,";
            if (dirty_userId)
                sql += "user_id = ?,";
            if (dirty_param)
                sql += "param = ?,";
            if (dirty_status)
                sql += "status = ?,";
            if (dirty_reqType)
                sql += "req_type = ?,";
            if (dirty_projectId)
                sql += "project_id = ?,";

            sql += " last_modify_by = ?,last_modify_dt = ? where 1=1 and seq_id = ? ";
            // perpare set ...
            if (dirty_rptId)
                paraList.add(new Parameter(Parameter.String, this.rptId));
            if (dirty_outputFileType)
                paraList.add(new Parameter(Parameter.String, this.outputFileType));
            if (dirty_userId)
                paraList.add(new Parameter(Parameter.String, this.userId));
            if (dirty_param)
                paraList.add(new Parameter(Parameter.String, this.param));
            if (dirty_status)
                paraList.add(new Parameter(Parameter.String, this.status));
            if (dirty_reqType)
                paraList.add(new Parameter(Parameter.String, this.reqType));
            if (dirty_projectId)
                paraList.add(new Parameter(Parameter.String, this.projectId));
            paraList.add(new Parameter(Parameter.String, countCon.getLastUpdBy()));
            paraList.add(new Parameter(Parameter.Timestamp, countCon.getLastUpTime()));
            // perpare where ...
            // MyGov5-01-207: Migrate DBMS to MySQL
            paraList.add(new Parameter(Parameter.String, this.seqId));
            // MyGov5-01-207: Migrate DBMS to MySQL
            countCon.executeStatement(sql, paraList);
        }

        if (HPFW_Connection.HISTORY_ONLY.equals(countCon.getUpdateMode())
                || HPFW_Connection.DIRECT_WITH_HISTORY.equals(countCon.getUpdateMode())) {
            ArrayList<Parameter> histParaList = new ArrayList<Parameter>();

            String hist_sql = "insert into ";
            hist_sql += thisTableName + thisTableHistName;
            hist_sql += " (HIST_SEQ,HIST_action,HIST_update_type,HIST_status,";

            // perpare set ...
            hist_sql += "seq_id,";
            if (dirty_rptId)
                hist_sql += "rpt_id,";
            if (dirty_outputFileType)
                hist_sql += "output_file_type,";
            if (dirty_userId)
                hist_sql += "user_id,";
            if (dirty_param)
                hist_sql += "param,";
            if (dirty_status)
                hist_sql += "status,";
            if (dirty_reqType)
                hist_sql += "req_type,";
            if (dirty_projectId)
                hist_sql += "project_id,";
            hist_sql += "last_modify_by,last_modify_dt,create_by,create_dt) values (?,?,?,?,";

            // perpare set ...
            hist_sql += "?,";
            if (dirty_rptId)
                hist_sql += "?,";
            if (dirty_outputFileType)
                hist_sql += "?,";
            if (dirty_userId)
                hist_sql += "?,";
            if (dirty_param)
                hist_sql += "?,";
            if (dirty_status)
                hist_sql += "?,";
            if (dirty_reqType)
                hist_sql += "?,";
            if (dirty_projectId)
                hist_sql += "?,";
            hist_sql += "?,?,?,?) ";
            // MyGov5-01-207: Migrate DBMS to MySQL
            histParaList.add(new Parameter(Parameter.String, MysqlCommonDBUtils.getNextMachineBaseSequence()));
            // MyGov5-01-207: Migrate DBMS to MySQL
            histParaList.add(new Parameter(Parameter.String, HPFW_Connection.UPDATE));
            histParaList.add(new Parameter(Parameter.String,
                    countCon.getUpdateMode().equals(HPFW_Connection.HISTORY_ONLY) ? HPFW_Connection.DUAL
                            : HPFW_Connection.REMOTE));
            histParaList.add(new Parameter(Parameter.String, HPFW_Connection.PENDING));

            // perpare set ...
            // MyGov5-01-207: Migrate DBMS to MySQL
            histParaList.add(new Parameter(Parameter.String, this.seqId));
            // MyGov5-01-207: Migrate DBMS to MySQL
            if (dirty_rptId)
                histParaList.add(new Parameter(Parameter.String, this.rptId));
            if (dirty_outputFileType)
                histParaList.add(new Parameter(Parameter.String, this.outputFileType));
            if (dirty_userId)
                histParaList.add(new Parameter(Parameter.String, this.userId));
            if (dirty_param)
                histParaList.add(new Parameter(Parameter.String, this.param));
            if (dirty_status)
                histParaList.add(new Parameter(Parameter.String, this.status));
            if (dirty_reqType)
                histParaList.add(new Parameter(Parameter.String, this.reqType));
            if (dirty_projectId)
                histParaList.add(new Parameter(Parameter.String, this.projectId));
            histParaList.add(new Parameter(Parameter.String, countCon.getLastUpdBy()));
            histParaList.add(new Parameter(Parameter.Timestamp, countCon.getLastUpTime()));
            histParaList.add(new Parameter(Parameter.String, countCon.getLastUpdBy()));
            histParaList.add(new Parameter(Parameter.Timestamp, countCon.getLastUpTime()));
            countCon.executeStatement(hist_sql, histParaList, true);
        }
        this.forUpdate = false;
    }

    public static ArrayList<AdmRptAdhocReq_> getResultList(HPFW_Connection countCon, String whereCluase,
            ArrayList<Parameter> paraL) throws SQLException {
        boolean needClose = countCon == null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<AdmRptAdhocReq_> result = new ArrayList<AdmRptAdhocReq_>();
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
                AdmRptAdhocReq_ obj = new AdmRptAdhocReq_();
                // seqId = rs.getString("seq_id")==null || "".equals(rs.getString("seq_id")) ? null : new Long(rs.getLong("seq_id")); //Long
                // MyGov5-01-207: Migrate DBMS to MySQL
                // obj.seqId = new Long(rs.getLong("seq_id")); //Long
                obj.seqId = rs.getString("seq_id"); // Long
                // MyGov5-01-207: Migrate DBMS to MySQL
                obj.rptId = rs.getString("rpt_id"); // String
                obj.outputFileType = rs.getString("output_file_type"); // String
                obj.userId = rs.getString("user_id"); // String
                obj.param = rs.getString("param"); // String
                obj.status = rs.getString("status"); // String
                obj.reqType = rs.getString("req_type"); // String
                obj.projectId = rs.getString("project_id"); // String
                obj.lastModifyBy = rs.getString("last_modify_by"); // String
                obj.lastModifyDt = rs.getTimestamp("last_modify_dt"); // Timestamp
                obj.createDt = rs.getTimestamp("create_dt"); // Timestamp
                obj.createBy = rs.getString("create_by"); // String
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

    public void init(HPFW_Connection countCon, final String inseqId, boolean forUpdate)
            throws SQLException, NullPointerException {
        this.forUpdate = forUpdate;
        boolean needClose = countCon == null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql = "select * from " + thisTableName + " where 1=1 and seq_id = ? ";
            if (forUpdate)
                sql += "for update";
            if (countCon == null)
                countCon = HPFW_Connection.getHPFW_Connection();
            stmt = countCon.getConnectionPtr().prepareStatement(sql);
            stmt.setString(1, inseqId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                // seqId = rs.getString("seq_id")==null || "".equals(rs.getString("seq_id")) ? null : new Long(rs.getLong("seq_id")); //Long
                // MyGov5-01-207: Migrate DBMS to MySQL
                // seqId = new Long(rs.getLong("seq_id")); //Long
                seqId = rs.getString("seq_id"); // Long
                // MyGov5-01-207: Migrate DBMS to MySQL
                rptId = rs.getString("rpt_id"); // String
                outputFileType = rs.getString("output_file_type"); // String
                userId = rs.getString("user_id"); // String
                param = rs.getString("param"); // String
                status = rs.getString("status"); // String
                reqType = rs.getString("req_type"); // String
                projectId = rs.getString("project_id"); // String
                lastModifyBy = rs.getString("last_modify_by"); // String
                lastModifyDt = rs.getTimestamp("last_modify_dt"); // Timestamp
                createDt = rs.getTimestamp("create_dt"); // Timestamp
                createBy = rs.getString("create_by"); // String
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
     * AdmRptAdhocReq_ Destroyer
     */
    protected void finalize() throws Throwable {
        seqId = null;
        rptId = null;
        outputFileType = null;
        userId = null;
        param = null;
        status = null;
        reqType = null;
        projectId = null;
        lastModifyBy = null;
        lastModifyDt = null;
        createDt = null;
        createBy = null;
    }

    /**
     * Get seq_id
     */
    public String getSeqId() {
        return seqId;
    }

    /**
     * Set seq_id
     */
    public void setSeqId(final String inSeqId) {
        seqId = inSeqId;
        dirty_seqId = true;
    }

    /**
     * Get rpt_id
     */
    public String getRptId() {
        return rptId == null ? "" : rptId;
    }

    /**
     * Set rpt_id
     */
    public void setRptId(final String inRptId) {
        rptId = inRptId;
        dirty_rptId = true;
    }

    /**
     * Get output_file_type
     */
    public String getOutputFileType() {
        return outputFileType == null ? "" : outputFileType;
    }

    /**
     * Set output_file_type
     */
    public void setOutputFileType(final String inOutputFileType) {
        outputFileType = inOutputFileType;
        dirty_outputFileType = true;
    }

    /**
     * Get user_id
     */
    public String getUserId() {
        return userId == null ? "" : userId;
    }

    /**
     * Set user_id
     */
    public void setUserId(final String inUserId) {
        userId = inUserId;
        dirty_userId = true;
    }

    /**
     * Get param
     */
    public String getParam() {
        return param == null ? "" : param;
    }

    /**
     * Set param
     */
    public void setParam(final String inParam) {
        param = inParam;
        dirty_param = true;
    }

    /**
     * Get status
     */
    public String getStatus() {
        return status == null ? "" : status;
    }

    /**
     * Set status
     */
    public void setStatus(final String inStatus) {
        status = inStatus;
        dirty_status = true;
    }

    /**
     * Get req_type
     */
    public String getReqType() {
        return reqType == null ? "" : reqType;
    }

    /**
     * Set req_type
     */
    public void setReqType(final String inReqType) {
        reqType = inReqType;
        dirty_reqType = true;
    }

    /**
     * Get project_id
     */
    public String getProjectId() {
        return projectId == null ? "" : projectId;
    }

    /**
     * Set project_id
     */
    public void setProjectId(final String inProjectId) {
        projectId = inProjectId;
        dirty_projectId = true;
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
}
