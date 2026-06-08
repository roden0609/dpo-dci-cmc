package hk.gov.cmc.common;

public final class ResultMessages {

    private ResultMessages() {
    }

    public static final String RESULT_MSG_GENERAL_ERROR = "General Error.";
    public static final String RESULT_MSG_SUCCESS = "Request processed successfully.";

    public static final String RESULT_MSG_SENDER_APPID_NOT_FOUND = "Sender AppID not found.";
    public static final String RESULT_MSG_USER_ID_NOT_FOUND = "Active user ID is not found.";
    public static final String RESULT_MSG_PORTAL_ID_NOT_FOUND = "Portal ID is not found.";
    public static final String RESULT_MSG_RECIPIENT_ID_IS_DUPLICATED = "Recipient ID is duplicated.";
    public static final String RESULT_MSG_EMSG_TEMPLATE_NOT_FOUND = "E-message template is not found.";
    public static final String RESULT_MSG_TO_DO_ITEM_TEMPLATE_NOT_FOUND = "To-Do-Item template is not found.";
    public static final String RESULT_MSG_APPLICATION_TEMPLATE_NOT_FOUND = "Application template is not found.";
    public static final String RESULT_MSG_CONTENT_DATA_NOT_MATCHED = "Meta data content do not match with template.";
    public static final String RESULT_MSG_META_DATA_EN_EMPTY = "English meta data content is mandatory.";
    public static final String RESULT_MSG_META_DATA_EN_LENGTH_OVER_LIMIT = "Length of English meta data content is over limit.";
    public static final String RESULT_MSG_META_DATA_TC_LENGTH_OVER_LIMIT = "Length of Traditional Chinese meta data content is over limit.";
    public static final String RESULT_MSG_META_DATA_SC_LENGTH_OVER_LIMIT = "Length of Simplified Chinese meta data content is over limit.";
    public static final String RESULT_MSG_TEMPLATE_SERVICE_PROVIDER_MUST_BE_SAME = "E-message and To-Do-Item and Application Status template must belong to the same service provider.";
    public static final String RESULT_MSG_TRAN_ID_IS_DUPLICATED = "Tran ID is duplicated within the message request.";
    public static final String RESULT_MSG_MESSAGE_PROCESSED_WITH_ERROR = "Individual transaction has problem, please refer to the transaction result code and description";
    public static final String RESULT_MSG_REQUEST_SCHEMA_INVALID = "Schema violated";
    public static final String RESULT_MSG_MERGED_SUBJECT_EN_LENGTH_OVER_LIMIT = "Length of Merged English subject is over limit.";
    public static final String RESULT_MSG_MERGED_SUBJECT_TC_LENGTH_OVER_LIMIT = "Length of Merged Traditional Chinese subject is over limit.";
    public static final String RESULT_MSG_MERGED_SUBJECT_SC_LENGTH_OVER_LIMIT = "Length of Merged Simplified Chinese subject is over limit.";
    public static final String RESULT_MSG_MERGED_CONTENT_EN_LENGTH_OVER_LIMIT = "Length of Merged English content is over limit.";
    public static final String RESULT_MSG_MERGED_CONTENT_TC_LENGTH_OVER_LIMIT = "Length of Merged Traditional Chinese content is over limit.";
    public static final String RESULT_MSG_MERGED_CONTENT_SC_LENGTH_OVER_LIMIT = "Length of Merged Simplified Chinese content is over limit.";
    public static final String RESULT_MSG_PROCESS_DATA_NOT_FOUND = "E-message, To-Do-Item or Bill-Account-Info or Application Status not found in the request.";

    // Obsoleted codes retained for not duplicate use in future codes - BEGIN
    // public static final String RESULT_MSG_BILL_ACCOUNT_INFO_EMPTY = "Bill-Account-Info data is mandatory.";
    // public static final String RESULT_MSG_SENDER_APP_ID_INVALID = "Sender AppID invalid.";
    // public static final String RESULT_MSG_BILL_ACCOUNT_INFO_ACCOUNT_LABEL_EN_LENGTH_OVER_LIMIT = "Bill-Account-Info : Length of Account Label English is over limit. Account Number : {0}";
    // public static final String RESULT_MSG_BILL_ACCOUNT_INFO_ACCOUNT_LABEL_TC_LENGTH_OVER_LIMIT = "Bill-Account-Info : Length of Account Label Traditional Chinese is over limit. Account Number : {0}";
    // public static final String RESULT_MSG_BILL_ACCOUNT_INFO_ACCOUNT_LABEL_SC_LENGTH_OVER_LIMIT = "Bill-Account-Info : Length of Account Label Simplified Chinese is over limit. Account Number : {0}";
    // public static final String RESULT_MSG_BILL_ACCOUNT_INFO_ACCOUNT_NO_LENGTH_OVER_LIMIT = "Bill-Account-Info : Length of Account Number is over limit. Account Number : {0}";
    // public static final String RESULT_MSG_BILL_ACCOUNT_INFO_BILL_DESC_EN_LENGTH_OVER_LIMIT = "Bill-Account-Info : Length of Bill Description English is over limit. Account Number : {0}";
    // public static final String RESULT_MSG_BILL_ACCOUNT_INFO_BILL_DESC_TC_LENGTH_OVER_LIMIT = "Bill-Account-Info : Length of Bill Description Traditional Chinese is over limit. Account Number : {0}";
    // public static final String RESULT_MSG_BILL_ACCOUNT_INFO_BILL_DESC_SC_LENGTH_OVER_LIMIT = "Bill-Account-Info : Length of Bill Description Simplified Chinese is over limit. Account Number : {0}";
    // public static final String RESULT_MSG_BILL_ACCOUNT_INFO_BILL_TYPE_LENGTH_OVER_LIMIT = "Bill-Account-Info : Length of Bill Type is over limit. Account Number : {0}";
    // public static final String RESULT_MSG_BILL_ACCOUNT_INFO_AMOUNT_INVALID = "Bill-Account-Info : Amount invalid. Account Number : {0}";
    // public static final String RESULT_MSG_BILL_ACCOUNT_INFO_AMOUNT_SIGN_LENGTH_OVER_LIMIT = "Bill-Account-Info : Length of Amount Sign is over limit. Account Number : {0}";
    // public static final String RESULT_MSG_BILL_ACCOUNT_RECORD_NOT_FOUND = "Bill-Account-Info record not found. Account Number : {0}";
    // public static final String RESULT_MSG_PAYMENT_TRANSACTION_MERCHANT_NAME_EN_LENGTH_OVER_LIMIT = "Payment-Transaction : Length of Merchant Name English is over limit. Account Number : {0}, Transaction-Reference-Number : {1}";
    // public static final String RESULT_MSG_PAYMENT_TRANSACTION_MERCHANT_NAME_TC_LENGTH_OVER_LIMIT = "Payment-Transaction : Length of Merchant Name Traditional Chinese is over limit. Account Number : {0}, Transaction-Reference-Number : {1}";
    // public static final String RESULT_MSG_PAYMENT_TRANSACTION_MERCHANT_NAME_SC_LENGTH_OVER_LIMIT = "Payment-Transaction : Length of Merchant Name Simplified Chinese is over limit. Account Number : {0}, Transaction-Reference-Number : {1}";
    // public static final String RESULT_MSG_PAYMENT_TRANSACTION_PAID_AMOUNT_INVLID = "Payment-Transaction : Paid Amount invalid. Account Number : {0}, Transaction-Reference-Number : {1}";
    // public static final String RESULT_MSG_PAYMENT_TRANSACTION_PAYMENT_METHOD_CD_LENGTH_OVER_LIMIT = "Payment-Transaction : Length of Payment Method CD is over limit. Account Number : {0}, Transaction-Reference-Number : {1}";
    // public static final String RESULT_MSG_PAYMENT_TRANSACTION_PAYMENT_TRAN_REF_NUMBER_LENGTH_OVER_LIMIT = "Payment-Transaction : Length of Payment Transaction Reference Number is over limit. Account Number : {0}, Transaction-Reference-Number : {1}";
    // public static final String RESULT_MSG_PAYMENT_TRANSACTION_RECORD_NOT_FOUND = "Payment-Transaction record not found. Account Number : {0}, Transaction-Reference-Number : {1}";
    // public static final String RESULT_MSG_BILL_ACCOUNT_BATCH_CONCURRENT_PROCESSING = "Bill-Account batch concurrent processing.";
    // public static final String RESULT_MSG_BILL_ACCOUNT_INFO_ACCOUNT_NO_CONCURRENT_PROCESSING = "Bill-Account-Info account number concurrent processing";
    // public static final String RESULT_MSG_BILL_ACCOUNT_INFO_TRAN_ID_EMPTY = "Bill-Account-Info transaction Id is empty";
    // public static final String RESULT_MSG_BILL_ACCOUNT_INFO_TRAN_ID_NOT_UP_TO_DATE = "Bill-Account-Info transaction-Id may not be up-to-date";
    // public static final String RESULT_MSG_BILL_ACCOUNT_INFO_ACCOUNT_LABEL_EN_EMPTY = "Bill-Account-Info : Account Label English is empty. Account Number : {0}";
    // public static final String RESULT_MSG_BILL_ACCOUNT_INFO_ACCOUNT_LABEL_TC_EMPTY = "Bill-Account-Info : Account Label Traditional Chinese is empty. Account Number : {0}";
    // public static final String RESULT_MSG_BILL_ACCOUNT_INFO_ACCOUNT_LABEL_SC_EMPTY = "Bill-Account-Info : Account Label Simplified Chinese is empty. Account Number : {0}";
    // public static final String RESULT_MSG_BILL_ACCOUNT_INFO_ACCOUNT_NO_EMPTY = "Bill-Account-Info : Account Number is empty. Account Number : {0}";
    // public static final String RESULT_MSG_BILL_ACCOUNT_INFO_BILL_DESC_EN_EMPTY = "Bill-Account-Info : Bill Description English is empty. Account Number : {0}";
    // public static final String RESULT_MSG_BILL_ACCOUNT_INFO_BILL_DESC_TC_EMPTY = "Bill-Account-Info : Bill Description Traditional Chinese is empty. Account Number : {0}";
    // public static final String RESULT_MSG_BILL_ACCOUNT_INFO_BILL_DESC_SC_EMPTY = "Bill-Account-Info : Bill Description Simplified Chinese is empty. Account Number : {0}";
    // public static final String RESULT_MSG_BILL_ACCOUNT_INFO_BILL_TYPE_EMPTY = "Bill-Account-Info : Bill Type is empty. Account Number : {0}";
    // public static final String RESULT_MSG_PAYMENT_TRANSACTION_MERCHANT_NAME_EN_EMPTY = "Payment-Transaction : Merchant Name English is empty. Account Number : {0}, Transaction-Reference-Number : {1}";
    // public static final String RESULT_MSG_PAYMENT_TRANSACTION_MERCHANT_NAME_TC_EMPTY = "Payment-Transaction : Merchant Name Traditional Chinese is empty. Account Number : {0}, Transaction-Reference-Number : {1}";
    // public static final String RESULT_MSG_PAYMENT_TRANSACTION_MERCHANT_NAME_SC_EMPTY = "Payment-Transaction : Merchant Name Simplified Chinese is empty. Account Number : {0}, Transaction-Reference-Number : {1}";
    // public static final String RESULT_MSG_PAYMENT_TRANSACTION_PAYMENT_METHOD_CD_EMPTY = "Payment-Transaction : Payment Method CD is empty. Account Number : {0}, Transaction-Reference-Number : {1}";
    // public static final String RESULT_MSG_PAYMENT_TRANSACTION_PAYMENT_TRAN_REF_NUMBER_EMPTY = "Payment-Transaction : Payment Transaction Reference Number is empty. Account Number : {0}, Transaction-Reference-Number : {1}";
    // public static final String RESULT_MSG_BILL_ACCOUNT_INFO_ISSUE_DATE_EMPTY = "Bill-Account-Info : Issue date is empty. Account Number : {0}";
    // public static final String RESULT_MSG_BILL_ACCOUNT_INFO_ONLINE_PAYMENT_EMPTY = "Bill-Account-Info : Online Payment Indicator is empty. Account Number : {0}";
    // public static final String RESULT_MSG_BILL_ACCOUNT_INFO_REMARK_LENGTH_OVER_LIMIT = "Bill-Account-Info : Length of Remark is over limit. Account Number : {0}";
    // public static final String RESULT_MSG_PAYMENT_TRANSACTION_PAYMENT_TRAN_DATE_EMPTY = "Payment-Transaction : Payment Transaction Date is empty. Account Number : {0}, Transaction-Reference-Number : {1}";
    // public static final String RESULT_MSG_PAYMENT_TRANSACTION_PAYMENT_METHOD_CANNOT_ADD_MYGOVHK_RECORD = "Payment-Transaction : Cannot add Payment Method CD equels to '01' record. Account Number : {0}, Transaction-Reference-Number : {1}";
    // public static final String RESULT_MSG_PAYMENT_TRANSACTION_PAYMENT_METHOD_CANNOT_DEL_MYGOVHK_RECORD = "Payment-Transaction : Cannot delete Payment Method CD equels to '01' record. Account Number : {0}, Transaction-Reference-Number : {1}";
    // public static final String RESULT_MSG_PAYMENT_TRANSACTION_PAYMENT_METHOD_CANNOT_ADD_MYGOVHK_ECHQ_RECORD = "Payment-Transaction : Cannot add Payment Method CD equels to '20' record. Account Number : {0}, Transaction-Reference-Number : {1}";
    // public static final String RESULT_MSG_PAYMENT_TRANSACTION_PAYMENT_METHOD_CANNOT_DEL_MYGOVHK_ECHQ_RECORD = "Payment-Transaction : Cannot delete Payment Method CD equels to '20' record. Account Number : {0}, Transaction-Reference-Number : {1}";
    // public static final String RESULT_MSG_BILL_ACCOUNT_INFO_BAL_AS_AT_DATE_EMPTY = "Bill-Account-Info : Balance as at date is empty. Account Number : {0}";
    // public static final String RESULT_MSG_BILL_ACCOUNT_INFO_BILLAMOUNT_INVALID = "Bill-Account-Info : Bill-amount invalid. Account Number : {0}";
    // public static final String RESULT_MSG_MERGED_EMAIL_SUBJECT_EN_LENGTH_OVER_LIMIT = "Length of Merged E-mail English subject is over limit.";
    // public static final String RESULT_MSG_MERGED_EMAIL_SUBJECT_TC_LENGTH_OVER_LIMIT = "Length of Merged E-mail Traditional Chinese subject is over limit.";
    // public static final String RESULT_MSG_MERGED_EMAIL_SUBJECT_SC_LENGTH_OVER_LIMIT = "Length of Merged E-mail Simplified Chinese subject is over limit.";
    // public static final String RESULT_MSG_MERGED_EMAIL_CONTENT_EN_LENGTH_OVER_LIMIT = "Length of Merged E-mail English content is over limit.";
    // public static final String RESULT_MSG_MERGED_EMAIL_CONTENT_TC_LENGTH_OVER_LIMIT = "Length of Merged E-mail Traditional Chinese content is over limit.";
    // public static final String RESULT_MSG_MERGED_EMAIL_CONTENT_SC_LENGTH_OVER_LIMIT = "Length of Merged E-mail Simplified Chinese content is over limit.";
    // public static final String RESULT_MSG_MERGED_MOBILE_SUBJECT_EN_LENGTH_OVER_LIMIT = "Length of Merged Mobile Message English subject is over limit.";
    // public static final String RESULT_MSG_MERGED_MOBILE_SUBJECT_TC_LENGTH_OVER_LIMIT = "Length of Merged Mobile Message Traditional Chinese subject is over limit.";
    // public static final String RESULT_MSG_MERGED_MOBILE_SUBJECT_SC_LENGTH_OVER_LIMIT = "Length of Merged Mobile Message Simplified Chinese subject is over limit.";
    // public static final String RESULT_MSG_MERGED_MOBILE_CONTENT_EN_LENGTH_OVER_LIMIT = "Length of Merged Mobile Message English content is over limit.";
    // public static final String RESULT_MSG_MERGED_MOBILE_CONTENT_TC_LENGTH_OVER_LIMIT = "Length of Merged Mobile Message Traditional Chinese content is over limit.";
    // public static final String RESULT_MSG_MERGED_MOBILE_CONTENT_SC_LENGTH_OVER_LIMIT = "Length of Merged Mobile Message Simplified Chinese content is over limit.";
    // Obsoleted codes retained for not duplicate use in future codes - END

    public static final String RESULT_MSG_RECIPIENT_IS_SUSPENDED = "Recipient is suspended.";
    public static final String RESULT_MSG_RECIPIENT_IS_INVALID = "Recipient is invalid.";
    public static final String RESULT_MSG_RECIPIENT_IS_DEREGISTERED = "Recipient is deregistered.";
    public static final String RESULT_MSG_MERGED_IAS_MSG_SUBJECT_EN_LENGTH_OVER_LIMIT = "Length of Merged iAM Smart Message English subject is over limit.";
    public static final String RESULT_MSG_MERGED_IAS_MSG_SUBJECT_TC_LENGTH_OVER_LIMIT = "Length of Merged iAM Smart Message Traditional Chinese subject is over limit.";
    public static final String RESULT_MSG_MERGED_IAS_MSG_SUBJECT_SC_LENGTH_OVER_LIMIT = "Length of Merged iAM Smart Message Simplified Chinese subject is over limit.";
    public static final String RESULT_MSG_MERGED_IAS_MSG_CONTENT_EN_LENGTH_OVER_LIMIT = "Length of Merged iAM Smart Message English content is over limit.";
    public static final String RESULT_MSG_MERGED_IAS_MSG_CONTENT_TC_LENGTH_OVER_LIMIT = "Length of Merged iAM Smart Message Traditional Chinese content is over limit.";
    public static final String RESULT_MSG_MERGED_IAS_MSG_CONTENT_SC_LENGTH_OVER_LIMIT = "Length of Merged iAM Smart Message Simplified Chinese content is over limit.";
    public static final String RESULT_MSG_TRAN_SUCCESS = "Transaction processed successfully";

    public static final String RESULT_MSG_IDP_ID_NOT_FOUND = "Identity Provider ID is not found.";
    public static final String RESULT_MSG_RECIPIENT_ID_NOT_FOUND = "Recipient ID is not found.";
    public static final String RESULT_MSG_RECIPIENT_IS_DELETED = "Recipient is deleted.";
    public static final String RESULT_MSG_TRAN_ID_LENGTH_OVER_LIMIT = "Transction ID length is over limit.";
    public static final String RESULT_MSG_TO_DO_ITEM_ACTION_NOT_VALID = "To-Do-Item action is invalid.";
    public static final String RESULT_MSG_CORRELATED_TRAN_ID_NOT_FOUND = "Correlated transaction ID is not found.";
    public static final String RESULT_MSG_USER_REJECT_MSG = "User reject to receive message.";
    public static final String RESULT_MSG_ITEM_DATE_IS_NOT_VALID = "To-Do-Item date is not valid.";
    public static final String RESULT_MSG_ITEM_DATE_NOT_FOUND = "To-Do-Item date must be entered for To-Do-Item.";
    public static final String RESULT_MSG_USER_FAIL_TO_RECEIVE_EMSG = "User failed to receive E-message.";
    public static final String RESULT_MSG_TO_DO_ITEM_ALREADY_MARK_DELETE = "To-Do-Item is already mark deleted";
    public static final String RESULT_MSG_TO_DO_ITEM_ALREADY_MARK_COMPLETE = "To-Do-Item is already mark completed";

    public static final String RESULT_MSG_APPLICATION_ALREADY_MARK_DELETE = "Application is already mark deleted.";
    public static final String RESULT_MSG_MERGED_IAS_APPLICATION_TITLE_EN_LENGTH_OVER_LIMIT = "Length of Merged iAM Smart Application English title is over limit.";
    public static final String RESULT_MSG_MERGED_IAS_APPLICATION_TITLE_TC_LENGTH_OVER_LIMIT = "Length of Merged iAM Smart Application Traditional Chinese title is over limit.";
    public static final String RESULT_MSG_MERGED_IAS_APPLICATION_TITLE_SC_LENGTH_OVER_LIMIT = "Length of Merged iAM Smart Application Simplified Chinese title is over limit.";
    public static final String RESULT_MSG_MERGED_IAS_APPLICATION_CONTENT_EN_LENGTH_OVER_LIMIT = "Length of Merged iAM Smart Application English content is over limit.";
    public static final String RESULT_MSG_MERGED_IAS_APPLICATION_CONTENT_TC_LENGTH_OVER_LIMIT = "Length of Merged iAM Smart Application Traditional Chinese content is over limit.";
    public static final String RESULT_MSG_MERGED_IAS_APPLICATION_CONTENT_SC_LENGTH_OVER_LIMIT = "Length of Merged iAM Smart Application Simplified Chinese content is over limit.";
    public static final String RESULT_MSG_APPLICATION_STATUS_NOT_VALID = "Application status code is not valid.";
    public static final String RESULT_MSG_RECIPIENT_ID_TYPE_NOT_VALID = "Recipient ID Type is not valid.";
    public static final String RESULT_MSG_APP_STATUS_UPDATE_DATE_IS_NOT_VALID = "Application status update date is not valid.";
    public static final String RESULT_MSG_EMSG_ACTION_IS_NOT_VALID = "E-Message action is invalid.";
    public static final String RESULT_MSG_MERGED_IAS_TO_DO_ITEM_SUBJECT_EN_LENGTH_OVER_LIMIT = "Length of Merged iAM Smart To-Do-Item English subject is over limit.";
    public static final String RESULT_MSG_MERGED_IAS_TO_DO_ITEM_SUBJECT_TC_LENGTH_OVER_LIMIT = "Length of Merged iAM Smart To-Do-Item Traditional Chinese subject is over limit.";
    public static final String RESULT_MSG_MERGED_IAS_TO_DO_ITEM_SUBJECT_SC_LENGTH_OVER_LIMIT = "Length of Merged iAM Smart To-Do-Item Simplified Chinese subject is over limit.";
    public static final String RESULT_MSG_MERGED_IAS_TO_DO_ITEM_CONTENT_EN_LENGTH_OVER_LIMIT = "Length of Merged iAM Smart To-Do-Item English content is over limit.";
    public static final String RESULT_MSG_MERGED_IAS_TO_DO_ITEM_CONTENT_TC_LENGTH_OVER_LIMIT = "Length of Merged iAM Smart To-Do-Item Traditional Chinese content is over limit.";
    public static final String RESULT_MSG_MERGED_IAS_TO_DO_ITEM_CONTENT_SC_LENGTH_OVER_LIMIT = "Length of Merged iAM Smart To-Do-Item Simplified Chinese content is over limit.";
    public static final String RESULT_MSG_RETRIEVE_CMC_TEMPLATE_0000 = "Success";
    public static final String RESULT_MSG_RETRIEVE_CMC_TEMPLATE_0001 = "CMC Template Not Found";
    public static final String RESULT_MSG_RETRIEVE_CMC_TEMPLATE_9999 = "General exception";
    public static final String RESULT_MSG_APPLICATION_ACTION_IS_NOT_VALID = "Application action is invalid.";

    public static final String RESULT_MSG_MAINTAIN_TEMPLATE_0000 = "Success";
    public static final String RESULT_MSG_MAINTAIN_TEMPLATE_0001 = "CMC Template Not Found";
    public static final String RESULT_MSG_MAINTAIN_TEMPLATE_9999 = "General exception";
}
