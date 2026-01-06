
package hk.gov.cmc.castor.maintainmessage;

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

public class MetaData_CT implements java.io.Serializable {

    private java.lang.String _dataContentEN;

    private java.lang.String _dataContentTC;

    private java.lang.String _dataContentSC;

    private hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.BillAccountInfo _billAccountInfo;

    private java.util.Vector _recipientList;

    public MetaData_CT()
    {
        super();
        _recipientList = new Vector();
    }

    public void addRecipient(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.Recipient vRecipient)
    throws java.lang.IndexOutOfBoundsException
    {
        _recipientList.addElement(vRecipient);
    }

    public void addRecipient(int index, hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.Recipient vRecipient)
    throws java.lang.IndexOutOfBoundsException
    {
        _recipientList.insertElementAt(vRecipient, index);
    }

    public java.util.Enumeration enumerateRecipient()
    {
        return _recipientList.elements();
    }

    public hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.BillAccountInfo getBillAccountInfo()
    {
        return this._billAccountInfo;
    }

    public java.lang.String getDataContentEN()
    {
        return this._dataContentEN;
    }

    public java.lang.String getDataContentSC()
    {
        return this._dataContentSC;
    }

    public java.lang.String getDataContentTC()
    {
        return this._dataContentTC;
    }

    public hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.Recipient getRecipient(int index)
    throws java.lang.IndexOutOfBoundsException
    {

        if ((index < 0) || (index >= _recipientList.size())) {
            throw new IndexOutOfBoundsException("getRecipient: Index value '"+index+"' not in range [0.."+(_recipientList.size() - 1) + "]");
        }

        return (hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.Recipient) _recipientList.elementAt(index);
    }

    public hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.Recipient[] getRecipient()
    {
        int size = _recipientList.size();
        hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.Recipient[] mArray = new hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.Recipient[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.Recipient) _recipientList.elementAt(index);
        }
        return mArray;
    }

    public int getRecipientCount()
    {
        return _recipientList.size();
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

    public void removeAllRecipient()
    {
        _recipientList.removeAllElements();
    }

    public hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.Recipient removeRecipient(int index)
    {
        java.lang.Object obj = _recipientList.elementAt(index);
        _recipientList.removeElementAt(index);
        return (hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.Recipient) obj;
    }

    public void setBillAccountInfo(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.BillAccountInfo billAccountInfo)
    {
        this._billAccountInfo = billAccountInfo;
    }

    public void setDataContentEN(java.lang.String dataContentEN)
    {
        this._dataContentEN = dataContentEN;
    }

    public void setDataContentSC(java.lang.String dataContentSC)
    {
        this._dataContentSC = dataContentSC;
    }

    public void setDataContentTC(java.lang.String dataContentTC)
    {
        this._dataContentTC = dataContentTC;
    }

    public void setRecipient(int index, hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.Recipient vRecipient)
    throws java.lang.IndexOutOfBoundsException
    {

        if ((index < 0) || (index >= _recipientList.size())) {
            throw new IndexOutOfBoundsException("setRecipient: Index value '"+index+"' not in range [0.." + (_recipientList.size() - 1) + "]");
        }
        _recipientList.setElementAt(vRecipient, index);
    }

    public void setRecipient(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.Recipient[] recipientArray)
    {

        _recipientList.removeAllElements();
        for (int i = 0; i < recipientArray.length; i++) {
            _recipientList.addElement(recipientArray[i]);
        }
    }

    public static hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MetaData_CT unmarshal(java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MetaData_CT) Unmarshaller.unmarshal(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MetaData_CT.class, reader);
    }

    public void validate()
    throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    }

    @Override
    public String toString() {
        return "MetaData_CT [_dataContentEN=" + _dataContentEN + ", _dataContentTC=" + _dataContentTC
        + ", _dataContentSC=" + _dataContentSC + ", _billAccountInfo=" + _billAccountInfo + ", _recipientList="
        + _recipientList + "]";
    }

}
