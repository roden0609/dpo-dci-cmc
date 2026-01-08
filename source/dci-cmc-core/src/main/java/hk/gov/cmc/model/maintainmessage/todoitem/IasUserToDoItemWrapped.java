package hk.gov.cmc.model.maintainmessage.todoitem;

public class IasUserToDoItemWrapped {

    private IasUserToDoItem iasUserToDoItem;
    private String hkid;

    public IasUserToDoItemWrapped() {
        super();
    }

    public IasUserToDoItem getIasUserToDoItem() {
        return iasUserToDoItem;
    }

    public void setIasUserToDoItem(IasUserToDoItem iasUserToDoItem) {
        this.iasUserToDoItem = iasUserToDoItem;
    }

    public String getHkid() {
        return hkid;
    }

    public void setHkid(String hkid) {
        this.hkid = hkid;
    }

    @Override
    public String toString() {
        return "IasUserToDoItemWrapped [iasUserToDoItem=" + iasUserToDoItem + ", hkid=" + hkid + "]";
    }
}
