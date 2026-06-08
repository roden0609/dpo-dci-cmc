package hk.gov.cmc.processor.maintainmessage.todoitem;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import hk.gov.cmc.common.CmcAppPropertyNames;
import hk.gov.cmc.common.IasApplicationConstant;
import hk.gov.cmc.common.IasToDoItemConstant;
import hk.gov.cmc.common.MsgTypeConstant;
import hk.gov.cmc.common.ResultCodes;
import hk.gov.cmc.common.ResultMessages;
import hk.gov.cmc.config.CmcEnvProperties;
import hk.gov.cmc.dto.maintainmessage.SingleMaintainMsgResult;
import hk.gov.cmc.model.maintainmessage.action.Action;
import hk.gov.cmc.model.maintainmessage.request.Recipient;
import hk.gov.cmc.model.maintainmessage.response.MaintainMessageResponse;
import hk.gov.cmc.model.maintainmessage.response.MessageResponse;
import hk.gov.cmc.model.maintainmessage.template.CmcTemplate;
import hk.gov.cmc.model.maintainmessage.user.IasUser;
import hk.gov.cmc.model.maintainmessage.user.IasUserWrapped;
import hk.gov.cmc.persistence.connection.hpfw.HPFW_Connection;
import hk.gov.cmc.persistence.maintainmessage.todoitem.IasUserToDoItem_;
import hk.gov.cmc.utils.maintainmessage.MaintainMessageUtils;
import hk.gov.cmc.utils.maintainmessage.ias.IasToDoItemUtils;
import hk.gov.cmc.utils.maintainmessage.ias.IasUtils;
import hk.gov.cmc.validator.IasToDoItemValidator;

public class IasToDoItemProcessor {

    private static Log logger = LogFactory.getLog(IasToDoItemProcessor.class);
    private final static CmcEnvProperties cmcEnvProperties = new CmcEnvProperties();

    public IasToDoItemProcessor() {
    }

    public SingleMaintainMsgResult processIasToDoItem(
            String portalId, String iasToDoItemId,
            Recipient recipient, IasUserWrapped iasUserWrapped,
            int toDoItemCutOffDay, int impDtUpperLimit,
            String dataContentEn, String dataContentTc, String dataContentSc,
            CmcTemplate cmcTemplate, HPFW_Connection conn,
            Map<String, String> iasToDoItemHandledCache, MaintainMessageResponse response) throws Exception {
        logger.info("processIasToDoItem - Start");
        logger.info("processIasToDoItem - portalId: " + portalId + ", iasToDoItemId: " + iasToDoItemId
                + ", recipient.getAction.getType: " + recipient.getAction().getType()
                + ", recipient.getIdpId: " + recipient.getIdpId()
                + ", recipient.getRecipientIdType: " + recipient.getRecipientIdType()
                + ", recipient.getTranId: " + recipient.getTranId());

        logger.debug("processIasToDoItem - portalId: " + portalId + ", iasToDoItemId: " + iasToDoItemId
                + ", recipient: " + recipient.toString() + ", iasUserWrapped.toString(): " + iasUserWrapped.toString()
                + ", cmcTemplate: " + cmcTemplate.toString() + ", toDoItemCutOffDay: " + toDoItemCutOffDay
                + ", impDtUpperLimit: " + impDtUpperLimit);
        logger.debug("processIasToDoItem - dataContentEn: " + dataContentEn + ", dataContentTc: " + dataContentTc
                + ", dataContentSc: " + dataContentSc);

        Properties properties = cmcEnvProperties.getProperties();
        MessageResponse msgRsp = null;
        SingleMaintainMsgResult singleMaintainMsgResult = new SingleMaintainMsgResult(iasToDoItemId, false);

        IasUser iasUser = iasUserWrapped.getIasUser();

        if (recipient.getAction() == Action.NEW) {
            logger.info("processIasToDoItem - action type: " + recipient.getAction().getType() + " is running"
                    + ". cmcTemplate.getClientId: " + cmcTemplate.getClientId()
                    + ", cmcTemplate.getTemplateId: " + cmcTemplate.getTemplateId()
                    + ", cmcTemplate.getTemplateVersion: " + cmcTemplate.getTemplateVersion()
                    + ", recipient.getTranId: " + recipient.getTranId() + ", recipient.getIdpId: "
                    + recipient.getIdpId());

            msgRsp = IasToDoItemValidator.validateItemDate(cmcTemplate.getClientId(), recipient.getIdpId(),
                    recipient.getRecipientId(),
                    recipient.getTranId(), toDoItemCutOffDay, impDtUpperLimit, recipient.getItemDate());

            if (msgRsp != null) {
                logger.info("processIasToDoItem - validateItemDate failed"
                        + ". cmcTemplate.getClientId: " + cmcTemplate.getClientId()
                        + ", cmcTemplate.getTemplateId: " + cmcTemplate.getTemplateId()
                        + ", cmcTemplate.getTemplateVersion: " + cmcTemplate.getTemplateVersion()
                        + ", msgRsp.getIdpId: " + msgRsp.getIdpId() + ", msgRsp.getTranId: " + msgRsp.getTranId()
                        + ", msgRsp.getTranResultCode: " + msgRsp.getTranResultCode()
                        + ", msgRsp.getTranResultMessage: " + msgRsp.getTranResultMessage());
                response.addMessageResponse(msgRsp);
            } else {
                // create the master record in IAS_TO_DO_ITEM
                if (iasToDoItemId == null || "".equals(iasToDoItemId)) {
                    iasToDoItemId = IasUtils
                            .getNextIasToDoItemId(properties.getProperty(CmcAppPropertyNames.SERVER_ID_PROPERTY_NAME));
                    logger.info("processIasToDoItem - action type: " + recipient.getAction().getType()
                            + ", generated iasToDoItemId: " + iasToDoItemId);

                    // merge the template and create the master record in IAS_TO_DO_ITEM
                    msgRsp = IasToDoItemUtils.mergeTemplateAndCreateIasToDoItem(
                            recipient.getIdpId(), recipient.getTranId(), recipient.getRecipientId(),
                            conn, portalId, iasToDoItemId,
                            cmcTemplate, dataContentEn, dataContentTc, dataContentSc,
                            properties);
                }

                if (msgRsp != null) {
                    logger.info("processIasToDoItem - mergeTemplateAndCreateIasToDoItem failed"
                            + ". cmcTemplate.getClientId: " + cmcTemplate.getClientId()
                            + ", cmcTemplate.getTemplateId: " + cmcTemplate.getTemplateId()
                            + ", cmcTemplate.getTemplateVersion: " + cmcTemplate.getTemplateVersion()
                            + ", msgRsp.getIdpId: " + msgRsp.getIdpId() + ", msgRsp.getTranId: " + msgRsp.getTranId()
                            + ", msgRsp.getTranResultCode: " + msgRsp.getTranResultCode()
                            + ", msgRsp.getTranResultMessage: " + msgRsp.getTranResultMessage());
                    response.addMessageResponse(msgRsp);
                } else {
                    // create the user to-do item record in IAS_USER_TO_DO_ITEM related to the master record
                    msgRsp = IasToDoItemUtils.createIasUserToDoItemByOpenIdOrHKID(conn,
                            cmcTemplate, recipient, iasUser, iasToDoItemId,
                            IasApplicationConstant.OPERATION_TYPE_CREATE);
                    if (msgRsp != null) {
                        logger.info(
                                "processIasToDoItem - createIasUserToDoItemByOpenIdOrHKID failed. msgRsp.getTranId: "
                                        + msgRsp.getTranId() + ", msgRsp.getIdpId: " + msgRsp.getIdpId()
                                        + ", msgRsp.getTranResultCode: " + msgRsp.getTranResultCode()
                                        + ", msgRsp.getTranResultMessage: " + msgRsp.getTranResultMessage());
                        response.addMessageResponse(msgRsp);
                    } else {
                        singleMaintainMsgResult.setSuccess(true);
                    }

                    // add to history cache map for next message checking is it already existed in ACTION=NEW, REPLACE, UPDATE, DELETE
                    iasToDoItemHandledCache.put(recipient.getTranId(), IasToDoItemConstant.HISTORY_NEW);
                }
            }

        } else if (recipient.getAction() == Action.REPLACE) {
            logger.info("processIasToDoItem - action type: " + recipient.getAction().getType() + " is running"
                    + ". cmcTemplate.getClientId: " + cmcTemplate.getClientId()
                    + ", cmcTemplate.getTemplateId: " + cmcTemplate.getTemplateId()
                    + ", cmcTemplate.getTemplateVersion: " + cmcTemplate.getTemplateVersion()
                    + ", recipient.getTranId: " + recipient.getTranId() + ", recipient.getCorrelatedTranId: "
                    + recipient.getCorrelatedTranId()
                    + ", recipient.getIdpId: " + recipient.getIdpId());

            msgRsp = IasToDoItemValidator.validateItemDate(cmcTemplate.getClientId(), recipient.getIdpId(),
                    recipient.getRecipientId(),
                    recipient.getTranId(), toDoItemCutOffDay, impDtUpperLimit, recipient.getItemDate());

            if (msgRsp != null) {
                logger.info("processIasToDoItem - validateItemDate failed"
                        + ". cmcTemplate.getClientId: " + cmcTemplate.getClientId()
                        + ", cmcTemplate.getTemplateId: " + cmcTemplate.getTemplateId()
                        + ", cmcTemplate.getTemplateVersion: " + cmcTemplate.getTemplateVersion()
                        + ", msgRsp.getIdpId: " + msgRsp.getIdpId() + ", msgRsp.getTranId: " + msgRsp.getTranId()
                        + ", msgRsp.getTranResultCode: " + msgRsp.getTranResultCode()
                        + ", msgRsp.getTranResultMessage: " + msgRsp.getTranResultMessage());
                response.addMessageResponse(msgRsp);
            } else {
                List<IasUserToDoItem_> existingIasUserToDoItemList = IasToDoItemUtils
                        .getCorrelatedIasUserToDoItemListByOpenIdOrHKID(
                                conn, cmcTemplate, recipient, iasUserWrapped, properties);
                msgRsp = IasToDoItemValidator.validateToDoItemIsExistedOrDeletedOrCompleted(
                        cmcTemplate.getClientId(), recipient.getIdpId(), recipient.getRecipientId(),
                        recipient.getTranId(), recipient.getCorrelatedTranId(),
                        iasToDoItemHandledCache, existingIasUserToDoItemList);
                if (msgRsp != null) {
                    logger.info("processIasToDoItem - validateToDoItemIsExistedOrDeletedOrCompleted failed"
                            + ". cmcTemplate.getClientId: " + cmcTemplate.getClientId()
                            + ", cmcTemplate.getTemplateId: " + cmcTemplate.getTemplateId()
                            + ", cmcTemplate.getTemplateVersion: " + cmcTemplate.getTemplateVersion()
                            + ", msgRsp.getIdpId: " + msgRsp.getIdpId() + ", msgRsp.getTranId: " + msgRsp.getTranId()
                            + ", msgRsp.getTranResultCode: " + msgRsp.getTranResultCode()
                            + ", msgRsp.getTranResultMessage: " + msgRsp.getTranResultMessage());
                    response.addMessageResponse(msgRsp);
                } else {
                    // create the master record in IAS_TO_DO_ITEM
                    if (iasToDoItemId == null || "".equals(iasToDoItemId)) {
                        iasToDoItemId = IasUtils.getNextIasToDoItemId(
                                properties.getProperty(CmcAppPropertyNames.SERVER_ID_PROPERTY_NAME));
                        logger.info("processIasToDoItem - action type: " + recipient.getAction().getType()
                                + ", generated iasToDoItemId: " + iasToDoItemId);

                        // merge the template and create the master record in IAS_TO_DO_ITEM
                        msgRsp = IasToDoItemUtils.mergeTemplateAndCreateIasToDoItem(
                                recipient.getIdpId(), recipient.getTranId(), recipient.getRecipientId(),
                                conn, portalId, iasToDoItemId,
                                cmcTemplate, dataContentEn, dataContentTc, dataContentSc,
                                properties);
                    }

                    if (msgRsp != null) {
                        logger.info("processIasToDoItem - mergeTemplateAndCreateIasToDoItem failed"
                                + ". cmcTemplate.getClientId: " + cmcTemplate.getClientId()
                                + ", cmcTemplate.getTemplateId: " + cmcTemplate.getTemplateId()
                                + ", cmcTemplate.getTemplateVersion: " + cmcTemplate.getTemplateVersion()
                                + ", msgRsp.getIdpId: " + msgRsp.getIdpId() + ", msgRsp.getTranId: "
                                + msgRsp.getTranId()
                                + ", msgRsp.getTranResultCode: " + msgRsp.getTranResultCode()
                                + ", msgRsp.getTranResultMessage: " + msgRsp.getTranResultMessage());
                        response.addMessageResponse(msgRsp);
                    } else {
                        // mark delete of the IAS_USER_TO_DO_ITEM record by recipient.getCorrelatedTranId, delayed the deletion to step 2 after the new record push noti is sent to iAM Smart
                        msgRsp = IasToDoItemUtils.markDeleteIasUserToDoItemByOpenIdOrHKIDAndCorrTranId(conn,
                                cmcTemplate, recipient, iasUser);

                        if (msgRsp != null) {
                            logger.info(
                                    "processIasToDoItem - markDeleteIasUserToDoItemByOpenIdOrHKIDAndCorrTranId failed"
                                            + ". cmcTemplate.getClientId: " + cmcTemplate.getClientId()
                                            + ", cmcTemplate.getTemplateId: " + cmcTemplate.getTemplateId()
                                            + ", cmcTemplate.getTemplateVersion: " + cmcTemplate.getTemplateVersion()
                                            + ", msgRsp.getIdpId: " + msgRsp.getIdpId() + ", msgRsp.getTranId: "
                                            + msgRsp.getTranId()
                                            + ", msgRsp.getTranResultCode: " + msgRsp.getTranResultCode()
                                            + ", msgRsp.getTranResultMessage: " + msgRsp.getTranResultMessage());
                            response.addMessageResponse(msgRsp);
                        } else {
                            // create the user to-do item record in IAS_USER_TO_DO_ITEM related to the master record
                            msgRsp = IasToDoItemUtils.createIasUserToDoItemByOpenIdOrHKID(conn,
                                    cmcTemplate, recipient, iasUser, iasToDoItemId,
                                    IasApplicationConstant.OPERATION_TYPE_REPLACE);
                            if (msgRsp != null) {
                                logger.info("processIasToDoItem - createIasUserToDoItemByOpenIdOrHKID failed"
                                        + ". cmcTemplate.getClientId: " + cmcTemplate.getClientId()
                                        + ", cmcTemplate.getTemplateId: " + cmcTemplate.getTemplateId()
                                        + ", cmcTemplate.getTemplateVersion: " + cmcTemplate.getTemplateVersion()
                                        + ", msgRsp.getIdpId: " + msgRsp.getIdpId() + ", msgRsp.getTranId: "
                                        + msgRsp.getTranId()
                                        + ", msgRsp.getTranResultCode: " + msgRsp.getTranResultCode()
                                        + ", msgRsp.getTranResultMessage: " + msgRsp.getTranResultMessage());
                                response.addMessageResponse(msgRsp);
                            } else {
                                singleMaintainMsgResult.setSuccess(true);
                            }

                            // add to history cache map for next message checking is it already existed in ACTION=NEW, REPLACE, UPDATE, DELETE
                            iasToDoItemHandledCache.put(recipient.getTranId(), IasToDoItemConstant.HISTORY_REPLACE);
                        }
                    }
                }
            }
        } else if (recipient.getAction() == Action.DELETE) {
            logger.info("processIasToDoItem - action type: " + recipient.getAction().getType() + " is running"
                    + ". cmcTemplate.getClientId: " + cmcTemplate.getClientId()
                    + ", cmcTemplate.getTemplateId: " + cmcTemplate.getTemplateId()
                    + ", cmcTemplate.getTemplateVersion: " + cmcTemplate.getTemplateVersion()
                    + ", recipient.getTranId: " + recipient.getTranId() + ", recipient.getCorrelatedTranId: "
                    + recipient.getCorrelatedTranId()
                    + ", recipient.getIdpId: " + recipient.getIdpId());

            List<IasUserToDoItem_> existingIasUserToDoItemList = IasToDoItemUtils
                    .getCorrelatedIasUserToDoItemListByOpenIdOrHKID(conn, cmcTemplate, recipient,
                            iasUserWrapped, properties);
            msgRsp = IasToDoItemValidator.validateToDoItemIsExistedOrDeletedOrCompleted(cmcTemplate.getClientId(),
                    recipient.getIdpId(),
                    recipient.getRecipientId(), recipient.getTranId(), recipient.getCorrelatedTranId(),
                    iasToDoItemHandledCache, existingIasUserToDoItemList);

            if (msgRsp != null) {
                logger.info("processIasToDoItem - validateToDoItemIsExistedOrDeletedOrCompleted failed"
                        + ". cmcTemplate.getClientId: " + cmcTemplate.getClientId()
                        + ", cmcTemplate.getTemplateId: " + cmcTemplate.getTemplateId()
                        + ", cmcTemplate.getTemplateVersion: " + cmcTemplate.getTemplateVersion()
                        + ", msgRsp.getIdpId: " + msgRsp.getIdpId() + ", msgRsp.getTranId: " + msgRsp.getTranId()
                        + ", msgRsp.getTranResultCode: " + msgRsp.getTranResultCode()
                        + ", msgRsp.getTranResultMessage: " + msgRsp.getTranResultMessage());
                response.addMessageResponse(msgRsp);
            } else {

                // mark delete of the IAS_USER_TO_DO_ITEM record by recipient.getTranId, delayed the deletion to step 2 after the new record push noti is sent to iAM Smart
                msgRsp = IasToDoItemUtils.markDeleteIasUserToDoItemByOpenIdOrHKIDAndCorrTranId(conn, cmcTemplate,
                        recipient, iasUser);

                if (msgRsp != null) {
                    logger.info("processIasToDoItem - markDeleteIasUserToDoItemByOpenIdOrHKIDAndTranId failed"
                            + ". cmcTemplate.getClientId: " + cmcTemplate.getClientId()
                            + ", cmcTemplate.getTemplateId: " + cmcTemplate.getTemplateId()
                            + ", cmcTemplate.getTemplateVersion: " + cmcTemplate.getTemplateVersion()
                            + ", msgRsp.getIdpId: " + msgRsp.getIdpId() + ", msgRsp.getTranId: " + msgRsp.getTranId()
                            + ", msgRsp.getTranResultCode: " + msgRsp.getTranResultCode()
                            + ", msgRsp.getTranResultMessage: " + msgRsp.getTranResultMessage());
                    response.addMessageResponse(msgRsp);
                } else {
                    singleMaintainMsgResult.setSuccess(true);
                }

                // add to history cache map for next message checking is it already existed in ACTION=REPLACE, UPDATE, DELETE
                iasToDoItemHandledCache.put(recipient.getCorrelatedTranId(), IasToDoItemConstant.HISTORY_DELETE);
            }

        } else if (recipient.getAction() == Action.MARK_COMPLETE) {
            logger.info("processIasToDoItem - action type: " + recipient.getAction().getType() + " is running"
                    + ". cmcTemplate.getClientId: " + cmcTemplate.getClientId()
                    + ", cmcTemplate.getTemplateId: " + cmcTemplate.getTemplateId()
                    + ", cmcTemplate.getTemplateVersion: " + cmcTemplate.getTemplateVersion()
                    + ", recipient.getTranId: " + recipient.getTranId()
                    + ", recipient.getCorrelatedTranId: " + recipient.getCorrelatedTranId()
                    + ", recipient.getIdpId: " + recipient.getIdpId());

            List<IasUserToDoItem_> existingIasUserToDoItemList = IasToDoItemUtils
                    .getCorrelatedIasUserToDoItemListByOpenIdOrHKID(conn, cmcTemplate, recipient, iasUserWrapped,
                            properties);
            msgRsp = IasToDoItemValidator.validateToDoItemIsExistedOrDeletedOrCompleted(cmcTemplate.getClientId(),
                    recipient.getIdpId(),
                    recipient.getRecipientId(), recipient.getTranId(), recipient.getCorrelatedTranId(),
                    iasToDoItemHandledCache, existingIasUserToDoItemList);

            if (msgRsp != null) {
                logger.info("processIasToDoItem - validateToDoItemIsExistedOrDeletedOrCompleted failed"
                        + ". cmcTemplate.getClientId: " + cmcTemplate.getClientId()
                        + ", cmcTemplate.getTemplateId: " + cmcTemplate.getTemplateId()
                        + ", cmcTemplate.getTemplateVersion: " + cmcTemplate.getTemplateVersion()
                        + ", msgRsp.getIdpId: " + msgRsp.getIdpId() + ", msgRsp.getTranId: " + msgRsp.getTranId()
                        + ", msgRsp.getTranResultCode: " + msgRsp.getTranResultCode()
                        + ", msgRsp.getTranResultMessage: " + msgRsp.getTranResultMessage());
                response.addMessageResponse(msgRsp);
            } else {

                // update the IAS_USER_TO_DO_ITEM record by recipient.getTranId
                msgRsp = IasToDoItemUtils.completeIasUserToDoItemByOpenIdOrHKID(conn, cmcTemplate,
                        recipient, iasUser);
                if (msgRsp != null) {
                    logger.info("processIasToDoItem - completeIasUserToDoItemByOpenIdOrHKID failed"
                            + ". cmcTemplate.getClientId: " + cmcTemplate.getClientId()
                            + ", cmcTemplate.getTemplateId: " + cmcTemplate.getTemplateId()
                            + ", cmcTemplate.getTemplateVersion: " + cmcTemplate.getTemplateVersion()
                            + ", msgRsp.getIdpId: " + msgRsp.getIdpId() + ", msgRsp.getTranId: " + msgRsp.getTranId()
                            + ", msgRsp.getTranResultCode: " + msgRsp.getTranResultCode()
                            + ", msgRsp.getTranResultMessage: " + msgRsp.getTranResultMessage());
                    response.addMessageResponse(msgRsp);
                } else {
                    singleMaintainMsgResult.setSuccess(true);
                }

            }

        } else {
            msgRsp = MaintainMessageUtils.getMessageResponse(recipient.getTranId(), recipient.getIdpId(),
                    recipient.getRecipientId(),
                    MsgTypeConstant.TO_DO_ITEM, ResultCodes.RESULT_CD_TO_DO_ITEM_ACTION_NOT_VALID,
                    ResultMessages.RESULT_MSG_TO_DO_ITEM_ACTION_NOT_VALID);
            response.addMessageResponse(msgRsp);
        }

        logger.info("processIasToDoItem - END");
        return singleMaintainMsgResult;
    }

}
