package hk.gov.cmc.batchjob;

import java.io.IOException;
import java.util.Properties;

import javax.naming.InitialContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import hk.gov.gcis.rm.common.utils.PropertiesUtils;
import hk.gov.cmc.appserver.ejb.session.asynmessage.IAsynMessageSessionBM;

public class AsynMessageBackgroundService {

    private static Log logger = LogFactory.getLog(AsynMessageBackgroundService.class);
    private Properties properties = null;

    public static void main(String args[]) throws Exception {

        try {

            if (args.length != 1) {
                System.out.println("usage: AsynMessageBackgroundService <property file>");
                System.exit(-1);
            }

            AsynMessageBackgroundService asynMessageService = new AsynMessageBackgroundService(args[0]);

            asynMessageService.sendMsgResponseByAsyn();

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

    public AsynMessageBackgroundService(String propfile) throws IOException {
        properties = PropertiesUtils.loadPropertiesFile(propfile);
    }

    public void sendMsgResponseByAsyn() throws Exception {
        InitialContext ctx = new InitialContext(properties);
        String asynMessageSessionEjbJndiName = properties.getProperty("ASYN_MESSAGE_SESSION_EJB_JNDI_NAME");
        IAsynMessageSessionBM asynmessageEJB = (IAsynMessageSessionBM) ctx.lookup(asynMessageSessionEjbJndiName);

        asynmessageEJB.sendMsgResponseByAsyn();
    }

}
