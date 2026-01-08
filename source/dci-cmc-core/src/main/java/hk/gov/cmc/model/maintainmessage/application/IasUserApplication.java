package hk.gov.cmc.model.maintainmessage.application;

import java.io.Serializable;
import java.sql.Timestamp;

public class IasUserApplication implements Serializable {

    private static final long serialVersionUID = -1L;
    protected String clientId = null;
    protected String serviceProviderId = null;
    protected String recipientId = null;
    protected String iasApplicationId = null;
    protected String recipientIdType = null;
    protected String hkidEncrypted = null;
    protected String notiId = null;
    protected String tranId = null;
    protected String actionTranId = null;
    protected String appRefNum = null;
    protected String readInd = null;
    protected String iasNotiStatus = null;
    protected String txId = null;
    protected Timestamp sentDt = null;
    protected String iasDeliveryStatus = null;
    protected String housekeepInd = null;
    protected String optIn = null;
    protected String titleEn = null;
    protected String titleTc = null;
    protected String titleSc = null;
    protected String status = null;
    protected String appStatus = null;
    protected Timestamp appStatusUpdateDate = null;
    protected String contactNum = null;
    protected String contactEmail = null;
    protected String miscInfo = null;
    protected String operationType = null;
    protected String iasOptCheckInd = null;
    protected String iasOptSpId = null;
    protected String mapNotiId = null;
    protected Timestamp createDt = null;
    protected String prevIasApplicationId = null;
    protected String prevIasNotiResult = null;

    public IasUserApplication() {
        super();
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getServiceProviderId() {
        return serviceProviderId;
    }

    public void setServiceProviderId(String serviceProviderId) {
        this.serviceProviderId = serviceProviderId;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    public String getIasApplicationId() {
        return iasApplicationId;
    }

    public void setIasApplicationId(String iasApplicationId) {
        this.iasApplicationId = iasApplicationId;
    }

    public String getRecipientIdType() {
        return recipientIdType;
    }

    public void setRecipientIdType(String recipientIdType) {
        this.recipientIdType = recipientIdType;
    }

    public String getHkidEncrypted() {
        return hkidEncrypted;
    }

    public void setHkidEncrypted(String hkidEncrypted) {
        this.hkidEncrypted = hkidEncrypted;
    }

    public String getNotiId() {
        return notiId;
    }

    public void setNotiId(String notiId) {
        this.notiId = notiId;
    }

    public String getTranId() {
        return tranId;
    }

    public void setTranId(String tranId) {
        this.tranId = tranId;
    }

    public String getActionTranId() {
        return actionTranId;
    }

    public void setActionTranId(String actionTranId) {
        this.actionTranId = actionTranId;
    }

    public String getReadInd() {
        return readInd;
    }

    public void setReadInd(String readInd) {
        this.readInd = readInd;
    }

    public String getIasNotiStatus() {
        return iasNotiStatus;
    }

    public void setIasNotiStatus(String iasNotiStatus) {
        this.iasNotiStatus = iasNotiStatus;
    }

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }

    public Timestamp getSentDt() {
        return sentDt;
    }

    public void setSentDt(Timestamp sentDt) {
        this.sentDt = sentDt;
    }

    public String getIasDeliveryStatus() {
        return iasDeliveryStatus;
    }

    public void setIasDeliveryStatus(String iasDeliveryStatus) {
        this.iasDeliveryStatus = iasDeliveryStatus;
    }

    public String getHousekeepInd() {
        return housekeepInd;
    }

    public void setHousekeepInd(String housekeepInd) {
        this.housekeepInd = housekeepInd;
    }

    public String getOptIn() {
        return optIn;
    }

    public void setOptIn(String optIn) {
        this.optIn = optIn;
    }

    public String getTitleEn() {
        return titleEn;
    }

    public void setTitleEn(String titleEn) {
        this.titleEn = titleEn;
    }

    public String getTitleTc() {
        return titleTc;
    }

    public void setTitleTc(String titleTc) {
        this.titleTc = titleTc;
    }

    public String getTitleSc() {
        return titleSc;
    }

    public void setTitleSc(String titleSc) {
        this.titleSc = titleSc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAppStatus() {
        return appStatus;
    }

    public void setAppStatus(String appStatus) {
        this.appStatus = appStatus;
    }

    public Timestamp getAppStatusUpdateDate() {
        return appStatusUpdateDate;
    }

    public void setAppStatusUpdateDate(Timestamp appStatusUpdateDate) {
        this.appStatusUpdateDate = appStatusUpdateDate;
    }

    public String getIasOptCheckInd() {
        return iasOptCheckInd;
    }

    public void setIasOptCheckInd(String iasOptCheckInd) {
        this.iasOptCheckInd = iasOptCheckInd;
    }

    public String getIasOptSpId() {
        return iasOptSpId;
    }

    public void setIasOptSpId(String iasOptSpId) {
        this.iasOptSpId = iasOptSpId;
    }

    public String getMapNotiId() {
        return mapNotiId;
    }

    public void setMapNotiId(String mapNotiId) {
        this.mapNotiId = mapNotiId;
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

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getAppRefNum() {
        return appRefNum;
    }

    public void setAppRefNum(String appRefNum) {
        this.appRefNum = appRefNum;
    }

    public Timestamp getCreateDt() {
        return createDt;
    }

    public void setCreateDt(Timestamp createDt) {
        this.createDt = createDt;
    }

    public String getPrevIasApplicationId() {
        return prevIasApplicationId;
    }

    public void setPrevIasApplicationId(String prevIasApplicationId) {
        this.prevIasApplicationId = prevIasApplicationId;
    }

    public String getPrevIasNotiResult() {
        return prevIasNotiResult;
    }

    public void setPrevIasNotiResult(String prevIasNotiResult) {
        this.prevIasNotiResult = prevIasNotiResult;
    }

    @Override
    public String toString() {
        return "IasUserApplication [clientId=" + clientId + ", serviceProviderId=" + serviceProviderId
                + ", recipientId=" + recipientId + ", iasApplicationId=" + iasApplicationId + ", recipientIdType="
                + recipientIdType + ", hkidEncrypted=" + hkidEncrypted + ", notiId=" + notiId + ", tranId=" + tranId
                + ", actionTranId=" + actionTranId + ", appRefNum=" + appRefNum + ", readInd=" + readInd + ", iasNotiStatus=" 
                + iasNotiStatus + ", txId=" + txId + ", sentDt=" + sentDt + ", iasDeliveryStatus=" + iasDeliveryStatus 
                + ", housekeepInd=" + housekeepInd + ", optIn=" + optIn + ", titleEn=" + titleEn + ", titleTc=" + titleTc 
                + ", titleSc=" + titleSc + ", status=" + status + ", appStatus=" + appStatus + ", appStatusUpdateDate="
                + appStatusUpdateDate + ", contactNum=" + contactNum + ", contactEmail=" + contactEmail + ", miscInfo="
                + miscInfo + ", operationType=" + operationType + ", iasOptCheckInd=" + iasOptCheckInd + ", iasOptSpId="
                + iasOptSpId + ", mapNotiId=" + mapNotiId + ", createDt=" + createDt + ", prevIasApplicationId="
                + prevIasApplicationId + ", prevIasNotiResult=" + prevIasNotiResult + "]";
    }

}
