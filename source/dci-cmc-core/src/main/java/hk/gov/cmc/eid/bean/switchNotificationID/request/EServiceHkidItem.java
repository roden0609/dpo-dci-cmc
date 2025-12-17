package hk.gov.cmc.eid.bean.switchNotificationID.request;

import java.io.Serializable;

public class EServiceHkidItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("clientID")
    private String clientID;

    @JsonProperty("HKID")
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

    public String getHKID() {
        return HKID;
    }

    public void setHKID(String hKID) {
        HKID = hKID;
    }

    @Override
    public String toString() {
        return "EServiceHkidItem [clientID=" + clientID + ", HKID=" + HKID + "]";
    }
}
