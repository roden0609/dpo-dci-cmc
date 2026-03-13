package hk.gov.cmc.eid.bean.switchNotificationID.request;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EServiceHkidItem implements Serializable {

    private static final long serialVersionUID = 1L;

    private String clientID;
    private String HKID;

    public EServiceHkidItem() {
    }

    public EServiceHkidItem(String clientID, String HKID) {
        this.clientID = clientID;
        this.HKID = HKID;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    @JsonProperty("HKID")
    public String getHKID() {
        return HKID;
    }

    @JsonProperty("HKID")
    public void setHKID(String HKID) {
        this.HKID = HKID;
    }

    @Override
    public String toString() {
        return "EServiceHkidItem [clientID=" + clientID + ", HKID=" + HKID + "]";
    }
}
