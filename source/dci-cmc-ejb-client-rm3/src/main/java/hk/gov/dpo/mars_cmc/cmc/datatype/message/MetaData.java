package hk.gov.dpo.mars_cmc.cmc.datatype.message;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

public class MetaData implements Serializable {

    private static final long serialVersionUID = 1L;

    private String m_dataContentEN;
    private String m_dataContentTC;
    private String m_dataContentSC;
    private ArrayList<Recipient> m_recipientList;

    public MetaData() {
        m_recipientList = new ArrayList<>();
    }

    public String getDataContentEN() {
        return m_dataContentEN;
    }

    public void setDataContentEN(String dataContentEN) {
        m_dataContentEN = dataContentEN;
    }

    public String getDataContentTC() {
        return m_dataContentTC;
    }

    public void setDataContentTC(String dataContentTC) {
        m_dataContentTC = dataContentTC;
    }

    public String getDataContentSC() {
        return m_dataContentSC;
    }

    public void setDataContentSC(String dataContentSC) {
        m_dataContentSC = dataContentSC;
    }

    public void addRecipient(Recipient recipient) {
        m_recipientList.add(recipient);
    }

    public List<Recipient> getRecipientList() {
        return m_recipientList;
    }

}
