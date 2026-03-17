package hk.gov.dpo.mars_cmc.cmc.client.maintainmessage;

import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import hk.gov.dpo.mars_cmc.cmc.datatype.message.Application;
import hk.gov.dpo.mars_cmc.cmc.datatype.message.EMessage;
import hk.gov.dpo.mars_cmc.cmc.datatype.message.MessageRequest;
import hk.gov.dpo.mars_cmc.cmc.datatype.message.MetaData;
import hk.gov.dpo.mars_cmc.cmc.datatype.message.Recipient;
import hk.gov.dpo.mars_cmc.cmc.datatype.message.ToDoItem;
import hk.gov.gcis.ss.common.utils.DatetimeUtils;
import hk.gov.gcis.ss.common.utils.PropertiesUtils;
import hk.gov.gcis.ss.messaging.jaxb.asynmsg.ScopesMessagingAcknowledgement;
import jakarta.xml.bind.JAXBContext;

public class MaintainIasMessageClientTest {

    public MaintainIasMessageClientTest() {
    }

    public static void main(String[] args) {

        try {
            if (args.length != 1) {
                System.out.println("Usage: java " + MaintainIasMessageClientTest.class.getName() + " <property file> ");
                System.exit(-1);
            }

            int numberOfTestCase = 1;
            System.out.println("numberOfTestCase: " + numberOfTestCase);
            for (int i = 1; i <= numberOfTestCase; i++) {
                String propFilePath = args[0] + "_" + String.format("%02d", i) + ".properties";
                System.out.println(i + ", propFilePath: " + propFilePath);

                Properties properties = PropertiesUtils.loadPropertiesFile(propFilePath);

                MaintainIasMessageClientTest maintainIasMessageClientTest = new MaintainIasMessageClientTest();

                maintainIasMessageClientTest.test(properties);
            }

        } catch (Throwable t) {
            t.printStackTrace(System.err);
        }

    }

    private void test(Properties properties) throws Exception {
        processMsgByAsynMode(properties);
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
                    recipient.setRecipientIDType(recipientIDType);
                    recipient.setRecipientID(recipientID);
                    if (itemDateStr != null && itemDateStr.length() > 0) {
                        if (DatetimeUtils.isValidDateTime(itemDateStr))
                            recipient.setItemDate(DatetimeUtils.convertToDate(itemDateStr));
                        else
                            throw new Exception("MSG_REQ_" + (m + 1) + "_META_DATA_" + (i + 1) + "_REPT_" + (j + 1)
                                    + "_ITEM_DATE is not valid");
                    }
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