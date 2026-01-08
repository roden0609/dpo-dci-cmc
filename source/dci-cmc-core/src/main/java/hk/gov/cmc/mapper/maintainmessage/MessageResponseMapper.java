package hk.gov.cmc.mapper.maintainmessage;

import hk.gov.cmc.jaxb.maintainmessage.MessageResponseCT;
import hk.gov.cmc.model.maintainmessage.response.MessageResponse;

public final class MessageResponseMapper {

    private MessageResponseMapper() {
    }

    public static MessageResponseCT toJaxb(MessageResponse domain) {
        if (domain == null) {
            return null;
        }

        MessageResponseCT ct = new MessageResponseCT();
        ct.setTranID(domain.getTranId());
        ct.setIdpID(domain.getIdpId());
        ct.setRecipientID(domain.getRecipientId());
        ct.setTranResultCode(domain.getTranResultCode());
        ct.setTranResultMessage(domain.getTranResultMessage());
        ct.setAppRefNum(domain.getAppRefNum());
        ct.setMsgType(domain.getMsgType());

        return ct;
    }

    public static MessageResponse fromJaxb(MessageResponseCT ct) {
        if (ct == null) {
            return null;
        }

        MessageResponse domain = new MessageResponse();
        domain.setTranId(ct.getTranID());
        domain.setIdpId(ct.getIdpID());
        domain.setRecipientId(ct.getRecipientID());
        domain.setTranResultCode(ct.getTranResultCode());
        domain.setTranResultMessage(ct.getTranResultMessage());
        domain.setAppRefNum(ct.getAppRefNum());
        domain.setMsgType(ct.getMsgType());

        return domain;
    }
}
