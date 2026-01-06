










package hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage;

  
 


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






public class MessageRequest_CT implements java.io.Serializable {


      
     
    

    


    private java.lang.String _portalID;

    


    private hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.EMessage _EMessage;

    


    private hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.ToDoItem _toDoItem;

    
    private hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.Application _application;
    

    


    private hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.CommonIndicator_ST _billAccountInfoInd;

    


    private java.util.Vector _metaDataList;


      
     
    

    public MessageRequest_CT() 
     {
        super();
        _metaDataList = new Vector();
    } 


      
     
    

    






    public void addMetaData(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MetaData vMetaData)
        throws java.lang.IndexOutOfBoundsException
    {
        _metaDataList.addElement(vMetaData);
    } 

    







    public void addMetaData(int index, hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MetaData vMetaData)
        throws java.lang.IndexOutOfBoundsException
    {
        _metaDataList.insertElementAt(vMetaData, index);
    } 

    






    public java.util.Enumeration enumerateMetaData()
    {
        return _metaDataList.elements();
    } 

    





    public hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.CommonIndicator_ST getBillAccountInfoInd()
    {
        return this._billAccountInfoInd;
    } 

    





    public hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.EMessage getEMessage()
    {
        return this._EMessage;
    } 

    







    public hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MetaData getMetaData(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        
        if ((index < 0) || (index >= _metaDataList.size())) {
            throw new IndexOutOfBoundsException("getMetaData: Index value '"+index+"' not in range [0.."+(_metaDataList.size() - 1) + "]");
        }
        
        return (hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MetaData) _metaDataList.elementAt(index);
    } 

    






    public hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MetaData[] getMetaData()
    {
        int size = _metaDataList.size();
        hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MetaData[] mArray = new hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MetaData[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MetaData) _metaDataList.elementAt(index);
        }
        return mArray;
    } 

    






    public int getMetaDataCount()
    {
        return _metaDataList.size();
    } 

    





    public java.lang.String getPortalID()
    {
        return this._portalID;
    } 

    





    public hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.ToDoItem getToDoItem()
    {
        return this._toDoItem;
    } 

    
    public hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.Application getApplication() {
        return this._application;
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

    



    public void removeAllMetaData()
    {
        _metaDataList.removeAllElements();
    } 

    







    public hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MetaData removeMetaData(int index)
    {
        java.lang.Object obj = _metaDataList.elementAt(index);
        _metaDataList.removeElementAt(index);
        return (hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MetaData) obj;
    } 

    





    public void setBillAccountInfoInd(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.CommonIndicator_ST billAccountInfoInd)
    {
        this._billAccountInfoInd = billAccountInfoInd;
    } 

    




    public void setEMessage(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.EMessage EMessage)
    {
        this._EMessage = EMessage;
    } 

    







    public void setMetaData(int index, hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MetaData vMetaData)
        throws java.lang.IndexOutOfBoundsException
    {
        
        if ((index < 0) || (index >= _metaDataList.size())) {
            throw new IndexOutOfBoundsException("setMetaData: Index value '"+index+"' not in range [0.." + (_metaDataList.size() - 1) + "]");
        }
        _metaDataList.setElementAt(vMetaData, index);
    } 

    






    public void setMetaData(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MetaData[] metaDataArray)
    {
        
        _metaDataList.removeAllElements();
        for (int i = 0; i < metaDataArray.length; i++) {
            _metaDataList.addElement(metaDataArray[i]);
        }
    } 

    




    public void setPortalID(java.lang.String portalID)
    {
        this._portalID = portalID;
    } 

    




    public void setToDoItem(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.ToDoItem toDoItem)
    {
        this._toDoItem = toDoItem;
    } 

    
    public void setApplication(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.Application application) {
        this._application = application;
    }
    

    







    public static hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageRequest_CT unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageRequest_CT) Unmarshaller.unmarshal(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageRequest_CT.class, reader);
    } 

    



    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } 

    
    @Override
    public String toString() {
        
        return "MessageRequest_CT [_portalID=" + _portalID + ", _EMessage=" + _EMessage + ", _toDoItem=" + _toDoItem
            + ", _application=" + _application + ", _billAccountInfoInd=" + _billAccountInfoInd + ", _metaDataList="
            + _metaDataList + "]";
        
    }
    

}
