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

import hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.BillAccountInfoAction_ST;
import hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.CommonIndicator_ST;
import hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.OnlinePaymentType_ST;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class BillAccountInfo_CT.
 * 
 * @version $Revision$ $Date$
 */
public class BillAccountInfo_CT implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _accountNameEN
     */
    private java.lang.String _accountNameEN;

    /**
     * Field _accountNameTC
     */
    private java.lang.String _accountNameTC;

    /**
     * Field _accountNameSC
     */
    private java.lang.String _accountNameSC;

    /**
     * Field _accountNo
     */
    private java.lang.String _accountNo;

    /**
     * Field _dueAmount
     */
    private hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.DueAmount _dueAmount;

    /**
     * Field _billType
     */
    private java.lang.String _billType;

    /**
     * Field _billDescEN
     */
    private java.lang.String _billDescEN;

    /**
     * Field _billDescTC
     */
    private java.lang.String _billDescTC;

    /**
     * Field _billDescSC
     */
    private java.lang.String _billDescSC;

    /**
     * Field _issueDate
     */
    private java.util.Date _issueDate;

    /**
     * Field _dueDate
     */
    private java.util.Date _dueDate;

    /**
     * Field _onlinePayment
     */
    private hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.OnlinePaymentType_ST _onlinePayment;

    /**
     * Field _remark
     */
    private java.lang.String _remark;

    /**
     * Field _billAccountInfoAction
     */
    private hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.BillAccountInfoAction_ST _billAccountInfoAction;

    /**
     * Field _overDueIndicator
     */
    private hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.CommonIndicator_ST _overDueIndicator;

    /**
     * Field _paymentTransactionList
     */
    private java.util.Vector _paymentTransactionList;


      //----------------/
     //- Constructors -/
    //----------------/

    public BillAccountInfo_CT() 
     {
        super();
        _paymentTransactionList = new Vector();
    } //-- hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.BillAccountInfo_CT()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addPaymentTransaction
     * 
     * 
     * 
     * @param vPaymentTransaction
     */
    public void addPaymentTransaction(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.PaymentTransaction vPaymentTransaction)
        throws java.lang.IndexOutOfBoundsException
    {
        _paymentTransactionList.addElement(vPaymentTransaction);
    } //-- void addPaymentTransaction(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.PaymentTransaction) 

    /**
     * Method addPaymentTransaction
     * 
     * 
     * 
     * @param index
     * @param vPaymentTransaction
     */
    public void addPaymentTransaction(int index, hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.PaymentTransaction vPaymentTransaction)
        throws java.lang.IndexOutOfBoundsException
    {
        _paymentTransactionList.insertElementAt(vPaymentTransaction, index);
    } //-- void addPaymentTransaction(int, hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.PaymentTransaction) 

    /**
     * Method enumeratePaymentTransaction
     * 
     * 
     * 
     * @return Enumeration
     */
    public java.util.Enumeration enumeratePaymentTransaction()
    {
        return _paymentTransactionList.elements();
    } //-- java.util.Enumeration enumeratePaymentTransaction() 

    /**
     * Returns the value of field 'accountNameEN'.
     * 
     * @return String
     * @return the value of field 'accountNameEN'.
     */
    public java.lang.String getAccountNameEN()
    {
        return this._accountNameEN;
    } //-- java.lang.String getAccountNameEN() 

    /**
     * Returns the value of field 'accountNameSC'.
     * 
     * @return String
     * @return the value of field 'accountNameSC'.
     */
    public java.lang.String getAccountNameSC()
    {
        return this._accountNameSC;
    } //-- java.lang.String getAccountNameSC() 

    /**
     * Returns the value of field 'accountNameTC'.
     * 
     * @return String
     * @return the value of field 'accountNameTC'.
     */
    public java.lang.String getAccountNameTC()
    {
        return this._accountNameTC;
    } //-- java.lang.String getAccountNameTC() 

    /**
     * Returns the value of field 'accountNo'.
     * 
     * @return String
     * @return the value of field 'accountNo'.
     */
    public java.lang.String getAccountNo()
    {
        return this._accountNo;
    } //-- java.lang.String getAccountNo() 

    /**
     * Returns the value of field 'billAccountInfoAction'.
     * 
     * @return BillAccountInfoAction_ST
     * @return the value of field 'billAccountInfoAction'.
     */
    public hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.BillAccountInfoAction_ST getBillAccountInfoAction()
    {
        return this._billAccountInfoAction;
    } //-- hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.BillAccountInfoAction_ST getBillAccountInfoAction() 

    /**
     * Returns the value of field 'billDescEN'.
     * 
     * @return String
     * @return the value of field 'billDescEN'.
     */
    public java.lang.String getBillDescEN()
    {
        return this._billDescEN;
    } //-- java.lang.String getBillDescEN() 

    /**
     * Returns the value of field 'billDescSC'.
     * 
     * @return String
     * @return the value of field 'billDescSC'.
     */
    public java.lang.String getBillDescSC()
    {
        return this._billDescSC;
    } //-- java.lang.String getBillDescSC() 

    /**
     * Returns the value of field 'billDescTC'.
     * 
     * @return String
     * @return the value of field 'billDescTC'.
     */
    public java.lang.String getBillDescTC()
    {
        return this._billDescTC;
    } //-- java.lang.String getBillDescTC() 

    /**
     * Returns the value of field 'billType'.
     * 
     * @return String
     * @return the value of field 'billType'.
     */
    public java.lang.String getBillType()
    {
        return this._billType;
    } //-- java.lang.String getBillType() 

    /**
     * Returns the value of field 'dueAmount'.
     * 
     * @return DueAmount
     * @return the value of field 'dueAmount'.
     */
    public hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.DueAmount getDueAmount()
    {
        return this._dueAmount;
    } //-- hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.DueAmount getDueAmount() 

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
     * Returns the value of field 'onlinePayment'.
     * 
     * @return OnlinePaymentType_ST
     * @return the value of field 'onlinePayment'.
     */
    public hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.OnlinePaymentType_ST getOnlinePayment()
    {
        return this._onlinePayment;
    } //-- hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.OnlinePaymentType_ST getOnlinePayment() 

    /**
     * Returns the value of field 'overDueIndicator'.
     * 
     * @return CommonIndicator_ST
     * @return the value of field 'overDueIndicator'.
     */
    public hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.CommonIndicator_ST getOverDueIndicator()
    {
        return this._overDueIndicator;
    } //-- hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.CommonIndicator_ST getOverDueIndicator() 

    /**
     * Method getPaymentTransaction
     * 
     * 
     * 
     * @param index
     * @return PaymentTransaction
     */
    public hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.PaymentTransaction getPaymentTransaction(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index >= _paymentTransactionList.size())) {
            throw new IndexOutOfBoundsException("getPaymentTransaction: Index value '"+index+"' not in range [0.."+(_paymentTransactionList.size() - 1) + "]");
        }
        
        return (hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.PaymentTransaction) _paymentTransactionList.elementAt(index);
    } //-- hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.PaymentTransaction getPaymentTransaction(int) 

    /**
     * Method getPaymentTransaction
     * 
     * 
     * 
     * @return PaymentTransaction
     */
    public hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.PaymentTransaction[] getPaymentTransaction()
    {
        int size = _paymentTransactionList.size();
        hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.PaymentTransaction[] mArray = new hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.PaymentTransaction[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.PaymentTransaction) _paymentTransactionList.elementAt(index);
        }
        return mArray;
    } //-- hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.PaymentTransaction[] getPaymentTransaction() 

    /**
     * Method getPaymentTransactionCount
     * 
     * 
     * 
     * @return int
     */
    public int getPaymentTransactionCount()
    {
        return _paymentTransactionList.size();
    } //-- int getPaymentTransactionCount() 

    /**
     * Returns the value of field 'remark'.
     * 
     * @return String
     * @return the value of field 'remark'.
     */
    public java.lang.String getRemark()
    {
        return this._remark;
    } //-- java.lang.String getRemark() 

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
     * Method removeAllPaymentTransaction
     * 
     */
    public void removeAllPaymentTransaction()
    {
        _paymentTransactionList.removeAllElements();
    } //-- void removeAllPaymentTransaction() 

    /**
     * Method removePaymentTransaction
     * 
     * 
     * 
     * @param index
     * @return PaymentTransaction
     */
    public hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.PaymentTransaction removePaymentTransaction(int index)
    {
        java.lang.Object obj = _paymentTransactionList.elementAt(index);
        _paymentTransactionList.removeElementAt(index);
        return (hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.PaymentTransaction) obj;
    } //-- hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.PaymentTransaction removePaymentTransaction(int) 

    /**
     * Sets the value of field 'accountNameEN'.
     * 
     * @param accountNameEN the value of field 'accountNameEN'.
     */
    public void setAccountNameEN(java.lang.String accountNameEN)
    {
        this._accountNameEN = accountNameEN;
    } //-- void setAccountNameEN(java.lang.String) 

    /**
     * Sets the value of field 'accountNameSC'.
     * 
     * @param accountNameSC the value of field 'accountNameSC'.
     */
    public void setAccountNameSC(java.lang.String accountNameSC)
    {
        this._accountNameSC = accountNameSC;
    } //-- void setAccountNameSC(java.lang.String) 

    /**
     * Sets the value of field 'accountNameTC'.
     * 
     * @param accountNameTC the value of field 'accountNameTC'.
     */
    public void setAccountNameTC(java.lang.String accountNameTC)
    {
        this._accountNameTC = accountNameTC;
    } //-- void setAccountNameTC(java.lang.String) 

    /**
     * Sets the value of field 'accountNo'.
     * 
     * @param accountNo the value of field 'accountNo'.
     */
    public void setAccountNo(java.lang.String accountNo)
    {
        this._accountNo = accountNo;
    } //-- void setAccountNo(java.lang.String) 

    /**
     * Sets the value of field 'billAccountInfoAction'.
     * 
     * @param billAccountInfoAction the value of field
     * 'billAccountInfoAction'.
     */
    public void setBillAccountInfoAction(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.BillAccountInfoAction_ST billAccountInfoAction)
    {
        this._billAccountInfoAction = billAccountInfoAction;
    } //-- void setBillAccountInfoAction(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.BillAccountInfoAction_ST) 

    /**
     * Sets the value of field 'billDescEN'.
     * 
     * @param billDescEN the value of field 'billDescEN'.
     */
    public void setBillDescEN(java.lang.String billDescEN)
    {
        this._billDescEN = billDescEN;
    } //-- void setBillDescEN(java.lang.String) 

    /**
     * Sets the value of field 'billDescSC'.
     * 
     * @param billDescSC the value of field 'billDescSC'.
     */
    public void setBillDescSC(java.lang.String billDescSC)
    {
        this._billDescSC = billDescSC;
    } //-- void setBillDescSC(java.lang.String) 

    /**
     * Sets the value of field 'billDescTC'.
     * 
     * @param billDescTC the value of field 'billDescTC'.
     */
    public void setBillDescTC(java.lang.String billDescTC)
    {
        this._billDescTC = billDescTC;
    } //-- void setBillDescTC(java.lang.String) 

    /**
     * Sets the value of field 'billType'.
     * 
     * @param billType the value of field 'billType'.
     */
    public void setBillType(java.lang.String billType)
    {
        this._billType = billType;
    } //-- void setBillType(java.lang.String) 

    /**
     * Sets the value of field 'dueAmount'.
     * 
     * @param dueAmount the value of field 'dueAmount'.
     */
    public void setDueAmount(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.DueAmount dueAmount)
    {
        this._dueAmount = dueAmount;
    } //-- void setDueAmount(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.DueAmount) 

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
     * Sets the value of field 'onlinePayment'.
     * 
     * @param onlinePayment the value of field 'onlinePayment'.
     */
    public void setOnlinePayment(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.OnlinePaymentType_ST onlinePayment)
    {
        this._onlinePayment = onlinePayment;
    } //-- void setOnlinePayment(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.OnlinePaymentType_ST) 

    /**
     * Sets the value of field 'overDueIndicator'.
     * 
     * @param overDueIndicator the value of field 'overDueIndicator'
     */
    public void setOverDueIndicator(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.CommonIndicator_ST overDueIndicator)
    {
        this._overDueIndicator = overDueIndicator;
    } //-- void setOverDueIndicator(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.CommonIndicator_ST) 

    /**
     * Method setPaymentTransaction
     * 
     * 
     * 
     * @param index
     * @param vPaymentTransaction
     */
    public void setPaymentTransaction(int index, hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.PaymentTransaction vPaymentTransaction)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index >= _paymentTransactionList.size())) {
            throw new IndexOutOfBoundsException("setPaymentTransaction: Index value '"+index+"' not in range [0.." + (_paymentTransactionList.size() - 1) + "]");
        }
        _paymentTransactionList.setElementAt(vPaymentTransaction, index);
    } //-- void setPaymentTransaction(int, hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.PaymentTransaction) 

    /**
     * Method setPaymentTransaction
     * 
     * 
     * 
     * @param paymentTransactionArray
     */
    public void setPaymentTransaction(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.PaymentTransaction[] paymentTransactionArray)
    {
        //-- copy array
        _paymentTransactionList.removeAllElements();
        for (int i = 0; i < paymentTransactionArray.length; i++) {
            _paymentTransactionList.addElement(paymentTransactionArray[i]);
        }
    } //-- void setPaymentTransaction(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.PaymentTransaction) 

    /**
     * Sets the value of field 'remark'.
     * 
     * @param remark the value of field 'remark'.
     */
    public void setRemark(java.lang.String remark)
    {
        this._remark = remark;
    } //-- void setRemark(java.lang.String) 

    /**
     * Method unmarshal
     * 
     * 
     * 
     * @param reader
     * @return BillAccountInfo_CT
     */
    public static hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.BillAccountInfo_CT unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.BillAccountInfo_CT) Unmarshaller.unmarshal(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.BillAccountInfo_CT.class, reader);
    } //-- hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.BillAccountInfo_CT unmarshal(java.io.Reader) 

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
