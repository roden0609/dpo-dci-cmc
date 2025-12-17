package hk.gov.cmc.persistence.connection.hpfw;

import java.util.Date;

public class MysqlSeqStore {
    private String seqName;
    private long seqNum;
    private long maxValue;
    private long minValue;
    private String cycleInd;
    private int incrBy;
    private Date createDt;
    private Date lastModifyDt;
    private String createBy;
    private String lastModifyBy;

    public String getSeqName() {
        return seqName;
    }

    public void setSeqName(String seqName) {
        this.seqName = seqName;
    }

    public long getSeqNum() {
        return seqNum;
    }

    public void setSeqNum(long seqNum) {
        this.seqNum = seqNum;
    }

    public long getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(long maxValue) {
        this.maxValue = maxValue;
    }

    public long getMinValue() {
        return minValue;
    }

    public void setMinValue(long minValue) {
        this.minValue = minValue;
    }

    public String getCycleInd() {
        return cycleInd;
    }

    public void setCycleInd(String cycleInd) {
        this.cycleInd = cycleInd;
    }

    public int getIncrBy() {
        return incrBy;
    }

    public void setIncrBy(int incrBy) {
        this.incrBy = incrBy;
    }

    public Date getCreateDt() {
        return createDt;
    }

    public void setCreateDt(Date createDt) {
        this.createDt = createDt;
    }

    public Date getLastModifyDt() {
        return lastModifyDt;
    }

    public void setLastModifyDt(Date lastModifyDt) {
        this.lastModifyDt = lastModifyDt;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getLastModifyBy() {
        return lastModifyBy;
    }

    public void setLastModifyBy(String lastModifyBy) {
        this.lastModifyBy = lastModifyBy;
    }
}
