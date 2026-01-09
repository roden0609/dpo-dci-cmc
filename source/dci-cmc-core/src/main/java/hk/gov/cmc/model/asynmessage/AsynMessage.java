package hk.gov.cmc.model.asynmessage;

import java.io.Serializable;

public class AsynMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    private String m_scopesMessageId;
    private String m_status;
    private String m_recipientAppId;
    private String m_recipientAppType;
    private String m_msgResponse;

    public String getScopesMessageId() {
        return m_scopesMessageId;
    }

    /**
     * set scope message id
     * 
     * @param scopesMessageId scope message id
     */
    public void setScopesMessageId(String scopesMessageId) {
        m_scopesMessageId = scopesMessageId;
    }

    /**
     * get status
     * 
     * @return status
     */
    public String getStatus() {
        return m_status;
    }

    /**
     * set status
     * 
     * @param status status
     */
    public void setStatus(String status) {
        m_status = status;
    }

    /**
     * get recipient application id
     * 
     * @return recipient application id
     */
    public String getRecipientAppId() {
        return m_recipientAppId;
    }

    /**
     * set recipient application id
     * 
     * @param recipientAppId recipient application id
     */
    public void setRecipientAppId(String recipientAppId) {
        m_recipientAppId = recipientAppId;
    }

    /**
     * get recipient application type
     * 
     * @return recipient application type
     */
    public String getRecipientAppType() {
        return m_recipientAppType;
    }

    /**
     * set recipient application type
     * 
     * @param recipientAppType recipient application type
     */
    public void setRecipientAppType(String recipientAppType) {
        m_recipientAppType = recipientAppType;
    }

    /**
     * get message response
     * 
     * @return message response
     */
    public String getMsgResponse() {
        return m_msgResponse;
    }

    /**
     * set message response
     * 
     * @param msgResponse message response
     */
    public void setMsgResponse(String msgResponse) {
        m_msgResponse = msgResponse;
    }

    @Override
    public String toString() {
        return "AsynMessage [m_scopesMessageId=" + m_scopesMessageId + ", m_status=" + m_status + ", m_recipientAppId="
                + m_recipientAppId + ", m_recipientAppType=" + m_recipientAppType + ", m_msgResponse=" + m_msgResponse
                + "]";
    }

}
