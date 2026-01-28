package hk.gov.cmc.utils.maintainmessage.ias;

import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import hk.gov.cmc.common.CmcAppConstants;
import hk.gov.cmc.common.CmcAppPropertyNames;
import hk.gov.cmc.common.IntegrationConstants;
import hk.gov.cmc.common.MsgTypeConstant;
import hk.gov.cmc.dao.maintainmessage.message.IasMessageDAO;
import hk.gov.cmc.model.maintainmessage.param.MessageParam;
import hk.gov.cmc.model.maintainmessage.request.Recipient;
import hk.gov.cmc.model.maintainmessage.response.MessageResponse;
import hk.gov.cmc.model.maintainmessage.template.CmcTemplate;
import hk.gov.cmc.model.maintainmessage.user.IasUser;
import hk.gov.cmc.persistence.connection.hpfw.HPFW_Connection;
import hk.gov.cmc.persistence.maintainmessage.notification.IasEsNotiMap_;
import hk.gov.cmc.utils.maintainmessage.template.TemplateUtils;
import hk.gov.cmc.validator.IasMessageValidator;

public class IasMessageUtils {

    private static Log logger = LogFactory.getLog(IasMessageUtils.class);

    public static MessageResponse mergeTemplateAndCreateIasMessage(
            String idpId, String tranId, String recipientId,
            HPFW_Connection conn, String portalId, String iasMsgId, CmcTemplate cmcTemplate,
            String dataContentEn, String dataContentTc, String dataContentSc,
            Properties properties) throws Exception {

        logger.debug("mergeTemplateAndCreateIasMessage - portalId: " + portalId + ", iasMsgId: " + iasMsgId
                + ", idpId: " + idpId + ", tranId: " + tranId + ", recipientId: " + recipientId);
        logger.debug("mergeTemplateAndCreateIasMessage - cmcTemplate: " + cmcTemplate);
        logger.debug("mergeTemplateAndCreateIasMessage - dataContentEn: " + dataContentEn
                + ", dataContentTc: " + dataContentTc
                + ", dataContentSc: " + dataContentSc);

        MessageResponse msgRsp = null;

        String mergedIasMessageTitleEn = null;
        if (cmcTemplate.getIasSubjectEn() != null && cmcTemplate.getIasSubjectEn().length() > 0) {
            mergedIasMessageTitleEn = TemplateUtils.getIasMergedContent(
                    cmcTemplate.getIasSubjectEn(), cmcTemplate.getIasSubjectTc(),
                    cmcTemplate.getIasSubjectSc(),
                    dataContentEn, dataContentTc, dataContentSc,
                    CmcAppConstants.LANGUAGE_EN);
        }
        String mergedIasMessageTitleTc = null;
        if (cmcTemplate.getIasSubjectTc() != null && cmcTemplate.getIasSubjectTc().length() > 0) {
            mergedIasMessageTitleTc = TemplateUtils.getIasMergedContent(
                    cmcTemplate.getIasSubjectEn(), cmcTemplate.getIasSubjectTc(),
                    cmcTemplate.getIasSubjectSc(),
                    dataContentEn, dataContentTc, dataContentSc,
                    CmcAppConstants.LANGUAGE_TC);
        }
        String mergedIasMessageTitleSc = null;
        if (cmcTemplate.getIasSubjectSc() != null && cmcTemplate.getIasSubjectSc().length() > 0) {
            mergedIasMessageTitleSc = TemplateUtils.getIasMergedContent(
                    cmcTemplate.getIasSubjectEn(), cmcTemplate.getIasSubjectTc(),
                    cmcTemplate.getIasSubjectSc(),
                    dataContentEn, dataContentTc, dataContentSc,
                    CmcAppConstants.LANGUAGE_SC);
        }
        String mergedIasMessageDetailEn = null;
        if (cmcTemplate.getIasContentEn() != null && cmcTemplate.getIasContentEn().length() > 0) {
            mergedIasMessageDetailEn = TemplateUtils.getIasMergedContent(
                    cmcTemplate.getIasContentEn(), cmcTemplate.getIasContentTc(),
                    cmcTemplate.getIasContentSc(),
                    dataContentEn, dataContentTc, dataContentSc,
                    CmcAppConstants.LANGUAGE_EN);
        }
        String mergedIasMessageDetailTc = null;
        if (cmcTemplate.getIasContentTc() != null && cmcTemplate.getIasContentTc().length() > 0) {
            mergedIasMessageDetailTc = TemplateUtils.getIasMergedContent(
                    cmcTemplate.getIasContentEn(), cmcTemplate.getIasContentTc(),
                    cmcTemplate.getIasContentSc(),
                    dataContentEn, dataContentTc, dataContentSc,
                    CmcAppConstants.LANGUAGE_TC);
        }
        String mergedIasMessageDetailSc = null;
        if (cmcTemplate.getIasContentSc() != null && cmcTemplate.getIasContentSc().length() > 0) {
            mergedIasMessageDetailSc = TemplateUtils.getIasMergedContent(
                    cmcTemplate.getIasContentEn(), cmcTemplate.getIasContentTc(),
                    cmcTemplate.getIasContentSc(),
                    dataContentEn, dataContentTc, dataContentSc,
                    CmcAppConstants.LANGUAGE_SC);
        }

        // validate merged message size and add to the web service response
        msgRsp = IasMessageValidator.validateMergedMsgSize(
                idpId, tranId, recipientId,
                mergedIasMessageTitleEn, mergedIasMessageTitleTc, mergedIasMessageTitleSc,
                mergedIasMessageDetailEn, mergedIasMessageDetailTc, mergedIasMessageDetailSc,
                MsgTypeConstant.MESSAGE);

        // create the master record in IAS_TO_DO_ITEM if it is the first message and the message size is valid
        if (msgRsp == null) {
            String encKeyStoreId = properties
                    .getProperty(CmcAppPropertyNames.CMC_MESSAGE_ENCRYPT_KEY_STORE_ID_PROPERTY_NAME);
            String iasEsAppSuffixTagName = properties
                    .getProperty(CmcAppPropertyNames.IAS_ES_APP_SUFFIX_TAG_NAME_PROPERTY_NAME);
            IasMessageDAO iasMessageDAO = new IasMessageDAO();

            String iasEsAppSuffixEn = cmcTemplate.getIasEsAppSuffixEn();
            String iasEsAppSuffixTc = cmcTemplate.getIasEsAppSuffixTc();
            String iasEsAppSuffixSc = cmcTemplate.getIasEsAppSuffixSc();
            if (TemplateUtils.getNodeValueFromMetaData(dataContentEn, iasEsAppSuffixTagName) != null) {
                iasEsAppSuffixEn = TemplateUtils.getNodeValueFromMetaData(dataContentEn, iasEsAppSuffixTagName);
            }
            if (TemplateUtils.getNodeValueFromMetaData(dataContentTc, iasEsAppSuffixTagName) != null) {
                iasEsAppSuffixTc = TemplateUtils.getNodeValueFromMetaData(dataContentTc, iasEsAppSuffixTagName);
            }
            if (TemplateUtils.getNodeValueFromMetaData(dataContentSc, iasEsAppSuffixTagName) != null) {
                iasEsAppSuffixSc = TemplateUtils.getNodeValueFromMetaData(dataContentSc, iasEsAppSuffixTagName);
            }

            iasMessageDAO.createIasMessage(portalId, iasMsgId,
                    cmcTemplate.getTemplateId(), cmcTemplate.getTemplateVersion(),
                    mergedIasMessageTitleEn, mergedIasMessageTitleTc, mergedIasMessageTitleSc,
                    mergedIasMessageDetailEn, mergedIasMessageDetailTc, mergedIasMessageDetailSc,
                    iasEsAppSuffixEn, iasEsAppSuffixTc, iasEsAppSuffixSc,
                    encKeyStoreId, conn);
        }

        return msgRsp;
    }

    public static MessageResponse createIasUserMessageByOpenId(HPFW_Connection conn, CmcTemplate cmcTemplate,
            Recipient recipient, IasUser iasUser, String iasMsgId) throws Exception {

        logger.debug("createIasUserMessageByOpenId - recipient: " + recipient + ", iasUser: " + iasUser + ", iasMsgId: "
                + iasMsgId);
        logger.debug("createIasUserMessageByOpenId - recipient.getRecipientIdType(): "
                + recipient.getRecipientIdType());

        MessageResponse msgRsp = null;

        IasMessageDAO iasMessageDAO = new IasMessageDAO();
        iasMessageDAO.createIasUserMessageByOpenId(conn, cmcTemplate.getServiceProviderId(),
                cmcTemplate.getClientId(), iasUser.getOpenId(), iasMsgId,
                recipient.getTranId(), iasUser.getNotiId());

        // Change Opt In from U to Y (For deregister user)
        if (iasUser.getNotiId() != null && IntegrationConstants.OPT_IN_U.equals(iasUser.getOptIn())) {
            IasEsNotiMap_ notiMap = new IasEsNotiMap_(conn, iasUser.getNotiId(),
                    cmcTemplate.getServiceProviderId());
            notiMap.setOptIn(IntegrationConstants.OPT_IN_Y);
            notiMap.update(conn);
        }

        return msgRsp;
    }
}
