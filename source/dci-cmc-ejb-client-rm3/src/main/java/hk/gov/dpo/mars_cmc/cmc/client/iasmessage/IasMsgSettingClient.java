package hk.gov.dpo.mars_cmc.cmc.client.iasmessage;

import hk.gov.dpo.mars_cmc.cmc.client.iasmessage.constant.Constants;
import hk.gov.dpo.mars_cmc.cmc.client.iasmessage.constant.PropertyNames;
import hk.gov.dpo.mars_cmc.cmc.client.iasmessage.dto.IasAssoQueue;
import hk.gov.dpo.mars_cmc.cmc.client.iasmessage.dto.IasAssoQueues;
import hk.gov.gcis.ss.common.client.ClientBase;
import hk.gov.gcis.ss.common.exception.ApplicationException;
import hk.gov.gcis.ss.common.exception.MissingPropertyException;
import hk.gov.gcis.ss.common.utils.PropertiesUtils;
import hk.gov.gcis.ss.common.utils.SoapUtils;
import hk.gov.gcis.ss.messaging.jaxb.asynmsg.ScopesMessagingAcknowledgement;
import hk.gov.gcis.ss.messaging.client.MessagingClient;
import hk.gov.gcis.ss.messaging.types.IMessageType;
import hk.gov.gcis.ss.messaging.types.IMessagingConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import javax.xml.parsers.ParserConfigurationException;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.*;

public class IasMsgSettingClient extends ClientBase implements Serializable {

    private static final long serialVersionUID = 1L;
    public final static Log log = LogFactory.getLog(IasMsgSettingClient.class);

    private MessagingClient clientKit;
    private Properties properties;

    public IasMsgSettingClient(Properties properties) {
        super(properties);
        this.properties = properties;
        this.clientKit = new MessagingClient(properties);
    }

    public IasMsgSettingClient(Properties properties, MessagingClient messagingClient) {
        super(properties);
        this.properties = properties;
        this.clientKit = messagingClient;
    }

    public ScopesMessagingAcknowledgement enable(List<String> openIdList)
            throws MissingPropertyException, ParserConfigurationException, SOAPException, ApplicationException, IOException, JAXBException, SAXException {

        List<IasAssoQueue> iasAssoQueueList = getIasAssoQueueList(openIdList, Constants.ASSO_STATUS_ASSOCIATE);
        IasAssoQueues iasAssoQueues = new IasAssoQueues();
        iasAssoQueues.setIasAssoQueues(iasAssoQueueList);

        JAXBContext jaxbContext = JAXBContext.newInstance(IasAssoQueues.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
        StringWriter sw = new StringWriter();
        jaxbMarshaller.marshal(iasAssoQueues, sw);
        log.debug("jaxbMarshaller.marshal iasAssoQueues=\n" + sw.toString());

        SOAPMessage requestMessage = SoapUtils.emptyMessage();
        SoapUtils.addBody(requestMessage, sw.toString());

        Map<Object, Object> inMap = new TreeMap<Object, Object>();
        List<Object> rcptList = new Vector<Object>();
        List<Object> rcptAppTypeList = new Vector<Object>();
        rcptList.add(PropertiesUtils.getMandatoryProperty(properties, PropertyNames.IAS_MSG_REQ_RECIPIENT_APP_ID_PROPERTY));
        rcptAppTypeList.add(PropertiesUtils.getMandatoryProperty(properties, PropertyNames.IAS_MSG_REQ_RECIPIENT_APP_TYPE_PROPERTY));

        inMap.put(IMessagingConstants.RECIPIENT_APP_ID_LIST, rcptList);
        inMap.put(IMessagingConstants.MESSAGE_TYPE, IMessageType.REQUEST);
        inMap.put(IMessagingConstants.SENDER_APP_ID, properties.getProperty(PropertyNames.IAS_MSG_REQ_SENDER_APP_ID_PROPERTY));

        log.debug("addAsyncRequest requestMessage=\n" + soapMessageToString(requestMessage));
        clientKit.addAsyncRequest(requestMessage, inMap, clientKit.getProperty(PropertyNames.IAS_MSG_REQ_SENDER_APP_TYPE_PROPERTY), rcptAppTypeList, null);

        log.debug("sendAsyncRequest requestMessage=\n" + soapMessageToString(requestMessage));
        SOAPMessage responseMsg = clientKit.sendAsyncRequest(requestMessage);
        log.debug("Async Message response=\n" + soapMessageToString(responseMsg));

        return clientKit.getAsyncResponse(responseMsg);
    }

    public ScopesMessagingAcknowledgement disable(List<String> openIdList)
            throws MissingPropertyException, ParserConfigurationException, SOAPException,
            ApplicationException, IOException, JAXBException, SAXException {

        List<IasAssoQueue> iasAssoQueueList = getIasAssoQueueList(openIdList, Constants.ASSO_STATUS_DISASSOCIATE);
        IasAssoQueues iasAssoQueues = new IasAssoQueues();
        iasAssoQueues.setIasAssoQueues(iasAssoQueueList);

        JAXBContext jaxbContext = JAXBContext.newInstance(IasAssoQueues.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
        StringWriter sw = new StringWriter();
        jaxbMarshaller.marshal(iasAssoQueues, sw);
        log.debug("jaxbMarshaller.marshal iasAssoQueues=\n" + sw.toString());

        SOAPMessage requestMessage = SoapUtils.emptyMessage();
        SoapUtils.addBody(requestMessage, sw.toString());

        Map<Object, Object> inMap = new TreeMap<Object, Object>();
        List<Object> rcptList = new Vector<Object>();
        List<Object> rcptAppTypeList = new Vector<Object>();
        rcptList.add(PropertiesUtils.getMandatoryProperty(properties, PropertyNames.IAS_MSG_REQ_RECIPIENT_APP_ID_PROPERTY));
        rcptAppTypeList.add(PropertiesUtils.getMandatoryProperty(properties, PropertyNames.IAS_MSG_REQ_RECIPIENT_APP_TYPE_PROPERTY));

        inMap.put(IMessagingConstants.RECIPIENT_APP_ID_LIST, rcptList);
        inMap.put(IMessagingConstants.MESSAGE_TYPE, IMessageType.REQUEST);
        inMap.put(IMessagingConstants.SENDER_APP_ID, properties.getProperty(PropertyNames.IAS_MSG_REQ_SENDER_APP_ID_PROPERTY));

        log.debug("addAsyncRequest requestMessage=\n" + soapMessageToString(requestMessage));
        clientKit.addAsyncRequest(requestMessage, inMap, clientKit.getProperty(PropertyNames.IAS_MSG_REQ_SENDER_APP_TYPE_PROPERTY), rcptAppTypeList, null);

        log.debug("sendAsyncRequest requestMessage=\n" + soapMessageToString(requestMessage));
        SOAPMessage responseMsg = clientKit.sendAsyncRequest(requestMessage);
        log.debug("Async Message response=\n" + soapMessageToString(responseMsg));

        return clientKit.getAsyncResponse(responseMsg);
    }

    private List<IasAssoQueue> getIasAssoQueueList(List<String> openIdList, String status) {

        List<IasAssoQueue> iasAssoQueueList = new ArrayList<IasAssoQueue>();
        IasAssoQueue iasAssoQueue = null;
        for (String openId : openIdList) {
            iasAssoQueue = new IasAssoQueue(openId);
            iasAssoQueue.setStatus(status);
            iasAssoQueueList.add(iasAssoQueue);
        }

        return iasAssoQueueList;
    }

    private String soapMessageToString(SOAPMessage message) throws SOAPException, IOException {

        ByteArrayOutputStream bas = new ByteArrayOutputStream(4096);
        message.writeTo(bas);
        return bas.toString("UTF-8");

    }
}
