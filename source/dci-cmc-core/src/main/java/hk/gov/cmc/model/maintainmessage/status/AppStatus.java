package hk.gov.cmc.model.maintainmessage.status;

public enum AppStatus {

    AR,
    UP,
    BP,
    RJ,
    CP,
    CA,
    AP,
    DB,
    AL,
    PD,
    NA;

    public static AppStatus fromValue(String value) {
        if (value == null) {
            return null;
        }
        return AppStatus.valueOf(value);
    }

    public String value() {
        return name();
    }
}
