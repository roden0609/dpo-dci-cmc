package hk.gov.cmc.common;

public final class IntegrationConstants {

    private IntegrationConstants() {
    }

    public static final String IAS_USER_STATUS_ACTIVE = "A";
    public static final String IAS_USER_STATUS_DEREGISTERED = "D";
    public static final String IAS_USER_STATUS_INVALID = "I";
    public static final String IAS_USER_STATUS_SUSPENDED = "S";
    public static final String IAS_USER_STATUS_MISSING = "M";

    public static final String IAS_NOTI_STATUS_NEW = "N";
    public static final String IAS_NOTI_STATUS_INVALID = "I";
    public static final String IAS_NOTI_STATUS_SENT = "S";
    public static final String IAS_NOTI_STATUS_OPT_OUT = "O";
    public static final String IAS_NOTI_STATUS_FAIL = "F";

    // OPT_IN - Opt-in to receive iAM Smart message from this BD
    public static final String OPT_IN_Y = "Y";
    // OPT_IN - Opt-out to receive iAM Smart message from this BD
    public static final String OPT_IN_N = "N";
    // OPT_IN - Undefine Opt-in option
    public static final String OPT_IN_U = "U";

    // IAS_IND - Support sending iAM Smart also together with My Message
    public static final String IAS_IND_Y = "Y";
    // IAS_IND - Not support iAM Smart
    public static final String IAS_IND_N = "N";
    // IAS_IND - Send iAM Smart Only
    public static final String IAS_IND_I = "I";

    // GET_NOTI_ID_STATUS - Get Notification ID API Result status - Valid NOTI ID
    public static final String GET_NOTI_ID_RESULT_VALID = "0";
    // GET_NOTI_ID_STATUS - Get Notification ID API Result status - Invalid NOTI ID
    public static final String GET_NOTI_ID_RESULT_INVALID = "1";

    // GET_NOTI_ID_STATUS - Get Notification ID API V2 Result status - Active
    public static final String GET_NOTI_ID_RESULT_V2_ACTIVE = "A";
    // GET_NOTI_ID_STATUS - Get Notification ID API V2 Result status - Suspend
    public static final String GET_NOTI_ID_RESULT_V2_SUSPEND = "S";
    // GET_NOTI_ID_STATUS - Get Notification ID API V2 Result status - Invalid
    public static final String GET_NOTI_ID_RESULT_V2_INVALID = "I";
    // GET_NOTI_ID_STATUS - Get Notification ID API V2 Result status - De-registered
    public static final String GET_NOTI_ID_RESULT_V2_DEREGISTERED = "D";
    // GET_NOTI_ID_STATUS - Get Notification ID API V2 Result status - Unknown error
    public static final String GET_NOTI_ID_RESULT_V2_UNKNOWN_ERROR = "U";

    // SEND_NOTI_ID_STATUS - Send Notification ID API Result status - Ready to send
    public static final String SEND_NOTI_ID_RESULT_READY_TO_SEND = "0";
    // SEND_NOTI_ID_STATUS - Send Notification ID API Result status - Sent
    public static final String SEND_NOTI_ID_RESULT_SENT = "1";
    // SEND_NOTI_ID_STATUS - Send Notification ID API Result status - Invalid NOTI ID
    public static final String SEND_NOTI_ID_RESULT_INVALID = "2";
    // SEND_NOTI_ID_STATUS - Send Notification ID API Result status - User is not active
    public static final String SEND_NOTI_ID_RESULT_NOT_ACTIVE = "3";
    // SEND_NOTI_ID_STATUS - Send Notification ID API Result status - Other errors
    public static final String SEND_NOTI_ID_RESULT_OTHER_ERROR = "4";
    // CMC-2024-011: Enhance Maintain Message Module To Support iAM Smart Application Status - BEGIN
    public static final String SEND_NOTI_ID_INVALID_CONSENT = "5";
    // CMC-2024-011: Enhance Maintain Message Module To Support iAM Smart Application Status - END

    // GET_NOTI_ID_RESULT_CODE - Get Notification ID API Result code - SUCCESS
    public static final String GET_NOTI_ID_RESULT_CODE_SUCCESS = "D00000";
    // GET_NOTI_ID_STATUS - Get Notification ID API Result code - partial success
    public static final String GET_NOTI_ID_RESULT_CODE_PARTIAL_SUCCESS = "D00001";

    // JOB_STATUS - Job status new for IAS_MSG_STATUS_QUEUE
    public static final String JOB_STATUS_NEW = "N";
    // JOB_STATUS - Job status complete for IAS_MSG_STATUS_QUEUE
    public static final String JOB_STATUS_COMPLETE = "C";
    // JOB_STATUS - Job status fail for IAS_MSG_STATUS_QUEUE
    public static final String JOB_STATUS_FAIL = "F";

    // ASSO_STATUS - Associate status new for IAS_ASSO_QUEUEGET_NOTI_ID_RESULT_CODE_SUCCESS
    public static final String ASSO_STATUS_ASSOCIATE = "A";
    // ASSO_STATUS - Associate complete for IAS_ASSO_QUEUE
    public static final String ASSO_STATUS_DISASSOCIATE = "D";

    // IAS_MSG_RCPT_TYPE -MyGov My ID
    public static final String IAS_MSG_RCPT_TYPE_MY_ID = "M";
    // IAS_MSG_RCPT_TYPE - iAM Smart Open ID / HKID
    public static final String IAS_MSG_RCPT_TYPE_IAM_SMART = "I";

    // iAM Smart Undelivered Message Reason - "General Error"
    public static final String IAS_UNDELIVER_MSG_REASON_GENERAL_ERROR = "General Error";
    // iAM Smart Undelivered Message Reason - "DEREGISTERED"
    public static final String IAS_UNDELIVER_MSG_REASON_DEREGISTERED = "User is de-registered";
    // iAM Smart Undelivered Message Reason - "SUSPENDED"
    public static final String IAS_UNDELIVER_MSG_REASON_SUSPENDED = "User is suspended";
    // iAM Smart Undelivered Message Reason - "INVALID"
    public static final String IAS_UNDELIVER_MSG_REASON_ACC_NOT_ACTIVE = "Account is not active";
    public static final String IAS_UNDELIVER_MSG_REASON_OPT_OUT = "User Opt-out to receive message";
    public static final String IAS_UNDELIVER_MSG_REASON_INVALID_NOTI_ID = "Notification ID is invalid";
    public static final String IAS_UNDELIVER_MSG_REASON_INVALID_CONSENT = "Invalid Consent";
    public static final String IAS_UNDELIVER_MSG_REASON_OTHER_EROR = "Other Errors";
}
