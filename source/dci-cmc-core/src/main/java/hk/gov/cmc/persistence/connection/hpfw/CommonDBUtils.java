package hk.gov.cmc.persistence.connection.hpfw;

import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CommonDBUtils {
    private static String SERVER_SEQUENCE_PREFIX = "";
    private static String SERVER_SEQUENCE_PREFIX_2 = "";
    private static String SERVER_ID = "";
    final private static short MAX_SEQ_LENGTH = 20; // actually length is 20-1=19, e.g. T1234567890123456789
    private static HashMap<Integer, String> zeroStringHash = null;

    private static Log logger = LogFactory.getLog(CommonDBUtils.class);

    private static String MYSQL_SEQ_TABLE_NAME = "seq_table";
    private static String MYSQL_EXIST_SEQ_NAME_PREFIX = "SEQ_";

    public static void initzeroStringHash() {
        if (zeroStringHash == null)
            zeroStringHash = new HashMap<Integer, String>();
        else
            zeroStringHash.clear();
        String zeroString = "";
        for (int i = MAX_SEQ_LENGTH - 1; i > 0; i--) {
            zeroStringHash.put(i, zeroString);
            zeroString += "0";
        }
    }

    public static String getSERVER_ID() {
        return SERVER_ID;
    }

    public static void setSERVER_ID(String _SERVER_ID) {
        SERVER_ID = _SERVER_ID;
    }

    public static String getSERVER_SEQUENCE_PREFIX() {
        return SERVER_SEQUENCE_PREFIX;
    }

    public static void setSERVER_SEQUENCE_PREFIX(String _SERVER_SEQUENCE_PREFIX) {
        SERVER_SEQUENCE_PREFIX = _SERVER_SEQUENCE_PREFIX;
    }

    public static String getSERVER_SEQUENCE_PREFIX_2() {
        return SERVER_SEQUENCE_PREFIX_2;
    }

    public static void setSERVER_SEQUENCE_PREFIX_2(String _SERVER_SEQUENCE_PREFIX_2) {
        SERVER_SEQUENCE_PREFIX_2 = _SERVER_SEQUENCE_PREFIX_2;
    }

    public static Timestamp getSystemDate(HPFW_Connection hpfwConnection) {
        Timestamp result = null;
        ResultSet rs = null;
        try {
            String sql = null;
            if (HPFW_Connection.STR_DB_MYSQL.equalsIgnoreCase(HPFW_Connection.getDbName())) {
                sql = "select CURRENT_TIMESTAMP(3) as systimestamp";
            } else {
                sql = "select Cast (systimestamp as timestamp(3)) as systimestamp from dual";
            }
            rs = hpfwConnection.getResultSet(sql, null);
            if (rs == null)
                return null;
            if (rs.next()) {
                result = rs.getTimestamp("systimestamp");
            }
        } catch (SQLException e) {
            if (logger.isErrorEnabled())
                logger.error(e.getMessage());
        } finally {
            HPFW_Connection.close(rs);
        }
        return result;

    }

    public static Timestamp getSystemDate(HPFW_Connection hpfwConnection, String dbName) {
        Timestamp result = null;
        ResultSet rs = null;
        String sql = null;
        try {
            if (HPFW_Connection.STR_DB_MYSQL.equalsIgnoreCase(dbName)) {
                sql = "select CURRENT_TIMESTAMP(3) as systimestamp";
            } else {
                sql = "select Cast (systimestamp as timestamp(3)) as systimestamp from dual";
            }
            rs = hpfwConnection.getResultSet(sql, null);
            if (rs == null)
                return null;
            if (rs.next()) {
                if (HPFW_Connection.STR_DB_MYSQL.equalsIgnoreCase(dbName)) {
                    result = rs.getTimestamp(1);
                } else {
                    result = rs.getTimestamp("systimestamp");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            HPFW_Connection.close(rs);
        }
        return result;

    }

    public static Timestamp getSystemDate(Connection con) {
        Timestamp result = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "";
        try {
            if (HPFW_Connection.STR_DB_MYSQL.equalsIgnoreCase(HPFW_Connection.getDbName())) {
                sql = "select CURRENT_TIMESTAMP(3) as systimestamp";
            } else {
                sql = "select Cast (systimestamp as timestamp(3)) as systimestamp from dual";
            }
            stmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery();
            if (rs.next()) {
                result = rs.getTimestamp("systimestamp");
            }
        } catch (SQLException e) {
            if (logger.isErrorEnabled())
                logger.error(e.getMessage());
        } finally {
            try {
                if (rs != null)
                    rs.close();
                rs = null;
            } catch (SQLException e) {
            }
            try {
                if (stmt != null)
                    stmt.close();
                stmt = null;
            } catch (SQLException e) {
            }
        }
        return result;
    }

    public static Timestamp getSystemDate(Connection con, String dbName) {
        Timestamp result = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql = null;
            if (HPFW_Connection.STR_DB_MYSQL.equalsIgnoreCase(dbName)) {
                sql = "select CURRENT_TIMESTAMP(3) as systimestamp";
            } else {
                sql = "select Cast (systimestamp as timestamp(3)) as systimestamp from dual";
            }
            stmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery();
            if (rs.next()) {
                if (HPFW_Connection.STR_DB_MYSQL.equalsIgnoreCase(dbName)) {
                    result = rs.getTimestamp(1);
                } else {
                    result = rs.getTimestamp("systimestamp");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
                rs = null;
            } catch (SQLException e) {
            }
            try {
                if (stmt != null)
                    stmt.close();
                stmt = null;
            } catch (SQLException e) {
            }
        }
        return result;
    }

    public static String getSequenceNoPrefix(HPFW_Connection hpfwConnection, String seqName) {
        String result = "";
        try {
            result = String.valueOf(
                    MysqlCommonDBUtils.getMySQLTableSequence(hpfwConnection, MYSQL_EXIST_SEQ_NAME_PREFIX + seqName));
        } catch (Exception e) {
            logger.error("getSequenceNoPrefix failed. seqName:" + seqName, e);
        }
        return result;
    }

    public static String getSequence(HPFW_Connection hpfwConnection, String seqName) {
        return getSequence(hpfwConnection, seqName, true);
    }

    private static String getSequence(HPFW_Connection hpfwConnection, String seqName, boolean withPrefix) {
        String result = "";

        int mode = 1;
        if (hpfwConnection.isSimpleConnection())
            mode = 3;
        else if (hpfwConnection.isPrimaryDataSource())
            mode = 1;
        else
            mode = 2;

        try {
            if (withPrefix)
                result = MysqlCommonDBUtils.getNextMachineBaseSequence();
            else
                result = String.valueOf(MysqlCommonDBUtils.getMySQLTableSequence(hpfwConnection,
                        MYSQL_EXIST_SEQ_NAME_PREFIX + seqName));
        } catch (Exception e) {
            logger.error("getSequence failed. seqName:" + seqName + " withPrefix:" + withPrefix, e);
        }
        return result;
    }

    public static String getSequence(Connection con, String seqName, boolean withPrefix, int mode) // mode 1=PRI, 2=SEC, 3=SIMPLE CON
            throws ConnectionFailException, Exception {
        String result = MysqlCommonDBUtils.getNextMachineBaseSequence();

        return result;
    }

    public static void setStatement(PreparedStatement statement, ArrayList<Parameter> paraList) throws SQLException {
        setStatement(statement, paraList, false, "");
    }

    public static void setStatement(PreparedStatement statement, ArrayList<Parameter> paraList, String debugSQL)
            throws SQLException {
        setStatement(statement, paraList, false, debugSQL);
    }

    public static void setStatement(PreparedStatement statement, ArrayList<Parameter> paraList,
            boolean replaceNULLtoDummyValue) throws SQLException {
        setStatement(statement, paraList, replaceNULLtoDummyValue, "");
    }

    public static void setStatement(PreparedStatement statement, int index, Parameter para,
            boolean replaceNULLtoDummyValue, String debugSQL) throws SQLException {
        if (statement == null || para == null)
            return;
        switch (para.type) {
            case Parameter.String:
                statement.setString(index,
                        ((para.value == null || "".equals(para.value)) && replaceNULLtoDummyValue)
                                ? Parameter.NULL_STRING
                                : para.value == null ? "" : (String) para.value);
                break;
            case Parameter.Integer:
                if (replaceNULLtoDummyValue && para.value == null)
                    statement.setInt(index, Parameter.NULL_INT);
                else if (para.value == null)
                    statement.setObject(index, null);
                else
                    statement.setInt(index, ((Integer) para.value).intValue());
                break;
            case Parameter.Long:
                if (replaceNULLtoDummyValue && para.value == null)
                    statement.setLong(index, Parameter.NULL_LONG);
                else if (para.value == null)
                    statement.setObject(index, null);
                else
                    statement.setLong(index, ((Long) para.value).longValue());
                break;
            case Parameter.BigDecimal:
                if (replaceNULLtoDummyValue && para.value == null)
                    statement.setBigDecimal(index, Parameter.NULL_BIGDECIMAL);
                else if (para.value == null)
                    statement.setObject(index, null);
                else
                    statement.setBigDecimal(index, (BigDecimal) para.value);
                break;
            case Parameter.Date:
                if (replaceNULLtoDummyValue && para.value == null)
                    statement.setDate(index, Parameter.NULL_DATE);
                else if (para.value == null)
                    statement.setDate(index, null);
                else
                    statement.setDate(index, (Date) para.value);
                break;
            case Parameter.Timestamp:
                if (replaceNULLtoDummyValue && para.value == null)
                    statement.setTimestamp(index, Parameter.NULL_TIMESTAMP);
                else if (para.value == null)
                    statement.setTimestamp(index, null);
                else
                    statement.setTimestamp(index, (Timestamp) para.value);
                break;
            case Parameter.Clob:
                statement.setClob(index, para.value == null ? null : (Clob) para.value);
                break;
            case Parameter.Blob:
                statement.setBlob(index, para.value == null ? null : (Blob) para.value);
                break;
            default:
                ;
        }
    }

    public static void setStatement(PreparedStatement statement, ArrayList<Parameter> paraList,
            boolean replaceNULLtoDummyValue, String debugSQL)
            throws SQLException {
        if (statement == null || paraList == null || paraList.isEmpty())
            return;
        Iterator<Parameter> itr = paraList.iterator();
        int i = 1;
        while (itr.hasNext()) {
            Parameter para = itr.next();
            try {
                setStatement(statement, i, para, replaceNULLtoDummyValue, debugSQL);
                i++;
            } catch (SQLException e) {
                if (logger.isErrorEnabled()) {
                    logger.error("[CommonDBUtils.setStatement] sql=" + debugSQL);
                    logger.error("[CommonDBUtils.setStatement] index=" + i);
                    logger.error("[CommonDBUtils.setStatement] type=" + para.type);
                    logger.error("[CommonDBUtils.setStatement] value=" + para.value);
                }
                for (int ii = 0; i < paraList.size(); i++) {
                    Parameter p = paraList.get(ii);
                    logger.error("[CommonDBUtils.setStatement] " + ii + ". " + p.type + "|" + para.value);
                }
                throw e;
            }
        }
    }

    private static String getMySQLSeq(HPFW_Connection tranConn, String seqName) throws Exception {
        String result = "";
        Connection tConn = null;
        try {
            String selectSQL = "select seq_value+1 as nextval from " + MYSQL_SEQ_TABLE_NAME
                    + " where table_name = ? for update";
            String updatesql = "update " + MYSQL_SEQ_TABLE_NAME + " set seq_value = seq_value+1 where table_name = ?";

            tConn = tranConn.getConnectionPtr();
            tranConn.setAutoCommit(false);
            PreparedStatement tranPS = tConn.prepareStatement(selectSQL);
            tranPS.setString(1, seqName);
            ResultSet tranRS = tranPS.executeQuery();
            long nextVal = -1;
            if (tranRS.next()) {
                nextVal = tranRS.getLong("nextval");
            }
            tranPS = null;
            tranRS.close();

            if (nextVal > 0) {
                tranPS = tConn.prepareStatement(updatesql);
                tranPS.setString(1, seqName);
                tranPS.executeUpdate();
            }
            tranConn.commit();
            result = nextVal + "";
        } catch (Exception ex) {
            if (tConn != null) {
                try {
                    tConn.rollback();
                } catch (Exception ignore) {
                }
            }
            throw ex;
        } finally {
        }
        return result;
    }
}
