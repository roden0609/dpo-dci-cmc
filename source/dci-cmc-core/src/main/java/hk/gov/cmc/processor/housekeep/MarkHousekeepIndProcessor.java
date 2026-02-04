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
import hk.gov.cmc.dao.housekeep.HouseKeepingRecordDAO;
import hk.gov.cmc.persistence.connection.hpfw.HPFW_Connection;
import jakarta.ejb.EJBException;

public class MarkHousekeepIndProcessor {

    private static Log logger = LogFactory.getLog(MarkHousekeepIndProcessor.class);
    private final static CmcEnvProperties cmcEnvProperties = new CmcEnvProperties();

    public MarkHousekeepIndProcessor() {
    }

    public static void markHouseKeepIndicator(String inputDate) throws EJBException {
        logger.info("markHouseKeepIndicator - START, inputDate: " + inputDate);

        HPFW_Connection conn = null;

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

            iasMsgHousekeepRetentionMonths = Integer
                    .parseInt(properties
                            .getProperty(CmcAppPropertyNames.IAS_MSG_HOUSEKEEP_RETENTION_MONTHS_PROPERTY_NAME));
            iasToDoItemHousekeepRetentionMonths = Integer.parseInt(
                    properties.getProperty(CmcAppPropertyNames.IAS_TODO_ITEM_HOUSEKEEP_RETENTION_MONTHS_PROPERTY_NAME));
            iasApplicationHousekeepRetentionMonths = Integer.parseInt(
                    properties
                            .getProperty(CmcAppPropertyNames.IAS_APPLICATION_HOUSEKEEP_RETENTION_MONTHS_PROPERTY_NAME));
            batchSize = Integer.parseInt(properties.getProperty("HOUSE_KEEP_DEL_HIST_TBL_BATCH_SIZE"));

            HouseKeepingRecordDAO dao = new HouseKeepingRecordDAO();

            logger.info("markHouseKeepIndicator - IAS_USER_MESSAGE - START");
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
            logger.info("markHouseKeepIndicator - Update " + updateCount + " record(s) in IAS_USER_MESSAGE");
            logger.info("markHouseKeepIndicator - IAS_USER_MESSAGE - END");

            logger.info("markHouseKeepIndicator - IAS_USER_TO_DO_ITEM - START");
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
            logger.info("markHouseKeepIndicator - Update " + updateCount + " record(s) in IAS_USER_TO_DO_ITEM");
            logger.info("markHouseKeepIndicator - IAS_USER_TO_DO_ITEM - END");

            logger.info("markHouseKeepIndicator - IAS_USER_APPLICATION - START");
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
            logger.info("markHouseKeepIndicator - Update " + updateCount + " record(s) in IAS_USER_APPLICATION");
            logger.info("markHouseKeepIndicator - IAS_USER_APPLICATION - END");
        } catch (Exception e) {
            logger.error("markHouseKeepIndicator - general exception", e);
            throw new EJBException(e);
        } finally {
            if (conn != null)
                conn.close();
        }
        logger.info("markHouseKeepIndicator - END");
    }
}
