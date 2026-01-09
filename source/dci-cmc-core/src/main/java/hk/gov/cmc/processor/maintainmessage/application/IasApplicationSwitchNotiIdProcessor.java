package hk.gov.cmc.processor.maintainmessage.application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bouncycastle.asn1.ocsp.ServiceLocator;

import hk.gov.cmc.common.CmcAppPropertyNames;
import hk.gov.cmc.common.IasApplicationConstant;
import hk.gov.cmc.common.IntegrationConstants;
import hk.gov.cmc.config.CmcEnvProperties;
import hk.gov.cmc.dao.maintainmessage.notification.IasApplicationNotiDAO;
import hk.gov.cmc.dao.maintainmessage.notification.IasNotiDAO;
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
import hk.gov.cmc.model.maintainmessage.application.IasUserApplication;
import hk.gov.cmc.model.maintainmessage.application.IasUserApplicationWrapped;
import hk.gov.cmc.model.maintainmessage.key.IasClientIdNotiIdPair;
import hk.gov.cmc.model.maintainmessage.key.IasMsgRecipientKey;
import hk.gov.cmc.model.maintainmessage.user.IasUser;
import hk.gov.cmc.persistence.connection.hpfw.HPFW_Connection;
import hk.gov.cmc.persistence.ias.application.IasUserApplication_;
import hk.gov.cmc.persistence.ias.notification.IasEsNotiMap_;
import hk.gov.cmc.utils.common.EncUtils;


public class IasApplicationSwitchNotiIdProcessor {

    private static Log logger = LogFactory.getLog(IasApplicationSwitchNotiIdProcessor.class);
    private final static CmcEnvProperties cmcEnvProperties = new CmcEnvProperties();

    public IasApplicationSwitchNotiIdProcessor() {
    }

    public List<IasClientIdNotiIdPair> updateIasUserAppNotiIDByESHKID(HPFW_Connection conn, 
        boolean isEIDUtilsInit, List<IasUserApplication> notiIdMissListWithOutHKID) throws Exception {

        logger.info("updateIasUserAppNotiIDByESHKID - START");

        List<IasClientIdNotiIdPair> deRegClientIdNotiIdPairList = new ArrayList<IasClientIdNotiIdPair>();

        Properties prop = cmcEnvProperties.getProperties();
        int iasGetNotiIdBatchLimit = Integer.parseInt(prop.getProperty(CmcAppPropertyNames.IAS_GET_NOTI_ID_BATCH_LIMIT_PROPERTY_NAME));
        String iasSwitchNotiIdEndPoint = prop.getProperty(CmcAppPropertyNames.IAS_SWITCH_NOTI_ID_BY_HKID_END_POINT);

        IasNotiDAO iasNotiDAO = new IasNotiDAO();
        IasApplicationNotiDAO iasAppNotiDAO = new IasApplicationNotiDAO();

        // Create HashMap iasHKIdNotiIdMap
        HashMap<IasMsgRecipientKey, NotificationIdByHKIDItem> iasHKIdNotiIdMap = new HashMap<IasMsgRecipientKey, NotificationIdByHKIDItem>();

        // Create List for invalid clientID + HKID
        List<IasMsgRecipientKey> iasInvalidEsHKIdList = new ArrayList<IasMsgRecipientKey>();

        // Create List for clientID + HKID pair to check duplicate in getNotiId request
        List<IasMsgRecipientKey> getNotiIdReqEsHKIdList = new ArrayList<IasMsgRecipientKey>();

        // Decrypt HKID
        List<IasUserApplicationWrapped> notiIdMissList = new ArrayList<IasUserApplicationWrapped>();
        for (IasUserApplication iasUserApplication : notiIdMissListWithOutHKID) {
            try {
                IasUserApplicationWrapped iasUserApplicationWrapped = new IasUserApplicationWrapped();
                iasUserApplicationWrapped.setIasUserApplication(iasUserApplication);
                String hkid = EncUtils.decrypt(
                    iasUserApplication.getHkidEncrypted(), 
                    prop.getProperty(CmcAppPropertyNames.HKID_ENCRYPT_PASSPHRASE_ID_PROPERTY_NAME), 
                    prop.getProperty(CmcAppPropertyNames.HKID_ENCRYPT_PASSPHRASE_USAGE_PROPERTY_NAME)
                );
                logger.info("updateIasUserAppNotiIDByESHKID - iasUserApplication.getHkidEncrypted=" + iasUserApplication.getHkidEncrypted() 
                    + ", iasUserApplication.getClientId=" + iasUserApplication.getClientId() + ", iasUserApplication.getTranId=" + iasUserApplication.getTranId());
                iasUserApplicationWrapped.setHkid(hkid);
                notiIdMissList.add(iasUserApplicationWrapped);
            } catch (Exception e) {
                logger.error("updateIasUserAppNotiIDByESHKID - Decrypt HKID failed for iasUserApplication.getHkidEncrypted=" 
                    + iasUserApplication.getHkidEncrypted() + ", iasUserApplication.getTranId=" + iasUserApplication.getTranId(), e);
            }
        }

        int totalNotiIdCnt = 0;
        // Part A: Retrieve the noti id from iAM Smart system by clientID and HKID
        while (totalNotiIdCnt < notiIdMissList.size()) {

            logger.debug("updateIasUserAppNotiIDByESHKID - totalNotiIdCnt=" + totalNotiIdCnt);

            int batchCnt = 0;

            ArrayList<EServiceHkidItem> eServiceHKIdItemList = new ArrayList<EServiceHkidItem>();
            while ((batchCnt < iasGetNotiIdBatchLimit) && (totalNotiIdCnt < notiIdMissList.size())) {
                IasUserApplicationWrapped iasUserApplicationWrapped = (IasUserApplicationWrapped) notiIdMissList.get(totalNotiIdCnt);
                logger.debug("iasUserApplicationWrapped=" + iasUserApplicationWrapped.toString());

                // Check duplicate client_id + hkid in getNotiId request
                IasMsgRecipientKey iasMsgRecipientKey = new IasMsgRecipientKey(iasUserApplicationWrapped.getIasUserApplication().getClientId(), iasUserApplicationWrapped.getHkid());
                if (!getNotiIdReqEsHKIdList.contains(iasMsgRecipientKey)) {
                    EServiceHkidItem eServiceHKIdItem = new EServiceHkidItem(iasUserApplicationWrapped.getIasUserApplication().getClientId(), iasUserApplicationWrapped.getHkid());
                    eServiceHKIdItemList.add(eServiceHKIdItem);
                    getNotiIdReqEsHKIdList.add(iasMsgRecipientKey);
                    batchCnt++;
                }

                totalNotiIdCnt++;
            }
            logger.info("updateIasUserAppNotiIDByESHKID - batchCnt=" + batchCnt);

            logger.info("updateIasUserAppNotiIDByESHKID - eServiceHKIdItemList.size=" + eServiceHKIdItemList.size());

            if (eServiceHKIdItemList.size() > 0) {
                EServiceHkidsBean eServiceHKIdsBean = new EServiceHkidsBean();
                eServiceHKIdsBean.setEServiceHKIDs(eServiceHKIdItemList);

                // Call switchNotificationID API to get missing NotiID from iAM Smart system by ClientID and HKID
                if (!isEIDUtilsInit) {
                    EIDUtils.initialize(prop);
                }

                EIDResponseBean getNotiIdResponse = EIDUtils.doRequestSwitchNotificationIDsByHKIDs(iasSwitchNotiIdEndPoint, eServiceHKIdsBean);

                if (getNotiIdResponse != null) {

                    logger.debug("updateIasUserAppNotiIDByESHKID - EIDResponseBean.getContent=" + getNotiIdResponse.getContent());

                    if (((IntegrationConstants.GET_NOTI_ID_RESULT_CODE_SUCCESS.equalsIgnoreCase(getNotiIdResponse.getCode())) || 
                            (IntegrationConstants.GET_NOTI_ID_RESULT_CODE_PARTIAL_SUCCESS.equalsIgnoreCase(getNotiIdResponse.getCode())))
                        && getNotiIdResponse.getContent() != null
                        && getNotiIdResponse.getContent().length() > 0) {

                        NotificationIdsByHKIDsBean notiIdsResult = new NotificationIdsByHKIDsBean(getNotiIdResponse.getContent());
                        ArrayList<NotificationIdByHKIDItem> notiIdResultList = notiIdsResult.getNotificationIDs();

                        // Loop the switchNotificationID result
                        for (NotificationIdByHKIDItem notiIdItem : notiIdResultList) {
                            IasMsgRecipientKey iasMsgRecipientKey = new IasMsgRecipientKey(notiIdItem.getClientID(), notiIdItem.getHKID());
                            IasClientIdNotiIdPair iasClientIdNotiIdPair = new IasClientIdNotiIdPair(notiIdItem.getClientID(), notiIdItem.getNotificationID());
                            if ((IntegrationConstants.GET_NOTI_ID_RESULT_VALID.equals(notiIdItem.getStatus())) ||
                                (IntegrationConstants.GET_NOTI_ID_RESULT_V2_ACTIVE.equals(notiIdItem.getStatus())) ||
                                (IntegrationConstants.GET_NOTI_ID_RESULT_V2_SUSPEND.equals(notiIdItem.getStatus())) ||
                                (IntegrationConstants.GET_NOTI_ID_RESULT_V2_INVALID.equals(notiIdItem.getStatus())) ||
                                (IntegrationConstants.GET_NOTI_ID_RESULT_V2_DEREGISTERED.equals(notiIdItem.getStatus()))) {
                                    iasHKIdNotiIdMap.put(iasMsgRecipientKey, notiIdItem);

                                if (IntegrationConstants.GET_NOTI_ID_RESULT_V2_DEREGISTERED.equals(notiIdItem.getStatus())) {
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
                        logger.warn("updateIasUserAppNotiIDByESHKID - EIDUtils.doRequestGetNotificationIDs Fail. "
                            + "EIDResponseBean.getCode=" + getNotiIdResponse.getCode() + ", EIDResponseBean.getMessage=" + getNotiIdResponse.getMessage()
                            + ", EIDResponseBean.getTxID=" + getNotiIdResponse.getTxID() + ". No DB Change, will retry again in next run.");
                    }
                } else {
                    logger.warn("updateIasUserAppNotiIDByESHKID - EIDUtils.doRequestGetNotificationIDs Fail. "
                        + "EIDResponseBean is null. No DB Change, will retry again in next run.");
                }
            }
        }

        logger.info("updateIasUserAppNotiIDByESHKID - iasHKIdNotiIdMap.size=" + iasHKIdNotiIdMap.size());
        for (IasMsgRecipientKey iasMsgRecipientKey : iasHKIdNotiIdMap.keySet()) {
            logger.info("updateIasUserAppNotiIDByESHKID - iasHKIdNotiIdMap ClientID=" + iasMsgRecipientKey.getClientId() 
                + ", NotificationID=" + iasHKIdNotiIdMap.get(iasMsgRecipientKey).getNotificationID() + ", Status=" + iasHKIdNotiIdMap.get(iasMsgRecipientKey).getStatus());
        }

        logger.info("updateIasUserAppNotiIDByESHKID - iasInvalidEsHKIdList.size=" + iasInvalidEsHKIdList.size());
        for (IasMsgRecipientKey iasMsgRecipientKey : iasInvalidEsHKIdList) {
            logger.info("updateIasUserAppNotiIDByESHKID - iasInvalidEsHKIdList ClientID=" + iasMsgRecipientKey.getClientId());
        }

        // Part A: Loop the notiIdMissList, insert ias_es_noti_map and update ias_user_application
        // CMC-2025-031: Remove duplicated HKID before inserting into IAS_ES_NOTI_MAP table - BEGIN
        List<String> insertedHkidList = new ArrayList<String>();
        // CMC-2025-031: Remove duplicated HKID before inserting into IAS_ES_NOTI_MAP table - END
        for (IasUserApplicationWrapped iasUserApplicationWrapped : notiIdMissList) {

            logger.debug("updateIasUserAppNotiIDByESHKID - ClientId=" + iasUserApplicationWrapped.getIasUserApplication().getClientId()
                + ", TranId=" + iasUserApplicationWrapped.getIasUserApplication().getTranId() + ", RecipientIdType=" + iasUserApplicationWrapped.getIasUserApplication().getRecipientIdType()
                + ", IasApplicationId=" + iasUserApplicationWrapped.getIasUserApplication().getIasApplicationId() + ", ServiceProviderId=" + iasUserApplicationWrapped.getIasUserApplication().getServiceProviderId());

            // Get the NOTI_ID from iasHKIdNotiIdMap
            NotificationIdByHKIDItem notiIdByHKIDItem = iasHKIdNotiIdMap.get(new IasMsgRecipientKey(iasUserApplicationWrapped.getIasUserApplication().getClientId(), iasUserApplicationWrapped.getHkid()));
            logger.debug("updateIasUserAppNotiIDByESHKID - notiIdByHKIDItem=" + notiIdByHKIDItem);

            // Client ID, HKID pair is valid
            if (notiIdByHKIDItem != null && notiIdByHKIDItem.getNotificationID() != null && notiIdByHKIDItem.getNotificationID().length() > 0) {
                logger.info("updateIasUserAppNotiIDByESHKID - update NOTI table - Client ID, HKID pair is valid. ClientId=" + iasUserApplicationWrapped.getIasUserApplication().getClientId() 
                    + ", TranId=" + iasUserApplicationWrapped.getIasUserApplication().getTranId()
                    + ", NotificationID=" + notiIdByHKIDItem.getNotificationID() + ", Status=" + notiIdByHKIDItem.getStatus());

                IasUser iasUser = iasNotiDAO.getIasEsNotiMap(conn, notiIdByHKIDItem.getNotificationID(), iasUserApplicationWrapped.getIasUserApplication().getServiceProviderId());

                // If IasEsNotiMap_ record not exist in DB, insert a new one
                if (iasUser == null) {
                    // Create IAS_ES_NOTI_MAP
                    IasEsNotiMap_ notiMap = new IasEsNotiMap_();
                    notiMap.setServiceProviderId(iasUserApplicationWrapped.getIasUserApplication().getServiceProviderId());
                    notiMap.setNotiId(notiIdByHKIDItem.getNotificationID());
                    notiMap.setHkidHashed(iasUserApplicationWrapped.getIasUserApplication().getRecipientId());
                    notiMap.setHkidEncrypted(iasUserApplicationWrapped.getIasUserApplication().getHkidEncrypted());
                    if (IntegrationConstants.GET_NOTI_ID_RESULT_V2_DEREGISTERED.equals(notiIdByHKIDItem.getStatus())) {
                        notiMap.setOptIn(IntegrationConstants.OPT_IN_U);
                    } else {
                        notiMap.setOptIn(IntegrationConstants.OPT_IN_Y);
                    }
                    notiMap.insert(conn);
                    // CMC-2025-031: Remove duplicated HKID before inserting into IAS_ES_NOTI_MAP table - BEGIN
                    insertedHkidList.add(iasUserApplicationWrapped.getHkid());
                    // CMC-2025-031: Remove duplicated HKID before inserting into IAS_ES_NOTI_MAP table - END
                } 
                // CMC-2025-021: Use symmetric encryption to encrypt HKID in iAM Smart To-Do Item and Application - BEGIN
                // else if (iasUser != null && iasUser.getHkidHashed() == null) {
                else {
                    if (iasUser.getHkidHashed() == null) {
                        // CMC-2025-021: Use symmetric encryption to encrypt HKID in iAM Smart To-Do Item and Application - END
                        // Update HKID in IAS_ES_NOTI_MAP
                        IasEsNotiMap_ notiMap = new IasEsNotiMap_(conn, notiIdByHKIDItem.getNotificationID(), iasUserApplicationWrapped.getIasUserApplication().getServiceProviderId());
                        notiMap.setHkidHashed(iasUserApplicationWrapped.getIasUserApplication().getRecipientId());
                        notiMap.setHkidEncrypted(iasUserApplicationWrapped.getIasUserApplication().getHkidEncrypted());
                        if (IntegrationConstants.GET_NOTI_ID_RESULT_V2_DEREGISTERED.equals(notiIdByHKIDItem.getStatus())) {
                            notiMap.setOptIn(IntegrationConstants.OPT_IN_U);
                        } else {
                            notiMap.setOptIn(IntegrationConstants.OPT_IN_Y);
                        }
                        notiMap.update(conn);
                        // CMC-2025-031: Remove duplicated HKID before inserting into IAS_ES_NOTI_MAP table - BEGIN
                        insertedHkidList.add(iasUserApplicationWrapped.getHkid());
                        // CMC-2025-031: Remove duplicated HKID before inserting into IAS_ES_NOTI_MAP table - END
                    } else {
                        // CMC-2025-031: Remove duplicated HKID before inserting into IAS_ES_NOTI_MAP table - BEGIN
                        if (!insertedHkidList.contains(iasUserApplicationWrapped.getHkid())) {
                            // CMC-2025-021: Use symmetric encryption to encrypt HKID in iAM Smart To-Do Item and Application - BEGIN
                            logger.warn("updateIasUserAppNotiIDByESHKID - One notificationId has more than one decrypted HKID, please check and contact iAM Smart Team if needed. "
                                + "iasUser.getHkidHashed() is not null for ClientId=" + iasUserApplicationWrapped.getIasUserApplication().getClientId() 
                                + ", RecipientId=" + iasUserApplicationWrapped.getIasUserApplication().getRecipientId()
                                + ", IasApplicationId=" + iasUserApplicationWrapped.getIasUserApplication().getIasApplicationId() + ". No DB Change.");
                            // CMC-2025-021: Use symmetric encryption to encrypt HKID in iAM Smart To-Do Item and Application - END
                        } else {
                            logger.info("updateIasUserAppNotiIDByESHKID - notificationId inserted into IAS_ES_NOTI_MAP table in the same batch before. "
                                + "iasUser.getHkidHashed() is not null for ClientId=" + iasUserApplicationWrapped.getIasUserApplication().getClientId() 
                                + ", RecipientId=" + iasUserApplicationWrapped.getIasUserApplication().getRecipientId()
                                + ", IasApplicationId=" + iasUserApplicationWrapped.getIasUserApplication().getIasApplicationId() + ". No DB Change.");
                        }
                        // CMC-2025-031: Remove duplicated HKID before inserting into IAS_ES_NOTI_MAP table - END
                    }
                }

                // Update NOTI_ID for IAS_USER_APPLICATION
                IasUserApplication_ tempIasUserApplication = new IasUserApplication_(conn, iasUserApplicationWrapped.getIasUserApplication().getClientId(),
                    iasUserApplicationWrapped.getIasUserApplication().getRecipientId(), iasUserApplicationWrapped.getIasUserApplication().getIasApplicationId());
                tempIasUserApplication.setNotiId(notiIdByHKIDItem.getNotificationID());
                tempIasUserApplication.update(conn);

            } else {
                logger.info("updateIasUserAppNotiIDByESHKID - Client ID, HKID pair is invalid. ClientID=" + iasUserApplicationWrapped.getIasUserApplication().getClientId() 
                    + ", TranId=" + iasUserApplicationWrapped.getIasUserApplication().getTranId());
                // Check if HKID invalid
                if (iasInvalidEsHKIdList.contains(new IasMsgRecipientKey(iasUserApplicationWrapped.getIasUserApplication().getClientId(), iasUserApplicationWrapped.getHkid()))) {
                    logger.info("updateIasUserAppNotiIDByESHKID - iasInvalidEsHKIdList contains Client ID, HKID pair. RecipientId=" + iasUserApplicationWrapped.getIasUserApplication().getRecipientId());
                    // Update IAS_NOTI_STATUS for IAS_USER_APPLICATION
                    IasUserApplication_ tempIasUserApplication = new IasUserApplication_(conn, iasUserApplicationWrapped.getIasUserApplication().getClientId(),
                        iasUserApplicationWrapped.getIasUserApplication().getRecipientId(), iasUserApplicationWrapped.getIasUserApplication().getIasApplicationId());
                    tempIasUserApplication.setIasNotiStatus(IntegrationConstants.IAS_NOTI_STATUS_INVALID);
                    tempIasUserApplication.update(conn);

                    // For iAM Smart Undelivered Report 06D
                    logger.info("updateIasUserAppNotiIDByESHKID - iasAppNotiDAO.createIasUndeliveredMessage (RECIPIENT_ID invalid)");
                    IasUserApplication iasUserApplication = iasUserApplicationWrapped.getIasUserApplication();
                    iasAppNotiDAO.createIasUndeliveredApplicationWithHKIdEncrypted(conn, iasUserApplication.getClientId(), iasUserApplication.getRecipientId(), 
                        iasUserApplication.getIasApplicationId(), iasUserApplication.getRecipientIdType(), iasUserApplication.getHkidEncrypted(), 
                        IntegrationConstants.IAS_MSG_RCPT_TYPE_IAM_SMART, IasApplicationConstant.STATUS_UNDELIVERED_APPLICATION_NOT_INFORMED, 
                        IntegrationConstants.IAS_UNDELIVER_MSG_REASON_ACC_NOT_ACTIVE);
                } else {
                    // Valid HKID but failed status
                    logger.info("updateIasUserAppNotiIDByESHKID - Valid HKID but failed status"
                        + ". ClientID=" + iasUserApplicationWrapped.getIasUserApplication().getClientId() + ", RecipientId=" + iasUserApplicationWrapped.getIasUserApplication().getRecipientId());
                }
            }
            conn.commit();
        }

        logger.info("updateIasUserAppNotiIDByESHKID - END");

        return deRegClientIdNotiIdPairList;
    }

    public List<IasClientIdNotiIdPair> updateIasUserAppNotiIDByESOpenID(HPFW_Connection conn, 
        boolean isEIDUtilsInit, List<IasUserApplication> notiIdMissList) throws Exception {

        logger.debug("updateIasUserAppNotiIDByESOpenID - START");

        List<IasClientIdNotiIdPair> deRegClientIdNotiIdPairList = new ArrayList<IasClientIdNotiIdPair>();

        Properties prop = cmcEnvProperties.getProperties();
        int iasGetNotiIdBatchLimit = Integer.parseInt(prop.getProperty(CmcAppPropertyNames.IAS_GET_NOTI_ID_BATCH_LIMIT_PROPERTY_NAME));
        String iasGetNotiIdEndPoint = prop.getProperty(CmcAppPropertyNames.IAS_GET_NOTI_ID_END_POINT_PROPERTY_NAME);

        IasNotiDAO iasNotiDAO = new IasNotiDAO();
        IasApplicationNotiDAO iasAppNotiDAO = new IasApplicationNotiDAO();

        // Create HashMap iasOpenNotiIdMap
        HashMap<IasMsgRecipientKey, NotificationIdItem> iasOpenIdNotiIdMap = new HashMap<IasMsgRecipientKey, NotificationIdItem>();

        // Create List for invalid clientID + OpenID
        List<IasMsgRecipientKey> iasInvalidEsOpenIdList = new ArrayList<IasMsgRecipientKey>();

        // Create List for clientID + OpenID pair to check duplicate in getNotiId request
        List<IasMsgRecipientKey> getNotiIdReqEsOpenIdList = new ArrayList<IasMsgRecipientKey>();

        int totalNotiIdCnt = 0;
        // Part A: Retrieve the noti id from iAM Smart system by clientID and OpenID
        while (totalNotiIdCnt < notiIdMissList.size()) {

            logger.debug("updateIasUserAppNotiIDByESOpenID - totalNotiIdCnt=" + totalNotiIdCnt);

            int batchCnt = 0;

            ArrayList<EServiceOpenIdItem> eServiceOpenIdItemList = new ArrayList<EServiceOpenIdItem>();
            while ((batchCnt < iasGetNotiIdBatchLimit) && (totalNotiIdCnt < notiIdMissList.size())) {
                IasUserApplication iasUserApplication = (IasUserApplication) notiIdMissList.get(totalNotiIdCnt);

                // Check duplicate client_id + open_id in getNotiId request
                IasMsgRecipientKey iasMsgRecipientKey = new IasMsgRecipientKey(iasUserApplication.getClientId(), iasUserApplication.getRecipientId());
                if (!getNotiIdReqEsOpenIdList.contains(iasMsgRecipientKey)) {
                    EServiceOpenIdItem eServiceOpenIdItem = new EServiceOpenIdItem(iasUserApplication.getClientId(), iasUserApplication.getRecipientId());
                    eServiceOpenIdItemList.add(eServiceOpenIdItem);
                    getNotiIdReqEsOpenIdList.add(iasMsgRecipientKey);
                    batchCnt++;
                }

                totalNotiIdCnt++;
            }
            logger.info("updateIasUserAppNotiIDByESOpenID - batchCnt=" + batchCnt);

            logger.info("updateIasUserAppNotiIDByESOpenID - eServiceOpenIdItemList.size=" + eServiceOpenIdItemList.size());

            if (eServiceOpenIdItemList.size() > 0) {
                EServiceOpenIdsBean eServiceOpenIdsBean = new EServiceOpenIdsBean();
                eServiceOpenIdsBean.setEServiceOpenIDs(eServiceOpenIdItemList);

                // Call getNotificationID API to get missing NotiID from iAM Smart system by Service Provider ID and Open ID
                if (!isEIDUtilsInit) {
                    EIDUtils.initialize(prop);
                }

                EIDResponseBean getNotiIdResponse = EIDUtils.doRequestGetNotificationIDs(iasGetNotiIdEndPoint, eServiceOpenIdsBean);

                if (getNotiIdResponse != null) {

                    logger.debug("updateIasUserAppNotiIDByESOpenID - EIDResponseBean.getContent=" + getNotiIdResponse.getContent());

                    if (((IntegrationConstants.GET_NOTI_ID_RESULT_CODE_SUCCESS.equalsIgnoreCase(getNotiIdResponse.getCode())) || 
                            (IntegrationConstants.GET_NOTI_ID_RESULT_CODE_PARTIAL_SUCCESS.equalsIgnoreCase(getNotiIdResponse.getCode())))
                        && getNotiIdResponse.getContent() != null
                        && getNotiIdResponse.getContent().length() > 0) {

                        NotificationIdsBean notificationIdsBean = new NotificationIdsBean(getNotiIdResponse.getContent());
                        ArrayList<NotificationIdItem> notiIdItemList = notificationIdsBean.getNotificationIDs();

                        // Loop the getNotificationID result
                        for (NotificationIdItem notiIdItem : notiIdItemList) {
                            IasMsgRecipientKey iasMsgRecipientKey = new IasMsgRecipientKey(notiIdItem.getClientID(), notiIdItem.getOpenID());
                            IasClientIdNotiIdPair iasClientIdNotiIdPair = new IasClientIdNotiIdPair(notiIdItem.getClientID(), notiIdItem.getNotificationID());
                            if ((IntegrationConstants.GET_NOTI_ID_RESULT_VALID.equals(notiIdItem.getStatus())) ||
                                (IntegrationConstants.GET_NOTI_ID_RESULT_V2_ACTIVE.equals(notiIdItem.getStatus())) ||
                                (IntegrationConstants.GET_NOTI_ID_RESULT_V2_SUSPEND.equals(notiIdItem.getStatus())) ||
                                (IntegrationConstants.GET_NOTI_ID_RESULT_V2_INVALID.equals(notiIdItem.getStatus())) ||
                                (IntegrationConstants.GET_NOTI_ID_RESULT_V2_DEREGISTERED.equals(notiIdItem.getStatus()))) {
                                iasOpenIdNotiIdMap.put(iasMsgRecipientKey, notiIdItem);

                                if (IntegrationConstants.GET_NOTI_ID_RESULT_V2_DEREGISTERED.equals(notiIdItem.getStatus())) {
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
                        logger.warn("updateIasUserAppNotiIDByESOpenID - EIDUtils.doRequestGetNotificationIDs Fail. "
                            + "EIDResponseBean.getCode=" + getNotiIdResponse.getCode() + ", EIDResponseBean.getMessage=" + getNotiIdResponse.getMessage()
                            + ", EIDResponseBean.getTxID=" + getNotiIdResponse.getTxID() + ". No DB Change, will retry again in next run.");
                    }
                } else {
                    logger.warn("updateIasUserAppNotiIDByESOpenID - EIDUtils.doRequestGetNotificationIDs Fail. "
                        + "EIDResponseBean is null. No DB Change, will retry again in next run.");
                }
            }
        }

        // Part A: Loop the notiIdMissList, insert ias_es_noti_map and update ias_user_application
        for (IasUserApplication iasUserApplication : notiIdMissList) {

            logger.debug("updateIasUserAppNotiIDByESOpenID - ClientId=" + iasUserApplication.getClientId()
                + ", RecipientId=" + iasUserApplication.getRecipientId() + ", RecipientIdType=" + iasUserApplication.getRecipientIdType()
                + ", IasApplicationId=" + iasUserApplication.getIasApplicationId() + ", ServiceProviderId=" + iasUserApplication.getServiceProviderId());

            // Get the NOTI_ID from iasOpenIdNotiIdMap
            NotificationIdItem notificationIdItem = iasOpenIdNotiIdMap.get(new IasMsgRecipientKey(iasUserApplication.getClientId(), iasUserApplication.getRecipientId()));

            // Client ID, Open ID pair is valid
            if (notificationIdItem != null && notificationIdItem.getNotificationID() != null
                && notificationIdItem.getNotificationID().length() > 0) {

                logger.info("updateIasUserAppNotiIDByESOpenID - RecipientId=" + iasUserApplication.getRecipientId()
                    + ", notificationIdItem.getNotificationID=" + notificationIdItem.getNotificationID() + ", notificationIdItem.getStatus=" + notificationIdItem.getStatus());

                IasUser iasUser = iasNotiDAO.getIasEsNotiMap(conn, notificationIdItem.getNotificationID(), iasUserApplication.getServiceProviderId());

                // If IasEsNotiMap_ record not exist in DB, insert a new one
                if (iasUser == null) {

                    // Create IAS_ES_NOTI_MAP
                    IasEsNotiMap_ notiMap = new IasEsNotiMap_();
                    notiMap.setServiceProviderId(iasUserApplication.getServiceProviderId());
                    notiMap.setNotiId(notificationIdItem.getNotificationID());
                    notiMap.setOpenId(iasUserApplication.getRecipientId());

                    // If Transactional message case (i.e. IAS_OPT_CHECK_IND=N), default opt in value is U
                    // Input field for "ias_opt_check_ind" is not used because "ias_show_es_set_btn" derive the same meaning. So, check "ias_show_es_set_btn" while processing message requests.
                    if (!"Y".equals(iasUserApplication.getIasOptCheckInd())) { // getIasOptCheckInd() using the value of ias_show_es_set_btn in DB
                        notiMap.setOptIn(IntegrationConstants.OPT_IN_U);
                    } else {
                        if (IntegrationConstants.GET_NOTI_ID_RESULT_V2_DEREGISTERED.equals(notificationIdItem.getStatus()))
                            notiMap.setOptIn(IntegrationConstants.OPT_IN_U);
                        else
                            notiMap.setOptIn(IntegrationConstants.OPT_IN_Y);
                    }

                    notiMap.insert(conn);
                } else if (iasUser != null && iasUser.getOpenId() == null) {
                    // Update OpenID in IAS_ES_NOTI_MAP
                    IasEsNotiMap_ notiMap = new IasEsNotiMap_(conn, notificationIdItem.getNotificationID(), iasUserApplication.getServiceProviderId());
                    notiMap.setOpenId(iasUserApplication.getRecipientId());
                    if (IntegrationConstants.GET_NOTI_ID_RESULT_V2_DEREGISTERED.equals(notificationIdItem.getStatus())) {
                        notiMap.setOptIn(IntegrationConstants.OPT_IN_U);
                    } else {
                        notiMap.setOptIn(IntegrationConstants.OPT_IN_Y);
                    }
                    notiMap.update(conn);
                }

                // Update NOTI_ID for IAS_USER_APPLICATION
                IasUserApplication_ tempIasUserApplication = new IasUserApplication_(conn,  iasUserApplication.getClientId(), iasUserApplication.getRecipientId(), iasUserApplication.getIasApplicationId());
                tempIasUserApplication.setNotiId(notificationIdItem.getNotificationID());
                tempIasUserApplication.update(conn);

            } else { // Check if OPEN_ID invalid
                if (iasInvalidEsOpenIdList.contains(new IasMsgRecipientKey(iasUserApplication.getClientId(), iasUserApplication.getRecipientId()))) {
                    // Update IAS_NOTI_STATUS for IAS_USER_APPLICATION
                    IasUserApplication_ tempIasUserApplication = new IasUserApplication_(conn, iasUserApplication.getClientId(), iasUserApplication.getRecipientId(), iasUserApplication.getIasApplicationId());
                    tempIasUserApplication.setIasNotiStatus(IntegrationConstants.IAS_NOTI_STATUS_INVALID);
                    tempIasUserApplication.update(conn);

                    // For iAM Smart Undelivered Report 06D
                    logger.info("updateIasUserAppNotiIDByESOpenID - iasAppNotiDAO.createIasUndeliveredMessage (RECIPIENT_ID invalid)");
                    iasAppNotiDAO.createIasUndeliveredApplication(conn, iasUserApplication.getClientId(), iasUserApplication.getRecipientId(), 
                        iasUserApplication.getIasApplicationId(), iasUserApplication.getRecipientIdType(), IntegrationConstants.IAS_MSG_RCPT_TYPE_IAM_SMART,  
                        IasApplicationConstant.STATUS_UNDELIVERED_APPLICATION_NOT_INFORMED, IntegrationConstants.IAS_UNDELIVER_MSG_REASON_ACC_NOT_ACTIVE);
                }
            }
            conn.commit();
        }

        logger.debug("updateIasUserAppNotiIDByESOpenID - END");

        return deRegClientIdNotiIdPairList;
    }

}
