package hk.gov.cmc.model.maintainmessage.param;

import java.io.Serializable;

public class MessageParam implements Serializable {

    private static final long serialVersionUID = -6196911591066047919L;
    public static final String MESSAGE_TYPE_ALL_MSG = "AL";
    public static final String MESSAGE_TYPE_INTERNET_MSG = "IN";
    public static final String MESSAGE_TYPE_MOBILE_MSG = "MO";
    public static final String MESSAGE_TYPE_MY_MSG = "MY";
    public static final String MESSAGE_TYPE_TODO_MSG = "TD";
    public static final String MESSAGE_TYPE_IAS_MSG = "IA";
    public static final String MESSAGE_TYPE_IAS_TO_DO_ITEM = "IT";
    public static final String MESSAGE_TYPE_IAS_APPLICATION = "AS";
    public static final String MESSAGE_PARAM_TYPE_STATIC = "S";
    public static final String MESSAGE_PARAM_TYPE_MARS_ATTR = "M";
    public static final String MARS_ATTR_ALIAS = "alias";

    private String paraName;
    private String paraSource;
    private String messageType;
    private String marsUserAttr;
    private String staticParaEn;
    private String staticParaTc;
    private String staticParaSc;

    public String getParaName() {
        return paraName;
    }

    public void setParaName(String paraName) {
        this.paraName = paraName;
    }

    public String getParaSource() {
        return paraSource;
    }

    public void setParaSource(String paraSource) {
        this.paraSource = paraSource;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getMarsUserAttr() {
        return marsUserAttr;
    }

    public void setMarsUserAttr(String marsUserAttr) {
        this.marsUserAttr = marsUserAttr;
    }

    public String getStaticParaEn() {
        return staticParaEn;
    }

    public void setStaticParaEn(String staticParaEn) {
        this.staticParaEn = staticParaEn;
    }

    public String getStaticParaTc() {
        return staticParaTc;
    }

    public void setStaticParaTc(String staticParaTc) {
        this.staticParaTc = staticParaTc;
    }

    public String getStaticParaSc() {
        return staticParaSc;
    }

    public void setStaticParaSc(String staticParaSc) {
        this.staticParaSc = staticParaSc;
    }

    @Override
    public String toString() {
        return "MessageParam [paraName=" + paraName + ", paraSource=" + paraSource + ", messageType=" + messageType
                + ", marsUserAttr=" + marsUserAttr + ", staticParaEn=" + staticParaEn + ", staticParaTc=" + staticParaTc
                + ", staticParaSc=" + staticParaSc + "]";
    }

}
