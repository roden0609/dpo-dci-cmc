
package hk.gov.cmc.castor.maintainmessage;

@SuppressWarnings("serial")
public class MessageResponse extends MessageResponse_CT
implements java.io.Serializable
{

    public MessageResponse() {
        super();
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

    public static hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageResponse unmarshal(final java.io.Reader reader) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageResponse) org.exolab.castor.xml.Unmarshaller.unmarshal(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageResponse.class, reader);
    }

    public void validate() throws org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    }

}
