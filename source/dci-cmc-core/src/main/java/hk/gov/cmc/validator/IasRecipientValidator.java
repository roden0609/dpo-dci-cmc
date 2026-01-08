package hk.gov.cmc.validator;

import java.util.HashSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import hk.gov.cmc.common.IntegrationConstants;
import hk.gov.cmc.common.MsgTypeConstant;
import hk.gov.cmc.common.ResultCodes;
import hk.gov.cmc.common.ResultMessages;
import hk.gov.cmc.model.maintainmessage.request.Recipient;
import hk.gov.cmc.model.maintainmessage.response.MaintainMessageResponse;
import hk.gov.cmc.model.maintainmessage.user.IasUser;
import hk.gov.cmc.persistence.connection.hpfw.HPFW_Connection;
import hk.gov.cmc.utils.maintainmessage.MaintainMessageUtils;

public class IasRecipientValidator {

    private static Log logger = LogFactory.getLog(IasRecipientValidator.class);

    public static MaintainMessageResponse validateIasRecipient(HPFW_Connection conn, Recipient recipient,
            IasUser iasUser,
            HashSet<String> iasUserSet,
            boolean hasToDoItemInRequest, boolean hasIasApplicationInRequest, boolean hasEMsg)
            throws Exception {
        logger.debug("validateIasRecipient - START");

        MaintainMessageResponse response = new MaintainMessageResponse();

        if (recipient.getRecipientId() == null || "".equals(recipient.getRecipientId())) {
            if (hasEMsg) {
                response.addMessageResponse(MaintainMessageUtils.getMessageResponse(recipient.getTranId(),
                        recipient.getIdpId(), recipient.getRecipientId(),
                        MsgTypeConstant.MESSAGE, ResultCodes.RESULT_CD_RECIPIENT_ID_NOT_FOUND,
                        ResultMessages.RESULT_MSG_RECIPIENT_ID_NOT_FOUND));
            }
            if (hasToDoItemInRequest) {
                response.addMessageResponse(MaintainMessageUtils.getMessageResponse(recipient.getTranId(),
                        recipient.getIdpId(), recipient.getRecipientId(),
                        MsgTypeConstant.TO_DO_ITEM, ResultCodes.RESULT_CD_RECIPIENT_ID_NOT_FOUND,
                        ResultMessages.RESULT_MSG_RECIPIENT_ID_NOT_FOUND));
            }
            if (hasIasApplicationInRequest) {
                response.addMessageResponse(MaintainMessageUtils.getMessageResponse(recipient.getTranId(),
                        recipient.getIdpId(), recipient.getRecipientId(),
                        MsgTypeConstant.APPLICATION, ResultCodes.RESULT_CD_RECIPIENT_ID_NOT_FOUND,
                        ResultMessages.RESULT_MSG_RECIPIENT_ID_NOT_FOUND));
            }
        }

        if (iasUserSet.contains(recipient.getRecipientId())) {
            if (hasEMsg) {
                response.addMessageResponse(
                        MaintainMessageUtils.getMessageResponse(recipient.getTranId(), recipient.getIdpId(),
                                recipient.getRecipientId(), MsgTypeConstant.MESSAGE,
                                ResultCodes.RESULT_CD_RECIPIENT_ID_IS_DUPLICATED,
                                ResultMessages.RESULT_MSG_RECIPIENT_ID_IS_DUPLICATED));
            }
            if (hasToDoItemInRequest) {
                response.addMessageResponse(
                        MaintainMessageUtils.getMessageResponse(recipient.getTranId(), recipient.getIdpId(),
                                recipient.getRecipientId(), MsgTypeConstant.TO_DO_ITEM,
                                ResultCodes.RESULT_CD_RECIPIENT_ID_IS_DUPLICATED,
                                ResultMessages.RESULT_MSG_RECIPIENT_ID_IS_DUPLICATED));
            }
            if (hasIasApplicationInRequest) {
                response.addMessageResponse(
                        MaintainMessageUtils.getMessageResponse(recipient.getTranId(), recipient.getIdpId(),
                                recipient.getRecipientId(), MsgTypeConstant.APPLICATION,
                                ResultCodes.RESULT_CD_RECIPIENT_ID_IS_DUPLICATED,
                                ResultMessages.RESULT_MSG_RECIPIENT_ID_IS_DUPLICATED));
            }
        }

        if (iasUser.getStatus() != null && IntegrationConstants.IAS_USER_STATUS_DEREGISTERED.equals(iasUser.getStatus())) {
            if (hasEMsg) {
                response.addMessageResponse(
                        MaintainMessageUtils.getMessageResponse(recipient.getTranId(), recipient.getIdpId(),
                                recipient.getRecipientId(), MsgTypeConstant.MESSAGE,
                                ResultCodes.RESULT_CD_RECIPIENT_IS_DEREGISTERED,
                                ResultMessages.RESULT_MSG_RECIPIENT_IS_DEREGISTERED));
            }
            if (hasToDoItemInRequest) {
                response.addMessageResponse(
                        MaintainMessageUtils.getMessageResponse(recipient.getTranId(), recipient.getIdpId(),
                                recipient.getRecipientId(), MsgTypeConstant.TO_DO_ITEM,
                                ResultCodes.RESULT_CD_RECIPIENT_IS_DEREGISTERED,
                                ResultMessages.RESULT_MSG_RECIPIENT_IS_DEREGISTERED));
            }
            if (hasIasApplicationInRequest) {
                response.addMessageResponse(
                        MaintainMessageUtils.getMessageResponse(recipient.getTranId(), recipient.getIdpId(),
                                recipient.getRecipientId(), MsgTypeConstant.APPLICATION,
                                ResultCodes.RESULT_CD_RECIPIENT_IS_DEREGISTERED,
                                ResultMessages.RESULT_MSG_RECIPIENT_IS_DEREGISTERED));
            }
        }

        // check User status to see whether he has been marked SUSPENDED or not
        if (iasUser.getStatus() != null && IntegrationConstants.IAS_USER_STATUS_SUSPENDED.equals(iasUser.getStatus())) {
            if (hasEMsg) {
                response.addMessageResponse(
                        MaintainMessageUtils.getMessageResponse(recipient.getTranId(), recipient.getIdpId(),
                                recipient.getRecipientId(), MsgTypeConstant.MESSAGE,
                                ResultCodes.RESULT_CD_RECIPIENT_IS_SUSPENDED,
                                ResultMessages.RESULT_MSG_RECIPIENT_IS_SUSPENDED));
            }
            if (hasToDoItemInRequest) {
                response.addMessageResponse(
                        MaintainMessageUtils.getMessageResponse(recipient.getTranId(), recipient.getIdpId(),
                                recipient.getRecipientId(), MsgTypeConstant.TO_DO_ITEM,
                                ResultCodes.RESULT_CD_RECIPIENT_IS_SUSPENDED,
                                ResultMessages.RESULT_MSG_RECIPIENT_IS_SUSPENDED));
            }
            if (hasIasApplicationInRequest) {
                response.addMessageResponse(
                        MaintainMessageUtils.getMessageResponse(recipient.getTranId(), recipient.getIdpId(),
                                recipient.getRecipientId(), MsgTypeConstant.APPLICATION,
                                ResultCodes.RESULT_CD_RECIPIENT_IS_SUSPENDED,
                                ResultMessages.RESULT_MSG_RECIPIENT_IS_SUSPENDED));
            }
        }

        if (iasUser.getStatus() != null && IntegrationConstants.IAS_USER_STATUS_INVALID.equals(iasUser.getStatus())) {
            if (hasEMsg) {
                response.addMessageResponse(
                        MaintainMessageUtils.getMessageResponse(recipient.getTranId(), recipient.getIdpId(),
                                recipient.getRecipientId(), MsgTypeConstant.MESSAGE,
                                ResultCodes.RESULT_CD_RECIPIENT_IS_INVALID,
                                ResultMessages.RESULT_MSG_RECIPIENT_IS_INVALID));
            }
            if (hasToDoItemInRequest) {
                response.addMessageResponse(
                        MaintainMessageUtils.getMessageResponse(recipient.getTranId(), recipient.getIdpId(),
                                recipient.getRecipientId(), MsgTypeConstant.TO_DO_ITEM,
                                ResultCodes.RESULT_CD_RECIPIENT_IS_INVALID,
                                ResultMessages.RESULT_MSG_RECIPIENT_IS_INVALID));
            }
            if (hasIasApplicationInRequest) {
                response.addMessageResponse(
                        MaintainMessageUtils.getMessageResponse(recipient.getTranId(), recipient.getIdpId(),
                                recipient.getRecipientId(), MsgTypeConstant.APPLICATION,
                                ResultCodes.RESULT_CD_RECIPIENT_IS_INVALID,
                                ResultMessages.RESULT_MSG_RECIPIENT_IS_INVALID));
            }
        }

        if (recipient.getTranId() != null && recipient.getTranId().length() > 20) {
            if (hasEMsg) {
                response.addMessageResponse(
                        MaintainMessageUtils.getMessageResponse(recipient.getTranId(), recipient.getIdpId(),
                                recipient.getRecipientId(), MsgTypeConstant.MESSAGE,
                                ResultCodes.RESULT_CD_TRAN_ID_LENGTH_OVER_LIMIT,
                                ResultMessages.RESULT_MSG_TRAN_ID_LENGTH_OVER_LIMIT));
            }
            if (hasToDoItemInRequest) {
                response.addMessageResponse(
                        MaintainMessageUtils.getMessageResponse(recipient.getTranId(), recipient.getIdpId(),
                                recipient.getRecipientId(), MsgTypeConstant.TO_DO_ITEM,
                                ResultCodes.RESULT_CD_TRAN_ID_LENGTH_OVER_LIMIT,
                                ResultMessages.RESULT_MSG_TRAN_ID_LENGTH_OVER_LIMIT));
            }
            if (hasIasApplicationInRequest) {
                response.addMessageResponse(
                        MaintainMessageUtils.getMessageResponse(recipient.getTranId(), recipient.getIdpId(),
                                recipient.getRecipientId(), MsgTypeConstant.APPLICATION,
                                ResultCodes.RESULT_CD_TRAN_ID_LENGTH_OVER_LIMIT,
                                ResultMessages.RESULT_MSG_TRAN_ID_LENGTH_OVER_LIMIT));
            }
        }

        logger.debug("validateIasRecipient - END");

        return response;
    }

}
