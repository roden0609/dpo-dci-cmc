
package hk.gov.cmc.persistence.connection.hpfw;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

public class Row {

    private String histSeq = "";
    private String tableName = "";
    private Timestamp lastUpdateDt = null;
    private String lastUpdateBy = "";
    private String action = HPFW_Connection.UPDATE;
    private ArrayList<Parameter> fields = new ArrayList<Parameter>();
    private String histUpdateType = "";

    public String insertSQLString;
    public String updateSQLString;
    public String deleteSQLString;
    public String getLastModifyDtString;
    private ArrayList<Parameter> dataList = null;
    private ArrayList<Parameter> keydataList = null;
    private boolean genSQLDone = false;

    public Row() {
        dataList = new ArrayList<Parameter>();
        keydataList = new ArrayList<Parameter>();
    }

    protected void finalize() throws Throwable {
        fields.clear();
        fields = null;
        keydataList.clear();
        keydataList = null;
    }

    public String getHistUpdateType() {
        return histUpdateType;
    }

    public void setHistUpdateType(String histUpdateType) {
        this.histUpdateType = histUpdateType;
    }

    public Timestamp getLastUpdateDt() {
        return lastUpdateDt;
    }

    public void setLastUpdateDt(Timestamp lastUpdateDt) {
        this.lastUpdateDt = lastUpdateDt;
    }

    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }

    public String getHistSeq() {
        return this.histSeq;
    }

    public void setHistSeq(String histSeq) {
        this.histSeq = histSeq;
    }

    public String getTableName() {
        return this.tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getActiion() {
        return this.action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void addFieldData(Parameter f) {
        if (f != null)
            fields.add(f);
    }

    public String printData() {
        String result = action + "-";
        for (int i = 0; i < fields.size(); i++) {
            result += fields.get(i).value + "|";
        }
        return result;
    }

    private void genSQL(Table schema) {
        if (genSQLDone)
            return;
        String questionMaskString = "";
        String fieldString = "";
        String setString = "";
        String whereString = "";

        for (int i = 0; i < this.fields.size(); i++) {
            Parameter f = fields.get(i);
            String columName = schema.getColumnName(i);
            if (f.value != null) {
                if ("I".equals(action)) {
                    boolean columnNullable = schema.getColumnNullable(i);

                    questionMaskString += "?,";
                    fieldString += columName + ",";
                    if ("LAST_MODIFY_BY".equals(columName))
                        f.value = getLastUpdateBy();
                    else if ("LAST_MODIFY_DT".equals(columName))
                        f.value = getLastUpdateDt();
                    if (f.isNullValue() && columnNullable)
                        f.value = null;
                    if (f.isPK) {
                        whereString += "and " + columName + " = ? ";
                        keydataList.add(f);
                    }
                    dataList.add(f);
                } else if ("U".equals(action)) {
                    boolean columnNullable = schema.getColumnNullable(i);

                    if (!"CREATE_BY".equals(columName) && !"CREATE_DT".equals(columName)) {

                        if ("LAST_MODIFY_BY".equals(columName))
                            f.value = getLastUpdateBy();
                        else if ("LAST_MODIFY_DT".equals(columName))
                            f.value = getLastUpdateDt();
                        if (f.isNullValue() && columnNullable)
                            f.value = null;
                        setString += columName + "=?,";

                        if (f.isPK) {

                            whereString += "and " + columName + " = ? ";
                            keydataList.add(f);
                        }
                        dataList.add(f);
                    }
                } else if ("D".equals(action)) {
                    if (f.isPK) {
                        whereString += "and " + columName + " = ? ";
                        keydataList.add(f);
                    }
                }
            }
        }

        questionMaskString = questionMaskString.equals("") ? ""
                : questionMaskString.substring(0, questionMaskString.length() - 1);
        fieldString = fieldString.equals("") ? "" : fieldString.substring(0, fieldString.length() - 1);
        setString = setString.equals("") ? "" : setString.substring(0, setString.length() - 1);
        insertSQLString = "insert into " + tableName + " (" + fieldString + ") values (" + questionMaskString + ")";
        updateSQLString = "update " + tableName + " set " + setString + " where 1=1 " + whereString;
        getLastModifyDtString = "select last_modify_dt from " + tableName + " where 1=1 " + whereString;
        deleteSQLString = "delete from " + tableName + " where 1=1 " + whereString;
        if ("U".equals(action))
            dataList.addAll(keydataList);
        genSQLDone = true;
    }

    public boolean checkLastUpdateDt(Connection con, Table schema) {
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            if (con == null || con.isClosed())
                return false;
            genSQL(schema);
            ps = con.prepareStatement(getLastModifyDtString);
            CommonDBUtils.setStatement(ps, keydataList, getLastModifyDtString);
            rs = ps.executeQuery();
            Timestamp t = null;
            if (rs.next()) {
                t = rs.getTimestamp(1);

                return getLastUpdateDt().equals(t) || getLastUpdateDt().after(t);
            }

        } catch (SQLException se) {

        } finally {
            try {
                if (ps != null)
                    ps.close();
            } catch (SQLException ignore) {
            }
            try {
                if (rs != null)
                    rs.close();
            } catch (SQLException ignore) {
            }
        }
        return false;
    }

    public PreparedStatement getActionStatement(Connection con, Table schema) {
        try {
            if (con == null || con.isClosed())
                return null;
            genSQL(schema);
            if (HPFW_Connection.INSERT.equals(this.action)) {
                PreparedStatement ps = con.prepareStatement(insertSQLString);
                CommonDBUtils.setStatement(ps, dataList, insertSQLString);
                return ps;
            } else if (HPFW_Connection.UPDATE.equals(this.action)) {
                PreparedStatement ps = con.prepareStatement(updateSQLString);
                CommonDBUtils.setStatement(ps, dataList, updateSQLString);
                return ps;
            } else if (HPFW_Connection.DELETE.equals(this.action)) {
                PreparedStatement ps = con.prepareStatement(deleteSQLString);
                CommonDBUtils.setStatement(ps, keydataList, deleteSQLString);
                return ps;
            } else
                return null;

        } catch (SQLException se) {

        }
        return null;
    }
}
