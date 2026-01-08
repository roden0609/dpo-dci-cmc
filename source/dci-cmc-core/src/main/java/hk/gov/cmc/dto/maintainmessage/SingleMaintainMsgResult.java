package hk.gov.cmc.dto.maintainmessage;

public class SingleMaintainMsgResult {

    private String createdMsgId;
    private boolean isSuccess;

    public SingleMaintainMsgResult() {
    }

    public SingleMaintainMsgResult(String createdMsgId, boolean isSuccess) {
        this.createdMsgId = createdMsgId;
        this.isSuccess = isSuccess;
    }

    public String getCreatedMsgId() {
        return createdMsgId;
    }

    public void setCreatedMsgId(String createdMsgId) {
        this.createdMsgId = createdMsgId;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

}
