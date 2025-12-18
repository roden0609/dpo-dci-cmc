package hk.gov.cmc.eid.bean.switchNotificationID.response;

import java.io.Serializable;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class NotificationIdsByHKIDsBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private ArrayList<NotificationIdByHKIDItem> notificationIDs;

    private static final Log log = LogFactory.getLog(NotificationIdsByHKIDsBean.class);

    public NotificationIdsByHKIDsBean() {
    }

    public NotificationIdsByHKIDsBean(String content) {
        log.debug("NotificationIdsByHKIDsBean content: " + content);

        ArrayList<NotificationIdByHKIDItem> notiArray = new ArrayList<>();

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(content);

            JsonNode jArray = root.get("notificationIDs");

            if (jArray != null && jArray.isArray()) {
                for (JsonNode node : jArray) {
                    String status = getJsonString(node, "status");
                    String clientID = getJsonString(node, "clientID");
                    String HKID = getJsonString(node, "HKID");
                    String notificationID = getJsonString(node, "notificationID");

                    NotificationIdByHKIDItem item = new NotificationIdByHKIDItem(
                            status, clientID, HKID, notificationID);

                    notiArray.add(item);
                }
            }

            this.notificationIDs = notiArray;

        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "Invalid JSON content for NotificationIdsByHKIDsBean", e);
        }
    }

    public ArrayList<NotificationIdByHKIDItem> getNotificationIDs() {
        return this.notificationIDs;
    }

    public void setNotificationIDs(ArrayList<NotificationIdByHKIDItem> n) {
        this.notificationIDs = n;
    }

    private static String getJsonString(JsonNode jsonNode, String fieldName) {
        JsonNode valueNode = jsonNode.get(fieldName);
        return (valueNode != null && !valueNode.isNull())
                ? valueNode.asText()
                : null;
    }

    @Override
    public String toString() {
        return "NotificationIdsByHKIDsBean [notificationIDs=" + notificationIDs + "]";
    }

}
