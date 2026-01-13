package hk.gov.cmc.utils.maintainmessage.ias;

import java.sql.Date;
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
import hk.gov.cmc.dao.maintainmessage.todoitem.IasToDoItemDAO;
import hk.gov.cmc.model.maintainmessage.param.MessageParam;
import hk.gov.cmc.model.maintainmessage.request.Recipient;
import hk.gov.cmc.model.maintainmessage.response.MessageResponse;
import hk.gov.cmc.model.maintainmessage.template.CmcTemplate;
import hk.gov.cmc.model.maintainmessage.user.IasUser;
import hk.gov.cmc.model.maintainmessage.user.IasUserWrapped;
import hk.gov.cmc.persistence.connection.hpfw.HPFW_Connection;
import hk.gov.cmc.persistence.ias.notification.IasEsNotiMap_;
import hk.gov.cmc.persistence.ias.todoitem.IasUserToDoItem_;
import hk.gov.cmc.utils.common.EncUtils;
import hk.gov.cmc.utils.maintainmessage.MaintainMessageUtils;
import hk.gov.cmc.utils.maintainmessage.template.TemplateUtils;
import hk.gov.cmc.validator.IasToDoItemValidator;

public class IasToDoItemUtils {

    private static Log logger = LogFactory.getLog(IasToDoItemUtils.class);

    public static MessageResponse mergeTemplateAndCreateIasToDoItem(
            String idpId, String tranId, String recipientId,
            HPFW_Connection conn, String portalId, String iasToDoItemId,
            CmcTemplate cmcTemplate, List<MessageParam> cmcMessageParam,
            String dataContentEn, String dataContentTc, String dataContentSc,
            Properties properties) throws Exception {

        logger.debug("mergeTemplateAndCreateIasToDoItem - portalId: " + portalId + ", iasToDoItemId: " + iasToDoItemId
                + ", idpId: " + idpId + ", tranId: " + tranId + ", recipientId: " + recipientId
                + ", cmcMessageParam: " + cmcMessageParam);
        logger.debug("mergeTemplateAndCreateIasToDoItem - cmcTemplate: " + cmcTemplate);
        logger.debug("mergeTemplateAndCreateIasToDoItem - dataContentEn: " + dataContentEn
                + ", dataContentTc: " + dataContentTc
                + ", dataContentSc: " + dataContentSc);

        MessageResponse msgRsp = null;

        String mergedIasToDoItemTitleEn = null;
        if (cmcTemplate.getIasSubjectEn() != null && cmcTemplate.getIasSubjectEn().length() > 0) {
            mergedIasToDoItemTitleEn = TemplateUtils.getIasMergedContent(
                    cmcTemplate.getIasSubjectEn(), cmcTemplate.getIasSubjectTc(),
                    cmcTemplate.getIasSubjectSc(),
                    dataContentEn, dataContentTc, dataContentSc,
                    CmcAppConstants.LANGUAGE_EN, cmcMessageParam);
        }
        String mergedIasToDoItemTitleTc = null;
        if (cmcTemplate.getIasSubjectTc() != null && cmcTemplate.getIasSubjectTc().length() > 0) {
            mergedIasToDoItemTitleTc = TemplateUtils.getIasMergedContent(
                    cmcTemplate.getIasSubjectEn(), cmcTemplate.getIasSubjectTc(),
                    cmcTemplate.getIasSubjectSc(),
                    dataContentEn, dataContentTc, dataContentSc,
                    CmcAppConstants.LANGUAGE_TC, cmcMessageParam);
        }
        String mergedIasToDoItemTitleSc = null;
        if (cmcTemplate.getIasSubjectSc() != null && cmcTemplate.getIasSubjectSc().length() > 0) {
            mergedIasToDoItemTitleSc = TemplateUtils.getIasMergedContent(
                    cmcTemplate.getIasSubjectEn(), cmcTemplate.getIasSubjectTc(),
                    cmcTemplate.getIasSubjectSc(),
                    dataContentEn, dataContentTc, dataContentSc,
                    CmcAppConstants.LANGUAGE_SC, cmcMessageParam);
        }
        String mergedIasToDoItemDetailEn = null;
        if (cmcTemplate.getIasContentEn() != null && cmcTemplate.getIasContentEn().length() > 0) {
            mergedIasToDoItemDetailEn = TemplateUtils.getIasMergedContent(
                    cmcTemplate.getIasContentEn(), cmcTemplate.getIasContentTc(),
                    cmcTemplate.getIasContentSc(),
                    dataContentEn, dataContentTc, dataContentSc,
                    CmcAppConstants.LANGUAGE_EN, cmcMessageParam);
        }
        String mergedIasToDoItemDetailTc = null;
        if (cmcTemplate.getIasContentTc() != null && cmcTemplate.getIasContentTc().length() > 0) {
            mergedIasToDoItemDetailTc = TemplateUtils.getIasMergedContent(
                    cmcTemplate.getIasContentEn(), cmcTemplate.getIasContentTc(),
                    cmcTemplate.getIasContentSc(),
                    dataContentEn, dataContentTc, dataContentSc,
                    CmcAppConstants.LANGUAGE_TC, cmcMessageParam);
        }
        String mergedIasToDoItemDetailSc = null;
        if (cmcTemplate.getIasContentSc() != null && cmcTemplate.getIasContentSc().length() > 0) {
            mergedIasToDoItemDetailSc = TemplateUtils.getIasMergedContent(
                    cmcTemplate.getIasContentEn(), cmcTemplate.getIasContentTc(),
                    cmcTemplate.getIasContentSc(),
                    dataContentEn, dataContentTc, dataContentSc,
                    CmcAppConstants.LANGUAGE_SC, cmcMessageParam);
        }

        // validate merged message size and add to the web service response
        msgRsp = IasToDoItemValidator.validateMergedToDoItemSize(
                idpId, tranId, recipientId,
                mergedIasToDoItemTitleEn, mergedIasToDoItemTitleTc, mergedIasToDoItemTitleSc,
                mergedIasToDoItemDetailEn, mergedIasToDoItemDetailTc, mergedIasToDoItemDetailSc,
                MsgTypeConstant.TO_DO_ITEM);

        // create the master record in IAS_TO_DO_ITEM if it is the first message and the message size is valid
        if (msgRsp == null) {
            String encKeyStoreId = properties
                    .getProperty(CmcAppPropertyNames.CMC_MESSAGE_ENCRYPT_KEY_STORE_ID_PROPERTY_NAME);
            String iasEsAppSuffixTagName = properties
                    .getProperty(CmcAppPropertyNames.IAS_ES_APP_SUFFIX_TAG_NAME_PROPERTY_NAME);
            IasToDoItemDAO iasToDoItemDAO = new IasToDoItemDAO();

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

            iasToDoItemDAO.createIasToDoItem(portalId, iasToDoItemId,
                    cmcTemplate.getTemplateId(), cmcTemplate.getTemplateVersion(),
                    mergedIasToDoItemTitleEn, mergedIasToDoItemTitleTc, mergedIasToDoItemTitleSc,
                    mergedIasToDoItemDetailEn, mergedIasToDoItemDetailTc, mergedIasToDoItemDetailSc,
                    iasEsAppSuffixEn, iasEsAppSuffixTc, iasEsAppSuffixSc,
                    encKeyStoreId, conn);
        }

        return msgRsp;
    }

    public static List<IasUserToDoItem_> getCorrelatedIasUserToDoItemListByOpenIdOrHKID(HPFW_Connection conn,
            CmcTemplate cmcTemplate, Recipient recipient, IasUserWrapped iasUserWrapped, Properties properties)
            throws Exception {

        IasToDoItemDAO iasToDoItemDAO = new IasToDoItemDAO();

        List<IasUserToDoItem_> iasUserToDoItemList = new ArrayList<IasUserToDoItem_>();
        if (RecipientIDTypeConstant.OPEN_ID.equals(recipient.getRecipientIdType())) {

            iasUserToDoItemList = iasToDoItemDAO.getIasUserToDoItemList(conn, cmcTemplate.getClientId(),
                    iasUserWrapped.getIasUser().getOpenId(), recipient.getCorrelatedTranId());

        } else if (RecipientIDTypeConstant.HKID.equals(recipient.getRecipientIdType())) {

            List<IasUserToDoItem_> tempActiveIasUserToDoItemList = iasToDoItemDAO.getIasUserToDoItemList(conn,
                    cmcTemplate.getClientId(), iasUserWrapped.getIasUser().getHkidHashed(),
                    recipient.getCorrelatedTranId());
            for (IasUserToDoItem_ iasUserToDoItem : tempActiveIasUserToDoItemList) {
                String hkidDecrypted = EncUtils.decrypt(
                        iasUserToDoItem.getHkidEncrypted(),
                        properties.getProperty(CmcAppPropertyNames.HKID_ENCRYPT_PASSPHRASE_ID_PROPERTY_NAME),
                        properties.getProperty(CmcAppPropertyNames.HKID_ENCRYPT_PASSPHRASE_USAGE_PROPERTY_NAME));
                if (hkidDecrypted.equals(iasUserWrapped.getHkid())) {
                    iasUserToDoItemList.add(iasUserToDoItem);
                }
            }

        }

        return iasUserToDoItemList;
    }

    public static MessageResponse createIasUserToDoItemByOpenIdOrHKID(HPFW_Connection conn, CmcTemplate cmcTemplate,
            Recipient recipient, IasUser iasUser, String iasToDoItemId, String operationType) throws Exception {

        logger.debug("createIasUserToDoItemByOpenIdOrHKID - recipient: " + recipient + ", iasUser: " + iasUser
                + ", iasToDoItemId: " + iasToDoItemId + ", operationType: " + operationType);
        logger.debug("createIasUserToDoItemByOpenIdOrHKID - recipient.getRecipientIdType(): "
                + recipient.getRecipientIdType());

        MessageResponse msgRsp = null;
        IasToDoItemDAO iasToDoItemDAO = new IasToDoItemDAO();

        if (RecipientIDTypeConstant.OPEN_ID.equals(recipient.getRecipientIdType())) {

            iasToDoItemDAO.createIasUserToDoItemByOpenId(
                    conn, cmcTemplate.getServiceProviderId(),
                    cmcTemplate.getClientId(), iasUser.getOpenId(), iasToDoItemId,
                    recipient.getTranId(), new Date(recipient.getItemDate().getTime()),
                    operationType, iasUser.getNotiId());

            // Change Opt In from U to Y (For deregister user)
            if (iasUser.getNotiId() != null && IntegrationConstants.OPT_IN_U.equals(iasUser.getOptIn())) {
                IasEsNotiMap_ notiMap = new IasEsNotiMap_(conn, iasUser.getNotiId(),
                        cmcTemplate.getServiceProviderId());
                notiMap.setOptIn(IntegrationConstants.OPT_IN_Y);
                notiMap.update(conn);
            }

        } else if (RecipientIDTypeConstant.HKID.equals(recipient.getRecipientIdType())) {

            iasToDoItemDAO.createIasUserToDoItemByHKID(
                    conn, cmcTemplate.getServiceProviderId(),
                    cmcTemplate.getClientId(), iasUser.getHkidHashed(), iasToDoItemId,
                    recipient.getTranId(), new Date(recipient.getItemDate().getTime()),
                    operationType, iasUser.getNotiId(), iasUser.getHkidEncrypted());

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
                    MsgTypeConstant.TO_DO_ITEM, ResultCodes.RESULT_CD_RECIPIENT_ID_TYPE_NOT_VALID,
                    ResultMessages.RESULT_MSG_RECIPIENT_ID_TYPE_NOT_VALID);
        }

        return msgRsp;
    }

    public static MessageResponse markDeleteIasUserToDoItemByOpenIdOrHKIDAndCorrTranId(HPFW_Connection conn,
            CmcTemplate cmcTemplate, Recipient recipient, IasUser iasUser) throws Exception {

        MessageResponse msgRsp = null;
        IasToDoItemDAO iasToDoItemDAO = new IasToDoItemDAO();

        if (RecipientIDTypeConstant.OPEN_ID.equals(recipient.getRecipientIdType())) {
            iasToDoItemDAO.markDeleteIasUserToDoItemByCorrTranId(conn, cmcTemplate.getClientId(), iasUser.getOpenId(),
                    recipient.getTranId(), recipient.getCorrelatedTranId());
        } else if (RecipientIDTypeConstant.HKID.equals(recipient.getRecipientIdType())) {
            iasToDoItemDAO.markDeleteIasUserToDoItemByCorrTranId(conn, cmcTemplate.getClientId(),
                    iasUser.getHkidHashed(), recipient.getTranId(), recipient.getCorrelatedTranId());
        } else {
            msgRsp = MaintainMessageUtils.getMessageResponse(recipient.getTranId(), recipient.getIdpId(),
                    recipient.getRecipientId(),
                    MsgTypeConstant.TO_DO_ITEM, ResultCodes.RESULT_CD_RECIPIENT_ID_TYPE_NOT_VALID,
                    ResultMessages.RESULT_MSG_RECIPIENT_ID_TYPE_NOT_VALID);
        }

        return msgRsp;
    }

    public static MessageResponse completeIasUserToDoItemByOpenIdOrHKID(HPFW_Connection conn, CmcTemplate cmcTemplate,
            Recipient recipient, IasUser iasUser) throws Exception {

        MessageResponse msgRsp = null;
        IasToDoItemDAO iasToDoItemDAO = new IasToDoItemDAO();

        if (RecipientIDTypeConstant.OPEN_ID.equals(recipient.getRecipientIdType())) {
            iasToDoItemDAO.completeIasUserToDoItem(conn, cmcTemplate.getClientId(), iasUser.getOpenId(),
                    recipient.getTranId(), recipient.getCorrelatedTranId());
        } else if (RecipientIDTypeConstant.HKID.equals(recipient.getRecipientIdType())) {
            iasToDoItemDAO.completeIasUserToDoItem(conn, cmcTemplate.getClientId(), iasUser.getHkidHashed(),
                    recipient.getTranId(), recipient.getCorrelatedTranId());
        } else {
            msgRsp = MaintainMessageUtils.getMessageResponse(recipient.getTranId(), recipient.getIdpId(),
                    recipient.getRecipientId(),
                    MsgTypeConstant.TO_DO_ITEM, ResultCodes.RESULT_CD_RECIPIENT_ID_TYPE_NOT_VALID,
                    ResultMessages.RESULT_MSG_RECIPIENT_ID_TYPE_NOT_VALID);
        }

        return msgRsp;
    }

}
