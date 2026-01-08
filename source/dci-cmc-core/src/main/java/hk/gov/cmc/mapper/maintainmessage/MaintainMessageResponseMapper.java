package hk.gov.cmc.mapper.maintainmessage;

import java.util.ArrayList;
import java.util.List;

import hk.gov.cmc.jaxb.maintainmessage.MaintainMessageResponse;
import hk.gov.cmc.jaxb.maintainmessage.MessageResponseCT;
import hk.gov.cmc.model.maintainmessage.response.MessageResponse;

public final class MaintainMessageResponseMapper {

    private MaintainMessageResponseMapper() {
    }

    public static MaintainMessageResponse toJaxb(
            hk.gov.cmc.model.maintainmessage.response.MaintainMessageResponse domain) {

        if (domain == null) {
            return null;
        }

        MaintainMessageResponse jaxb = new MaintainMessageResponse();
        jaxb.setResultCode(domain.getResultCode());
        jaxb.setResultMessage(domain.getResultMessage());

        if (domain.getMessageResponses() != null) {
            for (MessageResponse r : domain.getMessageResponses()) {
                MessageResponseCT ct = MessageResponseMapper.toJaxb(r);
                if (ct != null) {
                    jaxb.getMessageResponse().add(ct);
                }
            }
        }

        return jaxb;
    }

    public static hk.gov.cmc.model.maintainmessage.response.MaintainMessageResponse fromJaxb(
            MaintainMessageResponse jaxb) {

        if (jaxb == null) {
            return null;
        }

        hk.gov.cmc.model.maintainmessage.response.MaintainMessageResponse domain =
                new hk.gov.cmc.model.maintainmessage.response.MaintainMessageResponse();

        domain.setResultCode(jaxb.getResultCode());
        domain.setResultMessage(jaxb.getResultMessage());

        if (jaxb.getMessageResponse() != null) {
            List<MessageResponse> list = new ArrayList<>();
            for (MessageResponseCT ct : jaxb.getMessageResponse()) {
                MessageResponse r = MessageResponseMapper.fromJaxb(ct);
                if (r != null) {
                    list.add(r);
                }
            }
            domain.setMessageResponses(list);
        }

        return domain;
    }
}
