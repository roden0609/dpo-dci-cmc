package hk.gov.cmc.model.maintainmessage.key;

import java.io.Serializable;

public class IasMsgRecipientKey implements Serializable {

    private static final long serialVersionUID = 1L;
    private String clientId;
    private String openIdOrHkid;

    public IasMsgRecipientKey(String clientId, String openIdOrHkid) {
        this.clientId = clientId;
        this.openIdOrHkid = openIdOrHkid;
    }

    public String getClientId() {
        return this.clientId;
    }

    public String getOpenOrHkId() {
        return this.openIdOrHkid;
    }

    public boolean equals(Object other) { // $IGN_Check_for_equality_using_getClass$<Working as intended>
        if ((this == other)) { // $IGN_Use_Equals_Instead_Equality_Operator$<Working as intended>
            return true;
        }
        if ((other == null)) {
            return false;
        }
        if (!(other instanceof IasMsgRecipientKey)) {
            return false;
        }

        IasMsgRecipientKey castOther = (IasMsgRecipientKey) other; // $IGN_Avoid_casting_without_check$<Working as intended>

        return this.getClientId() != null && castOther.getClientId() != null
                && this.getClientId().equals(castOther.getClientId())
                && this.getOpenOrHkId() != null && castOther.getOpenOrHkId() != null
                && this.getOpenOrHkId().equals(castOther.getOpenOrHkId());

    }

    public int hashCode() {
        int result = 17;

        result = 37 * result + (getClientId() == null ? 0 : this.getClientId().hashCode());
        result = 37 * result + (getOpenOrHkId() == null ? 0 : this.getOpenOrHkId().hashCode());

        return result;
    }

}
