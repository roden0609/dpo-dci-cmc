package hk.gov.dpo.mars_cmc.cmc.datatype.message;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

public class MessageRequest implements Serializable {

    private static final long serialVersionUID = 1L;
    private String m_portalId;
    private EMessage m_eMessage;
    private ToDoItem m_toDoItem;
    private Application m_Application;
    private ArrayList<MetaData> m_metaDataList;

    public MessageRequest() {
        m_metaDataList = new ArrayList<>();
    }

    public String getPortalId() {
        return m_portalId;
    }

    public void setPortalId(String portalId) {
        m_portalId = portalId;
    }

    public EMessage getEMessage() {
        return m_eMessage;
    }

    public void setEMessage(EMessage eMessage) {
        m_eMessage = eMessage;
    }

    public ToDoItem getToDoItem() {
        return m_toDoItem;
    }

    public void setApplication(Application application) {
        m_Application = application;
    }

    public Application getApplication() {
        return m_Application;
    }

    public void setToDoItem(ToDoItem toDoItem) {
        m_toDoItem = toDoItem;
    }

    public void addMetaData(MetaData metaData) {
        m_metaDataList.add(metaData);
    }

    public List<MetaData> getMetaDataList() {
        return m_metaDataList;
    }

}
