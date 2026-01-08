package hk.gov.cmc.dao.maintainmessage.template;

import java.util.ArrayList;

import java.sql.ResultSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import hk.gov.cmc.model.maintainmessage.template.CmcTemplate;
import hk.gov.cmc.persistence.connection.hpfw.HPFW_Connection;
import hk.gov.cmc.persistence.connection.hpfw.Parameter;

public class CmcTemplateDao {

    private static Log logger = LogFactory.getLog(CmcTemplateDao.class);

    public CmcTemplateDao() {
    }

    public CmcTemplate getTemplateByIdVersion(HPFW_Connection conn, String templateId, String templateVersion,
            String typeInd) throws Exception {
        logger.info("getTemplateByIdVersion - templateId: " + templateId + ", templateVersion: " + templateVersion
                + ", typeInd: " + typeInd);

        CmcTemplate cmcTemplate = null;
        ResultSet rs = null;

        final String SELECT_TEMPLATE_CONTENT = "select t.TEMPLATE_ID, t.TEMPLATE_VERSION, t.SERVICE_PROVIDER_ID, t.SUBJECT_EN, t.SUBJECT_TC, t.SUBJECT_SC, t.CONTENT_EN, t.CONTENT_TC, t.CONTENT_SC, "
                + "e.DEFAULT_FOLDER_ID, e.USER_IND, s.APP_ID, s.REQ_LINKUP_IND, e.CUT_OFF_DATE, "
                + "t.EMAIL_IND, t.EMAIL_SUBJECT_EN, t.EMAIL_SUBJECT_TC, t.EMAIL_SUBJECT_SC, t.EMAIL_CONTENT_EN, t.EMAIL_CONTENT_TC, t.EMAIL_CONTENT_SC, "
                + "IFNULL(t.ALERT_EMAIL_IND, 'N') AS ALERT_EMAIL_IND, "
                + "t.IAS_SUBJECT_EN, t.IAS_SUBJECT_TC, t.IAS_SUBJECT_SC, t.IAS_CONTENT_EN, t.IAS_CONTENT_TC, t.IAS_CONTENT_SC, IFNULL(t.IAS_IND, 'N') AS IAS_IND, "
                + "s.CLIENT_ID, s.IAS_AUTO_CREATE_IND, IFNULL(t.IAS_SHOW_ES_SET_BTN, 'N') AS IAS_SHOW_ES_SET_BTN, IFNULL(s.IAS_OPT_SP_ID, s.SERVICE_PROVIDER_ID) AS IAS_OPT_SP_ID, "
                + "t.MOBILE_MSG_IND, t.MOBILE_SUBJECT_EN, t.MOBILE_SUBJECT_TC, t.MOBILE_SUBJECT_SC, t.MOBILE_CONTENT_EN, t.MOBILE_CONTENT_TC, t.MOBILE_CONTENT_SC, "
                + "IFNULL(t.TRUST_SP_IND, 'N') AS TRUST_SP_IND, t.NOTI_PRIORITY "
                + ", t.IAS_ES_APP_SUFFIX_EN, t.IAS_ES_APP_SUFFIX_TC, t.IAS_ES_APP_SUFFIX_SC "
                + "from CMC_TEMPLATE t, CMC_TEMPLATE_TYPE e, CMC_SERVICE_PROVIDER s "
                + "where t.TEMPLATE_ID = ? "
                + "and t.TEMPLATE_VERSION = ? "
                + "and t.TEMPLATE_TYPE = e.TEMPLATE_TYPE and e.TYPE_IND = ? "
                + "and t.SERVICE_PROVIDER_ID = s.SERVICE_PROVIDER_ID "
                + "and t.STATUS = 'A' and e.STATUS = 'A' and s.STATUS = 'A' ";

        try {

            ArrayList<Parameter> paraList = new ArrayList<Parameter>();
            paraList.add(new Parameter(Parameter.String, templateId));
            paraList.add(new Parameter(Parameter.String, templateVersion));
            paraList.add(new Parameter(Parameter.String, typeInd));
            rs = conn.getResultSet(SELECT_TEMPLATE_CONTENT, paraList);

            if (rs.next()) {
                cmcTemplate = new CmcTemplate();
                cmcTemplate.setTemplateId(rs.getString("TEMPLATE_ID"));
                cmcTemplate.setTemplateVersion(rs.getString("TEMPLATE_VERSION"));
                cmcTemplate.setSubjectEn(rs.getString("SUBJECT_EN"));
                cmcTemplate.setSubjectTc(rs.getString("SUBJECT_TC"));
                cmcTemplate.setSubjectSc(rs.getString("SUBJECT_SC"));
                cmcTemplate.setContentEn(rs.getString("CONTENT_EN"));
                cmcTemplate.setContentTc(rs.getString("CONTENT_TC"));
                cmcTemplate.setContentSc(rs.getString("CONTENT_SC"));
                cmcTemplate.setDefaultFolderId(rs.getString("DEFAULT_FOLDER_ID"));
                cmcTemplate.setServiceProviderId(rs.getString("SERVICE_PROVIDER_ID"));
                cmcTemplate.setUserInd(rs.getString("USER_IND"));
                cmcTemplate.setAppId(rs.getString("APP_ID"));
                cmcTemplate.setReqLinkupInd(rs.getString("REQ_LINKUP_IND"));
                cmcTemplate.setCutOffDate(String.valueOf(rs.getInt("CUT_OFF_DATE")));
                cmcTemplate.setEmailInd(rs.getString("EMAIL_IND"));
                cmcTemplate.setEmailSubjectEn(rs.getString("EMAIL_SUBJECT_EN"));
                cmcTemplate.setEmailSubjectTc(rs.getString("EMAIL_SUBJECT_TC"));
                cmcTemplate.setEmailSubjectSc(rs.getString("EMAIL_SUBJECT_SC"));
                cmcTemplate.setEmailContentEn(rs.getString("EMAIL_CONTENT_EN"));
                cmcTemplate.setEmailContentTc(rs.getString("EMAIL_CONTENT_TC"));
                cmcTemplate.setEmailContentSc(rs.getString("EMAIL_CONTENT_SC"));
                cmcTemplate.setAlertEmailInd(rs.getString("ALERT_EMAIL_IND"));
                cmcTemplate.setMobileMsgInd(rs.getString("MOBILE_MSG_IND"));
                cmcTemplate.setMobileSubjectEn(rs.getString("MOBILE_SUBJECT_EN"));
                cmcTemplate.setMobileSubjectTc(rs.getString("MOBILE_SUBJECT_TC"));
                cmcTemplate.setMobileSubjectSc(rs.getString("MOBILE_SUBJECT_SC"));
                cmcTemplate.setMobileContentEn(rs.getString("MOBILE_CONTENT_EN"));
                cmcTemplate.setMobileContentTc(rs.getString("MOBILE_CONTENT_TC"));
                cmcTemplate.setMobileContentSc(rs.getString("MOBILE_CONTENT_SC"));
                cmcTemplate.setTrustSpInd(rs.getString("TRUST_SP_IND"));
                cmcTemplate.setNotiPriority(rs.getString("NOTI_PRIORITY"));
                cmcTemplate.setIasInd(rs.getString("IAS_IND"));
                cmcTemplate.setIasSubjectEn(rs.getString("IAS_SUBJECT_EN"));
                cmcTemplate.setIasSubjectTc(rs.getString("IAS_SUBJECT_TC"));
                cmcTemplate.setIasSubjectSc(rs.getString("IAS_SUBJECT_SC"));
                cmcTemplate.setIasContentEn(rs.getString("IAS_CONTENT_EN"));
                cmcTemplate.setIasContentTc(rs.getString("IAS_CONTENT_TC"));
                cmcTemplate.setIasContentSc(rs.getString("IAS_CONTENT_SC"));
                cmcTemplate.setClientId(rs.getString("CLIENT_ID"));
                cmcTemplate.setIasAutoCreateInd(rs.getString("IAS_AUTO_CREATE_IND"));
                cmcTemplate.setIasOptSpId(rs.getString("IAS_OPT_SP_ID"));
                cmcTemplate.setIasShowEsSetBtn(rs.getString("IAS_SHOW_ES_SET_BTN"));
                cmcTemplate.setIasEsAppSuffixEn(rs.getString("IAS_ES_APP_SUFFIX_EN"));
                cmcTemplate.setIasEsAppSuffixTc(rs.getString("IAS_ES_APP_SUFFIX_TC"));
                cmcTemplate.setIasEsAppSuffixSc(rs.getString("IAS_ES_APP_SUFFIX_SC"));
            }

        } finally {
            if (rs != null) {
                HPFW_Connection.close(rs);
            }
        }

        return cmcTemplate;
    }
}
