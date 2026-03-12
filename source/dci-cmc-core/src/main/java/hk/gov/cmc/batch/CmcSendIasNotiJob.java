package hk.gov.cmc.batch;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import hk.gov.cmc.common.CmcAppConstants;
import hk.gov.cmc.common.CmcAppPropertyNames;
import hk.gov.cmc.common.IntegrationConstants;
import hk.gov.cmc.common.StatusConstants;
import hk.gov.cmc.config.CmcEnvProperties;
import hk.gov.cmc.dao.maintainmessage.notification.IasNotiDAO;
import hk.gov.cmc.eid.bean.EIDResponseBean;
import hk.gov.cmc.eid.bean.EServiceOpenIdItem;
import hk.gov.cmc.eid.bean.EServiceOpenIdsBean;
import hk.gov.cmc.eid.bean.IasGetNotiIdResult;
import hk.gov.cmc.eid.bean.NotificationBean;
import hk.gov.cmc.eid.bean.NotificationIdItem;
import hk.gov.cmc.eid.bean.NotificationIdsBean;
import hk.gov.cmc.eid.bean.NotificationItem;
import hk.gov.cmc.eid.bean.NotificationResultItem;
import hk.gov.cmc.eid.bean.NotificationResultsBean;
import hk.gov.cmc.eid.client.EIDUtils;
import hk.gov.cmc.model.maintainmessage.emessage.IasUserMsg;
import hk.gov.cmc.model.maintainmessage.job.IasAssoQueueJob;
import hk.gov.cmc.model.maintainmessage.key.IasMsgRecipientKey;
import hk.gov.cmc.model.maintainmessage.user.IasUser;
import hk.gov.cmc.persistence.connection.hpfw.HPFW_Connection;
import hk.gov.cmc.persistence.connection.hpfw.Parameter;
import hk.gov.cmc.persistence.maintainmessage.association.IasAssoQueue_;
import hk.gov.cmc.persistence.maintainmessage.message.IasMessage_;
import hk.gov.cmc.persistence.maintainmessage.message.IasUserMessage_;
import hk.gov.cmc.persistence.maintainmessage.notification.IasEsNotiMap_;
import hk.gov.cmc.utils.common.EncUtils;
import hk.gov.cmc.utils.job.JobControlUtils;
import hk.gov.cmc.utils.maintainmessage.ias.IasUtils;

public class CmcSendIasNotiJob {

    private static Log logger = LogFactory.getLog(CmcSendIasNotiJob.class);
    private final static CmcEnvProperties cmcEnvProperties = new CmcEnvProperties();

    public void execute(Map<String, Object> params) throws Exception {

        logger.info("[BATCH_JOB]CmcSendIasNotiJob - START");

        HPFW_Connection conn = null;
        JobControlUtils jobControlUtils = new JobControlUtils();
        Properties prop = cmcEnvProperties.getProperties();

        String jobControlName = prop.getProperty(CmcAppPropertyNames.JOB_CONTROL_SEND_IAS_NOTI_PROPERTY_NAME);
        String iasGetNotiIdEndPoint = prop.getProperty(CmcAppPropertyNames.IAS_GET_NOTI_ID_END_POINT_PROPERTY_NAME);
        String iasSendNotiEndPoint = prop.getProperty(CmcAppPropertyNames.IAS_SEND_NOTI_END_POINT_PROPERTY_NAME);
        int iasMsgProcessBatchLimit = Integer
                .parseInt(prop.getProperty(CmcAppPropertyNames.IAS_MSG_PROCESS_BATCH_LIMIT_PROPERTY_NAME));
        int iasAssoJobProcessBatchLimit = Integer
                .parseInt(prop.getProperty(CmcAppPropertyNames.IAS_ASSO_JOB_PROCESS_BATCH_LIMIT_PROPERTY_NAME));
        int iasSendNotiBatchLimit = Integer
                .parseInt(prop.getProperty(CmcAppPropertyNames.IAS_SEND_NOTI_BATCH_LIMIT_PROPERTY_NAME));
        int iasGetNotiIdBatchLimit = Integer
                .parseInt(prop.getProperty(CmcAppPropertyNames.IAS_GET_NOTI_ID_BATCH_LIMIT_PROPERTY_NAME));
        String myGovSpId = prop.getProperty(CmcAppPropertyNames.MYGOV_SP_ID_PROPERTY_NAME);
        String myGovPortalId = prop.getProperty(CmcAppPropertyNames.MYGOV_PORTAL_ID_PROPERTY_NAME);
        String myGovClientId = prop.getProperty(CmcAppPropertyNames.MYGOV_CLIENT_ID_PROPERTY_NAME);
        String serverId = prop.getProperty(CmcAppPropertyNames.SERVER_ID_PROPERTY_NAME);
        String welcomeMsgTemplId = prop.getProperty(CmcAppPropertyNames.IAS_WELCOME_MSG_TEMPLATE_ID_PROPERTY_NAME);
        String welcomeMsgTemplVer = prop
                .getProperty(CmcAppPropertyNames.IAS_WELCOME_MSG_TEMPLATE_VERSION_PROPERTY_NAME);
        String welcomeMsgTemplSpId = prop.getProperty(CmcAppPropertyNames.IAS_WELCOME_MSG_TEMPLATE_SP_ID_PROPERTY_NAME);
        String welcomeMsgTemplSpTag = prop
                .getProperty(CmcAppPropertyNames.IAS_WELCOME_MSG_TEMPLATE_SP_TAG_PROPERTY_NAME);
        String encKeyStoreId = prop.getProperty(CmcAppPropertyNames.CMC_MESSAGE_ENCRYPT_KEY_STORE_ID_PROPERTY_NAME);

        try {

            if (jobControlUtils.lockJobControl(jobControlName)) {
                conn = HPFW_Connection.getHPFW_Connection();

                conn.begin(null, conn.getLastUpTime(), HPFW_Connection.DIRECT_WITH_HISTORY);

                logger.info("[BATCH_JOB]CmcSendIasNotiJob - Update NOTI ID - Start");

                IasNotiDAO iasNotiDAO = new IasNotiDAO();

                // Retrieve ClientID, OpenID pair which missed NotiID
                List<IasUserMsg> notiIdMissList = iasNotiDAO.getNotiIdMissingIasUserMessage(conn,
                        iasMsgProcessBatchLimit);

                // Create List for deregistered clientID+OpenID with key IasMsgRecipientKey
                List<IasMsgRecipientKey> iasDeregOpenIdList = new ArrayList<IasMsgRecipientKey>();

                boolean eIDUtilsInit = false;

                logger.info("[BATCH_JOB]CmcSendIasNotiJob - notiIdMissList.size()=" + notiIdMissList.size());

                if (notiIdMissList.size() > 0) {

                    int totalNotiIdCnt = 0;
                    // Create HashMap iasOpenNotiIdMap with key IasMsgRecipientKey and value NOTI_ID
                    HashMap<IasMsgRecipientKey, IasGetNotiIdResult> iasOpenNotiIdMap = new HashMap<IasMsgRecipientKey, IasGetNotiIdResult>();

                    // Create List for invalid clientID+OpenID with key IasMsgRecipientKey
                    List<IasMsgRecipientKey> iasInvalidOpenIdList = new ArrayList<IasMsgRecipientKey>();

                    // Create List for clientID+OpenID pair to check duplicate in getNotiId request with key IasMsgRecipientKey
                    List<IasMsgRecipientKey> getNotiIdReqOpenIdList = new ArrayList<IasMsgRecipientKey>();

                    // Retrieve the noti id from iAM Smart system
                    while (totalNotiIdCnt < notiIdMissList.size()) {

                        logger.info("[BATCH_JOB]CmcSendIasNotiJob - iasUserMsgNotiIdMissList - totalNotiIdCnt="
                                + totalNotiIdCnt);

                        int batchCnt = 0;

                        ArrayList<EServiceOpenIdItem> eServiceOpenIdItemList = new ArrayList<EServiceOpenIdItem>();
                        while ((batchCnt < iasGetNotiIdBatchLimit) && (totalNotiIdCnt < notiIdMissList.size())) {
                            IasUserMsg iasUserMsg = (IasUserMsg) notiIdMissList.get(totalNotiIdCnt);

                            // Check duplicate client_id+open_id in getNotiId request
                            IasMsgRecipientKey iasMsgRecpKey = new IasMsgRecipientKey(iasUserMsg.getClientId(),
                                    iasUserMsg.getOpenId());
                            if (!getNotiIdReqOpenIdList.contains(iasMsgRecpKey)) {
                                EServiceOpenIdItem eServiceOpenIdItem = new EServiceOpenIdItem(iasUserMsg.getClientId(),
                                        iasUserMsg.getOpenId());
                                eServiceOpenIdItemList.add(eServiceOpenIdItem);
                                getNotiIdReqOpenIdList.add(iasMsgRecpKey);
                                batchCnt++;
                            }

                            totalNotiIdCnt++;
                        }
                        logger.info("[BATCH_JOB]CmcSendIasNotiJob - iasUserMsgNotiIdMissList - batchCnt=" + batchCnt);

                        logger.info(
                                "[BATCH_JOB]CmcSendIasNotiJob - iasUserMsgNotiIdMissList - eServiceOpenIdItemList.size()="
                                        + eServiceOpenIdItemList.size());

                        if (eServiceOpenIdItemList.size() > 0) {
                            EServiceOpenIdsBean eServiceOpenIdsBean = new EServiceOpenIdsBean();
                            eServiceOpenIdsBean.seteServiceOpenIDs(eServiceOpenIdItemList);

                            // Call getNotificationID API to get missing NotiID from iAM Smart system by Service Provider ID and Open ID
                            if (!eIDUtilsInit) {
                                EIDUtils.initialize(prop);
                                eIDUtilsInit = true;
                            }
                            EIDResponseBean getNotiIdResponse = EIDUtils
                                    .doRequestGetNotificationIDs(iasGetNotiIdEndPoint, eServiceOpenIdsBean);

                            if (getNotiIdResponse != null) {

                                logger.info(
                                        "[BATCH_JOB]CmcSendIasNotiJob - iasUserMsgNotiIdMissList - doRequestGetNotificationIDs - TxID="
                                                + getNotiIdResponse.getTxID() + ", Code="
                                                + getNotiIdResponse.getCode() + ", Message="
                                                + getNotiIdResponse.getMessage());

                                logger.info(
                                        "[BATCH_JOB]CmcSendIasNotiJob - iasUserMsgNotiIdMissList - doRequestGetNotificationIDs - Content="
                                                + getNotiIdResponse.getContent());

                                if (((IntegrationConstants.GET_NOTI_ID_RESULT_CODE_SUCCESS
                                        .equalsIgnoreCase(getNotiIdResponse.getCode()))
                                        || (IntegrationConstants.GET_NOTI_ID_RESULT_CODE_PARTIAL_SUCCESS
                                                .equalsIgnoreCase(getNotiIdResponse.getCode())))
                                        && getNotiIdResponse.getContent() != null
                                        && getNotiIdResponse.getContent().length() > 0) {

                                    NotificationIdsBean notiIdsResult = new NotificationIdsBean(
                                            getNotiIdResponse.getContent());
                                    ArrayList<NotificationIdItem> notiIdResultList = notiIdsResult.getNotificationIDs();

                                    // Loop the getNotificationID result
                                    for (NotificationIdItem notiItem : notiIdResultList) {
                                        IasMsgRecipientKey iasMsgRecpKey = new IasMsgRecipientKey(
                                                notiItem.getClientID(), notiItem.getOpenID());
                                        if ((IntegrationConstants.GET_NOTI_ID_RESULT_VALID.equals(notiItem.getStatus()))
                                                ||
                                                (IntegrationConstants.GET_NOTI_ID_RESULT_V2_ACTIVE
                                                        .equals(notiItem.getStatus()))
                                                ||
                                                (IntegrationConstants.GET_NOTI_ID_RESULT_V2_SUSPEND
                                                        .equals(notiItem.getStatus()))
                                                ||
                                                (IntegrationConstants.GET_NOTI_ID_RESULT_V2_INVALID
                                                        .equals(notiItem.getStatus()))
                                                ||
                                                (IntegrationConstants.GET_NOTI_ID_RESULT_V2_DEREGISTERED
                                                        .equals(notiItem.getStatus()))) {
                                            iasOpenNotiIdMap.put(iasMsgRecpKey, new IasGetNotiIdResult(
                                                    notiItem.getNotificationID(), notiItem.getStatus()));

                                            if (IntegrationConstants.GET_NOTI_ID_RESULT_V2_DEREGISTERED
                                                    .equals(notiItem.getStatus())) {
                                                if (!iasDeregOpenIdList.contains(iasMsgRecpKey)) {
                                                    iasDeregOpenIdList.add(iasMsgRecpKey);
                                                }
                                            }
                                        } else {
                                            if (!iasInvalidOpenIdList.contains(iasMsgRecpKey)) {
                                                iasInvalidOpenIdList.add(iasMsgRecpKey);
                                            }
                                        }
                                    }
                                } else {
                                    logger.warn(
                                            "[BATCH_JOB]CmcSendIasNotiJob - iasUserMsgNotiIdMissList - doGetNotificationIDs failed. Code="
                                                    + getNotiIdResponse.getCode() + ", Message="
                                                    + getNotiIdResponse.getMessage() + ", TxID="
                                                    + getNotiIdResponse.getTxID()
                                                    + ". No DB Change, will retry again in next run.");
                                }
                            } else {
                                logger.warn(
                                        "[BATCH_JOB]CmcSendIasNotiJob - iasUserMsgNotiIdMissList - doGetNotificationIDs failed. getNotiIdResponse is null. No DB Change, will retry again in next run.");
                            }
                        }
                    }

                    // Loop the notiIdMissList
                    for (IasUserMsg iasUserMsg : notiIdMissList) {

                        // Check if it is VIP
                        boolean iasAutoCreate = "Y".equals(iasUserMsg.getIasAutoCreateInd());

                        logger.info("[BATCH_JOB]CmcSendIasNotiJob - ClientId=" + iasUserMsg.getClientId() + ", OpenId="
                                + iasUserMsg.getOpenId() +
                                ", IasMsgId=" + iasUserMsg.getIasMsgId() + ", ServiceProviderId="
                                + iasUserMsg.getServiceProviderId() + ", iasAutoCreate=" + iasAutoCreate);

                        // Get the NOTI_ID from iasOpenNotiIdMap
                        IasGetNotiIdResult iasGetNotiIdResult = iasOpenNotiIdMap
                                .get(new IasMsgRecipientKey(iasUserMsg.getClientId(), iasUserMsg.getOpenId()));

                        // Client ID, Open ID pair is valid
                        if (iasGetNotiIdResult != null && iasGetNotiIdResult.getNotiId() != null
                                && iasGetNotiIdResult.getNotiId().length() > 0) {

                            logger.info("[BATCH_JOB]CmcSendIasNotiJob - Mapped notiId=" + iasGetNotiIdResult.getNotiId()
                                    + ", Status=" + iasGetNotiIdResult.getStatus());

                            // For mySubscription SP, If IasEsNotiMap_ record not exist in DB, insert a new one
                            if (!iasUserMsg.getServiceProviderId().equals(iasUserMsg.getIasOptSpId())) {

                                IasUser mySubIasUser = iasNotiDAO.getIasEsNotiMap(conn, iasGetNotiIdResult.getNotiId(),
                                        iasUserMsg.getIasOptSpId());

                                if (mySubIasUser == null) {
                                    // Create IAS_ES_NOTI_MAP
                                    IasEsNotiMap_ notiMap = new IasEsNotiMap_();
                                    notiMap.setServiceProviderId(iasUserMsg.getIasOptSpId());
                                    notiMap.setNotiId(iasGetNotiIdResult.getNotiId());
                                    if (IntegrationConstants.GET_NOTI_ID_RESULT_V2_DEREGISTERED
                                            .equals(iasGetNotiIdResult.getStatus()))
                                        notiMap.setOptIn(IntegrationConstants.OPT_IN_U);
                                    else
                                        notiMap.setOptIn(IntegrationConstants.OPT_IN_Y);

                                    notiMap.insert(conn);
                                }
                            } else {

                                IasUser iasUser = iasNotiDAO.getIasEsNotiMap(conn, iasGetNotiIdResult.getNotiId(),
                                        iasUserMsg.getServiceProviderId());

                                // If IasEsNotiMap_ record not exist in DB, insert a new one
                                if (iasUser == null) {

                                    // Create IAS_ES_NOTI_MAP
                                    IasEsNotiMap_ notiMap = new IasEsNotiMap_();
                                    notiMap.setServiceProviderId(iasUserMsg.getServiceProviderId());
                                    notiMap.setNotiId(iasGetNotiIdResult.getNotiId());

                                    // Only set Open ID for non-VIP flow, Because for VIP flow, open id is from MyGov, not the VIP
                                    if ((iasUserMsg.getClientId().equals(myGovClientId)) && iasAutoCreate) {
                                        logger.info(
                                                "[BATCH_JOB]CmcSendIasNotiJob - VIP, no need to set Open ID in ias_es_noti_map");
                                    } else {
                                        notiMap.setOpenId(iasUserMsg.getOpenId());
                                    }

                                    // If Transactional message case (i.e. IAS_OPT_CHECK_IND=N), default opt in value is U
                                    // Input field for "ias_opt_check_ind" is not used because "ias_show_es_set_btn" derive the same meaning. So, check "ias_show_es_set_btn" while processing message requests.
                                    // if ("N".equals(iasUserMsg.getIasOptCheckInd()))
                                    if (!"Y".equals(iasUserMsg.getIasOptCheckInd())) { // getIasOptCheckInd() using the value of ias_show_es_set_btn in DB
                                        notiMap.setOptIn(IntegrationConstants.OPT_IN_U);
                                    } else {
                                        if (IntegrationConstants.GET_NOTI_ID_RESULT_V2_DEREGISTERED
                                                .equals(iasGetNotiIdResult.getStatus()))
                                            notiMap.setOptIn(IntegrationConstants.OPT_IN_U);
                                        else
                                            notiMap.setOptIn(IntegrationConstants.OPT_IN_Y);
                                    }

                                    notiMap.insert(conn);
                                } else {
                                    // Check if IAS_USER_MESSAGE record is not auto created by VIP flow. i.e. Client ID != MyGov Client ID
                                    if ((!iasUserMsg.getClientId().equals(myGovClientId))
                                            && ((iasUser.getOpenId() == null || iasUser.getOpenId().length() == 0))) {
                                        // Update Open ID for map record previously created by VIP flow
                                        IasEsNotiMap_ notiMap = new IasEsNotiMap_(conn, iasGetNotiIdResult.getNotiId(),
                                                iasUserMsg.getServiceProviderId());
                                        notiMap.setOpenId(iasUserMsg.getOpenId());
                                        notiMap.update(conn);
                                    }
                                }
                            }

                            // If VIP flow (Auto created by VIP flow. i.e. Client ID = MyGov Client ID), create map record for MyGov if not exist
                            if ((iasUserMsg.getClientId().equals(myGovClientId)) && iasAutoCreate) {
                                boolean myGovNotiMapInDB = iasNotiDAO.isIasEsNotiMapExist(conn,
                                        iasGetNotiIdResult.getNotiId(), myGovSpId);

                                // If IasEsNotiMap_ record not exist in DB, insert a new one
                                if (myGovNotiMapInDB == false) {
                                    // Create IAS_ES_NOTI_MAP
                                    IasEsNotiMap_ notiMap = new IasEsNotiMap_();
                                    notiMap.setServiceProviderId(myGovSpId);
                                    notiMap.setNotiId(iasGetNotiIdResult.getNotiId());
                                    notiMap.setOpenId(iasUserMsg.getOpenId());
                                    notiMap.setOptIn(IntegrationConstants.OPT_IN_U);
                                    notiMap.insert(conn);
                                }
                            }

                            // Update NOTI_ID for IAS_USER_MESSAGE
                            IasUserMessage_ iasUserMessage = new IasUserMessage_(conn, iasUserMsg.getClientId(),
                                    iasUserMsg.getOpenId(), iasUserMsg.getIasMsgId());
                            iasUserMessage.setNotiId(iasGetNotiIdResult.getNotiId());
                            iasUserMessage.update(conn);

                        } else { // Check if OPEN_ID invalid
                            if (iasInvalidOpenIdList.contains(
                                    new IasMsgRecipientKey(iasUserMsg.getClientId(), iasUserMsg.getOpenId()))) {
                                // Update IAS_NOTI_STATUS for IAS_USER_MESSAGE
                                IasUserMessage_ iasUserMessage = new IasUserMessage_(conn, iasUserMsg.getClientId(),
                                        iasUserMsg.getOpenId(), iasUserMsg.getIasMsgId());
                                iasUserMessage.setIasNotiStatus(IntegrationConstants.IAS_NOTI_STATUS_INVALID);
                                iasUserMessage.update(conn);

                                // For iAM Smart Undelivered Report 06D
                                String rcptType = IntegrationConstants.IAS_MSG_RCPT_TYPE_IAM_SMART;
                                if ((iasUserMsg.getClientId().equals(myGovClientId)) && iasAutoCreate) {
                                    rcptType = IntegrationConstants.IAS_MSG_RCPT_TYPE_MY_ID;
                                }
                                logger.info(
                                        "[BATCH_JOB]CmcSendIasNotiJob - iasNotiDAO.createIasUndeliveredMessage (OPEN_ID invalid)");
                                iasNotiDAO.createIasUndeliveredMessage(conn, iasUserMsg.getClientId(),
                                        iasUserMsg.getOpenId(), iasUserMsg.getIasMsgId(),
                                        IntegrationConstants.IAS_UNDELIVER_MSG_REASON_ACC_NOT_ACTIVE, rcptType);
                            }
                        }
                        conn.commit();
                    }
                }

                logger.info("[BATCH_JOB]CmcSendIasNotiJob - Update NOTI ID - End");

                logger.info("[BATCH_JOB]CmcSendIasNotiJob - Create mySubscription mother missing map record- Start");

                // Retrieve mySubscription mother missing map record
                List<IasUserMsg> motherMissMapList = iasNotiDAO.getMotherMissingVipMapList(conn,
                        iasMsgProcessBatchLimit);
                logger.info("[BATCH_JOB]CmcSendIasNotiJob - motherMissMapList.size()=" + motherMissMapList.size());

                ArrayList<String> addedMissMapList = new ArrayList<>();

                // Loop the List
                for (int i = 0; i < motherMissMapList.size(); i++) {

                    IasUserMsg iasUserMsg = motherMissMapList.get(i);

                    // Create IAS_ES_NOTI_MAP for mySubscription mother if missing
                    if (!addedMissMapList.contains(iasUserMsg.getIasOptSpId() + iasUserMsg.getNotiId())) {

                        logger.info(
                                "[BATCH_JOB]CmcSendIasNotiJob - Create IAS_ES_NOTI_MAP for mySubscription mother missing record "
                                        + "IasOptSpId=" + iasUserMsg.getIasOptSpId() + ", NotiId="
                                        + iasUserMsg.getNotiId());

                        // Create IAS_ES_NOTI_MAP
                        IasEsNotiMap_ notiMap = new IasEsNotiMap_();
                        notiMap.setServiceProviderId(iasUserMsg.getIasOptSpId());
                        notiMap.setNotiId(iasUserMsg.getNotiId());
                        notiMap.setOptIn(IntegrationConstants.OPT_IN_Y);
                        notiMap.insert(conn);
                        conn.commit();

                        addedMissMapList.add(iasUserMsg.getIasOptSpId() + iasUserMsg.getNotiId());
                    }
                }
                logger.info("[BATCH_JOB]CmcSendIasNotiJob - Create mySubscription mother missing map record- End");

                logger.info("[BATCH_JOB]CmcSendIasNotiJob - Process ias_asso_queue record - Start");
                /*
                 * 1. Retrieve outstanding jobs from IAS_ASSO_QUEUE (number of jobs limited by configured value in property)
                 * 2. For each outstanding record,
                 * - retrieve ias_auto_create_ind, ias_opt_sp_id from cmc_service_provider by service_provider_id
                 * // check if mapping record for non-vip e-service exist
                 * - check if record exist in ias_es_noti_map by open_id=(open_id from IAS_ASSO_QUEUE) and service_provider_id=ias_opt_sp_id
                 * if yes,
                 * Check the record if opt_in = 'U'
                 * If yes,
                 * - Get account status from ias_user_noti_info
                 * - if account status <> 'D'
                 * - update the record by opt_in='Y'
                 * - create welcome message record for service_provider_id=ias_opt_sp_id
                 * If no, no action
                 * else,
                 * - call getNotificationID API to retrieve noti_id and account status from iAM Smart with open_id and client_id(in IAS_ASSO_QUEUE)
                 * // check if mapping record for vip e-service (not the record with SPID=MARS) exist
                 * - check if record exist in ias_es_noti_map by noti_id=(noti_id from getNotificationID API) and service_provider_id=ias_opt_sp_id
                 * if yes,
                 * // for the case when users associated iAM Smart in ROTI before associate with MyGovHK
                 * if ias_auto_create_ind='Y' and client_id(in IAS_ASSO_QUEUE)=Client ID of MyGovHK (in property file)
                 * - check if record exist in ias_es_noti_map by noti_id=(noti_id from getNotificationID API) and service_provider_id='MARS'
                 * if yes, no action
                 * else, insert new ias_es_noti_map record of service_provider_id='MARS', noti_id=(noti_id from getNotificationID API), open_id=(open_id from IAS_ASSO_QUEUE) and opt_in='U'
                 * // for the case when users associated MyGovHK before associate with iAM Smart in ROTI
                 * else,
                 * - update the record by open_id=(open_id from ias_asso_queue)
                 * 
                 * - Check the record if opt_in = 'U'
                 * If yes,
                 * if account status <> 'D'
                 * - update the record by opt_in='Y'
                 * - create welcome message record for service_provider_id=ias_opt_sp_id
                 * If no, no action
                 * else,
                 * // no mapping record in db and asso queue record is added by MyGovHK
                 * if ias_auto_create_ind='Y' and client_id(in IAS_ASSO_QUEUE)=Client ID of MyGovHK (in property file),
                 * - check if record exist in ias_es_noti_map by noti_id=(noti_id from getNotificationID API) and service_provider_id='MARS'
                 * if yes,
                 * no action
                 * else,
                 * insert new ias_es_noti_map record of service_provider_id='MARS', noti_id=(noti_id from getNotificationID API), open_id=(open_id from IAS_ASSO_QUEUE) and opt_in='U'
                 * 
                 * if account status <> 'D'
                 * - insert new ias_es_noti_map record of service_provider_id=ias_opt_sp_id, noti_id=(noti_id from getNotificationID API), open_id=null and opt_in='Y'
                 * else
                 * - insert new ias_es_noti_map record of service_provider_id=ias_opt_sp_id, noti_id=(noti_id from getNotificationID API), open_id=null and opt_in='U'
                 * 
                 * // no mapping record in db and asso queue record is added by ROTI
                 * else,
                 * if account status <> 'D'
                 * - insert new ias_es_noti_map record of service_provider_id=ias_opt_sp_id, noti_id=(noti_id from getNotificationID API), open_id=(open_id from IAS_ASSO_QUEUE) and opt_in='Y'
                 * else
                 * - insert new ias_es_noti_map record of service_provider_id=ias_opt_sp_id, noti_id=(noti_id from getNotificationID API), open_id=(open_id from IAS_ASSO_QUEUE) and opt_in='U'
                 * 
                 * if account status <> 'D'
                 * - create welcome message record for service_provider_id=ias_opt_sp_id
                 * 
                 */

                // Retrieve ClientID, OpenID pair which missed ias_es_noti_map record
                List<IasAssoQueueJob> iasAssoJobList = iasNotiDAO.getIasAssoJobFromIasAssoQueue(conn,
                        iasAssoJobProcessBatchLimit);

                logger.info("[BATCH_JOB]CmcSendIasNotiJob - iasAssoJobList.size()=" + iasAssoJobList.size());

                if (iasAssoJobList.size() > 0) {

                    Map<String, String> eMsgTemlFieldMap = iasNotiDAO.getTemplateByIdVersion(conn, welcomeMsgTemplId,
                            welcomeMsgTemplVer, welcomeMsgTemplSpId);
                    String templateSubjectEn = (String) eMsgTemlFieldMap.get("IAS_SUBJECT_EN");
                    String templateSubjectTc = (String) eMsgTemlFieldMap.get("IAS_SUBJECT_TC");
                    String templateSubjectSc = (String) eMsgTemlFieldMap.get("IAS_SUBJECT_SC");
                    String templateContentEn = (String) eMsgTemlFieldMap.get("IAS_CONTENT_EN");
                    String templateContentTc = (String) eMsgTemlFieldMap.get("IAS_CONTENT_TC");
                    String templateContentSc = (String) eMsgTemlFieldMap.get("IAS_CONTENT_SC");

                    // Create List for record not exist in ias_es_noti_map by open_id=(open_id from IAS_ASSO_QUEUE) and service_provider_id=ias_opt_sp_id
                    List<IasAssoQueueJob> outstandingAssoJobList = new ArrayList<IasAssoQueueJob>();

                    // check if record exist in ias_es_noti_map by open_id=(open_id from IAS_ASSO_QUEUE) and service_provider_id=ias_opt_sp_id
                    for (IasAssoQueueJob iasAssoJob : iasAssoJobList) {

                        logger.info("[BATCH_JOB]CmcSendIasNotiJob - iasAssoJob.getJobId()=" + iasAssoJob.getJobId());

                        if (iasAssoJob.getMapNotiId() != null && iasAssoJob.getMapNotiId().length() > 0) {

                            // Update the ias_es_noti_map by opt_in='Y' if opt_in='U'
                            if (IntegrationConstants.OPT_IN_U.equals(iasAssoJob.getOptIn())
                                    && (!IntegrationConstants.IAS_USER_STATUS_DEREGISTERED
                                            .equals(iasAssoJob.getAccStatus()))) {
                                IasEsNotiMap_ notiMap = new IasEsNotiMap_(conn, iasAssoJob.getMapNotiId(),
                                        iasAssoJob.getIasOptSpId());
                                notiMap.setOptIn(IntegrationConstants.OPT_IN_Y);
                                notiMap.update(conn);

                                // create welcome message record for service_provider_id=ias_opt_sp_id
                                String iasMsgId = IasUtils.getNextIasMsgId(serverId);

                                IasMessage_ iasMessage = new IasMessage_();
                                iasMessage.setIasMsgId(iasMsgId);
                                iasMessage.setPortalId(myGovPortalId);
                                iasMessage.setTemplateId(welcomeMsgTemplId);
                                iasMessage.setTemplateVersion(welcomeMsgTemplVer);
                                iasMessage.setSubjectEn(templateSubjectEn.replace(welcomeMsgTemplSpTag,
                                        "" + iasAssoJob.getServiceProviderNameEn()));
                                iasMessage.setSubjectTc(templateSubjectTc.replace(welcomeMsgTemplSpTag,
                                        "" + iasAssoJob.getServiceProviderNameTc()));
                                iasMessage.setSubjectSc(templateSubjectSc.replace(welcomeMsgTemplSpTag,
                                        "" + iasAssoJob.getServiceProviderNameSc()));

                                iasMessage.setContentEn(EncUtils.encrypt(templateContentEn.replace(welcomeMsgTemplSpTag,
                                        "" + iasAssoJob.getServiceProviderNameEn())));
                                iasMessage.setContentTc(EncUtils.encrypt(templateContentTc.replace(welcomeMsgTemplSpTag,
                                        "" + iasAssoJob.getServiceProviderNameTc())));
                                iasMessage.setContentSc(EncUtils.encrypt(templateContentSc.replace(welcomeMsgTemplSpTag,
                                        "" + iasAssoJob.getServiceProviderNameSc())));
                                iasMessage.setEncInd(CmcAppConstants.ENC_IND_YES);
                                iasMessage.setEncKeyStoreId(encKeyStoreId);

                                conn.begin(null, conn.getLastUpTime(), HPFW_Connection.DIRECT);
                                iasMessage.insert(conn);
                                conn.begin(null, conn.getLastUpTime(), HPFW_Connection.DIRECT_WITH_HISTORY);

                                // Create IAS_USER_MESSAGE
                                IasUserMessage_ iasUserMsg = new IasUserMessage_();
                                iasUserMsg.setClientId(iasAssoJob.getClientId());
                                iasUserMsg.setOpenId(iasAssoJob.getOpenId());
                                iasUserMsg.setNotiId(iasAssoJob.getMapNotiId());
                                iasUserMsg.setIasMsgId(iasMsgId);
                                iasUserMsg.setTranId(iasAssoJob.getJobId());
                                iasUserMsg.setReadInd(StatusConstants.READ_IND_UNREAD);
                                iasUserMsg.setDeleteInd(StatusConstants.DELETE_IND_NOT_DELETED);
                                iasUserMsg.setIasNotiStatus(IntegrationConstants.IAS_NOTI_STATUS_NEW);

                                iasUserMsg.insert(conn);
                            }

                            // Mark complete for the ias_asso_queue record
                            IasAssoQueue_ iasAssoQueue = new IasAssoQueue_(conn, iasAssoJob.getJobId());
                            iasAssoQueue.setJobStatus(IntegrationConstants.JOB_STATUS_COMPLETE);

                            conn.begin(null, conn.getLastUpTime(), HPFW_Connection.DIRECT);
                            iasAssoQueue.update(conn);
                            conn.commit();

                            conn.begin(null, conn.getLastUpTime(), HPFW_Connection.DIRECT_WITH_HISTORY);
                        } else {
                            outstandingAssoJobList.add(iasAssoJob);
                        }
                    }

                    // Retrieve the noti id from iAM Smart system

                    int totalNotiIdCnt = 0;
                    // Create HashMap iasOpenNotiIdMap with key IasMsgRecipientKey and value NOTI_ID
                    HashMap<IasMsgRecipientKey, IasGetNotiIdResult> iasOpenNotiIdMap = new HashMap<IasMsgRecipientKey, IasGetNotiIdResult>();

                    // Create List for invalid clientID+OpenID with key IasMsgRecipientKey
                    List<IasMsgRecipientKey> iasInvalidOpenIdList = new ArrayList<IasMsgRecipientKey>();

                    // Create List for clientID+OpenID pair to check duplicate in getNotiId request with key IasMsgRecipientKey
                    List<IasMsgRecipientKey> getNotiIdReqOpenIdList = new ArrayList<IasMsgRecipientKey>();

                    // Create List for deregistered clientID+OpenID with key IasMsgRecipientKey
                    List<IasMsgRecipientKey> assoIasDeregOpenIdList = new ArrayList<IasMsgRecipientKey>();

                    logger.info("[BATCH_JOB]CmcSendIasNotiJob - outstandingAssoJobList.size()="
                            + outstandingAssoJobList.size());

                    while (totalNotiIdCnt < outstandingAssoJobList.size()) {

                        logger.info("[BATCH_JOB]CmcSendIasNotiJob - outstandingAssoJobList - totalNotiIdCnt="
                                + totalNotiIdCnt);

                        int batchCnt = 0;

                        ArrayList<EServiceOpenIdItem> eServiceOpenIdItemList = new ArrayList<>();
                        while ((batchCnt < iasGetNotiIdBatchLimit) && (totalNotiIdCnt < iasAssoJobList.size())) {

                            IasAssoQueueJob iasAssoJob = (IasAssoQueueJob) iasAssoJobList.get(totalNotiIdCnt);

                            // Check duplicate client_id+open_id in getNotiId request
                            IasMsgRecipientKey iasMsgRecpKey = new IasMsgRecipientKey(iasAssoJob.getClientId(),
                                    iasAssoJob.getOpenId());
                            if (!getNotiIdReqOpenIdList.contains(iasMsgRecpKey)) {
                                EServiceOpenIdItem eServiceOpenIdItem = new EServiceOpenIdItem(iasAssoJob.getClientId(),
                                        iasAssoJob.getOpenId());
                                eServiceOpenIdItemList.add(eServiceOpenIdItem);
                                getNotiIdReqOpenIdList.add(iasMsgRecpKey);
                                batchCnt++;
                            }
                            totalNotiIdCnt++;
                        }

                        logger.info("[BATCH_JOB]CmcSendIasNotiJob - outstandingAssoJobList - batchCnt=" + batchCnt);

                        logger.info(
                                "[BATCH_JOB]CmcSendIasNotiJob - outstandingAssoJobList - eServiceOpenIdItemList.size()="
                                        + eServiceOpenIdItemList.size());

                        if (eServiceOpenIdItemList.size() > 0) {

                            EServiceOpenIdsBean eServiceOpenIdsBean = new EServiceOpenIdsBean();
                            eServiceOpenIdsBean.seteServiceOpenIDs(eServiceOpenIdItemList);

                            // Call getNotificationID API to get missing NotiID from iAM Smart system by Service Provider ID and Open ID
                            if (!eIDUtilsInit) {
                                EIDUtils.initialize(prop);
                                eIDUtilsInit = true;
                            }
                            EIDResponseBean getNotiIdResponse = EIDUtils
                                    .doRequestGetNotificationIDs(iasGetNotiIdEndPoint, eServiceOpenIdsBean);

                            if (getNotiIdResponse != null) {

                                logger.info(
                                        "[BATCH_JOB]CmcSendIasNotiJob - outstandingAssoJobList - EIDUtils.doRequestGetNotificationIDs - TxID="
                                                + getNotiIdResponse.getTxID() + ", Code="
                                                + getNotiIdResponse.getCode() + ", Message="
                                                + getNotiIdResponse.getMessage());

                                logger.info(
                                        "[BATCH_JOB]CmcSendIasNotiJob - outstandingAssoJobList - EIDUtils.doRequestGetNotificationIDs - Content="
                                                + getNotiIdResponse.getContent());

                                if (((IntegrationConstants.GET_NOTI_ID_RESULT_CODE_SUCCESS
                                        .equalsIgnoreCase(getNotiIdResponse.getCode()))
                                        || (IntegrationConstants.GET_NOTI_ID_RESULT_CODE_PARTIAL_SUCCESS
                                                .equalsIgnoreCase(getNotiIdResponse.getCode())))
                                        && getNotiIdResponse.getContent() != null
                                        && getNotiIdResponse.getContent().length() > 0) {

                                    NotificationIdsBean notiIdsResult = new NotificationIdsBean(
                                            getNotiIdResponse.getContent());
                                    ArrayList<NotificationIdItem> notiIdResultList = notiIdsResult.getNotificationIDs();

                                    // Loop the getNotificationID result
                                    for (NotificationIdItem notiItem : notiIdResultList) {
                                        IasMsgRecipientKey iasMsgRecpKey = new IasMsgRecipientKey(
                                                notiItem.getClientID(), notiItem.getOpenID());
                                        if ((IntegrationConstants.GET_NOTI_ID_RESULT_VALID.equals(notiItem.getStatus()))
                                                ||
                                                (IntegrationConstants.GET_NOTI_ID_RESULT_V2_ACTIVE
                                                        .equals(notiItem.getStatus()))
                                                ||
                                                (IntegrationConstants.GET_NOTI_ID_RESULT_V2_SUSPEND
                                                        .equals(notiItem.getStatus()))
                                                ||
                                                (IntegrationConstants.GET_NOTI_ID_RESULT_V2_INVALID
                                                        .equals(notiItem.getStatus()))
                                                ||
                                                (IntegrationConstants.GET_NOTI_ID_RESULT_V2_DEREGISTERED
                                                        .equals(notiItem.getStatus()))) {
                                            iasOpenNotiIdMap.put(iasMsgRecpKey, new IasGetNotiIdResult(
                                                    notiItem.getNotificationID(), notiItem.getStatus()));

                                            if (IntegrationConstants.GET_NOTI_ID_RESULT_V2_DEREGISTERED
                                                    .equals(notiItem.getStatus())) {
                                                if (!assoIasDeregOpenIdList.contains(iasMsgRecpKey)) {
                                                    assoIasDeregOpenIdList.add(iasMsgRecpKey);
                                                }
                                            }
                                        } else {
                                            if (!iasInvalidOpenIdList.contains(iasMsgRecpKey)) {
                                                iasInvalidOpenIdList.add(iasMsgRecpKey);
                                            }
                                        }
                                    }
                                } else {
                                    logger.warn(
                                            "[BATCH_JOB]CmcSendIasNotiJob - outstandingAssoJobList - EIDUtils.doRequestGetNotificationIDs failed. Code="
                                                    + getNotiIdResponse.getCode() + ", Message="
                                                    + getNotiIdResponse.getMessage() + ", TxID="
                                                    + getNotiIdResponse.getTxID()
                                                    + ". No DB Change, will retry again in next run.");
                                }
                            } else {
                                logger.warn(
                                        "[BATCH_JOB]CmcSendIasNotiJob - outstandingAssoJobList - EIDUtils.doRequestGetNotificationIDs failed. getNotiIdResponse is null. No DB Change, will retry again in next run.");
                            }
                        }

                    }

                    // Loop the outstandingAssoJobList
                    for (IasAssoQueueJob iasAssoJob : outstandingAssoJobList) {

                        logger.info("[BATCH_JOB]CmcSendIasNotiJob - iasAssoJob.getJobId()=" + iasAssoJob.getJobId());

                        // Check if it is VIP
                        boolean iasAutoCreate = "Y".equals(iasAssoJob.getIasAutoCreateInd());

                        logger.info("[BATCH_JOB]CmcSendIasNotiJob - ClientId=" + iasAssoJob.getClientId() + ", OpenId="
                                + iasAssoJob.getOpenId() + ", iasAutoCreate=" + iasAutoCreate);

                        // Get the NOTI_ID from iasOpenNotiIdMap
                        IasGetNotiIdResult iasGetNotiIdResult = iasOpenNotiIdMap
                                .get(new IasMsgRecipientKey(iasAssoJob.getClientId(), iasAssoJob.getOpenId()));

                        String jobStatus = IntegrationConstants.JOB_STATUS_COMPLETE;

                        // Client ID, Open ID pair is valid
                        if (iasGetNotiIdResult != null && iasGetNotiIdResult.getNotiId() != null
                                && iasGetNotiIdResult.getNotiId().length() > 0) {

                            logger.info(
                                    "[BATCH_JOB]CmcSendIasNotiJob - Mapped notiId=" + iasGetNotiIdResult.getNotiId());

                            IasUser iasUser = iasNotiDAO.getIasEsNotiMap(conn, iasGetNotiIdResult.getNotiId(),
                                    iasAssoJob.getIasOptSpId());

                            // Check if record exist in ias_es_noti_map by noti_id=(noti_id from getNotificationID API) and service_provider_id=ias_opt_sp_id
                            if (iasUser != null) {

                                // Check if it is VIP and asso queue record created by MyGov
                                if (iasAutoCreate && myGovClientId.equals(iasAssoJob.getClientId())) {

                                    /*
                                     * No action on the opt_in flag [Comment out this logic. Decided on 3 Dec 2020]
                                     * IasEsNotiMap_ dbNotiMap = new IasEsNotiMap_(conn, notiId, iasAssoJob.getIasOptSpId());
                                     * if (!IntegrationConstants.OPT_IN_Y.equals(dbNotiMap.getOptIn())) {
                                     * dbNotiMap.setOptIn(IntegrationConstants.OPT_IN_Y);
                                     * dbNotiMap.update(conn);
                                     * }
                                     */

                                    boolean myGovNotiMapInDB = iasNotiDAO.isIasEsNotiMapExist(conn,
                                            iasGetNotiIdResult.getNotiId(), myGovSpId);

                                    // If IasEsNotiMap_ record not exist in DB, insert a new one
                                    if (myGovNotiMapInDB == false) {
                                        // Create IAS_ES_NOTI_MAP
                                        IasEsNotiMap_ notiMap = new IasEsNotiMap_();
                                        notiMap.setServiceProviderId(myGovSpId);
                                        notiMap.setNotiId(iasGetNotiIdResult.getNotiId());
                                        notiMap.setOpenId(iasAssoJob.getOpenId());
                                        notiMap.setOptIn(IntegrationConstants.OPT_IN_U);
                                        notiMap.insert(conn);
                                    }
                                } else {
                                    IasEsNotiMap_ notiMap = new IasEsNotiMap_(conn, iasGetNotiIdResult.getNotiId(),
                                            iasAssoJob.getIasOptSpId());
                                    notiMap.setOpenId(iasAssoJob.getOpenId());
                                    notiMap.update(conn);
                                }

                                // Update the ias_es_noti_map by opt_in='Y' if opt_in='U'
                                if (IntegrationConstants.OPT_IN_U.equals(iasUser.getOptIn()) &&
                                        !IntegrationConstants.GET_NOTI_ID_RESULT_V2_DEREGISTERED
                                                .equals(iasGetNotiIdResult.getStatus())) {
                                    IasEsNotiMap_ notiMap2 = new IasEsNotiMap_(conn, iasGetNotiIdResult.getNotiId(),
                                            iasAssoJob.getIasOptSpId());
                                    notiMap2.setOptIn(IntegrationConstants.OPT_IN_Y);

                                    // create welcome message record for service_provider_id=ias_opt_sp_id
                                    String iasMsgId = IasUtils.getNextIasMsgId(serverId);

                                    IasMessage_ iasMessage = new IasMessage_();
                                    iasMessage.setIasMsgId(iasMsgId);
                                    iasMessage.setPortalId(myGovPortalId);
                                    iasMessage.setTemplateId(welcomeMsgTemplId);
                                    iasMessage.setTemplateVersion(welcomeMsgTemplVer);
                                    iasMessage.setSubjectEn(templateSubjectEn.replace(welcomeMsgTemplSpTag,
                                            "" + iasAssoJob.getServiceProviderNameEn()));
                                    iasMessage.setSubjectTc(templateSubjectTc.replace(welcomeMsgTemplSpTag,
                                            "" + iasAssoJob.getServiceProviderNameTc()));
                                    iasMessage.setSubjectSc(templateSubjectSc.replace(welcomeMsgTemplSpTag,
                                            "" + iasAssoJob.getServiceProviderNameSc()));

                                    iasMessage.setContentEn(EncUtils.encrypt(templateContentEn.replace(
                                            welcomeMsgTemplSpTag, "" + iasAssoJob.getServiceProviderNameEn())));
                                    iasMessage.setContentTc(EncUtils.encrypt(templateContentTc.replace(
                                            welcomeMsgTemplSpTag, "" + iasAssoJob.getServiceProviderNameTc())));
                                    iasMessage.setContentSc(EncUtils.encrypt(templateContentSc.replace(
                                            welcomeMsgTemplSpTag, "" + iasAssoJob.getServiceProviderNameSc())));
                                    iasMessage.setEncInd(CmcAppConstants.ENC_IND_YES);
                                    iasMessage.setEncKeyStoreId(encKeyStoreId);

                                    conn.begin(null, conn.getLastUpTime(), HPFW_Connection.DIRECT);
                                    iasMessage.insert(conn);
                                    conn.begin(null, conn.getLastUpTime(), HPFW_Connection.DIRECT_WITH_HISTORY);

                                    // Create IAS_USER_MESSAGE
                                    IasUserMessage_ iasUserMsg = new IasUserMessage_();
                                    iasUserMsg.setClientId(iasAssoJob.getClientId());
                                    iasUserMsg.setOpenId(iasAssoJob.getOpenId());
                                    iasUserMsg.setNotiId(iasGetNotiIdResult.getNotiId());
                                    iasUserMsg.setIasMsgId(iasMsgId);
                                    iasUserMsg.setTranId(iasAssoJob.getJobId());
                                    iasUserMsg.setReadInd(StatusConstants.READ_IND_UNREAD);
                                    iasUserMsg.setDeleteInd(StatusConstants.DELETE_IND_NOT_DELETED);
                                    iasUserMsg.setIasNotiStatus(IntegrationConstants.IAS_NOTI_STATUS_NEW);

                                    iasUserMsg.insert(conn);
                                    notiMap2.update(conn);
                                }

                            } else { // Map record not exist in DB

                                // Check if it is VIP and asso queue record created by MyGov
                                if (iasAutoCreate && myGovClientId.equals(iasAssoJob.getClientId())) {
                                    // - check if record exist in ias_es_noti_map by noti_id=(noti_id from getNotification API) and service_provider_id='MARS'
                                    boolean myGovNotiMapInDB = iasNotiDAO.isIasEsNotiMapExist(conn,
                                            iasGetNotiIdResult.getNotiId(), myGovSpId);

                                    // If IasEsNotiMap_ record not exist in DB, insert a new one
                                    if (myGovNotiMapInDB == false) {
                                        // Create IAS_ES_NOTI_MAP
                                        IasEsNotiMap_ notiMap = new IasEsNotiMap_();
                                        notiMap.setServiceProviderId(myGovSpId);
                                        notiMap.setNotiId(iasGetNotiIdResult.getNotiId());
                                        notiMap.setOpenId(iasAssoJob.getOpenId());
                                        notiMap.setOptIn(IntegrationConstants.OPT_IN_U);
                                        notiMap.insert(conn);
                                    }
                                    // - insert new ias_es_noti_map record of service_provider_id=ias_opt_sp_id, noti_id=(noti_id from getNotification API), open_id=null and opt_in='Y'
                                    IasEsNotiMap_ notiMap = new IasEsNotiMap_();
                                    notiMap.setServiceProviderId(iasAssoJob.getIasOptSpId());
                                    notiMap.setNotiId(iasGetNotiIdResult.getNotiId());
                                    if (IntegrationConstants.GET_NOTI_ID_RESULT_V2_DEREGISTERED
                                            .equals(iasGetNotiIdResult.getStatus()))
                                        notiMap.setOptIn(IntegrationConstants.OPT_IN_U);
                                    else
                                        notiMap.setOptIn(IntegrationConstants.OPT_IN_Y);
                                    notiMap.insert(conn);
                                } else {

                                    // - insert new ias_es_noti_map record of service_provider_id=ias_opt_sp_id, noti_id=(noti_id from getNotification API), open_id=(open_id from IAS_ASSO_QUEUE) and opt_in='Y'
                                    IasEsNotiMap_ notiMap = new IasEsNotiMap_();
                                    notiMap.setServiceProviderId(iasAssoJob.getIasOptSpId());
                                    notiMap.setNotiId(iasGetNotiIdResult.getNotiId());
                                    notiMap.setOpenId(iasAssoJob.getOpenId());
                                    if (IntegrationConstants.GET_NOTI_ID_RESULT_V2_DEREGISTERED
                                            .equals(iasGetNotiIdResult.getStatus()))
                                        notiMap.setOptIn(IntegrationConstants.OPT_IN_U);
                                    else
                                        notiMap.setOptIn(IntegrationConstants.OPT_IN_Y);
                                    notiMap.insert(conn);
                                }

                                if (!IntegrationConstants.GET_NOTI_ID_RESULT_V2_DEREGISTERED
                                        .equals(iasGetNotiIdResult.getStatus())) {
                                    // create welcome message record for service_provider_id=ias_opt_sp_id

                                    String iasMsgId = IasUtils.getNextIasMsgId(serverId);

                                    IasMessage_ iasMessage = new IasMessage_();
                                    iasMessage.setIasMsgId(iasMsgId);
                                    iasMessage.setPortalId(myGovPortalId);
                                    iasMessage.setTemplateId(welcomeMsgTemplId);
                                    iasMessage.setTemplateVersion(welcomeMsgTemplVer);
                                    iasMessage.setSubjectEn(templateSubjectEn.replace(welcomeMsgTemplSpTag,
                                            "" + iasAssoJob.getServiceProviderNameEn()));
                                    iasMessage.setSubjectTc(templateSubjectTc.replace(welcomeMsgTemplSpTag,
                                            "" + iasAssoJob.getServiceProviderNameTc()));
                                    iasMessage.setSubjectSc(templateSubjectSc.replace(welcomeMsgTemplSpTag,
                                            "" + iasAssoJob.getServiceProviderNameSc()));

                                    iasMessage.setContentEn(EncUtils.encrypt(templateContentEn.replace(
                                            welcomeMsgTemplSpTag, "" + iasAssoJob.getServiceProviderNameEn())));
                                    iasMessage.setContentTc(EncUtils.encrypt(templateContentTc.replace(
                                            welcomeMsgTemplSpTag, "" + iasAssoJob.getServiceProviderNameTc())));
                                    iasMessage.setContentSc(EncUtils.encrypt(templateContentSc.replace(
                                            welcomeMsgTemplSpTag, "" + iasAssoJob.getServiceProviderNameSc())));
                                    iasMessage.setEncInd(CmcAppConstants.ENC_IND_YES);
                                    iasMessage.setEncKeyStoreId(encKeyStoreId);

                                    conn.begin(null, conn.getLastUpTime(), HPFW_Connection.DIRECT);
                                    iasMessage.insert(conn);
                                    conn.begin(null, conn.getLastUpTime(), HPFW_Connection.DIRECT_WITH_HISTORY);

                                    // Create IAS_USER_MESSAGE
                                    IasUserMessage_ iasUserMsg = new IasUserMessage_();
                                    iasUserMsg.setClientId(iasAssoJob.getClientId());
                                    iasUserMsg.setOpenId(iasAssoJob.getOpenId());
                                    iasUserMsg.setNotiId(iasGetNotiIdResult.getNotiId());
                                    iasUserMsg.setIasMsgId(iasMsgId);
                                    iasUserMsg.setTranId(iasAssoJob.getJobId());
                                    iasUserMsg.setReadInd(StatusConstants.READ_IND_UNREAD);
                                    iasUserMsg.setDeleteInd(StatusConstants.DELETE_IND_NOT_DELETED);
                                    iasUserMsg.setIasNotiStatus(IntegrationConstants.IAS_NOTI_STATUS_NEW);

                                    iasUserMsg.insert(conn);
                                }
                            }
                        } else {

                            if (iasInvalidOpenIdList.contains(
                                    new IasMsgRecipientKey(iasAssoJob.getClientId(), iasAssoJob.getOpenId()))) {
                                logger.warn("[BATCH_JOB]CmcSendIasNotiJob - iasAssoJob.getClientId()="
                                        + iasAssoJob.getClientId() + ", iasAssoJob.getOpenId()="
                                        + iasAssoJob.getOpenId() + " is not valid.");
                                jobStatus = IntegrationConstants.JOB_STATUS_FAIL;
                            } else {
                                logger.warn("[BATCH_JOB]CmcSendIasNotiJob - iasAssoJob.getClientId()="
                                        + iasAssoJob.getClientId() + ", iasAssoJob.getOpenId()="
                                        + iasAssoJob.getOpenId()
                                        + " Invalid status code returned. Will retry this Asso Job.");
                                jobStatus = IntegrationConstants.JOB_STATUS_NEW;
                            }
                        }
                        // Mark complete for the ias_asso_queue record
                        IasAssoQueue_ iasAssoQueue = new IasAssoQueue_(conn, iasAssoJob.getJobId());
                        iasAssoQueue.setJobStatus(jobStatus);

                        conn.begin(null, conn.getLastUpTime(), HPFW_Connection.DIRECT);
                        iasAssoQueue.update(conn);
                        conn.begin(null, conn.getLastUpTime(), HPFW_Connection.DIRECT_WITH_HISTORY);

                        conn.commit();

                    }
                }

                logger.info("[BATCH_JOB]CmcSendIasNotiJob - Process ias_asso_queue record - End");

                logger.info("[BATCH_JOB]CmcSendIasNotiJob - Send Noti - Start");

                // Retrieve outstanding IAS_USER_MESSAGE to be sent notification
                List<IasUserMsg> iasUserMsgList = iasNotiDAO.getOutstandingMessage(conn, iasMsgProcessBatchLimit);
                logger.info("[BATCH_JOB]CmcSendIasNotiJob - iasUserMsgList.size()=" + iasUserMsgList.size());

                NotificationBean notiBean = new NotificationBean();
                // A Map<String, List<String>> to store the message id and noti id list
                ArrayList<String> notiIdList = new ArrayList<>();

                String nextIasMsgId = "";
                int notiIdCount = 0;

                ArrayList<NotificationItem> notiItemList = new ArrayList<NotificationItem>();

                ArrayList<String> mapSpNotiIdList = new ArrayList<String>();

                // Loop the List
                for (int i = 0; i < iasUserMsgList.size(); i++) {

                    IasUserMsg iasUserMsg = iasUserMsgList.get(i);

                    // Check iAM Smart account status, If de-registered, mark delete_ind to Y and not sending this by push noti
                    if (IntegrationConstants.IAS_USER_STATUS_DEREGISTERED.equals(iasUserMsg.getStatus()) ||
                            iasDeregOpenIdList.contains(
                                    new IasMsgRecipientKey(iasUserMsg.getClientId(), iasUserMsg.getOpenId()))) {
                        // Update DELETE_IND to Y for IAS_USER_MESSAGE
                        ArrayList<Parameter> paraSetList = new ArrayList<Parameter>();
                        ArrayList<Parameter> paraWhereList = new ArrayList<Parameter>();
                        paraSetList.add(new Parameter(Parameter.String, StatusConstants.DELETE_IND_DELETED));
                        paraWhereList.add(new Parameter(Parameter.String, iasUserMsg.getNotiId()));
                        paraWhereList.add(new Parameter(Parameter.String, iasUserMsg.getIasMsgId()));

                        IasUserMessage_.update(conn, "set DELETE_IND = ?", paraSetList,
                                "where NOTI_ID = ? and IAS_MSG_ID = ? ",
                                paraWhereList);

                        // For iAM Smart Undelivered Report 06D
                        // Check if it is VIP
                        boolean iasAutoCreate = "Y".equals(iasUserMsg.getIasAutoCreateInd());
                        String rcptType = IntegrationConstants.IAS_MSG_RCPT_TYPE_IAM_SMART;
                        if ((iasUserMsg.getClientId().equals(myGovClientId)) && iasAutoCreate) {
                            rcptType = IntegrationConstants.IAS_MSG_RCPT_TYPE_MY_ID;
                        }
                        iasNotiDAO.createIasUndeliveredMessage(conn, iasUserMsg.getClientId(), iasUserMsg.getOpenId(),
                                iasUserMsg.getIasMsgId(), IntegrationConstants.IAS_UNDELIVER_MSG_REASON_DEREGISTERED,
                                rcptType);

                        conn.commit();

                    } else {
                        notiIdList.add(iasUserMsg.getNotiId());
                        notiIdCount++;
                    }

                    // If Noti ID count reach API batch size limit or iasUserMsgList reach the last object or next iasMsgId is new
                    logger.debug("[BATCH_JOB]CmcSendIasNotiJob - new NotificationItem notiIdList.size: "
                            + notiIdList.size() + ", iasMsgId: " + iasUserMsg.getIasMsgId());
                    if (notiIdList.size() > 0 && ((notiIdCount == iasSendNotiBatchLimit) ||
                            (i == iasUserMsgList.size() - 1) ||
                            (!iasUserMsg.getIasMsgId().equals(iasUserMsgList.get(i + 1).getIasMsgId())))) {
                        // For TC Subject
                        if (iasUserMsg.getSubjectTc() == null || iasUserMsg.getSubjectTc().length() == 0) {
                            iasUserMsg.setSubjectTc(iasUserMsg.getSubjectEn());
                        }

                        // For SC Subject
                        if (iasUserMsg.getSubjectSc() == null || iasUserMsg.getSubjectSc().length() == 0) {
                            if (iasUserMsg.getSubjectTc() != null && iasUserMsg.getSubjectTc().length() > 0) {
                                iasUserMsg.setSubjectSc(iasUserMsg.getSubjectTc());
                            } else {
                                iasUserMsg.setSubjectSc(iasUserMsg.getSubjectEn());
                            }
                        }

                        NotificationItem notiItem = new NotificationItem(new ArrayList<String>(notiIdList),
                                iasUserMsg.getIasMsgId(),
                                iasUserMsg.getServiceProviderId(),
                                iasUserMsg.getSubjectEn(),
                                iasUserMsg.getSubjectTc(),
                                iasUserMsg.getSubjectSc());

                        notiItemList.add(notiItem);
                        notiIdList = new ArrayList<String>();
                    }

                    // If Noti ID count reach API batch size limit or iasUserMsgList reach the last object
                    logger.debug("[BATCH_JOB]CmcSendIasNotiJob - notiBean.setNotifications notiItemList.size: "
                            + notiItemList.size());
                    if (notiItemList.size() > 0
                            && ((notiIdCount == iasSendNotiBatchLimit) || (i == (iasUserMsgList.size() - 1)))) {
                        notiBean.setNotifications(new ArrayList<NotificationItem>(notiItemList));

                        if (!eIDUtilsInit) {
                            EIDUtils.initialize(prop);
                            eIDUtilsInit = true;
                        }

                        // Send iAM Smart Notification message
                        EIDResponseBean sendNotiResp = EIDUtils.doRequestSendNotificationMessages(iasSendNotiEndPoint,
                                notiBean);
                        java.sql.Timestamp sentDt = new java.sql.Timestamp(new Date().getTime());

                        if (sendNotiResp != null) {

                            logger.info("[BATCH_JOB]CmcSendIasNotiJob - EIDUtils.doRequestSendNotificationMessages - TxID="
                                    + sendNotiResp.getTxID() + ", Code="
                                    + sendNotiResp.getCode() + ", Message=" + sendNotiResp.getMessage());

                            logger.debug("[BATCH_JOB]CmcSendIasNotiJob - EIDUtils.doRequestSendNotificationMessages - Content="
                                    + sendNotiResp.getContent());

                            if (((IntegrationConstants.GET_NOTI_ID_RESULT_CODE_SUCCESS
                                    .equalsIgnoreCase(sendNotiResp.getCode()))
                                    || (IntegrationConstants.GET_NOTI_ID_RESULT_CODE_PARTIAL_SUCCESS
                                            .equalsIgnoreCase(sendNotiResp.getCode())))
                                    && sendNotiResp.getContent() != null && sendNotiResp.getContent().length() > 0) {
                                NotificationResultsBean notiResult = new NotificationResultsBean(
                                        sendNotiResp.getContent());
                                ArrayList<NotificationResultItem> notiResultList = notiResult.getNotificationResults();

                                // Loop the result
                                for (NotificationResultItem notiResultItem : notiResultList) {

                                    // Update TX_ID, IAS_NOTI_STATUS of IAS_USER_MESSAGE
                                    ArrayList<Parameter> paraSetList = new ArrayList<Parameter>();
                                    ArrayList<Parameter> paraWhereList = new ArrayList<Parameter>();
                                    paraSetList.add(
                                            new Parameter(Parameter.String, IntegrationConstants.IAS_NOTI_STATUS_SENT));
                                    paraSetList.add(new Parameter(Parameter.String, notiResultItem.getStatus()));
                                    paraSetList.add(new Parameter(Parameter.String, sendNotiResp.getTxID()));
                                    paraSetList.add(new Parameter(Parameter.Timestamp, sentDt));
                                    paraWhereList
                                            .add(new Parameter(Parameter.String, notiResultItem.getNotificationID()));
                                    paraWhereList.add(new Parameter(Parameter.String, notiResultItem.getMessageID()));

                                    IasUserMessage_.update(conn,
                                            "set IAS_NOTI_STATUS = ?, IAS_NOTI_RESULT = ?, TX_ID = ?, SENT_DT = ?",
                                            paraSetList, "where NOTI_ID = ? and IAS_MSG_ID = ? ", paraWhereList);

                                    if (!(IntegrationConstants.SEND_NOTI_ID_RESULT_READY_TO_SEND
                                            .equals(notiResultItem.getStatus())) &&
                                            !(IntegrationConstants.SEND_NOTI_ID_RESULT_SENT
                                                    .equals(notiResultItem.getStatus()))) {

                                        for (int j = 0; j < iasUserMsgList.size(); j++) {
                                            IasUserMsg ium = iasUserMsgList.get(j);
                                            if ((ium.getNotiId().equals(notiResultItem.getNotificationID()))
                                                    && (ium.getIasMsgId().equals(notiResultItem.getMessageID()))) {
                                                // For iAM Smart Undelivered Report 06D
                                                // Check if it is VIP
                                                boolean iasAutoCreate = "Y".equals(ium.getIasAutoCreateInd());
                                                String rcptType = IntegrationConstants.IAS_MSG_RCPT_TYPE_IAM_SMART;
                                                if ((ium.getClientId().equals(myGovClientId)) && iasAutoCreate) {
                                                    rcptType = IntegrationConstants.IAS_MSG_RCPT_TYPE_MY_ID;
                                                }

                                                String undeliverReason = IntegrationConstants.IAS_UNDELIVER_MSG_REASON_GENERAL_ERROR;
                                                if (IntegrationConstants.SEND_NOTI_ID_RESULT_INVALID
                                                        .equals(notiResultItem.getStatus())) {
                                                    undeliverReason = IntegrationConstants.IAS_UNDELIVER_MSG_REASON_INVALID_NOTI_ID;
                                                }
                                                if (IntegrationConstants.SEND_NOTI_ID_RESULT_NOT_ACTIVE
                                                        .equals(notiResultItem.getStatus())) {
                                                    undeliverReason = IntegrationConstants.IAS_UNDELIVER_MSG_REASON_ACC_NOT_ACTIVE;
                                                } else if (IntegrationConstants.SEND_NOTI_ID_RESULT_OTHER_ERROR
                                                        .equals(notiResultItem.getStatus())) {
                                                    undeliverReason = IntegrationConstants.IAS_UNDELIVER_MSG_REASON_OTHER_EROR;
                                                } else if (IntegrationConstants.SEND_NOTI_ID_INVALID_CONSENT
                                                        .equals(notiResultItem.getStatus())) {
                                                    undeliverReason = IntegrationConstants.IAS_UNDELIVER_MSG_REASON_INVALID_CONSENT;
                                                }

                                                iasNotiDAO.createIasUndeliveredMessage(conn, ium.getClientId(),
                                                        ium.getOpenId(), ium.getIasMsgId(), undeliverReason, rcptType);
                                            }
                                        }
                                    }
                                }
                                conn.commit();

                                notiIdCount = 0;
                                notiItemList = new ArrayList<NotificationItem>();

                            } else {
                                logger.warn(
                                        "[BATCH_JOB]CmcSendIasNotiJob - EIDUtils.doRequestSendNotificationMessages failed - Code="
                                                + sendNotiResp.getCode() +
                                                ", Message=" + sendNotiResp.getMessage() + ", TxID="
                                                + sendNotiResp.getTxID() + ". No DB Change, will retry in next run.");
                                logger.warn(
                                        "[BATCH_JOB]CmcSendIasNotiJob - EIDUtils.doRequestSendNotificationMessages failed - notiBean: "
                                                + IasUtils.getNotificationBeanStr(notiBean));
                            }
                        } else {
                            logger.warn(
                                    "[BATCH_JOB]CmcSendIasNotiJob - EIDUtils.doRequestSendNotificationMessages failed - sendNotiResp is null. No DB Change, will retry in next run.");
                        }
                    }
                }

                logger.info("[BATCH_JOB]CmcSendIasNotiJob - Send Noti - End");

                // release lock for concurrent control
                jobControlUtils.releaseJobControl(jobControlName);

            } else {
                logger.info("[BATCH_JOB]CmcSendIasNotiJob - CMC_MARS_SYN_JOB_LOCK table cannot be locked.");
            }
        } catch (Exception e) {
            logger.error("[BATCH_JOB]CmcSendIasNotiJob - exception", e);
            throw e;
        } finally {
            if (conn != null) {
                HPFW_Connection.close(conn);
            }
            jobControlUtils.releaseJobControl(jobControlName);
        }

        logger.info("[BATCH_JOB]CmcSendIasNotiJob - END");
    }

}
