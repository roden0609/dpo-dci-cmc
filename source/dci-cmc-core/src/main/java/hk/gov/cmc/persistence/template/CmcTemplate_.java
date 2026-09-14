package hk.gov.cmc.persistence.template;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import hk.gov.cmc.persistence.connection.hpfw.CommonDBUtils;
import hk.gov.cmc.persistence.connection.hpfw.HPFW_Connection;
import hk.gov.cmc.persistence.connection.hpfw.Parameter;

public class CmcTemplate_ implements Serializable {

  private static final long serialVersionUID = 1L;
  private boolean initialized = false;

  private final static String thisTableName = "CMC_TEMPLATE";
  private final static String thisTableHistName = "_H";

  private boolean forUpdate = false;
  private String templateId = null;
  private String templateVersion = null;
  private String serviceProviderId = null;
  private String templateType = null;
  private String subjectEn = null;
  private String subjectTc = null;
  private String subjectSc = null;
  private String contentEn = null;
  private String contentTc = null;
  private String contentSc = null;
  private String emailInd = null;
  private String emailSubjectEn = null;
  private String emailSubjectTc = null;
  private String emailSubjectSc = null;
  private String emailContentEn = null;
  private String emailContentTc = null;
  private String emailContentSc = null;
  private String status = null;
  private String autoCompleteInd = null;
  private String lockInd = null;
  private String alertEmailInd = null;
  private Timestamp createDt = null;
  private Timestamp lastModifyDt = null;
  private String createBy = null;
  private String lastModifyBy = null;
  private String mobileMsgInd = null;
  private String mobileSubjectEn = null;
  private String mobileSubjectTc = null;
  private String mobileSubjectSc = null;
  private String mobileContentEn = null;
  private String mobileContentTc = null;
  private String mobileContentSc = null;
  private String trustSpInd = null;
  private Integer mnotExpiryTime = null;
  private String notiPriority = null;
  private String iasInd = null;
  private String iasOptCheckInd = null;
  private String iasShowEsAppBtn = null;
  private String iasShowEsSetBtn = null;
  private String iasSubjectEn = null;
  private String iasSubjectTc = null;
  private String iasSubjectSc = null;
  private String iasContentEn = null;
  private String iasContentTc = null;
  private String iasContentSc = null;
  private String iasEsAppSuffixEn = null;
  private String iasEsAppSuffixTc = null;
  private String iasEsAppSuffixSc = null;

  private boolean dirty_templateId = false;
  private boolean dirty_templateVersion = false;
  private boolean dirty_serviceProviderId = false;
  private boolean dirty_templateType = false;
  private boolean dirty_subjectEn = false;
  private boolean dirty_subjectTc = false;
  private boolean dirty_subjectSc = false;
  private boolean dirty_contentEn = false;
  private boolean dirty_contentTc = false;
  private boolean dirty_contentSc = false;
  private boolean dirty_emailInd = false;
  private boolean dirty_emailSubjectEn = false;
  private boolean dirty_emailSubjectTc = false;
  private boolean dirty_emailSubjectSc = false;
  private boolean dirty_emailContentEn = false;
  private boolean dirty_emailContentTc = false;
  private boolean dirty_emailContentSc = false;
  private boolean dirty_status = false;
  private boolean dirty_autoCompleteInd = false;
  private boolean dirty_lockInd = false;
  private boolean dirty_alertEmailInd = false;
  private boolean dirty_createDt = false;
  private boolean dirty_lastModifyDt = false;
  private boolean dirty_createBy = false;
  private boolean dirty_lastModifyBy = false;
  private boolean dirty_mobileMsgInd = false;
  private boolean dirty_mobileSubjectEn = false;
  private boolean dirty_mobileSubjectTc = false;
  private boolean dirty_mobileSubjectSc = false;
  private boolean dirty_mobileContentEn = false;
  private boolean dirty_mobileContentTc = false;
  private boolean dirty_mobileContentSc = false;
  private boolean dirty_trustSpInd = false;
  private boolean dirty_mnotExpiryTime = false;
  private boolean dirty_notiPriority = false;
  private boolean dirty_iasInd = false;
  private boolean dirty_iasOptCheckInd = false;
  private boolean dirty_iasShowEsAppBtn = false;
  private boolean dirty_iasShowEsSetBtn = false;
  private boolean dirty_iasSubjectEn = false;
  private boolean dirty_iasSubjectTc = false;
  private boolean dirty_iasSubjectSc = false;
  private boolean dirty_iasContentEn = false;
  private boolean dirty_iasContentTc = false;
  private boolean dirty_iasContentSc = false;
  private boolean dirty_iasEsAppSuffixEn = false;
  private boolean dirty_iasEsAppSuffixTc = false;
  private boolean dirty_iasEsAppSuffixSc = false;

  public CmcTemplate_() {
    super();
  }

  public CmcTemplate_(HPFW_Connection countCon, String intemplateId, String intemplateVersion)
      throws SQLException, NullPointerException {
    this();
    init(countCon, intemplateId, intemplateVersion, false);
  }

  public CmcTemplate_(HPFW_Connection countCon, String intemplateId, String intemplateVersion, boolean forUpdate)
      throws SQLException, NullPointerException {
    this();
    init(countCon, intemplateId, intemplateVersion, forUpdate);
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
        String sql = "select template_id,template_version from " + thisTableName + " " + whereCluase;
        stmt = countCon.getConnectionPtr().prepareStatement(sql);
        CommonDBUtils.setStatement(stmt, paraL);
        rs = stmt.executeQuery();

        while (rs.next()) {
          String histsql = "insert into ";
          histsql += thisTableName + thisTableHistName;
          histsql += " (HIST_SEQ,HIST_action,HIST_update_type,HIST_status,HIST_server_id,last_modify_by,last_modify_dt,create_by,create_dt,template_id,template_version";
          histsql += ") values (?,?,?,?,?,?,?,?,?,?";
          // perpare where ...
          histsql += ",?";
          histsql += ",?";
          histsql += ")";
          ArrayList<Parameter> histParaList = new ArrayList<Parameter>();
          histParaList.add(
              new Parameter(Parameter.String, CommonDBUtils.getSequence(countCon, thisTableName + thisTableHistName)));
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
      sql += " where 1=1 and template_id = ?  and template_version = ? ";

      ArrayList<Parameter> paraList = new ArrayList<Parameter>();

      // perpare where ...
      paraList.add(new Parameter(Parameter.String, this.templateId));
      paraList.add(new Parameter(Parameter.String, this.templateVersion));
      countCon.executeStatement(sql, paraList);
    }

    if (HPFW_Connection.HISTORY_ONLY.equals(countCon.getUpdateMode())
        || HPFW_Connection.DIRECT_WITH_HISTORY.equals(countCon.getUpdateMode())) {
      String histsql = "insert into ";
      histsql += thisTableName + thisTableHistName;
      histsql += " (HIST_SEQ,HIST_action,HIST_update_type,HIST_status,HIST_server_id,last_modify_by,last_modify_dt,create_by,create_dt";
      histsql += ",TemplateId";
      histsql += ",TemplateVersion";
      histsql += ") values (?,?,?,?,?,?,?,?,?";
      // perpare where ...
      histsql += ",?";
      histsql += ",?";
      histsql += ")";
      ArrayList<Parameter> histParaList = new ArrayList<Parameter>();
      histParaList
          .add(new Parameter(Parameter.String, CommonDBUtils.getSequence(countCon, thisTableName + thisTableHistName)));
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
      histParaList.add(new Parameter(Parameter.String, this.templateId));
      histParaList.add(new Parameter(Parameter.String, this.templateVersion));
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
      if (templateId != null)
        sql += "template_id,";
      if (templateVersion != null)
        sql += "template_version,";
      if (serviceProviderId != null)
        sql += "service_provider_id,";
      if (templateType != null)
        sql += "template_type,";
      if (subjectEn != null)
        sql += "subject_en,";
      if (subjectTc != null)
        sql += "subject_tc,";
      if (subjectSc != null)
        sql += "subject_sc,";
      if (contentEn != null)
        sql += "content_en,";
      if (contentTc != null)
        sql += "content_tc,";
      if (contentSc != null)
        sql += "content_sc,";
      if (emailInd != null)
        sql += "email_ind,";
      if (emailSubjectEn != null)
        sql += "email_subject_en,";
      if (emailSubjectTc != null)
        sql += "email_subject_tc,";
      if (emailSubjectSc != null)
        sql += "email_subject_sc,";
      if (emailContentEn != null)
        sql += "email_content_en,";
      if (emailContentTc != null)
        sql += "email_content_tc,";
      if (emailContentSc != null)
        sql += "email_content_sc,";
      if (status != null)
        sql += "status,";
      if (autoCompleteInd != null)
        sql += "auto_complete_ind,";
      if (lockInd != null)
        sql += "lock_ind,";
      if (alertEmailInd != null)
        sql += "alert_email_ind,";
      if (mobileMsgInd != null)
        sql += "mobile_msg_ind,";
      if (mobileSubjectEn != null)
        sql += "mobile_subject_en,";
      if (mobileSubjectTc != null)
        sql += "mobile_subject_tc,";
      if (mobileSubjectSc != null)
        sql += "mobile_subject_sc,";
      if (mobileContentEn != null)
        sql += "mobile_content_en,";
      if (mobileContentTc != null)
        sql += "mobile_content_tc,";
      if (mobileContentSc != null)
        sql += "mobile_content_sc,";
      if (trustSpInd != null)
        sql += "trust_sp_ind,";
      if (mnotExpiryTime != null)
        sql += "mnot_expiry_time,";
      if (notiPriority != null)
        sql += "noti_priority,";
      if (iasInd != null)
        sql += "ias_ind,";
      if (iasOptCheckInd != null)
        sql += "ias_opt_check_ind,";
      if (iasShowEsAppBtn != null)
        sql += "ias_show_es_app_btn,";
      if (iasShowEsSetBtn != null)
        sql += "ias_show_es_set_btn,";
      if (iasSubjectEn != null)
        sql += "ias_subject_en,";
      if (iasSubjectTc != null)
        sql += "ias_subject_tc,";
      if (iasSubjectSc != null)
        sql += "ias_subject_sc,";
      if (iasContentEn != null)
        sql += "ias_content_en,";
      if (iasContentTc != null)
        sql += "ias_content_tc,";
      if (iasContentSc != null)
        sql += "ias_content_sc,";
      if (iasEsAppSuffixEn != null)
        sql += "ias_es_app_suffix_en,";
      if (iasEsAppSuffixTc != null)
        sql += "ias_es_app_suffix_tc,";
      if (iasEsAppSuffixSc != null)
        sql += "ias_es_app_suffix_sc,";
      sql += "create_by,last_modify_by,create_dt,last_modify_dt) values (";

      // perpare set ...
      if (templateId != null)
        sql += "?,";
      if (templateVersion != null)
        sql += "?,";
      if (serviceProviderId != null)
        sql += "?,";
      if (templateType != null)
        sql += "?,";
      if (subjectEn != null)
        sql += "?,";
      if (subjectTc != null)
        sql += "?,";
      if (subjectSc != null)
        sql += "?,";
      if (contentEn != null)
        sql += "?,";
      if (contentTc != null)
        sql += "?,";
      if (contentSc != null)
        sql += "?,";
      if (emailInd != null)
        sql += "?,";
      if (emailSubjectEn != null)
        sql += "?,";
      if (emailSubjectTc != null)
        sql += "?,";
      if (emailSubjectSc != null)
        sql += "?,";
      if (emailContentEn != null)
        sql += "?,";
      if (emailContentTc != null)
        sql += "?,";
      if (emailContentSc != null)
        sql += "?,";
      if (status != null)
        sql += "?,";
      if (autoCompleteInd != null)
        sql += "?,";
      if (lockInd != null)
        sql += "?,";
      if (alertEmailInd != null)
        sql += "?,";
      if (mobileMsgInd != null)
        sql += "?,";
      if (mobileSubjectEn != null)
        sql += "?,";
      if (mobileSubjectTc != null)
        sql += "?,";
      if (mobileSubjectSc != null)
        sql += "?,";
      if (mobileContentEn != null)
        sql += "?,";
      if (mobileContentTc != null)
        sql += "?,";
      if (mobileContentSc != null)
        sql += "?,";
      if (trustSpInd != null)
        sql += "?,";
      if (mnotExpiryTime != null)
        sql += "?,";
      if (notiPriority != null)
        sql += "?,";
      if (iasInd != null)
        sql += "?,";
      if (iasOptCheckInd != null)
        sql += "?,";
      if (iasShowEsAppBtn != null)
        sql += "?,";
      if (iasShowEsSetBtn != null)
        sql += "?,";
      if (iasSubjectEn != null)
        sql += "?,";
      if (iasSubjectTc != null)
        sql += "?,";
      if (iasSubjectSc != null)
        sql += "?,";
      if (iasContentEn != null)
        sql += "?,";
      if (iasContentTc != null)
        sql += "?,";
      if (iasContentSc != null)
        sql += "?,";
      if (iasEsAppSuffixEn != null)
        sql += "?,";
      if (iasEsAppSuffixTc != null)
        sql += "?,";
      if (iasEsAppSuffixSc != null)
        sql += "?,";
      sql += "?,?,?,?) ";

      // perpare set ...
      if (templateId != null)
        paraList.add(new Parameter(Parameter.String, this.templateId));
      if (templateVersion != null)
        paraList.add(new Parameter(Parameter.String, this.templateVersion));
      if (serviceProviderId != null)
        paraList.add(new Parameter(Parameter.String, this.serviceProviderId));
      if (templateType != null)
        paraList.add(new Parameter(Parameter.String, this.templateType));
      if (subjectEn != null)
        paraList.add(new Parameter(Parameter.String, this.subjectEn));
      if (subjectTc != null)
        paraList.add(new Parameter(Parameter.String, this.subjectTc));
      if (subjectSc != null)
        paraList.add(new Parameter(Parameter.String, this.subjectSc));
      if (contentEn != null)
        paraList.add(new Parameter(Parameter.String, this.contentEn));
      if (contentTc != null)
        paraList.add(new Parameter(Parameter.String, this.contentTc));
      if (contentSc != null)
        paraList.add(new Parameter(Parameter.String, this.contentSc));
      if (emailInd != null)
        paraList.add(new Parameter(Parameter.String, this.emailInd));
      if (emailSubjectEn != null)
        paraList.add(new Parameter(Parameter.String, this.emailSubjectEn));
      if (emailSubjectTc != null)
        paraList.add(new Parameter(Parameter.String, this.emailSubjectTc));
      if (emailSubjectSc != null)
        paraList.add(new Parameter(Parameter.String, this.emailSubjectSc));
      if (emailContentEn != null)
        paraList.add(new Parameter(Parameter.String, this.emailContentEn));
      if (emailContentTc != null)
        paraList.add(new Parameter(Parameter.String, this.emailContentTc));
      if (emailContentSc != null)
        paraList.add(new Parameter(Parameter.String, this.emailContentSc));
      if (status != null)
        paraList.add(new Parameter(Parameter.String, this.status));
      if (autoCompleteInd != null)
        paraList.add(new Parameter(Parameter.String, this.autoCompleteInd));
      if (lockInd != null)
        paraList.add(new Parameter(Parameter.String, this.lockInd));
      if (alertEmailInd != null)
        paraList.add(new Parameter(Parameter.String, this.alertEmailInd));
      if (mobileMsgInd != null)
        paraList.add(new Parameter(Parameter.String, this.mobileMsgInd));
      if (mobileSubjectEn != null)
        paraList.add(new Parameter(Parameter.String, this.mobileSubjectEn));
      if (mobileSubjectTc != null)
        paraList.add(new Parameter(Parameter.String, this.mobileSubjectTc));
      if (mobileSubjectSc != null)
        paraList.add(new Parameter(Parameter.String, this.mobileSubjectSc));
      if (mobileContentEn != null)
        paraList.add(new Parameter(Parameter.String, this.mobileContentEn));
      if (mobileContentTc != null)
        paraList.add(new Parameter(Parameter.String, this.mobileContentTc));
      if (mobileContentSc != null)
        paraList.add(new Parameter(Parameter.String, this.mobileContentSc));
      if (trustSpInd != null)
        paraList.add(new Parameter(Parameter.String, this.trustSpInd));
      if (mnotExpiryTime != null)
        paraList.add(new Parameter(Parameter.Integer, this.mnotExpiryTime));
      if (notiPriority != null)
        paraList.add(new Parameter(Parameter.String, this.notiPriority));
      if (iasInd != null)
        paraList.add(new Parameter(Parameter.String, this.iasInd));
      if (iasOptCheckInd != null)
        paraList.add(new Parameter(Parameter.String, this.iasOptCheckInd));
      if (iasShowEsAppBtn != null)
        paraList.add(new Parameter(Parameter.String, this.iasShowEsAppBtn));
      if (iasShowEsSetBtn != null)
        paraList.add(new Parameter(Parameter.String, this.iasShowEsSetBtn));
      if (iasSubjectEn != null)
        paraList.add(new Parameter(Parameter.String, this.iasSubjectEn));
      if (iasSubjectTc != null)
        paraList.add(new Parameter(Parameter.String, this.iasSubjectTc));
      if (iasSubjectSc != null)
        paraList.add(new Parameter(Parameter.String, this.iasSubjectSc));
      if (iasContentEn != null)
        paraList.add(new Parameter(Parameter.String, this.iasContentEn));
      if (iasContentTc != null)
        paraList.add(new Parameter(Parameter.String, this.iasContentTc));
      if (iasContentSc != null)
        paraList.add(new Parameter(Parameter.String, this.iasContentSc));
      if (iasEsAppSuffixEn != null)
        paraList.add(new Parameter(Parameter.String, this.iasEsAppSuffixEn));
      if (iasEsAppSuffixTc != null)
        paraList.add(new Parameter(Parameter.String, this.iasEsAppSuffixTc));
      if (iasEsAppSuffixSc != null)
        paraList.add(new Parameter(Parameter.String, this.iasEsAppSuffixSc));
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
      if (templateId != null)
        hist_sql += "template_id,";
      if (templateVersion != null)
        hist_sql += "template_version,";
      if (serviceProviderId != null)
        hist_sql += "service_provider_id,";
      if (templateType != null)
        hist_sql += "template_type,";
      if (subjectEn != null)
        hist_sql += "subject_en,";
      if (subjectTc != null)
        hist_sql += "subject_tc,";
      if (subjectSc != null)
        hist_sql += "subject_sc,";
      if (contentEn != null)
        hist_sql += "content_en,";
      if (contentTc != null)
        hist_sql += "content_tc,";
      if (contentSc != null)
        hist_sql += "content_sc,";
      if (emailInd != null)
        hist_sql += "email_ind,";
      if (emailSubjectEn != null)
        hist_sql += "email_subject_en,";
      if (emailSubjectTc != null)
        hist_sql += "email_subject_tc,";
      if (emailSubjectSc != null)
        hist_sql += "email_subject_sc,";
      if (emailContentEn != null)
        hist_sql += "email_content_en,";
      if (emailContentTc != null)
        hist_sql += "email_content_tc,";
      if (emailContentSc != null)
        hist_sql += "email_content_sc,";
      if (status != null)
        hist_sql += "status,";
      if (autoCompleteInd != null)
        hist_sql += "auto_complete_ind,";
      if (lockInd != null)
        hist_sql += "lock_ind,";
      if (alertEmailInd != null)
        hist_sql += "alert_email_ind,";
      if (mobileMsgInd != null)
        hist_sql += "mobile_msg_ind,";
      if (mobileSubjectEn != null)
        hist_sql += "mobile_subject_en,";
      if (mobileSubjectTc != null)
        hist_sql += "mobile_subject_tc,";
      if (mobileSubjectSc != null)
        hist_sql += "mobile_subject_sc,";
      if (mobileContentEn != null)
        hist_sql += "mobile_content_en,";
      if (mobileContentTc != null)
        hist_sql += "mobile_content_tc,";
      if (mobileContentSc != null)
        hist_sql += "mobile_content_sc,";
      if (trustSpInd != null)
        hist_sql += "trust_sp_ind,";
      if (mnotExpiryTime != null)
        hist_sql += "mnot_expiry_time,";
      if (notiPriority != null)
        hist_sql += "noti_priority,";
      if (iasInd != null)
        hist_sql += "ias_ind,";
      if (iasOptCheckInd != null)
        hist_sql += "ias_opt_check_ind,";
      if (iasShowEsAppBtn != null)
        hist_sql += "ias_show_es_app_btn,";
      if (iasShowEsSetBtn != null)
        hist_sql += "ias_show_es_set_btn,";
      if (iasSubjectEn != null)
        hist_sql += "ias_subject_en,";
      if (iasSubjectTc != null)
        hist_sql += "ias_subject_tc,";
      if (iasSubjectSc != null)
        hist_sql += "ias_subject_sc,";
      if (iasContentEn != null)
        hist_sql += "ias_content_en,";
      if (iasContentTc != null)
        hist_sql += "ias_content_tc,";
      if (iasContentSc != null)
        hist_sql += "ias_content_sc,";
      if (iasEsAppSuffixEn != null)
        hist_sql += "ias_es_app_suffix_en,";
      if (iasEsAppSuffixTc != null)
        hist_sql += "ias_es_app_suffix_tc,";
      if (iasEsAppSuffixSc != null)
        hist_sql += "ias_es_app_suffix_sc,";
      hist_sql += "create_by,last_modify_by,create_dt,last_modify_dt) values (?,?,?,?,?,";

      // perpare set ...
      if (templateId != null)
        hist_sql += "?,";
      if (templateVersion != null)
        hist_sql += "?,";
      if (serviceProviderId != null)
        hist_sql += "?,";
      if (templateType != null)
        hist_sql += "?,";
      if (subjectEn != null)
        hist_sql += "?,";
      if (subjectTc != null)
        hist_sql += "?,";
      if (subjectSc != null)
        hist_sql += "?,";
      if (contentEn != null)
        hist_sql += "?,";
      if (contentTc != null)
        hist_sql += "?,";
      if (contentSc != null)
        hist_sql += "?,";
      if (emailInd != null)
        hist_sql += "?,";
      if (emailSubjectEn != null)
        hist_sql += "?,";
      if (emailSubjectTc != null)
        hist_sql += "?,";
      if (emailSubjectSc != null)
        hist_sql += "?,";
      if (emailContentEn != null)
        hist_sql += "?,";
      if (emailContentTc != null)
        hist_sql += "?,";
      if (emailContentSc != null)
        hist_sql += "?,";
      if (status != null)
        hist_sql += "?,";
      if (autoCompleteInd != null)
        hist_sql += "?,";
      if (lockInd != null)
        hist_sql += "?,";
      if (alertEmailInd != null)
        hist_sql += "?,";
      if (mobileMsgInd != null)
        hist_sql += "?,";
      if (mobileSubjectEn != null)
        hist_sql += "?,";
      if (mobileSubjectTc != null)
        hist_sql += "?,";
      if (mobileSubjectSc != null)
        hist_sql += "?,";
      if (mobileContentEn != null)
        hist_sql += "?,";
      if (mobileContentTc != null)
        hist_sql += "?,";
      if (mobileContentSc != null)
        hist_sql += "?,";
      if (trustSpInd != null)
        hist_sql += "?,";
      if (mnotExpiryTime != null)
        hist_sql += "?,";
      if (notiPriority != null)
        hist_sql += "?,";
      if (iasInd != null)
        hist_sql += "?,";
      if (iasOptCheckInd != null)
        hist_sql += "?,";
      if (iasShowEsAppBtn != null)
        hist_sql += "?,";
      if (iasShowEsSetBtn != null)
        hist_sql += "?,";
      if (iasSubjectEn != null)
        hist_sql += "?,";
      if (iasSubjectTc != null)
        hist_sql += "?,";
      if (iasSubjectSc != null)
        hist_sql += "?,";
      if (iasContentEn != null)
        hist_sql += "?,";
      if (iasContentTc != null)
        hist_sql += "?,";
      if (iasContentSc != null)
        hist_sql += "?,";
      if (iasEsAppSuffixEn != null)
        hist_sql += "?,";
      if (iasEsAppSuffixTc != null)
        hist_sql += "?,";
      if (iasEsAppSuffixSc != null)
        hist_sql += "?,";
      hist_sql += "?,?,?,?) ";
      histParaList
          .add(new Parameter(Parameter.String, CommonDBUtils.getSequence(countCon, thisTableName + thisTableHistName)));
      histParaList.add(new Parameter(Parameter.String, HPFW_Connection.INSERT));
      histParaList.add(new Parameter(Parameter.String,
          countCon.getUpdateMode().equals(HPFW_Connection.HISTORY_ONLY) ? HPFW_Connection.DUAL
              : HPFW_Connection.REMOTE));
      histParaList.add(new Parameter(Parameter.String, HPFW_Connection.PENDING));
      histParaList.add(new Parameter(Parameter.String, CommonDBUtils.getSERVER_ID()));

      // perpare set ...
      if (templateId != null)
        histParaList.add(new Parameter(Parameter.String, this.templateId));
      if (templateVersion != null)
        histParaList.add(new Parameter(Parameter.String, this.templateVersion));
      if (serviceProviderId != null)
        histParaList.add(new Parameter(Parameter.String, this.serviceProviderId));
      if (templateType != null)
        histParaList.add(new Parameter(Parameter.String, this.templateType));
      if (subjectEn != null)
        histParaList.add(new Parameter(Parameter.String, this.subjectEn));
      if (subjectTc != null)
        histParaList.add(new Parameter(Parameter.String, this.subjectTc));
      if (subjectSc != null)
        histParaList.add(new Parameter(Parameter.String, this.subjectSc));
      if (contentEn != null)
        histParaList.add(new Parameter(Parameter.String, this.contentEn));
      if (contentTc != null)
        histParaList.add(new Parameter(Parameter.String, this.contentTc));
      if (contentSc != null)
        histParaList.add(new Parameter(Parameter.String, this.contentSc));
      if (emailInd != null)
        histParaList.add(new Parameter(Parameter.String, this.emailInd));
      if (emailSubjectEn != null)
        histParaList.add(new Parameter(Parameter.String, this.emailSubjectEn));
      if (emailSubjectTc != null)
        histParaList.add(new Parameter(Parameter.String, this.emailSubjectTc));
      if (emailSubjectSc != null)
        histParaList.add(new Parameter(Parameter.String, this.emailSubjectSc));
      if (emailContentEn != null)
        histParaList.add(new Parameter(Parameter.String, this.emailContentEn));
      if (emailContentTc != null)
        histParaList.add(new Parameter(Parameter.String, this.emailContentTc));
      if (emailContentSc != null)
        histParaList.add(new Parameter(Parameter.String, this.emailContentSc));
      if (status != null)
        histParaList.add(new Parameter(Parameter.String, this.status));
      if (autoCompleteInd != null)
        histParaList.add(new Parameter(Parameter.String, this.autoCompleteInd));
      if (lockInd != null)
        histParaList.add(new Parameter(Parameter.String, this.lockInd));
      if (alertEmailInd != null)
        histParaList.add(new Parameter(Parameter.String, this.alertEmailInd));
      if (mobileMsgInd != null)
        histParaList.add(new Parameter(Parameter.String, this.mobileMsgInd));
      if (mobileSubjectEn != null)
        histParaList.add(new Parameter(Parameter.String, this.mobileSubjectEn));
      if (mobileSubjectTc != null)
        histParaList.add(new Parameter(Parameter.String, this.mobileSubjectTc));
      if (mobileSubjectSc != null)
        histParaList.add(new Parameter(Parameter.String, this.mobileSubjectSc));
      if (mobileContentEn != null)
        histParaList.add(new Parameter(Parameter.String, this.mobileContentEn));
      if (mobileContentTc != null)
        histParaList.add(new Parameter(Parameter.String, this.mobileContentTc));
      if (mobileContentSc != null)
        histParaList.add(new Parameter(Parameter.String, this.mobileContentSc));
      if (trustSpInd != null)
        histParaList.add(new Parameter(Parameter.String, this.trustSpInd));
      if (mnotExpiryTime != null)
        histParaList.add(new Parameter(Parameter.Integer, this.mnotExpiryTime));
      if (notiPriority != null)
        histParaList.add(new Parameter(Parameter.String, this.notiPriority));
      if (iasInd != null)
        histParaList.add(new Parameter(Parameter.String, this.iasInd));
      if (iasOptCheckInd != null)
        histParaList.add(new Parameter(Parameter.String, this.iasOptCheckInd));
      if (iasShowEsAppBtn != null)
        histParaList.add(new Parameter(Parameter.String, this.iasShowEsAppBtn));
      if (iasShowEsSetBtn != null)
        histParaList.add(new Parameter(Parameter.String, this.iasShowEsSetBtn));
      if (iasSubjectEn != null)
        histParaList.add(new Parameter(Parameter.String, this.iasSubjectEn));
      if (iasSubjectTc != null)
        histParaList.add(new Parameter(Parameter.String, this.iasSubjectTc));
      if (iasSubjectSc != null)
        histParaList.add(new Parameter(Parameter.String, this.iasSubjectSc));
      if (iasContentEn != null)
        histParaList.add(new Parameter(Parameter.String, this.iasContentEn));
      if (iasContentTc != null)
        histParaList.add(new Parameter(Parameter.String, this.iasContentTc));
      if (iasContentSc != null)
        histParaList.add(new Parameter(Parameter.String, this.iasContentSc));
      if (iasEsAppSuffixEn != null)
        histParaList.add(new Parameter(Parameter.String, this.iasEsAppSuffixEn));
      if (iasEsAppSuffixTc != null)
        histParaList.add(new Parameter(Parameter.String, this.iasEsAppSuffixTc));
      if (iasEsAppSuffixSc != null)
        histParaList.add(new Parameter(Parameter.String, this.iasEsAppSuffixSc));
      histParaList.add(new Parameter(Parameter.String, countCon.getLastUpdBy()));
      histParaList.add(new Parameter(Parameter.String, countCon.getLastUpdBy()));
      histParaList.add(new Parameter(Parameter.Timestamp, countCon.getLastUpTime()));
      histParaList.add(new Parameter(Parameter.Timestamp, countCon.getLastUpTime()));
      countCon.executeStatement(hist_sql, histParaList, false);
    }
  }

  public static void update(HPFW_Connection countCon, String setClause, ArrayList<Parameter> paraL, String whereClause,
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
        String sql = "select template_id,template_version from " + thisTableName + " " + whereClause;
        stmt = countCon.getConnectionPtr().prepareStatement(sql);
        CommonDBUtils.setStatement(stmt, whereParaL);
        rs = stmt.executeQuery();

        while (rs.next()) {
          String histsql = "insert into ";
          histsql += thisTableName + thisTableHistName;
          histsql += " (HIST_SEQ,HIST_action,HIST_update_type,HIST_status,HIST_server_id,last_modify_by,last_modify_dt,create_by,create_dt,template_id,template_version,"
              + selectString;
          histsql += ") values (?,?,?,?,?,?,?,?,?,?,?";
          for (int i = 0; i < selectStringArray.length; i++)
            histsql += ",?";
          histsql += ")";

          ArrayList<Parameter> histParaList = new ArrayList<Parameter>();
          histParaList.add(
              new Parameter(Parameter.String, CommonDBUtils.getSequence(countCon, thisTableName + thisTableHistName)));
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
      if (dirty_serviceProviderId)
        sql += "service_provider_id = ?,";
      if (dirty_templateType)
        sql += "template_type = ?,";
      if (dirty_subjectEn)
        sql += "subject_en = ?,";
      if (dirty_subjectTc)
        sql += "subject_tc = ?,";
      if (dirty_subjectSc)
        sql += "subject_sc = ?,";
      if (dirty_contentEn)
        sql += "content_en = ?,";
      if (dirty_contentTc)
        sql += "content_tc = ?,";
      if (dirty_contentSc)
        sql += "content_sc = ?,";
      if (dirty_emailInd)
        sql += "email_ind = ?,";
      if (dirty_emailSubjectEn)
        sql += "email_subject_en = ?,";
      if (dirty_emailSubjectTc)
        sql += "email_subject_tc = ?,";
      if (dirty_emailSubjectSc)
        sql += "email_subject_sc = ?,";
      if (dirty_emailContentEn)
        sql += "email_content_en = ?,";
      if (dirty_emailContentTc)
        sql += "email_content_tc = ?,";
      if (dirty_emailContentSc)
        sql += "email_content_sc = ?,";
      if (dirty_status)
        sql += "status = ?,";
      if (dirty_autoCompleteInd)
        sql += "auto_complete_ind = ?,";
      if (dirty_lockInd)
        sql += "lock_ind = ?,";
      if (dirty_alertEmailInd)
        sql += "alert_email_ind = ?,";
      if (dirty_mobileMsgInd)
        sql += "mobile_msg_ind = ?,";
      if (dirty_mobileSubjectEn)
        sql += "mobile_subject_en = ?,";
      if (dirty_mobileSubjectTc)
        sql += "mobile_subject_tc = ?,";
      if (dirty_mobileSubjectSc)
        sql += "mobile_subject_sc = ?,";
      if (dirty_mobileContentEn)
        sql += "mobile_content_en = ?,";
      if (dirty_mobileContentTc)
        sql += "mobile_content_tc = ?,";
      if (dirty_mobileContentSc)
        sql += "mobile_content_sc = ?,";
      if (dirty_trustSpInd)
        sql += "trust_sp_ind = ?,";
      if (dirty_mnotExpiryTime)
        sql += "mnot_expiry_time = ?,";
      if (dirty_notiPriority)
        sql += "noti_priority = ?,";
      if (dirty_iasInd)
        sql += "ias_ind = ?,";
      if (dirty_iasOptCheckInd)
        sql += "ias_opt_check_ind = ?,";
      if (dirty_iasShowEsAppBtn)
        sql += "ias_show_es_app_btn = ?,";
      if (dirty_iasShowEsSetBtn)
        sql += "ias_show_es_set_btn = ?,";
      if (dirty_iasSubjectEn)
        sql += "ias_subject_en = ?,";
      if (dirty_iasSubjectTc)
        sql += "ias_subject_tc = ?,";
      if (dirty_iasSubjectSc)
        sql += "ias_subject_sc = ?,";
      if (dirty_iasContentEn)
        sql += "ias_content_en = ?,";
      if (dirty_iasContentTc)
        sql += "ias_content_tc = ?,";
      if (dirty_iasContentSc)
        sql += "ias_content_sc = ?,";
      if (dirty_iasEsAppSuffixEn)
        sql += "ias_es_app_suffix_en = ?,";
      if (dirty_iasEsAppSuffixTc)
        sql += "ias_es_app_suffix_tc = ?,";
      if (dirty_iasEsAppSuffixSc)
        sql += "ias_es_app_suffix_sc = ?,";

      sql += " last_modify_by = ?,last_modify_dt = ? where 1=1 and template_id = ?  and template_version = ? ";
      // perpare set ...
      if (dirty_serviceProviderId)
        paraList.add(new Parameter(Parameter.String, this.serviceProviderId));
      if (dirty_templateType)
        paraList.add(new Parameter(Parameter.String, this.templateType));
      if (dirty_subjectEn)
        paraList.add(new Parameter(Parameter.String, this.subjectEn));
      if (dirty_subjectTc)
        paraList.add(new Parameter(Parameter.String, this.subjectTc));
      if (dirty_subjectSc)
        paraList.add(new Parameter(Parameter.String, this.subjectSc));
      if (dirty_contentEn)
        paraList.add(new Parameter(Parameter.String, this.contentEn));
      if (dirty_contentTc)
        paraList.add(new Parameter(Parameter.String, this.contentTc));
      if (dirty_contentSc)
        paraList.add(new Parameter(Parameter.String, this.contentSc));
      if (dirty_emailInd)
        paraList.add(new Parameter(Parameter.String, this.emailInd));
      if (dirty_emailSubjectEn)
        paraList.add(new Parameter(Parameter.String, this.emailSubjectEn));
      if (dirty_emailSubjectTc)
        paraList.add(new Parameter(Parameter.String, this.emailSubjectTc));
      if (dirty_emailSubjectSc)
        paraList.add(new Parameter(Parameter.String, this.emailSubjectSc));
      if (dirty_emailContentEn)
        paraList.add(new Parameter(Parameter.String, this.emailContentEn));
      if (dirty_emailContentTc)
        paraList.add(new Parameter(Parameter.String, this.emailContentTc));
      if (dirty_emailContentSc)
        paraList.add(new Parameter(Parameter.String, this.emailContentSc));
      if (dirty_status)
        paraList.add(new Parameter(Parameter.String, this.status));
      if (dirty_autoCompleteInd)
        paraList.add(new Parameter(Parameter.String, this.autoCompleteInd));
      if (dirty_lockInd)
        paraList.add(new Parameter(Parameter.String, this.lockInd));
      if (dirty_alertEmailInd)
        paraList.add(new Parameter(Parameter.String, this.alertEmailInd));
      if (dirty_mobileMsgInd)
        paraList.add(new Parameter(Parameter.String, this.mobileMsgInd));
      if (dirty_mobileSubjectEn)
        paraList.add(new Parameter(Parameter.String, this.mobileSubjectEn));
      if (dirty_mobileSubjectTc)
        paraList.add(new Parameter(Parameter.String, this.mobileSubjectTc));
      if (dirty_mobileSubjectSc)
        paraList.add(new Parameter(Parameter.String, this.mobileSubjectSc));
      if (dirty_mobileContentEn)
        paraList.add(new Parameter(Parameter.String, this.mobileContentEn));
      if (dirty_mobileContentTc)
        paraList.add(new Parameter(Parameter.String, this.mobileContentTc));
      if (dirty_mobileContentSc)
        paraList.add(new Parameter(Parameter.String, this.mobileContentSc));
      if (dirty_trustSpInd)
        paraList.add(new Parameter(Parameter.String, this.trustSpInd));
      if (dirty_mnotExpiryTime)
        paraList.add(new Parameter(Parameter.Integer, this.mnotExpiryTime));
      if (dirty_notiPriority)
        paraList.add(new Parameter(Parameter.String, this.notiPriority));
      if (dirty_iasInd)
        paraList.add(new Parameter(Parameter.String, this.iasInd));
      if (dirty_iasOptCheckInd)
        paraList.add(new Parameter(Parameter.String, this.iasOptCheckInd));
      if (dirty_iasShowEsAppBtn)
        paraList.add(new Parameter(Parameter.String, this.iasShowEsAppBtn));
      if (dirty_iasShowEsSetBtn)
        paraList.add(new Parameter(Parameter.String, this.iasShowEsSetBtn));
      if (dirty_iasSubjectEn)
        paraList.add(new Parameter(Parameter.String, this.iasSubjectEn));
      if (dirty_iasSubjectTc)
        paraList.add(new Parameter(Parameter.String, this.iasSubjectTc));
      if (dirty_iasSubjectSc)
        paraList.add(new Parameter(Parameter.String, this.iasSubjectSc));
      if (dirty_iasContentEn)
        paraList.add(new Parameter(Parameter.String, this.iasContentEn));
      if (dirty_iasContentTc)
        paraList.add(new Parameter(Parameter.String, this.iasContentTc));
      if (dirty_iasContentSc)
        paraList.add(new Parameter(Parameter.String, this.iasContentSc));
      if (dirty_iasEsAppSuffixEn)
        paraList.add(new Parameter(Parameter.String, this.iasEsAppSuffixEn));
      if (dirty_iasEsAppSuffixTc)
        paraList.add(new Parameter(Parameter.String, this.iasEsAppSuffixTc));
      if (dirty_iasEsAppSuffixSc)
        paraList.add(new Parameter(Parameter.String, this.iasEsAppSuffixSc));
      paraList.add(new Parameter(Parameter.String, countCon.getLastUpdBy()));
      paraList.add(new Parameter(Parameter.Timestamp, countCon.getLastUpTime()));
      // perpare where ...
      paraList.add(new Parameter(Parameter.String, this.templateId));
      paraList.add(new Parameter(Parameter.String, this.templateVersion));
      countCon.executeStatement(sql, paraList);
    }

    if (HPFW_Connection.HISTORY_ONLY.equals(countCon.getUpdateMode())
        || HPFW_Connection.DIRECT_WITH_HISTORY.equals(countCon.getUpdateMode())) {
      ArrayList<Parameter> histParaList = new ArrayList<Parameter>();

      String hist_sql = "insert into ";
      hist_sql += thisTableName + thisTableHistName;
      hist_sql += " (HIST_SEQ,HIST_action,HIST_update_type,HIST_status,HIST_server_id,";

      // perpare set ...
      hist_sql += "template_id,";
      hist_sql += "template_version,";
      if (dirty_serviceProviderId)
        hist_sql += "service_provider_id,";
      if (dirty_templateType)
        hist_sql += "template_type,";
      if (dirty_subjectEn)
        hist_sql += "subject_en,";
      if (dirty_subjectTc)
        hist_sql += "subject_tc,";
      if (dirty_subjectSc)
        hist_sql += "subject_sc,";
      if (dirty_contentEn)
        hist_sql += "content_en,";
      if (dirty_contentTc)
        hist_sql += "content_tc,";
      if (dirty_contentSc)
        hist_sql += "content_sc,";
      if (dirty_emailInd)
        hist_sql += "email_ind,";
      if (dirty_emailSubjectEn)
        hist_sql += "email_subject_en,";
      if (dirty_emailSubjectTc)
        hist_sql += "email_subject_tc,";
      if (dirty_emailSubjectSc)
        hist_sql += "email_subject_sc,";
      if (dirty_emailContentEn)
        hist_sql += "email_content_en,";
      if (dirty_emailContentTc)
        hist_sql += "email_content_tc,";
      if (dirty_emailContentSc)
        hist_sql += "email_content_sc,";
      if (dirty_status)
        hist_sql += "status,";
      if (dirty_autoCompleteInd)
        hist_sql += "auto_complete_ind,";
      if (dirty_lockInd)
        hist_sql += "lock_ind,";
      if (dirty_alertEmailInd)
        hist_sql += "alert_email_ind,";
      if (dirty_mobileMsgInd)
        hist_sql += "mobile_msg_ind,";
      if (dirty_mobileSubjectEn)
        hist_sql += "mobile_subject_en,";
      if (dirty_mobileSubjectTc)
        hist_sql += "mobile_subject_tc,";
      if (dirty_mobileSubjectSc)
        hist_sql += "mobile_subject_sc,";
      if (dirty_mobileContentEn)
        hist_sql += "mobile_content_en,";
      if (dirty_mobileContentTc)
        hist_sql += "mobile_content_tc,";
      if (dirty_mobileContentSc)
        hist_sql += "mobile_content_sc,";
      if (dirty_trustSpInd)
        hist_sql += "trust_sp_ind,";
      if (dirty_mnotExpiryTime)
        hist_sql += "mnot_expiry_time,";
      if (dirty_notiPriority)
        hist_sql += "noti_priority,";
      if (dirty_iasInd)
        hist_sql += "ias_ind,";
      if (dirty_iasOptCheckInd)
        hist_sql += "ias_opt_check_ind,";
      if (dirty_iasShowEsAppBtn)
        hist_sql += "ias_show_es_app_btn,";
      if (dirty_iasShowEsSetBtn)
        hist_sql += "ias_show_es_set_btn,";
      if (dirty_iasSubjectEn)
        hist_sql += "ias_subject_en,";
      if (dirty_iasSubjectTc)
        hist_sql += "ias_subject_tc,";
      if (dirty_iasSubjectSc)
        hist_sql += "ias_subject_sc,";
      if (dirty_iasContentEn)
        hist_sql += "ias_content_en,";
      if (dirty_iasContentTc)
        hist_sql += "ias_content_tc,";
      if (dirty_iasContentSc)
        hist_sql += "ias_content_sc,";
      if (dirty_iasEsAppSuffixEn)
        hist_sql += "ias_es_app_suffix_en,";
      if (dirty_iasEsAppSuffixTc)
        hist_sql += "ias_es_app_suffix_tc,";
      if (dirty_iasEsAppSuffixSc)
        hist_sql += "ias_es_app_suffix_sc,";
      hist_sql += "last_modify_by,last_modify_dt,create_by,create_dt) values (?,?,?,?,?,";

      // perpare set ...
      hist_sql += "?,";
      hist_sql += "?,";
      if (dirty_serviceProviderId)
        hist_sql += "?,";
      if (dirty_templateType)
        hist_sql += "?,";
      if (dirty_subjectEn)
        hist_sql += "?,";
      if (dirty_subjectTc)
        hist_sql += "?,";
      if (dirty_subjectSc)
        hist_sql += "?,";
      if (dirty_contentEn)
        hist_sql += "?,";
      if (dirty_contentTc)
        hist_sql += "?,";
      if (dirty_contentSc)
        hist_sql += "?,";
      if (dirty_emailInd)
        hist_sql += "?,";
      if (dirty_emailSubjectEn)
        hist_sql += "?,";
      if (dirty_emailSubjectTc)
        hist_sql += "?,";
      if (dirty_emailSubjectSc)
        hist_sql += "?,";
      if (dirty_emailContentEn)
        hist_sql += "?,";
      if (dirty_emailContentTc)
        hist_sql += "?,";
      if (dirty_emailContentSc)
        hist_sql += "?,";
      if (dirty_status)
        hist_sql += "?,";
      if (dirty_autoCompleteInd)
        hist_sql += "?,";
      if (dirty_lockInd)
        hist_sql += "?,";
      if (dirty_alertEmailInd)
        hist_sql += "?,";
      if (dirty_mobileMsgInd)
        hist_sql += "?,";
      if (dirty_mobileSubjectEn)
        hist_sql += "?,";
      if (dirty_mobileSubjectTc)
        hist_sql += "?,";
      if (dirty_mobileSubjectSc)
        hist_sql += "?,";
      if (dirty_mobileContentEn)
        hist_sql += "?,";
      if (dirty_mobileContentTc)
        hist_sql += "?,";
      if (dirty_mobileContentSc)
        hist_sql += "?,";
      if (dirty_trustSpInd)
        hist_sql += "?,";
      if (dirty_mnotExpiryTime)
        hist_sql += "?,";
      if (dirty_notiPriority)
        hist_sql += "?,";
      if (dirty_iasInd)
        hist_sql += "?,";
      if (dirty_iasOptCheckInd)
        hist_sql += "?,";
      if (dirty_iasShowEsAppBtn)
        hist_sql += "?,";
      if (dirty_iasShowEsSetBtn)
        hist_sql += "?,";
      if (dirty_iasSubjectEn)
        hist_sql += "?,";
      if (dirty_iasSubjectTc)
        hist_sql += "?,";
      if (dirty_iasSubjectSc)
        hist_sql += "?,";
      if (dirty_iasContentEn)
        hist_sql += "?,";
      if (dirty_iasContentTc)
        hist_sql += "?,";
      if (dirty_iasContentSc)
        hist_sql += "?,";
      if (dirty_iasEsAppSuffixEn)
        hist_sql += "?,";
      if (dirty_iasEsAppSuffixTc)
        hist_sql += "?,";
      if (dirty_iasEsAppSuffixSc)
        hist_sql += "?,";
      hist_sql += "?,?,?,?) ";
      histParaList
          .add(new Parameter(Parameter.String, CommonDBUtils.getSequence(countCon, thisTableName + thisTableHistName)));
      histParaList.add(new Parameter(Parameter.String, HPFW_Connection.UPDATE));
      histParaList.add(new Parameter(Parameter.String,
          countCon.getUpdateMode().equals(HPFW_Connection.HISTORY_ONLY) ? HPFW_Connection.DUAL
              : HPFW_Connection.REMOTE));
      histParaList.add(new Parameter(Parameter.String, HPFW_Connection.PENDING));
      histParaList.add(new Parameter(Parameter.String, CommonDBUtils.getSERVER_ID()));

      // perpare set ...
      histParaList.add(new Parameter(Parameter.String, this.templateId));
      histParaList.add(new Parameter(Parameter.String, this.templateVersion));
      if (dirty_serviceProviderId)
        histParaList.add(new Parameter(Parameter.String, this.serviceProviderId));
      if (dirty_templateType)
        histParaList.add(new Parameter(Parameter.String, this.templateType));
      if (dirty_subjectEn)
        histParaList.add(new Parameter(Parameter.String, this.subjectEn));
      if (dirty_subjectTc)
        histParaList.add(new Parameter(Parameter.String, this.subjectTc));
      if (dirty_subjectSc)
        histParaList.add(new Parameter(Parameter.String, this.subjectSc));
      if (dirty_contentEn)
        histParaList.add(new Parameter(Parameter.String, this.contentEn));
      if (dirty_contentTc)
        histParaList.add(new Parameter(Parameter.String, this.contentTc));
      if (dirty_contentSc)
        histParaList.add(new Parameter(Parameter.String, this.contentSc));
      if (dirty_emailInd)
        histParaList.add(new Parameter(Parameter.String, this.emailInd));
      if (dirty_emailSubjectEn)
        histParaList.add(new Parameter(Parameter.String, this.emailSubjectEn));
      if (dirty_emailSubjectTc)
        histParaList.add(new Parameter(Parameter.String, this.emailSubjectTc));
      if (dirty_emailSubjectSc)
        histParaList.add(new Parameter(Parameter.String, this.emailSubjectSc));
      if (dirty_emailContentEn)
        histParaList.add(new Parameter(Parameter.String, this.emailContentEn));
      if (dirty_emailContentTc)
        histParaList.add(new Parameter(Parameter.String, this.emailContentTc));
      if (dirty_emailContentSc)
        histParaList.add(new Parameter(Parameter.String, this.emailContentSc));
      if (dirty_status)
        histParaList.add(new Parameter(Parameter.String, this.status));
      if (dirty_autoCompleteInd)
        histParaList.add(new Parameter(Parameter.String, this.autoCompleteInd));
      if (dirty_lockInd)
        histParaList.add(new Parameter(Parameter.String, this.lockInd));
      if (dirty_alertEmailInd)
        histParaList.add(new Parameter(Parameter.String, this.alertEmailInd));
      if (dirty_mobileMsgInd)
        histParaList.add(new Parameter(Parameter.String, this.mobileMsgInd));
      if (dirty_mobileSubjectEn)
        histParaList.add(new Parameter(Parameter.String, this.mobileSubjectEn));
      if (dirty_mobileSubjectTc)
        histParaList.add(new Parameter(Parameter.String, this.mobileSubjectTc));
      if (dirty_mobileSubjectSc)
        histParaList.add(new Parameter(Parameter.String, this.mobileSubjectSc));
      if (dirty_mobileContentEn)
        histParaList.add(new Parameter(Parameter.String, this.mobileContentEn));
      if (dirty_mobileContentTc)
        histParaList.add(new Parameter(Parameter.String, this.mobileContentTc));
      if (dirty_mobileContentSc)
        histParaList.add(new Parameter(Parameter.String, this.mobileContentSc));
      if (dirty_trustSpInd)
        histParaList.add(new Parameter(Parameter.String, this.trustSpInd));
      if (dirty_mnotExpiryTime)
        histParaList.add(new Parameter(Parameter.Integer, this.mnotExpiryTime));
      if (dirty_notiPriority)
        histParaList.add(new Parameter(Parameter.String, this.notiPriority));
      if (dirty_iasInd)
        histParaList.add(new Parameter(Parameter.String, this.iasInd));
      if (dirty_iasOptCheckInd)
        histParaList.add(new Parameter(Parameter.String, this.iasOptCheckInd));
      if (dirty_iasShowEsAppBtn)
        histParaList.add(new Parameter(Parameter.String, this.iasShowEsAppBtn));
      if (dirty_iasShowEsSetBtn)
        histParaList.add(new Parameter(Parameter.String, this.iasShowEsSetBtn));
      if (dirty_iasSubjectEn)
        histParaList.add(new Parameter(Parameter.String, this.iasSubjectEn));
      if (dirty_iasSubjectTc)
        histParaList.add(new Parameter(Parameter.String, this.iasSubjectTc));
      if (dirty_iasSubjectSc)
        histParaList.add(new Parameter(Parameter.String, this.iasSubjectSc));
      if (dirty_iasContentEn)
        histParaList.add(new Parameter(Parameter.String, this.iasContentEn));
      if (dirty_iasContentTc)
        histParaList.add(new Parameter(Parameter.String, this.iasContentTc));
      if (dirty_iasContentSc)
        histParaList.add(new Parameter(Parameter.String, this.iasContentSc));
      if (dirty_iasEsAppSuffixEn)
        histParaList.add(new Parameter(Parameter.String, this.iasEsAppSuffixEn));
      if (dirty_iasEsAppSuffixTc)
        histParaList.add(new Parameter(Parameter.String, this.iasEsAppSuffixTc));
      if (dirty_iasEsAppSuffixSc)
        histParaList.add(new Parameter(Parameter.String, this.iasEsAppSuffixSc));
      histParaList.add(new Parameter(Parameter.String, countCon.getLastUpdBy()));
      histParaList.add(new Parameter(Parameter.Timestamp, countCon.getLastUpTime()));
      histParaList.add(new Parameter(Parameter.String, countCon.getLastUpdBy()));
      histParaList.add(new Parameter(Parameter.Timestamp, countCon.getLastUpTime()));
      countCon.executeStatement(hist_sql, histParaList, true);
    }
    this.forUpdate = false;
  }

  public static ArrayList<CmcTemplate_> getResultList(HPFW_Connection countCon, String whereCluase,
      ArrayList<Parameter> paraL) throws SQLException {
    boolean needClose = countCon == null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    ArrayList<CmcTemplate_> result = new ArrayList<CmcTemplate_>();
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
        CmcTemplate_ obj = new CmcTemplate_();
        obj.templateId = rs.getString("template_id"); // String
        obj.templateVersion = rs.getString("template_version"); // String
        obj.serviceProviderId = rs.getString("service_provider_id"); // String
        obj.templateType = rs.getString("template_type"); // String
        obj.subjectEn = rs.getString("subject_en"); // String
        obj.subjectTc = rs.getString("subject_tc"); // String
        obj.subjectSc = rs.getString("subject_sc"); // String
        obj.contentEn = rs.getString("content_en"); // String
        obj.contentTc = rs.getString("content_tc"); // String
        obj.contentSc = rs.getString("content_sc"); // String
        obj.emailInd = rs.getString("email_ind"); // String
        obj.emailSubjectEn = rs.getString("email_subject_en"); // String
        obj.emailSubjectTc = rs.getString("email_subject_tc"); // String
        obj.emailSubjectSc = rs.getString("email_subject_sc"); // String
        obj.emailContentEn = rs.getString("email_content_en"); // String
        obj.emailContentTc = rs.getString("email_content_tc"); // String
        obj.emailContentSc = rs.getString("email_content_sc"); // String
        obj.status = rs.getString("status"); // String
        obj.autoCompleteInd = rs.getString("auto_complete_ind"); // String
        obj.lockInd = rs.getString("lock_ind"); // String
        obj.alertEmailInd = rs.getString("alert_email_ind"); // String
        obj.createDt = rs.getTimestamp("create_dt"); // Timestamp
        obj.lastModifyDt = rs.getTimestamp("last_modify_dt"); // Timestamp
        obj.createBy = rs.getString("create_by"); // String
        obj.lastModifyBy = rs.getString("last_modify_by"); // String
        obj.mobileMsgInd = rs.getString("mobile_msg_ind"); // String
        obj.mobileSubjectEn = rs.getString("mobile_subject_en"); // String
        obj.mobileSubjectTc = rs.getString("mobile_subject_tc"); // String
        obj.mobileSubjectSc = rs.getString("mobile_subject_sc"); // String
        obj.mobileContentEn = rs.getString("mobile_content_en"); // String
        obj.mobileContentTc = rs.getString("mobile_content_tc"); // String
        obj.mobileContentSc = rs.getString("mobile_content_sc"); // String
        obj.trustSpInd = rs.getString("trust_sp_ind"); // String
        // mnotExpiryTime = rs.getString("mnot_expiry_time")==null || "".equals(rs.getString("mnot_expiry_time")) ? null : new Integer(rs.getInt("mnot_expiry_time")); //Integer
        obj.mnotExpiryTime = new Integer(rs.getInt("mnot_expiry_time")); // Integer
        obj.notiPriority = rs.getString("noti_priority"); // String
        obj.iasInd = rs.getString("ias_ind"); // String
        obj.iasOptCheckInd = rs.getString("ias_opt_check_ind"); // String
        obj.iasShowEsAppBtn = rs.getString("ias_show_es_app_btn"); // String
        obj.iasShowEsSetBtn = rs.getString("ias_show_es_set_btn"); // String
        obj.iasSubjectEn = rs.getString("ias_subject_en"); // String
        obj.iasSubjectTc = rs.getString("ias_subject_tc"); // String
        obj.iasSubjectSc = rs.getString("ias_subject_sc"); // String
        obj.iasContentEn = rs.getString("ias_content_en"); // String
        obj.iasContentTc = rs.getString("ias_content_tc"); // String
        obj.iasContentSc = rs.getString("ias_content_sc"); // String
        obj.iasEsAppSuffixEn = rs.getString("ias_es_app_suffix_en"); // String
        obj.iasEsAppSuffixTc = rs.getString("ias_es_app_suffix_tc"); // String
        obj.iasEsAppSuffixSc = rs.getString("ias_es_app_suffix_sc"); // String
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

  public void init(HPFW_Connection countCon, final String intemplateId, final String intemplateVersion,
      boolean forUpdate) throws SQLException, NullPointerException {
    this.forUpdate = forUpdate;
    boolean needClose = countCon == null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      String sql = "select * from " + thisTableName + " where 1=1 and template_id = ?  and template_version = ? ";
      if (forUpdate)
        sql += "for update";
      if (countCon == null)
        countCon = HPFW_Connection.getHPFW_Connection();
      stmt = countCon.getConnectionPtr().prepareStatement(sql);
      stmt.setString(1, intemplateId);
      stmt.setString(2, intemplateVersion);
      rs = stmt.executeQuery();

      if (rs.next()) {
        templateId = rs.getString("template_id"); // String
        templateVersion = rs.getString("template_version"); // String
        serviceProviderId = rs.getString("service_provider_id"); // String
        templateType = rs.getString("template_type"); // String
        subjectEn = rs.getString("subject_en"); // String
        subjectTc = rs.getString("subject_tc"); // String
        subjectSc = rs.getString("subject_sc"); // String
        contentEn = rs.getString("content_en"); // String
        contentTc = rs.getString("content_tc"); // String
        contentSc = rs.getString("content_sc"); // String
        emailInd = rs.getString("email_ind"); // String
        emailSubjectEn = rs.getString("email_subject_en"); // String
        emailSubjectTc = rs.getString("email_subject_tc"); // String
        emailSubjectSc = rs.getString("email_subject_sc"); // String
        emailContentEn = rs.getString("email_content_en"); // String
        emailContentTc = rs.getString("email_content_tc"); // String
        emailContentSc = rs.getString("email_content_sc"); // String
        status = rs.getString("status"); // String
        autoCompleteInd = rs.getString("auto_complete_ind"); // String
        lockInd = rs.getString("lock_ind"); // String
        alertEmailInd = rs.getString("alert_email_ind"); // String
        createDt = rs.getTimestamp("create_dt"); // Timestamp
        lastModifyDt = rs.getTimestamp("last_modify_dt"); // Timestamp
        createBy = rs.getString("create_by"); // String
        lastModifyBy = rs.getString("last_modify_by"); // String
        mobileMsgInd = rs.getString("mobile_msg_ind"); // String
        mobileSubjectEn = rs.getString("mobile_subject_en"); // String
        mobileSubjectTc = rs.getString("mobile_subject_tc"); // String
        mobileSubjectSc = rs.getString("mobile_subject_sc"); // String
        mobileContentEn = rs.getString("mobile_content_en"); // String
        mobileContentTc = rs.getString("mobile_content_tc"); // String
        mobileContentSc = rs.getString("mobile_content_sc"); // String
        trustSpInd = rs.getString("trust_sp_ind"); // String
        // mnotExpiryTime = rs.getString("mnot_expiry_time")==null || "".equals(rs.getString("mnot_expiry_time")) ? null : new Integer(rs.getInt("mnot_expiry_time")); //Integer
        mnotExpiryTime = new Integer(rs.getInt("mnot_expiry_time")); // Integer
        notiPriority = rs.getString("noti_priority"); // String
        iasInd = rs.getString("ias_ind"); // String
        iasOptCheckInd = rs.getString("ias_opt_check_ind"); // String
        iasShowEsAppBtn = rs.getString("ias_show_es_app_btn"); // String
        iasShowEsSetBtn = rs.getString("ias_show_es_set_btn"); // String
        iasSubjectEn = rs.getString("ias_subject_en"); // String
        iasSubjectTc = rs.getString("ias_subject_tc"); // String
        iasSubjectSc = rs.getString("ias_subject_sc"); // String
        iasContentEn = rs.getString("ias_content_en"); // String
        iasContentTc = rs.getString("ias_content_tc"); // String
        iasContentSc = rs.getString("ias_content_sc"); // String
        iasEsAppSuffixEn = rs.getString("ias_es_app_suffix_en"); // String
        iasEsAppSuffixTc = rs.getString("ias_es_app_suffix_tc"); // String
        iasEsAppSuffixSc = rs.getString("ias_es_app_suffix_sc"); // String
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
   * CmcTemplate_ Destroyer
   */
  protected void finalize() throws Throwable {
    templateId = null;
    templateVersion = null;
    serviceProviderId = null;
    templateType = null;
    subjectEn = null;
    subjectTc = null;
    subjectSc = null;
    contentEn = null;
    contentTc = null;
    contentSc = null;
    emailInd = null;
    emailSubjectEn = null;
    emailSubjectTc = null;
    emailSubjectSc = null;
    emailContentEn = null;
    emailContentTc = null;
    emailContentSc = null;
    status = null;
    autoCompleteInd = null;
    lockInd = null;
    alertEmailInd = null;
    createDt = null;
    lastModifyDt = null;
    createBy = null;
    lastModifyBy = null;
    mobileMsgInd = null;
    mobileSubjectEn = null;
    mobileSubjectTc = null;
    mobileSubjectSc = null;
    mobileContentEn = null;
    mobileContentTc = null;
    mobileContentSc = null;
    trustSpInd = null;
    mnotExpiryTime = null;
    notiPriority = null;
    iasInd = null;
    iasOptCheckInd = null;
    iasShowEsAppBtn = null;
    iasShowEsSetBtn = null;
    iasSubjectEn = null;
    iasSubjectTc = null;
    iasSubjectSc = null;
    iasContentEn = null;
    iasContentTc = null;
    iasContentSc = null;
    iasEsAppSuffixEn = null;
    iasEsAppSuffixTc = null;
    iasEsAppSuffixSc = null;
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
   * Get service_provider_id
   */
  public String getServiceProviderId() {
    return serviceProviderId == null ? "" : serviceProviderId;
  }

  /**
   * Set service_provider_id
   */
  public void setServiceProviderId(final String inServiceProviderId) {
    serviceProviderId = inServiceProviderId;
    dirty_serviceProviderId = true;
  }

  /**
   * Get template_type
   */
  public String getTemplateType() {
    return templateType == null ? "" : templateType;
  }

  /**
   * Set template_type
   */
  public void setTemplateType(final String inTemplateType) {
    templateType = inTemplateType;
    dirty_templateType = true;
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
   * Get email_ind
   */
  public String getEmailInd() {
    return emailInd == null ? "" : emailInd;
  }

  /**
   * Set email_ind
   */
  public void setEmailInd(final String inEmailInd) {
    emailInd = inEmailInd;
    dirty_emailInd = true;
  }

  /**
   * Get email_subject_en
   */
  public String getEmailSubjectEn() {
    return emailSubjectEn == null ? "" : emailSubjectEn;
  }

  /**
   * Set email_subject_en
   */
  public void setEmailSubjectEn(final String inEmailSubjectEn) {
    emailSubjectEn = inEmailSubjectEn;
    dirty_emailSubjectEn = true;
  }

  /**
   * Get email_subject_tc
   */
  public String getEmailSubjectTc() {
    return emailSubjectTc == null ? "" : emailSubjectTc;
  }

  /**
   * Set email_subject_tc
   */
  public void setEmailSubjectTc(final String inEmailSubjectTc) {
    emailSubjectTc = inEmailSubjectTc;
    dirty_emailSubjectTc = true;
  }

  /**
   * Get email_subject_sc
   */
  public String getEmailSubjectSc() {
    return emailSubjectSc == null ? "" : emailSubjectSc;
  }

  /**
   * Set email_subject_sc
   */
  public void setEmailSubjectSc(final String inEmailSubjectSc) {
    emailSubjectSc = inEmailSubjectSc;
    dirty_emailSubjectSc = true;
  }

  /**
   * Get email_content_en
   */
  public String getEmailContentEn() {
    return emailContentEn == null ? "" : emailContentEn;
  }

  /**
   * Set email_content_en
   */
  public void setEmailContentEn(final String inEmailContentEn) {
    emailContentEn = inEmailContentEn;
    dirty_emailContentEn = true;
  }

  /**
   * Get email_content_tc
   */
  public String getEmailContentTc() {
    return emailContentTc == null ? "" : emailContentTc;
  }

  /**
   * Set email_content_tc
   */
  public void setEmailContentTc(final String inEmailContentTc) {
    emailContentTc = inEmailContentTc;
    dirty_emailContentTc = true;
  }

  /**
   * Get email_content_sc
   */
  public String getEmailContentSc() {
    return emailContentSc == null ? "" : emailContentSc;
  }

  /**
   * Set email_content_sc
   */
  public void setEmailContentSc(final String inEmailContentSc) {
    emailContentSc = inEmailContentSc;
    dirty_emailContentSc = true;
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
   * Get auto_complete_ind
   */
  public String getAutoCompleteInd() {
    return autoCompleteInd == null ? "" : autoCompleteInd;
  }

  /**
   * Set auto_complete_ind
   */
  public void setAutoCompleteInd(final String inAutoCompleteInd) {
    autoCompleteInd = inAutoCompleteInd;
    dirty_autoCompleteInd = true;
  }

  /**
   * Get lock_ind
   */
  public String getLockInd() {
    return lockInd == null ? "" : lockInd;
  }

  /**
   * Set lock_ind
   */
  public void setLockInd(final String inLockInd) {
    lockInd = inLockInd;
    dirty_lockInd = true;
  }

  /**
   * Get alert_email_ind
   */
  public String getAlertEmailInd() {
    return alertEmailInd == null ? "" : alertEmailInd;
  }

  /**
   * Set alert_email_ind
   */
  public void setAlertEmailInd(final String inAlertEmailInd) {
    alertEmailInd = inAlertEmailInd;
    dirty_alertEmailInd = true;
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
   * Get mobile_msg_ind
   */
  public String getMobileMsgInd() {
    return mobileMsgInd == null ? "" : mobileMsgInd;
  }

  /**
   * Set mobile_msg_ind
   */
  public void setMobileMsgInd(final String inMobileMsgInd) {
    mobileMsgInd = inMobileMsgInd;
    dirty_mobileMsgInd = true;
  }

  /**
   * Get mobile_subject_en
   */
  public String getMobileSubjectEn() {
    return mobileSubjectEn == null ? "" : mobileSubjectEn;
  }

  /**
   * Set mobile_subject_en
   */
  public void setMobileSubjectEn(final String inMobileSubjectEn) {
    mobileSubjectEn = inMobileSubjectEn;
    dirty_mobileSubjectEn = true;
  }

  /**
   * Get mobile_subject_tc
   */
  public String getMobileSubjectTc() {
    return mobileSubjectTc == null ? "" : mobileSubjectTc;
  }

  /**
   * Set mobile_subject_tc
   */
  public void setMobileSubjectTc(final String inMobileSubjectTc) {
    mobileSubjectTc = inMobileSubjectTc;
    dirty_mobileSubjectTc = true;
  }

  /**
   * Get mobile_subject_sc
   */
  public String getMobileSubjectSc() {
    return mobileSubjectSc == null ? "" : mobileSubjectSc;
  }

  /**
   * Set mobile_subject_sc
   */
  public void setMobileSubjectSc(final String inMobileSubjectSc) {
    mobileSubjectSc = inMobileSubjectSc;
    dirty_mobileSubjectSc = true;
  }

  /**
   * Get mobile_content_en
   */
  public String getMobileContentEn() {
    return mobileContentEn == null ? "" : mobileContentEn;
  }

  /**
   * Set mobile_content_en
   */
  public void setMobileContentEn(final String inMobileContentEn) {
    mobileContentEn = inMobileContentEn;
    dirty_mobileContentEn = true;
  }

  /**
   * Get mobile_content_tc
   */
  public String getMobileContentTc() {
    return mobileContentTc == null ? "" : mobileContentTc;
  }

  /**
   * Set mobile_content_tc
   */
  public void setMobileContentTc(final String inMobileContentTc) {
    mobileContentTc = inMobileContentTc;
    dirty_mobileContentTc = true;
  }

  /**
   * Get mobile_content_sc
   */
  public String getMobileContentSc() {
    return mobileContentSc == null ? "" : mobileContentSc;
  }

  /**
   * Set mobile_content_sc
   */
  public void setMobileContentSc(final String inMobileContentSc) {
    mobileContentSc = inMobileContentSc;
    dirty_mobileContentSc = true;
  }

  /**
   * Get trust_sp_ind
   */
  public String getTrustSpInd() {
    return trustSpInd == null ? "" : trustSpInd;
  }

  /**
   * Set trust_sp_ind
   */
  public void setTrustSpInd(final String inTrustSpInd) {
    trustSpInd = inTrustSpInd;
    dirty_trustSpInd = true;
  }

  /**
   * Get mnot_expiry_time
   */
  public Integer getMnotExpiryTime() {
    return mnotExpiryTime;
  }

  /**
   * Set mnot_expiry_time
   */
  public void setMnotExpiryTime(final Integer inMnotExpiryTime) {
    mnotExpiryTime = inMnotExpiryTime;
    dirty_mnotExpiryTime = true;
  }

  /**
   * Get noti_priority
   */
  public String getNotiPriority() {
    return notiPriority == null ? "" : notiPriority;
  }

  /**
   * Set noti_priority
   */
  public void setNotiPriority(final String inNotiPriority) {
    notiPriority = inNotiPriority;
    dirty_notiPriority = true;
  }

  /**
   * Get ias_ind
   */
  public String getIasInd() {
    return iasInd == null ? "" : iasInd;
  }

  /**
   * Set ias_ind
   */
  public void setIasInd(final String inIasInd) {
    iasInd = inIasInd;
    dirty_iasInd = true;
  }

  /**
   * Get ias_opt_check_ind
   */
  public String getIasOptCheckInd() {
    return iasOptCheckInd == null ? "" : iasOptCheckInd;
  }

  /**
   * Set ias_opt_check_ind
   */
  public void setIasOptCheckInd(final String inIasOptCheckInd) {
    iasOptCheckInd = inIasOptCheckInd;
    dirty_iasOptCheckInd = true;
  }

  /**
   * Get ias_show_es_app_btn
   */
  public String getIasShowEsAppBtn() {
    return iasShowEsAppBtn == null ? "" : iasShowEsAppBtn;
  }

  /**
   * Set ias_show_es_app_btn
   */
  public void setIasShowEsAppBtn(final String inIasShowEsAppBtn) {
    iasShowEsAppBtn = inIasShowEsAppBtn;
    dirty_iasShowEsAppBtn = true;
  }

  /**
   * Get ias_show_es_set_btn
   */
  public String getIasShowEsSetBtn() {
    return iasShowEsSetBtn == null ? "" : iasShowEsSetBtn;
  }

  /**
   * Set ias_show_es_set_btn
   */
  public void setIasShowEsSetBtn(final String inIasShowEsSetBtn) {
    iasShowEsSetBtn = inIasShowEsSetBtn;
    dirty_iasShowEsSetBtn = true;
  }

  /**
   * Get ias_subject_en
   */
  public String getIasSubjectEn() {
    return iasSubjectEn == null ? "" : iasSubjectEn;
  }

  /**
   * Set ias_subject_en
   */
  public void setIasSubjectEn(final String inIasSubjectEn) {
    iasSubjectEn = inIasSubjectEn;
    dirty_iasSubjectEn = true;
  }

  /**
   * Get ias_subject_tc
   */
  public String getIasSubjectTc() {
    return iasSubjectTc == null ? "" : iasSubjectTc;
  }

  /**
   * Set ias_subject_tc
   */
  public void setIasSubjectTc(final String inIasSubjectTc) {
    iasSubjectTc = inIasSubjectTc;
    dirty_iasSubjectTc = true;
  }

  /**
   * Get ias_subject_sc
   */
  public String getIasSubjectSc() {
    return iasSubjectSc == null ? "" : iasSubjectSc;
  }

  /**
   * Set ias_subject_sc
   */
  public void setIasSubjectSc(final String inIasSubjectSc) {
    iasSubjectSc = inIasSubjectSc;
    dirty_iasSubjectSc = true;
  }

  /**
   * Get ias_content_en
   */
  public String getIasContentEn() {
    return iasContentEn == null ? "" : iasContentEn;
  }

  /**
   * Set ias_content_en
   */
  public void setIasContentEn(final String inIasContentEn) {
    iasContentEn = inIasContentEn;
    dirty_iasContentEn = true;
  }

  /**
   * Get ias_content_tc
   */
  public String getIasContentTc() {
    return iasContentTc == null ? "" : iasContentTc;
  }

  /**
   * Set ias_content_tc
   */
  public void setIasContentTc(final String inIasContentTc) {
    iasContentTc = inIasContentTc;
    dirty_iasContentTc = true;
  }

  /**
   * Get ias_content_sc
   */
  public String getIasContentSc() {
    return iasContentSc == null ? "" : iasContentSc;
  }

  /**
   * Set ias_content_sc
   */
  public void setIasContentSc(final String inIasContentSc) {
    iasContentSc = inIasContentSc;
    dirty_iasContentSc = true;
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
}
