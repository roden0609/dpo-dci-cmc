






package hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage;

  
 


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






public class BillAccountInfo_CT implements java.io.Serializable {


      
     
    

    


    private java.lang.String _accountNameEN;

    


    private java.lang.String _accountNameTC;

    


    private java.lang.String _accountNameSC;

    


    private java.lang.String _accountNo;

    


    private hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.DueAmount _dueAmount;

    


    private java.lang.String _billType;

    


    private java.lang.String _billDescEN;

    


    private java.lang.String _billDescTC;

    


    private java.lang.String _billDescSC;

    


    private java.util.Date _issueDate;

    


    private java.util.Date _dueDate;

    


    private hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.OnlinePaymentType_ST _onlinePayment;

    


    private java.lang.String _remark;

    


    private hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.BillAccountInfoAction_ST _billAccountInfoAction;

    


    private hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.CommonIndicator_ST _overDueIndicator;

    


    private java.util.Vector _paymentTransactionList;


      
     
    

    public BillAccountInfo_CT() 
     {
        super();
        _paymentTransactionList = new Vector();
    } 


      
     
    

    






    public void addPaymentTransaction(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.PaymentTransaction vPaymentTransaction)
        throws java.lang.IndexOutOfBoundsException
    {
        _paymentTransactionList.addElement(vPaymentTransaction);
    } 

    







    public void addPaymentTransaction(int index, hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.PaymentTransaction vPaymentTransaction)
        throws java.lang.IndexOutOfBoundsException
    {
        _paymentTransactionList.insertElementAt(vPaymentTransaction, index);
    } 

    






    public java.util.Enumeration enumeratePaymentTransaction()
    {
        return _paymentTransactionList.elements();
    } 

    





    public java.lang.String getAccountNameEN()
    {
        return this._accountNameEN;
    } 

    





    public java.lang.String getAccountNameSC()
    {
        return this._accountNameSC;
    } 

    





    public java.lang.String getAccountNameTC()
    {
        return this._accountNameTC;
    } 

    





    public java.lang.String getAccountNo()
    {
        return this._accountNo;
    } 

    





    public hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.BillAccountInfoAction_ST getBillAccountInfoAction()
    {
        return this._billAccountInfoAction;
    } 

    





    public java.lang.String getBillDescEN()
    {
        return this._billDescEN;
    } 

    





    public java.lang.String getBillDescSC()
    {
        return this._billDescSC;
    } 

    





    public java.lang.String getBillDescTC()
    {
        return this._billDescTC;
    } 

    





    public java.lang.String getBillType()
    {
        return this._billType;
    } 

    





    public hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.DueAmount getDueAmount()
    {
        return this._dueAmount;
    } 

    





    public java.util.Date getDueDate()
    {
        return this._dueDate;
    } 

    





    public java.util.Date getIssueDate()
    {
        return this._issueDate;
    } 

    





    public hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.OnlinePaymentType_ST getOnlinePayment()
    {
        return this._onlinePayment;
    } 

    





    public hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.CommonIndicator_ST getOverDueIndicator()
    {
        return this._overDueIndicator;
    } 

    







    public hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.PaymentTransaction getPaymentTransaction(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        
        if ((index < 0) || (index >= _paymentTransactionList.size())) {
            throw new IndexOutOfBoundsException("getPaymentTransaction: Index value '"+index+"' not in range [0.."+(_paymentTransactionList.size() - 1) + "]");
        }
        
        return (hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.PaymentTransaction) _paymentTransactionList.elementAt(index);
    } 

    






    public hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.PaymentTransaction[] getPaymentTransaction()
    {
        int size = _paymentTransactionList.size();
        hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.PaymentTransaction[] mArray = new hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.PaymentTransaction[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.PaymentTransaction) _paymentTransactionList.elementAt(index);
        }
        return mArray;
    } 

    






    public int getPaymentTransactionCount()
    {
        return _paymentTransactionList.size();
    } 

    





    public java.lang.String getRemark()
    {
        return this._remark;
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

    



    public void removeAllPaymentTransaction()
    {
        _paymentTransactionList.removeAllElements();
    } 

    







    public hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.PaymentTransaction removePaymentTransaction(int index)
    {
        java.lang.Object obj = _paymentTransactionList.elementAt(index);
        _paymentTransactionList.removeElementAt(index);
        return (hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.PaymentTransaction) obj;
    } 

    




    public void setAccountNameEN(java.lang.String accountNameEN)
    {
        this._accountNameEN = accountNameEN;
    } 

    




    public void setAccountNameSC(java.lang.String accountNameSC)
    {
        this._accountNameSC = accountNameSC;
    } 

    




    public void setAccountNameTC(java.lang.String accountNameTC)
    {
        this._accountNameTC = accountNameTC;
    } 

    




    public void setAccountNo(java.lang.String accountNo)
    {
        this._accountNo = accountNo;
    } 

    





    public void setBillAccountInfoAction(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.BillAccountInfoAction_ST billAccountInfoAction)
    {
        this._billAccountInfoAction = billAccountInfoAction;
    } 

    




    public void setBillDescEN(java.lang.String billDescEN)
    {
        this._billDescEN = billDescEN;
    } 

    




    public void setBillDescSC(java.lang.String billDescSC)
    {
        this._billDescSC = billDescSC;
    } 

    




    public void setBillDescTC(java.lang.String billDescTC)
    {
        this._billDescTC = billDescTC;
    } 

    




    public void setBillType(java.lang.String billType)
    {
        this._billType = billType;
    } 

    




    public void setDueAmount(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.DueAmount dueAmount)
    {
        this._dueAmount = dueAmount;
    } 

    




    public void setDueDate(java.util.Date dueDate)
    {
        this._dueDate = dueDate;
    } 

    




    public void setIssueDate(java.util.Date issueDate)
    {
        this._issueDate = issueDate;
    } 

    




    public void setOnlinePayment(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.OnlinePaymentType_ST onlinePayment)
    {
        this._onlinePayment = onlinePayment;
    } 

    




    public void setOverDueIndicator(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.CommonIndicator_ST overDueIndicator)
    {
        this._overDueIndicator = overDueIndicator;
    } 

    







    public void setPaymentTransaction(int index, hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.PaymentTransaction vPaymentTransaction)
        throws java.lang.IndexOutOfBoundsException
    {
        
        if ((index < 0) || (index >= _paymentTransactionList.size())) {
            throw new IndexOutOfBoundsException("setPaymentTransaction: Index value '"+index+"' not in range [0.." + (_paymentTransactionList.size() - 1) + "]");
        }
        _paymentTransactionList.setElementAt(vPaymentTransaction, index);
    } 

    






    public void setPaymentTransaction(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.PaymentTransaction[] paymentTransactionArray)
    {
        
        _paymentTransactionList.removeAllElements();
        for (int i = 0; i < paymentTransactionArray.length; i++) {
            _paymentTransactionList.addElement(paymentTransactionArray[i]);
        }
    } 

    




    public void setRemark(java.lang.String remark)
    {
        this._remark = remark;
    } 

    







    public static hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.BillAccountInfo_CT unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.BillAccountInfo_CT) Unmarshaller.unmarshal(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.BillAccountInfo_CT.class, reader);
    } 

    



    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } 

}
