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

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Vector;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class ServiceProvider_CT.
 * 
 * @version $Revision$ $Date$
 */
public class ServiceProvider_CT implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _serviceProviderName
     */
    private java.lang.String _serviceProviderName;

    /**
     * Field _undeliveredDate
     */
    private java.lang.String _undeliveredDate;

    /**
     * Field _transactionList
     */
    private java.util.Vector _transactionList;


      //----------------/
     //- Constructors -/
    //----------------/

    public ServiceProvider_CT() 
     {
        super();
        _transactionList = new Vector();
    } //-- hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.ServiceProvider_CT()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addTransaction
     * 
     * 
     * 
     * @param vTransaction
     */
    public void addTransaction(hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.Transaction vTransaction)
        throws java.lang.IndexOutOfBoundsException
    {
        _transactionList.addElement(vTransaction);
    } //-- void addTransaction(hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.Transaction) 

    /**
     * Method addTransaction
     * 
     * 
     * 
     * @param index
     * @param vTransaction
     */
    public void addTransaction(int index, hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.Transaction vTransaction)
        throws java.lang.IndexOutOfBoundsException
    {
        _transactionList.insertElementAt(vTransaction, index);
    } //-- void addTransaction(int, hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.Transaction) 

    /**
     * Method enumerateTransaction
     * 
     * 
     * 
     * @return Enumeration
     */
    public java.util.Enumeration enumerateTransaction()
    {
        return _transactionList.elements();
    } //-- java.util.Enumeration enumerateTransaction() 

    /**
     * Returns the value of field 'serviceProviderName'.
     * 
     * @return String
     * @return the value of field 'serviceProviderName'.
     */
    public java.lang.String getServiceProviderName()
    {
        return this._serviceProviderName;
    } //-- java.lang.String getServiceProviderName() 

    /**
     * Method getTransaction
     * 
     * 
     * 
     * @param index
     * @return Transaction
     */
    public hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.Transaction getTransaction(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index >= _transactionList.size())) {
            throw new IndexOutOfBoundsException("getTransaction: Index value '"+index+"' not in range [0.."+(_transactionList.size() - 1) + "]");
        }
        
        return (hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.Transaction) _transactionList.elementAt(index);
    } //-- hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.Transaction getTransaction(int) 

    /**
     * Method getTransaction
     * 
     * 
     * 
     * @return Transaction
     */
    public hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.Transaction[] getTransaction()
    {
        int size = _transactionList.size();
        hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.Transaction[] mArray = new hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.Transaction[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.Transaction) _transactionList.elementAt(index);
        }
        return mArray;
    } //-- hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.Transaction[] getTransaction() 

    /**
     * Method getTransactionCount
     * 
     * 
     * 
     * @return int
     */
    public int getTransactionCount()
    {
        return _transactionList.size();
    } //-- int getTransactionCount() 

    /**
     * Returns the value of field 'undeliveredDate'.
     * 
     * @return String
     * @return the value of field 'undeliveredDate'.
     */
    public java.lang.String getUndeliveredDate()
    {
        return this._undeliveredDate;
    } //-- java.lang.String getUndeliveredDate() 

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
     * Method removeAllTransaction
     * 
     */
    public void removeAllTransaction()
    {
        _transactionList.removeAllElements();
    } //-- void removeAllTransaction() 

    /**
     * Method removeTransaction
     * 
     * 
     * 
     * @param index
     * @return Transaction
     */
    public hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.Transaction removeTransaction(int index)
    {
        java.lang.Object obj = _transactionList.elementAt(index);
        _transactionList.removeElementAt(index);
        return (hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.Transaction) obj;
    } //-- hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.Transaction removeTransaction(int) 

    /**
     * Sets the value of field 'serviceProviderName'.
     * 
     * @param serviceProviderName the value of field
     * 'serviceProviderName'.
     */
    public void setServiceProviderName(java.lang.String serviceProviderName)
    {
        this._serviceProviderName = serviceProviderName;
    } //-- void setServiceProviderName(java.lang.String) 

    /**
     * Method setTransaction
     * 
     * 
     * 
     * @param index
     * @param vTransaction
     */
    public void setTransaction(int index, hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.Transaction vTransaction)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index >= _transactionList.size())) {
            throw new IndexOutOfBoundsException("setTransaction: Index value '"+index+"' not in range [0.." + (_transactionList.size() - 1) + "]");
        }
        _transactionList.setElementAt(vTransaction, index);
    } //-- void setTransaction(int, hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.Transaction) 

    /**
     * Method setTransaction
     * 
     * 
     * 
     * @param transactionArray
     */
    public void setTransaction(hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.Transaction[] transactionArray)
    {
        //-- copy array
        _transactionList.removeAllElements();
        for (int i = 0; i < transactionArray.length; i++) {
            _transactionList.addElement(transactionArray[i]);
        }
    } //-- void setTransaction(hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.Transaction) 

    /**
     * Sets the value of field 'undeliveredDate'.
     * 
     * @param undeliveredDate the value of field 'undeliveredDate'.
     */
    public void setUndeliveredDate(java.lang.String undeliveredDate)
    {
        this._undeliveredDate = undeliveredDate;
    } //-- void setUndeliveredDate(java.lang.String) 

    /**
     * Method unmarshal
     * 
     * 
     * 
     * @param reader
     * @return ServiceProvider_CT
     */
    public static hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.ServiceProvider_CT unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.ServiceProvider_CT) Unmarshaller.unmarshal(hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.ServiceProvider_CT.class, reader);
    } //-- hk.gov.ogcio.mars_cmc.cmc.castor.adminreport.ServiceProvider_CT unmarshal(java.io.Reader) 

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
