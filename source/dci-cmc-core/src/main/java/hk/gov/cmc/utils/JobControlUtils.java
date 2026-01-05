
package hk.gov.cmc.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import hk.gov.cmc.common.CmcAppPropertyName;
import hk.gov.cmc.config.CmcEnvProperties;
import hk.gov.cmc.persistence.connection.hpfw.CommonDBUtils;
import hk.gov.cmc.persistence.connection.hpfw.HPFW_Connection;
import hk.gov.cmc.persistence.connection.hpfw.MysqlCommonDBUtils;
import hk.gov.gcis.rm.common.utils.PropertiesUtils;

public class JobControlUtils {

    protected static Log logger = LogFactory.getLog(JobControlUtils.class);
    private final static CmcEnvProperties cmcEnvProperties = new CmcEnvProperties();

    public boolean lockJobControl(String jobName) {
        HPFW_Connection priCon = null;
        Connection con = null;

        String serverId = "";
        serverId = CommonDBUtils.getSERVER_ID();
        if (serverId == null || "".equals(serverId))
            return false;
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;
        try {
            Properties properties = cmcEnvProperties.getProperties();
            long timeoutInMins = 999999;

            boolean timeoutCheckNeeded = "Y".equals(
                    properties.getProperty(
                            jobName + CmcAppPropertyName.JOB_CONTROL_TIMEOUT_CHECK_NEEDED_PROPERTY_SUFFIX));

            if (timeoutCheckNeeded)
                timeoutInMins = Long.parseLong(PropertiesUtils.getMandatoryProperty(properties,
                        jobName + CmcAppPropertyName.JOB_CONTROL_TIMEOUT_MINS_PROPERTY_SUFFIX));

            priCon = HPFW_Connection.getHPFW_Connection(HPFW_Connection.PRI);
            con = priCon.getConnectionPtr();
            con.setAutoCommit(false);

            if (con == null || con.isClosed())
                return false;

            logger.debug("lockJobControl acquireBatchLock: Start : " + jobName);

            if (!MysqlCommonDBUtils.acquireNamedLock(con, jobName)) {
                logger.info("[JobControlUtils]Resource busy during lockJobControl " + jobName);
                return false;
            }

            ps = con.prepareStatement("select server_id, lock_time from CMC_MARS_SYN_JOB_LOCK where SYN_JOB_NAME = ? ");
            ps.setString(1, jobName);

            rs = ps.executeQuery();
            if (rs.next()) {
                String currentLckServer = rs.getString("server_id");
                Timestamp lastLockTime = rs.getTimestamp("lock_time");
                long lockedTime = lastLockTime == null ? 0 : ((new Date()).getTime() - lastLockTime.getTime());

                if ("N".equals(currentLckServer) || (timeoutCheckNeeded && lockedTime > (timeoutInMins * 60 * 1000))) {

                    ps2 = con.prepareStatement(
                            "update CMC_MARS_SYN_JOB_LOCK set server_id = ?, lock_time = now(3) where SYN_JOB_NAME = ?");

                    ps2.setString(1, serverId);
                    ps2.setString(2, jobName);
                    ps2.execute();
                    con.commit();
                    return true;
                }

                logger.info("[JobControlUtils]lockJobControl " + jobName + " , other process is locking this job.");
                return false;
            } else
                return false;
        } catch (SQLException e) {

            logger.error("[JobControlUtils][lockJobControl " + jobName + "] SQLException " + e.getMessage());

            return false;
        } catch (Exception se) {

            logger.error("[JobControlUtils][lockJobControl " + jobName + "] Exception " + se.getMessage());
            return false;
        } finally {

            try {
                MysqlCommonDBUtils.releaseNamedLock(con, jobName);
            } catch (Exception ex) {
                logger.error("releaseNamedLock failed", ex);
            }

            if (rs != null)
                try {
                    rs.close();
                } catch (SQLException ex) {
                    logger.warn("rs close failed", ex);
                }
            if (ps != null)
                try {
                    ps.close();
                } catch (SQLException ex) {
                    logger.warn("rs close failed", ex);
                }
            if (ps2 != null)
                try {
                    ps2.close();
                } catch (SQLException ex) {
                    logger.warn("rs close failed", ex);
                }
            if (priCon != null)
                try {
                    HPFW_Connection.close(priCon);
                } catch (Exception ex) {
                    logger.warn("con close failed", ex);
                }
        }
    }

    public boolean releaseJobControl(String jobName) {
        HPFW_Connection priCon = null;
        Connection con = null;

        String serverId = "";
        serverId = CommonDBUtils.getSERVER_ID();
        if (serverId == null || "".equals(serverId))
            return false;
        PreparedStatement ps = null;
        try {

            priCon = HPFW_Connection.getHPFW_Connection(HPFW_Connection.PRI);
            con = priCon.getConnectionPtr();
            con.setAutoCommit(false);

            if (con == null || con.isClosed())
                return false;
            ps = con.prepareStatement(
                    "update CMC_MARS_SYN_JOB_LOCK set server_id = ?, lock_time = null where SYN_JOB_NAME = ? and server_id = ?");
            ps.setString(1, "N");
            ps.setString(2, jobName);
            ps.setString(3, serverId);
            ps.execute();
            con.commit();
            return true;
        } catch (Exception se) {
            logger.error("[JobControlUtils][releaseJobControl " + jobName + "] " + se.getMessage());
            return false;
        } finally {
            if (ps != null)
                try {
                    ps.close();
                } catch (SQLException ex) {
                    logger.warn("rs close failed", ex);
                }
            if (priCon != null)
                try {
                    HPFW_Connection.close(priCon);
                } catch (Exception ex) {
                    logger.warn("con close failed", ex);
                }
        }
    }

}