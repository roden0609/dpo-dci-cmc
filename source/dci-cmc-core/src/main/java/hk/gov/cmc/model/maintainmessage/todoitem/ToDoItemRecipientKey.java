package hk.gov.cmc.model.maintainmessage.todoitem;

import java.io.Serializable;

public class ToDoItemRecipientKey implements Serializable {

    private static final long serialVersionUID = 1L;
    private String userId;
    private String tranId;

    public ToDoItemRecipientKey(String userId, String tranId) {
        this.userId = userId;
        this.tranId = tranId;
    }

    public String getUserId() {
        return this.userId;
    }

    public String getTranId() {
        return this.tranId;
    }

    public boolean equals(Object other) {
        if ((this == other)) {
            return true;
        }
        if ((other == null)) {
            return false;
        }
        if (!(other instanceof ToDoItemRecipientKey)) {
            return false;
        }

        ToDoItemRecipientKey castOther = (ToDoItemRecipientKey) other;

        return this.getUserId() != null && castOther.getUserId() != null
                && this.getUserId().equals(castOther.getUserId())
                && this.getTranId() != null && castOther.getTranId() != null
                && this.getTranId().equals(castOther.getTranId());

    }

    public int hashCode() {
        int result = 17;

        result = 37 * result + (getUserId() == null ? 0 : this.getUserId().hashCode());
        result = 37 * result + (getTranId() == null ? 0 : this.getTranId().hashCode());

        return result;
    }

    @Override
    public String toString() {
        return "ToDoItemRecipientKey [userId=" + userId + ", tranId=" + tranId + "]";
    }

}
