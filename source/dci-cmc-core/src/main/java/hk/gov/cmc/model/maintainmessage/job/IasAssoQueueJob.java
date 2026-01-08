package hk.gov.cmc.model.maintainmessage.job;

import java.io.Serializable;
import java.sql.Timestamp;

public class IasAssoQueueJob implements Serializable {
  private static final long serialVersionUID = 1L;

  private String jobId = null;
  private String clientId = null;
  private String openId = null;
  private String serviceProviderId = null;
  private String assoStatus = null;
  private String jobStatus = null;
  private String iasAutoCreateInd = null;
  private String iasOptSpId = null;
  private String serviceProviderNameEn = null;
  private String serviceProviderNameTc = null;
  private String serviceProviderNameSc = null;
  private String mapNotiId = null;
  private String optIn = null;
  private String accStatus = null;
  private Timestamp createDt = null;
  private Timestamp lastModifyDt = null;

  public String getJobId() {
    return jobId == null ? "" : jobId;
  }

  public void setJobId(final String inJobId) {
    jobId = inJobId;
  }

  public String getClientId() {
    return clientId == null ? "" : clientId;
  }

  public void setClientId(final String inClientId) {
    clientId = inClientId;
  }

  public String getOpenId() {
    return openId == null ? "" : openId;
  }

  public void setOpenId(final String inOpenId) {
    openId = inOpenId;
  }

  public String getServiceProviderId() {
    return serviceProviderId == null ? "" : serviceProviderId;
  }

  public void setServiceProviderId(final String inServiceProviderId) {
    serviceProviderId = inServiceProviderId;
  }

  public String getAssoStatus() {
    return assoStatus == null ? "" : assoStatus;
  }

  public void setAssoStatus(final String inAssoStatus) {
    assoStatus = inAssoStatus;
  }

  public String getJobStatus() {
    return jobStatus == null ? "" : jobStatus;
  }

  public void setJobStatus(final String inJobStatus) {
    jobStatus = inJobStatus;
  }

  public String getIasAutoCreateInd() {
    return iasAutoCreateInd == null ? "" : iasAutoCreateInd;
  }

  public void setIasAutoCreateInd(final String inIasAutoCreateInd) {
    iasAutoCreateInd = inIasAutoCreateInd;
  }

  public String getIasOptSpId() {
    return iasOptSpId == null ? "" : iasOptSpId;
  }

  public void setIasOptSpId(final String inIasOptSpId) {
    iasOptSpId = inIasOptSpId;
  }

  public String getServiceProviderNameEn() {
    return serviceProviderNameEn == null ? "" : serviceProviderNameEn;
  }

  public void setServiceProviderNameEn(final String inServiceProviderNameEn) {
    serviceProviderNameEn = inServiceProviderNameEn;
  }

  public String getServiceProviderNameTc() {
    return serviceProviderNameTc == null ? "" : serviceProviderNameTc;
  }

  public void setServiceProviderNameTc(final String inServiceProviderNameTc) {
    serviceProviderNameTc = inServiceProviderNameTc;
  }

  public String getServiceProviderNameSc() {
    return serviceProviderNameSc == null ? "" : serviceProviderNameSc;
  }

  public void setServiceProviderNameSc(final String inServiceProviderNameSc) {
    serviceProviderNameSc = inServiceProviderNameSc;
  }

  public String getMapNotiId() {
    return mapNotiId == null ? "" : mapNotiId;
  }

  public void setMapNotiId(final String inMapNotiId) {
    mapNotiId = inMapNotiId;
  }

  public String getOptIn() {
    return optIn == null ? "" : optIn;
  }

  public void setOptIn(final String inOptIn) {
    optIn = inOptIn;
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

  public String getAccStatus() {
    return accStatus == null ? "" : accStatus;
  }

  public void setAccStatus(final String inAccStatus) {
    accStatus = inAccStatus;
  }

  @Override
  public String toString() {
    return "IasAssoQueueJob [jobId=" + jobId + ", clientId=" + clientId + ", openId=" + openId + ", serviceProviderId="
        + serviceProviderId + ", assoStatus=" + assoStatus + ", jobStatus=" + jobStatus + ", iasAutoCreateInd="
        + iasAutoCreateInd + ", iasOptSpId=" + iasOptSpId + ", serviceProviderNameEn=" + serviceProviderNameEn
        + ", serviceProviderNameTc=" + serviceProviderNameTc + ", serviceProviderNameSc=" + serviceProviderNameSc
        + ", mapNotiId=" + mapNotiId + ", optIn=" + optIn + ", accStatus=" + accStatus + ", createDt=" + createDt
        + ", lastModifyDt=" + lastModifyDt + "]";
  }

}
