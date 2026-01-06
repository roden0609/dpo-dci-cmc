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

import hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.CommonIndicator_ST;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Vector;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class MessageRequest_CT.
 * 
 * @version $Revision$ $Date$
 */
public class MessageRequest_CT implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _portalID
     */
    private java.lang.String _portalID;

    /**
     * Field _EMessage
     */
    private hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.EMessage _EMessage;

    /**
     * Field _toDoItem
     */
    private hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.ToDoItem _toDoItem;

    // CMC-2024-011: Enhance Maintain Message Module To Support iAM Smart Application Status - BEGIN
    private hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.Application _application;
    // CMC-2024-011: Enhance Maintain Message Module To Support iAM Smart Application Status - END

    /**
     * Field _billAccountInfoInd
     */
    private hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.CommonIndicator_ST _billAccountInfoInd;

    /**
     * Field _metaDataList
     */
    private java.util.Vector _metaDataList;


      //----------------/
     //- Constructors -/
    //----------------/

    public MessageRequest_CT() 
     {
        super();
        _metaDataList = new Vector();
    } //-- hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageRequest_CT()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addMetaData
     * 
     * 
     * 
     * @param vMetaData
     */
    public void addMetaData(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MetaData vMetaData)
        throws java.lang.IndexOutOfBoundsException
    {
        _metaDataList.addElement(vMetaData);
    } //-- void addMetaData(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MetaData) 

    /**
     * Method addMetaData
     * 
     * 
     * 
     * @param index
     * @param vMetaData
     */
    public void addMetaData(int index, hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MetaData vMetaData)
        throws java.lang.IndexOutOfBoundsException
    {
        _metaDataList.insertElementAt(vMetaData, index);
    } //-- void addMetaData(int, hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MetaData) 

    /**
     * Method enumerateMetaData
     * 
     * 
     * 
     * @return Enumeration
     */
    public java.util.Enumeration enumerateMetaData()
    {
        return _metaDataList.elements();
    } //-- java.util.Enumeration enumerateMetaData() 

    /**
     * Returns the value of field 'billAccountInfoInd'.
     * 
     * @return CommonIndicator_ST
     * @return the value of field 'billAccountInfoInd'.
     */
    public hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.CommonIndicator_ST getBillAccountInfoInd()
    {
        return this._billAccountInfoInd;
    } //-- hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.CommonIndicator_ST getBillAccountInfoInd() 

    /**
     * Returns the value of field 'EMessage'.
     * 
     * @return EMessage
     * @return the value of field 'EMessage'.
     */
    public hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.EMessage getEMessage()
    {
        return this._EMessage;
    } //-- hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.EMessage getEMessage() 

    /**
     * Method getMetaData
     * 
     * 
     * 
     * @param index
     * @return MetaData
     */
    public hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MetaData getMetaData(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index >= _metaDataList.size())) {
            throw new IndexOutOfBoundsException("getMetaData: Index value '"+index+"' not in range [0.."+(_metaDataList.size() - 1) + "]");
        }
        
        return (hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MetaData) _metaDataList.elementAt(index);
    } //-- hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MetaData getMetaData(int) 

    /**
     * Method getMetaData
     * 
     * 
     * 
     * @return MetaData
     */
    public hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MetaData[] getMetaData()
    {
        int size = _metaDataList.size();
        hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MetaData[] mArray = new hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MetaData[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MetaData) _metaDataList.elementAt(index);
        }
        return mArray;
    } //-- hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MetaData[] getMetaData() 

    /**
     * Method getMetaDataCount
     * 
     * 
     * 
     * @return int
     */
    public int getMetaDataCount()
    {
        return _metaDataList.size();
    } //-- int getMetaDataCount() 

    /**
     * Returns the value of field 'portalID'.
     * 
     * @return String
     * @return the value of field 'portalID'.
     */
    public java.lang.String getPortalID()
    {
        return this._portalID;
    } //-- java.lang.String getPortalID() 

    /**
     * Returns the value of field 'toDoItem'.
     * 
     * @return ToDoItem
     * @return the value of field 'toDoItem'.
     */
    public hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.ToDoItem getToDoItem()
    {
        return this._toDoItem;
    } //-- hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.ToDoItem getToDoItem() 

    // CMC-2024-011: Enhance Maintain Message Module To Support iAM Smart Application Status - BEGIN
    public hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.Application getApplication() {
        return this._application;
    }
    // CMC-2024-011: Enhance Maintain Message Module To Support iAM Smart Application Status - END

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
     * Method removeAllMetaData
     * 
     */
    public void removeAllMetaData()
    {
        _metaDataList.removeAllElements();
    } //-- void removeAllMetaData() 

    /**
     * Method removeMetaData
     * 
     * 
     * 
     * @param index
     * @return MetaData
     */
    public hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MetaData removeMetaData(int index)
    {
        java.lang.Object obj = _metaDataList.elementAt(index);
        _metaDataList.removeElementAt(index);
        return (hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MetaData) obj;
    } //-- hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MetaData removeMetaData(int) 

    /**
     * Sets the value of field 'billAccountInfoInd'.
     * 
     * @param billAccountInfoInd the value of field
     * 'billAccountInfoInd'.
     */
    public void setBillAccountInfoInd(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.CommonIndicator_ST billAccountInfoInd)
    {
        this._billAccountInfoInd = billAccountInfoInd;
    } //-- void setBillAccountInfoInd(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.CommonIndicator_ST) 

    /**
     * Sets the value of field 'EMessage'.
     * 
     * @param EMessage the value of field 'EMessage'.
     */
    public void setEMessage(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.EMessage EMessage)
    {
        this._EMessage = EMessage;
    } //-- void setEMessage(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.EMessage) 

    /**
     * Method setMetaData
     * 
     * 
     * 
     * @param index
     * @param vMetaData
     */
    public void setMetaData(int index, hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MetaData vMetaData)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index >= _metaDataList.size())) {
            throw new IndexOutOfBoundsException("setMetaData: Index value '"+index+"' not in range [0.." + (_metaDataList.size() - 1) + "]");
        }
        _metaDataList.setElementAt(vMetaData, index);
    } //-- void setMetaData(int, hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MetaData) 

    /**
     * Method setMetaData
     * 
     * 
     * 
     * @param metaDataArray
     */
    public void setMetaData(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MetaData[] metaDataArray)
    {
        //-- copy array
        _metaDataList.removeAllElements();
        for (int i = 0; i < metaDataArray.length; i++) {
            _metaDataList.addElement(metaDataArray[i]);
        }
    } //-- void setMetaData(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MetaData) 

    /**
     * Sets the value of field 'portalID'.
     * 
     * @param portalID the value of field 'portalID'.
     */
    public void setPortalID(java.lang.String portalID)
    {
        this._portalID = portalID;
    } //-- void setPortalID(java.lang.String) 

    /**
     * Sets the value of field 'toDoItem'.
     * 
     * @param toDoItem the value of field 'toDoItem'.
     */
    public void setToDoItem(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.ToDoItem toDoItem)
    {
        this._toDoItem = toDoItem;
    } //-- void setToDoItem(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.ToDoItem) 

    // CMC-2024-011: Enhance Maintain Message Module To Support iAM Smart Application Status - BEGIN
    public void setApplication(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.Application application) {
        this._application = application;
    }
    // CMC-2024-011: Enhance Maintain Message Module To Support iAM Smart Application Status - END

    /**
     * Method unmarshal
     * 
     * 
     * 
     * @param reader
     * @return MessageRequest_CT
     */
    public static hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageRequest_CT unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageRequest_CT) Unmarshaller.unmarshal(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageRequest_CT.class, reader);
    } //-- hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageRequest_CT unmarshal(java.io.Reader) 

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

    // CMC-2024-011: Enhance Maintain Message Module To Support iAM Smart Application Status - BEGIN
    @Override
    public String toString() {
        // CMC-2025-034: CMC send RVD VIP request to DCI-CMC in RPC mode - BEGIN
        return "MessageRequest_CT [_portalID=" + _portalID + ", _EMessage=" + _EMessage + ", _toDoItem=" + _toDoItem
            + ", _application=" + _application + ", _billAccountInfoInd=" + _billAccountInfoInd + ", _metaDataList="
            + _metaDataList + "]";
        // CMC-2025-034: CMC send RVD VIP request to DCI-CMC in RPC mode - END
    }
    // CMC-2024-011: Enhance Maintain Message Module To Support iAM Smart Application Status - END

}
