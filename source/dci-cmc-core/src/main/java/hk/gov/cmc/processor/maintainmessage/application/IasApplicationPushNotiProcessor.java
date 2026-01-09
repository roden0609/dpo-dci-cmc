package hk.gov.cmc.processor.maintainmessage.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import hk.gov.cmc.common.CmcAppPropertyNames;
import hk.gov.cmc.common.IasApplicationConstant;
import hk.gov.cmc.common.IntegrationConstants;
import hk.gov.cmc.common.RecipientIDTypeConstant;
import hk.gov.cmc.common.StatusConstants;
import hk.gov.cmc.config.CmcEnvProperties;
import hk.gov.cmc.dao.maintainmessage.notification.IasApplicationNotiDAO;
import hk.gov.cmc.eid.bean.EIDResponseBean;
import hk.gov.cmc.eid.bean.NotificationResultItem;
import hk.gov.cmc.eid.bean.NotificationResultsBean;
import hk.gov.cmc.eid.bean.pushNotification.request.PushNotificationBean;
import hk.gov.cmc.eid.bean.pushNotification.request.PushNotificationItem;
import hk.gov.cmc.eid.client.EIDUtils;
import hk.gov.cmc.model.maintainmessage.application.IasUserApplication;
import hk.gov.cmc.model.maintainmessage.key.IasClientIdNotiIdPair;
import hk.gov.cmc.persistence.connection.hpfw.HPFW_Connection;
import hk.gov.cmc.persistence.connection.hpfw.Parameter;
import hk.gov.cmc.persistence.ias.application.IasUserApplication_;

public class IasApplicationPushNotiProcessor {

    private static Log logger = LogFactory.getLog(IasApplicationPushNotiProcessor.class);
    private final static CmcEnvProperties cmcEnvProperties = new CmcEnvProperties();

    public IasApplicationPushNotiProcessor() {
    }

    public void pushIasApplicationToIAMSmart(HPFW_Connection conn, boolean isEIDUtilsInit,
            List<IasUserApplication> outstandingIasUserApplicationList,
            List<IasClientIdNotiIdPair> deregClientIdNotiIdPairList) throws Exception {

        Properties prop = cmcEnvProperties.getProperties();
        String iasPushNotiEndPoint = prop.getProperty(CmcAppPropertyNames.IAS_PUSH_NOTI_END_POINT);
        int iasPushNotiBatchLimit = Integer
                .parseInt(prop.getProperty(CmcAppPropertyNames.IAS_SEND_NOTI_BATCH_LIMIT_PROPERTY_NAME));

        IasApplicationNotiDAO iasAppNotiDAO = new IasApplicationNotiDAO();
        PushNotificationBean pushNotiBean = new PushNotificationBean();
        List<PushNotificationItem> pushNotiItemList = new ArrayList<PushNotificationItem>();

        List<String> notiIdList = new ArrayList<String>();
        int notiIdCount = 0;

        for (int i = 0; i < outstandingIasUserApplicationList.size(); i++) {

            IasUserApplication iasUserApplication = outstandingIasUserApplicationList.get(i);
            logger.debug("pushIasApplicationToIAMSmart - iasUserApplication: " + iasUserApplication.toString());

            // Check iAM Smart account status, If de-registered, mark delete_ind to Y and not sending this by push noti
            if (IntegrationConstants.IAS_USER_STATUS_DEREGISTERED.equals(iasUserApplication.getStatus()) ||
                    deregClientIdNotiIdPairList.contains(new IasClientIdNotiIdPair(iasUserApplication.getClientId(),
                            iasUserApplication.getNotiId()))) {

                // Update DELETE_IND to Y for IAS_USER_APPLICATION
                ArrayList<Parameter> paraSetList = new ArrayList<Parameter>();
                ArrayList<Parameter> paraWhereList = new ArrayList<Parameter>();
                paraSetList.add(new Parameter(Parameter.String, StatusConstants.DELETE_IND_DELETED));
                paraWhereList.add(new Parameter(Parameter.String, iasUserApplication.getNotiId()));

                IasUserApplication_.update(conn, "set DELETE_IND = ?", paraSetList,
                        "where NOTI_ID = ? and DELETE_IND != 'Y' ", paraWhereList);

                // For iAM Smart Undelivered Report 06D
                if (RecipientIDTypeConstant.HKID.equals(iasUserApplication.getRecipientIdType())) {
                    iasAppNotiDAO.createIasUndeliveredApplicationWithHKIdEncrypted(conn,
                            iasUserApplication.getClientId(), iasUserApplication.getRecipientId(),
                            iasUserApplication.getIasApplicationId(), iasUserApplication.getRecipientIdType(),
                            iasUserApplication.getHkidEncrypted(),
                            IntegrationConstants.IAS_MSG_RCPT_TYPE_IAM_SMART,
                            IasApplicationConstant.STATUS_UNDELIVERED_APPLICATION_NOT_INFORMED,
                            IntegrationConstants.IAS_UNDELIVER_MSG_REASON_DEREGISTERED);
                } else {
                    iasAppNotiDAO.createIasUndeliveredApplication(conn, iasUserApplication.getClientId(),
                            iasUserApplication.getRecipientId(),
                            iasUserApplication.getIasApplicationId(), iasUserApplication.getRecipientIdType(),
                            IntegrationConstants.IAS_MSG_RCPT_TYPE_IAM_SMART,
                            IasApplicationConstant.STATUS_UNDELIVERED_APPLICATION_NOT_INFORMED,
                            IntegrationConstants.IAS_UNDELIVER_MSG_REASON_DEREGISTERED);
                }

                conn.commit();

            } else {
                notiIdList.add(iasUserApplication.getNotiId());
                notiIdCount++;
            }

            // If Noti ID count reach API batch size limit or iasUserApplicationList reach the last object or next iasApplicationId is new
            logger.debug("pushIasApplicationToIAMSmart - new PushNotificationItem notiIdList.size: " + notiIdList.size()
                    + ", iasApplicationId: " + iasUserApplication.getIasApplicationId());
            if (notiIdList.size() > 0 &&
                    ((notiIdCount == iasPushNotiBatchLimit) ||
                            (i == outstandingIasUserApplicationList.size() - 1) ||
                            (!iasUserApplication.getIasApplicationId()
                                    .equals(outstandingIasUserApplicationList.get(i + 1).getIasApplicationId())))) {

                // For TC Title
                if (iasUserApplication.getTitleTc() == null || iasUserApplication.getTitleTc().length() == 0) {
                    iasUserApplication.setTitleTc(iasUserApplication.getTitleEn());
                }

                // For SC Title
                if (iasUserApplication.getTitleSc() == null || iasUserApplication.getTitleSc().length() == 0) {
                    if (iasUserApplication.getTitleTc() != null && iasUserApplication.getTitleTc().length() > 0) {
                        iasUserApplication.setTitleSc(iasUserApplication.getTitleTc());
                    } else {
                        iasUserApplication.setTitleSc(iasUserApplication.getTitleEn());
                    }
                }

                PushNotificationItem pushNotiItem = new PushNotificationItem(
                        new ArrayList<String>(notiIdList),
                        iasUserApplication.getServiceProviderId(),
                        IasApplicationConstant.MSG_TYPE_APPLICATION,
                        IasApplicationConstant.OPERATION_TYPE_UPDATE.equals(iasUserApplication.getOperationType())
                                ? IasApplicationConstant.OPERATION_TYPE_REPLACE
                                : iasUserApplication.getOperationType(),
                        Long.valueOf(iasUserApplication.getCreateDt().getTime()),
                        iasUserApplication.getIasApplicationId(),
                        iasUserApplication.getPrevIasApplicationId(),
                        iasUserApplication.getTitleEn(),
                        iasUserApplication.getTitleTc(),
                        iasUserApplication.getTitleSc(),
                        "{ \"status\": \"" + iasUserApplication.getAppStatus() + "\" }");

                logger.debug("pushIasApplicationToIAMSmart - pushNotiItem: " + pushNotiItem.toString());
                pushNotiItemList.add(pushNotiItem);
                notiIdList = new ArrayList<String>();
            }

            // If Noti ID count reach API batch size limit or iasUserApplicationList reach the last object
            logger.debug("pushIasApplicationToIAMSmart - notiBean.setNotifications pushNotiItemList.size: "
                    + pushNotiItemList.size());
            if (pushNotiItemList.size() > 0 &&
                    ((notiIdCount == iasPushNotiBatchLimit) || (i == (outstandingIasUserApplicationList.size() - 1)))) {
                pushNotiBean.setPushNotifications(new ArrayList<PushNotificationItem>(pushNotiItemList));

                if (!isEIDUtilsInit) {
                    EIDUtils.initialize(prop);
                }

                // Push iAM Smart Notification message
                EIDResponseBean pushNotiResp = EIDUtils.doRequestPushNotificationMessages(iasPushNotiEndPoint,
                        pushNotiBean);
                java.sql.Timestamp sentDt = new java.sql.Timestamp(new java.util.Date().getTime());

                if (pushNotiResp != null) {

                    logger.info("pushIasApplicationToIAMSmart - PushNoti Resp Content=" + pushNotiResp.getContent());

                    if (((IntegrationConstants.GET_NOTI_ID_RESULT_CODE_SUCCESS.equalsIgnoreCase(pushNotiResp.getCode()))
                            || (IntegrationConstants.GET_NOTI_ID_RESULT_CODE_PARTIAL_SUCCESS
                                    .equalsIgnoreCase(pushNotiResp.getCode())))
                            && pushNotiResp.getContent() != null && pushNotiResp.getContent().length() > 0) {
                        NotificationResultsBean notiResult = new NotificationResultsBean(pushNotiResp.getContent());
                        ArrayList<NotificationResultItem> notiResultList = notiResult.getNotificationResults();

                        // Loop the result
                        for (NotificationResultItem notiResultItem : notiResultList) {

                            // Update TX_ID, IAS_NOTI_STATUS of IAS_USER_APPLICATION
                            ArrayList<Parameter> paraSetList = new ArrayList<Parameter>();
                            ArrayList<Parameter> paraWhereList = new ArrayList<Parameter>();
                            paraSetList.add(new Parameter(Parameter.String, IntegrationConstants.IAS_NOTI_STATUS_SENT));
                            paraSetList.add(new Parameter(Parameter.String, notiResultItem.getStatus()));
                            paraSetList.add(new Parameter(Parameter.String, pushNotiResp.getTxID()));
                            paraSetList.add(new Parameter(Parameter.Timestamp, sentDt));
                            paraWhereList.add(new Parameter(Parameter.String, notiResultItem.getNotificationID()));
                            paraWhereList.add(new Parameter(Parameter.String, notiResultItem.getMessageID()));

                            IasUserApplication_.update(conn,
                                    "set IAS_NOTI_STATUS = ?, IAS_NOTI_RESULT = ?, TX_ID = ?, SENT_DT = ?", paraSetList,
                                    "where NOTI_ID = ? and IAS_APPLICATION_ID = ? ", paraWhereList);

                            if (!(IntegrationConstants.SEND_NOTI_ID_RESULT_READY_TO_SEND
                                    .equals(notiResultItem.getStatus())) &&
                                    !(IntegrationConstants.SEND_NOTI_ID_RESULT_SENT
                                            .equals(notiResultItem.getStatus()))) {

                                for (int j = 0; j < outstandingIasUserApplicationList.size(); j++) {
                                    IasUserApplication iua = outstandingIasUserApplicationList.get(j);
                                    if ((iua.getNotiId().equals(notiResultItem.getNotificationID())) &&
                                            (iua.getIasApplicationId().equals(notiResultItem.getMessageID()))) {
                                        // For iAM Smart Undelivered Report 06D
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

                                        if (RecipientIDTypeConstant.HKID.equals(iua.getRecipientIdType())) {
                                            iasAppNotiDAO.createIasUndeliveredApplicationWithHKIdEncrypted(conn,
                                                    iua.getClientId(), iua.getRecipientId(), iua.getIasApplicationId(),
                                                    iua.getRecipientIdType(),
                                                    iua.getHkidEncrypted(),
                                                    IntegrationConstants.IAS_MSG_RCPT_TYPE_IAM_SMART,
                                                    IasApplicationConstant.STATUS_UNDELIVERED_APPLICATION_NOT_INFORMED,
                                                    undeliverReason);
                                        } else {
                                            iasAppNotiDAO.createIasUndeliveredApplication(conn, iua.getClientId(),
                                                    iua.getRecipientId(), iua.getIasApplicationId(),
                                                    iua.getRecipientIdType(),
                                                    IntegrationConstants.IAS_MSG_RCPT_TYPE_IAM_SMART,
                                                    IasApplicationConstant.STATUS_UNDELIVERED_APPLICATION_NOT_INFORMED,
                                                    undeliverReason);
                                        }
                                    }
                                }
                            }
                        }
                        conn.commit();

                        notiIdCount = 0;
                        pushNotiItemList = new ArrayList<PushNotificationItem>();

                    } else {
                        logger.warn("pushIasApplicationToIAMSmart - PushNoti Fail. Code=" + pushNotiResp.getCode()
                                + ", Message=" + pushNotiResp.getMessage()
                                + ", TxID=" + pushNotiResp.getTxID() + ". No DB Change, will retry in next run.");
                        logger.debug(
                                "pushIasApplicationToIAMSmart - EIDUtils.doRequestPushNotificationMessages pushNotiBean.toString: "
                                        + pushNotiBean.toString());
                    }
                } else {
                    logger.warn(
                            "pushIasApplicationToIAMSmart - PushNoti Fail. pushNotiResp is null. No DB Change, will retry in next run.");
                }
            }
        }
    }

    public void pushIasApplicationToIAMSmartForDeletion(HPFW_Connection conn, boolean isEIDUtilsInit,
            List<IasUserApplication> outstandingIasUserApplicationList,
            List<IasClientIdNotiIdPair> deregClientIdNotiIdPairList) throws Exception {

        Properties prop = cmcEnvProperties.getProperties();
        String iasPushNotiEndPoint = prop.getProperty(CmcAppPropertyNames.IAS_PUSH_NOTI_END_POINT);
        int iasPushNotiBatchLimit = Integer
                .parseInt(prop.getProperty(CmcAppPropertyNames.IAS_SEND_NOTI_BATCH_LIMIT_PROPERTY_NAME));

        IasApplicationNotiDAO iasAppNotiDAO = new IasApplicationNotiDAO();
        PushNotificationBean pushNotiBean = new PushNotificationBean();
        List<PushNotificationItem> pushNotiItemList = new ArrayList<PushNotificationItem>();

        List<String> notiIdList = new ArrayList<String>();
        int notiIdCount = 0;

        for (int i = 0; i < outstandingIasUserApplicationList.size(); i++) {

            IasUserApplication iasUserApplication = outstandingIasUserApplicationList.get(i);
            logger.info(
                    "pushIasApplicationToIAMSmartForDeletion - iasUserApplication: " + iasUserApplication.toString());

            // Check iAM Smart account status, If de-registered, mark delete_ind to Y and not sending this by push noti
            if (IntegrationConstants.IAS_USER_STATUS_DEREGISTERED.equals(iasUserApplication.getStatus()) ||
                    deregClientIdNotiIdPairList.contains(new IasClientIdNotiIdPair(iasUserApplication.getClientId(),
                            iasUserApplication.getNotiId()))) {

                // Update DELETE_IND to Y for IAS_USER_APPLICATION
                ArrayList<Parameter> paraSetList = new ArrayList<Parameter>();
                ArrayList<Parameter> paraWhereList = new ArrayList<Parameter>();
                paraSetList.add(new Parameter(Parameter.String, StatusConstants.DELETE_IND_DELETED));
                paraWhereList.add(new Parameter(Parameter.String, iasUserApplication.getNotiId()));

                IasUserApplication_.update(conn, "set DELETE_IND = ?", paraSetList,
                        "where NOTI_ID = ? and DELETE_IND != 'Y' ", paraWhereList);

                // For iAM Smart Undelivered Report 06D
                if (RecipientIDTypeConstant.HKID.equals(iasUserApplication.getRecipientIdType())) {
                    iasAppNotiDAO.createIasUndeliveredApplicationWithHKIdEncrypted(conn,
                            iasUserApplication.getClientId(), iasUserApplication.getRecipientId(),
                            iasUserApplication.getIasApplicationId(), iasUserApplication.getRecipientIdType(),
                            iasUserApplication.getHkidEncrypted(),
                            IntegrationConstants.IAS_MSG_RCPT_TYPE_IAM_SMART,
                            IasApplicationConstant.STATUS_UNDELIVERED_APPLICATION_NOT_INFORMED,
                            IntegrationConstants.IAS_UNDELIVER_MSG_REASON_DEREGISTERED);
                } else {
                    iasAppNotiDAO.createIasUndeliveredApplication(conn, iasUserApplication.getClientId(),
                            iasUserApplication.getRecipientId(),
                            iasUserApplication.getIasApplicationId(), iasUserApplication.getRecipientIdType(),
                            IntegrationConstants.IAS_MSG_RCPT_TYPE_IAM_SMART,
                            IasApplicationConstant.STATUS_UNDELIVERED_APPLICATION_NOT_INFORMED,
                            IntegrationConstants.IAS_UNDELIVER_MSG_REASON_DEREGISTERED);
                }

                conn.commit();

            } else {

                // Check is the previous action successfully sent to iAM Smart with result code = 0 (ready to send) or 1 (sent)
                logger.info("pushIasApplicationToIAMSmartForDeletion - NotiId()=" + iasUserApplication.getNotiId() +
                        ", IasApplicationId()=" + iasUserApplication.getIasApplicationId() +
                        ", PrevIasApplicationId()=" + iasUserApplication.getPrevIasApplicationId() +
                        ", PrevIasNotiResult()=" + iasUserApplication.getPrevIasNotiResult());
                if (null == iasUserApplication.getPrevIasNotiResult() ||
                        IntegrationConstants.SEND_NOTI_ID_RESULT_READY_TO_SEND
                                .equals(iasUserApplication.getPrevIasNotiResult())
                        ||
                        IntegrationConstants.SEND_NOTI_ID_RESULT_SENT
                                .equals(iasUserApplication.getPrevIasNotiResult())) {

                    notiIdList.add(iasUserApplication.getNotiId());
                    notiIdCount++;
                } else {
                    logger.info(
                            "pushIasApplicationToIAMSmartForDeletion - DELET_IND still in pending(P), not mark to Y. " +
                                    "Because the previous action is not successfully sent to iAM Smart. iasUserApplication.getPrevIasNotiResult()="
                                    + iasUserApplication.getPrevIasNotiResult());
                }
                // notiIdList.add(iasUserApplication.getNotiId());
                // notiIdCount++;
            }

            // If Noti ID count reach API batch size limit or iasUserApplicationList reach the last object or next iasApplicationId is new
            logger.debug("pushIasApplicationToIAMSmartForDeletion - new PushNotificationItem notiIdList.size: "
                    + notiIdList.size() + ", iasApplicationId: " + iasUserApplication.getIasApplicationId());
            if (notiIdList.size() > 0 &&
                    ((notiIdCount == iasPushNotiBatchLimit) ||
                            (i == outstandingIasUserApplicationList.size() - 1) ||
                            (!iasUserApplication.getIasApplicationId()
                                    .equals(outstandingIasUserApplicationList.get(i + 1).getIasApplicationId())))) {

                PushNotificationItem pushNotiItem = new PushNotificationItem(
                        new ArrayList<String>(notiIdList),
                        iasUserApplication.getServiceProviderId(),
                        IasApplicationConstant.MSG_TYPE_APPLICATION,
                        IasApplicationConstant.OPERATION_TYPE_DELETE,
                        null,
                        iasUserApplication.getIasApplicationId(),
                        iasUserApplication.getPrevIasApplicationId(),
                        null,
                        null,
                        null,
                        null);

                pushNotiItemList.add(pushNotiItem);
                notiIdList = new ArrayList<String>();
            }

            // If Noti ID count reach API batch size limit or iasUserApplicationList reach the last object
            logger.debug("pushIasApplicationToIAMSmartForDeletion - notiBean.setNotifications pushNotiItemList.size: "
                    + pushNotiItemList.size());
            if (pushNotiItemList.size() > 0 &&
                    ((notiIdCount == iasPushNotiBatchLimit) || (i == (outstandingIasUserApplicationList.size() - 1)))) {
                pushNotiBean.setPushNotifications(new ArrayList<PushNotificationItem>(pushNotiItemList));

                if (!isEIDUtilsInit) {
                    EIDUtils.initialize(prop);
                }

                // Push iAM Smart Notification message
                EIDResponseBean pushNotiResp = EIDUtils.doRequestPushNotificationMessages(iasPushNotiEndPoint,
                        pushNotiBean);
                java.sql.Timestamp sentDt = new java.sql.Timestamp(new java.util.Date().getTime());

                if (pushNotiResp != null) {

                    logger.debug("pushIasApplicationToIAMSmartForDeletion - PushNoti Resp Content="
                            + pushNotiResp.getContent());

                    if (((IntegrationConstants.GET_NOTI_ID_RESULT_CODE_SUCCESS.equalsIgnoreCase(pushNotiResp.getCode()))
                            || (IntegrationConstants.GET_NOTI_ID_RESULT_CODE_PARTIAL_SUCCESS
                                    .equalsIgnoreCase(pushNotiResp.getCode())))
                            && pushNotiResp.getContent() != null && pushNotiResp.getContent().length() > 0) {
                        NotificationResultsBean notiResult = new NotificationResultsBean(pushNotiResp.getContent());
                        ArrayList<NotificationResultItem> notiResultList = notiResult.getNotificationResults();

                        // Loop the result
                        for (NotificationResultItem notiResultItem : notiResultList) {

                            // Update TX_ID, IAS_NOTI_STATUS of IAS_USER_APPLICATION
                            ArrayList<Parameter> paraSetList = new ArrayList<Parameter>();
                            ArrayList<Parameter> paraWhereList = new ArrayList<Parameter>();
                            paraSetList.add(new Parameter(Parameter.String, StatusConstants.DELETE_IND_DELETED));
                            paraSetList.add(new Parameter(Parameter.String, IntegrationConstants.IAS_NOTI_STATUS_SENT));
                            paraSetList.add(new Parameter(Parameter.String, notiResultItem.getStatus()));
                            paraSetList.add(new Parameter(Parameter.String, pushNotiResp.getTxID()));
                            paraSetList.add(new Parameter(Parameter.Timestamp, sentDt));
                            paraWhereList.add(new Parameter(Parameter.String, notiResultItem.getNotificationID()));
                            paraWhereList.add(new Parameter(Parameter.String, notiResultItem.getMessageID()));

                            IasUserApplication_.update(conn,
                                    "set DELETE_IND = ?, IAS_DELETE_NOTI_STATUS = ?, IAS_DELETE_NOTI_RESULT = ?, IAS_DELETE_NOTI_TX_ID = ?, IAS_DELETE_NOTI_SENT_DT = ?",
                                    paraSetList,
                                    "where NOTI_ID = ? and IAS_APPLICATION_ID = ? ", paraWhereList);

                            if (!(IntegrationConstants.SEND_NOTI_ID_RESULT_READY_TO_SEND
                                    .equals(notiResultItem.getStatus())) &&
                                    !(IntegrationConstants.SEND_NOTI_ID_RESULT_SENT
                                            .equals(notiResultItem.getStatus()))) {

                                for (int j = 0; j < outstandingIasUserApplicationList.size(); j++) {
                                    IasUserApplication iua = outstandingIasUserApplicationList.get(j);
                                    if ((iua.getNotiId().equals(notiResultItem.getNotificationID())) &&
                                            (iua.getIasApplicationId().equals(notiResultItem.getMessageID()))) {
                                        // For iAM Smart Undelivered Report 06D
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

                                        if (RecipientIDTypeConstant.HKID.equals(iua.getRecipientIdType())) {
                                            iasAppNotiDAO.createIasUndeliveredApplicationWithHKIdEncrypted(conn,
                                                    iua.getClientId(), iua.getRecipientId(), iua.getIasApplicationId(),
                                                    iua.getRecipientIdType(),
                                                    iua.getHkidEncrypted(),
                                                    IntegrationConstants.IAS_MSG_RCPT_TYPE_IAM_SMART,
                                                    IasApplicationConstant.STATUS_UNDELIVERED_APPLICATION_NOT_INFORMED,
                                                    undeliverReason);
                                        } else {
                                            iasAppNotiDAO.createIasUndeliveredApplication(conn, iua.getClientId(),
                                                    iua.getRecipientId(), iua.getIasApplicationId(),
                                                    iua.getRecipientIdType(),
                                                    IntegrationConstants.IAS_MSG_RCPT_TYPE_IAM_SMART,
                                                    IasApplicationConstant.STATUS_UNDELIVERED_APPLICATION_NOT_INFORMED,
                                                    undeliverReason);
                                        }
                                    }
                                }
                            }
                        }
                        conn.commit();

                        notiIdCount = 0;
                        pushNotiItemList = new ArrayList<PushNotificationItem>();

                    } else {
                        logger.warn("pushIasApplicationToIAMSmartForDeletion - PushNoti Fail. Code="
                                + pushNotiResp.getCode() + ", Message=" + pushNotiResp.getMessage()
                                + ", TxID=" + pushNotiResp.getTxID() + ". No DB Change, will retry in next run.");
                        logger.debug(
                                "pushIasApplicationToIAMSmartForDeletion - EIDUtils.doRequestPushNotificationMessages pushNotiBean.toString: "
                                        + pushNotiBean.toString());
                    }
                } else {
                    logger.warn(
                            "pushIasApplicationToIAMSmartForDeletion - PushNoti Fail. pushNotiResp is null. No DB Change, will retry in next run.");
                }
            }
        }
    }

}
