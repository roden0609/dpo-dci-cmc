
package hk.gov.cmc.eid.bean;

import java.io.Serializable;

public class NotificationResultItem implements Serializable {

    private static final long serialVersionUID = 1L;

    private String notificationID;
    private String messageID;
    private String status;

    public NotificationResultItem() {
    };

    public NotificationResultItem(String PnotificationID,
            String PmessageID,
            String Pstatus) {
        this.notificationID = PnotificationID;
        this.messageID = PmessageID;
        this.status = Pstatus;
    }

    public String getMessageID() {
        return this.messageID;
    }

    public void setMessageID(String msgid) {
        this.messageID = msgid;
    }

    public String getNotificationID() {
        return this.notificationID;
    }

    public void setNotificationID(String notiId) {
        this.notificationID = notiId;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "NotificationResultItem [notificationID=" + notificationID + ", messageID=" + messageID + ", status="
                + status + "]";
    }

}
