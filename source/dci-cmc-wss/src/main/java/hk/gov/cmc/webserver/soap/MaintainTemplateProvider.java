/**
 *
 * Design, Implementation and Support of Common Middleware Components and Reference Applications
 *
 * Web Service Provider
 *
 * Developed by: Gilbert Liao
 * Reviewed by: Kai So
 * Tester: Shaohui Wang
 *
 *
 */
package hk.gov.ogcio.mars_cmc.cmc.webserver.axis.maintaintemplate;

import javax.naming.InitialContext;
import javax.xml.soap.SOAPMessage;

import org.apache.axis.Message;
import org.apache.axis.MessageContext;
import org.apache.axis.message.SOAPBodyElement;
import org.apache.axis.message.SOAPEnvelope;
import org.apache.axis.utils.XMLUtils;
import org.exolab.castor.xml.Unmarshaller;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import hk.gov.spica_scopes.common.utils.CastorUtils;
import hk.gov.spica_scopes.common.utils.SoapUtils;
// MyGov5-03-111 - Adopt RM2 in EAP7 - BEGIN
//import hk.gov.spica_scopes.common.webserver.axis.AxisBase;
import hk.gov.ogcio.mars_cmc.framework.common.webserver.axis.AxisBase;
// MyGov5-03-111 - Adopt RM2 in EAP7 - END

import hk.gov.ogcio.mars_cmc.cmc.appserver.ejb.session.maintaintemplate.IMaintainTemplateSessionBM;
import hk.gov.ogcio.mars_cmc.cmc.castor.maintaintemplate.MaintainTemplateRequest;
import hk.gov.ogcio.mars_cmc.cmc.castor.maintaintemplate.MaintainTemplateResponse;
import hk.gov.ogcio.mars_cmc.cmc.castor.maintaintemplate.types.Result;
import hk.gov.ogcio.mars_cmc.cmc.datatype.message.Constants;

/**
 * Web Services provider for Maintain template
 */
public class MaintainTemplateProvider extends AxisBase {

	public static final String APPID_TAG_NAME = "SenderAppId";

	/**
	 * Process the incoming SOAP request for retrieving event detail
	 *	@param request SOAP request
	 *	@param response SOAP response
	 */
	public void processMessage(SOAPEnvelope request, SOAPEnvelope response) {
		logInfo("processMessage -- BEGIN");

		try {
			MessageContext mc = MessageContext.getCurrentContext();
			Message requestMsg = mc.getRequestMessage();
			Message responseMsg = mc.getResponseMessage();
            processMaintainTemplate(requestMsg, responseMsg);
			mc.setResponseMessage(responseMsg);

		} catch (Exception e) {
			logError("General exception caught in processMessage", e);
		}

		logInfo("processMessage -- END");
	}

	/**
	 * Process the incoming SOAP request for retrieving event detail
	 *	@param request SOAP request
	 *	@param response SOAP response
	 *	@return response SOAP response
	 */
	private SOAPMessage processMaintainTemplate(SOAPMessage requestMsg, SOAPMessage responseMsg) throws Exception {
		logInfo("processMaintainTemplate -- BEGIN");

		MaintainTemplateResponse response = null;
		try {
			String appId = null;
            SOAPEnvelope envelope = (SOAPEnvelope) requestMsg.getSOAPPart().getEnvelope();
            Document envDoc = envelope.getAsDocument();
            NodeList nodeList = envDoc.getElementsByTagName(APPID_TAG_NAME);
            if (nodeList != null && nodeList.getLength() > 0) {
                appId = nodeList.item(0).getFirstChild().getNodeValue();
            }

			SOAPBodyElement body = (SOAPBodyElement) SoapUtils.getFirstBodyElement(requestMsg);
			Document bodyDoc = body.getAsDocument();
			logDebug("Request body: " + XMLUtils.DocumentToString(bodyDoc));

			MaintainTemplateRequest MaintainTemplateRequest = (MaintainTemplateRequest) Unmarshaller.unmarshal(MaintainTemplateRequest.class, bodyDoc);

			InitialContext ctx = new InitialContext();
			//IMaintainTemplateSessionBM ejb = (IMaintainTemplateSessionBM) ctx.lookup("cmc/MaintainTemplateSessionEJB/local");
			IMaintainTemplateSessionBM ejb = (IMaintainTemplateSessionBM) ctx.lookup("java:global/cmc/cmc_app/MaintainTemplateSessionEJB!hk.gov.ogcio.mars_cmc.cmc.appserver.ejb.session.maintaintemplate.IMaintainTemplateSessionBMLocal");
			response = ejb.processMessage(appId, MaintainTemplateRequest);

			logInfo("processMaintainTemplate -- END");

		} catch (Exception ex) {
			logError("General exception caught in processMaintainTemplate", ex);
			return generateResponse(responseMsg, Result.VALUE_9999, Constants.RESULT_MSG_RETRIEVE_CMC_TEMPLATE_9999);
		}

		return generateResponse(responseMsg, response);
	}

	/**
	 *	Generate the result response message
	 *	@param responseMsg SOAP response
	 *	@param resultCd result code
	 *	@param resultMsg result message
	 *	@return response message of the result
	 */
	private SOAPMessage generateResponse(SOAPMessage responseMsg, Result resultCd, String resultMsg) throws Exception {

		MaintainTemplateResponse response = new MaintainTemplateResponse();
		response.setResultCd(resultCd);
		response.setResultMsg(resultMsg);
		return generateResponse(responseMsg, response);
	}

	/**
	 *	Generate the result response message
	 *	@param responseMsg SOAP response
	 *	@param maintainMessageResponse SOAP response
	 *	@return response message of the result
	 */
	private SOAPMessage generateResponse(SOAPMessage responseMsg, MaintainTemplateResponse response) throws Exception {
		return CastorUtils.marshal(responseMsg, response);
	}
}