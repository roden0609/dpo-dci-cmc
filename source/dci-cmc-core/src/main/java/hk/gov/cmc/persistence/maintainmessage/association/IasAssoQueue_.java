package hk.gov.cmc.persistence.maintainmessage.association;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.lang.Long;
import java.lang.String;
import java.lang.NullPointerException;
import java.util.ArrayList;

import hk.gov.cmc.persistence.connection.hpfw.CommonDBUtils;
import hk.gov.cmc.persistence.connection.hpfw.HPFW_Connection;
import hk.gov.cmc.persistence.connection.hpfw.Parameter;

public class IasAssoQueue_ implements Serializable {

    private static final long serialVersionUID = 1L;
    private boolean initialized = false;
    private final static String thisTableName = "IAS_ASSO_QUEUE";
    private final static String thisTableHistName = "_H";
    private boolean forUpdate = false;
    private String jobId = null;
    private String clientId = null;
    private String openId = null;
    private String serviceProviderId = null;
    private String assoStatus = null;
    private String jobStatus = null;
    private Timestamp createDt = null;
    private Timestamp lastModifyDt = null;
    private String createBy = null;
    private String lastModifyBy = null;

    private boolean dirty_jobId = false;
    private boolean dirty_clientId = false;
    private boolean dirty_openId = false;
    private boolean dirty_serviceProviderId = false;
    private boolean dirty_assoStatus = false;
    private boolean dirty_jobStatus = false;
    private boolean dirty_createDt = false;
    private boolean dirty_lastModifyDt = false;
    private boolean dirty_createBy = false;
    private boolean dirty_lastModifyBy = false;

    /**
     * IasAssoQueue_ Contructor
     */
    public IasAssoQueue_() {
        super();
    }

    /**
     * IasAssoQueue_ Constructor with specify PK
     */
    public IasAssoQueue_(HPFW_Connection countCon, String injobId) throws SQLException, NullPointerException {
        this();
        init(countCon, injobId, false);
    }

    public IasAssoQueue_(HPFW_Connection countCon, String injobId, boolean forUpdate)
            throws SQLException, NullPointerException {
        this();
        init(countCon, injobId, forUpdate);
    }

    static public void delete(HPFW_Connection countCon, String whereCluase, ArrayList<Parameter> paraL)
            throws SQLException {
        if (HPFW_Connection.DIRECT.equals(countCon.getUpdateMode())
                || HPFW_Connection.DIRECT_WITH_HISTORY.equals(countCon.getUpdateMode())) {
            String sql = "delete from ";
            sql += thisTableName + " ";
            sql += whereCluase;
            countCon.executeStatement(sql, paraL);
        }

        if (HPFW_Connection.HISTORY_ONLY.equals(countCon.getUpdateMode())
                || HPFW_Connection.DIRECT_WITH_HISTORY.equals(countCon.getUpdateMode())) {
            PreparedStatement stmt = null;
            ResultSet rs = null;

            try {
                String sql = "select job_id from " + thisTableName + " " + whereCluase;
                stmt = countCon.getConnectionPtr().prepareStatement(sql);
                CommonDBUtils.setStatement(stmt, paraL);
                rs = stmt.executeQuery();

                while (rs.next()) {
                    String histsql = "insert into ";
                    histsql += thisTableName + thisTableHistName;
                    histsql += " (HIST_SEQ,HIST_action,HIST_update_type,HIST_status,HIST_server_id,last_modify_by,last_modify_dt,create_by,create_dt,job_id";
                    histsql += ") values (?,?,?,?,?,?,?,?,?,?";
                    // perpare where ...
                    histsql += ",?";
                    histsql += ")";
                    ArrayList<Parameter> histParaList = new ArrayList<Parameter>();
                    histParaList.add(
                            new Parameter(Parameter.String,
                                    CommonDBUtils.getSequence(countCon, thisTableName + thisTableHistName)));
                    histParaList.add(new Parameter(Parameter.String, HPFW_Connection.DELETE));
                    histParaList.add(new Parameter(Parameter.String,
                            countCon.getUpdateMode().equals(HPFW_Connection.HISTORY_ONLY) ? HPFW_Connection.DUAL
                                    : HPFW_Connection.REMOTE));
                    histParaList.add(new Parameter(Parameter.String, HPFW_Connection.PENDING));
                    histParaList.add(new Parameter(Parameter.String, CommonDBUtils.getSERVER_ID()));
                    histParaList.add(new Parameter(Parameter.String, countCon.getLastUpdBy()));
                    histParaList.add(new Parameter(Parameter.Timestamp, countCon.getLastUpTime()));
                    histParaList.add(new Parameter(Parameter.String, countCon.getLastUpdBy()));
                    histParaList.add(new Parameter(Parameter.Timestamp, countCon.getLastUpTime()));
                    // perpare where ...
                    histParaList.add(new Parameter(Parameter.String, rs.getString(1)));
                    countCon.executeStatement(histsql, histParaList, true);
                }
            } catch (SQLException e) {
                throw e;
            } finally {
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (Exception ignore) {
                    }
                    rs = null;
                }
                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (Exception ignore) {
                    }
                    stmt = null;
                }
            }
        }
    }

    public void delete(HPFW_Connection countCon) throws SQLException {
        if (HPFW_Connection.DIRECT.equals(countCon.getUpdateMode())
                || HPFW_Connection.DIRECT_WITH_HISTORY.equals(countCon.getUpdateMode())) {
            String sql = "delete from ";
            sql += thisTableName;
            sql += " where 1=1 and job_id = ? ";

            ArrayList<Parameter> paraList = new ArrayList<Parameter>();

            // perpare where ...
            paraList.add(new Parameter(Parameter.String, this.jobId));
            countCon.executeStatement(sql, paraList);
        }

        if (HPFW_Connection.HISTORY_ONLY.equals(countCon.getUpdateMode())
                || HPFW_Connection.DIRECT_WITH_HISTORY.equals(countCon.getUpdateMode())) {
            String histsql = "insert into ";
            histsql += thisTableName + thisTableHistName;
            histsql += " (HIST_SEQ,HIST_action,HIST_update_type,HIST_status,HIST_server_id,last_modify_by,last_modify_dt,create_by,create_dt";
            histsql += ",JobId";
            histsql += ") values (?,?,?,?,?,?,?,?,?";
            // perpare where ...
            histsql += ",?";
            histsql += ")";
            ArrayList<Parameter> histParaList = new ArrayList<Parameter>();
            histParaList
                    .add(new Parameter(Parameter.String,
                            CommonDBUtils.getSequence(countCon, thisTableName + thisTableHistName)));
            histParaList.add(new Parameter(Parameter.String, HPFW_Connection.DELETE));
            histParaList.add(new Parameter(Parameter.String,
                    countCon.getUpdateMode().equals(HPFW_Connection.HISTORY_ONLY) ? HPFW_Connection.DUAL
                            : HPFW_Connection.REMOTE));
            histParaList.add(new Parameter(Parameter.String, HPFW_Connection.PENDING));
            histParaList.add(new Parameter(Parameter.String, CommonDBUtils.getSERVER_ID()));
            histParaList.add(new Parameter(Parameter.String, countCon.getLastUpdBy()));
            histParaList.add(new Parameter(Parameter.Timestamp, countCon.getLastUpTime()));
            histParaList.add(new Parameter(Parameter.String, countCon.getLastUpdBy()));
            histParaList.add(new Parameter(Parameter.Timestamp, countCon.getLastUpTime()));
            // perpare where ...
            histParaList.add(new Parameter(Parameter.String, this.jobId));
            countCon.executeStatement(histsql, histParaList, true);
        }
    }

    public void insert(HPFW_Connection countCon) throws SQLException {
        if (HPFW_Connection.DIRECT.equals(countCon.getUpdateMode())
                || HPFW_Connection.DIRECT_WITH_HISTORY.equals(countCon.getUpdateMode())) {
            ArrayList<Parameter> paraList = new ArrayList<Parameter>();

            String sql = "insert into ";
            sql += thisTableName;
            sql += " (";

            // perpare set ...
            if (jobId != null)
                sql += "job_id,";
            if (clientId != null)
                sql += "client_id,";
            if (openId != null)
                sql += "open_id,";
            if (serviceProviderId != null)
                sql += "service_provider_id,";
            if (assoStatus != null)
                sql += "asso_status,";
            if (jobStatus != null)
                sql += "job_status,";
            sql += "create_by,last_modify_by,create_dt,last_modify_dt) values (";

            // perpare set ...
            if (jobId != null)
                sql += "?,";
            if (clientId != null)
                sql += "?,";
            if (openId != null)
                sql += "?,";
            if (serviceProviderId != null)
                sql += "?,";
            if (assoStatus != null)
                sql += "?,";
            if (jobStatus != null)
                sql += "?,";
            sql += "?,?,?,?) ";

            // perpare set ...
            if (jobId != null)
                paraList.add(new Parameter(Parameter.String, this.jobId));
            if (clientId != null)
                paraList.add(new Parameter(Parameter.String, this.clientId));
            if (openId != null)
                paraList.add(new Parameter(Parameter.String, this.openId));
            if (serviceProviderId != null)
                paraList.add(new Parameter(Parameter.String, this.serviceProviderId));
            if (assoStatus != null)
                paraList.add(new Parameter(Parameter.String, this.assoStatus));
            if (jobStatus != null)
                paraList.add(new Parameter(Parameter.String, this.jobStatus));
            paraList.add(new Parameter(Parameter.String, countCon.getLastUpdBy()));
            paraList.add(new Parameter(Parameter.String, countCon.getLastUpdBy()));
            paraList.add(new Parameter(Parameter.Timestamp, countCon.getLastUpTime()));
            paraList.add(new Parameter(Parameter.Timestamp, countCon.getLastUpTime()));
            countCon.executeStatement(sql, paraList);
        }

        if (HPFW_Connection.HISTORY_ONLY.equals(countCon.getUpdateMode())
                || HPFW_Connection.DIRECT_WITH_HISTORY.equals(countCon.getUpdateMode())) {
            ArrayList<Parameter> histParaList = new ArrayList<Parameter>();

            String hist_sql = "insert into ";
            hist_sql += thisTableName + thisTableHistName;
            hist_sql += " (HIST_SEQ,HIST_action,HIST_update_type,HIST_status,HIST_server_id,";

            // perpare set ...
            if (jobId != null)
                hist_sql += "job_id,";
            if (clientId != null)
                hist_sql += "client_id,";
            if (openId != null)
                hist_sql += "open_id,";
            if (serviceProviderId != null)
                hist_sql += "service_provider_id,";
            if (assoStatus != null)
                hist_sql += "asso_status,";
            if (jobStatus != null)
                hist_sql += "job_status,";
            hist_sql += "create_by,last_modify_by,create_dt,last_modify_dt) values (?,?,?,?,?,";

            // perpare set ...
            if (jobId != null)
                hist_sql += "?,";
            if (clientId != null)
                hist_sql += "?,";
            if (openId != null)
                hist_sql += "?,";
            if (serviceProviderId != null)
                hist_sql += "?,";
            if (assoStatus != null)
                hist_sql += "?,";
            if (jobStatus != null)
                hist_sql += "?,";
            hist_sql += "?,?,?,?) ";
            histParaList
                    .add(new Parameter(Parameter.String,
                            CommonDBUtils.getSequence(countCon, thisTableName + thisTableHistName)));
            histParaList.add(new Parameter(Parameter.String, HPFW_Connection.INSERT));
            histParaList.add(new Parameter(Parameter.String,
                    countCon.getUpdateMode().equals(HPFW_Connection.HISTORY_ONLY) ? HPFW_Connection.DUAL
                            : HPFW_Connection.REMOTE));
            histParaList.add(new Parameter(Parameter.String, HPFW_Connection.PENDING));
            histParaList.add(new Parameter(Parameter.String, CommonDBUtils.getSERVER_ID()));

            // perpare set ...
            if (jobId != null)
                histParaList.add(new Parameter(Parameter.String, this.jobId));
            if (clientId != null)
                histParaList.add(new Parameter(Parameter.String, this.clientId));
            if (openId != null)
                histParaList.add(new Parameter(Parameter.String, this.openId));
            if (serviceProviderId != null)
                histParaList.add(new Parameter(Parameter.String, this.serviceProviderId));
            if (assoStatus != null)
                histParaList.add(new Parameter(Parameter.String, this.assoStatus));
            if (jobStatus != null)
                histParaList.add(new Parameter(Parameter.String, this.jobStatus));
            histParaList.add(new Parameter(Parameter.String, countCon.getLastUpdBy()));
            histParaList.add(new Parameter(Parameter.String, countCon.getLastUpdBy()));
            histParaList.add(new Parameter(Parameter.Timestamp, countCon.getLastUpTime()));
            histParaList.add(new Parameter(Parameter.Timestamp, countCon.getLastUpTime()));
            countCon.executeStatement(hist_sql, histParaList, false);
        }
    }

    public static void update(HPFW_Connection countCon, String setClause, ArrayList<Parameter> paraL,
            String whereClause,
            ArrayList<Parameter> whereParaL)
            throws SQLException {
        // e.g. whereClause = "set data1 = ?, data2 = ? " ; => "data1,data2"
        String selectString = setClause.toLowerCase();
        selectString = selectString.replaceAll("set", "");
        selectString = selectString.replaceAll("=", "");
        selectString = selectString.replaceAll(" ", "");
        selectString = selectString.replaceAll("\\?", "");
        String[] selectStringArray = selectString.split(",");

        if (HPFW_Connection.DIRECT.equals(countCon.getUpdateMode())
                || HPFW_Connection.DIRECT_WITH_HISTORY.equals(countCon.getUpdateMode())) {
            ArrayList<Parameter> tempList = new ArrayList<Parameter>();
            String sql = "update ";
            sql += thisTableName + " ";
            sql += setClause + " ,last_modify_by = ?,last_modify_dt = ? ";
            sql += whereClause + " ";
            tempList.addAll(paraL);
            tempList.add(new Parameter(Parameter.String, countCon.getLastUpdBy()));
            tempList.add(new Parameter(Parameter.Timestamp, countCon.getLastUpTime()));
            tempList.addAll(whereParaL);
            countCon.executeStatement(sql, tempList);
        }

        if (HPFW_Connection.HISTORY_ONLY.equals(countCon.getUpdateMode())
                || HPFW_Connection.DIRECT_WITH_HISTORY.equals(countCon.getUpdateMode())) {
            PreparedStatement stmt = null;
            ResultSet rs = null;
            try {
                String sql = "select job_id from " + thisTableName + " " + whereClause;
                stmt = countCon.getConnectionPtr().prepareStatement(sql);
                CommonDBUtils.setStatement(stmt, whereParaL);
                rs = stmt.executeQuery();

                while (rs.next()) {
                    String histsql = "insert into ";
                    histsql += thisTableName + thisTableHistName;
                    histsql += " (HIST_SEQ,HIST_action,HIST_update_type,HIST_status,HIST_server_id,last_modify_by,last_modify_dt,create_by,create_dt,job_id,"
                            + selectString;
                    histsql += ") values (?,?,?,?,?,?,?,?,?,?";
                    for (int i = 0; i < selectStringArray.length; i++)
                        histsql += ",?";
                    histsql += ")";

                    ArrayList<Parameter> histParaList = new ArrayList<Parameter>();
                    histParaList.add(
                            new Parameter(Parameter.String,
                                    CommonDBUtils.getSequence(countCon, thisTableName + thisTableHistName)));
                    histParaList.add(new Parameter(Parameter.String, HPFW_Connection.UPDATE));
                    histParaList.add(new Parameter(Parameter.String,
                            countCon.getUpdateMode().equals(HPFW_Connection.HISTORY_ONLY) ? HPFW_Connection.DUAL
                                    : HPFW_Connection.REMOTE));
                    histParaList.add(new Parameter(Parameter.String, HPFW_Connection.PENDING));
                    histParaList.add(new Parameter(Parameter.String, CommonDBUtils.getSERVER_ID()));
                    histParaList.add(new Parameter(Parameter.String, countCon.getLastUpdBy()));
                    histParaList.add(new Parameter(Parameter.Timestamp, countCon.getLastUpTime()));
                    histParaList.add(new Parameter(Parameter.String, countCon.getLastUpdBy()));
                    histParaList.add(new Parameter(Parameter.Timestamp, countCon.getLastUpTime()));
                    // perpare where ...
                    histParaList.add(new Parameter(Parameter.String, rs.getString(1)));
                    histParaList.addAll(paraL);
                    countCon.executeStatement(histsql, histParaList, true);

                }
            } catch (SQLException e) {
                throw e;
            } finally {
                if (paraL != null) {
                    paraL.clear();
                    paraL = null;
                }
                if (whereParaL != null) {
                    whereParaL.clear();
                    whereParaL = null;
                }

                if (rs != null) {
                    try {
                        rs.close();
                    } catch (Exception ignore) {
                    }
                    rs = null;
                }
                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (Exception ignore) {
                    }
                    stmt = null;
                }
            }
        }
    }

    public void update(HPFW_Connection countCon) throws SQLException {
        if (HPFW_Connection.DIRECT.equals(countCon.getUpdateMode())
                || HPFW_Connection.DIRECT_WITH_HISTORY.equals(countCon.getUpdateMode())) {
            ArrayList<Parameter> paraList = new ArrayList<Parameter>();

            String sql = "update ";
            sql += thisTableName;
            sql += " set ";
            // perpare set ...
            if (dirty_clientId)
                sql += "client_id = ?,";
            if (dirty_openId)
                sql += "open_id = ?,";
            if (dirty_serviceProviderId)
                sql += "service_provider_id = ?,";
            if (dirty_assoStatus)
                sql += "asso_status = ?,";
            if (dirty_jobStatus)
                sql += "job_status = ?,";

            sql += " last_modify_by = ?,last_modify_dt = ? where 1=1 and job_id = ? ";
            // perpare set ...
            if (dirty_clientId)
                paraList.add(new Parameter(Parameter.String, this.clientId));
            if (dirty_openId)
                paraList.add(new Parameter(Parameter.String, this.openId));
            if (dirty_serviceProviderId)
                paraList.add(new Parameter(Parameter.String, this.serviceProviderId));
            if (dirty_assoStatus)
                paraList.add(new Parameter(Parameter.String, this.assoStatus));
            if (dirty_jobStatus)
                paraList.add(new Parameter(Parameter.String, this.jobStatus));
            paraList.add(new Parameter(Parameter.String, countCon.getLastUpdBy()));
            paraList.add(new Parameter(Parameter.Timestamp, countCon.getLastUpTime()));
            // perpare where ...
            paraList.add(new Parameter(Parameter.String, this.jobId));
            countCon.executeStatement(sql, paraList);
        }

        if (HPFW_Connection.HISTORY_ONLY.equals(countCon.getUpdateMode())
                || HPFW_Connection.DIRECT_WITH_HISTORY.equals(countCon.getUpdateMode())) {
            ArrayList<Parameter> histParaList = new ArrayList<Parameter>();

            String hist_sql = "insert into ";
            hist_sql += thisTableName + thisTableHistName;
            hist_sql += " (HIST_SEQ,HIST_action,HIST_update_type,HIST_status,HIST_server_id,";

            // perpare set ...
            hist_sql += "job_id,";
            if (dirty_clientId)
                hist_sql += "client_id,";
            if (dirty_openId)
                hist_sql += "open_id,";
            if (dirty_serviceProviderId)
                hist_sql += "service_provider_id,";
            if (dirty_assoStatus)
                hist_sql += "asso_status,";
            if (dirty_jobStatus)
                hist_sql += "job_status,";
            hist_sql += "last_modify_by,last_modify_dt,create_by,create_dt) values (?,?,?,?,?,";

            // perpare set ...
            hist_sql += "?,";
            if (dirty_clientId)
                hist_sql += "?,";
            if (dirty_openId)
                hist_sql += "?,";
            if (dirty_serviceProviderId)
                hist_sql += "?,";
            if (dirty_assoStatus)
                hist_sql += "?,";
            if (dirty_jobStatus)
                hist_sql += "?,";
            hist_sql += "?,?,?,?) ";
            histParaList
                    .add(new Parameter(Parameter.String,
                            CommonDBUtils.getSequence(countCon, thisTableName + thisTableHistName)));
            histParaList.add(new Parameter(Parameter.String, HPFW_Connection.UPDATE));
            histParaList.add(new Parameter(Parameter.String,
                    countCon.getUpdateMode().equals(HPFW_Connection.HISTORY_ONLY) ? HPFW_Connection.DUAL
                            : HPFW_Connection.REMOTE));
            histParaList.add(new Parameter(Parameter.String, HPFW_Connection.PENDING));
            histParaList.add(new Parameter(Parameter.String, CommonDBUtils.getSERVER_ID()));

            // perpare set ...
            histParaList.add(new Parameter(Parameter.String, this.jobId));
            if (dirty_clientId)
                histParaList.add(new Parameter(Parameter.String, this.clientId));
            if (dirty_openId)
                histParaList.add(new Parameter(Parameter.String, this.openId));
            if (dirty_serviceProviderId)
                histParaList.add(new Parameter(Parameter.String, this.serviceProviderId));
            if (dirty_assoStatus)
                histParaList.add(new Parameter(Parameter.String, this.assoStatus));
            if (dirty_jobStatus)
                histParaList.add(new Parameter(Parameter.String, this.jobStatus));
            histParaList.add(new Parameter(Parameter.String, countCon.getLastUpdBy()));
            histParaList.add(new Parameter(Parameter.Timestamp, countCon.getLastUpTime()));
            histParaList.add(new Parameter(Parameter.String, countCon.getLastUpdBy()));
            histParaList.add(new Parameter(Parameter.Timestamp, countCon.getLastUpTime()));
            countCon.executeStatement(hist_sql, histParaList, true);
        }
        this.forUpdate = false;
    }

    public static ArrayList<IasAssoQueue_> getResultList(HPFW_Connection countCon, String whereCluase,
            ArrayList<Parameter> paraL) throws SQLException {
        boolean needClose = countCon == null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<IasAssoQueue_> result = new ArrayList<IasAssoQueue_>();
        String sql = "select " + thisTableName + ".* from ";
        try {
            sql += thisTableName + " ";
            sql += whereCluase;
            if (countCon == null)
                countCon = HPFW_Connection.getHPFW_Connection();
            stmt = countCon.getConnectionPtr().prepareStatement(sql);
            if (paraL != null)
                CommonDBUtils.setStatement(stmt, paraL);
            rs = stmt.executeQuery();

            while (rs.next()) {
                IasAssoQueue_ obj = new IasAssoQueue_();
                obj.jobId = rs.getString("job_id"); // String
                obj.clientId = rs.getString("client_id"); // String
                obj.openId = rs.getString("open_id"); // String
                obj.serviceProviderId = rs.getString("service_provider_id"); // String
                obj.assoStatus = rs.getString("asso_status"); // String
                obj.jobStatus = rs.getString("job_status"); // String
                obj.createDt = rs.getTimestamp("create_dt"); // Timestamp
                obj.lastModifyDt = rs.getTimestamp("last_modify_dt"); // Timestamp
                obj.createBy = rs.getString("create_by"); // String
                obj.lastModifyBy = rs.getString("last_modify_by"); // String
                obj.initialized = true;
                result.add(obj);
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception ignore) {
                }
                rs = null;
            }

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception ignore) {
                }
                stmt = null;
            }

            if (needClose && countCon != null) {
                try {
                    HPFW_Connection.close(countCon);
                    countCon = null;
                } catch (Exception ignore) {
                }
            }
        }
        return result;
    }

    public void setinitialized(boolean initialized) {
        this.initialized = initialized;
    }

    public void init(HPFW_Connection countCon, final String injobId, boolean forUpdate)
            throws SQLException, NullPointerException {
        this.forUpdate = forUpdate;
        boolean needClose = countCon == null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql = "select * from " + thisTableName + " where 1=1 and job_id = ? ";
            if (forUpdate)
                sql += "for update";
            if (countCon == null)
                countCon = HPFW_Connection.getHPFW_Connection();
            stmt = countCon.getConnectionPtr().prepareStatement(sql);
            stmt.setString(1, injobId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                jobId = rs.getString("job_id"); // String
                clientId = rs.getString("client_id"); // String
                openId = rs.getString("open_id"); // String
                serviceProviderId = rs.getString("service_provider_id"); // String
                assoStatus = rs.getString("asso_status"); // String
                jobStatus = rs.getString("job_status"); // String
                createDt = rs.getTimestamp("create_dt"); // Timestamp
                lastModifyDt = rs.getTimestamp("last_modify_dt"); // Timestamp
                createBy = rs.getString("create_by"); // String
                lastModifyBy = rs.getString("last_modify_by"); // String
                initialized = true;
            } else {
                throw new java.lang.NullPointerException();
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception ignore) {
                }
                rs = null;
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception ignore) {
                }
                stmt = null;
            }
            if (needClose && countCon != null) {
                try {
                    HPFW_Connection.close(countCon);
                    countCon = null;
                } catch (Exception ignore) {
                }
            }
        }
    }
    // }INIT

    /**
     * IasAssoQueue_ Destroyer
     */
    protected void finalize() throws Throwable {
        jobId = null;
        clientId = null;
        openId = null;
        serviceProviderId = null;
        assoStatus = null;
        jobStatus = null;
        createDt = null;
        lastModifyDt = null;
        createBy = null;
        lastModifyBy = null;
    }

    /**
     * Get job_id
     */
    public String getJobId() {
        return jobId == null ? "" : jobId;
    }

    /**
     * Set job_id
     */
    public void setJobId(final String inJobId) {
        jobId = inJobId;
        dirty_jobId = true;
    }

    /**
     * Get client_id
     */
    public String getClientId() {
        return clientId == null ? "" : clientId;
    }

    /**
     * Set client_id
     */
    public void setClientId(final String inClientId) {
        clientId = inClientId;
        dirty_clientId = true;
    }

    /**
     * Get open_id
     */
    public String getOpenId() {
        return openId == null ? "" : openId;
    }

    /**
     * Set open_id
     */
    public void setOpenId(final String inOpenId) {
        openId = inOpenId;
        dirty_openId = true;
    }

    /**
     * Get service_provider_id
     */
    public String getServiceProviderId() {
        return serviceProviderId == null ? "" : serviceProviderId;
    }

    /**
     * Set service_provider_id
     */
    public void setServiceProviderId(final String inServiceProviderId) {
        serviceProviderId = inServiceProviderId;
        dirty_serviceProviderId = true;
    }

    /**
     * Get asso_status
     */
    public String getAssoStatus() {
        return assoStatus == null ? "" : assoStatus;
    }

    /**
     * Set asso_status
     */
    public void setAssoStatus(final String inAssoStatus) {
        assoStatus = inAssoStatus;
        dirty_assoStatus = true;
    }

    /**
     * Get job_status
     */
    public String getJobStatus() {
        return jobStatus == null ? "" : jobStatus;
    }

    /**
     * Set job_status
     */
    public void setJobStatus(final String inJobStatus) {
        jobStatus = inJobStatus;
        dirty_jobStatus = true;
    }

    /**
     * Get create_dt
     */
    public Timestamp getCreateDt() {
        return createDt;
    }

    /**
     * Set create_dt
     */
    public void setCreateDt(final Timestamp inCreateDt) {
        createDt = inCreateDt;
        dirty_createDt = true;
    }

    /**
     * Get last_modify_dt
     */
    public Timestamp getLastModifyDt() {
        return lastModifyDt;
    }

    /**
     * Set last_modify_dt
     */
    public void setLastModifyDt(final Timestamp inLastModifyDt) {
        lastModifyDt = inLastModifyDt;
        dirty_lastModifyDt = true;
    }

    /**
     * Get create_by
     */
    public String getCreateBy() {
        return createBy == null ? "" : createBy;
    }

    /**
     * Set create_by
     */
    public void setCreateBy(final String inCreateBy) {
        createBy = inCreateBy;
        dirty_createBy = true;
    }

    /**
     * Get last_modify_by
     */
    public String getLastModifyBy() {
        return lastModifyBy == null ? "" : lastModifyBy;
    }

    /**
     * Set last_modify_by
     */
    public void setLastModifyBy(final String inLastModifyBy) {
        lastModifyBy = inLastModifyBy;
        dirty_lastModifyBy = true;
    }
}
