/**
 * Design, Implementation and Support of Common Middleware Components and Reference Applications 
 * 
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.0</a>, using an XML
 * 
 * Developed by: Gilbert Liao
 * Reviewed by: Kai So
 * Tester: Shaohui Wang
 * 
 */

package hk.gov.ogcio.mars_cmc.cmc.castor.adminreport;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.types.Reason_ST;
import hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.types.Type_ST;
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
 * Class Transaction_CT.
 * 
 * @version $Revision$ $Date$
 */
public class Transaction_CT implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _transactionID
     */
    private java.lang.String _transactionID;

    /**
     * Field _receivedTime
     */
    private java.lang.String _receivedTime;

    /**
     * Field _subjectTitle
     */
    private java.lang.String _subjectTitle;

    /**
     * Field _reason
     */
    private hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.types.Reason_ST _reason;

    /**
     * Field _type
     */
    private hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.types.Type_ST _type;


      //----------------/
     //- Constructors -/
    //----------------/

    public Transaction_CT() 
     {
        super();
    } //-- hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.Transaction_CT()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'reason'.
     * 
     * @return Reason_ST
     * @return the value of field 'reason'.
     */
    public hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.types.Reason_ST getReason()
    {
        return this._reason;
    } //-- hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.types.Reason_ST getReason() 

    /**
     * Returns the value of field 'receivedTime'.
     * 
     * @return String
     * @return the value of field 'receivedTime'.
     */
    public java.lang.String getReceivedTime()
    {
        return this._receivedTime;
    } //-- java.lang.String getReceivedTime() 

    /**
     * Returns the value of field 'subjectTitle'.
     * 
     * @return String
     * @return the value of field 'subjectTitle'.
     */
    public java.lang.String getSubjectTitle()
    {
        return this._subjectTitle;
    } //-- java.lang.String getSubjectTitle() 

    /**
     * Returns the value of field 'transactionID'.
     * 
     * @return String
     * @return the value of field 'transactionID'.
     */
    public java.lang.String getTransactionID()
    {
        return this._transactionID;
    } //-- java.lang.String getTransactionID() 

    /**
     * Returns the value of field 'type'.
     * 
     * @return Type_ST
     * @return the value of field 'type'.
     */
    public hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.types.Type_ST getType()
    {
        return this._type;
    } //-- hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.types.Type_ST getType() 

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
     * Sets the value of field 'reason'.
     * 
     * @param reason the value of field 'reason'.
     */
    public void setReason(hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.types.Reason_ST reason)
    {
        this._reason = reason;
    } //-- void setReason(hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.types.Reason_ST) 

    /**
     * Sets the value of field 'receivedTime'.
     * 
     * @param receivedTime the value of field 'receivedTime'.
     */
    public void setReceivedTime(java.lang.String receivedTime)
    {
        this._receivedTime = receivedTime;
    } //-- void setReceivedTime(java.lang.String) 

    /**
     * Sets the value of field 'subjectTitle'.
     * 
     * @param subjectTitle the value of field 'subjectTitle'.
     */
    public void setSubjectTitle(java.lang.String subjectTitle)
    {
        this._subjectTitle = subjectTitle;
    } //-- void setSubjectTitle(java.lang.String) 

    /**
     * Sets the value of field 'transactionID'.
     * 
     * @param transactionID the value of field 'transactionID'.
     */
    public void setTransactionID(java.lang.String transactionID)
    {
        this._transactionID = transactionID;
    } //-- void setTransactionID(java.lang.String) 

    /**
     * Sets the value of field 'type'.
     * 
     * @param type the value of field 'type'.
     */
    public void setType(hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.types.Type_ST type)
    {
        this._type = type;
    } //-- void setType(hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.types.Type_ST) 

    /**
     * Method unmarshal
     * 
     * 
     * 
     * @param reader
     * @return Transaction_CT
     */
    public static hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.Transaction_CT unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.Transaction_CT) Unmarshaller.unmarshal(hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.Transaction_CT.class, reader);
    } //-- hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.Transaction_CT unmarshal(java.io.Reader) 

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
