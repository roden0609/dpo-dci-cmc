
package hk.gov.cmc.castor.retrievecmctemplate;

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

public class RetrieveCmcTemplateRequest implements java.io.Serializable {

    private java.lang.String _templateId;

    private java.lang.String _templateVersion;

    private java.lang.String _emailContentRequired;

    private java.lang.String _mobileContentRequired;

    public RetrieveCmcTemplateRequest()
    {
        super();
    }

    public java.lang.String getEmailContentRequired()
    {
        return this._emailContentRequired;
    }

    public java.lang.String getMobileContentRequired()
    {
        return this._mobileContentRequired;
    }

    public java.lang.String getTemplateId()
    {
        return this._templateId;
    }

    public java.lang.String getTemplateVersion()
    {
        return this._templateVersion;
    }

    public boolean isValid()
    {
        try {
            validate();
        }
        catch (org.exolab.castor.xml.ValidationException vex) {
            return false;
        }
        return true;
    }

    public void marshal(java.io.Writer out)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {

        Marshaller.marshal(this, out);
    }

    public void marshal(org.xml.sax.ContentHandler handler)
    throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {

        Marshaller.marshal(this, handler);
    }

    public void setEmailContentRequired(java.lang.String emailContentRequired)
    {
        this._emailContentRequired = emailContentRequired;
    }

    public void setMobileContentRequired(java.lang.String mobileContentRequired)
    {
        this._mobileContentRequired = mobileContentRequired;
    }

    public void setTemplateId(java.lang.String templateId)
    {
        this._templateId = templateId;
    }

    public void setTemplateVersion(java.lang.String templateVersion)
    {
        this._templateVersion = templateVersion;
    }

    public static hk.gov.ogcio.mars_cmc.cmc.castor.retrievecmctemplate.RetrieveCmcTemplateRequest unmarshal(java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (hk.gov.ogcio.mars_cmc.cmc.castor.retrievecmctemplate.RetrieveCmcTemplateRequest) Unmarshaller.unmarshal(hk.gov.ogcio.mars_cmc.cmc.castor.retrievecmctemplate.RetrieveCmcTemplateRequest.class, reader);
    }

    public void validate()
    throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    }

}
