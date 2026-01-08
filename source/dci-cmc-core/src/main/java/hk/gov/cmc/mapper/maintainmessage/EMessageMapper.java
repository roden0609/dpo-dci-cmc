package hk.gov.cmc.mapper.maintainmessage;

import hk.gov.cmc.jaxb.maintainmessage.EMessageCT;
import hk.gov.cmc.model.maintainmessage.emessage.EMessage;

public final class EMessageMapper {

    private EMessageMapper() {
    }

    public static EMessageCT toJaxb(EMessage domain) {

        if (domain == null) {
            return null;
        }

        EMessageCT ct = new EMessageCT();

        ct.setTemplateID(domain.getTemplateId());
        ct.setTemplateVersion(domain.getTemplateVersion());

        return ct;
    }

    public static EMessage fromJaxb(EMessageCT ct) {

        if (ct == null) {
            return null;
        }

        EMessage domain = new EMessage();

        domain.setTemplateId(ct.getTemplateID());
        domain.setTemplateVersion(ct.getTemplateVersion());

        return domain;
    }
}
