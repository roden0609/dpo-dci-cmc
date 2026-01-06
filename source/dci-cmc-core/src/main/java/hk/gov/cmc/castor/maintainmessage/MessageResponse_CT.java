/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.4.1</a>, using an XML
 * Schema.
 * $Id$
 */

package hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage;

/**
 * 
 * 
 * @version $Revision$ $Date$
 */
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

    /**
     * Returns the value of field 'msgType'.
     * 
     * @return the value of field 'MsgType'.
     */
    public java.lang.String getMsgType() {
        return this.msgType;
    }

    /**
     * Returns the value of field 'appRefNum'.
     * 
     * @return the value of field 'AppRefNum'.
     */
    public java.lang.String getAppRefNum() {
        return this.appRefNum;
    }

    /**
     * Returns the value of field 'idpID'.
     * 
     * @return the value of field 'IdpID'.
     */
    public java.lang.String getIdpID() {
        return this.idpID;
    }

    /**
     * Returns the value of field 'recipientID'.
     * 
     * @return the value of field 'RecipientID'.
     */
    public java.lang.String getRecipientID() {
        return this.recipientID;
    }

    /**
     * Returns the value of field 'tranID'.
     * 
     * @return the value of field 'TranID'.
     */
    public java.lang.String getTranID() {
        return this.tranID;
    }

    /**
     * Returns the value of field 'tranResultCode'.
     * 
     * @return the value of field 'TranResultCode'.
     */
    public java.lang.String getTranResultCode() {
        return this.tranResultCode;
    }

    /**
     * Returns the value of field 'tranResultMessage'.
     * 
     * @return the value of field 'TranResultMessage'.
     */
    public java.lang.String getTranResultMessage() {
        return this.tranResultMessage;
    }

    /**
     * Method isValid.
     * 
     * @return true if this object is valid according to the schema
     */
    public boolean isValid() {
        try {
            validate();
        } catch (org.exolab.castor.xml.ValidationException vex) {
            return false;
        }
        return true;
    }

    /**
     * 
     * 
     * @param out
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     */
    public void marshal(final java.io.Writer out) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Marshaller.marshal(this, out);
    }

    /**
     * 
     * 
     * @param handler
     * @throws java.io.IOException if an IOException occurs during
     * marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     */
    public void marshal(final org.xml.sax.ContentHandler handler) throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Marshaller.marshal(this, handler);
    }

    /**
     * Sets the value of field 'msgType'.
     * 
     * @param msgType the value of field 'msgType'.
     */
    public void setMsgType(final java.lang.String msgType) {
        this.msgType = msgType;
    }

    /**
     * Sets the value of field 'appRefNum'.
     * 
     * @param appRefNum the value of field 'appRefNum'.
     */
    public void setAppRefNum(final java.lang.String appRefNum) {
        this.appRefNum = appRefNum;
    }

    /**
     * Sets the value of field 'idpID'.
     * 
     * @param idpID the value of field 'idpID'.
     */
    public void setIdpID(final java.lang.String idpID) {
        this.idpID = idpID;
    }

    /**
     * Sets the value of field 'recipientID'.
     * 
     * @param recipientID the value of field 'recipientID'.
     */
    public void setRecipientID(final java.lang.String recipientID) {
        this.recipientID = recipientID;
    }

    /**
     * Sets the value of field 'tranID'.
     * 
     * @param tranID the value of field 'tranID'.
     */
    public void setTranID(final java.lang.String tranID) {
        this.tranID = tranID;
    }

    /**
     * Sets the value of field 'tranResultCode'.
     * 
     * @param tranResultCode the value of field 'tranResultCode'.
     */
    public void setTranResultCode(final java.lang.String tranResultCode) {
        this.tranResultCode = tranResultCode;
    }

    /**
     * Sets the value of field 'tranResultMessage'.
     * 
     * @param tranResultMessage the value of field
     * 'tranResultMessage'.
     */
    public void setTranResultMessage(final java.lang.String tranResultMessage) {
        this.tranResultMessage = tranResultMessage;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled
     * hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageResponse_CT
     */
    public static hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageResponse_CT unmarshal(final java.io.Reader reader) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageResponse_CT) org.exolab.castor.xml.Unmarshaller.unmarshal(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageResponse_CT.class, reader);
    }

    /**
     * 
     * 
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     */
    public void validate() throws org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    }

}
