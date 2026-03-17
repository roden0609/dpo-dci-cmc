package hk.gov.cmc.model.maintainmessage.request;

import java.util.Date;

import hk.gov.cmc.model.maintainmessage.action.Action;
import hk.gov.cmc.model.maintainmessage.status.AppStatus;

public class Recipient {

    private String tranId;
    private String idpId;
    private String recipientId;
    private String appRefNum;
    private String recipientIdType;
    private Date itemDate;
    private Action action;
    private String correlatedTranId;
    private AppStatus appStatus;
    private Date appStatusUpdateDate;
    private String contactNum;
    private String contactEmail;
    private String miscInfo;

    public String getTranId() {
        return tranId;
    }

    public void setTranId(String tranId) {
        this.tranId = tranId;
    }

    public String getIdpId() {
        return idpId;
    }

    public void setIdpId(String idpId) {
        this.idpId = idpId;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    public String getAppRefNum() {
        return appRefNum;
    }

    public void setAppRefNum(String appRefNum) {
        this.appRefNum = appRefNum;
    }

    public String getRecipientIdType() {
        return recipientIdType;
    }

    public void setRecipientIdType(String recipientIdType) {
        this.recipientIdType = recipientIdType;
    }

    public Date getItemDate() {
        return itemDate;
    }

    public void setItemDate(Date itemDate) {
        this.itemDate = itemDate;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public String getCorrelatedTranId() {
        return correlatedTranId;
    }

    public void setCorrelatedTranId(String correlatedTranId) {
        this.correlatedTranId = correlatedTranId;
    }

    public AppStatus getAppStatus() {
        return appStatus;
    }

    public void setAppStatus(AppStatus appStatus) {
        this.appStatus = appStatus;
    }

    public Date getAppStatusUpdateDate() {
        return appStatusUpdateDate;
    }

    public void setAppStatusUpdateDate(Date appStatusUpdateDate) {
        this.appStatusUpdateDate = appStatusUpdateDate;
    }

    public String getContactNum() {
        return contactNum;
    }

    public void setContactNum(String contactNum) {
        this.contactNum = contactNum;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getMiscInfo() {
        return miscInfo;
    }

    public void setMiscInfo(String miscInfo) {
        this.miscInfo = miscInfo;
    }

    @Override
    public String toString() {
        return "Recipient [tranId=" + tranId + ", idpId=" + idpId + ", recipientId=" + recipientId + ", appRefNum="
                + appRefNum + ", recipientIdType=" + recipientIdType + ", itemDate=" + itemDate + ", action=" + action
                + ", correlatedTranId=" + correlatedTranId + ", appStatus=" + appStatus + ", appStatusUpdateDate="
                + appStatusUpdateDate + ", contactNum=" + contactNum + ", contactEmail=" + contactEmail + ", miscInfo="
                + miscInfo + "]";
    }

}
