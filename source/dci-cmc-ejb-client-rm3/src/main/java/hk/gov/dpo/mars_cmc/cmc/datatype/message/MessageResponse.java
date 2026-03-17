package hk.gov.dpo.mars_cmc.cmc.datatype.message;

import java.io.Serializable;

public class MessageResponse implements Serializable {

    private static final long serialVersionUID = 1L;
    private String m_tranID;
    private String m_idpID;
    private String m_recipientID;
    private String m_tranResultCode;
    private String m_tranResultMessage;
    private String m_msgType;

    public String getTranID() {
        return m_tranID;
    }

    public void setTranID(String tranID) {
        m_tranID = tranID;
    }

    public String getIdpID() {
        return m_idpID;
    }

    public void setIdpID(String idpID) {
        m_idpID = idpID;
    }

    public String getRecipientID() {
        return m_recipientID;
    }

    public void setRecipientID(String recipientID) {
        m_recipientID = recipientID;
    }

    public String getTranResultCode() {
        return m_tranResultCode;
    }

    public void setTranResultCode(String tranResultCode) {
        m_tranResultCode = tranResultCode;
    }

    public String getTranResultMessage() {
        return m_tranResultMessage;
    }

    public void setTranResultMessage(String tranResultMessage) {
        m_tranResultMessage = tranResultMessage;
    }

    public String getMsgType() {
        return m_msgType;
    }

    public void setMsgType(String msgType) {
        m_msgType = msgType;
    }

}
