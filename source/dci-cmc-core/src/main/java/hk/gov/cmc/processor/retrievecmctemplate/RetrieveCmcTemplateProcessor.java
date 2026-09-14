package hk.gov.cmc.processor.retrievecmctemplate;

import java.util.ArrayList;
import java.util.HashSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import hk.gov.cmc.persistence.connection.hpfw.Parameter;
import hk.gov.cmc.persistence.template.CmcTemplate_;
import hk.gov.cmc.common.ResultCodes;
import hk.gov.cmc.common.ResultMessages;
import hk.gov.cmc.config.CmcSystemParam;
import hk.gov.cmc.dao.maintainmessage.CmcServiceProviderDAO;
import hk.gov.cmc.model.retrievecmctemplate.request.RetrieveCmcTemplateRequest;
import hk.gov.cmc.model.retrievecmctemplate.response.RetrieveCmcTemplateResponse;
import hk.gov.cmc.model.retrievecmctemplate.response.TemplateDetail;
import hk.gov.cmc.persistence.connection.hpfw.HPFW_Connection;

public class RetrieveCmcTemplateProcessor {

    private static Log logger = LogFactory.getLog(RetrieveCmcTemplateProcessor.class);

    public RetrieveCmcTemplateResponse processMessage(HPFW_Connection conn, String appId,
            RetrieveCmcTemplateRequest retrieveCmcTemplateReq) throws Exception {
        logger.info("processMessage - START");

        String templateId = retrieveCmcTemplateReq.getTemplateId();
        String templateVersion = retrieveCmcTemplateReq.getTemplateVersion();

        RetrieveCmcTemplateResponse response = new RetrieveCmcTemplateResponse();
        response.setTemplateId(templateId);
        response.setTemplateVersion(templateVersion);

        logger.info("appId: " + appId + ", templateId: " + templateId + ",templateVersion:" + templateVersion);

        try {
            // Retrieve list of service provider id(s) from application id
            HashSet<String> serviceProviderIdSet = CmcServiceProviderDAO.getServiceProviderIdByAppId(conn, appId);

            // validate the application id
            if (serviceProviderIdSet.isEmpty()) {
                // no service provider is found
                response.setResultCd(ResultCodes.RESULT_CD_SENDER_APPID_NOT_FOUND); // 0001 template not found
                response.setResultMsg(ResultMessages.RESULT_MSG_SENDER_APPID_NOT_FOUND);
                return response;
            }

            ArrayList<Parameter> paraList = new ArrayList<Parameter>();
            paraList.add(new Parameter(Parameter.String, templateId));
            paraList.add(new Parameter(Parameter.String, templateVersion));

            ArrayList<CmcTemplate_> list = CmcTemplate_.getResultList(conn,
                    "where template_id= ? and template_version = ? and status = 'A'", paraList);
            if (list == null || list.isEmpty()) {
                // no template is found
                response.setResultCd(ResultCodes.RESULT_CD_RETRIEVE_CMC_TEMPLATE_NOT_FOUND); // 0001 template not found
                response.setResultMsg(ResultMessages.RESULT_MSG_RETRIEVE_CMC_TEMPLATE_NOT_FOUND);
                return response;
            }

            // Allow MARS to use B/D's templates to send MyMessage
            String trustSpAppId = CmcSystemParam.getPara("TRUST_SP_APP_ID");

            CmcTemplate_ template = list.get(0);
            if (!serviceProviderIdSet.contains(template.getServiceProviderId())) {
                if (trustSpAppId == null || !trustSpAppId.trim().equals(appId) || template.getTrustSpInd().equals("")
                        || template.getTrustSpInd().equals("N")) {
                    response.setResultCd(ResultCodes.RESULT_CD_RETRIEVE_CMC_TEMPLATE_NOT_FOUND); // 0001 template not found
                    response.setResultMsg(ResultMessages.RESULT_MSG_RETRIEVE_CMC_TEMPLATE_NOT_FOUND);
                    return response;
                }
            }

            TemplateDetail detail = new TemplateDetail();
            detail.setContentEn(template.getContentEn());
            detail.setContentTc(template.getContentTc());
            detail.setContentSc(template.getContentSc());
            detail.setSubjectEn(template.getSubjectEn());
            detail.setSubjectTc(template.getSubjectTc());
            detail.setSubjectSc(template.getSubjectSc());

            // Add EmailContentRequired flag for backward compatible support
            // if ("Y".equals(retrieveCmcTemplateReq.getEmailContentRequired())) {
            // detail.setEmailContentEn(template.getEmailContentEn());
            // detail.setEmailContentTc(template.getEmailContentTc());
            // detail.setEmailContentSc(template.getEmailContentSc());
            // detail.setEmailSubjectEn(template.getEmailSubjectEn());
            // detail.setEmailSubjectTc(template.getEmailSubjectTc());
            // detail.setEmailSubjectSc(template.getEmailSubjectSc());
            // }

            // Add MobileConentRequired flag for backward compatible support
            // if ("Y".equals(retrieveCmcTemplateReq.getMobileContentRequired())) {
            // detail.setMobileContentEn(template.getMobileContentEn());
            // detail.setMobileContentTc(template.getMobileContentTc());
            // detail.setMobileContentSc(template.getMobileContentSc());
            // detail.setMobileSubjectEn(template.getMobileSubjectEn());
            // detail.setMobileSubjectTc(template.getMobileSubjectTc());
            // detail.setMobileSubjectSc(template.getMobileSubjectSc());
            // }

            response.setTemplateDetail(detail);
            response.setResultCd(ResultCodes.RESULT_CD_SUCCESS); // 0000 template found
            response.setResultMsg(ResultMessages.RESULT_MSG_SUCCESS);

            logger.info("processMessage - END");
        } catch (Exception ex) {
            logger.error("processMessage error", ex);
            response.setResultCd(ResultCodes.RESULT_CD_GENERAL_ERROR);
            response.setResultMsg(ResultMessages.RESULT_MSG_GENERAL_ERROR);
        }

        return response;
    }
}
