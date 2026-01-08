package hk.gov.cmc.model.maintainmessage.request;

import java.util.List;
import java.util.Objects;

import hk.gov.cmc.model.maintainmessage.application.Application;
import hk.gov.cmc.model.maintainmessage.emessage.EMessage;
import hk.gov.cmc.model.maintainmessage.todoitem.ToDoItem;

public class MessageRequest {

    private String portalId;
    private EMessage eMessage;
    private ToDoItem toDoItem;
    private Application application;
    private List<MetaData> metaDataList;

    public String getPortalId() {
        return portalId;
    }

    public void setPortalId(String portalId) {
        this.portalId = portalId;
    }

    public EMessage getEMessage() {
        return eMessage;
    }

    public void setEMessage(EMessage eMessage) {
        this.eMessage = eMessage;
    }

    public ToDoItem getToDoItem() {
        return toDoItem;
    }

    public void setToDoItem(ToDoItem toDoItem) {
        this.toDoItem = toDoItem;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public List<MetaData> getMetaDataList() {
        return metaDataList;
    }

    public void setMetaDataList(List<MetaData> metaDataList) {
        this.metaDataList = metaDataList;
    }

    /* =========================
     * Object overrides
     * ========================= */

    @Override
    public String toString() {
        return "MessageRequest{" +
                "portalId='" + portalId + '\'' +
                ", eMessage=" + eMessage +
                ", toDoItem=" + toDoItem +
                ", application=" + application +
                ", metaDataList=" + metaDataList +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MessageRequest)) return false;
        MessageRequest that = (MessageRequest) o;
        return Objects.equals(portalId, that.portalId)
                && Objects.equals(eMessage, that.eMessage)
                && Objects.equals(toDoItem, that.toDoItem)
                && Objects.equals(application, that.application)
                && Objects.equals(metaDataList, that.metaDataList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                portalId,
                eMessage,
                toDoItem,
                application,
                metaDataList
        );
    }
}
