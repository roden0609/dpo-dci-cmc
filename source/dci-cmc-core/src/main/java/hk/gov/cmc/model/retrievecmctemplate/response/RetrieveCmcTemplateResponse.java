package hk.gov.cmc.model.retrievecmctemplate.response;

public class RetrieveCmcTemplateResponse {

    private String resultCd;
    private String resultMsg;
    private String templateId;
    private String templateVersion;
    private TemplateDetail templateDetail;

    public String getResultCd() {
        return resultCd;
    }

    public void setResultCd(String resultCd) {
        this.resultCd = resultCd;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getTemplateVersion() {
        return templateVersion;
    }

    public void setTemplateVersion(String templateVersion) {
        this.templateVersion = templateVersion;
    }

    public TemplateDetail getTemplateDetail() {
        return templateDetail;
    }

    public void setTemplateDetail(TemplateDetail templateDetail) {
        this.templateDetail = templateDetail;
    }

    @Override
    public String toString() {
        return "RetrieveCmcTemplateResponse [resultCd=" + resultCd + ", resultMsg=" + resultMsg + ", templateId="
                + templateId + ", templateVersion=" + templateVersion + ", templateDetail=" + templateDetail + "]";
    }

}
