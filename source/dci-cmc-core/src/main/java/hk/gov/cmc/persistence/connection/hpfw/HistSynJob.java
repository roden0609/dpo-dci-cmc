package hk.gov.cmc.persistence.connection.hpfw;

import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import hk.gov.cmc.utils.common.StringUtils;

public class HistSynJob {
    private static Log logger = LogFactory.getLog(HistSynJob.class);

    static private final String DB_HIST_SYN_BY_THREAD_ENABLED = "DB_HIST_SYN_BY_THREAD_ENABLED";
    static private final String DB_HIST_TABLE_LIST = "DB_HIST_TABLE_LIST";
    static private HashMap<String, Table> tableSchema = null;
    static private HashMap<String, String> tableListHash = null;
    static private HashMap<String, String> selectSQLHash = null;
    static private HashMap<String, ArrayList<String>> motherSonHash = null;
    static private HashMap<String, String> sonHash = null;

    static private int ORACLE_IN_MAX = 950;
    static {
        motherSonHash = new HashMap<String, ArrayList<String>>();

        sonHash = new HashMap<String, String>();
        ArrayList<String> sonList = new ArrayList<String>();

        sonList.add("CMC_USER_LINK_STATUS_H");
        sonHash.put("CMC_USER_LINK_STATUS_H", "CMC_USER_LINK_STATUS_H");

        sonList.add("CMC_USER_RECON_EXCEPTION_H");
        sonHash.put("CMC_USER_RECON_EXCEPTION_H", "CMC_USER_RECON_EXCEPTION_H");

        sonList.add("CMC_LS_RECON_EXCEPTION_H");
        sonHash.put("CMC_LS_RECON_EXCEPTION_H", "CMC_LS_RECON_EXCEPTION_H");

        sonList.add("CMC_MESSAGE_H");
        sonHash.put("CMC_MESSAGE_H", "CMC_MESSAGE_H");

        sonList.add("CMC_USER_MESSAGE_H");
        sonHash.put("CMC_USER_MESSAGE_H", "CMC_USER_MESSAGE_H");

        sonList.add("CMC_TO_DO_ITEM_H");
        sonHash.put("CMC_TO_DO_ITEM_H", "CMC_TO_DO_ITEM_H");

        sonList.add("CMC_USER_TO_DO_ITEM_H");
        sonHash.put("CMC_USER_TO_DO_ITEM_H", "CMC_USER_TO_DO_ITEM_H");

        motherSonHash.put("CMC_USER_H", sonList);
    }

    static private final String getFieldsSQL = "select distinct t.column_name, t.data_type, t.data_length, t.data_precision, t.data_scale, t.nullable, pk.isPK, t.column_id "
            +
            "from all_tab_columns t , user_synonyms us1,  " +
            "(select ucc.column_name, 'PK' isPK " +
            " from all_cons_columns ucc, all_constraints uc, user_synonyms us2  " +
            " where " +
            " upper(us2.table_name) = upper(uc.table_name) and  " +
            " upper(uc.owner) = upper(us2.table_owner) and  " +
            " upper(ucc.owner) = upper(us2.table_owner) and  " +
            " upper(uc.table_name) = upper(?) and " +
            " uc.CONSTRAINT_TYPE = 'P' and uc.CONSTRAINT_NAME = ucc.CONSTRAINT_NAME) pk " +
            " where " +
            " upper(us1.table_name) = upper(t.table_name) and  " +
            " upper(t.owner) = upper(us1.table_owner) and  " +
            " upper(t.table_name) = upper(?) and " +
            " t.column_name = pk.column_name (+) " +
            " order by pk.isPK, t.column_id ";

    public static void initJob(Properties properties) {
        String dsname1 = properties.getProperty(HPFW_Connection.DB_CONNECTION_DS_NAME_1_PROPERTY_NAME);
        String dsname2 = properties.getProperty(HPFW_Connection.DB_CONNECTION_DS_NAME_2_PROPERTY_NAME);
        String synByThreadEnabled = (properties.getProperty(DB_HIST_SYN_BY_THREAD_ENABLED) == null) ? "N"
                : properties.getProperty(DB_HIST_SYN_BY_THREAD_ENABLED);
        boolean synByThread = (synByThreadEnabled.equals("Y")) ? true : false;
        String tableList1 = properties.getProperty(DB_HIST_TABLE_LIST);
        if ((tableList1 == null) || dsname1 == null || dsname2 == null)
            return;
        if (tableListHash == null) {
            tableListHash = new HashMap<String, String>();
            tableSchema = new HashMap<String, Table>();
            selectSQLHash = new HashMap<String, String>();
        } else {
            tableListHash.clear();
            tableSchema.clear();
            selectSQLHash.clear();
        }
        if (tableList1 != null)
            InitSubJob(synByThread, tableList1, HPFW_Connection.DIRECT_WITH_HISTORY);
    }

    private static HashMap<String, String> initLCKTable(Connection conn, String tableList) {
        String tableArray[] = tableList.split(",");
        HashMap<String, String> result = new HashMap<String, String>();
        try {
            if (conn == null || conn.isClosed() || tableArray == null || tableArray.length < 1) {
                return result;
            }

            for (int i = 0; i < tableArray.length; i++) {
                String tableName = tableArray[i];
                PreparedStatement ps = conn
                        .prepareStatement("select syn_job_name from CMC_MARS_SYN_JOB_LOCK where syn_job_name = ?");
                ps.setString(1, tableName);
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    PreparedStatement ps2 = conn
                            .prepareStatement("insert into CMC_MARS_SYN_JOB_LOCK (syn_job_name) values (?)");
                    ps2.setString(1, tableName);
                    ps2.execute();
                    conn.commit();
                    ps2.close();
                    ps2 = null;
                }
                result.put(tableName, tableName);
                rs.close();
                rs = null;
                ps.close();
                ps = null;

            }
        } catch (Exception ex) {
            logger.info("CMC_MARS_SYN_JOB_LOCK is not exists, syn _H is disable!");
        }
        return result;
    }

    public static void InitSubJob(boolean synByThread, String tableList, String mode) {
        String tableArray[] = tableList.split(",");
        HPFW_Connection hpfwConn = null;
        HashMap<String, String> initLCKTable = null;
        try {
            hpfwConn = HPFW_Connection.getHPFW_Connection(HPFW_Connection.PRI);
            Connection conn = hpfwConn.getConnectionPtr();
            conn.setAutoCommit(false);
            initLCKTable = initLCKTable(conn, tableList);

            for (int i = 0; i < tableArray.length; i++) {
                String tableName = tableArray[i];
                if (initLCKTable.containsKey(tableName)) {

                    PreparedStatement pstmt = null;
                    ResultSet rs = null;
                    try {
                        tableListHash.put(tableName, mode);
                        pstmt = conn.prepareStatement(getFieldsSQL);
                        pstmt.setString(1, tableName.substring(0, tableName.length() - 2));
                        pstmt.setString(2, tableName.substring(0, tableName.length() - 2));
                        rs = pstmt.executeQuery();

                        Table table = new Table(tableName);
                        String selectSQL = "select ";
                        int ii = 1;
                        logger.debug("[HistSynJob InitSubJob] Table Name = " + tableName);
                        while (rs.next()) {
                            String columnName = rs.getString("column_name");
                            if (!columnName.equals("HIST_SEQ")
                                    && !columnName.equals("HIST_ACTION")
                                    && !columnName.equals("HIST_STATUS")
                                    && !columnName.equals("HIST_UPDATE_TYPE")) {
                                String nullable = rs.getString("nullable");
                                String data_type = rs.getString("data_type");
                                int data_precision = rs.getInt("data_precision");
                                int data_scale = rs.getInt("data_scale");

                                short fieldType = Parameter.oracleTypeToShort(data_type, data_precision, data_scale);
                                table.addColum((new TableColumn(columnName, fieldType, rs.getString("isPK") != null,
                                        "Y".equals(nullable))));
                                logger.debug("[HistSynJob InitSubJob] " + ii + ". " + columnName + "|type-" + fieldType
                                        + "|isPK=" + (rs.getString("isPK") != null) + "|nullable=" + nullable);
                                selectSQL += columnName + ",";
                                ii++;
                            }
                        }

                        selectSQL += "HIST_ACTION,HIST_SEQ,HIST_UPDATE_TYPE from " + tableName;
                        selectSQLHash.put(tableName, selectSQL);
                        tableSchema.put(tableName, table);
                    } catch (Exception se) {

                        if (logger.isErrorEnabled())
                            logger.error("[InitSubJob " + tableName + "] " + se.getMessage());
                    } finally {
                        pstmt.close();
                        pstmt = null;
                        rs.close();
                        rs = null;
                    }
                }
            }

            Iterator<String> itr = tableListHash.keySet().iterator();
            while (itr.hasNext()) {
                String t = itr.next();
                if (sonHash.containsKey(t)) {
                    logger.debug(t + " is a son table, syn job will not be fired.");
                } else {
                    String m = tableListHash.get(t);
                    if (synByThread) {
                        JobThread e = new JobThread();
                        e.init(t, m);
                        e.start();
                        logger.debug("[InitSubJob] Fire new syn job by thread - " + t);
                    } else {
                        logger.debug("[InitSubJob] Fire new syn job by cron - " + t);
                        HistSynJob.syn(t, m);
                    }
                }
            }
        } catch (SQLException se) {

            if (logger.isErrorEnabled())
                logger.error("[InitSubJob] " + se.getMessage());
        } finally {
            HPFW_Connection.close(hpfwConn);
        }
    }

    private static void update_HRecords(Connection con, String tName, String status, String condition,
            ArrayList<String> failList) throws SQLException {
        String runningSQL = "";
        PreparedStatement pstmt = null;
        PreparedStatement pstmt1 = null;
        try {
            if (failList == null || failList.isEmpty()) {

                runningSQL = "update " + tName
                        + " set LAST_MODIFY_BY = ?, LAST_MODIFY_DT = SYSDATE, HIST_STATUS = ? where HIST_STATUS = ? ";
                pstmt = con.prepareStatement(runningSQL);
                pstmt.setString(1, "SYSTEM");
                pstmt.setString(2, status);
                pstmt.setString(3, condition);
                pstmt.execute();
            } else {
                logger.warn("[HEALTH_CHECK] DB SYNCHRONIZATION fail case is found. table [" + tName
                        + "], failList.size()=" + failList.size());

                String str[] = new String[failList.size()];
                failList.toArray(str);

                int max = failList.size();
                logger.debug("failList.size ()=" + max);
                int batchSize = failList.size() % ORACLE_IN_MAX == 0 ? failList.size() / ORACLE_IN_MAX
                        : failList.size() / ORACLE_IN_MAX + 1;
                logger.debug("batchSize=" + batchSize);

                for (int i = 1; i <= batchSize; i++) {
                    int from = (i - 1) * ORACLE_IN_MAX;
                    int to = i == batchSize ? max : i * ORACLE_IN_MAX;
                    String failStr = StringUtils.array2String(str, true, from, to);

                    logger.warn("Sync fail case is found in table[" + tName + "]. batch[" + i + "], HIST_SEQ list="
                            + failStr);

                    runningSQL = "update " + tName +
                            " set LAST_MODIFY_BY = ?, " +
                            "LAST_MODIFY_DT = SYSDATE, " +
                            "HIST_STATUS = ? " +
                            "where " +
                            "HIST_SEQ in (" + failStr + ") ";

                    pstmt1 = con.prepareStatement(runningSQL);
                    pstmt1.setString(1, "SYSTEM");
                    pstmt1.setString(2, "F");
                    pstmt1.execute();
                }

                str = null;
                runningSQL = "update " + tName +
                        " set LAST_MODIFY_BY = ?, " +
                        "LAST_MODIFY_DT = SYSDATE, " +
                        "HIST_STATUS = ? " +
                        "where " +
                        "HIST_STATUS = ? ";

                pstmt = con.prepareStatement(runningSQL);
                pstmt.setString(1, "SYSTEM");
                pstmt.setString(2, status);
                pstmt.setString(3, condition);
                pstmt.execute();

            }
        } catch (SQLException se) {
            if (logger.isErrorEnabled()) {
                logger.error("[update_HRecords] Error SQL = " + runningSQL);
                logger.error("[update_HRecords] Error SQL para, status = " + status + ",condition=" + condition);
            }
            throw se;
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (Exception e) {
                }
                pstmt = null;
            }
            if (pstmt1 != null) {
                try {
                    pstmt1.close();
                } catch (Exception e) {
                }
                pstmt1 = null;
            }
        }
    }

    private static boolean releaseCMC_MARS_SYN_JOB_LOCK(Connection con, String tName) {
        String serverId = "";
        serverId = CommonDBUtils.getSERVER_ID();
        if (serverId == null || "".equals(serverId))
            return false;
        PreparedStatement ps = null;
        try {
            if (con == null || con.isClosed())
                return false;
            ps = con.prepareStatement(
                    "update CMC_MARS_SYN_JOB_LOCK set server_id = ?, lock_time = null where SYN_JOB_NAME = ? and server_id = ?");
            ps.setString(1, "N");
            ps.setString(2, tName);
            ps.setString(3, serverId);
            ps.execute();
            con.commit();
            return true;
        } catch (SQLException se) {

            if (logger.isErrorEnabled())
                logger.error("[releaseCMC_MARS_SYN_JOB_LOCK " + tName + "] " + se.getMessage());
            return false;
        } finally {
            if (ps != null)
                try {
                    ps.close();
                } catch (SQLException ignore) {
                }
        }
    }

    private static boolean lockCMC_MARS_SYN_JOB_LOCK(Connection con, String tName) {
        String serverId = "";
        serverId = CommonDBUtils.getSERVER_ID();
        if (serverId == null || "".equals(serverId))
            return false;
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;
        try {
            if (con == null || con.isClosed())
                return false;
            ps = con.prepareStatement(
                    "select server_id, lock_time from CMC_MARS_SYN_JOB_LOCK where SYN_JOB_NAME = ? for update nowait");
            ps.setString(1, tName);
            rs = ps.executeQuery();
            if (rs.next()) {
                String currentLckServer = rs.getString("server_id");
                if ("N".equals(currentLckServer) || serverId.equals(currentLckServer)) {

                    ps2 = con.prepareStatement(
                            "update CMC_MARS_SYN_JOB_LOCK set server_id = ?, lock_time = now(3) where SYN_JOB_NAME = ?");

                    ps2.setString(1, serverId);
                    ps2.setString(2, tName);
                    ps2.execute();
                    con.commit();
                    return true;
                }
                return false;
            } else
                return false;
        } catch (SQLException se) {
            int errorCode = se.getErrorCode();
            if (HPFW_Connection.isNetworkError(errorCode)) {
                logger.error("[lockCMC_MARS_SYN_JOB_LOCK " + tName + " network error] " + se.getMessage());
            } else {
                if (se != null && se.getMessage() != null && se.getMessage().indexOf("ORA-00054") > -1) {
                    logger.debug("[lockCMC_MARS_SYN_JOB_LOCK]Another job is locking " + tName + ", wait for next run.",
                            se);
                } else {
                    logger.error("[lockCMC_MARS_SYN_JOB_LOCK " + tName + " SQLException error] " + se.getMessage());
                }
            }
            return false;
        } catch (Exception e) {
            logger.error("[lockCMC_MARS_SYN_JOB_LOCK " + tName + " general error] " + e.getMessage());
            return false;
        } finally {
            if (rs != null)
                try {
                    rs.close();
                } catch (SQLException ignore) {
                }
            if (ps != null)
                try {
                    ps.close();
                } catch (SQLException ignore) {
                }
            if (ps2 != null)
                try {
                    ps2.close();
                } catch (SQLException ignore) {
                }
        }
    }

    private static void releaseRecords(Connection con, String tName, ArrayList<String> failList) throws SQLException {
        update_HRecords(con, tName, "S", CommonDBUtils.getSERVER_SEQUENCE_PREFIX(), failList);
    }

    private static void lockRecords(Connection con, String tName) throws SQLException {
        update_HRecords(con, tName, CommonDBUtils.getSERVER_SEQUENCE_PREFIX(), "P", null);
    }

    private static int rowToDb(String tName, Connection primaryConnection, Connection SecondaryConnection,
            ArrayList<Row> rows, Table schema, ArrayList<String> failList) {
        logger.debug(rows.size() + " " + tName + " rows are waiting for syn!");
        Iterator<Row> itr = rows.iterator();
        int suuccessCount = 0;
        while (itr.hasNext()) {
            Row r = itr.next();
            PreparedStatement rowPS = null;
            PreparedStatement rowPS2 = null;
            String histUpdateType = r.getHistUpdateType();
            if (HPFW_Connection.REMOTE.equals(histUpdateType)) {
                if (HPFW_Connection.INSERT.equals(r.getActiion())
                        || r.checkLastUpdateDt(SecondaryConnection, schema)) {
                    String sql = "";
                    try {
                        sql = HPFW_Connection.INSERT.equals(r.getActiion()) ? r.insertSQLString
                                : HPFW_Connection.DELETE.equals(r.getActiion()) ? r.deleteSQLString : r.updateSQLString;
                        rowPS = r.getActionStatement(SecondaryConnection, schema);
                        rowPS.execute();
                        SecondaryConnection.commit();
                        suuccessCount++;
                    } catch (SQLException e) {
                        if (logger.isErrorEnabled())
                            logger.error("[rowToDb " + tName + "] Error SQL = " + sql);

                        if (logger.isErrorEnabled())
                            logger.error("[rowToDb " + tName + "] " + e.getMessage());
                        try {
                            SecondaryConnection.rollback();
                        } catch (SQLException se) {
                            if (logger.isErrorEnabled())
                                logger.error("[rowToDb " + tName + "] " + se.getMessage());
                        }
                        failList.add(r.getHistSeq());
                    } finally {
                        if (rowPS != null)
                            try {
                                rowPS.close();
                            } catch (Exception ignore) {
                            }
                    }
                } else {
                    logger.info("HIST_SEQ=" + r.getHistSeq() + " of " + tName
                            + " is outdated or mother record not found![Update Remote]");
                    failList.add(r.getHistSeq());
                }
            } else if (HPFW_Connection.DUAL.equals(histUpdateType)) {
                if (HPFW_Connection.INSERT.equals(r.getActiion())
                        || (r.checkLastUpdateDt(primaryConnection, schema)
                                && r.checkLastUpdateDt(SecondaryConnection, schema))) {
                    String sql = "";
                    try {
                        sql = HPFW_Connection.INSERT.equals(r.getActiion()) ? r.insertSQLString
                                : HPFW_Connection.DELETE.equals(r.getActiion()) ? r.deleteSQLString : r.updateSQLString;
                        rowPS = r.getActionStatement(primaryConnection, schema);
                        rowPS2 = r.getActionStatement(SecondaryConnection, schema);
                        rowPS.execute();
                        rowPS2.execute();
                        primaryConnection.commit();
                        SecondaryConnection.commit();
                        suuccessCount++;
                    } catch (SQLException e) {
                        if (logger.isErrorEnabled())
                            logger.error("[rowToDb " + tName + "] Error SQL = " + sql);

                        if (logger.isErrorEnabled())
                            logger.error(e.getMessage());
                        try {
                            primaryConnection.rollback();
                        } catch (SQLException se) {
                            if (logger.isErrorEnabled())
                                logger.error("[rowToDb " + tName + "] " + se.getMessage());
                        }
                        try {
                            SecondaryConnection.rollback();
                        } catch (SQLException se) {
                            if (logger.isErrorEnabled())
                                logger.error("[rowToDb " + tName + "] " + se.getMessage());
                        }
                        failList.add(r.getHistSeq());
                    } finally {
                        if (rowPS != null)
                            try {
                                rowPS.close();
                            } catch (Exception ignore) {
                            }
                        if (rowPS2 != null)
                            try {
                                rowPS2.close();
                            } catch (Exception ignore) {
                            }
                    }
                } else {
                    logger.info("HIST_SEQ=" + r.getHistSeq() + " of " + tName
                            + " is outdated or mother record not found![Update Dual]");
                    failList.add(r.getHistSeq());
                }
            }
        }
        return suuccessCount;
    }

    private static void rsToRows(String tName, Connection primaryConnection, Table schema, Table liveData)
            throws SQLException {
        logger.debug("[HistSynJob rsToRows] calling rsToRows - " + tName);

        String selectSQL = selectSQLHash.get(tName) + " where HIST_STATUS = ? order by CREATE_DT, HIST_SEQ";
        PreparedStatement ps = primaryConnection.prepareStatement(selectSQL);
        ps.setString(1, CommonDBUtils.getSERVER_SEQUENCE_PREFIX());
        ResultSet rs = ps.executeQuery();

        int count = 0;
        while (rs.next()) {

            Row row = new Row();
            row.setHistUpdateType(rs.getString("HIST_UPDATE_TYPE"));
            row.setAction(rs.getString("HIST_ACTION"));
            row.setHistSeq(rs.getString("HIST_SEQ"));
            row.setLastUpdateDt(rs.getTimestamp("CREATE_DT"));
            row.setLastUpdateBy(rs.getString("CREATE_BY"));
            short datatype = -1;
            boolean isPK;

            for (int i = 0; i < schema.getColumnSize(); i++) {
                try {
                    datatype = schema.getColumnType(i);
                    isPK = schema.getIsPK(i);
                    switch (datatype) {
                        case Parameter.String:
                        case Parameter.Clob:
                            String data = rs.getString(i + 1);
                            row.addFieldData(new Parameter(datatype, data, isPK));
                            break;

                        case Parameter.Integer:
                            BigDecimal big = rs.getBigDecimal(i + 1);
                            Integer tempdata = big == null ? null : Integer.valueOf(big.intValue());
                            row.addFieldData(new Parameter(datatype, tempdata, isPK));
                            break;

                        case Parameter.Long:
                            BigDecimal big2 = rs.getBigDecimal(i + 1);
                            Long long_data = big2 == null ? null : Long.valueOf(big2.longValue());
                            row.addFieldData(new Parameter(datatype, long_data, isPK));
                            break;

                        case Parameter.BigDecimal:
                            BigDecimal big_data = rs.getBigDecimal(i + 1);
                            row.addFieldData(new Parameter(datatype, big_data, isPK));
                            break;

                        case Parameter.Date:
                            java.sql.Date date_data = rs.getDate(i + 1);
                            row.addFieldData(new Parameter(datatype, date_data, isPK));
                            break;

                        case Parameter.Timestamp:
                            java.sql.Timestamp stamp_data = rs.getTimestamp((i + 1));
                            row.addFieldData(new Parameter(datatype, stamp_data, isPK));
                            break;

                        case Parameter.Blob:
                            Blob blob_data = rs.getBlob((i + 1));
                            row.addFieldData(new Parameter(datatype, blob_data, isPK));
                            break;
                    }
                } catch (SQLException se) {
                    logger.error("[rsToRows] sql=" + selectSQL);
                    logger.error("[rsToRows] tName=" + tName + "|i+1=" + (i + 1) + "|datatype=" + datatype
                            + "|columenName=" + schema.getColumnName(i));
                    throw se;
                }
            }
            liveData.addRow(row);
            count++;
        }
        rs.close();
        ps.close();
        logger.debug("[HistSynJob rsToRows] " + count + " row of " + tName + " are waiting for syn");
    }

    private static void syn(String tName) {

        HPFW_Connection priCon = null;
        HPFW_Connection secCon = null;

        try {
            priCon = HPFW_Connection.getHPFW_Connection(HPFW_Connection.PRI);
            Connection primaryConnection = priCon.getConnectionPtr();
            primaryConnection.setAutoCommit(false);
            if (!HPFW_Connection.testConnection(primaryConnection))
                throw new ConnectionFailException("primaryConnection fail");

            secCon = HPFW_Connection.getHPFW_Connection(HPFW_Connection.SEC);
            Connection SecondaryConnection = secCon.getConnectionPtr();
            SecondaryConnection.setAutoCommit(false);
            if (!HPFW_Connection.testConnection(SecondaryConnection))
                throw new ConnectionFailException("SecondaryConnection fail");

            if (!lockCMC_MARS_SYN_JOB_LOCK(primaryConnection, tName)) {
                logger.debug("Another server is doing the syn job! Wait for next try!");
                return;
            }

            if (motherSonHash.containsKey(tName)) {

                ArrayList<String> tempSonList = motherSonHash.get(tName);
                logger.debug("[HistSynJob syn] son List size - " + tempSonList.size());
                for (int k = tempSonList.size() - 1; k >= 0; k--) {
                    String sontable = tempSonList.get(k);
                    lockRecords(primaryConnection, sontable);
                    logger.debug("[HistSynJob syn] Lock son table - " + sontable);
                }

            }

            lockRecords(primaryConnection, tName);
            logger.debug("[HistSynJob syn] Lock mother table - " + tName);
            primaryConnection.commit();

            ArrayList<Table> liveList = new ArrayList<Table>();

            Table liveData = new Table(tName);
            rsToRows(tName, primaryConnection, tableSchema.get(tName), liveData);
            liveList.add(liveData);

            if (motherSonHash.containsKey(tName)) {
                Iterator<String> itr = motherSonHash.get(tName).iterator();
                while (itr.hasNext()) {
                    String sontable = itr.next();
                    Table templiveData = new Table(sontable);
                    rsToRows(sontable, primaryConnection, tableSchema.get(sontable), templiveData);
                    liveList.add(templiveData);
                }
            }

            for (int i = 0; i < liveList.size(); i++) {
                String tepmTName = liveList.get(i).tableName;
                ArrayList<Row> rows = liveList.get(i).getRows();
                if (rows.size() > 0) {
                    ArrayList<String> failList = new ArrayList<String>();
                    int suuccessCount = rowToDb(tepmTName, primaryConnection, SecondaryConnection, rows,
                            tableSchema.get(tepmTName), failList);

                    releaseRecords(primaryConnection, tepmTName, failList);
                    primaryConnection.commit();

                    if (failList.size() > 0)
                        logger.warn("Syn " + tepmTName + " finish, " + suuccessCount + " success, " + failList.size()
                                + " fail");
                    else
                        logger.debug("Syn " + tepmTName + " finish, " + suuccessCount + " success, " + failList.size()
                                + " fail");

                    failList.clear();
                    failList = null;
                } else {
                    logger.debug("0 " + tepmTName + " rows are waiting for syn!");
                }
            }
            releaseCMC_MARS_SYN_JOB_LOCK(primaryConnection, tName);

            liveList.clear();
            liveList = null;
            liveData = null;
        } catch (SQLException se) {

            if (logger.isErrorEnabled())
                logger.error("[syn " + tName + "] " + se.getMessage());
        } finally {
            HPFW_Connection.close(priCon);
            HPFW_Connection.close(secCon);
        }
    }

    public static void syn(String tName, String mode)
            throws ConnectionFailException {
        if (HPFW_Connection.DIRECT_WITH_HISTORY.equals(mode)) {
            syn(tName);
        }
    }

    public static boolean isEnabled() {
        HPFW_Connection hpfwConn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        boolean enabled = true;
        try {
            hpfwConn = HPFW_Connection.getHPFW_Connection(HPFW_Connection.PRI);
            Connection conn = hpfwConn.getConnectionPtr();

            pstmt = conn.prepareStatement("select PARA_VALUE from CMC_SYSTEM_PARAM where PARA_NAME=? ");
            pstmt.setString(1, "HIST_SYNC_ENABLED");
            rs = pstmt.executeQuery();

            while (rs.next()) {
                String paraValue = rs.getString("PARA_VALUE");

                if ("N".equals(paraValue)) {
                    enabled = false;
                }
            }
        } catch (Exception se) {
            logger.error("General Exception found in [isEnabled] " + se.getMessage());
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (Exception e) {
                }
                pstmt = null;
            }

            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                }
                rs = null;
            }

            if (hpfwConn != null)
                HPFW_Connection.close(hpfwConn);
        }

        return enabled;
    }
}