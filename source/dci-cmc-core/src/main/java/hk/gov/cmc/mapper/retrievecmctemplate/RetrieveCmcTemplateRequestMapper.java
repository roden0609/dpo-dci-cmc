package hk.gov.cmc.mapper.retrievecmctemplate;

import hk.gov.cmc.jaxb.retrievecmctemplate.RetrieveCmcTemplateRequest;

public final class RetrieveCmcTemplateRequestMapper {

    private RetrieveCmcTemplateRequestMapper() {
    }

    public static RetrieveCmcTemplateRequest toJaxb(
            hk.gov.cmc.model.retrievecmctemplate.request.RetrieveCmcTemplateRequest domain) {

        if (domain == null) {
            return null;
        }

        RetrieveCmcTemplateRequest jaxb = new RetrieveCmcTemplateRequest();
        jaxb.setTemplateId(domain.getTemplateId());
        jaxb.setTemplateVersion(domain.getTemplateVersion());

        return jaxb;
    }

    public static hk.gov.cmc.model.retrievecmctemplate.request.RetrieveCmcTemplateRequest fromJaxb(
            RetrieveCmcTemplateRequest jaxb) {

        if (jaxb == null) {
            return null;
        }

        hk.gov.cmc.model.retrievecmctemplate.request.RetrieveCmcTemplateRequest domain = new hk.gov.cmc.model.retrievecmctemplate.request.RetrieveCmcTemplateRequest();

        domain.setTemplateId(jaxb.getTemplateId());
        domain.setTemplateVersion(jaxb.getTemplateVersion());

        return domain;
    }
}
