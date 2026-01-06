






package hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage;






@SuppressWarnings("serial")
public class MessageResponse_CT implements java.io.Serializable {

    private java.lang.String tranID;

    private java.lang.String idpID;

    private java.lang.String recipientID;

    private java.lang.String tranResultCode;

    private java.lang.String tranResultMessage;

    private java.lang.String appRefNum;

    private java.lang.String msgType;

    public MessageResponse_CT() {
        super();
    }

    




    public java.lang.String getMsgType() {
        return this.msgType;
    }

    




    public java.lang.String getAppRefNum() {
        return this.appRefNum;
    }

    




    public java.lang.String getIdpID() {
        return this.idpID;
    }

    




    public java.lang.String getRecipientID() {
        return this.recipientID;
    }

    




    public java.lang.String getTranID() {
        return this.tranID;
    }

    




    public java.lang.String getTranResultCode() {
        return this.tranResultCode;
    }

    




    public java.lang.String getTranResultMessage() {
        return this.tranResultMessage;
    }

    




    public boolean isValid() {
        try {
            validate();
        } catch (org.exolab.castor.xml.ValidationException vex) {
            return false;
        }
        return true;
    }

    








    public void marshal(final java.io.Writer out) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Marshaller.marshal(this, out);
    }

    










    public void marshal(final org.xml.sax.ContentHandler handler) throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Marshaller.marshal(this, handler);
    }

    




    public void setMsgType(final java.lang.String msgType) {
        this.msgType = msgType;
    }

    




    public void setAppRefNum(final java.lang.String appRefNum) {
        this.appRefNum = appRefNum;
    }

    




    public void setIdpID(final java.lang.String idpID) {
        this.idpID = idpID;
    }

    




    public void setRecipientID(final java.lang.String recipientID) {
        this.recipientID = recipientID;
    }

    




    public void setTranID(final java.lang.String tranID) {
        this.tranID = tranID;
    }

    




    public void setTranResultCode(final java.lang.String tranResultCode) {
        this.tranResultCode = tranResultCode;
    }

    





    public void setTranResultMessage(final java.lang.String tranResultMessage) {
        this.tranResultMessage = tranResultMessage;
    }

    










    public static hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageResponse_CT unmarshal(final java.io.Reader reader) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageResponse_CT) org.exolab.castor.xml.Unmarshaller.unmarshal(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageResponse_CT.class, reader);
    }

    





    public void validate() throws org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    }

}
