package hk.gov.cmc.model.maintainmessage.emessage;

import java.io.Serializable;
import java.lang.String;
import java.sql.Timestamp;

public class IasUserMsg implements Serializable {
  private static final long serialVersionUID = 1L;

  private String clientId = null;
  private String serviceProviderId = null;
  private String openId = null;
  private String iasMsgId = null;
  private String notiId = null;
  private String readInd = null;
  private String iasNotiStatus = null;
  private String txId = null;
  private Timestamp sentDt = null;
  private String iasDeliveryStatus = null;
  private String housekeepInd = null;
  private String optIn = null;
  private String subjectEn = null;
  private String subjectTc = null;
  private String subjectSc = null;
  private String status = null;
  private String iasAutoCreateInd = null;
  private String iasOptCheckInd = null;
  private String iasOptSpId = null;
  private String mapNotiId = null;

  public IasUserMsg() {
    super();
  }

  public String getClientId() {
    return clientId == null ? "" : clientId;
  }

  public void setClientId(final String inClientId) {
    clientId = inClientId;
  }

  public String getServiceProviderId() {
    return serviceProviderId == null ? "" : serviceProviderId;
  }

  public void setServiceProviderId(final String inServiceProviderId) {
    serviceProviderId = inServiceProviderId;
  }

  public String getOpenId() {
    return openId == null ? "" : openId;
  }

  public void setOpenId(final String inOpenId) {
    openId = inOpenId;
  }

  public String getIasMsgId() {
    return iasMsgId == null ? "" : iasMsgId;
  }

  public void setIasMsgId(final String inIasMsgId) {
    iasMsgId = inIasMsgId;
  }

  public String getNotiId() {
    return notiId == null ? "" : notiId;
  }

  public void setNotiId(final String inNotiId) {
    notiId = inNotiId;
  }

  public String getReadInd() {
    return readInd == null ? "" : readInd;
  }

  public void setReadInd(final String inReadInd) {
    readInd = inReadInd;
  }

  public String getIasNotiStatus() {
    return iasNotiStatus == null ? "" : iasNotiStatus;
  }

  public void setIasNotiStatus(final String inIasNotiStatus) {
    iasNotiStatus = inIasNotiStatus;
  }

  public String getTxId() {
    return txId == null ? "" : txId;
  }

  public void setTxId(final String inTxId) {
    txId = inTxId;
  }

  public Timestamp getSentDt() {
    return sentDt;
  }

  public void setSentDt(final Timestamp inSentDt) {
    sentDt = inSentDt;
  }

  public String getIasDeliveryStatus() {
    return iasDeliveryStatus == null ? "" : iasDeliveryStatus;
  }

  public void setIasDeliveryStatus(final String inIasDeliveryStatus) {
    iasDeliveryStatus = inIasDeliveryStatus;
  }

  public String getHousekeepInd() {
    return housekeepInd == null ? "" : housekeepInd;
  }

  public void setHousekeepInd(final String inHousekeepInd) {
    housekeepInd = inHousekeepInd;
  }

  public String getOptIn() {
    return optIn == null ? "" : optIn;
  }

  public void setOptIn(final String inOptIn) {
    optIn = inOptIn;
  }

  public String getSubjectEn() {
    return subjectEn == null ? "" : subjectEn;
  }

  public void setSubjectEn(final String inSubjectEn) {
    subjectEn = inSubjectEn;
  }

  public String getSubjectTc() {
    return subjectTc == null ? "" : subjectTc;
  }

  public void setSubjectTc(final String inSubjectTc) {
    subjectTc = inSubjectTc;
  }

  public String getSubjectSc() {
    return subjectSc == null ? "" : subjectSc;
  }

  public void setSubjectSc(final String inSubjectSc) {
    subjectSc = inSubjectSc;
  }

  public String getStatus() {
    return status == null ? "" : status;
  }

  public void setStatus(final String inStatus) {
    status = inStatus;
  }

  public String getIasAutoCreateInd() {
    return iasAutoCreateInd == null ? "" : iasAutoCreateInd;
  }

  public void setIasAutoCreateInd(final String inIasAutoCreateInd) {
    iasAutoCreateInd = inIasAutoCreateInd;
  }

  public String getIasOptCheckInd() {
    return iasOptCheckInd == null ? "" : iasOptCheckInd;
  }

  public void setIasOptCheckInd(final String inIasOptCheckInd) {
    iasOptCheckInd = inIasOptCheckInd;
  }

  public String getIasOptSpId() {
    return iasOptSpId == null ? "" : iasOptSpId;
  }

  public void setIasOptSpId(final String inIasOptSpId) {
    iasOptSpId = inIasOptSpId;
  }

  public String getMapNotiId() {
    return mapNotiId == null ? "" : mapNotiId;
  }

  public void setMapNotiId(final String inMapNotiId) {
    mapNotiId = inMapNotiId;
  }

  @Override
  public String toString() {
    return "IasUserMsg [clientId=" + clientId + ", serviceProviderId=" + serviceProviderId + ", openId=" + openId
        + ", iasMsgId=" + iasMsgId + ", notiId=" + notiId + ", readInd=" + readInd + ", iasNotiStatus=" + iasNotiStatus
        + ", txId=" + txId + ", sentDt=" + sentDt + ", iasDeliveryStatus=" + iasDeliveryStatus + ", housekeepInd="
        + housekeepInd + ", optIn=" + optIn + ", subjectEn=" + subjectEn + ", subjectTc=" + subjectTc + ", subjectSc="
        + subjectSc + ", status=" + status + ", iasAutoCreateInd=" + iasAutoCreateInd + ", iasOptCheckInd="
        + iasOptCheckInd + ", iasOptSpId=" + iasOptSpId + ", mapNotiId=" + mapNotiId + "]";
  }

}
