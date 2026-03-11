/*
 * Design, Implementation and Support of Common Middleware Components and Reference Applications
 *
 * Constants
 *
 * Developed by: Gilbert Liao
 * Reviewed by: Kai So
 * Tester: Shaohui Wang
 */
package hk.gov.dpo.mars_cmc.cmc.datatype.message;

// import Java standard package(s) -- BEGIN
// import Java standard package(s) -- END

// import third-party package(s) -- BEGIN
// import third-party package(s) -- END

// import internal library package(s) -- BEGIN
// import internal library package(s) -- END

// import module package(s) -- BEGIN
// import module package(s) -- END

/**
 * Constants used by the axis module for web services
 */
public class Constants
{
	// public constant(s) -- BEGIN
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
	public static final String RESULT_CD_APP_REF_NUM_NOT_FOUND = "3035";
	public static final String RESULT_CD_APPLICATION_STATUS_NOT_VALID = "3036";
	public static final String RESULT_CD_RECIPIENT_ID_TYPE_NOT_VALID = "3037"; // apply to iAM Smart To-Do-Item and iAM Smart Application Status only, not applicable to iAM Smart Message
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
	public static final String RESULT_MSG_GENERAL_ERROR="General Error.";
	// Individual error code "Success"
	public static final String RESULT_MSG_SUCCESS="Request processed successfully.";

	// Error description for MaintainMessageService
	// General error message "Sender appID not found"
	public static final String RESULT_MSG_SENDER_APPID_NOT_FOUND = "Sender AppID not found.";
	// General error message "User ID not found"
	public static final String RESULT_MSG_USER_ID_NOT_FOUND="Active user ID is not found.";
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
	// General error message "Sender AppID invalid"
	public static final String RESULT_MSG_SENDER_APP_ID_INVALID = "Sender AppID invalid.";

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
	public static final String RESULT_MSG_TRAN_SUCCESS="Transaction processed successfully";

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
	public static final String RESULT_MSG_APP_REF_NUM_NOT_FOUND = "Reference ID of iAM Smart Application is not found.";
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
//	public static final String MOBILE_MSG_STATUS_SENT_TO_EGIS_MSG = "P";
	public static final String MOBILE_MSG_STATUS_SUCCESS_RESPONSE = "S";
//	public static final String MOBILE_MSG_STATUS_NO_RESPONSE_AFT_THRESHOLD = "X";
	public static final String MOBILE_MSG_STATUS_FAILURE_RESPONSE = "F";
	public static final String MOBILE_MSG_STATUS_PARTIAL_FAILURE = "A";

	public static final String USER_MOBILE_MSG_STATUS_NEW = "N";
	public static final String USER_MOBILE_MSG_STATUS_SENT_TO_EGIS_MSG = "P";
	public static final String USER_MOBILE_MSG_STATUS_SUCCESS_RESPONSE = "S";
//	public static final String USER_MOBILE_MSG_STATUS_NO_RESPONSE_AFT_THRESHOLD = "X";
	public static final String USER_MOBILE_MSG_STATUS_FAILURE_RESPONSE = "F";
//	public static final String USER_MOBILE_MSG_STATUS_DEVICE_TOKEN_EMPTY = "E";

	public static final String RECIPIENT_RESPONSE_STATUS_SUCCESS = "S";
	public static final String RECIPIENT_RESPONSE_STATUS_DELAY = "D";
//	public static final String RECIPIENT_RESPONSE_STATUS_EXPIRED = "E";
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

	// User  is willing to receive e-mail - Yes
	public static final String USER_WILLING_TO_RECEIVE_EMAIL_YES = "Y";

	// User  is willing to receive e-mail - No
	public static final String USER_WILLING_TO_RECEIVE_EMAIL_NO = "N";
	// public constant(s) -- END


	//Result message for RetrieveCmcTemplateService "Success"
	public static final String RESULT_MSG_RETRIEVE_CMC_TEMPLATE_0000 = "Success";
	//Result message for RetrieveCmcTemplateService "CMC Template Not Found"
	public static final String RESULT_MSG_RETRIEVE_CMC_TEMPLATE_0001 = "CMC Template Not Found";
	//Result message for RetrieveCmcTemplateService "General Error"
	public static final String RESULT_MSG_RETRIEVE_CMC_TEMPLATE_9999 = "General exception";

	//Result message for MaintainTemplateService "Success"
	public static final String RESULT_MSG_MAINTAIN_TEMPLATE_0000 = "Success";
	//Result message for MaintainTemplateService "CMC Template Not Found"
	public static final String RESULT_MSG_MAINTAIN_TEMPLATE_0001 = "CMC Template Not Found";
	//Result message for MaintainTemplateService "General Error"
	public static final String RESULT_MSG_MAINTAIN_TEMPLATE_9999 = "General exception";


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
	// iAM Smart Undelivered Message Reason - "OPT OUT"
	public static final String IAS_UNDELIVER_MSG_REASON_OPT_OUT = "User Opt-out to receive message";
	// CMC-2024-011: Enhance Maintain Message Module To Support iAM Smart Application Status - BEGIN
	public static final String IAS_UNDELIVER_MSG_REASON_INVALID_NOTI_ID = "Notification ID is invalid";
	public static final String IAS_UNDELIVER_MSG_REASON_INVALID_CONSENT = "Invalid Consent";
	public static final String IAS_UNDELIVER_MSG_REASON_OTHER_EROR = "Other Errors";
	// CMC-2024-011: Enhance Maintain Message Module To Support iAM Smart Application Status - END

	// ENC_IND - Yes
	public static final String ENC_IND_YES = "Y";
	// ENC_IND - No
	public static final String ENC_IND_NO = "N";


// MyGov6-C1-002 Enhance Maintain Message module to support iAM Smart message -- END

	// public constant(s) -- END

	// public data member(s) -- BEGIN
	// public data member(s) -- END

	// public method(s) -- BEGIN
	// public method(s) -- END

	// protected constant(s) -- BEGIN
	// protected constant(s) -- END

	// protected method(s) -- BEGIN
	// protected method(s) -- END

	// private method(s) -- BEGIN
	// private method(s) -- END

	// private data member(s) -- BEGIN
	// private data member(s) -- END

	// private constant(s) -- BEGIN
	// private constant(s) -- END
}