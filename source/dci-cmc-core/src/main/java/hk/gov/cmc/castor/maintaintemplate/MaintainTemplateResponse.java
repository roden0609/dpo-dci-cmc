/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.0</a>, using an XML
 * Schema.
 * $Id$
 */

package hk.gov.ogcio.mars_cmc.cmc.castor.maintaintemplate;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import hk.gov.ogcio.mars_cmc.cmc.castor.maintaintemplate.types.Result;
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
 * Class MaintainTemplateResponse.
 * 
 * @version $Revision$ $Date$
 */
public class MaintainTemplateResponse implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _resultCd
     */
    private hk.gov.ogcio.mars_cmc.cmc.castor.maintaintemplate.types.Result _resultCd;

    /**
     * Field _resultMsg
     */
    private java.lang.String _resultMsg;

    /**
     * Field _action
     */
    private java.lang.String _action;

    /**
     * Field _templateId
     */
    private java.lang.String _templateId;

    /**
     * Field _templateVersion
     */
    private java.lang.String _templateVersion;


      //----------------/
     //- Constructors -/
    //----------------/

    public MaintainTemplateResponse() 
     {
        super();
    } //-- hk.gov.ogcio.mars_cmc.cmc.castor.maintaintemplate.MaintainTemplateResponse()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'action'.
     * 
     * @return String
     * @return the value of field 'action'.
     */
    public java.lang.String getAction()
    {
        return this._action;
    } //-- java.lang.String getAction() 

    /**
     * Returns the value of field 'resultCd'.
     * 
     * @return Result
     * @return the value of field 'resultCd'.
     */
    public hk.gov.ogcio.mars_cmc.cmc.castor.maintaintemplate.types.Result getResultCd()
    {
        return this._resultCd;
    } //-- hk.gov.ogcio.mars_cmc.cmc.castor.maintaintemplate.types.Result getResultCd() 

    /**
     * Returns the value of field 'resultMsg'.
     * 
     * @return String
     * @return the value of field 'resultMsg'.
     */
    public java.lang.String getResultMsg()
    {
        return this._resultMsg;
    } //-- java.lang.String getResultMsg() 

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
     * Sets the value of field 'action'.
     * 
     * @param action the value of field 'action'.
     */
    public void setAction(java.lang.String action)
    {
        this._action = action;
    } //-- void setAction(java.lang.String) 

    /**
     * Sets the value of field 'resultCd'.
     * 
     * @param resultCd the value of field 'resultCd'.
     */
    public void setResultCd(hk.gov.ogcio.mars_cmc.cmc.castor.maintaintemplate.types.Result resultCd)
    {
        this._resultCd = resultCd;
    } //-- void setResultCd(hk.gov.ogcio.mars_cmc.cmc.castor.maintaintemplate.types.Result) 

    /**
     * Sets the value of field 'resultMsg'.
     * 
     * @param resultMsg the value of field 'resultMsg'.
     */
    public void setResultMsg(java.lang.String resultMsg)
    {
        this._resultMsg = resultMsg;
    } //-- void setResultMsg(java.lang.String) 

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
     * @return MaintainTemplateResponse
     */
    public static hk.gov.ogcio.mars_cmc.cmc.castor.maintaintemplate.MaintainTemplateResponse unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (hk.gov.ogcio.mars_cmc.cmc.castor.maintaintemplate.MaintainTemplateResponse) Unmarshaller.unmarshal(hk.gov.ogcio.mars_cmc.cmc.castor.maintaintemplate.MaintainTemplateResponse.class, reader);
    } //-- hk.gov.ogcio.mars_cmc.cmc.castor.maintaintemplate.MaintainTemplateResponse unmarshal(java.io.Reader) 

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
