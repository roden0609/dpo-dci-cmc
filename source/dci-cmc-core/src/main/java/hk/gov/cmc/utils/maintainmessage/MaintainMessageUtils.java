package hk.gov.cmc.utils.maintainmessage;

import hk.gov.cmc.common.ResultCodes;
import hk.gov.cmc.common.ResultMessages;
import hk.gov.cmc.model.maintainmessage.response.MaintainMessageResponse;
import hk.gov.cmc.model.maintainmessage.response.MessageResponse;

public class MaintainMessageUtils {

    public MaintainMessageUtils() {
    }

    public static MaintainMessageResponse getMaintainMessageResponse(String resultCode, String resultMessage) {
        MaintainMessageResponse response = new MaintainMessageResponse();
        response.setResultCode(resultCode);
        response.setResultMessage(resultMessage);
        return response;
    }

    public static MessageResponse getMessageResponse(String tranId, String idpId, String recipientId,
            String msgType, String tranResultCd, String tranResultMsg) {
        MessageResponse msgRsp = new MessageResponse();
        msgRsp.setTranId(tranId);
        msgRsp.setIdpId(idpId);
        msgRsp.setRecipientId(recipientId);
        msgRsp.setMsgType(msgType);
        msgRsp.setTranResultCode(tranResultCd);
        msgRsp.setTranResultMessage(tranResultMsg);
        return msgRsp;
    }

    public static MaintainMessageResponse getConcludedMaintainMessageResponse(
            MaintainMessageResponse maintainMessageResponse) {
        boolean hasError = false;
        for (MessageResponse msgResp : maintainMessageResponse.getMessageResponses()) {
            if (!ResultCodes.RESULT_CD_TRAN_SUCCESS.equals(msgResp.getTranResultCode())) {
                hasError = true;
                break;
            }
        }

        if (hasError) {
            maintainMessageResponse.setResultCode(ResultCodes.RESULT_CD_MESSAGE_PROCESSED_WITH_ERROR);
            maintainMessageResponse.setResultMessage(ResultMessages.RESULT_MSG_MESSAGE_PROCESSED_WITH_ERROR);
        } else {
            maintainMessageResponse.setResultCode(ResultCodes.RESULT_CD_SUCCESS);
            maintainMessageResponse.setResultMessage(ResultMessages.RESULT_MSG_SUCCESS);
        }

        return maintainMessageResponse;
    }

}
