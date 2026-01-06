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
public class MaintainMessageResponse implements java.io.Serializable {

    private java.lang.String resultCode;

    private java.lang.String resultMessage;

    private java.util.Vector<hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageResponse> messageResponseList;

    public MaintainMessageResponse() {
        super();
        this.messageResponseList = new java.util.Vector<hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageResponse>();
    }

    /**
     * 
     * 
     * @param vMessageResponse
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addMessageResponse(final hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageResponse vMessageResponse) throws java.lang.IndexOutOfBoundsException {
        this.messageResponseList.addElement(vMessageResponse);
    }

    /**
     * 
     * 
     * @param index
     * @param vMessageResponse
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addMessageResponse(final int index,final hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageResponse vMessageResponse) throws java.lang.IndexOutOfBoundsException {
        this.messageResponseList.add(index, vMessageResponse);
    }

    /**
     * Method enumerateMessageResponse.
     * 
     * @return an Enumeration over all
     * hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageResponse
     * elements
     */
    public java.util.Enumeration<? extends hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageResponse> enumerateMessageResponse() {
        return this.messageResponseList.elements();
    }

    /**
     * Method getMessageResponse.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageResponse
     * at the given index
     */
    public hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageResponse getMessageResponse(final int index) throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this.messageResponseList.size()) {
            throw new IndexOutOfBoundsException("getMessageResponse: Index value '" + index + "' not in range [0.." + (this.messageResponseList.size() - 1) + "]");
        }

        return messageResponseList.get(index);
    }

    /**
     * Method getMessageResponse.Returns the contents of the
     * collection in an Array.  <p>Note:  Just in case the
     * collection contents are changing in another thread, we pass
     * a 0-length Array of the correct type into the API call. 
     * This way we <i>know</i> that the Array returned is of
     * exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageResponse[] getMessageResponse() {
        hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageResponse[] array = new hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageResponse[0];
        return this.messageResponseList.toArray(array);
    }

    /**
     * Method getMessageResponseCount.
     * 
     * @return the size of this collection
     */
    public int getMessageResponseCount() {
        return this.messageResponseList.size();
    }

    /**
     * Returns the value of field 'resultCode'.
     * 
     * @return the value of field 'ResultCode'.
     */
    public java.lang.String getResultCode() {
        return this.resultCode;
    }

    /**
     * Returns the value of field 'resultMessage'.
     * 
     * @return the value of field 'ResultMessage'.
     */
    public java.lang.String getResultMessage() {
        return this.resultMessage;
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
     */
    public void removeAllMessageResponse() {
        this.messageResponseList.clear();
    }

    /**
     * Method removeMessageResponse.
     * 
     * @param vMessageResponse
     * @return true if the object was removed from the collection.
     */
    public boolean removeMessageResponse(final hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageResponse vMessageResponse) {
        boolean removed = messageResponseList.remove(vMessageResponse);
        return removed;
    }

    /**
     * Method removeMessageResponseAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageResponse removeMessageResponseAt(final int index) {
        java.lang.Object obj = this.messageResponseList.remove(index);
        return (hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageResponse) obj;
    }

    /**
     * 
     * 
     * @param index
     * @param vMessageResponse
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setMessageResponse(final int index,final hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageResponse vMessageResponse) throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this.messageResponseList.size()) {
            throw new IndexOutOfBoundsException("setMessageResponse: Index value '" + index + "' not in range [0.." + (this.messageResponseList.size() - 1) + "]");
        }

        this.messageResponseList.set(index, vMessageResponse);
    }

    /**
     * 
     * 
     * @param vMessageResponseArray
     */
    public void setMessageResponse(final hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageResponse[] vMessageResponseArray) {
        //-- copy array
        messageResponseList.clear();

        for (int i = 0; i < vMessageResponseArray.length; i++) {
                this.messageResponseList.add(vMessageResponseArray[i]);
        }
    }

    /**
     * Sets the value of field 'resultCode'.
     * 
     * @param resultCode the value of field 'resultCode'.
     */
    public void setResultCode(final java.lang.String resultCode) {
        this.resultCode = resultCode;
    }

    /**
     * Sets the value of field 'resultMessage'.
     * 
     * @param resultMessage the value of field 'resultMessage'.
     */
    public void setResultMessage(final java.lang.String resultMessage) {
        this.resultMessage = resultMessage;
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
     * hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MaintainMessageResponse
     */
    public static hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MaintainMessageResponse unmarshal(final java.io.Reader reader) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MaintainMessageResponse) org.exolab.castor.xml.Unmarshaller.unmarshal(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MaintainMessageResponse.class, reader);
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
