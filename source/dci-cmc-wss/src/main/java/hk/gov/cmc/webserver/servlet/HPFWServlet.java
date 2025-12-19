package hk.gov.cmc.webserver.servlet;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hk.gov.ogcio.mars_cmc.framework.common.admin.Ref_Param;
import hk.gov.ogcio.mars_cmc.framework.common.sql.CommonDBUtils;
import hk.gov.ogcio.mars_cmc.framework.common.sql.HPFW_Connection;
import hk.gov.ogcio.mars_cmc.framework.common.sql.HistSynJob;
import hk.gov.ogcio.egis.rm.common.utils.ServiceLocator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HPFWServlet extends HttpServlet {

    private static final long serialVersionUID = 6850265215857459663L;

    private static Log logger = LogFactory.getLog(HPFWServlet.class);

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    public void init(ServletConfig servletconfig) throws ServletException {
        super.init(servletconfig);
        try {
            logger.debug("init HPFWServlet");
            ServiceLocator serviceLocator = ServiceLocator.getInstance();
            Properties properties = serviceLocator.getProperties();
            HPFW_Connection.initPool(properties);
            Ref_Param.loadHashTable();

            String synByThreadEnabled = (properties.getProperty("DB_HIST_SYN_BY_THREAD_ENABLED") == null) ? "N"
                    : properties.getProperty("DB_HIST_SYN_BY_THREAD_ENABLED");
            boolean synByThread = (synByThreadEnabled.equals("Y")) ? true : false;
            if (synByThread) {
                logger.debug("DB_HIST_SYN_BY_THREAD_ENABLED=" + synByThreadEnabled + ": HistSynJob started by threads");
                HistSynJob.initJob(properties);
            } else {
                logger.debug(
                        "DB_HIST_SYN_BY_THREAD_ENABLED=" + synByThreadEnabled + ": HistSynJob NOT started by threads");
            }

            logger.debug("SERVER_SEQUENCE_PREFIX=" + CommonDBUtils.getSERVER_SEQUENCE_PREFIX());
            logger.debug("end init HPFWServlet");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new ServletException(e.toString());
        }
    }

    public void destroy() {
    }
}
