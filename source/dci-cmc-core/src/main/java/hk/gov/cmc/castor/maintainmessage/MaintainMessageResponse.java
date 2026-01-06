






package hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage;






@SuppressWarnings("serial")
public class MaintainMessageResponse implements java.io.Serializable {

    private java.lang.String resultCode;

    private java.lang.String resultMessage;

    private java.util.Vector<hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageResponse> messageResponseList;

    public MaintainMessageResponse() {
        super();
        this.messageResponseList = new java.util.Vector<hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageResponse>();
    }

    






    public void addMessageResponse(final hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageResponse vMessageResponse) throws java.lang.IndexOutOfBoundsException {
        this.messageResponseList.addElement(vMessageResponse);
    }

    







    public void addMessageResponse(final int index,final hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageResponse vMessageResponse) throws java.lang.IndexOutOfBoundsException {
        this.messageResponseList.add(index, vMessageResponse);
    }

    






    public java.util.Enumeration<? extends hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageResponse> enumerateMessageResponse() {
        return this.messageResponseList.elements();
    }

    









    public hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageResponse getMessageResponse(final int index) throws java.lang.IndexOutOfBoundsException {
        
        if (index < 0 || index >= this.messageResponseList.size()) {
            throw new IndexOutOfBoundsException("getMessageResponse: Index value '" + index + "' not in range [0.." + (this.messageResponseList.size() - 1) + "]");
        }

        return messageResponseList.get(index);
    }

    









    public hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageResponse[] getMessageResponse() {
        hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageResponse[] array = new hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageResponse[0];
        return this.messageResponseList.toArray(array);
    }

    




    public int getMessageResponseCount() {
        return this.messageResponseList.size();
    }

    




    public java.lang.String getResultCode() {
        return this.resultCode;
    }

    




    public java.lang.String getResultMessage() {
        return this.resultMessage;
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

    

    public void removeAllMessageResponse() {
        this.messageResponseList.clear();
    }

    





    public boolean removeMessageResponse(final hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageResponse vMessageResponse) {
        boolean removed = messageResponseList.remove(vMessageResponse);
        return removed;
    }

    





    public hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageResponse removeMessageResponseAt(final int index) {
        java.lang.Object obj = this.messageResponseList.remove(index);
        return (hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageResponse) obj;
    }

    







    public void setMessageResponse(final int index,final hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageResponse vMessageResponse) throws java.lang.IndexOutOfBoundsException {
        
        if (index < 0 || index >= this.messageResponseList.size()) {
            throw new IndexOutOfBoundsException("setMessageResponse: Index value '" + index + "' not in range [0.." + (this.messageResponseList.size() - 1) + "]");
        }

        this.messageResponseList.set(index, vMessageResponse);
    }

    




    public void setMessageResponse(final hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageResponse[] vMessageResponseArray) {
        
        messageResponseList.clear();

        for (int i = 0; i < vMessageResponseArray.length; i++) {
                this.messageResponseList.add(vMessageResponseArray[i]);
        }
    }

    




    public void setResultCode(final java.lang.String resultCode) {
        this.resultCode = resultCode;
    }

    




    public void setResultMessage(final java.lang.String resultMessage) {
        this.resultMessage = resultMessage;
    }

    










    public static hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MaintainMessageResponse unmarshal(final java.io.Reader reader) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MaintainMessageResponse) org.exolab.castor.xml.Unmarshaller.unmarshal(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MaintainMessageResponse.class, reader);
    }

    





    public void validate() throws org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    }

}
