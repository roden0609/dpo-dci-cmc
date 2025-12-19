package hk.gov.cmc.webserver.servlet;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import java.util.Timer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import hk.gov.cmc.config.CmcEnvProperties;
import hk.gov.cmc.config.CmcSystemParam;
import hk.gov.cmc.eid.client.EIDClient;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CmcSysParamServlet extends HttpServlet {
    private static final long serialVersionUID = 2957686946503571956L;
    private static Log logger = LogFactory.getLog(CmcSysParamServlet.class);
    private static Timer cmcSysParamTimer = null;
    private final static long SYS_PARAM_DEFAULT_RELOAD_PERIOD = 60 * 60 * 1000; // 1 hr in milli-second
    private final static CmcEnvProperties cmcEnvProperties = new CmcEnvProperties();

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {

            logger.info("CmcSysParamServlet.doPost - Start");

            Properties props = cmcEnvProperties.getProperties();

            String ipList = ",127.0.0.1," + props.getProperty("CONFIG_RELOAD_VALID_IP") + ",";
            String clientIP = request.getRemoteAddr();

            logger.info("ipList: " + ipList);
            logger.info("clientIP: " + clientIP);
            logger.info("reload: " + request.getParameter("reload"));

            if (clientIP != null) {
                if (ipList.indexOf("," + clientIP + ",") > -1) {
                    String reloadRequest = request.getParameter("reload");

                    if ("REVOKECEK".equals(reloadRequest.toUpperCase())) {
                        logger.info("REVOKECEK Check EIDUtils.initialize");
                        if (!EIDClient.initialize(props)) {
                            throw new Exception("Servlet init EIDUtils.initialize failed in CmcSysParamServlet.doPost");
                        }
                        logger.info("EIDUtils.revokeSymmetricEncryptionKey() - Start");
                        EIDClient.revokeSymmetricEncryptionKey();
                        logger.info("EIDUtils.revokeSymmetricEncryptionKey() - End");
                    }

                }
            }
        } catch (Exception e) {
            logger.error("CmcSysParamServlet.doPost error", e);
            throw new ServletException(e.toString());
        } finally {
            logger.info("CmcSysParamServlet.doPost - End");
        }
    }

    public void init(ServletConfig servletconfig) throws ServletException {
        super.init(servletconfig);

        try {
            logger.info("init CmcSysParamServlet");

            Properties props = cmcEnvProperties.getProperties();

            long reloadPeriod = SYS_PARAM_DEFAULT_RELOAD_PERIOD;
            try {
                reloadPeriod = Long.parseLong(props.getProperty("LOAD_CMC_SYS_PARAM_MILLIS"));
            } catch (Exception e) {
                logger.info("SYS_PARAM_DEFAULT_RELOAD_PERIOD is not configured, use default value:" + reloadPeriod
                        + " millisecond");
            }
            logger.info("SYS_PARAM_DEFAULT_RELOAD_PERIOD = " + reloadPeriod + " millisecond");

            CmcSystemParam.reload();
            cmcSysParamTimer = new Timer();
            cmcSysParamTimer.schedule(new CmcSystemParam(), new Date(System.currentTimeMillis() + reloadPeriod),
                    reloadPeriod);

            logger.info("end init CmcSysParamServlet");
        } catch (Exception e) {
            if (logger.isErrorEnabled())
                logger.error(e.getMessage());
            throw new ServletException(e.toString());
        }
    }

    public void destroy() {
        logger.info("destroy PotServlet");
        if (cmcSysParamTimer != null) {
            logger.info("destroy cmcSysParamTimer");
            cmcSysParamTimer.cancel();
        }
        logger.info("end destroy PotServlet");
    }
}
