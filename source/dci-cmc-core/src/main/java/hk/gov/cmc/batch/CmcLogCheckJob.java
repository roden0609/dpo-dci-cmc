package hk.gov.cmc.batch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import hk.gov.cmc.config.CmcEnvProperties;
import hk.gov.cmc.utils.common.AesZipFileUtil;
import hk.gov.gcis.rm.common.utils.encoder.EncoderUtils;
import hk.gov.gcis.rm.common.utils.file.ReaderUtils;
import hk.gov.gcis.ss.noti.client.NotificationClient;
import hk.gov.gcis.ss.noti.datatype.Attachment;
import hk.gov.gcis.ss.noti.datatype.Recipient;
import jakarta.xml.soap.SOAPMessage;

public class CmcLogCheckJob {

    private static Log logger = LogFactory.getLog(CmcLogCheckJob.class);
    private final static CmcEnvProperties cmcEnvProperties = new CmcEnvProperties();

    public void execute(Map<String, Object> params) throws Exception {

        logger.info("[BATCH_JOB]CmcLogCheckJob - START");

        try {

            Properties prop = cmcEnvProperties.getProperties();

            String cmcLogCheckBatchDateString = (String) params.get("cmcLogCheckBatchDate");

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            Date cmcLogCheckBatchDate = sdf.parse(cmcLogCheckBatchDateString);

            String logCheckReportDir = prop.getProperty("CMC_LOG_CHECK_REPORT_DIR");
            String emailCharSet = prop.getProperty("CMC_LOG_CHECK_EMAIL_CHARSET");
            String emailContentType = prop.getProperty("CMC_LOG_CHECK_EMAIL_CONTENT_TYPE");
            String emailSubject = prop.getProperty("CMC_LOG_CHECK_EMAIL_SUBJECT");
            int logCheckAtthSizeLimit = Integer
                    .valueOf((String) prop.getProperty("CMC_LOG_CHECK_EMAIL_ATTH_SIZE_LIMIT"));
            String[] reportContentFilePrefix = prop.getProperty("CMC_LOG_CHECK_EMAIL_CONTENT_FILE_PREFIX")
                    .split(",");
            String[] reportAttachmentFilePrefix = prop.getProperty("CMC_LOG_CHECK_EMAIL_ATTACHMENT_FILE_PREFIX")
                    .split(",");
            String logFileExtension = prop.getProperty("CMC_LOG_CHECK_FILE_EXTENSION");
            String[] emails = prop.getProperty("CMC_LOG_CHECK_EMAILS").split(",");
            String logCheckKeyStoreFileName = prop.getProperty("LOG_CHECK_KEY_STORE_FILE_NAME");
            logger.info("sendNotificationEmail:" + logCheckKeyStoreFileName);
            prop.put("WSS_KEY_STORE_FILE_NAME", logCheckKeyStoreFileName);
            prop.put("KEY_STORE_FILE_NAME", logCheckKeyStoreFileName);

            // initialize Noti client
            NotificationClient client = new NotificationClient(prop);
            String chanType = "EM";
            String charSet = emailCharSet;
            String contentType = emailContentType;
            String subject = emailSubject + " on " + cmcLogCheckBatchDateString;

            // prepare email content
            String content = new String("");
            int numOfReportContent = reportContentFilePrefix.length;
            for (int i = 0; i < numOfReportContent; i++) {
                String contentFileName = logCheckReportDir + "/" + reportContentFilePrefix[i]
                        + cmcLogCheckBatchDateString + logFileExtension;
                logger.info("CmcLogCheckJob add content: " + contentFileName);

                FileReader contentFileReader = null;
                BufferedReader contentBufferReader = null;
                try {
                    File contentFile = new File(contentFileName);
                    // long contentSize = contentFile.length();

                    contentFileReader = new FileReader(contentFile);
                    contentBufferReader = new BufferedReader(contentFileReader);

                    String contentLine;
                    StringBuilder contentStringBuilder = new StringBuilder();
                    while ((contentLine = contentBufferReader.readLine()) != null) {
                        contentStringBuilder.append(contentLine + "\n");
                    }

                    content = content + contentStringBuilder.toString();

                } catch (java.io.FileNotFoundException fne) {
                    logger.warn("CmcLogCheckJob content file not found: " + contentFileName);
                    content = content + "\nContent file " + contentFileName + " is not found.\n";
                } finally {
                    if (contentFileReader != null) {
                        contentFileReader.close();
                    }
                    if (contentBufferReader != null) {
                        contentBufferReader.close();
                    }
                }
            }

            // prepare email attachment
            ArrayList<Attachment> attachList = new ArrayList<Attachment>();
            int numOfReportAttachment = reportAttachmentFilePrefix.length;

            int totalAtthSize = 0;

            for (int i = 0; i < numOfReportAttachment; i++) {
                String attachmentFile = logCheckReportDir + "/" + reportAttachmentFilePrefix[i]
                        + cmcLogCheckBatchDateString + logFileExtension;
                logger.info("CmcLogCheckJob add attachment: " + attachmentFile);

                File zipAttachmentFile = null;
                try {
                    char[] password = ("ogcio" + new SimpleDateFormat("yyyyMMdd").format(cmcLogCheckBatchDate)).toCharArray();

                    try {

                        zipAttachmentFile = AesZipFileUtil.zipAndEncryptFile(attachmentFile, String.valueOf(password));

                        if (zipAttachmentFile != null) {
                            byte[] zipAttachmentFileContent = EncoderUtils
                                    .BASE64Encode(ReaderUtils.loadFileToByteArray(zipAttachmentFile.getAbsolutePath()));

                            Attachment notiAttach = new Attachment();
                            notiAttach = new Attachment();
                            // CMC-BAT-45 - Fix the Log Health Check zip file corrupted issue -- BEGIN
                            // notiAttach.setContentType(emailContentType);
                            notiAttach.setContentType("application/zip");
                            // CMC-BAT-45 - Fix the Log Health Check zip file corrupted issue -- END
                            notiAttach.setFileName(zipAttachmentFile.getName());
                            notiAttach.setFileContent(zipAttachmentFileContent);
                            totalAtthSize += zipAttachmentFile.length();

                            attachList.add(notiAttach);
                        }
                    } catch (java.io.FileNotFoundException fne) {
                        logger.warn("CmcLogCheckJob detail file not found: " + attachmentFile);
                        content = content + "\nDetail file " + attachmentFile + " is not found.\n";
                    }
                } catch (Exception e) {
                    logger.error("CmcLogCheckJob exception", e);
                    throw e;
                }
            }

            Attachment[] attachmentArray = null;

            if (totalAtthSize > logCheckAtthSizeLimit) {
                logger.warn("totalAtthSize [" + totalAtthSize + "] is larger than logCheckAtthSizeLimit ["
                        + logCheckAtthSizeLimit + "]");
                content = content + "Total attachment size [" + totalAtthSize
                        + "] is larger than Noti Attachment Size Limit [" + logCheckAtthSizeLimit + "]\n";

            } else {
                attachmentArray = new Attachment[attachList.size()];
                for (int a = 0; a < attachList.size(); a++) {
                    attachmentArray[a] = (Attachment) attachList.get(a);
                }
            }

            // prepare recipient
            Recipient[] recipientArray = new Recipient[emails.length];
            for (int i = 0; i < emails.length; i++) {
                recipientArray[i] = new Recipient();
                recipientArray[i].setChanAddr(emails[i]);
                recipientArray[i].setAtthFiles(new Vector<Attachment>());
            }

            // send message
            SOAPMessage responseMessage = client.sendNotificationRequest(chanType, charSet, contentType,
                    subject, content.getBytes("UTF8"), attachmentArray, recipientArray);

            responseMessage.writeTo(System.out);

            logger.info("CmcLogCheckJob complete without error");
        } catch (Exception e) {
            logger.error("CmcLogCheckJob exception", e);
            throw e;
        }

        logger.info("[BATCH_JOB]CmcLogCheckJob - END");
    }
}
