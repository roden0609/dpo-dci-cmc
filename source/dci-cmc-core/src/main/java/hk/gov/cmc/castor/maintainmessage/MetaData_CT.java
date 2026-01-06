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
import java.util.Enumeration;
import java.util.Vector;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class MetaData_CT.
 * 
 * @version $Revision$ $Date$
 */
public class MetaData_CT implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _dataContentEN
     */
    private java.lang.String _dataContentEN;

    /**
     * Field _dataContentTC
     */
    private java.lang.String _dataContentTC;

    /**
     * Field _dataContentSC
     */
    private java.lang.String _dataContentSC;

    /**
     * Field _billAccountInfo
     */
    private hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.BillAccountInfo _billAccountInfo;

    /**
     * Field _recipientList
     */
    private java.util.Vector _recipientList;


      //----------------/
     //- Constructors -/
    //----------------/

    public MetaData_CT() 
     {
        super();
        _recipientList = new Vector();
    } //-- hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MetaData_CT()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addRecipient
     * 
     * 
     * 
     * @param vRecipient
     */
    public void addRecipient(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.Recipient vRecipient)
        throws java.lang.IndexOutOfBoundsException
    {
        _recipientList.addElement(vRecipient);
    } //-- void addRecipient(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.Recipient) 

    /**
     * Method addRecipient
     * 
     * 
     * 
     * @param index
     * @param vRecipient
     */
    public void addRecipient(int index, hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.Recipient vRecipient)
        throws java.lang.IndexOutOfBoundsException
    {
        _recipientList.insertElementAt(vRecipient, index);
    } //-- void addRecipient(int, hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.Recipient) 

    /**
     * Method enumerateRecipient
     * 
     * 
     * 
     * @return Enumeration
     */
    public java.util.Enumeration enumerateRecipient()
    {
        return _recipientList.elements();
    } //-- java.util.Enumeration enumerateRecipient() 

    /**
     * Returns the value of field 'billAccountInfo'.
     * 
     * @return BillAccountInfo
     * @return the value of field 'billAccountInfo'.
     */
    public hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.BillAccountInfo getBillAccountInfo()
    {
        return this._billAccountInfo;
    } //-- hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.BillAccountInfo getBillAccountInfo() 

    /**
     * Returns the value of field 'dataContentEN'.
     * 
     * @return String
     * @return the value of field 'dataContentEN'.
     */
    public java.lang.String getDataContentEN()
    {
        return this._dataContentEN;
    } //-- java.lang.String getDataContentEN() 

    /**
     * Returns the value of field 'dataContentSC'.
     * 
     * @return String
     * @return the value of field 'dataContentSC'.
     */
    public java.lang.String getDataContentSC()
    {
        return this._dataContentSC;
    } //-- java.lang.String getDataContentSC() 

    /**
     * Returns the value of field 'dataContentTC'.
     * 
     * @return String
     * @return the value of field 'dataContentTC'.
     */
    public java.lang.String getDataContentTC()
    {
        return this._dataContentTC;
    } //-- java.lang.String getDataContentTC() 

    /**
     * Method getRecipient
     * 
     * 
     * 
     * @param index
     * @return Recipient
     */
    public hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.Recipient getRecipient(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index >= _recipientList.size())) {
            throw new IndexOutOfBoundsException("getRecipient: Index value '"+index+"' not in range [0.."+(_recipientList.size() - 1) + "]");
        }
        
        return (hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.Recipient) _recipientList.elementAt(index);
    } //-- hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.Recipient getRecipient(int) 

    /**
     * Method getRecipient
     * 
     * 
     * 
     * @return Recipient
     */
    public hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.Recipient[] getRecipient()
    {
        int size = _recipientList.size();
        hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.Recipient[] mArray = new hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.Recipient[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.Recipient) _recipientList.elementAt(index);
        }
        return mArray;
    } //-- hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.Recipient[] getRecipient() 

    /**
     * Method getRecipientCount
     * 
     * 
     * 
     * @return int
     */
    public int getRecipientCount()
    {
        return _recipientList.size();
    } //-- int getRecipientCount() 

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
     * Method removeAllRecipient
     * 
     */
    public void removeAllRecipient()
    {
        _recipientList.removeAllElements();
    } //-- void removeAllRecipient() 

    /**
     * Method removeRecipient
     * 
     * 
     * 
     * @param index
     * @return Recipient
     */
    public hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.Recipient removeRecipient(int index)
    {
        java.lang.Object obj = _recipientList.elementAt(index);
        _recipientList.removeElementAt(index);
        return (hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.Recipient) obj;
    } //-- hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.Recipient removeRecipient(int) 

    /**
     * Sets the value of field 'billAccountInfo'.
     * 
     * @param billAccountInfo the value of field 'billAccountInfo'.
     */
    public void setBillAccountInfo(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.BillAccountInfo billAccountInfo)
    {
        this._billAccountInfo = billAccountInfo;
    } //-- void setBillAccountInfo(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.BillAccountInfo) 

    /**
     * Sets the value of field 'dataContentEN'.
     * 
     * @param dataContentEN the value of field 'dataContentEN'.
     */
    public void setDataContentEN(java.lang.String dataContentEN)
    {
        this._dataContentEN = dataContentEN;
    } //-- void setDataContentEN(java.lang.String) 

    /**
     * Sets the value of field 'dataContentSC'.
     * 
     * @param dataContentSC the value of field 'dataContentSC'.
     */
    public void setDataContentSC(java.lang.String dataContentSC)
    {
        this._dataContentSC = dataContentSC;
    } //-- void setDataContentSC(java.lang.String) 

    /**
     * Sets the value of field 'dataContentTC'.
     * 
     * @param dataContentTC the value of field 'dataContentTC'.
     */
    public void setDataContentTC(java.lang.String dataContentTC)
    {
        this._dataContentTC = dataContentTC;
    } //-- void setDataContentTC(java.lang.String) 

    /**
     * Method setRecipient
     * 
     * 
     * 
     * @param index
     * @param vRecipient
     */
    public void setRecipient(int index, hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.Recipient vRecipient)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index >= _recipientList.size())) {
            throw new IndexOutOfBoundsException("setRecipient: Index value '"+index+"' not in range [0.." + (_recipientList.size() - 1) + "]");
        }
        _recipientList.setElementAt(vRecipient, index);
    } //-- void setRecipient(int, hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.Recipient) 

    /**
     * Method setRecipient
     * 
     * 
     * 
     * @param recipientArray
     */
    public void setRecipient(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.Recipient[] recipientArray)
    {
        //-- copy array
        _recipientList.removeAllElements();
        for (int i = 0; i < recipientArray.length; i++) {
            _recipientList.addElement(recipientArray[i]);
        }
    } //-- void setRecipient(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.Recipient) 

    /**
     * Method unmarshal
     * 
     * 
     * 
     * @param reader
     * @return MetaData_CT
     */
    public static hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MetaData_CT unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MetaData_CT) Unmarshaller.unmarshal(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MetaData_CT.class, reader);
    } //-- hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MetaData_CT unmarshal(java.io.Reader) 

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
        return "MetaData_CT [_dataContentEN=" + _dataContentEN + ", _dataContentTC=" + _dataContentTC
                + ", _dataContentSC=" + _dataContentSC + ", _billAccountInfo=" + _billAccountInfo + ", _recipientList="
                + _recipientList + "]";
    }
    // CMC-2025-034: CMC send RVD VIP request to DCI-CMC in RPC mode - END

}
