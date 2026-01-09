package hk.gov.cmc.processor.maintainmessage.todoitem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import hk.gov.cmc.common.CmcAppPropertyNames;
import hk.gov.cmc.common.IasToDoItemConstant;
import hk.gov.cmc.common.IntegrationConstants;
import hk.gov.cmc.config.CmcEnvProperties;
import hk.gov.cmc.dao.maintainmessage.notification.IasNotiDAO;
import hk.gov.cmc.dao.maintainmessage.notification.IasToDoItemNotiDAO;
import hk.gov.cmc.eid.bean.EIDResponseBean;
import hk.gov.cmc.eid.bean.EServiceOpenIdItem;
import hk.gov.cmc.eid.bean.EServiceOpenIdsBean;
import hk.gov.cmc.eid.bean.NotificationIdItem;
import hk.gov.cmc.eid.bean.NotificationIdsBean;
import hk.gov.cmc.eid.bean.switchNotificationID.request.EServiceHkidItem;
import hk.gov.cmc.eid.bean.switchNotificationID.request.EServiceHkidsBean;
import hk.gov.cmc.eid.bean.switchNotificationID.response.NotificationIdByHKIDItem;
import hk.gov.cmc.eid.bean.switchNotificationID.response.NotificationIdsByHKIDsBean;
import hk.gov.cmc.eid.client.EIDUtils;
import hk.gov.cmc.model.maintainmessage.key.IasClientIdNotiIdPair;
import hk.gov.cmc.model.maintainmessage.key.IasMsgRecipientKey;
import hk.gov.cmc.model.maintainmessage.todoitem.IasUserToDoItem;
import hk.gov.cmc.model.maintainmessage.todoitem.IasUserToDoItemWrapped;
import hk.gov.cmc.model.maintainmessage.user.IasUser;
import hk.gov.cmc.persistence.connection.hpfw.HPFW_Connection;
import hk.gov.cmc.persistence.ias.notification.IasEsNotiMap_;
import hk.gov.cmc.persistence.ias.todoitem.IasUserToDoItem_;
import hk.gov.cmc.utils.common.EncUtils;

public class IasToDoItemSwitchNotiIdProcessor {

    private static Log logger = LogFactory.getLog(IasToDoItemSwitchNotiIdProcessor.class);
    private final static CmcEnvProperties cmcEnvProperties = new CmcEnvProperties();

    public IasToDoItemSwitchNotiIdProcessor() {
    }

    public List<IasClientIdNotiIdPair> updateIasUserToDoItemNotiIDByESHKID(HPFW_Connection conn,
            boolean isEIDUtilsInit, List<IasUserToDoItem> notiIdMissListWithOutHKID) throws Exception {

        logger.info("updateIasUserToDoItemNotiIDByESHKID - START");

        List<IasClientIdNotiIdPair> deRegClientIdNotiIdPairList = new ArrayList<IasClientIdNotiIdPair>();

        Properties prop = cmcEnvProperties.getProperties();
        int iasGetNotiIdBatchLimit = Integer
                .parseInt(prop.getProperty(CmcAppPropertyNames.IAS_GET_NOTI_ID_BATCH_LIMIT_PROPERTY_NAME));
        String iasSwitchNotiIdEndPoint = prop.getProperty(CmcAppPropertyNames.IAS_SWITCH_NOTI_ID_BY_HKID_END_POINT);

        IasNotiDAO iasNotiDAO = new IasNotiDAO();
        IasToDoItemNotiDAO iasToDoItemNotiDAO = new IasToDoItemNotiDAO();

        // Create HashMap iasHKIdNotiIdMap
        HashMap<IasMsgRecipientKey, NotificationIdByHKIDItem> iasHKIdNotiIdMap = new HashMap<IasMsgRecipientKey, NotificationIdByHKIDItem>();

        // Create List for invalid clientID + HKID
        List<IasMsgRecipientKey> iasInvalidEsHKIdList = new ArrayList<IasMsgRecipientKey>();

        // Create List for clientID + HKID pair to check duplicate in getNotiId request
        List<IasMsgRecipientKey> getNotiIdReqEsHKIdList = new ArrayList<IasMsgRecipientKey>();

        // Decrypt HKID
        List<IasUserToDoItemWrapped> notiIdMissList = new ArrayList<IasUserToDoItemWrapped>();
        for (IasUserToDoItem iasUserToDoItem : notiIdMissListWithOutHKID) {
            try {
                IasUserToDoItemWrapped iasUserToDoItemWrapped = new IasUserToDoItemWrapped();
                iasUserToDoItemWrapped.setIasUserToDoItem(iasUserToDoItem);
                String hkid = EncUtils.decrypt(
                        iasUserToDoItem.getHkidEncrypted(),
                        prop.getProperty(CmcAppPropertyNames.HKID_ENCRYPT_PASSPHRASE_ID_PROPERTY_NAME),
                        prop.getProperty(CmcAppPropertyNames.HKID_ENCRYPT_PASSPHRASE_USAGE_PROPERTY_NAME));
                logger.info("updateIasUserToDoItemNotiIDByESHKID - iasUserToDoItem.getHkidEncrypted="
                        + iasUserToDoItem.getHkidEncrypted() + ", iasUserToDoItem.getTranId="
                        + iasUserToDoItem.getTranId());
                iasUserToDoItemWrapped.setHkid(hkid);
                notiIdMissList.add(iasUserToDoItemWrapped);
            } catch (Exception e) {
                logger.error(
                        "updateIasUserToDoItemNotiIDByESHKID - Decrypt HKID failed for iasUserToDoItem.getHkidEncrypted="
                                + iasUserToDoItem.getHkidEncrypted() + ", iasUserToDoItem.getTranId="
                                + iasUserToDoItem.getTranId(),
                        e);
            }
        }

        int totalNotiIdCnt = 0;

        // Part A: Retrieve the noti id from iAM Smart system by clientID and HKID
        while (totalNotiIdCnt < notiIdMissList.size()) {

            logger.debug("updateIasUserToDoItemNotiIDByESHKID - totalNotiIdCnt=" + totalNotiIdCnt);

            int batchCnt = 0;

            ArrayList<EServiceHkidItem> eServiceHKIdItemList = new ArrayList<EServiceHkidItem>();
            while ((batchCnt < iasGetNotiIdBatchLimit) && (totalNotiIdCnt < notiIdMissList.size())) {
                IasUserToDoItemWrapped iasUserToDoItemWrapped = (IasUserToDoItemWrapped) notiIdMissList
                        .get(totalNotiIdCnt);
                logger.debug("iasUserToDoItemWrapped=" + iasUserToDoItemWrapped.toString());

                // Check duplicate client_id + hkid in getNotiId request
                IasMsgRecipientKey iasMsgRecipientKey = new IasMsgRecipientKey(
                        iasUserToDoItemWrapped.getIasUserToDoItem().getClientId(), iasUserToDoItemWrapped.getHkid());
                if (!getNotiIdReqEsHKIdList.contains(iasMsgRecipientKey)) {
                    EServiceHkidItem eServiceHKIdItem = new EServiceHkidItem(
                            iasUserToDoItemWrapped.getIasUserToDoItem().getClientId(),
                            iasUserToDoItemWrapped.getHkid());
                    eServiceHKIdItemList.add(eServiceHKIdItem);
                    getNotiIdReqEsHKIdList.add(iasMsgRecipientKey);
                    batchCnt++;
                }

                totalNotiIdCnt++;
            }
            logger.info("updateIasUserToDoItemNotiIDByESHKID - batchCnt=" + batchCnt);

            logger.info(
                    "updateIasUserToDoItemNotiIDByESHKID - eServiceHKIdItemList.size=" + eServiceHKIdItemList.size());

            if (eServiceHKIdItemList.size() > 0) {
                EServiceHkidsBean eServiceHKIdsBean = new EServiceHkidsBean();
                eServiceHKIdsBean.setEServiceHKIDs(eServiceHKIdItemList);

                // Call switchNotificationID API to get missing NotiID from iAM Smart system by ClientID and HKID
                if (!isEIDUtilsInit) {
                    EIDUtils.initialize(prop);
                }

                EIDResponseBean getNotiIdResponse = EIDUtils
                        .doRequestSwitchNotificationIDsByHKIDs(iasSwitchNotiIdEndPoint, eServiceHKIdsBean);

                if (getNotiIdResponse != null) {

                    logger.debug("updateIasUserToDoItemNotiIDByESHKID - EIDResponseBean.getContent="
                            + getNotiIdResponse.getContent());

                    if (((IntegrationConstants.GET_NOTI_ID_RESULT_CODE_SUCCESS
                            .equalsIgnoreCase(getNotiIdResponse.getCode())) ||
                            (IntegrationConstants.GET_NOTI_ID_RESULT_CODE_PARTIAL_SUCCESS
                                    .equalsIgnoreCase(getNotiIdResponse.getCode())))
                            && getNotiIdResponse.getContent() != null
                            && getNotiIdResponse.getContent().length() > 0) {

                        NotificationIdsByHKIDsBean notiIdsResult = new NotificationIdsByHKIDsBean(
                                getNotiIdResponse.getContent());
                        ArrayList<NotificationIdByHKIDItem> notiIdResultList = notiIdsResult.getNotificationIDs();

                        // Loop the switchNotificationID result
                        for (NotificationIdByHKIDItem notiIdItem : notiIdResultList) {
                            IasMsgRecipientKey iasMsgRecipientKey = new IasMsgRecipientKey(notiIdItem.getClientID(),
                                    notiIdItem.getHKID());
                            IasClientIdNotiIdPair iasClientIdNotiIdPair = new IasClientIdNotiIdPair(
                                    notiIdItem.getClientID(), notiIdItem.getNotificationID());
                            if ((IntegrationConstants.GET_NOTI_ID_RESULT_VALID.equals(notiIdItem.getStatus())) ||
                                    (IntegrationConstants.GET_NOTI_ID_RESULT_V2_ACTIVE.equals(notiIdItem.getStatus()))
                                    ||
                                    (IntegrationConstants.GET_NOTI_ID_RESULT_V2_SUSPEND.equals(notiIdItem.getStatus()))
                                    ||
                                    (IntegrationConstants.GET_NOTI_ID_RESULT_V2_INVALID.equals(notiIdItem.getStatus()))
                                    ||
                                    (IntegrationConstants.GET_NOTI_ID_RESULT_V2_DEREGISTERED
                                            .equals(notiIdItem.getStatus()))) {
                                iasHKIdNotiIdMap.put(iasMsgRecipientKey, notiIdItem);

                                if (IntegrationConstants.GET_NOTI_ID_RESULT_V2_DEREGISTERED
                                        .equals(notiIdItem.getStatus())) {
                                    if (!deRegClientIdNotiIdPairList.contains(iasClientIdNotiIdPair)) {
                                        deRegClientIdNotiIdPairList.add(iasClientIdNotiIdPair);
                                    }
                                }
                            } else {
                                if (!iasInvalidEsHKIdList.contains(iasMsgRecipientKey)) {
                                    iasInvalidEsHKIdList.add(iasMsgRecipientKey);
                                }
                            }
                        }
                    } else {
                        logger.warn("updateIasUserToDoItemNotiIDByESHKID - EIDUtils.doRequestGetNotificationIDs Fail. "
                                + "EIDResponseBean.getCode=" + getNotiIdResponse.getCode()
                                + ", EIDResponseBean.getMessage=" + getNotiIdResponse.getMessage()
                                + ", EIDResponseBean.getTxID=" + getNotiIdResponse.getTxID()
                                + ". No DB Change, will retry again in next run.");
                    }
                } else {
                    logger.warn("updateIasUserToDoItemNotiIDByESHKID - EIDUtils.doRequestGetNotificationIDs Fail. "
                            + "EIDResponseBean is null. No DB Change, will retry again in next run.");
                }
            }
        }

        logger.info("updateIasUserToDoItemNotiIDByESHKID - iasHKIdNotiIdMap.size=" + iasHKIdNotiIdMap.size());
        for (IasMsgRecipientKey iasMsgRecipientKey : iasHKIdNotiIdMap.keySet()) {
            logger.debug("updateIasUserToDoItemNotiIDByESHKID - iasHKIdNotiIdMap ClientID="
                    + iasMsgRecipientKey.getClientId() + ", HKID=" + iasMsgRecipientKey.getOpenOrHkId()
                    + ", NotificationID=" + iasHKIdNotiIdMap.get(iasMsgRecipientKey).getNotificationID() + ", Status="
                    + iasHKIdNotiIdMap.get(iasMsgRecipientKey).getStatus());
        }

        logger.info("updateIasUserToDoItemNotiIDByESHKID - iasInvalidEsHKIdList.size=" + iasInvalidEsHKIdList.size());
        for (IasMsgRecipientKey iasMsgRecipientKey : iasInvalidEsHKIdList) {
            logger.debug("updateIasUserToDoItemNotiIDByESHKID - iasInvalidEsHKIdList ClientID="
                    + iasMsgRecipientKey.getClientId() + ", HKID=" + iasMsgRecipientKey.getOpenOrHkId());
        }

        // Part A: Loop the notiIdMissList, insert ias_es_noti_map and update ias_user_to_do_item
        // CMC-2025-031: Remove duplicated HKID before inserting into IAS_ES_NOTI_MAP table - BEGIN
        List<String> insertedHkidList = new ArrayList<String>();
        // CMC-2025-031: Remove duplicated HKID before inserting into IAS_ES_NOTI_MAP table - END
        for (IasUserToDoItemWrapped iasUserToDoItemWrapped : notiIdMissList) {

            logger.debug("updateIasUserToDoItemNotiIDByESHKID - ClientId="
                    + iasUserToDoItemWrapped.getIasUserToDoItem().getClientId()
                    + ", RecipientId=" + iasUserToDoItemWrapped.getIasUserToDoItem().getRecipientId()
                    + ", RecipientIdType=" + iasUserToDoItemWrapped.getIasUserToDoItem().getRecipientIdType()
                    + ", IasToDoItemId=" + iasUserToDoItemWrapped.getIasUserToDoItem().getIasToDoItemId()
                    + ", ServiceProviderId=" + iasUserToDoItemWrapped.getIasUserToDoItem().getServiceProviderId());

            // Get the NOTI_ID from iasHKIdNotiIdMap
            NotificationIdByHKIDItem notiIdByHKIDItem = iasHKIdNotiIdMap.get(new IasMsgRecipientKey(
                    iasUserToDoItemWrapped.getIasUserToDoItem().getClientId(), iasUserToDoItemWrapped.getHkid()));
            logger.info("updateIasUserToDoItemNotiIDByESHKID - notiIdByHKIDItem=" + notiIdByHKIDItem);

            // Client ID, HKID pair is valid
            if (notiIdByHKIDItem != null && notiIdByHKIDItem.getNotificationID() != null
                    && notiIdByHKIDItem.getNotificationID().length() > 0) {

                logger.info("updateIasUserToDoItemNotiIDByESHKID - RecipientId="
                        + iasUserToDoItemWrapped.getIasUserToDoItem().getRecipientId()
                        + ", notiIdByHKIDItem.getNotificationID=" + notiIdByHKIDItem.getNotificationID()
                        + ", notiIdByHKIDItem.getStatus=" + notiIdByHKIDItem.getStatus());

                IasUser iasUser = iasNotiDAO.getIasEsNotiMap(conn, notiIdByHKIDItem.getNotificationID(),
                        iasUserToDoItemWrapped.getIasUserToDoItem().getServiceProviderId());

                // If IasEsNotiMap_ record not exist in DB, insert a new one
                if (iasUser == null) {
                    // Create IAS_ES_NOTI_MAP
                    IasEsNotiMap_ notiMap = new IasEsNotiMap_();
                    notiMap.setServiceProviderId(iasUserToDoItemWrapped.getIasUserToDoItem().getServiceProviderId());
                    notiMap.setNotiId(notiIdByHKIDItem.getNotificationID());
                    notiMap.setHkidHashed(iasUserToDoItemWrapped.getIasUserToDoItem().getRecipientId());
                    notiMap.setHkidEncrypted(iasUserToDoItemWrapped.getIasUserToDoItem().getHkidEncrypted());
                    if (IntegrationConstants.GET_NOTI_ID_RESULT_V2_DEREGISTERED.equals(notiIdByHKIDItem.getStatus())) {
                        notiMap.setOptIn(IntegrationConstants.OPT_IN_U);
                    } else {
                        notiMap.setOptIn(IntegrationConstants.OPT_IN_Y);
                    }
                    notiMap.insert(conn);
                    // CMC-2025-031: Remove duplicated HKID before inserting into IAS_ES_NOTI_MAP table - BEGIN
                    insertedHkidList.add(iasUserToDoItemWrapped.getHkid());
                    // CMC-2025-031: Remove duplicated HKID before inserting into IAS_ES_NOTI_MAP table - END
                }
                // CMC-2025-021: Use symmetric encryption to encrypt HKID in iAM Smart To-Do Item and Application - BEGIN
                // else if (iasUser != null && iasUser.getHkidHashed() == null) {
                else {
                    if (iasUser.getHkidHashed() == null) {
                        // CMC-2025-021: Use symmetric encryption to encrypt HKID in iAM Smart To-Do Item and Application - END
                        // Update HKID in IAS_ES_NOTI_MAP
                        IasEsNotiMap_ notiMap = new IasEsNotiMap_(conn, notiIdByHKIDItem.getNotificationID(),
                                iasUserToDoItemWrapped.getIasUserToDoItem().getServiceProviderId());
                        notiMap.setHkidHashed(iasUserToDoItemWrapped.getIasUserToDoItem().getRecipientId());
                        notiMap.setHkidEncrypted(iasUserToDoItemWrapped.getIasUserToDoItem().getHkidEncrypted());
                        if (IntegrationConstants.GET_NOTI_ID_RESULT_V2_DEREGISTERED
                                .equals(notiIdByHKIDItem.getStatus())) {
                            notiMap.setOptIn(IntegrationConstants.OPT_IN_U);
                        } else {
                            notiMap.setOptIn(IntegrationConstants.OPT_IN_Y);
                        }
                        notiMap.update(conn);
                        // CMC-2025-031: Remove duplicated HKID before inserting into IAS_ES_NOTI_MAP table - BEGIN
                        insertedHkidList.add(iasUserToDoItemWrapped.getHkid());
                        // CMC-2025-031: Remove duplicated HKID before inserting into IAS_ES_NOTI_MAP table - END
                    } else {
                        // CMC-2025-031: Remove duplicated HKID before inserting into IAS_ES_NOTI_MAP table - BEGIN
                        if (!insertedHkidList.contains(iasUserToDoItemWrapped.getHkid())) {
                            // CMC-2025-021: Use symmetric encryption to encrypt HKID in iAM Smart To-Do Item and Application - BEGIN
                            logger.warn(
                                    "updateIasUserToDoItemNotiIDByESHKID - One notificationId has more than one decrypted HKID, please check and contact iAM Smart Team if needed. "
                                            + "iasUser.getHkidHashed() is not null for ClientId="
                                            + iasUserToDoItemWrapped.getIasUserToDoItem().getClientId()
                                            + ", RecipientId="
                                            + iasUserToDoItemWrapped.getIasUserToDoItem().getRecipientId()
                                            + ", IasToDoItemId="
                                            + iasUserToDoItemWrapped.getIasUserToDoItem().getIasToDoItemId()
                                            + ". No DB Change.");
                            // CMC-2025-021: Use symmetric encryption to encrypt HKID in iAM Smart To-Do Item and Application - END
                        } else {
                            logger.info(
                                    "updateIasUserToDoItemNotiIDByESHKID - notificationId inserted into IAS_ES_NOTI_MAP table in the same batch before. "
                                            + "iasUser.getHkidHashed() is not null for ClientId="
                                            + iasUserToDoItemWrapped.getIasUserToDoItem().getClientId()
                                            + ", RecipientId="
                                            + iasUserToDoItemWrapped.getIasUserToDoItem().getRecipientId()
                                            + ", IasToDoItemId="
                                            + iasUserToDoItemWrapped.getIasUserToDoItem().getIasToDoItemId()
                                            + ". No DB Change.");
                        }
                        // CMC-2025-031: Remove duplicated HKID before inserting into IAS_ES_NOTI_MAP table - END
                    }
                }

                // Update NOTI_ID for IAS_USER_TO_DO_ITEM
                IasUserToDoItem_ tempIasUserToDoItem = new IasUserToDoItem_(conn,
                        iasUserToDoItemWrapped.getIasUserToDoItem().getClientId(),
                        iasUserToDoItemWrapped.getIasUserToDoItem().getRecipientId(),
                        iasUserToDoItemWrapped.getIasUserToDoItem().getIasToDoItemId());
                tempIasUserToDoItem.setNotiId(notiIdByHKIDItem.getNotificationID());
                tempIasUserToDoItem.update(conn);

            } else { // Check if HKID invalid
                if (iasInvalidEsHKIdList.contains(new IasMsgRecipientKey(
                        iasUserToDoItemWrapped.getIasUserToDoItem().getClientId(), iasUserToDoItemWrapped.getHkid()))) {
                    // Update IAS_NOTI_STATUS for IAS_USER_TO_DO_ITEM
                    IasUserToDoItem_ tempIasUserToDoItem = new IasUserToDoItem_(conn,
                            iasUserToDoItemWrapped.getIasUserToDoItem().getClientId(),
                            iasUserToDoItemWrapped.getIasUserToDoItem().getRecipientId(),
                            iasUserToDoItemWrapped.getIasUserToDoItem().getIasToDoItemId());
                    tempIasUserToDoItem.setIasNotiStatus(IntegrationConstants.IAS_NOTI_STATUS_INVALID);
                    tempIasUserToDoItem.update(conn);

                    // For iAM Smart Undelivered Report 06D
                    logger.info(
                            "updateIasUserToDoItemNotiIDByESHKID - iasToDoItemNotiDAO.createIasUndeliveredMessage (RECIPIENT_ID invalid)");
                    IasUserToDoItem iasUserToDoItem = iasUserToDoItemWrapped.getIasUserToDoItem();
                    iasToDoItemNotiDAO.createIasUndeliveredToDoItemWithHKIdEncrypted(conn,
                            iasUserToDoItem.getClientId(), iasUserToDoItem.getRecipientId(),
                            iasUserToDoItem.getIasToDoItemId(), iasUserToDoItem.getRecipientIdType(),
                            iasUserToDoItem.getHkidEncrypted(),
                            IntegrationConstants.IAS_MSG_RCPT_TYPE_IAM_SMART,
                            IasToDoItemConstant.STATUS_UNDELIVERED_TO_DO_ITEM_NOT_INFORMED,
                            IntegrationConstants.IAS_UNDELIVER_MSG_REASON_ACC_NOT_ACTIVE);
                }
            }
            conn.commit();
        }

        logger.info("updateIasUserToDoItemNotiIDByESHKID - END");

        return deRegClientIdNotiIdPairList;
    }

    public List<IasClientIdNotiIdPair> updateIasUserToDoItemNotiIDByESOpenID(HPFW_Connection conn,
            boolean isEIDUtilsInit, List<IasUserToDoItem> notiIdMissList) throws Exception {

        logger.debug("updateIasUserToDoItemNotiIDByESOpenID - START");

        List<IasClientIdNotiIdPair> deRegClientIdNotiIdPairList = new ArrayList<IasClientIdNotiIdPair>();

        Properties prop = cmcEnvProperties.getProperties();
        int iasGetNotiIdBatchLimit = Integer
                .parseInt(prop.getProperty(CmcAppPropertyNames.IAS_GET_NOTI_ID_BATCH_LIMIT_PROPERTY_NAME));
        String iasGetNotiIdEndPoint = prop.getProperty(CmcAppPropertyNames.IAS_GET_NOTI_ID_END_POINT_PROPERTY_NAME);

        IasNotiDAO iasNotiDAO = new IasNotiDAO();
        IasToDoItemNotiDAO iasToDoItemNotiDAO = new IasToDoItemNotiDAO();

        // Create HashMap iasOpenNotiIdMap
        HashMap<IasMsgRecipientKey, NotificationIdItem> iasOpenIdNotiIdMap = new HashMap<IasMsgRecipientKey, NotificationIdItem>();

        // Create List for invalid clientID + OpenID
        List<IasMsgRecipientKey> iasInvalidEsOpenIdList = new ArrayList<IasMsgRecipientKey>();

        // Create List for clientID + OpenID pair to check duplicate in getNotiId request
        List<IasMsgRecipientKey> getNotiIdReqEsOpenIdList = new ArrayList<IasMsgRecipientKey>();

        int totalNotiIdCnt = 0;
        // Part A: Retrieve the noti id from iAM Smart system by clientID and OpenID
        while (totalNotiIdCnt < notiIdMissList.size()) {

            logger.debug("updateIasUserToDoItemNotiIDByESOpenID - totalNotiIdCnt=" + totalNotiIdCnt);

            int batchCnt = 0;

            ArrayList<EServiceOpenIdItem> eServiceOpenIdItemList = new ArrayList<EServiceOpenIdItem>();
            while ((batchCnt < iasGetNotiIdBatchLimit) && (totalNotiIdCnt < notiIdMissList.size())) {
                IasUserToDoItem iasUserToDoItem = (IasUserToDoItem) notiIdMissList.get(totalNotiIdCnt);

                // Check duplicate client_id + open_id in getNotiId request
                IasMsgRecipientKey iasMsgRecipientKey = new IasMsgRecipientKey(iasUserToDoItem.getClientId(),
                        iasUserToDoItem.getRecipientId());
                if (!getNotiIdReqEsOpenIdList.contains(iasMsgRecipientKey)) {
                    EServiceOpenIdItem eServiceOpenIdItem = new EServiceOpenIdItem(iasUserToDoItem.getClientId(),
                            iasUserToDoItem.getRecipientId());
                    eServiceOpenIdItemList.add(eServiceOpenIdItem);
                    getNotiIdReqEsOpenIdList.add(iasMsgRecipientKey);
                    batchCnt++;
                }

                totalNotiIdCnt++;
            }
            logger.info("updateIasUserToDoItemNotiIDByESOpenID - batchCnt=" + batchCnt);

            logger.info("updateIasUserToDoItemNotiIDByESOpenID - eServiceOpenIdItemList.size="
                    + eServiceOpenIdItemList.size());

            if (eServiceOpenIdItemList.size() > 0) {
                EServiceOpenIdsBean eServiceOpenIdsBean = new EServiceOpenIdsBean();
                eServiceOpenIdsBean.setEServiceOpenIDs(eServiceOpenIdItemList);

                // Call getNotificationID API to get missing NotiID from iAM Smart system by Service Provider ID and Open ID
                if (!isEIDUtilsInit) {
                    EIDUtils.initialize(prop);
                }

                EIDResponseBean getNotiIdResponse = EIDUtils.doRequestGetNotificationIDs(iasGetNotiIdEndPoint,
                        eServiceOpenIdsBean);

                if (getNotiIdResponse != null) {

                    logger.debug("updateIasUserToDoItemNotiIDByESOpenID - EIDResponseBean.getContent="
                            + getNotiIdResponse.getContent());

                    if (((IntegrationConstants.GET_NOTI_ID_RESULT_CODE_SUCCESS
                            .equalsIgnoreCase(getNotiIdResponse.getCode())) ||
                            (IntegrationConstants.GET_NOTI_ID_RESULT_CODE_PARTIAL_SUCCESS
                                    .equalsIgnoreCase(getNotiIdResponse.getCode())))
                            && getNotiIdResponse.getContent() != null
                            && getNotiIdResponse.getContent().length() > 0) {

                        NotificationIdsBean notificationIdsBean = new NotificationIdsBean(
                                getNotiIdResponse.getContent());
                        ArrayList<NotificationIdItem> notiIdItemList = notificationIdsBean.getNotificationIDs();

                        // Loop the getNotificationID result
                        for (NotificationIdItem notiIdItem : notiIdItemList) {
                            IasMsgRecipientKey iasMsgRecipientKey = new IasMsgRecipientKey(notiIdItem.getClientID(),
                                    notiIdItem.getOpenID());
                            IasClientIdNotiIdPair iasClientIdNotiIdPair = new IasClientIdNotiIdPair(
                                    notiIdItem.getClientID(), notiIdItem.getNotificationID());
                            if ((IntegrationConstants.GET_NOTI_ID_RESULT_VALID.equals(notiIdItem.getStatus())) ||
                                    (IntegrationConstants.GET_NOTI_ID_RESULT_V2_ACTIVE.equals(notiIdItem.getStatus()))
                                    || (IntegrationConstants.GET_NOTI_ID_RESULT_V2_SUSPEND
                                            .equals(notiIdItem.getStatus()))
                                    || (IntegrationConstants.GET_NOTI_ID_RESULT_V2_INVALID
                                            .equals(notiIdItem.getStatus()))
                                    || (IntegrationConstants.GET_NOTI_ID_RESULT_V2_DEREGISTERED
                                            .equals(notiIdItem.getStatus()))) {

                                iasOpenIdNotiIdMap.put(iasMsgRecipientKey, notiIdItem);

                                if (IntegrationConstants.GET_NOTI_ID_RESULT_V2_DEREGISTERED
                                        .equals(notiIdItem.getStatus())) {
                                    if (!deRegClientIdNotiIdPairList.contains(iasClientIdNotiIdPair)) {
                                        deRegClientIdNotiIdPairList.add(iasClientIdNotiIdPair);
                                    }
                                }
                            } else {

                                if (!iasInvalidEsOpenIdList.contains(iasMsgRecipientKey)) {
                                    iasInvalidEsOpenIdList.add(iasMsgRecipientKey);
                                }

                            }
                        }
                    } else {
                        logger.warn(
                                "updateIasUserToDoItemNotiIDByESOpenID - EIDUtils.doRequestGetNotificationIDs Fail. "
                                        + "EIDResponseBean.getCode=" + getNotiIdResponse.getCode()
                                        + ", EIDResponseBean.getMessage=" + getNotiIdResponse.getMessage()
                                        + ", EIDResponseBean.getTxID=" + getNotiIdResponse.getTxID()
                                        + ". No DB Change, will retry again in next run.");
                    }
                } else {
                    logger.warn("updateIasUserToDoItemNotiIDByESOpenID - EIDUtils.doRequestGetNotificationIDs Fail. "
                            + "EIDResponseBean is null. No DB Change, will retry again in next run.");
                }
            }
        }

        // Part A: Loop the notiIdMissList, insert ias_es_noti_map and update ias_user_to_do_item
        for (IasUserToDoItem iasUserToDoItem : notiIdMissList) {

            logger.debug("updateIasUserToDoItemNotiIDByESOpenID - ClientId=" + iasUserToDoItem.getClientId()
                    + ", RecipientId=" + iasUserToDoItem.getRecipientId() + ", RecipientIdType="
                    + iasUserToDoItem.getRecipientIdType()
                    + ", IasToDoItemId=" + iasUserToDoItem.getIasToDoItemId() + ", ServiceProviderId="
                    + iasUserToDoItem.getServiceProviderId());

            // Get the NOTI_ID from iasOpenIdNotiIdMap
            NotificationIdItem notificationIdItem = iasOpenIdNotiIdMap
                    .get(new IasMsgRecipientKey(iasUserToDoItem.getClientId(), iasUserToDoItem.getRecipientId()));

            // Client ID, Open ID pair is valid
            if (notificationIdItem != null && notificationIdItem.getNotificationID() != null
                    && notificationIdItem.getNotificationID().length() > 0) {

                logger.info("updateIasUserToDoItemNotiIDByESOpenID - RecipientId=" + iasUserToDoItem.getRecipientId()
                        + ", notificationIdItem.getNotificationID=" + notificationIdItem.getNotificationID()
                        + ", notificationIdItem.getStatus=" + notificationIdItem.getStatus());

                IasUser iasUser = iasNotiDAO.getIasEsNotiMap(conn, notificationIdItem.getNotificationID(),
                        iasUserToDoItem.getServiceProviderId());

                // If IasEsNotiMap_ record not exist in DB, insert a new one
                if (iasUser == null) {

                    // Create IAS_ES_NOTI_MAP
                    IasEsNotiMap_ notiMap = new IasEsNotiMap_();
                    notiMap.setServiceProviderId(iasUserToDoItem.getServiceProviderId());
                    notiMap.setNotiId(notificationIdItem.getNotificationID());
                    notiMap.setOpenId(iasUserToDoItem.getRecipientId());

                    // If Transactional message case (i.e. IAS_OPT_CHECK_IND=N), default opt in value is U
                    // Input field for "ias_opt_check_ind" is not used because "ias_show_es_set_btn" derive the same meaning. So, check "ias_show_es_set_btn" while processing message requests.
                    if (!"Y".equals(iasUserToDoItem.getIasOptCheckInd())) { // getIasOptCheckInd() using the value of ias_show_es_set_btn in DB
                        notiMap.setOptIn(IntegrationConstants.OPT_IN_U);
                    } else {
                        if (IntegrationConstants.GET_NOTI_ID_RESULT_V2_DEREGISTERED
                                .equals(notificationIdItem.getStatus()))
                            notiMap.setOptIn(IntegrationConstants.OPT_IN_U);
                        else
                            notiMap.setOptIn(IntegrationConstants.OPT_IN_Y);
                    }

                    notiMap.insert(conn);
                } else if (iasUser != null && iasUser.getOpenId() == null) {
                    // Update OpenID in IAS_ES_NOTI_MAP
                    IasEsNotiMap_ notiMap = new IasEsNotiMap_(conn, notificationIdItem.getNotificationID(),
                            iasUserToDoItem.getServiceProviderId());
                    notiMap.setOpenId(iasUserToDoItem.getRecipientId());
                    if (IntegrationConstants.GET_NOTI_ID_RESULT_V2_DEREGISTERED
                            .equals(notificationIdItem.getStatus())) {
                        notiMap.setOptIn(IntegrationConstants.OPT_IN_U);
                    } else {
                        notiMap.setOptIn(IntegrationConstants.OPT_IN_Y);
                    }
                    notiMap.update(conn);
                }

                // Update NOTI_ID for IAS_USER_TO_DO_ITEM
                IasUserToDoItem_ tempIasUserToDoItem = new IasUserToDoItem_(conn, iasUserToDoItem.getClientId(),
                        iasUserToDoItem.getRecipientId(), iasUserToDoItem.getIasToDoItemId());
                tempIasUserToDoItem.setRecipientIdType(iasUserToDoItem.getRecipientIdType());
                tempIasUserToDoItem.setNotiId(notificationIdItem.getNotificationID());
                tempIasUserToDoItem.update(conn);

            } else { // Check if OPEN_ID invalid

                if (iasInvalidEsOpenIdList.contains(
                        new IasMsgRecipientKey(iasUserToDoItem.getClientId(), iasUserToDoItem.getRecipientId()))) {
                    // Update IAS_NOTI_STATUS for IAS_USER_TO_DO_ITEM
                    IasUserToDoItem_ tempIasUserToDoItem = new IasUserToDoItem_(conn, iasUserToDoItem.getClientId(),
                            iasUserToDoItem.getRecipientId(), iasUserToDoItem.getIasToDoItemId());
                    tempIasUserToDoItem.setRecipientIdType(iasUserToDoItem.getRecipientIdType());
                    tempIasUserToDoItem.setIasNotiStatus(IntegrationConstants.IAS_NOTI_STATUS_INVALID);
                    tempIasUserToDoItem.update(conn);

                    // For iAM Smart Undelivered Report 06D
                    logger.info(
                            "updateIasUserToDoItemNotiIDByESOpenID - iasToDoItemNotiDAO.createIasUndeliveredMessage (RECIPIENT_ID invalid)");
                    iasToDoItemNotiDAO.createIasUndeliveredToDoItem(conn, iasUserToDoItem.getClientId(),
                            iasUserToDoItem.getRecipientId(),
                            iasUserToDoItem.getIasToDoItemId(), iasUserToDoItem.getRecipientIdType(),
                            IntegrationConstants.IAS_MSG_RCPT_TYPE_IAM_SMART,
                            IasToDoItemConstant.STATUS_UNDELIVERED_TO_DO_ITEM_NOT_INFORMED,
                            IntegrationConstants.IAS_UNDELIVER_MSG_REASON_ACC_NOT_ACTIVE);
                }
            }
            conn.commit();
        }

        logger.debug("updateIasUserToDoItemNotiIDByESOpenID - END");

        return deRegClientIdNotiIdPairList;
    }

}
