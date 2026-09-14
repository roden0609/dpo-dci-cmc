package hk.gov.cmc.dao.maintainmessage;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import hk.gov.cmc.persistence.connection.hpfw.HPFW_Connection;
import hk.gov.cmc.persistence.connection.hpfw.Parameter;
import hk.gov.cmc.persistence.serviceprovider.CmcServiceProvider_;

public class CmcServiceProviderDAO {
    private static Log logger = LogFactory.getLog(CmcServiceProviderDAO.class);

    public CmcServiceProvider_ getServiceProviderByAppId(HPFW_Connection conn, String appId) throws Exception {
        CmcServiceProvider_ sp = null;
        logger.debug("getServiceProviderByAppId - appId: " + appId);
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
        }
        return sp;
    }

    public static HashSet<String> getServiceProviderIdByAppId(HPFW_Connection conn, String appId) throws Exception {
        logger.debug("getServiceProviderIdByAppId - appId: " + appId);

        HashSet<String> serviceProviderIdSet = new HashSet<String>();
        ResultSet rs = null;

        try {
            ArrayList<Parameter> paraList = new ArrayList<Parameter>();
            paraList.add(new Parameter(Parameter.String, appId));

            rs = conn.getResultSet("select service_provider_id from cmc_service_provider where app_id = ?", paraList);

            while (rs.next()) {
                serviceProviderIdSet.add(rs.getString("service_provider_id"));
            }

            logger.debug("getServiceProviderIdByAppId - END");
        } catch (Exception ex) {
            logger.error("getServiceProviderIdByAppId - General exception caught, ex: ", ex);
            throw ex;
        } finally {
            if (rs != null) {
                rs.close();
            }
        }

        return serviceProviderIdSet;
    }
}
