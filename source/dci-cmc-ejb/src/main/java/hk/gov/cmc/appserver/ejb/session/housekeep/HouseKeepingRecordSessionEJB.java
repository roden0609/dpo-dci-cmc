package hk.gov.cmc.appserver.ejb.session.housekeep;

import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Properties;

import org.bouncycastle.asn1.ocsp.ServiceLocator;

import hk.gov.cmc.model.maintainmessage.application.IasUserApplication;
import hk.gov.cmc.model.maintainmessage.emessage.IasUserMsg;
import hk.gov.cmc.model.maintainmessage.todoitem.IasUserToDoItem;
import hk.gov.cmc.persistence.connection.hpfw.HPFW_Connection;
import hk.gov.gcis.rm.common.javaee.ejb.EJBBase;
import jakarta.ejb.EJBException;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionManagement;
import jakarta.ejb.TransactionManagementType;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class HouseKeepingRecordSessionEJB extends EJBBase
        implements IHouseKeepingRecordSessionBMLocal, IHouseKeepingRecordSessionBMRemote {

    public void markHouseKeepIndicator(String inputDate) throws EJBException {
        logInfo("[BATCH_JOB][HouseKeepingRecord]markHouseKeepIndicator - START " + inputDate);

        HPFW_Connection conn = null;
        // JobControlUtils jobControlUtils = null;
        // String jobControlName = "";
        // boolean releaseLock = true;

        int delMsgMonthLimit = 9999;
        int markTrashedDelMymsgMonthLimit = 9999;
        int batchSize = 9999;
        int row = 0;
        int updateCont = 0;

        // MyGov6-C1-002 Enhance Maintain Message module to support iAM Smart message -- START
        int iasMsgHousekeepRetentionMonths = 9999;
        // MyGov6-C1-002 Enhance Maintain Message module to support iAM Smart message -- END

        // CMC-2024-007 - Enhance Maintain Message Module to Support iAM Smart To-Do Item - BEGIN
        int iasToDoItemHousekeepRetentionMonths = 9999;
        // CMC-2024-007 - Enhance Maintain Message Module to Support iAM Smart To-Do Item - END

        // CMC-2024-010: Enhance CMC Client To Retrieve iAM Smart Application Status - BEGIN
        int iasApplicationHousekeepRetentionMonths = 9999;
        // CMC-2024-010: Enhance CMC Client To Retrieve iAM Smart Application Status - END

        try {
            conn = HPFW_Connection.getHPFW_Connection(false);
            conn.begin(null, new Timestamp(Calendar.getInstance().getTime().getTime()),
                    HPFW_Connection.DIRECT_WITH_HISTORY);
            conn.setAutoCommit(false);

            Properties properties = ServiceLocator.getInstance(null).getProperties();

            delMsgMonthLimit = Integer.parseInt(properties.getProperty("HOUSE_KEEP_DEL_MSG_CREATED_MONTH_LIMIT"));

            // MyGov6-C1-002 Enhance Maintain Message module to support iAM Smart message -- START
            iasMsgHousekeepRetentionMonths = Integer
                    .parseInt(properties.getProperty(AppPropertyName.IAS_MSG_HOUSEKEEP_RETENTION_MONTHS_PROPERTY_NAME));
            // MyGov6-C1-002 Enhance Maintain Message module to support iAM Smart message -- END

            // CMC-2024-007 - Enhance Maintain Message Module to Support iAM Smart To-Do Item - BEGIN
            iasToDoItemHousekeepRetentionMonths = Integer.parseInt(
                    properties.getProperty(AppPropertyName.IAS_TODO_ITEM_HOUSEKEEP_RETENTION_MONTHS_PROPERTY_NAME));
            // CMC-2024-007 - Enhance Maintain Message Module to Support iAM Smart To-Do Item - END

            // CMC-2024-010: Enhance CMC Client To Retrieve iAM Smart Application Status - BEGIN
            iasApplicationHousekeepRetentionMonths = Integer.parseInt(
                    properties.getProperty(AppPropertyName.IAS_APPLICATION_HOUSEKEEP_RETENTION_MONTHS_PROPERTY_NAME));
            // CMC-2024-010: Enhance CMC Client To Retrieve iAM Smart Application Status - END

            // MyGov5-CMC-S012: House keeping trash message and working table -- START
            markTrashedDelMymsgMonthLimit = Integer
                    .parseInt(properties.getProperty(AppPropertyName.MARK_TRASHED_DEL_MYMSG_MONTH_LIMIT_PROPERTY_NAME));
            // MyGov5-CMC-S012: House keeping trash message and working table -- END

            batchSize = Integer.parseInt(properties.getProperty("HOUSE_KEEP_DEL_HIST_TBL_BATCH_SIZE"));
            // jobControlName = properties.getProperty("JOB_CONTROL_MARK_HOUSE_KEEP_IND");

            SimpleDateFormat dateChkFomatter = new SimpleDateFormat("yyyy-MM-dd");
            Calendar inputDateCalendar = Calendar.getInstance();
            inputDateCalendar.setTime(dateChkFomatter.parse(inputDate));
            inputDateCalendar.add(Calendar.MONTH, +delMsgMonthLimit);
            if (inputDateCalendar.getTime().after(new Date())) {
                logInfo("[BATCH_JOB][HouseKeepingRecord]markHouseKeepIndicator - InputDate should be before or equal to two years ago!");
                return;
            }

            // jobControlUtils = new JobControlUtils();
            // // Concurrent control
            // if (jobControlUtils.lockJobControl(jobControlName))
            // {
            // releaseLock = false;

            HouseKeepingRecordDAO dao = new HouseKeepingRecordDAO();

            // Part 1
            row = 1;
            updateCont = 0;
            while (row > 0) {
                row = dao.updateCmcUserMessageByBatch(conn, inputDate, batchSize);
                conn.commit();
                updateCont += row;
                if (row < batchSize) {
                    break;
                }
            }
            logInfo("[markHouseKeep] - Update " + updateCont + " record(s) in CMC_USER_MESSAGE");
            logInfo("[markHouseKeep] CMC_USER_MESSAGE END");

            // MyGov5-CMC-S012: House keeping trash message and working table -- START
            // Part 1.5
            row = 1;
            updateCont = 0;
            while (row > 0) {
                row = dao.updateTrashCmcUserMessageByBatch(conn, markTrashedDelMymsgMonthLimit, batchSize);
                conn.commit();
                updateCont += row;
                if (row < batchSize) {
                    break;
                }
            }
            logInfo("[markHouseKeep] - Update " + updateCont + " trash record(s) in CMC_USER_MESSAGE");
            logInfo("[markHouseKeep] CMC_USER_MESSAGE TRASH END");
            // MyGov5-CMC-S012: House keeping trash message and working table -- END

            // Part 2
            row = 1;
            updateCont = 0;
            while (row > 0) {
                row = dao.updateCmcUserToDoItemByBatch(conn, inputDate, batchSize);
                conn.commit();
                updateCont += row;
                if (row < batchSize) {
                    break;
                }
            }
            logInfo("[markHouseKeep] - Update " + updateCont + " record(s) in CMC_USER_TO_DO_ITEM");
            logInfo("[markHouseKeep] CMC_USER_TO_DO_ITEM END");

            // Part 3
            row = 1;
            updateCont = 0;
            while (row > 0) {
                row = dao.updateCmcUserAccBalanceByBatch(conn, inputDate, batchSize);
                conn.commit();
                updateCont += row;
                if (row < batchSize) {
                    break;
                }
            }
            logInfo("[markHouseKeep] - Update " + updateCont + " record(s) in CMC_USER_ACC_BALANCE");
            logInfo("[markHouseKeep] CMC_USER_ACC_BALANCE END");

            // Part 4
            row = 1;
            updateCont = 0;
            while (row > 0) {
                row = dao.updateCmcUserPaymentTranByBatch(conn, inputDate, batchSize);
                conn.commit();
                updateCont += row;
                if (row < batchSize) {
                    break;
                }
            }
            logInfo("[markHouseKeep] - Update " + updateCont + " record(s) in CMC_USER_PAYMENT_TRAN");
            logInfo("[markHouseKeep] CMC_USER_PAYMENT_TRAN END");

            // Part 5a
            row = 1;
            updateCont = 0;
            while (row > 0) {
                row = dao.updateCmcUserEmailByBatch(conn, inputDate, batchSize);
                conn.commit();
                updateCont += row;
                if (row < batchSize) {
                    break;
                }
            }
            logInfo("[markHouseKeep] - Update " + updateCont + " record(s) in CMC_USER_EMAIL");
            logInfo("[markHouseKeep] CMC_USER_EMAIL END");

            // Part 5b
            conn.setUpdateMode(HPFW_Connection.DIRECT);
            row = 1;
            updateCont = 0;
            while (row > 0) {
                row = dao.updateCmcUserEmailBkByBatch(conn, inputDate, batchSize);
                conn.commit();
                updateCont += row;
                if (row < batchSize) {
                    break;
                }
            }
            logInfo("[markHouseKeep] - Update " + updateCont + " record(s) in CMC_USER_EMAIL_BK");
            logInfo("[markHouseKeep] CMC_USER_EMAIL_BK END");

            // Part 6a
            conn.setUpdateMode(HPFW_Connection.DIRECT_WITH_HISTORY);
            row = 1;
            updateCont = 0;
            while (row > 0) {
                row = dao.updateCmcUserMobileMsgByBatch(conn, inputDate, batchSize);
                conn.commit();
                updateCont += row;
                if (row < batchSize) {
                    break;
                }
            }
            logInfo("[markHouseKeep] - Update " + updateCont + " record(s) in CMC_USER_MOBILE_MSG");
            logInfo("[markHouseKeep] CMC_USER_MOBILE_MSG END");

            // Part 6b
            conn.setUpdateMode(HPFW_Connection.DIRECT);
            row = 1;
            updateCont = 0;
            while (row > 0) {
                row = dao.updateCmcUserMobileMsgBkByBatch(conn, inputDate, batchSize);
                conn.commit();
                updateCont += row;
                if (row < batchSize) {
                    break;
                }
            }
            logInfo("[markHouseKeep] - Update " + updateCont + " record(s) in CMC_USER_MOBILE_MSG_BK");
            logInfo("[markHouseKeep] CMC_USER_MOBILE_MSG_BK END");

            // MyGov6-C1-002 Enhance Maintain Message module to support iAM Smart message -- START
            conn.setUpdateMode(HPFW_Connection.DIRECT_WITH_HISTORY);
            row = 1;
            updateCont = 0;
            while (row > 0) {
                row = dao.updateIasUserMessageByBatch(conn, iasMsgHousekeepRetentionMonths, batchSize);
                conn.commit();
                updateCont += row;
                if (row < batchSize) {
                    break;
                }
            }
            logInfo("[markHouseKeep] - Update " + updateCont + " record(s) in IAS_USER_MESSAGE");
            logInfo("[markHouseKeep] IAS_USER_MESSAGE END");
            // MyGov6-C1-002 Enhance Maintain Message module to support iAM Smart message -- END

            // CMC-2024-015 - Housekeep iAM Smart To-Do Item - BEGIN
            conn.setUpdateMode(HPFW_Connection.DIRECT_WITH_HISTORY);
            row = 1;
            updateCont = 0;
            while (row > 0) {
                row = dao.updateIasUserToDoItemByBatch(conn, iasToDoItemHousekeepRetentionMonths, batchSize);
                conn.commit();
                updateCont += row;
                if (row < batchSize) {
                    break;
                }
            }
            logInfo("[markHouseKeep] - Update " + updateCont + " record(s) in IAS_USER_TO_DO_ITEM");
            logInfo("[markHouseKeep] IAS_USER_TO_DO_ITEM END");
            // CMC-2024-015 - Housekeep iAM Smart To-Do Item - END

            // CMC-2024-016 - Housekeep iAM Smart Application Status - BEGIN
            conn.setUpdateMode(HPFW_Connection.DIRECT_WITH_HISTORY);
            row = 1;
            updateCont = 0;
            while (row > 0) {
                row = dao.updateIasUserApplicationByBatch(conn, iasApplicationHousekeepRetentionMonths, batchSize);
                conn.commit();
                updateCont += row;
                if (row < batchSize) {
                    break;
                }
            }
            logInfo("[markHouseKeep] - Update " + updateCont + " record(s) in IAS_USER_APPLICATION");
            logInfo("[markHouseKeep] IAS_USER_APPLICATION END");
            // CMC-2024-016 - Housekeep iAM Smart Application Status - END

            // release lock for concurrent control
            // if(!releaseLock)
            // {
            // if(jobControlUtils!=null)
            // {
            // jobControlUtils.releaseJobControl(jobControlName);
            // releaseLock=true;
            // }
            // }
            // }else{
            // logInfo("[markHouseKeep]CMC_MARS_SYN_JOB_LOCK table cannot be locked.");
            // }
        } catch (Exception e) {
            logError("[HouseKeepingRecord]General exception raised in markHouseKeepIndicator", e);
            throw new EJBException(e);
        } finally {
            if (conn != null)
                conn.close();
            // release lock for concurrent control
            // if(!releaseLock)
            // {
            // if(jobControlUtils!=null)
            // {
            // jobControlUtils.releaseJobControl(jobControlName);
            // releaseLock=true;
            // }
            // }
        }
        logInfo("[BATCH_JOB][HouseKeepingRecord]markHouseKeepIndicator - END");
    }

    public void archiveAndRemoveRecord(String inputDate) throws EJBException {
        logInfo("[BATCH_JOB][HouseKeepingRecord]archiveAndRemoveRecord - START " + inputDate);
        HPFW_Connection conn = null;

        // JobControlUtils jobControlUtils = null;
        // String jobControlName = "";
        // boolean releaseLock = true;

        int delMsgMonthLimit = 9999;
        int batchSize = 9999;
        int noOfBatch = 100;
        int startIndex = 0;
        int endIndex = 0;
        boolean recordExist = false;

        int thresoldCSV = 10;
        int thresoldLOB = 10;
        String path = "";

        Integer currCSVIncCnt = 0;
        Integer currCSVFileIdx = 1;
        FileWriter fWriterCSV = null;

        int currLOBIncCnt = 0;
        int currLOBFileIdx = 1;
        FileWriter fWriterLOB = null;

        int currBroadLOBIncCnt = 0;
        int currBroadLOBFileIdx = 1;
        FileWriter fWriterBroadLOB = null;

        SimpleDateFormat dateTimeFomatter = new SimpleDateFormat("yyyyMMddHHmm");

        String archiveDateStr = "";
        String curProcessingInfo = "";
        try {
            conn = HPFW_Connection.getHPFW_Connection(false);
            conn.begin(null, new Timestamp(Calendar.getInstance().getTime().getTime()),
                    HPFW_Connection.DIRECT_WITH_HISTORY);
            conn.setAutoCommit(false);

            Properties properties = ServiceLocator.getInstance(null).getProperties();

            delMsgMonthLimit = Integer.parseInt(properties.getProperty("HOUSE_KEEP_DEL_MSG_CREATED_MONTH_LIMIT"));
            path = properties.getProperty("HOUSE_KEEP_ARCHIVE_PATH");
            thresoldCSV = Integer.parseInt(properties.getProperty("HOUSE_KEEP_THRESOLD_CSV"));
            thresoldLOB = Integer.parseInt(properties.getProperty("HOUSE_KEEP_THRESOLD_LOB"));
            batchSize = Integer.parseInt(properties.getProperty("HOUSE_KEEP_DEL_HIST_TBL_BATCH_SIZE"));
            noOfBatch = Integer.parseInt(properties.getProperty("HOUSE_KEEP_NO_OF_BATCH"));
            // jobControlName = properties.getProperty("JOB_CONTROL_ARCHIVE_DEL_HOUSE_KEEP");

            SimpleDateFormat dateChkFomatter = new SimpleDateFormat("yyyy-MM-dd");
            Calendar inputDateCalendar = Calendar.getInstance();
            inputDateCalendar.setTime(dateChkFomatter.parse(inputDate));
            inputDateCalendar.add(Calendar.MONTH, +delMsgMonthLimit);
            if (inputDateCalendar.getTime().after(new Date())) {
                logInfo("[BATCH_JOB][HouseKeepingRecord]archiveAndRemoveRecord - InputDate should be before or equal to two years ago!");
                return;
            }

            archiveDateStr = dateTimeFomatter.format(new Date());

            // jobControlUtils = new JobControlUtils();
            // Concurrent control
            // if (jobControlUtils.lockJobControl(jobControlName))
            // {
            // releaseLock = false;

            HouseKeepingRecordDAO dao = new HouseKeepingRecordDAO();

            // ************************************************************************************************************
            // Part 8 CMC_USER_EMAIL
            // ************************************************************************************************************

            logInfo("[HouseKeepArchive] Start archive Email:");
            currCSVIncCnt = 0;
            currCSVFileIdx = 1;
            fWriterCSV = null;
            currLOBIncCnt = 0;
            currLOBFileIdx = 1;
            fWriterLOB = null;

            currBroadLOBIncCnt = 0;
            currBroadLOBFileIdx = 1;
            fWriterBroadLOB = null;

            String csvMsgCreateDt = "";
            String lobMsgCreateDt = "";
            String bCsvMsgCreateDt = "";
            String bLobMsgCreateDt = "";

            startIndex = 1;
            endIndex = batchSize;

            for (int i = 0; i < noOfBatch; i++) {
                List<CmcUser> emailIDList = dao.getEmailIDFromCmcUserEmail(conn, startIndex, endIndex, inputDate);
                if (emailIDList.size() <= 0) {
                    break;
                }
                for (CmcUser emIdElement : emailIDList) {
                    curProcessingInfo = "archive cmc_user_email:" + "current processing email_id:"
                            + emIdElement.getEmailID();

                    List<Object> returnResult = dao.archiveEmailCsvLobAndBroadcastLob(conn, emIdElement.getEmailID(),
                            path, thresoldCSV, batchSize,
                            // para8,
                            inputDate,
                            currCSVIncCnt, currCSVFileIdx, fWriterCSV, archiveDateStr, csvMsgCreateDt,
                            thresoldLOB, currLOBIncCnt, currLOBFileIdx, fWriterLOB, lobMsgCreateDt,
                            currBroadLOBIncCnt, currBroadLOBFileIdx, fWriterBroadLOB, bLobMsgCreateDt);

                    currCSVIncCnt = (int) returnResult.get(0);
                    currCSVFileIdx = (int) returnResult.get(1);
                    fWriterCSV = (FileWriter) returnResult.get(2);
                    csvMsgCreateDt = (String) returnResult.get(3);

                    currLOBIncCnt = (int) returnResult.get(4);
                    currLOBFileIdx = (int) returnResult.get(5);
                    fWriterLOB = (FileWriter) returnResult.get(6);
                    lobMsgCreateDt = (String) returnResult.get(7);

                    currBroadLOBIncCnt = (int) returnResult.get(8);
                    currBroadLOBFileIdx = (int) returnResult.get(9);
                    fWriterBroadLOB = (FileWriter) returnResult.get(10);
                    bLobMsgCreateDt = (String) returnResult.get(11);

                    dao.insertEmailArchiveIndex(conn, emIdElement.getEmailID(), batchSize,
                            // para8,
                            inputDate);
                    conn.commit();

                    // Update housekeep ind='X'
                    int row = 1;
                    while (row > 0) {
                        row = dao.updateCmcUserEmailHouseKeepInd(conn, emIdElement.getEmailID(), inputDate, batchSize);
                        conn.commit();
                        if (row < batchSize) {
                            break;
                        }
                    }
                }
            }

            if (fWriterCSV != null) {
                try {
                    fWriterCSV.close();
                } catch (Exception e) {
                    throw new EJBException(e);
                }
            }
            if (fWriterLOB != null) {
                try {
                    fWriterLOB.close();
                } catch (Exception e) {
                    throw new EJBException(e);
                }
            }
            if (fWriterBroadLOB != null) {
                try {
                    fWriterBroadLOB.close();
                } catch (Exception e) {
                    throw new EJBException(e);
                }
            }
            logInfo("[HouseKeepArchive] End archive Email");

            // ************************************************************************************************************
            // Part 9 CMC_USER_EMAIL_BK
            // ************************************************************************************************************

            logInfo("[HouseKeepArchive] Start archive Email BK:");
            currCSVIncCnt = 0;
            currCSVFileIdx = 1;
            fWriterCSV = null;
            currLOBIncCnt = 0;
            currLOBFileIdx = 1;
            fWriterLOB = null;

            currBroadLOBIncCnt = 0;
            currBroadLOBFileIdx = 1;
            fWriterBroadLOB = null;

            csvMsgCreateDt = "";
            lobMsgCreateDt = "";
            bCsvMsgCreateDt = "";
            bLobMsgCreateDt = "";

            startIndex = 1;
            endIndex = batchSize;

            for (int i = 0; i < noOfBatch; i++) {
                List<CmcUser> useEmailBkList = dao.getEmailIdFromCmcUserEmailBk(conn, startIndex, endIndex, inputDate);
                if (useEmailBkList.size() <= 0) {
                    break;
                }
                for (CmcUser emIdElement : useEmailBkList) {
                    curProcessingInfo = "archive cmc_user_email_bk:" + "current processing email_id:"
                            + emIdElement.getEmailID();

                    List<Object> returnResult = dao.archiveEmailBKCsvLobAndBroadcastLob(conn, emIdElement.getEmailID(),
                            path, thresoldCSV, batchSize,
                            // para8,
                            inputDate,
                            currCSVIncCnt, currCSVFileIdx, fWriterCSV, archiveDateStr, csvMsgCreateDt,
                            thresoldLOB, currLOBIncCnt, currLOBFileIdx, fWriterLOB, lobMsgCreateDt,
                            currBroadLOBIncCnt, currBroadLOBFileIdx, fWriterBroadLOB, bLobMsgCreateDt);
                    currCSVIncCnt = (int) returnResult.get(0);
                    currCSVFileIdx = (int) returnResult.get(1);
                    fWriterCSV = (FileWriter) returnResult.get(2);
                    csvMsgCreateDt = (String) returnResult.get(3);

                    currLOBIncCnt = (int) returnResult.get(4);
                    currLOBFileIdx = (int) returnResult.get(5);
                    fWriterLOB = (FileWriter) returnResult.get(6);
                    lobMsgCreateDt = (String) returnResult.get(7);

                    currBroadLOBIncCnt = (int) returnResult.get(8);
                    currBroadLOBFileIdx = (int) returnResult.get(9);
                    fWriterBroadLOB = (FileWriter) returnResult.get(10);
                    bLobMsgCreateDt = (String) returnResult.get(11);

                    dao.insertEmailBKArchiveIndex(conn, emIdElement.getEmailID(), batchSize, inputDate);
                    conn.commit();

                    // Update housekeep ind='X'
                    int row = 1;
                    while (row > 0) {
                        row = dao.updateCmcUserEmailBKHouseKeepInd(conn, emIdElement.getEmailID(), inputDate,
                                batchSize);
                        conn.commit();
                        if (row < batchSize) {
                            break;
                        }
                    }
                }
            }

            if (fWriterCSV != null) {
                try {
                    fWriterCSV.close();
                } catch (Exception e) {
                    throw new EJBException(e);
                }
            }
            if (fWriterLOB != null) {
                try {
                    fWriterLOB.close();
                } catch (Exception e) {
                    throw new EJBException(e);
                }
            }

            if (fWriterBroadLOB != null) {
                try {
                    fWriterBroadLOB.close();
                } catch (Exception e) {
                    throw new EJBException(e);
                }
            }
            logInfo("[HouseKeepArchive] End archive Email BK:");

            // ************************************************************************************************************
            // Part 10 CMC_USER_MOBILE_MESSAGE
            // ************************************************************************************************************

            logInfo("[HouseKeepArchive] Start archive Mobile Message: ");

            currCSVIncCnt = 0;
            currCSVFileIdx = 1;
            fWriterCSV = null;
            currLOBIncCnt = 0;
            currLOBFileIdx = 1;
            fWriterLOB = null;

            currBroadLOBIncCnt = 0;
            currBroadLOBFileIdx = 1;
            fWriterBroadLOB = null;

            csvMsgCreateDt = "";
            lobMsgCreateDt = "";
            bCsvMsgCreateDt = "";
            bLobMsgCreateDt = "";

            startIndex = 1;
            endIndex = batchSize;

            for (int i = 0; i < noOfBatch; i++) {
                logInfo("[HouseKeepArchive] Start Mobile Message select");
                List<CmcUser> mobileMsgIdList = dao.getMobileMsgIdFromCmcUserMobileMessage(conn, startIndex, endIndex,
                        inputDate);
                if (mobileMsgIdList.size() <= 0) {
                    break;
                }

                for (CmcUser msIdElement : mobileMsgIdList) {
                    curProcessingInfo = "archive cmc_user_mobile_msg:" + "current processing mobile_msg_id:"
                            + msIdElement.getMobileMessageID();

                    List<Object> returnResult = dao.archiveMobileMessageCsvLobAndBroadcastLob(conn,
                            msIdElement.getMobileMessageID(),
                            path, thresoldCSV, batchSize,
                            // para9,
                            inputDate,
                            currCSVIncCnt, currCSVFileIdx, fWriterCSV, archiveDateStr, csvMsgCreateDt,
                            thresoldLOB, currLOBIncCnt, currLOBFileIdx, fWriterLOB, lobMsgCreateDt,
                            currBroadLOBIncCnt, currBroadLOBFileIdx, fWriterBroadLOB, bLobMsgCreateDt);

                    currCSVIncCnt = (int) returnResult.get(0);
                    currCSVFileIdx = (int) returnResult.get(1);
                    fWriterCSV = (FileWriter) returnResult.get(2);
                    csvMsgCreateDt = (String) returnResult.get(3);

                    currLOBIncCnt = (int) returnResult.get(4);
                    currLOBFileIdx = (int) returnResult.get(5);
                    fWriterLOB = (FileWriter) returnResult.get(6);
                    lobMsgCreateDt = (String) returnResult.get(7);

                    currBroadLOBIncCnt = (int) returnResult.get(8);
                    currBroadLOBFileIdx = (int) returnResult.get(9);
                    fWriterBroadLOB = (FileWriter) returnResult.get(10);
                    bLobMsgCreateDt = (String) returnResult.get(11);

                    dao.insertMobileMsgArchiveIndex(conn, msIdElement.getMobileMessageID(), batchSize, inputDate);
                    conn.commit();

                    // Update housekeep ind='X'
                    int row = 1;
                    while (row > 0) {
                        row = dao.updateCmcUserMobileMsgHouseKeepInd(conn, msIdElement.getMobileMessageID(), inputDate,
                                batchSize);
                        conn.commit();
                        // logInfo("[HouseKeepArchive] - Update " + row + " record(s) in CMC_USER_MOBILE_MSG,HOUSEKEEP_IND to X ");
                        if (row < batchSize) {
                            break;
                        }
                    }
                }
            }

            if (fWriterCSV != null) {
                try {
                    fWriterCSV.close();
                } catch (Exception e) {
                    throw new EJBException(e);
                }
            }
            if (fWriterLOB != null) {
                try {
                    fWriterLOB.close();
                } catch (Exception e) {
                    throw new EJBException(e);
                }
            }

            if (fWriterBroadLOB != null) {
                try {
                    fWriterBroadLOB.close();
                } catch (Exception e) {
                    throw new EJBException(e);
                }
            }
            logInfo("[HouseKeepArchive] End archive My Message: ");

            // ************************************************************************************************************
            // Part 11 CMC_USER_MOBILE_MESSAGE_BK
            // ************************************************************************************************************

            logInfo("[HouseKeepArchive] Start archive Mobile Message BK: ");

            currCSVIncCnt = 0;
            currCSVFileIdx = 1;
            fWriterCSV = null;
            currLOBIncCnt = 0;
            currLOBFileIdx = 1;
            fWriterLOB = null;

            currBroadLOBIncCnt = 0;
            currBroadLOBFileIdx = 1;
            fWriterBroadLOB = null;

            csvMsgCreateDt = "";
            lobMsgCreateDt = "";
            bCsvMsgCreateDt = "";
            bLobMsgCreateDt = "";

            startIndex = 1;
            endIndex = batchSize;

            for (int i = 0; i < noOfBatch; i++) {
                logInfo("[HouseKeepArchive] Start Mobile Message BK select");
                List<CmcUser> mobileMsgBKList = dao.getMobileMsgIdFromCmcUserMobileMessageBk(conn, startIndex, endIndex,
                        inputDate);
                if (mobileMsgBKList.size() <= 0) {
                    break;
                }

                for (CmcUser mMsgIdElement : mobileMsgBKList) {
                    curProcessingInfo = "archive cmc_user_mobile_msg_bk:" + "current processing mobile_msg_id:"
                            + mMsgIdElement.getMobileMessageID();

                    List<Object> returnResult = dao.archiveMobileMessageBkCsvLobAndBroadcastLob(conn,
                            mMsgIdElement.getMobileMessageID(), path,
                            thresoldCSV, batchSize,
                            // para9,
                            inputDate,
                            currCSVIncCnt, currCSVFileIdx, fWriterCSV, archiveDateStr, csvMsgCreateDt,
                            thresoldLOB, currLOBIncCnt, currLOBFileIdx, fWriterLOB, lobMsgCreateDt,
                            currBroadLOBIncCnt, currBroadLOBFileIdx, fWriterBroadLOB, bLobMsgCreateDt);

                    currCSVIncCnt = (int) returnResult.get(0);
                    currCSVFileIdx = (int) returnResult.get(1);
                    fWriterCSV = (FileWriter) returnResult.get(2);
                    csvMsgCreateDt = (String) returnResult.get(3);

                    currLOBIncCnt = (int) returnResult.get(4);
                    currLOBFileIdx = (int) returnResult.get(5);
                    fWriterLOB = (FileWriter) returnResult.get(6);
                    lobMsgCreateDt = (String) returnResult.get(7);

                    currBroadLOBIncCnt = (int) returnResult.get(8);
                    currBroadLOBFileIdx = (int) returnResult.get(9);
                    fWriterBroadLOB = (FileWriter) returnResult.get(10);
                    bLobMsgCreateDt = (String) returnResult.get(11);

                    dao.insertMobileMsgBKArchiveIndex(conn, mMsgIdElement.getMobileMessageID(), batchSize, inputDate);
                    conn.commit();

                    // Update housekeep ind='X'
                    int row = 1;
                    while (row > 0) {
                        row = dao.updateCmcUserMobileMsgBKHouseKeepInd(conn, mMsgIdElement.getMobileMessageID(),
                                inputDate, batchSize);
                        conn.commit();
                        if (row < batchSize) {
                            break;
                        }
                    }
                }
            }

            if (fWriterCSV != null) {
                try {
                    fWriterCSV.close();
                } catch (Exception e) {
                    throw new EJBException(e);
                }
            }
            if (fWriterLOB != null) {
                try {
                    fWriterLOB.close();
                } catch (Exception e) {
                    throw new EJBException(e);
                }
            }
            if (fWriterBroadLOB != null) {
                try {
                    fWriterBroadLOB.close();
                } catch (Exception e) {
                    throw new EJBException(e);
                }
            }
            logInfo("[HouseKeepArchive] End archive My Message BK: ");

            // ************************************************************************************************************
            // Part 1 CMC_USER_MESSAGE
            // ************************************************************************************************************

            logInfo("[HouseKeepArchive] Start archive and delete My Message: ");

            currCSVIncCnt = 0;
            currCSVFileIdx = 1;
            fWriterCSV = null;
            currLOBIncCnt = 0;
            currLOBFileIdx = 1;
            fWriterLOB = null;

            currBroadLOBIncCnt = 0;
            currBroadLOBFileIdx = 1;
            fWriterBroadLOB = null;

            csvMsgCreateDt = "";
            lobMsgCreateDt = "";
            bCsvMsgCreateDt = "";
            bLobMsgCreateDt = "";

            startIndex = 1;
            endIndex = batchSize;

            for (int i = 0; i < noOfBatch; i++) {
                List<CmcUser> MsgIDList = dao.getMsgIDFromCmcUserMessage(conn, startIndex, endIndex, inputDate);
                if (MsgIDList.size() <= 0) {
                    break;
                }
                logInfo("[HouseKeepArchive] Start message select");
                for (CmcUser msIdElement : MsgIDList) {
                    curProcessingInfo = "archive cmc_user_message:" + "current processing message_id:"
                            + msIdElement.getMessageID();

                    List<Object> returnResult = dao.archiveMyMessageCsvLobAndBroadcastLob(conn,
                            msIdElement.getMessageID(),
                            path, thresoldCSV, batchSize,
                            // para1,
                            inputDate,
                            currCSVIncCnt, currCSVFileIdx, fWriterCSV, archiveDateStr, csvMsgCreateDt,
                            thresoldLOB, currLOBIncCnt, currLOBFileIdx, fWriterLOB, lobMsgCreateDt,
                            currBroadLOBIncCnt, currBroadLOBFileIdx, fWriterBroadLOB, bLobMsgCreateDt);

                    currCSVIncCnt = (int) returnResult.get(0);
                    currCSVFileIdx = (int) returnResult.get(1);
                    fWriterCSV = (FileWriter) returnResult.get(2);
                    csvMsgCreateDt = (String) returnResult.get(3);

                    currLOBIncCnt = (int) returnResult.get(4);
                    currLOBFileIdx = (int) returnResult.get(5);
                    fWriterLOB = (FileWriter) returnResult.get(6);
                    lobMsgCreateDt = (String) returnResult.get(7);

                    currBroadLOBIncCnt = (int) returnResult.get(8);
                    currBroadLOBFileIdx = (int) returnResult.get(9);
                    fWriterBroadLOB = (FileWriter) returnResult.get(10);
                    bLobMsgCreateDt = (String) returnResult.get(11);

                    dao.insertMyMessageArchiveIndex(conn, msIdElement.getMessageID(), batchSize, inputDate);
                    conn.commit();

                    // Update housekeep ind='X'
                    int row = 1;
                    while (row > 0) {
                        row = dao.updateCmcUserMessageHouseKeepInd(conn, msIdElement.getMessageID(), inputDate,
                                batchSize);
                        conn.commit();
                        if (row < batchSize) {
                            break;
                        }
                    }
                }
            }

            if (fWriterCSV != null) {
                try {
                    fWriterCSV.close();
                } catch (Exception e) {
                    throw new EJBException(e);
                }
            }
            if (fWriterLOB != null) {
                try {
                    fWriterLOB.close();
                } catch (Exception e) {
                    throw new EJBException(e);
                }
            }
            if (fWriterBroadLOB != null) {
                try {
                    fWriterBroadLOB.close();
                } catch (Exception e) {
                    throw new EJBException(e);
                }
            }
            logInfo("[HouseKeepArchive] End archive and delete My Message: ");

            // ************************************************************************************************************
            // Part 3 CMC_USER_TODO_ITEM
            // ************************************************************************************************************
            logInfo("[HouseKeepArchive] Start archive and delete To Do Item:");

            currCSVIncCnt = 0;
            currCSVFileIdx = 1;
            fWriterCSV = null;
            currLOBIncCnt = 0;
            currLOBFileIdx = 1;
            fWriterLOB = null;

            currBroadLOBIncCnt = 0;
            currBroadLOBFileIdx = 1;
            fWriterBroadLOB = null;

            csvMsgCreateDt = "";
            lobMsgCreateDt = "";
            bCsvMsgCreateDt = "";
            bLobMsgCreateDt = "";

            startIndex = 1;
            endIndex = batchSize;

            for (int i = 0; i < noOfBatch; i++) {
                logInfo("[HouseKeepArchive] Start To Do Item Selection");

                List<CmcUser> toDoItemList = dao.getTodoItemIdFromCmcUserToDoItem(conn, startIndex, endIndex,
                        inputDate);
                if (toDoItemList.size() <= 0) {
                    break;
                }
                for (CmcUser toDoItemEle : toDoItemList) {
                    curProcessingInfo = "archive cmc_user_to_do_item:" + "current processing to_do_item_id:"
                            + toDoItemEle.getToDoItemID();

                    List<Object> returnResult = dao.archiveMyToDoItemCsvLobAndBroadcastLob(conn,
                            toDoItemEle.getToDoItemID(),
                            path, thresoldCSV, batchSize,
                            // para2,
                            inputDate,
                            currCSVIncCnt, currCSVFileIdx, fWriterCSV, archiveDateStr, csvMsgCreateDt,
                            thresoldLOB, currLOBIncCnt, currLOBFileIdx, fWriterLOB, lobMsgCreateDt,
                            currBroadLOBIncCnt, currBroadLOBFileIdx, fWriterBroadLOB, bLobMsgCreateDt);

                    currCSVIncCnt = (int) returnResult.get(0);
                    currCSVFileIdx = (int) returnResult.get(1);
                    fWriterCSV = (FileWriter) returnResult.get(2);
                    csvMsgCreateDt = (String) returnResult.get(3);

                    currLOBIncCnt = (int) returnResult.get(4);
                    currLOBFileIdx = (int) returnResult.get(5);
                    fWriterLOB = (FileWriter) returnResult.get(6);
                    lobMsgCreateDt = (String) returnResult.get(7);

                    currBroadLOBIncCnt = (int) returnResult.get(8);
                    currBroadLOBFileIdx = (int) returnResult.get(9);
                    fWriterBroadLOB = (FileWriter) returnResult.get(10);
                    bLobMsgCreateDt = (String) returnResult.get(11);

                    dao.insertMyToDoItemArchiveIndex(conn, toDoItemEle.getToDoItemID(), batchSize, inputDate);
                    conn.commit();

                    // Update housekeep ind='X'
                    int row = 1;
                    while (row > 0) {
                        row = dao.updateCmcUserToDoItemHouseKeepInd(conn, toDoItemEle.getToDoItemID(), inputDate,
                                batchSize);
                        conn.commit();
                        if (row < batchSize) {
                            break;
                        }
                    }
                }
            }

            if (fWriterCSV != null) {
                try {
                    fWriterCSV.close();
                } catch (Exception e) {
                    throw new EJBException(e);
                }
            }
            if (fWriterLOB != null) {
                try {
                    fWriterLOB.close();
                } catch (Exception e) {
                    throw new EJBException(e);
                }
            }
            if (fWriterBroadLOB != null) {
                try {
                    fWriterBroadLOB.close();
                } catch (Exception e) {
                    throw new EJBException(e);
                }
            }
            logInfo("[HouseKeepArchive] End archive and delete To Do Item");

            // ************************************************************************************************************
            // Part 5 CMC_USER_PAYMENT_TRAN
            // ************************************************************************************************************

            logInfo("[HouseKeepArchive] Start archive and delete MyBill: ");

            currCSVIncCnt = 0;
            currCSVFileIdx = 1;
            fWriterCSV = null;
            currLOBIncCnt = 0;
            currLOBFileIdx = 1;
            fWriterLOB = null;

            currBroadLOBIncCnt = 0;
            currBroadLOBFileIdx = 1;
            fWriterBroadLOB = null;

            csvMsgCreateDt = "";

            startIndex = 1;
            endIndex = batchSize;

            for (int i = 0; i < noOfBatch; i++) {
                logInfo("[HouseKeepArchive] Start MyBill Selection");

                List<String> tranIdList = dao.getTranIdFromCmcUserPaymentTran(conn, startIndex, endIndex, inputDate);
                if (tranIdList.size() <= 0) {
                    break;
                }
                for (String tranId : tranIdList) {
                    curProcessingInfo = "archive cmc_user_payment_tran:" + "current processing tran_id:" + tranId;
                    List<Object> returnResultCSV = dao.archiveMyBillCSV(conn, tranId, path, thresoldCSV, batchSize,
                            // para4,
                            inputDate,
                            currCSVIncCnt, currCSVFileIdx, fWriterCSV, archiveDateStr, csvMsgCreateDt);

                    currCSVIncCnt = (int) returnResultCSV.get(0);
                    currCSVFileIdx = (int) returnResultCSV.get(1);
                    fWriterCSV = (FileWriter) returnResultCSV.get(2);
                    csvMsgCreateDt = (String) returnResultCSV.get(3);

                    dao.insertMyBillArchiveIndex(conn, tranId, batchSize, inputDate);
                    conn.commit();

                    // Update housekeep ind='X'
                    int row = 1;
                    while (row > 0) {
                        row = dao.updateCmcUserPaymentTranHousekeepInd(conn, tranId, inputDate, batchSize);
                        conn.commit();
                        if (row < batchSize) {
                            break;
                        }
                    }
                }
            }

            if (fWriterCSV != null) {
                try {
                    fWriterCSV.close();
                } catch (Exception e) {
                    throw new EJBException(e);
                }
            }

            logInfo("[HouseKeepArchive] End archive and delete MyBill");

            // release lock for concurrent control
            // if(!releaseLock)
            // {
            // if(jobControlUtils!=null)
            // {
            // jobControlUtils.releaseJobControl(jobControlName);
            // releaseLock=true;
            // }
            // }
            // }
            // else{
            // logInfo("[HouseKeeping]CMC_MARS_SYN_JOB_LOCK table cannot be locked.");
            // }
        } catch (Exception e) {
            logError("[HouseKeepingRecord]General exception raised in archiveAndRemoveRecord:" + curProcessingInfo, e);
            throw new EJBException(e);
        } finally {
            if (conn != null) {
                conn.close();
            }
            if (fWriterCSV != null) {
                try {
                    fWriterCSV.close();
                } catch (Exception e) {
                    throw new EJBException(e);
                }
            }
            if (fWriterLOB != null) {
                try {
                    fWriterLOB.close();
                } catch (Exception e) {
                    throw new EJBException(e);
                }
            }
            if (fWriterBroadLOB != null) {
                try {
                    fWriterBroadLOB.close();
                } catch (Exception e) {
                    throw new EJBException(e);
                }
            }
            // release lock for concurrent control
            // if(!releaseLock)
            // {
            // if(jobControlUtils!=null)
            // {
            // jobControlUtils.releaseJobControl(jobControlName);
            // releaseLock=true;
            // }
            // }
        }
        logInfo("[BATCH_JOB][HouseKeepingRecord]archiveAndRemoveRecord - END");
    }

    public void removeHistoricalRecord(String inputDate) throws EJBException {
        logInfo("[BATCH_JOB][HouseKeepingRecord]removeHistoricalRecord - START " + inputDate);

        HPFW_Connection conn = null;
        // JobControlUtils jobControlUtils = null;
        // String jobControlName = "";
        // boolean releaseLock = true;

        int batchSize = 0;
        int delNonUserTblMonthLimit = 0;
        int delHistTblMonthLimit = 9999;
        int delArchIdxTblMonthLimit = 9999;
        int noOfBatch = 999;
        String curProcessingInfo = "";
        int startIndex = 1;
        try {
            conn = HPFW_Connection.getHPFW_Connection(false);
            conn.begin(null, new Timestamp(Calendar.getInstance().getTime().getTime()),
                    HPFW_Connection.DIRECT_WITH_HISTORY);
            conn.setAutoCommit(false);

            Properties properties = ServiceLocator.getInstance(null).getProperties();

            batchSize = Integer.parseInt(properties.getProperty("HOUSE_KEEP_DEL_HIST_TBL_BATCH_SIZE"));
            delNonUserTblMonthLimit = Integer
                    .parseInt(properties.getProperty("HOUSE_KEEP_DEL_NON_USER_TBL_MONTH_LIMIT"));
            delArchIdxTblMonthLimit = Integer
                    .parseInt(properties.getProperty("HOUSE_KEEP_DEL_ARCHIVE_INDEX_TBL_MONTH_LIMIT"));
            delHistTblMonthLimit = Integer.parseInt(properties.getProperty("HOUSE_KEEP_DEL_HIST_TBL_MONTH_LIMIT"));
            noOfBatch = Integer.parseInt(properties.getProperty("HOUSE_KEEP_NO_OF_BATCH"));

            // jobControlName = properties.getProperty("JOB_CONTROL_REMOVE_HOUSE_KEEP_HIST");

            // jobControlUtils = new JobControlUtils();
            // Concurrent control
            // if (jobControlUtils.lockJobControl(jobControlName))
            // {
            // releaseLock = false;
            HouseKeepingRecordDAO dao = new HouseKeepingRecordDAO();

            int row = 1;
            int delCont = 0;
            while (row > 0) {
                row = dao.deleteCmcUserMessageHByCreateDate(conn, delHistTblMonthLimit, inputDate, batchSize);
                conn.commit();
                delCont += row;
                if (row < batchSize) {
                    break;
                }
            }
            logInfo("[removeHistoricalRecord] - Delete " + delCont + " record(s) in CMC_USER_MESSAGE_H");

            // MyGov6-C1-002 Enhance Maintain Message module to support iAM Smart message -- START
            int iasMsgHousekeepRetentionMonths = Integer
                    .parseInt(properties.getProperty(AppPropertyName.IAS_MSG_HOUSEKEEP_RETENTION_MONTHS_PROPERTY_NAME));

            row = 1;
            delCont = 0;
            while (row > 0) {
                row = dao.deleteIasUserMessageHByCreateDate(conn, iasMsgHousekeepRetentionMonths, batchSize);
                conn.commit();
                delCont += row;
                if (row < batchSize) {
                    break;
                }
            }
            logInfo("[removeHistoricalRecord] - Delete " + delCont + " record(s) in IAS_USER_MESSAGE_H");
            // MyGov6-C1-002 Enhance Maintain Message module to support iAM Smart message -- END

            // CMC-2024-015 - Housekeep iAM Smart To-Do Item - BEGIN
            int iasToDoItemHousekeepRetentionMonths = Integer.parseInt(
                    properties.getProperty(AppPropertyName.IAS_TODO_ITEM_HOUSEKEEP_RETENTION_MONTHS_PROPERTY_NAME));
            row = 1;
            delCont = 0;
            while (row > 0) {
                row = dao.deleteIasUserToDoItemHByCreateDate(conn, iasToDoItemHousekeepRetentionMonths, batchSize);
                conn.commit();
                delCont += row;
                if (row < batchSize) {
                    break;
                }
            }
            logInfo("[removeHistoricalRecord] - Delete " + delCont + " record(s) in IAS_USER_TO_DO_ITEM_H");
            // CMC-2024-015 - Housekeep iAM Smart To-Do Item - END

            // CMC-2024-016 - Housekeep iAM Smart Application Status - BEGIN
            int iasApplicationHousekeepRetentionMonths = Integer.parseInt(
                    properties.getProperty(AppPropertyName.IAS_APPLICATION_HOUSEKEEP_RETENTION_MONTHS_PROPERTY_NAME));
            row = 1;
            delCont = 0;
            while (row > 0) {
                row = dao.deleteIasUserApplicationHByCreateDate(conn, iasApplicationHousekeepRetentionMonths,
                        batchSize);
                conn.commit();
                delCont += row;
                if (row < batchSize) {
                    break;
                }
            }
            logInfo("[removeHistoricalRecord] - Delete " + delCont + " record(s) in IAS_USER_APPLICATION_H");
            // CMC-2024-016 - Housekeep iAM Smart Application Status - END

            row = 1;
            delCont = 0;
            while (row > 0) {
                row = dao.deleteCmcUserEmailHByCreateDate(conn, delHistTblMonthLimit, inputDate, batchSize);
                conn.commit();
                delCont += row;
                if (row < batchSize) {
                    break;
                }
            }
            logInfo("[removeHistoricalRecord] - Delete " + delCont + " record(s) in CMC_USER_EMAIL_H");

            row = 1;
            delCont = 0;
            while (row > 0) {
                row = dao.deleteCmcUserMobileMessageHByCreateDate(conn, delHistTblMonthLimit, inputDate, batchSize);
                conn.commit();
                delCont += row;
                if (row < batchSize) {
                    break;
                }
            }
            logInfo("[removeHistoricalRecord] - Delete " + delCont + " record(s) in CMC_USER_MOBILE_MSG_H");

            row = 1;
            delCont = 0;
            while (row > 0) {
                row = dao.deleteCmcMobileMessageHByCreateDate(conn, delHistTblMonthLimit, inputDate, batchSize);
                conn.commit();
                delCont += row;
                if (row < batchSize) {
                    break;
                }
            }
            logInfo("[removeHistoricalRecord] - Delete " + delCont + " record(s) in CMC_MOBILE_MSG_H");

            row = 1;
            delCont = 0;
            while (row > 0) {
                row = dao.deleteCmcMessageFolderHByCreateDate(conn, delHistTblMonthLimit, inputDate, batchSize);
                conn.commit();
                delCont += row;
                if (row < batchSize) {
                    break;
                }
            }
            logInfo("[removeHistoricalRecord] - Delete " + delCont + " record(s) in CMC_MESSAGE_FOLDER_H");

            row = 1;
            delCont = 0;
            while (row > 0) {
                row = dao.deleteCmcReminderHByCreateDate(conn, delHistTblMonthLimit, inputDate, batchSize);
                conn.commit();
                delCont += row;
                if (row < batchSize) {
                    break;
                }
            }
            logInfo("[removeHistoricalRecord] - Delete " + delCont + " record(s) in CMC_REMINDER_H");

            row = 1;
            delCont = 0;
            while (row > 0) {
                row = dao.deleteCmcUserToDoItemHByCreateDate(conn, delHistTblMonthLimit, inputDate, batchSize);
                conn.commit();
                delCont += row;
                if (row < batchSize) {
                    break;
                }
            }
            logInfo("[removeHistoricalRecord] - Delete " + delCont + " record(s) in CMC_USER_TO_DO_ITEM_H");

            row = 1;
            delCont = 0;
            while (row > 0) {
                row = dao.deleteCmcUserAccBalanceHByCreateDate(conn, delHistTblMonthLimit, inputDate, batchSize);
                conn.commit();
                delCont += row;
                if (row < batchSize) {
                    break;
                }
            }
            logInfo("[removeHistoricalRecord] - Delete " + delCont + " record(s) in CMC_USER_ACC_BALANCE_H");

            row = 1;
            delCont = 0;
            while (row > 0) {

                row = dao.deleteCmcUserPaymentTranHByCreateDate(conn, delHistTblMonthLimit, inputDate, batchSize);
                conn.commit();
                delCont += row;
                if (row < batchSize) {
                    break;
                }
            }
            logInfo("[removeHistoricalRecord] - Delete " + delCont + " record(s) in CMC_USER_PAYMENT_TRAN_H");

            // ************************************************************************************************************
            // Part 6 Delete working tables
            // ************************************************************************************************************

            logInfo("[removeHistoricalRecord] Start delete working tables: ");

            row = 1;
            delCont = 0;
            while (row > 0) {
                row = dao.deleteCmcUndeliveredBill(conn, delNonUserTblMonthLimit, inputDate, batchSize);
                conn.commit();
                delCont += row;
                if (row < batchSize) {
                    break;
                }
            }
            logInfo("[removeHistoricalRecord] - Delete " + delCont + " record(s) in CMC_UNDELIVERED_BILL");

            row = 1;
            delCont = 0;
            while (row > 0) {
                row = dao.deleteCmcUndeliveredBillH(conn, delNonUserTblMonthLimit, inputDate, batchSize);
                conn.commit();
                delCont += row;
                if (row < batchSize) {
                    break;
                }
            }
            logInfo("[removeHistoricalRecord] - Delete " + delCont + " record(s) in CMC_UNDELIVERED_BILL_H");

            row = 1;
            delCont = 0;
            while (row > 0) {
                row = dao.deleteCmcUndeliveredItem(conn, delNonUserTblMonthLimit, inputDate, batchSize);
                conn.commit();
                delCont += row;
                if (row < batchSize) {
                    break;
                }
            }
            logInfo("[removeHistoricalRecord] - Delete " + delCont + " record(s) in CMC_UNDELIVERED_ITEM");

            row = 1;
            delCont = 0;
            while (row > 0) {
                row = dao.deleteCmcUndeliveredItemH(conn, delNonUserTblMonthLimit, inputDate, batchSize);
                conn.commit();
                delCont += row;
                if (row < batchSize) {
                    break;
                }
            }
            logInfo("[removeHistoricalRecord] - Delete " + delCont + " record(s) in CMC_UNDELIVERED_ITEM_H");

            row = 1;
            delCont = 0;
            while (row > 0) {
                row = dao.deleteCmcUndeliveredMessage(conn, delNonUserTblMonthLimit, inputDate, batchSize);
                conn.commit();
                delCont += row;
                if (row < batchSize) {
                    break;
                }
            }
            logInfo("[removeHistoricalRecord] - Delete " + delCont + " record(s) in CMC_UNDELIVERED_MESSAGE");

            row = 1;
            delCont = 0;
            while (row > 0) {
                row = dao.deleteCmcUndeliveredMessageH(conn, delNonUserTblMonthLimit, inputDate, batchSize);
                conn.commit();
                delCont += row;
                if (row < batchSize) {
                    break;
                }
            }
            logInfo("[removeHistoricalRecord] - Delete " + delCont + " record(s) in CMC_UNDELIVERED_MESSAGE_H");

            row = 1;
            delCont = 0;
            while (row > 0) {
                row = dao.deleteCmcHiddenReinstateBill(conn, delNonUserTblMonthLimit, inputDate, batchSize);
                conn.commit();
                delCont += row;
                if (row < batchSize) {
                    break;
                }
            }
            logInfo("[removeHistoricalRecord] - Delete " + delCont + " record(s) in CMC_HIDDEN_REINSTATE_BILL");

            row = 1;
            delCont = 0;
            while (row > 0) {
                row = dao.deleteCmcHiddenReinstateItem(conn, delNonUserTblMonthLimit, inputDate, batchSize);
                conn.commit();
                delCont += row;
                if (row < batchSize) {
                    break;
                }
            }
            logInfo("[removeHistoricalRecord] - Delete " + delCont + " record(s) in CMC_HIDDEN_REINSTATE_ITEM");

            row = 1;
            delCont = 0;
            while (row > 0) {
                row = dao.deleteCmcHiddenReinstateMesg(conn, delNonUserTblMonthLimit, inputDate, batchSize);
                conn.commit();
                delCont += row;
                if (row < batchSize) {
                    break;
                }
            }
            logInfo("[removeHistoricalRecord] - Delete " + delCont + " record(s) in CMC_HIDDEN_REINSTATE_MESG");

            // ******************************************************************************************
            // Part 7
            // ******************************************************************************************

            row = 1;
            delCont = 0;
            while (row > 0) {
                row = dao.deleteCmcAsynMessageH(conn, delNonUserTblMonthLimit, inputDate, batchSize);
                conn.commit();
                delCont += row;
                if (row < batchSize) {
                    break;
                }
            }
            logInfo("[removeHistoricalRecord] - Delete " + delCont + " record(s) in CMC_ASYN_MESSAGE_H");

            row = 1;
            delCont = 0;
            while (row > 0) {
                row = dao.deleteCmcAsynMessage(conn, delNonUserTblMonthLimit, inputDate, batchSize);
                conn.commit();
                delCont += row;
                if (row < batchSize) {
                    break;
                }
            }
            logInfo("[removeHistoricalRecord] - Delete " + delCont + " record(s) in CMC_ASYN_MESSAGE");

            row = 1;
            delCont = 0;
            while (row > 0) {
                row = dao.deleteCmcAsynRspCtrlH(conn, delNonUserTblMonthLimit, inputDate, batchSize);
                conn.commit();
                delCont += row;
                if (row < batchSize) {
                    break;
                }
            }
            logInfo("[removeHistoricalRecord] - Delete " + delCont + " record(s) in CMC_ASYN_RSP_CTRL_H");

            row = 1;
            delCont = 0;
            while (row > 0) {
                row = dao.deleteCmcAsynRspCtrl(conn, delNonUserTblMonthLimit, inputDate, batchSize);
                conn.commit();
                delCont += row;
                if (row < batchSize) {
                    break;
                }
            }
            logInfo("[removeHistoricalRecord] - Delete " + delCont + " record(s) in CMC_ASYN_RSP_CTRL");

            row = 1;
            delCont = 0;
            while (row > 0) {
                row = dao.deleteCmcEgisNotiStatusH(conn, delNonUserTblMonthLimit, inputDate, batchSize);
                conn.commit();
                delCont += row;
                if (row < batchSize) {
                    break;
                }
            }
            logInfo("[removeHistoricalRecord] - Delete " + delCont + " record(s) in CMC_EGIS_NOTI_STATUS_H");

            row = 1;
            delCont = 0;
            while (row > 0) {
                row = dao.deleteCmcEgisNotiStatus(conn, delNonUserTblMonthLimit, inputDate, batchSize);
                conn.commit();
                delCont += row;
                if (row < batchSize) {
                    break;
                }
            }
            logInfo("[removeHistoricalRecord] - Delete " + delCont + " record(s) in CMC_EGIS_NOTI_STATUS");

            row = 1;
            delCont = 0;
            while (row > 0) {
                row = dao.deleteCmcPersonalEmailSchH(conn, delNonUserTblMonthLimit, inputDate, batchSize);
                conn.commit();
                delCont += row;
                if (row < batchSize) {
                    break;
                }
            }
            logInfo("[removeHistoricalRecord] - Delete " + delCont + " record(s) in CMC_PERSONAL_EMAIL_SCH_H");

            row = 1;
            delCont = 0;
            while (row > 0) {
                row = dao.deleteCmcPersonalEmailSch(conn, delNonUserTblMonthLimit, inputDate, batchSize);
                conn.commit();
                delCont += row;
                if (row < batchSize) {
                    break;
                }
            }
            logInfo("[removeHistoricalRecord] - Delete " + delCont + " record(s) in CMC_PERSONAL_EMAIL_SCH");

            row = 1;
            delCont = 0;
            while (row > 0) {
                row = dao.deleteCmcUserMessageAlert(conn, delNonUserTblMonthLimit, inputDate, batchSize);
                conn.commit();
                delCont += row;
                if (row < batchSize) {
                    break;
                }
            }
            logInfo("[removeHistoricalRecord] - Delete " + delCont + " record(s) in CMC_USER_MESSAGE_ALERT");
            logInfo("[removeHistoricalRecord] End delete working table: ");

            row = 1;
            int delParentCont = 0;
            while (row > 0) {
                row = dao.deleteCmcArchiveIndex(conn, delArchIdxTblMonthLimit, inputDate, batchSize);
                conn.commit();
                delParentCont += row;
                if (row < batchSize) {
                    break;
                }
            }
            logInfo("[removeHistoricalRecord] - Delete " + delParentCont + " record(s) in CMC_ARCHIVE_INDEX");

            // MyGov6-C1-002 Enhance Maintain Message module to support iAM Smart message -- START
            row = 1;
            int iasMsgStatusCont = 0;
            while (row > 0) {
                row = dao.deleteIasMsgStatusQueue(conn, iasMsgHousekeepRetentionMonths, batchSize);
                conn.commit();
                iasMsgStatusCont += row;
                if (row < batchSize) {
                    break;
                }
            }
            logInfo("[removeHistoricalRecord] - Delete " + iasMsgStatusCont + " record(s) in IAS_MSG_STATUS_QUEUE");

            row = 1;
            int iasAssoQueueCont = 0;
            while (row > 0) {
                row = dao.deleteIasAssoQueue(conn, iasMsgHousekeepRetentionMonths, batchSize);
                conn.commit();
                iasAssoQueueCont += row;
                if (row < batchSize) {
                    break;
                }
            }
            logInfo("[removeHistoricalRecord] - Delete " + iasAssoQueueCont + " record(s) in IAS_ASSO_QUEUE");
            // MyGov6-C1-002 Enhance Maintain Message module to support iAM Smart message -- END

            // release lock for concurrent control
            // if(!releaseLock)
            // {
            // if(jobControlUtils!=null)
            // {
            // jobControlUtils.releaseJobControl(jobControlName);
            // releaseLock=true;
            // }
            // }
            // }
            // else{
            // logInfo("[markHouseKeep]CMC_MARS_SYN_JOB_LOCK table cannot be locked.");
            // }

        } catch (Exception e) {
            logError("[HouseKeepingRecord]General exception raised in removeHistoricalRecord" + curProcessingInfo, e);
            throw new EJBException(e);
        } finally {
            if (conn != null) {
                conn.close();
            }
            // release lock for concurrent control
            // if(!releaseLock)
            // {
            // if(jobControlUtils!=null)
            // {
            // jobControlUtils.releaseJobControl(jobControlName);
            // releaseLock=true;
            // }
            // }
        }
        logInfo("[BATCH_JOB][HouseKeepingRecord]removeHistoricalRecord - END");
    }

    public void deleteMainTableRecord(String inputDate) throws EJBException {
        logInfo("[BATCH_JOB][HouseKeepingRecord]DeleteMainTableRecord - START " + inputDate);

        HPFW_Connection conn = null;
        // JobControlUtils jobControlUtils = null;
        // String jobControlName = "";
        // boolean releaseLock = true;

        int delMsgMonthLimit = 24;
        int batchSize = 0;

        int startIndex = 1;
        int noOfBatch = 999;
        String curProcessingInfo = "";

        int iasMsgHousekeepRetentionMonths = 24;

        // CMC-2024-007 - Enhance Maintain Message Module to Support iAM Smart To-Do Item - BEGIN
        int iasToDoItemHousekeepRetentionMonths = 24;
        // CMC-2024-007 - Enhance Maintain Message Module to Support iAM Smart To-Do Item - END

        // CMC-2024-010: Enhance CMC Client To Retrieve iAM Smart Application Status - BEGIN
        int iasApplicationHousekeepRetentionMonths = 24;
        // CMC-2024-010: Enhance CMC Client To Retrieve iAM Smart Application Status - END

        try {
            conn = HPFW_Connection.getHPFW_Connection(false);
            conn.begin(null, new Timestamp(Calendar.getInstance().getTime().getTime()),
                    HPFW_Connection.DIRECT_WITH_HISTORY);
            conn.setAutoCommit(false);

            Properties properties = ServiceLocator.getInstance(null).getProperties();

            delMsgMonthLimit = Integer.parseInt(properties.getProperty("HOUSE_KEEP_DEL_MSG_CREATED_MONTH_LIMIT"));
            batchSize = Integer.parseInt(properties.getProperty("HOUSE_KEEP_DEL_HIST_TBL_BATCH_SIZE"));
            noOfBatch = Integer.parseInt(properties.getProperty("HOUSE_KEEP_NO_OF_BATCH"));

            // MyGov6-C1-002 Enhance Maintain Message module to support iAM Smart message -- START
            iasMsgHousekeepRetentionMonths = Integer
                    .parseInt(properties.getProperty(AppPropertyName.IAS_MSG_HOUSEKEEP_RETENTION_MONTHS_PROPERTY_NAME));
            // MyGov6-C1-002 Enhance Maintain Message module to support iAM Smart message -- END

            // CMC-2024-007 - Enhance Maintain Message Module to Support iAM Smart To-Do Item - BEGIN
            iasToDoItemHousekeepRetentionMonths = Integer.parseInt(
                    properties.getProperty(AppPropertyName.IAS_TODO_ITEM_HOUSEKEEP_RETENTION_MONTHS_PROPERTY_NAME));
            // CMC-2024-007 - Enhance Maintain Message Module to Support iAM Smart To-Do Item - END

            // CMC-2024-010: Enhance CMC Client To Retrieve iAM Smart Application Status - BEGIN
            iasApplicationHousekeepRetentionMonths = Integer.parseInt(
                    properties.getProperty(AppPropertyName.IAS_APPLICATION_HOUSEKEEP_RETENTION_MONTHS_PROPERTY_NAME));
            // CMC-2024-010: Enhance CMC Client To Retrieve iAM Smart Application Status - END

            SimpleDateFormat dateTimeFomatter = new SimpleDateFormat("yyyy-MM-dd");
            Calendar inputDateCalendar = Calendar.getInstance();
            inputDateCalendar.setTime(dateTimeFomatter.parse(inputDate));
            inputDateCalendar.add(Calendar.MONTH, +delMsgMonthLimit);
            if (inputDateCalendar.getTime().after(new Date())) {
                logInfo("[BATCH_JOB][HouseKeepingRecord]DeleteMainTableRecord - InputDate should be before or equal to two years ago");
                return;
            }

            // jobControlName = properties.getProperty("JOB_CONTROL_REMOVE_HOUSE_KEEP_HIST");
            // jobControlUtils = new JobControlUtils();
            // Concurrent control
            // if (jobControlUtils.lockJobControl(jobControlName))
            // {
            // releaseLock = false;
            HouseKeepingRecordDAO dao = new HouseKeepingRecordDAO();

            // ************************************************************************************************************
            // Part 8 CMC_USER_EMAIL
            // ************************************************************************************************************
            // Delete normal email and dayend email
            logInfo("[DeleteMainTableRecord] Start delete email:");
            int delChildCont = 0;
            int delParentCont = 0;
            for (int i = 0; i < noOfBatch; i++) {
                List<CmcUser> emailIDList = dao.getDelEmailIDFromCmcUserEmail(conn, startIndex, batchSize, inputDate);

                if (emailIDList.size() <= 0) {
                    break;
                }
                for (CmcUser emIdElement : emailIDList) {
                    curProcessingInfo = "delete cmc_user_email:" + "current processing email_id:"
                            + emIdElement.getEmailID();
                    int row = 1;
                    while (row > 0) {
                        row = dao.deleteCmcUserEmailByEmailID(conn, emIdElement.getEmailID(), batchSize);
                        conn.commit();
                        delChildCont += row;
                        if (row < batchSize) {
                            break;
                        }
                    }

                    row = 1;
                    while (row > 0) {
                        row = row = dao.deleteCmcEmailByEmailID(conn, emIdElement.getEmailID());
                        conn.commit();
                        delParentCont += row;
                    }
                }
            }
            logInfo("[DeleteMainTableRecord] - Delete " + delChildCont + " record(s) in CMC_USER_EMAIL");
            logInfo("[DeleteMainTableRecord] - Delete " + delParentCont + " record(s) in CMC_EMAIL");
            logInfo("[DeleteMainTableRecord] End delete email:");

            // ************************************************************************************************************
            // Part 9 CMC_USER_EMAIL_BK
            // ************************************************************************************************************
            // Delete normal email and dayend email
            logInfo("[DeleteMainTableRecord] Start delete email BK");
            delChildCont = 0;
            delParentCont = 0;
            for (int i = 0; i < noOfBatch; i++) {
                List<CmcUser> useEmailBkList = dao.getDelEmailIdFromCmcUserEmailBk(conn, startIndex, batchSize,
                        inputDate);

                if (useEmailBkList.size() <= 0) {
                    break;
                }
                for (CmcUser emIdElement : useEmailBkList) {
                    curProcessingInfo = "delete cmc_user_email_bk:" + "current processing email_id:"
                            + emIdElement.getEmailID();
                    int row = 1;
                    while (row > 0) {
                        row = dao.deleteCmcUserEmailBKByEmailID(conn, emIdElement.getEmailID(), batchSize);
                        conn.commit();
                        delChildCont += row;
                        if (row < batchSize) {
                            break;
                        }
                    }

                    row = 1;
                    while (row > 0) {
                        row = dao.deleteCmcEmailBKByEmailID(conn, emIdElement.getEmailID());
                        conn.commit();
                        delParentCont += row;
                    }
                }
            }
            logInfo("[DeleteMainTableRecord] - Delete " + delChildCont + " record(s) in CMC_USER_EMAIL_BK");
            logInfo("[DeleteMainTableRecord] - Delete " + delParentCont + " record(s) in CMC_EMAIL_BK");
            logInfo("[DeleteMainTableRecord] End delete  email BK");

            // ************************************************************************************************************
            // Part 10 CMC_USER_MOBILE_MESSAGE
            // ************************************************************************************************************
            logInfo("[DeleteMainTableRecord] Start delete mobile message");
            delChildCont = 0;
            delParentCont = 0;
            for (int i = 0; i < noOfBatch; i++) {
                List<CmcUser> mobileMsgIdList = dao.getDelMobileMsgIdFromCmcUserMobileMessage(conn, startIndex,
                        batchSize, inputDate);
                if (mobileMsgIdList.size() <= 0) {
                    break;
                }
                for (CmcUser msIdElement : mobileMsgIdList) {
                    curProcessingInfo = "delete cmc_user_mobile_msg:" + "current processing mobile_msg_id:"
                            + msIdElement.getMobileMessageID();
                    int row = 1;
                    while (row > 0) {
                        row = dao.deleteCmcUserMobileMessageByMmsgID(conn, msIdElement.getMobileMessageID(), batchSize);
                        conn.commit();
                        delChildCont += row;
                        if (row < batchSize) {
                            break;
                        }
                    }

                    row = 1;
                    while (row > 0) {
                        row = dao.deleteCmcMobileMessageByMsgID(conn, msIdElement.getMobileMessageID());
                        conn.commit();
                        delParentCont += row;
                    }
                }
            }
            logInfo("[DeleteMainTableRecord] - Delete " + delChildCont + " record(s) in CMC_USER_MOBILE_MSG");
            logInfo("[DeleteMainTableRecord] - Delete " + delParentCont + " record(s) in CMC_MOBILE_MSG");
            logInfo("[DeleteMainTableRecord] End delete mobile message");

            // ************************************************************************************************************
            // Part 11 CMC_USER_MOBILE_MESSAGE_BK
            // ************************************************************************************************************
            logInfo("[DeleteMainTableRecord] Start delete mobile message BK");
            delChildCont = 0;
            delParentCont = 0;
            for (int i = 0; i < noOfBatch; i++) {
                List<CmcUser> mobileMsgBKList = dao.getDelMobileMsgIdFromCmcUserMobileMessageBk(conn, startIndex,
                        batchSize, inputDate);
                if (mobileMsgBKList.size() <= 0) {
                    break;
                }
                for (CmcUser mMsgIdElement : mobileMsgBKList) {
                    curProcessingInfo = "delete cmc_user_mobile_msg_bk:" + "current processing mobile_msg_id:"
                            + mMsgIdElement.getMobileMessageID();
                    int row = 1;
                    while (row > 0) {
                        row = dao.deleteCmcUserMobileMessageBKByMmsgID(conn, mMsgIdElement.getMobileMessageID(),
                                batchSize);
                        conn.commit();
                        delChildCont += row;
                        if (row < batchSize) {
                            break;
                        }
                    }

                    row = 1;
                    while (row > 0) {
                        row = dao.deleteCmcMobileMessageBKByMsgID(conn, mMsgIdElement.getMobileMessageID());
                        conn.commit();
                        delParentCont += row;
                    }
                }
            }
            logInfo("[DeleteMainTableRecord] - Delete " + delChildCont + " record(s) in CMC_USER_MOBILE_MSG_BK");
            logInfo("[DeleteMainTableRecord] - Delete " + delParentCont + " record(s) in CMC_MOBILE_MSG_BK");
            logInfo("[DeleteMainTableRecord] End delete mobile message BK");

            // ************************************************************************************************************
            // Part 1 CMC_USER_MESSAGE
            // ************************************************************************************************************
            logInfo("[DeleteMainTableRecord] Start delete my message");
            delChildCont = 0;
            delParentCont = 0;
            for (int i = 0; i < noOfBatch; i++) {
                List<CmcUser> MsgIDList = dao.getDelMsgIDFromCmcUserMessage(conn, startIndex, batchSize, inputDate);
                if (MsgIDList.size() <= 0) {
                    break;
                }
                for (CmcUser msIdElement : MsgIDList) {
                    curProcessingInfo = "delete cmc_user_message:" + "current processing message_id:"
                            + msIdElement.getMessageID();
                    int row = 1;
                    while (row > 0) {
                        row = dao.deleteCmcUserMessageByMsgID(conn, msIdElement.getMessageID(), batchSize);
                        conn.commit();
                        delChildCont += row;
                        if (row < batchSize) {
                            break;
                        }
                    }

                    row = 1;
                    while (row > 0) {
                        row = dao.deleteCmcMessageByMsgID(conn, msIdElement.getMessageID());
                        conn.commit();
                        delParentCont += row;
                    }
                }
            }
            logInfo("[DeleteMainTableRecord] - Delete " + delChildCont + " record(s) in CMC_USER_MESSAGE");
            logInfo("[DeleteMainTableRecord] - Delete " + delParentCont + " record(s) in CMC_MESSAGE");
            logInfo("[DeleteMainTableRecord] End delete My Message: ");

            // ************************************************************************************************************
            // Part 3 CMC_USER_TODO_ITEM
            // ************************************************************************************************************
            logInfo("[DeleteMainTableRecord] Start delete To Do Item:");
            delChildCont = 0;
            delParentCont = 0;
            int delReminderCnt = 0;
            for (int i = 0; i < noOfBatch; i++) {
                List<CmcUser> toDoItemList = dao.getDelTodoItemIdFromCmcUserToDoItem(conn, startIndex, batchSize,
                        inputDate);
                if (toDoItemList.size() <= 0) {
                    break;
                }
                for (CmcUser toDoItemEle : toDoItemList) {
                    curProcessingInfo = "delete cmc_user_to_do_item:" + "current processing to_do_item_id:"
                            + toDoItemEle.getToDoItemID();

                    int row = 1;
                    while (row > 0) {
                        row = dao.deleteCmcReminderByTodoItemID(conn, toDoItemEle.getToDoItemID(), batchSize);
                        conn.commit();
                        delReminderCnt += row;
                        if (row < batchSize) {
                            break;
                        }
                    }

                    row = 1;
                    while (row > 0) {
                        row = dao.deleteCmcUserToDoItemByToDoItemID(conn, toDoItemEle.getToDoItemID(), batchSize);
                        conn.commit();
                        delChildCont += row;
                        if (row < batchSize) {
                            break;
                        }
                    }

                    row = 1;
                    while (row > 0) {
                        row = dao.deleteCmcToDoItemByTodoItemID(conn, toDoItemEle.getToDoItemID());
                        conn.commit();
                        delParentCont += row;
                    }
                }
            }
            logInfo("[DeleteMainTableRecord] - Delete " + delReminderCnt + " record(s) in CMC_REMINDER");
            logInfo("[DeleteMainTableRecord] - Delete " + delChildCont + " record(s) in CMC_USER_TO_DO_ITEM");
            logInfo("[DeleteMainTableRecord] - Delete " + delParentCont + " record(s) in CMC_TO_DO_ITEM");
            logInfo("[DeleteMainTableRecord] End delete To Do Item");

            // ************************************************************************************************************
            // Part 5 CMC_USER_PAYMENT_TRAN
            // ************************************************************************************************************

            logInfo("[DeleteMainTableRecord] Start delete MyBill: ");
            int delUserPaymentCnt = 0;
            int delPsdoCnt = 0;
            int delPsdrCnt = 0;
            int delPosPayCnt = 0;
            int delPosPayTranSummaryCnt = 0;
            int delPrPsdrCnt = 0;
            for (int i = 0; i < noOfBatch; i++) {
                List<String> tranIdList = dao.getDelTranIdFromCmcUserPaymentTran(conn, startIndex, batchSize,
                        inputDate);
                if (tranIdList.size() <= 0) {
                    break;
                }
                for (String tranId : tranIdList) {
                    curProcessingInfo = "delete cmc_user_payment_tran:" + "current processing tran_id:" + tranId;
                    int row = 1;
                    while (row > 0) {
                        row = dao.deleteCmcUserPaymentTranByTranID(conn, tranId, batchSize);
                        conn.commit();
                        delUserPaymentCnt += row;
                        if (row < batchSize) {
                            break;
                        }
                    }
                    row = 1;
                    while (row > 0) {
                        row = dao.deletePaPosPsdoByTranID(conn, tranId, batchSize);
                        conn.commit();
                        delPsdoCnt += row;
                        if (row < batchSize) {
                            break;
                        }
                    }
                    row = 1;
                    while (row > 0) {
                        row = dao.deletePaPosPsdrByTranID(conn, tranId, batchSize);
                        conn.commit();
                        delPsdrCnt += row;
                        if (row < batchSize) {
                            break;
                        }
                    }
                    row = 1;
                    while (row > 0) {
                        row = dao.deletePaPosPayTranByTranID(conn, tranId, batchSize);
                        conn.commit();
                        delPosPayCnt += row;
                        if (row < batchSize) {
                            break;
                        }
                    }
                    row = 1;
                    while (row > 0) {
                        row = dao.deletePaPosPayTranSummaryByTranID(conn, tranId, batchSize);
                        conn.commit();
                        delPosPayTranSummaryCnt += row;
                        if (row < batchSize) {
                            break;
                        }
                    }
                    row = 1;
                    while (row > 0) {
                        row = dao.deletePrPsdrByTranID(conn, tranId, batchSize);
                        conn.commit();
                        delPrPsdrCnt += row;
                        if (row < batchSize) {
                            break;
                        }
                    }
                }
            }
            logInfo("[DeleteMainTableRecord] - Delete " + delUserPaymentCnt + " record(s) in CMC_USER_PAYMENT_TRAN");
            logInfo("[DeleteMainTableRecord] - Delete " + delPsdoCnt + " record(s) in PA_POS_PSDO");
            logInfo("[DeleteMainTableRecord] - Delete " + delPsdrCnt + " record(s) in PA_POS_PSDR");
            logInfo("[DeleteMainTableRecord] - Delete " + delPosPayCnt + " record(s) in PA_POS_PAY_TRAN");
            logInfo("[DeleteMainTableRecord] - Delete " + delPosPayTranSummaryCnt
                    + " record(s) in PA_POS_PAY_TRAN_SUMMARY");
            logInfo("[DeleteMainTableRecord] - Delete " + delPrPsdrCnt + " record(s) in PR_PSDR");
            logInfo("[DeleteMainTableRecord] End delete MyBill");

            // ************************************************************************************************************
            // Part 2 CMC_MESSAGE_FOLDER
            // ************************************************************************************************************

            logInfo("[DeleteMainTableRecord] Start delete my message folder");
            delParentCont = 0;
            for (int i = 0; i < noOfBatch; i++) {
                List<CmcUser> folderList = dao.getMessageFolderInfoForDel(conn, inputDate, startIndex, batchSize);
                if (folderList.size() <= 0) {
                    break;
                }
                for (CmcUser cmcUser : folderList) {
                    curProcessingInfo = "delete CMC_MESSAGE_FOLDER:" + "current processing folder_id:"
                            + cmcUser.getFolderID();
                    int row = 1;
                    while (row > 0) {
                        row = dao.deleteCmcMessageFolderByFolderID(conn, cmcUser.getFolderID(), batchSize);
                        conn.commit();
                        delParentCont += row;
                        if (row < batchSize) {
                            break;
                        }
                    }
                }
            }
            logInfo("[DeleteMainTableRecord] - Delete " + delParentCont + " record(s) in CMC_MESSAGE_FOLDER");
            logInfo("[DeleteMainTableRecord] End delete Message Folder:");

            // ************************************************************************************************************
            // Part 4 CMC_USER_ACC_BALANCE
            // ************************************************************************************************************

            logInfo("[DeleteMainTableRecord] Start delete Account Balance: ");
            delParentCont = 0;
            for (int i = 0; i < noOfBatch; i++) {
                int row = 1;
                while (row > 0) {
                    row = dao.deleteCmcUserAccBalanceByIssueDate(conn, inputDate, batchSize);
                    conn.commit();
                    delParentCont += row;
                    if (row < batchSize) {
                        break;
                    }
                }
            }
            logInfo("[DeleteMainTableRecord] - Delete " + delParentCont + " record(s) in CMC_USER_ACC_BALANCE");
            logInfo("[DeleteMainTableRecord] End delete Account Balance:");

            // MyGov6-C1-002 Enhance Maintain Message module to support iAM Smart message -- START
            // ************************************************************************************************************
            // Part 6 IAS_USER_MESSAGE
            // ************************************************************************************************************
            logInfo("[DeleteMainTableRecord] Start delete Ias Message");
            delChildCont = 0;
            delParentCont = 0;
            for (int i = 0; i < noOfBatch; i++) {
                List<IasUserMsg> MsgIDList = dao.getDelIasMsgIDFromIasUserMessage(conn, startIndex, batchSize,
                        iasMsgHousekeepRetentionMonths);
                if (MsgIDList.size() <= 0) {
                    break;
                }
                for (IasUserMsg msIdElement : MsgIDList) {
                    curProcessingInfo = "delete ias_user_message:" + "current processing ias_msg_id:"
                            + msIdElement.getIasMsgId();
                    int row = 1;
                    while (row > 0) {
                        row = dao.deleteIasUserMessageByIasMsgId(conn, msIdElement.getIasMsgId(), batchSize);
                        conn.commit();
                        delChildCont += row;
                        if (row < batchSize) {
                            break;
                        }
                    }

                    row = 1;
                    while (row > 0) {
                        row = dao.deleteIasMessageByIasMsgId(conn, msIdElement.getIasMsgId());
                        conn.commit();
                        delParentCont += row;
                    }
                }
            }
            logInfo("[DeleteMainTableRecord] - Delete " + delChildCont + " record(s) in IAS_USER_MESSAGE");
            logInfo("[DeleteMainTableRecord] - Delete " + delParentCont + " record(s) in IAS_MESSAGE");
            logInfo("[DeleteMainTableRecord] End delete Ias Message: ");
            // MyGov6-C1-002 Enhance Maintain Message module to support iAM Smart message -- END

            // CMC-2024-015 - Housekeep iAM Smart To-Do Item - BEGIN
            logInfo("[DeleteMainTableRecord] Start delete Ias ToDo Item");
            delChildCont = 0;
            delParentCont = 0;
            for (int i = 0; i < noOfBatch; i++) {
                List<IasUserToDoItem> toDoItemIDList = dao.getDelIasToDoItemIDFromIasUserToDoItem(conn, startIndex,
                        batchSize, iasToDoItemHousekeepRetentionMonths);
                if (toDoItemIDList.size() <= 0) {
                    break;
                }
                for (IasUserToDoItem toDoItemIdElement : toDoItemIDList) {
                    curProcessingInfo = "delete ias_user_to_do_item: current processing ias_to_do_item_id:"
                            + toDoItemIdElement.getIasToDoItemId();
                    int row = 1;
                    while (row > 0) {
                        row = dao.deleteIasUserToDoItemByIasToDoItemId(conn, toDoItemIdElement.getIasToDoItemId(),
                                batchSize);
                        conn.commit();
                        delChildCont += row;
                        if (row < batchSize) {
                            break;
                        }
                    }

                    row = 1;
                    while (row > 0) {
                        row = dao.deleteIasToDoItemByIasToDoItemId(conn, toDoItemIdElement.getIasToDoItemId());
                        conn.commit();
                        delParentCont += row;
                    }
                }
            }
            logInfo("[DeleteMainTableRecord] - Delete " + delChildCont + " record(s) in IAS_USER_TO_DO_ITEM");
            logInfo("[DeleteMainTableRecord] - Delete " + delParentCont + " record(s) in IAS_TO_DO_ITEM");
            logInfo("[DeleteMainTableRecord] End delete Ias ToDo Item");
            // CMC-2024-015 - Housekeep iAM Smart To-Do Item - END

            // CMC-2024-016 - Housekeep iAM Smart Application Status - BEGIN
            logInfo("[DeleteMainTableRecord] Start delete Ias Appliction");
            delChildCont = 0;
            delParentCont = 0;
            for (int i = 0; i < noOfBatch; i++) {
                List<IasUserApplication> applicationIDList = dao.getDelIasApplicationIDFromIasUserApplication(conn,
                        startIndex, batchSize, iasApplicationHousekeepRetentionMonths);
                if (applicationIDList.size() <= 0) {
                    break;
                }
                for (IasUserApplication applicationIdElement : applicationIDList) {
                    curProcessingInfo = "delete ias_user_to_do_item: current processing ias_to_do_item_id:"
                            + applicationIdElement.getIasApplicationId();
                    int row = 1;
                    while (row > 0) {
                        row = dao.deleteIasUserApplicationByIasApplicationId(conn,
                                applicationIdElement.getIasApplicationId(), batchSize);
                        conn.commit();
                        delChildCont += row;
                        if (row < batchSize) {
                            break;
                        }
                    }

                    row = 1;
                    while (row > 0) {
                        row = dao.deleteIasApplicationByIasApplicationId(conn,
                                applicationIdElement.getIasApplicationId());
                        conn.commit();
                        delParentCont += row;
                    }
                }
            }
            logInfo("[DeleteMainTableRecord] - Delete " + delChildCont + " record(s) in IAS_USER_APPLICATION");
            logInfo("[DeleteMainTableRecord] - Delete " + delParentCont + " record(s) in IAS_APPLICATION");
            logInfo("[DeleteMainTableRecord] End delete Ias Appliction");
            // CMC-2024-016 - Housekeep iAM Smart Application Status - END

            // release lock for concurrent control
            // if(!releaseLock)
            // {
            // if(jobControlUtils!=null)
            // {
            // jobControlUtils.releaseJobControl(jobControlName);
            // releaseLock=true;
            // }
            // }
            // }
            // else{
            // logInfo("[markHouseKeep]CMC_MARS_SYN_JOB_LOCK table cannot be locked.");
            // }

        } catch (Exception e) {
            logError("[DeleteMainTableRecord]General exception raised in DeleteMainTableRecord" + curProcessingInfo, e);
            throw new EJBException(e);
        } finally {
            if (conn != null) {
                conn.close();
            }
            // release lock for concurrent control
            // if(!releaseLock)
            // {
            // if(jobControlUtils!=null)
            // {
            // jobControlUtils.releaseJobControl(jobControlName);
            // releaseLock=true;
            // }
            // }
        }
        logInfo("[BATCH_JOB][HouseKeepingRecord]DeleteMainTableRecord - END");
    }

}
