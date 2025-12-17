package hk.gov.cmc.persistence.connection.hpfw;

public class Sql {
    public String sql = "";
    boolean isHist = false;
    boolean isBatchSQL = false;
    int windowSize = 0;
    int batchSize = 0;
    int timeoutSeconds = -1;

    public Sql(String sql, boolean isHist, int timeoutSeconds) {
        this.sql = sql;
        this.isHist = isHist;
        this.timeoutSeconds = timeoutSeconds;
    }

    public Sql(String sql, boolean isHist) {
        this.sql = sql;
        this.isHist = isHist;
    }

    public void setIsBatchSQL(boolean isBatchSQL) {
        this.isBatchSQL = isBatchSQL;
    }

    public boolean isBatchSQL() {
        return this.isBatchSQL;
    }

    public void setWindowSize(int windowSize) {
        this.windowSize = windowSize;
    }

    public int getWindowSize() {
        return this.windowSize;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    public int getBatchSize() {
        return this.batchSize;
    }

    public int getTimeoutSeconds() {
        return timeoutSeconds;
    }

    public void setTimeoutSeconds(int timeoutSeconds) {
        this.timeoutSeconds = timeoutSeconds;
    }
}
