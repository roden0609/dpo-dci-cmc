package hk.gov.ogcio.mars_cmc.cmc.service.validator;

import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import hk.gov.ogcio.egis.rm.common.utils.ServiceLocator;
import hk.gov.ogcio.mars_cmc.cmc.appserver.AppPropertyName;
import hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageResponse;
import hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.Action_ST;
import hk.gov.ogcio.mars_cmc.cmc.datatype.message.Constants;
import hk.gov.ogcio.mars_cmc.cmc.service.constant.MsgTypeConstant;
import hk.gov.ogcio.mars_cmc.cmc.service.constant.RecipientIDTypeConstant;
import hk.gov.ogcio.mars_cmc.cmc.utils.ResponseUtils;

public class IasMsgValidator {

    private static Log logger = LogFactory.getLog(IasMsgValidator.class);

    public IasMsgValidator() {
    }

    public static MessageResponse validateMergedMsgSize(
            String mergedSubjectEn, String mergedSubjectTc, String mergedSubjectSc,
            String mergedContentEn, String mergedContentTc, String mergedContentSc,
            String msgType) throws Exception {

        logger.debug("validateMergedMsgSize - mergedSubjectEn: " + mergedSubjectEn + ", mergedSubjectTc: "
                + mergedSubjectTc + ", mergedSubjectSc: " + mergedSubjectSc
                + ", mergedContentEn: " + mergedContentEn + ", mergedContentTc: " + mergedContentTc
                + ", mergedContentSc: " + mergedContentSc + ", msgType: " + msgType);

        Properties properties = ServiceLocator.getInstance(null).getProperties();

        int subjectESizeLimit = Integer
                .parseInt(properties.getProperty(CmcAppPropertyNames.MAINT_MSG_IAS_MSG_SUBJ_ENG_SIZE_LIMIT_PROPERTY_NAME));
        int subjectCSizeLimit = Integer
                .parseInt(properties.getProperty(CmcAppPropertyNames.MAINT_MSG_IAS_MSG_SUBJ_CHI_SIZE_LIMIT_PROPERTY_NAME));

        int contentESizeLimit = Integer.parseInt(
                properties.getProperty(CmcAppPropertyNames.MAINT_MSG_IAS_MSG_CONTENT_ENG_SIZE_LIMIT_PROPERTY_NAME));
        int contentCSizeLimit = Integer.parseInt(
                properties.getProperty(CmcAppPropertyNames.MAINT_MSG_IAS_MSG_CONTENT_CHI_SIZE_LIMIT_PROPERTY_NAME));

        if ((mergedSubjectEn == null || mergedSubjectEn.length() == 0) ||
                (mergedContentEn == null || mergedContentEn.length() == 0)) {
            return ResponseUtils.getMessageResponse(null, null, null, msgType, 
                ResultCodes.RESULT_CD_GENERAL_ERROR, ResultMessages.RESULT_MSG_GENERAL_ERROR);
        }

        if (getMsgSize(mergedSubjectEn) > subjectESizeLimit) {
            return ResponseUtils.getMessageResponse(null, null, null, msgType,
                    ResultCodes.RESULT_CD_MERGED_IAS_MSG_SUBJECT_EN_LENGTH_OVER_LIMIT,
                    ResultMessages.RESULT_MSG_MERGED_IAS_MSG_SUBJECT_EN_LENGTH_OVER_LIMIT);
        }

        if (getMsgSize(mergedSubjectTc) > subjectCSizeLimit) {
            return ResponseUtils.getMessageResponse(null, null, null, msgType,
                    ResultCodes.RESULT_CD_MERGED_IAS_MSG_SUBJECT_TC_LENGTH_OVER_LIMIT,
                    ResultMessages.RESULT_MSG_MERGED_IAS_MSG_SUBJECT_TC_LENGTH_OVER_LIMIT);
        }

        if (getMsgSize(mergedSubjectSc) > subjectCSizeLimit) {
            return ResponseUtils.getMessageResponse(null, null, null, msgType,
                    ResultCodes.RESULT_CD_MERGED_IAS_MSG_SUBJECT_SC_LENGTH_OVER_LIMIT,
                    ResultMessages.RESULT_MSG_MERGED_IAS_MSG_SUBJECT_SC_LENGTH_OVER_LIMIT);
        }

        if (getMsgSize(mergedContentEn) > contentESizeLimit) {
            return ResponseUtils.getMessageResponse(null, null, null, msgType,
                    ResultCodes.RESULT_CD_MERGED_IAS_MSG_CONTENT_EN_LENGTH_OVER_LIMIT,
                    ResultMessages.RESULT_MSG_MERGED_IAS_MSG_CONTENT_EN_LENGTH_OVER_LIMIT);
        }

        if (getMsgSize(mergedContentTc) > contentCSizeLimit) {
            return ResponseUtils.getMessageResponse(null, null, null, msgType,
                    ResultCodes.RESULT_CD_MERGED_IAS_MSG_CONTENT_TC_LENGTH_OVER_LIMIT,
                    ResultMessages.RESULT_MSG_MERGED_IAS_MSG_CONTENT_TC_LENGTH_OVER_LIMIT);
        }

        if (getMsgSize(mergedContentSc) > contentCSizeLimit) {
            return ResponseUtils.getMessageResponse(null, null, null, msgType,
                    ResultCodes.RESULT_CD_MERGED_IAS_MSG_CONTENT_SC_LENGTH_OVER_LIMIT,
                    ResultMessages.RESULT_MSG_MERGED_IAS_MSG_CONTENT_SC_LENGTH_OVER_LIMIT);
        }

        return null;
    }

    public static MessageResponse validateOptInStatus(String clientId, String idpId, String msgRequestRecipientId, String tranId,
        boolean iasOptCheck, String optIn) throws Exception {

        logger.debug("validateOptInStatus - tranId: " + tranId + ", idpId: " + idpId  + ", msgRequestRecipientId: " + 
                msgRequestRecipientId + ", iasOptCheck: " + iasOptCheck + ", optIn: " + optIn);

        if ((iasOptCheck) && ((optIn != null && (Constants.OPT_IN_N.equals(optIn) || Constants.OPT_IN_U.equals(optIn))))) {
            return ResponseUtils.getMessageResponse(tranId, idpId, msgRequestRecipientId, MsgTypeConstant.MESSAGE, 
                ResultCodes.RESULT_CD_USER_REJECT_MSG, ResultMessages.RESULT_MSG_USER_REJECT_MSG);
        }

        return null;
    }

    public static MessageResponse validateAction(String clientId, String idpId, String msgRequestRecipientId, String tranId, int action) throws Exception {

        logger.debug("validateAction - tranId: " + tranId + ", idpId: " + idpId + ", msgRequestRecipientId: " + msgRequestRecipientId + ", action: " + action);

        if (action == Action_ST.UPDATE_TYPE) {
            return ResponseUtils.getMessageResponse(tranId, idpId, msgRequestRecipientId, MsgTypeConstant.MESSAGE, 
                ResultCodes.RESULT_CD_EMSG_ACTION_IS_NOT_VALID, ResultMessages.RESULT_MSG_EMSG_ACTION_IS_NOT_VALID);
        }

        return null;
    }

    public static MessageResponse validateRecipientIdTypeOnlyEmptyOrOpenId(String clientId, String idpId, String msgRequestRecipientId, String tranId, 
        String recipientIdType) throws Exception {

        logger.debug("validateRecipientIdTypeOnlyEmptyOrOpenId - recipientIdType: " + recipientIdType);

        if (!StringUtils.isEmpty(recipientIdType) && !RecipientIDTypeConstant.OPEN_ID.equals(recipientIdType)) {
            return ResponseUtils.getMessageResponse(tranId, idpId, msgRequestRecipientId, MsgTypeConstant.MESSAGE, 
                ResultCodes.RESULT_CD_RECIPIENT_ID_TYPE_NOT_VALID, ResultMessages.RESULT_MSG_RECIPIENT_ID_TYPE_NOT_VALID);
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
