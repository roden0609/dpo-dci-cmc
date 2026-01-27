package hk.gov.cmc.utils.maintainmessage.ias;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import hk.gov.cmc.common.CmcAppConstants;
import hk.gov.cmc.common.CmcAppPropertyNames;
import hk.gov.cmc.common.IntegrationConstants;
import hk.gov.cmc.common.MsgTypeConstant;
import hk.gov.cmc.common.RecipientIDTypeConstant;
import hk.gov.cmc.common.ResultCodes;
import hk.gov.cmc.common.ResultMessages;
import hk.gov.cmc.dao.maintainmessage.application.IasApplicationDAO;
import hk.gov.cmc.model.maintainmessage.param.MessageParam;
import hk.gov.cmc.model.maintainmessage.request.Recipient;
import hk.gov.cmc.model.maintainmessage.response.MessageResponse;
import hk.gov.cmc.model.maintainmessage.template.CmcTemplate;
import hk.gov.cmc.model.maintainmessage.user.IasUser;
import hk.gov.cmc.model.maintainmessage.user.IasUserWrapped;
import hk.gov.cmc.persistence.connection.hpfw.HPFW_Connection;
import hk.gov.cmc.persistence.maintainmessage.application.IasUserApplication_;
import hk.gov.cmc.persistence.maintainmessage.notification.IasEsNotiMap_;
import hk.gov.cmc.utils.common.EncUtils;
import hk.gov.cmc.utils.maintainmessage.MaintainMessageUtils;
import hk.gov.cmc.utils.maintainmessage.template.TemplateUtils;
import hk.gov.cmc.validator.IasApplicationValidator;

public class IasApplicationUtils {

    private static Log logger = LogFactory.getLog(IasApplicationUtils.class);

    public static MessageResponse mergeTemplateAndCreateIasApplication(
            String idpId, String tranId, String recipientId,
            HPFW_Connection conn, String portalId, String iasApplicationId,
            CmcTemplate cmcTemplate, List<MessageParam> cmcMessageParam,
            String dataContentEn, String dataContentTc, String dataContentSc,
            Properties properties) throws Exception {

        logger.debug("mergeTemplateAndCreateIasApplication - portalId: " + portalId + ", iasApplicationId: "
                + iasApplicationId
                + ", idpId: " + idpId + ", tranId: " + tranId + ", recipientId: " + recipientId + ", cmcMessageParam: "
                + cmcMessageParam);
        logger.debug("mergeTemplateAndCreateIasApplication - cmcTemplate: " + cmcTemplate);
        logger.debug("mergeTemplateAndCreateIasApplication - dataContentEn: " + dataContentEn + ", dataContentTc: "
                + dataContentTc
                + ", dataContentSc: " + dataContentSc);

        MessageResponse msgRsp = null;

        String mergedIasApplicationTitleEn = null;
        if (cmcTemplate.getIasSubjectEn() != null && cmcTemplate.getIasSubjectEn().length() > 0) {
            mergedIasApplicationTitleEn = TemplateUtils.getIasMergedContent(
                    cmcTemplate.getIasSubjectEn(), cmcTemplate.getIasSubjectTc(),
                    cmcTemplate.getIasSubjectSc(),
                    dataContentEn, dataContentTc, dataContentSc,
                    CmcAppConstants.LANGUAGE_EN, cmcMessageParam);
        }
        String mergedIasApplicationTitleTc = null;
        if (cmcTemplate.getIasSubjectTc() != null && cmcTemplate.getIasSubjectTc().length() > 0) {
            mergedIasApplicationTitleTc = TemplateUtils.getIasMergedContent(
                    cmcTemplate.getIasSubjectEn(), cmcTemplate.getIasSubjectTc(),
                    cmcTemplate.getIasSubjectSc(),
                    dataContentEn, dataContentTc, dataContentSc,
                    CmcAppConstants.LANGUAGE_TC, cmcMessageParam);
        }
        String mergedIasApplicationTitleSc = null;
        if (cmcTemplate.getIasSubjectSc() != null && cmcTemplate.getIasSubjectSc().length() > 0) {
            mergedIasApplicationTitleSc = TemplateUtils.getIasMergedContent(
                    cmcTemplate.getIasSubjectEn(), cmcTemplate.getIasSubjectTc(),
                    cmcTemplate.getIasSubjectSc(),
                    dataContentEn, dataContentTc, dataContentSc,
                    CmcAppConstants.LANGUAGE_SC, cmcMessageParam);
        }
        String mergedIasApplicationDetailEn = null;
        if (cmcTemplate.getIasContentEn() != null && cmcTemplate.getIasContentEn().length() > 0) {
            mergedIasApplicationDetailEn = TemplateUtils.getIasMergedContent(
                    cmcTemplate.getIasContentEn(), cmcTemplate.getIasContentTc(),
                    cmcTemplate.getIasContentSc(),
                    dataContentEn, dataContentTc, dataContentSc,
                    CmcAppConstants.LANGUAGE_EN, cmcMessageParam);
        }
        String mergedIasApplicationDetailTc = null;
        if (cmcTemplate.getIasContentTc() != null && cmcTemplate.getIasContentTc().length() > 0) {
            mergedIasApplicationDetailTc = TemplateUtils.getIasMergedContent(
                    cmcTemplate.getIasContentEn(), cmcTemplate.getIasContentTc(),
                    cmcTemplate.getIasContentSc(),
                    dataContentEn, dataContentTc, dataContentSc,
                    CmcAppConstants.LANGUAGE_TC, cmcMessageParam);
        }
        String mergedIasApplicationDetailSc = null;
        if (cmcTemplate.getIasContentSc() != null && cmcTemplate.getIasContentSc().length() > 0) {
            mergedIasApplicationDetailSc = TemplateUtils.getIasMergedContent(
                    cmcTemplate.getIasContentEn(), cmcTemplate.getIasContentTc(),
                    cmcTemplate.getIasContentSc(),
                    dataContentEn, dataContentTc, dataContentSc,
                    CmcAppConstants.LANGUAGE_SC, cmcMessageParam);
        }

        // validate merged message size and add to the web service response
        msgRsp = IasApplicationValidator.validateMergedApplicationSize(
                idpId, tranId, recipientId,
                mergedIasApplicationTitleEn, mergedIasApplicationTitleTc, mergedIasApplicationTitleSc,
                mergedIasApplicationDetailEn, mergedIasApplicationDetailTc, mergedIasApplicationDetailSc,
                MsgTypeConstant.APPLICATION);

        // create the master record in IAS_APPLICATION if it is the first message and the message size is valid
        if (msgRsp == null) {
            String encKeyStoreId = properties
                    .getProperty(CmcAppPropertyNames.CMC_MESSAGE_ENCRYPT_KEY_STORE_ID_PROPERTY_NAME);
            String iasEsAppSuffixTagName = properties
                    .getProperty(CmcAppPropertyNames.IAS_ES_APP_SUFFIX_TAG_NAME_PROPERTY_NAME);
            IasApplicationDAO iasApplicationDAO = new IasApplicationDAO();

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

            iasApplicationDAO.createIasApplication(portalId, iasApplicationId,
                    cmcTemplate.getTemplateId(), cmcTemplate.getTemplateVersion(),
                    mergedIasApplicationTitleEn, mergedIasApplicationTitleTc, mergedIasApplicationTitleSc,
                    mergedIasApplicationDetailEn, mergedIasApplicationDetailTc,
                    mergedIasApplicationDetailSc,
                    iasEsAppSuffixEn, iasEsAppSuffixTc, iasEsAppSuffixSc,
                    encKeyStoreId, iasEsAppSuffixTagName, conn);
        }

        return msgRsp;
    }

    public static List<IasUserApplication_> getCorrelatedIasUserApplicationListByOpenIdOrHKID(HPFW_Connection conn,
            CmcTemplate cmcTemplate, Recipient recipient, IasUserWrapped iasUserWrapped, Properties properties)
            throws Exception {

        IasApplicationDAO iasApplicationDAO = new IasApplicationDAO();

        List<IasUserApplication_> iasUserApplicationList = new ArrayList<IasUserApplication_>();
        if (RecipientIDTypeConstant.OPEN_ID.equals(recipient.getRecipientIdType())) {

            iasUserApplicationList = iasApplicationDAO.getIasUserApplicationList(conn, cmcTemplate.getClientId(),
                    iasUserWrapped.getIasUser().getOpenId(), recipient.getCorrelatedTranId());

        } else if (RecipientIDTypeConstant.HKID.equals(recipient.getRecipientIdType())) {

            List<IasUserApplication_> tempActiveIasUserApplicationList = iasApplicationDAO.getIasUserApplicationList(
                    conn, cmcTemplate.getClientId(), iasUserWrapped.getIasUser().getHkidHashed(),
                    recipient.getCorrelatedTranId());
            for (IasUserApplication_ iasUserApplication : tempActiveIasUserApplicationList) {
                String hkidDecrypted = EncUtils.decrypt(
                        iasUserApplication.getHkidEncrypted(),
                        properties.getProperty(CmcAppPropertyNames.HKID_ENCRYPT_PASSPHRASE_ID_PROPERTY_NAME),
                        properties.getProperty(CmcAppPropertyNames.HKID_ENCRYPT_PASSPHRASE_USAGE_PROPERTY_NAME));
                if (hkidDecrypted.equals(iasUserWrapped.getHkid())) {
                    iasUserApplicationList.add(iasUserApplication);
                }
            }

        }

        return iasUserApplicationList;
    }

    public static MessageResponse markDeleteIasUserApplicationByOpenIdOrHKIDAndCorrTranId(HPFW_Connection conn,
            CmcTemplate cmcTemplate, Recipient recipient, IasUser iasUser) throws Exception {

        MessageResponse msgRsp = null;
        IasApplicationDAO iasApplicationDAO = new IasApplicationDAO();

        if (RecipientIDTypeConstant.OPEN_ID.equals(recipient.getRecipientIdType())) {
            iasApplicationDAO.markDeleteIasUserApplicationByCorrTranId(conn, cmcTemplate.getClientId(),
                    iasUser.getOpenId(), recipient.getTranId(), recipient.getCorrelatedTranId());
        } else if (RecipientIDTypeConstant.HKID.equals(recipient.getRecipientIdType())) {
            iasApplicationDAO.markDeleteIasUserApplicationByCorrTranId(conn, cmcTemplate.getClientId(),
                    iasUser.getHkidHashed(), recipient.getTranId(), recipient.getCorrelatedTranId());
        } else {
            msgRsp = MaintainMessageUtils.getMessageResponse(recipient.getTranId(), recipient.getIdpId(),
                    recipient.getRecipientId(),
                    MsgTypeConstant.APPLICATION, ResultCodes.RESULT_CD_RECIPIENT_ID_TYPE_NOT_VALID,
                    ResultMessages.RESULT_MSG_RECIPIENT_ID_TYPE_NOT_VALID);
        }

        return msgRsp;
    }
    // CMC-2025-022: Use TRAN_ID to correlate iAM Smart Application Status instead of APP_REF_NUM - END

    public static MessageResponse createIasUserApplicationByOpenIdOrHKID(HPFW_Connection conn, CmcTemplate cmcTemplate,
            Recipient recipient, IasUser iasUser, String iasApplicationId, String operationType) throws Exception {

        logger.debug("createIasUserApplicationByOpenIdOrHKID - recipient: " + recipient + ", iasUser: " + iasUser
                + ", iasApplicationId: " + iasApplicationId + ", operationType: " + operationType);
        logger.debug("createIasUserApplicationByOpenIdOrHKID - recipient.getRecipientIdType(): "
                + recipient.getRecipientIdType());

        MessageResponse msgRsp = null;
        IasApplicationDAO iasApplicationDAO = new IasApplicationDAO();

        if (RecipientIDTypeConstant.OPEN_ID.equals(recipient.getRecipientIdType())) {
            iasApplicationDAO.createIasUserApplicationByOpenId(
                    conn, cmcTemplate.getServiceProviderId(),
                    cmcTemplate.getClientId(), iasUser.getOpenId(), iasApplicationId, recipient.getTranId(),
                    recipient.getAppRefNum(), recipient.getAppStatus().value(),
                    new Timestamp(recipient.getAppStatusUpdateDate().toInstant().toEpochMilli()),
                    recipient.getContactNum(), recipient.getContactEmail(), recipient.getMiscInfo(),
                    operationType, iasUser.getNotiId());

            // Change Opt In from U to Y (For deregister user)
            if (iasUser.getNotiId() != null && IntegrationConstants.OPT_IN_U.equals(iasUser.getOptIn())) {
                IasEsNotiMap_ notiMap = new IasEsNotiMap_(conn, iasUser.getNotiId(),
                        cmcTemplate.getServiceProviderId());
                notiMap.setOptIn(IntegrationConstants.OPT_IN_Y);
                notiMap.update(conn);
            }

        } else if (RecipientIDTypeConstant.HKID.equals(recipient.getRecipientIdType())) {

            iasApplicationDAO.createIasUserApplicationByHKID(
                    conn, cmcTemplate.getServiceProviderId(),
                    cmcTemplate.getClientId(), iasUser.getHkidHashed(), iasApplicationId, recipient.getTranId(),
                    recipient.getAppRefNum(), recipient.getAppStatus().value(),
                    new Timestamp(recipient.getAppStatusUpdateDate().getTime()),
                    recipient.getContactNum(), recipient.getContactEmail(),
                    recipient.getMiscInfo(), operationType, iasUser.getNotiId(), iasUser.getHkidEncrypted());

            // Change Opt In from U to Y (For deregister user)
            if (iasUser.getNotiId() != null && IntegrationConstants.OPT_IN_U.equals(iasUser.getOptIn())) {
                IasEsNotiMap_ notiMap = new IasEsNotiMap_(conn, iasUser.getNotiId(),
                        cmcTemplate.getServiceProviderId());
                notiMap.setOptIn(IntegrationConstants.OPT_IN_Y);
                notiMap.update(conn);
            }

        } else {
            msgRsp = MaintainMessageUtils.getMessageResponse(recipient.getTranId(), recipient.getIdpId(),
                    recipient.getRecipientId(),
                    MsgTypeConstant.APPLICATION, ResultCodes.RESULT_CD_RECIPIENT_ID_TYPE_NOT_VALID,
                    ResultMessages.RESULT_MSG_RECIPIENT_ID_TYPE_NOT_VALID);
        }

        return msgRsp;
    }

}
