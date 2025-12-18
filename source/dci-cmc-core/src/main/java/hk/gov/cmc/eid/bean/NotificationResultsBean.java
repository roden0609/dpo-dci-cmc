package hk.gov.cmc.eid.bean;

import java.io.Serializable;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class NotificationResultsBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private ArrayList<NotificationResultItem> notificationResults;

    public NotificationResultsBean() {
    }

    public NotificationResultsBean(String content) {
        ArrayList<NotificationResultItem> notiArray = new ArrayList<>();

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(content);

            JsonNode jArray = root.get("notificationResults");

            if (jArray != null && jArray.isArray()) {
                for (JsonNode node : jArray) {
                    String status = getJsonString(node, "status");
                    String messageID = getJsonString(node, "messageID");
                    String notificationID = getJsonString(node, "notificationID");

                    NotificationResultItem n = new NotificationResultItem(
                            notificationID,
                            messageID,
                            status);

                    notiArray.add(n);
                }
            }

            this.notificationResults = notiArray;
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "Invalid JSON content for NotificationResultsBean", e);
        }
    }

    public ArrayList<NotificationResultItem> getNotificationResults() {
        return this.notificationResults;
    }

    public void setNotificationResults(ArrayList<NotificationResultItem> n) {
        this.notificationResults = n;
    }

    private static String getJsonString(JsonNode jsonNode, String fieldName) {
        JsonNode valueNode = jsonNode.get(fieldName);
        return (valueNode != null && !valueNode.isNull())
                ? valueNode.asText()
                : null;
    }

    @Override
    public String toString() {
        return "NotificationResultsBean [notificationResults=" + notificationResults + "]";
    }

}
