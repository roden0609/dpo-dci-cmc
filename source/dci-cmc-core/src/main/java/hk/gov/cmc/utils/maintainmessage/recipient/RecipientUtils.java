package hk.gov.cmc.utils.maintainmessage.recipient;

import java.util.List;

import hk.gov.cmc.model.maintainmessage.action.Action;
import hk.gov.cmc.model.maintainmessage.request.Recipient;

public class RecipientUtils {

    public static boolean isNewOrReplace(List<Recipient> recipients) {
        for (Recipient recipient : recipients) {

            Action action = Action.NEW;
            if (recipient.getAction() != null) {
                action = recipient.getAction();
            }

            if (action == Action.NEW || action == Action.REPLACE) {
                return true;
            }
        }
        return false;
    }
}
