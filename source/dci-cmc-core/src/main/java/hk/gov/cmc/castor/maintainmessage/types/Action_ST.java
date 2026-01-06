
package hk.gov.cmc.castor.maintainmessage.types;

import java.util.Enumeration;
import java.util.Hashtable;

public class Action_ST implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    public static final int NEW_TYPE = 0;

    public static final Action_ST NEW = new Action_ST(NEW_TYPE, "New");

    public static final int DELETE_TYPE = 1;

    public static final Action_ST DELETE = new Action_ST(DELETE_TYPE, "Delete");

    public static final int REPLACE_TYPE = 2;

    public static final Action_ST REPLACE = new Action_ST(REPLACE_TYPE, "Replace");

    public static final int MARKCOMPLETE_TYPE = 3;

    public static final Action_ST MARKCOMPLETE = new Action_ST(MARKCOMPLETE_TYPE, "MarkComplete");

    public static final int UPDATE_TYPE = 4;
    public static final Action_ST UPDATE = new Action_ST(UPDATE_TYPE, "Update");

    private static Hashtable<String, Action_ST> _memberTable = init();

    private int type = -1;

    private String stringValue = null;

    private Action_ST(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    }

    public static Enumeration<Action_ST> enumerate() {
        return _memberTable.elements();
    }

    public int getType() {
        return this.type;
    }

    private static Hashtable<String, Action_ST> init() {
        Hashtable<String, Action_ST> members = new Hashtable<>();
        members.put("New", NEW);
        members.put("Delete", DELETE);
        members.put("Replace", REPLACE);
        members.put("MarkComplete", MARKCOMPLETE);

        members.put("Update", UPDATE);

        return members;
    }

    private Object readResolve() {
        return valueOf(this.stringValue);
    }

    public String toString() {
        return this.stringValue;
    }

    public static Action_ST valueOf(String string) {
        Object obj = null;
        if (string != null)
        obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid Action_ST";
            throw new IllegalArgumentException(err);
        }
        return (Action_ST) obj;
    }

}
