
package hk.gov.cmc.eid.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class NotificationItem implements Serializable {

    private static final long serialVersionUID = 1L;

    private ArrayList<String> notificationIDs;
    private String messageID;
    private String spID;
    private String enMessage;
    private String tcMessage;
    private String scMessage;

    public NotificationItem() {
    };

    public NotificationItem(ArrayList<String> PnotificationIDs,
            String PmessageID,
            String PspID,
            String PenMessage,
            String PtcMessage,
            String PscMessage) {
        this.notificationIDs = PnotificationIDs;
        this.messageID = PmessageID;
        this.spID = PspID;
        this.enMessage = PenMessage;
        this.tcMessage = PtcMessage;
        this.scMessage = PscMessage;
    }

    public ArrayList<String> getNotificationIDs() {
        return this.notificationIDs;
    }

    public void setNotificationIDs(ArrayList<String> t) {
        this.notificationIDs = t;
    }

    public String getMessageID() {
        return this.messageID;
    }

    public void setMessageID(String msgid) {
        this.messageID = msgid;
    }

    public String getSpID() {
        return this.spID;
    }

    public void setSpID(String inSpId) {
        this.spID = inSpId;
    }

    public String getEnMessage() {
        return this.enMessage;
    }

    public void setEnMessage(String enmsg) {
        this.enMessage = enmsg;
    }

    public String getTcMessage() {
        return this.tcMessage;
    }

    public void setTcMessage(String tcmsg) {
        this.tcMessage = tcmsg;
    }

    public String getScMessage() {
        return this.scMessage;
    }

    public void setScMessage(String scmsg) {
        this.scMessage = scmsg;
    }

    @Override
    public String toString() {
        return "NotificationItem [notificationIDs=" + notificationIDs + ", messageID=" + messageID + ", spID=" + spID
                + ", enMessage=" + enMessage + ", tcMessage=" + tcMessage + ", scMessage=" + scMessage + "]";
    }

}
