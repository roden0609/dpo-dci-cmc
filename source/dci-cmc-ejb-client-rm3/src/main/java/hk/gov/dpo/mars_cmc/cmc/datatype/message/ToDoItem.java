package hk.gov.dpo.mars_cmc.cmc.datatype.message;

import java.io.Serializable;

public class ToDoItem implements Serializable {

    private static final long serialVersionUID = 1L;
    private String m_templateID;
    private String m_templateVersion;

    public String getTemplateID() {
        return m_templateID;
    }

    public void setTemplateID(String templateID) {
        m_templateID = templateID;
    }

    public String getTemplateVersion() {
        return m_templateVersion;
    }

    public void setTemplateVersion(String templateVersion) {
        m_templateVersion = templateVersion;
    }

}
