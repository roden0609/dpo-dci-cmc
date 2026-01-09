package hk.gov.cmc.validator;

import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import hk.gov.cmc.common.CmcAppPropertyNames;
import hk.gov.cmc.common.IntegrationConstants;
import hk.gov.cmc.common.MsgTypeConstant;
import hk.gov.cmc.common.RecipientIDTypeConstant;
import hk.gov.cmc.common.ResultCodes;
import hk.gov.cmc.common.ResultMessages;
import hk.gov.cmc.config.CmcEnvProperties;
import hk.gov.cmc.model.maintainmessage.action.Action;
import hk.gov.cmc.model.maintainmessage.response.MessageResponse;
import hk.gov.cmc.utils.maintainmessage.MaintainMessageUtils;

public class IasMessageValidator {

    private static Log logger = LogFactory.getLog(IasMessageValidator.class);
    private final static CmcEnvProperties cmcEnvProperties = new CmcEnvProperties();

    public IasMessageValidator() {
    }

    public static MessageResponse validateMergedMsgSize(
            String idpId, String tranId, String recipientId,
            String mergedSubjectEn, String mergedSubjectTc, String mergedSubjectSc,
            String mergedContentEn, String mergedContentTc, String mergedContentSc,
            String msgType) throws Exception {

        logger.debug("validateMergedMsgSize - idpId: " + idpId + ", tranId: " + tranId + ", recipientId: " + recipientId
                + "mergedSubjectEn: " + mergedSubjectEn + ", mergedSubjectTc: " + mergedSubjectTc
                + ", mergedSubjectSc: " + mergedSubjectSc
                + ", mergedContentEn: " + mergedContentEn + ", mergedContentTc: " + mergedContentTc
                + ", mergedContentSc: " + mergedContentSc
                + ", msgType: " + msgType);

        Properties properties = cmcEnvProperties.getProperties();

        int subjectESizeLimit = Integer
                .parseInt(properties
                        .getProperty(CmcAppPropertyNames.MAINT_MSG_IAS_MSG_SUBJ_ENG_SIZE_LIMIT_PROPERTY_NAME));
        int subjectCSizeLimit = Integer
                .parseInt(properties
                        .getProperty(CmcAppPropertyNames.MAINT_MSG_IAS_MSG_SUBJ_CHI_SIZE_LIMIT_PROPERTY_NAME));

        int contentESizeLimit = Integer.parseInt(
                properties.getProperty(CmcAppPropertyNames.MAINT_MSG_IAS_MSG_CONTENT_ENG_SIZE_LIMIT_PROPERTY_NAME));
        int contentCSizeLimit = Integer.parseInt(
                properties.getProperty(CmcAppPropertyNames.MAINT_MSG_IAS_MSG_CONTENT_CHI_SIZE_LIMIT_PROPERTY_NAME));

        if ((mergedSubjectEn == null || mergedSubjectEn.length() == 0) ||
                (mergedContentEn == null || mergedContentEn.length() == 0)) {
            return MaintainMessageUtils.getMessageResponse(tranId, idpId, recipientId, msgType,
                    ResultCodes.RESULT_CD_GENERAL_ERROR, ResultMessages.RESULT_MSG_GENERAL_ERROR);
        }

        if (getMsgSize(mergedSubjectEn) > subjectESizeLimit) {
            return MaintainMessageUtils.getMessageResponse(tranId, idpId, recipientId, msgType,
                    ResultCodes.RESULT_CD_MERGED_IAS_MSG_SUBJECT_EN_LENGTH_OVER_LIMIT,
                    ResultMessages.RESULT_MSG_MERGED_IAS_MSG_SUBJECT_EN_LENGTH_OVER_LIMIT);
        }

        if (getMsgSize(mergedSubjectTc) > subjectCSizeLimit) {
            return MaintainMessageUtils.getMessageResponse(tranId, idpId, recipientId, msgType,
                    ResultCodes.RESULT_CD_MERGED_IAS_MSG_SUBJECT_TC_LENGTH_OVER_LIMIT,
                    ResultMessages.RESULT_MSG_MERGED_IAS_MSG_SUBJECT_TC_LENGTH_OVER_LIMIT);
        }

        if (getMsgSize(mergedSubjectSc) > subjectCSizeLimit) {
            return MaintainMessageUtils.getMessageResponse(tranId, idpId, recipientId, msgType,
                    ResultCodes.RESULT_CD_MERGED_IAS_MSG_SUBJECT_SC_LENGTH_OVER_LIMIT,
                    ResultMessages.RESULT_MSG_MERGED_IAS_MSG_SUBJECT_SC_LENGTH_OVER_LIMIT);
        }

        if (getMsgSize(mergedContentEn) > contentESizeLimit) {
            return MaintainMessageUtils.getMessageResponse(tranId, idpId, recipientId, msgType,
                    ResultCodes.RESULT_CD_MERGED_IAS_MSG_CONTENT_EN_LENGTH_OVER_LIMIT,
                    ResultMessages.RESULT_MSG_MERGED_IAS_MSG_CONTENT_EN_LENGTH_OVER_LIMIT);
        }

        if (getMsgSize(mergedContentTc) > contentCSizeLimit) {
            return MaintainMessageUtils.getMessageResponse(tranId, idpId, recipientId, msgType,
                    ResultCodes.RESULT_CD_MERGED_IAS_MSG_CONTENT_TC_LENGTH_OVER_LIMIT,
                    ResultMessages.RESULT_MSG_MERGED_IAS_MSG_CONTENT_TC_LENGTH_OVER_LIMIT);
        }

        if (getMsgSize(mergedContentSc) > contentCSizeLimit) {
            return MaintainMessageUtils.getMessageResponse(tranId, idpId, recipientId, msgType,
                    ResultCodes.RESULT_CD_MERGED_IAS_MSG_CONTENT_SC_LENGTH_OVER_LIMIT,
                    ResultMessages.RESULT_MSG_MERGED_IAS_MSG_CONTENT_SC_LENGTH_OVER_LIMIT);
        }

        return null;
    }

    public static MessageResponse validateOptInStatus(String clientId, String idpId, String msgRequestRecipientId,
            String tranId,
            boolean iasOptCheck, String optIn) throws Exception {

        logger.debug("validateOptInStatus - tranId: " + tranId + ", idpId: " + idpId + ", msgRequestRecipientId: " +
                msgRequestRecipientId + ", iasOptCheck: " + iasOptCheck + ", optIn: " + optIn);

        if ((iasOptCheck) && ((optIn != null
                && (IntegrationConstants.OPT_IN_N.equals(optIn) || IntegrationConstants.OPT_IN_U.equals(optIn))))) {
            return MaintainMessageUtils.getMessageResponse(tranId, idpId, msgRequestRecipientId,
                    MsgTypeConstant.MESSAGE,
                    ResultCodes.RESULT_CD_USER_REJECT_MSG, ResultMessages.RESULT_MSG_USER_REJECT_MSG);
        }

        return null;
    }

    public static MessageResponse validateAction(String clientId, String idpId, String msgRequestRecipientId,
            String tranId, Action action) throws Exception {

        logger.debug("validateAction - tranId: " + tranId + ", idpId: " + idpId + ", msgRequestRecipientId: "
                + msgRequestRecipientId + ", action: " + action);

        if (action != Action.NEW) {
            return MaintainMessageUtils.getMessageResponse(tranId, idpId, msgRequestRecipientId,
                    MsgTypeConstant.MESSAGE,
                    ResultCodes.RESULT_CD_EMSG_ACTION_IS_NOT_VALID, ResultMessages.RESULT_MSG_EMSG_ACTION_IS_NOT_VALID);
        }

        return null;
    }

    public static MessageResponse validateRecipientIdTypeOnlyEmptyOrOpenId(String clientId, String idpId,
            String msgRequestRecipientId, String tranId,
            String recipientIdType) throws Exception {

        logger.debug("validateRecipientIdTypeOnlyEmptyOrOpenId - recipientIdType: " + recipientIdType);

        if (!StringUtils.isEmpty(recipientIdType) && !RecipientIDTypeConstant.OPEN_ID.equals(recipientIdType)) {
            return MaintainMessageUtils.getMessageResponse(tranId, idpId, msgRequestRecipientId,
                    MsgTypeConstant.MESSAGE,
                    ResultCodes.RESULT_CD_RECIPIENT_ID_TYPE_NOT_VALID,
                    ResultMessages.RESULT_MSG_RECIPIENT_ID_TYPE_NOT_VALID);
        }

        return null;
    }

    private static int getMsgSize(String inStr) throws Exception {
        int result = 0;
        if (inStr != null) {
            result = inStr.getBytes("UTF-8").length;
        }
        return result;
    }
}
