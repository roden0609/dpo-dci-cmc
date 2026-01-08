package hk.gov.cmc.model.maintainmessage.todoitem;

import java.io.Serializable;
import java.sql.Timestamp;

public class IasUserToDoItem implements Serializable {

    private static final long serialVersionUID = 6119286463820410304L;
    private String clientId = null;
    private String serviceProviderId = null;
    private String recipientId = null;
    private String iasToDoItemId = null;
    private String recipientIdType = null;
    private String hkidEncrypted = null;
    private String notiId = null;
    private String readInd = null;
    private String iasNotiStatus = null;
    private String txId = null;
    private Timestamp sentDt = null;
    private String iasDeliveryStatus = null;
    private String housekeepInd = null;
    private String optIn = null;
    private String titleEn = null;
    private String titleTc = null;
    private String titleSc = null;
    private String status = null;
    private String iasAutoCreateInd = null;
    private String iasOptCheckInd = null;
    private String iasOptSpId = null;
    private String mapNotiId = null;
    private String itemDate = null;
    private String operationType = null;
    private Timestamp createDt = null;
    private String tranId = null;
    private String prevTranId = null;
    private String prevIasToDoItemId = null;

    public IasUserToDoItem() {
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

    public String getIasToDoItemId() {
        return iasToDoItemId;
    }

    public void setIasToDoItemId(String iasToDoItemId) {
        this.iasToDoItemId = iasToDoItemId;
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

    public String getIasAutoCreateInd() {
        return iasAutoCreateInd;
    }

    public void setIasAutoCreateInd(String iasAutoCreateInd) {
        this.iasAutoCreateInd = iasAutoCreateInd;
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

    public String getItemDate() {
        return itemDate;
    }

    public void setItemDate(String itemDate) {
        this.itemDate = itemDate;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public Timestamp getCreateDt() {
        return createDt;
    }

    public void setCreateDt(Timestamp createDt) {
        this.createDt = createDt;
    }

    public String getPrevIasToDoItemId() {
        return prevIasToDoItemId;
    }

    public void setPrevIasToDoItemId(String prevIasToDoItemId) {
        this.prevIasToDoItemId = prevIasToDoItemId;
    }

    public String getTranId() {
        return tranId;
    }

    public void setTranId(String tranId) {
        this.tranId = tranId;
    }

    public String getPrevTranId() {
        return prevTranId;
    }

    public void setPrevTranId(String prevTranId) {
        this.prevTranId = prevTranId;
    }

    @Override
    public String toString() {
        return "IasUserToDoItem [clientId=" + clientId + ", serviceProviderId=" + serviceProviderId + ", recipientId="
                + recipientId + ", iasToDoItemId=" + iasToDoItemId + ", recipientIdType=" + recipientIdType
                + ", hkidEncrypted=" + hkidEncrypted + ", notiId=" + notiId + ", readInd=" + readInd
                + ", iasNotiStatus=" + iasNotiStatus + ", txId=" + txId + ", sentDt=" + sentDt + ", iasDeliveryStatus="
                + iasDeliveryStatus + ", housekeepInd=" + housekeepInd + ", optIn=" + optIn + ", titleEn=" + titleEn
                + ", titleTc=" + titleTc + ", titleSc=" + titleSc + ", status=" + status + ", iasAutoCreateInd="
                + iasAutoCreateInd + ", iasOptCheckInd=" + iasOptCheckInd + ", iasOptSpId=" + iasOptSpId
                + ", mapNotiId=" + mapNotiId + ", itemDate=" + itemDate + ", operationType=" + operationType
                + ", createDt=" + createDt + ", tranId=" + tranId + ", prevTranId=" + prevTranId
                + ", prevIasToDoItemId=" + prevIasToDoItemId + "]";
    }

}
