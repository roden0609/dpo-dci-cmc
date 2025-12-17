package hk.gov.cmc.common;

public class Constants {
    // Web Services Result Code
    // Error code for MaintainMessageService
    // General error code "Success"
    public static final String RESULT_CD_SUCCESS = "0000";
    // General error code "Sender AppID Not Found"
    public static final String RESULT_CD_SENDER_APPID_NOT_FOUND = "0001";
    // General error code "Portal ID Not Found"
    public static final String RESULT_CD_PORTAL_ID_NOT_FOUND = "0002";
    // General error code "Recipient ID Is duplicated"
    public static final String RESULT_CD_RECIPIENT_ID_IS_DUPLICATED = "0003";
    // General error code "EMessage Template not found"
    public static final String RESULT_CD_EMSG_TEMPLATE_NOT_FOUND = "0004";
    // General error code "ToDO item template not found"
    public static final String RESULT_CD_TO_DO_ITEM_TEMPLATE_NOT_FOUND = "0005";
    // General error code "Content Data not matched"
    public static final String RESULT_CD_CONTENT_DATA_NOT_MATCHED = "0006";
    // General error code "English meta data is empty"
    public static final String RESULT_CD_META_DATA_EN_EMPTY = "0007";
    // General error code "Length of english meta data is over limit"
    public static final String RESULT_CD_META_DATA_EN_LENGTH_OVER_LIMIT = "0008";
    // General error code "Length of traditional chinese meta data is over limit"
    public static final String RESULT_CD_META_DATA_TC_LENGTH_OVER_LIMIT = "0009";
    // General error code "Length of simplfied chinese meta data is over limit"
    public static final String RESULT_CD_META_DATA_SC_LENGTH_OVER_LIMIT = "0010";
    // General error code "Template service provide must be the same"
    public static final String RESULT_CD_TEMPLATE_SERVICE_PROVIDER_MUST_BE_SAME = "0011";
    // General error code "TRAN ID is duplicated"
    public static final String RESULT_CD_TRAN_ID_IS_DUPLICATED = "0012";
    public static final String RESULT_CD_MERGED_SUBJECT_EN_LENGTH_OVER_LIMIT = "0013";
    public static final String RESULT_CD_MERGED_SUBJECT_TC_LENGTH_OVER_LIMIT = "0014";
    public static final String RESULT_CD_MERGED_SUBJECT_SC_LENGTH_OVER_LIMIT = "0015";
    public static final String RESULT_CD_MERGED_CONTENT_EN_LENGTH_OVER_LIMIT = "0016";
    public static final String RESULT_CD_MERGED_CONTENT_TC_LENGTH_OVER_LIMIT = "0017";
    public static final String RESULT_CD_MERGED_CONTENT_SC_LENGTH_OVER_LIMIT = "0018";
    // General error code "Process data not found"
    public static final String RESULT_CD_PROCESS_DATA_NOT_FOUND = "0019";
    // General error code "Bill-Account-Info data is empty"
    public static final String RESULT_CD_BILL_ACCOUNT_INFO_EMPTY = "0020";
    // General error code "Sender AppID invalid"
    public static final String RESULT_CD_SENDER_APP_ID_INVALID = "0021";

    // General error code "Bill-Account-Info account label en length over limit"
    public static final String RESULT_CD_BILL_ACCOUNT_INFO_ACCOUNT_LABEL_EN_LENGTH_OVER_LIMIT = "0022";
    // General error code "Bill-Account-Info account label tc length over limit"
    public static final String RESULT_CD_BILL_ACCOUNT_INFO_ACCOUNT_LABEL_TC_LENGTH_OVER_LIMIT = "0023";
    // General error code "Bill-Account-Info account label sc length over limit"
    public static final String RESULT_CD_BILL_ACCOUNT_INFO_ACCOUNT_LABEL_SC_LENGTH_OVER_LIMIT = "0024";
    // General error code "Bill-Account-Info account number length over limit"
    public static final String RESULT_CD_BILL_ACCOUNT_INFO_ACCOUNT_NO_LENGTH_OVER_LIMIT = "0025";
    // General error code "Bill-Account-Info bill description en length over limit"
    public static final String RESULT_CD_BILL_ACCOUNT_INFO_BILL_DESC_EN_LENGTH_OVER_LIMIT = "0026";
    // General error code "Bill-Account-Info bill description tc length over limit"
    public static final String RESULT_CD_BILL_ACCOUNT_INFO_BILL_DESC_TC_LENGTH_OVER_LIMIT = "0027";
    // General error code "Bill-Account-Info bill description sc length over limit"
    public static final String RESULT_CD_BILL_ACCOUNT_INFO_BILL_DESC_SC_LENGTH_OVER_LIMIT = "0028";
    // General error code "Bill-Account-Info bill type length over limit"
    public static final String RESULT_CD_BILL_ACCOUNT_INFO_BILL_TYPE_LENGTH_OVER_LIMIT = "0032";
    // General error code "Bill-Account-Info amount invalid"
    public static final String RESULT_CD_BILL_ACCOUNT_INFO_AMOUNT_INVALID = "0033";
    // General error code "Bill-Account-Info amount sign length over limit"
    public static final String RESULT_CD_BILL_ACCOUNT_INFO_AMOUNT_SIGN_LENGTH_OVER_LIMIT = "0034";
    // General error code "Bill-Account record not found"
    public static final String RESULT_CD_BILL_ACCOUNT_RECORD_NOT_FOUND = "0035";
    // General error code "Payment-Transaction merchant name en length over limit"
    public static final String RESULT_CD_PAYMENT_TRANSACTION_MERCHANT_NAME_EN_LENGTH_OVER_LIMIT = "0036";
    // General error code "Payment-Transaction merchant name tc length over limit"
    public static final String RESULT_CD_PAYMENT_TRANSACTION_MERCHANT_NAME_TC_LENGTH_OVER_LIMIT = "0037";
    // General error code "Payment-Transaction merchant name sc length over limit"
    public static final String RESULT_CD_PAYMENT_TRANSACTION_MERCHANT_NAME_SC_LENGTH_OVER_LIMIT = "0038";
    // General error code "Payment-Transaction paid amount invalid"
    public static final String RESULT_CD_PAYMENT_TRANSACTION_PAID_AMOUNT_INVLID = "0039";
    // General error code "Payment-Transaction payment method cd length over limit"
    public static final String RESULT_CD_PAYMENT_TRANSACTION_PAYMENT_METHOD_CD_LENGTH_OVER_LIMIT = "0040";
    // General error code "Payment-Transaction payment transaction reference number length over limit"
    public static final String RESULT_CD_PAYMENT_TRANSACTION_PAYMENT_TRAN_REF_NUMBER_LENGTH_OVER_LIMIT = "0044";
    // General error code "Payment-Transaction record not found"
    public static final String RESULT_CD_PAYMENT_TRANSACTION_RECORD_NOT_FOUND = "0045";
    // General error code "Bill-Account batch concurrent processing"
    public static final String RESULT_CD_BILL_ACCOUNT_BATCH_CONCURRENT_PROCESSING = "0046";
    // General error code "Bill-Account-Info account number concurrent processing"
    public static final String RESULT_CD_BILL_ACCOUNT_INFO_ACCOUNT_NO_CONCURRENT_PROCESSING = "0047";
    // General error code "Bill-Account-Info transaction Id is empty"
    public static final String RESULT_CD_BILL_ACCOUNT_INFO_TRAN_ID_EMPTY = "0048";
    // General error code "Bill-Account-Info transaction-Id may not be up-to-date"
    public static final String RESULT_CD_BILL_ACCOUNT_INFO_TRAN_ID_NOT_UP_TO_DATE = "0049";
    // General error code "Bill-Account-Info account label en is empty"
    public static final String RESULT_CD_BILL_ACCOUNT_INFO_ACCOUNT_LABEL_EN_EMPTY = "0050";
    // General error code "Bill-Account-Info account label tc is empty"
    public static final String RESULT_CD_BILL_ACCOUNT_INFO_ACCOUNT_LABEL_TC_EMPTY = "0051";
    // General error code "Bill-Account-Info account label sc is empty"
    public static final String RESULT_CD_BILL_ACCOUNT_INFO_ACCOUNT_LABEL_SC_EMPTY = "0052";
    // General error code "Bill-Account-Info account number is empty"
    public static final String RESULT_CD_BILL_ACCOUNT_INFO_ACCOUNT_NO_EMPTY = "0053";
    // General error code "Bill-Account-Info bill description en is empty"
    public static final String RESULT_CD_BILL_ACCOUNT_INFO_BILL_DESC_EN_EMPTY = "0054";
    // General error code "Bill-Account-Info bill description tc is empty"
    public static final String RESULT_CD_BILL_ACCOUNT_INFO_BILL_DESC_TC_EMPTY = "0055";
    // General error code "Bill-Account-Info bill description sc is empty"
    public static final String RESULT_CD_BILL_ACCOUNT_INFO_BILL_DESC_SC_EMPTY = "0056";
    // General error code "Bill-Account-Info bill type is empty"
    public static final String RESULT_CD_BILL_ACCOUNT_INFO_BILL_TYPE_EMPTY = "0057";
    // General error code "Payment-Transaction merchant name en is empty"
    public static final String RESULT_CD_PAYMENT_TRANSACTION_MERCHANT_NAME_EN_EMPTY = "0058";
    // General error code "Payment-Transaction merchant name tc is empty"
    public static final String RESULT_CD_PAYMENT_TRANSACTION_MERCHANT_NAME_TC_EMPTY = "0059";
    // General error code "Payment-Transaction merchant name sc is empty"
    public static final String RESULT_CD_PAYMENT_TRANSACTION_MERCHANT_NAME_SC_EMPTY = "0060";
    // General error code "Payment-Transaction payment method cd is empty"
    public static final String RESULT_CD_PAYMENT_TRANSACTION_PAYMENT_METHOD_CD_EMPTY = "0061";
    // General error code "Payment-Transaction payment transaction reference number is empty"
    public static final String RESULT_CD_PAYMENT_TRANSACTION_PAYMENT_TRAN_REF_NUMBER_EMPTY = "0062";
    // General error code "Bill-Account-Info issue date is empty"
    public static final String RESULT_CD_BILL_ACCOUNT_INFO_ISSUE_DATE_EMPTY = "0063";
    // General error code "Bill-Account-Info online payment is empty"
    public static final String RESULT_CD_BILL_ACCOUNT_INFO_ONLINE_PAYMENT_EMPTY = "0064";
    // General error code "Bill-Account-Info remark length over limit"
    public static final String RESULT_CD_BILL_ACCOUNT_INFO_REMARK_LENGTH_OVER_LIMIT = "0065";
    // General error code "Payment-Transaction payment transaction date is empty"
    public static final String RESULT_CD_PAYMENT_TRANSACTION_PAYMENT_TRAN_DATE_EMPTY = "0066";

    // CMC-RA16-17 - Revise the e-Message request from RS such that they can voided those payment transaction -- BEGIN
    // General error code "Payment-Transaction payment transaction cannot add Payment Method CD '01' record"
    public static final String RESULT_CD_PAYMENT_TRANSACTION_PAYMENT_METHOD_CD_CANNOT_ADD_MYGOVHK_RECORD = "0067";
    // General error code "Payment-Transaction payment transaction cannot delete Payment Method CD '01' record"
    public static final String RESULT_CD_PAYMENT_TRANSACTION_PAYMENT_METHOD_CD_CANNOT_DEL_MYGOVHK_RECORD = "0068";
    // CMC-RA16-17 - Revise the e-Message request from RS such that they can voided those payment transaction -- END

    // CMC-RA16-34 - Support partial payment and allow multiple bills in single payment -- BEGIN
    // General error code "Bill-Account-Info balance as at date is empty"
    public static final String RESULT_CD_BILL_ACCOUNT_INFO_BAL_AS_AT_DATE_EMPTY = "0069";
    // General error code "Bill-Account-Info bill-amount invalid"
    public static final String RESULT_CD_BILL_ACCOUNT_INFO_BILLAMOUNT_INVALID = "0070";
    // CMC-RA16-34 - Support partial payment and allow multiple bills in single payment -- END

    // CMC-2024-011: Enhance Maintain Message Module To Support iAM Smart Application Status - BEGIN
    public static final String RESULT_CD_APPLICATION_TEMPLATE_NOT_FOUND = "0071";
    // CMC-2024-011: Enhance Maintain Message Module To Support iAM Smart Application Status - END

    // Individual error code "Transaction success"
    public static final String RESULT_CD_TRAN_SUCCESS = "3000";
    // Individual error code "IDP ID Not found"
    public static final String RESULT_CD_IDP_ID_NOT_FOUND = "3001";
    // Individual error code "Recipient ID Not found"
    public static final String RESULT_CD_RECIPIENT_ID_NOT_FOUND = "3002";
    // Individual error code "Recipient is deleted"
    public static final String RESULT_CD_RECIPIENT_IS_DELETED = "3003";
    // Individual error code "Tran ID Length is over the limit"
    public static final String RESULT_CD_TRAN_ID_LENGTH_OVER_LIMIT = "3004";
    // Individual error code "TODO Item action is not valid"
    public static final String RESULT_CD_TO_DO_ITEM_ACTION_NOT_VALID = "3005";
    // Individual error code "Correlated TranID not found"
    public static final String RESULT_CD_CORRELATED_TRAN_ID_NOT_FOUND = "3006";
    // Individual error code "User reject message"
    public static final String RESULT_CD_USER_REJECT_MSG = "3007";
    // Individual error code "Item data is not valid"
    public static final String RESULT_CD_ITEM_DATE_IS_NOT_VALID = "3008";
    // Individual error code "Item data not found"
    public static final String RESULT_CD_ITEM_DATE_NOT_FOUND = "3009";
    // Individual error code "User fail to receive message"
    public static final String RESULT_CD_USER_FAIL_TO_RECEIVE_EMSG = "3010";
    // Individual error code "ToDO item is already marked as delete"
    public static final String RESULT_CD_TO_DO_ITEM_ALREADY_MARK_DELETE = "3011";
    // Individual error code "ToDO item is already marked as complete"
    public static final String RESULT_CD_TO_DO_ITEM_ALREADY_MARK_COMPLETE = "3012";

    // Individual error code "Message Processed with error"
    public static final String RESULT_CD_MESSAGE_PROCESSED_WITH_ERROR = "9997";
    // Individual error code "Request schema is not valid"
    public static final String RESULT_CD_REQUEST_SCHEMA_INVALID = "9998";
    // Individual error code "General Error"
    public static final String RESULT_CD_GENERAL_ERROR = "9999";
    // CMC-BAT-09 -- BEGIN
    // Email error code
    public static final String RESULT_CD_MERGED_EMAIL_SUBJECT_EN_LENGTH_OVER_LIMIT = "3013";
    public static final String RESULT_CD_MERGED_EMAIL_SUBJECT_TC_LENGTH_OVER_LIMIT = "3014";
    public static final String RESULT_CD_MERGED_EMAIL_SUBJECT_SC_LENGTH_OVER_LIMIT = "3015";
    public static final String RESULT_CD_MERGED_EMAIL_CONTENT_EN_LENGTH_OVER_LIMIT = "3016";
    public static final String RESULT_CD_MERGED_EMAIL_CONTENT_TC_LENGTH_OVER_LIMIT = "3017";
    public static final String RESULT_CD_MERGED_EMAIL_CONTENT_SC_LENGTH_OVER_LIMIT = "3018";
    // CMC-BAT-09 -- END
    // mobile application modification -- START
    public static final String RESULT_CD_MERGED_MOBILE_SUBJECT_EN_LENGTH_OVER_LIMIT = "3019";
    public static final String RESULT_CD_MERGED_MOBILE_SUBJECT_TC_LENGTH_OVER_LIMIT = "3020";
    public static final String RESULT_CD_MERGED_MOBILE_SUBJECT_SC_LENGTH_OVER_LIMIT = "3021";
    public static final String RESULT_CD_MERGED_MOBILE_CONTENT_EN_LENGTH_OVER_LIMIT = "3022";
    public static final String RESULT_CD_MERGED_MOBILE_CONTENT_TC_LENGTH_OVER_LIMIT = "3023";
    public static final String RESULT_CD_MERGED_MOBILE_CONTENT_SC_LENGTH_OVER_LIMIT = "3024";
    // mobile application modification -- END

    // MyGov6-C1-001: Enhance CMC client for supporting iAM Smart Message -- START
    // Maintain message error code "Recipient is suspended."
    public static final String RESULT_CD_RECIPIENT_IS_SUSPENDED = "3025";
    // Maintain message error code "Recipient is invalid."
    public static final String RESULT_CD_RECIPIENT_IS_INVALID = "3026";
    // Maintain message error code "Recipient is deregistered."
    public static final String RESULT_CD_RECIPIENT_IS_DEREGISTERED = "3027";
    // Maintain message error code "Length of Merged iAM Smart Message English subject is over limit."
    public static final String RESULT_CD_MERGED_IAS_MSG_SUBJECT_EN_LENGTH_OVER_LIMIT = "3028";
    // Maintain message error code "Length of Merged iAM Smart Message Traditional Chinese subject is over limit."
    public static final String RESULT_CD_MERGED_IAS_MSG_SUBJECT_TC_LENGTH_OVER_LIMIT = "3029";
    // Maintain message error code "Length of Merged iAM Smart Message Simplified Chinese subject is over limit."
    public static final String RESULT_CD_MERGED_IAS_MSG_SUBJECT_SC_LENGTH_OVER_LIMIT = "3030";
    // Maintain message error code "Length of Merged iAM Smart Message English content is over limit."
    public static final String RESULT_CD_MERGED_IAS_MSG_CONTENT_EN_LENGTH_OVER_LIMIT = "3031";
    // Maintain message error code "Length of Merged iAM Smart Message Traditional Chinese content is over limit."
    public static final String RESULT_CD_MERGED_IAS_MSG_CONTENT_TC_LENGTH_OVER_LIMIT = "3032";
    // Maintain message error code "Length of Merged iAM Smart Message Simplified Chinese content is over limit."
    public static final String RESULT_CD_MERGED_IAS_MSG_CONTENT_SC_LENGTH_OVER_LIMIT = "3033";
    // MyGov6-C1-001: Enhance CMC client for supporting iAM Smart Message -- END

    // CMC-2024-011: Enhance Maintain Message Module To Support iAM Smart Application Status - BEGIN
    public static final String RESULT_CD_APPLICATION_ALREADY_MARK_DELETE = "3034";
    // public static final String RESULT_CD_APP_REF_NUM_NOT_FOUND = "3035";
    public static final String RESULT_CD_APPLICATION_STATUS_NOT_VALID = "3036";
    // CMC-2025-017: Block iAM Smart Message receive HKID as recipientId - BEGIN
    public static final String RESULT_CD_RECIPIENT_ID_TYPE_NOT_VALID = "3037";
    // CMC-2025-017: Block iAM Smart Message receive HKID as recipientId - END
    public static final String RESULT_CD_APP_STATUS_UPDATE_DATE_IS_NOT_VALID = "3038";
    public static final String RESULT_CD_EMSG_ACTION_IS_NOT_VALID = "3039";
    // CMC-2024-011: Enhance Maintain Message Module To Support iAM Smart Application Status - END

    // CMC-2025-016: Restrict iAM Smart To Do Item and Application subject max length (limited EN/TC/SC to 70/30/30 characters) - BEGIN
    public static final String RESULT_CD_MERGED_IAS_TO_DO_ITEM_SUBJECT_EN_LENGTH_OVER_LIMIT = "3040";
    public static final String RESULT_CD_MERGED_IAS_TO_DO_ITEM_SUBJECT_TC_LENGTH_OVER_LIMIT = "3041";
    public static final String RESULT_CD_MERGED_IAS_TO_DO_ITEM_SUBJECT_SC_LENGTH_OVER_LIMIT = "3042";
    public static final String RESULT_CD_MERGED_IAS_TO_DO_ITEM_CONTENT_EN_LENGTH_OVER_LIMIT = "3043";
    public static final String RESULT_CD_MERGED_IAS_TO_DO_ITEM_CONTENT_TC_LENGTH_OVER_LIMIT = "3044";
    public static final String RESULT_CD_MERGED_IAS_TO_DO_ITEM_CONTENT_SC_LENGTH_OVER_LIMIT = "3045";

    public static final String RESULT_CD_MERGED_IAS_APPLICATION_TITLE_EN_LENGTH_OVER_LIMIT = "3046";
    public static final String RESULT_CD_MERGED_IAS_APPLICATION_TITLE_TC_LENGTH_OVER_LIMIT = "3047";
    public static final String RESULT_CD_MERGED_IAS_APPLICATION_TITLE_SC_LENGTH_OVER_LIMIT = "3048";
    public static final String RESULT_CD_MERGED_IAS_APPLICATION_CONTENT_EN_LENGTH_OVER_LIMIT = "3049";
    public static final String RESULT_CD_MERGED_IAS_APPLICATION_CONTENT_TC_LENGTH_OVER_LIMIT = "3050";
    public static final String RESULT_CD_MERGED_IAS_APPLICATION_CONTENT_SC_LENGTH_OVER_LIMIT = "3051";
    // CMC-2025-016: Restrict iAM Smart To Do Item and Application subject max length (limited EN/TC/SC to 70/30/30 characters) - END

    // Individual error code "General Error"
    public static final String RESULT_MSG_GENERAL_ERROR = "General Error.";
    // Individual error code "Success"
    public static final String RESULT_MSG_SUCCESS = "Request processed successfully.";

    // Error description for MaintainMessageService
    // General error message "Sender appID not found"
    public static final String RESULT_MSG_SENDER_APPID_NOT_FOUND = "Sender AppID not found.";
    // General error message "User ID not found"
    public static final String RESULT_MSG_USER_ID_NOT_FOUND = "Active user ID is not found.";
    // General error message "Portal ID not found"
    public static final String RESULT_MSG_PORTAL_ID_NOT_FOUND = "Portal ID is not found.";
    // General error message "Recipient ID is duplicated"
    public static final String RESULT_MSG_RECIPIENT_ID_IS_DUPLICATED = "Recipient ID is duplicated.";
    // General error message "EMessage template not found"
    public static final String RESULT_MSG_EMSG_TEMPLATE_NOT_FOUND = "E-message template is not found.";
    // General error message "TODO Template not found"
    public static final String RESULT_MSG_TO_DO_ITEM_TEMPLATE_NOT_FOUND = "To-Do-Item template is not found.";
    public static final String RESULT_MSG_APPLICATION_TEMPLATE_NOT_FOUND = "Application template is not found.";
    // General error message "Content data not matched"
    public static final String RESULT_MSG_CONTENT_DATA_NOT_MATCHED = "Meta data content do not match with template.";
    // General error message "English meta data is empty"
    public static final String RESULT_MSG_META_DATA_EN_EMPTY = "English meta data content is mandatory.";
    // General error message "English Meta data length is over limit"
    public static final String RESULT_MSG_META_DATA_EN_LENGTH_OVER_LIMIT = "Length of English meta data content is over limit.";
    // General error message "Tradition chinese Meta data length is over limit"
    public static final String RESULT_MSG_META_DATA_TC_LENGTH_OVER_LIMIT = "Length of Traditional Chinese meta data content is over limit.";
    // General error message "Simplified chinese Meta data length is over limit"
    public static final String RESULT_MSG_META_DATA_SC_LENGTH_OVER_LIMIT = "Length of Simplified Chinese meta data content is over limit.";
    // General error message "Template service provider must be the same"
    // CMC-2024-011: Enhance Maintain Message Module To Support iAM Smart Application Status - BEGIN
    public static final String RESULT_MSG_TEMPLATE_SERVICE_PROVIDER_MUST_BE_SAME = "E-message and To-Do-Item and Application Status template must belong to the same service provider.";
    // CMC-2024-011: Enhance Maintain Message Module To Support iAM Smart Application Status - END
    // General error message "TranID is duplicated"
    public static final String RESULT_MSG_TRAN_ID_IS_DUPLICATED = "Tran ID is duplicated within the message request.";
    // General error message "Message Processed with error"
    public static final String RESULT_MSG_MESSAGE_PROCESSED_WITH_ERROR = "Individual transaction has problem, please refer to the transaction result code and description";
    // General error message "Request schema is invalid"
    public static final String RESULT_MSG_REQUEST_SCHEMA_INVALID = "Schema violated";
    public static final String RESULT_MSG_MERGED_SUBJECT_EN_LENGTH_OVER_LIMIT = "Length of Merged English subject is over limit.";
    public static final String RESULT_MSG_MERGED_SUBJECT_TC_LENGTH_OVER_LIMIT = "Length of Merged Traditional Chinese subject is over limit.";
    public static final String RESULT_MSG_MERGED_SUBJECT_SC_LENGTH_OVER_LIMIT = "Length of Merged Simplified Chinese subject is over limit.";
    public static final String RESULT_MSG_MERGED_CONTENT_EN_LENGTH_OVER_LIMIT = "Length of Merged English content is over limit.";
    public static final String RESULT_MSG_MERGED_CONTENT_TC_LENGTH_OVER_LIMIT = "Length of Merged Traditional Chinese content is over limit.";
    public static final String RESULT_MSG_MERGED_CONTENT_SC_LENGTH_OVER_LIMIT = "Length of Merged Simplified Chinese content is over limit.";
    // General error message "Process data not found"
    // CMC-2024-011: Enhance Maintain Message Module To Support iAM Smart Application Status - BEGIN
    public static final String RESULT_MSG_PROCESS_DATA_NOT_FOUND = "E-message, To-Do-Item or Bill-Account-Info or Application Status not found in the request.";
    // CMC-2024-011: Enhance Maintain Message Module To Support iAM Smart Application Status - END
    // General error message "Bill-Account-Info data is empty"
    public static final String RESULT_MSG_BILL_ACCOUNT_INFO_EMPTY = "Bill-Account-Info data is mandatory.";
    // General error message "Sender AppID invalid"
    public static final String RESULT_MSG_SENDER_APP_ID_INVALID = "Sender AppID invalid.";
    // General error code "Bill-Account-Info account label en length over limit"
    public static final String RESULT_MSG_BILL_ACCOUNT_INFO_ACCOUNT_LABEL_EN_LENGTH_OVER_LIMIT = "Bill-Account-Info : Length of Account Label English is over limit. Account Number : {0}";
    // General error code "Bill-Account-Info account label tc length over limit"
    public static final String RESULT_MSG_BILL_ACCOUNT_INFO_ACCOUNT_LABEL_TC_LENGTH_OVER_LIMIT = "Bill-Account-Info : Length of Account Label Traditional Chinese is over limit. Account Number : {0}";
    // General error code "Bill-Account-Info account label sc length over limit"
    public static final String RESULT_MSG_BILL_ACCOUNT_INFO_ACCOUNT_LABEL_SC_LENGTH_OVER_LIMIT = "Bill-Account-Info : Length of Account Label Simplified Chinese is over limit. Account Number : {0}";
    // General error code "Bill-Account-Info account number length over limit"
    public static final String RESULT_MSG_BILL_ACCOUNT_INFO_ACCOUNT_NO_LENGTH_OVER_LIMIT = "Bill-Account-Info : Length of Account Number is over limit. Account Number : {0}";
    // General error code "Bill-Account-Info bill description en length over limit"
    public static final String RESULT_MSG_BILL_ACCOUNT_INFO_BILL_DESC_EN_LENGTH_OVER_LIMIT = "Bill-Account-Info : Length of Bill Description English is over limit. Account Number : {0}";
    // General error code "Bill-Account-Info bill description tc length over limit"
    public static final String RESULT_MSG_BILL_ACCOUNT_INFO_BILL_DESC_TC_LENGTH_OVER_LIMIT = "Bill-Account-Info : Length of Bill Description Traditional Chinese is over limit. Account Number : {0}";
    // General error code "Bill-Account-Info bill description sc length over limit"
    public static final String RESULT_MSG_BILL_ACCOUNT_INFO_BILL_DESC_SC_LENGTH_OVER_LIMIT = "Bill-Account-Info : Length of Bill Description Simplified Chinese is over limit. Account Number : {0}";
    // General error code "Bill-Account-Info bill type length over limit"
    public static final String RESULT_MSG_BILL_ACCOUNT_INFO_BILL_TYPE_LENGTH_OVER_LIMIT = "Bill-Account-Info : Length of Bill Type is over limit. Account Number : {0}";
    // General error message "Bill-Account-Info amount invalid"
    public static final String RESULT_MSG_BILL_ACCOUNT_INFO_AMOUNT_INVALID = "Bill-Account-Info : Amount invalid. Account Number : {0}";
    // General error code "Bill-Account-Info amount sign length over limit"
    public static final String RESULT_MSG_BILL_ACCOUNT_INFO_AMOUNT_SIGN_LENGTH_OVER_LIMIT = "Bill-Account-Info : Length of Amount Sign is over limit. Account Number : {0}";
    // General error message "Bill-Account record not found"
    public static final String RESULT_MSG_BILL_ACCOUNT_RECORD_NOT_FOUND = "Bill-Account-Info record not found. Account Number : {0}";
    // General error code "Payment-Transaction merchant name en length over limit"
    public static final String RESULT_MSG_PAYMENT_TRANSACTION_MERCHANT_NAME_EN_LENGTH_OVER_LIMIT = "Payment-Transaction : Length of Merchant Name English is over limit. Account Number : {0}, Transaction-Reference-Number : {1}";
    // General error code "Payment-Transaction merchant name tc length over limit"
    public static final String RESULT_MSG_PAYMENT_TRANSACTION_MERCHANT_NAME_TC_LENGTH_OVER_LIMIT = "Payment-Transaction : Length of Merchant Name Traditional Chinese is over limit. Account Number : {0}, Transaction-Reference-Number : {1}";
    // General error code "Payment-Transaction merchant name sc length over limit"
    public static final String RESULT_MSG_PAYMENT_TRANSACTION_MERCHANT_NAME_SC_LENGTH_OVER_LIMIT = "Payment-Transaction : Length of Merchant Name Simplified Chinese is over limit. Account Number : {0}, Transaction-Reference-Number : {1}";
    // General error message "Payment-Transaction paid amount invalid"
    public static final String RESULT_MSG_PAYMENT_TRANSACTION_PAID_AMOUNT_INVLID = "Payment-Transaction : Paid Amount invalid. Account Number : {0}, Transaction-Reference-Number : {1}";
    // General error code "Payment-Transaction payment method cd length over limit"
    public static final String RESULT_MSG_PAYMENT_TRANSACTION_PAYMENT_METHOD_CD_LENGTH_OVER_LIMIT = "Payment-Transaction : Length of Payment Method CD is over limit. Account Number : {0}, Transaction-Reference-Number : {1}";
    // General error code "Payment-Transaction payment transaction reference number length over limit"
    public static final String RESULT_MSG_PAYMENT_TRANSACTION_PAYMENT_TRAN_REF_NUMBER_LENGTH_OVER_LIMIT = "Payment-Transaction : Length of Payment Transaction Reference Number is over limit. Account Number : {0}, Transaction-Reference-Number : {1}";
    // General error message "Payment-Transaction record not found"
    public static final String RESULT_MSG_PAYMENT_TRANSACTION_RECORD_NOT_FOUND = "Payment-Transaction record not found. Account Number : {0}, Transaction-Reference-Number : {1}";
    // General error message "Bill-Account batch concurrent processing"
    public static final String RESULT_MSG_BILL_ACCOUNT_BATCH_CONCURRENT_PROCESSING = "Bill-Account batch concurrent processing.";
    // General error message "Bill-Account-Info account number concurrent processing"
    public static final String RESULT_MSG_BILL_ACCOUNT_INFO_ACCOUNT_NO_CONCURRENT_PROCESSING = "Bill-Account-Info account number concurrent processing";
    // General error code "Bill-Account-Info transaction Id is empty"
    public static final String RESULT_MSG_BILL_ACCOUNT_INFO_TRAN_ID_EMPTY = "Bill-Account-Info transaction Id is empty";
    // General error code "Bill-Account-Info transaction-Id may not be up-to-date"
    public static final String RESULT_MSG_BILL_ACCOUNT_INFO_TRAN_ID_NOT_UP_TO_DATE = "Bill-Account-Info transaction-Id may not be up-to-date";

    // General error code "Bill-Account-Info account label en is empty"
    public static final String RESULT_MSG_BILL_ACCOUNT_INFO_ACCOUNT_LABEL_EN_EMPTY = "Bill-Account-Info : Account Label English is empty. Account Number : {0}";
    // General error code "Bill-Account-Info account label tc is empty"
    public static final String RESULT_MSG_BILL_ACCOUNT_INFO_ACCOUNT_LABEL_TC_EMPTY = "Bill-Account-Info : Account Label Traditional Chinese is empty. Account Number : {0}";
    // General error code "Bill-Account-Info account label sc is empty"
    public static final String RESULT_MSG_BILL_ACCOUNT_INFO_ACCOUNT_LABEL_SC_EMPTY = "Bill-Account-Info : Account Label Simplified Chinese is empty. Account Number : {0}";
    // General error code "Bill-Account-Info account number is empty"
    public static final String RESULT_MSG_BILL_ACCOUNT_INFO_ACCOUNT_NO_EMPTY = "Bill-Account-Info : Account Number is empty. Account Number : {0}";
    // General error code "Bill-Account-Info bill description en is empty"
    public static final String RESULT_MSG_BILL_ACCOUNT_INFO_BILL_DESC_EN_EMPTY = "Bill-Account-Info : Bill Description English is empty. Account Number : {0}";
    // General error code "Bill-Account-Info bill description tc is empty"
    public static final String RESULT_MSG_BILL_ACCOUNT_INFO_BILL_DESC_TC_EMPTY = "Bill-Account-Info : Bill Description Traditional Chinese is empty. Account Number : {0}";
    // General error code "Bill-Account-Info bill description sc is empty"
    public static final String RESULT_MSG_BILL_ACCOUNT_INFO_BILL_DESC_SC_EMPTY = "Bill-Account-Info : Bill Description Simplified Chinese is empty. Account Number : {0}";
    // General error code "Bill-Account-Info bill type is empty"
    public static final String RESULT_MSG_BILL_ACCOUNT_INFO_BILL_TYPE_EMPTY = "Bill-Account-Info : Bill Type is empty. Account Number : {0}";
    // General error code "Payment-Transaction merchant name en is empty"
    public static final String RESULT_MSG_PAYMENT_TRANSACTION_MERCHANT_NAME_EN_EMPTY = "Payment-Transaction : Merchant Name English is empty. Account Number : {0}, Transaction-Reference-Number : {1}";
    // General error code "Payment-Transaction merchant name tc is empty"
    public static final String RESULT_MSG_PAYMENT_TRANSACTION_MERCHANT_NAME_TC_EMPTY = "Payment-Transaction : Merchant Name Traditional Chinese is empty. Account Number : {0}, Transaction-Reference-Number : {1}";
    // General error code "Payment-Transaction merchant name sc is empty"
    public static final String RESULT_MSG_PAYMENT_TRANSACTION_MERCHANT_NAME_SC_EMPTY = "Payment-Transaction : Merchant Name Simplified Chinese is empty. Account Number : {0}, Transaction-Reference-Number : {1}";
    // General error code "Payment-Transaction payment method cd is empty"
    public static final String RESULT_MSG_PAYMENT_TRANSACTION_PAYMENT_METHOD_CD_EMPTY = "Payment-Transaction : Payment Method CD is empty. Account Number : {0}, Transaction-Reference-Number : {1}";
    // General error code "Payment-Transaction payment transaction reference number is empty"
    public static final String RESULT_MSG_PAYMENT_TRANSACTION_PAYMENT_TRAN_REF_NUMBER_EMPTY = "Payment-Transaction : Payment Transaction Reference Number is empty. Account Number : {0}, Transaction-Reference-Number : {1}";
    // General error code "Bill-Account-Info issue date is empty"
    public static final String RESULT_MSG_BILL_ACCOUNT_INFO_ISSUE_DATE_EMPTY = "Bill-Account-Info : Issue date is empty. Account Number : {0}";
    // General error code "Bill-Account-Info online payment is empty"
    public static final String RESULT_MSG_BILL_ACCOUNT_INFO_ONLINE_PAYMENT_EMPTY = "Bill-Account-Info : Online Payment Indicator is empty. Account Number : {0}";
    // General error code "Bill-Account-Info remark length over limit"
    public static final String RESULT_MSG_BILL_ACCOUNT_INFO_REMARK_LENGTH_OVER_LIMIT = "Bill-Account-Info : Length of Remark is over limit. Account Number : {0}";
    // General error code "Payment-Transaction payment transaction date is empty"
    public static final String RESULT_MSG_PAYMENT_TRANSACTION_PAYMENT_TRAN_DATE_EMPTY = "Payment-Transaction : Payment Transaction Date is empty. Account Number : {0}, Transaction-Reference-Number : {1}";

    // CMC-RA16-17 - Revise the e-Message request from RS such that they can voided those payment transaction -- BEGIN
    // General error code "Payment-Transaction cannot add Payment Method CD '01' record"
    public static final String RESULT_MSG_PAYMENT_TRANSACTION_PAYMENT_METHOD_CANNOT_ADD_MYGOVHK_RECORD = "Payment-Transaction : Cannot add Payment Method CD equels to '01' record. Account Number : {0}, Transaction-Reference-Number : {1}";
    // General error code "Payment-Transaction cannot delete Payment Method CD '01' record"
    public static final String RESULT_MSG_PAYMENT_TRANSACTION_PAYMENT_METHOD_CANNOT_DEL_MYGOVHK_RECORD = "Payment-Transaction : Cannot delete Payment Method CD equels to '01' record. Account Number : {0}, Transaction-Reference-Number : {1}";
    // CMC-RA16-17 - Revise the e-Message request from RS such that they can voided those payment transaction -- END

    // CMC-COR-95 - Support e-Cheque payment method -- BEGIN
    // General error code "Payment-Transaction cannot add Payment Method CD '20' record"
    public static final String RESULT_MSG_PAYMENT_TRANSACTION_PAYMENT_METHOD_CANNOT_ADD_MYGOVHK_ECHQ_RECORD = "Payment-Transaction : Cannot add Payment Method CD equels to '20' record. Account Number : {0}, Transaction-Reference-Number : {1}";
    // General error code "Payment-Transaction cannot delete Payment Method CD '20' record"
    public static final String RESULT_MSG_PAYMENT_TRANSACTION_PAYMENT_METHOD_CANNOT_DEL_MYGOVHK_ECHQ_RECORD = "Payment-Transaction : Cannot delete Payment Method CD equels to '20' record. Account Number : {0}, Transaction-Reference-Number : {1}";
    // CMC-COR-95 - Support e-Cheque payment method -- END

    // CMC-RA16-34 - Support partial payment and allow multiple bills in single payment -- BEGIN
    // General error code "Bill-Account-Info balance as at date is empty"
    public static final String RESULT_MSG_BILL_ACCOUNT_INFO_BAL_AS_AT_DATE_EMPTY = "Bill-Account-Info : Balance as at date is empty. Account Number : {0}";
    // General error message "Bill-Account-Info bill-amount invalid"
    public static final String RESULT_MSG_BILL_ACCOUNT_INFO_BILLAMOUNT_INVALID = "Bill-Account-Info : Bill-amount invalid. Account Number : {0}";
    // CMC-RA16-34 - Support partial payment and allow multiple bills in single payment -- END

    // CMC-BAT-09 -- BEGIN
    // Email error message
    public static final String RESULT_MSG_MERGED_EMAIL_SUBJECT_EN_LENGTH_OVER_LIMIT = "Length of Merged E-mail English subject is over limit.";
    public static final String RESULT_MSG_MERGED_EMAIL_SUBJECT_TC_LENGTH_OVER_LIMIT = "Length of Merged E-mail Traditional Chinese subject is over limit.";
    public static final String RESULT_MSG_MERGED_EMAIL_SUBJECT_SC_LENGTH_OVER_LIMIT = "Length of Merged E-mail Simplified Chinese subject is over limit.";
    public static final String RESULT_MSG_MERGED_EMAIL_CONTENT_EN_LENGTH_OVER_LIMIT = "Length of Merged E-mail English content is over limit.";
    public static final String RESULT_MSG_MERGED_EMAIL_CONTENT_TC_LENGTH_OVER_LIMIT = "Length of Merged E-mail Traditional Chinese content is over limit.";
    public static final String RESULT_MSG_MERGED_EMAIL_CONTENT_SC_LENGTH_OVER_LIMIT = "Length of Merged E-mail Simplified Chinese content is over limit.";
    // CMC-BAT-09 -- END
    // mobile application modification -- START
    public static final String RESULT_MSG_MERGED_MOBILE_SUBJECT_EN_LENGTH_OVER_LIMIT = "Length of Merged Mobile Message English subject is over limit.";
    public static final String RESULT_MSG_MERGED_MOBILE_SUBJECT_TC_LENGTH_OVER_LIMIT = "Length of Merged Mobile Message Traditional Chinese subject is over limit.";
    public static final String RESULT_MSG_MERGED_MOBILE_SUBJECT_SC_LENGTH_OVER_LIMIT = "Length of Merged Mobile Message Simplified Chinese subject is over limit.";
    public static final String RESULT_MSG_MERGED_MOBILE_CONTENT_EN_LENGTH_OVER_LIMIT = "Length of Merged Mobile Message English content is over limit.";
    public static final String RESULT_MSG_MERGED_MOBILE_CONTENT_TC_LENGTH_OVER_LIMIT = "Length of Merged Mobile Message Traditional Chinese content is over limit.";
    public static final String RESULT_MSG_MERGED_MOBILE_CONTENT_SC_LENGTH_OVER_LIMIT = "Length of Merged Mobile Message Simplified Chinese content is over limit.";
    // mobile application modification -- END

    // MyGov6-C1-001: Enhance CMC client for supporting iAM Smart Message -- START
    // Maintain message error code "Recipient is suspended."
    public static final String RESULT_MSG_RECIPIENT_IS_SUSPENDED = "Recipient is suspended.";
    // Maintain message error code "Recipient is invalid."
    public static final String RESULT_MSG_RECIPIENT_IS_INVALID = "Recipient is invalid.";
    // Maintain message error code "Recipient is deregistered."
    public static final String RESULT_MSG_RECIPIENT_IS_DEREGISTERED = "Recipient is deregistered.";
    // Maintain message error code "Length of Merged iAM Smart Message English subject is over limit."
    public static final String RESULT_MSG_MERGED_IAS_MSG_SUBJECT_EN_LENGTH_OVER_LIMIT = "Length of Merged iAM Smart Message English subject is over limit.";
    // Maintain message error code "Length of Merged iAM Smart Message Traditional Chinese subject is over limit."
    public static final String RESULT_MSG_MERGED_IAS_MSG_SUBJECT_TC_LENGTH_OVER_LIMIT = "Length of Merged iAM Smart Message Traditional Chinese subject is over limit.";
    // Maintain message error code "Length of Merged iAM Smart Message Simplified Chinese subject is over limit."
    public static final String RESULT_MSG_MERGED_IAS_MSG_SUBJECT_SC_LENGTH_OVER_LIMIT = "Length of Merged iAM Smart Message Simplified Chinese subject is over limit.";
    // Maintain message error code "Length of Merged iAM Smart Message English content is over limit."
    public static final String RESULT_MSG_MERGED_IAS_MSG_CONTENT_EN_LENGTH_OVER_LIMIT = "Length of Merged iAM Smart Message English content is over limit.";
    // Maintain message error code "Length of Merged iAM Smart Message Traditional Chinese content is over limit."
    public static final String RESULT_MSG_MERGED_IAS_MSG_CONTENT_TC_LENGTH_OVER_LIMIT = "Length of Merged iAM Smart Message Traditional Chinese content is over limit.";
    // Maintain message error code "Length of Merged iAM Smart Message Simplified Chinese content is over limit."
    public static final String RESULT_MSG_MERGED_IAS_MSG_CONTENT_SC_LENGTH_OVER_LIMIT = "Length of Merged iAM Smart Message Simplified Chinese content is over limit.";
    // MyGov6-C1-001: Enhance CMC client for supporting iAM Smart Message -- END

    // Individual error message "Success"
    public static final String RESULT_MSG_TRAN_SUCCESS = "Transaction processed successfully";

    // Individual error message "IDP ID not found"
    public static final String RESULT_MSG_IDP_ID_NOT_FOUND = "Identity Provider ID is not found.";
    // Individual error message "Recipient ID not found"
    public static final String RESULT_MSG_RECIPIENT_ID_NOT_FOUND = "Recipient ID is not found.";
    // Individual error message "Recipient ID is deleted"
    public static final String RESULT_MSG_RECIPIENT_IS_DELETED = "Recipient is deleted.";
    // Individual error message "TranID length is over limit"
    public static final String RESULT_MSG_TRAN_ID_LENGTH_OVER_LIMIT = "Transction ID length is over limit.";
    // Individual error message "TODO action is not valid"
    public static final String RESULT_MSG_TO_DO_ITEM_ACTION_NOT_VALID = "To-Do-Item action is invalid.";
    // Individual error message "Correlated TranID is not found"
    public static final String RESULT_MSG_CORRELATED_TRAN_ID_NOT_FOUND = "Correlated transaction ID is not found.";
    // Individual error message "User Reject Message"
    public static final String RESULT_MSG_USER_REJECT_MSG = "User reject to receive message.";
    // Individual error message "Item date is not valid"
    public static final String RESULT_MSG_ITEM_DATE_IS_NOT_VALID = "To-Do-Item date is not valid.";
    // Individual error message "Item Date not found"
    public static final String RESULT_MSG_ITEM_DATE_NOT_FOUND = "To-Do-Item date must be entered for To-Do-Item.";
    // Individual error message "User fail to receive emessage"
    public static final String RESULT_MSG_USER_FAIL_TO_RECEIVE_EMSG = "User failed to receive E-message.";
    // Individual error message "TODO item already marked as delete"
    public static final String RESULT_MSG_TO_DO_ITEM_ALREADY_MARK_DELETE = "To-Do-Item is already mark deleted";
    // Individual error message "TODO item already marked as complete"
    public static final String RESULT_MSG_TO_DO_ITEM_ALREADY_MARK_COMPLETE = "To-Do-Item is already mark completed";

    // CMC-2024-011: Enhance Maintain Message Module To Support iAM Smart Application Status - BEGIN
    public static final String RESULT_MSG_APPLICATION_ALREADY_MARK_DELETE = "Application is already mark deleted.";
    // public static final String RESULT_MSG_APP_REF_NUM_NOT_FOUND = "Reference ID of iAM Smart Application is not found.";
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
    // CMC-2024-011: Enhance Maintain Message Module To Support iAM Smart Application Status - END

    // CMC-2025-016: Restrict iAM Smart To Do Item and Application subject max length (limited EN/TC/SC to 70/30/30 characters) - BEGIN
    public static final String RESULT_MSG_MERGED_IAS_TO_DO_ITEM_SUBJECT_EN_LENGTH_OVER_LIMIT = "Length of Merged iAM Smart To-Do-Item English subject is over limit.";
    public static final String RESULT_MSG_MERGED_IAS_TO_DO_ITEM_SUBJECT_TC_LENGTH_OVER_LIMIT = "Length of Merged iAM Smart To-Do-Item Traditional Chinese subject is over limit.";
    public static final String RESULT_MSG_MERGED_IAS_TO_DO_ITEM_SUBJECT_SC_LENGTH_OVER_LIMIT = "Length of Merged iAM Smart To-Do-Item Simplified Chinese subject is over limit.";
    public static final String RESULT_MSG_MERGED_IAS_TO_DO_ITEM_CONTENT_EN_LENGTH_OVER_LIMIT = "Length of Merged iAM Smart To-Do-Item English content is over limit.";
    public static final String RESULT_MSG_MERGED_IAS_TO_DO_ITEM_CONTENT_TC_LENGTH_OVER_LIMIT = "Length of Merged iAM Smart To-Do-Item Traditional Chinese content is over limit.";
    public static final String RESULT_MSG_MERGED_IAS_TO_DO_ITEM_CONTENT_SC_LENGTH_OVER_LIMIT = "Length of Merged iAM Smart To-Do-Item Simplified Chinese content is over limit.";
    // CMC-2025-016: Restrict iAM Smart To Do Item and Application subject max length (limited EN/TC/SC to 70/30/30 characters) - END

    public static final String MOBILE_MSG_STATUS_NEW = "N";
    // public static final String MOBILE_MSG_STATUS_SENT_TO_EGIS_MSG = "P";
    public static final String MOBILE_MSG_STATUS_SUCCESS_RESPONSE = "S";
    // public static final String MOBILE_MSG_STATUS_NO_RESPONSE_AFT_THRESHOLD = "X";
    public static final String MOBILE_MSG_STATUS_FAILURE_RESPONSE = "F";
    public static final String MOBILE_MSG_STATUS_PARTIAL_FAILURE = "A";

    public static final String USER_MOBILE_MSG_STATUS_NEW = "N";
    public static final String USER_MOBILE_MSG_STATUS_SENT_TO_EGIS_MSG = "P";
    public static final String USER_MOBILE_MSG_STATUS_SUCCESS_RESPONSE = "S";
    // public static final String USER_MOBILE_MSG_STATUS_NO_RESPONSE_AFT_THRESHOLD = "X";
    public static final String USER_MOBILE_MSG_STATUS_FAILURE_RESPONSE = "F";
    // public static final String USER_MOBILE_MSG_STATUS_DEVICE_TOKEN_EMPTY = "E";

    public static final String RECIPIENT_RESPONSE_STATUS_SUCCESS = "S";
    public static final String RECIPIENT_RESPONSE_STATUS_DELAY = "D";
    // public static final String RECIPIENT_RESPONSE_STATUS_EXPIRED = "E";
    public static final String RECIPIENT_RESPONSE_STATUS_READ = "R";

    public static final String USER_MESSAGE_MOBILE_STATUS_SUCCESS = "Y";
    public static final String USER_MESSAGE_MOBILE_STATUS_FAIL = "F";
    public static final String USER_MESSAGE_MOBILE_STATUS_PROCESSING = "P";

    // User status code "ACTIVE"
    public static final String USER_STATUS_ACTIVE = "A";
    // User status code "DELETED"
    public static final String USER_STATUS_DELETED = "D";
    // User status code "SUSPENDED"
    public static final String USER_STATUS_SUSPENDED = "S";

    // Link status code "LINK_UP"
    public static final String LINK_STATUS_LINK_UP = "L";
    // Link status code "DELINK"
    public static final String LINK_STATUS_DELINK = "D";
    // Link status code "SUSPEND"
    public static final String LINK_STATUS_SUSPEND = "S";

    // Portal server status code "ACTIVE"
    public static final String PORTAL_STATUS_ACTIVE = "A";
    // Portal server status code "INACTIVE"
    public static final String PORTAL_STATUS_INACTIVE = "I";

    // Idp server status code "ACTIVE"
    public static final String IDP_STATUS_ACTIVE = "A";
    // Idp server status code "INACTIVE"
    public static final String IDP_STATUS_INACTIVE = "I";

    // Hidden indicator code "Hidden"
    public static final String HIDDEN_IND_HIDDEN = "Y";
    // Hidden indicator code "Not Hidden"
    public static final String HIDDEN_IND_NOT_HIDDEN = "N";

    // Hidden indicator code "Reject"
    public static final String REJECT_IND_REJECT = "Y";
    // Hidden indicator code "Not reject"
    public static final String REJECT_IND_NOT_REJECT = "N";

    // Hidden indicator code "Recon"
    public static final String RECON_IND_RECON = "Y";
    // Hidden indicator code "Not recon"
    public static final String RECON_IND_NOT_RECON = "N";

    // Archive indicator code "Archived"
    public static final String ARCHIVE_IND_ARCHIVED = "Y";
    // Archive indicator code "Not archived"
    public static final String ARCHIVE_IND_NOT_ARCHIVED = "N";

    // Delete indicator code "Deleted"
    public static final String DELETE_IND_DELETED = "Y";
    // Delete indicator code "Not Deleted"
    public static final String DELETE_IND_NOT_DELETED = "N";
    // CMC-2025-002: Accept HKID input in IasToDoItem and IasApplication function - BEGIN
    // Delete indicator code "Pending Delete"
    public static final String DELETE_IND_PENDING = "P";
    // CMC-2025-002: Accept HKID input in IasToDoItem and IasApplication function - END

    // Complete indicator code "Completed"
    public static final String COMPLETE_IND_COMPLETED = "C";
    // Complete indicator code "InCompleted"
    public static final String COMPLETE_IND_IN_COMPLETED = "I";

    // Complete by code "BY_USER"
    public static final String COMPLETE_BY_USER = "U";
    // Complete by code "BY_RS"
    public static final String COMPLETE_BY_RS = "RS";

    // Read indicator code "READ"
    public static final String READ_IND_READ = "R";
    // Read indicator code "UNREAD"
    public static final String READ_IND_UNREAD = "U";

    // CMC-ADM-23 Add logic to check dayend alert is checked if email indicator is checked -- BEGIN
    public static final String DEFAULT_ALERT_DT = "20000101";
    // CMC-ADM-23 Add logic to check dayend alert is checked if email indicator is checked -- END

    // Read indicator code "NOT_INFORMED"
    public static final String UNDELIVERED_MSG_NOT_INFORMED_SP = "N";
    // Read indicator code "INFORMED"
    public static final String UNDELIVERED_MSG_INFORMED_SP = "I";
    // Read indicator code "CANCELLED"
    public static final String UNDELIVERED_MSG_CANCELLED = "C";

    // CMC_USER_EMAIL STATUS - New
    public static final String USER_EMAIL_STATUS_NEW = "N";
    // CMC_USER_EMAIL STATUS - Profile Retrieved (Obsoleted)
    public static final String USER_EMAIL_STATUS_PROFILE_RETRIEVED = "P";
    // CMC_USER_EMAIL STATUS - Profile Retrieve Fail (Obsoleted)
    public static final String USER_EMAIL_STATUS_PROFILE_RETRIEVE_FAIL = "E";
    // User Email status code "Profile Retrieved - User unwilling to receive e-mail" (Obsoleted)
    public static final String USER_EMAIL_STATUS_USER_UNWILLING_TO_RECEIVE_EMAIL = "U";
    // CMC_USER_EMAIL STATUS - Sent Successfully through Noti
    public static final String USER_EMAIL_STATUS_SENT_SUCCESS = "S";
    // CMC_USER_EMAIL STATUS - Sent Failed through Noti
    public static final String USER_EMAIL_STATUS_SENT_FAIL = "F";
    // CMC_USER_EMAIL STATUS - Sent through Noti with Exception
    public static final String USER_EMAIL_STATUS_SENT_EXCEPTION = "X";
    // CMC_USER_EMAIL STATUS - Deleted (Obsoleted)
    public static final String USER_EMAIL_STATUS_DELETED = "D";

    // User is willing to receive e-mail - Yes
    public static final String USER_WILLING_TO_RECEIVE_EMAIL_YES = "Y";

    // User is willing to receive e-mail - No
    public static final String USER_WILLING_TO_RECEIVE_EMAIL_NO = "N";
    // public constant(s) -- END

    // Result message for RetrieveCmcTemplateService "Success"
    public static final String RESULT_MSG_RETRIEVE_CMC_TEMPLATE_0000 = "Success";
    // Result message for RetrieveCmcTemplateService "CMC Template Not Found"
    public static final String RESULT_MSG_RETRIEVE_CMC_TEMPLATE_0001 = "CMC Template Not Found";
    // Result message for RetrieveCmcTemplateService "General Error"
    public static final String RESULT_MSG_RETRIEVE_CMC_TEMPLATE_9999 = "General exception";

    // Result message for MaintainTemplateService "Success"
    public static final String RESULT_MSG_MAINTAIN_TEMPLATE_0000 = "Success";
    // Result message for MaintainTemplateService "CMC Template Not Found"
    public static final String RESULT_MSG_MAINTAIN_TEMPLATE_0001 = "CMC Template Not Found";
    // Result message for MaintainTemplateService "General Error"
    public static final String RESULT_MSG_MAINTAIN_TEMPLATE_9999 = "General exception";

    // CMC-RA16-17 - Revise the e-Message request from RS such that they can voided those payment transaction -- BEGIN
    public static final String PAYMENT_METHOD_MYGOVHK = "01";
    // CMC-RA16-17 - Revise the e-Message request from RS such that they can voided those payment transaction -- END
    //
    // CMC-COR-95 - Support e-Cheque payment method -- BEGIN
    public static final String PAYMENT_METHOD_MYGOVHK_ECHQ = "20";
    // CMC-COR-95 - Support e-Cheque payment method -- END

    public static final int BILL_ACCOUNT_INFO_ACCOUNT_LABEL_EN_MAX_LENGTH = 20;
    public static final int BILL_ACCOUNT_INFO_ACCOUNT_LABEL_TC_MAX_LENGTH = 20;
    public static final int BILL_ACCOUNT_INFO_ACCOUNT_LABEL_SC_MAX_LENGTH = 20;
    public static final int BILL_ACCOUNT_INFO_ACCOUNT_NO_MAX_LENGTH = 30;
    public static final int BILL_ACCOUNT_INFO_ACCOUNT_BILL_DESC_EN_MAX_LENGTH = 255;
    public static final int BILL_ACCOUNT_INFO_ACCOUNT_BILL_DESC_TC_MAX_LENGTH = 255;
    public static final int BILL_ACCOUNT_INFO_ACCOUNT_BILL_DESC_SC_MAX_LENGTH = 255;
    public static final int BILL_ACCOUNT_INFO_ACCOUNT_BILL_TYPE_MAX_LENGTH = 2;
    public static final int BILL_ACCOUNT_INFO_ACCOUNT_AMOUNT_SIGN_MAX_LENGTH = 1;
    public static final int BILL_ACCOUNT_INFO_ACCOUNT_REMARK_MAX_LENGTH = 255;
    public static final int PAYMENT_TRANSACTION_MERCHANT_NAME_EN_MAX_LENGTH = 255;
    public static final int PAYMENT_TRANSACTION_MERCHANT_NAME_TC_MAX_LENGTH = 255;
    public static final int PAYMENT_TRANSACTION_MERCHANT_NAME_SC_MAX_LENGTH = 255;
    public static final int PAYMENT_TRANSACTION_PAYMENT_METHOD_CD_MAX_LENGTH = 2;
    public static final int PAYMENT_TRANSACTION_PAYMENT_TRAN_REF_NUMBER_MAX_LENGTH = 20;

    // MyGov6-C1-002 Enhance Maintain Message module to support iAM Smart message -- START
    // iAM Smart User status code "ACTIVE"
    public static final String IAS_USER_STATUS_ACTIVE = "A";
    // iAM Smart User status code "DEREGISTERED"
    public static final String IAS_USER_STATUS_DEREGISTERED = "D";
    // iAM Smart User status code "INVALID"
    public static final String IAS_USER_STATUS_INVALID = "I";
    // iAM Smart User status code "SUSPENDED"
    public static final String IAS_USER_STATUS_SUSPENDED = "S";
    // iAM Smart User status code "MISSING"
    public static final String IAS_USER_STATUS_MISSING = "M";

    // IAS NOTI STATUS - New
    public static final String IAS_NOTI_STATUS_NEW = "N";
    // IAS NOTI STATUS - Open ID invalid
    public static final String IAS_NOTI_STATUS_I = "I";
    // IAS NOTI STATUS - Sent successfully
    public static final String IAS_NOTI_STATUS_SENT = "S";
    // IAS NOTI STATUS - User Opt-out to receive message
    public static final String IAS_NOTI_STATUS_OPT_OUT = "O";
    // IAS NOTI STATUS - Fail to send
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

    // ENC_IND - Yes
    public static final String ENC_IND_YES = "Y";
    // ENC_IND - No
    public static final String ENC_IND_NO = "N";

}