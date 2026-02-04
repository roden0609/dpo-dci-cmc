package hk.gov.cmc.processor.housekeep;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import hk.gov.cmc.common.CmcAppPropertyNames;
import hk.gov.cmc.config.CmcEnvProperties;
import hk.gov.cmc.persistence.connection.hpfw.HPFW_Connection;
import jakarta.ejb.EJBException;

public class MarkHousekeepIndProcessor {
    
    private static Log logger = LogFactory.getLog(MarkHousekeepIndProcessor.class);
    private final static CmcEnvProperties cmcEnvProperties = new CmcEnvProperties();

    public MarkHousekeepIndProcessor() {
    }

    public static void markHouseKeepIndicator(String inputDate) throws EJBException {
        logger.info("markHouseKeepIndicator - START");
        logger.info("markHouseKeepIndicator - inputDate: " + inputDate);

        HPFW_Connection conn = null;

        int delMsgMonthLimit = 9999;
        int markTrashedDelMymsgMonthLimit = 9999;
        int batchSize = 9999;
        int row = 0;
        int updateCount = 0;

        int iasMsgHousekeepRetentionMonths = 9999;
        int iasToDoItemHousekeepRetentionMonths = 9999;
        int iasApplicationHousekeepRetentionMonths = 9999;

        try {
            conn = HPFW_Connection.getHPFW_Connection(false);
            conn.begin(null, new Timestamp(Calendar.getInstance().getTime().getTime()),
                    HPFW_Connection.DIRECT_WITH_HISTORY);
            conn.setAutoCommit(false);

            Properties properties = cmcEnvProperties.getProperties();

            delMsgMonthLimit = Integer.parseInt(properties.getProperty("HOUSE_KEEP_DEL_MSG_CREATED_MONTH_LIMIT"));

            iasMsgHousekeepRetentionMonths = Integer
                    .parseInt(properties.getProperty(CmcAppPropertyNames.IAS_MSG_HOUSEKEEP_RETENTION_MONTHS_PROPERTY_NAME));
            iasToDoItemHousekeepRetentionMonths = Integer.parseInt(
                    properties.getProperty(CmcAppPropertyNames.IAS_TODO_ITEM_HOUSEKEEP_RETENTION_MONTHS_PROPERTY_NAME));
            iasApplicationHousekeepRetentionMonths = Integer.parseInt(
                    properties.getProperty(CmcAppPropertyNames.IAS_APPLICATION_HOUSEKEEP_RETENTION_MONTHS_PROPERTY_NAME));
            markTrashedDelMymsgMonthLimit = Integer
                    .parseInt(properties.getProperty(CmcAppPropertyNames.MARK_TRASHED_DEL_MYMSG_MONTH_LIMIT_PROPERTY_NAME));
            batchSize = Integer.parseInt(properties.getProperty("HOUSE_KEEP_DEL_HIST_TBL_BATCH_SIZE"));
            SimpleDateFormat dateChkFomatter = new SimpleDateFormat("yyyy-MM-dd");
            Calendar inputDateCalendar = Calendar.getInstance();
            inputDateCalendar.setTime(dateChkFomatter.parse(inputDate));
            inputDateCalendar.add(Calendar.MONTH, +delMsgMonthLimit);
            if (inputDateCalendar.getTime().after(new Date())) {
                logger.info("markHouseKeepIndicator - InputDate should be before or equal to two years ago!");
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
            updateCount = 0;
            while (row > 0) {
                row = dao.updateCmcUserMessageByBatch(conn, inputDate, batchSize);
                conn.commit();
                updateCount += row;
                if (row < batchSize) {
                    break;
                }
            }
            logInfo("[markHouseKeep] - Update " + updateCount + " record(s) in CMC_USER_MESSAGE");
            logInfo("[markHouseKeep] CMC_USER_MESSAGE END");

            // MyGov5-CMC-S012: House keeping trash message and working table -- START
            // Part 1.5
            row = 1;
            updateCount = 0;
            while (row > 0) {
                row = dao.updateTrashCmcUserMessageByBatch(conn, markTrashedDelMymsgMonthLimit, batchSize);
                conn.commit();
                updateCount += row;
                if (row < batchSize) {
                    break;
                }
            }
            logInfo("[markHouseKeep] - Update " + updateCount + " trash record(s) in CMC_USER_MESSAGE");
            logInfo("[markHouseKeep] CMC_USER_MESSAGE TRASH END");
            // MyGov5-CMC-S012: House keeping trash message and working table -- END

            // Part 2
            row = 1;
            updateCount = 0;
            while (row > 0) {
                row = dao.updateCmcUserToDoItemByBatch(conn, inputDate, batchSize);
                conn.commit();
                updateCount += row;
                if (row < batchSize) {
                    break;
                }
            }
            logInfo("[markHouseKeep] - Update " + updateCount + " record(s) in CMC_USER_TO_DO_ITEM");
            logInfo("[markHouseKeep] CMC_USER_TO_DO_ITEM END");

            // Part 3
            row = 1;
            updateCount = 0;
            while (row > 0) {
                row = dao.updateCmcUserAccBalanceByBatch(conn, inputDate, batchSize);
                conn.commit();
                updateCount += row;
                if (row < batchSize) {
                    break;
                }
            }
            logInfo("[markHouseKeep] - Update " + updateCount + " record(s) in CMC_USER_ACC_BALANCE");
            logInfo("[markHouseKeep] CMC_USER_ACC_BALANCE END");

            // Part 4
            row = 1;
            updateCount = 0;
            while (row > 0) {
                row = dao.updateCmcUserPaymentTranByBatch(conn, inputDate, batchSize);
                conn.commit();
                updateCount += row;
                if (row < batchSize) {
                    break;
                }
            }
            logInfo("[markHouseKeep] - Update " + updateCount + " record(s) in CMC_USER_PAYMENT_TRAN");
            logInfo("[markHouseKeep] CMC_USER_PAYMENT_TRAN END");

            // Part 5a
            row = 1;
            updateCount = 0;
            while (row > 0) {
                row = dao.updateCmcUserEmailByBatch(conn, inputDate, batchSize);
                conn.commit();
                updateCount += row;
                if (row < batchSize) {
                    break;
                }
            }
            logInfo("[markHouseKeep] - Update " + updateCount + " record(s) in CMC_USER_EMAIL");
            logInfo("[markHouseKeep] CMC_USER_EMAIL END");

            // Part 5b
            conn.setUpdateMode(HPFW_Connection.DIRECT);
            row = 1;
            updateCount = 0;
            while (row > 0) {
                row = dao.updateCmcUserEmailBkByBatch(conn, inputDate, batchSize);
                conn.commit();
                updateCount += row;
                if (row < batchSize) {
                    break;
                }
            }
            logInfo("[markHouseKeep] - Update " + updateCount + " record(s) in CMC_USER_EMAIL_BK");
            logInfo("[markHouseKeep] CMC_USER_EMAIL_BK END");

            // Part 6a
            conn.setUpdateMode(HPFW_Connection.DIRECT_WITH_HISTORY);
            row = 1;
            updateCount = 0;
            while (row > 0) {
                row = dao.updateCmcUserMobileMsgByBatch(conn, inputDate, batchSize);
                conn.commit();
                updateCount += row;
                if (row < batchSize) {
                    break;
                }
            }
            logInfo("[markHouseKeep] - Update " + updateCount + " record(s) in CMC_USER_MOBILE_MSG");
            logInfo("[markHouseKeep] CMC_USER_MOBILE_MSG END");

            // Part 6b
            conn.setUpdateMode(HPFW_Connection.DIRECT);
            row = 1;
            updateCount = 0;
            while (row > 0) {
                row = dao.updateCmcUserMobileMsgBkByBatch(conn, inputDate, batchSize);
                conn.commit();
                updateCount += row;
                if (row < batchSize) {
                    break;
                }
            }
            logInfo("[markHouseKeep] - Update " + updateCount + " record(s) in CMC_USER_MOBILE_MSG_BK");
            logInfo("[markHouseKeep] CMC_USER_MOBILE_MSG_BK END");

            // MyGov6-C1-002 Enhance Maintain Message module to support iAM Smart message -- START
            conn.setUpdateMode(HPFW_Connection.DIRECT_WITH_HISTORY);
            row = 1;
            updateCount = 0;
            while (row > 0) {
                row = dao.updateIasUserMessageByBatch(conn, iasMsgHousekeepRetentionMonths, batchSize);
                conn.commit();
                updateCount += row;
                if (row < batchSize) {
                    break;
                }
            }
            logInfo("[markHouseKeep] - Update " + updateCount + " record(s) in IAS_USER_MESSAGE");
            logInfo("[markHouseKeep] IAS_USER_MESSAGE END");
            // MyGov6-C1-002 Enhance Maintain Message module to support iAM Smart message -- END

            // CMC-2024-015 - Housekeep iAM Smart To-Do Item - BEGIN
            conn.setUpdateMode(HPFW_Connection.DIRECT_WITH_HISTORY);
            row = 1;
            updateCount = 0;
            while (row > 0) {
                row = dao.updateIasUserToDoItemByBatch(conn, iasToDoItemHousekeepRetentionMonths, batchSize);
                conn.commit();
                updateCount += row;
                if (row < batchSize) {
                    break;
                }
            }
            logInfo("[markHouseKeep] - Update " + updateCount + " record(s) in IAS_USER_TO_DO_ITEM");
            logInfo("[markHouseKeep] IAS_USER_TO_DO_ITEM END");
            // CMC-2024-015 - Housekeep iAM Smart To-Do Item - END

            // CMC-2024-016 - Housekeep iAM Smart Application Status - BEGIN
            conn.setUpdateMode(HPFW_Connection.DIRECT_WITH_HISTORY);
            row = 1;
            updateCount = 0;
            while (row > 0) {
                row = dao.updateIasUserApplicationByBatch(conn, iasApplicationHousekeepRetentionMonths, batchSize);
                conn.commit();
                updateCount += row;
                if (row < batchSize) {
                    break;
                }
            }
            logInfo("[markHouseKeep] - Update " + updateCount + " record(s) in IAS_USER_APPLICATION");
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
}
