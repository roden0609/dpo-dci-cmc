package hk.gov.cmc.mapper.maintainmessage;

import java.util.ArrayList;
import java.util.List;

import hk.gov.cmc.jaxb.maintainmessage.ApplicationCT;
import hk.gov.cmc.jaxb.maintainmessage.EMessageCT;
import hk.gov.cmc.jaxb.maintainmessage.MessageRequestCT;
import hk.gov.cmc.jaxb.maintainmessage.MetaDataCT;
import hk.gov.cmc.jaxb.maintainmessage.ToDoItemCT;
import hk.gov.cmc.model.maintainmessage.request.MessageRequest;
import hk.gov.cmc.model.maintainmessage.request.MetaData;

public final class MessageRequestMapper {

    private MessageRequestMapper() {
    }

    public static MessageRequestCT toJaxb(MessageRequest domain) {

        if (domain == null) {
            return null;
        }

        MessageRequestCT ct = new MessageRequestCT();

        ct.setPortalID(domain.getPortalId());

        // EMessage
        if (domain.getEMessage() != null) {
            EMessageCT eMessageCT =
                    EMessageMapper.toJaxb(domain.getEMessage());
            ct.setEMessage(eMessageCT);
        }

        // ToDoItem
        if (domain.getToDoItem() != null) {
            ToDoItemCT toDoItemCT =
                    ToDoItemMapper.toJaxb(domain.getToDoItem());
            ct.setToDoItem(toDoItemCT);
        }

        // Application
        if (domain.getApplication() != null) {
            ApplicationCT applicationCT =
                    ApplicationMapper.toJaxb(domain.getApplication());
            ct.setApplication(applicationCT);
        }

        // MetaData (unbounded)
        if (domain.getMetaDataList() != null) {
            for (MetaData m : domain.getMetaDataList()) {
                MetaDataCT metaDataCT =
                        MetaDataMapper.toJaxb(m);
                if (metaDataCT != null) {
                    ct.getMetaData().add(metaDataCT);
                }
            }
        }

        return ct;
    }

    public static MessageRequest fromJaxb(MessageRequestCT ct) {

        if (ct == null) {
            return null;
        }

        MessageRequest domain = new MessageRequest();

        domain.setPortalId(ct.getPortalID());

        // EMessage
        if (ct.getEMessage() != null) {
            domain.setEMessage(
                    EMessageMapper.fromJaxb(ct.getEMessage()));
        }

        // ToDoItem
        if (ct.getToDoItem() != null) {
            domain.setToDoItem(
                    ToDoItemMapper.fromJaxb(ct.getToDoItem()));
        }

        // Application
        if (ct.getApplication() != null) {
            domain.setApplication(
                    ApplicationMapper.fromJaxb(ct.getApplication()));
        }

        // MetaData (unbounded)
        if (ct.getMetaData() != null) {
            List<MetaData> list = new ArrayList<>();
            for (MetaDataCT m : ct.getMetaData()) {
                MetaData metaData =
                        MetaDataMapper.fromJaxb(m);
                if (metaData != null) {
                    list.add(metaData);
                }
            }
            domain.setMetaDataList(list);
        }

        return domain;
    }
}
