package hk.gov.dpo.mars_cmc.cmc.client.iasmessage.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "IasAssoQueues")
@XmlAccessorType (XmlAccessType.FIELD)
public class IasAssoQueues {

    @XmlElement(name = "IasAssoQueue")
    private List<IasAssoQueue> iasAssoQueues = null;

    public List<IasAssoQueue> getIasAssoQueues() {
        return iasAssoQueues;
    }

    public void setIasAssoQueues(List<IasAssoQueue> iasAssoQueues) {
        this.iasAssoQueues = iasAssoQueues;
    }

}
