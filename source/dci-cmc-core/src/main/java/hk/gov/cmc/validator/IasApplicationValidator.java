package hk.gov.cmc.validator;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import hk.gov.cmc.common.CmcAppPropertyNames;
import hk.gov.cmc.common.ResultCodes;
import hk.gov.cmc.common.ResultMessages;
import hk.gov.ogcio.egis.rm.common.utils.ServiceLocator;
import hk.gov.ogcio.mars_cmc.cmc.appserver.AppPropertyName;
import hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageResponse;
import hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.AppStatus_ST;
import hk.gov.ogcio.mars_cmc.cmc.datatype.message.Constants;
import hk.gov.ogcio.mars_cmc.cmc.service.constant.IasApplicationConstant;
import hk.gov.ogcio.mars_cmc.cmc.service.constant.MsgTypeConstant;
import hk.gov.ogcio.mars_cmc.cmc.service.databean.IasUserApplication_;
import hk.gov.ogcio.mars_cmc.cmc.utils.ResponseUtils;

public class IasApplicationValidator {

    private static Log logger = LogFactory.getLog(IasApplicationValidator.class);

    public IasApplicationValidator() {
    }

    // CMC-2025-016: Restrict iAM Smart To Do Item and Application subject max length (limited EN/TC/SC to 70/30/30 characters) - BEGIN
    public static MessageResponse validateMergedApplicationSize(
            String idpId, String tranId, String recipientId,
            String mergedSubjectEn, String mergedSubjectTc, String mergedSubjectSc,
            String mergedContentEn, String mergedContentTc, String mergedContentSc,
            String msgType) throws Exception {

        logger.debug("validateMergedApplicationSize - idpId: " + idpId + ", tranId: " + tranId + ", recipientId: " + recipientId
                + ", mergedSubjectEn: " + mergedSubjectEn + ", mergedSubjectTc: "+ mergedSubjectTc + ", mergedSubjectSc: " + mergedSubjectSc
                + ", mergedContentEn: " + mergedContentEn + ", mergedContentTc: " + mergedContentTc + ", mergedContentSc: " + mergedContentSc 
                + ", msgType: " + msgType);

        Properties properties = ServiceLocator.getInstance(null).getProperties();

        int subjectESizeLimit = Integer
                .parseInt(properties.getProperty(CmcAppPropertyNames.MAINT_MSG_IAS_APPLICATION_SUBJ_ENG_SIZE_LIMIT_PROPERTY_NAME));
        int subjectCSizeLimit = Integer
                .parseInt(properties.getProperty(CmcAppPropertyNames.MAINT_MSG_IAS_APPLICATION_SUBJ_CHI_SIZE_LIMIT_PROPERTY_NAME));

        int contentESizeLimit = Integer.parseInt(
                properties.getProperty(CmcAppPropertyNames.MAINT_MSG_IAS_APPLICATION_CONTENT_ENG_SIZE_LIMIT_PROPERTY_NAME));
        int contentCSizeLimit = Integer.parseInt(
                properties.getProperty(CmcAppPropertyNames.MAINT_MSG_IAS_APPLICATION_CONTENT_CHI_SIZE_LIMIT_PROPERTY_NAME));

        if ((mergedSubjectEn == null || mergedSubjectEn.length() == 0) ||
                (mergedContentEn == null || mergedContentEn.length() == 0)) {
            return ResponseUtils.getMessageResponse(tranId, idpId, recipientId, msgType, 
                ResultCodes.RESULT_CD_GENERAL_ERROR, ResultMessages.RESULT_MSG_GENERAL_ERROR);
        }

        if (getMsgSize(mergedSubjectEn) > subjectESizeLimit) {
            return ResponseUtils.getMessageResponse(tranId, idpId, recipientId, msgType,
                    ResultCodes.RESULT_CD_MERGED_IAS_APPLICATION_TITLE_EN_LENGTH_OVER_LIMIT,
                    ResultMessages.RESULT_MSG_MERGED_IAS_APPLICATION_TITLE_EN_LENGTH_OVER_LIMIT);
        }

        if (getMsgSize(mergedSubjectTc) > subjectCSizeLimit) {
            return ResponseUtils.getMessageResponse(tranId, idpId, recipientId, msgType,
                    ResultCodes.RESULT_CD_MERGED_IAS_APPLICATION_TITLE_TC_LENGTH_OVER_LIMIT,
                    ResultMessages.RESULT_MSG_MERGED_IAS_APPLICATION_TITLE_TC_LENGTH_OVER_LIMIT);
        }

        if (getMsgSize(mergedSubjectSc) > subjectCSizeLimit) {
            return ResponseUtils.getMessageResponse(tranId, idpId, recipientId, msgType,
                    ResultCodes.RESULT_CD_MERGED_IAS_APPLICATION_TITLE_SC_LENGTH_OVER_LIMIT,
                    ResultMessages.RESULT_MSG_MERGED_IAS_APPLICATION_TITLE_SC_LENGTH_OVER_LIMIT);
        }

        if (getMsgSize(mergedContentEn) > contentESizeLimit) {
            return ResponseUtils.getMessageResponse(tranId, idpId, recipientId, msgType,
                    ResultCodes.RESULT_CD_MERGED_IAS_APPLICATION_CONTENT_EN_LENGTH_OVER_LIMIT,
                    ResultMessages.RESULT_MSG_MERGED_IAS_APPLICATION_CONTENT_EN_LENGTH_OVER_LIMIT);
        }

        if (getMsgSize(mergedContentTc) > contentCSizeLimit) {
            return ResponseUtils.getMessageResponse(tranId, idpId, recipientId, msgType,
                    ResultCodes.RESULT_CD_MERGED_IAS_APPLICATION_CONTENT_TC_LENGTH_OVER_LIMIT,
                    ResultMessages.RESULT_MSG_MERGED_IAS_APPLICATION_CONTENT_TC_LENGTH_OVER_LIMIT);
        }

        if (getMsgSize(mergedContentSc) > contentCSizeLimit) {
            return ResponseUtils.getMessageResponse(tranId, idpId, recipientId, msgType,
                    ResultCodes.RESULT_CD_MERGED_IAS_APPLICATION_CONTENT_SC_LENGTH_OVER_LIMIT,
                    ResultMessages.RESULT_MSG_MERGED_IAS_APPLICATION_CONTENT_SC_LENGTH_OVER_LIMIT);
        }

        return null;
    }
    // CMC-2025-016: Restrict iAM Smart To Do Item and Application subject max length (limited EN/TC/SC to 70/30/30 characters) - END

    // CMC-2025-022: Use TRAN_ID to correlate iAM Smart Application Status instead of APP_REF_NUM - BEGIN
    // public static MessageResponse validateApplicationIsExistedOrDeleted(String clientId, String idpId, String msgRequestRecipientId, String tranId, String appRefNum, 
    //     Map<String, String> iasApplicationHandledCache, List<IasUserApplication_> existingIasUserApplicationList) throws Exception {
    //     logger.debug("validateApplicationIsExistedOrDeleted - clientId: " + clientId + ", idpId: " + idpId 
    //         + ", msgRequestRecipientId: " + msgRequestRecipientId + ", tranId: " + tranId + ", appRefNum: " + appRefNum 
    //         + ", iasApplicationHandledCache.size: " + iasApplicationHandledCache.size());

    //     String cachedAppRefNumAction = null;
    //     if (iasApplicationHandledCache != null) {
    //         cachedAppRefNumAction = iasApplicationHandledCache.get(appRefNum);
    //     }

    //     logger.debug("validateApplicationIsExistedOrDeleted - cachedAppRefNumAction: " + cachedAppRefNumAction);
    //     if (cachedAppRefNumAction != null && IasApplicationConstant.HISTORY_DELETE.equals(cachedAppRefNumAction)) { 
    //         return ResponseUtils.getMessageResponse(tranId, idpId, msgRequestRecipientId, MsgTypeConstant.APPLICATION, 
    //                 ResultCodes.RESULT_CD_APPLICATION_ALREADY_MARK_DELETE, ResultMessages.RESULT_MSG_APPLICATION_ALREADY_MARK_DELETE);
    //     }

    //     if (existingIasUserApplicationList != null && existingIasUserApplicationList.size() > 0) {
    //         if (Constants.DELETE_IND_PENDING.equals(existingIasUserApplicationList.get(0).getDeleteInd()) ||
    //             Constants.DELETE_IND_DELETED.equals(existingIasUserApplicationList.get(0).getDeleteInd())) {
    //                 return ResponseUtils.getMessageResponse(tranId, idpId, msgRequestRecipientId, MsgTypeConstant.APPLICATION, 
    //                     ResultCodes.RESULT_CD_APPLICATION_ALREADY_MARK_DELETE, ResultMessages.RESULT_MSG_APPLICATION_ALREADY_MARK_DELETE);
    //         } else {
    //             return null;
    //         }
    //     } else {
    //         return ResponseUtils.getMessageResponse(tranId, idpId, msgRequestRecipientId, MsgTypeConstant.APPLICATION, 
    //             ResultCodes.RESULT_CD_APP_REF_NUM_NOT_FOUND, ResultMessages.RESULT_MSG_APP_REF_NUM_NOT_FOUND);
    //     }
    // }
    public static MessageResponse validateApplicationIsExistedOrDeleted(
        String clientId, String idpId, String msgRequestRecipientId, 
        String tranId, String corrTranId, 
        Map<String, String> iasApplicationHandledCache, List<IasUserApplication_> existingIasUserApplicationList
    ) throws Exception {
        logger.debug("validateApplicationIsExistedOrDeleted - clientId: " + clientId + ", idpId: " + idpId 
            + ", msgRequestRecipientId: " + msgRequestRecipientId + ", tranId: " + tranId + ", corrTranId: " + corrTranId 
            + ", iasApplicationHandledCache.size: " + iasApplicationHandledCache.size());

        String cachedTranIdAction = null;
        if (iasApplicationHandledCache != null) {
            cachedTranIdAction = iasApplicationHandledCache.get(corrTranId);
        }

        logger.debug("validateApplicationIsExistedOrDeleted - cachedTranIdAction: " + cachedTranIdAction);
        if (cachedTranIdAction != null && IasApplicationConstant.HISTORY_DELETE.equals(cachedTranIdAction)) { 
            return ResponseUtils.getMessageResponse(tranId, idpId, msgRequestRecipientId, MsgTypeConstant.APPLICATION, 
                    ResultCodes.RESULT_CD_APPLICATION_ALREADY_MARK_DELETE, ResultMessages.RESULT_MSG_APPLICATION_ALREADY_MARK_DELETE);
        }

        if (existingIasUserApplicationList != null && existingIasUserApplicationList.size() > 0) {
            if (Constants.DELETE_IND_PENDING.equals(existingIasUserApplicationList.get(0).getDeleteInd()) ||
                Constants.DELETE_IND_DELETED.equals(existingIasUserApplicationList.get(0).getDeleteInd())) {
                    return ResponseUtils.getMessageResponse(tranId, idpId, msgRequestRecipientId, MsgTypeConstant.APPLICATION, 
                        ResultCodes.RESULT_CD_APPLICATION_ALREADY_MARK_DELETE, ResultMessages.RESULT_MSG_APPLICATION_ALREADY_MARK_DELETE);
            } else {
                return null;
            }
        } else {
            return ResponseUtils.getMessageResponse(tranId, idpId, msgRequestRecipientId, MsgTypeConstant.APPLICATION, 
                ResultCodes.RESULT_CD_CORRELATED_TRAN_ID_NOT_FOUND, ResultMessages.RESULT_MSG_CORRELATED_TRAN_ID_NOT_FOUND);
        }
    }
    // CMC-2025-022: Use TRAN_ID to correlate iAM Smart Application Status instead of APP_REF_NUM - END

    public static MessageResponse validateAppStatusUpdateDate(String clientId, String idpId, String msgRequestRecipientId, 
        String tranId, Date appStatusUpdateDate, List<IasUserApplication_> existingIasUserApplicationList) throws Exception {
        logger.debug("validateAppStatusUpdateDate - clientId: " + clientId + ", idpId: " + idpId 
            + ", msgRequestRecipientId: " + msgRequestRecipientId + ", tranId: " + tranId + ", appStatusUpdateDate: " + appStatusUpdateDate);

        if (existingIasUserApplicationList != null && existingIasUserApplicationList.size() > 0) {
            IasUserApplication_ iasUserApplication_ = existingIasUserApplicationList.get(0);
            if (iasUserApplication_.getAppStatusUpdateDate() != null) {
                if (appStatusUpdateDate != null) {
                    if (appStatusUpdateDate.after(iasUserApplication_.getAppStatusUpdateDate())) {
                        return null;
                    } else {
                        return ResponseUtils.getMessageResponse(tranId, idpId, msgRequestRecipientId, MsgTypeConstant.APPLICATION, 
                            ResultCodes.RESULT_CD_APP_STATUS_UPDATE_DATE_IS_NOT_VALID, ResultMessages.RESULT_MSG_APP_STATUS_UPDATE_DATE_IS_NOT_VALID);
                    }
                } else {
                    return ResponseUtils.getMessageResponse(tranId, idpId, msgRequestRecipientId, MsgTypeConstant.APPLICATION, 
                        ResultCodes.RESULT_CD_APP_STATUS_UPDATE_DATE_IS_NOT_VALID, ResultMessages.RESULT_MSG_APP_STATUS_UPDATE_DATE_IS_NOT_VALID);
                }
            }

        }

        return null;
    }

    public static MessageResponse validateAppStatus(String clientId, String idpId, String msgRequestRecipientId, 
        String tranId, String appStatus) throws Exception {
        logger.debug("validateAppStatus - clientId: " + clientId + ", idpId: " + idpId 
            + ", msgRequestRecipientId: " + msgRequestRecipientId + ", tranId: " + tranId + ", appStatus: " + appStatus);

        try {
            AppStatus_ST.fromValue(appStatus);
        } catch (IllegalArgumentException e) {
            return ResponseUtils.getMessageResponse(tranId, idpId, msgRequestRecipientId, MsgTypeConstant.APPLICATION, 
                ResultCodes.RESULT_CD_APPLICATION_STATUS_NOT_VALID, ResultMessages.RESULT_MSG_APPLICATION_STATUS_NOT_VALID);
        }

        return null;
    }

    // CMC-2025-016: Restrict iAM Smart To Do Item and Application subject max length (limited EN/TC/SC to 70/30/30 characters) - BEGIN
    private static int getMsgSize(String inStr) throws Exception {
        int result = 0;
        if (inStr != null) {
            result = inStr.getBytes("UTF-8").length;
        }
        return result;
    }
    // CMC-2025-016: Restrict iAM Smart To Do Item and Application subject max length (limited EN/TC/SC to 70/30/30 characters) - END

}
