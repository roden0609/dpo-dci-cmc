package hk.gov.cmc.model.maintainmessage.response;

import java.util.List;

public class MaintainMessageResponse {

    private String resultCode;
    private String resultMessage;
    private List<MessageResponse> messageResponses;

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    public List<MessageResponse> getMessageResponses() {
        return messageResponses;
    }

    public void setMessageResponses(List<MessageResponse> messageResponses) {
        this.messageResponses = messageResponses;
    }

    public void addMessageResponse(MessageResponse messageResponse) {
        this.messageResponses.add(messageResponse);
    }
}
