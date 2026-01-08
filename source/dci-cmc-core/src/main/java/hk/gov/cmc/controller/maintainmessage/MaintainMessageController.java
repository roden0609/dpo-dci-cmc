package hk.gov.cmc.controller.maintainmessage;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import hk.gov.cmc.model.maintainmessage.application.Application;
import hk.gov.cmc.model.maintainmessage.emessage.EMessage;
import hk.gov.cmc.model.maintainmessage.request.MaintainMessageRequest;
import hk.gov.cmc.model.maintainmessage.request.MessageRequest;
import hk.gov.cmc.model.maintainmessage.request.MetaData;
import hk.gov.cmc.model.maintainmessage.request.Recipient;
import hk.gov.cmc.model.maintainmessage.response.MaintainMessageResponse;
import hk.gov.cmc.model.maintainmessage.todoitem.ToDoItem;
import hk.gov.cmc.persistence.connection.hpfw.HPFW_Connection;
import hk.gov.cmc.utils.maintainmessage.MaintainMessageUtils;
import hk.gov.cmc.validator.MaintainMessageValidator;

public class MaintainMessageController {

    private static Log logger = LogFactory.getLog(MaintainMessageController.class);

    public MaintainMessageController() {
    }

    public MaintainMessageResponse processMessage(HPFW_Connection conn, String appId,
            MaintainMessageRequest maintMsgReq) throws Exception {
        logger.info("processMessage start - appId: " + appId);

        MaintainMessageResponse response = new MaintainMessageResponse();

        try {

            MaintainMessageResponse checkHasProcessDataResponse = MaintainMessageValidator
                    .checkHasProcessDataRequest(conn, maintMsgReq);
            if (checkHasProcessDataResponse != null) {
                return checkHasProcessDataResponse;
            }

            MaintainMessageResponse checkAllMessagesResponse = MaintainMessageValidator.checkAllMessagesRequest(conn,
                    appId, maintMsgReq);
            if (checkAllMessagesResponse != null) {
                return checkAllMessagesResponse;
            }

            MaintainMessageResponse checkTranIdResponse = MaintainMessageValidator
                    .checkTranIdDuplicateAndOverLimit(maintMsgReq);
            if (checkTranIdResponse != null) {
                return checkTranIdResponse;
            }

            List<MessageRequest> msgReqList = maintMsgReq.getMessageRequests();
            if (msgReqList != null) {
                logger.info("processMessage - msgReqList.size: " + msgReqList.size());
            }

            for (MessageRequest msgReq : msgReqList) {
                EMessage eMsg = msgReq.getEMessage();
                ToDoItem toDoItem = msgReq.getToDoItem();
                Application application = msgReq.getApplication();
                List<MetaData> metaDataList = msgReq.getMetaDataList();

                for (MetaData metaData : metaDataList) {
                    String dataContentEn = metaData.getDataContentEN();
                    String dataContentTc = metaData.getDataContentTC();
                    String dataContentSc = metaData.getDataContentSC();

                    if (dataContentTc == null || dataContentTc.length() == 0)
                        dataContentTc = dataContentEn;

                    if (dataContentSc == null || dataContentSc.length() == 0)
                        dataContentSc = dataContentTc;

                    List<Recipient> recipientList = metaData.getRecipients();

                    MessageProcessor messageProcessor = new MessageProcessor();
                    if (recipientList != null && recipientList.size() > 0) {
                        messageProcessor.processMessage(response,
                                conn, msgReq.getPortalId(), recipientList,
                                eMsg, toDoItem, application,
                                dataContentEn, dataContentTc, dataContentSc);

                        logger.info("processMessage - appId: " + appId
                                + ", messageProcessor.processMessage completed, recipientList:" + recipientList.size()
                                + ", response.getResultCode=" + response.getResultCode()
                                + ", response.getResultMessage="
                                + response.getResultMessage());
                    } else {
                        logger.info("processMessage - appId: " + appId
                                + ", messageProcessor.processMessage did not process due to recipientList is empty.");
                    }

                }

            }

        } catch (Exception ex) {
            logger.error("processMessage error", ex);
            throw ex;
        } finally {
            logger.info("processMessage end, appId: " + appId);
        }

        return MaintainMessageUtils.getConcludedMaintainMessageResponse(response);
    }

}
