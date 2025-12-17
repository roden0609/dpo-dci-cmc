package hk.gov.cmc.webserver.soap;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import jakarta.ejb.EJB;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.Marshaller;

import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.soap.SOAPBody;
import jakarta.xml.soap.SOAPBodyElement;
import jakarta.xml.soap.SOAPEnvelope;
import jakarta.xml.soap.SOAPException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.wss4j.dom.WSConstants;
import org.apache.wss4j.dom.handler.WSHandlerResult;
import org.apache.wss4j.dom.handler.RequestData;
import org.apache.wss4j.dom.engine.WSSecurityEngine;
import org.apache.wss4j.dom.engine.WSSecurityEngineResult;

import org.apache.xml.security.Init;
import org.apache.xml.security.utils.EncryptionConstants;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.naming.InitialContext;
import java.util.List;
import java.util.Map;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Properties;

import hk.gov.cmc.appserver.ejb.session.maintainmessage.IMaintainMessageSessionBM;
import hk.gov.cmc.common.Constants;
import hk.gov.cmc.domain.common.ServiceLocator;
import hk.gov.cmc.jaxb.maintainmessage.MaintainMessageRequest;
import hk.gov.cmc.jaxb.maintainmessage.MaintainMessageResponse;

@WebService(serviceName = "MaintainMessageService", portName = "MaintainMessagePort", targetNamespace = "http://cmc.gov.hk/ws/maintainmessage")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL)
public class MaintainMessageSoapProvider {

    private static Log logger = LogFactory.getLog(MaintainMessageSoapProvider.class);
    private static final String APPID_TAG_NAME = "SenderAppId";

    @EJB
    private IMaintainMessageSessionBM maintainMessageEJB;

    static {
        Init.init();
    }

    public void processMessage(SOAPEnvelope request, SOAPEnvelope response) {
        logger.info("processMessage - BEGIN");

        try {
            MessageContext mc = MessageContext.getCurrentContext();
            Message requestMsg = mc.getRequestMessage();
            Message responseMsg = mc.getResponseMessage();
            processMaintainMessage(requestMsg, responseMsg);
            mc.setResponseMessage(responseMsg);

        } catch (Exception e) {
            logger.error("General exception caught in processMessage", e);
        }

        logger.info("processMessage - END");
    }

    private static String soapMessageToString(SOAPMessage message) throws Exception {

        String result = "";

        if (message == null)
            return null;
        ByteArrayOutputStream bas = new ByteArrayOutputStream(4096);
        message.writeTo(bas);
        result = bas.toString("UTF-8");

        return result;
    }

    private SOAPMessage processMaintainMessage(SOAPMessage requestMsg, SOAPMessage responseMsg) throws Exception {
        logger.info("processMaintainMessage - BEGIN");

        try {

            logger.info("processMaintainMessage requestMsg=\n" + soapMessageToString(requestMsg));

            String appId = null;
            SOAPEnvelope envelope = (SOAPEnvelope) requestMsg.getSOAPPart().getEnvelope();
            Document envDoc = envelope.getAsDocument();
            NodeList nodeList = envDoc.getElementsByTagName(APPID_TAG_NAME);
            if (nodeList != null && nodeList.getLength() > 0)
                appId = nodeList.item(0).getFirstChild().getNodeValue();
            if (appId == null || appId.length() == 0) {
                return generateResponse(responseMsg, Constants.RESULT_CD_SENDER_APPID_NOT_FOUND,
                        Constants.RESULT_MSG_SENDER_APPID_NOT_FOUND);
            }

            logger.info("processMaintainMessage - appId=" + appId);

            SOAPBodyElement body = (SOAPBodyElement) SoapUtils.getFirstBodyElement(requestMsg);
            Document bodyDoc = body.getAsDocument();
            logger.debug("Request body: " + XMLUtils.DocumentToString(bodyDoc));

            Properties properties = ServiceLocator.getInstance().getProperties();

            String cmcToDciCmcAppIdMap = properties
                    .getProperty(AppPropertyName.CMC_TO_DCI_CMC_APP_ID_MAP_PROPERTY_NAME);
            Map<String, String> cmcToDciMap = parseCmcToDciCmcAppIdMap(cmcToDciCmcAppIdMap);

            if (cmcToDciMap.containsKey(appId)) {
                logger.info("processMaintainMessage - appId " + appId + " found in CMC_TO_DCI_CMC_APP_ID_MAP");
                appId = cmcToDciMap.get(appId);
                logger.info("processMaintainMessage - map appId from CMC " + appId + " to DCI-CMC " + appId);
            }

            NodeList encryptedDataList = bodyDoc.getDocumentElement().getElementsByTagNameNS(
                    EncryptionConstants.EncryptionSpecNS, EncryptionConstants._TAG_ENCRYPTEDDATA);

            MaintainMessageRequest maintainMessageRequest = null;

            if (encryptedDataList.getLength() > 0) {

                logger.info("This message is encrypted in-transit. encryptedDataList: " + encryptedDataList.getLength()
                        + ", appId: " + appId);

                HPFW_Connection conn = HPFW_Connection.getHPFW_Connection(false);

                EncUtils encUtils = new EncUtils();

                if (!encUtils.keyExistInKMU(conn, bodyDoc)) {
                    logger.warn("Key not found in processMaintainMessage.");
                    return generateResponse(responseMsg, Constants.RESULT_CD_GENERAL_ERROR,
                            Constants.RESULT_MSG_GENERAL_ERROR);
                } else {

                    String pkiUtilJNDIName = properties.getProperty(PROPERTY_NAME_PKI_UTIL_EJB_REMOTE_JNDI_NAME,
                            CommonConstants.GLOBAL_CONTEXT_NAME + CommonConstants.CONTEXT_NAME_SEPARATOR
                                    + EGIS_RM_KEYSERVICE_MODULE_NAME + CommonConstants.CONTEXT_NAME_SEPARATOR
                                    + EGIS_RM_KEYSERVICE_MODULE_NAME + CommonConstants.DEFAULT_EJB_MODULE_NAME_SUFFIX
                                    + CommonConstants.CONTEXT_NAME_SEPARATOR
                                    + "PKIUtil!hk.gov.ogcio.egis.rm.keyservice.appserver.ejb.session.IPKIUtil");

                    InitialContext context = new InitialContext(properties);
                    IPKIUtil pkiUtil = (IPKIUtil) context.lookup(pkiUtilJNDIName);

                    Document decDoc = pkiUtil.decryptXML(null, bodyDoc, null);

                    maintainMessageRequest = (MaintainMessageRequest) Unmarshaller
                            .unmarshal(MaintainMessageRequest.class, decDoc);
                }
            } else {
                logger.info(
                        "This message is not encrypted in-transit. encryptedDataList: " + encryptedDataList.getLength()
                                + ", appId: " + appId);
                maintainMessageRequest = (MaintainMessageRequest) Unmarshaller.unmarshal(MaintainMessageRequest.class,
                        bodyDoc);
            }

            InitialContext ctx = new InitialContext();
            IMaintainMessageSessionBM maintainMessageEJB = (IMaintainMessageSessionBM) ctx.lookup(
                    "java:global/cmc/cmc_app/MaintainMessageSessionEJB!hk.gov.ogcio.mars_cmc.cmc.appserver.ejb.session.maintainmessage.IMaintainMessageSessionBMLocal");

            MaintainMessageResponse maintainMessageResponse = maintainMessageEJB.processMessage(appId,
                    maintainMessageRequest);

            logger.info("processMaintainMessage - END");

            return generateResponse(responseMsg, maintainMessageResponse);

        } catch (Exception ex) {
            logger.error("General exception caught in processMaintainMessage", ex);
            return generateResponse(responseMsg, Constants.RESULT_CD_GENERAL_ERROR, Constants.RESULT_MSG_GENERAL_ERROR);
        }

    }

    // CMC-2025-034: CMC send RVD VIP request to DCI-CMC in RPC mode - BEGIN
    private Map<String, String> parseCmcToDciCmcAppIdMap(String cmcToDciCmcAppIdMap) {
        Map<String, String> result = new HashMap<String, String>();
        if (StringUtils.isEmpty(cmcToDciCmcAppIdMap)) {
            return result;
        }

        String[] mappingPairs = cmcToDciCmcAppIdMap.split(",");
        for (String mappingPair : mappingPairs) {
            String trimmedMappingPair = mappingPair.trim();
            if (trimmedMappingPair.length() == 0) {
                continue;
            }
            String[] mapping = trimmedMappingPair.split("\\|");
            if (mapping.length != 2) {
                logger.warn("parseCmcToDciCmcAppIdMap - invalid mapping format: " + trimmedMappingPair);
                continue;
            }
            String cmcAppId = mapping[0].trim();
            String dciCmcAppId = mapping[1].trim();
            if (cmcAppId.length() == 0 || dciCmcAppId.length() == 0) {
                logger.warn("parseCmcToDciCmcAppIdMap - empty appId found in mapping: " + trimmedMappingPair);
                continue;
            }
            result.put(cmcAppId, dciCmcAppId);
        }
        return result;
    }

    private SOAPMessage generateResponse(SOAPMessage responseMsg, String resultCd, String resultMsg) throws Exception {

        MaintainMessageResponse maintainMessageResponse = new MaintainMessageResponse();
        maintainMessageResponse.setResultCode(resultCd);
        maintainMessageResponse.setResultMessage(resultMsg);
        return generateResponse(responseMsg, maintainMessageResponse);
    }

    private SOAPMessage generateResponse(SOAPMessage responseMsg, MaintainMessageResponse maintainMessageResponse)
            throws Exception {
        return CastorUtils.marshal(responseMsg, maintainMessageResponse);
    }

}
