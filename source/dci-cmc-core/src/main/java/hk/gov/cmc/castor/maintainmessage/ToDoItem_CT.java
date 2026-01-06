/*
 * Design, Implementation and Support of Common Middleware Components and Reference Applications 
 * 
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.0</a>, using an XML
 * 
 * Developed by: Gilbert Liao
 * Reviewed by: Kai So
 * Tester: Shaohui Wang
 */

package hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage;

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
 * Class ToDoItem_CT.
 * 
 * @version $Revision$ $Date$
 */
public class ToDoItem_CT implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _templateID
     */
    private java.lang.String _templateID;

    /**
     * Field _templateVersion
     */
    private java.lang.String _templateVersion;


      //----------------/
     //- Constructors -/
    //----------------/

    public ToDoItem_CT() 
     {
        super();
    } //-- hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.ToDoItem_CT()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'templateID'.
     * 
     * @return String
     * @return the value of field 'templateID'.
     */
    public java.lang.String getTemplateID()
    {
        return this._templateID;
    } //-- java.lang.String getTemplateID() 

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
     * Sets the value of field 'templateID'.
     * 
     * @param templateID the value of field 'templateID'.
     */
    public void setTemplateID(java.lang.String templateID)
    {
        this._templateID = templateID;
    } //-- void setTemplateID(java.lang.String) 

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
     * @return ToDoItem_CT
     */
    public static hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.ToDoItem_CT unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.ToDoItem_CT) Unmarshaller.unmarshal(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.ToDoItem_CT.class, reader);
    } //-- hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.ToDoItem_CT unmarshal(java.io.Reader) 

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

    // CMC-2025-034: CMC send RVD VIP request to DCI-CMC in RPC mode - BEGIN
	@Override
	public String toString() {
        return "ToDoItem_CT [_templateID=" + _templateID + ", _templateVersion=" + _templateVersion + "]";
    }
    // CMC-2025-034: CMC send RVD VIP request to DCI-CMC in RPC mode - END

}
