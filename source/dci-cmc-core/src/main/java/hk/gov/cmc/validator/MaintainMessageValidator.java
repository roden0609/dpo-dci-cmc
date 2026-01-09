package hk.gov.cmc.validator;

import java.util.HashSet;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import hk.gov.cmc.common.CmcAppConstants;
import hk.gov.cmc.common.IntegrationConstants;
import hk.gov.cmc.common.ResultCodes;
import hk.gov.cmc.common.ResultMessages;
import hk.gov.cmc.common.StatusConstants;
import hk.gov.cmc.config.CmcSystemParam;
import hk.gov.cmc.dao.maintainmessage.CmcPortalServerDAO;
import hk.gov.cmc.dao.maintainmessage.template.CmcTemplateDAO;
import hk.gov.cmc.model.maintainmessage.application.Application;
import hk.gov.cmc.model.maintainmessage.emessage.EMessage;
import hk.gov.cmc.model.maintainmessage.request.MaintainMessageRequest;
import hk.gov.cmc.model.maintainmessage.request.MessageRequest;
import hk.gov.cmc.model.maintainmessage.request.MetaData;
import hk.gov.cmc.model.maintainmessage.request.Recipient;
import hk.gov.cmc.model.maintainmessage.response.MaintainMessageResponse;
import hk.gov.cmc.model.maintainmessage.template.CmcTemplate;
import hk.gov.cmc.model.maintainmessage.todoitem.ToDoItem;
import hk.gov.cmc.persistence.connection.hpfw.HPFW_Connection;
import hk.gov.cmc.utils.maintainmessage.MaintainMessageUtils;

public class MaintainMessageValidator {

    private static final Log logger = LogFactory.getLog(MaintainMessageValidator.class);

    public static MaintainMessageResponse checkHasProcessDataRequest(HPFW_Connection conn,
            MaintainMessageRequest maintMsgReq)
            throws Exception {
        List<MessageRequest> msgReqList = maintMsgReq.getMessageRequests();
        if (msgReqList != null) {
            logger.info("checkHasProcessDataRequest - msgReqArray.size: " + msgReqList.size());
        }

        for (int i = 0; i < msgReqList.size(); i++) {

            MessageRequest msgReq = msgReqList.get(i);
            if (msgReq != null) {
                logger.info("checkHasProcessDataRequest - msgReq.toString(): " + msgReq.toString());
            }

            EMessage eMsg = msgReq.getEMessage();
            ToDoItem toDoItem = msgReq.getToDoItem();
            Application application = msgReq.getApplication();

            if (eMsg == null && toDoItem == null && application == null) {
                return MaintainMessageUtils.getMaintainMessageResponse(ResultCodes.RESULT_CD_PROCESS_DATA_NOT_FOUND,
                        ResultMessages.RESULT_MSG_PROCESS_DATA_NOT_FOUND);
            }

        }

        return null;
    }

    public static MaintainMessageResponse checkAllMessagesRequest(HPFW_Connection conn, String appId,
            MaintainMessageRequest maintMsgReq) throws Exception {
        List<MessageRequest> msgReqList = maintMsgReq.getMessageRequests();

        String trustSpAppId = CmcSystemParam.getPara("TRUST_SP_APP_ID");

        CmcPortalServerDAO cmcPortalServerDAO = new CmcPortalServerDAO();
        CmcTemplateDAO cmcTemplateDAO = new CmcTemplateDAO();

        for (MessageRequest msgReq : msgReqList) {
            String portalStatus = cmcPortalServerDAO.getPortalStatusByPortalId(conn, msgReq.getPortalId());
            if (portalStatus == null || !StatusConstants.PORTAL_STATUS_ACTIVE.equals(portalStatus)) {
                return MaintainMessageUtils.getMaintainMessageResponse(ResultCodes.RESULT_CD_PORTAL_ID_NOT_FOUND,
                        ResultMessages.RESULT_MSG_PORTAL_ID_NOT_FOUND);
            }

            EMessage eMsg = msgReq.getEMessage();
            ToDoItem toDoItem = msgReq.getToDoItem();
            Application application = msgReq.getApplication();

            String eMsgServiceProviderId = null;
            String toDoItemServiceProviderId = null;
            String applicationServiceProviderId = null;

            if (eMsg != null) {
                CmcTemplate cmcTemplate = cmcTemplateDAO.getTemplateByIdVersion(conn, eMsg.getTemplateId(),
                        eMsg.getTemplateVersion(), CmcAppConstants.EMSG_TEMPLATE_TYPE);

                if (cmcTemplate == null) {
                    return MaintainMessageUtils.getMaintainMessageResponse(
                            ResultCodes.RESULT_CD_EMSG_TEMPLATE_NOT_FOUND,
                            ResultMessages.RESULT_MSG_EMSG_TEMPLATE_NOT_FOUND);
                }

                logger.info("eMsg - appId: " + appId + ", trustSpAppId: " + trustSpAppId
                        + ", cmcTemplate.getAppId: " + cmcTemplate.getAppId()
                        + ", cmcTemplate.getTrustSpInd: " + cmcTemplate.getTrustSpInd()
                        + ", cmcTemplate.getServiceProviderId: " + cmcTemplate.getServiceProviderId()
                        + ", cmcTemplate.getIasInd: " + cmcTemplate.getIasInd()
                        + ", cmcTemplate.getClientId: " + cmcTemplate.getClientId());

                if (!appId.equals(cmcTemplate.getAppId())) {
                    if (trustSpAppId == null || !trustSpAppId.trim().equals(appId)
                            || cmcTemplate.getTrustSpInd().equals("N")) {
                        return MaintainMessageUtils.getMaintainMessageResponse(
                                ResultCodes.RESULT_CD_EMSG_TEMPLATE_NOT_FOUND,
                                ResultMessages.RESULT_MSG_EMSG_TEMPLATE_NOT_FOUND);
                    }
                }

                eMsgServiceProviderId = cmcTemplate.getServiceProviderId();
                boolean iasAutoCreate = "Y".equals(cmcTemplate.getIasAutoCreateInd());

                if ((!iasAutoCreate) && ((IntegrationConstants.IAS_IND_Y.equals(cmcTemplate.getIasInd())
                        || IntegrationConstants.IAS_IND_I.equals(cmcTemplate.getIasInd())))) {
                    if (cmcTemplate.getClientId() == null) {
                        logger.error("ClientID is not configured properly for SP [" + eMsgServiceProviderId + "]");
                        return MaintainMessageUtils.getMaintainMessageResponse(ResultCodes.RESULT_CD_GENERAL_ERROR,
                                ResultMessages.RESULT_MSG_GENERAL_ERROR);
                    }
                }

            }

            if (toDoItem != null) {

                CmcTemplate cmcTemplate = cmcTemplateDAO.getTemplateByIdVersion(conn, toDoItem.getTemplateId(),
                        toDoItem.getTemplateVersion(), CmcAppConstants.TO_DO_ITEM_TEMPLATE_TYPE);

                if (cmcTemplate == null) {
                    return MaintainMessageUtils.getMaintainMessageResponse(
                            ResultCodes.RESULT_CD_TO_DO_ITEM_TEMPLATE_NOT_FOUND,
                            ResultMessages.RESULT_MSG_TO_DO_ITEM_TEMPLATE_NOT_FOUND);
                }

                logger.info("toDoItem - appId: " + appId + ", trustSpAppId: " + trustSpAppId
                        + ", cmcTemplate.getAppId: " + cmcTemplate.getAppId()
                        + ", cmcTemplate.getTrustSpInd: " + cmcTemplate.getTrustSpInd()
                        + ", cmcTemplate.getServiceProviderId: " + cmcTemplate.getServiceProviderId()
                        + ", cmcTemplate.getIasInd: " + cmcTemplate.getIasInd()
                        + ", cmcTemplate.getClientId: " + cmcTemplate.getClientId());

                toDoItemServiceProviderId = cmcTemplate.getServiceProviderId();
                if (eMsgServiceProviderId != null
                        && (!eMsgServiceProviderId.equals(toDoItemServiceProviderId))) {
                    return MaintainMessageUtils.getMaintainMessageResponse(
                            ResultCodes.RESULT_CD_TEMPLATE_SERVICE_PROVIDER_MUST_BE_SAME,
                            ResultMessages.RESULT_MSG_TEMPLATE_SERVICE_PROVIDER_MUST_BE_SAME);
                }

                if (!appId.equals(cmcTemplate.getAppId())) {
                    return MaintainMessageUtils.getMaintainMessageResponse(
                            ResultCodes.RESULT_CD_TO_DO_ITEM_TEMPLATE_NOT_FOUND,
                            ResultMessages.RESULT_MSG_TO_DO_ITEM_TEMPLATE_NOT_FOUND);
                }
            }

            if (application != null) {

                CmcTemplate cmcTemplate = cmcTemplateDAO.getTemplateByIdVersion(conn, application.getTemplateId(),
                        application.getTemplateVersion(), CmcAppConstants.APPLICATION_TEMPLATE_TYPE);

                if (cmcTemplate == null) {
                    return MaintainMessageUtils.getMaintainMessageResponse(
                            ResultCodes.RESULT_CD_APPLICATION_TEMPLATE_NOT_FOUND,
                            ResultMessages.RESULT_MSG_APPLICATION_TEMPLATE_NOT_FOUND);
                }

                logger.info("application - appId: " + appId + ", trustSpAppId: " + trustSpAppId
                        + ", cmcTemplate.getAppId: " + cmcTemplate.getAppId()
                        + ", cmcTemplate.getTrustSpInd: " + cmcTemplate.getTrustSpInd()
                        + ", cmcTemplate.getServiceProviderId: " + cmcTemplate.getServiceProviderId()
                        + ", cmcTemplate.getIasInd: " + cmcTemplate.getIasInd()
                        + ", cmcTemplate.getClientId: " + cmcTemplate.getClientId());

                applicationServiceProviderId = cmcTemplate.getServiceProviderId();
                if (eMsgServiceProviderId != null && (!eMsgServiceProviderId.equals(applicationServiceProviderId))) {
                    return MaintainMessageUtils.getMaintainMessageResponse(
                            ResultCodes.RESULT_CD_TEMPLATE_SERVICE_PROVIDER_MUST_BE_SAME,
                            ResultMessages.RESULT_MSG_TEMPLATE_SERVICE_PROVIDER_MUST_BE_SAME);
                }

                if (!appId.equals(cmcTemplate.getAppId())) {
                    return MaintainMessageUtils.getMaintainMessageResponse(
                            ResultCodes.RESULT_CD_APPLICATION_TEMPLATE_NOT_FOUND,
                            ResultMessages.RESULT_MSG_APPLICATION_TEMPLATE_NOT_FOUND);
                }
            }

            List<MetaData> metaDataList = msgReq.getMetaDataList();
            for (MetaData metaData : metaDataList) {

                String dataContentEn = metaData.getDataContentEN();
                if (dataContentEn == null || dataContentEn.length() == 0) {
                    return MaintainMessageUtils.getMaintainMessageResponse(
                            ResultCodes.RESULT_CD_META_DATA_EN_EMPTY,
                            ResultMessages.RESULT_MSG_META_DATA_EN_EMPTY);
                }
            }
        }

        return null;
    }

    public static MaintainMessageResponse checkTranIdDuplicateAndOverLimit(MaintainMessageRequest maintMsgReq)
            throws Exception {
        boolean isLengthExceed = false;
        boolean isDuplicated = false;
        HashSet<String> hashSet = new HashSet<>();

        List<MessageRequest> msgReqList = maintMsgReq.getMessageRequests();

        for (int i = 0; i < msgReqList.size() && !isDuplicated && !isLengthExceed; i++) {

            MessageRequest msgReq = msgReqList.get(i);
            List<MetaData> metaDataList = msgReq.getMetaDataList();

            for (int j = 0; j < metaDataList.size() && !isDuplicated && !isLengthExceed; j++) {
                MetaData metaData = metaDataList.get(j);
                List<Recipient> recipientList = metaData.getRecipients();

                for (int k = 0; k < recipientList.size(); k++) {
                    String tranID = recipientList.get(k).getTranId();
                    if (tranID != null && tranID.length() > 20) {
                        isLengthExceed = true;
                        break;
                    }

                    if (tranID != null && hashSet.contains(tranID)) {

                        isDuplicated = true;
                        break;
                    } else {
                        hashSet.add(tranID);
                    }
                }
            }
        }

        if (isLengthExceed) {
            return MaintainMessageUtils.getMaintainMessageResponse(
                    ResultCodes.RESULT_CD_TRAN_ID_LENGTH_OVER_LIMIT,
                    ResultMessages.RESULT_MSG_TRAN_ID_LENGTH_OVER_LIMIT);
        } else if (isDuplicated) {
            return MaintainMessageUtils.getMaintainMessageResponse(
                    ResultCodes.RESULT_CD_TRAN_ID_IS_DUPLICATED,
                    ResultMessages.RESULT_MSG_TRAN_ID_IS_DUPLICATED);
        }

        return null;
    }
}
