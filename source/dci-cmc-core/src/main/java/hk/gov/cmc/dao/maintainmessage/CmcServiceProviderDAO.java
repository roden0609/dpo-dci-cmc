package hk.gov.cmc.dao.maintainmessage;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import hk.gov.cmc.persistence.connection.hpfw.HPFW_Connection;
import hk.gov.cmc.persistence.connection.hpfw.Parameter;
import hk.gov.cmc.persistence.maintainmessage.CmcServiceProvider_;

public class CmcServiceProviderDAO {
    private static Log logger = LogFactory.getLog(CmcServiceProviderDAO.class);

    public CmcServiceProvider_ getServiceProviderByAppId(HPFW_Connection conn, String appId) throws Exception {
        CmcServiceProvider_ sp = null;
        logger.debug("getServiceProviderByAppId - START");
        try {
            ArrayList<Parameter> paraList = new ArrayList<Parameter>();
            paraList.add(new Parameter(Parameter.String, appId));

            ArrayList<CmcServiceProvider_> spList = CmcServiceProvider_.getResultList(conn, "where app_id = ?",
                    paraList);
            if (spList != null && spList.size() > 0)
                sp = spList.get(0);
        } catch (Exception ex) {
            logger.error("getServiceProviderByAppId - General exception caught, ex: ", ex);
            throw ex;
        } finally {
            logger.debug("getServiceProviderByAppId - END");
        }
        return sp;
    }
}
