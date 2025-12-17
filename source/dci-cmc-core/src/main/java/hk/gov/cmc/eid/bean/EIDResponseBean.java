
package hk.gov.cmc.eid.bean;

import java.io.Serializable;

public class EIDResponseBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String txID;
    private String code;
    private String message;
    private String content;
    private String secretKey;

    public String getTxID() {
        return txID;
    }

    public void setTxID(String txID) {
        this.txID = txID;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
