package hk.gov.cmc.model.maintainmessage.request;

import java.util.List;
import java.util.Objects;

public class MetaData {

    private String dataContentEN;
    private String dataContentTC;
    private String dataContentSC;
    private List<Recipient> recipients;

    public String getDataContentEN() {
        return dataContentEN;
    }

    public void setDataContentEN(String dataContentEN) {
        this.dataContentEN = dataContentEN;
    }

    public String getDataContentTC() {
        return dataContentTC;
    }

    public void setDataContentTC(String dataContentTC) {
        this.dataContentTC = dataContentTC;
    }

    public String getDataContentSC() {
        return dataContentSC;
    }

    public void setDataContentSC(String dataContentSC) {
        this.dataContentSC = dataContentSC;
    }

    public List<Recipient> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<Recipient> recipients) {
        this.recipients = recipients;
    }

    @Override
    public String toString() {
        return "MetaData{" +
                "dataContentEN='" + dataContentEN + '\'' +
                ", dataContentTC='" + dataContentTC + '\'' +
                ", dataContentSC='" + dataContentSC + '\'' +
                ", recipients=" + recipients +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MetaData)) return false;
        MetaData metaData = (MetaData) o;
        return Objects.equals(dataContentEN, metaData.dataContentEN)
                && Objects.equals(dataContentTC, metaData.dataContentTC)
                && Objects.equals(dataContentSC, metaData.dataContentSC)
                && Objects.equals(recipients, metaData.recipients);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                dataContentEN,
                dataContentTC,
                dataContentSC,
                recipients
        );
    }
}
