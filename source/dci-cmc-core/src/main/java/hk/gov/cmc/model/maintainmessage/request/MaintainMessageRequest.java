package hk.gov.cmc.model.maintainmessage.request;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MaintainMessageRequest {

    private List<MessageRequest> messageRequests = new ArrayList<>();

    public List<MessageRequest> getMessageRequests() {
        return messageRequests;
    }

    public void setMessageRequests(List<MessageRequest> messageRequests) {
        this.messageRequests = messageRequests != null
                ? messageRequests
                : new ArrayList<>();
    }

    public void addMessageRequest(MessageRequest request) {
        if (request != null) {
            this.messageRequests.add(request);
        }
    }

    @Override
    public String toString() {
        return "MaintainMessageRequest{" +
                "messageRequests=" + messageRequests +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MaintainMessageRequest)) return false;
        MaintainMessageRequest that = (MaintainMessageRequest) o;
        return Objects.equals(messageRequests, that.messageRequests);
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageRequests);
    }
}
