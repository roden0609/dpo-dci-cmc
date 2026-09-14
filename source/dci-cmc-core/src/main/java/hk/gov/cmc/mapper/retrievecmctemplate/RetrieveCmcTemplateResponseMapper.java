package hk.gov.cmc.mapper.retrievecmctemplate;

import hk.gov.cmc.jaxb.retrievecmctemplate.RetrieveCmcTemplateResponse;

public final class RetrieveCmcTemplateResponseMapper {

    private RetrieveCmcTemplateResponseMapper() {
    }

    public static RetrieveCmcTemplateResponse toJaxb(
            hk.gov.cmc.model.retrievecmctemplate.response.RetrieveCmcTemplateResponse domain) {

        if (domain == null) {
            return null;
        }

        RetrieveCmcTemplateResponse jaxb = new RetrieveCmcTemplateResponse();
        jaxb.setResultCd(domain.getResultCd());
        jaxb.setResultMsg(domain.getResultMsg());
        jaxb.setTemplateId(domain.getTemplateId());
        jaxb.setTemplateVersion(domain.getTemplateVersion());

        jaxb.setTemplateDetail(TemplateDetailMapper.toJaxb(domain.getTemplateDetail()));

        return jaxb;
    }

    public static hk.gov.cmc.model.retrievecmctemplate.response.RetrieveCmcTemplateResponse fromJaxb(
            RetrieveCmcTemplateResponse jaxb) {

        if (jaxb == null) {
            return null;
        }

        hk.gov.cmc.model.retrievecmctemplate.response.RetrieveCmcTemplateResponse domain = new hk.gov.cmc.model.retrievecmctemplate.response.RetrieveCmcTemplateResponse();

        domain.setResultCd(jaxb.getResultCd());
        domain.setResultMsg(jaxb.getResultMsg());
        domain.setTemplateId(jaxb.getTemplateId());
        domain.setTemplateVersion(jaxb.getTemplateVersion());
        if (jaxb.getTemplateDetail() != null) {
            domain.setTemplateDetail(TemplateDetailMapper.fromJaxb(jaxb.getTemplateDetail()));
        }

        return domain;
    }
}
