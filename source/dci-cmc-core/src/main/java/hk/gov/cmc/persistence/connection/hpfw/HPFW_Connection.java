package hk.gov.cmc.persistence.connection.hpfw;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HPFW_Connection implements Serializable {
    private static final long serialVersionUID = 2756605212161446967L;

    static public final String DB_CONNECTION_DS_NAME_1_PROPERTY_NAME = "DB_CONNECTION_DS_NAME_1";
    static public final String DB_CONNECTION_DS_NAME_2_PROPERTY_NAME = "DB_CONNECTION_DS_NAME_2";
    static public final String DB_CONNECTION_SECONDARY_DS_NAME_1_PROPERTY_NAME = "DB_SIMPLE_CONNECTION_DS_NAME_1";
    static public final String SERVER_ID = "SERVER_ID";
    static public final String MYSQL_SELFGEN_SEQ_PREFIX = "MYSQL_SELFGEN_SEQ_PREFIX";

    static public final int batchCommitSize = 500;

    static public final int DB_ORACLE = 0;

    static public final int DB_MYSQL = 2;
    static public final String STR_DB_MYSQL = "MYSQL";

    static public final String MASTER = "MASTER";
    static public final String SLAVE = "SLAVE";

    static public final String PRI = "PRI";
    static public final String SEC = "SEC";

    static public final String DIRECT = "DIRECT";
    static public final String HISTORY_ONLY = "HISTORY_ONLY";
    static public final String DIRECT_WITH_HISTORY = "DIRECT_WITH_HISTORY";

    static public final String LOCAL = "L";
    static public final String DUAL = "D";
    static public final String REMOTE = "R";

    static public final String INSERT = "I";
    static public final String UPDATE = "U";
    static public final String DELETE = "D";

    static public final String PENDING = "P";

    static private Context initContext = null;

    static private String dbName = null;

    static private DataSource ds1 = null;
    static private DataSource ds2 = null;
    static private DataSource pri_ds = null;
    static private DataSource sec_ds = null;

    static private DataSource simple_ds1 = null;

    static private ConcurrentHashMap<Connection, HPFW_Connection> globalMapping = new ConcurrentHashMap<Connection, HPFW_Connection>();
    static private ConcurrentHashMap<Long, HPFW_Connection> threadConnectioMap = new ConcurrentHashMap<Long, HPFW_Connection>();

    private String lastUpdBy = null;
    private Timestamp lastUpTime = null;
    private Connection connectionPtr = null;
    private long threadID = -1;
    private String currentDS = PRI;
    private String updateMode = DIRECT;
    private boolean returnSameConUnderSameThread = true;
    private List<Sql> sqlList = null;
    private List<ArrayList<Parameter>> paramList = null;
    private boolean isSimpleConnection = false;
    private boolean isContainserManaged = false;
    private boolean isAutoCommit = true;

    static private HashMap<Integer, Integer> networkErrorHash = new HashMap<Integer, Integer>();
    private static String mysqlSelfGenSeqPrefix = null;

    private static Log logger = LogFactory.getLog(HPFW_Connection.class);

    static {
        networkErrorHash.put(17002, 17002);
        networkErrorHash.put(17410, 17410);
        networkErrorHash.put(03113, 03113);
        networkErrorHash.put(12560, 12560);
        networkErrorHash.put(01034, 01034);
        networkErrorHash.put(01033, 01033);
        networkErrorHash.put(12514, 12514);
    }

    private void switchDS() {
        currentDS = currentDS.equals(PRI) ? SEC : PRI;
        logger.debug("switch DS to " + currentDS);
        if (connectionPtr != null)
            close(connectionPtr);
        Connection c = getConnection(currentDS, this.isContainserManaged);
    }

    public void setIsContainserManaged(boolean isContainserManaged) {
        this.isContainserManaged = isContainserManaged;
    }

    public boolean isPrimaryDataSource() {
        return currentDS.equals(PRI);
    }

    public boolean isSameConUnderSameThread() {
        return returnSameConUnderSameThread;
    }

    public void setSameConUnderSameThread(boolean returnSameConUnderSameThread) {
        this.returnSameConUnderSameThread = returnSameConUnderSameThread;
    }

    public void setUpdateMode(String updateMode) {
        this.updateMode = updateMode;
    }

    public String getUpdateMode() {
        return updateMode;
    }

    public String getLastUpdBy() {
        return this.lastUpdBy;
    }

    public void setLastUpdBy(String lastUpdBy) {
        this.lastUpdBy = lastUpdBy;
    }

    public Timestamp getLastUpTime() {
        return this.lastUpTime;
    }

    public void setLastUpTime(Timestamp lastUpTime) {
        this.lastUpTime = lastUpTime;
    }

    public Connection getConnectionPtr() {
        return this.connectionPtr;
    }

    private void setConnectionPtr(Connection connectionPtr) {
        this.connectionPtr = connectionPtr;
    }

    public long getThreadID() {
        return this.threadID;
    }

    private void setThreadID(long threadID) {
        this.threadID = threadID;
    }

    public boolean isSimpleConnection() {
        return this.isSimpleConnection;
    }

    private void setIsSimpleConnection(boolean isSimpleConnection) {
        this.isSimpleConnection = isSimpleConnection;
    }

    public static String getMysqlSelfGenSeqPrefix() {
        return mysqlSelfGenSeqPrefix;
    }

    static public boolean isNetworkError(int code) {
        return networkErrorHash.containsKey(code);
    }

    public boolean isNetworkError(int code, Connection conn) {
        if (networkErrorHash.containsKey(code))
            return true;
        else {
            return !this.testConnection(conn);
        }
    }

    public void begin(final String userId, final Timestamp lastUptDt) throws Exception {
        begin(userId, lastUptDt, DIRECT);
    }

    public void begin(final String userId, final Timestamp lastUptDt, final String mode) throws Exception {
        this.updateMode = mode;
        setLastUpdBy(userId == null || "equals".equals(userId) ? "SYSTEM" : userId);
        setLastUpTime(lastUptDt == null ? CommonDBUtils.getSystemDate(this, dbName) : lastUptDt);
        if (sqlList == null)
            sqlList = new ArrayList<Sql>();
        if (paramList == null)
            paramList = new ArrayList<ArrayList<Parameter>>();
    }

    public void executeBatchStatement(String sqlString, ArrayList<Parameter> para, int windowSize) {
        executeBatchStatement(sqlString, para, windowSize, 0);
    }

    public void executeBatchStatement(String sqlString, ArrayList<Parameter> para, int windowSize, int batchSize) {
        if (sqlList == null)
            sqlList = new ArrayList<Sql>();
        Sql sql = new Sql(sqlString, false);
        sql.setIsBatchSQL(true);
        sql.setWindowSize(windowSize);
        sql.setBatchSize(batchSize);
        sqlList.add(sql);
        if (paramList == null)
            paramList = new ArrayList<ArrayList<Parameter>>();
        paramList.add(para == null ? new ArrayList<Parameter>() : para);
    }

    public void executeStatement(String sqlString, ArrayList<Parameter> para) {
        executeStatement(sqlString, para, false);
    }

    public void executeStatement(String sqlString, ArrayList<Parameter> para, boolean isHist, int timeoutSeconds) {
        if (sqlList == null)
            sqlList = new ArrayList<Sql>();
        sqlList.add(new Sql(sqlString, isHist, timeoutSeconds));
        if (paramList == null)
            paramList = new ArrayList<ArrayList<Parameter>>();
        paramList.add(para == null ? new ArrayList<Parameter>() : para);
    }

    public void executeStatement(String sqlString, ArrayList<Parameter> para, boolean isHist) {
        executeStatement(sqlString, para, isHist, -1);
    }

    private boolean executeBatch(Connection conn, Sql s, ArrayList<Parameter> paraList)
            throws SQLException {
        int realBatchSize = s.getBatchSize() > 0 ? s.getBatchSize() : batchCommitSize;
        PreparedStatement statement = null;
        int windowSize = s.getWindowSize();
        int executeSize = paraList.size() / windowSize;
        try {
            statement = conn.prepareStatement(s.sql);
            for (int i = 0; i < executeSize; i++) {
                if (i != 0 && (i + 1) % realBatchSize == 0) {
                    int[] updateCounts = statement.executeBatch();
                    for (int k = 0; k < updateCounts.length; k++) {
                        if (updateCounts[k] == Statement.EXECUTE_FAILED)
                            return false;
                    }
                }
                for (int j = 0; j < windowSize; j++) {
                    CommonDBUtils.setStatement(statement, j + 1, paraList.get(i * windowSize + j), false, s.sql);
                }
                statement.addBatch();
            }
            int[] updateCounts = statement.executeBatch();
            for (int k = 0; k < updateCounts.length; k++) {
                if (updateCounts[k] == Statement.EXECUTE_FAILED)
                    return false;
            }
            return true;
        } catch (BatchUpdateException ex) {

            if (logger.isErrorEnabled())
                logger.error(ex.getMessage());
            throw ex;
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (Exception ignore) {
                }
            }
        }
    }

    private void commit(Connection conn, List<Sql> theSqlList, List<ArrayList<Parameter>> theParamList)
            throws ConnectionFailException, Exception {
        isAutoCommit = true;
        try {
            Sql s = null;
            Iterator<Sql> itr = theSqlList.iterator();
            int i = 0;
            if (!isContainserManaged)
                conn.setAutoCommit(false);
            while (itr.hasNext()) {
                s = itr.next();
                PreparedStatement statement = null;
                try {
                    if (s.isBatchSQL) {
                        if (!executeBatch(conn, s, theParamList.get(i)))
                            throw new SQLException("executeBatch fail!");
                    } else {
                        statement = conn.prepareStatement(s.sql);
                        if (s.getTimeoutSeconds() >= 0)
                            statement.setQueryTimeout(s.getTimeoutSeconds());
                        CommonDBUtils.setStatement(statement, theParamList.get(i), s.isHist);
                        statement.executeUpdate();
                    }
                } catch (SQLException se) {
                    if (logger.isErrorEnabled())
                        logger.error("[Connection commit] error SQL : " + s == null ? "" : s.sql);
                    for (int j = 0; j < theParamList.get(i).size(); j++)
                        if (logger.isErrorEnabled())
                            logger.error("[Connection commit] " + theParamList.get(i).get(j).type + " - "
                                    + theParamList.get(i).get(j).value + ",");
                    try {
                        if (!isContainserManaged)
                            conn.rollback();
                    } catch (SQLException e) {

                        if (logger.isErrorEnabled())
                            logger.error(e.getMessage());
                    }
                    throw isNetworkError(se.getErrorCode(), conn) ? new ConnectionFailException() : se;
                } catch (Exception e) {
                    if (logger.isErrorEnabled())
                        logger.error("[Connection commit] error SQL : " + s == null ? "" : s.sql);
                    for (int j = 0; j < theParamList.get(i).size(); j++)
                        System.err.print("[Connection commit] " + theParamList.get(i).get(j).type + " - "
                                + theParamList.get(i).get(j).value + ",");
                    try {
                        if (!isContainserManaged)
                            conn.rollback();
                    } catch (SQLException se) {

                        if (logger.isErrorEnabled())
                            logger.error(se.getMessage());
                    }
                    throw e;
                } finally {
                    if (statement != null) {
                        statement.close();
                        statement = null;
                    }
                }
                i++;
            }
            if (!isContainserManaged)
                conn.commit();
        } catch (ConnectionFailException ex) {

            if (logger.isErrorEnabled())
                logger.error(ex.getMessage());
            try {
                if (!isContainserManaged)
                    conn.rollback();
            } catch (SQLException se) {

                if (logger.isErrorEnabled())
                    logger.error(se.getMessage());
            }
            throw ex;
        } catch (Exception ex) {

            if (logger.isErrorEnabled())
                logger.error(ex.getMessage());
            try {
                if (!isContainserManaged)
                    conn.rollback();
            } catch (SQLException se) {

                if (logger.isErrorEnabled())
                    logger.error(se.getMessage());
            }
            throw ex;
        } finally {
            clear();
        }
    }

    public void rollback() throws ConnectionFailException, SQLException {
        try {
            this.getConnectionPtr().rollback();
        } catch (SQLException se) {
            throw isNetworkError(se.getErrorCode(), this.getConnectionPtr()) ? new ConnectionFailException() : se;
        } finally {
            isAutoCommit = true;
        }
    }

    public void commit(String mode) throws ConnectionFailException, Exception {
        if ((mode.equals(PRI) && this.isPrimaryDataSource())
                || (mode.equals(SEC) && !this.isPrimaryDataSource())) {

        } else {
            currentDS = mode;
            if (connectionPtr != null)
                close(connectionPtr);
            Connection c = getConnection(mode, this.isContainserManaged);
        }
        commit(this.getConnectionPtr(), this.sqlList, this.paramList);
    }

    public String commit() throws ConnectionFailException, Exception {
        if (sqlList == null) {
            sqlList = new ArrayList<Sql>();
            lastUpTime = CommonDBUtils.getSystemDate(this, dbName);
        }
        if (paramList == null)
            paramList = new ArrayList<ArrayList<Parameter>>();
        try {
            commit(this.getConnectionPtr(), this.sqlList, this.paramList);
            return this.isPrimaryDataSource() ? PRI : SEC;
        } catch (ConnectionFailException ce) {
            HPFW_Connection drConnection = getHPFW_Connection(this.isPrimaryDataSource() ? SEC : PRI);
            commit(drConnection.getConnectionPtr(), this.sqlList, this.paramList);
            return this.isPrimaryDataSource() ? SEC : PRI;
        }
    }

    private ResultSet getResultSet(Connection conn, String sql, ArrayList<Parameter> paraL, int timeoutSeconds)
            throws SQLException {
        PreparedStatement statement = null;
        ResultSet r = null;
        try {
            statement = conn.prepareStatement(sql);
            if (timeoutSeconds >= 0)
                statement.setQueryTimeout(timeoutSeconds);
            CommonDBUtils.setStatement(statement, paraL);
            r = statement.executeQuery();
            return r;
        } catch (SQLException ex) {

            if (logger.isErrorEnabled()) {
                logger.error(ex.getMessage());
                logger.error("error SQL : " + sql == null ? "" : sql);
                logger.error("error arguements : ");
            }
            if (paraL != null) {
                for (int j = 0; j < paraL.size(); j++) {
                    System.err.print(paraL.get(j).value + ",");
                }
            }
            throw ex;
        } catch (Exception e) {
            throw new SQLException(e.getMessage());
        } finally {
            if (paraL != null) {
                paraL.clear();
                paraL = null;
            }
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

    public ResultSet getResultSet(String sql, ArrayList<Parameter> paraL, int timeoutSeconds)
            throws SQLException, ConnectionFailException {
        return getResultSet(sql, paraL, true, timeoutSeconds);
    }

    public ResultSet getResultSet(String sql, ArrayList<Parameter> paraL) throws SQLException, ConnectionFailException {
        return getResultSet(sql, paraL, true, -1);
    }

    public ResultSet getResultSet(String sql, ArrayList<Parameter> paraL, boolean needFailOver, int timeoutSeconds)
            throws SQLException, ConnectionFailException {
        ResultSet r = null;
        try {
            r = getResultSet(this.getConnectionPtr(), sql, paraL, timeoutSeconds);
            return r;
        } catch (SQLException ex1) {
            if (!needFailOver || this.isSimpleConnection()) {
                if (isNetworkError(ex1.getErrorCode(), this.getConnectionPtr())) {
                    throw new ConnectionFailException();
                } else
                    throw ex1;
            } else {
                if (isNetworkError(ex1.getErrorCode(), this.getConnectionPtr())) {
                    try {

                        switchDS();
                        r = getResultSet(this.getConnectionPtr(), sql, paraL, timeoutSeconds);
                        return r;
                    } catch (SQLException ex2) {
                        if (isNetworkError(ex2.getErrorCode(), this.getConnectionPtr())) {
                            throw new ConnectionFailException();
                        }
                        throw ex2;
                    }
                } else
                    throw ex1;
            }
        }
    }

    public ResultSet getResultSet(String sql, ArrayList<Parameter> paraL, boolean needFailOver)
            throws SQLException, ConnectionFailException {
        return getResultSet(sql, paraL, needFailOver, -1);
    }

    private static boolean openNew(Connection c, boolean isSimple, boolean isContainManaged) {
        try {
            if (c == null || c.isClosed())
                return false;

            c.setAutoCommit(false);

        } catch (Exception ex) {
            return false;
        }

        HPFW_Connection hc = new HPFW_Connection();
        hc.setIsSimpleConnection(isSimple);
        hc.setIsContainserManaged(isContainManaged);
        hc.setConnectionPtr(c);
        hc.setThreadID(Thread.currentThread().getId());

        hc.setAutoCommit(false);

        globalMapping.put(c, hc);
        return true;
    }

    private static boolean openNew(Connection c, boolean isConnectionManaged) {
        return openNew(c, false, isConnectionManaged);
    }

    public void close() {
        close(this);
    }

    public void clear() {
        clear(false);
    }

    public void clear(boolean setNull) {
        if (sqlList != null)
            sqlList.clear();
        if (paramList != null) {
            Iterator<ArrayList<Parameter>> itor = paramList.iterator();
            while (itor.hasNext()) {
                ArrayList<Parameter> paraList = itor.next();
                paraList.clear();
            }
            paramList.clear();
        }
        if (setNull) {
            sqlList = null;
            paramList = null;
        }
    }

    public void setAutoCommit(boolean auto) {
        try {
            if (!isContainserManaged)
                this.getConnectionPtr().setAutoCommit(auto);
            isAutoCommit = auto;
        } catch (SQLException ignore) {
        }
    }

    public static void close(HPFW_Connection c) {
        if (c == null)
            return;
        c.clear(true);
        close(c.getConnectionPtr(), !c.isContainserManaged && !c.isAutoCommit);
    }

    private static void close(Connection c, boolean needRollback) {
        if (c == null)
            return;
        if (globalMapping.containsKey(c)) {
            HPFW_Connection o = globalMapping.get(c);
            globalMapping.remove(c);

            if (o.isSameConUnderSameThread())
                threadConnectioMap.remove(o.getThreadID());
            logger.debug("thread id - " + o.getThreadID() + " close");
            o = null;
        }

        if (needRollback) {
            try {
                logger.debug("run autoRollBack()");
                c.rollback();
            } catch (Exception ignore) {
            }
            ;
        }

        try {
            c.close();
        } catch (Exception ignore) {
        }
        ;
        c = null;
    }

    public static void close(Connection c) {
        close(c, false);
    }

    public static void initPool(Properties properties) throws Exception {
        logger.info("initPool");
        String dsname1 = properties.getProperty(DB_CONNECTION_DS_NAME_1_PROPERTY_NAME);
        String dsname2 = properties.getProperty(DB_CONNECTION_DS_NAME_2_PROPERTY_NAME);
        String sec_dsname1 = properties.getProperty(DB_CONNECTION_SECONDARY_DS_NAME_1_PROPERTY_NAME);
        String server_id = properties.getProperty(SERVER_ID);
        logger.info("dsname1: " + dsname1 + ", dsname2: " + dsname2 + ", sec_dsname1: " + sec_dsname1 + ", server_id: "
                + server_id);

        dbName = properties.getProperty("DB") == null ? "" : properties.getProperty("DB").toUpperCase();
        logger.info("dbName: " + dbName);

        mysqlSelfGenSeqPrefix = properties.getProperty(MYSQL_SELFGEN_SEQ_PREFIX);
        logger.info("mysqlSelfGenSeqPrefix: " + mysqlSelfGenSeqPrefix);

        try {
            if (initContext == null) {
                initContext = new InitialContext();

                if (dsname1 != null && !"".equals(dsname1))
                    ds1 = (DataSource) initContext.lookup(dsname1);
                if (dsname2 != null && !"".equals(dsname2))
                    ds2 = (DataSource) initContext.lookup(dsname2);
                simple_ds1 = sec_dsname1 == null || "".equals(sec_dsname1) ? null
                        : (DataSource) initContext.lookup(sec_dsname1);
            }
            logger.info("ds1 is null: " + (ds1 == null) + ", ds2 is null: " + (ds2 == null) + ", simple_ds1 is null: " + (simple_ds1 == null));

            if (simple_ds1 != null) {
                logger.debug("Test simple pool - " + sec_dsname1);
                Connection sConn = simple_ds1.getConnection();
                HPFW_Connection.testConnection(sConn);
                sConn.close();
            } else {
                logger.debug("Simple pool is not set.");
            }

            if (ds1 != null) {
                String SERVER_SEQUENCE_PREFIX = properties.getProperty("SERVER_SEQUENCE_PREFIX");
                if (SERVER_SEQUENCE_PREFIX == null || "".equals(SERVER_SEQUENCE_PREFIX)) {
                    throw new Exception(
                            "SERVER_SEQUENCE_PREFIX not set, please check your properties file e.g. common_app.properties");
                } else if ("P".equals(SERVER_SEQUENCE_PREFIX) || "F".equals(SERVER_SEQUENCE_PREFIX)
                        || "S".equals(SERVER_SEQUENCE_PREFIX)) {
                    throw new Exception(
                            "SERVER_SEQUENCE_PREFIX cannot set as 'P','S','F', the recommended values are 'T' or 'W', please check your properties file e.g. common_app.properties");
                }
                CommonDBUtils.setSERVER_SEQUENCE_PREFIX(SERVER_SEQUENCE_PREFIX);
                CommonDBUtils.setSERVER_ID(server_id);
                CommonDBUtils.initzeroStringHash();

                logger.debug("Test master pool - " + dsname1);
                Connection conn = HPFW_Connection.getConnection(MASTER);
                if (conn != null && HPFW_Connection.testConnection(conn)) {
                    pri_ds = ds1;
                    sec_ds = ds2;
                    logger.debug("PRI ds is - " + dsname1);
                    logger.debug("SEC ds is - " + dsname2);
                } else {
                    pri_ds = ds2;
                    sec_ds = ds1;
                    logger.debug("PRI ds is - " + dsname2);
                    logger.debug("SEC ds is - " + dsname1);
                }
                HPFW_Connection.close(conn);

                logger.debug("Test slave pool - " + dsname2);
                Connection conn1 = HPFW_Connection.getConnection(SLAVE);
                if (conn1 != null)
                    HPFW_Connection.testConnection(conn1);
                HPFW_Connection.close(conn1);
            } else {
                logger.debug("Master pool is not set.");
            }

            if (ds2 != null) {
                String SERVER_SEQUENCE_PREFIX_2 = properties.getProperty("SERVER_SEQUENCE_PREFIX_2");
                if (SERVER_SEQUENCE_PREFIX_2 == null || "".equals(SERVER_SEQUENCE_PREFIX_2)) {
                    throw new Exception(
                            "SERVER_SEQUENCE_PREFIX_2 not set, please check your properties file e.g. common_app.properties");
                } else if ("P".equals(SERVER_SEQUENCE_PREFIX_2) || "F".equals(SERVER_SEQUENCE_PREFIX_2)
                        || "S".equals(SERVER_SEQUENCE_PREFIX_2)) {
                    throw new Exception(
                            "SERVER_SEQUENCE_PREFIX cannot set as 'P','S','F', the recommended values are 'T' or 'W', please check your properties file e.g. common_app.properties");
                }
                CommonDBUtils.setSERVER_SEQUENCE_PREFIX_2(SERVER_SEQUENCE_PREFIX_2);
            }
        } catch (Exception e) {

            if (logger.isErrorEnabled())
                logger.error(e.getMessage());
        }
    }

    public static Connection getConnection(String connURL, String user, String pwd, Driver driver)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
        DriverManager.registerDriver(driver);
        Properties props = new Properties();
        props.put("user", user);
        props.put("password", pwd);
        props.put("SetBigStringTryClob", "true");
        Connection conn = DriverManager.getConnection(connURL, props);
        openNew(conn, false);
        return conn;
    }

    private static Connection getConnection(DataSource ds, boolean isConnectionManaged) throws SQLException {

        Connection con = ds.getConnection();
        openNew(con, isConnectionManaged);
        return con;
    }

    private static Connection getConnection(final String mode) {
        return getConnection(mode, false);
    }

    private static Connection getConnection(final String mode, boolean isConnectionManaged) {
        logger.debug("getConnection(mode=" + mode + ", isConnectionManaged=" + isConnectionManaged + ")");
        try {
            return getConnection(
                    PRI.equals(mode) ? pri_ds : SEC.equals(mode) ? sec_ds : MASTER.equals(mode) ? ds1 : ds2,
                    isConnectionManaged);
        } catch (Exception e) {
            logger.error("getConnection fail, e: " + e);
            return null;
        }
    }

    public static HPFW_Connection getSimpleHPFW_Connection(boolean isConnectionManaged) {
        Connection conn = getSimpleConnection(isConnectionManaged);
        return globalMapping.get(conn);
    }

    public static HPFW_Connection getSimpleHPFW_Connection() {
        return getSimpleHPFW_Connection(false);
    }

    public static Connection getSimpleConnection(boolean isConnectionManaged) {
        try {
            long starttime = System.currentTimeMillis();
            long tid = Thread.currentThread().getId();
            Connection con = simple_ds1.getConnection();

            openNew(con, true, isConnectionManaged);
            long endtime = System.currentTimeMillis();
            logger.debug("thread id = " + tid + " | time(second): " + ((double) endtime - starttime) / 100);

            return con;
        } catch (Exception e) {
            return null;
        }
    }

    public static Connection getConnection() {
        return getConnection(false);
    }

    public static Connection getConnection(boolean sameConPerThread, boolean isContainManaged) {
        long starttime = System.currentTimeMillis();
        long tid = Thread.currentThread().getId();
        if (sameConPerThread) {
            if (threadConnectioMap.containsKey(tid)) {
                HPFW_Connection hc = threadConnectioMap.get(tid);
                return hc.getConnectionPtr();
            }
        }
        Connection conn = getConnection(PRI);
        conn = conn == null ? getConnection(SEC) : conn;
        HPFW_Connection hpc = globalMapping.get(conn);
        hpc.setIsContainserManaged(isContainManaged);
        hpc.setSameConUnderSameThread(sameConPerThread);
        if (sameConPerThread)
            threadConnectioMap.put(tid, hpc);
        long endtime = System.currentTimeMillis();
        logger.debug("thread id = " + tid + " | time(second): " + ((double) endtime - starttime) / 100);

        return conn;
    }

    public static Connection getConnection(boolean sameConPerThread) {
        return getConnection(sameConPerThread, false);
    }

    public static HPFW_Connection getHPFW_Connection(final String mode) {
        Connection conn = getConnection(PRI.equals(mode) ? PRI : SEC);
        HPFW_Connection hpc = globalMapping.get(conn);
        hpc.currentDS = mode;
        hpc.setSameConUnderSameThread(false);
        return hpc;
    }

    public static HPFW_Connection getHPFW_Connection(boolean sameConPerThread) {
        return getHPFW_Connection(sameConPerThread, false);
    }

    public static HPFW_Connection getHPFW_Connection(boolean sameConPerThread, boolean isContainerManaged) {
        logger.debug("getHPFW_Connection(sameConPerThread=" + sameConPerThread + ", isContainerManaged="
                + isContainerManaged + ")");
        Connection conn = getConnection(sameConPerThread, isContainerManaged);
        return globalMapping.get(conn);
    }

    public static HPFW_Connection getHPFW_Connection() {
        logger.debug("getHPFW_Connection");
        return getHPFW_Connection(false, false);
    }

    public static boolean testConnection(Connection con) {
        return testConnection(con, STR_DB_MYSQL.equals(dbName) ? DB_MYSQL : DB_ORACLE);
    }

    public static boolean testConnection(Connection con, int dbType) {
        PreparedStatement statement = null;
        ResultSet r = null;
        try {
            if (con == null || con.isClosed())
                return false;
            boolean testresult = false;
            if (dbType == DB_ORACLE || dbType == DB_MYSQL)
                statement = con.prepareStatement("select 'A' as output from dual");

            else
                return false;

            r = statement.executeQuery();
            if (r.next()) {
                testresult = "A".equals(r.getString("output"));
            }
            if (!testresult)
                logger.debug("test connection Fail !");
            return testresult;
        } catch (SQLException e) {

            if (logger.isErrorEnabled())
                logger.error(e.getMessage());
            return false;
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (Exception ignore) {
                }
                statement = null;
            }
            if (r != null) {
                try {
                    r.close();
                } catch (Exception ignore) {
                }
                statement = null;
            }
        }
    }

    public static String getDbName() {
        return dbName;
    }

    public static void main(String[] args) {
        try {

            long starttime = System.currentTimeMillis();

            logger.debug("Start time:" + starttime);

            HPFW_Connection conn = HPFW_Connection.getHPFW_Connection(false);
            conn.begin("STEVE", null);

            ArrayList<Parameter> paraList = new ArrayList<Parameter>();
            for (int i = 0; i < 4678; i++) {
                paraList.add(new Parameter(Parameter.BigDecimal, new BigDecimal(i)));
                paraList.add(new Parameter(Parameter.String, "testing" + i));
            }
            conn.executeBatchStatement("insert into test (ID,data1) values (?,?) ", paraList, 2, 1000);
            conn.commit();

            logger.debug("end time :" + (System.currentTimeMillis()));
            logger.debug("time used:" + (System.currentTimeMillis() - starttime));
        } catch (Exception e) {

            if (logger.isErrorEnabled())
                logger.error(e.getMessage());
        }
    }
}