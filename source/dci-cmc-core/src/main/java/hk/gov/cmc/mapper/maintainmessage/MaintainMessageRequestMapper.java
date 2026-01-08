package hk.gov.cmc.mapper.maintainmessage;

import java.util.ArrayList;
import java.util.List;

import hk.gov.cmc.jaxb.maintainmessage.MaintainMessageRequest;
import hk.gov.cmc.jaxb.maintainmessage.MessageRequestCT;
import hk.gov.cmc.model.maintainmessage.request.MessageRequest;

public final class MaintainMessageRequestMapper {

    private MaintainMessageRequestMapper() {
    }

    public static MaintainMessageRequest toJaxb(
            hk.gov.cmc.model.maintainmessage.request.MaintainMessageRequest domain) {

        if (domain == null) {
            return null;
        }

        MaintainMessageRequest jaxb = new MaintainMessageRequest();

        if (domain.getMessageRequests() != null) {
            for (MessageRequest r : domain.getMessageRequests()) {
                MessageRequestCT ct = MessageRequestMapper.toJaxb(r);
                if (ct != null) {
                    jaxb.getMessageRequest().add(ct);
                }
            }
        }

        return jaxb;
    }

    public static hk.gov.cmc.model.maintainmessage.request.MaintainMessageRequest fromJaxb(
            MaintainMessageRequest jaxb) {

        if (jaxb == null) {
            return null;
        }

        hk.gov.cmc.model.maintainmessage.request.MaintainMessageRequest domain =
                new hk.gov.cmc.model.maintainmessage.request.MaintainMessageRequest();

        if (jaxb.getMessageRequest() != null) {
            List<MessageRequest> list = new ArrayList<>();
            for (MessageRequestCT ct : jaxb.getMessageRequest()) {
                MessageRequest r = MessageRequestMapper.fromJaxb(ct);
                if (r != null) {
                    list.add(r);
                }
            }
            domain.setMessageRequests(list);
        }

        return domain;
    }
}
