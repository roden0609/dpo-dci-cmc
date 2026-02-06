package hk.gov.cmc.webserver.servlet;

import java.io.IOException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import hk.gov.cmc.common.CmcAppPropertyNames;
import hk.gov.cmc.common.ReportConstants;
import hk.gov.cmc.config.CmcEnvProperties;
import hk.gov.cmc.processor.report.ReportApiProcessor;
import hk.gov.cmc.utils.job.JobControlUtils;
import hk.gov.gcis.rm.common.utils.PropertiesUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class RegenerateAdminBatchReportServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static Log logger = LogFactory.getLog(RegenerateAdminBatchReportServlet.class);
    private final static CmcEnvProperties cmcEnvProperties = new CmcEnvProperties();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("RegenerateAdminBatchReportServlet start");
        Properties props;

        /* cmc job control lock */
        JobControlUtils jobControlUtils = null;
        String jobControlName = "";
        boolean releaseLock = true;

        try {
            props = cmcEnvProperties.getProperties();
            jobControlName = PropertiesUtils.getMandatoryProperty(props,
                    CmcAppPropertyNames.JOB_CONTROL_REGEN_ADMIN_BATCH_REPORT_PROPERTY_NAME);
            jobControlUtils = new JobControlUtils();

            if (jobControlUtils.lockJobControl(jobControlName)) {
                releaseLock = false;

                ReportApiProcessor.executeBatchRegen(ReportConstants.PROJECT_CMC);
            } else {
                logger.info("[GEN_ADMIN_REPORT] CMC_MARS_SYN_JOB_LOCK table cannot be locked." + jobControlName);
            }
        } catch (Exception e) {
            logger.error("General Error Found in RegenerateAdminBatchReportServlet: " + e.getMessage(), e);
        } finally {
            if (!releaseLock) {
                if (jobControlUtils != null) {
                    jobControlUtils.releaseJobControl(jobControlName);
                    releaseLock = true;
                }
            }
        }
        logger.info("RegenerateAdminBatchReportServlet end");
    }
}
