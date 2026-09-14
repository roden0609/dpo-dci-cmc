package hk.gov.cmc.webserver.soap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.naming.InitialContext;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.security.Init;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import hk.gov.cmc.appserver.ejb.session.maintainmessage.IMaintainMessageSessionBMLocal;
import hk.gov.cmc.appserver.ejb.session.retrievecmctemplate.IRetrieveCmcTemplateSessionBM;
import hk.gov.cmc.common.CmcAppConstants;
import hk.gov.cmc.common.ResultCodes;
import hk.gov.cmc.common.ResultMessages;
import hk.gov.cmc.jaxb.retrievecmctemplate.RetrieveCmcTemplateRequest;
import hk.gov.cmc.jaxb.retrievecmctemplate.RetrieveCmcTemplateResponse;
import hk.gov.cmc.utils.soap.CmcSoapUtils;
import jakarta.ejb.EJB;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.soap.MessageFactory;
import jakarta.xml.soap.SOAPBody;
import jakarta.xml.soap.SOAPEnvelope;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.ws.Provider;
import jakarta.xml.ws.Service;
import jakarta.xml.ws.ServiceMode;
import jakarta.xml.ws.WebServiceProvider;

@WebServiceProvider(serviceName = "RetrieveCmcTemplateService", portName = "RetrieveCmcTemplatePort", targetNamespace = "http://ws.mygovhk.gov.hk/schema/RetrieveCmcTemplateRequest.xsd")
@ServiceMode(Service.Mode.MESSAGE)
public class RetrieveCmcTemplateSoapProvider implements Provider<Source> {

    private static Log logger = LogFactory.getLog(RetrieveCmcTemplateSoapProvider.class);

    @EJB
    private IMaintainMessageSessionBMLocal maintainMessageEJB;

    static {
        Init.init();
    }

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

            SOAPMessage response = processRetrieveCmcTemplate(requestMsg, MessageFactory.newInstance().createMessage());

            return response.getSOAPPart().getContent();
        } catch (Exception e) {
            logger.error("General exception caught in invoke", e);
            return null;
        } finally {
            logger.info("invoke - END");
        }
    }

    private SOAPMessage processRetrieveCmcTemplate(SOAPMessage requestMsg, SOAPMessage responseMsg) throws Exception {
        logger.info("processRetrieveCmcTemplate - BEGIN");

        try {

            logger.info("processRetrieveCmcTemplate requestMsg=\n" + CmcSoapUtils.soapMessageToString(requestMsg));

            String appId = null;
            SOAPEnvelope envelope = (SOAPEnvelope) requestMsg.getSOAPPart().getEnvelope();
            NodeList nodeList = envelope.getHeader().getElementsByTagNameNS("*", CmcAppConstants.APPID_TAG_NAME);

            logger.info("processRetrieveCmcTemplate nodeList == null: " + (nodeList == null));
            logger.info("processRetrieveCmcTemplate nodeList.getLength: "
                    + (nodeList != null ? nodeList.getLength() : "null"));

            if (nodeList != null && nodeList.getLength() > 0) {
                appId = nodeList.item(0).getFirstChild().getNodeValue();
            }

            if (appId == null || appId.length() == 0) {
                return generateResponse(responseMsg, ResultCodes.RESULT_CD_SENDER_APPID_NOT_FOUND,
                        ResultMessages.RESULT_MSG_SENDER_APPID_NOT_FOUND);
            }

            SOAPBody body = requestMsg.getSOAPPart().getEnvelope().getBody();
            Document bodyDoc = body.extractContentAsDocument();
            logger.debug("Request body: " + CmcSoapUtils.documentToString(bodyDoc));

            RetrieveCmcTemplateRequest retrieveCmcTemplateRequest = null;
            JAXBContext jaxbContext = JAXBContext.newInstance(RetrieveCmcTemplateRequest.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            JAXBElement<RetrieveCmcTemplateRequest> root = unmarshaller.unmarshal(bodyDoc,
                    RetrieveCmcTemplateRequest.class);
            retrieveCmcTemplateRequest = root.getValue();

            InitialContext ctx = new InitialContext();
            IRetrieveCmcTemplateSessionBM retrieveCmcTemplateEJB = (IRetrieveCmcTemplateSessionBM) ctx.lookup(
                    "java:global/dci-cmc-app/dci-cmc-ejb/RetrieveCmcTemplateSessionEJB!hk.gov.cmc.appserver.ejb.session.retrievecmctemplate.IRetrieveCmcTemplateSessionBMLocal");

            RetrieveCmcTemplateResponse retrieveCmcTemplateResponse = retrieveCmcTemplateEJB.processMessage(appId,
                    retrieveCmcTemplateRequest);

            logger.info("processRetrieveCmcTemplate - END");

            return generateResponse(responseMsg, retrieveCmcTemplateResponse);

        } catch (Exception ex) {
            logger.error("General exception caught in processRetrieveCmcTemplate", ex);
            return generateResponse(responseMsg, ResultCodes.RESULT_CD_GENERAL_ERROR,
                    ResultMessages.RESULT_MSG_GENERAL_ERROR);
        }
    }

    private SOAPMessage generateResponse(SOAPMessage responseMsg, String resultCd, String resultMsg) throws Exception {

        RetrieveCmcTemplateResponse retrieveCmcTemplateResponse = new RetrieveCmcTemplateResponse();
        retrieveCmcTemplateResponse.setResultCd(resultCd);
        retrieveCmcTemplateResponse.setResultMsg(resultMsg);
        return generateResponse(responseMsg, retrieveCmcTemplateResponse);
    }

    private SOAPMessage generateResponse(SOAPMessage responseMsg,
            RetrieveCmcTemplateResponse retrieveCmcTemplateResponse)
            throws Exception {
        SOAPEnvelope envelope = responseMsg.getSOAPPart().getEnvelope();
        SOAPBody body = envelope.getBody();
        body.removeContents();

        JAXBContext jaxbContext = JAXBContext.newInstance(RetrieveCmcTemplateResponse.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);
        marshaller.marshal(retrieveCmcTemplateResponse, body);
        responseMsg.saveChanges();
        return responseMsg;
    }
}