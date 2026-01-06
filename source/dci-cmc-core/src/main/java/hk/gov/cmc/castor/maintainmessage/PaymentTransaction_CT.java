






package hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage;

  
 


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






public class PaymentTransaction_CT implements java.io.Serializable {


      
     
    

    


    private java.lang.String _merchantNameEN;

    


    private java.lang.String _merchantNameTC;

    


    private java.lang.String _merchantNameSC;

    


    private java.util.Date _transactionDate;

    


    private java.lang.String _transactionReferenceNumber;

    


    private java.lang.String _paymentMethodCode;

    


    private hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.PaymentTransactionAction_ST _paymentTransactionAction;

    


    private java.util.Date _issueDate;

    


    private java.util.Date _dueDate;

    


    private java.util.Date _voidDate;

    


    private java.lang.String _paidAmount;


      
     
    

    public PaymentTransaction_CT() 
     {
        super();
    } 


      
     
    

    





    public java.util.Date getDueDate()
    {
        return this._dueDate;
    } 

    





    public java.util.Date getIssueDate()
    {
        return this._issueDate;
    } 

    





    public java.lang.String getMerchantNameEN()
    {
        return this._merchantNameEN;
    } 

    





    public java.lang.String getMerchantNameSC()
    {
        return this._merchantNameSC;
    } 

    





    public java.lang.String getMerchantNameTC()
    {
        return this._merchantNameTC;
    } 

    





    public java.lang.String getPaidAmount()
    {
        return this._paidAmount;
    } 

    





    public java.lang.String getPaymentMethodCode()
    {
        return this._paymentMethodCode;
    } 

    





    public hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.PaymentTransactionAction_ST getPaymentTransactionAction()
    {
        return this._paymentTransactionAction;
    } 

    





    public java.util.Date getTransactionDate()
    {
        return this._transactionDate;
    } 

    





    public java.lang.String getTransactionReferenceNumber()
    {
        return this._transactionReferenceNumber;
    } 

    





    public java.util.Date getVoidDate()
    {
        return this._voidDate;
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

    




    public void setDueDate(java.util.Date dueDate)
    {
        this._dueDate = dueDate;
    } 

    




    public void setIssueDate(java.util.Date issueDate)
    {
        this._issueDate = issueDate;
    } 

    




    public void setMerchantNameEN(java.lang.String merchantNameEN)
    {
        this._merchantNameEN = merchantNameEN;
    } 

    




    public void setMerchantNameSC(java.lang.String merchantNameSC)
    {
        this._merchantNameSC = merchantNameSC;
    } 

    




    public void setMerchantNameTC(java.lang.String merchantNameTC)
    {
        this._merchantNameTC = merchantNameTC;
    } 

    




    public void setPaidAmount(java.lang.String paidAmount)
    {
        this._paidAmount = paidAmount;
    } 

    





    public void setPaymentMethodCode(java.lang.String paymentMethodCode)
    {
        this._paymentMethodCode = paymentMethodCode;
    } 

    





    public void setPaymentTransactionAction(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.PaymentTransactionAction_ST paymentTransactionAction)
    {
        this._paymentTransactionAction = paymentTransactionAction;
    } 

    




    public void setTransactionDate(java.util.Date transactionDate)
    {
        this._transactionDate = transactionDate;
    } 

    





    public void setTransactionReferenceNumber(java.lang.String transactionReferenceNumber)
    {
        this._transactionReferenceNumber = transactionReferenceNumber;
    } 

    




    public void setVoidDate(java.util.Date voidDate)
    {
        this._voidDate = voidDate;
    } 

    







    public static hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.PaymentTransaction_CT unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.PaymentTransaction_CT) Unmarshaller.unmarshal(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.PaymentTransaction_CT.class, reader);
    } 

    



    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } 

}
