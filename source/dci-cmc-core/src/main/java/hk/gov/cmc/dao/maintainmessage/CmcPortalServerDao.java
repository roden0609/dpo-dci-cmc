package hk.gov.cmc.dao.maintainmessage;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import hk.gov.cmc.persistence.connection.hpfw.HPFW_Connection;
import hk.gov.cmc.persistence.connection.hpfw.Parameter;

public class CmcPortalServerDAO {
    private static Log logger = LogFactory.getLog(CmcPortalServerDAO.class);

    public CmcPortalServerDAO() {
    }

    public String getPortalStatusByPortalId(HPFW_Connection conn, String portalId) throws Exception {
        logger.debug("[MaintMsg]getPortalStatusByPortalId - START");

        String result = "";
        ResultSet rs = null;

        try {

            ArrayList<Parameter> paraList = new ArrayList<Parameter>();

            paraList.add(new Parameter(Parameter.String, portalId));

            rs = conn.getResultSet("select STATUS from CMC_PORTAL_SERVER where PORTAL_ID = ? ", paraList);

            if (rs.next()) {
                result = rs.getString("STATUS");
            }

            logger.debug("[MaintMsg]getPortalStatusByPortalId - END");

            return result;

        } catch (Exception ex) {
            logger.error("[MaintMsg]General exception caught in getPortalStatusByPortalId", ex);
            throw ex;
        } finally {
            try {
                if (rs != null)
                    HPFW_Connection.close(rs);
            } catch (Exception ex) {
                logger.error("[MaintMsg]General exception caught in getPortalStatusByPortalId - rs.close();", ex);
            }
        }
    }
}