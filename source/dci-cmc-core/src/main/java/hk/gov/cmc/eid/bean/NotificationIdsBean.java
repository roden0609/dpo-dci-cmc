package hk.gov.cmc.eid.bean;

import java.io.Serializable;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class NotificationIdsBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private ArrayList<NotificationIdItem> notificationIDs;

    public NotificationIdsBean() {
    }

    public NotificationIdsBean(String content) {
        ArrayList<NotificationIdItem> notiArray = new ArrayList<>();

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(content);

            JsonNode jArray = root.get("notificationIDs");

            if (jArray != null && jArray.isArray()) {
                for (JsonNode node : jArray) {
                    String status = getJsonString(node, "status");
                    String clientID = getJsonString(node, "clientID");
                    String openID = getJsonString(node, "openID");
                    String notificationID = getJsonString(node, "notificationID");

                    NotificationIdItem n = new NotificationIdItem(
                            status,
                            clientID,
                            openID,
                            notificationID);

                    notiArray.add(n);
                }
            }

            this.notificationIDs = notiArray;
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "Invalid JSON content for NotificationIdsBean", e);
        }
    }

    public ArrayList<NotificationIdItem> getNotificationIDs() {
        return this.notificationIDs;
    }

    public void setNotificationIDs(ArrayList<NotificationIdItem> n) {
        this.notificationIDs = n;
    }

    private static String getJsonString(JsonNode jsonNode, String fieldName) {
        JsonNode valueNode = jsonNode.get(fieldName);
        return (valueNode != null && !valueNode.isNull())
                ? valueNode.asText()
                : null;
    }

    public void printBean() {
        int i = 0;
        for (NotificationIdItem t : this.notificationIDs) {
            System.out.println("notificationIDs Item#" + i + ":" +
                    "ClientId=" + t.getClientID() +
                    ",OpenId=" + t.getOpenID() +
                    ",NotificationID=" + t.getNotificationID() +
                    ",Status=" + t.getStatus());
            i++;
        }
    }

    @Override
    public String toString() {
        return "NotificationIdsBean [notificationIDs=" + notificationIDs + "]";
    }
}
