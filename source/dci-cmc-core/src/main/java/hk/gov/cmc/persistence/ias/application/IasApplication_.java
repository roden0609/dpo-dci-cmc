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

public class IasApplication_ implements Serializable {
    private static final long serialVersionUID = 1L;
    protected boolean initialized = false;

    private final static String thisTableName = "IAS_APPLICATION";
    private final static String thisTableHistName = "_H";
    private boolean forUpdate = false;
    protected String iasApplicationId = null;
    protected String portalId = null;
    protected String templateId = null;
    protected String templateVersion = null;
    protected String titleEn = null;
    protected String titleTc = null;
    protected String titleSc = null;
    protected String detailEn = null;
    protected String detailTc = null;
    protected String detailSc = null;
    protected String iasEsAppSuffixEn = null;
    protected String iasEsAppSuffixTc = null;
    protected String iasEsAppSuffixSc = null;
    protected Timestamp createDt = null;
    protected Timestamp lastModifyDt = null;
    protected String createBy = null;
    protected String lastModifyBy = null;
    protected String encInd = null;
    protected String encKeyStoreId = null;

    protected boolean dirty_iasApplicationId = false;
    protected boolean dirty_portalId = false;
    protected boolean dirty_templateId = false;
    protected boolean dirty_templateVersion = false;
    protected boolean dirty_titleEn = false;
    protected boolean dirty_titleTc = false;
    protected boolean dirty_titleSc = false;
    protected boolean dirty_detailEn = false;
    protected boolean dirty_detailTc = false;
    protected boolean dirty_detailSc = false;
    protected boolean dirty_iasEsAppSuffixEn = false;
    protected boolean dirty_iasEsAppSuffixTc = false;
    protected boolean dirty_iasEsAppSuffixSc = false;
    protected boolean dirty_createDt = false;
    protected boolean dirty_lastModifyDt = false;
    protected boolean dirty_createBy = false;
    protected boolean dirty_lastModifyBy = false;
    protected boolean dirty_encInd = false;
    protected boolean dirty_encKeyStoreId = false;

    public IasApplication_() {
        super();
    }

    public IasApplication_(HPFW_Connection countCon, String inIasApplicationId)
            throws SQLException, NullPointerException {
        this();
        init(countCon, inIasApplicationId, false);
    }

    public IasApplication_(HPFW_Connection countCon, String inIasApplicationId, boolean forUpdate)
            throws SQLException, NullPointerException {
        this();
        init(countCon, inIasApplicationId, forUpdate);
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
                String sql = "select ias_application_id from " + thisTableName + " " + whereCluase;
                stmt = countCon.getConnectionPtr().prepareStatement(sql);
                CommonDBUtils.setStatement(stmt, paraL);
                rs = stmt.executeQuery();

                while (rs.next()) {
                    String histsql = "insert into ";
                    histsql += thisTableName + thisTableHistName;
                    histsql += " (HIST_SEQ,HIST_action,HIST_update_type,HIST_status,HIST_server_id,last_modify_by,last_modify_dt,create_by,create_dt,ias_application_id";
                    histsql += ") values (?,?,?,?,?,?,?,?,?,?";
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
            sql += " where 1=1 and ias_application_id = ? ";

            ArrayList<Parameter> paraList = new ArrayList<Parameter>();

            // perpare where ...
            paraList.add(new Parameter(Parameter.String, this.iasApplicationId));
            countCon.executeStatement(sql, paraList);
        }

        if (HPFW_Connection.HISTORY_ONLY.equals(countCon.getUpdateMode())
                || HPFW_Connection.DIRECT_WITH_HISTORY.equals(countCon.getUpdateMode())) {
            String histsql = "insert into ";
            histsql += thisTableName + thisTableHistName;
            histsql += " (HIST_SEQ,HIST_action,HIST_update_type,HIST_status,HIST_server_id,last_modify_by,last_modify_dt,create_by,create_dt";
            histsql += ",ias_application_id";
            histsql += ") values (?,?,?,?,?,?,?,?,?";
            // perpare where ...
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
            histParaList.add(new Parameter(Parameter.String, this.iasApplicationId));
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
            if (iasApplicationId != null)
                sql += "ias_application_id,";
            if (portalId != null)
                sql += "portal_id,";
            if (templateId != null)
                sql += "template_id,";
            if (templateVersion != null)
                sql += "template_version,";
            if (titleEn != null)
                sql += "title_en,";
            if (titleTc != null)
                sql += "title_tc,";
            if (titleSc != null)
                sql += "title_sc,";
            if (detailEn != null)
                sql += "detail_en,";
            if (detailTc != null)
                sql += "detail_tc,";
            if (detailSc != null)
                sql += "detail_sc,";
            if (iasEsAppSuffixEn != null)
                sql += "ias_es_app_suffix_en,";
            if (iasEsAppSuffixTc != null)
                sql += "ias_es_app_suffix_tc,";
            if (iasEsAppSuffixSc != null)
                sql += "ias_es_app_suffix_sc,";
            if (encInd != null)
                sql += "enc_ind,";
            if (encKeyStoreId != null)
                sql += "enc_key_store_id,";
            sql += "create_by,last_modify_by,create_dt,last_modify_dt) values (";

            // perpare set ...
            if (iasApplicationId != null)
                sql += "?,";
            if (portalId != null)
                sql += "?,";
            if (templateId != null)
                sql += "?,";
            if (templateVersion != null)
                sql += "?,";
            if (titleEn != null)
                sql += "?,";
            if (titleTc != null)
                sql += "?,";
            if (titleSc != null)
                sql += "?,";
            if (detailEn != null)
                sql += "?,";
            if (detailTc != null)
                sql += "?,";
            if (detailSc != null)
                sql += "?,";
            if (iasEsAppSuffixEn != null)
                sql += "?,";
            if (iasEsAppSuffixTc != null)
                sql += "?,";
            if (iasEsAppSuffixSc != null)
                sql += "?,";
            if (encInd != null)
                sql += "?,";
            if (encKeyStoreId != null)
                sql += "?,";
            sql += "?,?,?,?) ";

            // perpare set ...
            if (iasApplicationId != null)
                paraList.add(new Parameter(Parameter.String, this.iasApplicationId));
            if (portalId != null)
                paraList.add(new Parameter(Parameter.String, this.portalId));
            if (templateId != null)
                paraList.add(new Parameter(Parameter.String, this.templateId));
            if (templateVersion != null)
                paraList.add(new Parameter(Parameter.String, this.templateVersion));
            if (titleEn != null)
                paraList.add(new Parameter(Parameter.String, this.titleEn));
            if (titleTc != null)
                paraList.add(new Parameter(Parameter.String, this.titleTc));
            if (titleSc != null)
                paraList.add(new Parameter(Parameter.String, this.titleSc));
            if (detailEn != null)
                paraList.add(new Parameter(Parameter.String, this.detailEn));
            if (detailTc != null)
                paraList.add(new Parameter(Parameter.String, this.detailTc));
            if (detailSc != null)
                paraList.add(new Parameter(Parameter.String, this.detailSc));
            if (iasEsAppSuffixEn != null)
                paraList.add(new Parameter(Parameter.String, this.iasEsAppSuffixEn));
            if (iasEsAppSuffixTc != null)
                paraList.add(new Parameter(Parameter.String, this.iasEsAppSuffixTc));
            if (iasEsAppSuffixSc != null)
                paraList.add(new Parameter(Parameter.String, this.iasEsAppSuffixSc));
            if (encInd != null)
                paraList.add(new Parameter(Parameter.String, this.encInd));
            if (encKeyStoreId != null)
                paraList.add(new Parameter(Parameter.String, this.encKeyStoreId));
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
            if (iasApplicationId != null)
                hist_sql += "ias_application_id,";
            if (portalId != null)
                hist_sql += "portal_id,";
            if (templateId != null)
                hist_sql += "template_id,";
            if (templateVersion != null)
                hist_sql += "template_version,";
            if (titleEn != null)
                hist_sql += "title_en,";
            if (titleTc != null)
                hist_sql += "title_tc,";
            if (titleSc != null)
                hist_sql += "title_sc,";
            if (detailEn != null)
                hist_sql += "detail_en,";
            if (detailTc != null)
                hist_sql += "detail_tc,";
            if (detailSc != null)
                hist_sql += "detail_sc,";
            if (iasEsAppSuffixEn != null)
                hist_sql += "ias_es_app_suffix_en,";
            if (iasEsAppSuffixTc != null)
                hist_sql += "ias_es_app_suffix_tc,";
            if (iasEsAppSuffixSc != null)
                hist_sql += "ias_es_app_suffix_sc,";
            if (encInd != null)
                hist_sql += "enc_ind,";
            if (encKeyStoreId != null)
                hist_sql += "enc_key_store_id,";
            hist_sql += "create_by,last_modify_by,create_dt,last_modify_dt) values (?,?,?,?,?,";

            // perpare set ...
            if (iasApplicationId != null)
                hist_sql += "?,";
            if (portalId != null)
                hist_sql += "?,";
            if (templateId != null)
                hist_sql += "?,";
            if (templateVersion != null)
                hist_sql += "?,";
            if (titleEn != null)
                hist_sql += "?,";
            if (titleTc != null)
                hist_sql += "?,";
            if (titleSc != null)
                hist_sql += "?,";
            if (detailEn != null)
                hist_sql += "?,";
            if (detailTc != null)
                hist_sql += "?,";
            if (detailSc != null)
                hist_sql += "?,";
            if (iasEsAppSuffixEn != null)
                hist_sql += "?,";
            if (iasEsAppSuffixTc != null)
                hist_sql += "?,";
            if (iasEsAppSuffixSc != null)
                hist_sql += "?,";
            if (encInd != null)
                hist_sql += "?,";
            if (encKeyStoreId != null)
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
            if (iasApplicationId != null)
                histParaList.add(new Parameter(Parameter.String, this.iasApplicationId));
            if (portalId != null)
                histParaList.add(new Parameter(Parameter.String, this.portalId));
            if (templateId != null)
                histParaList.add(new Parameter(Parameter.String, this.templateId));
            if (templateVersion != null)
                histParaList.add(new Parameter(Parameter.String, this.templateVersion));
            if (titleEn != null)
                histParaList.add(new Parameter(Parameter.String, this.titleEn));
            if (titleTc != null)
                histParaList.add(new Parameter(Parameter.String, this.titleTc));
            if (titleSc != null)
                histParaList.add(new Parameter(Parameter.String, this.titleSc));
            if (detailEn != null)
                histParaList.add(new Parameter(Parameter.String, this.detailEn));
            if (detailTc != null)
                histParaList.add(new Parameter(Parameter.String, this.detailTc));
            if (detailSc != null)
                histParaList.add(new Parameter(Parameter.String, this.detailSc));
            if (iasEsAppSuffixEn != null)
                histParaList.add(new Parameter(Parameter.String, this.iasEsAppSuffixEn));
            if (iasEsAppSuffixTc != null)
                histParaList.add(new Parameter(Parameter.String, this.iasEsAppSuffixTc));
            if (iasEsAppSuffixSc != null)
                histParaList.add(new Parameter(Parameter.String, this.iasEsAppSuffixSc));
            if (encInd != null)
                histParaList.add(new Parameter(Parameter.String, this.encInd));
            if (encKeyStoreId != null)
                histParaList.add(new Parameter(Parameter.String, this.encKeyStoreId));
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
                String sql = "select ias_application_id from " + thisTableName + " " + whereClause;
                stmt = countCon.getConnectionPtr().prepareStatement(sql);
                CommonDBUtils.setStatement(stmt, whereParaL);
                rs = stmt.executeQuery();

                while (rs.next()) {
                    String histsql = "insert into ";
                    histsql += thisTableName + thisTableHistName;
                    histsql += " (HIST_SEQ,HIST_action,HIST_update_type,HIST_status,HIST_server_id,last_modify_by,last_modify_dt,create_by,create_dt,ias_application_id,"
                            + selectString;
                    histsql += ") values (?,?,?,?,?,?,?,?,?,?";
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
            if (dirty_portalId)
                sql += "portal_id = ?,";
            if (dirty_templateId)
                sql += "template_id = ?,";
            if (dirty_templateVersion)
                sql += "template_version = ?,";
            if (dirty_titleEn)
                sql += "title_en = ?,";
            if (dirty_titleTc)
                sql += "title_tc = ?,";
            if (dirty_titleSc)
                sql += "title_sc = ?,";
            if (dirty_detailEn)
                sql += "detail_en = ?,";
            if (dirty_detailTc)
                sql += "detail_tc = ?,";
            if (dirty_detailSc)
                sql += "detail_sc = ?,";
            if (dirty_iasEsAppSuffixEn)
                sql += "ias_es_app_suffix_en = ?,";
            if (dirty_iasEsAppSuffixTc)
                sql += "ias_es_app_suffix_tc = ?,";
            if (dirty_iasEsAppSuffixSc)
                sql += "ias_es_app_suffix_sc = ?,";
            if (dirty_encInd)
                sql += "enc_ind = ?,";
            if (dirty_encKeyStoreId)
                sql += "enc_key_store_id = ?,";

            sql += " last_modify_by = ?,last_modify_dt = ? where 1=1 and ias_application_id = ? ";
            // perpare set ...
            if (dirty_portalId)
                paraList.add(new Parameter(Parameter.String, this.portalId));
            if (dirty_templateId)
                paraList.add(new Parameter(Parameter.String, this.templateId));
            if (dirty_templateVersion)
                paraList.add(new Parameter(Parameter.String, this.templateVersion));
            if (dirty_titleEn)
                paraList.add(new Parameter(Parameter.String, this.titleEn));
            if (dirty_titleTc)
                paraList.add(new Parameter(Parameter.String, this.titleTc));
            if (dirty_titleSc)
                paraList.add(new Parameter(Parameter.String, this.titleSc));
            if (dirty_detailEn)
                paraList.add(new Parameter(Parameter.String, this.detailEn));
            if (dirty_detailTc)
                paraList.add(new Parameter(Parameter.String, this.detailTc));
            if (dirty_detailSc)
                paraList.add(new Parameter(Parameter.String, this.detailSc));
            if (dirty_iasEsAppSuffixEn)
                paraList.add(new Parameter(Parameter.String, this.iasEsAppSuffixEn));
            if (dirty_iasEsAppSuffixTc)
                paraList.add(new Parameter(Parameter.String, this.iasEsAppSuffixTc));
            if (dirty_iasEsAppSuffixSc)
                paraList.add(new Parameter(Parameter.String, this.iasEsAppSuffixSc));
            if (dirty_encInd)
                paraList.add(new Parameter(Parameter.String, this.encInd));
            if (dirty_encKeyStoreId)
                paraList.add(new Parameter(Parameter.String, this.encKeyStoreId));
            paraList.add(new Parameter(Parameter.String, countCon.getLastUpdBy()));
            paraList.add(new Parameter(Parameter.Timestamp, countCon.getLastUpTime()));
            // perpare where ...
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
            hist_sql += "ias_application_id,";
            if (dirty_portalId)
                hist_sql += "portal_id,";
            if (dirty_templateId)
                hist_sql += "template_id,";
            if (dirty_templateVersion)
                hist_sql += "template_version,";
            if (dirty_titleEn)
                hist_sql += "title_en,";
            if (dirty_titleTc)
                hist_sql += "title_tc,";
            if (dirty_titleSc)
                hist_sql += "title_sc,";
            if (dirty_detailEn)
                hist_sql += "detail_en,";
            if (dirty_detailTc)
                hist_sql += "detail_tc,";
            if (dirty_detailSc)
                hist_sql += "detail_sc,";
            if (dirty_iasEsAppSuffixEn)
                hist_sql += "ias_es_app_suffix_en,";
            if (dirty_iasEsAppSuffixTc)
                hist_sql += "ias_es_app_suffix_tc,";
            if (dirty_iasEsAppSuffixSc)
                hist_sql += "ias_es_app_suffix_sc,";
            if (dirty_encInd)
                hist_sql += "enc_ind,";
            if (dirty_encKeyStoreId)
                hist_sql += "enc_key_store_id,";
            hist_sql += "last_modify_by,last_modify_dt,create_by,create_dt) values (?,?,?,?,?,";

            // perpare set ...
            hist_sql += "?,";
            if (dirty_portalId)
                hist_sql += "?,";
            if (dirty_templateId)
                hist_sql += "?,";
            if (dirty_templateVersion)
                hist_sql += "?,";
            if (dirty_titleEn)
                hist_sql += "?,";
            if (dirty_titleTc)
                hist_sql += "?,";
            if (dirty_titleSc)
                hist_sql += "?,";
            if (dirty_detailEn)
                hist_sql += "?,";
            if (dirty_detailTc)
                hist_sql += "?,";
            if (dirty_detailSc)
                hist_sql += "?,";
            if (dirty_iasEsAppSuffixEn)
                hist_sql += "?,";
            if (dirty_iasEsAppSuffixTc)
                hist_sql += "?,";
            if (dirty_iasEsAppSuffixSc)
                hist_sql += "?,";
            if (dirty_encInd)
                hist_sql += "?,";
            if (dirty_encKeyStoreId)
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
            histParaList.add(new Parameter(Parameter.String, this.iasApplicationId));
            if (dirty_portalId)
                histParaList.add(new Parameter(Parameter.String, this.portalId));
            if (dirty_templateId)
                histParaList.add(new Parameter(Parameter.String, this.templateId));
            if (dirty_templateVersion)
                histParaList.add(new Parameter(Parameter.String, this.templateVersion));
            if (dirty_titleEn)
                histParaList.add(new Parameter(Parameter.String, this.titleEn));
            if (dirty_titleTc)
                histParaList.add(new Parameter(Parameter.String, this.titleTc));
            if (dirty_titleSc)
                histParaList.add(new Parameter(Parameter.String, this.titleSc));
            if (dirty_detailEn)
                histParaList.add(new Parameter(Parameter.String, this.detailEn));
            if (dirty_detailTc)
                histParaList.add(new Parameter(Parameter.String, this.detailTc));
            if (dirty_detailSc)
                histParaList.add(new Parameter(Parameter.String, this.detailSc));
            if (dirty_iasEsAppSuffixEn)
                histParaList.add(new Parameter(Parameter.String, this.iasEsAppSuffixEn));
            if (dirty_iasEsAppSuffixTc)
                histParaList.add(new Parameter(Parameter.String, this.iasEsAppSuffixTc));
            if (dirty_iasEsAppSuffixSc)
                histParaList.add(new Parameter(Parameter.String, this.iasEsAppSuffixSc));
            if (dirty_encInd)
                histParaList.add(new Parameter(Parameter.String, this.encInd));
            if (dirty_encKeyStoreId)
                histParaList.add(new Parameter(Parameter.String, this.encKeyStoreId));
            histParaList.add(new Parameter(Parameter.String, countCon.getLastUpdBy()));
            histParaList.add(new Parameter(Parameter.Timestamp, countCon.getLastUpTime()));
            histParaList.add(new Parameter(Parameter.String, countCon.getLastUpdBy()));
            histParaList.add(new Parameter(Parameter.Timestamp, countCon.getLastUpTime()));
            countCon.executeStatement(hist_sql, histParaList, true);
        }
        this.forUpdate = false;
    }

    public static ArrayList<IasApplication_> getResultList(HPFW_Connection countCon, String whereCluase,
            ArrayList<Parameter> paraL) throws SQLException {
        boolean needClose = countCon == null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<IasApplication_> result = new ArrayList<IasApplication_>();
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
                IasApplication_ obj = new IasApplication_();
                obj.iasApplicationId = rs.getString("ias_application_id"); // String
                obj.portalId = rs.getString("portal_id"); // String
                obj.templateId = rs.getString("template_id"); // String
                obj.templateVersion = rs.getString("template_version"); // String
                obj.titleEn = rs.getString("title_en"); // String
                obj.titleTc = rs.getString("title_tc"); // String
                obj.titleSc = rs.getString("title_sc"); // String
                obj.detailEn = rs.getString("detail_en"); // String
                obj.detailTc = rs.getString("detail_tc"); // String
                obj.detailSc = rs.getString("detail_sc"); // String
                obj.iasEsAppSuffixEn = rs.getString("ias_es_app_suffix_en"); // String
                obj.iasEsAppSuffixTc = rs.getString("ias_es_app_suffix_tc"); // String
                obj.iasEsAppSuffixSc = rs.getString("ias_es_app_suffix_sc"); // String
                obj.createDt = rs.getTimestamp("create_dt"); // Timestamp
                obj.lastModifyDt = rs.getTimestamp("last_modify_dt"); // Timestamp
                obj.createBy = rs.getString("create_by"); // String
                obj.lastModifyBy = rs.getString("last_modify_by"); // String
                obj.encInd = rs.getString("enc_ind"); // String
                obj.encKeyStoreId = rs.getString("enc_key_store_id"); // String
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

    public void init(HPFW_Connection countCon, final String inIasApplicationId, boolean forUpdate)
            throws SQLException, NullPointerException {
        this.forUpdate = forUpdate;
        boolean needClose = countCon == null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql = "select * from " + thisTableName + " where 1=1 and ias_application_id = ? ";
            if (forUpdate)
                sql += "for update";
            if (countCon == null)
                countCon = HPFW_Connection.getHPFW_Connection();
            stmt = countCon.getConnectionPtr().prepareStatement(sql);
            stmt.setString(1, inIasApplicationId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                iasApplicationId = rs.getString("ias_application_id"); // String
                portalId = rs.getString("portal_id"); // String
                templateId = rs.getString("template_id"); // String
                templateVersion = rs.getString("template_version"); // String
                titleEn = rs.getString("title_en"); // String
                titleTc = rs.getString("title_tc"); // String
                titleSc = rs.getString("title_sc"); // String
                detailEn = rs.getString("detail_en"); // String
                detailTc = rs.getString("detail_tc"); // String
                detailSc = rs.getString("detail_sc"); // String
                iasEsAppSuffixEn = rs.getString("ias_es_app_suffix_en"); // String
                iasEsAppSuffixTc = rs.getString("ias_es_app_suffix_tc"); // String
                iasEsAppSuffixSc = rs.getString("ias_es_app_suffix_sc"); // String
                createDt = rs.getTimestamp("create_dt"); // Timestamp
                lastModifyDt = rs.getTimestamp("last_modify_dt"); // Timestamp
                createBy = rs.getString("create_by"); // String
                lastModifyBy = rs.getString("last_modify_by"); // String
                encInd = rs.getString("enc_ind"); // String
                encKeyStoreId = rs.getString("enc_key_store_id"); // String
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

    public String getIasApplicationId() {
        return iasApplicationId == null ? "" : iasApplicationId;
    }

    public void setIasApplicationId(final String inIasApplicationId) {
        iasApplicationId = inIasApplicationId;
        dirty_iasApplicationId = true;
    }

    public String getPortalId() {
        return portalId;
    }

    public void setPortalId(final String inPortalId) {
        portalId = inPortalId;
        dirty_portalId = true;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(final String inTemplateId) {
        templateId = inTemplateId;
        dirty_templateId = true;
    }

    public String getTemplateVersion() {
        return templateVersion;
    }

    public void setTemplateVersion(final String inTemplateVersion) {
        templateVersion = inTemplateVersion;
        dirty_templateVersion = true;
    }

    public String getTitleEn() {
        return titleEn;
    }

    public void setTitleEn(final String inTitleEn) {
        titleEn = inTitleEn;
        dirty_titleEn = true;
    }

    public String getTitleTc() {
        return titleTc;
    }

    public void setTitleTc(final String inTitleTc) {
        titleTc = inTitleTc;
        dirty_titleTc = true;
    }

    public String getTitleSc() {
        return titleSc;
    }

    public void setTitleSc(final String inTitleSc) {
        titleSc = inTitleSc;
        dirty_titleSc = true;
    }

    public String getDetailEn() {
        return detailEn;
    }

    public void setDetailEn(final String inDetailEn) {
        detailEn = inDetailEn;
        dirty_detailEn = true;
    }

    public String getDetailTc() {
        return detailTc;
    }

    public void setDetailTc(final String inDetailTc) {
        detailTc = inDetailTc;
        dirty_detailTc = true;
    }

    public String getDetailSc() {
        return detailSc;
    }

    public void setDetailSc(final String inDetailSc) {
        detailSc = inDetailSc;
        dirty_detailSc = true;
    }

    public String getIasEsAppSuffixEn() {
        return iasEsAppSuffixEn;
    }

    public void setIasEsAppSuffixEn(final String inIasEsAppSuffixEn) {
        iasEsAppSuffixEn = inIasEsAppSuffixEn;
        dirty_iasEsAppSuffixEn = true;
    }

    public String getIasEsAppSuffixTc() {
        return iasEsAppSuffixTc == null ? "" : iasEsAppSuffixTc;
    }

    public void setIasEsAppSuffixTc(final String inIasEsAppSuffixTc) {
        iasEsAppSuffixTc = inIasEsAppSuffixTc;
        dirty_iasEsAppSuffixTc = true;
    }

    public String getIasEsAppSuffixSc() {
        return iasEsAppSuffixSc;
    }

    public void setIasEsAppSuffixSc(final String inIasEsAppSuffixSc) {
        iasEsAppSuffixSc = inIasEsAppSuffixSc;
        dirty_iasEsAppSuffixSc = true;
    }

    public Timestamp getCreateDt() {
        return createDt;
    }

    public void setCreateDt(final Timestamp inCreateDt) {
        createDt = inCreateDt;
        dirty_createDt = true;
    }

    public Timestamp getLastModifyDt() {
        return lastModifyDt;
    }

    public void setLastModifyDt(final Timestamp inLastModifyDt) {
        lastModifyDt = inLastModifyDt;
        dirty_lastModifyDt = true;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(final String inCreateBy) {
        createBy = inCreateBy;
        dirty_createBy = true;
    }

    public String getLastModifyBy() {
        return lastModifyBy;
    }

    public void setLastModifyBy(final String inLastModifyBy) {
        lastModifyBy = inLastModifyBy;
        dirty_lastModifyBy = true;
    }

    public String getEncInd() {
        return encInd;
    }

    public void setEncInd(final String inEncInd) {
        encInd = inEncInd;
        dirty_encInd = true;
    }

    public String getEncKeyStoreId() {
        return encKeyStoreId;
    }

    public void setEncKeyStoreId(final String inEncKeyStoreId) {
        encKeyStoreId = inEncKeyStoreId;
        dirty_encKeyStoreId = true;
    }
}
