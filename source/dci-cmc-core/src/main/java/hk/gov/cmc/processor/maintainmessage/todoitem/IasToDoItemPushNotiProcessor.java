package hk.gov.cmc.processor.maintainmessage.todoitem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import hk.gov.cmc.common.CmcAppPropertyNames;
import hk.gov.cmc.common.IasApplicationConstant;
import hk.gov.cmc.common.IasToDoItemConstant;
import hk.gov.cmc.common.IntegrationConstants;
import hk.gov.cmc.common.RecipientIDTypeConstant;
import hk.gov.cmc.common.StatusConstants;
import hk.gov.cmc.config.CmcEnvProperties;
import hk.gov.cmc.dao.maintainmessage.notification.IasToDoItemNotiDAO;
import hk.gov.cmc.eid.bean.EIDResponseBean;
import hk.gov.cmc.eid.bean.NotificationResultItem;
import hk.gov.cmc.eid.bean.NotificationResultsBean;
import hk.gov.cmc.eid.bean.pushNotification.request.PushNotificationBean;
import hk.gov.cmc.eid.bean.pushNotification.request.PushNotificationItem;
import hk.gov.cmc.eid.client.EIDUtils;
import hk.gov.cmc.model.maintainmessage.key.IasClientIdNotiIdPair;
import hk.gov.cmc.model.maintainmessage.todoitem.IasUserToDoItem;
import hk.gov.cmc.persistence.connection.hpfw.HPFW_Connection;
import hk.gov.cmc.persistence.connection.hpfw.Parameter;
import hk.gov.cmc.persistence.maintainmessage.todoitem.IasUserToDoItem_;

public class IasToDoItemPushNotiProcessor {

    private static Log logger = LogFactory.getLog(IasToDoItemPushNotiProcessor.class);
    private final static CmcEnvProperties cmcEnvProperties = new CmcEnvProperties();

    public IasToDoItemPushNotiProcessor() {
    }

    public void pushIasToDoItemToIAMSmart(HPFW_Connection conn, boolean isEIDUtilsInit,
            List<IasUserToDoItem> outstandingIasUserToDoItemList,
            List<IasClientIdNotiIdPair> deregClientIdNotiIdPairList) throws Exception {

        Properties prop = cmcEnvProperties.getProperties();
        String iasPushNotiEndPoint = prop.getProperty(CmcAppPropertyNames.IAS_PUSH_NOTI_END_POINT);
        int iasPushNotiBatchLimit = Integer
                .parseInt(prop.getProperty(CmcAppPropertyNames.IAS_SEND_NOTI_BATCH_LIMIT_PROPERTY_NAME));

        IasToDoItemNotiDAO iasAppNotiDAO = new IasToDoItemNotiDAO();
        PushNotificationBean pushNotiBean = new PushNotificationBean();
        List<PushNotificationItem> pushNotiItemList = new ArrayList<PushNotificationItem>();

        List<String> notiIdList = new ArrayList<String>();
        int notiIdCount = 0;

        for (int i = 0; i < outstandingIasUserToDoItemList.size(); i++) {

            IasUserToDoItem iasUserToDoItem = outstandingIasUserToDoItemList.get(i);

            // Check iAM Smart account status, If de-registered, mark delete_ind to Y and not sending this by push noti
            if (IntegrationConstants.IAS_USER_STATUS_DEREGISTERED.equals(iasUserToDoItem.getStatus()) ||
                    deregClientIdNotiIdPairList.contains(
                            new IasClientIdNotiIdPair(iasUserToDoItem.getClientId(), iasUserToDoItem.getNotiId()))) {

                // Update DELETE_IND to Y for IAS_USER_TO_DO_ITEM
                ArrayList<Parameter> paraSetList = new ArrayList<Parameter>();
                ArrayList<Parameter> paraWhereList = new ArrayList<Parameter>();
                paraSetList.add(new Parameter(Parameter.String, StatusConstants.DELETE_IND_DELETED));
                paraWhereList.add(new Parameter(Parameter.String, iasUserToDoItem.getNotiId()));

                IasUserToDoItem_.update(conn, "set DELETE_IND = ?", paraSetList,
                        "where NOTI_ID = ? and DELETE_IND != 'Y' ", paraWhereList);

                // For iAM Smart Undelivered Report 06D
                if (RecipientIDTypeConstant.HKID.equals(iasUserToDoItem.getRecipientIdType())) {
                    iasAppNotiDAO.createIasUndeliveredToDoItemWithHKIdEncrypted(conn, iasUserToDoItem.getClientId(),
                            iasUserToDoItem.getRecipientId(),
                            iasUserToDoItem.getIasToDoItemId(), iasUserToDoItem.getRecipientIdType(),
                            iasUserToDoItem.getHkidEncrypted(),
                            IntegrationConstants.IAS_MSG_RCPT_TYPE_IAM_SMART,
                            IasToDoItemConstant.STATUS_UNDELIVERED_TO_DO_ITEM_NOT_INFORMED,
                            IntegrationConstants.IAS_UNDELIVER_MSG_REASON_DEREGISTERED);
                } else {
                    iasAppNotiDAO.createIasUndeliveredToDoItem(conn, iasUserToDoItem.getClientId(),
                            iasUserToDoItem.getRecipientId(),
                            iasUserToDoItem.getIasToDoItemId(), iasUserToDoItem.getRecipientIdType(),
                            IntegrationConstants.IAS_MSG_RCPT_TYPE_IAM_SMART,
                            IasToDoItemConstant.STATUS_UNDELIVERED_TO_DO_ITEM_NOT_INFORMED,
                            IntegrationConstants.IAS_UNDELIVER_MSG_REASON_DEREGISTERED);
                }

                conn.commit();

            } else {
                notiIdList.add(iasUserToDoItem.getNotiId());
                notiIdCount++;
            }

            // If Noti ID count reach API batch size limit or iasUserToDoItemList reach the last object or next iasToDoItemId is new
            logger.debug("pushIasToDoItemToIAMSmart - new PushNotificationItem notiIdList.size: " + notiIdList.size()
                    + ", iasToDoItemId: " + iasUserToDoItem.getIasToDoItemId());
            if (notiIdList.size() > 0 &&
                    ((notiIdCount == iasPushNotiBatchLimit) ||
                            (i == outstandingIasUserToDoItemList.size() - 1) ||
                            (!iasUserToDoItem.getIasToDoItemId()
                                    .equals(outstandingIasUserToDoItemList.get(i + 1).getIasToDoItemId())))) {

                // For TC Title
                if (iasUserToDoItem.getTitleTc() == null || iasUserToDoItem.getTitleTc().length() == 0) {
                    iasUserToDoItem.setTitleTc(iasUserToDoItem.getTitleEn());
                }

                // For SC Title
                if (iasUserToDoItem.getTitleSc() == null || iasUserToDoItem.getTitleSc().length() == 0) {
                    if (iasUserToDoItem.getTitleTc() != null && iasUserToDoItem.getTitleTc().length() > 0) {
                        iasUserToDoItem.setTitleSc(iasUserToDoItem.getTitleTc());
                    } else {
                        iasUserToDoItem.setTitleSc(iasUserToDoItem.getTitleEn());
                    }
                }

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                PushNotificationItem pushNotiItem = new PushNotificationItem(
                        new ArrayList<String>(notiIdList),
                        iasUserToDoItem.getServiceProviderId(),
                        IasToDoItemConstant.MSG_TYPE_APPLICATION,
                        iasUserToDoItem.getOperationType(),
                        Long.valueOf(iasUserToDoItem.getCreateDt().getTime()),
                        iasUserToDoItem.getIasToDoItemId(),
                        iasUserToDoItem.getPrevIasToDoItemId(),
                        iasUserToDoItem.getTitleEn(),
                        iasUserToDoItem.getTitleTc(),
                        iasUserToDoItem.getTitleSc(),
                        "{ \"dueDate\": \"" + Long.valueOf(sdf.parse(iasUserToDoItem.getItemDate()).getTime())
                                + "\" }");

                pushNotiItemList.add(pushNotiItem);
                notiIdList = new ArrayList<String>();
            }

            // If Noti ID count reach API batch size limit or iasUserToDoItemList reach the last object
            logger.debug("pushIasToDoItemToIAMSmart - notiBean.setNotifications pushNotiItemList.size: "
                    + pushNotiItemList.size());
            if (pushNotiItemList.size() > 0 &&
                    ((notiIdCount == iasPushNotiBatchLimit) || (i == (outstandingIasUserToDoItemList.size() - 1)))) {
                pushNotiBean.setPushNotifications(new ArrayList<PushNotificationItem>(pushNotiItemList));

                if (!isEIDUtilsInit) {
                    EIDUtils.initialize(prop);
                }

                // Push iAM Smart Notification message
                EIDResponseBean pushNotiResp = EIDUtils.doRequestPushNotificationMessages(iasPushNotiEndPoint,
                        pushNotiBean);
                java.sql.Timestamp sentDt = new java.sql.Timestamp(new java.util.Date().getTime());

                if (pushNotiResp != null) {

                    logger.debug("pushIasToDoItemToIAMSmart - PushNoti Resp Content=" + pushNotiResp.getContent());

                    if (((IntegrationConstants.GET_NOTI_ID_RESULT_CODE_SUCCESS.equalsIgnoreCase(pushNotiResp.getCode()))
                            || (IntegrationConstants.GET_NOTI_ID_RESULT_CODE_PARTIAL_SUCCESS
                                    .equalsIgnoreCase(pushNotiResp.getCode())))
                            && pushNotiResp.getContent() != null && pushNotiResp.getContent().length() > 0) {
                        NotificationResultsBean notiResult = new NotificationResultsBean(pushNotiResp.getContent());
                        ArrayList<NotificationResultItem> notiResultList = notiResult.getNotificationResults();

                        // Loop the result
                        for (NotificationResultItem notiResultItem : notiResultList) {

                            // Update TX_ID, IAS_NOTI_STATUS of IAS_USER_TO_DO_ITEM
                            ArrayList<Parameter> paraSetList = new ArrayList<Parameter>();
                            ArrayList<Parameter> paraWhereList = new ArrayList<Parameter>();
                            paraSetList.add(new Parameter(Parameter.String, IntegrationConstants.IAS_NOTI_STATUS_SENT));
                            paraSetList.add(new Parameter(Parameter.String, notiResultItem.getStatus()));
                            paraSetList.add(new Parameter(Parameter.String, pushNotiResp.getTxID()));
                            paraSetList.add(new Parameter(Parameter.Timestamp, sentDt));
                            paraWhereList.add(new Parameter(Parameter.String, notiResultItem.getNotificationID()));
                            paraWhereList.add(new Parameter(Parameter.String, notiResultItem.getMessageID()));

                            IasUserToDoItem_.update(conn,
                                    "set IAS_NOTI_STATUS = ?, IAS_NOTI_RESULT = ?, TX_ID = ?, SENT_DT = ?", paraSetList,
                                    "where NOTI_ID = ? and IAS_TO_DO_ITEM_ID = ? ", paraWhereList);

                            if (!(IntegrationConstants.SEND_NOTI_ID_RESULT_READY_TO_SEND
                                    .equals(notiResultItem.getStatus())) &&
                                    !(IntegrationConstants.SEND_NOTI_ID_RESULT_SENT
                                            .equals(notiResultItem.getStatus()))) {

                                for (int j = 0; j < outstandingIasUserToDoItemList.size(); j++) {
                                    IasUserToDoItem iutd = outstandingIasUserToDoItemList.get(j);
                                    if ((iutd.getNotiId().equals(notiResultItem.getNotificationID())) &&
                                            (iutd.getIasToDoItemId().equals(notiResultItem.getMessageID()))) {
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

                                        if (RecipientIDTypeConstant.HKID.equals(iutd.getRecipientIdType())) {
                                            iasAppNotiDAO.createIasUndeliveredToDoItemWithHKIdEncrypted(conn,
                                                    iutd.getClientId(), iutd.getRecipientId(), iutd.getIasToDoItemId(),
                                                    iutd.getRecipientIdType(),
                                                    iutd.getHkidEncrypted(),
                                                    IntegrationConstants.IAS_MSG_RCPT_TYPE_IAM_SMART,
                                                    IasToDoItemConstant.STATUS_UNDELIVERED_TO_DO_ITEM_NOT_INFORMED,
                                                    undeliverReason);
                                        } else {
                                            iasAppNotiDAO.createIasUndeliveredToDoItem(conn, iutd.getClientId(),
                                                    iutd.getRecipientId(), iutd.getIasToDoItemId(),
                                                    iutd.getRecipientIdType(),
                                                    IntegrationConstants.IAS_MSG_RCPT_TYPE_IAM_SMART,
                                                    IasToDoItemConstant.STATUS_UNDELIVERED_TO_DO_ITEM_NOT_INFORMED,
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
                        logger.warn("pushIasToDoItemToIAMSmart - PushNoti Fail. Code=" + pushNotiResp.getCode()
                                + ", Message=" + pushNotiResp.getMessage()
                                + ", TxID=" + pushNotiResp.getTxID() + ". No DB Change, will retry in next run.");
                        logger.debug(
                                "pushIasToDoItemToIAMSmart - EIDUtils.doRequestPushNotificationMessages pushNotiBean.toString: "
                                        + pushNotiBean.toString());
                    }
                } else {
                    logger.warn(
                            "pushIasToDoItemToIAMSmart - PushNoti Fail. pushNotiResp is null. No DB Change, will retry in next run.");
                }
            }
        }
    }

    public void pushIasToDoItemToIAMSmartForDeletion(HPFW_Connection conn, boolean isEIDUtilsInit,
            List<IasUserToDoItem> outstandingIasUserToDoItemList,
            List<IasClientIdNotiIdPair> deregClientIdNotiIdPairList) throws Exception {

        Properties prop = cmcEnvProperties.getProperties();
        String iasPushNotiEndPoint = prop.getProperty(CmcAppPropertyNames.IAS_PUSH_NOTI_END_POINT);
        int iasPushNotiBatchLimit = Integer
                .parseInt(prop.getProperty(CmcAppPropertyNames.IAS_SEND_NOTI_BATCH_LIMIT_PROPERTY_NAME));

        IasToDoItemNotiDAO iasAppNotiDAO = new IasToDoItemNotiDAO();
        PushNotificationBean pushNotiBean = new PushNotificationBean();
        List<PushNotificationItem> pushNotiItemList = new ArrayList<PushNotificationItem>();

        List<String> notiIdList = new ArrayList<String>();
        int notiIdCount = 0;

        for (int i = 0; i < outstandingIasUserToDoItemList.size(); i++) {

            IasUserToDoItem iasUserToDoItem = outstandingIasUserToDoItemList.get(i);

            // Check iAM Smart account status, If de-registered, mark delete_ind to Y and not sending this by push noti
            if (IntegrationConstants.IAS_USER_STATUS_DEREGISTERED.equals(iasUserToDoItem.getStatus()) ||
                    deregClientIdNotiIdPairList.contains(
                            new IasClientIdNotiIdPair(iasUserToDoItem.getClientId(), iasUserToDoItem.getNotiId()))) {

                // Update DELETE_IND to Y for IAS_USER_TO_DO_ITEM
                ArrayList<Parameter> paraSetList = new ArrayList<Parameter>();
                ArrayList<Parameter> paraWhereList = new ArrayList<Parameter>();
                paraSetList.add(new Parameter(Parameter.String, StatusConstants.DELETE_IND_DELETED));
                paraWhereList.add(new Parameter(Parameter.String, iasUserToDoItem.getNotiId()));

                IasUserToDoItem_.update(conn, "set DELETE_IND = ?", paraSetList,
                        "where NOTI_ID = ? and DELETE_IND != 'Y' ", paraWhereList);

                // For iAM Smart Undelivered Report 06D
                if (RecipientIDTypeConstant.HKID.equals(iasUserToDoItem.getRecipientIdType())) {
                    iasAppNotiDAO.createIasUndeliveredToDoItemWithHKIdEncrypted(conn, iasUserToDoItem.getClientId(),
                            iasUserToDoItem.getRecipientId(),
                            iasUserToDoItem.getIasToDoItemId(), iasUserToDoItem.getRecipientIdType(),
                            iasUserToDoItem.getHkidEncrypted(),
                            IntegrationConstants.IAS_MSG_RCPT_TYPE_IAM_SMART,
                            IasToDoItemConstant.STATUS_UNDELIVERED_TO_DO_ITEM_NOT_INFORMED,
                            IntegrationConstants.IAS_UNDELIVER_MSG_REASON_DEREGISTERED);
                } else {
                    iasAppNotiDAO.createIasUndeliveredToDoItem(conn, iasUserToDoItem.getClientId(),
                            iasUserToDoItem.getRecipientId(),
                            iasUserToDoItem.getIasToDoItemId(), iasUserToDoItem.getRecipientIdType(),
                            IntegrationConstants.IAS_MSG_RCPT_TYPE_IAM_SMART,
                            IasToDoItemConstant.STATUS_UNDELIVERED_TO_DO_ITEM_NOT_INFORMED,
                            IntegrationConstants.IAS_UNDELIVER_MSG_REASON_DEREGISTERED);
                }

                conn.commit();

            } else {
                notiIdList.add(iasUserToDoItem.getNotiId());
                notiIdCount++;
            }

            // If Noti ID count reach API batch size limit or iasUserToDoItemList reach the last object or next iasToDoItemId is new
            logger.debug("pushIasToDoItemToIAMSmartForDeletion - new PushNotificationItem notiIdList.size: "
                    + notiIdList.size() + ", iasToDoItemId: " + iasUserToDoItem.getIasToDoItemId());
            if (notiIdList.size() > 0 &&
                    ((notiIdCount == iasPushNotiBatchLimit) ||
                            (i == outstandingIasUserToDoItemList.size() - 1) ||
                            (!iasUserToDoItem.getIasToDoItemId()
                                    .equals(outstandingIasUserToDoItemList.get(i + 1).getIasToDoItemId())))) {

                PushNotificationItem pushNotiItem = new PushNotificationItem(
                        new ArrayList<String>(notiIdList),
                        iasUserToDoItem.getServiceProviderId(),
                        IasToDoItemConstant.MSG_TYPE_APPLICATION,
                        IasApplicationConstant.OPERATION_TYPE_DELETE,
                        null,
                        iasUserToDoItem.getIasToDoItemId(),
                        iasUserToDoItem.getPrevIasToDoItemId(),
                        null,
                        null,
                        null,
                        null);

                pushNotiItemList.add(pushNotiItem);
                notiIdList = new ArrayList<String>();
            }

            // If Noti ID count reach API batch size limit or iasUserToDoItemList reach the last object
            logger.debug("pushIasToDoItemToIAMSmartForDeletion - notiBean.setNotifications pushNotiItemList.size: "
                    + pushNotiItemList.size());
            if (pushNotiItemList.size() > 0 &&
                    ((notiIdCount == iasPushNotiBatchLimit) || (i == (outstandingIasUserToDoItemList.size() - 1)))) {
                pushNotiBean.setPushNotifications(new ArrayList<PushNotificationItem>(pushNotiItemList));

                if (!isEIDUtilsInit) {
                    EIDUtils.initialize(prop);
                }

                // Push iAM Smart Notification message
                EIDResponseBean pushNotiResp = EIDUtils.doRequestPushNotificationMessages(iasPushNotiEndPoint,
                        pushNotiBean);
                java.sql.Timestamp sentDt = new java.sql.Timestamp(new java.util.Date().getTime());

                if (pushNotiResp != null) {

                    logger.debug("pushIasToDoItemToIAMSmartForDeletion - PushNoti Resp Content="
                            + pushNotiResp.getContent());

                    if (((IntegrationConstants.GET_NOTI_ID_RESULT_CODE_SUCCESS.equalsIgnoreCase(pushNotiResp.getCode()))
                            || (IntegrationConstants.GET_NOTI_ID_RESULT_CODE_PARTIAL_SUCCESS
                                    .equalsIgnoreCase(pushNotiResp.getCode())))
                            && pushNotiResp.getContent() != null && pushNotiResp.getContent().length() > 0) {
                        NotificationResultsBean notiResult = new NotificationResultsBean(pushNotiResp.getContent());
                        ArrayList<NotificationResultItem> notiResultList = notiResult.getNotificationResults();

                        // Loop the result
                        for (NotificationResultItem notiResultItem : notiResultList) {

                            // Update TX_ID, IAS_NOTI_STATUS of IAS_USER_TO_DO_ITEM
                            ArrayList<Parameter> paraSetList = new ArrayList<Parameter>();
                            ArrayList<Parameter> paraWhereList = new ArrayList<Parameter>();
                            paraSetList.add(new Parameter(Parameter.String, StatusConstants.DELETE_IND_DELETED));
                            paraSetList.add(new Parameter(Parameter.String, IntegrationConstants.IAS_NOTI_STATUS_SENT));
                            paraSetList.add(new Parameter(Parameter.String, notiResultItem.getStatus()));
                            paraSetList.add(new Parameter(Parameter.String, pushNotiResp.getTxID()));
                            paraSetList.add(new Parameter(Parameter.Timestamp, sentDt));
                            paraWhereList.add(new Parameter(Parameter.String, notiResultItem.getNotificationID()));
                            paraWhereList.add(new Parameter(Parameter.String, notiResultItem.getMessageID()));

                            IasUserToDoItem_.update(conn,
                                    "set DELETE_IND = ?, IAS_DELETE_NOTI_STATUS = ?, IAS_DELETE_NOTI_RESULT = ?, IAS_DELETE_NOTI_TX_ID = ?, IAS_DELETE_NOTI_SENT_DT = ?",
                                    paraSetList,
                                    "where NOTI_ID = ? and IAS_TO_DO_ITEM_ID = ? ", paraWhereList);

                            if (!(IntegrationConstants.SEND_NOTI_ID_RESULT_READY_TO_SEND
                                    .equals(notiResultItem.getStatus())) &&
                                    !(IntegrationConstants.SEND_NOTI_ID_RESULT_SENT
                                            .equals(notiResultItem.getStatus()))) {

                                for (int j = 0; j < outstandingIasUserToDoItemList.size(); j++) {
                                    IasUserToDoItem iutd = outstandingIasUserToDoItemList.get(j);
                                    if ((iutd.getNotiId().equals(notiResultItem.getNotificationID())) &&
                                            (iutd.getIasToDoItemId().equals(notiResultItem.getMessageID()))) {
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

                                        if (RecipientIDTypeConstant.HKID.equals(iutd.getRecipientIdType())) {
                                            iasAppNotiDAO.createIasUndeliveredToDoItemWithHKIdEncrypted(conn,
                                                    iutd.getClientId(), iutd.getRecipientId(), iutd.getIasToDoItemId(),
                                                    iutd.getRecipientIdType(),
                                                    iutd.getHkidEncrypted(),
                                                    IntegrationConstants.IAS_MSG_RCPT_TYPE_IAM_SMART,
                                                    IasToDoItemConstant.STATUS_UNDELIVERED_TO_DO_ITEM_NOT_INFORMED,
                                                    undeliverReason);
                                        } else {
                                            iasAppNotiDAO.createIasUndeliveredToDoItem(conn, iutd.getClientId(),
                                                    iutd.getRecipientId(), iutd.getIasToDoItemId(),
                                                    iutd.getRecipientIdType(),
                                                    IntegrationConstants.IAS_MSG_RCPT_TYPE_IAM_SMART,
                                                    IasToDoItemConstant.STATUS_UNDELIVERED_TO_DO_ITEM_NOT_INFORMED,
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
                        logger.warn("pushIasToDoItemToIAMSmartForDeletion - PushNoti Fail. Code="
                                + pushNotiResp.getCode() + ", Message=" + pushNotiResp.getMessage()
                                + ", TxID=" + pushNotiResp.getTxID() + ". No DB Change, will retry in next run.");
                        logger.debug(
                                "pushIasToDoItemToIAMSmartForDeletion - EIDUtils.doRequestPushNotificationMessages pushNotiBean.toString: "
                                        + pushNotiBean.toString());
                    }
                } else {
                    logger.warn(
                            "pushIasToDoItemToIAMSmartForDeletion - PushNoti Fail. pushNotiResp is null. No DB Change, will retry in next run.");
                }
            }
        }
    }

}
