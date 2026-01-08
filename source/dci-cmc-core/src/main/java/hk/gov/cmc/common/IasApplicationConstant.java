package hk.gov.cmc.common;

public class IasApplicationConstant {

    public static final String HISTORY_NEW = "New";
    public static final String HISTORY_DELETE = "Delete";
    public static final String HISTORY_REPLACE = "Replace";
    public static final String HISTORY_UPDATE = "Update";

    public static final String MSG_TYPE_APPLICATION = "application";

    public static final String OPERATION_TYPE_CREATE = "c";
    public static final String OPERATION_TYPE_UPDATE = "u";
    public static final String OPERATION_TYPE_DELETE = "d";
    public static final String OPERATION_TYPE_REPLACE = "r";

    public static final String STATUS_UNDELIVERED_APPLICATION_NOT_INFORMED = "N"; // the undelivered message is not informed to the service provided
    public static final String STATUS_UNDELIVERED_APPLICATION_INFORMED = "I"; // the undelivered message has been informed to the service provided
    public static final String STATUS_UNDELIVERED_APPLICATION_CANCELLED = "C"; // the undelivered message has been cancelled as the message is re-instated or the account is re-activated
}
