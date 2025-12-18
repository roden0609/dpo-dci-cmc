
package hk.gov.cmc.eid.bean;

import java.io.Serializable;

public class EServiceOpenIdItem implements Serializable {

    private static final long serialVersionUID = 1L;

    private String clientID;
    private String openID;

    public EServiceOpenIdItem() {
    }

    public EServiceOpenIdItem(String clientID, String openID) {
        this.clientID = clientID;
        this.openID = openID;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientid) {
        this.clientID = clientid;
    }

    public String getOpenID() {
        return openID;
    }

    public void setOpenID(String openid) {
        this.openID = openid;
    }

    @Override
    public String toString() {
        return "EServiceOpenIdItem [clientID=" + clientID + ", openID=" + openID + "]";
    }

}
