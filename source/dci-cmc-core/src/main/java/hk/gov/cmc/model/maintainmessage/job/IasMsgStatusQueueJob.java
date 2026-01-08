package hk.gov.cmc.model.maintainmessage.job;

import java.io.Serializable;
import java.sql.Timestamp;

public class IasMsgStatusQueueJob implements Serializable {
  private static final long serialVersionUID = 1L;

  private String jobId = null;
  private String notiId = null;
  private String accountStatus = null;
  private String spId = null;
  private String msgType = null;
  private String jobStatus = null;
  private Timestamp createDt = null;
  private Timestamp lastModifyDt = null;

  public String getJobId() {
    return jobId == null ? "" : jobId;
  }

  public void setJobId(final String inJobId) {
    jobId = inJobId;
  }

  public String getNotiId() {
    return notiId == null ? "" : notiId;
  }

  public void setNotiId(final String inNotiId) {
    notiId = inNotiId;
  }

  public String getAccountStatus() {
    return accountStatus == null ? "" : accountStatus;
  }

  public void setAccountStatus(final String inAccountStatus) {
    accountStatus = inAccountStatus;
  }

  public String getJobStatus() {
    return jobStatus == null ? "" : jobStatus;
  }

  public void setJobStatus(final String inJobStatus) {
    jobStatus = inJobStatus;
  }

  public Timestamp getCreateDt() {
    return createDt;
  }

  public void setCreateDt(final Timestamp inCreateDt) {
    createDt = inCreateDt;
  }

  public Timestamp getLastModifyDt() {
    return lastModifyDt;
  }

  public void setLastModifyDt(final Timestamp inLastModifyDt) {
    lastModifyDt = inLastModifyDt;
  }

  public String getSpId() {
    return spId;
  }

  public void setSpId(String spId) {
    this.spId = spId;
  }

  public String getMsgType() {
    return msgType;
  }

  public void setMsgType(String msgType) {
    this.msgType = msgType;
  }

  @Override
  public String toString() {
    return "IasMsgStatusQueueJob [jobId=" + jobId + ", notiId=" + notiId + ", accountStatus=" + accountStatus
        + ", spId=" + spId + ", msgType=" + msgType + ", jobStatus=" + jobStatus + ", createDt=" + createDt
        + ", lastModifyDt=" + lastModifyDt + "]";
  }

}
