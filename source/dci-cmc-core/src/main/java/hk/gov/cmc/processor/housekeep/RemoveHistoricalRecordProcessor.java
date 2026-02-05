package hk.gov.cmc.processor.housekeep;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import hk.gov.cmc.common.CmcAppPropertyNames;
import hk.gov.cmc.config.CmcEnvProperties;
import hk.gov.cmc.dao.housekeep.HouseKeepingRecordDAO;
import hk.gov.cmc.persistence.connection.hpfw.HPFW_Connection;
import jakarta.ejb.EJBException;

public class RemoveHistoricalRecordProcessor {
    private static Log logger = LogFactory.getLog(RemoveHistoricalRecordProcessor.class);
    private final static CmcEnvProperties cmcEnvProperties = new CmcEnvProperties();

    public RemoveHistoricalRecordProcessor() {
    }

    public static void removeHistoricalRecord(String inputDate) throws EJBException {
        logger.info("removeHistoricalRecord - START, inputDate: " + inputDate);

        HPFW_Connection conn = null;

        int batchSize = 0;
        int delNonUserTblMonthLimit = 0;
        int delArchIdxTblMonthLimit = 9999;

        try {
            conn = HPFW_Connection.getHPFW_Connection(false);
            conn.begin(null, new Timestamp(Calendar.getInstance().getTime().getTime()),
                    HPFW_Connection.DIRECT_WITH_HISTORY);
            conn.setAutoCommit(false);

            Properties properties = cmcEnvProperties.getProperties();

            batchSize = Integer.parseInt(properties.getProperty("HOUSE_KEEP_DEL_HIST_TBL_BATCH_SIZE"));
            delNonUserTblMonthLimit = Integer
                    .parseInt(properties.getProperty("HOUSE_KEEP_DEL_NON_USER_TBL_MONTH_LIMIT"));
            delArchIdxTblMonthLimit = Integer
                    .parseInt(properties.getProperty("HOUSE_KEEP_DEL_ARCHIVE_INDEX_TBL_MONTH_LIMIT"));

            HouseKeepingRecordDAO dao = new HouseKeepingRecordDAO();

            int iasMsgHousekeepRetentionMonths = Integer
                    .parseInt(properties
                            .getProperty(CmcAppPropertyNames.IAS_MSG_HOUSEKEEP_RETENTION_MONTHS_PROPERTY_NAME));

            int row = 1;
            int delCont = 0;
            while (row > 0) {
                row = dao.deleteIasUserMessageHByCreateDate(conn, iasMsgHousekeepRetentionMonths, batchSize);
                conn.commit();
                delCont += row;
                if (row < batchSize) {
                    break;
                }
            }
            logger.info("removeHistoricalRecord - Delete " + delCont + " record(s) in IAS_USER_MESSAGE_H");

            int iasToDoItemHousekeepRetentionMonths = Integer.parseInt(
                    properties.getProperty(CmcAppPropertyNames.IAS_TODO_ITEM_HOUSEKEEP_RETENTION_MONTHS_PROPERTY_NAME));
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
            logger.info("removeHistoricalRecord - Delete " + delCont + " record(s) in IAS_USER_TO_DO_ITEM_H");

            int iasApplicationHousekeepRetentionMonths = Integer.parseInt(
                    properties
                            .getProperty(CmcAppPropertyNames.IAS_APPLICATION_HOUSEKEEP_RETENTION_MONTHS_PROPERTY_NAME));
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
            logger.info("removeHistoricalRecord - Delete " + delCont + " record(s) in IAS_USER_APPLICATION_H");

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
            logger.info("removeHistoricalRecord - Delete " + delCont + " record(s) in CMC_ASYN_MESSAGE_H");

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
            logger.info("removeHistoricalRecord - Delete " + delCont + " record(s) in CMC_ASYN_MESSAGE");

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
            logger.info("removeHistoricalRecord - Delete " + iasMsgStatusCont + " record(s) in IAS_MSG_STATUS_QUEUE");

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
            logger.info("removeHistoricalRecord - Delete " + iasAssoQueueCont + " record(s) in IAS_ASSO_QUEUE");

        } catch (Exception e) {
            logger.error("removeHistoricalRecord - general exception", e);
            throw new EJBException(e);
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
        logger.info("removeHistoricalRecord - END");
    }
}
