package hk.gov.cmc.mapper.maintainmessage;

import hk.gov.cmc.jaxb.maintainmessage.ToDoItemCT;
import hk.gov.cmc.model.maintainmessage.todoitem.ToDoItem;

public final class ToDoItemMapper {

    private ToDoItemMapper() {
    }

    public static ToDoItemCT toJaxb(ToDoItem domain) {
        if (domain == null) {
            return null;
        }

        ToDoItemCT ct = new ToDoItemCT();
        ct.setTemplateID(domain.getTemplateId());
        ct.setTemplateVersion(domain.getTemplateVersion());
        return ct;
    }

    public static ToDoItem fromJaxb(ToDoItemCT ct) {
        if (ct == null) {
            return null;
        }

        ToDoItem domain = new ToDoItem();
        domain.setTemplateId(ct.getTemplateID());
        domain.setTemplateVersion(ct.getTemplateVersion());
        return domain;
    }
}
