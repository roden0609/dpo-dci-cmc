package hk.gov.cmc.batch;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import hk.gov.cmc.config.CmcEnvProperties;
import hk.gov.cmc.dao.healthcheck.HealthCheckDAO;
import hk.gov.cmc.persistence.connection.hpfw.HPFW_Connection;
import hk.gov.cmc.utils.common.AesZipFileUtil;
import hk.gov.gcis.rm.common.utils.encoder.EncoderUtils;
import hk.gov.gcis.rm.common.utils.file.ReaderUtils;
import hk.gov.gcis.ss.noti.client.NotificationClient;
import hk.gov.gcis.ss.noti.datatype.Attachment;
import hk.gov.gcis.ss.noti.datatype.Recipient;
import jakarta.xml.soap.SOAPMessage;

public class CmcHealthCheckJob {

    private static Log logger = LogFactory.getLog(CmcHealthCheckJob.class);
    private final static CmcEnvProperties cmcEnvProperties = new CmcEnvProperties();

    private final static HealthCheckDAO dao = new HealthCheckDAO();

    private final static String REPORT_TEMPLATE = "CMC health checking notification as of {currentDateStr}\r\n" +
            "\r\n" +
            "FOR YOUR IMMEDIATE ACTION IF VALUE IS > 0\r\n" +
            "1) number of iAM Smart Message push notification not send successfully: {numOfIasNotiNotSuccess}\r\n" +
            "2) number of iAM Smart Message fail to get notification ID from iAM Smart: {numOfGetNotiIdNotSuccess}\r\n";

    private final static String REPORT_TEMPLATE2 = "\r\n==========================================================================================\r\n"
            +
            "\r\n" +
            "FOR YOUR IMMEDIATE ACTION IF VALUE IS == 0\r\n" +
            "1) number of iAM Smart account status update on previous day: {numOfIasAccStatusUpdate}\r\n";

    private final static String REPORT_TEMPLATE3 = "\r\n==========================================================================================\r\n"
            +
            "\r\n" +
            "FOR YOUR INFORMATION\r\n" +
            "1) number of iAM Smart message per template on previous day: \r\n{numOfIasMsgPerTemplate}\r\n" +
            "2) Number of iAM Smart Message to be housekeep next month: \r\n{numOfIasMessageHouseKeepNextMonth}\r\n" +
            "3) number of iAM Smart Message issued per service provider in previous month: \r\n{numOfIasMsgIssuedPerSPPreviousMonth}\r\n";

    private final static String REPORT_TEMPLATE_EMAIL_CONTENT = REPORT_TEMPLATE +
            REPORT_TEMPLATE2 +
            REPORT_TEMPLATE3;

    private final static String REPORT_TEMPLATE_ATTACHMENT = REPORT_TEMPLATE +
            REPORT_TEMPLATE2 +
            REPORT_TEMPLATE3;

    public void execute(Map<String, Object> params) throws Exception {

        logger.info("[BATCH_JOB]CmcHealthCheckJob - START");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf_file = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = new Date();
        String currentDateStr = sdf.format(currentDate);

        HPFW_Connection hpfwConn = null;
        try {
            Properties prop = cmcEnvProperties.getProperties();
            hpfwConn = HPFW_Connection.getHPFW_Connection();

            String reportContent = REPORT_TEMPLATE_EMAIL_CONTENT.replace("{currentDateStr}", currentDateStr);
            String reportFileContent = REPORT_TEMPLATE_ATTACHMENT.replace("{currentDateStr}", currentDateStr);

            // INFO: 1) number of iAM Smart message per template on previous day
            List<String> numOfIasMsgPerTemplateList = dao.retrieveNumOfIasMsgPerTemplate(hpfwConn);
            String numOfIasMsgPerTemplateReport = "";
            String numOfIasMsgPerTemplateReportNum = "";
            int numOfIasMsgPerTemplateReportCount = 0;
            if (numOfIasMsgPerTemplateList.size() == 0) {
                numOfIasMsgPerTemplateReport = numOfIasMsgPerTemplateReport + "\t0\r\n";
                numOfIasMsgPerTemplateReportNum = numOfIasMsgPerTemplateReport;
            } else {
                for (int i = 0; i < numOfIasMsgPerTemplateList.size(); i++) {
                    String numOfIasMsgPerTemplate = (String) numOfIasMsgPerTemplateList.get(i);
                    numOfIasMsgPerTemplateReport = numOfIasMsgPerTemplateReport + "\t" + numOfIasMsgPerTemplate
                            + "\r\n";
                    logger.info("CmcHealthCheckJob - numOfIasMsgPerTemplate [" + i + "]: " + numOfIasMsgPerTemplate);

                    numOfIasMsgPerTemplateReportCount += getReportRecordCount(numOfIasMsgPerTemplate);
                }
                numOfIasMsgPerTemplateReportNum = numOfIasMsgPerTemplateReportNum + "\t"
                        + numOfIasMsgPerTemplateReportCount + "\r\n";
            }
            reportContent = reportContent.replace("{numOfIasMsgPerTemplate}", "" + numOfIasMsgPerTemplateReportNum);
            reportFileContent = reportFileContent.replace("{numOfIasMsgPerTemplate}",
                    "" + numOfIasMsgPerTemplateReport);

            Calendar cal;
            cal = Calendar.getInstance();
            int mday = cal.get(Calendar.DAY_OF_MONTH);

            // INFO: 2) number of iAM Smart message to be housekept next month
            String numOfIasMessageHouseKeepNextMonth = dao.retrieveNumOfIasMessageHouseKeepNextMonth(hpfwConn);
            reportContent = reportContent.replace("{numOfIasMessageHouseKeepNextMonth}",
                    "" + numOfIasMessageHouseKeepNextMonth);
            reportFileContent = reportFileContent.replace("{numOfIasMessageHouseKeepNextMonth}",
                    "" + numOfIasMessageHouseKeepNextMonth);

            // INFO: 3) number of iAM Smart message issued per service provider in previous month
            // run only on the first day of each month
            if (mday != 1) {
                reportContent = reportContent.replace("{numOfIasMsgIssuedPerSPPreviousMonth}",
                        "\t" + "(Figure will be available on the first day of the month)\r\n");
                reportFileContent = reportFileContent.replace("{numOfIasMsgIssuedPerSPPreviousMonth}",
                        "\t" + "(Figure will be available on the first day of the month)\r\n");
            } else {
                int totalCountOfIasMsgIssuedPerSPPreviousMonth = 0;

                List<String> numOfIasMsgIssuedPerSPPreviousMonthList = dao
                        .retrieveNumOfIasMsgIssuedPerSPPreviousMonth(hpfwConn);
                String numOfIasMsgIssuedPerSPPreviousMonth = "";

                if (numOfIasMsgIssuedPerSPPreviousMonthList.size() == 0) {
                    numOfIasMsgIssuedPerSPPreviousMonth = "0\r\n";
                    reportContent = reportContent.replace("{numOfIasMsgIssuedPerSPPreviousMonth}",
                            "\t" + numOfIasMsgIssuedPerSPPreviousMonth);
                    reportFileContent = reportFileContent.replace("{numOfIasMsgIssuedPerSPPreviousMonth}",
                            "\t" + numOfIasMsgIssuedPerSPPreviousMonth);
                } else {
                    for (int i = 0; i < numOfIasMsgIssuedPerSPPreviousMonthList.size(); i++) {
                        String tempIasMsgIssuedPerSP = (String) numOfIasMsgIssuedPerSPPreviousMonthList.get(i);
                        String[] splitInfo = tempIasMsgIssuedPerSP.split(", ");

                        String tempIasMsgIssuedCountStr = splitInfo[2];
                        totalCountOfIasMsgIssuedPerSPPreviousMonth += Integer.parseInt(tempIasMsgIssuedCountStr);

                        numOfIasMsgIssuedPerSPPreviousMonth += "\t" + tempIasMsgIssuedPerSP + "\r\n";
                        logger.info("CmcHealthCheckJob - tempIasMsgIssuedPerSP [" + i + "]: "
                                + numOfIasMsgIssuedPerSPPreviousMonth);
                    }

                    String totalCountStr = String.valueOf(totalCountOfIasMsgIssuedPerSPPreviousMonth);
                    reportContent = reportContent.replace("{numOfIasMsgIssuedPerSPPreviousMonth}",
                            "\t" + totalCountStr);
                    reportFileContent = reportFileContent.replace("{numOfIasMsgIssuedPerSPPreviousMonth}",
                            "\t" + totalCountStr + "\r\n" + numOfIasMsgIssuedPerSPPreviousMonth);
                }
            }

            // ACTION: 1) number of iAM Smart notification not send success
            int numOfIasSentWithError = dao.retrieveNumOfIasNotiSentWithError(hpfwConn);
            int numOfIasNotiNotSuccess = dao.retrieveNumOfIasNotiNotSendSuccess(hpfwConn);
            logger.info("CmcHealthCheckJob - numOfIasSentWithError: " + numOfIasSentWithError);
            logger.info("CmcHealthCheckJob - numOfIasNotiNotSuccess: " + numOfIasNotiNotSuccess);
            int totalNotSuccess = numOfIasSentWithError + numOfIasNotiNotSuccess;
            reportContent = reportContent.replace("{numOfIasNotiNotSuccess}", "" + totalNotSuccess);
            reportFileContent = reportFileContent.replace("{numOfIasNotiNotSuccess}", "" + totalNotSuccess);

            // ACTION: 2) number of iAM Smart get notification ID not success
            int numOfGetNotiIdNotSuccess = dao.retrieveNumOfGetNotiIdNotSuccess(hpfwConn);
            logger.info("CmcHealthCheckJob - numOfGetNotiIdNotSuccess: " + numOfGetNotiIdNotSuccess);
            reportContent = reportContent.replace("{numOfGetNotiIdNotSuccess}", "" + numOfGetNotiIdNotSuccess);
            reportFileContent = reportFileContent.replace("{numOfGetNotiIdNotSuccess}", "" + numOfGetNotiIdNotSuccess);

            // ACTION: 1) number of iAM Smart Account Status update
            int numOfIasAccStatusUpdate = dao.retrieveNumOfIasAccStatusUpdate(hpfwConn);
            logger.info("CmcHealthCheckJob - numOfIasAccStatusUpdate: " + numOfIasAccStatusUpdate);
            reportContent = reportContent.replace("{numOfIasAccStatusUpdate}", "" + numOfIasAccStatusUpdate);
            reportFileContent = reportFileContent.replace("{numOfIasAccStatusUpdate}", "" + numOfIasAccStatusUpdate);

            String healthCheckReportDir = prop.getProperty("CMC_HEALTH_CHECK_REPORT_DIR");
            String healthCheckReportFileName = prop.getProperty("CMC_HEALTH_CHECK_REPORT_FILE_NAME");
            File file = new File(healthCheckReportDir + "/" + healthCheckReportFileName + "_"
                    + sdf_file.format(currentDate) + ".txt");

            FileUtils.writeStringToFile(file, reportFileContent);

            sendNotificationEmail(prop, reportContent, sdf_file.format(currentDate), file.getAbsolutePath(),
                    reportFileContent);

            logger.info("CmcHealthCheckJob complete without error");
        } catch (Exception e) {
            logger.error("CmcHealthCheckJob exception", e);
            throw e;
        } finally {
            HPFW_Connection.close(hpfwConn);
        }

        logger.info("[BATCH_JOB]CmcHealthCheckJob - END");
    }

    public int extractNumber(String s) {
        String[] tempS = null;
        int tempCounter = 0;
        tempS = s.split(",");
        tempCounter = Integer.parseInt(tempS[tempS.length - 1].trim());
        return tempCounter;
    }

    private void sendNotificationEmail(Properties inProp, String reportContent, String formatCurrentDate,
            String filePath, String reportFileContent) throws Exception {
        Properties prop = new Properties();
        prop.putAll(inProp);

        String emailCharSet = prop.getProperty("CMC_HEALTH_CHECK_EMAIL_CHARSET");
        String emailContentType = prop.getProperty("CMC_HEALTH_CHECK_EMAIL_CONTENT_TYPE");
        String emailSubject = prop.getProperty("CMC_HEALTH_CHECK_EMAIL_SUBJECT");
        String[] emails = prop.getProperty("CMC_HEALTH_CHECK_EMAILS").split(",");

        String healthCheckKeyStoreFileName = prop.getProperty("HEALTH_CHECK_KEY_STORE_FILE_NAME");
        logger.info("sendNotificationEmail:" + healthCheckKeyStoreFileName);
        prop.put("WSS_KEY_STORE_FILE_NAME", healthCheckKeyStoreFileName);
        prop.put("KEY_STORE_FILE_NAME", healthCheckKeyStoreFileName);

        NotificationClient client = new NotificationClient(prop);
        String chanType = "EM";
        String charSet = emailCharSet;
        String contentType = emailContentType;
        String subject = emailSubject + " " + formatCurrentDate;
        String content = reportContent;

        Attachment[] attachmentArray = new Attachment[0];
        if (reportFileContent != null && !reportFileContent.equals("")) {
            logger.info("CmcHealthCheckJob emailContentType:" + emailContentType);
            attachmentArray = new Attachment[2];
            attachmentArray[0] = new Attachment();
            attachmentArray[0].setContentType("application/zip");

            File outFile = null;
            try {
                String password = "ogcio" + new SimpleDateFormat("yyyyMMdd").format(new Date());
                outFile = AesZipFileUtil.zipAndEncryptFile(filePath, password);

                if (outFile != null) {
                    byte[] fileContent = EncoderUtils
                            .BASE64Encode(ReaderUtils.loadFileToByteArray(outFile.getAbsolutePath()));
                    attachmentArray[0].setFileName(outFile.getName());
                    attachmentArray[0].setFileContent(fileContent);
                }

                // String housekeepHealthCheckReportDir = prop.getProperty("CMC_HEALTH_CHECK_REPORT_DIR");
                // String housekeepHealthCheckReportFileName = prop.getProperty("CMC_HOUSEKEEP_HEALTH_CHECK_REPORT_FILE_NAME");
                // byte[] housekeepFileContent = EncoderUtils.BASE64Encode(ReaderUtils.loadFileToByteArray(housekeepHealthCheckReportDir + "/" + housekeepHealthCheckReportFileName + "_" + formatCurrentDate + ".txt"));
                // attachmentArray[1] = new Attachment();
                // attachmentArray[1].setContentType("text/plain");
                // attachmentArray[1].setFileName(housekeepHealthCheckReportFileName + "_" + formatCurrentDate + ".txt");
                // attachmentArray[1].setFileContent(housekeepFileContent);

            } catch (Exception e) {
                logger.error("CmcHealthCheckJob exception", e);
                throw e;
            }

        }

        Recipient[] recipientArray = new Recipient[emails.length];
        for (int i = 0; i < emails.length; i++) {
            recipientArray[i] = new Recipient();
            recipientArray[i].setChanAddr(emails[i]);
            recipientArray[i].setAtthFiles(new Vector<Attachment>());
        }

        SOAPMessage responseMessage = client.sendNotificationRequest(chanType, charSet, contentType,
                subject, content.getBytes("UTF8"), attachmentArray, recipientArray);

        responseMessage.writeTo(System.out);
    }

    private int getReportRecordCount(String reportRecord) {
        int recondCount = 0;
        String[] recordArr = reportRecord.split(",");
        if (recordArr == null || recordArr.length == 0 ||
                recordArr[recordArr.length - 1] == null || recordArr[recordArr.length - 1].trim().length() == 0)
            return 0;
        else {
            try {
                recondCount = Integer.parseInt(recordArr[recordArr.length - 1].trim());
            } catch (NumberFormatException ex) {
                logger.info("Error : Cannot retrieve report record count: " + reportRecord);
            }
        }
        return recondCount;
    }

}
