package hk.gov.cmc.mapper.maintainmessage;

import hk.gov.cmc.jaxb.maintainmessage.ApplicationCT;
import hk.gov.cmc.model.maintainmessage.application.Application;

public final class ApplicationMapper {

    private ApplicationMapper() {
    }

    public static ApplicationCT toJaxb(Application domain) {
        if (domain == null) {
            return null;
        }

        ApplicationCT ct = new ApplicationCT();
        ct.setTemplateID(domain.getTemplateId());
        ct.setTemplateVersion(domain.getTemplateVersion());
        return ct;
    }

    public static Application fromJaxb(ApplicationCT ct) {
        if (ct == null) {
            return null;
        }

        Application domain = new Application();
        domain.setTemplateId(ct.getTemplateID());
        domain.setTemplateVersion(ct.getTemplateVersion());
        return domain;
    }
}
