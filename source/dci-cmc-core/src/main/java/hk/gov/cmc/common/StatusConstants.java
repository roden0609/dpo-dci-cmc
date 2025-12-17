package hk.gov.cmc.common;

public final class StatusConstants {

    private StatusConstants() {
    }

    public static final String MOBILE_MSG_STATUS_NEW = "N";
    public static final String MOBILE_MSG_STATUS_SUCCESS_RESPONSE = "S";
    public static final String MOBILE_MSG_STATUS_FAILURE_RESPONSE = "F";
    public static final String MOBILE_MSG_STATUS_PARTIAL_FAILURE = "A";

    public static final String USER_MOBILE_MSG_STATUS_NEW = "N";
    public static final String USER_MOBILE_MSG_STATUS_SENT_TO_EGIS_MSG = "P";
    public static final String USER_MOBILE_MSG_STATUS_SUCCESS_RESPONSE = "S";
    public static final String USER_MOBILE_MSG_STATUS_FAILURE_RESPONSE = "F";

    public static final String RECIPIENT_RESPONSE_STATUS_SUCCESS = "S";
    public static final String RECIPIENT_RESPONSE_STATUS_DELAY = "D";
    public static final String RECIPIENT_RESPONSE_STATUS_READ = "R";

    public static final String USER_MESSAGE_MOBILE_STATUS_SUCCESS = "Y";
    public static final String USER_MESSAGE_MOBILE_STATUS_FAIL = "F";
    public static final String USER_MESSAGE_MOBILE_STATUS_PROCESSING = "P";

    public static final String USER_STATUS_ACTIVE = "A";
    public static final String USER_STATUS_DELETED = "D";
    public static final String USER_STATUS_SUSPENDED = "S";
    public static final String LINK_STATUS_LINK_UP = "L";
    public static final String LINK_STATUS_DELINK = "D";
    public static final String LINK_STATUS_SUSPEND = "S";
    public static final String PORTAL_STATUS_ACTIVE = "A";
    public static final String PORTAL_STATUS_INACTIVE = "I";

    public static final String IDP_STATUS_ACTIVE = "A";
    public static final String IDP_STATUS_INACTIVE = "I";

    public static final String HIDDEN_IND_HIDDEN = "Y";
    public static final String HIDDEN_IND_NOT_HIDDEN = "N";

    public static final String REJECT_IND_REJECT = "Y";
    public static final String REJECT_IND_NOT_REJECT = "N";

    public static final String RECON_IND_RECON = "Y";
    public static final String RECON_IND_NOT_RECON = "N";

    public static final String ARCHIVE_IND_ARCHIVED = "Y";
    public static final String ARCHIVE_IND_NOT_ARCHIVED = "N";

    public static final String DELETE_IND_DELETED = "Y";
    public static final String DELETE_IND_NOT_DELETED = "N";
    public static final String DELETE_IND_PENDING = "P";

    public static final String COMPLETE_IND_COMPLETED = "C";
    public static final String COMPLETE_IND_IN_COMPLETED = "I";

    public static final String COMPLETE_BY_USER = "U";
    public static final String COMPLETE_BY_RS = "RS";

    public static final String READ_IND_READ = "R";
    public static final String READ_IND_UNREAD = "U";

    public static final String DEFAULT_ALERT_DT = "20000101";

    public static final String UNDELIVERED_MSG_NOT_INFORMED_SP = "N";
    public static final String UNDELIVERED_MSG_INFORMED_SP = "I";
    public static final String UNDELIVERED_MSG_CANCELLED = "C";

    public static final String USER_EMAIL_STATUS_NEW = "N";
    public static final String USER_EMAIL_STATUS_PROFILE_RETRIEVED = "P";
    public static final String USER_EMAIL_STATUS_PROFILE_RETRIEVE_FAIL = "E";
    public static final String USER_EMAIL_STATUS_USER_UNWILLING_TO_RECEIVE_EMAIL = "U";
    public static final String USER_EMAIL_STATUS_SENT_SUCCESS = "S";
    public static final String USER_EMAIL_STATUS_SENT_FAIL = "F";
    public static final String USER_EMAIL_STATUS_SENT_EXCEPTION = "X";
    public static final String USER_EMAIL_STATUS_DELETED = "D";

    public static final String USER_WILLING_TO_RECEIVE_EMAIL_YES = "Y";

    public static final String USER_WILLING_TO_RECEIVE_EMAIL_NO = "N";
}
