






package hk.gov.ogcio.mars_cmc.cmc.castor.maintaintemplate;

  
 


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






public class MaintainTemplateResponse implements java.io.Serializable {


      
     
    

    


    private hk.gov.ogcio.mars_cmc.cmc.castor.maintaintemplate.types.Result _resultCd;

    


    private java.lang.String _resultMsg;

    


    private java.lang.String _action;

    


    private java.lang.String _templateId;

    


    private java.lang.String _templateVersion;


      
     
    

    public MaintainTemplateResponse() 
     {
        super();
    } 


      
     
    

    





    public java.lang.String getAction()
    {
        return this._action;
    } 

    





    public hk.gov.ogcio.mars_cmc.cmc.castor.maintaintemplate.types.Result getResultCd()
    {
        return this._resultCd;
    } 

    





    public java.lang.String getResultMsg()
    {
        return this._resultMsg;
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

    




    public void setAction(java.lang.String action)
    {
        this._action = action;
    } 

    




    public void setResultCd(hk.gov.ogcio.mars_cmc.cmc.castor.maintaintemplate.types.Result resultCd)
    {
        this._resultCd = resultCd;
    } 

    




    public void setResultMsg(java.lang.String resultMsg)
    {
        this._resultMsg = resultMsg;
    } 

    




    public void setTemplateId(java.lang.String templateId)
    {
        this._templateId = templateId;
    } 

    




    public void setTemplateVersion(java.lang.String templateVersion)
    {
        this._templateVersion = templateVersion;
    } 

    







    public static hk.gov.ogcio.mars_cmc.cmc.castor.maintaintemplate.MaintainTemplateResponse unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (hk.gov.ogcio.mars_cmc.cmc.castor.maintaintemplate.MaintainTemplateResponse) Unmarshaller.unmarshal(hk.gov.ogcio.mars_cmc.cmc.castor.maintaintemplate.MaintainTemplateResponse.class, reader);
    } 

    



    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } 

}
