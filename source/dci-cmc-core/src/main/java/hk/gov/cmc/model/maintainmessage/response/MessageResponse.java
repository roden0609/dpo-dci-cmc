package hk.gov.cmc.model.maintainmessage.response;

import java.io.Serializable;

public class MessageResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private String tranId;
    private String idpId;
    private String recipientId;
    private String tranResultCode;
    private String tranResultMessage;
    private String appRefNum;
    private String msgType;

    public MessageResponse() {
    }

    public String getTranId() {
        return tranId;
    }

    public void setTranId(String tranId) {
        this.tranId = tranId;
    }

    public String getIdpId() {
        return idpId;
    }

    public void setIdpId(String idpId) {
        this.idpId = idpId;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    public String getTranResultCode() {
        return tranResultCode;
    }

    public void setTranResultCode(String tranResultCode) {
        this.tranResultCode = tranResultCode;
    }

    public String getTranResultMessage() {
        return tranResultMessage;
    }

    public void setTranResultMessage(String tranResultMessage) {
        this.tranResultMessage = tranResultMessage;
    }

    public String getAppRefNum() {
        return appRefNum;
    }

    public void setAppRefNum(String appRefNum) {
        this.appRefNum = appRefNum;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }
}
