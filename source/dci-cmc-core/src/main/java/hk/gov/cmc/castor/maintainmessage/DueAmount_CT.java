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
 * Class DueAmount_CT.
 * 
 * @version $Revision$ $Date$
 */
public class DueAmount_CT implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _amount
     */
    private java.lang.String _amount;

    /**
     * Field _sign
     */
    private java.lang.String _sign;


      //----------------/
     //- Constructors -/
    //----------------/

    public DueAmount_CT() 
     {
        super();
    } //-- hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.DueAmount_CT()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'amount'.
     * 
     * @return String
     * @return the value of field 'amount'.
     */
    public java.lang.String getAmount()
    {
        return this._amount;
    } //-- java.lang.String getAmount() 

    /**
     * Returns the value of field 'sign'.
     * 
     * @return String
     * @return the value of field 'sign'.
     */
    public java.lang.String getSign()
    {
        return this._sign;
    } //-- java.lang.String getSign() 

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
     * Sets the value of field 'amount'.
     * 
     * @param amount the value of field 'amount'.
     */
    public void setAmount(java.lang.String amount)
    {
        this._amount = amount;
    } //-- void setAmount(java.lang.String) 

    /**
     * Sets the value of field 'sign'.
     * 
     * @param sign the value of field 'sign'.
     */
    public void setSign(java.lang.String sign)
    {
        this._sign = sign;
    } //-- void setSign(java.lang.String) 

    /**
     * Method unmarshal
     * 
     * 
     * 
     * @param reader
     * @return DueAmount_CT
     */
    public static hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.DueAmount_CT unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.DueAmount_CT) Unmarshaller.unmarshal(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.DueAmount_CT.class, reader);
    } //-- hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.DueAmount_CT unmarshal(java.io.Reader) 

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
