package hk.gov.cmc.eid.bean.pushNotification.request;

import java.io.Serializable;
import java.util.ArrayList;

public class PushNotificationItem implements Serializable {

    private static final long serialVersionUID = 1L;

    private ArrayList<String> notificationIDs;
    private String spID;
    private String messageType;
    private String operationType;
    private Long notificationDate;
    private String messageID;
    private String preMessageID;
    private String enMessage;
    private String tcMessage;
    private String scMessage;
    private String extraParams;

    public PushNotificationItem() {
    }

    public PushNotificationItem(ArrayList<String> notificationIDs, String spID, String messageType,
            String operationType, Long notificationDate, String messageID, String preMessageID, String enMessage,
            String tcMessage, String scMessage, String extraParams) {
        this.notificationIDs = notificationIDs;
        this.spID = spID;
        this.messageType = messageType;
        this.operationType = operationType;
        this.notificationDate = notificationDate;
        this.messageID = messageID;
        this.preMessageID = preMessageID;
        this.enMessage = enMessage;
        this.tcMessage = tcMessage;
        this.scMessage = scMessage;
        this.extraParams = extraParams;
    }

    public ArrayList<String> getNotificationIDs() {
        return notificationIDs;
    }

    public void setNotificationIDs(ArrayList<String> notificationIDs) {
        this.notificationIDs = notificationIDs;
    }

    public String getSpID() {
        return spID;
    }

    public void setSpID(String spID) {
        this.spID = spID;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public Long getNotificationDate() {
        return notificationDate;
    }

    public void setNotificationDate(Long notificationDate) {
        this.notificationDate = notificationDate;
    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public String getPreMessageID() {
        return preMessageID;
    }

    public void setPreMessageID(String preMessageID) {
        this.preMessageID = preMessageID;
    }

    public String getEnMessage() {
        return enMessage;
    }

    public void setEnMessage(String enMessage) {
        this.enMessage = enMessage;
    }

    public String getTcMessage() {
        return tcMessage;
    }

    public void setTcMessage(String tcMessage) {
        this.tcMessage = tcMessage;
    }

    public String getScMessage() {
        return scMessage;
    }

    public void setScMessage(String scMessage) {
        this.scMessage = scMessage;
    }

    public String getExtraParams() {
        return extraParams;
    }

    public void setExtraParams(String extraParams) {
        this.extraParams = extraParams;
    }

    @Override
    public String toString() {
        return "PushNotificationItem [notificationIDs=" + notificationIDs + ", spID=" + spID + ", messageType="
                + messageType + ", operationType=" + operationType + ", notificationDate=" + notificationDate
                + ", messageID=" + messageID + ", preMessageID=" + preMessageID + ", enMessage=" + enMessage
                + ", tcMessage=" + tcMessage + ", scMessage=" + scMessage + ", extraParams=" + extraParams + "]";
    };

}
