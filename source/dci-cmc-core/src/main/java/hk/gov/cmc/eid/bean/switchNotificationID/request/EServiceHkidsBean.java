package hk.gov.cmc.eid.bean.switchNotificationID.request;

import java.io.Serializable;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EServiceHkidsBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private ArrayList<EServiceHkidItem> eServiceHKIDs;

    public EServiceHkidsBean() {
    }

    public EServiceHkidsBean(String content) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootArray = mapper.readTree(content);

            ArrayList<EServiceHkidItem> hkidArray = new ArrayList<>();

            if (rootArray != null && rootArray.isArray()) {
                for (JsonNode node : rootArray) {
                    String clientID = getJsonString(node, "clientID");
                    String hkid = getJsonString(node, "HKID");
                    hkidArray.add(new EServiceHkidItem(clientID, hkid));
                }
            }

            this.eServiceHKIDs = hkidArray;

        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid JSON content", e);
        }
    }

    private static String getJsonString(JsonNode jsonNode, String fieldName) {
        JsonNode valueNode = jsonNode.get(fieldName);
        return (valueNode != null && !valueNode.isNull())
                ? valueNode.asText()
                : null;
    }

    public void seteServiceHKIDs(ArrayList<EServiceHkidItem> eServiceHKIDs) {
        this.eServiceHKIDs = eServiceHKIDs;
    }

    public ArrayList<EServiceHkidItem> geteServiceHKIDs() {
        return this.eServiceHKIDs;
    }

    @Override
    public String toString() {
        return "EServiceHkidsBean [eServiceHKIDs=" + eServiceHKIDs + "]";
    }
}
