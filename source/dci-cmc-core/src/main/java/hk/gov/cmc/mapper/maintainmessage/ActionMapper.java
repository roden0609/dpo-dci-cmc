package hk.gov.cmc.mapper.maintainmessage;

import hk.gov.cmc.jaxb.maintainmessage.ActionST;
import hk.gov.cmc.model.maintainmessage.action.Action;

public final class ActionMapper {

    private ActionMapper() {
    }

    public static ActionST toJaxb(Action domain) {
        if (domain == null) {
            return null;
        }
        return ActionST.fromValue(domain.value());
    }

    public static Action fromJaxb(ActionST jaxb) {
        if (jaxb == null) {
            return null;
        }
        return Action.fromValue(jaxb.value());
    }

    public static Action fromString(String value) {
        return Action.fromValue(value);
    }

    public static String toString(Action action) {
        return action != null ? action.value() : null;
    }
}
