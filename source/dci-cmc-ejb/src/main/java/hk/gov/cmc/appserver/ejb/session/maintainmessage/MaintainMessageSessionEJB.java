
package hk.gov.cmc.appserver.ejb.session.maintainmessage;

import java.sql.SQLException;
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
import hk.gov.cmc.dao.asynmessage.AsynMessageDAO;
import hk.gov.cmc.jaxb.maintainmessage.MaintainMessageRequest;
import hk.gov.cmc.jaxb.maintainmessage.MaintainMessageResponse;
import hk.gov.cmc.mapper.maintainmessage.MaintainMessageRequestMapper;
import hk.gov.cmc.mapper.maintainmessage.MaintainMessageResponseMapper;
import hk.gov.cmc.persistence.connection.hpfw.HPFW_Connection;
import hk.gov.cmc.processor.maintainmessage.MaintainMessageProcessor;
import hk.gov.cmc.utils.common.EncryptionUtils;
import hk.gov.cmc.utils.job.JobControlUtils;
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
        logInfo("[MaintainMessageSessionEJB]processMessage - START");

        MaintainMessageResponse retrieveEventDetailResponse = null;

        HPFW_Connection conn = null;

        try {
            conn = HPFW_Connection.getHPFW_Connection(false);

            conn.begin(sendAppId, null, HPFW_Connection.DIRECT_WITH_HISTORY);

            logInfo("[MaintainMessageSessionEJB]processMessage sendAppId: " + sendAppId + ", maintMsgReq: " + maintMsgReq.toString());

            MaintainMessageProcessor maintainMessageProcessor = new MaintainMessageProcessor();
            hk.gov.cmc.model.maintainmessage.request.MaintainMessageRequest maintMsgReqDomain = MaintainMessageRequestMapper
                    .fromJaxb(maintMsgReq);
            hk.gov.cmc.model.maintainmessage.response.MaintainMessageResponse maintainMsgRespDomain = maintainMessageProcessor
                    .processMessage(conn, sendAppId, maintMsgReqDomain);
            retrieveEventDetailResponse = MaintainMessageResponseMapper.toJaxb(maintainMsgRespDomain);

            conn.commit();

        } catch (RuntimeException e) {
            logError("[MaintainMessageSessionEJB]Runtime exception raised in processMessage", e);
            throw e;
        } catch (Exception e) {
            logError("[MaintainMessageSessionEJB]General exception raised in processMessage", e);
            throw new EJBException(e);
        } finally {
            try {
                if (conn != null)
                    HPFW_Connection.close(conn);
            } catch (Exception ex) {
                logError("[MaintainMessageSessionEJB]General exception caught in processMessage - conn.close();", ex);
            }
        }

        logInfo("[MaintainMessageSessionEJB]processMessage - END");

        return retrieveEventDetailResponse;
    }

    public void processMessageByBatchPull(String getAsynAppIdStr, String singlePullCallLimitStr) throws EJBException {
        logInfo("[BATCH_JOB][MaintainMessageSessionEJB]processMessageByBatchPull - START");

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
            MaintainMessageProcessor maintainMessageProcessor = new MaintainMessageProcessor();
            AsynMessageDAO asynMessageDAO = new AsynMessageDAO();

            for (int a = 0; a < sendAsynAppIdList.size(); a++) {

                String getAsynSenderAppId = sendAsynAppIdList.get(a).trim();
                jobControlName = CmcAppConstants.MAINT_MSG_GET_ASYN_BY_APP_PREFIX + getAsynSenderAppId;
                logInfo("[MaintainMessageSessionEJB]processMessageByBatchPull - SenderAppId: " + getAsynSenderAppId + ", a: " + a + ", jobControlName: " + jobControlName);

                boolean callSinglePull = true;

                int singlePullCnt = 0;

                while (callSinglePull && singlePullCnt < singlePullCallLimit) {
                    logInfo("[MaintainMessageSessionEJB]processMessageByBatchPull - SenderAppId: " + getAsynSenderAppId + ", callSinglePull: " + callSinglePull 
                        + ", singlePullCnt: " + singlePullCnt);

                    singlePullCnt++;

                    boolean lockSuccess = jobControlUtils.lockJobControl(jobControlName);
                    logInfo("[MaintainMessageSessionEJB]processMessageByBatchPull - SenderAppId: " + getAsynSenderAppId + ", callSinglePull: " + callSinglePull 
                        + ", singlePullCnt: " + singlePullCnt + ", lockSuccess: " + lockSuccess);
                    if (lockSuccess) {
                        releaseLock = false;

                        String scopesMsgId = "";
                        String scopesCorrelationId = "";

                        try {

                            SOAPMessage requestSoapMsg = getMessageBySinglePull(getAsynSenderAppId);

                            AsyncRequest asyncRequest = msgClient.getAsyncRequest(requestSoapMsg);

                            if (asyncRequest == null) {
                                logWarn("[MaintainMessageSessionEJB]processMessageByBatchPull - SenderAppId: " + getAsynSenderAppId + " asyncRequest is NULL");
                            } else {
                                scopesMsgId = asyncRequest.getMessageId();
                                scopesCorrelationId = asyncRequest.getCorrelationId();
                            }

                            logInfo("[MaintainMessageSessionEJB]processMessageByBatchPull - SenderAppId: " + getAsynSenderAppId + ", singlePullCnt: " + singlePullCnt
                                    + ", scopesMsgId: " + scopesMsgId + ", scopesCorrelationId: " + scopesCorrelationId);

                            if (scopesMsgId == null || scopesMsgId.length() == 0) {

                                if (asyncRequest != null) {
                                    logInfo("[MaintainMessageSessionEJB]processMessageByBatchPull - SenderAppId: " + getAsynSenderAppId + ", no message in queue.");
                                }
                                callSinglePull = false;
                            } else {

                                List<String> msgIdList = new ArrayList<>();
                                msgIdList.add(scopesMsgId);

                                logInfo("[MaintainMessageSessionEJB]processMessageByBatchPull - SenderAppId: " + getAsynSenderAppId + ", send PullAck for scopesMsgId: " + scopesMsgId 
                                    + ", maintMsgReqRecptAppId: " + maintMsgReqRecptAppId + ", maintMsgReqRecptAppType: " + maintMsgReqRecptAppType
                                    + ", msgIdList: " + msgIdList.toString());
                                boolean sendPullAckSuccess = sendPullAck(msgClient, scopesMsgId, maintMsgReqRecptAppId,
                                        maintMsgReqRecptAppType, msgIdList);
                                logInfo("[MaintainMessageSessionEJB]processMessageByBatchPull - SenderAppId: " + getAsynSenderAppId + ", sendPullAckSuccess: " + sendPullAckSuccess);

                                if (sendPullAckSuccess) {

                                    MaintainMessageResponse maintMsgResponse = null;
                                    SOAPMessage responseSoapMsg = SoapUtils.emptyMessage();
                                    String sendAppId = null;
                                    boolean createAsynSuccess = false;

                                    try {

                                        if (asynMessageDAO.isAsynMessageRetrievedBefore(conn, scopesMsgId)) {
                                            logWarn("[MaintainMessageSessionEJB]processMessageByBatchPull - SenderAppId: " + getAsynSenderAppId + ", asyn msg already retrieved: "
                                                    + scopesMsgId);
                                        } else {

                                            conn.begin(null, null, HPFW_Connection.DIRECT_WITH_HISTORY);

                                            asynMessageDAO.createAsynMessageRecord(conn, scopesMsgId,
                                                    maintMsgReqRecptAppId, maintMsgReqRecptAppType, null,
                                                    CmcAppConstants.ASYN_MSG_STATUS_ACKNOWLEDGED);

                                            try {
                                                conn.commit();
                                                createAsynSuccess = true;
                                                logInfo("[MaintainMessageSessionEJB]processMessageByBatchPull - SenderAppId: " + getAsynSenderAppId + ", scopesMsgId: " + scopesMsgId 
                                                        + ", maintMsgReqRecptAppId: " + maintMsgReqRecptAppId + ", maintMsgReqRecptAppType: " + maintMsgReqRecptAppType
                                                        + " createAsynMessageRecord success and DB committed.");
                                            } catch (SQLException sqlEx) {
                                                String errMessage = sqlEx.getMessage();
                                                if (errMessage.indexOf("ORA-00001") == -1) {
                                                    logError("[MaintainMessageSessionEJB]processMessageByBatchPull - SenderAppId: " + getAsynSenderAppId + ", Error message: "
                                                            + errMessage);
                                                } else {
                                                    logWarn("[MaintainMessageSessionEJB]processMessageByBatchPull - SenderAppId: " + getAsynSenderAppId + ", corr_id exists in DB");
                                                }
                                            }

                                            logInfo("[MaintainMessageSessionEJB]processMessageByBatchPull - SenderAppId: " + getAsynSenderAppId + ", noConcurrentAppIdList: "
                                                    + noConcurrentAppIdList.toString());
                                            if (!noConcurrentAppIdList.contains(getAsynSenderAppId)) {
                                                logInfo("[MaintainMessageSessionEJB]processMessageByBatchPull - SenderAppId: " + getAsynSenderAppId
                                                        + ", releaseLock: " + releaseLock + ", jobControlUtils is null: " + (jobControlUtils == null)
                                                        + ", jobControlName: " + jobControlName);
                                                if (!releaseLock) {
                                                    if (jobControlUtils != null) {
                                                        jobControlUtils.releaseJobControl(jobControlName);
                                                        releaseLock = true;
                                                        logInfo("[MaintainMessageSessionEJB]processMessageByBatchPull - SenderAppId: " + getAsynSenderAppId
                                                                + ", released lock before processing message.");
                                                    }
                                                }
                                            }
                                        }

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
                                                logError("[MaintainMessageSessionEJB]SenderAppId is null in asyn request.");

                                            } else {

                                                // SOAPElement body = SoapUtils.getFirstBodyElement(requestSoapMsg);
                                                SOAPBody body = (SOAPBody) requestSoapMsg.getSOAPBody();
                                                Document bodyDoc = body.extractContentAsDocument();

                                                NodeList encryptedDataList = bodyDoc.getDocumentElement()
                                                        .getElementsByTagNameNS(EncryptionConstants.EncryptionSpecNS,
                                                                EncryptionConstants._TAG_ENCRYPTEDDATA);

                                                MaintainMessageRequest maintMsgReq = null;

                                                if (encryptedDataList.getLength() > 0) {

                                                    logInfo("[MaintainMessageSessionEJB]processMessageByBatchPull - This message is encrypted in-transit. encryptedDataList: "
                                                            + encryptedDataList.getLength() + ", sendAppId: "
                                                            + sendAppId);

                                                    if (!EncryptionUtils.isKeyExistInKMU(conn, bodyDoc)) {
                                                        logWarn("[MaintainMessageSessionEJB]processMessageByBatchPull - Key not found in processMessageByBatchPull.");
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

                                                    logInfo("[MaintainMessageSessionEJB]processMessageByBatchPull - This message is not encrypted in-transit. encryptedDataList: "
                                                            + encryptedDataList.getLength() + ", sendAppId: "
                                                            + sendAppId);

                                                    maintMsgReq = SoapUtils.documentToJaxb(bodyDoc,
                                                            MaintainMessageRequest.class);

                                                }

                                                conn.begin(null, null, HPFW_Connection.DIRECT_WITH_HISTORY);

                                                if (maintMsgReq != null) {
                                                    // maintMsgResponse = maintainMessageDAO.processMessage(conn, sendAppId, maintMsgReq);
                                                    maintMsgResponse = MaintainMessageResponseMapper
                                                            .toJaxb(maintainMessageProcessor.processMessage(conn,
                                                                    sendAppId, MaintainMessageRequestMapper
                                                                            .fromJaxb(maintMsgReq)));
                                                }

                                                conn.commit();

                                            }
                                        }

                                    } catch (Exception pEx) {
                                        logError(
                                                "[MaintainMessageSessionEJB]processMessageByBatchPull - General exception caught in maintainMessageDAO.processMessage",
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
                                                    "[MaintainMessageSessionEJB]processMessageByBatchPull - General exception raised in send ResponseMsg by Async, scopesMsgId: "
                                                            + scopesMsgId + "scopesCorrelationId: " + scopesCorrelationId
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

                                                logWarn("[MaintainMessageSessionEJB]processMessageByBatchPull - [SOAP_FAULT] sendAsyncRequest: DetailCode: "
                                                        + soapFaultEntry.getDetailCode() + ", DetailDescription: "
                                                        + soapFaultEntry.getDetailDescription() + ", FaultCode: "
                                                        + soapFaultEntry.getFaultCode() + ", FaultString: "
                                                        + soapFaultEntry.getFaultString());

                                                if (IErrorCodes.WS_WARN_ASYNC_DUPLICATE_RESPONSE.equals(detailCode)) {

                                                    logWarn("[MaintainMessageSessionEJB]processMessageByBatchPull - sendMsgResponseByAsyn response msg already sent before: scopesMsgId: "
                                                            + scopesMsgId + "scopesCorrelationId: "
                                                            + scopesCorrelationId);
                                                    returnResponseSuccess = true;
                                                }

                                            }
                                        }

                                        if (returnResponseSuccess) {
                                            try {

                                                logInfo("[MaintainMessageSessionEJB]processMessageByBatchPull - Send response success. scopesMsgId: " + scopesMsgId);

                                                conn.begin(null, null, HPFW_Connection.DIRECT_WITH_HISTORY);

                                                if (createAsynSuccess) {
                                                    asynMessageDAO.updateAsynMessageStatus(conn, scopesMsgId,
                                                            CmcAppConstants.ASYN_MSG_STATUS_RESPONSE_SENT);
                                                } else {
                                                    asynMessageDAO.createAsynMessageRecord(conn, scopesMsgId,
                                                            maintMsgReqRecptAppId, maintMsgReqRecptAppType, null,
                                                            CmcAppConstants.ASYN_MSG_STATUS_RESPONSE_SENT);
                                                }

                                                conn.commit();

                                            } catch (Exception e1) {
                                                logError("[MaintainMessageSessionEJB]processMessageByBatchPull - updateAsynMessageStatus failed. scopesMsgId: "
                                                        + scopesMsgId);
                                            }
                                        } else {
                                            logWarn("[MaintainMessageSessionEJB]processMessageByBatchPull - sendMsgResponseByAsyn send response msg failed. scopesMsgId: "
                                                    + scopesMsgId);

                                            asynMessageDAO.updateAsynMessage(conn, scopesMsgId, responseSoapMsg,
                                                    CmcAppConstants.ASYN_MSG_STATUS_ACKNOWLEDGED);
                                            conn.commit();

                                        }
                                    }
                                }
                            }
                        } catch (Exception ex) {

                            logError("[MaintainMessageSessionEJB]processMessageByBatchPull - General exception raised, scopesMsgId: "
                                    + scopesMsgId + ", scopesCorrelationId: " + scopesCorrelationId + ": ", ex);
                        }

                        logInfo("[MaintainMessageSessionEJB]processMessageByBatchPull - SenderAppId: " + getAsynSenderAppId
                                + ", releaseLock: " + releaseLock + ", jobControlUtils is null: " + (jobControlUtils == null)
                                + ", jobControlName: " + jobControlName + ", after processing message.");
                        if (!releaseLock) {
                            if (jobControlUtils != null) {
                                jobControlUtils.releaseJobControl(jobControlName);
                                releaseLock = true;
                                logInfo("[MaintainMessageSessionEJB]processMessageByBatchPull - SenderAppId: " + getAsynSenderAppId
                                        + ", released lock after processing message.");
                            }
                        }

                    } else {
                        logInfo("[MaintainMessageSessionEJB]processMessageByBatchPull - SenderAppId: " + getAsynSenderAppId + ", another job is running");
                        Thread.sleep(500);
                    }
                }

            }

        } catch (RuntimeException e) {
            logError("[MaintainMessageSessionEJB]processMessageByBatchPull - Runtime exception raised, e: ", e);
            throw e;
        } catch (Exception e) {
            logError("[MaintainMessageSessionEJB]processMessageByBatchPull - General exception raised, e: ", e);
            throw new EJBException(e);
        } finally {
            try {
                if (conn != null)
                    HPFW_Connection.close(conn);
            } catch (Exception ex) {
                logError("[MaintainMessageSessionEJB]processMessageByBatchPull - General exception caught in finally block - conn.close();", ex);
            }

            logInfo("[MaintainMessageSessionEJB]processMessageByBatchPull - finally releaseLock: " + releaseLock + ", jobControlUtils is null: " + (jobControlUtils == null)
                    + ", jobControlName: " + jobControlName);
            if (!releaseLock) {
                if (jobControlUtils != null) {
                    jobControlUtils.releaseJobControl(jobControlName);
                    releaseLock = true;
                    logInfo("[MaintainMessageSessionEJB]processMessageByBatchPull - finally released lock.");
                }
            }

        }

        logInfo("[MaintainMessageSessionEJB]processMessageByBatchPull - END");
    }

    private SOAPMessage getMessageBySinglePull(String senderAppId) throws EJBException {
        logInfo("[MaintainMessageSessionEJB]getMessageBySinglePull senderAppId[" + senderAppId + "] - START");

        SOAPMessage responseMsg = null;

        try {

            Properties properties = cmcEnvProperties.getProperties();

            properties.forEach((k, v) -> {
                logDebug("[cmcEnvProperties] " + k + " = " + v);
            });

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

                    logWarn("[MaintainMessageSessionEJB] [SOAP_FAULT] sendSinglePullRequest: DetailCode: "
                            + soapFaultEntry.getDetailCode() + ", DetailDescription: "
                            + soapFaultEntry.getDetailDescription() + ", FaultCode: " + soapFaultEntry.getFaultCode()
                            + ", FaultString: " + soapFaultEntry.getFaultString());

                }
            }

        } catch (RuntimeException e) {
            logError("[MaintainMessageSessionEJB]Runtime exception raised in getMessageBySinglePull", e);
            throw e;
        } catch (Exception e) {
            logError("[MaintainMessageSessionEJB]General exception raised in getMessageBySinglePull", e);
            throw new EJBException(e);
        }

        logInfo("[MaintainMessageSessionEJB]getMessageBySinglePull senderAppId[" + senderAppId + "] - END");

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
                logWarn("[MaintainMessageSessionEJB]sendMsgResponseByAsyn send pull ack failed: " + scopesMsgId);
                result = false;
            } else {
                logInfo("[MaintainMessageSessionEJB]sendPullAck success scopesMsgId: " + scopesMsgId);
                result = true;
            }

        } catch (Exception ex) {
            result = false;
        }

        return result;
    }

}
