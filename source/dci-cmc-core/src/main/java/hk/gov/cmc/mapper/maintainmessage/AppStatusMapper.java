package hk.gov.cmc.mapper.maintainmessage;

import hk.gov.cmc.jaxb.maintainmessage.AppStatusST;
import hk.gov.cmc.model.maintainmessage.status.AppStatus;

public final class AppStatusMapper {

    private AppStatusMapper() {
    }

    public static AppStatusST toJaxb(AppStatus domain) {
        if (domain == null) {
            return null;
        }
        return AppStatusST.fromValue(domain.name());
    }

    public static AppStatus fromJaxb(AppStatusST jaxb) {
        if (jaxb == null) {
            return null;
        }
        return AppStatus.fromValue(jaxb.value());
    }

    public static AppStatus fromString(String value) {
        return AppStatus.fromValue(value);
    }

    public static String toString(AppStatus status) {
        return status != null ? status.value() : null;
    }
}
