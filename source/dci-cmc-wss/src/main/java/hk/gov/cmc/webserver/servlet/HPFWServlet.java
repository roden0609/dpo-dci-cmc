package hk.gov.cmc.webserver.servlet;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import hk.gov.cmc.config.CmcEnvProperties;
import hk.gov.cmc.persistence.connection.hpfw.CommonDBUtils;
import hk.gov.cmc.persistence.connection.hpfw.HPFW_Connection;
import hk.gov.cmc.persistence.connection.hpfw.HistSynJob;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HPFWServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static Log logger = LogFactory.getLog(HPFWServlet.class);
    private final static CmcEnvProperties cmcEnvProperties = new CmcEnvProperties();

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    public void init(ServletConfig servletconfig) throws ServletException {
        super.init(servletconfig);
        try {
            logger.info("init HPFWServlet");
            Properties properties = cmcEnvProperties.getProperties();
            logger.debug("properties is null: " + (properties == null));
            if (properties != null) {
                StringBuilder builder = new StringBuilder(256);
                builder.append("properties:");
                for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                    builder.append('\n').append(entry.getKey()).append('=').append(entry.getValue());
                }
                logger.debug(builder.toString());
            }
            HPFW_Connection.initPool(properties);

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
