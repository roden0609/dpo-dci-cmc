package hk.gov.cmc.appserver.ejb.session.asynmessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import hk.gov.cmc.common.CmcAppConstants;
import hk.gov.cmc.common.CmcAppPropertyNames;
import hk.gov.cmc.config.CmcEnvProperties;
import hk.gov.cmc.dao.asynmessage.AsynMessageDAO;
import hk.gov.cmc.model.asynmessage.AsynMessage;
import hk.gov.cmc.persistence.connection.hpfw.HPFW_Connection;
import hk.gov.cmc.utils.common.WebServiceUtils;
import hk.gov.gcis.rm.common.javaee.ejb.EJBBase;
import hk.gov.gcis.ss.common.utils.SoapUtils;
import hk.gov.gcis.ss.common.webserver.elements.FaultEntry;
import hk.gov.gcis.ss.messaging.client.MessagingClient;
import hk.gov.gcis.ss.messaging.fault.IErrorCodes;
import jakarta.ejb.EJBException;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionManagement;
import jakarta.ejb.TransactionManagementType;
import jakarta.xml.soap.SOAPMessage;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class AsynMessageSessionEJB extends EJBBase
        implements IAsynMessageSessionBMRemote, IAsynMessageSessionBMLocal {

    private final static CmcEnvProperties cmcEnvProperties = new CmcEnvProperties();
    private static Log logger = LogFactory.getLog(AsynMessageSessionEJB.class);

    public void sendMsgResponseByAsyn() throws EJBException {
        logger.info("[AsynMessageSessionEJB]sendMsgResponseByAsyn - START");

        HPFW_Connection conn = null;

        try {

            Properties properties = cmcEnvProperties.getProperties();

            MessagingClient msgClient = new MessagingClient(properties);

            conn = HPFW_Connection.getHPFW_Connection(false);

            conn.begin(null, null, HPFW_Connection.DIRECT_WITH_HISTORY);

            AsynMessageDAO asynMessageDAO = new AsynMessageDAO();

            List<AsynMessage> responseMsgList = asynMessageDAO.getOutstandingMessage(conn);

            logger.info("[AsynMessageSessionEJB]sendMsgResponseByAsyn - number of outstanding message: "
                    + responseMsgList.size());

            for (int i = 0; i < responseMsgList.size(); i++) {
                AsynMessage asynMessage = (AsynMessage) responseMsgList.get(i);
                String scopesMessageId = asynMessage.getScopesMessageId();
                String recipientAppId = asynMessage.getRecipientAppId();
                String recipientAppType = asynMessage.getRecipientAppType();
                String status = asynMessage.getStatus();
                String rspAsynMsgRequestStr = asynMessage.getMsgResponse();

                if (CmcAppConstants.ASYN_MSG_STATUS_RECEIVED.equals(status)) {

                    try {
                        ArrayList<String> msgIdList = new ArrayList<>();
                        msgIdList.add(scopesMessageId);

                        SOAPMessage pullAckRequest = msgClient.createPullAckRequest(recipientAppId, recipientAppType,
                                msgIdList);

                        logger.info("[AsynMessageSessionEJB]sendMsgResponseByAsyn - pullAckRequest scopesMsgId["
                                + scopesMessageId + "]: \n"
                                + WebServiceUtils.soapMessageToString(pullAckRequest));

                        SOAPMessage pullAckResponse = msgClient.sendPullAckRequest(pullAckRequest);

                        logger.info("[AsynMessageSessionEJB]sendMsgResponseByAsyn - pullAckResponse scopesMsgId["
                                + scopesMessageId + "]: \n"
                                + WebServiceUtils.soapMessageToString(pullAckResponse));

                        if (pullAckResponse.getSOAPPart().getEnvelope().getBody().hasFault()) {
                            logger.warn("[AsynMessageSessionEJB]sendMsgResponseByAsyn - send pull ack failed: "
                                    + scopesMessageId);
                        } else {

                            if (rspAsynMsgRequestStr == null || rspAsynMsgRequestStr.length() == 0) {
                                logger.warn("[AsynMessageSessionEJB]sendMsgResponseByAsyn - rspAsynMsgRequestStr is null or empty, scopesMessageId="
                                        + scopesMessageId + ", so just can send ack and mark status as S.");
                                asynMessageDAO.updateAsynMessageStatus(conn, scopesMessageId,
                                        CmcAppConstants.ASYN_MSG_STATUS_RESPONSE_SENT);
                            } else {
                                asynMessageDAO.updateAsynMessageStatus(conn, scopesMessageId,
                                        CmcAppConstants.ASYN_MSG_STATUS_ACKNOWLEDGED);
                            }
                        }

                    } catch (Exception e1) {
                        logger.error("[AsynMessageSessionEJB]sendMsgResponseByAsyn - Send Ack failed msgId: "
                                + scopesMessageId, e1);
                    }
                }

                try {

                    if (rspAsynMsgRequestStr == null || rspAsynMsgRequestStr.length() == 0) {
                        logger.warn("[AsynMessageSessionEJB]sendMsgResponseByAsyn - rspAsynMsgRequestStr is null or empty, scopesMessageId="
                                + scopesMessageId + ", so cannot send response.");
                    } else {

                        SOAPMessage rspAsynMsgRequest = SoapUtils.smartGenerateMessage(rspAsynMsgRequestStr);

                        String soapaction = properties
                                .getProperty(CmcAppPropertyNames.ASYN_MSG_SOAP_ACTION_PROPERTY_NAME);
                        rspAsynMsgRequest.getMimeHeaders().setHeader(SoapUtils.SOAP_ACTION, soapaction);

                        logger.info("[AsynMessageSessionEJB]sendMsgResponseByAsyn - rspAsynMsgRequest scopesMsgId["
                                + scopesMessageId + "]: \n" + WebServiceUtils.soapMessageToString(rspAsynMsgRequest));

                        SOAPMessage rspAsynMsgResponse = msgClient.sendAsyncRequest(rspAsynMsgRequest);

                        logger.info("[AsynMessageSessionEJB]sendMsgResponseByAsyn - rspAsynMsgResponse scopesMsgId["
                                + scopesMessageId + "]: \n" + WebServiceUtils.soapMessageToString(rspAsynMsgResponse));

                        boolean returnResponseSuccess = true;
                        if (rspAsynMsgResponse.getSOAPPart().getEnvelope().getBody().hasFault()) {
                            returnResponseSuccess = false;

                            FaultEntry soapFaultEntry = SoapUtils.getFaultEntry(rspAsynMsgResponse);
                            if (soapFaultEntry != null) {
                                String detailCode = soapFaultEntry.getDetailCode();

                                logger.warn("[AsynMessageSessionEJB]sendMsgResponseByAsyn - SoapFault FaultCode: "
                                        + soapFaultEntry.getFaultCode());
                                logger.warn("[AsynMessageSessionEJB]sendMsgResponseByAsyn - SoapFault FaultString: "
                                        + soapFaultEntry.getFaultString());
                                logger.warn("[AsynMessageSessionEJB]sendMsgResponseByAsyn - SoapFault DetailCode: "
                                        + soapFaultEntry.getDetailCode());
                                logger.warn("[AsynMessageSessionEJB]sendMsgResponseByAsyn - SoapFault DetailDescription: "
                                        + soapFaultEntry.getDetailDescription());

                                if (IErrorCodes.WS_WARN_ASYNC_DUPLICATE_RESPONSE.equals(detailCode)) {
                                    logger.warn("[AsynMessageSessionEJB]sendMsgResponseByAsyn - response msg already sent before: "
                                            + scopesMessageId);
                                    returnResponseSuccess = true;
                                }
                            }
                        }

                        if (returnResponseSuccess) {
                            asynMessageDAO.updateAsynMessage(conn, scopesMessageId, null,
                                    CmcAppConstants.ASYN_MSG_STATUS_RESPONSE_SENT);
                        } else {
                            logger.warn("[AsynMessageSessionEJB]sendMsgResponseByAsyn - send response msg failed: "
                                    + scopesMessageId);
                        }
                    }

                } catch (Exception e1) {
                    logger.error("[AsynMessageSessionEJB]sendMsgResponseByAsyn - Send response failed msgId="
                            + scopesMessageId, e1);
                }
            }

            conn.commit();

        } catch (RuntimeException e) {
            logger.error("[AsynMessageSessionEJB]sendMsgResponseByAsyn - Runtime exception raised", e);
            throw e;
        } catch (Exception e) {
            logger.error("[AsynMessageSessionEJB]sendMsgResponseByAsyn - General exception raised", e);
            throw new EJBException(e);
        } finally {
            try {
                if (conn != null)
                    HPFW_Connection.close(conn);
            } catch (Exception ex) {
                logger.error("[AsynMessageSessionEJB]sendMsgResponseByAsyn - General exception caught in conn.close();",
                        ex);
            }
        }

        logger.info("[AsynMessageSessionEJB]sendMsgResponseByAsyn - END");
    }
}
