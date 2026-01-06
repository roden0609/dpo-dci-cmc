package hk.gov.cmc.controller.maintainmessage;

import java.io.StringReader;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang3.StringEscapeUtils;
import org.bouncycastle.asn1.cms.MetaData;
import org.bouncycastle.asn1.ocsp.ServiceLocator;
import org.bouncycastle.cms.Recipient;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import hk.gov.cmc.config.CmcSystemParam;
import hk.gov.cmc.jaxb.maintainmessage.MaintainMessageRequest;
import hk.gov.cmc.jaxb.maintainmessage.MaintainMessageResponse;
import hk.gov.cmc.kmu.client.KMUUtils;
import hk.gov.cmc.persistence.connection.hpfw.CommonDBUtils;
import hk.gov.cmc.persistence.connection.hpfw.HPFW_Connection;
import hk.gov.cmc.utils.JobControlUtils;
import hk.gov.ogcio.mars_cmc.cmc.appserver.AppConstants;
import hk.gov.ogcio.mars_cmc.cmc.appserver.AppPropertyName;
import hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.EMessage;
import hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageRequest;
import hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageResponse;
import hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.ToDoItem;
import hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.Action_ST;
import hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.CommonIndicator_ST;
import hk.gov.ogcio.mars_cmc.cmc.client.maintainmessage.MaintainMessageClient;
import hk.gov.ogcio.mars_cmc.cmc.datatype.MessageParam;
import hk.gov.ogcio.mars_cmc.cmc.datatype.message.IasUser;
import hk.gov.ogcio.mars_cmc.cmc.datatype.message.IasUserWrapped;
import hk.gov.ogcio.mars_cmc.cmc.datatype.message.ToDoItemRecipientKey;
import hk.gov.ogcio.mars_cmc.cmc.service.constant.MsgTypeConstant;
import hk.gov.ogcio.mars_cmc.cmc.service.constant.RecipientIDTypeConstant;
import hk.gov.ogcio.mars_cmc.cmc.service.controller.IasApplicationController;
import hk.gov.ogcio.mars_cmc.cmc.service.controller.IasToDoItemController;
import hk.gov.ogcio.mars_cmc.cmc.service.dao.cmctemplate.CmcTemplateDao;
import hk.gov.ogcio.mars_cmc.cmc.service.dao.iasNotification.IasUserNotiInfoDAO;
import hk.gov.ogcio.mars_cmc.cmc.service.databean.CmcTemplate;
import hk.gov.ogcio.mars_cmc.cmc.service.databean.CmcUser_;
import hk.gov.ogcio.mars_cmc.cmc.service.databean.IasEsNotiMap_;
import hk.gov.ogcio.mars_cmc.cmc.service.databean.IasMessage_;
import hk.gov.ogcio.mars_cmc.cmc.service.databean.IasUserMessage_;
import hk.gov.ogcio.mars_cmc.cmc.service.databean.IasUserToDoItem_;
import hk.gov.ogcio.mars_cmc.cmc.service.dto.SingleMaintainMsgResult;
import hk.gov.ogcio.mars_cmc.cmc.service.utils.IasRecipientUtils;
import hk.gov.ogcio.mars_cmc.cmc.service.validator.IasMsgValidator;
import hk.gov.ogcio.mars_cmc.cmc.service.validator.IasRecipientValidator;
import hk.gov.ogcio.mars_cmc.cmc.utils.EncUtils;
import hk.gov.ogcio.mars_cmc.cmc.utils.IasUtils;
import hk.gov.ogcio.mars_cmc.cmc.utils.MessageRequestTransformUtils;
import hk.gov.ogcio.mars_cmc.cmc.utils.ResponseUtils;
import hk.gov.ogcio.mars_cmc.cmc.utils.ValidationUtils;
import hk.gov.ogcio.mars_cmc.framework.common.utils.XsltUtil;
import hk.gov.ogcio.mars_cmc.mars.query.dao.MarsUserAttributeDAO;
import hk.gov.ogcio.mars_cmc.mars.query.databean.MarsUserObject;
import jakarta.xml.soap.SOAPMessage;

public class MaintainMessageController {

    public static final String HISTORY_NEW = "New";
    public static final String HISTORY_DELETE = "Delete";
    public static final String HISTORY_REPLACE = "Replace";
    public static final String HISTORY_MARKCOMPLETE = "Markcomplete";

    public static final String HISTORY_UPDATE = "Update";

    private static final String LANGUAGE_EN = "EN";
    private static final String LANGUAGE_TC = "TC";
    private static final String LANGUAGE_SC = "SC";

    public MaintainMessageController() {

        m_toDoItemHistory = new HashMap();
        m_newUserToDoItem = new HashMap();

        m_iasToDoItemHistory = new HashMap<String, String>();
        m_newIasUserToDoItem = new HashMap<ToDoItemRecipientKey, ArrayList<IasUserToDoItem_>>();

        m_iasApplicationHistory = new HashMap<String, String>();

    }

    public MaintainMessageResponse processMessage(HPFW_Connection conn, String appId,
            MaintainMessageRequest maintMsgReq) throws Exception {
        logInfo("[MaintMsg]processMessage - START");

        MaintainMessageResponse response = new MaintainMessageResponse();

        String serverSequencePrefix1 = CommonDBUtils.getSERVER_SEQUENCE_PREFIX();

        logDebug("Step 0.1 : Create temp-object for request checking");
        String billAcctServiceProviderId = "";
        boolean releaseLock = true;

        Properties properties = ServiceLocator.getInstance(null).getProperties();

        String billAcctUpdateJobControlName = AppConstants.MAINT_BILL_GET_ASYN_BY_APP_PREFIX + appId;

        JobControlUtils jobControlUtils = new JobControlUtils();

        try {

            MaintainMessageResponse checkHasProcessDataResponse = checkHasProcessDataRequest(conn, maintMsgReq);
            if (checkHasProcessDataResponse != null) {
                return checkHasProcessDataResponse;
            }

            MaintainMessageResponse checkAllResponse = checkAllMessageRequest(conn, appId, maintMsgReq);
            if (checkAllResponse != null) {
                return checkAllResponse;
            }

            String checkTranIdResultCd = checkTranIdDuplicateAndOverLimit(maintMsgReq);
            if (ResultCodes.RESULT_CD_TRAN_ID_LENGTH_OVER_LIMIT.equals(checkTranIdResultCd)) {
                return getMaintainMessageResponse(ResultCodes.RESULT_CD_TRAN_ID_LENGTH_OVER_LIMIT,
                        ResultMessages.RESULT_MSG_TRAN_ID_LENGTH_OVER_LIMIT);
            }
            if (ResultCodes.RESULT_CD_TRAN_ID_IS_DUPLICATED.equals(checkTranIdResultCd)) {
                return getMaintainMessageResponse(ResultCodes.RESULT_CD_TRAN_ID_IS_DUPLICATED,
                        ResultMessages.RESULT_MSG_TRAN_ID_IS_DUPLICATED);
            }

            Map<String, String> billAcctServiceProviderMap = getServiceProviderByAppId(conn, appId);
            billAcctServiceProviderId = billAcctServiceProviderMap.get("SERVICE_PROVIDER_ID");
            String billAcctRegLinkupInd = billAcctServiceProviderMap.get("REQ_LINKUP_IND");

            MessageRequest[] msgReqArray = maintMsgReq.getMessageRequest();
            if (msgReqArray != null) {
                logInfo("processMessage - msgReqArray.length: " + msgReqArray.length);
            }

            List<MessageRequest> msgReqListToDCI = new ArrayList<MessageRequest>();

            for (int i = 0; i < msgReqArray.length; i++) {

                MessageRequest msgReq = msgReqArray[i];

                EMessage eMsg = msgReq.getEMessage();
                ToDoItem toDoItem = msgReq.getToDoItem();

                Application application = msgReq.getApplication();

                CommonIndicator_ST billAcctInfoInd = msgReq.getBillAccountInfoInd();

                MetaData[] metaDataArray = msgReq.getMetaData();

                MessageRequest msgReqToDCI = new MessageRequest();
                msgReqToDCI.setPortalID(msgReq.getPortalID());
                msgReqToDCI.setEMessage(eMsg);
                msgReqToDCI.setToDoItem(toDoItem);
                msgReqToDCI.setApplication(application);
                List<MetaData> metaDataListToDCI = new ArrayList<MetaData>();

                for (int j = 0; j < metaDataArray.length; j++) {
                    MetaData metaData = metaDataArray[j];

                    String dataContentEn = metaData.getDataContentEN();
                    String dataContentTc = metaData.getDataContentTC();
                    String dataContentSc = metaData.getDataContentSC();

                    MetaData metaDataToDCI = new MetaData();
                    metaDataToDCI.setDataContentEN(dataContentEn);
                    metaDataToDCI.setDataContentTC(dataContentTc);
                    metaDataToDCI.setDataContentSC(dataContentSc);

                    if (dataContentTc == null || dataContentTc.length() == 0)
                        dataContentTc = dataContentEn;

                    if (dataContentSc == null || dataContentSc.length() == 0)
                        dataContentSc = dataContentTc;

                    Recipient[] recipientArray = metaData.getRecipient();

                    List<Recipient> recipientListToDCI = new ArrayList<Recipient>();

                    response = maintainMessage(response, conn, msgReq.getPortalID(), recipientArray,
                            eMsg, toDoItem, application, dataContentEn, dataContentTc, dataContentSc, dataContentEn,
                            dataContentTc, dataContentSc,
                            recipientListToDCI);

                    logInfo("After maintainMessage, response ResultCd=" + response.getResultCode() + ", ResultMessage="
                            + response.getResultMessage()
                            + ", recipientListToDCI.size=" + recipientListToDCI.size());
                    for (Recipient recp : recipientListToDCI) {
                        logInfo("Recipient to DCI-CMC: Recipient=" + recp.toString());
                    }
                    if (recipientListToDCI.size() > 0) {
                        metaDataToDCI.setRecipient(recipientListToDCI.toArray(new Recipient[0]));
                        metaDataListToDCI.add(metaDataToDCI);
                    } else {
                        logInfo("No recipient to send to DCI-CMC for this meta-data. recipientListToDCI.size="
                                + recipientListToDCI.size());
                    }

                }

                logInfo("After processing all meta-data, metaDataListToDCI.size=" + metaDataListToDCI.size());
                if (metaDataListToDCI.size() > 0) {
                    msgReqToDCI.setMetaData(metaDataListToDCI.toArray(new MetaData[0]));
                    msgReqListToDCI.add(msgReqToDCI);
                } else {
                    logInfo("No meta-data to send to DCI-CMC for this message request. metaDataListToDCI.size="
                            + metaDataListToDCI.size());
                }

            }

            MaintainMessageClient maintainMessageClient = new MaintainMessageClient(properties);
            logInfo("prepare message request array for DCI-CMC, msgReqListToDCI.size=" + msgReqListToDCI.size());
            hk.gov.ogcio.mars_cmc.cmc.datatype.message.MessageRequest[] msgReqArrayToDCI = new hk.gov.ogcio.mars_cmc.cmc.datatype.message.MessageRequest[msgReqListToDCI
                    .size()];
            for (int i = 0; i < msgReqListToDCI.size(); i++) {
                logInfo("transform message request index " + i + " to datatype message request for DCI-CMC");
                logInfo("Before transform, msgReqListToDCI.get(i): " + msgReqListToDCI.get(i).toString());
                msgReqArrayToDCI[i] = MessageRequestTransformUtils
                        .transformToDatatypeMessageRequest(msgReqListToDCI.get(i));
                logInfo("After transform, msgReqArrayToDCI[" + i + "]: " + msgReqArrayToDCI[i].toString());
            }

            if (msgReqArrayToDCI.length > 0) {
                logInfo("Sending VIP message to DCI-CMC. msgReqArrayToDCI.length=" + msgReqArrayToDCI.length);
                SOAPMessage responseMsg = maintainMessageClient.sendMaintainMessageRequest(msgReqArrayToDCI);
                MaintainMessageResponse maintainMessageResponse = maintainMessageClient
                        .getMaintainMessageResponse(responseMsg);
                logInfo("Send VIP message to DCI-CMC. ResultCd=" + maintainMessageResponse.getResultCode()
                        + ", ResultMessage=" + maintainMessageResponse.getResultMessage());
                if (!ResultCodes.RESULT_CD_SUCCESS.equals(maintainMessageResponse.getResultCode())) {
                    logWarn("Send VIP message to DCI-CMC failed. ResultCode=" + maintainMessageResponse.getResultCode()
                            + ", ResultMessage=" + maintainMessageResponse.getResultMessage());
                    MaintainMessageResponse sendToDCICMCFailedResponse = new MaintainMessageResponse();
                    sendToDCICMCFailedResponse.setResultCode(ResultCodes.RESULT_CD_GENERAL_ERROR);
                    sendToDCICMCFailedResponse.setResultMessage(ResultMessages.RESULT_MSG_GENERAL_ERROR);
                    return sendToDCICMCFailedResponse;
                } else {
                    logInfo("Send VIP message to DCI-CMC successfully.");
                }
            } else {
                logInfo("No VIP message to send to DCI-CMC. msgReqArrayToDCI.length=" + msgReqArrayToDCI.length);
            }

            logInfo("[MaintMsg]processMessage - END");

        } catch (Exception ex) {
            logError("[MaintMsg]General exception caught in processMessage", ex);
            throw ex;
        }

        return getConcludedMaintainMessageResponse(response);
    }

    public Map<String, String> getUserByUserId(HPFW_Connection conn, String userId, String serviceProviderId)
            throws Exception {
        logDebug("[MaintMsg]getUserByUserId - START");
        ResultSet rs = null;
        HashMap<String, String> map = new HashMap<String, String>();
        try {
            logInfo("[MaintMsg]getUserByUserId - userId=" + userId);
            ArrayList<Parameter> paraList = new ArrayList<Parameter>();
            paraList.add(new Parameter(Parameter.String, serviceProviderId));
            paraList.add(new Parameter(Parameter.String, userId));
            rs = conn.getResultSet(SELECT_CMC_USER_INFO_BY_USER_ID, paraList);

            if (rs.next()) {
                map.put("USER_ID", rs.getString("USER_ID"));
                map.put("IDP_ID", rs.getString("IDP_ID"));
                map.put("USER_STATUS", rs.getString("USER_STATUS"));
                map.put("SERVICE_PROVIDER_ID", rs.getString("SERVICE_PROVIDER_ID"));
                map.put("LINK_STATUS", rs.getString("LINK_STATUS"));
                map.put("REJECT_IND", rs.getString("REJECT_IND"));
                map.put("HIDDEN_IND", rs.getString("HIDDEN_IND"));
                map.put("MY_ID_KEY", rs.getString("MY_ID_KEY"));
            }

            logDebug("[MaintMsg]getUserByUserId - END");
            return map;

        } catch (Exception ex) {
            logError("[MaintMsg]General exception caught in getUserByUserId", ex);
            throw ex;
        } finally {
            try {
                HPFW_Connection.close(rs);
            } catch (Exception ex) {
                logError("[MaintMsg]General exception caught in getUserByUserId - rs.close();", ex);
            }
        }
    }

    private MaintainMessageResponse maintainMessage(MaintainMessageResponse response, HPFW_Connection conn,
            String portalId, Recipient[] recipientArray,
            EMessage eMsg, ToDoItem toDoItem, Application application, String subjectEn, String subjectTc,
            String subjectSc, String contentEn, String contentTc, String contentSc,

            List<Recipient> recipientListToDCI

    ) throws Exception {
        logInfo("[MaintMsg]maintainMessage - START");

        if (eMsg != null) {
            if (eMsg.getTemplateID() != null) {
                logInfo("[MaintMsg]eMsg template id=" + eMsg.getTemplateID());
            }

            if (eMsg.getTemplateVersion() != null) {
                logInfo("[MaintMsg]eMsg template ver=" + eMsg.getTemplateVersion());
            }
        }

        if (toDoItem != null) {
            if (toDoItem.getTemplateID() != null) {
                logInfo("[MaintMsg]toDoItem template id=" + toDoItem.getTemplateID());
            }

            if (toDoItem.getTemplateVersion() != null) {
                logInfo("[MaintMsg]toDoItem template ver=" + toDoItem.getTemplateVersion());
            }
        }

        if (application != null) {
            if (application.getTemplateID() != null) {
                logInfo("[MaintMsg]application template id=" + application.getTemplateID());
            }

            if (application.getTemplateVersion() != null) {
                logInfo("[MaintMsg]application template ver=" + application.getTemplateVersion());
            }
        }

        if (recipientArray != null) {
            logInfo("[MaintMsg]recipient count=" + recipientArray.length);
        }

        Properties properties = ServiceLocator.getInstance(null).getProperties();

        int subjectSizeLimit = Integer
                .parseInt(properties.getProperty(CmcAppPropertyNames.MAINT_MSG_SUBJ_SIZE_LIMIT_PROPERTY_NAME));
        int contentSizeLimit = Integer
                .parseInt(properties.getProperty(CmcAppPropertyNames.MAINT_MSG_CONTENT_SIZE_LIMIT_PROPERTY_NAME));

        String messageId = "";
        String toDoItemId = "";
        String userInd = "";
        String eMsgServiceProviderId = "";
        String toDoItemServiceProviderId = "";

        String applicationServiceProviderId = "";

        String requestServiceProviderId = "";
        String reqLinkupInd = "";
        int toDoItemCutOffDay = 0;
        String emailId = "";

        String mobileId = "";

        String esClientId = "";
        String openId = "";
        String iasNotiId = "";

        String iasMsgId = "";
        String iasOptSpId = "";

        String eMsgIasInd = "";
        boolean eMsgIasOptCheck = false;
        boolean eMsgIasAutoCreate = false;
        String eMsgDefaultFolderName = "";
        String eMsgEmailInd = "";
        String eMsgAlertEmailInd = "";
        String eMsgMobileMsgInd = "";
        String eMsgTemplateNotiPriority = "";

        String toDoItemIasInd = "";
        boolean toDoItemIasOptCheck = false;
        boolean toDoItemIasAutoCreate = false;
        String toDoItemDefaultFolderName = "";
        String toDoItemEmailInd = "";
        String toDoItemAlertEmailInd = "";
        String toDoItemMobileMsgInd = "";
        String toDoItemTemplateNotiPriority = "";

        String iasIdpId = properties.getProperty(CmcAppPropertyNames.IAS_IDP_ID_PROPERTY_NAME);
        String myGovSpId = properties.getProperty(CmcAppPropertyNames.MYGOV_SP_ID_PROPERTY_NAME);
        String myGovClientId = properties.getProperty(CmcAppPropertyNames.MYGOV_CLIENT_ID_PROPERTY_NAME);
        String iasEsAppSuffixTagName = properties.getProperty(CmcAppPropertyNames.IAS_ES_APP_SUFFIX_TAG_NAME_PROPERTY_NAME);
        String serverId = properties.getProperty(CmcAppPropertyNames.SERVER_ID_PROPERTY_NAME);

        String iasToDoItemId = "";

        String iasApplicationId = "";

        String encKeyStoreId = properties.getProperty(CmcAppPropertyNames.CMC_MESSAGE_ENCRYPT_KEY_STORE_ID_PROPERTY_NAME);

        boolean eMsgCreated = false;
        boolean toDoItemCreated = false;
        boolean hasInvalidField = false;
        boolean hasToDoItemInRequest = false;

        boolean hasApplicationInRequest = false;

        boolean isBroadcastMsg = false;

        Map eMsgTemlFieldMap = null;
        Map toDoItemTemlFieldMap = null;

        Map<String, String> iasApplicationTemplateFieldMap = null;
        CmcTemplate cmcMsgTemplate = null;
        CmcTemplate cmcToDoItemTemplate = null;
        CmcTemplate cmcApplicationTemplate = null;
        CmcTemplateDao cmcTemplateDao = new CmcTemplateDao();

        if (eMsg != null) {

            eMsgTemlFieldMap = getTemplateByIdVersion(conn, eMsg.getTemplateID(), eMsg.getTemplateVersion(), "M");
            eMsgServiceProviderId = (String) eMsgTemlFieldMap.get("SERVICE_PROVIDER_ID");

            esClientId = (String) eMsgTemlFieldMap.get("CLIENT_ID");
            eMsgIasInd = (String) eMsgTemlFieldMap.get("IAS_IND");

            eMsgIasOptCheck = "Y".equals((String) eMsgTemlFieldMap.get("IAS_SHOW_ES_SET_BTN"));
            eMsgIasAutoCreate = "Y".equals((String) eMsgTemlFieldMap.get("IAS_AUTO_CREATE_IND"));
            iasOptSpId = (String) eMsgTemlFieldMap.get("IAS_OPT_SP_ID");

            reqLinkupInd = (String) eMsgTemlFieldMap.get("REQ_LINKUP_IND");
            eMsgDefaultFolderName = (String) eMsgTemlFieldMap.get("DEFAULT_FOLDER_ID");
            requestServiceProviderId = eMsgServiceProviderId;
            eMsgEmailInd = (String) eMsgTemlFieldMap.get("EMAIL_IND");
            eMsgAlertEmailInd = (String) eMsgTemlFieldMap.get("ALERT_EMAIL_IND");

            eMsgMobileMsgInd = (String) eMsgTemlFieldMap.get("MOBILE_MSG_IND");

            eMsgTemplateNotiPriority = (String) eMsgTemlFieldMap.get("NOTI_PRIORITY");

            logInfo("[MaintMsg]eMsgEmailInd=" + eMsgEmailInd);

            cmcMsgTemplate = cmcTemplateDao.getTemplateByIdVersion(conn, eMsg.getTemplateID(),
                    eMsg.getTemplateVersion(), AppConstants.EMSG_TEMPLATE_TYPE);

        }

        if (toDoItem != null) {

            toDoItemTemlFieldMap = getTemplateByIdVersion(conn, toDoItem.getTemplateID(), toDoItem.getTemplateVersion(),
                    "I");
            toDoItemServiceProviderId = (String) toDoItemTemlFieldMap.get("SERVICE_PROVIDER_ID");
            requestServiceProviderId = toDoItemServiceProviderId;

            esClientId = (String) toDoItemTemlFieldMap.get("CLIENT_ID");
            toDoItemIasInd = (String) toDoItemTemlFieldMap.get("IAS_IND");

            toDoItemIasAutoCreate = "Y".equals((String) toDoItemTemlFieldMap.get("IAS_AUTO_CREATE_IND"));
            iasOptSpId = (String) toDoItemTemlFieldMap.get("IAS_OPT_SP_ID");
            cmcToDoItemTemplate = cmcTemplateDao.getTemplateByIdVersion(conn, toDoItem.getTemplateID(),
                    toDoItem.getTemplateVersion(), AppConstants.TO_DO_ITEM_TEMPLATE_TYPE);

            if (isNewOrReplace(recipientArray)) {

                if (eMsg == null) {
                    reqLinkupInd = (String) toDoItemTemlFieldMap.get("REQ_LINKUP_IND");

                }

                String cutOffDateStr = (String) toDoItemTemlFieldMap.get("CUT_OFF_DATE");
                if (cutOffDateStr != null)
                    toDoItemCutOffDay = Integer.parseInt(cutOffDateStr);
            }
            hasToDoItemInRequest = true;

        }

        if (application != null) {
            iasApplicationTemplateFieldMap = getTemplateByIdVersion(conn, application.getTemplateID(),
                    application.getTemplateVersion(), AppConstants.APPLICATION_TEMPLATE_TYPE);
            cmcApplicationTemplate = cmcTemplateDao.getTemplateByIdVersion(conn, application.getTemplateID(),
                    application.getTemplateVersion(), AppConstants.APPLICATION_TEMPLATE_TYPE);
            applicationServiceProviderId = (String) iasApplicationTemplateFieldMap.get("SERVICE_PROVIDER_ID");
            requestServiceProviderId = applicationServiceProviderId;
            esClientId = (String) iasApplicationTemplateFieldMap.get("CLIENT_ID");

            iasOptSpId = (String) iasApplicationTemplateFieldMap.get("IAS_OPT_SP_ID");
            hasApplicationInRequest = true;
        }

        HashSet duplicateRecptSet = getDuplicateRecipientSet(recipientArray, iasIdpId);

        HashSet<String> duplicateIasRecipientSet = IasRecipientUtils.getIasDuplicationRecipientHashSet(recipientArray,
                iasIdpId);

        int impDtUpperLimit = Integer
                .parseInt(properties.getProperty(CmcAppPropertyNames.TTO_DO_ITEM_IMPORTANT_DT_UPPER_LIMIT_PROPERTY_NAME));

        boolean eMsgSizeInvalid = false;
        boolean eMailSizeInvalid = false;

        boolean eMobileSizeInvalid = false;

        boolean toDoItemSizeNotValid = false;
        MessageResponse validateMsgSizeErrorResp = null;
        MessageResponse validateEmailSizeErrorResp = null;
        MessageResponse validateToDoItemSizeErrorResp = null;

        MessageResponse validateMobileSizeErrorResp = null;

        MessageResponse validateIasSizeErrorResp = null;
        boolean iasMsgSizeInvalid = false;

        MessageResponse validateIasToDoItemSizeErrorResp = null;
        boolean iasToDoItemSizeInvalid = false;

        MessageResponse validateIasApplicationSizeErrorResp = null;
        boolean iasApplicationSizeInvalid = false;

        String mergedEmailSubjectEn = "";
        String mergedEmailSubjectTc = "";
        String mergedEmailSubjectSc = "";
        String mergedEmailContentEn = "";
        String mergedEmailContentTc = "";
        String mergedEmailContentSc = "";

        String mergedMobileSubjectEn = "";
        String mergedMobileSubjectTc = "";
        String mergedMobileSubjectSc = "";
        String mergedMobileContentEn = "";
        String mergedMobileContentTc = "";
        String mergedMobileContentSc = "";

        List<MessageParam> msgParm_MyMessage;
        List<MessageParam> msgParm_InternetMsg;
        List<MessageParam> msgParm_MobileMsg;
        List<MessageParam> msgParm_AllMessage;
        List<MessageParam> msgParm_ToDoMessage;
        List<MessageParam> msgParm_IasMsg;

        List<MessageParam> cmcMsgParamIasToDoItem;

        List<MessageParam> cmcMsgParamIasApplication;

        msgParm_MyMessage = getMsgParamByMsgType(conn, MessageParam.MESSAGE_TYPE_MY_MSG);
        msgParm_InternetMsg = getMsgParamByMsgType(conn, MessageParam.MESSAGE_TYPE_INTERNET_MSG);
        msgParm_MobileMsg = getMsgParamByMsgType(conn, MessageParam.MESSAGE_TYPE_MOBILE_MSG);
        msgParm_AllMessage = getMsgParamByMsgType(conn, MessageParam.MESSAGE_TYPE_ALL_MSG);
        msgParm_ToDoMessage = getMsgParamByMsgType(conn, MessageParam.MESSAGE_TYPE_TODO_MSG);
        msgParm_IasMsg = getMsgParamByMsgType(conn, MessageParam.MESSAGE_TYPE_IAS_MSG);

        cmcMsgParamIasToDoItem = getMsgParamByMsgType(conn, MessageParam.MESSAGE_TYPE_IAS_TO_DO_ITEM);

        cmcMsgParamIasApplication = getMsgParamByMsgType(conn, MessageParam.MESSAGE_TYPE_IAS_APPLICATION);

        updateStaticSystemParam(msgParm_MyMessage);
        updateStaticSystemParam(msgParm_InternetMsg);
        updateStaticSystemParam(msgParm_MobileMsg);
        updateStaticSystemParam(msgParm_ToDoMessage);
        updateStaticSystemParam(msgParm_IasMsg);
        updateStaticSystemParam(msgParm_AllMessage);

        msgParm_MyMessage.addAll(msgParm_AllMessage);
        msgParm_InternetMsg.addAll(msgParm_AllMessage);
        msgParm_MobileMsg.addAll(msgParm_AllMessage);
        msgParm_ToDoMessage.addAll(msgParm_AllMessage);
        msgParm_IasMsg.addAll(msgParm_AllMessage);

        cmcMsgParamIasToDoItem.addAll(msgParm_AllMessage);

        cmcMsgParamIasApplication.addAll(msgParm_AllMessage);

        List<String> myIdKeyList = new ArrayList<String>();

        Map<String, IasUserWrapped> validatedIasUserWrappedMap = new HashMap<String, IasUserWrapped>();
        List<String> iasMsgCreatedNotiIdList = new ArrayList<String>();
        List<IasUser> iasUserList = new ArrayList<IasUser>();
        List<IasUser> missingIasUserList = new ArrayList<IasUser>();

        Map<String, Map<String, String>> validatedUserMap = new HashMap<String, Map<String, String>>();

        for (int r = 0; r < recipientArray.length; r++) {

            Recipient recipient = recipientArray[r];

            if (iasIdpId.equals(recipient.getIdpID())) {

                IasUserNotiInfoDAO iasUserNotiInfoDAO = new IasUserNotiInfoDAO();
                IasUserWrapped iasUserWrapped = null;

                if (RecipientIDTypeConstant.HKID.equals(recipient.getRecipientIDType())) {

                    String hkidHashed = EncryptionUtils.hashString(recipient.getRecipientID());
                    List<IasUser> getByHkidIasUserList = iasUserNotiInfoDAO.getIasUserNotiInfoByHKIDHashed(conn,
                            iasOptSpId, hkidHashed);
                    if (getByHkidIasUserList.size() <= 0) {

                        IasUser iasUser = new IasUser();
                        iasUser.setClientId(esClientId);
                        iasUser.setHkidHashed(hkidHashed);

                        String hkidEncrypted = EncUtils.encrypt(recipient.getRecipientID(),
                                properties.getProperty(CmcAppPropertyNames.HKID_ENCRYPT_PASSPHRASE_ID_PROPERTY_NAME),
                                properties.getProperty(CmcAppPropertyNames.HKID_ENCRYPT_PASSPHRASE_USAGE_PROPERTY_NAME));

                        iasUser.setHkidEncrypted(hkidEncrypted);
                        iasUser.setStatus(Constants.IAS_USER_STATUS_MISSING);
                        logInfo("HKID, getByHkidIasUserList.size() <= 0, recipient.getTranID: " + recipient.getTranID()
                                + ", iasUser: " + iasUser.toString());

                        iasUserWrapped = new IasUserWrapped();
                        iasUserWrapped.setIasUser(iasUser);
                        iasUserWrapped.setHkid(recipient.getRecipientID());
                    } else if (getByHkidIasUserList.size() == 1) {
                        IasUser iasUser = getByHkidIasUserList.get(0);
                        logInfo("HKID, getByHkidIasUserList.size() == 1, recipient.getTranID: " + recipient.getTranID()
                                + ", iasUser: " + iasUser.toString());

                        iasUserWrapped = new IasUserWrapped();

                        String hkidDecrypted = EncUtils.decrypt(
                                iasUser.getHkidEncrypted(),
                                properties.getProperty(CmcAppPropertyNames.HKID_ENCRYPT_PASSPHRASE_ID_PROPERTY_NAME),
                                properties.getProperty(CmcAppPropertyNames.HKID_ENCRYPT_PASSPHRASE_USAGE_PROPERTY_NAME));
                        if (hkidDecrypted.equals(recipient.getRecipientID())) {
                            iasUserWrapped.setIasUser(iasUser);
                            iasUserWrapped.setHkid(recipient.getRecipientID());
                            iasMsgCreatedNotiIdList.add(iasUser.getNotiId());
                        } else {
                            IasUser iasUserNew = new IasUser();
                            logInfo("HKID collision, getByHkidIasUserList.size() == 1, recipient.getTranID: "
                                    + recipient.getTranID() + ", iasUser: " + iasUser.toString());
                            iasUserNew.setClientId(esClientId);
                            iasUserNew.setHkidHashed(hkidHashed);
                            String hkidEncrypted = EncUtils.encrypt(
                                    recipient.getRecipientID(),
                                    properties.getProperty(CmcAppPropertyNames.HKID_ENCRYPT_PASSPHRASE_ID_PROPERTY_NAME),
                                    properties
                                            .getProperty(CmcAppPropertyNames.HKID_ENCRYPT_PASSPHRASE_USAGE_PROPERTY_NAME));
                            iasUserNew.setHkidEncrypted(hkidEncrypted);
                            iasUserNew.setStatus(Constants.IAS_USER_STATUS_MISSING);
                            iasUserWrapped.setIasUser(iasUserNew);
                            iasUserWrapped.setHkid(recipient.getRecipientID());
                        }

                    } else {

                        for (IasUser iasUserItem : getByHkidIasUserList) {

                            String hkidDecrypted = EncUtils.decrypt(
                                    iasUserItem.getHkidEncrypted(),
                                    properties.getProperty(CmcAppPropertyNames.HKID_ENCRYPT_PASSPHRASE_ID_PROPERTY_NAME),
                                    properties
                                            .getProperty(CmcAppPropertyNames.HKID_ENCRYPT_PASSPHRASE_USAGE_PROPERTY_NAME));

                            if (recipient.getRecipientID().equals(hkidDecrypted)) {
                                IasUser iasUser = iasUserItem;
                                logInfo("HKID, getByHkidIasUserList.size() > 1, recipient.getTranID: "
                                        + recipient.getTranID() + ", iasUser: " + iasUser.toString());

                                iasUserWrapped = new IasUserWrapped();
                                iasUserWrapped.setIasUser(iasUser);
                                iasUserWrapped.setHkid(recipient.getRecipientID());

                                iasMsgCreatedNotiIdList.add(iasUserItem.getNotiId());
                                break;
                            } else {

                                IasUser iasUser = new IasUser();
                                iasUser.setClientId(esClientId);
                                iasUser.setHkidHashed(hkidHashed);
                                String hkidEncrypted = EncUtils.encrypt(recipient.getRecipientID(),
                                        properties
                                                .getProperty(CmcAppPropertyNames.HKID_ENCRYPT_PASSPHRASE_ID_PROPERTY_NAME),
                                        properties.getProperty(
                                                CmcAppPropertyNames.HKID_ENCRYPT_PASSPHRASE_USAGE_PROPERTY_NAME));
                                iasUser.setHkidEncrypted(hkidEncrypted);
                                iasUser.setStatus(Constants.IAS_USER_STATUS_MISSING);
                                logInfo("HKID, getByHkidIasUserList.size() > 1, but all matched hash decrypted HKID is not match the recipientId, recipient.getTranID: "
                                        + recipient.getTranID() + ", iasUser: " + iasUser.toString());
                                iasUserWrapped = new IasUserWrapped();
                                iasUserWrapped.setIasUser(iasUser);
                                iasUserWrapped.setHkid(recipient.getRecipientID());

                            }
                        }
                    }

                } else {

                    IasUser iasUser = iasUserNotiInfoDAO.getIasUserNotiInfoByOpenId(conn, iasOptSpId,
                            recipient.getRecipientID());
                    if (iasUser == null) {
                        iasUser = new IasUser();
                        iasUser.setClientId(esClientId);
                        iasUser.setOpenId(recipient.getRecipientID());
                        iasUser.setStatus(Constants.IAS_USER_STATUS_MISSING);
                    } else {
                        iasMsgCreatedNotiIdList.add(iasUser.getNotiId());
                    }
                    logInfo("OPEN_ID, recipient.getTranID: " + recipient.getTranID() + ", iasUser: "
                            + iasUser.toString());

                    iasUserWrapped = new IasUserWrapped();
                    iasUserWrapped.setIasUser(iasUser);

                }

                logInfo("IasRecipientValidator.validateIasRecipient - recipient == null: " + (recipient == null)
                        + ", iasUserWrapped == null: " + (iasUserWrapped == null));

                MaintainMessageResponse validateRecipientMaintainMessageResponse = IasRecipientValidator
                        .validateIasRecipient(
                                conn, recipient, iasUserWrapped.getIasUser(), duplicateIasRecipientSet,

                                toDoItem != null, application != null, eMsg != null);

                if (validateRecipientMaintainMessageResponse.getMessageResponse() != null
                        && validateRecipientMaintainMessageResponse.getMessageResponse().length > 0) {
                    for (MessageResponse messageResponse : validateRecipientMaintainMessageResponse
                            .getMessageResponse()) {
                        response.addMessageResponse(messageResponse);
                    }
                } else {

                    validatedIasUserWrappedMap.put(recipient.getRecipientID(), iasUserWrapped);
                }
            }

        }

        logInfo("validatedIasUserWrappedMap.size()=" + validatedIasUserWrappedMap.size());
        logInfo("myIdKeyList=" + myIdKeyList.size());

        Map<String, MarsUserObject> userProfileHash = MarsUserAttributeDAO.getMarsUserByMyIdKey(conn.getConnectionPtr(),
                myIdKeyList);

        String cmcUserMsgBroadcast = "N";
        String cmcUserEmailBroadcast = "N";
        String cmcUserMobileBroadcast = "N";

        int userSendEmailCount = 0;
        int userSendMobileMsgCount = 0;

        boolean isSPAllowedToSendInternetMessage = false;
        boolean isSPAllowedToSendMobileMessage = false;

        Map<String, String> serviceProviderMap = getServiceProviderInfoBySPID(conn, requestServiceProviderId);
        String spEmailInd = (String) serviceProviderMap.get("EMAIL_IND");
        String spMobileInd = (String) serviceProviderMap.get("MOBILE_IND");

        if (recipientArray.length > 1) {
            isBroadcastMsg = true;
            cmcUserMsgBroadcast = "Y";
        }

        if ("Y".equals(spEmailInd)) {
            isSPAllowedToSendInternetMessage = true;
        }

        if ("Y".equals(spMobileInd)) {
            isSPAllowedToSendMobileMessage = true;
        }

        for (int i = 0; i < myIdKeyList.size(); i++) {

            if ("N".equals(cmcUserEmailBroadcast) || "N".equals(cmcUserMobileBroadcast)) {

                if (!isSPAllowedToSendInternetMessage && !isSPAllowedToSendMobileMessage) {
                    break;
                }

                MarsUserObject userData = userProfileHash.get(myIdKeyList.get(i));
                logInfo("userData.getReceiveInternetMessage() = " + userData.getReceiveInternetMessage());
                if (isSPAllowedToSendInternetMessage) {
                    if ("N".equals(cmcUserEmailBroadcast)) {
                        if ("Y".equals(userData.getReceiveInternetMessage()) && userData.isEmailAddressActive()) {
                            userSendEmailCount++;
                            if (userSendEmailCount > 1) {
                                cmcUserEmailBroadcast = "Y";
                            }
                        }
                    }
                }

                if (isSPAllowedToSendMobileMessage) {
                    if ("N".equals(cmcUserMobileBroadcast)) {
                        if (userData.getMobileDeviceID() != null) {
                            userSendMobileMsgCount++;
                            if (userSendMobileMsgCount > 1) {
                                cmcUserMobileBroadcast = "Y";
                            }
                        }
                    }
                }

            } else {
                break;
            }

        }

        IasToDoItemController iasToDoItemController = new IasToDoItemController();
        IasApplicationController iasApplicationController = new IasApplicationController();

        for (int r = 0; r < recipientArray.length; r++) {
            Recipient recipient = recipientArray[r];
            logInfo("recipient.getIdpID: " + recipient.getIdpID() + ", recipient.getCorrelatedTranID: "
                    + recipient.getCorrelatedTranID()
                    + ", recipient.getTranID: " + recipient.getTranID() + ", recipient.getAction: "
                    + recipient.getAction()
                    + ", eMsgIasAutoCreate: " + eMsgIasAutoCreate + ", toDoItemIasAutoCreate: "
                    + toDoItemIasAutoCreate);

            boolean isIasMessage = iasIdpId.equals(recipient.getIdpID());

            logInfo("isIasMessage=" + isIasMessage);

            if (isIasMessage) {

                int action = Action_ST.NEW_TYPE;
                if (recipient.getAction() != null) {
                    action = recipient.getAction().getType();
                }
                logInfo("action=" + action);

                IasUserWrapped validatedIasUserWrapped = validatedIasUserWrappedMap.get(recipient.getRecipientID());
                if (validatedIasUserWrapped != null) {
                    IasUser validatedIasUser = validatedIasUserWrapped.getIasUser();

                    SingleMaintainMsgResult processIasToDoItemResult = null;
                    SingleMaintainMsgResult processIasApplicationResult = null;

                    if (eMsg != null) {

                        MessageResponse msgRsp = null;

                        if (iasMsgId == null || "".equals(iasMsgId)) {

                            iasMsgId = IasUtils.getNextIasMsgId(serverId);

                            String mergedIasSubjectEn = null;
                            String mergedIasSubjectTc = null;
                            String mergedIasSubjectSc = null;
                            String mergedIasContentEn = null;
                            String mergedIasContentTc = null;
                            String mergedIasContentSc = null;

                            String templateSubjecEn = (String) eMsgTemlFieldMap.get("IAS_SUBJECT_EN");
                            String templateSubjecTc = (String) eMsgTemlFieldMap.get("IAS_SUBJECT_TC");
                            String templateSubjecSc = (String) eMsgTemlFieldMap.get("IAS_SUBJECT_SC");

                            if (templateSubjecEn != null && templateSubjecEn.length() > 0) {
                                mergedIasSubjectEn = getMergedContent(eMsgTemlFieldMap, subjectEn, subjectTc, subjectSc,
                                        "IAS_SUBJECT", LANGUAGE_EN, msgParm_IasMsg, null, false);
                                mergedIasContentEn = getMergedContent(eMsgTemlFieldMap, contentEn, contentTc, contentSc,
                                        "IAS_CONTENT", LANGUAGE_EN, msgParm_IasMsg, null, false);
                            } else {
                                mergedIasSubjectEn = null;
                                mergedIasContentEn = null;
                            }

                            if (templateSubjecTc != null && templateSubjecTc.length() > 0) {
                                mergedIasSubjectTc = getMergedContent(eMsgTemlFieldMap, subjectEn, subjectTc, subjectSc,
                                        "IAS_SUBJECT", LANGUAGE_TC, msgParm_IasMsg, null, false);
                                mergedIasContentTc = getMergedContent(eMsgTemlFieldMap, contentEn, contentTc, contentSc,
                                        "IAS_CONTENT", LANGUAGE_TC, msgParm_IasMsg, null, false);
                            } else {
                                mergedIasSubjectTc = null;
                                mergedIasContentTc = null;
                            }

                            if (templateSubjecSc != null && templateSubjecSc.length() > 0) {
                                mergedIasSubjectSc = getMergedContent(eMsgTemlFieldMap, subjectEn, subjectTc, subjectSc,
                                        "IAS_SUBJECT", LANGUAGE_SC, msgParm_IasMsg, null, false);
                                mergedIasContentSc = getMergedContent(eMsgTemlFieldMap, contentEn, contentTc, contentSc,
                                        "IAS_CONTENT", LANGUAGE_SC, msgParm_IasMsg, null, false);
                            } else {
                                mergedIasSubjectSc = null;
                                mergedIasContentSc = null;
                            }

                            validateIasSizeErrorResp = validateIasMsgSize(eMsgTemlFieldMap, mergedIasSubjectEn,
                                    mergedIasSubjectTc, mergedIasSubjectSc, mergedIasContentEn, mergedIasContentTc,
                                    mergedIasContentSc, properties);

                            if (validateIasSizeErrorResp != null) {
                                iasMsgSizeInvalid = true;
                                response.addMessageResponse(
                                        getRecipientInvalidResponse(recipient, validateIasSizeErrorResp));
                            } else {

                                IasMessage_ iasMessage = new IasMessage_();
                                iasMessage.setIasMsgId(iasMsgId);
                                iasMessage.setPortalId(portalId);
                                iasMessage.setTemplateId(eMsg.getTemplateID());
                                iasMessage.setTemplateVersion(eMsg.getTemplateVersion());
                                iasMessage.setSubjectEn(mergedIasSubjectEn);
                                iasMessage.setSubjectTc(mergedIasSubjectTc);
                                iasMessage.setSubjectSc(mergedIasSubjectSc);

                                mergedIasContentEn = EncUtils.encrypt(mergedIasContentEn);
                                mergedIasContentTc = EncUtils.encrypt(mergedIasContentTc);
                                mergedIasContentSc = EncUtils.encrypt(mergedIasContentSc);
                                iasMessage.setEncInd(Constants.ENC_IND_YES);
                                iasMessage.setEncKeyStoreId(encKeyStoreId);

                                iasMessage.setContentEn(mergedIasContentEn);
                                iasMessage.setContentTc(mergedIasContentTc);
                                iasMessage.setContentSc(mergedIasContentSc);

                                iasMessage.setIasEsAppSuffixEn(cmcMsgTemplate.getIasEsAppSuffixEn());
                                iasMessage.setIasEsAppSuffixTc(cmcMsgTemplate.getIasEsAppSuffixTc());
                                iasMessage.setIasEsAppSuffixSc(cmcMsgTemplate.getIasEsAppSuffixSc());
                                if (getNodeValueFromMetaData(contentEn, iasEsAppSuffixTagName) != null) {
                                    iasMessage.setIasEsAppSuffixEn(
                                            getNodeValueFromMetaData(contentEn, iasEsAppSuffixTagName));
                                }
                                if (getNodeValueFromMetaData(contentTc, iasEsAppSuffixTagName) != null) {
                                    iasMessage.setIasEsAppSuffixTc(
                                            getNodeValueFromMetaData(contentTc, iasEsAppSuffixTagName));
                                }
                                if (getNodeValueFromMetaData(contentSc, iasEsAppSuffixTagName) != null) {
                                    iasMessage.setIasEsAppSuffixSc(
                                            getNodeValueFromMetaData(contentSc, iasEsAppSuffixTagName));
                                }

                                conn.begin(null, conn.getLastUpTime(), HPFW_Connection.DIRECT);
                                iasMessage.insert(conn);
                                conn.begin(null, conn.getLastUpTime(), HPFW_Connection.DIRECT_WITH_HISTORY);
                            }
                        }

                        if (!iasMsgSizeInvalid) {

                            msgRsp = IasMsgValidator.validateRecipientIdTypeOnlyEmptyOrOpenId(
                                    cmcMsgTemplate.getClientId(),
                                    recipient.getIdpID(), recipient.getRecipientID(), recipient.getTranID(),
                                    recipient.getRecipientIDType());

                            if (msgRsp != null) {
                                response.addMessageResponse(msgRsp);
                            } else {

                                msgRsp = IasMsgValidator.validateOptInStatus(cmcMsgTemplate.getClientId(),
                                        recipient.getIdpID(),
                                        recipient.getRecipientID(), recipient.getTranID(), eMsgIasOptCheck,
                                        validatedIasUser.getOptIn());

                                if (msgRsp != null) {
                                    response.addMessageResponse(msgRsp);
                                } else {

                                    msgRsp = IasMsgValidator.validateAction(cmcMsgTemplate.getClientId(),
                                            recipient.getIdpID(),
                                            recipient.getRecipientID(), recipient.getTranID(), action);
                                    if (msgRsp != null) {
                                        response.addMessageResponse(msgRsp);
                                    } else {

                                        IasUserMessage_ iasUserMsg = new IasUserMessage_();
                                        iasUserMsg.setClientId(esClientId);
                                        iasUserMsg.setOpenId(recipient.getRecipientID());
                                        iasUserMsg.setIasMsgId(iasMsgId);
                                        iasUserMsg.setTranId(recipient.getTranID());
                                        iasUserMsg.setReadInd(Constants.READ_IND_UNREAD);
                                        iasUserMsg.setDeleteInd(Constants.DELETE_IND_NOT_DELETED);
                                        iasUserMsg.setIasNotiStatus(Constants.IAS_NOTI_STATUS_NEW);

                                        if ((validatedIasUser != null)
                                                && (!Constants.IAS_USER_STATUS_MISSING
                                                        .equals(validatedIasUser.getStatus()))
                                                && (validatedIasUser.getNotiId() != null)
                                                && (validatedIasUser.getNotiId().length() > 0)) {
                                            iasUserMsg.setNotiId(validatedIasUser.getNotiId());
                                        }

                                        iasUserMsg.insert(conn);

                                        if ((validatedIasUser != null) && (validatedIasUser.getNotiId() != null)
                                                && (Constants.OPT_IN_U.equals(validatedIasUser.getOptIn()))) {
                                            IasEsNotiMap_ notiMap = new IasEsNotiMap_(conn,
                                                    validatedIasUser.getNotiId(), eMsgServiceProviderId);
                                            notiMap.setOptIn(Constants.OPT_IN_Y);
                                            notiMap.update(conn);
                                        }

                                        response.addMessageResponse(ResponseUtils.getMessageResponse(
                                                recipient.getTranID(), recipient.getIdpID(), recipient.getRecipientID(),
                                                MsgTypeConstant.MESSAGE, ResultCodes.RESULT_CD_TRAN_SUCCESS,
                                                ResultMessages.RESULT_MSG_TRAN_SUCCESS));

                                    }
                                }
                            }
                        }

                    }

                    logInfo("prepare to process toDoItem, toDoItem: " + toDoItem);
                    if (toDoItem != null

                    ) {
                        try {
                            processIasToDoItemResult = iasToDoItemController.processIasToDoItem(
                                    portalId, iasToDoItemId,
                                    toDoItem, recipient, validatedIasUserWrapped,
                                    toDoItemCutOffDay, impDtUpperLimit,
                                    subjectEn, subjectTc, subjectSc,
                                    contentEn, contentTc, contentSc,
                                    cmcToDoItemTemplate, cmcMsgParamIasToDoItem, conn,
                                    m_iasToDoItemHistory, response);
                            if (processIasToDoItemResult.isSuccess()) {
                                response.addMessageResponse(
                                        ResponseUtils.getMessageResponse(recipient.getTranID(), recipient.getIdpID(),
                                                recipient.getRecipientID(),
                                                MsgTypeConstant.TO_DO_ITEM, ResultCodes.RESULT_CD_TRAN_SUCCESS,
                                                ResultMessages.RESULT_MSG_TRAN_SUCCESS));
                            }
                            iasToDoItemId = processIasToDoItemResult.getCreatedMsgId();
                        } catch (Exception e) {
                            response.addMessageResponse(
                                    ResponseUtils.getMessageResponse(recipient.getTranID(), recipient.getIdpID(),
                                            recipient.getRecipientID(),
                                            MsgTypeConstant.TO_DO_ITEM, ResultCodes.RESULT_CD_GENERAL_ERROR,
                                            ResultMessages.RESULT_MSG_GENERAL_ERROR));
                            logInfo("processIasToDoItem - iasToDoItemController.processIasToDoItem failed"
                                    + ". cmcTemplate.getClientId: " + cmcToDoItemTemplate.getClientId()
                                    + ", cmcTemplate.getTemplateId: " + cmcToDoItemTemplate.getTemplateId()
                                    + ", cmcTemplate.getTemplateVersion: " + cmcToDoItemTemplate.getTemplateVersion()
                                    + ", recipient.getTranID: " + recipient.getTranID() + ", recipient.getIdpID: "
                                    + recipient.getIdpID()
                                    + ", msgType: " + MsgTypeConstant.TO_DO_ITEM + ", TranResultCode: "
                                    + ResultCodes.RESULT_CD_GENERAL_ERROR + ", TranResultMessage: "
                                    + ResultMessages.RESULT_MSG_GENERAL_ERROR);
                            logInfo("processIasToDoItem - iasToDoItemController.processIasToDoItem exception: " + e);
                        }
                    }

                    logInfo("prepare to process application, application: " + application);
                    if (application != null

                    ) {
                        try {
                            processIasApplicationResult = iasApplicationController.processIasApplication(
                                    portalId, iasApplicationId,
                                    application, recipient, validatedIasUserWrapped,
                                    subjectEn, subjectTc, subjectSc,
                                    contentEn, contentTc, contentSc,
                                    cmcApplicationTemplate, cmcMsgParamIasApplication, conn,
                                    m_iasApplicationHistory, response);
                            if (processIasApplicationResult.isSuccess()) {
                                response.addMessageResponse(
                                        ResponseUtils.getMessageResponse(recipient.getTranID(), recipient.getIdpID(),
                                                recipient.getRecipientID(),
                                                MsgTypeConstant.APPLICATION, ResultCodes.RESULT_CD_TRAN_SUCCESS,
                                                ResultMessages.RESULT_MSG_TRAN_SUCCESS));
                            }
                            iasApplicationId = processIasApplicationResult.getCreatedMsgId();
                        } catch (Exception e) {
                            logInfo("processIasApplication - iasApplicationController.processIasApplication failed"
                                    + ". cmcTemplate.getClientId: " + cmcApplicationTemplate.getClientId()
                                    + ", cmcTemplate.getTemplateId: " + cmcApplicationTemplate.getTemplateId()
                                    + ", cmcTemplate.getTemplateVersion: " + cmcApplicationTemplate.getTemplateVersion()
                                    + ", recipient.getTranID: " + recipient.getTranID() + ", recipient.getAppRefNum: "
                                    + recipient.getAppRefNum() + ", recipient.getIdpID: " + recipient.getIdpID()
                                    + ", msgType: " + MsgTypeConstant.APPLICATION + ", TranResultCode: "
                                    + ResultCodes.RESULT_CD_GENERAL_ERROR + ", TranResultMessage: "
                                    + ResultMessages.RESULT_MSG_GENERAL_ERROR);
                            logInfo("processIasApplication - iasApplicationController.processIasApplication exception: "
                                    + e);
                            response.addMessageResponse(
                                    ResponseUtils.getMessageResponse(recipient.getTranID(), recipient.getIdpID(),
                                            recipient.getRecipientID(),
                                            MsgTypeConstant.APPLICATION, ResultCodes.RESULT_CD_GENERAL_ERROR,
                                            ResultMessages.RESULT_MSG_GENERAL_ERROR));
                        }
                    }

                }
            }
        }

        logInfo("[MaintMsg]numOfRecordProcessed=" + recipientArray.length);
        logMaintMesgResult(response);

        logInfo("[MaintMsg]maintainMessage - END");

        return response;
    }

    public void cmcUserMessageInsertTrigger(HPFW_Connection conn, String user_id, String message_id,
            String account_status, String link_status, String req_linkup, String hidden_ind) throws Exception {

        logDebug("cmcUserMessageInsertTrigger user_id=" + user_id + ", message_id=" + message_id + ", account_status="
                + account_status + ", link_status=" + link_status + ", req_linkup=" + req_linkup + ", hidden_ind="
                + hidden_ind);

        if ("D".equals(account_status)) {

            createCmcUndeliveredMessage(conn, user_id, message_id, "N", "Account is delete");
            createCmcHiddenReinstateMessage(conn, user_id, message_id, "Account is delete", "H");
        } else if ("S".equals(account_status)) {

            createCmcUndeliveredMessage(conn, user_id, message_id, "N", "Account is suspended");
            createCmcHiddenReinstateMessage(conn, user_id, message_id, "Account is suspended", "H");
        } else {
            if ("Y".equals(req_linkup)) {
                if ("D".equals(link_status)) {
                    createCmcUndeliveredMessage(conn, user_id, message_id, "N", "User has delinked");
                    createCmcHiddenReinstateMessage(conn, user_id, message_id, "User has delinked", "H");
                } else if ("P".equals(link_status)) {
                    createCmcUndeliveredMessage(conn, user_id, message_id, "N", "User was still connecting with RS");
                } else if ("L".equals(link_status)) {
                    if ("Y".equals(hidden_ind)) {
                        createCmcUndeliveredMessage(conn, user_id, message_id, "N",
                                "User record has not been reinstated");
                        createCmcHiddenReinstateMessage(conn, user_id, message_id,
                                "User record has not been reinstated", "H");
                    }
                }
            }
        }
    }

    private void createCmcUndeliveredMessage(HPFW_Connection conn, String user_id, String message_id, String status,
            String reason) throws Exception {

        String InsertSQL = "INSERT INTO cmc_undelivered_message " +
                "(USER_ID, MESSAGE_ID, STATUS, REASON, CREATE_DT, LAST_MODIFY_DT, CREATE_BY, LAST_MODIFY_BY) " +
                "VALUES (?, ?, ?, ?, now(3), now(3), 'CMC', 'CMC')";

        ArrayList<Parameter> paraI = new ArrayList<Parameter>();
        paraI.add(new Parameter(Parameter.String, user_id));
        paraI.add(new Parameter(Parameter.String, message_id));
        paraI.add(new Parameter(Parameter.String, status));
        paraI.add(new Parameter(Parameter.String, reason));

        conn.executeStatement(InsertSQL, paraI);
    }

    private void createCmcHiddenReinstateMessage(HPFW_Connection conn, String user_id, String message_id, String reason,
            String type) throws Exception {

        String InsertSQL = "INSERT INTO cmc_hidden_reinstate_mesg " +
                " (USER_ID, MESSAGE_ID, REASON, TYPE, CREATE_DT, LAST_MODIFY_DT, CREATE_BY, LAST_MODIFY_BY) " +
                " VALUES (?, ?, ?, ?, now(3), now(3), 'CMC', 'CMC')";

        ArrayList<Parameter> paraI = new ArrayList<Parameter>();
        paraI.add(new Parameter(Parameter.String, user_id));
        paraI.add(new Parameter(Parameter.String, message_id));
        paraI.add(new Parameter(Parameter.String, reason));
        paraI.add(new Parameter(Parameter.String, type));

        conn.executeStatement(InsertSQL, paraI);
    }

    public void cmcUserItemInsertTrigger(HPFW_Connection conn, String user_id, String to_do_item_id,
            String account_status, String link_status, String req_linkup, String hidden_ind) throws Exception {

        if ("D".equals(account_status)) {

            createCmcUndeliveredItem(conn, user_id, to_do_item_id, "N", "Account is delete");
            createCmcHiddenReinstateItem(conn, user_id, to_do_item_id, "Account is delete", "H");
        } else if ("S".equals(account_status)) {

            createCmcUndeliveredItem(conn, user_id, to_do_item_id, "N", "Account is suspended");
            createCmcHiddenReinstateItem(conn, user_id, to_do_item_id, "Account is suspended", "H");
        } else {
            if ("Y".equals(req_linkup)) {
                if ("D".equals(link_status)) {
                    createCmcUndeliveredItem(conn, user_id, to_do_item_id, "N", "User has delinked");
                    createCmcHiddenReinstateItem(conn, user_id, to_do_item_id, "User has delinked", "H");
                } else if ("P".equals(link_status)) {
                    createCmcUndeliveredItem(conn, user_id, to_do_item_id, "N", "User was still connecting with RS");
                } else if ("L".equals(link_status)) {
                    if ("Y".equals(hidden_ind)) {
                        createCmcUndeliveredItem(conn, user_id, to_do_item_id, "N",
                                "User record has not been reinstated");
                        createCmcHiddenReinstateItem(conn, user_id, to_do_item_id,
                                "User record has not been reinstated", "H");
                    }
                }
            }
        }
    }

    private void createCmcUndeliveredItem(HPFW_Connection conn, String user_id, String to_do_item_id, String status,
            String reason) throws Exception {

        String InsertSQL = "INSERT INTO cmc_undelivered_item " +
                "(USER_ID, TO_DO_ITEM_ID, STATUS, REASON, CREATE_DT, LAST_MODIFY_DT, CREATE_BY, LAST_MODIFY_BY) " +
                "VALUES (?, ?, ?, ?, now(3), now(3), 'CMC', 'CMC')";

        ArrayList<Parameter> paraI = new ArrayList<Parameter>();
        paraI.add(new Parameter(Parameter.String, user_id));
        paraI.add(new Parameter(Parameter.String, to_do_item_id));
        paraI.add(new Parameter(Parameter.String, status));
        paraI.add(new Parameter(Parameter.String, reason));

        conn.executeStatement(InsertSQL, paraI);
    }

    private void createCmcHiddenReinstateItem(HPFW_Connection conn, String user_id, String to_do_item_id, String reason,
            String type) throws Exception {

        String InsertSQL = "INSERT INTO cmc_hidden_reinstate_item " +
                " (USER_ID, TO_DO_ITEM_ID, REASON, TYPE, CREATE_DT, LAST_MODIFY_DT, CREATE_BY, LAST_MODIFY_BY) " +
                " VALUES (?, ?, ?, ?, now(3), now(3), 'CMC', 'CMC')";

        ArrayList<Parameter> paraI = new ArrayList<Parameter>();
        paraI.add(new Parameter(Parameter.String, user_id));
        paraI.add(new Parameter(Parameter.String, to_do_item_id));
        paraI.add(new Parameter(Parameter.String, reason));
        paraI.add(new Parameter(Parameter.String, type));

        conn.executeStatement(InsertSQL, paraI);
    }

    private boolean isNewOrReplace(Recipient[] recipientArray) {
        for (int i = 0; i < recipientArray.length; i++) {
            Recipient recipient = recipientArray[i];

            int action = Action_ST.NEW_TYPE;
            if (recipient.getAction() != null) {
                action = recipient.getAction().getType();
            }

            if (action == Action_ST.NEW_TYPE || action == Action_ST.REPLACE_TYPE) {
                return true;
            }
        }
        return false;
    }

    private MessageResponse getMessageResponse(String tranId, String IdpId, String recipientId, String tranResultCd,
            String tranResultMsg) {
        MessageResponse msgRsp = new MessageResponse();
        msgRsp.setTranID(tranId);
        msgRsp.setIdpID(IdpId);
        msgRsp.setRecipientID(recipientId);
        msgRsp.setTranResultCode(tranResultCd);
        msgRsp.setTranResultMessage(tranResultMsg);

        return msgRsp;
    }

    private String createCmcUser(HPFW_Connection conn, String myId, String idpId, String status) throws Exception {
        CmcUser_ cmcUser = new CmcUser_();

        String userId = String.valueOf(CommonDBUtils.getSequence(conn, "USER_ID"));

        cmcUser.setUserId(userId);
        cmcUser.setMyId(myId);
        cmcUser.setIdpId(idpId);
        cmcUser.setStatus(status);

        cmcUser.insert(conn);

        return userId;
    }

    private MaintainMessageResponse getMaintainMessageResponse(String resultCode, String resultMessage) {
        MaintainMessageResponse response = new MaintainMessageResponse();
        response.setResultCode(resultCode);
        response.setResultMessage(resultMessage);
        return response;
    }

    private MaintainMessageResponse getConcludedMaintainMessageResponse(
            MaintainMessageResponse maintainMessageResponse) {
        boolean hasError = false;
        for (int i = 0; i < maintainMessageResponse.getMessageResponseCount(); i++) {
            if (!ResultCodes.RESULT_CD_TRAN_SUCCESS
                    .equals(maintainMessageResponse.getMessageResponse(i).getTranResultCode())) {
                hasError = true;
            }
        }

        if (hasError) {
            maintainMessageResponse.setResultCode(ResultCodes.RESULT_CD_MESSAGE_PROCESSED_WITH_ERROR);
            maintainMessageResponse.setResultMessage(ResultMessages.RESULT_MSG_MESSAGE_PROCESSED_WITH_ERROR);
        } else {
            maintainMessageResponse.setResultCode(ResultCodes.RESULT_CD_SUCCESS);
            maintainMessageResponse.setResultMessage(ResultMessages.RESULT_MSG_SUCCESS);
        }

        return maintainMessageResponse;
    }

    private HashSet getDuplicateRecipientSet(Recipient[] recipientArray, String iasIdpId) throws Exception {
        boolean isDuplicated = false;
        HashSet allHashSet = new HashSet();
        HashSet duplicateHashSet = new HashSet();
        for (int i = 0; i < recipientArray.length; i++) {

            if (!iasIdpId.equals(recipientArray[i].getIdpID())) {

                String recipientID = recipientArray[i].getRecipientID();

                int action = Action_ST.NEW_TYPE;
                if (recipientArray[i].getAction() != null) {
                    action = recipientArray[i].getAction().getType();
                }

                if (action == Action_ST.NEW_TYPE || action == Action_ST.REPLACE_TYPE) {
                    if (allHashSet.contains(recipientID)) {
                        if (!duplicateHashSet.contains(recipientID)) {
                            duplicateHashSet.add(recipientID);
                        }
                    } else {
                        allHashSet.add(recipientID);
                    }
                }
            }
        }

        return duplicateHashSet;
    }

    private HashSet getDuplicateIasRecipientSet(Recipient[] recipientArray, String iasIdpId) throws Exception {
        boolean isDuplicated = false;
        HashSet allHashSet = new HashSet();
        HashSet duplicateHashSet = new HashSet();
        for (int i = 0; i < recipientArray.length; i++) {

            if (iasIdpId.equals(recipientArray[i].getIdpID())) {

                String recipientID = recipientArray[i].getRecipientID();

                if (allHashSet.contains(recipientID)) {
                    if (!duplicateHashSet.contains(recipientID)) {
                        duplicateHashSet.add(recipientID);
                    }
                } else {
                    allHashSet.add(recipientID);
                }
            }
        }

        return duplicateHashSet;
    }

    private boolean checkDuplicateTranId(MaintainMessageRequest maintMsgReq) throws Exception {
        boolean isDuplicated = false;
        HashSet hashSet = new HashSet();

        MessageRequest[] msgReqArray = maintMsgReq.getMessageRequest();

        for (int i = 0; i < msgReqArray.length && !isDuplicated; i++) {

            MessageRequest msgReq = msgReqArray[i];
            MetaData[] metaDataArray = msgReq.getMetaData();

            for (int j = 0; j < metaDataArray.length && !isDuplicated; j++) {
                MetaData metaData = metaDataArray[j];
                Recipient[] recipientArray = metaData.getRecipient();

                for (int k = 0; k < recipientArray.length && !isDuplicated; k++) {
                    String tranID = recipientArray[k].getTranID();
                    if (hashSet.contains(tranID)) {
                        isDuplicated = true;
                        break;
                    } else {
                        hashSet.add(tranID);
                    }
                }
            }
        }

        return isDuplicated;
    }

    private String checkTranIdDuplicateAndOverLimit(MaintainMessageRequest maintMsgReq) throws Exception {
        boolean isLengthExceed = false;
        boolean isDuplicated = false;
        HashSet hashSet = new HashSet();

        MessageRequest[] msgReqArray = maintMsgReq.getMessageRequest();

        for (int i = 0; i < msgReqArray.length && !isDuplicated && !isLengthExceed; i++) {

            MessageRequest msgReq = msgReqArray[i];
            MetaData[] metaDataArray = msgReq.getMetaData();

            for (int j = 0; j < metaDataArray.length && !isDuplicated && !isLengthExceed; j++) {
                MetaData metaData = metaDataArray[j];
                Recipient[] recipientArray = metaData.getRecipient();

                for (int k = 0; k < recipientArray.length; k++) {
                    String tranID = recipientArray[k].getTranID();
                    if (tranID != null && tranID.length() > 20) {
                        isLengthExceed = true;
                        break;
                    }

                    if (tranID != null && hashSet.contains(tranID)) {

                        isDuplicated = true;
                        break;
                    } else {
                        hashSet.add(tranID);
                    }
                }
            }
        }

        if (isLengthExceed) {
            return ResultCodes.RESULT_CD_TRAN_ID_LENGTH_OVER_LIMIT;
        } else if (isDuplicated) {
            return ResultCodes.RESULT_CD_TRAN_ID_IS_DUPLICATED;
        } else {
            return "0";
        }
    }

    private MaintainMessageResponse checkHasProcessDataRequest(HPFW_Connection conn, MaintainMessageRequest maintMsgReq)
            throws Exception {
        MessageRequest[] msgReqArray = maintMsgReq.getMessageRequest();
        if (msgReqArray != null) {
            logInfo("checkHasProcessDataRequest - msgReqArray.length: " + msgReqArray.length);
        }

        for (int i = 0; i < msgReqArray.length; i++) {

            MessageRequest msgReq = msgReqArray[i];
            if (msgReq != null) {
                logInfo("checkHasProcessDataRequest - msgReq.toString(): " + msgReq.toString());
            }

            EMessage eMsg = msgReq.getEMessage();
            ToDoItem toDoItem = msgReq.getToDoItem();

            Application application = msgReq.getApplication();

            CommonIndicator_ST billAcctInfoInd = msgReq.getBillAccountInfoInd();

            if (eMsg == null && toDoItem == null && application == null
                    && (billAcctInfoInd == null || billAcctInfoInd.equals(CommonIndicator_ST.N)))
                return getMaintainMessageResponse(ResultCodes.RESULT_CD_PROCESS_DATA_NOT_FOUND,
                        ResultMessages.RESULT_MSG_PROCESS_DATA_NOT_FOUND);

        }
        return null;
    }

    private MaintainMessageResponse checkAllMessageRequest(HPFW_Connection conn, String appId,
            MaintainMessageRequest maintMsgReq) throws Exception {
        MessageRequest[] msgReqArray = maintMsgReq.getMessageRequest();

        String trustSpAppId = CmcSystemParam.getPara("TRUST_SP_APP_ID");

        for (int i = 0; i < msgReqArray.length; i++) {

            MessageRequest msgReq = msgReqArray[i];

            String portalStatus = getPortalStatusByPortalId(conn, msgReq.getPortalID());
            if (portalStatus == null || !Constants.PORTAL_STATUS_ACTIVE.equals(portalStatus)) {
                return getMaintainMessageResponse(ResultCodes.RESULT_CD_PORTAL_ID_NOT_FOUND,
                        ResultMessages.RESULT_MSG_PORTAL_ID_NOT_FOUND);
            }

            EMessage eMsg = msgReq.getEMessage();
            ToDoItem toDoItem = msgReq.getToDoItem();

            Application application = msgReq.getApplication();

            CommonIndicator_ST billAcctInfoInd = msgReq.getBillAccountInfoInd();

            String eMsgServiceProviderId = null;
            String toDoItemServiceProviderId = null;

            String applicationServiceProviderId = null;

            if (eMsg != null) {

                Map eMsgTemlFieldMap = getTemplateByIdVersion(conn, eMsg.getTemplateID(), eMsg.getTemplateVersion(),
                        "M");

                if (eMsgTemlFieldMap == null) {
                    return getMaintainMessageResponse(ResultCodes.RESULT_CD_EMSG_TEMPLATE_NOT_FOUND,
                            ResultMessages.RESULT_MSG_EMSG_TEMPLATE_NOT_FOUND);
                }
                logInfo("eMsgTemlFieldMap is not null");

                String temlAppId = (String) eMsgTemlFieldMap.get("APP_ID");

                String trustSpInd = (String) eMsgTemlFieldMap.get("TRUST_SP_IND");

                logInfo("appId: " + appId + ", temlAppId: " + temlAppId + ", trustSpAppId: " + trustSpAppId
                        + ", trustSpInd: " + trustSpInd);
                if (!appId.equals(temlAppId)) {
                    if (trustSpAppId == null || !trustSpAppId.trim().equals(appId) || trustSpInd.equals("N")) {
                        return getMaintainMessageResponse(ResultCodes.RESULT_CD_EMSG_TEMPLATE_NOT_FOUND,
                                ResultMessages.RESULT_MSG_EMSG_TEMPLATE_NOT_FOUND);
                    }
                }

                eMsgServiceProviderId = (String) eMsgTemlFieldMap.get("SERVICE_PROVIDER_ID");

                String iasInd = (String) eMsgTemlFieldMap.get("IAS_IND");
                String clientID = (String) eMsgTemlFieldMap.get("CLIENT_ID");
                boolean iasAutoCreate = "Y".equals((String) eMsgTemlFieldMap.get("IAS_AUTO_CREATE_IND"));

                if ((!iasAutoCreate) && ((Constants.IAS_IND_Y.equals(iasInd) || Constants.IAS_IND_I.equals(iasInd)))) {
                    if (clientID == null) {
                        logError("ClientID is not comfigured properly for SP [" + eMsgServiceProviderId + "]");
                        return getMaintainMessageResponse(ResultCodes.RESULT_CD_GENERAL_ERROR,
                                ResultMessages.RESULT_MSG_GENERAL_ERROR);
                    }
                }

            }

            if (toDoItem != null) {

                Map toDoItemTemlFieldMap = getTemplateByIdVersion(conn, toDoItem.getTemplateID(),
                        toDoItem.getTemplateVersion(), AppConstants.TO_DO_ITEM_TEMPLATE_TYPE);

                if (toDoItemTemlFieldMap == null) {
                    return getMaintainMessageResponse(ResultCodes.RESULT_CD_TO_DO_ITEM_TEMPLATE_NOT_FOUND,
                            ResultMessages.RESULT_MSG_TO_DO_ITEM_TEMPLATE_NOT_FOUND);
                }

                toDoItemServiceProviderId = (String) toDoItemTemlFieldMap.get("SERVICE_PROVIDER_ID");
                if (eMsgServiceProviderId != null && (!eMsgServiceProviderId.equals(toDoItemServiceProviderId))) {
                    return getMaintainMessageResponse(ResultCodes.RESULT_CD_TEMPLATE_SERVICE_PROVIDER_MUST_BE_SAME,
                            ResultMessages.RESULT_MSG_TEMPLATE_SERVICE_PROVIDER_MUST_BE_SAME);
                }

                String temlAppId = (String) toDoItemTemlFieldMap.get("APP_ID");
                if (!appId.equals(temlAppId)) {
                    return getMaintainMessageResponse(ResultCodes.RESULT_CD_TO_DO_ITEM_TEMPLATE_NOT_FOUND,
                            ResultMessages.RESULT_MSG_TO_DO_ITEM_TEMPLATE_NOT_FOUND);
                }
            }

            if (application != null) {

                Map iasApplicationTemplateFieldMap = getTemplateByIdVersion(conn, application.getTemplateID(),
                        application.getTemplateVersion(), AppConstants.APPLICATION_TEMPLATE_TYPE);

                if (iasApplicationTemplateFieldMap == null) {
                    return getMaintainMessageResponse(ResultCodes.RESULT_CD_APPLICATION_TEMPLATE_NOT_FOUND,
                            ResultMessages.RESULT_MSG_APPLICATION_TEMPLATE_NOT_FOUND);
                }

                applicationServiceProviderId = (String) iasApplicationTemplateFieldMap.get("SERVICE_PROVIDER_ID");
                if (eMsgServiceProviderId != null && (!eMsgServiceProviderId.equals(applicationServiceProviderId))) {
                    return getMaintainMessageResponse(ResultCodes.RESULT_CD_TEMPLATE_SERVICE_PROVIDER_MUST_BE_SAME,
                            ResultMessages.RESULT_MSG_TEMPLATE_SERVICE_PROVIDER_MUST_BE_SAME);
                }

                String temlAppId = (String) iasApplicationTemplateFieldMap.get("APP_ID");
                if (!appId.equals(temlAppId)) {
                    return getMaintainMessageResponse(ResultCodes.RESULT_CD_APPLICATION_TEMPLATE_NOT_FOUND,
                            ResultMessages.RESULT_MSG_APPLICATION_TEMPLATE_NOT_FOUND);
                }
            }

            MetaData[] metaDataArray = msgReq.getMetaData();
            for (int j = 0; j < metaDataArray.length; j++) {
                MetaData metaData = metaDataArray[j];

                String dataContentEn = metaData.getDataContentEN();
                if (dataContentEn == null || dataContentEn.length() == 0) {
                    return getMaintainMessageResponse(ResultCodes.RESULT_CD_META_DATA_EN_EMPTY,
                            ResultMessages.RESULT_MSG_META_DATA_EN_EMPTY);
                }
            }
        }
        return null;
    }

    private MessageResponse validateIasRecipient(HPFW_Connection conn, Recipient recipient, IasUser iasUser,
            HashSet iasUserSet, boolean iasOptCheck) throws Exception {
        logDebug("[MaintMsg]validateIasRecipient - START");

        try {
            int action = Action_ST.NEW_TYPE;
            if (recipient.getAction() != null) {
                action = recipient.getAction().getType();
            }

            String tranID = recipient.getTranID();
            String idpID = recipient.getIdpID();

            if (recipient.getRecipientID() == null || "".equals(recipient.getRecipientID())) {
                return getMessageResponse(recipient.getTranID(), recipient.getIdpID(), recipient.getRecipientID(),
                        ResultCodes.RESULT_CD_RECIPIENT_ID_NOT_FOUND, ResultMessages.RESULT_MSG_RECIPIENT_ID_NOT_FOUND);
            }

            if (iasUserSet.contains(recipient.getRecipientID())) {
                return getMessageResponse(recipient.getTranID(), recipient.getIdpID(), recipient.getRecipientID(),
                        ResultCodes.RESULT_CD_RECIPIENT_ID_IS_DUPLICATED,
                        ResultMessages.RESULT_MSG_RECIPIENT_ID_IS_DUPLICATED);
            }

            if (iasUser.getStatus() != null && Constants.IAS_USER_STATUS_DEREGISTERED.equals(iasUser.getStatus())) {
                return getMessageResponse(recipient.getTranID(), recipient.getIdpID(), recipient.getRecipientID(),
                        ResultCodes.RESULT_CD_RECIPIENT_IS_DEREGISTERED, ResultMessages.RESULT_MSG_RECIPIENT_IS_DEREGISTERED);
            }

            if (iasUser.getStatus() != null && Constants.IAS_USER_STATUS_SUSPENDED.equals(iasUser.getStatus())) {
                return getMessageResponse(recipient.getTranID(), recipient.getIdpID(), recipient.getRecipientID(),
                        ResultCodes.RESULT_CD_RECIPIENT_IS_SUSPENDED, ResultMessages.RESULT_MSG_RECIPIENT_IS_SUSPENDED);
            }

            if (iasUser.getStatus() != null && Constants.IAS_USER_STATUS_INVALID.equals(iasUser.getStatus())) {
                return getMessageResponse(recipient.getTranID(), recipient.getIdpID(), recipient.getRecipientID(),
                        ResultCodes.RESULT_CD_RECIPIENT_IS_INVALID, ResultMessages.RESULT_MSG_RECIPIENT_IS_INVALID);
            }

            if ((iasOptCheck) && ((iasUser.getOptIn() != null && (Constants.OPT_IN_N.equals(iasUser.getOptIn())
                    || Constants.OPT_IN_U.equals(iasUser.getOptIn()))))) {
                return getMessageResponse(recipient.getTranID(), recipient.getIdpID(), recipient.getRecipientID(),
                        ResultCodes.RESULT_CD_USER_REJECT_MSG, ResultMessages.RESULT_MSG_USER_REJECT_MSG);
            }

            if (tranID != null && tranID.length() > 20) {
                return getMessageResponse(recipient.getTranID(), recipient.getIdpID(), recipient.getRecipientID(),
                        ResultCodes.RESULT_CD_TRAN_ID_LENGTH_OVER_LIMIT, ResultMessages.RESULT_MSG_TRAN_ID_LENGTH_OVER_LIMIT);
            }

            logDebug("[MaintMsg]validateIasRecipient - END");

            return null;

        } catch (Exception ex) {

            logError("[MaintMsg]General exception caught in validateIasRecipient", ex);
            throw ex;
        }
    }

    private MessageResponse validateMsgSize(Recipient recipient, String subjectEn, String subjectTc, String subjectSc,
            String contentEn, String contentTc, String contentSc, int subjectSizeLimit, int contentSizeLimit)
            throws Exception {

        if ((subjectEn == null || subjectEn.length() == 0) ||
                (contentEn == null || contentEn.length() == 0)) {
            return getMessageResponse(null, null, null, ResultCodes.RESULT_CD_GENERAL_ERROR,
                    ResultMessages.RESULT_MSG_GENERAL_ERROR);
        }

        if (getMsgSize(subjectEn) > subjectSizeLimit) {
            return getMessageResponse(null, null, null, ResultCodes.RESULT_CD_MERGED_SUBJECT_EN_LENGTH_OVER_LIMIT,
                    ResultMessages.RESULT_MSG_MERGED_SUBJECT_EN_LENGTH_OVER_LIMIT);
        }

        if (getMsgSize(subjectTc) > subjectSizeLimit) {
            return getMessageResponse(null, null, null, ResultCodes.RESULT_CD_MERGED_SUBJECT_TC_LENGTH_OVER_LIMIT,
                    ResultMessages.RESULT_MSG_MERGED_SUBJECT_TC_LENGTH_OVER_LIMIT);
        }

        if (getMsgSize(subjectSc) > subjectSizeLimit) {
            return getMessageResponse(null, null, null, ResultCodes.RESULT_CD_MERGED_SUBJECT_SC_LENGTH_OVER_LIMIT,
                    ResultMessages.RESULT_MSG_MERGED_SUBJECT_SC_LENGTH_OVER_LIMIT);
        }

        if (getMsgSize(contentEn) > contentSizeLimit) {
            return getMessageResponse(null, null, null, ResultCodes.RESULT_CD_MERGED_CONTENT_EN_LENGTH_OVER_LIMIT,
                    ResultMessages.RESULT_MSG_MERGED_CONTENT_EN_LENGTH_OVER_LIMIT);
        }

        if (getMsgSize(contentTc) > contentSizeLimit) {
            return getMessageResponse(null, null, null, ResultCodes.RESULT_CD_MERGED_CONTENT_TC_LENGTH_OVER_LIMIT,
                    ResultMessages.RESULT_MSG_MERGED_CONTENT_TC_LENGTH_OVER_LIMIT);
        }

        if (getMsgSize(contentSc) > contentSizeLimit) {
            return getMessageResponse(null, null, null, ResultCodes.RESULT_CD_MERGED_CONTENT_SC_LENGTH_OVER_LIMIT,
                    ResultMessages.RESULT_MSG_MERGED_CONTENT_SC_LENGTH_OVER_LIMIT);
        }

        return null;
    }

    private MessageResponse getRecipientInvalidResponse(Recipient recipient, MessageResponse msgRsp) {
        msgRsp.setTranID(recipient.getTranID());
        msgRsp.setIdpID(recipient.getIdpID());
        msgRsp.setRecipientID(recipient.getRecipientID());

        return msgRsp;
    }

    private Map<String, String> getServiceProviderByAppId(HPFW_Connection conn, String appId) throws Exception {
        logDebug("[MaintMsg]getServiceProviderByAppId - START");

        ResultSet rs = null;
        try {
            ArrayList<Parameter> paraList = new ArrayList<Parameter>();
            paraList.add(new Parameter(Parameter.String, appId));
            rs = conn.getResultSet(SELECT_SERVICE_PROVIDER_BY_APP_ID, paraList);
            Map<String, String> map = new HashMap<String, String>();
            if (rs.next()) {
                map.put("SERVICE_PROVIDER_ID", rs.getString("service_provider_id"));
                map.put("REQ_LINKUP_IND", rs.getString("req_linkup_ind"));
            }

            logDebug("[MaintMsg]getServiceProviderByAppId - END");

            return map;

        } catch (Exception ex) {
            logError("[MaintMsg]General exception caught in getServiceProviderByAppId", ex);
            throw ex;
        } finally {
            try {
                if (rs != null)
                    HPFW_Connection.close(rs);
            } catch (Exception ex) {
                logError("[MaintMsg]General exception caught in getServiceProviderByAppId - rs.close();", ex);
            }
        }
    }

    private Map getTemplateByIdVersion(HPFW_Connection conn, String templateId, String templateVersion, String typeInd)
            throws Exception {
        logDebug("[MaintMsg]getTemplateByIdVersion - START");
        ResultSet rs = null;

        try {

            ArrayList paraList = new ArrayList();

            paraList.add(new Parameter(Parameter.String, templateId));
            paraList.add(new Parameter(Parameter.String, templateVersion));
            paraList.add(new Parameter(Parameter.String, typeInd));

            rs = conn.getResultSet(SELECT_TEMPLATE_CONTENT, paraList);

            HashMap map = null;
            if (rs.next()) {
                map = new HashMap();
                map.put("SUBJECT_EN", rs.getString("SUBJECT_EN"));
                map.put("SUBJECT_TC", rs.getString("SUBJECT_TC"));
                map.put("SUBJECT_SC", rs.getString("SUBJECT_SC"));
                map.put("CONTENT_EN", rs.getString("CONTENT_EN"));
                map.put("CONTENT_TC", rs.getString("CONTENT_TC"));
                map.put("CONTENT_SC", rs.getString("CONTENT_SC"));
                map.put("DEFAULT_FOLDER_ID", rs.getString("DEFAULT_FOLDER_ID"));
                map.put("SERVICE_PROVIDER_ID", rs.getString("SERVICE_PROVIDER_ID"));
                map.put("USER_IND", rs.getString("USER_IND"));
                map.put("APP_ID", rs.getString("APP_ID"));
                map.put("REQ_LINKUP_IND", rs.getString("REQ_LINKUP_IND"));
                map.put("CUT_OFF_DATE", String.valueOf(rs.getInt("CUT_OFF_DATE")));
                map.put("EMAIL_IND", rs.getString("EMAIL_IND"));
                map.put("EMAIL_SUBJECT_EN", rs.getString("EMAIL_SUBJECT_EN"));
                map.put("EMAIL_SUBJECT_TC", rs.getString("EMAIL_SUBJECT_TC"));
                map.put("EMAIL_SUBJECT_SC", rs.getString("EMAIL_SUBJECT_SC"));
                map.put("EMAIL_CONTENT_EN", rs.getString("EMAIL_CONTENT_EN"));
                map.put("EMAIL_CONTENT_TC", rs.getString("EMAIL_CONTENT_TC"));
                map.put("EMAIL_CONTENT_SC", rs.getString("EMAIL_CONTENT_SC"));

                map.put("ALERT_EMAIL_IND", rs.getString("ALERT_EMAIL_IND"));

                map.put("MOBILE_MSG_IND", rs.getString("MOBILE_MSG_IND"));
                map.put("MOBILE_SUBJECT_EN", rs.getString("MOBILE_SUBJECT_EN"));
                map.put("MOBILE_SUBJECT_TC", rs.getString("MOBILE_SUBJECT_TC"));
                map.put("MOBILE_SUBJECT_SC", rs.getString("MOBILE_SUBJECT_SC"));
                map.put("MOBILE_CONTENT_EN", rs.getString("MOBILE_CONTENT_EN"));
                map.put("MOBILE_CONTENT_TC", rs.getString("MOBILE_CONTENT_TC"));
                map.put("MOBILE_CONTENT_SC", rs.getString("MOBILE_CONTENT_SC"));

                map.put("TRUST_SP_IND", rs.getString("TRUST_SP_IND"));

                map.put("NOTI_PRIORITY", rs.getString("NOTI_PRIORITY"));

                map.put("IAS_IND", rs.getString("IAS_IND"));
                map.put("IAS_SUBJECT_EN", rs.getString("IAS_SUBJECT_EN"));
                map.put("IAS_SUBJECT_TC", rs.getString("IAS_SUBJECT_TC"));
                map.put("IAS_SUBJECT_SC", rs.getString("IAS_SUBJECT_SC"));
                map.put("IAS_CONTENT_EN", rs.getString("IAS_CONTENT_EN"));
                map.put("IAS_CONTENT_TC", rs.getString("IAS_CONTENT_TC"));
                map.put("IAS_CONTENT_SC", rs.getString("IAS_CONTENT_SC"));
                map.put("CLIENT_ID", rs.getString("CLIENT_ID"));
                map.put("IAS_AUTO_CREATE_IND", rs.getString("IAS_AUTO_CREATE_IND"));

                map.put("IAS_OPT_SP_ID", rs.getString("IAS_OPT_SP_ID"));
                map.put("IAS_SHOW_ES_SET_BTN", rs.getString("IAS_SHOW_ES_SET_BTN"));

            }

            logDebug("[MaintMsg]getTemplateByIdVersion - END");

            return map;

        } catch (Exception ex) {

            logError("[MaintMsg]General exception caught in getTemplateByIdVersion", ex);
            throw ex;
        } finally {
            try {
                if (rs != null)
                    HPFW_Connection.close(rs);
            } catch (Exception ex) {
                logError("[MaintMsg]General exception caught in getTemplateByIdVersion - rs.close();", ex);
            }
        }
    }

    private Map<String, String> getUserByMyClientId(Properties p, HPFW_Connection conn, String clientId,
            String serviceProviderId, String realMyId) throws Exception {
        logDebug("[MaintMsg]getUserByMyClientId - START");
        ResultSet rs = null;
        ResultSet rs1 = null;
        HashMap<String, String> map = new HashMap<String, String>();
        try {
            logInfo("[MaintMsg]getUserByMyClientId - clientId=" + clientId);
            ArrayList<Parameter> paraList = new ArrayList<Parameter>();
            paraList.add(new Parameter(Parameter.String, serviceProviderId));
            paraList.add(new Parameter(Parameter.String, clientId));
            rs = conn.getResultSet(SELECT_CMC_USER_INFO_BY_MY_ID, paraList);

            int total = 0;
            while (rs.next()) {
                total++;
            }

            if (total >= 1) {
                ArrayList<Parameter> paraList1 = new ArrayList<Parameter>();
                paraList1.add(new Parameter(Parameter.String, serviceProviderId));
                paraList1.add(new Parameter(Parameter.String, clientId));
                rs1 = conn.getResultSet(SELECT_CMC_USER_INFO_BY_MY_ID, paraList1);
                if (total == 1) {
                    if (rs1.next()) {
                        map.put("USER_ID", rs1.getString("USER_ID"));
                        map.put("IDP_ID", rs1.getString("IDP_ID"));
                        map.put("USER_STATUS", rs1.getString("USER_STATUS"));
                        map.put("SERVICE_PROVIDER_ID", rs1.getString("SERVICE_PROVIDER_ID"));
                        map.put("LINK_STATUS", rs1.getString("LINK_STATUS"));
                        map.put("REJECT_IND", rs1.getString("REJECT_IND"));
                        map.put("HIDDEN_IND", rs1.getString("HIDDEN_IND"));
                        map.put("MY_ID_KEY", rs1.getString("MY_ID_KEY"));
                    }
                } else {
                    while (rs1.next()) {
                        String decryptMyId = null;
                        try {
                            String b64 = rs1.getString("b64_enc_my_id");
                            decryptMyId = KMUUtils.decryptByKey(p, b64, CmcAppPropertyNames.CMC_CORE_CERT_FILE_NAME_P12);
                        } catch (Exception ignore) {
                        }
                        if (decryptMyId != null && decryptMyId.equals(realMyId)) {
                            map.put("USER_ID", rs1.getString("USER_ID"));
                            map.put("IDP_ID", rs1.getString("IDP_ID"));
                            map.put("USER_STATUS", rs1.getString("USER_STATUS"));
                            map.put("SERVICE_PROVIDER_ID", rs1.getString("SERVICE_PROVIDER_ID"));
                            map.put("LINK_STATUS", rs1.getString("LINK_STATUS"));
                            map.put("REJECT_IND", rs1.getString("REJECT_IND"));
                            map.put("HIDDEN_IND", rs1.getString("HIDDEN_IND"));
                            map.put("MY_ID_KEY", rs1.getString("MY_ID_KEY"));
                        }
                    }
                }
            }
            logDebug("[MaintMsg]getUserByMyId - END");
            return map;

        } catch (Exception ex) {
            logError("[MaintMsg]General exception caught in getUserByMyId", ex);
            throw ex;
        } finally {
            try {
                HPFW_Connection.close(rs);
                HPFW_Connection.close(rs1);
            } catch (Exception ex) {
                logError("[MaintMsg]General exception caught in getUserByMyId - rs.close();", ex);
            }
        }
    }

    private String getPortalStatusByPortalId(HPFW_Connection conn, String portalId) throws Exception {
        logDebug("[MaintMsg]getPortalStatusByPortalId - START");

        String result = "";
        ResultSet rs = null;

        try {

            ArrayList paraList = new ArrayList();

            paraList.add(new Parameter(Parameter.String, portalId));

            rs = conn.getResultSet(SELECT_CMC_PORTAL_SERVER_BY_PORTAL_ID, paraList);

            HashMap map = new HashMap();
            if (rs.next()) {
                result = rs.getString("STATUS");
            }

            logDebug("[MaintMsg]getPortalStatusByPortalId - END");

            return result;

        } catch (Exception ex) {
            logError("[MaintMsg]General exception caught in getPortalStatusByPortalId", ex);
            throw ex;
        } finally {
            try {
                if (rs != null)
                    HPFW_Connection.close(rs);
            } catch (Exception ex) {
                logError("[MaintMsg]General exception caught in getPortalStatusByPortalId - rs.close();", ex);
            }
        }
    }

    private String getIdpByIdpId(HPFW_Connection conn, String idpId) throws Exception {
        logDebug("[MaintMsg]getIdpByIdpId - START");

        String result = "";
        ResultSet rs = null;

        try {

            ArrayList paraList = new ArrayList();

            paraList.add(new Parameter(Parameter.String, idpId));

            rs = conn.getResultSet(SELECT_CMC_IDP_BY_IDP_ID, paraList);

            HashMap map = new HashMap();
            if (rs.next()) {
                result = rs.getString("STATUS");
            }

            logDebug("[MaintMsg]getIdpByIdpId - END");

            return result;

        } catch (Exception ex) {
            logError("[MaintMsg]General exception caught in getIdpByIdpId", ex);
            throw ex;
        } finally {
            try {
                if (rs != null)
                    HPFW_Connection.close(rs);
            } catch (Exception ex) {
                logError("[MaintMsg]General exception caught in getIdpByIdpId - rs.close();", ex);
            }
        }
    }

    private Map<String, String> getServiceProviderInfoBySPID(HPFW_Connection conn, String spID) throws Exception {
        logDebug("getServiceProviderInfoBySPID - START");
        Map<String, String> resultMap = new HashMap<String, String>();
        ResultSet rs = null;

        try {
            ArrayList paraList = new ArrayList();
            paraList.add(new Parameter(Parameter.String, spID));
            rs = conn.getResultSet(SELECT_SERVICE_PROVIDER_INFO_BY_SERVICE_PROVIDER_ID, paraList);

            if (rs.next()) {
                resultMap.put("EMAIL_IND", rs.getString("EMAIL_IND"));
                resultMap.put("MOBILE_IND", rs.getString("MOBILE_IND"));
            }

            return resultMap;
        } catch (Exception ex) {
            logError("[MaintMsg]General exception caught in getServiceProviderInfoBySPID", ex);
            throw ex;
        } finally {
            try {
                if (rs != null)
                    HPFW_Connection.close(rs);
            } catch (Exception ex) {
                logError("[MaintMsg]General exception caught in getServiceProviderInfoBySPID - rs.close();", ex);
            }

            logInfo("email ind = " + resultMap.get("EMAIL_IND").toString());
            logInfo("mobile ind = " + resultMap.get("MOBILE_IND").toString());
            logDebug("getServiceProviderInfoBySPID - END");
        }
    }

    private String getMergedContent(Map temlMap, String dataEn, String dataTc, String dataSc, String type, String lang,
            List<MessageParam> paraList, MarsUserObject userObject, boolean isBroadcastMsg) throws Exception {
        logDebug("getMergedContent temlMap: " + temlMap + ", dataEn: " + dataEn + ", dataTc: " + dataTc + ", dataSc: "
                + dataSc + ", type: " + type + ", lang: " + lang + ", paraList: " + paraList + ", userObject: "
                + userObject + ", isBroadcastMsg: " + isBroadcastMsg);

        String result = "";

        String temlEn = (String) temlMap.get(type + "_EN");
        String temlTc = (String) temlMap.get(type + "_TC");
        String temlSc = (String) temlMap.get(type + "_SC");

        if (lang.equals(LANGUAGE_SC)) {

            if (temlSc != null && temlSc.length() > 0) {
                result = XsltUtil.xml2htmlwithMergedParam(temlSc, dataSc,
                        getMergedParamMap(paraList, userObject, LANGUAGE_SC, isBroadcastMsg));
            } else if (temlTc != null && temlTc.length() > 0) {
                result = XsltUtil.xml2htmlwithMergedParam(temlTc, dataTc,
                        getMergedParamMap(paraList, userObject, LANGUAGE_TC, isBroadcastMsg));
            } else {
                result = XsltUtil.xml2htmlwithMergedParam(temlEn, dataEn,
                        getMergedParamMap(paraList, userObject, LANGUAGE_EN, isBroadcastMsg));
            }

        } else if (lang.equals(LANGUAGE_TC)) {

            if (temlTc != null && temlTc.length() > 0) {
                result = XsltUtil.xml2htmlwithMergedParam(temlTc, dataTc,
                        getMergedParamMap(paraList, userObject, LANGUAGE_TC, isBroadcastMsg));
            } else {
                result = XsltUtil.xml2htmlwithMergedParam(temlEn, dataEn,
                        getMergedParamMap(paraList, userObject, LANGUAGE_EN, isBroadcastMsg));
            }

        } else {
            result = XsltUtil.xml2htmlwithMergedParam(temlEn, dataEn,
                    getMergedParamMap(paraList, userObject, LANGUAGE_EN, isBroadcastMsg));
        }

        logDebug("getMergedContent result: " + result);
        return result;
    }

    private Map<String, String> getMergedParamMap(List<MessageParam> paraList, MarsUserObject userObject, String lang,
            boolean isBroadcastMsg) throws Exception {
        Map<String, String> mergedParamMap = new HashMap<String, String>();

        for (MessageParam tempParam : paraList) {
            if (MessageParam.MESSAGE_PARAM_TYPE_MARS_ATTR.equals(tempParam.getParaSource())) {
                if (MessageParam.MARS_ATTR_ALIAS.equals(tempParam.getMarsUserAttr())) {
                    if (isBroadcastMsg) {
                        if (LANGUAGE_EN.equals(lang)) {
                            mergedParamMap.put(tempParam.getParaName(), tempParam.getStaticParaEn());
                        } else if (LANGUAGE_TC.equals(lang)) {
                            mergedParamMap.put(tempParam.getParaName(), tempParam.getStaticParaTc());
                        } else {
                            mergedParamMap.put(tempParam.getParaName(), tempParam.getStaticParaSc());
                        }
                    } else {

                        if (userObject != null) {
                            mergedParamMap.put(tempParam.getParaName(), userObject.getAlias());
                        }

                    }
                }
            } else if (MessageParam.MESSAGE_PARAM_TYPE_STATIC.equals(tempParam.getParaSource())) {
                if (LANGUAGE_EN.equals(lang)) {
                    mergedParamMap.put(tempParam.getParaName(), tempParam.getStaticParaEn());
                } else if (LANGUAGE_TC.equals(lang)) {
                    mergedParamMap.put(tempParam.getParaName(), tempParam.getStaticParaTc());
                } else {
                    mergedParamMap.put(tempParam.getParaName(), tempParam.getStaticParaSc());
                }
            }
        }

        return mergedParamMap;
    }

    private int getMsgSize(String inStr) throws Exception {

        int result = 0;

        if (inStr != null) {
            result = inStr.getBytes("UTF-8").length;
        }

        return result;
    }

    private void logMaintMesgResult(MaintainMessageResponse response) {

        try {
            int numOfSuccess = 0;
            int numOfFail = 0;

            MessageResponse[] messageResponseArray = response.getMessageResponse();

            if (messageResponseArray != null) {

                for (int i = 0; i < messageResponseArray.length; i++) {
                    MessageResponse messageResponse = messageResponseArray[i];

                    if (ResultCodes.RESULT_CD_TRAN_SUCCESS.equals(messageResponse.getTranResultCode()))
                        numOfSuccess++;
                    else
                        numOfFail++;
                }
            }

            logInfo("[MaintMsg]numOfSuccess=" + numOfSuccess);
            logInfo("[MaintMsg]numOfFail=" + numOfFail);
        } catch (Exception ex) {
            logWarn("Exception caught in logMaintMesgResult");
        }
    }

    private IasUserToDoItem_ getIasUserToDoItemFromHashMap(String tranId) {

        IasUserToDoItem_ iasUserToDoItem = null;

        String action = (String) m_iasToDoItemHistory.get(tranId);

        if (action != null) {
            iasUserToDoItem = new IasUserToDoItem_();
            iasUserToDoItem.setDeleteInd(
                    HISTORY_DELETE.equals(action) ? Constants.DELETE_IND_DELETED : Constants.DELETE_IND_NOT_DELETED);
            iasUserToDoItem.setCompleteInd(HISTORY_MARKCOMPLETE.equals(action) ? Constants.COMPLETE_IND_COMPLETED
                    : Constants.COMPLETE_IND_IN_COMPLETED);
        }

        return iasUserToDoItem;
    }

    private boolean userToDoItemCreated(String userId, String tranId) {

        boolean result = false;

        ToDoItemRecipientKey toDoItemRecipientKey = new ToDoItemRecipientKey(userId, tranId);

        if (m_newUserToDoItem.containsKey(toDoItemRecipientKey)) {
            ArrayList userToDoItemList = (ArrayList) m_newUserToDoItem.get(toDoItemRecipientKey);
            if (userToDoItemList != null && userToDoItemList.size() > 0) {
                result = true;
            }
        }

        return result;
    }

    private boolean iasUserToDoItemCreated(String recipientId, String tranId) {
        boolean result = false;
        ToDoItemRecipientKey toDoItemRecipientKey = new ToDoItemRecipientKey(recipientId, tranId);

        if (m_newIasUserToDoItem.containsKey(toDoItemRecipientKey)) {
            ArrayList<IasUserToDoItem_> iasUserToDoItemList = (ArrayList<IasUserToDoItem_>) m_newIasUserToDoItem
                    .get(toDoItemRecipientKey);
            if (iasUserToDoItemList != null && iasUserToDoItemList.size() > 0) {
                result = true;
            }
        }
        return result;
    }

    private void updateIasUserToDoItemByRecipientKey(HPFW_Connection conn, String openId, String corrTranId,
            String actionTranId, String deleteInd, String completeInd) throws Exception {

        ToDoItemRecipientKey toDoItemRecipientKey = new ToDoItemRecipientKey(openId, corrTranId);

        ArrayList<IasUserToDoItem_> iasUserToDoItemList = (ArrayList<IasUserToDoItem_>) m_newIasUserToDoItem
                .get(toDoItemRecipientKey);
        if (iasUserToDoItemList != null && iasUserToDoItemList.size() > 0) {

            for (int i = 0; i < iasUserToDoItemList.size(); i++) {
                IasUserToDoItem_ iasUserToDoItem = (IasUserToDoItem_) iasUserToDoItemList.get(i);
                if (deleteInd != null) {
                    iasUserToDoItem.setDeleteInd(deleteInd);
                }
                if (completeInd != null) {
                    iasUserToDoItem.setCompleteInd(completeInd);
                    iasUserToDoItem.setCompleteBy(Constants.COMPLETE_BY_RS);
                    iasUserToDoItem.setCompleteDt(new java.sql.Timestamp(new Date().getTime()));
                }
                iasUserToDoItem.setActionTranId(actionTranId);
                iasUserToDoItem.update(conn);
            }
        }
    }

    private List<MessageParam> getMsgParamByMsgType(HPFW_Connection conn, String msgType) throws Exception {
        logDebug("getMsgParamByMsgType - START");
        List<MessageParam> resultList = new ArrayList<MessageParam>();
        ResultSet rs = null;

        try {
            ArrayList paraList = new ArrayList();
            paraList.add(new Parameter(Parameter.String, msgType));

            rs = conn.getResultSet(SELECT_MSG_PARAM_BY_MESSAGE_TYPE, paraList);

            while (rs.next()) {
                MessageParam tempBean = new MessageParam();
                tempBean.setParaName(rs.getString("PARA_NAME"));
                tempBean.setParaSource(rs.getString("PARA_SOURCE"));
                tempBean.setMessageType(rs.getString("MESSAGE_TYPE"));
                tempBean.setMarsUserAttr(rs.getString("MARS_USER_ATTR"));
                tempBean.setStaticParaEn(rs.getString("STATIC_PARA_NAME_EN"));
                tempBean.setStaticParaTc(rs.getString("STATIC_PARA_NAME_TC"));
                tempBean.setStaticParaSc(rs.getString("STATIC_PARA_NAME_SC"));

                resultList.add(tempBean);
            }
        } catch (Exception ex) {
            logError("[MaintMsg]General exception caught in getMsgParamByMsgType", ex);
            throw ex;
        } finally {
            try {
                if (rs != null)
                    HPFW_Connection.close(rs);
            } catch (Exception ex) {
                logError("[MaintMsg]General exception caught in getMsgParamByMsgType - rs.close();", ex);
            }
        }

        logDebug("getMsgParamByMsgType - END");
        return resultList;
    }

    private void updateStaticSystemParam(List<MessageParam> paramList) throws Exception {
        for (MessageParam tempBean : paramList) {
            String tempStaticParaEn = tempBean.getStaticParaEn();
            String tempStaticParaTc = tempBean.getStaticParaTc();
            String tempStaticParaSc = tempBean.getStaticParaSc();
            String tempPara = "";

            if (tempStaticParaEn != null && !"".equals(tempStaticParaEn)) {
                tempPara = CmcSystemParam.getPara(tempStaticParaEn);
                tempBean.setStaticParaEn(StringEscapeUtils.unescapeHtml(tempPara));
            }

            if (tempStaticParaTc != null && !"".equals(tempStaticParaTc)) {
                tempPara = CmcSystemParam.getPara(tempStaticParaTc);
                tempBean.setStaticParaTc(StringEscapeUtils.unescapeHtml(tempPara));
            }

            if (tempStaticParaSc != null && !"".equals(tempStaticParaSc)) {
                tempPara = CmcSystemParam.getPara(tempStaticParaSc);
                tempBean.setStaticParaSc(StringEscapeUtils.unescapeHtml(tempPara));
            }
        }
    }

    private String getExactSentOutLanguage(Map<String, String> template, String type, String userLangPref) {
        String resultLanguage;

        String temlEn = (String) template.get(type + "_EN");
        String temlTc = (String) template.get(type + "_TC");
        String temlSc = (String) template.get(type + "_SC");

        if (ValidationUtils.LANG_PREF_SC.equals(userLangPref)) {
            if (temlSc != null && temlSc.length() > 0) {
                resultLanguage = ValidationUtils.LANG_PREF_SC;
            } else if (temlTc != null && temlTc.length() > 0) {
                resultLanguage = ValidationUtils.LANG_PREF_TC;
            } else {
                resultLanguage = ValidationUtils.LANG_PREF_EN;
            }
        } else if (ValidationUtils.LANG_PREF_TC.equals(userLangPref)) {
            if (temlTc != null && temlTc.length() > 0) {
                resultLanguage = ValidationUtils.LANG_PREF_TC;
            } else {
                resultLanguage = ValidationUtils.LANG_PREF_EN;
            }
        } else {
            resultLanguage = ValidationUtils.LANG_PREF_EN;
        }

        logInfo("Sent out language = " + resultLanguage);

        return resultLanguage;
    }

    private String getNotVoidPaymentTranId(HPFW_Connection conn, String accountBalanceId, String tranRefNumber)
            throws Exception {
        logDebug("[MaintMsg]getNotVoidPaymentTranId - START");

        ResultSet rs = null;
        try {
            ArrayList<Parameter> paraList = new ArrayList<Parameter>();
            paraList.add(new Parameter(Parameter.String, accountBalanceId));
            paraList.add(new Parameter(Parameter.String, tranRefNumber));
            rs = conn.getResultSet(SELECT_PAYMENT_TRAN_ID_BY_USER_ACCT_INFO_NOT_VOID, paraList);
            String result = "";
            if (rs.next()) {
                result = rs.getString("tran_id");
            }
            logDebug("[MaintMsg]getNotVoidPaymentTranId - END");

            return result;

        } catch (Exception ex) {
            logError("[MaintMsg]General exception caught in getNotVoidPaymentTranId", ex);
            throw ex;
        } finally {
            try {
                if (rs != null)
                    HPFW_Connection.close(rs);
            } catch (Exception ex) {
                logError("[MaintMsg]General exception caught in getNotVoidPaymentTranId - rs.close();", ex);
            }
        }
    }

    private IasUser getIasUserInfo(HPFW_Connection conn, String spId, String openId) throws Exception {
        logDebug("[MaintMsg]getIasUserInfo - START");

        IasUser iasUser = null;
        ResultSet rs = null;

        try {

            ArrayList paraList = new ArrayList();

            paraList.add(new Parameter(Parameter.String, spId));
            paraList.add(new Parameter(Parameter.String, openId));

            rs = conn.getResultSet(SELECT_IAS_USER_INFO_BY_SP_ID_OPEN_ID, paraList);

            HashMap map = new HashMap();
            if (rs.next()) {
                iasUser = new IasUser();
                iasUser.setNotiId(rs.getString("NOTI_ID"));
                iasUser.setOpenId(rs.getString("OPEN_ID"));
                iasUser.setOptIn(rs.getString("OPT_IN"));
                iasUser.setStatus(rs.getString("STATUS"));
            }

            logDebug("[MaintMsg]getIasUserInfo - END");

            return iasUser;

        } catch (Exception ex) {
            logError("[MaintMsg]General exception caught in getIasUserInfo", ex);
            throw ex;
        } finally {
            try {
                if (rs != null)
                    HPFW_Connection.close(rs);
            } catch (Exception ex) {
                logError("[MaintMsg]General exception caught in getIasUserInfo - rs.close();", ex);
            }
        }
    }

    public IasUser getIasEsNotiMap(HPFW_Connection conn, String notiId, String spId) throws Exception {
        logDebug("getIasEsNotiMap - START");

        IasUser iasUser = null;
        ResultSet rs = null;

        try {

            ArrayList paraList = new ArrayList();

            paraList.add(new Parameter(Parameter.String, notiId));
            paraList.add(new Parameter(Parameter.String, spId));

            rs = conn.getResultSet(SELECT_IAS_ES_NOTI_MAP_BY_NOTI_ID_SP_ID, paraList);

            if (rs.next()) {
                iasUser = new IasUser();
                iasUser.setNotiId(rs.getString("NOTI_ID"));
                iasUser.setOpenId(rs.getString("OPEN_ID"));
                iasUser.setOptIn(rs.getString("OPT_IN"));
            }

            logDebug("getIasEsNotiMap - END");

            return iasUser;

        } catch (Exception ex) {
            logError("General exception caught in getIasEsNotiMap", ex);
            throw ex;
        } finally {
            try {
                if (rs != null)
                    HPFW_Connection.close(rs);
            } catch (Exception ex) {
                logError("General exception caught in getIasEsNotiMap - rs.close();", ex);
            }
        }
    }

    private MessageResponse validateIasMsgSize(Map eMsgTemlFieldMap, String subjectEn, String subjectTc,
            String subjectSc, String contentEn, String contentTc, String contentSc, Properties properties)
            throws Exception {
        int subjectESizeLimit = Integer
                .parseInt(properties.getProperty(CmcAppPropertyNames.MAINT_MSG_IAS_MSG_SUBJ_ENG_SIZE_LIMIT_PROPERTY_NAME));
        int subjectCSizeLimit = Integer
                .parseInt(properties.getProperty(CmcAppPropertyNames.MAINT_MSG_IAS_MSG_SUBJ_CHI_SIZE_LIMIT_PROPERTY_NAME));

        int contentESizeLimit = Integer.parseInt(
                properties.getProperty(CmcAppPropertyNames.MAINT_MSG_IAS_MSG_CONTENT_ENG_SIZE_LIMIT_PROPERTY_NAME));
        int contentCSizeLimit = Integer.parseInt(
                properties.getProperty(CmcAppPropertyNames.MAINT_MSG_IAS_MSG_CONTENT_CHI_SIZE_LIMIT_PROPERTY_NAME));

        if ((subjectEn == null || subjectEn.length() == 0) ||
                (contentEn == null || contentEn.length() == 0)) {

            return getMessageResponse(null, null, null, ResultCodes.RESULT_CD_GENERAL_ERROR,
                    ResultMessages.RESULT_MSG_GENERAL_ERROR);
        }

        logDebug("subjectEn=" + subjectEn);
        logDebug("getMsgSize(subjectEn)=" + getMsgSize(subjectEn));
        logDebug("subjectESizeLimit=" + subjectESizeLimit);

        if (getMsgSize(subjectEn) > subjectESizeLimit) {
            return getMessageResponse(null, null, null, ResultCodes.RESULT_CD_MERGED_IAS_MSG_SUBJECT_EN_LENGTH_OVER_LIMIT,
                    ResultMessages.RESULT_MSG_MERGED_IAS_MSG_SUBJECT_EN_LENGTH_OVER_LIMIT);
        }

        if (getMsgSize(subjectTc) > subjectCSizeLimit) {
            return getMessageResponse(null, null, null, ResultCodes.RESULT_CD_MERGED_IAS_MSG_SUBJECT_TC_LENGTH_OVER_LIMIT,
                    ResultMessages.RESULT_MSG_MERGED_IAS_MSG_SUBJECT_TC_LENGTH_OVER_LIMIT);
        }

        if (getMsgSize(subjectSc) > subjectCSizeLimit) {
            return getMessageResponse(null, null, null, ResultCodes.RESULT_CD_MERGED_IAS_MSG_SUBJECT_SC_LENGTH_OVER_LIMIT,
                    ResultMessages.RESULT_MSG_MERGED_IAS_MSG_SUBJECT_SC_LENGTH_OVER_LIMIT);
        }

        if (getMsgSize(contentEn) > contentESizeLimit) {
            return getMessageResponse(null, null, null, ResultCodes.RESULT_CD_MERGED_IAS_MSG_CONTENT_EN_LENGTH_OVER_LIMIT,
                    ResultMessages.RESULT_MSG_MERGED_IAS_MSG_CONTENT_EN_LENGTH_OVER_LIMIT);
        }

        if (getMsgSize(contentTc) > contentCSizeLimit) {
            return getMessageResponse(null, null, null, ResultCodes.RESULT_CD_MERGED_IAS_MSG_CONTENT_TC_LENGTH_OVER_LIMIT,
                    ResultMessages.RESULT_MSG_MERGED_IAS_MSG_CONTENT_TC_LENGTH_OVER_LIMIT);
        }

        if (getMsgSize(contentSc) > contentCSizeLimit) {
            return getMessageResponse(null, null, null, ResultCodes.RESULT_CD_MERGED_IAS_MSG_CONTENT_SC_LENGTH_OVER_LIMIT,
                    ResultMessages.RESULT_MSG_MERGED_IAS_MSG_CONTENT_SC_LENGTH_OVER_LIMIT);
        }

        return null;
    }

    private String getNodeValueFromMetaData(String metaData, String tagName) throws Exception {
        String result = null;
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        InputSource src = new InputSource();
        src.setCharacterStream(new StringReader(metaData));

        Document doc = builder.parse(src);
        NodeList nodeList = doc.getElementsByTagName(tagName);

        if ((nodeList != null) && (nodeList.getLength() > 0))
            result = doc.getElementsByTagName(tagName).item(0).getTextContent();

        return result;
    }

    private void init() throws Exception {

    }

    private List m_temlFieldMapList = null;
    private HashMap m_toDoItemHistory = null;
    private HashMap m_newUserToDoItem = null;

    private HashMap<String, String> m_iasToDoItemHistory = null;
    private HashMap<ToDoItemRecipientKey, ArrayList<IasUserToDoItem_>> m_newIasUserToDoItem = null;

    private HashMap<String, String> m_iasApplicationHistory = null;

    private final String SELECT_TEMPLATE_CONTENT = "select t.SERVICE_PROVIDER_ID, t.SUBJECT_EN,  t.SUBJECT_TC, t.SUBJECT_SC, t.CONTENT_EN,  t.CONTENT_TC, t.CONTENT_SC, e.DEFAULT_FOLDER_ID, e.USER_IND, s.APP_ID, s.REQ_LINKUP_IND, e.CUT_OFF_DATE, t.EMAIL_IND, t.EMAIL_SUBJECT_EN, t.EMAIL_SUBJECT_TC, t.EMAIL_SUBJECT_SC, t.EMAIL_CONTENT_EN, t.EMAIL_CONTENT_TC, t.EMAIL_CONTENT_SC, IFNULL(t.ALERT_EMAIL_IND, 'N') AS ALERT_EMAIL_IND, "
            +
            "t.IAS_SUBJECT_EN, t.IAS_SUBJECT_TC, t.IAS_SUBJECT_SC, t.IAS_CONTENT_EN, t.IAS_CONTENT_TC, t.IAS_CONTENT_SC, IFNULL(t.IAS_IND, 'N') AS IAS_IND, s.CLIENT_ID, s.IAS_AUTO_CREATE_IND, IFNULL(t.IAS_SHOW_ES_SET_BTN, 'N') AS IAS_SHOW_ES_SET_BTN, IFNULL(s.IAS_OPT_SP_ID, s.SERVICE_PROVIDER_ID) AS IAS_OPT_SP_ID, "
            +
            "t.MOBILE_MSG_IND, t.MOBILE_SUBJECT_EN, t.MOBILE_SUBJECT_TC, t.MOBILE_SUBJECT_SC, t.MOBILE_CONTENT_EN, t.MOBILE_CONTENT_TC, t.MOBILE_CONTENT_SC, IFNULL(t.TRUST_SP_IND, 'N') AS TRUST_SP_IND, t.NOTI_PRIORITY  "
            + "from CMC_TEMPLATE t, CMC_TEMPLATE_TYPE e, CMC_SERVICE_PROVIDER s "
            + "where t.TEMPLATE_ID = ? "
            + "and t.TEMPLATE_VERSION = ? "
            + "and t.TEMPLATE_TYPE = e.TEMPLATE_TYPE and e.TYPE_IND = ? "
            + "and t.SERVICE_PROVIDER_ID = s.SERVICE_PROVIDER_ID "
            + "and t.STATUS = 'A'  and e.STATUS = 'A'   and s.STATUS = 'A' ";

    private final String SELECT_CMC_USER_INFO_BY_MY_ID = "select u.USER_ID, u.b64_enc_my_id, u.IDP_ID, u.STATUS as USER_STATUS, l.SERVICE_PROVIDER_ID, L.STATUS as LINK_STATUS, l.REJECT_IND, l.HIDDEN_IND, u.MY_ID_KEY  "
            + "from CMC_USER u LEFT JOIN CMC_USER_LINK_STATUS l on (u.USER_ID = l.USER_ID and l.SERVICE_PROVIDER_ID = ?) "
            + "where u.MY_ID = ? and u.status <> 'P' ";

    private final String SELECT_CMC_USER_INFO_BY_TOKENISEDEID = "select u.USER_ID, u.b64_enc_my_id, u.IDP_ID, u.STATUS as USER_STATUS, l.SERVICE_PROVIDER_ID, L.STATUS as LINK_STATUS, l.REJECT_IND, l.HIDDEN_IND, u.MY_ID_KEY  "
            + "from CMC_USER u LEFT JOIN CMC_USER_LINK_STATUS l on (u.USER_ID = l.USER_ID and l.SERVICE_PROVIDER_ID = ?) ";

    private final String SELECT_CMC_USER_INFO_BY_USER_ID = "select u.USER_ID, u.b64_enc_my_id, u.IDP_ID, u.STATUS as USER_STATUS, l.SERVICE_PROVIDER_ID, L.STATUS as LINK_STATUS, l.REJECT_IND, l.HIDDEN_IND, u.MY_ID_KEY  "
            + "from CMC_USER u LEFT JOIN CMC_USER_LINK_STATUS l on (u.USER_ID = l.USER_ID and l.SERVICE_PROVIDER_ID = ?) "
            + "where u.USER_ID = ? ";

    private final String SELECT_CMC_PORTAL_SERVER_BY_PORTAL_ID = "select STATUS "
            + "from CMC_PORTAL_SERVER "
            + "where PORTAL_ID = ? ";

    private final String SELECT_CMC_IDP_BY_IDP_ID = "select DESCRIPTION, APP_ID, STATUS "
            + "from CMC_IDP "
            + "where IDP_ID = ? ";

    private final String SELECT_TRAN_ID_BY_CORRELATION_TRAN_ID = "select TO_DO_ITEM_ID, DELETE_IND, COMPLETE_IND "
            + "from CMC_USER_TO_DO_ITEM "
            + "where TRAN_ID = ? ";

    private final String SELECT_SERVICE_PROVIDER_BY_APP_ID = "SELECT service_provider_id, req_linkup_ind " +
            "FROM cmc_service_provider " +
            "WHERE app_id = ?";

    private final String SELECT_ACCOUNT_BALANCE_ID_BY_USER_ACCT_INFO = "SELECT account_balance_id " +
            "FROM cmc_user_acc_balance " +
            "WHERE user_id = ? " +
            "	AND service_provider_id = ? " +
            "	AND account_no = ? " +
            "	AND delete_ind = 'N'";

    private final String SELECT_PAYMENT_TRAN_ID_BY_USER_ACCT_INFO = "SELECT tran_id " +
            "FROM cmc_user_payment_tran " +
            "WHERE account_balance_id = ? " +
            "	AND tran_ref_number = ? " +
            "	AND delete_ind = 'N'";

    private final String SELECT_SERVICE_PROVIDER_INFO_BY_SERVICE_PROVIDER_ID = "SELECT service_provider_id, email_ind, mobile_ind "
            +
            "FROM cmc_service_provider " +
            "WHERE service_provider_id = ? ";

    private final String SELECT_TEMPLATE_DO_NOT_REQUIRE_LINKUP = ""
            + "SELECT ct.template_id "
            + "FROM   cmc_template ct, "
            + "       cmc_service_provider sp "
            + "WHERE  ct.service_provider_id = sp.service_provider_id "
            + "       AND ct.template_id = ? "
            + "       AND ct.template_version = ? "
            + "       AND sp.req_linkup_ind = 'N' ";

    private final String SELECT_WHETHER_USER_HAS_LINKUPED = ""
            + "SELECT ct.template_id "
            + "FROM   cmc_template ct, "
            + "       cmc_service_provider sp, "
            + "       cmc_user_link_status culs, "
            + "       cmc_user u "
            + "WHERE  u.user_id = culs.user_id "
            + "       AND culs.hidden_ind = 'N' "
            + "       AND culs.status = 'L' "
            + "       AND ct.service_provider_id = sp.service_provider_id "
            + "       AND culs.service_provider_id = sp.service_provider_id "
            + "       AND ct.template_id = ? "
            + "       AND ct.template_version = ? "
            + "       AND u.my_id_key =? ";

    private final String SELECT_MSG_PARAM_BY_MESSAGE_TYPE = ""
            + "SELECT para_name, "
            + "       para_source, "
            + "       message_type, "
            + "       mars_user_attr, "
            + "       static_para_name_en, "
            + "       static_para_name_tc, "
            + "       static_para_name_sc "
            + "FROM   cmc_message_param "
            + "WHERE  message_type = ? ";

    private final String SELECT_PAYMENT_TRAN_ID_BY_USER_ACCT_INFO_NOT_VOID = SELECT_PAYMENT_TRAN_ID_BY_USER_ACCT_INFO +
            "	AND IFNULL(void_ind, 'N') = 'N'";

    private final String SELECT_IAS_USER_INFO_BY_SP_ID_OPEN_ID = "select B.NOTI_ID, B.OPEN_ID, B.OPT_IN, A.STATUS "
            + "from IAS_ES_NOTI_MAP B LEFT JOIN IAS_USER_NOTI_INFO A ON (B.NOTI_ID = A.NOTI_ID) "
            + "where B.SERVICE_PROVIDER_ID = ? AND B.OPEN_ID = ?";

    private final String SELECT_IAS_ES_NOTI_MAP_BY_NOTI_ID_SP_ID = "select NOTI_ID, OPEN_ID, OPT_IN "
            + "from IAS_ES_NOTI_MAP  "
            + "where NOTI_ID = ? "
            + "AND SERVICE_PROVIDER_ID = ? ";

}
