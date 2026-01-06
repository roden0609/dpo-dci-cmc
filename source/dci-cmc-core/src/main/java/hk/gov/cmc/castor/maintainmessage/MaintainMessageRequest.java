
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

public class MaintainMessageRequest implements java.io.Serializable {

    private java.util.Vector _messageRequestList;

    public MaintainMessageRequest()
    {
        super();
        _messageRequestList = new Vector();
    }

    public void addMessageRequest(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageRequest vMessageRequest)
    throws java.lang.IndexOutOfBoundsException
    {
        _messageRequestList.addElement(vMessageRequest);
    }

    public void addMessageRequest(int index, hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageRequest vMessageRequest)
    throws java.lang.IndexOutOfBoundsException
    {
        _messageRequestList.insertElementAt(vMessageRequest, index);
    }

    public java.util.Enumeration enumerateMessageRequest()
    {
        return _messageRequestList.elements();
    }

    public hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageRequest getMessageRequest(int index)
    throws java.lang.IndexOutOfBoundsException
    {

        if ((index < 0) || (index >= _messageRequestList.size())) {
            throw new IndexOutOfBoundsException("getMessageRequest: Index value '"+index+"' not in range [0.."+(_messageRequestList.size() - 1) + "]");
        }

        return (hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageRequest) _messageRequestList.elementAt(index);
    }

    public hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageRequest[] getMessageRequest()
    {
        int size = _messageRequestList.size();
        hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageRequest[] mArray = new hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageRequest[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageRequest) _messageRequestList.elementAt(index);
        }
        return mArray;
    }

    public int getMessageRequestCount()
    {
        return _messageRequestList.size();
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

    public void removeAllMessageRequest()
    {
        _messageRequestList.removeAllElements();
    }

    public hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageRequest removeMessageRequest(int index)
    {
        java.lang.Object obj = _messageRequestList.elementAt(index);
        _messageRequestList.removeElementAt(index);
        return (hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageRequest) obj;
    }

    public void setMessageRequest(int index, hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageRequest vMessageRequest)
    throws java.lang.IndexOutOfBoundsException
    {

        if ((index < 0) || (index >= _messageRequestList.size())) {
            throw new IndexOutOfBoundsException("setMessageRequest: Index value '"+index+"' not in range [0.." + (_messageRequestList.size() - 1) + "]");
        }
        _messageRequestList.setElementAt(vMessageRequest, index);
    }

    public void setMessageRequest(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageRequest[] messageRequestArray)
    {

        _messageRequestList.removeAllElements();
        for (int i = 0; i < messageRequestArray.length; i++) {
            _messageRequestList.addElement(messageRequestArray[i]);
        }
    }

    public static hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MaintainMessageRequest unmarshal(java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MaintainMessageRequest) Unmarshaller.unmarshal(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MaintainMessageRequest.class, reader);
    }

    public void validate()
    throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    }

    @Override
    public String toString() {
        return "MaintainMessageRequest [messageRequestList=" + _messageRequestList + "]";
    }

}
