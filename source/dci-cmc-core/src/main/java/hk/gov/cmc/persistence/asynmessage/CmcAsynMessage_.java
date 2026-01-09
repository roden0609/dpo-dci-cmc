package hk.gov.cmc.persistence.asynmessage;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import hk.gov.cmc.persistence.connection.hpfw.CommonDBUtils;
import hk.gov.cmc.persistence.connection.hpfw.HPFW_Connection;
import hk.gov.cmc.persistence.connection.hpfw.Parameter;

public class CmcAsynMessage_ implements Serializable {
  private static final long serialVersionUID = 1L;
  private boolean initialized = false;

  private final static String thisTableName = "CMC_ASYN_MESSAGE";
  private final static String thisTableHistName = "_H";
  private boolean forUpdate = false;

  private String msgId = null;
  private String msgResponse = null;
  private String recipientAppId = null;
  private String recipientAppType = null;
  private String status = null;
  private Timestamp createDt = null;
  private Timestamp lastModifyDt = null;
  private String createBy = null;
  private String lastModifyBy = null;

  private boolean dirty_msgId = false;
  private boolean dirty_msgResponse = false;
  private boolean dirty_recipientAppId = false;
  private boolean dirty_recipientAppType = false;
  private boolean dirty_status = false;
  private boolean dirty_createDt = false;
  private boolean dirty_lastModifyDt = false;
  private boolean dirty_createBy = false;
  private boolean dirty_lastModifyBy = false;

  /**
   * CmcAsynMessage_ Contructor
   */
  public CmcAsynMessage_() {
    super();
  }

  /**
   * CmcAsynMessage_ Constructor with specify PK
   */
  public CmcAsynMessage_(HPFW_Connection countCon, String inmsgId) throws SQLException, NullPointerException {
    this();
    init(countCon, inmsgId, false);
  }

  public CmcAsynMessage_(HPFW_Connection countCon, String inmsgId, boolean forUpdate)
      throws SQLException, NullPointerException {
    this();
    init(countCon, inmsgId, forUpdate);
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
        String sql = "select msg_id from " + thisTableName + " " + whereCluase;
        stmt = countCon.getConnectionPtr().prepareStatement(sql);
        CommonDBUtils.setStatement(stmt, paraL);
        rs = stmt.executeQuery();

        while (rs.next()) {
          String histsql = "insert into ";
          histsql += thisTableName + thisTableHistName;
          histsql += " (HIST_SEQ,HIST_action,HIST_update_type,HIST_status,HIST_server_id,last_modify_by,last_modify_dt,create_by,create_dt,msg_id";
          histsql += ") values (?,?,?,?,?,?,?,?,?,?";
          // perpare where ...
          histsql += ",?";
          histsql += ")";
          ArrayList<Parameter> histParaList = new ArrayList<Parameter>();
          histParaList.add(
              new Parameter(Parameter.String, CommonDBUtils.getSequence(countCon, thisTableName + thisTableHistName)));
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
      sql += " where 1=1 and msg_id = ? ";

      ArrayList<Parameter> paraList = new ArrayList<Parameter>();

      // perpare where ...
      paraList.add(new Parameter(Parameter.String, this.msgId));
      countCon.executeStatement(sql, paraList);
    }

    if (HPFW_Connection.HISTORY_ONLY.equals(countCon.getUpdateMode())
        || HPFW_Connection.DIRECT_WITH_HISTORY.equals(countCon.getUpdateMode())) {
      String histsql = "insert into ";
      histsql += thisTableName + thisTableHistName;
      histsql += " (HIST_SEQ,HIST_action,HIST_update_type,HIST_status,HIST_server_id,last_modify_by,last_modify_dt,create_by,create_dt,msg_id) values (?,?,?,?,?,?,?,?,?,?)";
      ArrayList<Parameter> histParaList = new ArrayList<Parameter>();
      histParaList
          .add(new Parameter(Parameter.String, CommonDBUtils.getSequence(countCon, thisTableName + thisTableHistName)));
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
      histParaList.add(new Parameter(Parameter.String, this.msgId));
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
      if (msgId != null)
        sql += "msg_id,";
      if (msgResponse != null)
        sql += "msg_response,";
      if (recipientAppId != null)
        sql += "recipient_app_id,";
      if (recipientAppType != null)
        sql += "recipient_app_type,";
      if (status != null)
        sql += "status,";
      sql += "create_by,last_modify_by,create_dt,last_modify_dt) values (";

      // perpare set ...
      if (msgId != null)
        sql += "?,";
      if (msgResponse != null)
        sql += "?,";
      if (recipientAppId != null)
        sql += "?,";
      if (recipientAppType != null)
        sql += "?,";
      if (status != null)
        sql += "?,";
      sql += "?,?,?,?) ";

      // perpare set ...
      if (msgId != null)
        paraList.add(new Parameter(Parameter.String, this.msgId));
      if (msgResponse != null)
        paraList.add(new Parameter(Parameter.String, this.msgResponse));
      if (recipientAppId != null)
        paraList.add(new Parameter(Parameter.String, this.recipientAppId));
      if (recipientAppType != null)
        paraList.add(new Parameter(Parameter.String, this.recipientAppType));
      if (status != null)
        paraList.add(new Parameter(Parameter.String, this.status));
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
      if (msgId != null)
        hist_sql += "msg_id,";
      if (msgResponse != null)
        hist_sql += "msg_response,";
      if (recipientAppId != null)
        hist_sql += "recipient_app_id,";
      if (recipientAppType != null)
        hist_sql += "recipient_app_type,";
      if (status != null)
        hist_sql += "status,";
      hist_sql += "create_by,last_modify_by,create_dt,last_modify_dt) values (?,?,?,?,?,";

      // perpare set ...
      if (msgId != null)
        hist_sql += "?,";
      if (msgResponse != null)
        hist_sql += "?,";
      if (recipientAppId != null)
        hist_sql += "?,";
      if (recipientAppType != null)
        hist_sql += "?,";
      if (status != null)
        hist_sql += "?,";
      hist_sql += "?,?,?,?) ";
      histParaList
          .add(new Parameter(Parameter.String, CommonDBUtils.getSequence(countCon, thisTableName + thisTableHistName)));
      histParaList.add(new Parameter(Parameter.String, HPFW_Connection.INSERT));
      histParaList.add(new Parameter(Parameter.String,
          countCon.getUpdateMode().equals(HPFW_Connection.HISTORY_ONLY) ? HPFW_Connection.DUAL
              : HPFW_Connection.REMOTE));
      histParaList.add(new Parameter(Parameter.String, HPFW_Connection.PENDING));
      histParaList.add(new Parameter(Parameter.String, CommonDBUtils.getSERVER_ID()));

      // perpare set ...
      if (msgId != null)
        histParaList.add(new Parameter(Parameter.String, this.msgId));
      if (msgResponse != null)
        histParaList.add(new Parameter(Parameter.String, this.msgResponse));
      if (recipientAppId != null)
        histParaList.add(new Parameter(Parameter.String, this.recipientAppId));
      if (recipientAppType != null)
        histParaList.add(new Parameter(Parameter.String, this.recipientAppType));
      if (status != null)
        histParaList.add(new Parameter(Parameter.String, this.status));
      histParaList.add(new Parameter(Parameter.String, countCon.getLastUpdBy()));
      histParaList.add(new Parameter(Parameter.String, countCon.getLastUpdBy()));
      histParaList.add(new Parameter(Parameter.Timestamp, countCon.getLastUpTime()));
      histParaList.add(new Parameter(Parameter.Timestamp, countCon.getLastUpTime()));
      countCon.executeStatement(hist_sql, histParaList, false);
    }
  }

  public static void update(HPFW_Connection countCon, String setClause, ArrayList<Parameter> paraL, String whereClause,
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
        String sql = "select msg_id from " + thisTableName + " " + whereClause;
        stmt = countCon.getConnectionPtr().prepareStatement(sql);
        CommonDBUtils.setStatement(stmt, whereParaL);
        rs = stmt.executeQuery();

        while (rs.next()) {
          String histsql = "insert into ";
          histsql += thisTableName + thisTableHistName;
          histsql += " (HIST_SEQ,HIST_action,HIST_update_type,HIST_status,HIST_server_id,last_modify_by,last_modify_dt,create_by,create_dt,msg_id,"
              + selectString;
          histsql += ") values (?,?,?,?,?,?,?,?,?,?";
          for (int i = 0; i < selectStringArray.length; i++)
            histsql += ",?";
          histsql += ")";

          ArrayList<Parameter> histParaList = new ArrayList<Parameter>();
          histParaList.add(
              new Parameter(Parameter.String, CommonDBUtils.getSequence(countCon, thisTableName + thisTableHistName)));
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
      if (dirty_msgResponse)
        sql += "msg_response = ?,";
      if (dirty_recipientAppId)
        sql += "recipient_app_id = ?,";
      if (dirty_recipientAppType)
        sql += "recipient_app_type = ?,";
      if (dirty_status)
        sql += "status = ?,";

      sql += " last_modify_by = ?,last_modify_dt = ? where 1=1 and msg_id = ? ";
      // perpare set ...
      if (dirty_msgResponse)
        paraList.add(new Parameter(Parameter.String, this.msgResponse));
      if (dirty_recipientAppId)
        paraList.add(new Parameter(Parameter.String, this.recipientAppId));
      if (dirty_recipientAppType)
        paraList.add(new Parameter(Parameter.String, this.recipientAppType));
      if (dirty_status)
        paraList.add(new Parameter(Parameter.String, this.status));
      paraList.add(new Parameter(Parameter.String, countCon.getLastUpdBy()));
      paraList.add(new Parameter(Parameter.Timestamp, countCon.getLastUpTime()));
      // perpare where ...
      paraList.add(new Parameter(Parameter.String, this.msgId));
      countCon.executeStatement(sql, paraList);
    }

    if (HPFW_Connection.HISTORY_ONLY.equals(countCon.getUpdateMode())
        || HPFW_Connection.DIRECT_WITH_HISTORY.equals(countCon.getUpdateMode())) {
      ArrayList<Parameter> histParaList = new ArrayList<Parameter>();

      String hist_sql = "insert into ";
      hist_sql += thisTableName + thisTableHistName;
      hist_sql += " (HIST_SEQ,HIST_action,HIST_update_type,HIST_status,HIST_server_id,";

      // perpare set ...
      hist_sql += "msg_id,";
      if (dirty_msgResponse)
        hist_sql += "msg_response,";
      if (dirty_recipientAppId)
        hist_sql += "recipient_app_id,";
      if (dirty_recipientAppType)
        hist_sql += "recipient_app_type,";
      if (dirty_status)
        hist_sql += "status,";
      hist_sql += "last_modify_by,last_modify_dt,create_by,create_dt) values (?,?,?,?,?,";

      // perpare set ...
      hist_sql += "?,";
      if (dirty_msgResponse)
        hist_sql += "?,";
      if (dirty_recipientAppId)
        hist_sql += "?,";
      if (dirty_recipientAppType)
        hist_sql += "?,";
      if (dirty_status)
        hist_sql += "?,";
      hist_sql += "?,?,?,?) ";
      histParaList
          .add(new Parameter(Parameter.String, CommonDBUtils.getSequence(countCon, thisTableName + thisTableHistName)));
      histParaList.add(new Parameter(Parameter.String, HPFW_Connection.UPDATE));
      histParaList.add(new Parameter(Parameter.String,
          countCon.getUpdateMode().equals(HPFW_Connection.HISTORY_ONLY) ? HPFW_Connection.DUAL
              : HPFW_Connection.REMOTE));
      histParaList.add(new Parameter(Parameter.String, HPFW_Connection.PENDING));
      histParaList.add(new Parameter(Parameter.String, CommonDBUtils.getSERVER_ID()));

      // perpare set ...
      histParaList.add(new Parameter(Parameter.String, this.msgId));
      if (dirty_msgResponse)
        histParaList.add(new Parameter(Parameter.String, this.msgResponse));
      if (dirty_recipientAppId)
        histParaList.add(new Parameter(Parameter.String, this.recipientAppId));
      if (dirty_recipientAppType)
        histParaList.add(new Parameter(Parameter.String, this.recipientAppType));
      if (dirty_status)
        histParaList.add(new Parameter(Parameter.String, this.status));
      histParaList.add(new Parameter(Parameter.String, countCon.getLastUpdBy()));
      histParaList.add(new Parameter(Parameter.Timestamp, countCon.getLastUpTime()));
      histParaList.add(new Parameter(Parameter.String, countCon.getLastUpdBy()));
      histParaList.add(new Parameter(Parameter.Timestamp, countCon.getLastUpTime()));
      countCon.executeStatement(hist_sql, histParaList, true);
    }
    this.forUpdate = false;
  }

  public static ArrayList<CmcAsynMessage_> getResultList(HPFW_Connection countCon, String whereCluase,
      ArrayList<Parameter> paraL) throws SQLException {
    boolean needClose = countCon == null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    ArrayList<CmcAsynMessage_> result = new ArrayList<CmcAsynMessage_>();
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
        CmcAsynMessage_ obj = new CmcAsynMessage_();
        obj.msgId = rs.getString("msg_id"); // String
        obj.msgResponse = rs.getString("msg_response"); // String
        obj.recipientAppId = rs.getString("recipient_app_id"); // String
        obj.recipientAppType = rs.getString("recipient_app_type"); // String
        obj.status = rs.getString("status"); // String
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

  public void init(HPFW_Connection countCon, final String inmsgId, boolean forUpdate)
      throws SQLException, NullPointerException {
    this.forUpdate = forUpdate;
    boolean needClose = countCon == null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      String sql = "select * from " + thisTableName + " where 1=1 and msg_id = ? ";
      if (forUpdate)
        sql += "for update";
      if (countCon == null)
        countCon = HPFW_Connection.getHPFW_Connection();
      stmt = countCon.getConnectionPtr().prepareStatement(sql);
      stmt.setString(1, inmsgId);
      rs = stmt.executeQuery();

      if (rs.next()) {
        msgId = rs.getString("msg_id"); // String
        msgResponse = rs.getString("msg_response"); // String
        recipientAppId = rs.getString("recipient_app_id"); // String
        recipientAppType = rs.getString("recipient_app_type"); // String
        status = rs.getString("status"); // String
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
   * CmcAsynMessage_ Destroyer
   */
  protected void finalize() throws Throwable {
    msgId = null;
    msgResponse = null;
    recipientAppId = null;
    recipientAppType = null;
    status = null;
    createDt = null;
    lastModifyDt = null;
    createBy = null;
    lastModifyBy = null;
  }

  /**
   * Get msg_id
   */
  public String getMsgId() {
    return msgId == null ? "" : msgId;
  }

  /**
   * Set msg_id
   */
  public void setMsgId(final String inMsgId) {
    msgId = inMsgId;
    dirty_msgId = true;
  }

  /**
   * Get msg_response
   */
  public String getMsgResponse() {
    return msgResponse == null ? "" : msgResponse;
  }

  /**
   * Set msg_response
   */
  public void setMsgResponse(final String inMsgResponse) {
    msgResponse = inMsgResponse;
    dirty_msgResponse = true;
  }

  /**
   * Get recipient_app_id
   */
  public String getRecipientAppId() {
    return recipientAppId == null ? "" : recipientAppId;
  }

  /**
   * Set recipient_app_id
   */
  public void setRecipientAppId(final String inRecipientAppId) {
    recipientAppId = inRecipientAppId;
    dirty_recipientAppId = true;
  }

  /**
   * Get recipient_app_type
   */
  public String getRecipientAppType() {
    return recipientAppType == null ? "" : recipientAppType;
  }

  /**
   * Set recipient_app_type
   */
  public void setRecipientAppType(final String inRecipientAppType) {
    recipientAppType = inRecipientAppType;
    dirty_recipientAppType = true;
  }

  /**
   * Get status
   */
  public String getStatus() {
    return status == null ? "" : status;
  }

  /**
   * Set status
   */
  public void setStatus(final String inStatus) {
    status = inStatus;
    dirty_status = true;
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
