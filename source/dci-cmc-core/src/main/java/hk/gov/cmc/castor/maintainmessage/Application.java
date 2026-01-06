package hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage;

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

public class Application extends Application_CT
        implements java.io.Serializable {

    public Application() {
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

    public void marshal(java.io.Writer out)
            throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {

        Marshaller.marshal(this, out);
    }

    public void marshal(org.xml.sax.ContentHandler handler)
            throws java.io.IOException, org.exolab.castor.xml.MarshalException,
            org.exolab.castor.xml.ValidationException {

        Marshaller.marshal(this, handler);
    }

    public static hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.Application_CT unmarshal(java.io.Reader reader)
            throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.Application_CT) Unmarshaller
                .unmarshal(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.Application.class, reader);
    }

    public void validate()
            throws org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    }

    @Override
    public String toString() {
        return super.toString() + ", Application []";
    }

}
