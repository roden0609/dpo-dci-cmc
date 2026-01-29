package hk.gov.cmc.batchjob;

import hk.gov.cmc.appserver.ejb.session.batchfw.IFwBatchSessionBM;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.naming.InitialContext;

public class FwBatchClient {

    private static Properties m_properties = new Properties();
    private static String mode = null;
    private static String clsImpl = null;
    private static String methodName = null;
    private static String paramsString = null;

    private static Log logger = LogFactory.getLog(FwBatchClient.class);

    public static void main(String args[]) throws Exception {

        try {
            // 1) R - call batch using reflection
            // usage: FwBatchClient <property file> R <clsImpl> <methodName> {params}
            // example: FwBatchClient xxx.properties R hk.gov.immd.vors.batch.VorsItineraryStatJob execute statDate=20100816
            if (args.length < 2) {
                System.exit(-1);
            }

            FwBatchClient client = new FwBatchClient(args[0]);

            mode = args[1];
            if ("R".equals(mode)) {
                clsImpl = args[2];
                methodName = args[3];
                if (args.length > 4) {
                    paramsString = args[4];
                }
                client.callBatchUsingReflection(clsImpl, methodName, paramsString);
            }
        } catch (Exception ex) {

            // log warn instead of error if server side processing time exceed InvokerLocator timeout threshold
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

    public FwBatchClient(String propfile) throws IOException {
        m_properties.load(new FileInputStream(propfile));
    }

    private void callBatchUsingReflection(String clsImpl, String methodName, String paramsString) throws Exception {

        InitialContext ctx = new InitialContext(m_properties);
        String jndiName = m_properties.getProperty("BATCHFW_REMOTE_EJB_JNDI_NAME");

        IFwBatchSessionBM batchEjb = (IFwBatchSessionBM) ctx.lookup(jndiName);
        batchEjb.callBatchUsingReflection(clsImpl, methodName, paramsToMap(paramsString));
    }

    private Map<String, Object> paramsToMap(String paramsString) {

        Map<String, Object> params = new HashMap<String, Object>();
        if (paramsString != null) {
            String[] nameValuePairs = paramsString.split("&");
            for (String nameValuePair : nameValuePairs) {
                String[] ss = nameValuePair.split("=");
                String name = ss[0];
                String value = ss[1];
                params.put(name, value);
            }
        }

        return params;
    }
}
