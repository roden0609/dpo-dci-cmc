package hk.gov.dpo.mars_cmc.cmc.client.iasmessage.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "IasAssoQueue")
@XmlAccessorType (XmlAccessType.FIELD)
public class IasAssoQueue {

    private String openId;
    private String status;

    public IasAssoQueue() {
    }

    public IasAssoQueue(String openId) {
        super();
        this.openId = openId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
