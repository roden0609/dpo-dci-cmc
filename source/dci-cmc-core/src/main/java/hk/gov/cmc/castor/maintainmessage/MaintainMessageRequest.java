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
 * Class MaintainMessageRequest.
 * 
 * @version $Revision$ $Date$
 */
public class MaintainMessageRequest implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _messageRequestList
     */
    private java.util.Vector _messageRequestList;


      //----------------/
     //- Constructors -/
    //----------------/

    public MaintainMessageRequest() 
     {
        super();
        _messageRequestList = new Vector();
    } //-- hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MaintainMessageRequest()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addMessageRequest
     * 
     * 
     * 
     * @param vMessageRequest
     */
    public void addMessageRequest(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageRequest vMessageRequest)
        throws java.lang.IndexOutOfBoundsException
    {
        _messageRequestList.addElement(vMessageRequest);
    } //-- void addMessageRequest(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageRequest) 

    /**
     * Method addMessageRequest
     * 
     * 
     * 
     * @param index
     * @param vMessageRequest
     */
    public void addMessageRequest(int index, hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageRequest vMessageRequest)
        throws java.lang.IndexOutOfBoundsException
    {
        _messageRequestList.insertElementAt(vMessageRequest, index);
    } //-- void addMessageRequest(int, hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageRequest) 

    /**
     * Method enumerateMessageRequest
     * 
     * 
     * 
     * @return Enumeration
     */
    public java.util.Enumeration enumerateMessageRequest()
    {
        return _messageRequestList.elements();
    } //-- java.util.Enumeration enumerateMessageRequest() 

    /**
     * Method getMessageRequest
     * 
     * 
     * 
     * @param index
     * @return MessageRequest
     */
    public hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageRequest getMessageRequest(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index >= _messageRequestList.size())) {
            throw new IndexOutOfBoundsException("getMessageRequest: Index value '"+index+"' not in range [0.."+(_messageRequestList.size() - 1) + "]");
        }
        
        return (hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageRequest) _messageRequestList.elementAt(index);
    } //-- hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageRequest getMessageRequest(int) 

    /**
     * Method getMessageRequest
     * 
     * 
     * 
     * @return MessageRequest
     */
    public hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageRequest[] getMessageRequest()
    {
        int size = _messageRequestList.size();
        hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageRequest[] mArray = new hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageRequest[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageRequest) _messageRequestList.elementAt(index);
        }
        return mArray;
    } //-- hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageRequest[] getMessageRequest() 

    /**
     * Method getMessageRequestCount
     * 
     * 
     * 
     * @return int
     */
    public int getMessageRequestCount()
    {
        return _messageRequestList.size();
    } //-- int getMessageRequestCount() 

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
     * Method removeAllMessageRequest
     * 
     */
    public void removeAllMessageRequest()
    {
        _messageRequestList.removeAllElements();
    } //-- void removeAllMessageRequest() 

    /**
     * Method removeMessageRequest
     * 
     * 
     * 
     * @param index
     * @return MessageRequest
     */
    public hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageRequest removeMessageRequest(int index)
    {
        java.lang.Object obj = _messageRequestList.elementAt(index);
        _messageRequestList.removeElementAt(index);
        return (hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageRequest) obj;
    } //-- hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageRequest removeMessageRequest(int) 

    /**
     * Method setMessageRequest
     * 
     * 
     * 
     * @param index
     * @param vMessageRequest
     */
    public void setMessageRequest(int index, hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageRequest vMessageRequest)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index >= _messageRequestList.size())) {
            throw new IndexOutOfBoundsException("setMessageRequest: Index value '"+index+"' not in range [0.." + (_messageRequestList.size() - 1) + "]");
        }
        _messageRequestList.setElementAt(vMessageRequest, index);
    } //-- void setMessageRequest(int, hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageRequest) 

    /**
     * Method setMessageRequest
     * 
     * 
     * 
     * @param messageRequestArray
     */
    public void setMessageRequest(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageRequest[] messageRequestArray)
    {
        //-- copy array
        _messageRequestList.removeAllElements();
        for (int i = 0; i < messageRequestArray.length; i++) {
            _messageRequestList.addElement(messageRequestArray[i]);
        }
    } //-- void setMessageRequest(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageRequest) 

    /**
     * Method unmarshal
     * 
     * 
     * 
     * @param reader
     * @return MaintainMessageRequest
     */
    public static hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MaintainMessageRequest unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MaintainMessageRequest) Unmarshaller.unmarshal(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MaintainMessageRequest.class, reader);
    } //-- hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MaintainMessageRequest unmarshal(java.io.Reader) 

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
        return "MaintainMessageRequest [messageRequestList=" + _messageRequestList + "]";
    }
    // CMC-2025-034: CMC send RVD VIP request to DCI-CMC in RPC mode - END
}
