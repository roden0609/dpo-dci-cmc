package hk.gov.cmc.dao.report;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import hk.gov.cmc.persistence.connection.hpfw.HPFW_Connection;

public class GenAdminBatchReportDAO {

    private static Log logger = LogFactory.getLog(GenAdminBatchReportDAO.class);
    private static final String GET_REPORT_ID = "select rpt_id from adm_rpt_info where rpt_id = ? ";

    public static boolean isValidReportId(HPFW_Connection hpfwConn, String reportId) throws Exception {
        boolean result = false;

        Connection conn = hpfwConn.getConnectionPtr();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement(GET_REPORT_ID);
            ps.setString(1, reportId);
            rs = ps.executeQuery();

            if (rs.next()) {
                result = true;
            }
        } catch (Exception e) {
            logger.error("Exception caught in GenAdminBatchReportDAO isValidReportId e : " + e);
            throw e;
        } finally {
            if (rs != null)
                rs.close();
            if (ps != null)
                ps.close();
        }

        return result;
    }
}
