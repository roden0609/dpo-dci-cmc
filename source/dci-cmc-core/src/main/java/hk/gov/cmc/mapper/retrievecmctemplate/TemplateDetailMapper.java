package hk.gov.cmc.mapper.retrievecmctemplate;

import hk.gov.cmc.jaxb.retrievecmctemplate.TemplateDetailCT;
import hk.gov.cmc.model.retrievecmctemplate.response.TemplateDetail;

public class TemplateDetailMapper {

    public static TemplateDetailCT toJaxb(TemplateDetail domain) {
        if (domain == null) {
            return null;
        }

        TemplateDetailCT ct = new TemplateDetailCT();
        ct.setSubjectEn(domain.getSubjectEn());
        ct.setSubjectTc(domain.getSubjectTc());
        ct.setSubjectSc(domain.getSubjectSc());
        ct.setContentEn(domain.getContentEn());
        ct.setContentTc(domain.getContentTc());
        ct.setContentSc(domain.getContentSc());

        return ct;
    }

    public static TemplateDetail fromJaxb(TemplateDetailCT ct) {
        if (ct == null) {
            return null;
        }

        TemplateDetail domain = new TemplateDetail();
        domain.setSubjectEn(ct.getSubjectEn());
        domain.setSubjectTc(ct.getSubjectTc());
        domain.setSubjectSc(ct.getSubjectSc());
        domain.setContentEn(ct.getContentEn());
        domain.setContentTc(ct.getContentTc());
        domain.setContentSc(ct.getContentSc());

        return domain;
    }
}
