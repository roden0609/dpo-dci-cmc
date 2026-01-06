package hk.gov.ogcio.mars_cmc.cmc.service.controller;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import hk.gov.ogcio.egis.rm.common.utils.ServiceLocator;
import hk.gov.ogcio.mars_cmc.cmc.appserver.AppPropertyName;
import hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MaintainMessageResponse;
import hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageResponse;
import hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.Recipient;
import hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.ToDoItem;
import hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.Action_ST;
import hk.gov.ogcio.mars_cmc.cmc.datatype.MessageParam;
import hk.gov.ogcio.mars_cmc.cmc.datatype.message.IasUser;
import hk.gov.ogcio.mars_cmc.cmc.datatype.message.IasUserWrapped;
import hk.gov.ogcio.mars_cmc.cmc.service.constant.IasApplicationConstant;
import hk.gov.ogcio.mars_cmc.cmc.service.constant.IasToDoItemConstant;
import hk.gov.ogcio.mars_cmc.cmc.service.dao.iastodoitem.IasToDoItemDAO;
import hk.gov.ogcio.mars_cmc.cmc.service.databean.CmcTemplate;
import hk.gov.ogcio.mars_cmc.cmc.service.databean.IasUserToDoItem_;
import hk.gov.ogcio.mars_cmc.cmc.service.dto.SingleMaintainMsgResult;
import hk.gov.ogcio.mars_cmc.cmc.service.utils.IasToDoItemUtils;
import hk.gov.ogcio.mars_cmc.cmc.service.validator.IasToDoItemValidator;
import hk.gov.ogcio.mars_cmc.cmc.utils.IasUtils;
import hk.gov.ogcio.mars_cmc.framework.common.sql.HPFW_Connection;

public class IasToDoItemController {

    private static Log logger = LogFactory.getLog(IasToDoItemController.class);

    public IasToDoItemController() {
    }

    public SingleMaintainMsgResult processIasToDoItem(
            String portalId, String iasToDoItemId, 
            ToDoItem toDoItem, Recipient recipient, IasUserWrapped iasUserWrapped,
            int toDoItemCutOffDay, int impDtUpperLimit,
            String titleEn, String titleTc, String titleSc,
            String detailEn, String detailTc, String detailSc, 
            CmcTemplate cmcTemplate, List<MessageParam> cmcMessageParam, HPFW_Connection conn,
            Map<String, String> iasToDoItemHandledCache, MaintainMessageResponse response
    ) throws Exception {
        logger.info("processIasToDoItem - Start");
        logger.info("processIasToDoItem - portalId: " + portalId + ", iasToDoItemId: " + iasToDoItemId + 
            ", recipient.getAction.getType: " + recipient.getAction().getType() + ", recipient.getIdpID: " + recipient.getIdpID() +
            ", recipient.getRecipientIDType: " + recipient.getRecipientIDType() + ", recipient.getTranID: " + recipient.getTranID());

        logger.debug("processIasToDoItem - portalId: " + portalId + ", iasToDoItemId: " + iasToDoItemId + ", toDoItem: " + toDoItem.toString() + 
            ", recipient: " + recipient.toString() + ", iasUserWrapped.toString(): " + iasUserWrapped.toString() + 
            ", cmcTemplate: " + cmcTemplate.toString() + ", cmcMessageParam size: " + cmcMessageParam.size() + 
            ", toDoItemCutOffDay: " + toDoItemCutOffDay + ", impDtUpperLimit: " + impDtUpperLimit);
        logger.debug("processIasToDoItem - titleEn: " + titleEn + ", titleTc: " + titleTc + ", titleSc: " + titleSc);
        logger.debug("processIasToDoItem - detailEn: " + detailEn + ", detailTc: " + detailTc + ", detailSc: " + detailSc);

        IasToDoItemDAO iasToDoItemDAO = new IasToDoItemDAO();
        Properties properties = ServiceLocator.getInstance(null).getProperties();
        MessageResponse msgRsp = null;
        SingleMaintainMsgResult singleMaintainMsgResult = new SingleMaintainMsgResult(iasToDoItemId, false);

        IasUser iasUser = iasUserWrapped.getIasUser();

        if (recipient.getAction().getType() == Action_ST.NEW_TYPE) {
            logger.info("processIasToDoItem - action type: " + recipient.getAction().getType() + " is running"
                + ". cmcTemplate.getClientId: " + cmcTemplate.getClientId() 
                + ", cmcTemplate.getTemplateId: " + cmcTemplate.getTemplateId() + ", cmcTemplate.getTemplateVersion: " + cmcTemplate.getTemplateVersion()
                + ", recipient.getTranID: " + recipient.getTranID() + ", recipient.getIdpID: " + recipient.getIdpID());

            msgRsp = IasToDoItemValidator.validateItemDate(cmcTemplate.getClientId(), recipient.getIdpID(), recipient.getRecipientID(), 
                recipient.getTranID(), toDoItemCutOffDay, impDtUpperLimit, recipient.getItemDate());

            if (msgRsp != null) {
                logger.info("processIasToDoItem - validateItemDate failed"
                    + ". cmcTemplate.getClientId: " + cmcTemplate.getClientId() 
                    + ", cmcTemplate.getTemplateId: " + cmcTemplate.getTemplateId() + ", cmcTemplate.getTemplateVersion: " + cmcTemplate.getTemplateVersion()
                    + ", msgRsp.getIdpID: " + msgRsp.getIdpID() + ", msgRsp.getTranID: " + msgRsp.getTranID() 
                    + ", msgRsp.getTranResultCode: " + msgRsp.getTranResultCode() + ", msgRsp.getTranResultMessage: " + msgRsp.getTranResultMessage());
                response.addMessageResponse(msgRsp);
            } else {
                // create the master record in IAS_TO_DO_ITEM
                if (iasToDoItemId == null || "".equals(iasToDoItemId)) {
                    iasToDoItemId = IasUtils.getNextIasToDoItemId(properties.getProperty(CmcAppPropertyNames.SERVER_ID_PROPERTY_NAME));
                    logger.info("processIasToDoItem - action type: " + recipient.getAction().getType() + ", generated iasToDoItemId: " + iasToDoItemId);

                    // merge the template and create the master record in IAS_TO_DO_ITEM
                    msgRsp = IasToDoItemUtils.mergeTemplateAndCreateIasToDoItem(
                        recipient.getIdpID(), recipient.getTranID(), recipient.getRecipientID(),
                        conn, portalId, iasToDoItemId, 
                        cmcTemplate, cmcMessageParam, titleEn, titleTc, titleSc, detailEn, detailTc, detailSc, properties);
                }

                if (msgRsp != null) {
                    logger.info("processIasToDoItem - mergeTemplateAndCreateIasToDoItem failed"
                        + ". cmcTemplate.getClientId: " + cmcTemplate.getClientId() 
                        + ", cmcTemplate.getTemplateId: " + cmcTemplate.getTemplateId() + ", cmcTemplate.getTemplateVersion: " + cmcTemplate.getTemplateVersion()
                        + ", msgRsp.getIdpID: " + msgRsp.getIdpID() + ", msgRsp.getTranID: " + msgRsp.getTranID() 
                        + ", msgRsp.getTranResultCode: " + msgRsp.getTranResultCode() + ", msgRsp.getTranResultMessage: " + msgRsp.getTranResultMessage());
                    response.addMessageResponse(msgRsp);
                } else {
                    // create the user to-do item record in IAS_USER_TO_DO_ITEM related to the master record
                    msgRsp = IasToDoItemUtils.createIasUserToDoItemByOpenIdOrHKID(conn, iasToDoItemDAO, cmcTemplate, recipient, iasUser, iasToDoItemId, IasApplicationConstant.OPERATION_TYPE_CREATE);
                    if (msgRsp != null) {
                        logger.info("processIasToDoItem - createIasUserToDoItemByOpenIdOrHKID failed. msgRsp.getTranID: " + msgRsp.getTranID() + ", msgRsp.getIdpID: " + msgRsp.getIdpID() 
                            + ", msgRsp.getTranResultCode: " + msgRsp.getTranResultCode() + ", msgRsp.getTranResultMessage: " + msgRsp.getTranResultMessage());
                        response.addMessageResponse(msgRsp);
                    } else {
                        singleMaintainMsgResult.setSuccess(true);
                        // response.addMessageResponse(ResponseUtils.getMessageResponse(recipient.getTranID(), recipient.getIdpID(), 
                        //     recipient.getRecipientID(), ResultCodes.RESULT_CD_TRAN_SUCCESS, ResultMessages.RESULT_MSG_TRAN_SUCCESS));
                    }

                    // add to history cache map for next message checking is it already existed in ACTION=NEW, REPLACE, UPDATE, DELETE
                    iasToDoItemHandledCache.put(recipient.getTranID(), IasToDoItemConstant.HISTORY_NEW);
                }
            }

        } else if (recipient.getAction().getType() == Action_ST.REPLACE_TYPE) {
            logger.info("processIasToDoItem - action type: " + recipient.getAction().getType() + " is running"
                + ". cmcTemplate.getClientId: " + cmcTemplate.getClientId() 
                + ", cmcTemplate.getTemplateId: " + cmcTemplate.getTemplateId() + ", cmcTemplate.getTemplateVersion: " + cmcTemplate.getTemplateVersion()
                + ", recipient.getTranID: " + recipient.getTranID() + ", recipient.getCorrelatedTranID: " + recipient.getCorrelatedTranID()
                + ", recipient.getIdpID: " + recipient.getIdpID());

            msgRsp = IasToDoItemValidator.validateItemDate(cmcTemplate.getClientId(), recipient.getIdpID(), recipient.getRecipientID(), 
                recipient.getTranID(), toDoItemCutOffDay, impDtUpperLimit, recipient.getItemDate());

            if (msgRsp != null) {
                logger.info("processIasToDoItem - validateItemDate failed"
                    + ". cmcTemplate.getClientId: " + cmcTemplate.getClientId() 
                    + ", cmcTemplate.getTemplateId: " + cmcTemplate.getTemplateId() + ", cmcTemplate.getTemplateVersion: " + cmcTemplate.getTemplateVersion()
                    + ", msgRsp.getIdpID: " + msgRsp.getIdpID() + ", msgRsp.getTranID: " + msgRsp.getTranID() 
                    + ", msgRsp.getTranResultCode: " + msgRsp.getTranResultCode() + ", msgRsp.getTranResultMessage: " + msgRsp.getTranResultMessage());
                response.addMessageResponse(msgRsp);
            } else {
                List<IasUserToDoItem_> existingIasUserToDoItemList = IasToDoItemUtils.getCorrelatedIasUserToDoItemListByOpenIdOrHKID(
                    conn, iasToDoItemDAO, cmcTemplate, recipient, iasUserWrapped, properties);
                msgRsp = IasToDoItemValidator.validateToDoItemIsExistedOrDeletedOrCompleted(
                    cmcTemplate.getClientId(), recipient.getIdpID(), recipient.getRecipientID(), 
                    recipient.getTranID(), recipient.getCorrelatedTranID(), 
                    iasToDoItemHandledCache, existingIasUserToDoItemList);
                if (msgRsp != null) {
                    logger.info("processIasToDoItem - validateToDoItemIsExistedOrDeletedOrCompleted failed"
                        + ". cmcTemplate.getClientId: " + cmcTemplate.getClientId() 
                        + ", cmcTemplate.getTemplateId: " + cmcTemplate.getTemplateId() + ", cmcTemplate.getTemplateVersion: " + cmcTemplate.getTemplateVersion()
                        + ", msgRsp.getIdpID: " + msgRsp.getIdpID() + ", msgRsp.getTranID: " + msgRsp.getTranID() 
                        + ", msgRsp.getTranResultCode: " + msgRsp.getTranResultCode() + ", msgRsp.getTranResultMessage: " + msgRsp.getTranResultMessage());
                    response.addMessageResponse(msgRsp);
                } else {
                    // create the master record in IAS_TO_DO_ITEM
                    if (iasToDoItemId == null || "".equals(iasToDoItemId)) {
                        iasToDoItemId = IasUtils.getNextIasToDoItemId(properties.getProperty(CmcAppPropertyNames.SERVER_ID_PROPERTY_NAME));
                        logger.info("processIasToDoItem - action type: " + recipient.getAction().getType() + ", generated iasToDoItemId: " + iasToDoItemId);

                        // merge the template and create the master record in IAS_TO_DO_ITEM
                        msgRsp = IasToDoItemUtils.mergeTemplateAndCreateIasToDoItem(
                            recipient.getIdpID(), recipient.getTranID(), recipient.getRecipientID(),
                            conn, portalId, iasToDoItemId, 
                            cmcTemplate, cmcMessageParam, titleEn, titleTc, titleSc, detailEn, detailTc, detailSc, properties);
                    }

                    if (msgRsp != null) {
                        logger.info("processIasToDoItem - mergeTemplateAndCreateIasToDoItem failed"
                            + ". cmcTemplate.getClientId: " + cmcTemplate.getClientId() 
                            + ", cmcTemplate.getTemplateId: " + cmcTemplate.getTemplateId() + ", cmcTemplate.getTemplateVersion: " + cmcTemplate.getTemplateVersion()
                            + ", msgRsp.getIdpID: " + msgRsp.getIdpID() + ", msgRsp.getTranID: " + msgRsp.getTranID() 
                            + ", msgRsp.getTranResultCode: " + msgRsp.getTranResultCode() + ", msgRsp.getTranResultMessage: " + msgRsp.getTranResultMessage());
                        response.addMessageResponse(msgRsp);
                    } else {
                        // mark delete of the IAS_USER_TO_DO_ITEM record by recipient.getCorrelatedTranID, delayed the deletion to step 2 after the new record push noti is sent to iAM Smart
                        msgRsp = IasToDoItemUtils.markDeleteIasUserToDoItemByOpenIdOrHKIDAndCorrTranId(conn, iasToDoItemDAO, cmcTemplate, recipient, iasUser);

                        if (msgRsp != null) {
                            logger.info("processIasToDoItem - markDeleteIasUserToDoItemByOpenIdOrHKIDAndCorrTranId failed"
                                + ". cmcTemplate.getClientId: " + cmcTemplate.getClientId() 
                                + ", cmcTemplate.getTemplateId: " + cmcTemplate.getTemplateId() + ", cmcTemplate.getTemplateVersion: " + cmcTemplate.getTemplateVersion()
                                + ", msgRsp.getIdpID: " + msgRsp.getIdpID() + ", msgRsp.getTranID: " + msgRsp.getTranID() 
                                + ", msgRsp.getTranResultCode: " + msgRsp.getTranResultCode() + ", msgRsp.getTranResultMessage: " + msgRsp.getTranResultMessage());
                            response.addMessageResponse(msgRsp);
                        } else {
                            // create the user to-do item record in IAS_USER_TO_DO_ITEM related to the master record
                            msgRsp = IasToDoItemUtils.createIasUserToDoItemByOpenIdOrHKID(conn, iasToDoItemDAO, cmcTemplate, recipient, iasUser, iasToDoItemId, IasApplicationConstant.OPERATION_TYPE_REPLACE);
                            if (msgRsp != null) {
                                logger.info("processIasToDoItem - createIasUserToDoItemByOpenIdOrHKID failed"
                                    + ". cmcTemplate.getClientId: " + cmcTemplate.getClientId() 
                                    + ", cmcTemplate.getTemplateId: " + cmcTemplate.getTemplateId() + ", cmcTemplate.getTemplateVersion: " + cmcTemplate.getTemplateVersion()
                                    + ", msgRsp.getIdpID: " + msgRsp.getIdpID() + ", msgRsp.getTranID: " + msgRsp.getTranID() 
                                    + ", msgRsp.getTranResultCode: " + msgRsp.getTranResultCode() + ", msgRsp.getTranResultMessage: " + msgRsp.getTranResultMessage());
                                response.addMessageResponse(msgRsp);
                            } else {
                                singleMaintainMsgResult.setSuccess(true);
                                // response.addMessageResponse(ResponseUtils.getMessageResponse(recipient.getTranID(), recipient.getIdpID(), 
                                //     recipient.getRecipientID(), ResultCodes.RESULT_CD_TRAN_SUCCESS, ResultMessages.RESULT_MSG_TRAN_SUCCESS));
                            }

                            // add to history cache map for next message checking is it already existed in ACTION=NEW, REPLACE, UPDATE, DELETE
                            iasToDoItemHandledCache.put(recipient.getTranID(), IasToDoItemConstant.HISTORY_REPLACE);
                        }
                    }
                }
            }
        } else if (recipient.getAction().getType() == Action_ST.DELETE_TYPE) {
            logger.info("processIasToDoItem - action type: " + recipient.getAction().getType() + " is running"
                + ". cmcTemplate.getClientId: " + cmcTemplate.getClientId() 
                + ", cmcTemplate.getTemplateId: " + cmcTemplate.getTemplateId() + ", cmcTemplate.getTemplateVersion: " + cmcTemplate.getTemplateVersion()
                + ", recipient.getTranID: " + recipient.getTranID() + ", recipient.getCorrelatedTranID: " + recipient.getCorrelatedTranID() 
                + ", recipient.getIdpID: " + recipient.getIdpID());

            List<IasUserToDoItem_> existingIasUserToDoItemList = IasToDoItemUtils.getCorrelatedIasUserToDoItemListByOpenIdOrHKID(conn, iasToDoItemDAO, cmcTemplate, recipient, iasUserWrapped, properties);
            msgRsp = IasToDoItemValidator.validateToDoItemIsExistedOrDeletedOrCompleted(cmcTemplate.getClientId(), recipient.getIdpID(), 
                recipient.getRecipientID(), recipient.getTranID(), recipient.getCorrelatedTranID(), iasToDoItemHandledCache, existingIasUserToDoItemList);

            if (msgRsp != null) {
                logger.info("processIasToDoItem - validateToDoItemIsExistedOrDeletedOrCompleted failed"
                    + ". cmcTemplate.getClientId: " + cmcTemplate.getClientId() 
                    + ", cmcTemplate.getTemplateId: " + cmcTemplate.getTemplateId() + ", cmcTemplate.getTemplateVersion: " + cmcTemplate.getTemplateVersion()
                    + ", msgRsp.getIdpID: " + msgRsp.getIdpID() + ", msgRsp.getTranID: " + msgRsp.getTranID() 
                    + ", msgRsp.getTranResultCode: " + msgRsp.getTranResultCode() + ", msgRsp.getTranResultMessage: " + msgRsp.getTranResultMessage());
                response.addMessageResponse(msgRsp);
            } else {
                
                // mark delete of the IAS_USER_TO_DO_ITEM record by recipient.getTranId, delayed the deletion to step 2 after the new record push noti is sent to iAM Smart
                msgRsp = IasToDoItemUtils.markDeleteIasUserToDoItemByOpenIdOrHKIDAndCorrTranId(conn, iasToDoItemDAO, cmcTemplate, recipient, iasUser);

                if (msgRsp != null) {
                    logger.info("processIasToDoItem - markDeleteIasUserToDoItemByOpenIdOrHKIDAndTranId failed"
                        + ". cmcTemplate.getClientId: " + cmcTemplate.getClientId() 
                        + ", cmcTemplate.getTemplateId: " + cmcTemplate.getTemplateId() + ", cmcTemplate.getTemplateVersion: " + cmcTemplate.getTemplateVersion()
                        + ", msgRsp.getIdpID: " + msgRsp.getIdpID() + ", msgRsp.getTranID: " + msgRsp.getTranID() 
                        + ", msgRsp.getTranResultCode: " + msgRsp.getTranResultCode() + ", msgRsp.getTranResultMessage: " + msgRsp.getTranResultMessage());
                    response.addMessageResponse(msgRsp);
                } else {
                    singleMaintainMsgResult.setSuccess(true);
                    // response.addMessageResponse(ResponseUtils.getMessageResponse(
                    //     recipient.getTranID(), recipient.getIdpID(), recipient.getRecipientID(), ResultCodes.RESULT_CD_TRAN_SUCCESS, ResultMessages.RESULT_MSG_TRAN_SUCCESS));
                }

                // add to history cache map for next message checking is it already existed in ACTION=REPLACE, UPDATE, DELETE
                iasToDoItemHandledCache.put(recipient.getCorrelatedTranID(), IasToDoItemConstant.HISTORY_DELETE);
            }

        } else if (recipient.getAction().getType() == Action_ST.MARKCOMPLETE_TYPE) {
            logger.info("processIasToDoItem - action type: " + recipient.getAction().getType() + " is running"
                + ". cmcTemplate.getClientId: " + cmcTemplate.getClientId() 
                + ", cmcTemplate.getTemplateId: " + cmcTemplate.getTemplateId() + ", cmcTemplate.getTemplateVersion: " + cmcTemplate.getTemplateVersion()
                + ", recipient.getTranID: " + recipient.getTranID() + ", recipient.getCorrelatedTranID: " + recipient.getCorrelatedTranID() 
                + ", recipient.getIdpID: " + recipient.getIdpID());

            List<IasUserToDoItem_> existingIasUserToDoItemList = IasToDoItemUtils.getCorrelatedIasUserToDoItemListByOpenIdOrHKID(conn, iasToDoItemDAO, cmcTemplate, recipient, iasUserWrapped, properties);
            msgRsp = IasToDoItemValidator.validateToDoItemIsExistedOrDeletedOrCompleted(cmcTemplate.getClientId(), recipient.getIdpID(), 
                recipient.getRecipientID(), recipient.getTranID(), recipient.getCorrelatedTranID(), iasToDoItemHandledCache, existingIasUserToDoItemList);

            if (msgRsp != null) {
                logger.info("processIasToDoItem - validateToDoItemIsExistedOrDeletedOrCompleted failed"
                    + ". cmcTemplate.getClientId: " + cmcTemplate.getClientId() 
                    + ", cmcTemplate.getTemplateId: " + cmcTemplate.getTemplateId() + ", cmcTemplate.getTemplateVersion: " + cmcTemplate.getTemplateVersion()
                    + ", msgRsp.getIdpID: " + msgRsp.getIdpID() + ", msgRsp.getTranID: " + msgRsp.getTranID() 
                    + ", msgRsp.getTranResultCode: " + msgRsp.getTranResultCode() + ", msgRsp.getTranResultMessage: " + msgRsp.getTranResultMessage());
                response.addMessageResponse(msgRsp);
            } else {

                // update the IAS_USER_TO_DO_ITEM record by recipient.getTranId
                msgRsp = IasToDoItemUtils.completeIasUserToDoItemByOpenIdOrHKID(conn, iasToDoItemDAO, cmcTemplate, recipient, iasUser);
                if (msgRsp != null) {
                    logger.info("processIasToDoItem - completeIasUserToDoItemByOpenIdOrHKID failed"
                        + ". cmcTemplate.getClientId: " + cmcTemplate.getClientId() 
                        + ", cmcTemplate.getTemplateId: " + cmcTemplate.getTemplateId() + ", cmcTemplate.getTemplateVersion: " + cmcTemplate.getTemplateVersion()
                        + ", msgRsp.getIdpID: " + msgRsp.getIdpID() + ", msgRsp.getTranID: " + msgRsp.getTranID() 
                        + ", msgRsp.getTranResultCode: " + msgRsp.getTranResultCode() + ", msgRsp.getTranResultMessage: " + msgRsp.getTranResultMessage());
                    response.addMessageResponse(msgRsp);
                } else {
                    singleMaintainMsgResult.setSuccess(true);
                    // response.addMessageResponse(ResponseUtils.getMessageResponse(
                    //     recipient.getCorrelatedTranID(), recipient.getIdpID(), recipient.getRecipientID(), ResultCodes.RESULT_CD_TRAN_SUCCESS, ResultMessages.RESULT_MSG_TRAN_SUCCESS));
                }

            }

        }

        logger.info("processIasToDoItem - END");
        return singleMaintainMsgResult;
        // return iasToDoItemId;
    }

}
