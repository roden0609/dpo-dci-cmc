/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.0</a>, using an XML
 * Schema.
 * $Id$
 */

package hk.gov.ogcio.mars_cmc.cmc.castor.retrievecmctemplate;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class RetrieveCmcTemplateRequest.
 * 
 * @version $Revision$ $Date$
 */
public class RetrieveCmcTemplateRequest implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _templateId
     */
    private java.lang.String _templateId;

    /**
     * Field _templateVersion
     */
    private java.lang.String _templateVersion;

    /**
     * Field _emailContentRequired
     */
    private java.lang.String _emailContentRequired;

    /**
     * Field _mobileContentRequired
     */
    private java.lang.String _mobileContentRequired;


      //----------------/
     //- Constructors -/
    //----------------/

    public RetrieveCmcTemplateRequest() 
     {
        super();
    } //-- hk.gov.ogcio.mars_cmc.cmc.castor.retrievecmctemplate.RetrieveCmcTemplateRequest()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'emailContentRequired'.
     * 
     * @return String
     * @return the value of field 'emailContentRequired'.
     */
    public java.lang.String getEmailContentRequired()
    {
        return this._emailContentRequired;
    } //-- java.lang.String getEmailContentRequired() 

    /**
     * Returns the value of field 'mobileContentRequired'.
     * 
     * @return String
     * @return the value of field 'mobileContentRequired'.
     */
    public java.lang.String getMobileContentRequired()
    {
        return this._mobileContentRequired;
    } //-- java.lang.String getMobileContentRequired() 

    /**
     * Returns the value of field 'templateId'.
     * 
     * @return String
     * @return the value of field 'templateId'.
     */
    public java.lang.String getTemplateId()
    {
        return this._templateId;
    } //-- java.lang.String getTemplateId() 

    /**
     * Returns the value of field 'templateVersion'.
     * 
     * @return String
     * @return the value of field 'templateVersion'.
     */
    public java.lang.String getTemplateVersion()
    {
        return this._templateVersion;
    } //-- java.lang.String getTemplateVersion() 

    /**
     * Method isValid
     * 
     * 
     * 
     * @return boolean
     */
    public boolean isValid()
    {
        try {
            validate();
        }
        catch (org.exolab.castor.xml.ValidationException vex) {
            return false;
        }
        return true;
    } //-- boolean isValid() 

    /**
     * Method marshal
     * 
     * 
     * 
     * @param out
     */
    public void marshal(java.io.Writer out)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, out);
    } //-- void marshal(java.io.Writer) 

    /**
     * Method marshal
     * 
     * 
     * 
     * @param handler
     */
    public void marshal(org.xml.sax.ContentHandler handler)
        throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, handler);
    } //-- void marshal(org.xml.sax.ContentHandler) 

    /**
     * Sets the value of field 'emailContentRequired'.
     * 
     * @param emailContentRequired the value of field
     * 'emailContentRequired'.
     */
    public void setEmailContentRequired(java.lang.String emailContentRequired)
    {
        this._emailContentRequired = emailContentRequired;
    } //-- void setEmailContentRequired(java.lang.String) 

    /**
     * Sets the value of field 'mobileContentRequired'.
     * 
     * @param mobileContentRequired the value of field
     * 'mobileContentRequired'.
     */
    public void setMobileContentRequired(java.lang.String mobileContentRequired)
    {
        this._mobileContentRequired = mobileContentRequired;
    } //-- void setMobileContentRequired(java.lang.String) 

    /**
     * Sets the value of field 'templateId'.
     * 
     * @param templateId the value of field 'templateId'.
     */
    public void setTemplateId(java.lang.String templateId)
    {
        this._templateId = templateId;
    } //-- void setTemplateId(java.lang.String) 

    /**
     * Sets the value of field 'templateVersion'.
     * 
     * @param templateVersion the value of field 'templateVersion'.
     */
    public void setTemplateVersion(java.lang.String templateVersion)
    {
        this._templateVersion = templateVersion;
    } //-- void setTemplateVersion(java.lang.String) 

    /**
     * Method unmarshal
     * 
     * 
     * 
     * @param reader
     * @return RetrieveCmcTemplateRequest
     */
    public static hk.gov.ogcio.mars_cmc.cmc.castor.retrievecmctemplate.RetrieveCmcTemplateRequest unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (hk.gov.ogcio.mars_cmc.cmc.castor.retrievecmctemplate.RetrieveCmcTemplateRequest) Unmarshaller.unmarshal(hk.gov.ogcio.mars_cmc.cmc.castor.retrievecmctemplate.RetrieveCmcTemplateRequest.class, reader);
    } //-- hk.gov.ogcio.mars_cmc.cmc.castor.retrievecmctemplate.RetrieveCmcTemplateRequest unmarshal(java.io.Reader) 

    /**
     * Method validate
     * 
     */
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
