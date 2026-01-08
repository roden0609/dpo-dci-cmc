package hk.gov.cmc.model.maintainmessage.action;

public enum Action {

    NEW(0, "New"),
    DELETE(1, "Delete"),
    REPLACE(2, "Replace"),
    MARK_COMPLETE(3, "MarkComplete"),
    UPDATE(4, "Update");

    private final int type;
    private final String value;

    Action(int type, String value) {
        this.type = type;
        this.value = value;
    }

    public int getType() {
        return type;
    }

    public String value() {
        return value;
    }

    public static Action fromValue(String value) {
        if (value == null) {
            return null;
        }
        for (Action a : values()) {
            if (a.value.equals(value)) {
                return a;
            }
        }
        throw new IllegalArgumentException(value);
    }

    public static Action fromType(int type) {
        for (Action a : values()) {
            if (a.type == type) {
                return a;
            }
        }
        throw new IllegalArgumentException(String.valueOf(type));
    }
}
