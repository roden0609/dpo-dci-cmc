package hk.gov.ogcio.mars_cmc.cmc.service.validator;

import java.util.HashSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MaintainMessageResponse;
import hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.Recipient;
import hk.gov.ogcio.mars_cmc.cmc.datatype.message.Constants;
import hk.gov.ogcio.mars_cmc.cmc.datatype.message.IasUser;
import hk.gov.ogcio.mars_cmc.cmc.service.constant.MsgTypeConstant;
import hk.gov.ogcio.mars_cmc.cmc.utils.ResponseUtils;
import hk.gov.ogcio.mars_cmc.framework.common.sql.HPFW_Connection;

public class IasRecipientValidator {

    private static Log logger = LogFactory.getLog(IasRecipientValidator.class);

    public static MaintainMessageResponse validateIasRecipient(HPFW_Connection conn, Recipient recipient, IasUser iasUser,
            HashSet<String> iasUserSet, 
            // CMC-2025-015: Fixed VIP iAM Smart Message cannot be sent to iAM Smart user - BEGIN
            // boolean iasOptCheck,
            // CMC-2025-015: Fixed VIP iAM Smart Message cannot be sent to iAM Smart user - END
            boolean hasToDoItemInRequest, boolean hasIasApplicationInRequest, boolean hasEMsg)
            throws Exception {
        logger.debug("validateIasRecipient - START");

        MaintainMessageResponse response = new MaintainMessageResponse();
        // int action = Action_ST.NEW_TYPE;
        // if (recipient.getAction() != null) {
        //     action = recipient.getAction().getType();
        // }

        // check recipient ID is exist
        if (recipient.getRecipientID() == null || "".equals(recipient.getRecipientID())) {
            if (hasEMsg) {
                response.addMessageResponse(ResponseUtils.getMessageResponse(recipient.getTranID(), recipient.getIdpID(), recipient.getRecipientID(), 
                    MsgTypeConstant.MESSAGE, ResultCodes.RESULT_CD_RECIPIENT_ID_NOT_FOUND, ResultMessages.RESULT_MSG_RECIPIENT_ID_NOT_FOUND));
            }
            if (hasToDoItemInRequest) {
                response.addMessageResponse(ResponseUtils.getMessageResponse(recipient.getTranID(), recipient.getIdpID(), recipient.getRecipientID(), 
                    MsgTypeConstant.TO_DO_ITEM, ResultCodes.RESULT_CD_RECIPIENT_ID_NOT_FOUND, ResultMessages.RESULT_MSG_RECIPIENT_ID_NOT_FOUND));
            }
            if (hasIasApplicationInRequest) {
                response.addMessageResponse(ResponseUtils.getMessageResponse(recipient.getTranID(), recipient.getIdpID(), recipient.getRecipientID(), 
                    MsgTypeConstant.APPLICATION, ResultCodes.RESULT_CD_RECIPIENT_ID_NOT_FOUND, ResultMessages.RESULT_MSG_RECIPIENT_ID_NOT_FOUND));
            }
        }

        // reject duplicate recipient
        if (iasUserSet.contains(recipient.getRecipientID())) {
            if (hasEMsg) {
                response.addMessageResponse(ResponseUtils.getMessageResponse(recipient.getTranID(), recipient.getIdpID(),
                        recipient.getRecipientID(), MsgTypeConstant.MESSAGE,
                        ResultCodes.RESULT_CD_RECIPIENT_ID_IS_DUPLICATED,
                        ResultMessages.RESULT_MSG_RECIPIENT_ID_IS_DUPLICATED));
            }
            if (hasToDoItemInRequest) {
                response.addMessageResponse(ResponseUtils.getMessageResponse(recipient.getTranID(), recipient.getIdpID(),
                        recipient.getRecipientID(), MsgTypeConstant.TO_DO_ITEM,
                        ResultCodes.RESULT_CD_RECIPIENT_ID_IS_DUPLICATED,
                        ResultMessages.RESULT_MSG_RECIPIENT_ID_IS_DUPLICATED));
            }
            if (hasIasApplicationInRequest) {
                response.addMessageResponse(ResponseUtils.getMessageResponse(recipient.getTranID(), recipient.getIdpID(),
                        recipient.getRecipientID(), MsgTypeConstant.APPLICATION,
                        ResultCodes.RESULT_CD_RECIPIENT_ID_IS_DUPLICATED,
                        ResultMessages.RESULT_MSG_RECIPIENT_ID_IS_DUPLICATED));
            }
        }

        // check User status to see whether he has been marked DEREGISTERED or not
        if (iasUser.getStatus() != null && Constants.IAS_USER_STATUS_DEREGISTERED.equals(iasUser.getStatus())) {
            if (hasEMsg) {
                response.addMessageResponse(ResponseUtils.getMessageResponse(recipient.getTranID(), recipient.getIdpID(),
                        recipient.getRecipientID(), MsgTypeConstant.MESSAGE,
                        ResultCodes.RESULT_CD_RECIPIENT_IS_DEREGISTERED,
                        ResultMessages.RESULT_MSG_RECIPIENT_IS_DEREGISTERED));
            }
            if (hasToDoItemInRequest) {
                response.addMessageResponse(ResponseUtils.getMessageResponse(recipient.getTranID(), recipient.getIdpID(),
                        recipient.getRecipientID(), MsgTypeConstant.TO_DO_ITEM,
                        ResultCodes.RESULT_CD_RECIPIENT_IS_DEREGISTERED,
                        ResultMessages.RESULT_MSG_RECIPIENT_IS_DEREGISTERED));
            }
            if (hasIasApplicationInRequest) {
                response.addMessageResponse(ResponseUtils.getMessageResponse(recipient.getTranID(), recipient.getIdpID(),
                        recipient.getRecipientID(), MsgTypeConstant.APPLICATION,
                        ResultCodes.RESULT_CD_RECIPIENT_IS_DEREGISTERED,
                        ResultMessages.RESULT_MSG_RECIPIENT_IS_DEREGISTERED));
            }
        }

        // check User status to see whether he has been marked SUSPENDED or not
        if (iasUser.getStatus() != null && Constants.IAS_USER_STATUS_SUSPENDED.equals(iasUser.getStatus())) {
            if (hasEMsg) {
                response.addMessageResponse(ResponseUtils.getMessageResponse(recipient.getTranID(), recipient.getIdpID(),
                        recipient.getRecipientID(), MsgTypeConstant.MESSAGE,
                        ResultCodes.RESULT_CD_RECIPIENT_IS_SUSPENDED,
                        ResultMessages.RESULT_MSG_RECIPIENT_IS_SUSPENDED));
            }
            if (hasToDoItemInRequest) {
                response.addMessageResponse(ResponseUtils.getMessageResponse(recipient.getTranID(), recipient.getIdpID(),
                        recipient.getRecipientID(), MsgTypeConstant.TO_DO_ITEM,
                        ResultCodes.RESULT_CD_RECIPIENT_IS_SUSPENDED,
                        ResultMessages.RESULT_MSG_RECIPIENT_IS_SUSPENDED));
            }
            if (hasIasApplicationInRequest) {
                response.addMessageResponse(ResponseUtils.getMessageResponse(recipient.getTranID(), recipient.getIdpID(),
                        recipient.getRecipientID(), MsgTypeConstant.APPLICATION,
                        ResultCodes.RESULT_CD_RECIPIENT_IS_SUSPENDED,
                        ResultMessages.RESULT_MSG_RECIPIENT_IS_SUSPENDED));
            }
        }

        // check User status to see whether he has been marked INVALID or not
        if (iasUser.getStatus() != null && Constants.IAS_USER_STATUS_INVALID.equals(iasUser.getStatus())) {
            if (hasEMsg) {
                response.addMessageResponse(ResponseUtils.getMessageResponse(recipient.getTranID(), recipient.getIdpID(),
                        recipient.getRecipientID(), MsgTypeConstant.MESSAGE,
                        ResultCodes.RESULT_CD_RECIPIENT_IS_INVALID,
                        ResultMessages.RESULT_MSG_RECIPIENT_IS_INVALID));
            }
            if (hasToDoItemInRequest) {
                response.addMessageResponse(ResponseUtils.getMessageResponse(recipient.getTranID(), recipient.getIdpID(),
                        recipient.getRecipientID(), MsgTypeConstant.TO_DO_ITEM,
                        ResultCodes.RESULT_CD_RECIPIENT_IS_INVALID,
                        ResultMessages.RESULT_MSG_RECIPIENT_IS_INVALID));
            }
            if (hasIasApplicationInRequest) {
                response.addMessageResponse(ResponseUtils.getMessageResponse(recipient.getTranID(), recipient.getIdpID(),
                        recipient.getRecipientID(), MsgTypeConstant.APPLICATION,
                        ResultCodes.RESULT_CD_RECIPIENT_IS_INVALID,
                        ResultMessages.RESULT_MSG_RECIPIENT_IS_INVALID));
            }
        }

        // check User OPT IN value to see whether he has opt out to receive message or not
		// check OPT_IN value only if request contains iAM Smart message type
        // Moved to IasMsgValidator.validateOptInStatus
        // if ((iasOptCheck) && ((iasUser.getOptIn() != null
        //         && (Constants.OPT_IN_N.equals(iasUser.getOptIn()) || Constants.OPT_IN_U.equals(iasUser.getOptIn()))))) {
        //     return ResponseUtils.getMessageResponse(recipient.getTranID(), recipient.getIdpID(),
        //             recipient.getRecipientID(), ResultCodes.RESULT_CD_USER_REJECT_MSG,
        //             ResultMessages.RESULT_MSG_USER_REJECT_MSG);
        // }

        // check TranID length
        if (recipient.getTranID() != null && recipient.getTranID().length() > 20) {
            if (hasEMsg) {
                response.addMessageResponse(ResponseUtils.getMessageResponse(recipient.getTranID(), recipient.getIdpID(),
                        recipient.getRecipientID(), MsgTypeConstant.MESSAGE,
                        ResultCodes.RESULT_CD_TRAN_ID_LENGTH_OVER_LIMIT,
                        ResultMessages.RESULT_MSG_TRAN_ID_LENGTH_OVER_LIMIT));
            }
            if (hasToDoItemInRequest) {
                response.addMessageResponse(ResponseUtils.getMessageResponse(recipient.getTranID(), recipient.getIdpID(),
                        recipient.getRecipientID(), MsgTypeConstant.TO_DO_ITEM,
                        ResultCodes.RESULT_CD_TRAN_ID_LENGTH_OVER_LIMIT,
                        ResultMessages.RESULT_MSG_TRAN_ID_LENGTH_OVER_LIMIT));
            }
            if (hasIasApplicationInRequest) {
                response.addMessageResponse(ResponseUtils.getMessageResponse(recipient.getTranID(), recipient.getIdpID(),
                        recipient.getRecipientID(), MsgTypeConstant.APPLICATION,
                        ResultCodes.RESULT_CD_TRAN_ID_LENGTH_OVER_LIMIT,
                        ResultMessages.RESULT_MSG_TRAN_ID_LENGTH_OVER_LIMIT));
            }
        }

        // check application status is valid or not
        // Moved to IasApplicationValidator.validateAppStatus
        // if (hasIasApplicationInRequest) {
        //     try {
        //         AppStatus_ST.fromValue(recipient.getAppStatus());
        //     } catch (IllegalArgumentException e) {
        //         return ResponseUtils.getMessageResponse(recipient.getTranID(), recipient.getIdpID(), recipient.getRecipientID(), 
        //             ResultCodes.RESULT_CD_APPLICATION_STATUS_NOT_VALID, ResultMessages.RESULT_MSG_APPLICATION_STATUS_NOT_VALID);
        //     }
        // }

        // check action is valid or not
        // Moved to IasMsgValidator.validateAction
        // if (hasEMsg) {
        //     if (action == Action_ST.UPDATE_TYPE) {
        //         return ResponseUtils.getMessageResponse(recipient.getTranID(), recipient.getIdpID(),
        //             recipient.getRecipientID(), ResultCodes.RESULT_CD_EMSG_ACTION_IS_NOT_VALID, ResultMessages.RESULT_MSG_EMSG_ACTION_IS_NOT_VALID);
        //     }
        // }

        // Moved to IasToDoItemValidator.validateItemDate
        // if (action == Action_ST.NEW_TYPE || action == Action_ST.REPLACE_TYPE) {
        //     // check ItemDate for to-do-item
        //     if (hasToDoItemInRequest) {
        //         if (recipient.getItemDate() == null) {
        //             return ResponseUtils.getMessageResponse(recipient.getTranID(), recipient.getIdpID(),
        //                 recipient.getRecipientID(), ResultCodes.RESULT_CD_ITEM_DATE_NOT_FOUND, ResultMessages.RESULT_MSG_ITEM_DATE_NOT_FOUND);
        //         }
        //     }

        //     // check ItemDate to make sure it must on or after cutoff date
        //     if (recipient.getItemDate() != null) {

        //         Calendar cutOffCal = Calendar.getInstance();

        //         cutOffCal.set(Calendar.HOUR_OF_DAY, 0);
        //         cutOffCal.set(Calendar.MINUTE, 0);
        //         cutOffCal.set(Calendar.SECOND, 0);
        //         cutOffCal.set(Calendar.MILLISECOND, 0);

        //         cutOffCal.add(Calendar.DAY_OF_YEAR, toDoItemCutOffDay);

        //         Calendar impUpperLimitCal = Calendar.getInstance();

        //         impUpperLimitCal.set(Calendar.HOUR_OF_DAY, 0);
        //         impUpperLimitCal.set(Calendar.MINUTE, 0);
        //         impUpperLimitCal.set(Calendar.SECOND, 0);
        //         impUpperLimitCal.set(Calendar.MILLISECOND, 0);

        //         impUpperLimitCal.add(Calendar.DAY_OF_YEAR, impDtUpperLimit - 1); // count today also

        //         Calendar itemCal = Calendar.getInstance();
        //         itemCal.setTime(recipient.getItemDate());

        //         itemCal.set(Calendar.HOUR_OF_DAY, 0);
        //         itemCal.set(Calendar.MINUTE, 0);
        //         itemCal.set(Calendar.SECOND, 0);
        //         itemCal.set(Calendar.MILLISECOND, 0);

        //         if (itemCal.before(cutOffCal) || itemCal.after(impUpperLimitCal)) {
        //             return ResponseUtils.getMessageResponse(recipient.getTranID(), recipient.getIdpID(),
        //                     recipient.getRecipientID(),
        //                     ResultCodes.RESULT_CD_ITEM_DATE_IS_NOT_VALID, ResultMessages.RESULT_MSG_ITEM_DATE_IS_NOT_VALID);
        //         }
        //     }
        // }

        logger.debug("validateIasRecipient - END");

        return response;
    }

}
