package hk.gov.cmc.processor.maintainmessage.application;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import hk.gov.cmc.common.CmcAppPropertyNames;
import hk.gov.cmc.common.IasApplicationConstant;
import hk.gov.cmc.config.CmcEnvProperties;
import hk.gov.cmc.dao.maintainmessage.application.IasApplicationDAO;
import hk.gov.cmc.dto.maintainmessage.SingleMaintainMsgResult;
import hk.gov.cmc.model.maintainmessage.action.Action;
import hk.gov.cmc.model.maintainmessage.param.MessageParam;
import hk.gov.cmc.model.maintainmessage.request.Recipient;
import hk.gov.cmc.model.maintainmessage.response.MaintainMessageResponse;
import hk.gov.cmc.model.maintainmessage.response.MessageResponse;
import hk.gov.cmc.model.maintainmessage.template.CmcTemplate;
import hk.gov.cmc.model.maintainmessage.user.IasUser;
import hk.gov.cmc.model.maintainmessage.user.IasUserWrapped;
import hk.gov.cmc.persistence.connection.hpfw.HPFW_Connection;
import hk.gov.cmc.persistence.ias.application.IasApplication_;
import hk.gov.cmc.persistence.ias.application.IasUserApplication_;
import hk.gov.cmc.utils.maintainmessage.ias.IasApplicationUtils;
import hk.gov.cmc.utils.maintainmessage.ias.IasUtils;
import hk.gov.cmc.validator.IasApplicationValidator;

public class IasApplicationProcessor {

    private static Log logger = LogFactory.getLog(IasApplicationProcessor.class);
    private final static CmcEnvProperties cmcEnvProperties = new CmcEnvProperties();

    public IasApplicationProcessor() {
    }

    public SingleMaintainMsgResult processIasApplication(
            String portalId, String iasApplicationId,
            Recipient recipient, IasUserWrapped iasUserWrapped,
            String dataContentEn, String dataContentTc, String dataContentSc,
            CmcTemplate cmcTemplate, List<MessageParam> cmcMessageParam, HPFW_Connection conn,
            Map<String, String> iasApplicationHandledCache, MaintainMessageResponse response) throws Exception {
        logger.info("processIasApplication - Start");
        logger.info("processIasApplication - portalId: " + portalId + ", iasApplicationId: " + iasApplicationId
                + ", recipient.getAction.getType: " + recipient.getAction().getType()
                + ", recipient.getIdpId: " + recipient.getIdpId()
                + ", recipient.getRecipientIdType: " + recipient.getRecipientIdType()
                + ", recipient.getAppRefNum: " + recipient.getAppRefNum());

        logger.debug("processIasApplication - portalId: " + portalId + ", iasApplicationId: " + iasApplicationId
                + ", recipient: " + recipient.toString() + ", iasUserWrapped.toString(): " + iasUserWrapped.toString()
                + ", cmcTemplate: " + cmcTemplate.toString() + ", cmcMessageParam size: " + cmcMessageParam.size());
        logger.debug("processIasApplication - dataContentEn: " + dataContentEn + ", dataContentTc: " + dataContentTc
                + ", dataContentSc: " + dataContentSc);

        IasApplicationDAO iasApplicationDAO = new IasApplicationDAO();
        Properties properties = cmcEnvProperties.getProperties();
        MessageResponse msgRsp = null;
        SingleMaintainMsgResult singleMaintainMsgResult = new SingleMaintainMsgResult(iasApplicationId, false);

        IasUser iasUser = iasUserWrapped.getIasUser();

        msgRsp = IasApplicationValidator.validateAppStatus(cmcTemplate.getClientId(),
                recipient.getIdpId(), recipient.getRecipientId(), recipient.getTranId(),
                recipient.getAppStatus().value());

        if (msgRsp != null) {
            logger.info("processIasApplication - validateAppStatus failed"
                    + ". cmcTemplate.getClientId: " + cmcTemplate.getClientId()
                    + ", cmcTemplate.getTemplateId: " + cmcTemplate.getTemplateId()
                    + ", cmcTemplate.getTemplateVersion: " + cmcTemplate.getTemplateVersion()
                    + ", msgRsp.getIdpId: " + msgRsp.getIdpId() + ", msgRsp.getAppRefNum: " + msgRsp.getAppRefNum()
                    + ", msgRsp.getTranResultCode: " + msgRsp.getTranResultCode() + ", msgRsp.getTranResultMessage: "
                    + msgRsp.getTranResultMessage());
            response.addMessageResponse(msgRsp);
        } else {
            if (recipient.getAction() == Action.NEW) {
                logger.info("processIasApplication - action type: " + recipient.getAction().getType() + " is running"
                        + ". cmcTemplate.getClientId: " + cmcTemplate.getClientId()
                        + ", cmcTemplate.getTemplateId: " + cmcTemplate.getTemplateId()
                        + ", cmcTemplate.getTemplateVersion: " + cmcTemplate.getTemplateVersion()
                        + ", recipient.getTranId: " + recipient.getTranId() + ", recipient.getIdpId: "
                        + recipient.getIdpId());

                // create the master record in IAS_APPLICATION
                if (iasApplicationId == null || "".equals(iasApplicationId)) {
                    iasApplicationId = IasUtils.getNextIasApplicationId(
                            properties.getProperty(CmcAppPropertyNames.SERVER_ID_PROPERTY_NAME));
                    logger.info("processIasApplication - action type: " + recipient.getAction().getType()
                            + ", generated iasApplicationId: " + iasApplicationId);

                    // merge the template and create the master record in IAS_APPLICATION
                    msgRsp = IasApplicationUtils.mergeTemplateAndCreateIasApplication(
                            recipient.getIdpId(), recipient.getTranId(), recipient.getRecipientId(),
                            conn, portalId, iasApplicationId, cmcTemplate, cmcMessageParam,
                            dataContentEn, dataContentTc, dataContentSc,
                            properties);
                }

                if (msgRsp != null) {
                    logger.info("processIasApplication - mergeTemplateAndCreateIasApplication failed"
                            + ". cmcTemplate.getClientId: " + cmcTemplate.getClientId()
                            + ", cmcTemplate.getTemplateId: " + cmcTemplate.getTemplateId()
                            + ", cmcTemplate.getTemplateVersion: " + cmcTemplate.getTemplateVersion()
                            + ", msgRsp.getIdpId: " + msgRsp.getIdpId()
                            + ", msgRsp.getAppRefNum: " + msgRsp.getAppRefNum()
                            + ", msgRsp.getTranResultCode: " + msgRsp.getTranResultCode()
                            + ", msgRsp.getTranResultMessage: " + msgRsp.getTranResultMessage());
                    response.addMessageResponse(msgRsp);
                } else {
                    // create the user application record in IAS_USER_APPLICATION related to the master record
                    msgRsp = IasApplicationUtils.createIasUserApplicationByOpenIdOrHKID(conn, iasApplicationDAO,
                            cmcTemplate, recipient, iasUser,
                            iasApplicationId, IasApplicationConstant.OPERATION_TYPE_CREATE);
                    if (msgRsp != null) {
                        response.addMessageResponse(msgRsp);
                    } else {
                        singleMaintainMsgResult.setSuccess(true);
                    }

                    // add to history cache map for next message checking is it already existed in ACTION=NEW, REPLACE, UPDATE, DELETE
                    iasApplicationHandledCache.put(recipient.getTranId(), IasApplicationConstant.HISTORY_NEW);
                }

            } else if (recipient.getAction() == Action.REPLACE) {
                logger.info("processIasApplication - action value: " + recipient.getAction().value() + " is running"
                        + ". cmcTemplate.getClientId: " + cmcTemplate.getClientId()
                        + ", cmcTemplate.getTemplateId: " + cmcTemplate.getTemplateId()
                        + ", cmcTemplate.getTemplateVersion: " + cmcTemplate.getTemplateVersion()
                        + ", recipient.getTranId: " + recipient.getTranId()
                        + ", recipient.getCorrelatedTranId: " + recipient.getCorrelatedTranId()
                        + ", recipient.getIdpId: " + recipient.getIdpId());

                List<IasUserApplication_> existingIasUserApplicationList = IasApplicationUtils
                        .getCorrelatedIasUserApplicationListByOpenIdOrHKID(
                                conn, iasApplicationDAO, cmcTemplate, recipient, iasUserWrapped, properties);
                msgRsp = IasApplicationValidator.validateApplicationIsExistedOrDeleted(
                        cmcTemplate.getClientId(), recipient.getIdpId(), recipient.getRecipientId(),
                        recipient.getTranId(), recipient.getCorrelatedTranId(),
                        iasApplicationHandledCache, existingIasUserApplicationList);

                if (msgRsp != null) {
                    logger.info("processIasApplication - validateApplicationIsExistedOrDeleted failed"
                            + ". cmcTemplate.getClientId: " + cmcTemplate.getClientId()
                            + ", cmcTemplate.getTemplateId: " + cmcTemplate.getTemplateId()
                            + ", cmcTemplate.getTemplateVersion: " + cmcTemplate.getTemplateVersion()
                            + ", msgRsp.getIdpId: " + msgRsp.getIdpId()
                            + ", msgRsp.getAppRefNum: " + msgRsp.getAppRefNum()
                            + ", msgRsp.getTranResultCode: " + msgRsp.getTranResultCode()
                            + ", msgRsp.getTranResultMessage: " + msgRsp.getTranResultMessage());
                    response.addMessageResponse(msgRsp);
                } else {

                    msgRsp = IasApplicationValidator.validateAppStatusUpdateDate(cmcTemplate.getClientId(),
                            recipient.getIdpId(),
                            recipient.getRecipientId(), recipient.getTranId(), recipient.getAppStatusUpdateDate(),
                            existingIasUserApplicationList);

                    if (msgRsp != null) {
                        logger.info("processIasApplication - validateAppStatusUpdateDate failed"
                                + ". cmcTemplate.getClientId: " + cmcTemplate.getClientId()
                                + ", cmcTemplate.getTemplateId: " + cmcTemplate.getTemplateId()
                                + ", cmcTemplate.getTemplateVersion: " + cmcTemplate.getTemplateVersion()
                                + ", msgRsp.getIdpId: " + msgRsp.getIdpId()
                                + ", msgRsp.getAppRefNum: " + msgRsp.getAppRefNum()
                                + ", msgRsp.getTranResultCode: " + msgRsp.getTranResultCode()
                                + ", msgRsp.getTranResultMessage: " + msgRsp.getTranResultMessage());
                        response.addMessageResponse(msgRsp);
                    } else {
                        if (iasApplicationId == null || "".equals(iasApplicationId)) {
                            iasApplicationId = IasUtils.getNextIasApplicationId(
                                    properties.getProperty(CmcAppPropertyNames.SERVER_ID_PROPERTY_NAME));
                            logger.info("processIasApplication - action type: " + recipient.getAction().getType()
                                    + ", generated iasApplicationId: " + iasApplicationId);

                            // merge the template and create the master record in IAS_APPLICATION
                            msgRsp = IasApplicationUtils.mergeTemplateAndCreateIasApplication(
                                    recipient.getIdpId(), recipient.getTranId(), recipient.getRecipientId(),
                                    conn, portalId, iasApplicationId,
                                    cmcTemplate, cmcMessageParam,
                                    dataContentEn, dataContentTc, dataContentSc,
                                    properties);
                        }

                        if (msgRsp != null) {
                            logger.info("processIasApplication - mergeTemplateAndCreateIasApplication failed"
                                    + ". cmcTemplate.getClientId: " + cmcTemplate.getClientId()
                                    + ", cmcTemplate.getTemplateId: " + cmcTemplate.getTemplateId()
                                    + ", cmcTemplate.getTemplateVersion: " + cmcTemplate.getTemplateVersion()
                                    + ", msgRsp.getIdpId: " + msgRsp.getIdpId()
                                    + ", msgRsp.getAppRefNum: " + msgRsp.getAppRefNum()
                                    + ", msgRsp.getTranResultCode: " + msgRsp.getTranResultCode()
                                    + ", msgRsp.getTranResultMessage: " + msgRsp.getTranResultMessage());
                            response.addMessageResponse(msgRsp);
                        } else {

                            // mark delete of the previous record, delayed the deletion to step 2 after the new record push noti is sent to iAM Smart
                            msgRsp = IasApplicationUtils.markDeleteIasUserApplicationByOpenIdOrHKIDAndCorrTranId(conn,
                                    iasApplicationDAO, cmcTemplate, recipient, iasUser);
                            if (msgRsp != null) {
                                logger.info(
                                        "processIasApplication - markDeleteIasUserApplicationByOpenIdOrHKIDAndCorrTranId failed"
                                                + ". cmcTemplate.getClientId: " + cmcTemplate.getClientId()
                                                + ", cmcTemplate.getTemplateId: " + cmcTemplate.getTemplateId()
                                                + ", cmcTemplate.getTemplateVersion: "
                                                + cmcTemplate.getTemplateVersion()
                                                + ", msgRsp.getIdpId: " + msgRsp.getIdpId()
                                                + ", msgRsp.getTranId: " + msgRsp.getTranId()
                                                + ", msgRsp.getTranResultCode: " + msgRsp.getTranResultCode()
                                                + ", msgRsp.getTranResultMessage: " + msgRsp.getTranResultMessage());
                                response.addMessageResponse(msgRsp);
                            } else {
                                // create the user application record in IAS_USER_APPLICATION related to the master record
                                msgRsp = IasApplicationUtils.createIasUserApplicationByOpenIdOrHKID(conn,
                                        iasApplicationDAO, cmcTemplate,
                                        recipient, iasUser, iasApplicationId,
                                        IasApplicationConstant.OPERATION_TYPE_REPLACE);
                                if (msgRsp != null) {
                                    response.addMessageResponse(msgRsp);
                                } else {
                                    singleMaintainMsgResult.setSuccess(true);
                                }

                                // add to history cache map for next message checking is it already existed in ACTION=NEW, REPLACE, UPDATE, DELETE
                                iasApplicationHandledCache.put(recipient.getAppRefNum(),
                                        IasApplicationConstant.HISTORY_REPLACE);
                            }
                        }
                    }
                }

            } else if (recipient.getAction() == Action.DELETE) {
                logger.info("processIasApplication - action value: " + recipient.getAction().value() + " is running"
                        + ". cmcTemplate.getClientId: " + cmcTemplate.getClientId()
                        + ", cmcTemplate.getTemplateId: " + cmcTemplate.getTemplateId()
                        + ", cmcTemplate.getTemplateVersion: " + cmcTemplate.getTemplateVersion()
                        + ", recipient.getTranId: " + recipient.getTranId()
                        + ", recipient.getCorrelatedTranId: " + recipient.getCorrelatedTranId()
                        + ", recipient.getIdpId: " + recipient.getIdpId());

                List<IasUserApplication_> existingIasUserApplicationList = IasApplicationUtils
                        .getCorrelatedIasUserApplicationListByOpenIdOrHKID(
                                conn, iasApplicationDAO, cmcTemplate, recipient, iasUserWrapped, properties);
                msgRsp = IasApplicationValidator.validateApplicationIsExistedOrDeleted(
                        cmcTemplate.getClientId(), recipient.getIdpId(), recipient.getRecipientId(),
                        recipient.getTranId(), recipient.getCorrelatedTranId(),
                        iasApplicationHandledCache, existingIasUserApplicationList);

                if (msgRsp != null) {
                    logger.info("processIasApplication - validateApplicationIsExistedOrDeleted failed"
                            + ". cmcTemplate.getClientId: " + cmcTemplate.getClientId()
                            + ", cmcTemplate.getTemplateId: " + cmcTemplate.getTemplateId()
                            + ", cmcTemplate.getTemplateVersion: " + cmcTemplate.getTemplateVersion()
                            + ", msgRsp.getIdpId: " + msgRsp.getIdpId() + ", msgRsp.getAppRefNum: "
                            + msgRsp.getAppRefNum()
                            + ", msgRsp.getTranResultCode: " + msgRsp.getTranResultCode()
                            + ", msgRsp.getTranResultMessage: " + msgRsp.getTranResultMessage());
                    response.addMessageResponse(msgRsp);
                } else {

                    // mark delete of the previous record, delayed the deletion to step 2 after the new record push noti is sent to iAM Smart
                    // add to history cache map for next message checking is it already existed in ACTION=NEW, REPLACE, UPDATE, DELETE
                    msgRsp = IasApplicationUtils.markDeleteIasUserApplicationByOpenIdOrHKIDAndCorrTranId(conn,
                            iasApplicationDAO, cmcTemplate, recipient, iasUser);
                    if (msgRsp != null) {
                        logger.info(
                                "processIasApplication - markDeleteIasUserApplicationByOpenIdOrHKIDAndCorrTranId failed"
                                        + ". cmcTemplate.getClientId: " + cmcTemplate.getClientId()
                                        + ", cmcTemplate.getTemplateId: " + cmcTemplate.getTemplateId()
                                        + ", cmcTemplate.getTemplateVersion: " + cmcTemplate.getTemplateVersion()
                                        + ", msgRsp.getIdpId: " + msgRsp.getIdpId()
                                        + ", msgRsp.getTranId: " + msgRsp.getTranId()
                                        + ", msgRsp.getTranResultCode: " + msgRsp.getTranResultCode()
                                        + ", msgRsp.getTranResultMessage: " + msgRsp.getTranResultMessage());
                        response.addMessageResponse(msgRsp);
                    } else {
                        singleMaintainMsgResult.setSuccess(true);
                    }

                    iasApplicationHandledCache.put(recipient.getCorrelatedTranId(),
                            IasApplicationConstant.HISTORY_DELETE);
                }

            } else if (recipient.getAction() == Action.UPDATE) {
                logger.info("processIasApplication - action value: " + recipient.getAction().value() + " is running"
                        + ". cmcTemplate.getClientId: " + cmcTemplate.getClientId()
                        + ", cmcTemplate.getTemplateId: " + cmcTemplate.getTemplateId()
                        + ", cmcTemplate.getTemplateVersion: " + cmcTemplate.getTemplateVersion()
                        + ", recipient.getTranId: " + recipient.getTranId()
                        + ", recipient.getCorrelatedTranId: " + recipient.getCorrelatedTranId()
                        + ", recipient.getIdpId: " + recipient.getIdpId());

                List<IasUserApplication_> existingIasUserApplicationList = IasApplicationUtils
                        .getCorrelatedIasUserApplicationListByOpenIdOrHKID(
                                conn, iasApplicationDAO, cmcTemplate, recipient, iasUserWrapped, properties);
                msgRsp = IasApplicationValidator.validateApplicationIsExistedOrDeleted(
                        cmcTemplate.getClientId(), recipient.getIdpId(), recipient.getRecipientId(),
                        recipient.getTranId(), recipient.getCorrelatedTranId(),
                        iasApplicationHandledCache, existingIasUserApplicationList);

                if (msgRsp != null) {
                    logger.info("processIasApplication - validateApplicationIsExistedOrDeleted failed"
                            + ". cmcTemplate.getClientId: " + cmcTemplate.getClientId()
                            + ", cmcTemplate.getTemplateId: " + cmcTemplate.getTemplateId()
                            + ", cmcTemplate.getTemplateVersion: " + cmcTemplate.getTemplateVersion()
                            + ", msgRsp.getIdpId: " + msgRsp.getIdpId() + ", msgRsp.getAppRefNum: "
                            + msgRsp.getAppRefNum()
                            + ", msgRsp.getTranResultCode: " + msgRsp.getTranResultCode()
                            + ", msgRsp.getTranResultMessage: " + msgRsp.getTranResultMessage());
                    response.addMessageResponse(msgRsp);
                } else {

                    msgRsp = IasApplicationValidator.validateAppStatusUpdateDate(cmcTemplate.getClientId(),
                            recipient.getIdpId(),
                            recipient.getRecipientId(), recipient.getTranId(), recipient.getAppStatusUpdateDate(),
                            existingIasUserApplicationList);
                    if (msgRsp != null) {
                        logger.info("processIasApplication - validateAppStatusUpdateDate failed"
                                + ". cmcTemplate.getClientId: " + cmcTemplate.getClientId()
                                + ", cmcTemplate.getTemplateId: " + cmcTemplate.getTemplateId()
                                + ", cmcTemplate.getTemplateVersion: " + cmcTemplate.getTemplateVersion()
                                + ", msgRsp.getIdpId: " + msgRsp.getIdpId() + ", msgRsp.getAppRefNum: "
                                + msgRsp.getAppRefNum()
                                + ", msgRsp.getTranResultCode: " + msgRsp.getTranResultCode()
                                + ", msgRsp.getTranResultMessage: " + msgRsp.getTranResultMessage());
                        response.addMessageResponse(msgRsp);
                    } else {
                        // mark delete of the previous record, delayed the deletion to step 2 after the new record push noti is sent to iAM Smart
                        msgRsp = IasApplicationUtils.markDeleteIasUserApplicationByOpenIdOrHKIDAndCorrTranId(conn,
                                iasApplicationDAO, cmcTemplate, recipient, iasUser);
                        iasApplicationHandledCache.put(recipient.getTranId(), IasApplicationConstant.HISTORY_DELETE);

                        // Duplicate the previous master record to the new master record
                        IasUserApplication_ toBeDeleteIasUserApplication_ = existingIasUserApplicationList.get(0);
                        IasApplication_ iasApplication_ = new IasApplication_(conn,
                                toBeDeleteIasUserApplication_.getIasApplicationId());
                        String newIasApplicationId = IasUtils.getNextIasApplicationId(
                                properties.getProperty(CmcAppPropertyNames.SERVER_ID_PROPERTY_NAME));
                        logger.info("processIasApplication - action type: " + recipient.getAction().getType()
                                + ", original iasApplicationId; " + iasApplication_.getIasApplicationId()
                                + ", generated newIasApplicationId: " + newIasApplicationId);
                        iasApplication_.setIasApplicationId(newIasApplicationId);
                        conn.begin(null, conn.getLastUpTime(), HPFW_Connection.DIRECT);
                        iasApplication_.insert(conn);
                        conn.begin(null, conn.getLastUpTime(), HPFW_Connection.DIRECT_WITH_HISTORY);

                        // create the user application record in IAS_USER_APPLICATION related to the master record
                        msgRsp = IasApplicationUtils.createIasUserApplicationByOpenIdOrHKID(conn, iasApplicationDAO,
                                cmcTemplate,
                                recipient, iasUser, newIasApplicationId, IasApplicationConstant.OPERATION_TYPE_UPDATE);
                        if (msgRsp != null) {
                            response.addMessageResponse(msgRsp);
                        } else {
                            singleMaintainMsgResult.setSuccess(true);
                        }

                        // add to history cache map for next message checking is it already existed in ACTION=NEW, REPLACE, UPDATE, DELETE
                        iasApplicationHandledCache.put(recipient.getTranId(), IasApplicationConstant.HISTORY_UPDATE);
                    }
                }
            }
        }

        logger.info("processIasApplication - END");
        return singleMaintainMsgResult;
    }

}
