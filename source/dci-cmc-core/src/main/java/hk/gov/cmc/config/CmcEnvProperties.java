package hk.gov.cmc.config;

import hk.gov.gcis.rm.common.javaee.EnvPropertiesBase;
import java.io.IOException;
import java.util.Properties;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class CmcEnvProperties extends EnvPropertiesBase {
    private volatile boolean envInitialized = false;
    private String commonPropertiesFilename;
    private String appPropertiesFilename;

    private void initEnvEntries() {
        if (envInitialized) {
            return;
        }
        synchronized (this) {
            if (envInitialized) {
                return;
            }
            try {
                InitialContext context = new InitialContext();
                Object common = context.lookup("java:comp/env/COMMON_PROPERTIES_FILENAME");
                Object app = context.lookup("java:comp/env/APP_PROPERTIES_FILENAME");
                if (common != null) {
                    commonPropertiesFilename = common.toString();
                } else {
                    logWarn("Missing env-entry: COMMON_PROPERTIES_FILENAME");
                }
                if (app != null) {
                    appPropertiesFilename = app.toString();
                } else {
                    logWarn("Missing env-entry: APP_PROPERTIES_FILENAME");
                }
            } catch (NamingException e) {
                logWarn("Failed to lookup COMMON_PROPERTIES_FILENAME or APP_PROPERTIES_FILENAME", e);
            } finally {
                envInitialized = true;
            }
        }
    }

    @Override
    protected String getCommonPropertiesFilename() {
        initEnvEntries();
        return commonPropertiesFilename;
    }

    @Override
    protected String getAppPropertiesFilename() {
        initEnvEntries();
        return appPropertiesFilename;
    }

    public Properties getProperties() throws IOException {
        return super.getRuntimeProperties();
    }
}
