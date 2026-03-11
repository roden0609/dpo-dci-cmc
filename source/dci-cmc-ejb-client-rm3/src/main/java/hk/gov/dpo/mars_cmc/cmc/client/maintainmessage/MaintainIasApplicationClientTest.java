package hk.gov.dpo.mars_cmc.cmc.client.maintainmessage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

import hk.gov.dpo.mars_cmc.cmc.datatype.message.Application;
import hk.gov.dpo.mars_cmc.cmc.datatype.message.EMessage;
import hk.gov.dpo.mars_cmc.cmc.datatype.message.MessageRequest;
import hk.gov.dpo.mars_cmc.cmc.datatype.message.MetaData;
import hk.gov.dpo.mars_cmc.cmc.datatype.message.Recipient;
import hk.gov.dpo.mars_cmc.cmc.datatype.message.ToDoItem;
import hk.gov.dpo.mars_cmc.cmc.jaxb.maintainmessage.MaintainMessageResponse;
import hk.gov.gcis.ss.common.utils.DatetimeUtils;
import hk.gov.gcis.ss.common.utils.PropertiesUtils;
import hk.gov.gcis.ss.messaging.jaxb.asynmsg.ScopesMessagingAcknowledgement;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.soap.SOAPMessage;

public class MaintainIasApplicationClientTest {

    private static final String CORR_ID_FILE = "tempCorrId.txt";
    private static final String RESPONSE_FILE = "responseMessage.txt";

    public MaintainIasApplicationClientTest() {
    }

    public static void main(String[] args) {

        try {
            if (args.length != 1) {
                System.out.println(
                        "Usage: java " + MaintainIasApplicationClientTest.class.getName() + " <property file> ");
                System.exit(-1);
            }

            int numberOfTestCase = 1;
            System.out.println("numberOfTestCase: " + numberOfTestCase);
            for (int i = 1; i <= numberOfTestCase; i++) {
                String propFilePath = args[0] + "_" + String.format("%02d", i) + ".properties";
                System.out.println(i + ", propFilePath: " + propFilePath);

                Properties properties = PropertiesUtils.loadPropertiesFile(propFilePath);

                MaintainIasApplicationClientTest maintainIasApplicationClientTest = new MaintainIasApplicationClientTest();

                maintainIasApplicationClientTest.test(properties);
            }

        } catch (Throwable t) {
            t.printStackTrace(System.err);
        }

    }

    private void test(Properties properties) throws Exception {
        processMsgByAsynMode(properties);
    }

    private String getEgisCorrelationId(BufferedReader input) throws Exception {
        System.out.println();
        System.out.print("Please input the Correlation ID: ");
        return input.readLine().trim();
    }

    private String getMessageId(BufferedReader input) throws Exception {
        System.out.println();
        System.out.print("Please input the message ID of response: ");
        return input.readLine().trim();
    }

    private int getBatchCount(BufferedReader input) throws Exception {
        System.out.println();
        System.out.print("Please input the number of batches: ");
        return Integer.parseInt(input.readLine().trim());
    }

    private int getRequestCount(BufferedReader input) throws Exception {
        System.out.println();
        System.out.print("Please input the number of requests in one batch: ");
        return Integer.parseInt(input.readLine().trim());
    }

    private void processMsgByRpcMode(Properties properties) throws Exception {

        MessageRequest[] messageRequestArray = getMsgReqArrayByProperties(properties);

        // Create request

        MaintainMessageClient maintainMessageClient = new MaintainMessageClient(properties);

        SOAPMessage responseMsg = maintainMessageClient.sendMaintainMessageRequest(messageRequestArray);

        responseMsg.writeTo(System.out);
        System.out.println();

        MaintainMessageResponse maintainMessageResponse = maintainMessageClient.getMaintainMessageResponse(responseMsg);

        System.out.println("ResultCd=" + maintainMessageResponse.getResultCode());
        System.out.println("ResultMessage=" + maintainMessageResponse.getResultMessage());

    }

    private void processMsgByAsynMode(Properties properties) throws Exception {

        MessageRequest[] messageRequestArray = getMsgReqArrayByProperties(properties);

        // Create request
        MaintainMessageClient maintainMessageClient = new MaintainMessageClient(properties);

        ScopesMessagingAcknowledgement asyncResponse = maintainMessageClient
                .sendAysnMaintainMessageRequest(messageRequestArray);

        System.out.println("ScopesMessagingAcknowledgement correlation ID=" + asyncResponse.getCorrId());

        System.out.println("\nResponseMessage=\n");
        JAXBContext jaxbContext = JAXBContext.newInstance(ScopesMessagingAcknowledgement.class);
        jaxbContext.createMarshaller().marshal(asyncResponse, new PrintWriter(System.out, true));
        System.out.println();

    }

    private void processMsgByAsynModeLoadTest(Properties properties, int batchCount, int requestCount)
            throws Exception {
        for (int i = 0; i < batchCount; i++) {

            MessageRequest[] messageRequestArray = getMsgReqArrayByPropertiesLoadTest(properties, requestCount);

            // Create request

            MaintainMessageClient maintainMessageClient = new MaintainMessageClient(properties);

            ScopesMessagingAcknowledgement asyncResponse = maintainMessageClient
                    .sendAysnMaintainMessageRequest(messageRequestArray);

            System.out.println("ScopesMessagingAcknowledgement correlation ID=" + asyncResponse.getCorrId());

            FileWriter fileWriter = new FileWriter(CORR_ID_FILE, true);
            fileWriter.write(asyncResponse.getCorrId() + "\r\n");
            fileWriter.flush();
            fileWriter.close();

            System.out.println("\nResponseMessage=\n");
            JAXBContext jaxbContext = JAXBContext.newInstance(ScopesMessagingAcknowledgement.class);
            jaxbContext.createMarshaller().marshal(asyncResponse, new PrintWriter(System.out, true));
            System.out.println();

            Thread.sleep(2000);
        }

    }

    private void retrieveResponseBySinglePull(Properties properties, String egisCorrelationId) throws Exception {

        MaintainMessageClient maintainMessageClient = new MaintainMessageClient(properties);

        SOAPMessage singlePullResponse = maintainMessageClient.retrieveAsynResponseBySinglePull(egisCorrelationId);

        singlePullResponse.writeTo(System.out);
        System.out.println();

        String rspScopesMsgId = maintainMessageClient.getMessageIdFromSinglePullResponse(singlePullResponse);

        System.out.println("Single Pull Response: message_id=" + rspScopesMsgId);

        if (rspScopesMsgId != null && rspScopesMsgId.length() > 0) {

            MaintainMessageResponse maintainMessageResponse = maintainMessageClient
                    .getMaintainMessageResponse(singlePullResponse);

            if (maintainMessageResponse != null) {
                System.out.println("ResultCd=" + maintainMessageResponse.getResultCode());
                System.out.println("ResultMessage=" + maintainMessageResponse.getResultMessage());

                System.out.println("\nResponseMessage=\n");
                JAXBContext context = JAXBContext.newInstance(MaintainMessageResponse.class);
                Marshaller marshaller = context.createMarshaller();
                marshaller.marshal(maintainMessageResponse, new PrintWriter(System.out, true));
                System.out.println();
            }
        }
    }

    private void retrieveResponseBySinglePullLoadTest(Properties properties) throws Exception {

        MaintainMessageClient maintainMessageClient = new MaintainMessageClient(properties);

        BufferedReader buf = new BufferedReader(new FileReader(CORR_ID_FILE));
        FileOutputStream fos = new FileOutputStream(RESPONSE_FILE);
        PrintWriter printWriter = new PrintWriter(fos);

        String corrId = null;
        while ((corrId = buf.readLine()) != null) {

            SOAPMessage singlePullResponse = maintainMessageClient.retrieveAsynResponseBySinglePull(corrId);

            singlePullResponse.writeTo(fos);
            fos.flush();
            printWriter.println();

            String rspScopesMsgId = maintainMessageClient.getMessageIdFromSinglePullResponse(singlePullResponse);

            printWriter.println("Single Pull Response: message_id=" + rspScopesMsgId);
            if (rspScopesMsgId != null && rspScopesMsgId.length() > 0) {

                MaintainMessageResponse maintainMessageResponse = maintainMessageClient
                        .getMaintainMessageResponse(singlePullResponse);
                if (maintainMessageResponse != null) {
                    // System.out.println("ResultCd="+maintainMessageResponse.getResultCode());
                    // System.out.println("ResultMessage="+maintainMessageResponse.getResultMessage());
                    // System.out.println("\nResponseMessage=\n");
                    JAXBContext context = JAXBContext.newInstance(MaintainMessageResponse.class);
                    Marshaller marshaller = context.createMarshaller();
                    marshaller.marshal(maintainMessageResponse, printWriter);
                    printWriter.println();
                }
                SOAPMessage pullAckResponse = maintainMessageClient.sendPullAckRequest(rspScopesMsgId);
                if (pullAckResponse != null) {
                    printWriter.println("pullAckResponse=");
                    printWriter.flush();
                    pullAckResponse.writeTo(fos);
                    fos.flush();
                    printWriter.println();
                    printWriter.flush();
                }
            }
        }
        buf.close();
        printWriter.close();
        fos.close();

        (new File(CORR_ID_FILE)).delete();

    }

    private void sendPullAckRequest(Properties properties, String rspScopesMsgId) throws Exception {

        MaintainMessageClient maintainMessageClient = new MaintainMessageClient(properties);

        SOAPMessage pullAckResponse = maintainMessageClient.sendPullAckRequest(rspScopesMsgId);

        if (pullAckResponse != null) {
            System.out.println("pullAckResponse=\n");
            pullAckResponse.writeTo(System.out);
            System.out.println();
        }
    }

    private MessageRequest[] getMsgReqArrayByProperties(Properties properties) throws Exception {

        int msgReqCount = Integer.parseInt(properties.getProperty("MSG_REQ_COUNT"));

        MessageRequest[] messageRequestArray = new MessageRequest[msgReqCount];

        for (int m = 0; m < msgReqCount; m++) {

            messageRequestArray[m] = new MessageRequest();

            String portalId = properties.getProperty("MSG_REQ_" + (m + 1) + "_PORTAL_ID");

            messageRequestArray[m].setPortalId(portalId);

            String eMsgTemlId = properties.getProperty("MSG_REQ_" + (m + 1) + "_EMSG_TEMPLATE_ID");
            String eMsgTemlVer = properties.getProperty("MSG_REQ_" + (m + 1) + "_EMSG_TEMPLATE_VERSION");

            String toDoItemTemlId = properties.getProperty("MSG_REQ_" + (m + 1) + "_ITEM_TEMPLATE_ID");
            String toDoItemTemlVer = properties.getProperty("MSG_REQ_" + (m + 1) + "_ITEM_TEMPLATE_VERSION");

            String applicationTemlId = properties.getProperty("MSG_REQ_" + (m + 1) + "_APPLICATION_TEMPLATE_ID");
            String applicationTemlVer = properties.getProperty("MSG_REQ_" + (m + 1) + "_APPLICATION_TEMPLATE_VERSION");

            EMessage eMessage = null;
            if (eMsgTemlId != null && eMsgTemlId.length() > 0) {
                eMessage = new EMessage();
                eMessage.setTemplateID(eMsgTemlId);
                eMessage.setTemplateVersion(eMsgTemlVer);
            }

            ToDoItem toDoItem = null;
            if (toDoItemTemlId != null && toDoItemTemlId.length() > 0) {
                toDoItem = new ToDoItem();
                toDoItem.setTemplateID(toDoItemTemlId);
                toDoItem.setTemplateVersion(toDoItemTemlVer);
            }

            Application application = null;
            if (applicationTemlId != null && applicationTemlId.length() > 0) {
                application = new Application();
                application.setTemplateID(applicationTemlId);
                application.setTemplateVersion(applicationTemlVer);
            }

            int numOfMetaData = Integer.parseInt(properties.getProperty("MSG_REQ_" + (m + 1) + "_META_DATA_COUNT"));

            MetaData[] metaDataArray = new MetaData[numOfMetaData];

            System.out.println("numOfMetaData=" + numOfMetaData);

            for (int i = 0; i < numOfMetaData; i++) {

                metaDataArray[i] = new MetaData();

                String filePathEN = properties
                        .getProperty("MSG_REQ_" + (m + 1) + "_META_DATA_" + (i + 1) + "_CONTENT_EN_FILE_PATH");
                String filePathTC = properties
                        .getProperty("MSG_REQ_" + (m + 1) + "_META_DATA_" + (i + 1) + "_CONTENT_TC_FILE_PATH");
                String filePathSC = properties
                        .getProperty("MSG_REQ_" + (m + 1) + "_META_DATA_" + (i + 1) + "_CONTENT_SC_FILE_PATH");

                String fileNameEN = "";
                if (filePathEN.length() > 0) {
                    fileNameEN = filePathEN.substring(filePathEN.lastIndexOf("\\") + 1, filePathEN.length());
                    // byte[] fileContentEN = ReaderUtils.loadFileToByteArray(filePathEN);
                    byte[] fileContentEN = Files.readAllBytes(Paths.get(filePathEN));
                    metaDataArray[i].setDataContentEN(removeBom(new String(fileContentEN, "UTF8")));
                }

                String fileNameTC = "";
                if (filePathTC.length() > 0) {
                    fileNameTC = filePathTC.substring(filePathTC.lastIndexOf("\\") + 1, filePathTC.length());
                    // byte[] fileContentTC = ReaderUtils.loadFileToByteArray(filePathTC);
                    byte[] fileContentTC = Files.readAllBytes(Paths.get(filePathTC));
                    metaDataArray[i].setDataContentTC(removeBom(new String(fileContentTC, "UTF8")));
                }

                String fileNameSC = "";
                if (filePathSC.length() > 0) {
                    fileNameSC = filePathSC.substring(filePathSC.lastIndexOf("\\") + 1, filePathSC.length());
                    // byte[] fileContentSC = ReaderUtils.loadFileToByteArray(filePathSC);
                    byte[] fileContentSC = Files.readAllBytes(Paths.get(filePathSC));
                    metaDataArray[i].setDataContentSC(removeBom(new String(fileContentSC, "UTF8")));
                }

                int numOfRecipient = Integer.parseInt(
                        properties.getProperty("MSG_REQ_" + (m + 1) + "_META_DATA_" + (i + 1) + "_REPT_COUNT"));

                System.out.println("numOfRecipient=" + numOfRecipient);

                for (int j = 0; j < numOfRecipient; j++) {

                    Recipient recipient = new Recipient();

                    String tranID = properties.getProperty(
                            "MSG_REQ_" + (m + 1) + "_META_DATA_" + (i + 1) + "_REPT_" + (j + 1) + "_TRAN_ID");
                    String idpID = properties.getProperty(
                            "MSG_REQ_" + (m + 1) + "_META_DATA_" + (i + 1) + "_REPT_" + (j + 1) + "_IDP_ID");
                    String recipientID = properties.getProperty(
                            "MSG_REQ_" + (m + 1) + "_META_DATA_" + (i + 1) + "_REPT_" + (j + 1) + "_RECIPIENT_ID");
                    String itemDateStr = properties.getProperty(
                            "MSG_REQ_" + (m + 1) + "_META_DATA_" + (i + 1) + "_REPT_" + (j + 1) + "_ITEM_DATE");
                    String action = properties.getProperty(
                            "MSG_REQ_" + (m + 1) + "_META_DATA_" + (i + 1) + "_REPT_" + (j + 1) + "_ACTION");
                    String correlatedTranID = properties.getProperty(
                            "MSG_REQ_" + (m + 1) + "_META_DATA_" + (i + 1) + "_REPT_" + (j + 1) + "_CORR_TRAN_ID");
                    String appRefNum = properties.getProperty(
                            "MSG_REQ_" + (m + 1) + "_META_DATA_" + (i + 1) + "_REPT_" + (j + 1) + "_APP_REF_NUM");
                    String appStatus = properties.getProperty(
                            "MSG_REQ_" + (m + 1) + "_META_DATA_" + (i + 1) + "_REPT_" + (j + 1) + "_APP_STATUS");
                    String contactNum = properties.getProperty(
                            "MSG_REQ_" + (m + 1) + "_META_DATA_" + (i + 1) + "_REPT_" + (j + 1) + "_CONTACT_NUM");
                    String contactEmail = properties.getProperty(
                            "MSG_REQ_" + (m + 1) + "_META_DATA_" + (i + 1) + "_REPT_" + (j + 1) + "_CONTACT_EMAIL");
                    String miscInfo = properties.getProperty(
                            "MSG_REQ_" + (m + 1) + "_META_DATA_" + (i + 1) + "_REPT_" + (j + 1) + "_MISC_INFO");
                    String recipientIDType = properties.getProperty(
                            "MSG_REQ_" + (m + 1) + "_META_DATA_" + (i + 1) + "_REPT_" + (j + 1) + "_RECIPIENT_ID_TYPE");
                    String appStatusUpdateDate = properties.getProperty("MSG_REQ_" + (m + 1) + "_META_DATA_" + (i + 1)
                            + "_REPT_" + (j + 1) + "_APP_STATUS_UPDATE_DATE");

                    recipient.setTranID(tranID);
                    recipient.setIdpID(idpID);
                    recipient.setRecipientID(recipientID);
                    if (itemDateStr != null && itemDateStr.length() > 0) {
                        if (DatetimeUtils.isValidDateTime(itemDateStr))
                            recipient.setItemDate(DatetimeUtils.convertToDate(itemDateStr));
                        else
                            throw new Exception("MSG_REQ_" + (m + 1) + "_META_DATA_" + (i + 1) + "_REPT_" + (j + 1)
                                    + "_ITEM_DATE is not valid");
                    }

                    recipient.setRecipientIDType(recipientIDType);
                    recipient.setAction(action);
                    recipient.setCorrelatedTranID(correlatedTranID);
                    recipient.setAppRefNum(appRefNum);
                    recipient.setAppStatus(appStatus);
                    if (appStatusUpdateDate != null && appStatusUpdateDate.length() > 0) {
                        if (DatetimeUtils.isValidDateTime(appStatusUpdateDate))
                            recipient.setAppStatusUpdateDate(DatetimeUtils.convertToDate(appStatusUpdateDate));
                        else
                            throw new Exception("MSG_REQ_" + (m + 1) + "_META_DATA_" + (i + 1) + "_REPT_" + (j + 1)
                                    + "_APP_STATUS_UPDATE_DATE is not valid");
                    }
                    recipient.setContactNum(contactNum);
                    recipient.setContactEmail(contactEmail);
                    recipient.setMiscInfo(miscInfo);
                    metaDataArray[i].addRecipient(recipient);
                }
            }

            messageRequestArray[m].setEMessage(eMessage);

            messageRequestArray[m].setToDoItem(toDoItem);

            messageRequestArray[m].setApplication(application);

            for (int i = 0; i < metaDataArray.length; i++) {
                messageRequestArray[m].addMetaData(metaDataArray[i]);
            }
        }

        return messageRequestArray;
    }

    private MessageRequest[] getMsgReqArrayByPropertiesLoadTest(Properties properties, int requestCount)
            throws Exception {

        int msgReqCount = requestCount;

        MessageRequest[] messageRequestArray = new MessageRequest[msgReqCount];

        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String tranIdPrefix = dateFormatter.format(Calendar.getInstance().getTime());

        DecimalFormat format3Digit = new DecimalFormat("000");
        DecimalFormat format2Digit = new DecimalFormat("00");

        for (int m = 0; m < msgReqCount; m++) {

            messageRequestArray[m] = new MessageRequest();

            String portalId = properties.getProperty("MSG_REQ_1_PORTAL_ID");

            messageRequestArray[m].setPortalId(portalId);

            String eMsgTemlId = properties.getProperty("MSG_REQ_1_EMSG_TEMPLATE_ID");
            String eMsgTemlVer = properties.getProperty("MSG_REQ_1_EMSG_TEMPLATE_VERSION");

            String toDoItemTemlId = properties.getProperty("MSG_REQ_1_ITEM_TEMPLATE_ID");
            String toDoItemTemlVer = properties.getProperty("MSG_REQ_1_ITEM_TEMPLATE_VERSION");

            String applicationTemlId = properties.getProperty("MSG_REQ_" + (m + 1) + "_APPLICATION_TEMPLATE_ID");
            String applicationTemlVer = properties.getProperty("MSG_REQ_" + (m + 1) + "_APPLICATION_TEMPLATE_VERSION");

            EMessage eMessage = null;
            if (eMsgTemlId != null && eMsgTemlId.length() > 0) {
                eMessage = new EMessage();
                eMessage.setTemplateID(eMsgTemlId);
                eMessage.setTemplateVersion(eMsgTemlVer);
            }

            ToDoItem toDoItem = null;
            if (toDoItemTemlId != null && toDoItemTemlId.length() > 0) {
                toDoItem = new ToDoItem();
                toDoItem.setTemplateID(toDoItemTemlId);
                toDoItem.setTemplateVersion(toDoItemTemlVer);
            }

            Application application = null;
            if (applicationTemlId != null && applicationTemlId.length() > 0) {
                application = new Application();
                application.setTemplateID(applicationTemlId);
                application.setTemplateVersion(applicationTemlVer);
            }

            int numOfMetaData = Integer.parseInt(properties.getProperty("MSG_REQ_1_META_DATA_COUNT"));

            MetaData[] metaDataArray = new MetaData[numOfMetaData];

            // System.out.println("numOfMetaData=" + numOfMetaData);

            for (int i = 0; i < numOfMetaData; i++) {

                metaDataArray[i] = new MetaData();

                String filePathEN = properties.getProperty("MSG_REQ_1_META_DATA_" + (i + 1) + "_CONTENT_EN_FILE_PATH");
                String filePathTC = properties.getProperty("MSG_REQ_1_META_DATA_" + (i + 1) + "_CONTENT_TC_FILE_PATH");
                String filePathSC = properties.getProperty("MSG_REQ_1_META_DATA_" + (i + 1) + "_CONTENT_SC_FILE_PATH");

                String fileNameEN = "";
                if (filePathEN.length() > 0) {
                    fileNameEN = filePathEN.substring(filePathEN.lastIndexOf("\\") + 1, filePathEN.length());
                    byte[] fileContentEN = Files.readAllBytes(Paths.get(filePathEN));
                    metaDataArray[i].setDataContentEN(removeBom(new String(fileContentEN, "UTF8")));
                }

                String fileNameTC = "";
                if (filePathTC.length() > 0) {
                    fileNameTC = filePathTC.substring(filePathTC.lastIndexOf("\\") + 1, filePathTC.length());
                    byte[] fileContentTC = Files.readAllBytes(Paths.get(filePathTC));
                    metaDataArray[i].setDataContentTC(removeBom(new String(fileContentTC, "UTF8")));
                }

                String fileNameSC = "";
                if (filePathSC.length() > 0) {
                    fileNameSC = filePathSC.substring(filePathSC.lastIndexOf("\\") + 1, filePathSC.length());
                    byte[] fileContentSC = Files.readAllBytes(Paths.get(filePathSC));
                    metaDataArray[i].setDataContentSC(removeBom(new String(fileContentSC, "UTF8")));
                }

                int numOfRecipient = Integer
                        .parseInt(properties.getProperty("MSG_REQ_1_META_DATA_" + (i + 1) + "_REPT_COUNT"));

                // System.out.println("numOfRecipient=" + numOfRecipient);

                for (int j = 0; j < numOfRecipient; j++) {

                    Recipient recipient = new Recipient();

                    // Tran ID format: "YYYYMMDDHHMISS(m+1)j"
                    String tranID = tranIdPrefix + format3Digit.format(m + 1) + format2Digit.format(j);
                    String idpID = properties
                            .getProperty("MSG_REQ_1_META_DATA_" + (i + 1) + "_REPT_" + (j + 1) + "_IDP_ID");
                    String recipientID = properties
                            .getProperty("MSG_REQ_1_META_DATA_" + (i + 1) + "_REPT_" + (j + 1) + "_RECIPIENT_ID");
                    String itemDateStr = properties
                            .getProperty("MSG_REQ_1_META_DATA_" + (i + 1) + "_REPT_" + (j + 1) + "_ITEM_DATE");
                    String action = properties
                            .getProperty("MSG_REQ_1_META_DATA_" + (i + 1) + "_REPT_" + (j + 1) + "_ACTION");
                    String correlatedTranID = properties
                            .getProperty("MSG_REQ_1_META_DATA_" + (i + 1) + "_REPT_" + (j + 1) + "_CORR_TRAN_ID");
                    String appRefNum = properties
                            .getProperty("MSG_REQ_1_META_DATA_" + (i + 1) + "_REPT_" + (j + 1) + "_APP_REF_NUM");
                    String appStatus = properties
                            .getProperty("MSG_REQ_1_META_DATA_" + (i + 1) + "_REPT_" + (j + 1) + "_APP_STATUS");
                    String appStatusUpdateDate = properties.getProperty(
                            "MSG_REQ_1_META_DATA_" + (i + 1) + "_REPT_" + (j + 1) + "_APP_STATUS_UPDATE_DATE");
                    String recipientIDType = properties
                            .getProperty("MSG_REQ_1_META_DATA_" + (i + 1) + "_REPT_" + (j + 1) + "_RECIPIENT_ID_TYPE");
                    String contactNum = properties
                            .getProperty("MSG_REQ_1_META_DATA_" + (i + 1) + "_REPT_" + (j + 1) + "_CONTACT_NUM");
                    String contactEmail = properties
                            .getProperty("MSG_REQ_1_META_DATA_" + (i + 1) + "_REPT_" + (j + 1) + "_CONTACT_EMAIL");

                    recipient.setTranID(tranID);
                    recipient.setIdpID(idpID);
                    recipient.setRecipientID(recipientID);
                    if (itemDateStr != null && itemDateStr.length() > 0) {
                        if (DatetimeUtils.isValidDateTime(itemDateStr))
                            recipient.setItemDate(DatetimeUtils.convertToDate(itemDateStr));
                        else
                            throw new Exception(
                                    "MSG_REQ_1_META_DATA_" + (i + 1) + "_REPT_" + (j + 1) + "_ITEM_DATE is not valid");
                    }

                    recipient.setAction(action);
                    recipient.setCorrelatedTranID(correlatedTranID);

                    recipient.setRecipientIDType(recipientIDType);
                    recipient.setAppRefNum(appRefNum);
                    recipient.setAppStatus(appStatus);
                    if (appStatusUpdateDate != null && appStatusUpdateDate.length() > 0) {
                        if (DatetimeUtils.isValidDateTime(appStatusUpdateDate))
                            recipient.setAppStatusUpdateDate(DatetimeUtils.convertToDate(appStatusUpdateDate));
                        else
                            throw new Exception("MSG_REQ_1_META_DATA_" + (i + 1) + "_REPT_" + (j + 1)
                                    + "_APP_STATUS_UPDATE_DATE is not valid");
                    }
                    recipient.setContactNum(contactNum);
                    recipient.setContactEmail(contactEmail);

                    metaDataArray[i].addRecipient(recipient);
                }
            }

            messageRequestArray[m].setEMessage(eMessage);

            messageRequestArray[m].setToDoItem(toDoItem);

            messageRequestArray[m].setApplication(application);

            for (int i = 0; i < metaDataArray.length; i++) {
                messageRequestArray[m].addMetaData(metaDataArray[i]);
            }
        }

        return messageRequestArray;
    }

    private String removeBom(String in) {

        String resultStr = in;

        if (in != null && in.length() > 0) {
            Character c = in.charAt(0);
            if (c == '\ufeff') {
                resultStr = in.substring(1, in.length());
            }
        }

        return resultStr;
    }

}