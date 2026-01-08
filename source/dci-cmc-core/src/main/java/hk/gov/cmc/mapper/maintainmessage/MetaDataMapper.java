package hk.gov.cmc.mapper.maintainmessage;

import java.util.ArrayList;
import java.util.List;

import hk.gov.cmc.jaxb.maintainmessage.MetaDataCT;
import hk.gov.cmc.jaxb.maintainmessage.RecipientCT;
import hk.gov.cmc.model.maintainmessage.request.MetaData;
import hk.gov.cmc.model.maintainmessage.request.Recipient;

public final class MetaDataMapper {

    private MetaDataMapper() {
    }

    public static MetaDataCT toJaxb(MetaData domain) {

        if (domain == null) {
            return null;
        }

        MetaDataCT ct = new MetaDataCT();

        ct.setDataContentEN(domain.getDataContentEN());
        ct.setDataContentTC(domain.getDataContentTC());
        ct.setDataContentSC(domain.getDataContentSC());

        if (domain.getRecipients() != null) {
            for (Recipient r : domain.getRecipients()) {
                RecipientCT recipientCT =
                        RecipientMapper.toJaxb(r);
                if (recipientCT != null) {
                    ct.getRecipient().add(recipientCT);
                }
            }
        }

        return ct;
    }

    public static MetaData fromJaxb(MetaDataCT ct) {

        if (ct == null) {
            return null;
        }

        MetaData domain = new MetaData();

        domain.setDataContentEN(ct.getDataContentEN());
        domain.setDataContentTC(ct.getDataContentTC());
        domain.setDataContentSC(ct.getDataContentSC());

        if (ct.getRecipient() != null) {
            List<Recipient> list = new ArrayList<>();
            for (RecipientCT r : ct.getRecipient()) {
                Recipient recipient =
                        RecipientMapper.fromJaxb(r);
                if (recipient != null) {
                    list.add(recipient);
                }
            }
            domain.setRecipients(list);
        }

        return domain;
    }
}
