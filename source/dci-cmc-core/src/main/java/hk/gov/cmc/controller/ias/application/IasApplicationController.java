package hk.gov.ogcio.mars_cmc.cmc.service.controller;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import hk.gov.ogcio.egis.rm.common.utils.ServiceLocator;
import hk.gov.ogcio.mars_cmc.cmc.appserver.AppPropertyName;
import hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.Application;
import hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MaintainMessageResponse;
import hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.MessageResponse;
import hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.Recipient;
import hk.gov.ogcio.mars_cmc.cmc.castor.maintainmessage.types.Action_ST;
import hk.gov.ogcio.mars_cmc.cmc.datatype.MessageParam;
import hk.gov.ogcio.mars_cmc.cmc.datatype.message.IasUser;
import hk.gov.ogcio.mars_cmc.cmc.datatype.message.IasUserWrapped;
import hk.gov.ogcio.mars_cmc.cmc.service.constant.IasApplicationConstant;
import hk.gov.ogcio.mars_cmc.cmc.service.dao.iasapplication.IasApplicationDAO;
import hk.gov.ogcio.mars_cmc.cmc.service.databean.CmcTemplate;
import hk.gov.ogcio.mars_cmc.cmc.service.databean.IasApplication_;
import hk.gov.ogcio.mars_cmc.cmc.service.databean.IasUserApplication_;
import hk.gov.ogcio.mars_cmc.cmc.service.dto.SingleMaintainMsgResult;
import hk.gov.ogcio.mars_cmc.cmc.service.utils.IasApplicationUtils;
import hk.gov.ogcio.mars_cmc.cmc.service.validator.IasApplicationValidator;
import hk.gov.ogcio.mars_cmc.cmc.utils.IasUtils;
import hk.gov.ogcio.mars_cmc.framework.common.sql.HPFW_Connection;

public class IasApplicationController {

    private static Log logger = LogFactory.getLog(IasApplicationController.class);

    public IasApplicationController() {
    }

    public SingleMaintainMsgResult processIasApplication(
            String portalId, String iasApplicationId,
            Application application, Recipient recipient, IasUserWrapped iasUserWrapped,
            String titleEn, String titleTc, String titleSc,
            String detailEn, String detailTc, String detailSc,
            CmcTemplate cmcTemplate, List<MessageParam> cmcMessageParam, HPFW_Connection conn,
            Map<String, String> iasApplicationHandledCache, MaintainMessageResponse response) throws Exception {
        logger.info("processIasApplication - Start");
        logger.info("processIasApplication - portalId: " + portalId + ", iasApplicationId: " + iasApplicationId 
            + ", recipient.getAction.getType: " + recipient.getAction().getType() + ", recipient.getIdpID: " + recipient.getIdpID() 
            + ", recipient.getRecipientIDType: " + recipient.getRecipientIDType() + ", recipient.getAppRefNum: " + recipient.getAppRefNum());

        logger.debug("processIasApplication - portalId: " + portalId + ", iasApplicationId: " + iasApplicationId
            + ", application: " + application.toString() + ", recipient: " + recipient.toString() + ", iasUserWrapped.toString(): " + iasUserWrapped.toString()
            + ", cmcTemplate: " + cmcTemplate.toString() + ", cmcMessageParam size: " + cmcMessageParam.size());
        logger.debug("processIasApplication - titleEn: " + titleEn + ", titleTc: " + titleTc + ", titleSc: " + titleSc);
        logger.debug("processIasApplication - detailEn: " + detailEn + ", detailTc: " + detailTc + ", detailSc: " + detailSc);

        IasApplicationDAO iasApplicationDAO = new IasApplicationDAO();
        Properties properties = ServiceLocator.getInstance(null).getProperties();
        MessageResponse msgRsp = null;
        SingleMaintainMsgResult singleMaintainMsgResult = new SingleMaintainMsgResult(iasApplicationId, false);

        IasUser iasUser = iasUserWrapped.getIasUser();

        msgRsp = IasApplicationValidator.validateAppStatus(cmcTemplate.getClientId(), 
            recipient.getIdpID(), recipient.getRecipientID(), recipient.getTranID(), recipient.getAppStatus());

        if (msgRsp != null) {
            logger.info("processIasApplication - validateAppStatus failed" 
                + ". cmcTemplate.getClientId: " + cmcTemplate.getClientId() 
                + ", cmcTemplate.getTemplateId: " + cmcTemplate.getTemplateId() + ", cmcTemplate.getTemplateVersion: " + cmcTemplate.getTemplateVersion()
                + ", msgRsp.getIdpID: " + msgRsp.getIdpID() + ", msgRsp.getAppRefNum: " + msgRsp.getAppRefNum() 
                + ", msgRsp.getTranResultCode: " + msgRsp.getTranResultCode() + ", msgRsp.getTranResultMessage: " + msgRsp.getTranResultMessage());
            response.addMessageResponse(msgRsp);
        } else {
            if (recipient.getAction().getType() == Action_ST.NEW_TYPE) {
                logger.info("processIasApplication - action type: " + recipient.getAction().getType() + " is running"
                    + ". cmcTemplate.getClientId: " + cmcTemplate.getClientId() 
                    + ", cmcTemplate.getTemplateId: " + cmcTemplate.getTemplateId() + ", cmcTemplate.getTemplateVersion: " + cmcTemplate.getTemplateVersion()
                    + ", recipient.getTranID: " + recipient.getTranID() + ", recipient.getIdpID: " + recipient.getIdpID());

                // create the master record in IAS_APPLICATION
                if (iasApplicationId == null || "".equals(iasApplicationId)) {
                    iasApplicationId = IasUtils.getNextIasApplicationId(properties.getProperty(CmcAppPropertyNames.SERVER_ID_PROPERTY_NAME));
                    logger.info("processIasApplication - action type: " + recipient.getAction().getType() + ", generated iasApplicationId: " + iasApplicationId);

                    // merge the template and create the master record in IAS_APPLICATION
                    msgRsp = IasApplicationUtils.mergeTemplateAndCreateIasApplication(
                        recipient.getIdpID(), recipient.getTranID(), recipient.getRecipientID(),
                        conn, portalId, iasApplicationId, cmcTemplate, cmcMessageParam, 
                        titleEn, titleTc, titleSc, detailEn, detailTc, detailSc, 
                        properties, response);
                }

                if (msgRsp != null) {
                    logger.info("processIasApplication - mergeTemplateAndCreateIasApplication failed" 
                        + ". cmcTemplate.getClientId: " + cmcTemplate.getClientId() 
                        + ", cmcTemplate.getTemplateId: " + cmcTemplate.getTemplateId() + ", cmcTemplate.getTemplateVersion: " + cmcTemplate.getTemplateVersion()
                        + ", msgRsp.getIdpID: " + msgRsp.getIdpID() + ", msgRsp.getAppRefNum: " + msgRsp.getAppRefNum()
                        + ", msgRsp.getTranResultCode: " + msgRsp.getTranResultCode() + ", msgRsp.getTranResultMessage: " + msgRsp.getTranResultMessage());
                    response.addMessageResponse(msgRsp);
                } else {
                    // create the user application record in IAS_USER_APPLICATION related to the master record
                    msgRsp = IasApplicationUtils.createIasUserApplicationByOpenIdOrHKID(conn, iasApplicationDAO, cmcTemplate, recipient, iasUser, 
                        iasApplicationId, IasApplicationConstant.OPERATION_TYPE_CREATE, response);
                    if (msgRsp != null) {
                        response.addMessageResponse(msgRsp);
                    } else {
                        singleMaintainMsgResult.setSuccess(true);
                        // response.addMessageResponse(ResponseUtils.getMessageResponse(recipient.getAppRefNum(), 
                        //     recipient.getIdpID(), recipient.getRecipientID(), ResultCodes.RESULT_CD_TRAN_SUCCESS, ResultMessages.RESULT_MSG_TRAN_SUCCESS));
                    }

                    // CMC-2025-022: Use TRAN_ID to correlate iAM Smart Application Status instead of APP_REF_NUM - BEGIN
                    // add to history cache map for next message checking is it already existed in ACTION=NEW, REPLACE, UPDATE, DELETE
                    // iasApplicationHandledCache.put(recipient.getAppRefNum(), IasApplicationConstant.HISTORY_NEW);
                    iasApplicationHandledCache.put(recipient.getTranID(), IasApplicationConstant.HISTORY_NEW);
                    // CMC-2025-022: Use TRAN_ID to correlate iAM Smart Application Status instead of APP_REF_NUM - END
                }

            } else if (recipient.getAction().getType() == Action_ST.REPLACE_TYPE) {
                logger.info("processIasApplication - action type: " + recipient.getAction().getType() + " is running"
                    + ". cmcTemplate.getClientId: " + cmcTemplate.getClientId() 
                    + ", cmcTemplate.getTemplateId: " + cmcTemplate.getTemplateId() + ", cmcTemplate.getTemplateVersion: " + cmcTemplate.getTemplateVersion()
                    + ", recipient.getTranID: " + recipient.getTranID() + ", recipient.getCorrelatedTranID: " + recipient.getCorrelatedTranID()
                    + ", recipient.getIdpID: " + recipient.getIdpID());

                // CMC-2025-022: Use TRAN_ID to correlate iAM Smart Application Status instead of APP_REF_NUM - BEGIN
                // List<IasUserApplication_> existingIasUserApplicationList = IasApplicationUtils.getIasUserApplicationListByOpenIdOrHKID(
                //     conn, iasApplicationDAO, cmcTemplate, recipient, iasUserWrapped, properties);
                // msgRsp = IasApplicationValidator.validateApplicationIsExistedOrDeleted(
                //     cmcTemplate.getClientId(), recipient.getIdpID(), recipient.getRecipientID(), recipient.getTranID(), recipient.getAppRefNum(), 
                //     iasApplicationHandledCache, existingIasUserApplicationList);
                List<IasUserApplication_> existingIasUserApplicationList = IasApplicationUtils.getCorrelatedIasUserApplicationListByOpenIdOrHKID(
                    conn, iasApplicationDAO, cmcTemplate, recipient, iasUserWrapped, properties);
                msgRsp = IasApplicationValidator.validateApplicationIsExistedOrDeleted(
                    cmcTemplate.getClientId(), recipient.getIdpID(), recipient.getRecipientID(), 
                    recipient.getTranID(), recipient.getCorrelatedTranID(), 
                    iasApplicationHandledCache, existingIasUserApplicationList);
                // CMC-2025-022: Use TRAN_ID to correlate iAM Smart Application Status instead of APP_REF_NUM - END

                if (msgRsp != null) {
                    logger.info("processIasApplication - validateApplicationIsExistedOrDeleted failed" 
                        + ". cmcTemplate.getClientId: " + cmcTemplate.getClientId() 
                        + ", cmcTemplate.getTemplateId: " + cmcTemplate.getTemplateId() + ", cmcTemplate.getTemplateVersion: " + cmcTemplate.getTemplateVersion()
                        + ", msgRsp.getIdpID: " + msgRsp.getIdpID() + ", msgRsp.getAppRefNum: " + msgRsp.getAppRefNum()
                        + ", msgRsp.getTranResultCode: " + msgRsp.getTranResultCode() + ", msgRsp.getTranResultMessage: " + msgRsp.getTranResultMessage());
                    response.addMessageResponse(msgRsp);
                } else {
                    
                    msgRsp = IasApplicationValidator.validateAppStatusUpdateDate(cmcTemplate.getClientId(), recipient.getIdpID(), 
                        recipient.getRecipientID(), recipient.getTranID(), recipient.getAppStatusUpdateDate(), existingIasUserApplicationList);

                    if (msgRsp != null) {
                        logger.info("processIasApplication - validateAppStatusUpdateDate failed" 
                            + ". cmcTemplate.getClientId: " + cmcTemplate.getClientId() 
                            + ", cmcTemplate.getTemplateId: " + cmcTemplate.getTemplateId() + ", cmcTemplate.getTemplateVersion: " + cmcTemplate.getTemplateVersion()
                            + ", msgRsp.getIdpID: " + msgRsp.getIdpID() + ", msgRsp.getAppRefNum: " + msgRsp.getAppRefNum() 
                            + ", msgRsp.getTranResultCode: " + msgRsp.getTranResultCode() + ", msgRsp.getTranResultMessage: " + msgRsp.getTranResultMessage());
                        response.addMessageResponse(msgRsp);
                    } else {
                        if (iasApplicationId == null || "".equals(iasApplicationId)) {
                            iasApplicationId = IasUtils.getNextIasApplicationId(properties.getProperty(CmcAppPropertyNames.SERVER_ID_PROPERTY_NAME));
                            logger.info("processIasApplication - action type: " + recipient.getAction().getType() + ", generated iasApplicationId: " + iasApplicationId);

                            // merge the template and create the master record in IAS_APPLICATION
                            msgRsp = IasApplicationUtils.mergeTemplateAndCreateIasApplication(
                                recipient.getIdpID(), recipient.getTranID(), recipient.getRecipientID(),
                                conn, portalId, iasApplicationId, 
                                cmcTemplate, cmcMessageParam, titleEn, titleTc, titleSc, detailEn, detailTc, detailSc, properties, response);
                        }

                        if (msgRsp != null) {
                            logger.info("processIasApplication - mergeTemplateAndCreateIasApplication failed" 
                                + ". cmcTemplate.getClientId: " + cmcTemplate.getClientId() 
                                + ", cmcTemplate.getTemplateId: " + cmcTemplate.getTemplateId() + ", cmcTemplate.getTemplateVersion: " + cmcTemplate.getTemplateVersion()
                                + ", msgRsp.getIdpID: " + msgRsp.getIdpID() + ", msgRsp.getAppRefNum: " + msgRsp.getAppRefNum() 
                                + ", msgRsp.getTranResultCode: " + msgRsp.getTranResultCode() + ", msgRsp.getTranResultMessage: " + msgRsp.getTranResultMessage());
                            response.addMessageResponse(msgRsp);
                        } else {

                            // CMC-2025-022: Use TRAN_ID to correlate iAM Smart Application Status instead of APP_REF_NUM - BEGIN
                            // mark delete of the previous record, delayed the deletion to step 2 after the new record push noti is sent to iAM Smart
                            // IasUserApplication_ toBeDeleteIasUserApplication_ = existingIasUserApplicationList.get(0);
                            // iasApplicationDAO.markDeleteIasUserApplication(conn, toBeDeleteIasUserApplication_.getClientId(), toBeDeleteIasUserApplication_.getRecipientId(), toBeDeleteIasUserApplication_.getIasApplicationId());
                            msgRsp = IasApplicationUtils.markDeleteIasUserApplicationByOpenIdOrHKIDAndCorrTranId(conn, iasApplicationDAO, cmcTemplate, recipient, iasUser);
                            if (msgRsp != null) {
                                logger.info("processIasApplication - markDeleteIasUserApplicationByOpenIdOrHKIDAndCorrTranId failed"
                                        + ". cmcTemplate.getClientId: " + cmcTemplate.getClientId() 
                                        + ", cmcTemplate.getTemplateId: " + cmcTemplate.getTemplateId() + ", cmcTemplate.getTemplateVersion: " + cmcTemplate.getTemplateVersion()
                                        + ", msgRsp.getIdpID: " + msgRsp.getIdpID() + ", msgRsp.getTranID: " + msgRsp.getTranID() 
                                        + ", msgRsp.getTranResultCode: " + msgRsp.getTranResultCode() + ", msgRsp.getTranResultMessage: " + msgRsp.getTranResultMessage());
                                    response.addMessageResponse(msgRsp);
                            } else {
                                // CMC-2025-022: Use TRAN_ID to correlate iAM Smart Application Status instead of APP_REF_NUM - END
                                // create the user application record in IAS_USER_APPLICATION related to the master record
                                msgRsp = IasApplicationUtils.createIasUserApplicationByOpenIdOrHKID(conn, iasApplicationDAO, cmcTemplate, 
                                    recipient, iasUser, iasApplicationId, IasApplicationConstant.OPERATION_TYPE_REPLACE, response);
                                if (msgRsp != null) {
                                    response.addMessageResponse(msgRsp);
                                } else {
                                    singleMaintainMsgResult.setSuccess(true);
                                    // response.addMessageResponse(ResponseUtils.getMessageResponse(recipient.getAppRefNum(), 
                                    //     recipient.getIdpID(), recipient.getRecipientID(), ResultCodes.RESULT_CD_TRAN_SUCCESS, ResultMessages.RESULT_MSG_TRAN_SUCCESS));
                                }

                                // add to history cache map for next message checking is it already existed in ACTION=NEW, REPLACE, UPDATE, DELETE
                                iasApplicationHandledCache.put(recipient.getAppRefNum(), IasApplicationConstant.HISTORY_REPLACE);
                                // CMC-2025-022: Use TRAN_ID to correlate iAM Smart Application Status instead of APP_REF_NUM - BEGIN
                            }
                            // CMC-2025-022: Use TRAN_ID to correlate iAM Smart Application Status instead of APP_REF_NUM - END
                        }
                    }
                }

            } else if (recipient.getAction().getType() == Action_ST.DELETE_TYPE) {
                logger.info("processIasApplication - action type: " + recipient.getAction().getType() + " is running"
                    + ". cmcTemplate.getClientId: " + cmcTemplate.getClientId() 
                    + ", cmcTemplate.getTemplateId: " + cmcTemplate.getTemplateId() + ", cmcTemplate.getTemplateVersion: " + cmcTemplate.getTemplateVersion()
                    + ", recipient.getTranID: " + recipient.getTranID() + ", recipient.getCorrelatedTranID: " + recipient.getCorrelatedTranID() 
                    + ", recipient.getIdpID: " + recipient.getIdpID());

                // CMC-2025-022: Use TRAN_ID to correlate iAM Smart Application Status instead of APP_REF_NUM - BEGIN
                // List<IasUserApplication_> existingIasUserApplicationList = IasApplicationUtils.getIasUserApplicationListByOpenIdOrHKID(
                //     conn, iasApplicationDAO, cmcTemplate, recipient, iasUserWrapped, properties);
                // msgRsp = IasApplicationValidator.validateApplicationIsExistedOrDeleted(
                //     cmcTemplate.getClientId(), recipient.getIdpID(), recipient.getRecipientID(), recipient.getTranID(), recipient.getAppRefNum(), 
                //     iasApplicationHandledCache, existingIasUserApplicationList);
                List<IasUserApplication_> existingIasUserApplicationList = IasApplicationUtils.getCorrelatedIasUserApplicationListByOpenIdOrHKID(
                    conn, iasApplicationDAO, cmcTemplate, recipient, iasUserWrapped, properties);
                msgRsp = IasApplicationValidator.validateApplicationIsExistedOrDeleted(
                    cmcTemplate.getClientId(), recipient.getIdpID(), recipient.getRecipientID(), 
                    recipient.getTranID(), recipient.getCorrelatedTranID(), 
                    iasApplicationHandledCache, existingIasUserApplicationList);
                // CMC-2025-022: Use TRAN_ID to correlate iAM Smart Application Status instead of APP_REF_NUM - END

                if (msgRsp != null) {
                    logger.info("processIasApplication - validateApplicationIsExistedOrDeleted failed" 
                        + ". cmcTemplate.getClientId: " + cmcTemplate.getClientId() 
                        + ", cmcTemplate.getTemplateId: " + cmcTemplate.getTemplateId() + ", cmcTemplate.getTemplateVersion: " + cmcTemplate.getTemplateVersion()
                        + ", msgRsp.getIdpID: " + msgRsp.getIdpID() + ", msgRsp.getAppRefNum: " + msgRsp.getAppRefNum() 
                        + ", msgRsp.getTranResultCode: " + msgRsp.getTranResultCode() + ", msgRsp.getTranResultMessage: " + msgRsp.getTranResultMessage());
                    response.addMessageResponse(msgRsp);
                } else {

                    // CMC-2025-022: Use TRAN_ID to correlate iAM Smart Application Status instead of APP_REF_NUM - BEGIN
                    // mark delete of the previous record, delayed the deletion to step 2 after the new record push noti is sent to iAM Smart
                    // IasUserApplication_ toBeDeleteIasUserApplication_ = existingIasUserApplicationList.get(0);
                    // iasApplicationDAO.markDeleteIasUserApplication(conn, toBeDeleteIasUserApplication_.getClientId(), toBeDeleteIasUserApplication_.getRecipientId(), toBeDeleteIasUserApplication_.getIasApplicationId());
                    // add to history cache map for next message checking is it already existed in ACTION=NEW, REPLACE, UPDATE, DELETE
                    // iasApplicationHandledCache.put(recipient.getAppRefNum(), IasApplicationConstant.HISTORY_DELETE);

                    msgRsp = IasApplicationUtils.markDeleteIasUserApplicationByOpenIdOrHKIDAndCorrTranId(conn, iasApplicationDAO, cmcTemplate, recipient, iasUser);
                    if (msgRsp != null) {
                        logger.info("processIasApplication - markDeleteIasUserApplicationByOpenIdOrHKIDAndCorrTranId failed"
                            + ". cmcTemplate.getClientId: " + cmcTemplate.getClientId() 
                            + ", cmcTemplate.getTemplateId: " + cmcTemplate.getTemplateId() + ", cmcTemplate.getTemplateVersion: " + cmcTemplate.getTemplateVersion()
                            + ", msgRsp.getIdpID: " + msgRsp.getIdpID() + ", msgRsp.getTranID: " + msgRsp.getTranID() 
                            + ", msgRsp.getTranResultCode: " + msgRsp.getTranResultCode() + ", msgRsp.getTranResultMessage: " + msgRsp.getTranResultMessage());
                        response.addMessageResponse(msgRsp);
                    } else {
                        singleMaintainMsgResult.setSuccess(true);
                    }

                    iasApplicationHandledCache.put(recipient.getCorrelatedTranID(), IasApplicationConstant.HISTORY_DELETE);
                    // CMC-2025-022: Use TRAN_ID to correlate iAM Smart Application Status instead of APP_REF_NUM - END
                }

            } else if (recipient.getAction().getType() == Action_ST.UPDATE_TYPE) {
                logger.info("processIasApplication - action type: " + recipient.getAction().getType() + " is running"
                    + ". cmcTemplate.getClientId: " + cmcTemplate.getClientId() 
                    + ", cmcTemplate.getTemplateId: " + cmcTemplate.getTemplateId() + ", cmcTemplate.getTemplateVersion: " + cmcTemplate.getTemplateVersion()
                    + ", recipient.getTranID: " + recipient.getTranID() + ", recipient.getCorrelatedTranID: " + recipient.getCorrelatedTranID() 
                    + ", recipient.getIdpID: " + recipient.getIdpID());

                // CMC-2025-022: Use TRAN_ID to correlate iAM Smart Application Status instead of APP_REF_NUM - BEGIN
                // List<IasUserApplication_> existingIasUserApplicationList = IasApplicationUtils.getIasUserApplicationListByOpenIdOrHKID(
                //     conn, iasApplicationDAO, cmcTemplate, recipient, iasUserWrapped, properties);
                // msgRsp = IasApplicationValidator.validateApplicationIsExistedOrDeleted(
                //     cmcTemplate.getClientId(), recipient.getIdpID(), recipient.getRecipientID(), recipient.getTranID(), recipient.getAppRefNum(), 
                //     iasApplicationHandledCache, existingIasUserApplicationList);
                List<IasUserApplication_> existingIasUserApplicationList = IasApplicationUtils.getCorrelatedIasUserApplicationListByOpenIdOrHKID(
                    conn, iasApplicationDAO, cmcTemplate, recipient, iasUserWrapped, properties);
                msgRsp = IasApplicationValidator.validateApplicationIsExistedOrDeleted(
                    cmcTemplate.getClientId(), recipient.getIdpID(), recipient.getRecipientID(), 
                    recipient.getTranID(), recipient.getCorrelatedTranID(), 
                    iasApplicationHandledCache, existingIasUserApplicationList);
                // CMC-2025-022: Use TRAN_ID to correlate iAM Smart Application Status instead of APP_REF_NUM - END

                if (msgRsp != null) {
                    logger.info("processIasApplication - validateApplicationIsExistedOrDeleted failed" 
                        + ". cmcTemplate.getClientId: " + cmcTemplate.getClientId() 
                        + ", cmcTemplate.getTemplateId: " + cmcTemplate.getTemplateId() + ", cmcTemplate.getTemplateVersion: " + cmcTemplate.getTemplateVersion()
                        + ", msgRsp.getIdpID: " + msgRsp.getIdpID() + ", msgRsp.getAppRefNum: " + msgRsp.getAppRefNum() 
                        + ", msgRsp.getTranResultCode: " + msgRsp.getTranResultCode() + ", msgRsp.getTranResultMessage: " + msgRsp.getTranResultMessage());
                    response.addMessageResponse(msgRsp);
                } else {

                    msgRsp = IasApplicationValidator.validateAppStatusUpdateDate(cmcTemplate.getClientId(), recipient.getIdpID(), 
                        recipient.getRecipientID(), recipient.getTranID(), recipient.getAppStatusUpdateDate(), existingIasUserApplicationList);
                    if (msgRsp != null) {
                        logger.info("processIasApplication - validateAppStatusUpdateDate failed" 
                            + ". cmcTemplate.getClientId: " + cmcTemplate.getClientId() 
                            + ", cmcTemplate.getTemplateId: " + cmcTemplate.getTemplateId() + ", cmcTemplate.getTemplateVersion: " + cmcTemplate.getTemplateVersion()
                            + ", msgRsp.getIdpID: " + msgRsp.getIdpID() + ", msgRsp.getAppRefNum: " + msgRsp.getAppRefNum() 
                            + ", msgRsp.getTranResultCode: " + msgRsp.getTranResultCode() + ", msgRsp.getTranResultMessage: " + msgRsp.getTranResultMessage());
                        response.addMessageResponse(msgRsp);
                    } else {
                        // CMC-2025-022: Use TRAN_ID to correlate iAM Smart Application Status instead of APP_REF_NUM - BEGIN
                        // mark delete of the previous record, delayed the deletion to step 2 after the new record push noti is sent to iAM Smart
                        // IasUserApplication_ toBeDeleteIasUserApplication_ = existingIasUserApplicationList.get(0);
                        // iasApplicationDAO.markDeleteIasUserApplication(conn, toBeDeleteIasUserApplication_.getClientId(), toBeDeleteIasUserApplication_.getRecipientId(), toBeDeleteIasUserApplication_.getIasApplicationId());
                        msgRsp = IasApplicationUtils.markDeleteIasUserApplicationByOpenIdOrHKIDAndCorrTranId(conn, iasApplicationDAO, cmcTemplate, recipient, iasUser);
                        iasApplicationHandledCache.put(recipient.getTranID(), IasApplicationConstant.HISTORY_DELETE);
                        // CMC-2025-022: Use TRAN_ID to correlate iAM Smart Application Status instead of APP_REF_NUM - END

                        // Duplicate the previous master record to the new master record
                        // CMC-2025-022: Use TRAN_ID to correlate iAM Smart Application Status instead of APP_REF_NUM - BEGIN
                        IasUserApplication_ toBeDeleteIasUserApplication_ = existingIasUserApplicationList.get(0);
                        // CMC-2025-022: Use TRAN_ID to correlate iAM Smart Application Status instead of APP_REF_NUM - END
                        IasApplication_ iasApplication_ = new IasApplication_(conn, toBeDeleteIasUserApplication_.getIasApplicationId());
                        String newIasApplicationId = IasUtils.getNextIasApplicationId(properties.getProperty(CmcAppPropertyNames.SERVER_ID_PROPERTY_NAME));
                        logger.info("processIasApplication - action type: " + recipient.getAction().getType() 
                            + ", original iasApplicationId; " + iasApplication_.getIasApplicationId() + ", generated newIasApplicationId: " + newIasApplicationId);
                        iasApplication_.setIasApplicationId(newIasApplicationId);
                        conn.begin(null, conn.getLastUpTime(), HPFW_Connection.DIRECT);
                        iasApplication_.insert(conn);
                        conn.begin(null, conn.getLastUpTime(), HPFW_Connection.DIRECT_WITH_HISTORY);

                        // create the user application record in IAS_USER_APPLICATION related to the master record
                        msgRsp = IasApplicationUtils.createIasUserApplicationByOpenIdOrHKID(conn, iasApplicationDAO, cmcTemplate, 
                            recipient, iasUser, newIasApplicationId, IasApplicationConstant.OPERATION_TYPE_UPDATE, response);
                        if (msgRsp != null) {
                            response.addMessageResponse(msgRsp);
                        } else {
                            singleMaintainMsgResult.setSuccess(true);
                            // response.addMessageResponse(ResponseUtils.getMessageResponse(recipient.getAppRefNum(), 
                            //     recipient.getIdpID(), recipient.getRecipientID(), ResultCodes.RESULT_CD_TRAN_SUCCESS, ResultMessages.RESULT_MSG_TRAN_SUCCESS));
                        }

                        // CMC-2025-022: Use TRAN_ID to correlate iAM Smart Application Status instead of APP_REF_NUM - BEGIN
                        // add to history cache map for next message checking is it already existed in ACTION=NEW, REPLACE, UPDATE, DELETE
                        // iasApplicationHandledCache.put(recipient.getAppRefNum(), IasApplicationConstant.HISTORY_UPDATE);
                        iasApplicationHandledCache.put(recipient.getTranID(), IasApplicationConstant.HISTORY_UPDATE);
                        // CMC-2025-022: Use TRAN_ID to correlate iAM Smart Application Status instead of APP_REF_NUM - END
                    }
                }
            }
        }

        logger.info("processIasApplication - END");
        return singleMaintainMsgResult;
        // return iasApplicationId;
    }

}
