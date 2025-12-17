package hk.gov.cmc.eid.bean.switchNotificationID.response;

import java.io.Serializable;

public class NotificationIdByHKIDItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private String status;
    private String clientID;
    private String HKID;
    private String notificationID;

    public NotificationIdByHKIDItem() {
    }

    public NotificationIdByHKIDItem(String status, String clientID, String HKID, String notificationID) {
        this.status = status;
        this.clientID = clientID;
        this.HKID = HKID;
        this.notificationID = notificationID;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String paramstatus) {
        this.status = paramstatus;
    }

    public String getClientID() {
        return this.clientID;
    }

    public void setClientID(String paramclientid) {
        this.clientID = paramclientid;
    }

    public String getHKID() {
        return HKID;
    }

    public void setHKID(String hKID) {
        HKID = hKID;
    }

    public String getNotificationID() {
        return this.notificationID;
    }

    public void setNotificationID(String paramnotificationid) {
        this.notificationID = paramnotificationid;
    }
}
