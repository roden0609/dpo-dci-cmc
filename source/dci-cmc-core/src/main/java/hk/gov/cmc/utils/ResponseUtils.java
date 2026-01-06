package hk.gov.cmc.utils;


public class ResponseUtils {

    public ResponseUtils() {
    }

    public static MessageResponse getMessageResponse(String tranId, String idpId, String recipientId,
            String msgType, String tranResultCd, String tranResultMsg) {
        MessageResponse msgRsp = new MessageResponse();
        msgRsp.setTranID(tranId);
        msgRsp.setIdpID(idpId);
        msgRsp.setRecipientID(recipientId);
        msgRsp.setMsgType(msgType);
        msgRsp.setTranResultCode(tranResultCd);
        msgRsp.setTranResultMessage(tranResultMsg);
        return msgRsp;
    }

}
