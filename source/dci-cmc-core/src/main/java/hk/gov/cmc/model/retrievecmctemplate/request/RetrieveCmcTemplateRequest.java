package hk.gov.cmc.model.retrievecmctemplate.request;

public class RetrieveCmcTemplateRequest {

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
        return "RetrieveCmcTemplateRequest [templateId=" + templateId + ", templateVersion=" + templateVersion + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((templateId == null) ? 0 : templateId.hashCode());
        result = prime * result + ((templateVersion == null) ? 0 : templateVersion.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RetrieveCmcTemplateRequest other = (RetrieveCmcTemplateRequest) obj;
        if (templateId == null) {
            if (other.templateId != null)
                return false;
        } else if (!templateId.equals(other.templateId))
            return false;
        if (templateVersion == null) {
            if (other.templateVersion != null)
                return false;
        } else if (!templateVersion.equals(other.templateVersion))
            return false;
        return true;
    }

}
