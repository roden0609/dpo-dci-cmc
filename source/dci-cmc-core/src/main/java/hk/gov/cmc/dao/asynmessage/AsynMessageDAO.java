package hk.gov.cmc.dao.asynmessage;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import hk.gov.cmc.common.CmcAppConstants;
import hk.gov.cmc.model.asynmessage.AsynMessage;
import hk.gov.cmc.persistence.asynmessage.CmcAsynMessage_;
import hk.gov.cmc.persistence.connection.hpfw.HPFW_Connection;
import hk.gov.cmc.persistence.connection.hpfw.Parameter;
import hk.gov.gcis.ss.common.utils.SoapUtils;
import jakarta.xml.soap.SOAPMessage;

public class AsynMessageDAO {

    private static Log logger = LogFactory.getLog(AsynMessageDAO.class);
    private final String SELECT_OUTSTANDING_MSG = "select MSG_ID, STATUS, RECIPIENT_APP_ID, RECIPIENT_APP_TYPE, MSG_RESPONSE "
            + "from CMC_ASYN_MESSAGE "
            + "where STATUS <> ? ";
    private final String SELECT_MSG_BY_MSG_ID = "select STATUS "
            + "from CMC_ASYN_MESSAGE "
            + "where MSG_ID = ? ";

    public AsynMessageDAO() {
    }

    public boolean createAsynMessageRecord(HPFW_Connection conn, String messageId, String recipientAppId,
            String recipientAppType, SOAPMessage msgResponse, String status) throws Exception {
        logger.debug("createAsynMessageRecord - START");

        try {

            // create CMC_ASYN_MESSAGE record
            CmcAsynMessage_ cmcAsynMessage = new CmcAsynMessage_();
            cmcAsynMessage.setMsgId(messageId);
            cmcAsynMessage.setRecipientAppId(recipientAppId);
            cmcAsynMessage.setRecipientAppType(recipientAppType);
            if (msgResponse != null)
                cmcAsynMessage.setMsgResponse(new String(SoapUtils.serializeMsg(msgResponse)));
            cmcAsynMessage.setStatus(status);
            cmcAsynMessage.insert(conn);

            logger.debug("createAsynMessageRecord - END");

        } catch (java.sql.SQLException sqlEx) {
            String errMessage = sqlEx.getMessage();
            if (errMessage.indexOf("ORA-00001") == -1) {
                logger.debug("General exception caught in createAsynMessageRecord Error message: " + errMessage);
                throw sqlEx;
            } else {
                // asyn message already retrieved
                logger.warn("General exception caught in createAsynMessageRecord", sqlEx);
                return false;
            }
        } catch (Exception ex) {
            logger.error("General exception caught in createAsynMessageRecord", ex);
            throw ex;
        }

        return true;
    }

    public boolean createAsynMessageRecord(HPFW_Connection conn, String messageId, String recipientAppId,
            String recipientAppType, SOAPMessage msgResponse) throws Exception {
        return createAsynMessageRecord(conn, messageId, recipientAppId, recipientAppType, msgResponse,
                CmcAppConstants.ASYN_MSG_STATUS_RECEIVED);
    }

    public void updateAsynMessageStatus(HPFW_Connection conn, String msgId, String status) throws Exception {
        logger.debug("updateAsynMessageStatus - START");

        try {
            ArrayList<Parameter> paraSetList = new ArrayList<>();
            ArrayList<Parameter> paraWhereList = new ArrayList<>();
            paraSetList.add(new Parameter(Parameter.String, status));
            paraWhereList.add(new Parameter(Parameter.String, msgId));

            CmcAsynMessage_.update(conn, "set STATUS = ?", paraSetList, "where MSG_ID = ? ", paraWhereList);

            logger.debug("updateAsynMessageStatus - END");

        } catch (Exception ex) {
            logger.error("General exception caught in updateAsynMessageStatus", ex);
            throw ex;
        }
    }

    public List<AsynMessage> getOutstandingMessage(HPFW_Connection conn) throws Exception {
        logger.debug("getOutstandingMessage - START");

        ArrayList<AsynMessage> resultList = new ArrayList<>();
        ResultSet rs = null;

        try {

            ArrayList<Parameter> paraList = new ArrayList<>();

            paraList.add(new Parameter(Parameter.String, CmcAppConstants.ASYN_MSG_STATUS_RESPONSE_SENT));

            rs = conn.getResultSet(SELECT_OUTSTANDING_MSG, paraList);

            while (rs.next()) {
                AsynMessage asynMessage = new AsynMessage();
                asynMessage.setScopesMessageId(rs.getString("MSG_ID"));
                asynMessage.setStatus(rs.getString("STATUS"));
                asynMessage.setRecipientAppId(rs.getString("RECIPIENT_APP_ID"));
                asynMessage.setRecipientAppType(rs.getString("RECIPIENT_APP_TYPE"));
                asynMessage.setMsgResponse(rs.getString("MSG_RESPONSE"));
                resultList.add(asynMessage);
            }

            logger.debug("getOutstandingMessage - END");

            return resultList;

        } catch (Exception ex) {

            logger.error("General exception caught in getOutstandingMessage", ex);
            throw ex;
        } finally {
            try {
                if (rs != null)
                    HPFW_Connection.close(rs);
            } catch (Exception ex) {
                logger.error("General exception caught in getOutstandingMessage - rs.close();", ex);
            }
        }
    }

    public boolean isAsynMessageRetrievedBefore(HPFW_Connection conn, String scopesMsgId) throws Exception {
        logger.debug("isAsynMessageRetrievedBefore - START");

        ResultSet rs = null;

        try {
            ArrayList<Parameter> paraList = new ArrayList<>();
            paraList.add(new Parameter(Parameter.String, scopesMsgId));
            rs = conn.getResultSet(SELECT_MSG_BY_MSG_ID, paraList);

            if (rs.next()) {
                return true;
            }

            logger.debug("isAsynMessageRetrievedBefore - END");

        } catch (Exception ex) {
            logger.error("General exception caught in isAsynMessageRetrievedBefore", ex);
            throw ex;
        } finally {
            try {
                if (rs != null)
                    HPFW_Connection.close(rs);
            } catch (Exception ex) {
                logger.error("General exception caught in isAsynMessageRetrievedBefore - rs.close();", ex);
            }
        }

        return false;
    }

    public void updateAsynMessage(HPFW_Connection conn, String msgId, SOAPMessage msgResponse, String status)
            throws Exception {
        logger.debug("updateAsynMessage - START");

        try {

            // update CMC_ASYN_MESSAGE message response and status
            ArrayList<Parameter> paraSetList = new ArrayList<>();
            ArrayList<Parameter> paraWhereList = new ArrayList<>();

            if (msgResponse != null)
                paraSetList.add(new Parameter(Parameter.String, new String(SoapUtils.serializeMsg(msgResponse))));
            else
                paraSetList.add(new Parameter(Parameter.String, null));

            paraSetList.add(new Parameter(Parameter.String, status));

            paraWhereList.add(new Parameter(Parameter.String, msgId));

            CmcAsynMessage_.update(conn, "set MSG_RESPONSE = ?, STATUS = ?", paraSetList, "where MSG_ID = ? ",
                    paraWhereList);

            logger.debug("updateAsynMessage - END");

        } catch (Exception ex) {
            logger.error("General exception caught in updateAsynMessage", ex);
            throw ex;
        }
    }

}
