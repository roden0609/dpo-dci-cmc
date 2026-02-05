package hk.gov.cmc.persistence.report;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import hk.gov.cmc.persistence.connection.hpfw.CommonDBUtils;
import hk.gov.cmc.persistence.connection.hpfw.HPFW_Connection;
import hk.gov.cmc.persistence.connection.hpfw.Parameter;

public class AdmRptInfo_ implements Serializable {
    private static final long serialVersionUID = 1L;
    private boolean initialized = false;

    private final static String thisTableName = "adm_rpt_info";
    private final static String thisTableHistName = "_h";
    private boolean forUpdate = false;
    private String rptId = null;
    private String rptDesc = null;
    private String implClass = null;
    private String lastModifyBy = null;
    private Timestamp lastModifyDt = null;
    private String teamBatchRpt = null;
    private String spidBatchRpt = null;
    private String batchRptFuncId = null;
    private String batchRptDefaultFormat = null;
    private String marsRpt = null;
    private String cmcRpt = null;
    private String adhocRptFuncId = null;

    private boolean dirty_rptId = false;
    private boolean dirty_rptDesc = false;
    private boolean dirty_implClass = false;
    private boolean dirty_lastModifyBy = false;
    private boolean dirty_lastModifyDt = false;
    private boolean dirty_teamBatchRpt = false;
    private boolean dirty_spidBatchRpt = false;
    private boolean dirty_batchRptFuncId = false;
    private boolean dirty_batchRptDefaultFormat = false;
    private boolean dirty_marsRpt = false;
    private boolean dirty_cmcRpt = false;
    private boolean dirty_adhocRptFuncId = false;

    /**
     * AdmRptInfo_ Contructor
     */
    public AdmRptInfo_() {
        super();
    }

    /**
     * AdmRptInfo_ Constructor with specify PK
     */
    public AdmRptInfo_(HPFW_Connection countCon, String inrptId) throws SQLException, NullPointerException {
        this();
        init(countCon, inrptId, false);
    }

    public AdmRptInfo_(HPFW_Connection countCon, String inrptId, boolean forUpdate)
            throws SQLException, NullPointerException {
        this();
        init(countCon, inrptId, forUpdate);
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
                String sql = "select rpt_id from " + thisTableName + " " + whereCluase;
                stmt = countCon.getConnectionPtr().prepareStatement(sql);
                CommonDBUtils.setStatement(stmt, paraL);
                rs = stmt.executeQuery();

                while (rs.next()) {
                    String histsql = "insert into ";
                    histsql += thisTableName + thisTableHistName;
                    histsql += " (HIST_SEQ,HIST_action,HIST_update_type,HIST_status,last_modify_by,last_modify_dt,create_by,create_dt,rpt_id";
                    histsql += ") values (?,?,?,?,?,?,?,?";
                    // perpare where ...
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
            sql += " where 1=1 and rpt_id = ? ";

            ArrayList<Parameter> paraList = new ArrayList<Parameter>();

            // perpare where ...
            paraList.add(new Parameter(Parameter.String, this.rptId));
            countCon.executeStatement(sql, paraList);
        }

        if (HPFW_Connection.HISTORY_ONLY.equals(countCon.getUpdateMode())
                || HPFW_Connection.DIRECT_WITH_HISTORY.equals(countCon.getUpdateMode())) {
            String histsql = "insert into ";
            histsql += thisTableName + thisTableHistName;
            histsql += " (HIST_SEQ,HIST_action,HIST_update_type,HIST_status,last_modify_by,last_modify_dt,create_by,create_dt,rpt_id) values (?,?,?,?,?,?,?,?,?)";
            ArrayList<Parameter> histParaList = new ArrayList<Parameter>();
            histParaList
                    .add(new Parameter(Parameter.String,
                            CommonDBUtils.getSequence(countCon, thisTableName + thisTableHistName)));
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
            histParaList.add(new Parameter(Parameter.String, this.rptId));
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
            if (rptId != null)
                sql += "rpt_id,";
            if (rptDesc != null)
                sql += "rpt_desc,";
            if (implClass != null)
                sql += "impl_class,";
            if (teamBatchRpt != null)
                sql += "team_batch_rpt,";
            if (spidBatchRpt != null)
                sql += "spid_batch_rpt,";
            if (batchRptFuncId != null)
                sql += "batch_rpt_func_id,";
            if (batchRptDefaultFormat != null)
                sql += "batch_rpt_default_format,";
            if (marsRpt != null)
                sql += "mars_rpt,";
            if (cmcRpt != null)
                sql += "cmc_rpt,";
            if (adhocRptFuncId != null)
                sql += "adhoc_rpt_func_id,";
            sql += "create_by,last_modify_by,create_dt,last_modify_dt) values (";

            // perpare set ...
            if (rptId != null)
                sql += "?,";
            if (rptDesc != null)
                sql += "?,";
            if (implClass != null)
                sql += "?,";
            if (teamBatchRpt != null)
                sql += "?,";
            if (spidBatchRpt != null)
                sql += "?,";
            if (batchRptFuncId != null)
                sql += "?,";
            if (batchRptDefaultFormat != null)
                sql += "?,";
            if (marsRpt != null)
                sql += "?,";
            if (cmcRpt != null)
                sql += "?,";
            if (adhocRptFuncId != null)
                sql += "?,";
            sql += "?,?,?,?) ";

            // perpare set ...
            if (rptId != null)
                paraList.add(new Parameter(Parameter.String, this.rptId));
            if (rptDesc != null)
                paraList.add(new Parameter(Parameter.String, this.rptDesc));
            if (implClass != null)
                paraList.add(new Parameter(Parameter.String, this.implClass));
            if (teamBatchRpt != null)
                paraList.add(new Parameter(Parameter.String, this.teamBatchRpt));
            if (spidBatchRpt != null)
                paraList.add(new Parameter(Parameter.String, this.spidBatchRpt));
            if (batchRptFuncId != null)
                paraList.add(new Parameter(Parameter.String, this.batchRptFuncId));
            if (batchRptDefaultFormat != null)
                paraList.add(new Parameter(Parameter.String, this.batchRptDefaultFormat));
            if (marsRpt != null)
                paraList.add(new Parameter(Parameter.String, this.marsRpt));
            if (cmcRpt != null)
                paraList.add(new Parameter(Parameter.String, this.cmcRpt));
            if (adhocRptFuncId != null)
                paraList.add(new Parameter(Parameter.String, this.adhocRptFuncId));
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
            if (rptId != null)
                hist_sql += "rpt_id,";
            if (rptDesc != null)
                hist_sql += "rpt_desc,";
            if (implClass != null)
                hist_sql += "impl_class,";
            if (teamBatchRpt != null)
                hist_sql += "team_batch_rpt,";
            if (spidBatchRpt != null)
                hist_sql += "spid_batch_rpt,";
            if (batchRptFuncId != null)
                hist_sql += "batch_rpt_func_id,";
            if (batchRptDefaultFormat != null)
                hist_sql += "batch_rpt_default_format,";
            if (marsRpt != null)
                hist_sql += "mars_rpt,";
            if (cmcRpt != null)
                hist_sql += "cmc_rpt,";
            if (adhocRptFuncId != null)
                hist_sql += "adhoc_rpt_func_id,";
            hist_sql += "create_by,last_modify_by,create_dt,last_modify_dt) values (?,?,?,?,";

            // perpare set ...
            if (rptId != null)
                hist_sql += "?,";
            if (rptDesc != null)
                hist_sql += "?,";
            if (implClass != null)
                hist_sql += "?,";
            if (teamBatchRpt != null)
                hist_sql += "?,";
            if (spidBatchRpt != null)
                hist_sql += "?,";
            if (batchRptFuncId != null)
                hist_sql += "?,";
            if (batchRptDefaultFormat != null)
                hist_sql += "?,";
            if (marsRpt != null)
                hist_sql += "?,";
            if (cmcRpt != null)
                hist_sql += "?,";
            if (adhocRptFuncId != null)
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

            // perpare set ...
            if (rptId != null)
                histParaList.add(new Parameter(Parameter.String, this.rptId));
            if (rptDesc != null)
                histParaList.add(new Parameter(Parameter.String, this.rptDesc));
            if (implClass != null)
                histParaList.add(new Parameter(Parameter.String, this.implClass));
            if (teamBatchRpt != null)
                histParaList.add(new Parameter(Parameter.String, this.teamBatchRpt));
            if (spidBatchRpt != null)
                histParaList.add(new Parameter(Parameter.String, this.spidBatchRpt));
            if (batchRptFuncId != null)
                histParaList.add(new Parameter(Parameter.String, this.batchRptFuncId));
            if (batchRptDefaultFormat != null)
                histParaList.add(new Parameter(Parameter.String, this.batchRptDefaultFormat));
            if (marsRpt != null)
                histParaList.add(new Parameter(Parameter.String, this.marsRpt));
            if (cmcRpt != null)
                histParaList.add(new Parameter(Parameter.String, this.cmcRpt));
            if (adhocRptFuncId != null)
                histParaList.add(new Parameter(Parameter.String, this.adhocRptFuncId));
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
                String sql = "select rpt_id from " + thisTableName + " " + whereClause;
                stmt = countCon.getConnectionPtr().prepareStatement(sql);
                CommonDBUtils.setStatement(stmt, whereParaL);
                rs = stmt.executeQuery();

                while (rs.next()) {
                    String histsql = "insert into ";
                    histsql += thisTableName + thisTableHistName;
                    histsql += " (HIST_SEQ,HIST_action,HIST_update_type,HIST_status,last_modify_by,last_modify_dt,create_by,create_dt,rpt_id,"
                            + selectString;
                    histsql += ") values (?,?,?,?,?,?,?,?,?";
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
            if (dirty_rptDesc)
                sql += "rpt_desc = ?,";
            if (dirty_implClass)
                sql += "impl_class = ?,";
            if (dirty_teamBatchRpt)
                sql += "team_batch_rpt = ?,";
            if (dirty_spidBatchRpt)
                sql += "spid_batch_rpt = ?,";
            if (dirty_batchRptFuncId)
                sql += "batch_rpt_func_id = ?,";
            if (dirty_batchRptDefaultFormat)
                sql += "batch_rpt_default_format = ?,";
            if (dirty_marsRpt)
                sql += "mars_rpt = ?,";
            if (dirty_cmcRpt)
                sql += "cmc_rpt = ?,";
            if (dirty_adhocRptFuncId)
                sql += "adhoc_rpt_func_id = ?,";

            sql += " last_modify_by = ?,last_modify_dt = ? where 1=1 and rpt_id = ? ";
            // perpare set ...
            if (dirty_rptDesc)
                paraList.add(new Parameter(Parameter.String, this.rptDesc));
            if (dirty_implClass)
                paraList.add(new Parameter(Parameter.String, this.implClass));
            if (dirty_teamBatchRpt)
                paraList.add(new Parameter(Parameter.String, this.teamBatchRpt));
            if (dirty_spidBatchRpt)
                paraList.add(new Parameter(Parameter.String, this.spidBatchRpt));
            if (dirty_batchRptFuncId)
                paraList.add(new Parameter(Parameter.String, this.batchRptFuncId));
            if (dirty_batchRptDefaultFormat)
                paraList.add(new Parameter(Parameter.String, this.batchRptDefaultFormat));
            if (dirty_marsRpt)
                paraList.add(new Parameter(Parameter.String, this.marsRpt));
            if (dirty_cmcRpt)
                paraList.add(new Parameter(Parameter.String, this.cmcRpt));
            if (dirty_adhocRptFuncId)
                paraList.add(new Parameter(Parameter.String, this.adhocRptFuncId));
            paraList.add(new Parameter(Parameter.String, countCon.getLastUpdBy()));
            paraList.add(new Parameter(Parameter.Timestamp, countCon.getLastUpTime()));
            // perpare where ...
            paraList.add(new Parameter(Parameter.String, this.rptId));
            countCon.executeStatement(sql, paraList);
        }

        if (HPFW_Connection.HISTORY_ONLY.equals(countCon.getUpdateMode())
                || HPFW_Connection.DIRECT_WITH_HISTORY.equals(countCon.getUpdateMode())) {
            ArrayList<Parameter> histParaList = new ArrayList<Parameter>();

            String hist_sql = "insert into ";
            hist_sql += thisTableName + thisTableHistName;
            hist_sql += " (HIST_SEQ,HIST_action,HIST_update_type,HIST_status,";

            // perpare set ...
            hist_sql += "rpt_id,";
            if (dirty_rptDesc)
                hist_sql += "rpt_desc,";
            if (dirty_implClass)
                hist_sql += "impl_class,";
            if (dirty_teamBatchRpt)
                hist_sql += "team_batch_rpt,";
            if (dirty_spidBatchRpt)
                hist_sql += "spid_batch_rpt,";
            if (dirty_batchRptFuncId)
                hist_sql += "batch_rpt_func_id,";
            if (dirty_batchRptDefaultFormat)
                hist_sql += "batch_rpt_default_format,";
            if (dirty_marsRpt)
                hist_sql += "mars_rpt,";
            if (dirty_cmcRpt)
                hist_sql += "cmc_rpt,";
            if (dirty_adhocRptFuncId)
                hist_sql += "adhoc_rpt_func_id,";
            hist_sql += "last_modify_by,last_modify_dt,create_by,create_dt) values (?,?,?,?,";

            // perpare set ...
            hist_sql += "?,";
            if (dirty_rptDesc)
                hist_sql += "?,";
            if (dirty_implClass)
                hist_sql += "?,";
            if (dirty_teamBatchRpt)
                hist_sql += "?,";
            if (dirty_spidBatchRpt)
                hist_sql += "?,";
            if (dirty_batchRptFuncId)
                hist_sql += "?,";
            if (dirty_batchRptDefaultFormat)
                hist_sql += "?,";
            if (dirty_marsRpt)
                hist_sql += "?,";
            if (dirty_cmcRpt)
                hist_sql += "?,";
            if (dirty_adhocRptFuncId)
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

            // perpare set ...
            histParaList.add(new Parameter(Parameter.String, this.rptId));
            if (dirty_rptDesc)
                histParaList.add(new Parameter(Parameter.String, this.rptDesc));
            if (dirty_implClass)
                histParaList.add(new Parameter(Parameter.String, this.implClass));
            if (dirty_teamBatchRpt)
                histParaList.add(new Parameter(Parameter.String, this.teamBatchRpt));
            if (dirty_spidBatchRpt)
                histParaList.add(new Parameter(Parameter.String, this.spidBatchRpt));
            if (dirty_batchRptFuncId)
                histParaList.add(new Parameter(Parameter.String, this.batchRptFuncId));
            if (dirty_batchRptDefaultFormat)
                histParaList.add(new Parameter(Parameter.String, this.batchRptDefaultFormat));
            if (dirty_marsRpt)
                histParaList.add(new Parameter(Parameter.String, this.marsRpt));
            if (dirty_cmcRpt)
                histParaList.add(new Parameter(Parameter.String, this.cmcRpt));
            if (dirty_adhocRptFuncId)
                histParaList.add(new Parameter(Parameter.String, this.adhocRptFuncId));
            histParaList.add(new Parameter(Parameter.String, countCon.getLastUpdBy()));
            histParaList.add(new Parameter(Parameter.Timestamp, countCon.getLastUpTime()));
            histParaList.add(new Parameter(Parameter.String, countCon.getLastUpdBy()));
            histParaList.add(new Parameter(Parameter.Timestamp, countCon.getLastUpTime()));
            countCon.executeStatement(hist_sql, histParaList, true);
        }
        this.forUpdate = false;
    }

    public static ArrayList<AdmRptInfo_> getResultList(HPFW_Connection countCon, String whereCluase,
            ArrayList<Parameter> paraL) throws SQLException {
        boolean needClose = countCon == null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<AdmRptInfo_> result = new ArrayList<AdmRptInfo_>();
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
                AdmRptInfo_ obj = new AdmRptInfo_();
                obj.rptId = rs.getString("rpt_id"); // String
                obj.rptDesc = rs.getString("rpt_desc"); // String
                obj.implClass = rs.getString("impl_class"); // String
                obj.lastModifyBy = rs.getString("last_modify_by"); // String
                obj.lastModifyDt = rs.getTimestamp("last_modify_dt"); // Timestamp
                obj.teamBatchRpt = rs.getString("team_batch_rpt"); // String
                obj.spidBatchRpt = rs.getString("spid_batch_rpt"); // String
                obj.batchRptFuncId = rs.getString("batch_rpt_func_id"); // String
                obj.batchRptDefaultFormat = rs.getString("batch_rpt_default_format"); // String
                obj.marsRpt = rs.getString("mars_rpt"); // String
                obj.cmcRpt = rs.getString("cmc_rpt"); // String
                obj.adhocRptFuncId = rs.getString("adhoc_rpt_func_id"); // String
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

    public void init(HPFW_Connection countCon, final String inrptId, boolean forUpdate)
            throws SQLException, NullPointerException {
        this.forUpdate = forUpdate;
        boolean needClose = countCon == null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql = "select * from " + thisTableName + " where 1=1 and rpt_id = ? ";
            if (forUpdate)
                sql += "for update";
            if (countCon == null)
                countCon = HPFW_Connection.getHPFW_Connection();
            stmt = countCon.getConnectionPtr().prepareStatement(sql);
            stmt.setString(1, inrptId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                rptId = rs.getString("rpt_id"); // String
                rptDesc = rs.getString("rpt_desc"); // String
                implClass = rs.getString("impl_class"); // String
                lastModifyBy = rs.getString("last_modify_by"); // String
                lastModifyDt = rs.getTimestamp("last_modify_dt"); // Timestamp
                teamBatchRpt = rs.getString("team_batch_rpt"); // String
                spidBatchRpt = rs.getString("spid_batch_rpt"); // String
                batchRptFuncId = rs.getString("batch_rpt_func_id"); // String
                batchRptDefaultFormat = rs.getString("batch_rpt_default_format"); // String
                marsRpt = rs.getString("mars_rpt"); // String
                cmcRpt = rs.getString("cmc_rpt"); // String
                adhocRptFuncId = rs.getString("adhoc_rpt_func_id"); // String
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
     * AdmRptInfo_ Destroyer
     */
    protected void finalize() throws Throwable {
        rptId = null;
        rptDesc = null;
        implClass = null;
        lastModifyBy = null;
        lastModifyDt = null;
        teamBatchRpt = null;
        spidBatchRpt = null;
        batchRptFuncId = null;
        batchRptDefaultFormat = null;
        marsRpt = null;
        cmcRpt = null;
        adhocRptFuncId = null;
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
     * Get rpt_desc
     */
    public String getRptDesc() {
        return rptDesc == null ? "" : rptDesc;
    }

    /**
     * Set rpt_desc
     */
    public void setRptDesc(final String inRptDesc) {
        rptDesc = inRptDesc;
        dirty_rptDesc = true;
    }

    /**
     * Get impl_class
     */
    public String getImplClass() {
        return implClass == null ? "" : implClass;
    }

    /**
     * Set impl_class
     */
    public void setImplClass(final String inImplClass) {
        implClass = inImplClass;
        dirty_implClass = true;
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
     * Get team_batch_rpt
     */
    public String getTeamBatchRpt() {
        return teamBatchRpt == null ? "" : teamBatchRpt;
    }

    /**
     * Set team_batch_rpt
     */
    public void setTeamBatchRpt(final String inTeamBatchRpt) {
        teamBatchRpt = inTeamBatchRpt;
        dirty_teamBatchRpt = true;
    }

    /**
     * Get spid_batch_rpt
     */
    public String getSpidBatchRpt() {
        return spidBatchRpt == null ? "" : spidBatchRpt;
    }

    /**
     * Set spid_batch_rpt
     */
    public void setSpidBatchRpt(final String inSpidBatchRpt) {
        spidBatchRpt = inSpidBatchRpt;
        dirty_spidBatchRpt = true;
    }

    /**
     * Get batch_rpt_func_id
     */
    public String getBatchRptFuncId() {
        return batchRptFuncId == null ? "" : batchRptFuncId;
    }

    /**
     * Set batch_rpt_func_id
     */
    public void setBatchRptFuncId(final String inBatchRptFuncId) {
        batchRptFuncId = inBatchRptFuncId;
        dirty_batchRptFuncId = true;
    }

    /**
     * Get batch_rpt_default_format
     */
    public String getBatchRptDefaultFormat() {
        return batchRptDefaultFormat == null ? "" : batchRptDefaultFormat;
    }

    /**
     * Set batch_rpt_default_format
     */
    public void setBatchRptDefaultFormat(final String inBatchRptDefaultFormat) {
        batchRptDefaultFormat = inBatchRptDefaultFormat;
        dirty_batchRptDefaultFormat = true;
    }

    /**
     * Get mars_rpt
     */
    public String getMarsRpt() {
        return marsRpt == null ? "" : marsRpt;
    }

    /**
     * Set mars_rpt
     */
    public void setMarsRpt(final String inMarsRpt) {
        marsRpt = inMarsRpt;
        dirty_marsRpt = true;
    }

    /**
     * Get cmc_rpt
     */
    public String getCmcRpt() {
        return cmcRpt == null ? "" : cmcRpt;
    }

    /**
     * Set cmc_rpt
     */
    public void setCmcRpt(final String inCmcRpt) {
        cmcRpt = inCmcRpt;
        dirty_cmcRpt = true;
    }

    /**
     * Get adhoc_rpt_func_id
     */
    public String getAdhocRptFuncId() {
        return adhocRptFuncId == null ? "" : adhocRptFuncId;
    }

    /**
     * Set adhoc_rpt_func_id
     */
    public void setAdhocRptFuncId(final String inAdhocRptFuncId) {
        adhocRptFuncId = inAdhocRptFuncId;
        dirty_adhocRptFuncId = true;
    }
}
