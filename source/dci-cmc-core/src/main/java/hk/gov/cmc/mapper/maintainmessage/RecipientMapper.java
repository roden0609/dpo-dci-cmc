package hk.gov.cmc.mapper.maintainmessage;

import hk.gov.cmc.jaxb.maintainmessage.RecipientCT;
import hk.gov.cmc.mapper.utils.XmlDateMapper;
import hk.gov.cmc.model.maintainmessage.request.Recipient;

public final class RecipientMapper {

    private RecipientMapper() {
    }

    public static RecipientCT toJaxb(Recipient domain) {
        if (domain == null) {
            return null;
        }

        RecipientCT ct = new RecipientCT();
        ct.setTranID(domain.getTranId());
        ct.setIdpID(domain.getIdpId());
        ct.setRecipientID(domain.getRecipientId());
        ct.setAppRefNum(domain.getAppRefNum());
        ct.setRecipientIDType(domain.getRecipientIdType());
        ct.setItemDate(XmlDateMapper.toXml(domain.getItemDate()));
        ct.setAction(ActionMapper.toJaxb(domain.getAction()));
        ct.setCorrelatedTranID(domain.getCorrelatedTranId());
        ct.setAppStatus(AppStatusMapper.toJaxb(domain.getAppStatus()));
        ct.setAppStatusUpdateDate(XmlDateMapper.toXml(domain.getAppStatusUpdateDate()));
        ct.setContactNum(domain.getContactNum());
        ct.setContactEmail(domain.getContactEmail());
        ct.setMiscInfo(domain.getMiscInfo());
        return ct;
    }

    public static Recipient fromJaxb(RecipientCT ct) {
        if (ct == null) {
            return null;
        }

        Recipient domain = new Recipient();
        domain.setTranId(ct.getTranID());
        domain.setIdpId(ct.getIdpID());
        domain.setRecipientId(ct.getRecipientID());
        domain.setAppRefNum(ct.getAppRefNum());
        domain.setRecipientIdType(ct.getRecipientIDType());
        domain.setItemDate(XmlDateMapper.fromXml(ct.getItemDate()));
        domain.setAction(ActionMapper.fromJaxb(ct.getAction()));
        domain.setCorrelatedTranId(ct.getCorrelatedTranID());
        domain.setAppStatus(AppStatusMapper.fromJaxb(ct.getAppStatus()));
        domain.setAppStatusUpdateDate(XmlDateMapper.fromXml(ct.getAppStatusUpdateDate()));
        domain.setContactNum(ct.getContactNum());
        domain.setContactEmail(ct.getContactEmail());
        domain.setMiscInfo(ct.getMiscInfo());
        return domain;
    }
}
