package hk.gov.cmc.eid.bean;

import java.io.Serializable;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class StatusResultsBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private ArrayList<StatusResultItem> statusResults;

    public StatusResultsBean() {
    }

    public StatusResultsBean(String content) {
        ArrayList<StatusResultItem> statusArray = new ArrayList<>();

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(content);
            JsonNode jArray = root.get("statusResults");

            if (jArray != null && jArray.isArray()) {
                for (JsonNode node : jArray) {
                    String status = getJsonString(node, "status");
                    String messageID = getJsonString(node, "messageID");
                    String notificationID = getJsonString(node, "notificationID");

                    StatusResultItem n = new StatusResultItem(notificationID, messageID, status);
                    statusArray.add(n);
                }
            }

            this.statusResults = statusArray;

        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "Invalid JSON content for StatusResultsBean", e);
        }
    }

    public ArrayList<StatusResultItem> getStatusResults() {
        return this.statusResults;
    }

    public void setStatusResults(ArrayList<StatusResultItem> n) {
        this.statusResults = n;
    }

    private static String getJsonString(JsonNode jsonNode, String fieldName) {
        JsonNode valueNode = jsonNode.get(fieldName);
        return (valueNode != null && !valueNode.isNull())
                ? valueNode.asText()
                : null;
    }

    @Override
    public String toString() {
        return "StatusResultsBean [statusResults=" + statusResults + "]";
    }

}
