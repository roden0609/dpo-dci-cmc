






package hk.gov.ogcio.mars_cmc.cmc.castor.retrievecmctemplate;

  
 


import hk.gov.ogcio.mars_cmc.cmc.castor.retrievecmctemplate.types.Result;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;






public class RetrieveCmcTemplateResponse implements java.io.Serializable {


      
     
    

    


    private hk.gov.ogcio.mars_cmc.cmc.castor.retrievecmctemplate.types.Result _resultCd;

    


    private java.lang.String _resultMsg;

    


    private java.lang.String _templateId;

    


    private java.lang.String _templateVersion;

    


    private hk.gov.ogcio.mars_cmc.cmc.castor.retrievecmctemplate.TemplateDetail _templateDetail;


      
     
    

    public RetrieveCmcTemplateResponse() 
     {
        super();
    } 


      
     
    

    





    public hk.gov.ogcio.mars_cmc.cmc.castor.retrievecmctemplate.types.Result getResultCd()
    {
        return this._resultCd;
    } 

    





    public java.lang.String getResultMsg()
    {
        return this._resultMsg;
    } 

    





    public hk.gov.ogcio.mars_cmc.cmc.castor.retrievecmctemplate.TemplateDetail getTemplateDetail()
    {
        return this._templateDetail;
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

    




    public void setResultCd(hk.gov.ogcio.mars_cmc.cmc.castor.retrievecmctemplate.types.Result resultCd)
    {
        this._resultCd = resultCd;
    } 

    




    public void setResultMsg(java.lang.String resultMsg)
    {
        this._resultMsg = resultMsg;
    } 

    




    public void setTemplateDetail(hk.gov.ogcio.mars_cmc.cmc.castor.retrievecmctemplate.TemplateDetail templateDetail)
    {
        this._templateDetail = templateDetail;
    } 

    




    public void setTemplateId(java.lang.String templateId)
    {
        this._templateId = templateId;
    } 

    




    public void setTemplateVersion(java.lang.String templateVersion)
    {
        this._templateVersion = templateVersion;
    } 

    







    public static hk.gov.ogcio.mars_cmc.cmc.castor.retrievecmctemplate.RetrieveCmcTemplateResponse unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (hk.gov.ogcio.mars_cmc.cmc.castor.retrievecmctemplate.RetrieveCmcTemplateResponse) Unmarshaller.unmarshal(hk.gov.ogcio.mars_cmc.cmc.castor.retrievecmctemplate.RetrieveCmcTemplateResponse.class, reader);
    } 

    



    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } 

}
