package hk.gov.ogcio.mars_cmc.cmc.service.validator;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import hk.gov.ogcio.egis.rm.common.utils.ServiceLocator;
import hk.gov.ogcio.mars_cmc.cmc.appserver.AppPropertyName;
import hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageResponse;
import hk.gov.ogcio.mars_cmc.cmc.datatype.message.Constants;
import hk.gov.ogcio.mars_cmc.cmc.service.constant.IasToDoItemConstant;
import hk.gov.ogcio.mars_cmc.cmc.service.constant.MsgTypeConstant;
import hk.gov.ogcio.mars_cmc.cmc.service.databean.IasUserToDoItem_;
import hk.gov.ogcio.mars_cmc.cmc.utils.ResponseUtils;

public class IasToDoItemValidator {

    private static Log logger = LogFactory.getLog(IasToDoItemValidator.class);

    public IasToDoItemValidator() {
    }

    // CMC-2025-016: Restrict iAM Smart To Do Item and Application subject max length (limited EN/TC/SC to 70/30/30 characters) - BEGIN
    public static MessageResponse validateMergedToDoItemSize(
            String idpId, String tranId, String recipientId,
            String mergedSubjectEn, String mergedSubjectTc, String mergedSubjectSc,
            String mergedContentEn, String mergedContentTc, String mergedContentSc,
            String msgType) throws Exception {

        logger.debug("validateMergedToDoItemSize - idpId: " + idpId + ", tranId: " + tranId + ", recipientId: " + recipientId
                + ", mergedSubjectEn: " + mergedSubjectEn + ", mergedSubjectTc: " + mergedSubjectTc + ", mergedSubjectSc: " + mergedSubjectSc
                + ", mergedContentEn: " + mergedContentEn + ", mergedContentTc: " + mergedContentTc + ", mergedContentSc: " + mergedContentSc 
                + ", msgType: " + msgType);

        Properties properties = ServiceLocator.getInstance(null).getProperties();

        int subjectESizeLimit = Integer
                .parseInt(properties.getProperty(CmcAppPropertyNames.MAINT_MSG_IAS_TO_DO_ITEM_SUBJ_ENG_SIZE_LIMIT_PROPERTY_NAME));
        int subjectCSizeLimit = Integer
                .parseInt(properties.getProperty(CmcAppPropertyNames.MAINT_MSG_IAS_TO_DO_ITEM_SUBJ_CHI_SIZE_LIMIT_PROPERTY_NAME));

        int contentESizeLimit = Integer.parseInt(
                properties.getProperty(CmcAppPropertyNames.MAINT_MSG_IAS_TO_DO_ITEM_CONTENT_ENG_SIZE_LIMIT_PROPERTY_NAME));
        int contentCSizeLimit = Integer.parseInt(
                properties.getProperty(CmcAppPropertyNames.MAINT_MSG_IAS_TO_DO_ITEM_CONTENT_CHI_SIZE_LIMIT_PROPERTY_NAME));

        if ((mergedSubjectEn == null || mergedSubjectEn.length() == 0) ||
                (mergedContentEn == null || mergedContentEn.length() == 0)) {
            return ResponseUtils.getMessageResponse(tranId, idpId, recipientId, msgType, 
                ResultCodes.RESULT_CD_GENERAL_ERROR, ResultMessages.RESULT_MSG_GENERAL_ERROR);
        }

        if (getMsgSize(mergedSubjectEn) > subjectESizeLimit) {
            return ResponseUtils.getMessageResponse(tranId, idpId, recipientId, msgType,
                    ResultCodes.RESULT_CD_MERGED_IAS_TO_DO_ITEM_SUBJECT_EN_LENGTH_OVER_LIMIT,
                    ResultMessages.RESULT_MSG_MERGED_IAS_TO_DO_ITEM_SUBJECT_EN_LENGTH_OVER_LIMIT);
        }

        if (getMsgSize(mergedSubjectTc) > subjectCSizeLimit) {
            return ResponseUtils.getMessageResponse(tranId, idpId, recipientId, msgType,
                    ResultCodes.RESULT_CD_MERGED_IAS_TO_DO_ITEM_SUBJECT_TC_LENGTH_OVER_LIMIT,
                    ResultMessages.RESULT_MSG_MERGED_IAS_TO_DO_ITEM_SUBJECT_TC_LENGTH_OVER_LIMIT);
        }

        if (getMsgSize(mergedSubjectSc) > subjectCSizeLimit) {
            return ResponseUtils.getMessageResponse(tranId, idpId, recipientId, msgType,
                    ResultCodes.RESULT_CD_MERGED_IAS_TO_DO_ITEM_SUBJECT_SC_LENGTH_OVER_LIMIT,
                    ResultMessages.RESULT_MSG_MERGED_IAS_TO_DO_ITEM_SUBJECT_SC_LENGTH_OVER_LIMIT);
        }

        if (getMsgSize(mergedContentEn) > contentESizeLimit) {
            return ResponseUtils.getMessageResponse(tranId, idpId, recipientId, msgType,
                    ResultCodes.RESULT_CD_MERGED_IAS_TO_DO_ITEM_CONTENT_EN_LENGTH_OVER_LIMIT,
                    ResultMessages.RESULT_MSG_MERGED_IAS_TO_DO_ITEM_CONTENT_EN_LENGTH_OVER_LIMIT);
        }

        if (getMsgSize(mergedContentTc) > contentCSizeLimit) {
            return ResponseUtils.getMessageResponse(tranId, idpId, recipientId, msgType,
                    ResultCodes.RESULT_CD_MERGED_IAS_TO_DO_ITEM_CONTENT_TC_LENGTH_OVER_LIMIT,
                    ResultMessages.RESULT_MSG_MERGED_IAS_TO_DO_ITEM_CONTENT_TC_LENGTH_OVER_LIMIT);
        }

        if (getMsgSize(mergedContentSc) > contentCSizeLimit) {
            return ResponseUtils.getMessageResponse(tranId, idpId, recipientId, msgType,
                    ResultCodes.RESULT_CD_MERGED_IAS_TO_DO_ITEM_CONTENT_SC_LENGTH_OVER_LIMIT,
                    ResultMessages.RESULT_MSG_MERGED_IAS_TO_DO_ITEM_CONTENT_SC_LENGTH_OVER_LIMIT);
        }

        return null;
    }
    // CMC-2025-016: Restrict iAM Smart To Do Item and Application subject max length (limited EN/TC/SC to 70/30/30 characters) - END

    public static MessageResponse validateToDoItemIsExistedOrDeletedOrCompleted(
        String clientId, String idpId, String msgRequestRecipientId, 
        String tranId, String corrTranId, 
        Map<String, String> iasToDoItemHandledCache, List<IasUserToDoItem_> existingIasUserToDoItemList
    ) throws Exception {
        logger.debug(
            "validateToDoItemIsExistedOrDeletedOrCompleted - clientId: " + clientId + ", idpId: " + idpId 
            + ", msgRequestRecipientId: " + msgRequestRecipientId + ", tranId: " + tranId + ", corrTranId: " + corrTranId + 
            ", iasToDoItemHandledCache.size: " + iasToDoItemHandledCache.size() + ", existingIasUserToDoItemList.size: " + existingIasUserToDoItemList.size());

        String cachedTranIdAction = null;
        if (iasToDoItemHandledCache != null) {
            cachedTranIdAction = iasToDoItemHandledCache.get(corrTranId);
        }

        logger.debug("validateToDoItemIsExistedOrDeletedOrCompleted - cachedTranIdAction: " + cachedTranIdAction);
        if (cachedTranIdAction != null) {
            if (IasToDoItemConstant.HISTORY_DELETE.equals(cachedTranIdAction)) {
                return ResponseUtils.getMessageResponse(tranId, idpId, msgRequestRecipientId, MsgTypeConstant.TO_DO_ITEM,
                    ResultCodes.RESULT_CD_TO_DO_ITEM_ALREADY_MARK_DELETE, ResultMessages.RESULT_MSG_TO_DO_ITEM_ALREADY_MARK_DELETE);
            } else if (IasToDoItemConstant.HISTORY_MARKCOMPLETE.equals(cachedTranIdAction)) {
                return ResponseUtils.getMessageResponse(tranId, idpId, msgRequestRecipientId, MsgTypeConstant.TO_DO_ITEM, 
                    ResultCodes.RESULT_CD_TO_DO_ITEM_ALREADY_MARK_COMPLETE, ResultMessages.RESULT_MSG_TO_DO_ITEM_ALREADY_MARK_COMPLETE);
            } else {
                return null;
            }
        } else {
            if (existingIasUserToDoItemList != null && existingIasUserToDoItemList.size() > 0) {
                IasUserToDoItem_ iasUserToDoItem = existingIasUserToDoItemList.get(0);
                if (Constants.DELETE_IND_DELETED.equals(iasUserToDoItem.getDeleteInd())) {
                    logger.debug("validateToDoItemIsExistedOrDeletedOrCompleted - DB existing record is marked as delete, iasUserToDoItem.getDeleteInd: " + iasUserToDoItem.getDeleteInd());
                    return ResponseUtils.getMessageResponse(tranId, idpId, msgRequestRecipientId, MsgTypeConstant.TO_DO_ITEM, 
                        ResultCodes.RESULT_CD_TO_DO_ITEM_ALREADY_MARK_DELETE, ResultMessages.RESULT_MSG_TO_DO_ITEM_ALREADY_MARK_DELETE);
                } else if (Constants.COMPLETE_IND_COMPLETED.equals(iasUserToDoItem.getCompleteInd())) {
                    logger.debug("validateToDoItemIsExistedOrDeletedOrCompleted - DB existing record is marked as complete, iasUserToDoItem.getCompleteInd: " + iasUserToDoItem.getCompleteInd());
                    return ResponseUtils.getMessageResponse(tranId, idpId, msgRequestRecipientId, MsgTypeConstant.TO_DO_ITEM, 
                        ResultCodes.RESULT_CD_TO_DO_ITEM_ALREADY_MARK_COMPLETE, ResultMessages.RESULT_MSG_TO_DO_ITEM_ALREADY_MARK_COMPLETE);
                } else {
                    return null;
                }
            } else {
                return ResponseUtils.getMessageResponse(tranId, idpId, msgRequestRecipientId, MsgTypeConstant.TO_DO_ITEM, 
                    ResultCodes.RESULT_CD_CORRELATED_TRAN_ID_NOT_FOUND, ResultMessages.RESULT_MSG_CORRELATED_TRAN_ID_NOT_FOUND);
            }
        }
    }

    public static MessageResponse validateItemDate(String clientId, String idpId, String msgRequestRecipientId, String tranId, 
        int toDoItemCutOffDay, int impDtUpperLimit, Date itemDate) throws Exception {

        logger.debug("validateAction - tranId: " + tranId + ", idpId: " + idpId + ", msgRequestRecipientId: " + msgRequestRecipientId + 
            ", toDoItemCutOffDay: " + toDoItemCutOffDay + ", impDtUpperLimit: " + impDtUpperLimit + ", itemDate: " + itemDate);

        if (itemDate == null) {
            return ResponseUtils.getMessageResponse(tranId, idpId, MsgTypeConstant.TO_DO_ITEM,
                msgRequestRecipientId, ResultCodes.RESULT_CD_ITEM_DATE_NOT_FOUND, ResultMessages.RESULT_MSG_ITEM_DATE_NOT_FOUND);
        } else {
            Calendar cutOffCal = Calendar.getInstance();

            cutOffCal.set(Calendar.HOUR_OF_DAY, 0);
            cutOffCal.set(Calendar.MINUTE, 0);
            cutOffCal.set(Calendar.SECOND, 0);
            cutOffCal.set(Calendar.MILLISECOND, 0);

            cutOffCal.add(Calendar.DAY_OF_YEAR, toDoItemCutOffDay);

            Calendar impUpperLimitCal = Calendar.getInstance();

            impUpperLimitCal.set(Calendar.HOUR_OF_DAY, 0);
            impUpperLimitCal.set(Calendar.MINUTE, 0);
            impUpperLimitCal.set(Calendar.SECOND, 0);
            impUpperLimitCal.set(Calendar.MILLISECOND, 0);

            impUpperLimitCal.add(Calendar.DAY_OF_YEAR, impDtUpperLimit - 1); // count today also

            Calendar itemCal = Calendar.getInstance();
            itemCal.setTime(itemDate);

            itemCal.set(Calendar.HOUR_OF_DAY, 0);
            itemCal.set(Calendar.MINUTE, 0);
            itemCal.set(Calendar.SECOND, 0);
            itemCal.set(Calendar.MILLISECOND, 0);

            if (itemCal.before(cutOffCal) || itemCal.after(impUpperLimitCal)) {
                return ResponseUtils.getMessageResponse(tranId, idpId, msgRequestRecipientId, MsgTypeConstant.TO_DO_ITEM,
                    ResultCodes.RESULT_CD_ITEM_DATE_IS_NOT_VALID, ResultMessages.RESULT_MSG_ITEM_DATE_IS_NOT_VALID);
            }
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
