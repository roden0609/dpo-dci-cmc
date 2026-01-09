package hk.gov.cmc.processor.maintainmessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import hk.gov.cmc.common.CmcAppConstants;
import hk.gov.cmc.common.CmcAppPropertyNames;
import hk.gov.cmc.common.IntegrationConstants;
import hk.gov.cmc.common.MsgTypeConstant;
import hk.gov.cmc.common.RecipientIDTypeConstant;
import hk.gov.cmc.common.ResultCodes;
import hk.gov.cmc.common.ResultMessages;
import hk.gov.cmc.config.CmcEnvProperties;
import hk.gov.cmc.dao.maintainmessage.notification.IasUserNotiInfoDAO;
import hk.gov.cmc.dao.maintainmessage.param.MessageParamDao;
import hk.gov.cmc.dao.maintainmessage.template.CmcTemplateDao;
import hk.gov.cmc.dto.maintainmessage.SingleMaintainMsgResult;
import hk.gov.cmc.model.maintainmessage.action.Action;
import hk.gov.cmc.model.maintainmessage.application.Application;
import hk.gov.cmc.model.maintainmessage.emessage.EMessage;
import hk.gov.cmc.model.maintainmessage.param.MessageParam;
import hk.gov.cmc.model.maintainmessage.request.Recipient;
import hk.gov.cmc.model.maintainmessage.response.MaintainMessageResponse;
import hk.gov.cmc.model.maintainmessage.response.MessageResponse;
import hk.gov.cmc.model.maintainmessage.template.CmcTemplate;
import hk.gov.cmc.model.maintainmessage.todoitem.ToDoItem;
import hk.gov.cmc.model.maintainmessage.user.IasUser;
import hk.gov.cmc.model.maintainmessage.user.IasUserWrapped;
import hk.gov.cmc.persistence.connection.hpfw.HPFW_Connection;
import hk.gov.cmc.processor.maintainmessage.application.IasApplicationProcessor;
import hk.gov.cmc.processor.maintainmessage.emessage.IasEMessageProcessor;
import hk.gov.cmc.processor.maintainmessage.todoitem.IasToDoItemProcessor;
import hk.gov.cmc.utils.common.EncUtils;
import hk.gov.cmc.utils.common.EncryptionUtils;
import hk.gov.cmc.utils.maintainmessage.MaintainMessageUtils;
import hk.gov.cmc.utils.maintainmessage.ias.IasRecipientUtils;
import hk.gov.cmc.utils.maintainmessage.param.ParamUtils;
import hk.gov.cmc.utils.maintainmessage.recipient.RecipientUtils;
import hk.gov.cmc.validator.IasRecipientValidator;

public class MessageProcessor {

    private static Log logger = LogFactory.getLog(MessageProcessor.class);
    private final static CmcEnvProperties cmcEnvProperties = new CmcEnvProperties();

    String esClientId;
    String iasOptSpId;
    String iasIdpId;

    // E-message
    CmcTemplate cmcEMsgTemplate;

    // Application
    boolean hasApplicationInRequest = false;
    CmcTemplate cmcApplicationTemplate;

    // To-do item
    boolean hasToDoItemInRequest = false;
    CmcTemplate cmcToDoItemTemplate;
    int toDoItemCutOffDay = 0;
    HashMap<String, String> iasToDoItemHandledCache;
    HashMap<String, String> iasApplicationHandledCache;

    public MessageProcessor() {
    }

    public void processMessage(MaintainMessageResponse response,
            HPFW_Connection conn, String portalId, List<Recipient> recipients,
            EMessage eMsg, ToDoItem toDoItem, Application application,
            String dataContentEn, String dataContentTc, String dataContentSc) throws Exception {

        logger.info("processor.processMessage - start");

        Properties properties = cmcEnvProperties.getProperties();
        iasIdpId = properties.getProperty(CmcAppPropertyNames.IAS_IDP_ID_PROPERTY_NAME);

        if (eMsg != null) {
            if (eMsg.getTemplateId() != null) {
                logger.info("processor.processMessage - eMsg.getTemplateId: " + eMsg.getTemplateId());
            }
            if (eMsg.getTemplateVersion() != null) {
                logger.info("processor.processMessage - eMsg.getTemplateVersion: " + eMsg.getTemplateVersion());
            }
        }

        if (toDoItem != null) {
            if (toDoItem.getTemplateId() != null) {
                logger.info("processor.processMessage - toDoItem.getTemplateId: " + toDoItem.getTemplateId());
            }
            if (toDoItem.getTemplateVersion() != null) {
                logger.info("processor.processMessage - toDoItem.getTemplateVersion: " + toDoItem.getTemplateVersion());
            }
        }

        if (application != null) {
            if (application.getTemplateId() != null) {
                logger.info("processor.processMessage - application.getTemplateId: " + application.getTemplateId());
            }
            if (application.getTemplateVersion() != null) {
                logger.info("processor.processMessage - application.getTemplateVersion: "
                        + application.getTemplateVersion());
            }
        }

        if (recipients != null) {
            logger.info("processor.processMessage - recipients.size: " + recipients.size());
        }

        int subjectSizeLimit = Integer
                .parseInt(properties.getProperty(CmcAppPropertyNames.MAINT_MSG_SUBJ_SIZE_LIMIT_PROPERTY_NAME));
        int contentSizeLimit = Integer
                .parseInt(properties.getProperty(CmcAppPropertyNames.MAINT_MSG_CONTENT_SIZE_LIMIT_PROPERTY_NAME));

        // String messageId = "";
        // String toDoItemId = "";
        // String userInd = "";
        // String eMsgServiceProviderId = "";
        // String toDoItemServiceProviderId = "";
        // String applicationServiceProviderId = "";
        // String reqLinkupInd = "";
        // int toDoItemCutOffDay = 0;
        // String openId = "";
        // String iasNotiId = "";
        // String iasMsgId = "";
        // String iasOptSpId = "";
        // String eMsgIasInd = "";
        // boolean eMsgIasOptCheck = false;
        // boolean eMsgIasAutoCreate = false;
        // String eMsgDefaultFolderName = "";
        // String eMsgEmailInd = "";
        // String eMsgAlertEmailInd = "";
        // String eMsgMobileMsgInd = "";
        // String eMsgTemplateNotiPriority = "";
        // String toDoItemIasInd = "";
        // boolean toDoItemIasOptCheck = false;
        // boolean toDoItemIasAutoCreate = false;
        // String toDoItemDefaultFolderName = "";
        // String toDoItemEmailInd = "";
        // String toDoItemAlertEmailInd = "";
        // String toDoItemMobileMsgInd = "";
        // String toDoItemTemplateNotiPriority = "";
        // String iasIdpId = properties.getProperty(CmcAppPropertyNames.IAS_IDP_ID_PROPERTY_NAME);
        // String myGovSpId = properties.getProperty(CmcAppPropertyNames.MYGOV_SP_ID_PROPERTY_NAME);
        // String myGovClientId = properties.getProperty(CmcAppPropertyNames.MYGOV_CLIENT_ID_PROPERTY_NAME);
        // String iasEsAppSuffixTagName = properties
        // .getProperty(CmcAppPropertyNames.IAS_ES_APP_SUFFIX_TAG_NAME_PROPERTY_NAME);
        // String serverId = properties.getProperty(CmcAppPropertyNames.SERVER_ID_PROPERTY_NAME);
        // String iasToDoItemId = "";
        // String iasApplicationId = "";
        // String encKeyStoreId = properties
        // .getProperty(CmcAppPropertyNames.CMC_MESSAGE_ENCRYPT_KEY_STORE_ID_PROPERTY_NAME);
        // boolean eMsgCreated = false;
        // boolean toDoItemCreated = false;
        // boolean hasInvalidField = false;
        // boolean isBroadcastMsg = false;
        // Map eMsgTemlFieldMap = null;
        // Map toDoItemTemlFieldMap = null;
        // Map<String, String> iasApplicationTemplateFieldMap = null;

        CmcTemplateDao cmcTemplateDao = new CmcTemplateDao();

        // Validate eMsg template
        if (eMsg != null) {
            cmcEMsgTemplate = cmcTemplateDao.getTemplateByIdVersion(conn, eMsg.getTemplateId(),
                    eMsg.getTemplateVersion(), CmcAppConstants.EMSG_TEMPLATE_TYPE);
            esClientId = cmcEMsgTemplate.getClientId();
            iasOptSpId = cmcEMsgTemplate.getIasOptSpId();
        }

        // Validate to-do-item template
        if (toDoItem != null) {
            cmcToDoItemTemplate = cmcTemplateDao.getTemplateByIdVersion(conn, toDoItem.getTemplateId(),
                    toDoItem.getTemplateVersion(), CmcAppConstants.TO_DO_ITEM_TEMPLATE_TYPE);

            esClientId = esClientId == null ? cmcToDoItemTemplate.getClientId() : esClientId;
            iasOptSpId = iasOptSpId == null ? cmcToDoItemTemplate.getIasOptSpId() : iasOptSpId;

            // Check if there is a recipient for New or Replace to-do-item, create a new to-do-item in DB
            if (RecipientUtils.isNewOrReplace(recipients)) {
                // get cut off date from DB template type
                if (cmcToDoItemTemplate.getCutOffDate() != null) {
                    toDoItemCutOffDay = Integer.parseInt(cmcToDoItemTemplate.getCutOffDate());
                }
            }

            hasToDoItemInRequest = true;
        }

        // Validate application status template
        if (application != null) {
            cmcApplicationTemplate = cmcTemplateDao.getTemplateByIdVersion(
                    conn, application.getTemplateId(), application.getTemplateVersion(),
                    CmcAppConstants.APPLICATION_TEMPLATE_TYPE);

            esClientId = esClientId == null ? cmcApplicationTemplate.getClientId() : esClientId;
            iasOptSpId = iasOptSpId == null ? cmcApplicationTemplate.getIasOptSpId() : iasOptSpId;

            hasApplicationInRequest = true;
        }

        HashSet<String> duplicateIasRecipientSet = IasRecipientUtils.getIasDuplicationRecipientHashSet(recipients,
                iasIdpId);

        int impDtUpperLimit = Integer.parseInt(
                properties.getProperty(CmcAppPropertyNames.TTO_DO_ITEM_IMPORTANT_DT_UPPER_LIMIT_PROPERTY_NAME));

        // MyGov6-C1-002 Enhance Maintain Message module to support iAM Smart message -- START
        MessageResponse validateIasSizeErrorResp = null;
        boolean iasMsgSizeInvalid = false;
        // MyGov6-C1-002 Enhance Maintain Message module to support iAM Smart message -- END

        List<MessageParam> cmcMsgParamAll = MessageParamDao.getMsgParamByMsgType(conn,
                MessageParam.MESSAGE_TYPE_ALL_MSG);
        List<MessageParam> cmcMsgParamIasMessage = MessageParamDao.getMsgParamByMsgType(conn,
                MessageParam.MESSAGE_TYPE_IAS_MSG);
        List<MessageParam> cmcMsgParamIasToDoItem = MessageParamDao.getMsgParamByMsgType(conn,
                MessageParam.MESSAGE_TYPE_IAS_TO_DO_ITEM);
        List<MessageParam> cmcMsgParamIasApplication = MessageParamDao.getMsgParamByMsgType(conn,
                MessageParam.MESSAGE_TYPE_IAS_APPLICATION);

        ParamUtils.updateStaticSystemParam(cmcMsgParamIasMessage);
        ParamUtils.updateStaticSystemParam(cmcMsgParamIasToDoItem);
        ParamUtils.updateStaticSystemParam(cmcMsgParamIasApplication);
        ParamUtils.updateStaticSystemParam(cmcMsgParamAll);

        // Append message parameters of all message to each message type
        cmcMsgParamIasMessage.addAll(cmcMsgParamAll);
        cmcMsgParamIasToDoItem.addAll(cmcMsgParamAll);
        cmcMsgParamIasApplication.addAll(cmcMsgParamAll);

        Map<String, IasUserWrapped> validatedIasUserWrappedMap = new HashMap<String, IasUserWrapped>();
        List<String> iasMsgCreatedNotiIdList = new ArrayList<String>();
        List<IasUser> iasUserList = new ArrayList<IasUser>();
        List<IasUser> missingIasUserList = new ArrayList<IasUser>();
        Map<String, Map<String, String>> validatedUserMap = new HashMap<String, Map<String, String>>();

        // Prepare validated iAM Smart user list (validatedIasUserWrappedMap) for sending iAM Smart message later
        for (Recipient recipient : recipients) {
            if (iasIdpId.equals(recipient.getIdpId())) {

                IasUserNotiInfoDAO iasUserNotiInfoDAO = new IasUserNotiInfoDAO();
                IasUserWrapped iasUserWrapped = null;

                if (RecipientIDTypeConstant.HKID.equals(recipient.getRecipientIdType())) {

                    String hkidHashed = EncryptionUtils.hashString(recipient.getRecipientId());
                    List<IasUser> getByHkidIasUserList = iasUserNotiInfoDAO.getIasUserNotiInfoByHKIDHashed(conn,
                            iasOptSpId, hkidHashed);

                    if (getByHkidIasUserList.size() <= 0) {

                        // Prepare the HKID list to call iAM Smart Swith Notification API
                        IasUser iasUser = new IasUser();
                        iasUser.setClientId(esClientId);
                        iasUser.setHkidHashed(hkidHashed);
                        String hkidEncrypted = EncUtils.encrypt(recipient.getRecipientId(),
                                properties.getProperty(CmcAppPropertyNames.HKID_ENCRYPT_PASSPHRASE_ID_PROPERTY_NAME),
                                properties
                                        .getProperty(CmcAppPropertyNames.HKID_ENCRYPT_PASSPHRASE_USAGE_PROPERTY_NAME));
                        iasUser.setHkidEncrypted(hkidEncrypted);
                        iasUser.setStatus(IntegrationConstants.IAS_USER_STATUS_MISSING);
                        logger.info("HKID, getByHkidIasUserList.size() <= 0, recipient.getTranId: "
                                + recipient.getTranId() + ", iasUser: " + iasUser.toString());

                        iasUserWrapped = new IasUserWrapped();
                        iasUserWrapped.setIasUser(iasUser);
                        iasUserWrapped.setHkid(recipient.getRecipientId());

                    } else if (getByHkidIasUserList.size() == 1) {

                        IasUser iasUser = getByHkidIasUserList.get(0);
                        logger.info("HKID, getByHkidIasUserList.size() == 1, recipient.getTranId: "
                                + recipient.getTranId() + ", iasUser: " + iasUser.toString());
                        iasUserWrapped = new IasUserWrapped();
                        String hkidDecrypted = EncUtils.decrypt(
                                iasUser.getHkidEncrypted(),
                                properties.getProperty(CmcAppPropertyNames.HKID_ENCRYPT_PASSPHRASE_ID_PROPERTY_NAME),
                                properties
                                        .getProperty(CmcAppPropertyNames.HKID_ENCRYPT_PASSPHRASE_USAGE_PROPERTY_NAME));

                        if (hkidDecrypted.equals(recipient.getRecipientId())) {
                            iasUserWrapped.setIasUser(iasUser);
                            iasUserWrapped.setHkid(recipient.getRecipientId());
                            iasMsgCreatedNotiIdList.add(iasUser.getNotiId());
                        } else {
                            IasUser iasUserNew = new IasUser();
                            logger.info("HKID collision, getByHkidIasUserList.size() == 1, recipient.getTranId: "
                                    + recipient.getTranId() + ", iasUser: " + iasUser.toString());
                            iasUserNew.setClientId(esClientId);
                            iasUserNew.setHkidHashed(hkidHashed);
                            String hkidEncrypted = EncUtils.encrypt(
                                    recipient.getRecipientId(),
                                    properties
                                            .getProperty(CmcAppPropertyNames.HKID_ENCRYPT_PASSPHRASE_ID_PROPERTY_NAME),
                                    properties.getProperty(
                                            CmcAppPropertyNames.HKID_ENCRYPT_PASSPHRASE_USAGE_PROPERTY_NAME));
                            iasUserNew.setHkidEncrypted(hkidEncrypted);
                            iasUserNew.setStatus(IntegrationConstants.IAS_USER_STATUS_MISSING);
                            iasUserWrapped.setIasUser(iasUserNew);
                            iasUserWrapped.setHkid(recipient.getRecipientId());
                        }

                    } else {

                        // Prevent same hash value but different HKID
                        for (IasUser iasUserItem : getByHkidIasUserList) {
                            String hkidDecrypted = EncUtils.decrypt(
                                    iasUserItem.getHkidEncrypted(),
                                    properties
                                            .getProperty(CmcAppPropertyNames.HKID_ENCRYPT_PASSPHRASE_ID_PROPERTY_NAME),
                                    properties.getProperty(
                                            CmcAppPropertyNames.HKID_ENCRYPT_PASSPHRASE_USAGE_PROPERTY_NAME));

                            if (recipient.getRecipientId().equals(hkidDecrypted)) {
                                IasUser iasUser = iasUserItem;
                                logger.info("HKID, getByHkidIasUserList.size() > 1, recipient.getTranId: "
                                        + recipient.getTranId() + ", iasUser: " + iasUser.toString());

                                iasUserWrapped = new IasUserWrapped();
                                iasUserWrapped.setIasUser(iasUser);
                                iasUserWrapped.setHkid(recipient.getRecipientId());

                                iasMsgCreatedNotiIdList.add(iasUserItem.getNotiId());
                                break;
                            } else {
                                IasUser iasUser = new IasUser();
                                iasUser.setClientId(esClientId);
                                iasUser.setHkidHashed(hkidHashed);
                                String hkidEncrypted = EncUtils.encrypt(recipient.getRecipientId(),
                                        properties.getProperty(
                                                CmcAppPropertyNames.HKID_ENCRYPT_PASSPHRASE_ID_PROPERTY_NAME),
                                        properties.getProperty(
                                                CmcAppPropertyNames.HKID_ENCRYPT_PASSPHRASE_USAGE_PROPERTY_NAME));
                                iasUser.setHkidEncrypted(hkidEncrypted);
                                iasUser.setStatus(IntegrationConstants.IAS_USER_STATUS_MISSING);
                                logger.info(
                                        "HKID, getByHkidIasUserList.size() > 1, but all matched hash decrypted HKID is not match the recipientId, recipient.getTranId: "
                                                + recipient.getTranId() + ", iasUser: " + iasUser.toString());
                                iasUserWrapped = new IasUserWrapped();
                                iasUserWrapped.setIasUser(iasUser);
                                iasUserWrapped.setHkid(recipient.getRecipientId());
                            }

                        }
                    }

                } else {

                    // Default to use OPEN_ID if recipient ID type is not specified to serve the old iAM Smart Message API
                    IasUser iasUser = iasUserNotiInfoDAO.getIasUserNotiInfoByOpenId(conn, iasOptSpId,
                            recipient.getRecipientId());
                    if (iasUser == null) {
                        iasUser = new IasUser();
                        iasUser.setClientId(esClientId);
                        iasUser.setOpenId(recipient.getRecipientId());
                        iasUser.setStatus(IntegrationConstants.IAS_USER_STATUS_MISSING);
                    } else {
                        iasMsgCreatedNotiIdList.add(iasUser.getNotiId());
                    }
                    logger.info("OPEN_ID, recipient.getTranId: " + recipient.getTranId() + ", iasUser: "
                            + iasUser.toString());

                    iasUserWrapped = new IasUserWrapped();
                    iasUserWrapped.setIasUser(iasUser);

                }

                logger.info("IasRecipientValidator.validateIasRecipient - recipient == null: " + (recipient == null)
                        + ", iasUserWrapped == null: " + (iasUserWrapped == null));
                MaintainMessageResponse validateRecipientMaintainMessageResponse = IasRecipientValidator
                        .validateIasRecipient(
                                conn, recipient, iasUserWrapped.getIasUser(), duplicateIasRecipientSet,
                                toDoItem != null, application != null, eMsg != null);

                if (validateRecipientMaintainMessageResponse.getMessageResponses() != null
                        && validateRecipientMaintainMessageResponse.getMessageResponses().size() > 0) {
                    for (MessageResponse messageResponse : validateRecipientMaintainMessageResponse
                            .getMessageResponses()) {
                        response.addMessageResponse(messageResponse);
                    }
                } else {
                    validatedIasUserWrappedMap.put(recipient.getRecipientId(), iasUserWrapped);
                }
            }
        }

        logger.info("processor.processMessage - validatedIasUserWrappedMap.size: " + validatedIasUserWrappedMap.size());

        IasEMessageProcessor iasEMessageProcessor = new IasEMessageProcessor();
        IasToDoItemProcessor iasToDoItemProcessor = new IasToDoItemProcessor();
        IasApplicationProcessor iasApplicationProcessor = new IasApplicationProcessor();

        // Loop through each recipient to process iAM Smart message
        String iasToDoItemId = "";
        String iasApplicationId = "";
        for (Recipient recipient : recipients) {

            logger.info("recipient.getIdpId: " + recipient.getIdpId()
                    + ", recipient.getCorrelatedTranId: " + recipient.getCorrelatedTranId()
                    + ", recipient.getTranId: " + recipient.getTranId()
                    + ", recipient.getAction: " + recipient.getAction());

            if (iasIdpId.equals(recipient.getIdpId())) {

                Action action = Action.NEW;
                if (recipient.getAction() != null) {
                    action = recipient.getAction();
                }

                IasUserWrapped validatedIasUserWrapped = validatedIasUserWrappedMap.get(recipient.getRecipientId());
                if (validatedIasUserWrapped != null) {
                    IasUser validatedIasUser = validatedIasUserWrapped.getIasUser();

                    SingleMaintainMsgResult processIasToDoItemResult = null;
                    SingleMaintainMsgResult processIasApplicationResult = null;

                    // if (eMsg != null) {

                    // MessageResponse msgRsp = null;
                    // // CMC-2024-007 - Enhance Maintain Message Module to Support iAM Smart To-Do Item - END

                    // // CMC-2024-007 - Enhance Maintain Message Module to Support iAM Smart To-Do Item - BEGIN
                    // // processIasMsgResult = new SingleMaintainMsgResult(iasMsgId, false);
                    // // CMC-2024-007 - Enhance Maintain Message Module to Support iAM Smart To-Do Item - END

                    // // iasMsgId = NULL indicates IAS_MESSAGE record is not created yet
                    // if (iasMsgId == null || "".equals(iasMsgId)) {

                    // iasMsgId = IasUtils.getNextIasMsgId(serverId);

                    // String mergedIasSubjectEn = null;
                    // String mergedIasSubjectTc = null;
                    // String mergedIasSubjectSc = null;
                    // String mergedIasContentEn = null;
                    // String mergedIasContentTc = null;
                    // String mergedIasContentSc = null;

                    // String templateSubjecEn = (String) eMsgTemlFieldMap.get("IAS_SUBJECT_EN");
                    // String templateSubjecTc = (String) eMsgTemlFieldMap.get("IAS_SUBJECT_TC");
                    // String templateSubjecSc = (String) eMsgTemlFieldMap.get("IAS_SUBJECT_SC");

                    // if (templateSubjecEn != null && templateSubjecEn.length() > 0) {
                    // mergedIasSubjectEn = getMergedContent(eMsgTemlFieldMap, subjectEn, subjectTc, subjectSc,
                    // "IAS_SUBJECT", LANGUAGE_EN, cmcMsgParamIasMessage, null, false);
                    // mergedIasContentEn = getMergedContent(eMsgTemlFieldMap, contentEn, contentTc, contentSc,
                    // "IAS_CONTENT", LANGUAGE_EN, cmcMsgParamIasMessage, null, false);
                    // } else {
                    // mergedIasSubjectEn = null;
                    // mergedIasContentEn = null;
                    // }

                    // if (templateSubjecTc != null && templateSubjecTc.length() > 0) {
                    // mergedIasSubjectTc = getMergedContent(eMsgTemlFieldMap, subjectEn, subjectTc, subjectSc,
                    // "IAS_SUBJECT", LANGUAGE_TC, cmcMsgParamIasMessage, null, false);
                    // mergedIasContentTc = getMergedContent(eMsgTemlFieldMap, contentEn, contentTc, contentSc,
                    // "IAS_CONTENT", LANGUAGE_TC, cmcMsgParamIasMessage, null, false);
                    // } else {
                    // mergedIasSubjectTc = null;
                    // mergedIasContentTc = null;
                    // }

                    // if (templateSubjecSc != null && templateSubjecSc.length() > 0) {
                    // mergedIasSubjectSc = getMergedContent(eMsgTemlFieldMap, subjectEn, subjectTc, subjectSc,
                    // "IAS_SUBJECT", LANGUAGE_SC, cmcMsgParamIasMessage, null, false);
                    // mergedIasContentSc = getMergedContent(eMsgTemlFieldMap, contentEn, contentTc, contentSc,
                    // "IAS_CONTENT", LANGUAGE_SC, cmcMsgParamIasMessage, null, false);
                    // } else {
                    // mergedIasSubjectSc = null;
                    // mergedIasContentSc = null;
                    // }

                    // validateIasSizeErrorResp = validateIasMsgSize(eMsgTemlFieldMap, mergedIasSubjectEn,
                    // mergedIasSubjectTc, mergedIasSubjectSc, mergedIasContentEn, mergedIasContentTc,
                    // mergedIasContentSc, properties);

                    // if (validateIasSizeErrorResp != null) {
                    // iasMsgSizeInvalid = true;
                    // response.addMessageResponse(
                    // getRecipientInvalidResponse(recipient, validateIasSizeErrorResp));
                    // } else {

                    // IasMessage_ iasMessage = new IasMessage_();
                    // iasMessage.setIasMsgId(iasMsgId);
                    // iasMessage.setPortalId(portalId);
                    // iasMessage.setTemplateId(eMsg.getTemplateId());
                    // iasMessage.setTemplateVersion(eMsg.getTemplateVersion());
                    // iasMessage.setSubjectEn(mergedIasSubjectEn);
                    // iasMessage.setSubjectTc(mergedIasSubjectTc);
                    // iasMessage.setSubjectSc(mergedIasSubjectSc);

                    // // MyGov6-C2-002: Enable at-rest encryption in all message content -- BEGIN
                    // mergedIasContentEn = EncUtils.encrypt(mergedIasContentEn);
                    // mergedIasContentTc = EncUtils.encrypt(mergedIasContentTc);
                    // mergedIasContentSc = EncUtils.encrypt(mergedIasContentSc);
                    // iasMessage.setEncInd(IntegrationConstants.ENC_IND_YES);
                    // iasMessage.setEncKeyStoreId(encKeyStoreId);
                    // // MyGov6-C2-002: Enable at-rest encryption in all message content -- END

                    // iasMessage.setContentEn(mergedIasContentEn);
                    // iasMessage.setContentTc(mergedIasContentTc);
                    // iasMessage.setContentSc(mergedIasContentSc);

                    // // CMC-2025-027: Apply suffix in template level in iAM Smart Message, iAM Smart To-Do Item, iAM Smart Application Status - BEGIN
                    // // iasMessage.setIasEsAppSuffixEn(getNodeValueFromMetaData(contentEn, iasEsAppSuffixTagName));
                    // // iasMessage.setIasEsAppSuffixTc(getNodeValueFromMetaData(contentTc, iasEsAppSuffixTagName));
                    // // iasMessage.setIasEsAppSuffixSc(getNodeValueFromMetaData(contentSc, iasEsAppSuffixTagName));
                    // iasMessage.setIasEsAppSuffixEn(cmcEMsgTemplate.getIasEsAppSuffixEn());
                    // iasMessage.setIasEsAppSuffixTc(cmcEMsgTemplate.getIasEsAppSuffixTc());
                    // iasMessage.setIasEsAppSuffixSc(cmcEMsgTemplate.getIasEsAppSuffixSc());
                    // if (getNodeValueFromMetaData(contentEn, iasEsAppSuffixTagName) != null) {
                    // iasMessage.setIasEsAppSuffixEn(
                    // getNodeValueFromMetaData(contentEn, iasEsAppSuffixTagName));
                    // }
                    // if (getNodeValueFromMetaData(contentTc, iasEsAppSuffixTagName) != null) {
                    // iasMessage.setIasEsAppSuffixTc(
                    // getNodeValueFromMetaData(contentTc, iasEsAppSuffixTagName));
                    // }
                    // if (getNodeValueFromMetaData(contentSc, iasEsAppSuffixTagName) != null) {
                    // iasMessage.setIasEsAppSuffixSc(
                    // getNodeValueFromMetaData(contentSc, iasEsAppSuffixTagName));
                    // }
                    // // CMC-2025-027: Apply suffix in template level in iAM Smart Message, iAM Smart To-Do Item, iAM Smart Application Status - END

                    // conn.begin(null, conn.getLastUpTime(), HPFW_Connection.DIRECT);
                    // iasMessage.insert(conn);
                    // conn.begin(null, conn.getLastUpTime(), HPFW_Connection.DIRECT_WITH_HISTORY);
                    // }
                    // }

                    // if (!iasMsgSizeInvalid) {
                    // // CMC-2025-017: Block iAM Smart Message receive HKID as recipientId - BEGIN
                    // msgRsp = IasMsgValidator.validateRecipientIdTypeOnlyEmptyOrOpenId(
                    // cmcEMsgTemplate.getClientId(),
                    // recipient.getIdpId(), recipient.getRecipientId(), recipient.getTranId(),
                    // recipient.getRecipientIdType());
                    // // CMC-2025-017: Block iAM Smart Message receive HKID as recipientId - END
                    // if (msgRsp != null) {
                    // response.addMessageResponse(msgRsp);
                    // } else {
                    // // CMC-2025-015: Fixed VIP iAM Smart Message cannot be sent to iAM Smart user - BEGIN
                    // // msgRsp = IasMsgValidator.validateOptInStatus(cmcMsgTemplate.getClientId(), recipient.getIdpId(),
                    // // recipient.getRecipientId(), recipient.getTranId(), iasOptCheck, validatedIasUser.getOptIn());
                    // msgRsp = IasMsgValidator.validateOptInStatus(cmcEMsgTemplate.getClientId(),
                    // recipient.getIdpId(),
                    // recipient.getRecipientId(), recipient.getTranId(), eMsgIasOptCheck,
                    // validatedIasUser.getOptIn());
                    // // CMC-2025-015: Fixed VIP iAM Smart Message cannot be sent to iAM Smart user - END

                    // if (msgRsp != null) {
                    // response.addMessageResponse(msgRsp);
                    // } else {

                    // msgRsp = IasMsgValidator.validateAction(cmcEMsgTemplate.getClientId(),
                    // recipient.getIdpId(),
                    // recipient.getRecipientId(), recipient.getTranId(), action);
                    // if (msgRsp != null) {
                    // response.addMessageResponse(msgRsp);
                    // } else {
                    // // Create IAS_USER_MESSAGE
                    // IasUserMessage_ iasUserMsg = new IasUserMessage_();
                    // iasUserMsg.setClientId(esClientId);
                    // iasUserMsg.setOpenId(recipient.getRecipientId());
                    // iasUserMsg.setIasMsgId(iasMsgId);
                    // iasUserMsg.setTranId(recipient.getTranId());
                    // iasUserMsg.setReadInd(IntegrationConstants.READ_IND_UNREAD);
                    // iasUserMsg.setDeleteInd(IntegrationConstants.DELETE_IND_NOT_DELETED);
                    // iasUserMsg.setIasNotiStatus(IntegrationConstants.IAS_NOTI_STATUS_NEW);

                    // if ((validatedIasUser != null)
                    // && (!IntegrationConstants.IAS_USER_STATUS_MISSING
                    // .equals(validatedIasUser.getStatus()))
                    // && (validatedIasUser.getNotiId() != null)
                    // && (validatedIasUser.getNotiId().length() > 0)) {
                    // iasUserMsg.setNotiId(validatedIasUser.getNotiId());
                    // }

                    // iasUserMsg.insert(conn);

                    // // Change Opt In from U to Y (For deregister user)
                    // if ((validatedIasUser != null) && (validatedIasUser.getNotiId() != null)
                    // && (IntegrationConstants.OPT_IN_U
                    // .equals(validatedIasUser.getOptIn()))) {
                    // IasEsNotiMap_ notiMap = new IasEsNotiMap_(conn,
                    // validatedIasUser.getNotiId(), eMsgServiceProviderId);
                    // notiMap.setOptIn(IntegrationConstants.OPT_IN_Y);
                    // notiMap.update(conn);
                    // }

                    // // CMC-2024-007 - Enhance Maintain Message Module to Support iAM Smart To-Do Item - BEGIN
                    // // processIasMsgResult.setSuccess(true);
                    // response.addMessageResponse(MaintainMessageUtils.getMessageResponse(
                    // recipient.getTranId(), recipient.getIdpId(), recipient.getRecipientId(),
                    // MsgTypeConstant.MESSAGE, IntegrationConstants.RESULT_CD_TRAN_SUCCESS,
                    // IntegrationConstants.RESULT_MSG_TRAN_SUCCESS));
                    // // response.addMessageResponse(getMessageResponse(recipient.getTranId(), recipient.getIdpId(), recipient.getRecipientId(), IntegrationConstants.RESULT_CD_TRAN_SUCCESS, IntegrationConstants.RESULT_MSG_TRAN_SUCCESS));
                    // // CMC-2024-007 - Enhance Maintain Message Module to Support iAM Smart To-Do Item - END
                    // }
                    // }
                    // }
                    // }
                    // // CMC-2024-007 - Enhance Maintain Message Module to Support iAM Smart To-Do Item - BEGIN
                    // }

                    logger.info("processor.processMessage - toDoItem: " + toDoItem);
                    if (toDoItem != null) {
                        try {
                            processIasToDoItemResult = iasToDoItemProcessor.processIasToDoItem(
                                    portalId, iasToDoItemId,
                                    recipient, validatedIasUserWrapped,
                                    toDoItemCutOffDay, impDtUpperLimit,
                                    dataContentEn, dataContentTc, dataContentSc,
                                    cmcToDoItemTemplate, cmcMsgParamIasToDoItem, conn,
                                    iasToDoItemHandledCache, response);
                            if (processIasToDoItemResult.isSuccess()) {
                                response.addMessageResponse(
                                        MaintainMessageUtils.getMessageResponse(recipient.getTranId(),
                                                recipient.getIdpId(),
                                                recipient.getRecipientId(),
                                                MsgTypeConstant.TO_DO_ITEM,
                                                ResultCodes.RESULT_CD_TRAN_SUCCESS,
                                                ResultMessages.RESULT_MSG_TRAN_SUCCESS));
                            }
                            iasToDoItemId = processIasToDoItemResult.getCreatedMsgId();
                        } catch (Exception e) {
                            response.addMessageResponse(
                                    MaintainMessageUtils.getMessageResponse(recipient.getTranId(), recipient.getIdpId(),
                                            recipient.getRecipientId(),
                                            MsgTypeConstant.TO_DO_ITEM,
                                            ResultCodes.RESULT_CD_GENERAL_ERROR,
                                            ResultMessages.RESULT_MSG_GENERAL_ERROR));
                            logger.info("processIasToDoItem - iasToDoItemProcessor.processIasToDoItem failed"
                                    + ". cmcTemplate.getClientId: " + cmcToDoItemTemplate.getClientId()
                                    + ", cmcTemplate.getTemplateId: " + cmcToDoItemTemplate.getTemplateId()
                                    + ", cmcTemplate.getTemplateVersion: " + cmcToDoItemTemplate.getTemplateVersion()
                                    + ", recipient.getTranId: " + recipient.getTranId()
                                    + ", recipient.getIdpId: " + recipient.getIdpId()
                                    + ", msgType: " + MsgTypeConstant.TO_DO_ITEM
                                    + ", TranResultCode: " + ResultCodes.RESULT_CD_GENERAL_ERROR
                                    + ", TranResultMessage: " + ResultMessages.RESULT_MSG_GENERAL_ERROR);
                            logger.info(
                                    "processIasToDoItem - iasToDoItemProcessor.processIasToDoItem exception: " + e);
                        }
                    }

                    logger.info("processor.processMessage - application: " + application);
                    if (application != null) {
                        try {
                            processIasApplicationResult = iasApplicationProcessor.processIasApplication(
                                    portalId, iasApplicationId,
                                    recipient, validatedIasUserWrapped,
                                    dataContentEn, dataContentTc, dataContentSc,
                                    cmcApplicationTemplate, cmcMsgParamIasApplication, conn,
                                    iasApplicationHandledCache, response);
                            if (processIasApplicationResult.isSuccess()) {
                                response.addMessageResponse(
                                        MaintainMessageUtils.getMessageResponse(recipient.getTranId(),
                                                recipient.getIdpId(),
                                                recipient.getRecipientId(),
                                                MsgTypeConstant.APPLICATION,
                                                ResultCodes.RESULT_CD_TRAN_SUCCESS,
                                                ResultMessages.RESULT_MSG_TRAN_SUCCESS));
                            }
                            iasApplicationId = processIasApplicationResult.getCreatedMsgId();
                        } catch (Exception e) {
                            logger.info("processIasApplication - iasApplicationProcessor.processIasApplication failed"
                                    + ". cmcTemplate.getClientId: " + cmcApplicationTemplate.getClientId()
                                    + ", cmcTemplate.getTemplateId: " + cmcApplicationTemplate.getTemplateId()
                                    + ", cmcTemplate.getTemplateVersion: " + cmcApplicationTemplate.getTemplateVersion()
                                    + ", recipient.getTranId: " + recipient.getTranId()
                                    + ", recipient.getAppRefNum: " + recipient.getAppRefNum()
                                    + ", recipient.getIdpId: " + recipient.getIdpId()
                                    + ", msgType: " + MsgTypeConstant.APPLICATION
                                    + ", TranResultCode: " + ResultCodes.RESULT_CD_GENERAL_ERROR
                                    + ", TranResultMessage: " + ResultMessages.RESULT_MSG_GENERAL_ERROR);
                            logger.info(
                                    "processIasApplication - iasApplicationProcessor.processIasApplication exception: "
                                            + e);
                            response.addMessageResponse(
                                    MaintainMessageUtils.getMessageResponse(recipient.getTranId(), recipient.getIdpId(),
                                            recipient.getRecipientId(),
                                            MsgTypeConstant.APPLICATION, ResultCodes.RESULT_CD_GENERAL_ERROR,
                                            ResultMessages.RESULT_MSG_GENERAL_ERROR));
                        }
                    }
                }
            }
        }

        logger.info("processor.processMessage - processed recipients.size: " + recipients.size());
        logProcessedMessageResult(response);

        logger.info("processor.processMessage - end");
    }

    private static void logProcessedMessageResult(MaintainMessageResponse response) throws Exception {
        int numOfSuccess = 0;
        int numOfFail = 0;

        List<MessageResponse> messageResponses = response.getMessageResponses();
        if (messageResponses != null) {
            for (MessageResponse messageResponse : messageResponses) {
                if (ResultCodes.RESULT_CD_TRAN_SUCCESS.equals(messageResponse.getTranResultCode())) {
                    numOfSuccess++;
                } else {
                    numOfFail++;
                }
            }
        }

        logger.info("processor.processMessage - numOfSuccess: " + numOfSuccess + ", numOfFail: " + numOfFail);
    }
}
