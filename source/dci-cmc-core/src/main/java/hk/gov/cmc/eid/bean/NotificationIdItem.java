
package hk.gov.cmc.eid.bean;

import java.io.Serializable;

public class NotificationIdItem implements Serializable
{

    
    private static final long serialVersionUID = 1L;

    private String status;
    private String clientID;
    private String openID;
    private String notificationID;

    public NotificationIdItem() {}

    public NotificationIdItem(String status, String clientID, String openID, String notificationID ) {
        this.status = status;
        this.clientID = clientID;
        this.openID = openID;
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

    public String getOpenID() {
        return this.openID;
    }

    public void setOpenID(String paramopenid) {
        this.openID = paramopenid;
    }

    public String getNotificationID() {
        return this.notificationID;
    }

    public void setNotificationID(String paramnotificationid) {
        this.notificationID = paramnotificationid;
    }
}
