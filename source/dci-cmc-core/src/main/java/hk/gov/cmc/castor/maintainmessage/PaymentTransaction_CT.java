/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.0</a>, using an XML
 * Schema.
 * $Id$
 */

package hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.PaymentTransactionAction_ST;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.Date;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class PaymentTransaction_CT.
 * 
 * @version $Revision$ $Date$
 */
public class PaymentTransaction_CT implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _merchantNameEN
     */
    private java.lang.String _merchantNameEN;

    /**
     * Field _merchantNameTC
     */
    private java.lang.String _merchantNameTC;

    /**
     * Field _merchantNameSC
     */
    private java.lang.String _merchantNameSC;

    /**
     * Field _transactionDate
     */
    private java.util.Date _transactionDate;

    /**
     * Field _transactionReferenceNumber
     */
    private java.lang.String _transactionReferenceNumber;

    /**
     * Field _paymentMethodCode
     */
    private java.lang.String _paymentMethodCode;

    /**
     * Field _paymentTransactionAction
     */
    private hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.PaymentTransactionAction_ST _paymentTransactionAction;

    /**
     * Field _issueDate
     */
    private java.util.Date _issueDate;

    /**
     * Field _dueDate
     */
    private java.util.Date _dueDate;

    /**
     * Field _voidDate
     */
    private java.util.Date _voidDate;

    /**
     * Field _paidAmount
     */
    private java.lang.String _paidAmount;


      //----------------/
     //- Constructors -/
    //----------------/

    public PaymentTransaction_CT() 
     {
        super();
    } //-- hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.PaymentTransaction_CT()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'dueDate'.
     * 
     * @return Date
     * @return the value of field 'dueDate'.
     */
    public java.util.Date getDueDate()
    {
        return this._dueDate;
    } //-- java.util.Date getDueDate() 

    /**
     * Returns the value of field 'issueDate'.
     * 
     * @return Date
     * @return the value of field 'issueDate'.
     */
    public java.util.Date getIssueDate()
    {
        return this._issueDate;
    } //-- java.util.Date getIssueDate() 

    /**
     * Returns the value of field 'merchantNameEN'.
     * 
     * @return String
     * @return the value of field 'merchantNameEN'.
     */
    public java.lang.String getMerchantNameEN()
    {
        return this._merchantNameEN;
    } //-- java.lang.String getMerchantNameEN() 

    /**
     * Returns the value of field 'merchantNameSC'.
     * 
     * @return String
     * @return the value of field 'merchantNameSC'.
     */
    public java.lang.String getMerchantNameSC()
    {
        return this._merchantNameSC;
    } //-- java.lang.String getMerchantNameSC() 

    /**
     * Returns the value of field 'merchantNameTC'.
     * 
     * @return String
     * @return the value of field 'merchantNameTC'.
     */
    public java.lang.String getMerchantNameTC()
    {
        return this._merchantNameTC;
    } //-- java.lang.String getMerchantNameTC() 

    /**
     * Returns the value of field 'paidAmount'.
     * 
     * @return String
     * @return the value of field 'paidAmount'.
     */
    public java.lang.String getPaidAmount()
    {
        return this._paidAmount;
    } //-- java.lang.String getPaidAmount() 

    /**
     * Returns the value of field 'paymentMethodCode'.
     * 
     * @return String
     * @return the value of field 'paymentMethodCode'.
     */
    public java.lang.String getPaymentMethodCode()
    {
        return this._paymentMethodCode;
    } //-- java.lang.String getPaymentMethodCode() 

    /**
     * Returns the value of field 'paymentTransactionAction'.
     * 
     * @return PaymentTransactionAction_ST
     * @return the value of field 'paymentTransactionAction'.
     */
    public hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.PaymentTransactionAction_ST getPaymentTransactionAction()
    {
        return this._paymentTransactionAction;
    } //-- hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.PaymentTransactionAction_ST getPaymentTransactionAction() 

    /**
     * Returns the value of field 'transactionDate'.
     * 
     * @return Date
     * @return the value of field 'transactionDate'.
     */
    public java.util.Date getTransactionDate()
    {
        return this._transactionDate;
    } //-- java.util.Date getTransactionDate() 

    /**
     * Returns the value of field 'transactionReferenceNumber'.
     * 
     * @return String
     * @return the value of field 'transactionReferenceNumber'.
     */
    public java.lang.String getTransactionReferenceNumber()
    {
        return this._transactionReferenceNumber;
    } //-- java.lang.String getTransactionReferenceNumber() 

    /**
     * Returns the value of field 'voidDate'.
     * 
     * @return Date
     * @return the value of field 'voidDate'.
     */
    public java.util.Date getVoidDate()
    {
        return this._voidDate;
    } //-- java.util.Date getVoidDate() 

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
     * Sets the value of field 'dueDate'.
     * 
     * @param dueDate the value of field 'dueDate'.
     */
    public void setDueDate(java.util.Date dueDate)
    {
        this._dueDate = dueDate;
    } //-- void setDueDate(java.util.Date) 

    /**
     * Sets the value of field 'issueDate'.
     * 
     * @param issueDate the value of field 'issueDate'.
     */
    public void setIssueDate(java.util.Date issueDate)
    {
        this._issueDate = issueDate;
    } //-- void setIssueDate(java.util.Date) 

    /**
     * Sets the value of field 'merchantNameEN'.
     * 
     * @param merchantNameEN the value of field 'merchantNameEN'.
     */
    public void setMerchantNameEN(java.lang.String merchantNameEN)
    {
        this._merchantNameEN = merchantNameEN;
    } //-- void setMerchantNameEN(java.lang.String) 

    /**
     * Sets the value of field 'merchantNameSC'.
     * 
     * @param merchantNameSC the value of field 'merchantNameSC'.
     */
    public void setMerchantNameSC(java.lang.String merchantNameSC)
    {
        this._merchantNameSC = merchantNameSC;
    } //-- void setMerchantNameSC(java.lang.String) 

    /**
     * Sets the value of field 'merchantNameTC'.
     * 
     * @param merchantNameTC the value of field 'merchantNameTC'.
     */
    public void setMerchantNameTC(java.lang.String merchantNameTC)
    {
        this._merchantNameTC = merchantNameTC;
    } //-- void setMerchantNameTC(java.lang.String) 

    /**
     * Sets the value of field 'paidAmount'.
     * 
     * @param paidAmount the value of field 'paidAmount'.
     */
    public void setPaidAmount(java.lang.String paidAmount)
    {
        this._paidAmount = paidAmount;
    } //-- void setPaidAmount(java.lang.String) 

    /**
     * Sets the value of field 'paymentMethodCode'.
     * 
     * @param paymentMethodCode the value of field
     * 'paymentMethodCode'.
     */
    public void setPaymentMethodCode(java.lang.String paymentMethodCode)
    {
        this._paymentMethodCode = paymentMethodCode;
    } //-- void setPaymentMethodCode(java.lang.String) 

    /**
     * Sets the value of field 'paymentTransactionAction'.
     * 
     * @param paymentTransactionAction the value of field
     * 'paymentTransactionAction'.
     */
    public void setPaymentTransactionAction(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.PaymentTransactionAction_ST paymentTransactionAction)
    {
        this._paymentTransactionAction = paymentTransactionAction;
    } //-- void setPaymentTransactionAction(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.PaymentTransactionAction_ST) 

    /**
     * Sets the value of field 'transactionDate'.
     * 
     * @param transactionDate the value of field 'transactionDate'.
     */
    public void setTransactionDate(java.util.Date transactionDate)
    {
        this._transactionDate = transactionDate;
    } //-- void setTransactionDate(java.util.Date) 

    /**
     * Sets the value of field 'transactionReferenceNumber'.
     * 
     * @param transactionReferenceNumber the value of field
     * 'transactionReferenceNumber'.
     */
    public void setTransactionReferenceNumber(java.lang.String transactionReferenceNumber)
    {
        this._transactionReferenceNumber = transactionReferenceNumber;
    } //-- void setTransactionReferenceNumber(java.lang.String) 

    /**
     * Sets the value of field 'voidDate'.
     * 
     * @param voidDate the value of field 'voidDate'.
     */
    public void setVoidDate(java.util.Date voidDate)
    {
        this._voidDate = voidDate;
    } //-- void setVoidDate(java.util.Date) 

    /**
     * Method unmarshal
     * 
     * 
     * 
     * @param reader
     * @return PaymentTransaction_CT
     */
    public static hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.PaymentTransaction_CT unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.PaymentTransaction_CT) Unmarshaller.unmarshal(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.PaymentTransaction_CT.class, reader);
    } //-- hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.PaymentTransaction_CT unmarshal(java.io.Reader) 

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
