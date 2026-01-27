package hk.gov.cmc.appserver.ejb.session.iasmessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import hk.gov.cmc.common.CmcAppConstants;
import hk.gov.cmc.common.CmcAppPropertyNames;
import hk.gov.cmc.common.IntegrationConstants;
import hk.gov.cmc.config.CmcEnvProperties;
import hk.gov.cmc.config.CmcSystemParam;
import hk.gov.cmc.dao.asynmessage.AsynMessageDAO;
import hk.gov.cmc.dao.maintainmessage.CmcServiceProviderDAO;
import hk.gov.cmc.jaxb.iasmsgsetting.IasAssoQueue;
import hk.gov.cmc.jaxb.iasmsgsetting.IasAssoQueues;
import hk.gov.cmc.persistence.connection.hpfw.HPFW_Connection;
import hk.gov.cmc.persistence.connection.hpfw.MysqlCommonDBUtils;
import hk.gov.cmc.persistence.maintainmessage.CmcServiceProvider_;
import hk.gov.cmc.persistence.maintainmessage.association.IasAssoQueue_;
import hk.gov.cmc.utils.common.WebServiceUtils;
import hk.gov.cmc.utils.job.JobControlUtils;
import hk.gov.gcis.rm.common.javaee.ejb.EJBBase;
import hk.gov.gcis.rm.common.utils.PropertiesUtils;
import hk.gov.gcis.ss.common.utils.SoapUtils;
import hk.gov.gcis.ss.common.webserver.elements.FaultEntry;
import hk.gov.gcis.ss.messaging.client.MessagingClient;
import hk.gov.gcis.ss.messaging.types.IMessagingConstants;
import hk.gov.gcis.ss.messaging.webserver.elements.asyncmessaging.AsyncRequest;
import jakarta.ejb.EJBException;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionManagement;
import jakarta.ejb.TransactionManagementType;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.soap.SOAPMessage;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class IasMsgSettingSessionEJB extends EJBBase
        implements IIasMsgSettingSessionBMRemote, IIasMsgSettingSessionBMLocal {

    private final static CmcEnvProperties cmcEnvProperties = new CmcEnvProperties();

    public void processMessageByBatchPull(String getAsynAppIdStr, String singlePullCallLimitStr) throws EJBException {
        logInfo("[IasMsgSettingSessionEJB]processMessageByBatchPull - START");

        HPFW_Connection conn = null;
        JobControlUtils jobControlUtils = new JobControlUtils();
        String jobControlName = "";

        try {

            Properties properties = cmcEnvProperties.getProperties();

            conn = HPFW_Connection.getHPFW_Connection();
            conn.begin(null, conn.getLastUpTime(), HPFW_Connection.DIRECT_WITH_HISTORY);
            conn.setAutoCommit(false);

            String recipientAppId = PropertiesUtils.getMandatoryProperty(properties,
                    CmcAppPropertyNames.IAS_MSG_REQ_RECIPIENT_APP_ID_PROPERTY_NAME);
            String recipientAppType = PropertiesUtils.getMandatoryProperty(properties,
                    CmcAppPropertyNames.IAS_MSG_REQ_RECIPIENT_APP_TYPE_PROPERTY_NAME);
            String senderAppType = PropertiesUtils.getMandatoryProperty(properties,
                    CmcAppPropertyNames.IAS_MSG_REQ_SENDER_APP_TYPE_PROPERTY_NAME);
            int singlePullCallLimit = Integer.parseInt(PropertiesUtils.getMandatoryProperty(properties,
                    CmcAppPropertyNames.IAS_MSG_SINGLE_PULL_CALL_LIMIT_PER_BD_PROPERTY));
            if (singlePullCallLimitStr != null && !singlePullCallLimitStr.trim().equals("")) {
                singlePullCallLimit = Integer.parseInt(singlePullCallLimitStr);
            }
            String iasAppIdTransformListStr = PropertiesUtils.getMandatoryProperty(properties,
                    CmcAppPropertyNames.IAS_MSG_APP_ID_TRANSFORM_LIST_PROPERTY_NAME);
            List<String> iasAppIdTransformList = Arrays.asList(iasAppIdTransformListStr.split(","));
            Map<String, String> iasAppIdTransformMap = new HashMap<String, String>();
            for (String iasAppIdTransformStr : iasAppIdTransformList) {
                iasAppIdTransformMap.put(iasAppIdTransformStr.split(":")[0], iasAppIdTransformStr.split(":")[1]);
            }

            String serverId = PropertiesUtils.getMandatoryProperty(properties,
                    CmcAppPropertyNames.SERVER_ID_PROPERTY_NAME);
            if (getAsynAppIdStr == null || getAsynAppIdStr.trim().equals(""))
                getAsynAppIdStr = CmcSystemParam
                        .getPara(CmcAppConstants.IAS_MSG_GET_ASYN_APP_ID_LIST_PREFIX + serverId);

            String[] sendAsynAppIdArry = null;
            if (getAsynAppIdStr != null && getAsynAppIdStr.length() > 0) {
                sendAsynAppIdArry = getAsynAppIdStr.split(",");
            }
            logInfo("[IasMsgSettingSessionEJB]processMessageByBatchPull - sendAsynAppIdArry: "
                    + Arrays.toString(sendAsynAppIdArry) + ", singlePullCallLimit: " + singlePullCallLimit);

            for (String senderAppId : sendAsynAppIdArry) {

                logInfo("[IasMsgSettingSessionEJB]processMessageByBatchPull - Processing senderAppId: " + senderAppId);

                jobControlName = CmcAppConstants.IAS_MSG_GET_ASYN_BY_APP_PREFIX + senderAppId;
                if (jobControlUtils.lockJobControl(jobControlName)) {
                    MessagingClient msgClient = new MessagingClient(properties);

                    boolean callSinglePull = true;
                    int singlePullCnt = 0;
                    while (callSinglePull && singlePullCnt < singlePullCallLimit) {
                        singlePullCnt++;

                        String messageId = "";
                        String correlationId = "";
                        String retrievedSenderAppId = "";

                        try {
                            SOAPMessage responseSoapMsg = getMessageBySinglePull(msgClient, senderAppId, recipientAppId,
                                    senderAppType, recipientAppType);
                            logInfo(
                                    "[IasMsgSettingSessionEJB]processMessageByBatchPull - getMessageBySinglePull responseSoapMsg: \n"
                                            + WebServiceUtils.soapMessageToString(responseSoapMsg));
                            AsyncRequest asyncRequest = msgClient.getAsyncRequest(responseSoapMsg);

                            if (asyncRequest == null) {
                                logWarn("[IasMsgSettingSessionEJB]processMessageByBatchPull - msgClient.getAsyncRequest is NULL");
                            } else {
                                messageId = asyncRequest.getMessageId();
                                correlationId = asyncRequest.getCorrelationId();
                                retrievedSenderAppId = asyncRequest.getSenderAppId();
                            }

                            logInfo("[IasMsgSettingSessionEJB]processMessageByBatchPull - msgClient.getAsyncRequest"
                                    + ". singlePullCnt: " + singlePullCnt + ", messageId: " + messageId
                                    + ", correlationId: " + correlationId + ", retrievedSenderAppId: "
                                    + retrievedSenderAppId);

                            if (messageId == null || messageId.length() == 0) {
                                if (asyncRequest != null) {
                                    logInfo("[IasMsgSettingSessionEJB]processMessageByBatchPull - No message in queue");
                                }
                                callSinglePull = false;
                            } else {
                                List<String> msgIdList = new ArrayList<String>();
                                msgIdList.add(messageId);
                                boolean sendPullAck = sendPullAck(msgClient, recipientAppId, recipientAppType,
                                        msgIdList);

                                if (sendPullAck) {
                                    if (retrievedSenderAppId == null || "".equals(retrievedSenderAppId)) {
                                        logWarn("[IasMsgSettingSessionEJB]processMessageByBatchPull - retrievedSenderAppId is null");
                                    } else {

                                        if (iasAppIdTransformMap.containsKey(retrievedSenderAppId)) {
                                            logInfo("[IasMsgSettingSessionEJB]processMessageByBatchPull - senderAppId is transformed from "
                                                    + retrievedSenderAppId + " to "
                                                    + iasAppIdTransformMap.get(retrievedSenderAppId));
                                            retrievedSenderAppId = iasAppIdTransformMap.get(retrievedSenderAppId);
                                        }
                                        CmcServiceProviderDAO cmcServiceProviderDAO = new CmcServiceProviderDAO();
                                        CmcServiceProvider_ cmcServiceProvider = cmcServiceProviderDAO
                                                .getServiceProviderByAppId(conn, retrievedSenderAppId);

                                        boolean createAsynSuccess = false;
                                        AsynMessageDAO asynMessageDAO = new AsynMessageDAO();
                                        if (asynMessageDAO.isAsynMessageRetrievedBefore(conn, messageId)) {
                                            logWarn("[IasMsgSettingSessionEJB]processMessageByBatchPull - msgClient.getAsyncRequest asyn msg already retrieved messageId="
                                                    + messageId);
                                        } else {
                                            createAsynSuccess = asynMessageDAO.createAsynMessageRecord(conn, messageId,
                                                    recipientAppId, recipientAppType, null,
                                                    CmcAppConstants.ASYN_MSG_STATUS_ACKNOWLEDGED);
                                        }

                                        if (createAsynSuccess) {
                                            try {
                                                JAXBContext jaxbContext = JAXBContext.newInstance(IasAssoQueues.class);
                                                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                                                IasAssoQueues iasAssoQueues = (IasAssoQueues) jaxbUnmarshaller
                                                        .unmarshal(SoapUtils.getFirstBodyElement(responseSoapMsg));

                                                IasAssoQueue_ iasAssoQueue_ = null;
                                                for (IasAssoQueue iasAssoQueue : iasAssoQueues.getIasAssoQueues()) {
                                                    if (iasAssoQueue.getOpenId() != null
                                                            && !"".equals(iasAssoQueue.getOpenId())) {
                                                        iasAssoQueue_ = new IasAssoQueue_();
                                                        iasAssoQueue_.setOpenId(iasAssoQueue.getOpenId());
                                                        iasAssoQueue_.setJobId(
                                                                MysqlCommonDBUtils.getNextMachineBaseSequence());
                                                        iasAssoQueue_.setClientId(cmcServiceProvider.getClientId());
                                                        iasAssoQueue_.setServiceProviderId(
                                                                cmcServiceProvider.getIasOptSpId());
                                                        iasAssoQueue_.setAssoStatus(
                                                                IntegrationConstants.ASSO_STATUS_ASSOCIATE);
                                                        iasAssoQueue_.setJobStatus(IntegrationConstants.JOB_STATUS_NEW);
                                                        conn.setUpdateMode(HPFW_Connection.DIRECT);
                                                        iasAssoQueue_.insert(conn);
                                                    } else {
                                                        logWarn("[IasMsgSettingSessionEJB]processMessageByBatchPull - iasAssoQueue.getOpenId() is null or empty. This record will not added to DB and it will be ignored. "
                                                                +
                                                                "iasAssoQueue.getOpenId(): " + iasAssoQueue.getOpenId()
                                                                + ", retrievedSenderAppId: " + retrievedSenderAppId);
                                                    }
                                                }
                                                conn.setUpdateMode(HPFW_Connection.DIRECT_WITH_HISTORY);
                                                asynMessageDAO.updateAsynMessageStatus(conn, messageId,
                                                        CmcAppConstants.ASYN_MSG_STATUS_RESPONSE_SENT);
                                                conn.commit();
                                            } catch (Exception e) {
                                                logError(
                                                        "[IasMsgSettingSessionEJB]processMessageByBatchPull - createAsynMessageRecord status='A' success. senderAppId="
                                                                + senderAppId + ", messageId=" + messageId
                                                                + ", correlationId=" + correlationId + ". Exception:"
                                                                + e);
                                                conn.rollback();
                                            }
                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {
                            logError("[IasMsgSettingSessionEJB]processMessageByBatchPull - senderAppId=" + senderAppId
                                    + ", messageId=" + messageId
                                    + ", correlationId=" + correlationId + ". Exception:" + e);
                        }
                    }
                } else {
                    logInfo("[IasMsgSettingSessionEJB]processMessageByBatchPull - CMC_MARS_SYN_JOB_LOCK table cannot be locked. jobControlName="
                            + jobControlName);
                }

                jobControlUtils.releaseJobControl(jobControlName);
            }
        } catch (Exception e) {
            logError("[IasMsgSettingSessionEJB]processMessageByBatchPull - Exception in ", e);
            throw new EJBException(e);
        } finally {
            try {
                if (conn != null)
                    HPFW_Connection.close(conn);
            } catch (Exception ex) {
                logError(
                        "[IasMsgSettingSessionEJB]processMessageByBatchPull - General exception caught in conn.close();",
                        ex);
            }
        }

        logInfo("[IasMsgSettingSessionEJB]processMessageByBatchPull - END");
    }

    private SOAPMessage getMessageBySinglePull(MessagingClient msgClient,
            String senderAppId, String recipientAppId,
            String senderAppType, String recipientAppType) throws Exception {

        logInfo("[IasMsgSettingSessionEJB]getMessageBySinglePull - senderAppId[" + senderAppId + "]"
                + ",recipientAppId[" + recipientAppId + "]" + ",senderAppType[" + senderAppType + "] ,recipientAppType["
                + recipientAppType + "] - START");

        SOAPMessage responseMsg = null;

        try {

            Map<String, String> inMap = new TreeMap<>();
            inMap.put(IMessagingConstants.RECIPIENT_APP_ID, recipientAppId);
            inMap.put(IMessagingConstants.SENDER_APP_ID, senderAppId);

            SOAPMessage requestMsg = SoapUtils.emptyMessage();
            msgClient.addSinglePullRequest(requestMsg, inMap, senderAppType, recipientAppType);
            responseMsg = msgClient.sendSinglePullRequest(requestMsg);

            logInfo("[IasMsgSettingSessionEJB]getMessageBySinglePull - responseMsg: \n"
                    + WebServiceUtils.soapMessageToString(responseMsg));

            if (responseMsg.getSOAPPart().getEnvelope().getBody().hasFault()) {
                FaultEntry soapFaultEntry = SoapUtils.getFaultEntry(responseMsg);
                if (soapFaultEntry != null) {
                    logWarn("[IasMsgSettingSessionEJB][SOAP_FAULT]getMessageBySinglePull - sendSinglePullRequest: DetailCode="
                            + soapFaultEntry.getDetailCode() + ", DetailDescription="
                            + soapFaultEntry.getDetailDescription() + ", FaultCode=" + soapFaultEntry.getFaultCode()
                            + ", FaultString=" + soapFaultEntry.getFaultString());
                }
            }
        } catch (Exception e) {
            throw e;
        }

        logInfo("[IasMsgSettingSessionEJB]getMessageBySinglePull - senderAppId[" + senderAppId + "]"
                + ",recipientAppId[" + recipientAppId + "]" + ",senderAppType[" + senderAppType + "] ,recipientAppType["
                + recipientAppType + "] - END");

        return responseMsg;
    }

    private boolean sendPullAck(MessagingClient msgClient, String recipientAppId, String recipientAppType,
            List<String> msgIdList) throws Exception {
        boolean result = false;

        try {

            SOAPMessage pullAckRequest = msgClient.createPullAckRequest(recipientAppId, recipientAppType, msgIdList);
            SOAPMessage pullAckResponse = msgClient.sendPullAckRequest(pullAckRequest);

            if (pullAckResponse.getSOAPPart().getEnvelope().getBody().hasFault()) {
                logWarn("[IasMsgSettingSessionEJB]sendPullAck - failed msgIdList=" + msgIdList.toString());
                result = false;
            } else {
                logInfo("[IasMsgSettingSessionEJB]sendPullAck - success msgIdList=" + msgIdList.toString());
                result = true;
            }

        } catch (Exception e) {
            throw e;
        }

        return result;
    }

}
