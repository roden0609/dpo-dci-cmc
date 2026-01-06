
package hk.gov.cmc.castor.maintainmessage;

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

public class Recipient_CT implements java.io.Serializable, Cloneable {

    private java.lang.String _tranID;

    private java.lang.String _idpID;

    private java.lang.String _recipientID;

    private java.util.Date _itemDate;

    private hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.Action_ST _action;

    private java.lang.String _correlatedTranID;

    private java.lang.String _recipientIDType;
    private java.lang.String _appRefNum;
    private java.lang.String _appStatus;
    private java.util.Date _appStatusUpdateDate;
    private java.lang.String _contactEmail;
    private java.lang.String _contactNum;
    private java.lang.String _miscInfo;

    public Recipient_CT()
    {
        super();
    }

    public hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.Action_ST getAction()
    {
        return this._action;
    }

    public java.lang.String getCorrelatedTranID()
    {
        return this._correlatedTranID;
    }

    public java.lang.String getIdpID()
    {
        return this._idpID;
    }

    public java.util.Date getItemDate()
    {
        return this._itemDate;
    }

    public java.lang.String getRecipientID()
    {
        return this._recipientID;
    }

    public java.lang.String getTranID()
    {
        return this._tranID;
    }

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

    public void setAction(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.Action_ST action)
    {
        this._action = action;
    }

    public void setCorrelatedTranID(java.lang.String correlatedTranID)
    {
        this._correlatedTranID = correlatedTranID;
    }

    public void setIdpID(java.lang.String idpID)
    {
        this._idpID = idpID;
    }

    public void setItemDate(java.util.Date itemDate)
    {
        this._itemDate = itemDate;
    }

    public void setRecipientID(java.lang.String recipientID)
    {
        this._recipientID = recipientID;
    }

    public void setTranID(java.lang.String tranID)
    {
        this._tranID = tranID;
    }

    public static hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.Recipient_CT unmarshal(java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.Recipient_CT) Unmarshaller.unmarshal(hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.Recipient_CT.class, reader);
    }

    public void validate()
    throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    }

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

    @Override
    public String toString() {
        return "Recipient_CT [_tranID=" + _tranID + ", _idpID=" + _idpID + ", _recipientID=" + _recipientID
        + ", _itemDate=" + _itemDate + ", _action=" + _action + ", _correlatedTranID=" + _correlatedTranID
        + ", _recipientIDType=" + _recipientIDType + ", _appRefNum=" + _appRefNum + ", _appStatus=" + _appStatus
        + ", _appStatusUpdateDate=" + _appStatusUpdateDate + ", _contactEmail=" + _contactEmail
        + ", _contactNum=" + _contactNum + ", _miscInfo=" + _miscInfo + "]";
    }

}
