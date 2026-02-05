package hk.gov.cmc.processor.report;

import java.io.Serializable;
import java.util.Date;

public class ReportResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean success;
    private Date reportingDate;
    private Date dataStartDate;
    private String outputFileType;
    private byte[] outputFileByteArray;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Date getReportingDate() {
        return reportingDate;
    }

    public void setReportingDate(Date reportingDate) {
        this.reportingDate = reportingDate;
    }

    public Date getDataStartDate() {
        return dataStartDate;
    }

    public void setDataStartDate(Date dataStartDate) {
        this.dataStartDate = dataStartDate;
    }

    public String getOutputFileType() {
        return outputFileType;
    }

    public void setOutputFileType(String outputFileType) {
        this.outputFileType = outputFileType;
    }

    public byte[] getOutputFileByteArray() {
        return outputFileByteArray;
    }

    public void setOutputFileByteArray(byte[] outputFileByteArray) {
        this.outputFileByteArray = outputFileByteArray;
    }
}
