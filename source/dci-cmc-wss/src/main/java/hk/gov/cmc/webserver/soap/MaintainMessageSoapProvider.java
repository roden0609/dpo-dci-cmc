package hk.gov.cmc.webserver.soap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.security.Init;
import org.apache.xml.security.utils.EncryptionConstants;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import hk.gov.cmc.appserver.ejb.session.maintainmessage.IMaintainMessageSessionBM;
import hk.gov.cmc.appserver.ejb.session.maintainmessage.IMaintainMessageSessionBMLocal;
import hk.gov.cmc.common.CmcAppConstants;
import hk.gov.cmc.common.CmcAppPropertyNames;
import hk.gov.cmc.common.ResultCodes;
import hk.gov.cmc.common.ResultMessages;
import hk.gov.cmc.config.CmcEnvProperties;
import hk.gov.cmc.jaxb.maintainmessage.MaintainMessageRequest;
import hk.gov.cmc.jaxb.maintainmessage.MaintainMessageResponse;
import hk.gov.cmc.persistence.connection.hpfw.HPFW_Connection;
import hk.gov.cmc.utils.common.EncryptionUtils;
import hk.gov.gcis.rm.keyservice.appserver.AppPropertyNames;
import hk.gov.gcis.rm.keyservice.appserver.ejb.session.IPKIUtil;
import hk.gov.gcis.rm.keyservice.common.Constants;
import hk.gov.gcis.ss.common.utils.SoapUtils;
import jakarta.ejb.EJB;
// import jakarta.jws.WebService;
// import jakarta.jws.soap.SOAPBinding;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.soap.MessageFactory;
import jakarta.xml.soap.SOAPBody;
import jakarta.xml.soap.SOAPBodyElement;
import jakarta.xml.soap.SOAPEnvelope;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.ws.Provider;
import jakarta.xml.ws.Service;
import jakarta.xml.ws.ServiceMode;
import jakarta.xml.ws.WebServiceProvider;

// @WebService(serviceName = "MaintainMessageService", portName = "MaintainMessagePort", targetNamespace = "http://cmc.gov.hk/ws/maintainmessage")
// @SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL)
@WebServiceProvider(serviceName = "MaintainMessageService", portName = "MaintainMessagePort", targetNamespace = "http://ws.mygovhk.gov.hk/schema/MaintainMessageRequest.xsd")
@ServiceMode(Service.Mode.MESSAGE)
public class MaintainMessageSoapProvider implements Provider<Source> {

    private static Log logger = LogFactory.getLog(MaintainMessageSoapProvider.class);
    private final static CmcEnvProperties cmcEnvProperties = new CmcEnvProperties();

    @EJB
    private IMaintainMessageSessionBMLocal maintainMessageEJB;

    static {
        Init.init();
    }

    // @WebMethod
    // public SOAPMessage processMessage(SOAPMessage request) {
    // logger.info("processMessage - BEGIN");
    // try {
    // SOAPMessage response = MessageFactory.newInstance().createMessage();
    // return processMaintainMessage(request, response);
    // } catch (Exception e) {
    // logger.error("General exception caught in processMessage", e);
    // return null;
    // } finally {
    // logger.info("processMessage - END");
    // }
    // }
    @Override
    public Source invoke(Source request) {
        try {
            logger.info("invoke - BEGIN");

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(request, new StreamResult(byteArrayOutputStream));
            InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

            MessageFactory messageFactory = MessageFactory.newInstance();

            SOAPMessage requestMsg = messageFactory.createMessage(null, inputStream);
            requestMsg.saveChanges();

            SOAPMessage response = processMaintainMessage(requestMsg, MessageFactory.newInstance().createMessage());

            return response.getSOAPPart().getContent();
        } catch (Exception e) {
            logger.error("General exception caught in invoke", e);
            return null;
        } finally {
            logger.info("invoke - END");
        }
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
            NodeList nodeList = envelope.getHeader().getElementsByTagNameNS("*", CmcAppConstants.APPID_TAG_NAME);
            logger.info("processMaintainMessage nodeList == null: " + (nodeList == null));
            logger.info("processMaintainMessage nodeList.getLength: " + (nodeList != null ? nodeList.getLength() : "null"));
            if (nodeList != null && nodeList.getLength() > 0)
                appId = nodeList.item(0).getFirstChild().getNodeValue();
            if (appId == null || appId.length() == 0) {
                return generateResponse(responseMsg, ResultCodes.RESULT_CD_SENDER_APPID_NOT_FOUND,
                        ResultMessages.RESULT_MSG_SENDER_APPID_NOT_FOUND);
            }

            logger.info("processMaintainMessage - appId=" + appId);

            // SOAPBodyElement body = (SOAPBodyElement) SoapUtils.getFirstBodyElement(requestMsg);
            // Document bodyDoc = toDocument(body);
            SOAPBody body = requestMsg.getSOAPPart().getEnvelope().getBody();
            Document bodyDoc = body.extractContentAsDocument();
            logger.debug("Request body: " + documentToString(bodyDoc));

            Properties properties = cmcEnvProperties.getProperties();

            String cmcToDciCmcAppIdMap = properties
                    .getProperty(CmcAppPropertyNames.CMC_TO_DCI_CMC_APP_ID_MAP_PROPERTY_NAME);
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

                if (!EncryptionUtils.isKeyExistInKMU(conn, bodyDoc)) {
                    logger.warn("Key not found in processMaintainMessage.");
                    return generateResponse(responseMsg, ResultCodes.RESULT_CD_GENERAL_ERROR,
                            ResultMessages.RESULT_MSG_GENERAL_ERROR);
                } else {

                    String pkiUtilJNDIName = properties.getProperty(
                            AppPropertyNames.PROPERTY_NAME_PKI_UTIL_EJB_REMOTE_JNDI_NAME,
                            CmcAppConstants.GLOBAL_CONTEXT_NAME + CmcAppConstants.CONTEXT_NAME_SEPARATOR
                                    + Constants.GCIS_RM_KEYSERVICE_MODULE_NAME + CmcAppConstants.CONTEXT_NAME_SEPARATOR
                                    + Constants.GCIS_RM_KEYSERVICE_MODULE_NAME
                                    + CmcAppConstants.DEFAULT_EJB_MODULE_NAME_SUFFIX
                                    + CmcAppConstants.CONTEXT_NAME_SEPARATOR
                                    + "PKIUtil!hk.gov.gcis.rm.keyservice.appserver.ejb.session.IPKIUtil");

                    InitialContext context = new InitialContext(properties);
                    IPKIUtil pkiUtil = (IPKIUtil) context.lookup(pkiUtilJNDIName);

                    Document decDoc = pkiUtil.decryptXML(null, bodyDoc, null);

                    JAXBContext jaxbContext = JAXBContext.newInstance(MaintainMessageRequest.class);
                    Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
                    JAXBElement<MaintainMessageRequest> root = unmarshaller.unmarshal(decDoc,
                            MaintainMessageRequest.class);
                    maintainMessageRequest = root.getValue();
                }
            } else {
                logger.info(
                        "This message is not encrypted in-transit. encryptedDataList: " + encryptedDataList.getLength()
                                + ", appId: " + appId);
                JAXBContext jaxbContext = JAXBContext.newInstance(MaintainMessageRequest.class);
                Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
                JAXBElement<MaintainMessageRequest> root = unmarshaller.unmarshal(bodyDoc,
                        MaintainMessageRequest.class);
                maintainMessageRequest = root.getValue();
            }

            InitialContext ctx = new InitialContext();
            IMaintainMessageSessionBM maintainMessageEJB = (IMaintainMessageSessionBM) ctx.lookup(
                    "java:global/dci-cmc-app/dci-cmc-ejb/MaintainMessageSessionEJB!hk.gov.cmc.appserver.ejb.session.maintainmessage.IMaintainMessageSessionBMLocal");

            MaintainMessageResponse maintainMessageResponse = maintainMessageEJB.processMessage(appId,
                    maintainMessageRequest);

            logger.info("processMaintainMessage - END");

            return generateResponse(responseMsg, maintainMessageResponse);

        } catch (Exception ex) {
            logger.error("General exception caught in processMaintainMessage", ex);
            return generateResponse(responseMsg, ResultCodes.RESULT_CD_GENERAL_ERROR,
                    ResultMessages.RESULT_MSG_GENERAL_ERROR);
        }

    }

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
        SOAPEnvelope envelope = responseMsg.getSOAPPart().getEnvelope();
        SOAPBody body = envelope.getBody();
        body.removeContents();

        JAXBContext jaxbContext = JAXBContext.newInstance(MaintainMessageResponse.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);
        marshaller.marshal(maintainMessageResponse, body);
        responseMsg.saveChanges();
        return responseMsg;
    }

    private Document toDocument(org.w3c.dom.Node node) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();
        document.appendChild(document.importNode(node, true));
        return document;
    }

    private String documentToString(Document document) throws Exception {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.setOutputProperty(OutputKeys.INDENT, "no");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        transformer.transform(new DOMSource(document), new StreamResult(outputStream));
        return outputStream.toString("UTF-8");
    }

}
