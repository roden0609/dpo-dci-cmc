package hk.gov.dpo.mars_cmc.cmc.client.maintainmessage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.Vector;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;

import hk.gov.dpo.mars_cmc.cmc.client.common.PropertyNames;
import hk.gov.dpo.mars_cmc.cmc.client.utils.XmlEncUtils;
import hk.gov.dpo.mars_cmc.cmc.datatype.message.Application;
import hk.gov.dpo.mars_cmc.cmc.datatype.message.EMessage;
import hk.gov.dpo.mars_cmc.cmc.datatype.message.MessageRequest;
import hk.gov.dpo.mars_cmc.cmc.datatype.message.MessageResponse;
import hk.gov.dpo.mars_cmc.cmc.datatype.message.MetaData;
import hk.gov.dpo.mars_cmc.cmc.datatype.message.Recipient;
import hk.gov.dpo.mars_cmc.cmc.datatype.message.ToDoItem;
import hk.gov.dpo.mars_cmc.cmc.jaxb.maintainmessage.ActionST;
import hk.gov.dpo.mars_cmc.cmc.jaxb.maintainmessage.AppStatusST;
import hk.gov.dpo.mars_cmc.cmc.jaxb.maintainmessage.ApplicationCT;
import hk.gov.dpo.mars_cmc.cmc.jaxb.maintainmessage.EMessageCT;
import hk.gov.dpo.mars_cmc.cmc.jaxb.maintainmessage.MaintainMessageRequest;
import hk.gov.dpo.mars_cmc.cmc.jaxb.maintainmessage.MaintainMessageResponse;
import hk.gov.dpo.mars_cmc.cmc.jaxb.maintainmessage.MessageRequestCT;
import hk.gov.dpo.mars_cmc.cmc.jaxb.maintainmessage.MessageResponseCT;
import hk.gov.dpo.mars_cmc.cmc.jaxb.maintainmessage.MetaDataCT;
import hk.gov.dpo.mars_cmc.cmc.jaxb.maintainmessage.RecipientCT;
import hk.gov.dpo.mars_cmc.cmc.jaxb.maintainmessage.ToDoItemCT;
import hk.gov.gcis.ss.common.client.ClientBase;
import hk.gov.gcis.ss.common.utils.PropertiesUtils;
import hk.gov.gcis.ss.common.utils.SoapUtils;
import hk.gov.gcis.ss.messaging.client.MessagingClient;
import hk.gov.gcis.ss.messaging.jaxb.asynmsg.ScopesMessagingAcknowledgement;
import hk.gov.gcis.ss.messaging.types.IMessageType;
import hk.gov.gcis.ss.messaging.types.IMessagingConstants;
import hk.gov.gcis.ss.messaging.webserver.elements.singlepull.SinglePullResponse;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.soap.SOAPBodyElement;
import jakarta.xml.soap.SOAPMessage;

public class MaintainMessageClient extends ClientBase {

    private static final long serialVersionUID = 1L;
    private MessagingClient m_msgClient = null;
    private Properties m_properties = null;

    public final static String REQUEST_TYPE = "REQUEST";
    public final static String MAINT_MSG_XML_ENCRYPTION_ENABLED_YES = "Y";
    public final static Log log = LogFactory.getLog(MaintainMessageClient.class);

    public MaintainMessageClient(Properties properties) {
        super(properties);
        backwardCompatible();
        m_msgClient = new MessagingClient(properties);
        m_properties = properties;
    }

    public MaintainMessageClient(Properties properties, MessagingClient messagingClient) {
        super(properties);
        backwardCompatible();
        m_msgClient = messagingClient;
        m_properties = properties;
    }

    public void backwardCompatible() {
        String soapaction = getProperty("REQUEST_ACTION");
        String endpoint = getProperty("REQUEST_END_POINT");

        if (soapaction != null) {
            if (null == getProperty(PropertyNames.MAINT_MESSAGE_SOAP_ACTION_PROPERTY)) {
                setProperty(PropertyNames.MAINT_MESSAGE_SOAP_ACTION_PROPERTY, soapaction);
            }
        }
        if (endpoint != null) {
            if (null == getProperty(PropertyNames.MAINT_MESSAGE_END_POINT_PROPERTY)) {
                setProperty(PropertyNames.MAINT_MESSAGE_END_POINT_PROPERTY, endpoint);
            }
        }
    }

    public MaintainMessageRequest getMaintainMessageRequest(MessageRequest[] messageReqArray) throws Exception {

        // Create request
        MaintainMessageRequest maintainMessageRequest = new MaintainMessageRequest();

        for (int i = 0; i < messageReqArray.length; i++) {
            log.debug("messageReqArray[" + i + "]:" + messageReqArray[i].toString());
            // get message request data object
            MessageRequest messageRequest = messageReqArray[i];
            EMessage eMessage = messageRequest.getEMessage();
            ToDoItem toDoItem = messageRequest.getToDoItem();
            Application application = messageRequest.getApplication();
            List<MetaData> metaDataList = messageRequest.getMetaDataList();

            MessageRequestCT wsMessageRequest = new MessageRequestCT();

            // set authorized portal server to display the e-Message and to-do item
            wsMessageRequest.setPortalID(messageRequest.getPortalId());

            // create E-Message webservices object
            if (eMessage != null) {
                EMessageCT wsEMessage = new EMessageCT();
                wsEMessage.setTemplateID(eMessage.getTemplateID());
                wsEMessage.setTemplateVersion(eMessage.getTemplateVersion());
                wsMessageRequest.setEMessage(wsEMessage);
            }

            // create To-Do Item webservices object
            if (toDoItem != null) {
                ToDoItemCT wsToDoItem = new ToDoItemCT();
                wsToDoItem.setTemplateID(toDoItem.getTemplateID());
                wsToDoItem.setTemplateVersion(toDoItem.getTemplateVersion());
                wsMessageRequest.setToDoItem(wsToDoItem);
            }

            // create iAM Smart application status webservice object
            if (application != null) {
                ApplicationCT wsApplication = new ApplicationCT();
                wsApplication.setTemplateID(application.getTemplateID());
                wsApplication.setTemplateVersion(application.getTemplateVersion());
                wsMessageRequest.setApplication(wsApplication);
            }
            // create MetaData webservices object
            for (int j = 0; j < metaDataList.size(); j++) {
                log.debug("metaDataList[" + j + "]:" + metaDataList.get(j).toString());
                MetaData metaData = (MetaData) metaDataList.get(j);
                List<Recipient> recipientList = metaData.getRecipientList();
                MetaDataCT wsMetaData = new MetaDataCT();
                wsMetaData.setDataContentEN(metaData.getDataContentEN());
                wsMetaData.setDataContentTC(metaData.getDataContentTC());
                wsMetaData.setDataContentSC(metaData.getDataContentSC());

                // create Recipient webservices object
                for (int k = 0; k < recipientList.size(); k++) {
                    log.debug("recipientList[" + k + "]:" + recipientList.get(k).toString());
                    Recipient recipient = (Recipient) recipientList.get(k);
                    RecipientCT wsRecipient = new RecipientCT();
                    wsRecipient.setTranID(recipient.getTranID());
                    wsRecipient.setIdpID(recipient.getIdpID());
                    wsRecipient.setRecipientID(recipient.getRecipientID());
                    if (recipient.getItemDate() != null) {
                        wsRecipient.setItemDate(DatatypeFactory.newInstance()
                                .newXMLGregorianCalendar(recipient.getItemDate().toInstant().toString()));
                    }
                    wsRecipient.setAction(ActionST.fromValue(recipient.getAction()));
                    wsRecipient.setCorrelatedTranID(recipient.getCorrelatedTranID());

                    // CMC-2024-010: Enhance CMC Client To Retrieve iAM Smart Application Status - BEGIN
                    wsRecipient.setRecipientIDType(recipient.getRecipientIDType());
                    wsRecipient.setAppRefNum(recipient.getAppRefNum());
                    if (recipient.getAppStatus() != null && !recipient.getAppStatus().equals("")) {
                        wsRecipient.setAppStatus(AppStatusST.fromValue(recipient.getAppStatus()));
                    }
                    if (recipient.getAppStatusUpdateDate() != null) {
                        wsRecipient.setAppStatusUpdateDate(DatatypeFactory.newInstance()
                                .newXMLGregorianCalendar(recipient.getAppStatusUpdateDate().toInstant().toString()));
                    }
                    wsRecipient.setContactNum(recipient.getContactNum());
                    wsRecipient.setContactEmail(recipient.getContactEmail());
                    wsRecipient.setMiscInfo(recipient.getMiscInfo());
                    wsRecipient.setRecipientIDType(recipient.getRecipientIDType());
                    wsMetaData.getRecipient().add(wsRecipient);
                }
                wsMessageRequest.getMetaData().add(wsMetaData);
            }
            maintainMessageRequest.getMessageRequest().add(wsMessageRequest);
        }

        return maintainMessageRequest;
    }

    public SOAPMessage sendMaintainMessageRequest(MessageRequest[] messageReqArray) throws Exception {

        // Create request
        MaintainMessageRequest maintainMessageRequest = getMaintainMessageRequest(messageReqArray);

        StringWriter sw = new StringWriter();
        JAXBContext context = JAXBContext.newInstance(MaintainMessageRequest.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.marshal(maintainMessageRequest, sw);
        sw.close();

        SOAPMessage requestMessage = SoapUtils.emptyMessage();

        if (MAINT_MSG_XML_ENCRYPTION_ENABLED_YES
                .equals(m_properties.getProperty(PropertyNames.MAINT_MSG_XML_ENCRYPTION_ENABLED_PROPERTY_NAME))) {

            // Encrypt MaintainMessageRequest
            String friendlyAlias = "session_key";

            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setNamespaceAware(true);
            DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
            ByteArrayInputStream inStream = new ByteArrayInputStream(sw.toString().getBytes("UTF-8"));
            Document rawDoc = builder.parse(inStream);

            XmlEncUtils xmlEncUtils = new XmlEncUtils();

            Document encDoc = xmlEncUtils.encryptXML(m_properties, friendlyAlias, rawDoc);

            inStream.close();
            inStream = null;

            StringWriter sw2 = new StringWriter();
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            transformer.transform(new DOMSource(encDoc), new StreamResult(sw2));
            SoapUtils.addBody(requestMessage, sw2.toString());
        } else {
            SoapUtils.addBody(requestMessage, sw.toString());
        }
        // MyGov6-C2-003: Enable at-transit encryption when B/Ds send messages to CMC - END

        String soapaction = getProperty(PropertyNames.MAINT_MESSAGE_SOAP_ACTION_PROPERTY);
        requestMessage.getMimeHeaders().setHeader(SoapUtils.SOAP_ACTION, soapaction);

        log.debug("MaintainMessage request=\n" + soapMessageToString(requestMessage));

        SOAPMessage responseMsg = m_msgClient.sendMessageEx(requestMessage,
                PropertyNames.MAINT_MESSAGE_END_POINT_PROPERTY);

        log.debug("MaintainMessage response=\n" + soapMessageToString(responseMsg));

        return responseMsg;

    }

    public ScopesMessagingAcknowledgement sendAysnMaintainMessageRequest(MessageRequest[] messageReqArray)
            throws Exception {

        // Create request
        MaintainMessageRequest maintainMessageRequest = getMaintainMessageRequest(messageReqArray);

        StringWriter sw = new StringWriter();
        JAXBContext context = JAXBContext.newInstance(MaintainMessageRequest.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.marshal(maintainMessageRequest, sw);
        sw.close();

        SOAPMessage requestMessage = SoapUtils.emptyMessage();

        if (MAINT_MSG_XML_ENCRYPTION_ENABLED_YES
                .equals(m_properties.getProperty(PropertyNames.MAINT_MSG_XML_ENCRYPTION_ENABLED_PROPERTY_NAME))) {

            // Encrypt MaintainMessageRequest
            String friendlyAlias = "session_key";

            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setNamespaceAware(true);
            DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
            ByteArrayInputStream inStream = new ByteArrayInputStream(sw.toString().getBytes("UTF-8"));
            Document rawDoc = builder.parse(inStream);
            XmlEncUtils xmlEncUtils = new XmlEncUtils();

            Document encDoc = xmlEncUtils.encryptXML(m_properties, friendlyAlias, rawDoc);
            inStream.close();
            inStream = null;

            StringWriter sw2 = new StringWriter();
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            transformer.transform(new DOMSource(encDoc), new StreamResult(sw2));
            SoapUtils.addBody(requestMessage, sw2.toString());
        } else {
            SoapUtils.addBody(requestMessage, sw.toString());
        }

        Map<Object, Object> inMap = new TreeMap<Object, Object>();
        List<Object> rcptList = new Vector<Object>();
        List<Object> rcptAppTypeList = new Vector<Object>();
        rcptList.add(PropertiesUtils.getMandatoryProperty(m_properties,
                PropertyNames.MAINT_MSG_REQ_RECIPIENT_APP_ID_PROPERTY));
        rcptAppTypeList.add(PropertiesUtils.getMandatoryProperty(m_properties,
                PropertyNames.MAINT_MSG_REQ_RECIPIENT_APP_TYPE_PROPERTY));

        inMap.put(IMessagingConstants.RECIPIENT_APP_ID_LIST, rcptList);
        inMap.put(IMessagingConstants.MESSAGE_TYPE, IMessageType.REQUEST);
        inMap.put(IMessagingConstants.SENDER_APP_ID,
                m_properties.getProperty(PropertyNames.MAINT_MSG_REQ_SENDER_APP_ID_PROPERTY));

        m_msgClient.addAsyncRequest(requestMessage, inMap,
                m_properties.getProperty(PropertyNames.MAINT_MSG_REQ_SENDER_APP_TYPE_PROPERTY), rcptAppTypeList, null);

        log.debug("Async Message request=\n" + soapMessageToString(requestMessage));

        SOAPMessage responseMsg = m_msgClient.sendAsyncRequest(requestMessage);

        log.debug("Async Message response=\n" + soapMessageToString(responseMsg));

        return m_msgClient.getAsyncResponse(responseMsg);
    }

    public MaintainMessageResponse getMaintainMessageResponse(SOAPMessage responseMsg) throws Exception {
        MaintainMessageResponse maintainMessageResponse = null;
        SOAPBodyElement body = (SOAPBodyElement) SoapUtils.getFirstBodyElement(responseMsg);
        if (body != null) {
            try {
                JAXBContext context = JAXBContext.newInstance(MaintainMessageResponse.class);
                Unmarshaller unmarshaller = context.createUnmarshaller();
                maintainMessageResponse = (MaintainMessageResponse) unmarshaller.unmarshal(body);
            } catch (Exception ex) {
                log.error("getMaintainMessageResponse: unmarshal MaintainMessageResponse failed: ", ex);
                log.error("SOAPMessage with error=\n" + soapMessageToString(responseMsg));
            }
        }
        return maintainMessageResponse;
    }

    public List<MessageResponse> getMessageResponseList(MaintainMessageResponse maintainMessageResponse)
            throws Exception {

        int count = maintainMessageResponse.getMessageResponse().size();

        ArrayList<MessageResponse> responseList = new ArrayList<MessageResponse>();

        List<MessageResponseCT> responseArr = maintainMessageResponse.getMessageResponse();

        for (int i = 0; i < count; i++) {
            MessageResponse messageResponse = new MessageResponse();

            messageResponse.setTranID(responseArr.get(i).getTranID());
            messageResponse.setIdpID(responseArr.get(i).getIdpID());
            messageResponse.setRecipientID(responseArr.get(i).getRecipientID());
            messageResponse.setTranResultCode(responseArr.get(i).getTranResultCode());
            messageResponse.setTranResultMessage(responseArr.get(i).getTranResultMessage());
            messageResponse.setMsgType(responseArr.get(i).getMsgType());

            responseList.add(messageResponse);
        }
        return responseList;
    }

    public SOAPMessage retrieveAsynResponseBySinglePull(String egisCorrrelationId) throws Exception {

        // Construct the Single Pull Message Request
        Map<Object, Object> inMap = new TreeMap<Object, Object>();

        inMap.put(IMessagingConstants.CORRELATION_ID, egisCorrrelationId);
        inMap.put(IMessagingConstants.RECIPIENT_APP_ID,
                m_properties.getProperty(PropertyNames.MAINT_MSG_REQ_SENDER_APP_ID_PROPERTY));

        String mainMsgReqSenderAppId = m_properties.getProperty(PropertyNames.MAINT_MSG_REQ_SENDER_APP_ID_PROPERTY);
        String mainMsgReqRecipientAppId = m_properties
                .getProperty(PropertyNames.MAINT_MSG_REQ_RECIPIENT_APP_ID_PROPERTY);
        String mainMsgReqSenderAppType = m_properties.getProperty(PropertyNames.MAINT_MSG_REQ_SENDER_APP_TYPE_PROPERTY);
        String mainMsgReqRecipientAppType = m_properties
                .getProperty(PropertyNames.MAINT_MSG_REQ_RECIPIENT_APP_TYPE_PROPERTY);

        // create batch pull request by MessagingClient
        SOAPMessage requestMsg = SoapUtils.emptyMessage();
        m_msgClient.addSinglePullRequest(requestMsg, inMap, mainMsgReqRecipientAppType, mainMsgReqSenderAppType);

        try {
            log.debug("Single pull request=\n" + soapMessageToString(requestMsg));
        } catch (Exception e) {
            log.debug("Single pull request soapMessageToString got exception: ", e);
        }

        // send request & retrieve message
        SOAPMessage responseMsg = m_msgClient.sendSinglePullRequest(requestMsg);

        try {
            log.debug("Single pull response=\n" + soapMessageToString(responseMsg));
        } catch (Exception e) {
            log.debug("Single pull response soapMessageToString got exception: ", e);
        }

        SinglePullResponse singlePullResponse = m_msgClient.getSinglePullResponse(responseMsg);

        String rspScopesMsgId = getMessageIdFromSinglePullResponse(responseMsg);

        log.debug("Single Pull Response: message_id=" + rspScopesMsgId);

        return responseMsg;
    }

    public SOAPMessage sendPullAckRequest(String rspMsgId) throws Exception {

        // Construct the Pull Ack Message Request
        String mainMsgReqSenderAppId = m_properties.getProperty(PropertyNames.MAINT_MSG_REQ_SENDER_APP_ID_PROPERTY);
        String mainMsgReqSenderAppType = m_properties.getProperty(PropertyNames.MAINT_MSG_REQ_SENDER_APP_TYPE_PROPERTY);

        ArrayList<Object> msgIdList = new ArrayList<Object>();
        msgIdList.add(rspMsgId);

        // send PullAckRequest to XmlFw
        SOAPMessage pullAckRequest = m_msgClient.createPullAckRequest(mainMsgReqSenderAppId, mainMsgReqSenderAppType,
                msgIdList);

        try {
            log.debug("PullAckRequest=\n" + soapMessageToString(pullAckRequest));
        } catch (Exception e) {
            log.debug("PullAckRequest soapMessageToString got exception: ", e);
        }

        SOAPMessage pullAckResponse = m_msgClient.sendPullAckRequest(pullAckRequest);

        try {
            log.debug("pullAckResponse=\n" + soapMessageToString(pullAckResponse));
        } catch (Exception e) {
            log.debug("pullAckResponse soapMessageToString got exception: ", e);
        }

        return pullAckResponse;
    }

    public String getMessageIdFromSinglePullResponse(SOAPMessage responseMsg) throws Exception {
        return m_msgClient.getAsyncRequest(responseMsg).getMessageId();
    }

    private static String soapMessageToString(SOAPMessage message) {

        String result = "";

        try {
            if (message == null)
                return null;
            ByteArrayOutputStream bas = new ByteArrayOutputStream(4096);
            message.writeTo(bas);
            result = bas.toString("UTF-8");
        } catch (Exception ex) {
            log.warn("soapMessageToString exception:", ex);
        }

        return result;
    }

}
