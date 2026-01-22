package hk.gov.cmc.persistence.connection.hpfw;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import hk.gov.gcis.rm.common.utils.StringUtils;

public class MysqlCommonDBUtils implements Serializable {

    private static final long serialVersionUID = 8599740163024133794L;

    private static Log logger = LogFactory.getLog(MysqlCommonDBUtils.class);
    private static String MYSQL_SEQ_TABLE_NAME = "SEQ_STORE";
    private static int seqRetryCnt = 7;

    private static int runningNumber = 0;
    private static long preRunningTimeInSecond = 0L;
    private static String seqPrefix = HPFW_Connection.getMysqlSelfGenSeqPrefix();

    private static final String SQL_ACQUIRE_LOCK = "SELECT GET_LOCK(?,1)";
    private static final String SQL_RELEASE_LOCK = "select release_lock(?);";
    private static final String SQL_CHECK_LOCK = "SELECT IS_USED_LOCK(?)";

    public static int queryTimeOutInSecond = 3;

    private static String selectSQL = "select "
            + "SEQ_NAME,SEQ_NUM,MAX_VALUE,MIN_VALUE,CYCLE_IND,INCR_BY,CREATE_DT,LAST_MODIFY_DT,CREATE_BY,LAST_MODIFY_BY "
            + " FROM " + MYSQL_SEQ_TABLE_NAME + " WHERE SEQ_NAME = ? ";

    private static String updatesql = "UPDATE " + MYSQL_SEQ_TABLE_NAME + " SET SEQ_NUM = ?, LAST_MODIFY_DT = now(3) "
            + " WHERE SEQ_NAME = ? AND SEQ_NUM = ? ";

    /*
     * Unique ID generator by Java
     * i. Primary key prefixed with site ID
     * ii. 20 characters Unique ID format:
     * 1-Char Site ID + 2-Char App ID + 1-Digit Server ID + 12-Digit Time-lapse in second from Epoch + 4-Digit Running Number
     */
    public static synchronized String getNextMachineBaseSequence() {
        String sequenceString = "";
        long currRunningTimeInSecond = 0;
        currRunningTimeInSecond = getCurrentRunningTimeInSecond();

        if (preRunningTimeInSecond == 0) {
            preRunningTimeInSecond = currRunningTimeInSecond;
        }
        if (preRunningTimeInSecond == currRunningTimeInSecond) {
            if (runningNumber >= 9999) {
                wait(1000);
                currRunningTimeInSecond = getCurrentRunningTimeInSecond();
            } else {
                runningNumber = runningNumber + 1;
                sequenceString = seqPrefix + StringUtils.leftPad(String.valueOf(currRunningTimeInSecond), "0", 12)
                        + StringUtils.leftPad(String.valueOf(runningNumber), "0", 4);
            }
        }
        if (preRunningTimeInSecond != currRunningTimeInSecond) {
            runningNumber = 1;
            sequenceString = seqPrefix + StringUtils.leftPad(String.valueOf(currRunningTimeInSecond), "0", 12)
                    + StringUtils.leftPad(String.valueOf(runningNumber), "0", 4);
            preRunningTimeInSecond = currRunningTimeInSecond;
        }

        return sequenceString;
    }

    private static void wait(int mSeconds) {
        try {
            Thread.sleep(mSeconds);
        } catch (InterruptedException e) {
            logger.error(e);
        }
    }

    private static long getCurrentRunningTimeInSecond() {
        Calendar currRunningTime = Calendar.getInstance();
        String strCurrRunningInSecond = Long.toString(currRunningTime.getTimeInMillis());
        strCurrRunningInSecond = strCurrRunningInSecond.substring(0, (strCurrRunningInSecond.length() - 3));
        return Long.parseLong(strCurrRunningInSecond);
    }

    public static Long getMySQLTableSequence(HPFW_Connection tranConn, String seqName) throws SQLException {
        Connection tConn = tranConn.getConnectionPtr();
        return getMySQLTableSequence(tConn, seqName);
    }

    public static Long getMySQLTableSequence(Connection tConn, String seqName) throws SQLException {
        // Connection tConn = null;
        Long nextSequence = null;
        PreparedStatement ps = null;
        ResultSet rSet = null;
        MysqlSeqStore seqStore = null;

        try {

            tConn.setAutoCommit(false);

            for (int i = 0; i < seqRetryCnt; i++) {
                seqStore = null;
                ps = tConn.prepareStatement(selectSQL);
                ps.setString(1, seqName.toUpperCase());
                rSet = ps.executeQuery();
                long currVal = -1;
                if (rSet.next()) {

                    seqStore = new MysqlSeqStore();
                    seqStore.setSeqName(rSet.getString("SEQ_NAME"));
                    seqStore.setSeqNum(rSet.getLong("SEQ_NUM"));
                    seqStore.setMaxValue(rSet.getLong("MAX_VALUE"));
                    seqStore.setMinValue(rSet.getLong("MIN_VALUE"));
                    seqStore.setCycleInd(rSet.getString("CYCLE_IND"));
                    seqStore.setIncrBy(rSet.getInt("INCR_BY"));
                    seqStore.setCreateDt(rSet.getDate("CREATE_DT"));
                    seqStore.setLastModifyDt(rSet.getDate("LAST_MODIFY_DT"));
                    seqStore.setCreateBy(rSet.getString("CREATE_BY"));
                    seqStore.setLastModifyBy(rSet.getString("LAST_MODIFY_BY"));
                }

                rSet.close();
                rSet = null;
                ps.close();
                ps = null;

                if (seqStore != null && seqStore.getSeqNum() > 0 && seqStore.getIncrBy() > 0) {
                    currVal = seqStore.getSeqNum() + seqStore.getIncrBy();
                    if (currVal > seqStore.getMaxValue() && "Y".equalsIgnoreCase(seqStore.getCycleInd())) {
                        currVal = seqStore.getMinValue() + seqStore.getIncrBy();
                    }
                    ps = tConn.prepareStatement(updatesql);
                    ps.setLong(1, (currVal));
                    ps.setString(2, seqName);
                    ps.setLong(3, seqStore.getSeqNum());
                    int noOfRowsUpdate = ps.executeUpdate();
                    if (noOfRowsUpdate > 0) {
                        nextSequence = currVal;
                        break;
                    } else {
                        wait(250);
                    }
                }
            }
            tConn.commit();
            if (nextSequence == null) {
                throw new SQLException(
                        "Retrieve mysql sequence fail after trying " + seqRetryCnt + " times,please try again!");
            }
        } catch (Exception ex) {
            if (tConn != null) {
                try {
                    tConn.rollback();
                } catch (Exception ignore) {
                }
            }
            throw new SQLException(ex.toString());
        } finally {

            if (rSet != null) {
                try {
                    rSet.close();
                } catch (SQLException e) {
                    logger.error("Close result set fail");
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    logger.error("Close cursor fail");
                }
            }
        }
        return nextSequence;
    }

    public static boolean acquireNamedLock(Connection conn, String name)
            throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        try {
            logger.debug("acquireBatchLock: Start : " + name);

            conn.setAutoCommit(false);

            ps = conn.prepareStatement(SQL_CHECK_LOCK);
            ps.setString(1, name);
            rs2 = ps.executeQuery();
            if (!rs2.next()) {
                throw new SQLException("Unexpected Error: no row return for checking lock");
            }
            String checkResult = rs2.getString(1);
            logger.debug("acquireBatchLock: check lock result=" + checkResult);
            if (checkResult == null) {
                ps = conn.prepareStatement(SQL_ACQUIRE_LOCK);
                ps.setString(1, name);
                rs = ps.executeQuery();
                if (!rs.next()) {
                    throw new SQLException("Unexpected Error: no row return for acquiring lock");
                }
                int result = rs.getInt(1);
                logger.debug("acquireBatchLock: acquire lock result=" + result);
                conn.commit();
                if (result == 1) {
                    logger.debug("AcquireBatchLock: successfully to acquire lock, name = " + name);
                    return true;
                } else {
                    logger.debug("AcquireBatchLock: fail to acquire lock, name= " + name);
                    return false;
                }

            } else {
                logger.debug("acquireBatchLock: fail to acquire lock due to IS_USED_LOCK result");
                return false;
            }

        } catch (Exception e) {
            if (conn != null)
                conn.rollback();
            logger.error("Error in acquireBatchLock.");
            throw new SQLException(e.toString(), e);
        } finally {
            close(rs2);
            close(rs);
        }
    }

    public static void releaseNamedLock(Connection conn, String name)
            throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        logger.debug("releaseBatchLock - Start : " + name);
        try {
            conn.setAutoCommit(false);
            ps = conn.prepareStatement(SQL_RELEASE_LOCK);
            ps.setString(1, name);
            rs = ps.executeQuery();
            if (!rs.next()) {
                throw new SQLException("Fail to release Exclusive Batch Lock: " + name);
            }
            String result = rs.getString(1);
            logger.debug("releaseBatchLock: release lock result=" + result);

            conn.commit();
        } catch (Exception e) {
            if (conn != null)
                conn.rollback();
            logger.error("Error in releaseBatchLock.");
            throw new SQLException(e.toString(), e);
        } finally {
            close(rs);
            logger.debug("releaseBatchLock - End ");
        }
    }

    public static void close(ResultSet rs) {
        try {
            if (rs != null) {
                Statement ps = rs.getStatement();
                rs.close();
                rs = null;
                if (ps != null)
                    ps.close();
                ps = null;
            }
        } catch (Exception e) {
        }
    }

}
