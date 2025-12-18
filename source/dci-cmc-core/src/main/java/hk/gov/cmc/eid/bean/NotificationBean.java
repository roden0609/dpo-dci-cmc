package hk.gov.cmc.eid.bean;

import java.io.Serializable;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class NotificationBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private ArrayList<NotificationItem> notifications;

    public NotificationBean() {
    }

    public NotificationBean(String content) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootArray = mapper.readTree(content);

            ArrayList<NotificationItem> notiArray = new ArrayList<>();

            if (rootArray != null && rootArray.isArray()) {
                for (JsonNode node : rootArray) {

                    // notificationIDs array
                    ArrayList<String> notiIdsList = new ArrayList<>();
                    JsonNode notiIdsNode = node.get("notificationIDs");

                    if (notiIdsNode != null && notiIdsNode.isArray()) {
                        for (JsonNode idNode : notiIdsNode) {
                            notiIdsList.add(idNode.asText());
                        }
                    }

                    String messageID = getJsonString(node, "messageID");
                    String spID = getJsonString(node, "spID");
                    String enMessage = getJsonString(node, "enMessage");
                    String tcMessage = getJsonString(node, "tcMessage");
                    String scMessage = getJsonString(node, "scMessage");

                    NotificationItem n = new NotificationItem(
                            notiIdsList,
                            messageID,
                            spID,
                            enMessage,
                            tcMessage,
                            scMessage);

                    notiArray.add(n);
                }
            }

            this.notifications = notiArray;

        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "Invalid JSON content for NotificationBean", e);
        }
    }

    public ArrayList<NotificationItem> getNotifications() {
        return this.notifications;
    }

    public void setNotifications(ArrayList<NotificationItem> n) {
        this.notifications = n;
    }

    private static String getJsonString(JsonNode jsonNode, String fieldName) {
        JsonNode valueNode = jsonNode.get(fieldName);
        return (valueNode != null && !valueNode.isNull())
                ? valueNode.asText()
                : null;
    }

    @Override
    public String toString() {
        return "NotificationBean [notifications=" + notifications + "]";
    }

}
