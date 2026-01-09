package hk.gov.cmc.dao.maintainmessage.param;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import hk.gov.cmc.model.maintainmessage.param.MessageParam;
import hk.gov.cmc.persistence.connection.hpfw.HPFW_Connection;
import hk.gov.cmc.persistence.connection.hpfw.Parameter;

public class MessageParamDAO {

    private static Log logger = LogFactory.getLog(MessageParamDAO.class);

    public static List<MessageParam> getMsgParamByMsgType(HPFW_Connection conn, String msgType) throws Exception {
        logger.debug("getMsgParamByMsgType - start");
        List<MessageParam> resultList = new ArrayList<MessageParam>();
        ResultSet rs = null;

        try {
            ArrayList<Parameter> paraList = new ArrayList<Parameter>();
            paraList.add(new Parameter(Parameter.String, msgType));

            rs = conn.getResultSet("SELECT para_name, "
                    + "       para_source, "
                    + "       message_type, "
                    + "       mars_user_attr, "
                    + "       static_para_name_en, "
                    + "       static_para_name_tc, "
                    + "       static_para_name_sc "
                    + "FROM   cmc_message_param "
                    + "WHERE  message_type = ? ", paraList);

            while (rs.next()) {
                MessageParam tempBean = new MessageParam();
                tempBean.setParaName(rs.getString("PARA_NAME"));
                tempBean.setParaSource(rs.getString("PARA_SOURCE"));
                tempBean.setMessageType(rs.getString("MESSAGE_TYPE"));
                tempBean.setMarsUserAttr(rs.getString("MARS_USER_ATTR"));
                tempBean.setStaticParaEn(rs.getString("STATIC_PARA_NAME_EN"));
                tempBean.setStaticParaTc(rs.getString("STATIC_PARA_NAME_TC"));
                tempBean.setStaticParaSc(rs.getString("STATIC_PARA_NAME_SC"));

                resultList.add(tempBean);
            }
        } catch (Exception ex) {
            logger.error("getMsgParamByMsgType - error, ex:", ex);
            throw ex;
        } finally {
            try {
                if (rs != null)
                    HPFW_Connection.close(rs);
            } catch (Exception ex) {
                logger.error("getMsgParamByMsgType - error closing ResultSet, ex:", ex);
            }

            logger.debug("getMsgParamByMsgType - end");
        }
        return resultList;
    }
}
