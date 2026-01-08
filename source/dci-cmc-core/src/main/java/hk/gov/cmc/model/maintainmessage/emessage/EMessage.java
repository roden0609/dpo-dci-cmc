package hk.gov.cmc.model.maintainmessage.emessage;

public class EMessage {

    private String templateId;
    private String templateVersion;

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

    @Override
    public String toString() {
        return "EMessage [templateId=" + templateId + ", templateVersion=" + templateVersion + "]";
    }

}
