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
 * Class TemplateDetail_CT.
 * 
 * @version $Revision$ $Date$
 */
public class TemplateDetail_CT implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _subjectEn
     */
    private java.lang.String _subjectEn;

    /**
     * Field _subjectTc
     */
    private java.lang.String _subjectTc;

    /**
     * Field _subjectSc
     */
    private java.lang.String _subjectSc;

    /**
     * Field _contentEn
     */
    private java.lang.String _contentEn;

    /**
     * Field _contentTc
     */
    private java.lang.String _contentTc;

    /**
     * Field _contentSc
     */
    private java.lang.String _contentSc;

    /**
     * Field _emailSubjectEn
     */
    private java.lang.String _emailSubjectEn;

    /**
     * Field _emailSubjectTc
     */
    private java.lang.String _emailSubjectTc;

    /**
     * Field _emailSubjectSc
     */
    private java.lang.String _emailSubjectSc;

    /**
     * Field _emailContentEn
     */
    private java.lang.String _emailContentEn;

    /**
     * Field _emailContentTc
     */
    private java.lang.String _emailContentTc;

    /**
     * Field _emailContentSc
     */
    private java.lang.String _emailContentSc;

    /**
     * Field _mobileSubjectEn
     */
    private java.lang.String _mobileSubjectEn;

    /**
     * Field _mobileSubjectTc
     */
    private java.lang.String _mobileSubjectTc;

    /**
     * Field _mobileSubjectSc
     */
    private java.lang.String _mobileSubjectSc;

    /**
     * Field _mobileContentEn
     */
    private java.lang.String _mobileContentEn;

    /**
     * Field _mobileContentTc
     */
    private java.lang.String _mobileContentTc;

    /**
     * Field _mobileContentSc
     */
    private java.lang.String _mobileContentSc;


      //----------------/
     //- Constructors -/
    //----------------/

    public TemplateDetail_CT() 
     {
        super();
    } //-- hk.gov.ogcio.mars_cmc.cmc.castor.retrievecmctemplate.TemplateDetail_CT()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'contentEn'.
     * 
     * @return String
     * @return the value of field 'contentEn'.
     */
    public java.lang.String getContentEn()
    {
        return this._contentEn;
    } //-- java.lang.String getContentEn() 

    /**
     * Returns the value of field 'contentSc'.
     * 
     * @return String
     * @return the value of field 'contentSc'.
     */
    public java.lang.String getContentSc()
    {
        return this._contentSc;
    } //-- java.lang.String getContentSc() 

    /**
     * Returns the value of field 'contentTc'.
     * 
     * @return String
     * @return the value of field 'contentTc'.
     */
    public java.lang.String getContentTc()
    {
        return this._contentTc;
    } //-- java.lang.String getContentTc() 

    /**
     * Returns the value of field 'emailContentEn'.
     * 
     * @return String
     * @return the value of field 'emailContentEn'.
     */
    public java.lang.String getEmailContentEn()
    {
        return this._emailContentEn;
    } //-- java.lang.String getEmailContentEn() 

    /**
     * Returns the value of field 'emailContentSc'.
     * 
     * @return String
     * @return the value of field 'emailContentSc'.
     */
    public java.lang.String getEmailContentSc()
    {
        return this._emailContentSc;
    } //-- java.lang.String getEmailContentSc() 

    /**
     * Returns the value of field 'emailContentTc'.
     * 
     * @return String
     * @return the value of field 'emailContentTc'.
     */
    public java.lang.String getEmailContentTc()
    {
        return this._emailContentTc;
    } //-- java.lang.String getEmailContentTc() 

    /**
     * Returns the value of field 'emailSubjectEn'.
     * 
     * @return String
     * @return the value of field 'emailSubjectEn'.
     */
    public java.lang.String getEmailSubjectEn()
    {
        return this._emailSubjectEn;
    } //-- java.lang.String getEmailSubjectEn() 

    /**
     * Returns the value of field 'emailSubjectSc'.
     * 
     * @return String
     * @return the value of field 'emailSubjectSc'.
     */
    public java.lang.String getEmailSubjectSc()
    {
        return this._emailSubjectSc;
    } //-- java.lang.String getEmailSubjectSc() 

    /**
     * Returns the value of field 'emailSubjectTc'.
     * 
     * @return String
     * @return the value of field 'emailSubjectTc'.
     */
    public java.lang.String getEmailSubjectTc()
    {
        return this._emailSubjectTc;
    } //-- java.lang.String getEmailSubjectTc() 

    /**
     * Returns the value of field 'mobileContentEn'.
     * 
     * @return String
     * @return the value of field 'mobileContentEn'.
     */
    public java.lang.String getMobileContentEn()
    {
        return this._mobileContentEn;
    } //-- java.lang.String getMobileContentEn() 

    /**
     * Returns the value of field 'mobileContentSc'.
     * 
     * @return String
     * @return the value of field 'mobileContentSc'.
     */
    public java.lang.String getMobileContentSc()
    {
        return this._mobileContentSc;
    } //-- java.lang.String getMobileContentSc() 

    /**
     * Returns the value of field 'mobileContentTc'.
     * 
     * @return String
     * @return the value of field 'mobileContentTc'.
     */
    public java.lang.String getMobileContentTc()
    {
        return this._mobileContentTc;
    } //-- java.lang.String getMobileContentTc() 

    /**
     * Returns the value of field 'mobileSubjectEn'.
     * 
     * @return String
     * @return the value of field 'mobileSubjectEn'.
     */
    public java.lang.String getMobileSubjectEn()
    {
        return this._mobileSubjectEn;
    } //-- java.lang.String getMobileSubjectEn() 

    /**
     * Returns the value of field 'mobileSubjectSc'.
     * 
     * @return String
     * @return the value of field 'mobileSubjectSc'.
     */
    public java.lang.String getMobileSubjectSc()
    {
        return this._mobileSubjectSc;
    } //-- java.lang.String getMobileSubjectSc() 

    /**
     * Returns the value of field 'mobileSubjectTc'.
     * 
     * @return String
     * @return the value of field 'mobileSubjectTc'.
     */
    public java.lang.String getMobileSubjectTc()
    {
        return this._mobileSubjectTc;
    } //-- java.lang.String getMobileSubjectTc() 

    /**
     * Returns the value of field 'subjectEn'.
     * 
     * @return String
     * @return the value of field 'subjectEn'.
     */
    public java.lang.String getSubjectEn()
    {
        return this._subjectEn;
    } //-- java.lang.String getSubjectEn() 

    /**
     * Returns the value of field 'subjectSc'.
     * 
     * @return String
     * @return the value of field 'subjectSc'.
     */
    public java.lang.String getSubjectSc()
    {
        return this._subjectSc;
    } //-- java.lang.String getSubjectSc() 

    /**
     * Returns the value of field 'subjectTc'.
     * 
     * @return String
     * @return the value of field 'subjectTc'.
     */
    public java.lang.String getSubjectTc()
    {
        return this._subjectTc;
    } //-- java.lang.String getSubjectTc() 

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
     * Sets the value of field 'contentEn'.
     * 
     * @param contentEn the value of field 'contentEn'.
     */
    public void setContentEn(java.lang.String contentEn)
    {
        this._contentEn = contentEn;
    } //-- void setContentEn(java.lang.String) 

    /**
     * Sets the value of field 'contentSc'.
     * 
     * @param contentSc the value of field 'contentSc'.
     */
    public void setContentSc(java.lang.String contentSc)
    {
        this._contentSc = contentSc;
    } //-- void setContentSc(java.lang.String) 

    /**
     * Sets the value of field 'contentTc'.
     * 
     * @param contentTc the value of field 'contentTc'.
     */
    public void setContentTc(java.lang.String contentTc)
    {
        this._contentTc = contentTc;
    } //-- void setContentTc(java.lang.String) 

    /**
     * Sets the value of field 'emailContentEn'.
     * 
     * @param emailContentEn the value of field 'emailContentEn'.
     */
    public void setEmailContentEn(java.lang.String emailContentEn)
    {
        this._emailContentEn = emailContentEn;
    } //-- void setEmailContentEn(java.lang.String) 

    /**
     * Sets the value of field 'emailContentSc'.
     * 
     * @param emailContentSc the value of field 'emailContentSc'.
     */
    public void setEmailContentSc(java.lang.String emailContentSc)
    {
        this._emailContentSc = emailContentSc;
    } //-- void setEmailContentSc(java.lang.String) 

    /**
     * Sets the value of field 'emailContentTc'.
     * 
     * @param emailContentTc the value of field 'emailContentTc'.
     */
    public void setEmailContentTc(java.lang.String emailContentTc)
    {
        this._emailContentTc = emailContentTc;
    } //-- void setEmailContentTc(java.lang.String) 

    /**
     * Sets the value of field 'emailSubjectEn'.
     * 
     * @param emailSubjectEn the value of field 'emailSubjectEn'.
     */
    public void setEmailSubjectEn(java.lang.String emailSubjectEn)
    {
        this._emailSubjectEn = emailSubjectEn;
    } //-- void setEmailSubjectEn(java.lang.String) 

    /**
     * Sets the value of field 'emailSubjectSc'.
     * 
     * @param emailSubjectSc the value of field 'emailSubjectSc'.
     */
    public void setEmailSubjectSc(java.lang.String emailSubjectSc)
    {
        this._emailSubjectSc = emailSubjectSc;
    } //-- void setEmailSubjectSc(java.lang.String) 

    /**
     * Sets the value of field 'emailSubjectTc'.
     * 
     * @param emailSubjectTc the value of field 'emailSubjectTc'.
     */
    public void setEmailSubjectTc(java.lang.String emailSubjectTc)
    {
        this._emailSubjectTc = emailSubjectTc;
    } //-- void setEmailSubjectTc(java.lang.String) 

    /**
     * Sets the value of field 'mobileContentEn'.
     * 
     * @param mobileContentEn the value of field 'mobileContentEn'.
     */
    public void setMobileContentEn(java.lang.String mobileContentEn)
    {
        this._mobileContentEn = mobileContentEn;
    } //-- void setMobileContentEn(java.lang.String) 

    /**
     * Sets the value of field 'mobileContentSc'.
     * 
     * @param mobileContentSc the value of field 'mobileContentSc'.
     */
    public void setMobileContentSc(java.lang.String mobileContentSc)
    {
        this._mobileContentSc = mobileContentSc;
    } //-- void setMobileContentSc(java.lang.String) 

    /**
     * Sets the value of field 'mobileContentTc'.
     * 
     * @param mobileContentTc the value of field 'mobileContentTc'.
     */
    public void setMobileContentTc(java.lang.String mobileContentTc)
    {
        this._mobileContentTc = mobileContentTc;
    } //-- void setMobileContentTc(java.lang.String) 

    /**
     * Sets the value of field 'mobileSubjectEn'.
     * 
     * @param mobileSubjectEn the value of field 'mobileSubjectEn'.
     */
    public void setMobileSubjectEn(java.lang.String mobileSubjectEn)
    {
        this._mobileSubjectEn = mobileSubjectEn;
    } //-- void setMobileSubjectEn(java.lang.String) 

    /**
     * Sets the value of field 'mobileSubjectSc'.
     * 
     * @param mobileSubjectSc the value of field 'mobileSubjectSc'.
     */
    public void setMobileSubjectSc(java.lang.String mobileSubjectSc)
    {
        this._mobileSubjectSc = mobileSubjectSc;
    } //-- void setMobileSubjectSc(java.lang.String) 

    /**
     * Sets the value of field 'mobileSubjectTc'.
     * 
     * @param mobileSubjectTc the value of field 'mobileSubjectTc'.
     */
    public void setMobileSubjectTc(java.lang.String mobileSubjectTc)
    {
        this._mobileSubjectTc = mobileSubjectTc;
    } //-- void setMobileSubjectTc(java.lang.String) 

    /**
     * Sets the value of field 'subjectEn'.
     * 
     * @param subjectEn the value of field 'subjectEn'.
     */
    public void setSubjectEn(java.lang.String subjectEn)
    {
        this._subjectEn = subjectEn;
    } //-- void setSubjectEn(java.lang.String) 

    /**
     * Sets the value of field 'subjectSc'.
     * 
     * @param subjectSc the value of field 'subjectSc'.
     */
    public void setSubjectSc(java.lang.String subjectSc)
    {
        this._subjectSc = subjectSc;
    } //-- void setSubjectSc(java.lang.String) 

    /**
     * Sets the value of field 'subjectTc'.
     * 
     * @param subjectTc the value of field 'subjectTc'.
     */
    public void setSubjectTc(java.lang.String subjectTc)
    {
        this._subjectTc = subjectTc;
    } //-- void setSubjectTc(java.lang.String) 

    /**
     * Method unmarshal
     * 
     * 
     * 
     * @param reader
     * @return TemplateDetail_CT
     */
    public static hk.gov.ogcio.mars_cmc.cmc.castor.retrievecmctemplate.TemplateDetail_CT unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (hk.gov.ogcio.mars_cmc.cmc.castor.retrievecmctemplate.TemplateDetail_CT) Unmarshaller.unmarshal(hk.gov.ogcio.mars_cmc.cmc.castor.retrievecmctemplate.TemplateDetail_CT.class, reader);
    } //-- hk.gov.ogcio.mars_cmc.cmc.castor.retrievecmctemplate.TemplateDetail_CT unmarshal(java.io.Reader) 

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
