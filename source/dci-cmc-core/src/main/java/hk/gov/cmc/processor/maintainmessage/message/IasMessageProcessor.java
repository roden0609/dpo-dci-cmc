package hk.gov.cmc.processor.maintainmessage.message;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import hk.gov.cmc.common.CmcAppPropertyNames;
import hk.gov.cmc.common.IasApplicationConstant;
import hk.gov.cmc.config.CmcEnvProperties;
import hk.gov.cmc.dao.maintainmessage.message.IasMessageDAO;
import hk.gov.cmc.dto.maintainmessage.SingleMaintainMsgResult;
import hk.gov.cmc.model.maintainmessage.param.MessageParam;
import hk.gov.cmc.model.maintainmessage.request.Recipient;
import hk.gov.cmc.model.maintainmessage.response.MaintainMessageResponse;
import hk.gov.cmc.model.maintainmessage.response.MessageResponse;
import hk.gov.cmc.model.maintainmessage.template.CmcTemplate;
import hk.gov.cmc.model.maintainmessage.user.IasUser;
import hk.gov.cmc.model.maintainmessage.user.IasUserWrapped;
import hk.gov.cmc.persistence.connection.hpfw.HPFW_Connection;
import hk.gov.cmc.utils.maintainmessage.ias.IasMessageUtils;
import hk.gov.cmc.utils.maintainmessage.ias.IasToDoItemUtils;
import hk.gov.cmc.utils.maintainmessage.ias.IasUtils;
import hk.gov.cmc.validator.IasMessageValidator;

public class IasMessageProcessor {

    private static Log logger = LogFactory.getLog(IasMessageProcessor.class);
    private final static CmcEnvProperties cmcEnvProperties = new CmcEnvProperties();

    public IasMessageProcessor() {
    }

    public SingleMaintainMsgResult processIasMessage(
            String portalId, String iasMsgId,
            Recipient recipient, IasUserWrapped iasUserWrapped,
            boolean eMsgIasOptCheck,
            String dataContentEn, String dataContentTc, String dataContentSc,
            CmcTemplate cmcTemplate, List<MessageParam> cmcMessageParam, HPFW_Connection conn,
            MaintainMessageResponse response) throws Exception {
        logger.info("processIasMessage - Start");
        logger.info("processIasMessage - portalId: " + portalId + ", iasMsgId: " + iasMsgId
                + ", recipient.getAction.getType: " + recipient.getAction().getType()
                + ", recipient.getIdpId: " + recipient.getIdpId()
                + ", recipient.getRecipientIdType: " + recipient.getRecipientIdType()
                + ", recipient.getTranId: " + recipient.getTranId());

        logger.debug("processIasMessage - portalId: " + portalId + ", iasMsgId: " + iasMsgId
                + ", recipient: " + recipient.toString()
                + ", iasUserWrapped.toString(): " + iasUserWrapped.toString()
                + ", cmcTemplate: " + cmcTemplate.toString()
                + ", cmcMessageParam size: " + cmcMessageParam.size()
                + ", eMsgIasOptCheck: " + eMsgIasOptCheck);
        logger.debug("processIasMessage - dataContentEn: " + dataContentEn + ", dataContentTc: " + dataContentTc
                + ", dataContentSc: " + dataContentSc);

        Properties properties = cmcEnvProperties.getProperties();
        MessageResponse msgRsp = null;
        SingleMaintainMsgResult singleMaintainMsgResult = new SingleMaintainMsgResult(iasMsgId, false);

        IasUser iasUser = iasUserWrapped.getIasUser();

        msgRsp = IasMessageValidator.validateRecipientIdTypeOnlyEmptyOrOpenId(cmcTemplate.getClientId(),
                recipient.getIdpId(), recipient.getRecipientId(), recipient.getTranId(),
                recipient.getRecipientIdType());

        if (msgRsp != null) {
            response.addMessageResponse(msgRsp);
        } else {
            msgRsp = IasMessageValidator.validateOptInStatus(cmcTemplate.getClientId(), recipient.getIdpId(),
                    recipient.getRecipientId(), recipient.getTranId(), eMsgIasOptCheck, iasUser.getOptIn());

            if (msgRsp != null) {
                response.addMessageResponse(msgRsp);
            } else {
                msgRsp = IasMessageValidator.validateAction(cmcTemplate.getClientId(), recipient.getIdpId(),
                        recipient.getRecipientId(), recipient.getTranId(), recipient.getAction());

                if (msgRsp != null) {
                    response.addMessageResponse(msgRsp);
                } else {
                    // create the master record in IAS_MESSAGE
                    if (iasMsgId == null || "".equals(iasMsgId)) {
                        iasMsgId = IasUtils
                                .getNextIasMsgId(properties.getProperty(CmcAppPropertyNames.SERVER_ID_PROPERTY_NAME));
                        logger.info("processIasMessage - action type: " + recipient.getAction().getType()
                                + ", generated iasMsgId: " + iasMsgId);

                        // merge the template and create the master record in IAS_MESSAGE
                        msgRsp = IasMessageUtils.mergeTemplateAndCreateIasMessage(
                                recipient.getIdpId(), recipient.getTranId(), recipient.getRecipientId(),
                                conn, portalId, iasMsgId,
                                cmcTemplate, cmcMessageParam,
                                dataContentEn, dataContentTc, dataContentSc,
                                properties);
                    }

                    if (msgRsp != null) {
                        logger.info("processIasMessage - mergeTemplateAndCreateIasMessage failed"
                                + ". cmcTemplate.getClientId: " + cmcTemplate.getClientId()
                                + ", cmcTemplate.getTemplateId: " + cmcTemplate.getTemplateId()
                                + ", cmcTemplate.getTemplateVersion: " + cmcTemplate.getTemplateVersion()
                                + ", msgRsp.getIdpId: " + msgRsp.getIdpId()
                                + ", msgRsp.getTranId: " + msgRsp.getTranId()
                                + ", msgRsp.getTranResultCode: " + msgRsp.getTranResultCode()
                                + ", msgRsp.getTranResultMessage: " + msgRsp.getTranResultMessage());
                        response.addMessageResponse(msgRsp);
                    } else {
                        msgRsp = IasMessageUtils.createIasUserMessageByOpenId(conn, cmcTemplate, recipient, iasUser,
                                iasMsgId);
                        if (msgRsp != null) {
                            logger.info(
                                    "processIasMessage - createIasUserMessageByOpenId failed. msgRsp.getTranId: "
                                            + msgRsp.getTranId() + ", msgRsp.getIdpId: " + msgRsp.getIdpId()
                                            + ", msgRsp.getTranResultCode: " + msgRsp.getTranResultCode()
                                            + ", msgRsp.getTranResultMessage: " + msgRsp.getTranResultMessage());
                            response.addMessageResponse(msgRsp);
                        } else {
                            singleMaintainMsgResult.setSuccess(true);
                        }
                    }
                }
            }
        }

        return null;
    }
}
