package hk.gov.cmc.persistence.ias.notification;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import hk.gov.cmc.persistence.connection.hpfw.CommonDBUtils;
import hk.gov.cmc.persistence.connection.hpfw.HPFW_Connection;
import hk.gov.cmc.persistence.connection.hpfw.Parameter;

public class IasEsNotiMap_ implements Serializable {
  private static final long serialVersionUID = 1L;
  protected boolean initialized = false;
  private final static String thisTableName = "IAS_ES_NOTI_MAP";
  private final static String thisTableHistName = "_H";
  private boolean forUpdate = false;
  private String notiId = null;
  private String serviceProviderId = null;
  private String openId = null;
  private String hkidHashed = null;
  private String hkidEncrypted = null;
  private String optIn = null;
  private Timestamp createDt = null;
  private Timestamp lastModifyDt = null;
  private String createBy = null;
  private String lastModifyBy = null;
  private boolean dirty_notiId = false;
  private boolean dirty_serviceProviderId = false;
  private boolean dirty_openId = false;
  private boolean dirty_hkidHashed = false;
  private boolean dirty_hkidEncrypted = false;
  private boolean dirty_optIn = false;
  private boolean dirty_createDt = false;
  private boolean dirty_lastModifyDt = false;
  private boolean dirty_createBy = false;
  private boolean dirty_lastModifyBy = false;

  public IasEsNotiMap_() {
    super();
  }

  public IasEsNotiMap_(HPFW_Connection countCon, String innotiId, String inserviceProviderId)
      throws SQLException, NullPointerException {
    this();
    init(countCon, innotiId, inserviceProviderId, false);
  }

  public IasEsNotiMap_(HPFW_Connection countCon, String innotiId, String inserviceProviderId, boolean forUpdate)
      throws SQLException, NullPointerException {
    this();
    init(countCon, innotiId, inserviceProviderId, forUpdate);
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
        String sql = "select noti_id,service_provider_id from " + thisTableName + " " + whereCluase;
        stmt = countCon.getConnectionPtr().prepareStatement(sql);
        CommonDBUtils.setStatement(stmt, paraL);
        rs = stmt.executeQuery();

        while (rs.next()) {
          String histsql = "insert into ";
          histsql += thisTableName + thisTableHistName;
          histsql += " (HIST_SEQ,HIST_action,HIST_update_type,HIST_status,HIST_server_id,last_modify_by,last_modify_dt,create_by,create_dt,noti_id,service_provider_id";
          histsql += ") values (?,?,?,?,?,?,?,?,?,?";
          // perpare where ...
          histsql += ",?";
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
          histParaList.add(new Parameter(Parameter.String, rs.getString(2)));
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
      sql += " where 1=1 and noti_id = ?  and service_provider_id = ? ";

      ArrayList<Parameter> paraList = new ArrayList<Parameter>();

      // perpare where ...
      paraList.add(new Parameter(Parameter.String, this.notiId));
      paraList.add(new Parameter(Parameter.String, this.serviceProviderId));
      countCon.executeStatement(sql, paraList);
    }

    if (HPFW_Connection.HISTORY_ONLY.equals(countCon.getUpdateMode())
        || HPFW_Connection.DIRECT_WITH_HISTORY.equals(countCon.getUpdateMode())) {
      String histsql = "insert into ";
      histsql += thisTableName + thisTableHistName;
      histsql += " (HIST_SEQ,HIST_action,HIST_update_type,HIST_status,HIST_server_id,last_modify_by,last_modify_dt,create_by,create_dt";
      histsql += ",NotiId";
      histsql += ",ServiceProviderId";
      histsql += ") values (?,?,?,?,?,?,?,?,?";
      // perpare where ...
      histsql += ",?";
      histsql += ",?";
      histsql += ")";
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
      histParaList.add(new Parameter(Parameter.String, this.notiId));
      histParaList.add(new Parameter(Parameter.String, this.serviceProviderId));
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
      if (notiId != null)
        sql += "noti_id,";
      if (serviceProviderId != null)
        sql += "service_provider_id,";
      if (openId != null)
        sql += "open_id,";
      if (hkidHashed != null)
        sql += "hkid_hashed,";
      if (hkidEncrypted != null)
        sql += "hkid_encrypted,";
      if (optIn != null)
        sql += "opt_in,";
      sql += "create_by,last_modify_by,create_dt,last_modify_dt) values (";

      // perpare set ...
      if (notiId != null)
        sql += "?,";
      if (serviceProviderId != null)
        sql += "?,";
      if (openId != null)
        sql += "?,";
      if (hkidHashed != null)
        sql += "?,";
      if (hkidEncrypted != null)
        sql += "?,";
      if (optIn != null)
        sql += "?,";
      sql += "?,?,?,?) ";

      // perpare set ...
      if (notiId != null)
        paraList.add(new Parameter(Parameter.String, this.notiId));
      if (serviceProviderId != null)
        paraList.add(new Parameter(Parameter.String, this.serviceProviderId));
      if (openId != null)
        paraList.add(new Parameter(Parameter.String, this.openId));
      if (hkidHashed != null)
        paraList.add(new Parameter(Parameter.String, this.hkidHashed));
      if (hkidEncrypted != null)
        paraList.add(new Parameter(Parameter.String, this.hkidEncrypted));
      if (optIn != null)
        paraList.add(new Parameter(Parameter.String, this.optIn));
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
      if (notiId != null)
        hist_sql += "noti_id,";
      if (serviceProviderId != null)
        hist_sql += "service_provider_id,";
      if (openId != null)
        hist_sql += "open_id,";
      if (hkidHashed != null)
        hist_sql += "hkid_hashed,";
      if (hkidEncrypted != null)
        hist_sql += "hkid_encrypted,";
      if (optIn != null)
        hist_sql += "opt_in,";
      hist_sql += "create_by,last_modify_by,create_dt,last_modify_dt) values (?,?,?,?,?,";

      // perpare set ...
      if (notiId != null)
        hist_sql += "?,";
      if (serviceProviderId != null)
        hist_sql += "?,";
      if (openId != null)
        hist_sql += "?,";
      if (hkidHashed != null)
        hist_sql += "?,";
      if (hkidEncrypted != null)
        hist_sql += "?,";
      if (optIn != null)
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
      if (notiId != null)
        histParaList.add(new Parameter(Parameter.String, this.notiId));
      if (serviceProviderId != null)
        histParaList.add(new Parameter(Parameter.String, this.serviceProviderId));
      if (openId != null)
        histParaList.add(new Parameter(Parameter.String, this.openId));
      if (hkidHashed != null)
        histParaList.add(new Parameter(Parameter.String, this.hkidHashed));
      if (hkidEncrypted != null)
        histParaList.add(new Parameter(Parameter.String, this.hkidEncrypted));
      if (optIn != null)
        histParaList.add(new Parameter(Parameter.String, this.optIn));
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
        String sql = "select noti_id,service_provider_id from " + thisTableName + " " + whereClause;
        stmt = countCon.getConnectionPtr().prepareStatement(sql);
        CommonDBUtils.setStatement(stmt, whereParaL);
        rs = stmt.executeQuery();

        while (rs.next()) {
          String histsql = "insert into ";
          histsql += thisTableName + thisTableHistName;
          histsql += " (HIST_SEQ,HIST_action,HIST_update_type,HIST_status,HIST_server_id,last_modify_by,last_modify_dt,create_by,create_dt,noti_id,service_provider_id,"
              + selectString;
          histsql += ") values (?,?,?,?,?,?,?,?,?,?,?";
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
          histParaList.add(new Parameter(Parameter.String, rs.getString(2)));
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
      if (dirty_openId)
        sql += "open_id = ?,";
      if (dirty_hkidHashed)
        sql += "hkid_hashed = ?,";
      if (dirty_hkidEncrypted)
        sql += "hkid_encrypted = ?,";
      if (dirty_optIn)
        sql += "opt_in = ?,";

      sql += " last_modify_by = ?,last_modify_dt = ? where 1=1 and noti_id = ?  and service_provider_id = ? ";
      // perpare set ...
      if (dirty_openId)
        paraList.add(new Parameter(Parameter.String, this.openId));
      if (dirty_hkidHashed)
        paraList.add(new Parameter(Parameter.String, this.hkidHashed));
      if (dirty_hkidEncrypted)
        paraList.add(new Parameter(Parameter.String, this.hkidEncrypted));
      if (dirty_optIn)
        paraList.add(new Parameter(Parameter.String, this.optIn));
      paraList.add(new Parameter(Parameter.String, countCon.getLastUpdBy()));
      paraList.add(new Parameter(Parameter.Timestamp, countCon.getLastUpTime()));
      // perpare where ...
      paraList.add(new Parameter(Parameter.String, this.notiId));
      paraList.add(new Parameter(Parameter.String, this.serviceProviderId));
      countCon.executeStatement(sql, paraList);
    }

    if (HPFW_Connection.HISTORY_ONLY.equals(countCon.getUpdateMode())
        || HPFW_Connection.DIRECT_WITH_HISTORY.equals(countCon.getUpdateMode())) {
      ArrayList<Parameter> histParaList = new ArrayList<Parameter>();

      String hist_sql = "insert into ";
      hist_sql += thisTableName + thisTableHistName;
      hist_sql += " (HIST_SEQ,HIST_action,HIST_update_type,HIST_status,HIST_server_id,";

      // perpare set ...
      hist_sql += "noti_id,";
      hist_sql += "service_provider_id,";
      if (dirty_openId)
        hist_sql += "open_id,";
      if (dirty_hkidHashed)
        hist_sql += "hkid_hashed,";
      if (dirty_hkidEncrypted)
        hist_sql += "hkid_encrypted,";
      if (dirty_optIn)
        hist_sql += "opt_in,";
      hist_sql += "last_modify_by,last_modify_dt,create_by,create_dt) values (?,?,?,?,?,";

      // perpare set ...
      hist_sql += "?,";
      hist_sql += "?,";
      if (dirty_openId)
        hist_sql += "?,";
      if (dirty_hkidHashed)
        hist_sql += "?,";
      if (dirty_hkidEncrypted)
        hist_sql += "?,";
      if (dirty_optIn)
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
      histParaList.add(new Parameter(Parameter.String, this.notiId));
      histParaList.add(new Parameter(Parameter.String, this.serviceProviderId));
      if (dirty_openId)
        histParaList.add(new Parameter(Parameter.String, this.openId));
      if (dirty_hkidHashed)
        histParaList.add(new Parameter(Parameter.String, this.hkidHashed));
      if (dirty_hkidEncrypted)
        histParaList.add(new Parameter(Parameter.String, this.hkidEncrypted));
      if (dirty_optIn)
        histParaList.add(new Parameter(Parameter.String, this.optIn));
      histParaList.add(new Parameter(Parameter.String, countCon.getLastUpdBy()));
      histParaList.add(new Parameter(Parameter.Timestamp, countCon.getLastUpTime()));
      histParaList.add(new Parameter(Parameter.String, countCon.getLastUpdBy()));
      histParaList.add(new Parameter(Parameter.Timestamp, countCon.getLastUpTime()));
      countCon.executeStatement(hist_sql, histParaList, true);
    }
    this.forUpdate = false;
  }

  public static ArrayList<IasEsNotiMap_> getResultList(HPFW_Connection countCon, String whereCluase,
      ArrayList<Parameter> paraL) throws SQLException {
    boolean needClose = countCon == null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    ArrayList<IasEsNotiMap_> result = new ArrayList<IasEsNotiMap_>();
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
        IasEsNotiMap_ obj = new IasEsNotiMap_();
        obj.notiId = rs.getString("noti_id"); // String
        obj.serviceProviderId = rs.getString("service_provider_id"); // String
        obj.openId = rs.getString("open_id"); // String
        obj.hkidHashed = rs.getString("hkid_hashed"); // String
        obj.hkidEncrypted = rs.getString("hkid_encrypted"); // String
        obj.optIn = rs.getString("opt_in"); // String
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

  public void init(HPFW_Connection countCon, final String innotiId, final String inserviceProviderId, boolean forUpdate)
      throws SQLException, NullPointerException {
    this.forUpdate = forUpdate;
    boolean needClose = countCon == null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      String sql = "select * from " + thisTableName + " where 1=1 and noti_id = ?  and service_provider_id = ? ";
      if (forUpdate)
        sql += "for update";
      if (countCon == null)
        countCon = HPFW_Connection.getHPFW_Connection();
      stmt = countCon.getConnectionPtr().prepareStatement(sql);
      stmt.setString(1, innotiId);
      stmt.setString(2, inserviceProviderId);
      rs = stmt.executeQuery();

      if (rs.next()) {
        notiId = rs.getString("noti_id"); // String
        serviceProviderId = rs.getString("service_provider_id"); // String
        openId = rs.getString("open_id"); // String
        hkidHashed = rs.getString("hkid_hashed"); // String
        hkidEncrypted = rs.getString("hkid_encrypted"); // String
        optIn = rs.getString("opt_in"); // String
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

  protected void finalize() throws Throwable {
    notiId = null;
    serviceProviderId = null;
    openId = null;
    hkidHashed = null;
    hkidEncrypted = null;
    optIn = null;
    createDt = null;
    lastModifyDt = null;
    createBy = null;
    lastModifyBy = null;
  }

  public String getNotiId() {
    return notiId == null ? "" : notiId;
  }

  public void setNotiId(final String inNotiId) {
    notiId = inNotiId;
    dirty_notiId = true;
  }

  public String getServiceProviderId() {
    return serviceProviderId == null ? "" : serviceProviderId;
  }

  public void setServiceProviderId(final String inServiceProviderId) {
    serviceProviderId = inServiceProviderId;
    dirty_serviceProviderId = true;
  }

  public String getOpenId() {
    return openId == null ? "" : openId;
  }

  public void setOpenId(final String inOpenId) {
    openId = inOpenId;
    dirty_openId = true;
  }

  public String getHkidHashed() {
    return hkidHashed == null ? "" : hkidHashed;
  }

  public void setHkidHashed(final String inHkidHashed) {
    hkidHashed = inHkidHashed;
    dirty_hkidHashed = true;
  }

  public String getHkidEncrypted() {
    return hkidEncrypted == null ? "" : hkidEncrypted;
  }

  public void setHkidEncrypted(final String inHkidEncrypted) {
    hkidEncrypted = inHkidEncrypted;
    dirty_hkidEncrypted = true;
  }

  public String getOptIn() {
    return optIn == null ? "" : optIn;
  }

  public void setOptIn(final String inOptIn) {
    optIn = inOptIn;
    dirty_optIn = true;
  }

  public Timestamp getCreateDt() {
    return createDt;
  }

  public void setCreateDt(final Timestamp inCreateDt) {
    createDt = inCreateDt;
    dirty_createDt = true;
  }

  public Timestamp getLastModifyDt() {
    return lastModifyDt;
  }

  public void setLastModifyDt(final Timestamp inLastModifyDt) {
    lastModifyDt = inLastModifyDt;
    dirty_lastModifyDt = true;
  }

  public String getCreateBy() {
    return createBy == null ? "" : createBy;
  }

  public void setCreateBy(final String inCreateBy) {
    createBy = inCreateBy;
    dirty_createBy = true;
  }

  public String getLastModifyBy() {
    return lastModifyBy == null ? "" : lastModifyBy;
  }

  public void setLastModifyBy(final String inLastModifyBy) {
    lastModifyBy = inLastModifyBy;
    dirty_lastModifyBy = true;
  }
}
