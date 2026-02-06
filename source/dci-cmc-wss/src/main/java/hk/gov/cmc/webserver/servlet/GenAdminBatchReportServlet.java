package hk.gov.cmc.webserver.servlet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import hk.gov.cmc.common.CmcAppPropertyNames;
import hk.gov.cmc.common.ReportConstants;
import hk.gov.cmc.config.CmcEnvProperties;
import hk.gov.cmc.dao.report.GenAdminBatchReportDAO;
import hk.gov.cmc.persistence.connection.hpfw.HPFW_Connection;
import hk.gov.cmc.processor.report.ReportApiProcessor;
import hk.gov.cmc.utils.job.JobControlUtils;
import hk.gov.gcis.rm.common.utils.PropertiesUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class GenAdminBatchReportServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static Log logger = LogFactory.getLog(GenAdminBatchReportServlet.class);
    private final static CmcEnvProperties cmcEnvProperties = new CmcEnvProperties();

    private static final String REQUSET_PARA_NAME_REPORT_ID = "reportId";
    private static final String REQUEST_PARA_NAME_BATCH_NAME = "batchName";

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doPost(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        logger.info("GenAdminBatchReportServlet starts");
        HPFW_Connection hpfwConn = null;

        /* cmc job control lock */
        JobControlUtils jobControlUtils = null;
        String jobControlName = "";
        boolean releaseLock = true;
        Properties props;

        String reportId = null;
        String batchName = null;

        try {
            reportId = req.getParameter(REQUSET_PARA_NAME_REPORT_ID);
            batchName = req.getParameter(REQUEST_PARA_NAME_BATCH_NAME);

            logger.info("req.getParameter(reportId) = " + reportId + ", req.getParameter(batchName) = " + batchName);

            hpfwConn = HPFW_Connection.getHPFW_Connection(false);
            hpfwConn.setAutoCommit(false);
            hpfwConn.begin(null, null, HPFW_Connection.DIRECT);

            String[] reportIdList;

            if (reportId == null) {
                logger.info("No report ID is received....");
                return;
            } else {
                reportIdList = reportId.split(",");

                if (reportIdList != null) {
                    for (String tempReportId : reportIdList) {
                        logger.info("tempReportId = " + tempReportId);
                        if (!GenAdminBatchReportDAO.isValidReportId(hpfwConn, tempReportId)) {
                            logger.info("Invalid report ID ....");
                            return;
                        }
                    }
                }
            }

            props = cmcEnvProperties.getProperties();
            jobControlName = PropertiesUtils.getMandatoryProperty(props,
                    CmcAppPropertyNames.JOB_CONTROL_GEN_ADMIN_REPORT_PROPERTY_NAME) + "_" + batchName;
            jobControlUtils = new JobControlUtils();

            if (jobControlUtils.lockJobControl(jobControlName)) {
                releaseLock = false;

                Map<String, String> paramMap = new HashMap<String, String>();
                Calendar cal = Calendar.getInstance();
                Date now = cal.getTime();

                SimpleDateFormat sdf = new SimpleDateFormat(ReportConstants.REPORTING_DATE_FORMAT);
                String reportingDateStr = sdf.format(now);

                paramMap.put(ReportConstants.MAP_PARAM_KEY_REPORTING_DATE, reportingDateStr);

                Enumeration<String> parameterNames = req.getParameterNames();
                while (parameterNames.hasMoreElements()) {
                    String paramName = parameterNames.nextElement();

                    if (!REQUSET_PARA_NAME_REPORT_ID.equals(paramName)
                            && !REQUEST_PARA_NAME_BATCH_NAME.equals(paramName)) {
                        logger.info("additional paramName = " + paramName);
                        paramMap.put(paramName, (String) req.getParameter(paramName));
                    }
                }

                for (String tempReportId : reportIdList) {
                    logger.info("prepare generate report tempReportId = " + tempReportId);
                    ReportApiProcessor.generateBatchReport(tempReportId, paramMap);
                }
            } else {
                logger.info("[GEN_ADMIN_REPORT] CMC_MARS_SYN_JOB_LOCK table cannot be locked." + jobControlName);
            }

        } catch (Exception e) {
            logger.error("General Error Found in GenAdminBatchReportServlet: " + e.getMessage(), e);
        } finally {
            if (hpfwConn != null)
                HPFW_Connection.close(hpfwConn);

            if (!releaseLock) {
                if (jobControlUtils != null) {
                    jobControlUtils.releaseJobControl(jobControlName);
                    releaseLock = true;
                }
            }
        }

        logger.info("GenAdminBatchReportServlet end");
    }

}
