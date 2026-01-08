package hk.gov.cmc.utils.maintainmessage.ias;

import java.util.HashSet;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import hk.gov.cmc.model.maintainmessage.request.Recipient;

public class IasRecipientUtils {
    private static Log logger = LogFactory.getLog(IasRecipientUtils.class);

    public static HashSet<String> getIasDuplicationRecipientHashSet(List<Recipient> recipients, String iasIdpId) {
        logger.info("getIasDuplicationRecipientHashSet - start");

        HashSet<String> allHashSet = new HashSet<String>();
        HashSet<String> duplicateHashSet = new HashSet<String>();

        for (Recipient recipient : recipients) {

            // This function check for iAM Smart user only
            if (iasIdpId.equals(recipient.getIdpId())) {

                String recipientID = recipient.getRecipientId();

                if (allHashSet.contains(recipientID)) {
                    if (!duplicateHashSet.contains(recipientID)) {
                        duplicateHashSet.add(recipientID);
                    }
                } else {
                    allHashSet.add(recipientID);
                }
            }
        }

        logger.info("getIasDuplicationRecipientHashSet - end");
        return duplicateHashSet;
    }
}
