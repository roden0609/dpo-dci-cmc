package hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage;

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

public class Application_CT implements java.io.Serializable {

    private java.lang.String _templateID;
    private java.lang.String _templateVersion;

    public Application_CT() {
        super();
    }

    public java.lang.String getTemplateID() {
        return this._templateID;
    }

    public java.lang.String getTemplateVersion() {
        return this._templateVersion;
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

    public void setTemplateID(java.lang.String templateID) {
        this._templateID = templateID;
    }

    public void setTemplateVersion(java.lang.String templateVersion) {
        this._templateVersion = templateVersion;
    }

    public static hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.Application_CT unmarshal(java.io.Reader reader)
            throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.Application_CT) Unmarshaller
                .unmarshal(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.Application_CT.class, reader);
    }

    public void validate()
            throws org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    }

    @Override
    public String toString() {
        return "Application_CT [_templateID=" + _templateID + ", _templateVersion=" + _templateVersion + "]";
    }

}
