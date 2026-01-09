package hk.gov.cmc.validator;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import hk.gov.cmc.common.CmcAppPropertyNames;
import hk.gov.cmc.common.IasToDoItemConstant;
import hk.gov.cmc.common.MsgTypeConstant;
import hk.gov.cmc.common.ResultCodes;
import hk.gov.cmc.common.ResultMessages;
import hk.gov.cmc.common.StatusConstants;
import hk.gov.cmc.config.CmcEnvProperties;
import hk.gov.cmc.model.maintainmessage.response.MessageResponse;
import hk.gov.cmc.persistence.ias.todoitem.IasUserToDoItem_;
import hk.gov.cmc.utils.maintainmessage.MaintainMessageUtils;

public class IasToDoItemValidator {

    private static Log logger = LogFactory.getLog(IasToDoItemValidator.class);
    private final static CmcEnvProperties cmcEnvProperties = new CmcEnvProperties();

    public IasToDoItemValidator() {
    }

    public static MessageResponse validateMergedToDoItemSize(
            String idpId, String tranId, String recipientId,
            String mergedSubjectEn, String mergedSubjectTc, String mergedSubjectSc,
            String mergedContentEn, String mergedContentTc, String mergedContentSc,
            String msgType) throws Exception {

        logger.debug(
                "validateMergedToDoItemSize - idpId: " + idpId + ", tranId: " + tranId + ", recipientId: " + recipientId
                        + ", mergedSubjectEn: " + mergedSubjectEn + ", mergedSubjectTc: " + mergedSubjectTc
                        + ", mergedSubjectSc: " + mergedSubjectSc
                        + ", mergedContentEn: " + mergedContentEn + ", mergedContentTc: " + mergedContentTc
                        + ", mergedContentSc: " + mergedContentSc
                        + ", msgType: " + msgType);

        Properties properties = cmcEnvProperties.getProperties();

        int subjectESizeLimit = Integer
                .parseInt(properties
                        .getProperty(CmcAppPropertyNames.MAINT_MSG_IAS_TO_DO_ITEM_SUBJ_ENG_SIZE_LIMIT_PROPERTY_NAME));
        int subjectCSizeLimit = Integer
                .parseInt(properties
                        .getProperty(CmcAppPropertyNames.MAINT_MSG_IAS_TO_DO_ITEM_SUBJ_CHI_SIZE_LIMIT_PROPERTY_NAME));

        int contentESizeLimit = Integer.parseInt(
                properties.getProperty(
                        CmcAppPropertyNames.MAINT_MSG_IAS_TO_DO_ITEM_CONTENT_ENG_SIZE_LIMIT_PROPERTY_NAME));
        int contentCSizeLimit = Integer.parseInt(
                properties.getProperty(
                        CmcAppPropertyNames.MAINT_MSG_IAS_TO_DO_ITEM_CONTENT_CHI_SIZE_LIMIT_PROPERTY_NAME));

        if ((mergedSubjectEn == null || mergedSubjectEn.length() == 0) ||
                (mergedContentEn == null || mergedContentEn.length() == 0)) {
            return MaintainMessageUtils.getMessageResponse(tranId, idpId, recipientId, msgType,
                    ResultCodes.RESULT_CD_GENERAL_ERROR, ResultMessages.RESULT_MSG_GENERAL_ERROR);
        }

        if (getMsgSize(mergedSubjectEn) > subjectESizeLimit) {
            return MaintainMessageUtils.getMessageResponse(tranId, idpId, recipientId, msgType,
                    ResultCodes.RESULT_CD_MERGED_IAS_TO_DO_ITEM_SUBJECT_EN_LENGTH_OVER_LIMIT,
                    ResultMessages.RESULT_MSG_MERGED_IAS_TO_DO_ITEM_SUBJECT_EN_LENGTH_OVER_LIMIT);
        }

        if (getMsgSize(mergedSubjectTc) > subjectCSizeLimit) {
            return MaintainMessageUtils.getMessageResponse(tranId, idpId, recipientId, msgType,
                    ResultCodes.RESULT_CD_MERGED_IAS_TO_DO_ITEM_SUBJECT_TC_LENGTH_OVER_LIMIT,
                    ResultMessages.RESULT_MSG_MERGED_IAS_TO_DO_ITEM_SUBJECT_TC_LENGTH_OVER_LIMIT);
        }

        if (getMsgSize(mergedSubjectSc) > subjectCSizeLimit) {
            return MaintainMessageUtils.getMessageResponse(tranId, idpId, recipientId, msgType,
                    ResultCodes.RESULT_CD_MERGED_IAS_TO_DO_ITEM_SUBJECT_SC_LENGTH_OVER_LIMIT,
                    ResultMessages.RESULT_MSG_MERGED_IAS_TO_DO_ITEM_SUBJECT_SC_LENGTH_OVER_LIMIT);
        }

        if (getMsgSize(mergedContentEn) > contentESizeLimit) {
            return MaintainMessageUtils.getMessageResponse(tranId, idpId, recipientId, msgType,
                    ResultCodes.RESULT_CD_MERGED_IAS_TO_DO_ITEM_CONTENT_EN_LENGTH_OVER_LIMIT,
                    ResultMessages.RESULT_MSG_MERGED_IAS_TO_DO_ITEM_CONTENT_EN_LENGTH_OVER_LIMIT);
        }

        if (getMsgSize(mergedContentTc) > contentCSizeLimit) {
            return MaintainMessageUtils.getMessageResponse(tranId, idpId, recipientId, msgType,
                    ResultCodes.RESULT_CD_MERGED_IAS_TO_DO_ITEM_CONTENT_TC_LENGTH_OVER_LIMIT,
                    ResultMessages.RESULT_MSG_MERGED_IAS_TO_DO_ITEM_CONTENT_TC_LENGTH_OVER_LIMIT);
        }

        if (getMsgSize(mergedContentSc) > contentCSizeLimit) {
            return MaintainMessageUtils.getMessageResponse(tranId, idpId, recipientId, msgType,
                    ResultCodes.RESULT_CD_MERGED_IAS_TO_DO_ITEM_CONTENT_SC_LENGTH_OVER_LIMIT,
                    ResultMessages.RESULT_MSG_MERGED_IAS_TO_DO_ITEM_CONTENT_SC_LENGTH_OVER_LIMIT);
        }

        return null;
    }

    public static MessageResponse validateToDoItemIsExistedOrDeletedOrCompleted(
            String clientId, String idpId, String msgRequestRecipientId,
            String tranId, String corrTranId,
            Map<String, String> iasToDoItemHandledCache, List<IasUserToDoItem_> existingIasUserToDoItemList)
            throws Exception {
        logger.debug(
                "validateToDoItemIsExistedOrDeletedOrCompleted - clientId: " + clientId + ", idpId: " + idpId
                        + ", msgRequestRecipientId: " + msgRequestRecipientId + ", tranId: " + tranId + ", corrTranId: "
                        + corrTranId +
                        ", iasToDoItemHandledCache.size: " + iasToDoItemHandledCache.size()
                        + ", existingIasUserToDoItemList.size: " + existingIasUserToDoItemList.size());

        String cachedTranIdAction = null;
        if (iasToDoItemHandledCache != null) {
            cachedTranIdAction = iasToDoItemHandledCache.get(corrTranId);
        }

        logger.debug("validateToDoItemIsExistedOrDeletedOrCompleted - cachedTranIdAction: " + cachedTranIdAction);
        if (cachedTranIdAction != null) {
            if (IasToDoItemConstant.HISTORY_DELETE.equals(cachedTranIdAction)) {
                return MaintainMessageUtils.getMessageResponse(tranId, idpId, msgRequestRecipientId,
                        MsgTypeConstant.TO_DO_ITEM,
                        ResultCodes.RESULT_CD_TO_DO_ITEM_ALREADY_MARK_DELETE,
                        ResultMessages.RESULT_MSG_TO_DO_ITEM_ALREADY_MARK_DELETE);
            } else if (IasToDoItemConstant.HISTORY_MARKCOMPLETE.equals(cachedTranIdAction)) {
                return MaintainMessageUtils.getMessageResponse(tranId, idpId, msgRequestRecipientId,
                        MsgTypeConstant.TO_DO_ITEM,
                        ResultCodes.RESULT_CD_TO_DO_ITEM_ALREADY_MARK_COMPLETE,
                        ResultMessages.RESULT_MSG_TO_DO_ITEM_ALREADY_MARK_COMPLETE);
            } else {
                return null;
            }
        } else {
            if (existingIasUserToDoItemList != null && existingIasUserToDoItemList.size() > 0) {
                IasUserToDoItem_ iasUserToDoItem = existingIasUserToDoItemList.get(0);
                if (StatusConstants.DELETE_IND_DELETED.equals(iasUserToDoItem.getDeleteInd())) {
                    logger.debug(
                            "validateToDoItemIsExistedOrDeletedOrCompleted - DB existing record is marked as delete, iasUserToDoItem.getDeleteInd: "
                                    + iasUserToDoItem.getDeleteInd());
                    return MaintainMessageUtils.getMessageResponse(tranId, idpId, msgRequestRecipientId,
                            MsgTypeConstant.TO_DO_ITEM,
                            ResultCodes.RESULT_CD_TO_DO_ITEM_ALREADY_MARK_DELETE,
                            ResultMessages.RESULT_MSG_TO_DO_ITEM_ALREADY_MARK_DELETE);
                } else if (StatusConstants.COMPLETE_IND_COMPLETED.equals(iasUserToDoItem.getCompleteInd())) {
                    logger.debug(
                            "validateToDoItemIsExistedOrDeletedOrCompleted - DB existing record is marked as complete, iasUserToDoItem.getCompleteInd: "
                                    + iasUserToDoItem.getCompleteInd());
                    return MaintainMessageUtils.getMessageResponse(tranId, idpId, msgRequestRecipientId,
                            MsgTypeConstant.TO_DO_ITEM,
                            ResultCodes.RESULT_CD_TO_DO_ITEM_ALREADY_MARK_COMPLETE,
                            ResultMessages.RESULT_MSG_TO_DO_ITEM_ALREADY_MARK_COMPLETE);
                } else {
                    return null;
                }
            } else {
                return MaintainMessageUtils.getMessageResponse(tranId, idpId, msgRequestRecipientId,
                        MsgTypeConstant.TO_DO_ITEM,
                        ResultCodes.RESULT_CD_CORRELATED_TRAN_ID_NOT_FOUND,
                        ResultMessages.RESULT_MSG_CORRELATED_TRAN_ID_NOT_FOUND);
            }
        }
    }

    public static MessageResponse validateItemDate(String clientId, String idpId, String msgRequestRecipientId,
            String tranId,
            int toDoItemCutOffDay, int impDtUpperLimit, Date itemDate) throws Exception {

        logger.debug("validateAction - tranId: " + tranId + ", idpId: " + idpId + ", msgRequestRecipientId: "
                + msgRequestRecipientId +
                ", toDoItemCutOffDay: " + toDoItemCutOffDay + ", impDtUpperLimit: " + impDtUpperLimit + ", itemDate: "
                + itemDate);

        if (itemDate == null) {
            return MaintainMessageUtils.getMessageResponse(tranId, idpId, MsgTypeConstant.TO_DO_ITEM,
                    msgRequestRecipientId, ResultCodes.RESULT_CD_ITEM_DATE_NOT_FOUND,
                    ResultMessages.RESULT_MSG_ITEM_DATE_NOT_FOUND);
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
                return MaintainMessageUtils.getMessageResponse(tranId, idpId, msgRequestRecipientId,
                        MsgTypeConstant.TO_DO_ITEM,
                        ResultCodes.RESULT_CD_ITEM_DATE_IS_NOT_VALID, ResultMessages.RESULT_MSG_ITEM_DATE_IS_NOT_VALID);
            }
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
