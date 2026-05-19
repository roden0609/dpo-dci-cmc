package hk.gov.dpo.mars_cmc.cmc.client.iasmessage.sample;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import hk.gov.dpo.mars_cmc.cmc.client.iasmessage.IasMsgSettingClient;
import hk.gov.gcis.ss.common.utils.PropertiesUtils;
import hk.gov.gcis.ss.messaging.jaxb.asynmsg.ScopesMessagingAcknowledgement;

public class OnIasEnableSample {

    public static void main(String args[]) {

        try {
            if (args.length != 1) {
                System.out.println("Usage: java " + OnIasEnableSample.class.getName() + " <property file> ");
                System.exit(-1);
            }

            Properties properties = PropertiesUtils.loadPropertiesFile(args[0]);

            IasMsgSettingClient iasMsgSettingClient = new IasMsgSettingClient(properties);
            List<String> openIdList = new ArrayList<String>();
            openIdList = Arrays.asList(PropertiesUtils.getMandatoryProperty(properties, "OPEN_ID_LIST").split(","));

            ScopesMessagingAcknowledgement ack = iasMsgSettingClient.enable(openIdList);
            System.out.println("CorrId=" + ack.getCorrId() + ", MsgId=" + ack.getMsgId());

        } catch (Throwable t) {
            System.out.println("Exception in " + OnIasEnableSample.class.getName() + ": " + t.getMessage());
        }
    }

}
