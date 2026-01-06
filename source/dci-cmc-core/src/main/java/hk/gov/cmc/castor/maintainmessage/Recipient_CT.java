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

import hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.Action_ST;
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

/**
 * Class Recipient_CT.
 * 
 * @version $Revision$ $Date$
 */
// CMC-2025-034: CMC send RVD VIP request to DCI-CMC in RPC mode - BEGIN
public class Recipient_CT implements java.io.Serializable, Cloneable {
// CMC-2025-034: CMC send RVD VIP request to DCI-CMC in RPC mode - END


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _tranID
     */
    private java.lang.String _tranID;

    /**
     * Field _idpID
     */
    private java.lang.String _idpID;

    /**
     * Field _recipientID
     */
    private java.lang.String _recipientID;

    /**
     * Field _itemDate
     */
    private java.util.Date _itemDate;

    /**
     * Field _action
     */
    private hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.Action_ST _action;

    /**
     * Field _correlatedTranID
     */
    private java.lang.String _correlatedTranID;


      //----------------/
     //- Constructors -/
    //----------------/

    // CMC-2024-011: Enhance Maintain Message Module To Support iAM Smart Application Status - BEGIN
    private java.lang.String _recipientIDType;
    private java.lang.String _appRefNum;
    private java.lang.String _appStatus;
    private java.util.Date _appStatusUpdateDate;
    private java.lang.String _contactEmail;
    private java.lang.String _contactNum;
    private java.lang.String _miscInfo;
    // CMC-2024-011: Enhance Maintain Message Module To Support iAM Smart Application Status - END

    public Recipient_CT() 
     {
        super();
    } //-- hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.Recipient_CT()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'action'.
     * 
     * @return Action_ST
     * @return the value of field 'action'.
     */
    public hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.Action_ST getAction()
    {
        return this._action;
    } //-- hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.Action_ST getAction() 

    /**
     * Returns the value of field 'correlatedTranID'.
     * 
     * @return String
     * @return the value of field 'correlatedTranID'.
     */
    public java.lang.String getCorrelatedTranID()
    {
        return this._correlatedTranID;
    } //-- java.lang.String getCorrelatedTranID() 

    /**
     * Returns the value of field 'idpID'.
     * 
     * @return String
     * @return the value of field 'idpID'.
     */
    public java.lang.String getIdpID()
    {
        return this._idpID;
    } //-- java.lang.String getIdpID() 

    /**
     * Returns the value of field 'itemDate'.
     * 
     * @return Date
     * @return the value of field 'itemDate'.
     */
    public java.util.Date getItemDate()
    {
        return this._itemDate;
    } //-- java.util.Date getItemDate() 

    /**
     * Returns the value of field 'recipientID'.
     * 
     * @return String
     * @return the value of field 'recipientID'.
     */
    public java.lang.String getRecipientID()
    {
        return this._recipientID;
    } //-- java.lang.String getRecipientID() 

    /**
     * Returns the value of field 'tranID'.
     * 
     * @return String
     * @return the value of field 'tranID'.
     */
    public java.lang.String getTranID()
    {
        return this._tranID;
    } //-- java.lang.String getTranID() 

    // CMC-2024-011: Enhance Maintain Message Module To Support iAM Smart Application Status - BEGIN
    public void setAppRefNum(java.lang.String appRefNum) {
        this._appRefNum = appRefNum;
    }
    public String getAppRefNum() {
        return this._appRefNum;
    }
    public void setAppStatus(java.lang.String appStatus) {
        this._appStatus = appStatus;
    }
    public String getAppStatus() {
        return this._appStatus;
    }
    public void setAppStatusUpdateDate(java.util.Date appStatusUpdateDate) {
        this._appStatusUpdateDate = appStatusUpdateDate;
    }
    public java.util.Date getAppStatusUpdateDate() {
        return this._appStatusUpdateDate;
    }
    public void setRecipientIDType(java.lang.String recipientIDType) {
        this._recipientIDType = recipientIDType;
    }
    public String getRecipientIDType() {
        return this._recipientIDType;
    }
    public void setContactEmail(java.lang.String contactEmail) {
        this._contactEmail = contactEmail;
    }
    public String getContactEmail() {
        return this._contactEmail;
    }
    public void setContactNum(java.lang.String contactNum) {
        this._contactNum = contactNum;
    }
    public String getContactNum() {
        return this._contactNum;
    }
    public void setMiscInfo(java.lang.String miscInfo) {
        this._miscInfo = miscInfo;
    }
    public String getMiscInfo() {
        return this._miscInfo;
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
     * Sets the value of field 'action'.
     * 
     * @param action the value of field 'action'.
     */
    public void setAction(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.Action_ST action)
    {
        this._action = action;
    } //-- void setAction(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.Action_ST) 

    /**
     * Sets the value of field 'correlatedTranID'.
     * 
     * @param correlatedTranID the value of field 'correlatedTranID'
     */
    public void setCorrelatedTranID(java.lang.String correlatedTranID)
    {
        this._correlatedTranID = correlatedTranID;
    } //-- void setCorrelatedTranID(java.lang.String) 

    /**
     * Sets the value of field 'idpID'.
     * 
     * @param idpID the value of field 'idpID'.
     */
    public void setIdpID(java.lang.String idpID)
    {
        this._idpID = idpID;
    } //-- void setIdpID(java.lang.String) 

    /**
     * Sets the value of field 'itemDate'.
     * 
     * @param itemDate the value of field 'itemDate'.
     */
    public void setItemDate(java.util.Date itemDate)
    {
        this._itemDate = itemDate;
    } //-- void setItemDate(java.util.Date) 

    /**
     * Sets the value of field 'recipientID'.
     * 
     * @param recipientID the value of field 'recipientID'.
     */
    public void setRecipientID(java.lang.String recipientID)
    {
        this._recipientID = recipientID;
    } //-- void setRecipientID(java.lang.String) 

    /**
     * Sets the value of field 'tranID'.
     * 
     * @param tranID the value of field 'tranID'.
     */
    public void setTranID(java.lang.String tranID)
    {
        this._tranID = tranID;
    } //-- void setTranID(java.lang.String) 

    /**
     * Method unmarshal
     * 
     * 
     * 
     * @param reader
     * @return Recipient_CT
     */
    public static hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.Recipient_CT unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.Recipient_CT) Unmarshaller.unmarshal(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.Recipient_CT.class, reader);
    } //-- hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.Recipient_CT unmarshal(java.io.Reader) 

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
    public Recipient_CT clone() {
        try {
            Recipient_CT copy = (Recipient_CT) super.clone();
            copy._itemDate = (this._itemDate == null) ? null : (Date) this._itemDate.clone();
            copy._appStatusUpdateDate = (this._appStatusUpdateDate == null) ? null : (Date) this._appStatusUpdateDate.clone();
            return copy;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }
    // CMC-2025-034: CMC send RVD VIP request to DCI-CMC in RPC mode - END

    @Override
    public String toString() {
        return "Recipient_CT [_tranID=" + _tranID + ", _idpID=" + _idpID + ", _recipientID=" + _recipientID
                + ", _itemDate=" + _itemDate + ", _action=" + _action + ", _correlatedTranID=" + _correlatedTranID
                + ", _recipientIDType=" + _recipientIDType + ", _appRefNum=" + _appRefNum + ", _appStatus=" + _appStatus
                + ", _appStatusUpdateDate=" + _appStatusUpdateDate + ", _contactEmail=" + _contactEmail
                + ", _contactNum=" + _contactNum + ", _miscInfo=" + _miscInfo + "]";
    }

}
