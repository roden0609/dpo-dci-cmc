package hk.gov.cmc.eid.bean;

import java.io.Serializable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TxIdBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String txID;

    public TxIdBean() {
    }

    public TxIdBean(String content) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(content);
            this.txID = getJsonString(root, "txID");
        } catch (Exception e) {
            // fallback: treat content as plain txID (same as old behavior)
            this.txID = content;
        }
    }

    public String getTxID() {
        return this.txID;
    }

    public void setTxID(String txid) {
        this.txID = txid;
    }

    private static String getJsonString(JsonNode jsonNode, String fieldName) {
        JsonNode valueNode = jsonNode.get(fieldName);
        return (valueNode != null && !valueNode.isNull())
                ? valueNode.asText()
                : null;
    }

    @Override
    public String toString() {
        return "TxIdBean [txID=" + txID + "]";
    }
}
