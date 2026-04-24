package hk.gov.cmc.dao.healthcheck;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import hk.gov.cmc.persistence.connection.hpfw.HPFW_Connection;
import hk.gov.cmc.persistence.connection.hpfw.Parameter;

public class HealthCheckDAO {

    private static Log logger = LogFactory.getLog(HealthCheckDAO.class);

    private static final String HEALTH_CHECK_NUM_OF_IAS_MESSAGE_PER_TEMPLATE = "select CONCAT(IFNULL(template_id, ''), ', ', IFNULL(template_version, '')) AS template_list, count(1) cnt "
            + "from ias_message where create_dt between DATE(now() - INTERVAL 1 DAY) and DATE(now()) "
            + "group by template_id, template_version order by template_id, template_version ";

    private static final String NUM_OF_IAS_MESSAGE_HOUSEKEEP_NEXT_MONTH = "select count(1) as resultValue from ias_user_message "
            + "where create_dt < (STR_TO_DATE(DATE_FORMAT(NOW() ,'%Y%m01'), '%Y%m%d') - INTERVAL 23 MONTH) ";

    private static final String NUM_OF_IAS_MSG_ISSUED_PER_SP_PREVIOUS_MONTH = ""
            + "select DATE_FORMAT(NOW() - INTERVAL 1 MONTH, '%Y-%m') as currentmonth, t.service_provider_id, count(1) as cnt "
            + "from ias_user_message um, ias_message m, cmc_template t "
            + "where um.create_dt between STR_TO_DATE(DATE_FORMAT(NOW() - INTERVAL 1 MONTH, '%Y%m01'), '%Y%m%d') and STR_TO_DATE(DATE_FORMAT(NOW() ,'%Y%m01'), '%Y%m%d') "
            + "and um.ias_msg_id = m.ias_msg_id "
            + "and m.template_id = t.template_id "
            + "and m.template_version = t.template_version "
            + "group by DATE_FORMAT(NOW() - INTERVAL 1 MONTH, '%Y-%m') , t.service_provider_id "
            + "order by currentmonth, service_provider_id";

    private static final String HEALTH_CHECK_NUM_OF_IAS_NOTI_SENT_WITH_ERROR = " select count(1) as cnt " +
            " from ias_user_message ium" +
            " where ium.create_dt between DATE(now() - INTERVAL 1 DAY) and DATE(now()) " +
            " and ium.ias_noti_status = 'S' " +
            " and ium.ias_noti_result = '4'";

    private static final String HEALTH_CHECK_NUM_OF_IAS_NOTI_NOT_SENT_SUCCESS = " select count(1) as cnt " +
            " from ias_user_message ium" +
            " where ium.ias_noti_status = 'N' and delete_ind <> 'Y'" +
            " and ium.create_dt < now() - INTERVAL 1 HOUR";

    private static final String HEALTH_CHECK_NUM_OF_GET_NOTI_ID_NOT_SUCCESS = " select count(1) as cnt " +
            " from ias_user_message ium" +
            " where ium.create_dt between DATE(now() - INTERVAL 1 DAY) and DATE(now()) " +
            " and ium.ias_noti_status = 'I' ";

    private static final String HEALTH_CHECK_NUM_OF_IAS_ACC_STATUS_UPDATE = "select count(1) as cnt " +
            "from ias_user_noti_info " +
            "where last_modify_dt between DATE(now() - INTERVAL 1 DAY) and DATE(now()) ";

    private static final String SELECT_OVERTIME_LOCKING_SYNC_JOB = "SELECT SYN_JOB_NAME, SERVER_ID, DATE_FORMAT(LOCK_TIME, '%Y-%m-%d, %H:%i:%s') "
            + " FROM CMC_MARS_SYN_JOB_LOCK "
            + " WHERE LOCK_TIME < ? ";

    public List<String> retrieveNumOfIasMsgPerTemplate(HPFW_Connection cn) throws Exception {
        List<String> returnList = new ArrayList<String>();
        ResultSet rs = null;
        String template_list = null;
        int cnt = -1;

        try {
            ArrayList<Parameter> params = new ArrayList<Parameter>();
            rs = cn.getResultSet(HEALTH_CHECK_NUM_OF_IAS_MESSAGE_PER_TEMPLATE, params);
            while (rs.next()) {
                template_list = rs.getString("template_list");
                cnt = rs.getInt("cnt");
                returnList.add(template_list + ", " + cnt);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (rs != null) {
                rs.close();
            }
        }

        return returnList;
    }

    public String retrieveNumOfIasMessageHouseKeepNextMonth(HPFW_Connection cn) throws Exception {
        StringBuilder result = new StringBuilder();
        ResultSet rs = null;
        try {
            // ArrayList<Parameter> params = new ArrayList<Parameter>();
            rs = cn.getResultSet(NUM_OF_IAS_MESSAGE_HOUSEKEEP_NEXT_MONTH, null);
            while (rs.next()) {
                String resultValue = rs.getString("resultValue");
                result.append("\t" + resultValue + " \r\n");
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
        return result.toString();
    }

    public List<String> retrieveNumOfIasMsgIssuedPerSPPreviousMonth(HPFW_Connection hpfwConn) throws Exception {
        List<String> returnList = new ArrayList<String>();
        ResultSet rs = null;
        try {
            ArrayList<Parameter> params = new ArrayList<Parameter>();
            rs = hpfwConn.getResultSet(NUM_OF_IAS_MSG_ISSUED_PER_SP_PREVIOUS_MONTH, params);
            while (rs.next()) {
                String currentMonth = rs.getString("currentmonth");
                String serviceProviderId = rs.getString("service_provider_id");
                int cnt = rs.getInt("cnt");
                returnList.add(currentMonth + ", " + serviceProviderId + ", " + cnt);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
        return returnList;
    }

    public int retrieveNumOfIasNotiSentWithError(HPFW_Connection cn) throws Exception {

        int cnt = -1;
        ResultSet rs = null;
        try {
            ArrayList<Parameter> params = new ArrayList<Parameter>();

            rs = cn.getResultSet(HEALTH_CHECK_NUM_OF_IAS_NOTI_SENT_WITH_ERROR, params);
            if (rs.next()) {
                cnt = rs.getInt("cnt");
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (rs != null) {
                rs.close();
            }
        }

        return cnt;
    }

    public int retrieveNumOfIasNotiNotSendSuccess(HPFW_Connection cn) throws Exception {

        int cnt = -1;
        ResultSet rs = null;
        try {
            ArrayList<Parameter> params = new ArrayList<Parameter>();

            rs = cn.getResultSet(HEALTH_CHECK_NUM_OF_IAS_NOTI_NOT_SENT_SUCCESS, params);
            if (rs.next()) {
                cnt = rs.getInt("cnt");
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (rs != null) {
                rs.close();
            }
        }

        return cnt;
    }

    public int retrieveNumOfGetNotiIdNotSuccess(HPFW_Connection cn) throws Exception {

        int cnt = -1;
        ResultSet rs = null;
        try {
            ArrayList<Parameter> params = new ArrayList<Parameter>();

            rs = cn.getResultSet(HEALTH_CHECK_NUM_OF_GET_NOTI_ID_NOT_SUCCESS, params);
            if (rs.next()) {
                cnt = rs.getInt("cnt");
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (rs != null) {
                rs.close();
            }
        }

        return cnt;
    }

    public int retrieveNumOfIasAccStatusUpdate(HPFW_Connection cn) throws Exception {

        int cnt = -1;
        ResultSet rs = null;
        try {
            ArrayList<Parameter> params = new ArrayList<Parameter>();

            rs = cn.getResultSet(HEALTH_CHECK_NUM_OF_IAS_ACC_STATUS_UPDATE, params);
            if (rs.next()) {
                cnt = rs.getInt("cnt");
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (rs != null) {
                rs.close();
            }
        }

        return cnt;
    }

    public void checkOverTimeLockingJobs(HPFW_Connection conn, int lockThresholdInHour) throws Exception {

        logger.debug("checkOverTimeLockingJobs - START");
        ResultSet rs = null;
        try {

            Calendar timeoutCal = Calendar.getInstance();

            timeoutCal.add(Calendar.HOUR_OF_DAY, -lockThresholdInHour);

            ArrayList<Parameter> paraList = new ArrayList<Parameter>();
            paraList.add(new Parameter(Parameter.Timestamp, new Timestamp(timeoutCal.getTime().getTime())));

            rs = conn.getResultSet(SELECT_OVERTIME_LOCKING_SYNC_JOB, paraList);
            while (rs.next()) {

                String synJobName = rs.getString(1);
                String serverId = rs.getString(2);
                String lockTime = rs.getString(3);

                logger.error("[HEALTH_CHECK] Locking time for [" + synJobName + "] by server [" + serverId
                        + "] is over " + lockThresholdInHour + " hours. LockTime=[" + lockTime + "]");

            }
        } catch (Exception ex) {
            logger.error("General exception caught in checkOverTimeLockingJobs", ex);
            throw ex;
        } finally {
            try {
                if (rs != null)
                    HPFW_Connection.close(rs);
            } catch (Exception ex) {
                logger.error("General exception caught in checkOverTimeLockingJobs - rs.close();", ex);
            }
            logger.debug("checkOverTimeLockingJobs - END");
        }
    }
}
