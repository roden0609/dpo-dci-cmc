/**
 *
 * Design, Implementation and Support of Common Middleware Components and Reference Applications
 *
 * Maintain Message Provider
 *
 * Developed by: Gilbert Liao
 * Reviewed by: Kai So
 * Tester: Shaohui Wang
 *
 *
 */
package hk.gov.ogcio.mars_cmc.cmc.webserver.axis.maintainmessage;

//import Java standard package(s) -- BEGIN
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import javax.naming.InitialContext;
import javax.xml.soap.SOAPMessage;
//import Java standard package(s) -- END

//import third-party package(s) -- BEGIN
import org.apache.axis.Message;
import org.apache.axis.MessageContext;
import org.apache.axis.message.SOAPBodyElement;
import org.apache.axis.message.SOAPEnvelope;
import org.apache.axis.utils.XMLUtils;
import org.apache.xml.security.utils.EncryptionConstants;
import org.exolab.castor.xml.Unmarshaller;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import javax.ejb.EJB;
//import third-party package(s) -- END

//import internal library package(s) -- BEGIN
import hk.gov.spica_scopes.common.utils.CastorUtils;
import hk.gov.ogcio.egis.rm.common.utils.ServiceLocator;
import hk.gov.spica_scopes.common.utils.SoapUtils;
import hk.gov.spica_scopes.common.utils.StringUtils;
import hk.gov.spica_scopes.common.utils.DatetimeUtils;
import hk.gov.ogcio.egis.rm.keyservice.PKIUtil;
// MyGov5-03-111 - Adopt RM2 in EAP7 - BEGIN
//import hk.gov.spica_scopes.common.webserver.axis.AxisBase;
import hk.gov.ogcio.mars_cmc.framework.common.webserver.axis.AxisBase;
// MyGov5-03-111 - Adopt RM2 in EAP7 - END
//import internal library package(s) -- END

//import module package(s) -- BEGIN
import hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MaintainMessageRequest;
import hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MaintainMessageResponse;
import hk.gov.ogcio.mars_cmc.cmc.datatype.message.Constants;
import hk.gov.ogcio.mars_cmc.cmc.appserver.AppPropertyName;
import hk.gov.ogcio.mars_cmc.cmc.appserver.ejb.session.maintainmessage.IMaintainMessageSessionBM;
//import module package(s) -- END


import javax.naming.InitialContext;
import hk.gov.ogcio.mars_cmc.framework.common.CommonConstants;
import static hk.gov.ogcio.egis.rm.keyservice.appserver.AppPropertyNames.PROPERTY_NAME_PKI_UTIL_EJB_REMOTE_JNDI_NAME;
import static hk.gov.ogcio.egis.rm.keyservice.common.Constants.EGIS_RM_KEYSERVICE_MODULE_NAME;
import hk.gov.ogcio.egis.rm.keyservice.exception.KeyServiceException;
import hk.gov.ogcio.egis.rm.keyservice.exception.KeyNotFoundException;
import hk.gov.ogcio.egis.rm.keyservice.appserver.ejb.session.IPKIUtil;
import hk.gov.ogcio.mars_cmc.framework.common.sql.HPFW_Connection;
import hk.gov.ogcio.mars_cmc.cmc.utils.EncUtils;

/**
 * Maintain Message Provider
 */
public class MaintainMessageProvider extends AxisBase {

	// public constant(s) -- BEGIN
	// public constant(s) -- END

	// public data member(s) -- BEGIN
	// public data member(s) -- END

	// public method(s) -- BEGIN
	/**
	 * Constructor
	 */
	public MaintainMessageProvider() throws Exception {
		init();
	}

	/**
	 * Process the incoming SOAP request for maintaining e-message & to-do-item
	 *	@param request SOAP request
	 *	@param response SOAP response
	 */
	public void processMessage(SOAPEnvelope request, SOAPEnvelope response) {
		logInfo("processMessage -- BEGIN");

		try {
			MessageContext mc = MessageContext.getCurrentContext();
			Message requestMsg = mc.getRequestMessage();
			Message responseMsg = mc.getResponseMessage();
            processMaintainMessage(requestMsg, responseMsg);
			mc.setResponseMessage(responseMsg);

		} catch (Exception e) {
			logError("General exception caught in processMessage", e);
		}

		logInfo("processMessage -- END");
	}

	// public method(s) -- END

	// protected constant(s) -- BEGIN
	// protected constant(s) -- END

	// protected data member(s) -- BEGIN
	// protected data member(s) -- END

	// protected method(s) -- BEGIN
	// protected method(s) -- END

	// private method(s) -- BEGIN

	// CMC-2025-034: CMC send RVD VIP request to DCI-CMC in RPC mode - BEGIN
	private static String soapMessageToString(SOAPMessage message) throws Exception {

		String result = "";

		if (message == null)
			return null;
		ByteArrayOutputStream bas = new ByteArrayOutputStream(4096);
		message.writeTo(bas);
		result = bas.toString("UTF-8");

		return result;
	}
	// CMC-2025-034: CMC send RVD VIP request to DCI-CMC in RPC mode - END

	/**
	 * Process the incoming SOAP request for retrieving events
	 *	@param request SOAP request
	 *	@param response SOAP response
	 *	@return response SOAP response
	 */
	private SOAPMessage processMaintainMessage(SOAPMessage requestMsg, SOAPMessage responseMsg) throws Exception {
		logInfo("processMaintainMessage -- BEGIN");

		try {

			// CMC-2025-034: CMC send RVD VIP request to DCI-CMC in RPC mode - BEGIN
			logInfo("processMaintainMessage requestMsg=\n" + soapMessageToString(requestMsg));
			// CMC-2025-034: CMC send RVD VIP request to DCI-CMC in RPC mode - END

			// check sender app id from scopes messaging

			String appId = null;
            SOAPEnvelope envelope = (SOAPEnvelope) requestMsg.getSOAPPart().getEnvelope();
            Document envDoc = envelope.getAsDocument();
            NodeList nodeList = envDoc.getElementsByTagName(APPID_TAG_NAME);
            if (nodeList != null && nodeList.getLength() > 0)
                appId = nodeList.item(0).getFirstChild().getNodeValue();
            if (appId == null || appId.length() == 0) {
                return generateResponse(responseMsg, Constants.RESULT_CD_SENDER_APPID_NOT_FOUND, Constants.RESULT_MSG_SENDER_APPID_NOT_FOUND);
            }

            logInfo("processMaintainMessage -- appId="+appId);

			SOAPBodyElement body = (SOAPBodyElement) SoapUtils.getFirstBodyElement(requestMsg);
			Document bodyDoc = body.getAsDocument();
			logDebug("Request body: " + XMLUtils.DocumentToString(bodyDoc));

			// MyGov6-C2-003: Enable at-transit encryption when B/Ds send messages to CMC - BEGIN
			Properties properties = ServiceLocator.getInstance().getProperties();
			//PKIUtil pkiUtil = new PKIUtil(properties);

			// CMC-2025-034: CMC send RVD VIP request to DCI-CMC in RPC mode - BEGIN
			String cmcToDciCmcAppIdMap = properties.getProperty(AppPropertyName.CMC_TO_DCI_CMC_APP_ID_MAP_PROPERTY_NAME);
			// CMC_TO_DCI_CMC_APP_ID_MAP=ogcio.ogcmr.app112|ogcio.ogcmr.app111 (app112 uat cmc app_id, app111 is RA1 app_id)
			Map<String, String> cmcToDciMap = parseCmcToDciCmcAppIdMap(cmcToDciCmcAppIdMap);

			if (cmcToDciMap.containsKey(appId)) {
				logInfo("processMaintainMessage -- appId " + appId + " found in CMC_TO_DCI_CMC_APP_ID_MAP");
				appId = cmcToDciMap.get(appId);
				logInfo("processMaintainMessage -- map appId from CMC " + appId + " to DCI-CMC " + appId);
			}
			// CMC-2025-034: CMC send RVD VIP request to DCI-CMC in RPC mode - END

			NodeList encryptedDataList = bodyDoc.getDocumentElement().getElementsByTagNameNS(EncryptionConstants.EncryptionSpecNS, EncryptionConstants._TAG_ENCRYPTEDDATA);

			MaintainMessageRequest maintainMessageRequest = null;

			if (encryptedDataList.getLength() > 0) {

				// CMC-2021-005 - Add log message to determine is the MaintainMessage encrypted in-transit - BEGIN
				logInfo("This message is encrypted in-transit. encryptedDataList: " + encryptedDataList.getLength() + ", appId: " + appId);
				// CMC-2021-005 - Add log message to determine is the MaintainMessage encrypted in-transit - END

				HPFW_Connection conn = HPFW_Connection.getHPFW_Connection(false);

				EncUtils encUtils = new EncUtils();

				if (!encUtils.keyExistInKMU(conn, bodyDoc)) {
					logWarn("Key not found in processMaintainMessage.");
					return generateResponse(responseMsg, Constants.RESULT_CD_GENERAL_ERROR, Constants.RESULT_MSG_GENERAL_ERROR);
				}
				else {

					String pkiUtilJNDIName = properties.getProperty(PROPERTY_NAME_PKI_UTIL_EJB_REMOTE_JNDI_NAME,
							CommonConstants.GLOBAL_CONTEXT_NAME + CommonConstants.CONTEXT_NAME_SEPARATOR + EGIS_RM_KEYSERVICE_MODULE_NAME + CommonConstants.CONTEXT_NAME_SEPARATOR
							+ EGIS_RM_KEYSERVICE_MODULE_NAME + CommonConstants.DEFAULT_EJB_MODULE_NAME_SUFFIX + CommonConstants.CONTEXT_NAME_SEPARATOR
							+ "PKIUtil!hk.gov.ogcio.egis.rm.keyservice.appserver.ejb.session.IPKIUtil");

					InitialContext context = new InitialContext(properties);
					IPKIUtil pkiUtil = (IPKIUtil) context.lookup(pkiUtilJNDIName);

					Document decDoc = pkiUtil.decryptXML(null, bodyDoc, null);

					maintainMessageRequest = (MaintainMessageRequest) Unmarshaller.unmarshal(MaintainMessageRequest.class, decDoc);
				}
			}
			else {
				// CMC-2021-005 - Add log message to determine is the MaintainMessage encrypted in-transit - BEGIN
				logInfo("This message is not encrypted in-transit. encryptedDataList: " + encryptedDataList.getLength() + ", appId: " + appId);
				// CMC-2021-005 - Add log message to determine is the MaintainMessage encrypted in-transit - END
				maintainMessageRequest = (MaintainMessageRequest) Unmarshaller.unmarshal(MaintainMessageRequest.class, bodyDoc);
			}
			//MaintainMessageRequest maintainMessageRequest = (MaintainMessageRequest) Unmarshaller.unmarshal(MaintainMessageRequest.class, decDoc);
			// MyGov6-C2-003: Enable at-transit encryption when B/Ds send messages to CMC - END

			InitialContext ctx = new InitialContext();
			//IMaintainMessageSessionBM maintainMessageEJB = (IMaintainMessageSessionBM) ctx.lookup("cmc/MaintainMessageSessionEJB/local");
			IMaintainMessageSessionBM maintainMessageEJB = (IMaintainMessageSessionBM) ctx.lookup("java:global/cmc/cmc_app/MaintainMessageSessionEJB!hk.gov.ogcio.mars_cmc.cmc.appserver.ejb.session.maintainmessage.IMaintainMessageSessionBMLocal");

			MaintainMessageResponse maintainMessageResponse  = maintainMessageEJB.processMessage(appId, maintainMessageRequest);

			logInfo("processMaintainMessage -- END");

			return generateResponse(responseMsg, maintainMessageResponse);

		} catch (Exception ex) {
			// System.out.print(ex.getMessage());
			logError("General exception caught in processMaintainMessage", ex);
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
					logWarn("parseCmcToDciCmcAppIdMap -- invalid mapping format: " + trimmedMappingPair);
					continue;
				}
				String cmcAppId = mapping[0].trim();
				String dciCmcAppId = mapping[1].trim();
				if (cmcAppId.length() == 0 || dciCmcAppId.length() == 0) {
					logWarn("parseCmcToDciCmcAppIdMap -- empty appId found in mapping: " + trimmedMappingPair);
					continue;
				}
				result.put(cmcAppId, dciCmcAppId);
			}
			return result;
		}
		// CMC-2025-034: CMC send RVD VIP request to DCI-CMC in RPC mode - END

	private void init() throws Exception {
		// TO-DO: Getting properties and intialize the member variables
	}

	/**
	 *	Generate the result response message
	 *	@param responseMsg SOAP response
	 *	@param resultCd result code
	 *	@param resultMsg result message
	 *	@return response message of the result
	 */
	private SOAPMessage generateResponse(SOAPMessage responseMsg, String resultCd, String resultMsg) throws Exception {

		MaintainMessageResponse maintainMessageResponse = new MaintainMessageResponse();
		maintainMessageResponse.setResultCode(resultCd);
		maintainMessageResponse.setResultMessage(resultMsg);
		return generateResponse(responseMsg, maintainMessageResponse);
	}

	/**
	 *	Generate the result response message
	 *	@param responseMsg SOAP response
	 *	@param maintainMessageResponse SOAP response
	 *	@return response message of the result
	 */
	private SOAPMessage generateResponse(SOAPMessage responseMsg, MaintainMessageResponse maintainMessageResponse) throws Exception {
		return CastorUtils.marshal(responseMsg, maintainMessageResponse);
	}
	// private method(s) -- END

	// private data member(s) -- BEGIN
	// private data member(s) -- END

	// private constant(s) -- BEGIN
	public static final String APPID_TAG_NAME = "SenderAppId";
	// private constant(s) -- END

}
