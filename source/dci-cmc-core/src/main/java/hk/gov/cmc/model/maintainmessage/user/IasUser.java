package hk.gov.cmc.model.maintainmessage.user;

import java.io.Serializable;
import java.lang.String;

public class IasUser implements Serializable {

    private static final long serialVersionUID = 1L;

    protected String notiId = null;
    protected String clientId = null;
    protected String openId = null;
    protected String optIn = null;
    protected String status = null;
    protected String hkidHashed = null;
    protected String hkidEncrypted = null;

    public IasUser() {
        super();
    }

    public String getNotiId() {
        return notiId;
    }

    public void setNotiId(String notiId) {
        this.notiId = notiId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getOptIn() {
        return optIn;
    }

    public void setOptIn(String optIn) {
        this.optIn = optIn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getHkidHashed() {
        return hkidHashed;
    }

    public void setHkidHashed(String hkidHashed) {
        this.hkidHashed = hkidHashed;
    }

    public String getHkidEncrypted() {
        return hkidEncrypted;
    }

    public void setHkidEncrypted(String hkidEncrypted) {
        this.hkidEncrypted = hkidEncrypted;
    }

    @Override
    public String toString() {
        return "IasUser [notiId=" + notiId + ", clientId=" + clientId + ", openId=" + openId + ", optIn=" + optIn
                + ", status=" + status + ", hkidHashed=" + hkidHashed + ", hkidEncrypted=" + hkidEncrypted
                + "]";
    }

}
