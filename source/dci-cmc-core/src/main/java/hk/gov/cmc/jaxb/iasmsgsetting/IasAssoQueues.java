package hk.gov.cmc.jaxb.iasmsgsetting;

import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

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
