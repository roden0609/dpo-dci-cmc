package hk.gov.cmc.persistence.ias.message;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import hk.gov.cmc.persistence.connection.hpfw.CommonDBUtils;
import hk.gov.cmc.persistence.connection.hpfw.HPFW_Connection;
import hk.gov.cmc.persistence.connection.hpfw.Parameter;

public class IasMessage_ implements Serializable {

    private static final long serialVersionUID = 1L;
    private boolean initialized = false;

    private final static String thisTableName = "IAS_MESSAGE";
    private final static String thisTableHistName = "_H";
    private boolean forUpdate = false;

    private String iasMsgId = null;
    private String portalId = null;
    private String templateId = null;
    private String templateVersion = null;
    private String refId = null;
    private String subjectEn = null;
    private String contentEn = null;
    private String subjectTc = null;
    private String contentTc = null;
    private String subjectSc = null;
    private String contentSc = null;
    private String iasEsAppSuffixEn = null;
    private String iasEsAppSuffixTc = null;
    private String iasEsAppSuffixSc = null;
    private Timestamp createDt = null;
    private Timestamp lastModifyDt = null;
    private String createBy = null;
    private String lastModifyBy = null;
    private String encInd = null;
    private String encKeyStoreId = null;

    private boolean dirty_iasMsgId = false;
    private boolean dirty_portalId = false;
    private boolean dirty_templateId = false;
    private boolean dirty_templateVersion = false;
    private boolean dirty_refId = false;
    private boolean dirty_subjectEn = false;
    private boolean dirty_contentEn = false;
    private boolean dirty_subjectTc = false;
    private boolean dirty_contentTc = false;
    private boolean dirty_subjectSc = false;
    private boolean dirty_contentSc = false;
    private boolean dirty_iasEsAppSuffixEn = false;
    private boolean dirty_iasEsAppSuffixTc = false;
    private boolean dirty_iasEsAppSuffixSc = false;
    private boolean dirty_createDt = false;
    private boolean dirty_lastModifyDt = false;
    private boolean dirty_createBy = false;
    private boolean dirty_lastModifyBy = false;
    private boolean dirty_encInd = false;
    private boolean dirty_encKeyStoreId = false;

    /**
     * IasMessage_ Contructor
     */
    public IasMessage_() {
        super();
    }

    /**
     * IasMessage_ Constructor with specify PK
     */
    public IasMessage_(HPFW_Connection countCon, String iniasMsgId) throws SQLException, NullPointerException {
        this();
        init(countCon, iniasMsgId, false);
    }

    public IasMessage_(HPFW_Connection countCon, String iniasMsgId, boolean forUpdate)
            throws SQLException, NullPointerException {
        this();
        init(countCon, iniasMsgId, forUpdate);
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
                String sql = "select ias_msg_id from " + thisTableName + " " + whereCluase;
                stmt = countCon.getConnectionPtr().prepareStatement(sql);
                CommonDBUtils.setStatement(stmt, paraL);
                rs = stmt.executeQuery();

                while (rs.next()) {
                    String histsql = "insert into ";
                    histsql += thisTableName + thisTableHistName;
                    histsql += " (HIST_SEQ,HIST_action,HIST_update_type,HIST_status,HIST_server_id,last_modify_by,last_modify_dt,create_by,create_dt,ias_msg_id";
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
            sql += " where 1=1 and ias_msg_id = ? ";

            ArrayList<Parameter> paraList = new ArrayList<Parameter>();

            // perpare where ...
            paraList.add(new Parameter(Parameter.String, this.iasMsgId));
            countCon.executeStatement(sql, paraList);
        }

        if (HPFW_Connection.HISTORY_ONLY.equals(countCon.getUpdateMode())
                || HPFW_Connection.DIRECT_WITH_HISTORY.equals(countCon.getUpdateMode())) {
            String histsql = "insert into ";
            histsql += thisTableName + thisTableHistName;
            histsql += " (HIST_SEQ,HIST_action,HIST_update_type,HIST_status,HIST_server_id,last_modify_by,last_modify_dt,create_by,create_dt";
            histsql += ",IasMsgId";
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
            if (iasMsgId != null)
                sql += "ias_msg_id,";
            if (portalId != null)
                sql += "portal_id,";
            if (templateId != null)
                sql += "template_id,";
            if (templateVersion != null)
                sql += "template_version,";
            if (refId != null)
                sql += "ref_id,";
            if (subjectEn != null)
                sql += "subject_en,";
            if (contentEn != null)
                sql += "content_en,";
            if (subjectTc != null)
                sql += "subject_tc,";
            if (contentTc != null)
                sql += "content_tc,";
            if (subjectSc != null)
                sql += "subject_sc,";
            if (contentSc != null)
                sql += "content_sc,";
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
            if (iasMsgId != null)
                sql += "?,";
            if (portalId != null)
                sql += "?,";
            if (templateId != null)
                sql += "?,";
            if (templateVersion != null)
                sql += "?,";
            if (refId != null)
                sql += "?,";
            if (subjectEn != null)
                sql += "?,";
            if (contentEn != null)
                sql += "?,";
            if (subjectTc != null)
                sql += "?,";
            if (contentTc != null)
                sql += "?,";
            if (subjectSc != null)
                sql += "?,";
            if (contentSc != null)
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
            if (iasMsgId != null)
                paraList.add(new Parameter(Parameter.String, this.iasMsgId));
            if (portalId != null)
                paraList.add(new Parameter(Parameter.String, this.portalId));
            if (templateId != null)
                paraList.add(new Parameter(Parameter.String, this.templateId));
            if (templateVersion != null)
                paraList.add(new Parameter(Parameter.String, this.templateVersion));
            if (refId != null)
                paraList.add(new Parameter(Parameter.String, this.refId));
            if (subjectEn != null)
                paraList.add(new Parameter(Parameter.String, this.subjectEn));
            if (contentEn != null)
                paraList.add(new Parameter(Parameter.String, this.contentEn));
            if (subjectTc != null)
                paraList.add(new Parameter(Parameter.String, this.subjectTc));
            if (contentTc != null)
                paraList.add(new Parameter(Parameter.String, this.contentTc));
            if (subjectSc != null)
                paraList.add(new Parameter(Parameter.String, this.subjectSc));
            if (contentSc != null)
                paraList.add(new Parameter(Parameter.String, this.contentSc));
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
            if (iasMsgId != null)
                hist_sql += "ias_msg_id,";
            if (portalId != null)
                hist_sql += "portal_id,";
            if (templateId != null)
                hist_sql += "template_id,";
            if (templateVersion != null)
                hist_sql += "template_version,";
            if (refId != null)
                hist_sql += "ref_id,";
            if (subjectEn != null)
                hist_sql += "subject_en,";
            if (contentEn != null)
                hist_sql += "content_en,";
            if (subjectTc != null)
                hist_sql += "subject_tc,";
            if (contentTc != null)
                hist_sql += "content_tc,";
            if (subjectSc != null)
                hist_sql += "subject_sc,";
            if (contentSc != null)
                hist_sql += "content_sc,";
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
            if (iasMsgId != null)
                hist_sql += "?,";
            if (portalId != null)
                hist_sql += "?,";
            if (templateId != null)
                hist_sql += "?,";
            if (templateVersion != null)
                hist_sql += "?,";
            if (refId != null)
                hist_sql += "?,";
            if (subjectEn != null)
                hist_sql += "?,";
            if (contentEn != null)
                hist_sql += "?,";
            if (subjectTc != null)
                hist_sql += "?,";
            if (contentTc != null)
                hist_sql += "?,";
            if (subjectSc != null)
                hist_sql += "?,";
            if (contentSc != null)
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
            if (iasMsgId != null)
                histParaList.add(new Parameter(Parameter.String, this.iasMsgId));
            if (portalId != null)
                histParaList.add(new Parameter(Parameter.String, this.portalId));
            if (templateId != null)
                histParaList.add(new Parameter(Parameter.String, this.templateId));
            if (templateVersion != null)
                histParaList.add(new Parameter(Parameter.String, this.templateVersion));
            if (refId != null)
                histParaList.add(new Parameter(Parameter.String, this.refId));
            if (subjectEn != null)
                histParaList.add(new Parameter(Parameter.String, this.subjectEn));
            if (contentEn != null)
                histParaList.add(new Parameter(Parameter.String, this.contentEn));
            if (subjectTc != null)
                histParaList.add(new Parameter(Parameter.String, this.subjectTc));
            if (contentTc != null)
                histParaList.add(new Parameter(Parameter.String, this.contentTc));
            if (subjectSc != null)
                histParaList.add(new Parameter(Parameter.String, this.subjectSc));
            if (contentSc != null)
                histParaList.add(new Parameter(Parameter.String, this.contentSc));
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
                String sql = "select ias_msg_id from " + thisTableName + " " + whereClause;
                stmt = countCon.getConnectionPtr().prepareStatement(sql);
                CommonDBUtils.setStatement(stmt, whereParaL);
                rs = stmt.executeQuery();

                while (rs.next()) {
                    String histsql = "insert into ";
                    histsql += thisTableName + thisTableHistName;
                    histsql += " (HIST_SEQ,HIST_action,HIST_update_type,HIST_status,HIST_server_id,last_modify_by,last_modify_dt,create_by,create_dt,ias_msg_id,"
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
            if (dirty_refId)
                sql += "ref_id = ?,";
            if (dirty_subjectEn)
                sql += "subject_en = ?,";
            if (dirty_contentEn)
                sql += "content_en = ?,";
            if (dirty_subjectTc)
                sql += "subject_tc = ?,";
            if (dirty_contentTc)
                sql += "content_tc = ?,";
            if (dirty_subjectSc)
                sql += "subject_sc = ?,";
            if (dirty_contentSc)
                sql += "content_sc = ?,";
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

            sql += " last_modify_by = ?,last_modify_dt = ? where 1=1 and ias_msg_id = ? ";
            // perpare set ...
            if (dirty_portalId)
                paraList.add(new Parameter(Parameter.String, this.portalId));
            if (dirty_templateId)
                paraList.add(new Parameter(Parameter.String, this.templateId));
            if (dirty_templateVersion)
                paraList.add(new Parameter(Parameter.String, this.templateVersion));
            if (dirty_refId)
                paraList.add(new Parameter(Parameter.String, this.refId));
            if (dirty_subjectEn)
                paraList.add(new Parameter(Parameter.String, this.subjectEn));
            if (dirty_contentEn)
                paraList.add(new Parameter(Parameter.String, this.contentEn));
            if (dirty_subjectTc)
                paraList.add(new Parameter(Parameter.String, this.subjectTc));
            if (dirty_contentTc)
                paraList.add(new Parameter(Parameter.String, this.contentTc));
            if (dirty_subjectSc)
                paraList.add(new Parameter(Parameter.String, this.subjectSc));
            if (dirty_contentSc)
                paraList.add(new Parameter(Parameter.String, this.contentSc));
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
            hist_sql += "ias_msg_id,";
            if (dirty_portalId)
                hist_sql += "portal_id,";
            if (dirty_templateId)
                hist_sql += "template_id,";
            if (dirty_templateVersion)
                hist_sql += "template_version,";
            if (dirty_refId)
                hist_sql += "ref_id,";
            if (dirty_subjectEn)
                hist_sql += "subject_en,";
            if (dirty_contentEn)
                hist_sql += "content_en,";
            if (dirty_subjectTc)
                hist_sql += "subject_tc,";
            if (dirty_contentTc)
                hist_sql += "content_tc,";
            if (dirty_subjectSc)
                hist_sql += "subject_sc,";
            if (dirty_contentSc)
                hist_sql += "content_sc,";
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
            if (dirty_refId)
                hist_sql += "?,";
            if (dirty_subjectEn)
                hist_sql += "?,";
            if (dirty_contentEn)
                hist_sql += "?,";
            if (dirty_subjectTc)
                hist_sql += "?,";
            if (dirty_contentTc)
                hist_sql += "?,";
            if (dirty_subjectSc)
                hist_sql += "?,";
            if (dirty_contentSc)
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
            histParaList.add(new Parameter(Parameter.String, this.iasMsgId));
            if (dirty_portalId)
                histParaList.add(new Parameter(Parameter.String, this.portalId));
            if (dirty_templateId)
                histParaList.add(new Parameter(Parameter.String, this.templateId));
            if (dirty_templateVersion)
                histParaList.add(new Parameter(Parameter.String, this.templateVersion));
            if (dirty_refId)
                histParaList.add(new Parameter(Parameter.String, this.refId));
            if (dirty_subjectEn)
                histParaList.add(new Parameter(Parameter.String, this.subjectEn));
            if (dirty_contentEn)
                histParaList.add(new Parameter(Parameter.String, this.contentEn));
            if (dirty_subjectTc)
                histParaList.add(new Parameter(Parameter.String, this.subjectTc));
            if (dirty_contentTc)
                histParaList.add(new Parameter(Parameter.String, this.contentTc));
            if (dirty_subjectSc)
                histParaList.add(new Parameter(Parameter.String, this.subjectSc));
            if (dirty_contentSc)
                histParaList.add(new Parameter(Parameter.String, this.contentSc));
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

    public static ArrayList<IasMessage_> getResultList(HPFW_Connection countCon, String whereCluase,
            ArrayList<Parameter> paraL) throws SQLException {
        boolean needClose = countCon == null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<IasMessage_> result = new ArrayList<IasMessage_>();
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
                IasMessage_ obj = new IasMessage_();
                obj.iasMsgId = rs.getString("ias_msg_id"); // String
                obj.portalId = rs.getString("portal_id"); // String
                obj.templateId = rs.getString("template_id"); // String
                obj.templateVersion = rs.getString("template_version"); // String
                obj.refId = rs.getString("ref_id"); // String
                obj.subjectEn = rs.getString("subject_en"); // String
                obj.contentEn = rs.getString("content_en"); // String
                obj.subjectTc = rs.getString("subject_tc"); // String
                obj.contentTc = rs.getString("content_tc"); // String
                obj.subjectSc = rs.getString("subject_sc"); // String
                obj.contentSc = rs.getString("content_sc"); // String
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

    public void init(HPFW_Connection countCon, final String iniasMsgId, boolean forUpdate)
            throws SQLException, NullPointerException {
        this.forUpdate = forUpdate;
        boolean needClose = countCon == null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql = "select * from " + thisTableName + " where 1=1 and ias_msg_id = ? ";
            if (forUpdate)
                sql += "for update";
            if (countCon == null)
                countCon = HPFW_Connection.getHPFW_Connection();
            stmt = countCon.getConnectionPtr().prepareStatement(sql);
            stmt.setString(1, iniasMsgId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                iasMsgId = rs.getString("ias_msg_id"); // String
                portalId = rs.getString("portal_id"); // String
                templateId = rs.getString("template_id"); // String
                templateVersion = rs.getString("template_version"); // String
                refId = rs.getString("ref_id"); // String
                subjectEn = rs.getString("subject_en"); // String
                contentEn = rs.getString("content_en"); // String
                subjectTc = rs.getString("subject_tc"); // String
                contentTc = rs.getString("content_tc"); // String
                subjectSc = rs.getString("subject_sc"); // String
                contentSc = rs.getString("content_sc"); // String
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
    // }INIT

    /**
     * IasMessage_ Destroyer
     */
    protected void finalize() throws Throwable {
        iasMsgId = null;
        portalId = null;
        templateId = null;
        templateVersion = null;
        refId = null;
        subjectEn = null;
        contentEn = null;
        subjectTc = null;
        contentTc = null;
        subjectSc = null;
        contentSc = null;
        iasEsAppSuffixEn = null;
        iasEsAppSuffixTc = null;
        iasEsAppSuffixSc = null;
        createDt = null;
        lastModifyDt = null;
        createBy = null;
        lastModifyBy = null;
        encInd = null;
        encKeyStoreId = null;
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
     * Get portal_id
     */
    public String getPortalId() {
        return portalId == null ? "" : portalId;
    }

    /**
     * Set portal_id
     */
    public void setPortalId(final String inPortalId) {
        portalId = inPortalId;
        dirty_portalId = true;
    }

    /**
     * Get template_id
     */
    public String getTemplateId() {
        return templateId == null ? "" : templateId;
    }

    /**
     * Set template_id
     */
    public void setTemplateId(final String inTemplateId) {
        templateId = inTemplateId;
        dirty_templateId = true;
    }

    /**
     * Get template_version
     */
    public String getTemplateVersion() {
        return templateVersion == null ? "" : templateVersion;
    }

    /**
     * Set template_version
     */
    public void setTemplateVersion(final String inTemplateVersion) {
        templateVersion = inTemplateVersion;
        dirty_templateVersion = true;
    }

    /**
     * Get ref_id
     */
    public String getRefId() {
        return refId == null ? "" : refId;
    }

    /**
     * Set ref_id
     */
    public void setRefId(final String inRefId) {
        refId = inRefId;
        dirty_refId = true;
    }

    /**
     * Get subject_en
     */
    public String getSubjectEn() {
        return subjectEn == null ? "" : subjectEn;
    }

    /**
     * Set subject_en
     */
    public void setSubjectEn(final String inSubjectEn) {
        subjectEn = inSubjectEn;
        dirty_subjectEn = true;
    }

    /**
     * Get content_en
     */
    public String getContentEn() {
        return contentEn == null ? "" : contentEn;
    }

    /**
     * Set content_en
     */
    public void setContentEn(final String inContentEn) {
        contentEn = inContentEn;
        dirty_contentEn = true;
    }

    /**
     * Get subject_tc
     */
    public String getSubjectTc() {
        return subjectTc == null ? "" : subjectTc;
    }

    /**
     * Set subject_tc
     */
    public void setSubjectTc(final String inSubjectTc) {
        subjectTc = inSubjectTc;
        dirty_subjectTc = true;
    }

    /**
     * Get content_tc
     */
    public String getContentTc() {
        return contentTc == null ? "" : contentTc;
    }

    /**
     * Set content_tc
     */
    public void setContentTc(final String inContentTc) {
        contentTc = inContentTc;
        dirty_contentTc = true;
    }

    /**
     * Get subject_sc
     */
    public String getSubjectSc() {
        return subjectSc == null ? "" : subjectSc;
    }

    /**
     * Set subject_sc
     */
    public void setSubjectSc(final String inSubjectSc) {
        subjectSc = inSubjectSc;
        dirty_subjectSc = true;
    }

    /**
     * Get content_sc
     */
    public String getContentSc() {
        return contentSc == null ? "" : contentSc;
    }

    /**
     * Set content_sc
     */
    public void setContentSc(final String inContentSc) {
        contentSc = inContentSc;
        dirty_contentSc = true;
    }

    /**
     * Get ias_es_app_suffix_en
     */
    public String getIasEsAppSuffixEn() {
        return iasEsAppSuffixEn == null ? "" : iasEsAppSuffixEn;
    }

    /**
     * Set ias_es_app_suffix_en
     */
    public void setIasEsAppSuffixEn(final String inIasEsAppSuffixEn) {
        iasEsAppSuffixEn = inIasEsAppSuffixEn;
        dirty_iasEsAppSuffixEn = true;
    }

    /**
     * Get ias_es_app_suffix_tc
     */
    public String getIasEsAppSuffixTc() {
        return iasEsAppSuffixTc == null ? "" : iasEsAppSuffixTc;
    }

    /**
     * Set ias_es_app_suffix_tc
     */
    public void setIasEsAppSuffixTc(final String inIasEsAppSuffixTc) {
        iasEsAppSuffixTc = inIasEsAppSuffixTc;
        dirty_iasEsAppSuffixTc = true;
    }

    /**
     * Get ias_es_app_suffix_sc
     */
    public String getIasEsAppSuffixSc() {
        return iasEsAppSuffixSc == null ? "" : iasEsAppSuffixSc;
    }

    /**
     * Set ias_es_app_suffix_sc
     */
    public void setIasEsAppSuffixSc(final String inIasEsAppSuffixSc) {
        iasEsAppSuffixSc = inIasEsAppSuffixSc;
        dirty_iasEsAppSuffixSc = true;
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

    /**
     * Get enc_ind
     */
    public String getEncInd() {
        return encInd == null ? "" : encInd;
    }

    /**
     * Set enc_ind
     */
    public void setEncInd(final String inEncInd) {
        encInd = inEncInd;
        dirty_encInd = true;
    }

    /**
     * Get enc_key_store_id
     */
    public String getEncKeyStoreId() {
        return encKeyStoreId == null ? "" : encKeyStoreId;
    }

    /**
     * Set enc_key_store_id
     */
    public void setEncKeyStoreId(final String inEncKeyStoreId) {
        encKeyStoreId = inEncKeyStoreId;
        dirty_encKeyStoreId = true;
    }
}
