






package hk.gov.ogcio.mars_cmc.cmc.castor.retrievecmctemplate;

  
 


import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;






public class TemplateDetail_CT implements java.io.Serializable {


      
     
    

    


    private java.lang.String _subjectEn;

    


    private java.lang.String _subjectTc;

    


    private java.lang.String _subjectSc;

    


    private java.lang.String _contentEn;

    


    private java.lang.String _contentTc;

    


    private java.lang.String _contentSc;

    


    private java.lang.String _emailSubjectEn;

    


    private java.lang.String _emailSubjectTc;

    


    private java.lang.String _emailSubjectSc;

    


    private java.lang.String _emailContentEn;

    


    private java.lang.String _emailContentTc;

    


    private java.lang.String _emailContentSc;

    


    private java.lang.String _mobileSubjectEn;

    


    private java.lang.String _mobileSubjectTc;

    


    private java.lang.String _mobileSubjectSc;

    


    private java.lang.String _mobileContentEn;

    


    private java.lang.String _mobileContentTc;

    


    private java.lang.String _mobileContentSc;


      
     
    

    public TemplateDetail_CT() 
     {
        super();
    } 


      
     
    

    





    public java.lang.String getContentEn()
    {
        return this._contentEn;
    } 

    





    public java.lang.String getContentSc()
    {
        return this._contentSc;
    } 

    





    public java.lang.String getContentTc()
    {
        return this._contentTc;
    } 

    





    public java.lang.String getEmailContentEn()
    {
        return this._emailContentEn;
    } 

    





    public java.lang.String getEmailContentSc()
    {
        return this._emailContentSc;
    } 

    





    public java.lang.String getEmailContentTc()
    {
        return this._emailContentTc;
    } 

    





    public java.lang.String getEmailSubjectEn()
    {
        return this._emailSubjectEn;
    } 

    





    public java.lang.String getEmailSubjectSc()
    {
        return this._emailSubjectSc;
    } 

    





    public java.lang.String getEmailSubjectTc()
    {
        return this._emailSubjectTc;
    } 

    





    public java.lang.String getMobileContentEn()
    {
        return this._mobileContentEn;
    } 

    





    public java.lang.String getMobileContentSc()
    {
        return this._mobileContentSc;
    } 

    





    public java.lang.String getMobileContentTc()
    {
        return this._mobileContentTc;
    } 

    





    public java.lang.String getMobileSubjectEn()
    {
        return this._mobileSubjectEn;
    } 

    





    public java.lang.String getMobileSubjectSc()
    {
        return this._mobileSubjectSc;
    } 

    





    public java.lang.String getMobileSubjectTc()
    {
        return this._mobileSubjectTc;
    } 

    





    public java.lang.String getSubjectEn()
    {
        return this._subjectEn;
    } 

    





    public java.lang.String getSubjectSc()
    {
        return this._subjectSc;
    } 

    





    public java.lang.String getSubjectTc()
    {
        return this._subjectTc;
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

    




    public void setContentEn(java.lang.String contentEn)
    {
        this._contentEn = contentEn;
    } 

    




    public void setContentSc(java.lang.String contentSc)
    {
        this._contentSc = contentSc;
    } 

    




    public void setContentTc(java.lang.String contentTc)
    {
        this._contentTc = contentTc;
    } 

    




    public void setEmailContentEn(java.lang.String emailContentEn)
    {
        this._emailContentEn = emailContentEn;
    } 

    




    public void setEmailContentSc(java.lang.String emailContentSc)
    {
        this._emailContentSc = emailContentSc;
    } 

    




    public void setEmailContentTc(java.lang.String emailContentTc)
    {
        this._emailContentTc = emailContentTc;
    } 

    




    public void setEmailSubjectEn(java.lang.String emailSubjectEn)
    {
        this._emailSubjectEn = emailSubjectEn;
    } 

    




    public void setEmailSubjectSc(java.lang.String emailSubjectSc)
    {
        this._emailSubjectSc = emailSubjectSc;
    } 

    




    public void setEmailSubjectTc(java.lang.String emailSubjectTc)
    {
        this._emailSubjectTc = emailSubjectTc;
    } 

    




    public void setMobileContentEn(java.lang.String mobileContentEn)
    {
        this._mobileContentEn = mobileContentEn;
    } 

    




    public void setMobileContentSc(java.lang.String mobileContentSc)
    {
        this._mobileContentSc = mobileContentSc;
    } 

    




    public void setMobileContentTc(java.lang.String mobileContentTc)
    {
        this._mobileContentTc = mobileContentTc;
    } 

    




    public void setMobileSubjectEn(java.lang.String mobileSubjectEn)
    {
        this._mobileSubjectEn = mobileSubjectEn;
    } 

    




    public void setMobileSubjectSc(java.lang.String mobileSubjectSc)
    {
        this._mobileSubjectSc = mobileSubjectSc;
    } 

    




    public void setMobileSubjectTc(java.lang.String mobileSubjectTc)
    {
        this._mobileSubjectTc = mobileSubjectTc;
    } 

    




    public void setSubjectEn(java.lang.String subjectEn)
    {
        this._subjectEn = subjectEn;
    } 

    




    public void setSubjectSc(java.lang.String subjectSc)
    {
        this._subjectSc = subjectSc;
    } 

    




    public void setSubjectTc(java.lang.String subjectTc)
    {
        this._subjectTc = subjectTc;
    } 

    







    public static hk.gov.ogcio.mars_cmc.cmc.castor.retrievecmctemplate.TemplateDetail_CT unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (hk.gov.ogcio.mars_cmc.cmc.castor.retrievecmctemplate.TemplateDetail_CT) Unmarshaller.unmarshal(hk.gov.ogcio.mars_cmc.cmc.castor.retrievecmctemplate.TemplateDetail_CT.class, reader);
    } 

    



    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } 

}
