package hk.gov.cmc.batchjob;

import java.io.IOException;
import java.util.Properties;

import javax.naming.InitialContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import hk.gov.cmc.appserver.ejb.session.maintainmessage.IMaintainMessageSessionBM;
import hk.gov.gcis.rm.common.utils.PropertiesUtils;

public class MaintainMessageBackgroundService {

    private static Log logger = LogFactory.getLog(MaintainMessageBackgroundService.class);
    private Properties properties = null;

    public static void main(String args[]) throws Exception {
        try {
            if (args.length < 1 || args.length > 3) {
                System.out.println(
                        "usage: MaintainMessageBackgroundService <property file> <asyn app Id list> <single pull call limit>");
                System.exit(-1);
            }
            String asynAppIdStr = "";
            String singlePullCallLimitStr = "";
            if (args.length >= 2) {
                asynAppIdStr = args[1];
            }
            if (args.length == 3) {
                singlePullCallLimitStr = args[2];
                try {
                    Integer.parseInt(singlePullCallLimitStr);
                } catch (Exception ex) {
                    System.out.println("Please input a number for <single pull call limit>.");
                    System.exit(-1);
                }
            }
            MaintainMessageBackgroundService maintainMessageService = new MaintainMessageBackgroundService(args[0]);

            maintainMessageService.processMessageByBatchPull(asynAppIdStr, singlePullCallLimitStr);

        } catch (Exception ex) {
            if ((ex.getMessage() != null && ex.getMessage().indexOf("Socket timed out") > -1)
                    || (ex.getCause() != null && ex.getCause().getMessage() != null
                            && (ex.getCause().getMessage()).indexOf("Socket timed out") > -1)) {
                logger.warn("General Exception is caught: ", ex);
            } else {
                logger.error("General Exception is caught: ", ex);
            }
            throw ex;
        }
    }

    public MaintainMessageBackgroundService(String propfile) throws IOException {
        properties = PropertiesUtils.loadPropertiesFile(propfile);
    }

    public void processMessageByBatchPull(String asynAppIdStr, String singlePullCallLimitStr) throws Exception {
        InitialContext ctx = new InitialContext(properties);
        String maintainMessageSessionEjbJndiName = properties.getProperty("MAINTAIN_MESSAGE_SESSION_EJB_JNDI_NAME");
        IMaintainMessageSessionBM maintainmessageEJB = (IMaintainMessageSessionBM) ctx
                .lookup(maintainMessageSessionEjbJndiName);

        maintainmessageEJB.processMessageByBatchPull(asynAppIdStr, singlePullCallLimitStr);
    }
}
