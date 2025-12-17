package hk.gov.cmc.persistence.connection.hpfw;

public class TableColumn {
    public String name;
    public short type;
    public boolean isPK;
    public boolean nullable;

    public TableColumn(String name, short type, boolean isPK, boolean nullable) {
        this.name = name;
        this.type = type;
        this.isPK = isPK;
        this.nullable = nullable;
    }

    /**
     * finalize
     */
    protected void finalize() throws Throwable {
    }
}
