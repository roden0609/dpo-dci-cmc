package hk.gov.cmc.model.maintainmessage.key;

public class IasClientIdNotiIdPair {
    private String clientId;
    private String notiId;

    public IasClientIdNotiIdPair() {
    }

    public IasClientIdNotiIdPair(String clientId, String notiId) {
        this.clientId = clientId;
        this.notiId = notiId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getNotiId() {
        return notiId;
    }

    public void setNotiId(String notiId) {
        this.notiId = notiId;
    }

    public boolean equals(Object other) {
        if ((this == other)) {
            return true;
        }
        if ((other == null)) {
            return false;
        }
        if (!(other instanceof IasClientIdNotiIdPair)) {
            return false;
        }

        IasClientIdNotiIdPair castOther = (IasClientIdNotiIdPair) other;

        return this.getClientId() != null && castOther.getClientId() != null
                && this.getClientId().equals(castOther.getClientId())
                && this.getNotiId() != null && castOther.getNotiId() != null
                && this.getNotiId().equals(castOther.getNotiId());
    }

    @Override
    public String toString() {
        return "IasClientIdNotiIdPair [clientId=" + clientId + ", notiId=" + notiId + "]";
    }

}
