package hk.gov.cmc.batch;

import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import hk.gov.cmc.config.CmcEnvProperties;
import hk.gov.cmc.dao.healthcheck.HealthCheckDAO;
import hk.gov.cmc.persistence.connection.hpfw.HPFW_Connection;

public class CmcSyncJobHealthCheck {

    private static Log logger = LogFactory.getLog(CmcSyncJobHealthCheck.class);
    private final static CmcEnvProperties cmcEnvProperties = new CmcEnvProperties();

    private final static HealthCheckDAO dao = new HealthCheckDAO();

    public void execute(Map<String, Object> params) throws Exception {

        logger.info("[BATCH_JOB]CmcSyncJobHealthCheck - START");

        HPFW_Connection conn = null;

        try {

            conn = HPFW_Connection.getHPFW_Connection();

            Properties prop = cmcEnvProperties.getProperties();

            int lockThresholdInHour = Integer.parseInt(prop.getProperty("CMC_LOCK_TIME_THRESHOLD_IN_HOUR"));

            dao.checkOverTimeLockingJobs(conn, lockThresholdInHour);

        } catch (Exception e) {
            logger.error("CmcSyncJobHealthCheck exception", e);
            throw e;
        } finally {
            if (conn != null)
                HPFW_Connection.close(conn);
        }

        logger.info("[BATCH_JOB]CmcSyncJobHealthCheck - END");
    }
}
