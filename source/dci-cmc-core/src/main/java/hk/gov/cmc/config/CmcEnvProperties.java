package hk.gov.cmc.config;

import hk.gov.gcis.rm.common.javaee.EnvPropertiesBase;
import java.io.IOException;
import java.util.Properties;

public class CmcEnvProperties extends EnvPropertiesBase {

    public Properties getProperties() throws IOException {
        return super.getRuntimeProperties();
    }
}
