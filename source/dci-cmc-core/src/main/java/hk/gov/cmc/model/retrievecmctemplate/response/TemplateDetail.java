package hk.gov.cmc.model.retrievecmctemplate.response;

public class TemplateDetail {

    private String subjectEn;
    private String subjectTc;
    private String subjectSc;
    private String contentEn;
    private String contentTc;
    private String contentSc;

    public String getSubjectEn() {
        return subjectEn;
    }

    public void setSubjectEn(String subjectEn) {
        this.subjectEn = subjectEn;
    }

    public String getSubjectTc() {
        return subjectTc;
    }

    public void setSubjectTc(String subjectTc) {
        this.subjectTc = subjectTc;
    }

    public String getSubjectSc() {
        return subjectSc;
    }

    public void setSubjectSc(String subjectSc) {
        this.subjectSc = subjectSc;
    }

    public String getContentEn() {
        return contentEn;
    }

    public void setContentEn(String contentEn) {
        this.contentEn = contentEn;
    }

    public String getContentTc() {
        return contentTc;
    }

    public void setContentTc(String contentTc) {
        this.contentTc = contentTc;
    }

    public String getContentSc() {
        return contentSc;
    }

    public void setContentSc(String contentSc) {
        this.contentSc = contentSc;
    }

    @Override
    public String toString() {
        return "TemplateDetail [subjectEn=" + subjectEn + ", subjectTc=" + subjectTc + ", subjectSc=" + subjectSc
                + ", contentEn=" + contentEn + ", contentTc=" + contentTc + ", contentSc=" + contentSc + "]";
    }

}
