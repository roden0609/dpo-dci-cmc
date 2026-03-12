package hk.gov.cmc.eid.bean;

import java.io.Serializable;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EServiceOpenIdsBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private ArrayList<EServiceOpenIdItem> eServiceOpenIDs;

    public EServiceOpenIdsBean() {
    }

    public EServiceOpenIdsBean(String content) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootArray = mapper.readTree(content);

            ArrayList<EServiceOpenIdItem> openIdArray = new ArrayList<>();

            if (rootArray != null && rootArray.isArray()) {
                for (JsonNode node : rootArray) {
                    String clientID = getJsonString(node, "clientID");
                    String openID = getJsonString(node, "openID");
                    openIdArray.add(new EServiceOpenIdItem(clientID, openID));
                }
            }

            this.eServiceOpenIDs = openIdArray;

        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "Invalid JSON content for EServiceOpenIdsBean", e);
        }
    }

    public ArrayList<EServiceOpenIdItem> geteServiceOpenIDs() {
        return this.eServiceOpenIDs;
    }

    public void seteServiceOpenIDs(ArrayList<EServiceOpenIdItem> n) {
        this.eServiceOpenIDs = n;
    }

    private static String getJsonString(JsonNode jsonNode, String fieldName) {
        JsonNode valueNode = jsonNode.get(fieldName);
        return (valueNode != null && !valueNode.isNull())
                ? valueNode.asText()
                : null;
    }

    @Override
    public String toString() {
        return "EServiceOpenIdsBean [eServiceOpenIDs=" + eServiceOpenIDs + "]";
    }

}
