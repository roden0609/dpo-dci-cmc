package hk.gov.cmc.eid.bean;

import java.io.Serializable;

public class IasGetNotiIdResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private String notiId;
    private String status;

    public IasGetNotiIdResult(String notiId, String status) {
        this.notiId = notiId;
        this.status = status;
    }

    public String getNotiId() {
        return this.notiId;
    }

    public String getStatus() {
        return this.status;
    }
}
