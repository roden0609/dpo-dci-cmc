package hk.gov.cmc.persistence.connection.hpfw;

import java.util.ArrayList;

public class Table {
    public String tableName; // _H table name
    public String actualTableName;
    public ArrayList<TableColumn> colums = new ArrayList<TableColumn>();
    public ArrayList<Row> rows = new ArrayList<Row>();

    public Table(String tableName) {
        this.tableName = tableName;
        this.actualTableName = tableName.substring(0, tableName.length() - 2);
    }

    public void addColum(TableColumn t) {
        colums.add(t);
    }

    public void addRow(Row r) {
        r.setTableName(this.actualTableName);
        rows.add(r);
    }

    public ArrayList<Row> getRows() {
        return rows;
    }

    public final String getColumnName(int i) {
        return colums.get(i).name;
    }

    public final int getColumnSize() {
        return colums.size();
    }

    public final boolean getIsPK(int i) {
        return colums.get(i).isPK;
    }

    public final short getColumnType(int i) {
        return colums.get(i).type;
    }

    public final boolean getColumnNullable(int i) {
        return colums.get(i).nullable;
    }

    public void printAllData() {
        for (int i = 0; i < rows.size(); i++) {
            String result = i + ". ";
            Row r = rows.get(i);
            result += r.printData();
        }
    }

    protected void finalize() throws Throwable {
        colums.clear();
        colums = null;
        rows.clear();
        rows = null;
    }
}
