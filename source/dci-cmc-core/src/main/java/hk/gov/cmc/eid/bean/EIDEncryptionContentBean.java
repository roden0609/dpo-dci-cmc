package hk.gov.cmc.eid.bean;

import java.io.Serializable;

public class EIDEncryptionContentBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
