
package hk.gov.cmc.appserver.ejb.session.maintainmessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.Vector;

import javax.naming.InitialContext;

import org.apache.xml.security.utils.EncryptionConstants;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import hk.gov.cmc.common.CmcAppConstants;
import hk.gov.cmc.common.CmcAppPropertyNames;
import hk.gov.cmc.common.ResultCodes;
import hk.gov.cmc.common.ResultMessages;
import hk.gov.cmc.config.CmcEnvProperties;
import hk.gov.cmc.config.CmcSystemParam;
import hk.gov.cmc.jaxb.maintainmessage.MaintainMessageRequest;
import hk.gov.cmc.jaxb.maintainmessage.MaintainMessageResponse;
import hk.gov.cmc.persistence.connection.hpfw.HPFW_Connection;
import hk.gov.cmc.utils.EncryptionUtils;
import hk.gov.cmc.utils.JobControlUtils;
import hk.gov.gcis.rm.common.javaee.ejb.EJBBase;
import hk.gov.gcis.rm.common.utils.PropertiesUtils;
import hk.gov.gcis.rm.common.utils.XmlUtils;
import hk.gov.gcis.rm.keyservice.appserver.AppPropertyNames;
import hk.gov.gcis.rm.keyservice.appserver.ejb.session.IPKIUtil;
import hk.gov.gcis.rm.keyservice.common.Constants;
import hk.gov.gcis.ss.common.utils.SoapUtils;
import hk.gov.gcis.ss.common.webserver.elements.FaultEntry;
import hk.gov.gcis.ss.messaging.client.MessagingClient;
import hk.gov.gcis.ss.messaging.fault.IErrorCodes;
import hk.gov.gcis.ss.messaging.types.IMessageType;
import hk.gov.gcis.ss.messaging.types.IMessagingConstants;
import hk.gov.gcis.ss.messaging.webserver.elements.asyncmessaging.AsyncRequest;
import jakarta.ejb.EJBException;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionManagement;
import jakarta.ejb.TransactionManagementType;
import jakarta.xml.soap.Name;
import jakarta.xml.soap.SOAPBody;
import jakarta.xml.soap.SOAPElement;
import jakarta.xml.soap.SOAPEnvelope;
import jakarta.xml.soap.SOAPMessage;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class MaintainMessageSessionEJB extends EJBBase
        implements IMaintainMessageSessionBMRemote, IMaintainMessageSessionBMLocal {

    private final static CmcEnvProperties cmcEnvProperties = new CmcEnvProperties();

    public MaintainMessageResponse processMessage(String sendAppId, MaintainMessageRequest maintMsgReq)
            throws EJBException {
        logInfo("[MaintMsg]processMessage - START");

        MaintainMessageResponse retrieveEventDetailResponse = null;

        HPFW_Connection conn = null;

        try {
            conn = HPFW_Connection.getHPFW_Connection(false);

            conn.begin(sendAppId, null, HPFW_Connection.DIRECT_WITH_HISTORY);

            logInfo("[MaintMsg]processMessage sendAppId=" + sendAppId + ", maintMsgReq=" + maintMsgReq.toString());

            retrieveEventDetailResponse = new MaintainMessageController().processMessage(conn, sendAppId, maintMsgReq);

            conn.commit();

        } catch (RuntimeException e) {
            logError("[MaintMsg]Runtime exception raised in processMessage", e);
            throw e;
        } catch (Exception e) {
            logError("[MaintMsg]General exception raised in processMessage", e);
            throw new EJBException(e);
        } finally {
            try {
                if (conn != null)
                    HPFW_Connection.close(conn);
            } catch (Exception ex) {
                logError("[MaintMsg]General exception caught in processMessage - conn.close();", ex);
            }
        }

        logInfo("[MaintMsg]processMessage - END");

        return retrieveEventDetailResponse;
    }

    public void processMessageByBatchPull(String getAsynAppIdStr, String singlePullCallLimitStr) throws EJBException {
        logInfo("[BATCH_JOB][MaintMsg]processMessageByBatchPull - START");

        HPFW_Connection conn = null;
        JobControlUtils jobControlUtils = null;
        String jobControlName = "";
        boolean releaseLock = true;

        try {
            Properties properties = cmcEnvProperties.getProperties();

            String maintMsgReqRecptAppId = PropertiesUtils.getMandatoryProperty(properties,
                    CmcAppPropertyNames.MAINT_MSG_REQ_RECIPIENT_APP_ID_PROPERTY_NAME);

            String maintMsgReqSenderAppType = PropertiesUtils.getMandatoryProperty(properties,
                    CmcAppPropertyNames.MAINT_MSG_REQ_SENDER_APP_TYPE_PROPERTY_NAME);
            String maintMsgReqRecptAppType = PropertiesUtils.getMandatoryProperty(properties,
                    CmcAppPropertyNames.MAINT_MSG_REQ_RECIPIENT_APP_TYPE_PROPERTY_NAME);
            int singlePullCallLimit = Integer.parseInt(PropertiesUtils.getMandatoryProperty(properties,
                    CmcAppPropertyNames.MAINT_MSG_SINGLE_PULL_CALL_LIMIT_PROPERTY));

            if (singlePullCallLimitStr != null && !singlePullCallLimitStr.trim().equals("")) {
                singlePullCallLimit = Integer.parseInt(singlePullCallLimitStr);
            }

            MessagingClient msgClient = new MessagingClient(properties);

            jobControlUtils = new JobControlUtils();

            conn = HPFW_Connection.getHPFW_Connection(false);

            List<String> sendAsynAppIdList = new ArrayList<>();
            List<String> noConcurrentAppIdList = new ArrayList<>();

            String serverId = PropertiesUtils.getMandatoryProperty(properties,
                    CmcAppPropertyNames.SERVER_ID_PROPERTY_NAME);

            if (getAsynAppIdStr == null || getAsynAppIdStr.trim().equals(""))
                getAsynAppIdStr = CmcSystemParam
                        .getPara(CmcAppConstants.MAINT_MSG_GET_ASYN_APP_ID_LIST_PREFIX + serverId);

            String noConcurrentAppIdStr = CmcSystemParam.getPara(CmcAppConstants.MAINT_MSG_NO_CONCURRENT_APP_ID_LIST);

            if (getAsynAppIdStr != null && getAsynAppIdStr.length() > 0) {
                String[] getAsynAppIdArray = getAsynAppIdStr.split(",");
                sendAsynAppIdList = Arrays.asList(getAsynAppIdArray);
            }

            if (noConcurrentAppIdStr != null && noConcurrentAppIdStr.length() > 0) {
                String[] noConcurrentAppIdArray = noConcurrentAppIdStr.split(",");
                noConcurrentAppIdList = Arrays.asList(noConcurrentAppIdArray);
            }

            // MaintainMessageDAO maintainMessageDAO = new MaintainMessageDAO();
            // AsynMessageDAO asynMessageDAO = new AsynMessageDAO();

            for (int a = 0; a < sendAsynAppIdList.size(); a++) {

                String getAsynSenderAppId = sendAsynAppIdList.get(a).trim();
                jobControlName = CmcAppConstants.MAINT_MSG_GET_ASYN_BY_APP_PREFIX + getAsynSenderAppId;

                boolean callSinglePull = true;

                int singlePullCnt = 0;

                while (callSinglePull && singlePullCnt < singlePullCallLimit) {

                    singlePullCnt++;

                    if (jobControlUtils.lockJobControl(jobControlName)) {
                        releaseLock = false;

                        String scopesMsgId = "";
                        String scopesCorrelationId = "";

                        try {

                            SOAPMessage requestSoapMsg = getMessageBySinglePull(getAsynSenderAppId);

                            AsyncRequest asyncRequest = msgClient.getAsyncRequest(requestSoapMsg);

                            if (asyncRequest == null) {
                                logWarn("[MaintMsg]processMessageByBatchPull asyncRequest is NULL");
                            } else {
                                scopesMsgId = asyncRequest.getMessageId();
                                scopesCorrelationId = asyncRequest.getCorrelationId();
                            }

                            logInfo("[MaintMsg]processMessageByBatchPull singlePullCnt=" + singlePullCnt
                                    + ", scopesMsgId=" + scopesMsgId + ", scopesCorrelationId=" + scopesCorrelationId);

                            if (scopesMsgId == null || scopesMsgId.length() == 0) {

                                if (asyncRequest != null) {
                                    logInfo("No message in queue");
                                }
                                callSinglePull = false;
                            } else {

                                List<String> msgIdList = new ArrayList<>();
                                msgIdList.add(scopesMsgId);

                                boolean sendPullAckSuccess = sendPullAck(msgClient, scopesMsgId, maintMsgReqRecptAppId,
                                        maintMsgReqRecptAppType, msgIdList);

                                if (sendPullAckSuccess) {

                                    MaintainMessageResponse maintMsgResponse = null;
                                    SOAPMessage responseSoapMsg = SoapUtils.emptyMessage();
                                    String sendAppId = null;
                                    boolean createAsynSuccess = false;

                                    try {

                                        // if (asynMessageDAO.isAsynMessageRetrievedBefore(conn, scopesMsgId)) {
                                        // logWarn("[MaintMsg]processMessageByBatchPull asyn msg already retrieved: "
                                        // + scopesMsgId);
                                        // } else {

                                        // conn.begin(null, null, HPFW_Connection.DIRECT_WITH_HISTORY);

                                        // asynMessageDAO.createAsynMessageRecord(conn, scopesMsgId,
                                        // maintMsgReqRecptAppId, maintMsgReqRecptAppType, null,
                                        // CmcAppConstants.ASYN_MSG_STATUS_ACKNOWLEDGED);

                                        // try {
                                        // conn.commit();
                                        // createAsynSuccess = true;
                                        // } catch (java.sql.SQLException sqlEx) {
                                        // String errMessage = sqlEx.getMessage();
                                        // if (errMessage.indexOf("ORA-00001") == -1) {
                                        // logError("[MaintMsg]processMessageByBatchPull Error message: "
                                        // + errMessage);
                                        // } else {
                                        // logWarn("[MaintMsg]processMessageByBatchPull corr_id exists in DB");
                                        // }
                                        // }

                                        // if (!noConcurrentAppIdList.contains(getAsynSenderAppId)) {

                                        // if (!releaseLock) {
                                        // if (jobControlUtils != null) {
                                        // jobControlUtils.releaseJobControl(jobControlName);
                                        // releaseLock = true;
                                        // }
                                        // }
                                        // }
                                        // }

                                        if (createAsynSuccess) {

                                            Name scopesName = SoapUtils.createName(IMessagingConstants.MESSAGING_HEADER,
                                                    null, IMessagingConstants.MESSAGING_NS);
                                            SOAPElement soapElement = SoapUtils.getFirstHeaderElement(requestSoapMsg,
                                                    scopesName);
                                            NodeList nodeList = soapElement
                                                    .getElementsByTagName(CmcAppConstants.APPID_TAG_NAME);

                                            if (nodeList != null && nodeList.getLength() > 0)
                                                sendAppId = nodeList.item(0).getFirstChild().getNodeValue();
                                            if (sendAppId == null || sendAppId.length() == 0) {
                                                logError("[MaintMsg]SenderAppId is null in asyn request.");

                                            } else {

                                                SOAPElement body = SoapUtils.getFirstBodyElement(requestSoapMsg);

                                                SOAPBody bod = (SOAPBody) requestSoapMsg.getSOAPBody();
                                                Document bodyDoc = bod.extractContentAsDocument();

                                                NodeList encryptedDataList = bodyDoc.getDocumentElement()
                                                        .getElementsByTagNameNS(EncryptionConstants.EncryptionSpecNS,
                                                                EncryptionConstants._TAG_ENCRYPTEDDATA);

                                                MaintainMessageRequest maintMsgReq = null;

                                                if (encryptedDataList.getLength() > 0) {

                                                    logInfo("This message is encrypted in-transit. encryptedDataList: "
                                                            + encryptedDataList.getLength() + ", sendAppId: "
                                                            + sendAppId);

                                                    if (!EncryptionUtils.isKeyExistInKMU(conn, bodyDoc)) {
                                                        logWarn("Key not found in processMessageByBatchPull.");
                                                        maintMsgResponse = generateResponse(
                                                                ResultCodes.RESULT_CD_GENERAL_ERROR,
                                                                ResultMessages.RESULT_MSG_GENERAL_ERROR);
                                                    } else {

                                                        String pkiUtilJNDIName = properties.getProperty(
                                                                AppPropertyNames.PROPERTY_NAME_PKI_UTIL_EJB_REMOTE_JNDI_NAME,
                                                                CmcAppConstants.GLOBAL_CONTEXT_NAME
                                                                        + CmcAppConstants.CONTEXT_NAME_SEPARATOR
                                                                        + Constants.GCIS_RM_KEYSERVICE_MODULE_NAME
                                                                        + CmcAppConstants.CONTEXT_NAME_SEPARATOR
                                                                        + Constants.GCIS_RM_KEYSERVICE_MODULE_NAME
                                                                        + CmcAppConstants.DEFAULT_EJB_MODULE_NAME_SUFFIX
                                                                        + CmcAppConstants.CONTEXT_NAME_SEPARATOR
                                                                        + "PKIUtil!hk.gov.gcis.rm.keyservice.appserver.ejb.session.IPKIUtil");

                                                        InitialContext context = new InitialContext(properties);
                                                        IPKIUtil pkiUtil = (IPKIUtil) context.lookup(pkiUtilJNDIName);

                                                        Document decDoc = pkiUtil.decryptXML(null, bodyDoc, null);

                                                        maintMsgReq = SoapUtils.documentToJaxb(decDoc,
                                                                MaintainMessageRequest.class);
                                                    }
                                                } else {

                                                    logInfo("This message is not encrypted in-transit. encryptedDataList: "
                                                            + encryptedDataList.getLength() + ", sendAppId: "
                                                            + sendAppId);

                                                    maintMsgReq = SoapUtils.documentToJaxb(bodyDoc,
                                                            MaintainMessageRequest.class);

                                                }

                                                conn.begin(null, null, HPFW_Connection.DIRECT_WITH_HISTORY);

                                                if (maintMsgReq != null) {
                                                    // maintMsgResponse = maintainMessageDAO.processMessage(conn,
                                                    // sendAppId, maintMsgReq);
                                                }

                                                conn.commit();

                                            }
                                        }

                                    } catch (Exception pEx) {
                                        logError(
                                                "[MaintMsg]General exception caught in maintainMessageDAO.processMessage",
                                                pEx);
                                        maintMsgResponse = generateResponse(ResultCodes.RESULT_CD_GENERAL_ERROR,
                                                ResultMessages.RESULT_MSG_GENERAL_ERROR);

                                        conn.clear();
                                    }

                                    if (sendAppId != null && sendAppId.length() > 0) {

                                        // responseSoapMsg = CastorUtils.marshal(responseSoapMsg, maintMsgResponse);
                                        Document requestDocument = SoapUtils.jaxbToDocument(maintMsgResponse,
                                                MaintainMessageResponse.class);
                                        SOAPEnvelope envelopeMessage = responseSoapMsg.getSOAPPart().getEnvelope();
                                        Document requestDocumentCopy = XmlUtils.copyDOM(requestDocument);
                                        envelopeMessage.getBody().addDocument(requestDocumentCopy);

                                        String soapaction = properties
                                                .getProperty(CmcAppPropertyNames.ASYN_MSG_SOAP_ACTION_PROPERTY_NAME);
                                        responseSoapMsg.getMimeHeaders().setHeader(SoapUtils.SOAP_ACTION, soapaction);

                                        responseSoapMsg = createAsyncResponseMsg(msgClient, maintMsgResponse, sendAppId,
                                                maintMsgReqSenderAppType, maintMsgReqRecptAppId,
                                                maintMsgReqRecptAppType, scopesCorrelationId);

                                        boolean returnResponseSuccess = true;

                                        SOAPMessage rspAsynMsgResponse = null;
                                        try {
                                            rspAsynMsgResponse = msgClient.sendAsyncRequest(responseSoapMsg);

                                        } catch (Exception ex) {

                                            logError(
                                                    "[MaintMsg]General exception raised in send ResponseMsg by Async, scopesMsgId="
                                                            + scopesMsgId + "scopesCorrelationId=" + scopesCorrelationId
                                                            + ": ",
                                                    ex);
                                            returnResponseSuccess = false;
                                        }

                                        if (rspAsynMsgResponse != null && rspAsynMsgResponse.getSOAPPart().getEnvelope()
                                                .getBody().hasFault()) {
                                            returnResponseSuccess = false;

                                            FaultEntry soapFaultEntry = SoapUtils.getFaultEntry(rspAsynMsgResponse);
                                            if (soapFaultEntry != null) {
                                                String detailCode = soapFaultEntry.getDetailCode();

                                                logWarn("[MaintMsg] [SOAP_FAULT] sendAsyncRequest: DetailCode="
                                                        + soapFaultEntry.getDetailCode() + ", DetailDescription="
                                                        + soapFaultEntry.getDetailDescription() + ", FaultCode="
                                                        + soapFaultEntry.getFaultCode() + ", FaultString="
                                                        + soapFaultEntry.getFaultString());

                                                if (IErrorCodes.WS_WARN_ASYNC_DUPLICATE_RESPONSE.equals(detailCode)) {

                                                    logWarn("[MaintMsg]sendMsgResponseByAsyn response msg already sent before: scopesMsgId="
                                                            + scopesMsgId + "scopesCorrelationId="
                                                            + scopesCorrelationId);
                                                    returnResponseSuccess = true;
                                                }

                                            }
                                        }

                                        if (returnResponseSuccess) {
                                            try {

                                                logInfo("[MaintMsg]Send response success. scopesMsgId=" + scopesMsgId);

                                                conn.begin(null, null, HPFW_Connection.DIRECT_WITH_HISTORY);

                                                if (createAsynSuccess) {
                                                    // asynMessageDAO.updateAsynMessageStatus(conn, scopesMsgId,
                                                    // CmcAppConstants.ASYN_MSG_STATUS_RESPONSE_SENT);
                                                } else {
                                                    // asynMessageDAO.createAsynMessageRecord(conn, scopesMsgId,
                                                    // maintMsgReqRecptAppId, maintMsgReqRecptAppType, null,
                                                    // CmcAppConstants.ASYN_MSG_STATUS_RESPONSE_SENT);
                                                }

                                                conn.commit();

                                            } catch (Exception e1) {
                                                logError("[MaintMsg]updateAsynMessageStatus failed. scopesMsgId="
                                                        + scopesMsgId);
                                            }
                                        } else {
                                            logWarn("[MaintMsg]sendMsgResponseByAsyn send response msg failed. scopesMsgId="
                                                    + scopesMsgId);

                                            // asynMessageDAO.updateAsynMessage(conn, scopesMsgId, responseSoapMsg,
                                            // CmcAppConstants.ASYN_MSG_STATUS_ACKNOWLEDGED);
                                            conn.commit();

                                        }
                                    }
                                }
                            }
                        } catch (Exception ex) {

                            logError("[MaintMsg]General exception raised in processMessageByBatchPull scopesMsgId="
                                    + scopesMsgId + ", scopesCorrelationId=" + scopesCorrelationId + ": ", ex);
                        }

                        if (!releaseLock) {
                            if (jobControlUtils != null) {
                                jobControlUtils.releaseJobControl(jobControlName);
                                releaseLock = true;
                            }
                        }

                    } else {
                        logInfo("[MaintMsg]Another job is running for getting asyn by sender app id="
                                + getAsynSenderAppId);
                        Thread.sleep(500);
                    }
                }

            }

        } catch (RuntimeException e) {
            logError("[MaintMsg]Runtime exception raised in processMessageByBatchPull", e);
            throw e;
        } catch (Exception e) {
            logError("[MaintMsg]General exception raised in processMessageByBatchPull", e);
            throw new EJBException(e);
        } finally {
            try {
                if (conn != null)
                    HPFW_Connection.close(conn);
            } catch (Exception ex) {
                logError("[MaintMsg]General exception caught in processMessageByBatchPull - conn.close();", ex);
            }

            if (!releaseLock) {
                if (jobControlUtils != null) {
                    jobControlUtils.releaseJobControl(jobControlName);
                    releaseLock = true;
                }
            }

        }

        logInfo("[BATCH_JOB][MaintMsg]processMessageByBatchPull - END");
    }

    private SOAPMessage[] getMessageByBatchPull() throws EJBException {
        logInfo("[MaintMsg]getMessageByBatchPull - START");

        SOAPMessage[] responseMsgArray = null;

        try {

            Properties properties = cmcEnvProperties.getProperties();

            MessagingClient msgClient = new MessagingClient(properties);

            Map<String, String> inMap = new TreeMap<>();
            inMap.put(IMessagingConstants.RECIPIENT_APP_ID,
                    properties.getProperty(CmcAppPropertyNames.MAINT_MSG_REQ_RECIPIENT_APP_ID_PROPERTY_NAME));

            SOAPMessage requestMsg = SoapUtils.emptyMessage();
            msgClient.addBatchPullRequest(requestMsg, inMap,
                    properties.getProperty(CmcAppPropertyNames.MAINT_MSG_REQ_SENDER_APP_TYPE_PROPERTY_NAME),
                    properties.getProperty(CmcAppPropertyNames.MAINT_MSG_REQ_RECIPIENT_APP_TYPE_PROPERTY_NAME));

            SOAPMessage responseMsg = msgClient.sendBatchPullRequest(requestMsg);

            if (!responseMsg.getSOAPPart().getEnvelope().getBody().hasFault()) {

                responseMsgArray = msgClient.getMsgsFromResponse(responseMsg);
            }

        } catch (RuntimeException e) {
            logError("[MaintMsg]Runtime exception raised in getMessageByBatchPull", e);
            throw e;
        } catch (Exception e) {
            logError("[MaintMsg]General exception raised in getMessageByBatchPull", e);
            throw new EJBException(e);
        }

        return responseMsgArray;
    }

    private SOAPMessage getMessageBySinglePull(String senderAppId) throws EJBException {
        logInfo("[MaintMsg]getMessageBySinglePull senderAppId[" + senderAppId + "] - START");

        SOAPMessage responseMsg = null;

        try {

            Properties properties = cmcEnvProperties.getProperties();

            MessagingClient msgClient = new MessagingClient(properties);

            Map<String, String> inMap = new TreeMap<>();
            inMap.put(IMessagingConstants.RECIPIENT_APP_ID,
                    properties.getProperty(CmcAppPropertyNames.MAINT_MSG_REQ_RECIPIENT_APP_ID_PROPERTY_NAME));
            inMap.put(IMessagingConstants.SENDER_APP_ID, senderAppId);

            SOAPMessage requestMsg = SoapUtils.emptyMessage();
            msgClient.addSinglePullRequest(requestMsg, inMap,
                    properties.getProperty(CmcAppPropertyNames.MAINT_MSG_REQ_SENDER_APP_TYPE_PROPERTY_NAME),
                    properties.getProperty(CmcAppPropertyNames.MAINT_MSG_REQ_RECIPIENT_APP_TYPE_PROPERTY_NAME));

            responseMsg = msgClient.sendSinglePullRequest(requestMsg);

            if (responseMsg.getSOAPPart().getEnvelope().getBody().hasFault()) {
                FaultEntry soapFaultEntry = SoapUtils.getFaultEntry(responseMsg);
                if (soapFaultEntry != null) {

                    logWarn("[MaintMsg] [SOAP_FAULT] sendSinglePullRequest: DetailCode="
                            + soapFaultEntry.getDetailCode() + ", DetailDescription="
                            + soapFaultEntry.getDetailDescription() + ", FaultCode=" + soapFaultEntry.getFaultCode()
                            + ", FaultString=" + soapFaultEntry.getFaultString());

                }
            }

        } catch (RuntimeException e) {
            logError("[MaintMsg]Runtime exception raised in getMessageBySinglePull", e);
            throw e;
        } catch (Exception e) {
            logError("[MaintMsg]General exception raised in getMessageBySinglePull", e);
            throw new EJBException(e);
        }

        logInfo("[MaintMsg]getMessageBySinglePull senderAppId[" + senderAppId + "] - END");

        return responseMsg;
    }

    private MaintainMessageResponse generateResponse(String resultCd, String resultMsg)
            throws Exception {

        MaintainMessageResponse maintainMessageResponse = new MaintainMessageResponse();
        maintainMessageResponse.setResultCode(resultCd);
        maintainMessageResponse.setResultMessage(resultMsg);
        return maintainMessageResponse;
    }

    private static SOAPMessage createAsyncResponseMsg(MessagingClient msgClient, Object messageCastorObject,
            String clientAppId, String clientAppType, String serverAppId, String serverAppType,
            String scopesCorrelationId) throws Exception {
        SOAPMessage responseSoapMsg = SoapUtils.emptyMessage();
        // responseSoapMsg = CastorUtils.marshal(responseSoapMsg, messageCastorObject);
        Document requestDocument = SoapUtils.jaxbToDocument(messageCastorObject, MaintainMessageResponse.class);
        SOAPEnvelope envelopeMessage = responseSoapMsg.getSOAPPart().getEnvelope();
        Document requestDocumentCopy = XmlUtils.copyDOM(requestDocument);
        envelopeMessage.getBody().addDocument(requestDocumentCopy);

        List<String> rcptList = new Vector<>();
        List<String> rcptAppTypeList = new Vector<>();
        rcptList.add(clientAppId);
        rcptAppTypeList.add(clientAppType);

        Map<String, Object> inMap = new TreeMap<>();

        inMap.put(IMessagingConstants.RECIPIENT_APP_ID_LIST, rcptList);
        inMap.put(IMessagingConstants.MESSAGE_TYPE, IMessageType.RESPONSE);

        inMap.put(IMessagingConstants.SENDER_APP_ID, serverAppId);
        inMap.put(IMessagingConstants.CORRELATION_ID, scopesCorrelationId);

        msgClient.addAsyncRequest(responseSoapMsg, inMap, serverAppType, rcptAppTypeList, null);
        return responseSoapMsg;
    }

    private boolean sendPullAck(MessagingClient msgClient, String scopesMsgId, String maintMsgReqRecptAppId,
            String maintMsgReqRecptAppType, List<String> msgIdList) {
        boolean result = false;

        try {

            SOAPMessage pullAckRequest = msgClient.createPullAckRequest(maintMsgReqRecptAppId, maintMsgReqRecptAppType,
                    msgIdList);
            SOAPMessage pullAckResponse = msgClient.sendPullAckRequest(pullAckRequest);

            if (pullAckResponse.getSOAPPart().getEnvelope().getBody().hasFault()) {
                logWarn("[MaintMsg]sendMsgResponseByAsyn send pull ack failed: " + scopesMsgId);
                result = false;
            } else {
                logInfo("[MaintMsg]sendPullAck success scopesMsgId=" + scopesMsgId);
                result = true;
            }

        } catch (Exception ex) {
            result = false;
        }

        return result;
    }

}
