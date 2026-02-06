package hk.gov.cmc.processor.report.impl;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import hk.gov.cmc.common.ReportConstants;
import hk.gov.cmc.config.CmcEnvProperties;
import hk.gov.cmc.persistence.connection.hpfw.HPFW_Connection;
import hk.gov.cmc.persistence.report.AdmRptInfo_;
import hk.gov.cmc.processor.report.IReportImplement;
import hk.gov.cmc.processor.report.ReportResult;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.pdf.JRPdfExporter;

public class IAS06DReport implements IReportImplement {

    private static Log logger = LogFactory.getLog(IAS06DReport.class);
    private static String JASPER_REPORT_PATH = null;
    private final static CmcEnvProperties cmcEnvProperties = new CmcEnvProperties();

    static {
        try {
            Properties prop = cmcEnvProperties.getProperties();
            JASPER_REPORT_PATH = prop.getProperty("JASPER_REPORT_PATH");
        } catch (Exception e) {
            logger.error("Fail to get JASPER_REPORT_PATH parameter from property file");
        }
    }

    @Override
    public List<ReportResult> generateReport(AdmRptInfo_ admRptInfo, Map<String, String> paramMap) throws Exception {
        if (logger.isDebugEnabled())
            logger.debug("generateReport() begin");

        if (logger.isDebugEnabled()) {
            logger.debug("AdmRptInfo_ indebugation:");
            logger.debug("BatchRptDefaultFormat[" + admRptInfo.getBatchRptDefaultFormat() + "]");
            logger.debug("BatchRptFuncId[" + admRptInfo.getBatchRptFuncId() + "]");
            logger.debug("CmcRpt[" + admRptInfo.getCmcRpt() + "]");
            logger.debug("ImplClass[" + admRptInfo.getImplClass() + "]");
            logger.debug("LastModifyBy[" + admRptInfo.getLastModifyBy() + "]");
            logger.debug("LastModifyDt[" + admRptInfo.getLastModifyDt() + "]");
            logger.debug("MarsRpt[" + admRptInfo.getMarsRpt() + "]");
            logger.debug("RptDesc[" + admRptInfo.getRptDesc() + "]");
            logger.debug("RptId[" + admRptInfo.getRptId() + "]");
            logger.debug("SpidBatchRpt[" + admRptInfo.getSpidBatchRpt() + "]");
            logger.debug("TeamBatchRpt[" + admRptInfo.getTeamBatchRpt() + "]");
            if (paramMap != null && !paramMap.isEmpty()) {
                logger.debug("paramMap key and value:");
                for (String key : paramMap.keySet()) {
                    logger.debug("key[" + key + "], value[" + paramMap.get(key) + "]");
                }
            }
        }

        List<ReportResult> resultList = new ArrayList<ReportResult>();
        HPFW_Connection hpfwConn = null;
        ByteArrayOutputStream baos = null;

        SimpleDateFormat displaySdf = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat inputDateSdf = new SimpleDateFormat("yyyyMMdd");

        try {
            boolean isAdhoc = MAP_PARAM_VALUE_YES.equals(paramMap.get(MAP_PARAM_KEY_IS_ADHOC));
            boolean isBatchRegen = MAP_PARAM_VALUE_YES.equals(paramMap.get(MAP_PARAM_KEY_IS_BATCH_REGEN));

            if (logger.isDebugEnabled()) {
                logger.debug("isAdhoc[" + isAdhoc + "]");
                logger.debug("isBatchRegen[" + isBatchRegen + "]");
            }

            String reportingDateStr = paramMap.get(ReportConstants.MAP_PARAM_KEY_REPORTING_DATE);
            String spId = paramMap.get(MAP_PARAM_KEY_SP_ID);

            if (reportingDateStr == null || reportingDateStr.trim().length() == 0) {
                throw new NullPointerException(
                        "paramMap " + ReportConstants.MAP_PARAM_KEY_REPORTING_DATE + " is null or empty.");
            }
            if (spId == null || spId.trim().length() == 0) {
                throw new NullPointerException("paramMap " + MAP_PARAM_KEY_SP_ID + " is null or empty.");
            }

            SimpleDateFormat sdf = new SimpleDateFormat(ReportConstants.REPORTING_DATE_FORMAT);
            sdf.setLenient(false);
            Date reportingDate = sdf.parse(reportingDateStr);

            // Request Date in old reporting framework is passed from shell script which is set to "-1 days"
            // Now the request date is calculated within report generation class instead of passing from script
            Calendar cal = Calendar.getInstance();
            cal.setTime(reportingDate);
            cal.add(Calendar.DATE, -1);
            Date requestDate = cal.getTime();
            String requestDateStr = inputDateSdf.format(requestDate);

            if (logger.isDebugEnabled()) {
                logger.debug("reportingDateStr[" + reportingDateStr + "]");
                logger.debug("spId[" + spId + "]");
                logger.debug("requestDate[" + requestDate + "]");
            }

            StringBuilder whereClause = new StringBuilder();
            if (!MAP_PARAM_VALUE_IS_ADMIN.equals(spId)) {
                whereClause.append("and csp.service_provider_id='").append(spId).append("'");
            }

            // Jasperreport parameters
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("SPID", (MAP_PARAM_VALUE_IS_ADMIN.equals(spId) ? null : spId));
            parameters.put("SEARCH_BY_SPID", whereClause.toString());
            parameters.put("SUBREPORT_DIR", JASPER_REPORT_PATH + "/");
            parameters.put("REQUEST_DATE", displaySdf.format(requestDate));
            parameters.put("GEN_DATE", displaySdf.format(reportingDate));

            hpfwConn = HPFW_Connection.getHPFW_Connection();

            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    JASPER_REPORT_PATH + "/" + admRptInfo.getRptId() + ".jasper", parameters,
                    hpfwConn.getConnectionPtr());

            baos = new ByteArrayOutputStream();

            // Export to pdf
            JRPdfExporter exporter = new JRPdfExporter();
            List<JasperPrint> jasperPrintList = Collections.singletonList(jasperPrint);
            exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(baos));
            exporter.exportReport();
            baos.close();

            ReportResult pdfResult = new ReportResult();
            pdfResult.setSuccess(true);
            pdfResult.setOutputFileType(admRptInfo.getBatchRptDefaultFormat());
            pdfResult.setDataStartDate(requestDate);
            pdfResult.setReportingDate(reportingDate);
            pdfResult.setOutputFileByteArray(baos.toByteArray());
            resultList.add(pdfResult);

            HPFW_Connection.close(hpfwConn);

            if (logger.isInfoEnabled())
                logger.info("IAS06DReport successful reportingDateStr[" + reportingDateStr + "], requestDate["
                        + requestDateStr + "], spId[" + spId + "]");

            return resultList;
        } catch (Exception ex) {
            logger.error("General exception raised in generateReport", ex);
            throw ex;
        } finally {
            if (hpfwConn != null)
                HPFW_Connection.close(hpfwConn);
            if (baos != null)
                baos.close();
            if (logger.isDebugEnabled())
                logger.debug("generateReport() end");
        }
    }
}
