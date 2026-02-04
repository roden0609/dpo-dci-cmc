package hk.gov.cmc.processor.housekeep;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import hk.gov.cmc.common.CmcAppPropertyNames;
import hk.gov.cmc.config.CmcEnvProperties;
import hk.gov.cmc.dao.housekeep.HouseKeepingRecordDAO;
import hk.gov.cmc.model.maintainmessage.application.IasUserApplication;
import hk.gov.cmc.model.maintainmessage.emessage.IasUserMsg;
import hk.gov.cmc.model.maintainmessage.todoitem.IasUserToDoItem;
import hk.gov.cmc.persistence.connection.hpfw.HPFW_Connection;
import jakarta.ejb.EJBException;

public class DeleteMainTableRecordProcessor {

    private static Log logger = LogFactory.getLog(DeleteMainTableRecordProcessor.class);
    private final static CmcEnvProperties cmcEnvProperties = new CmcEnvProperties();

    public DeleteMainTableRecordProcessor() {
    }

    public static void deleteMainTableRecord(String inputDate) {
        logger.info("deleteMainTableRecord - START, inputDate: " + inputDate);

        HPFW_Connection conn = null;

        int batchSize = 0;

        int startIndex = 1;
        int noOfBatch = 999;
        String curProcessingInfo = "";

        int iasMsgHousekeepRetentionMonths = 24;
        int iasToDoItemHousekeepRetentionMonths = 24;
        int iasApplicationHousekeepRetentionMonths = 24;

        try {
            conn = HPFW_Connection.getHPFW_Connection(false);
            conn.begin(null, new Timestamp(Calendar.getInstance().getTime().getTime()),
                    HPFW_Connection.DIRECT_WITH_HISTORY);
            conn.setAutoCommit(false);

            Properties properties = cmcEnvProperties.getProperties();

            batchSize = Integer.parseInt(properties.getProperty("HOUSE_KEEP_DEL_HIST_TBL_BATCH_SIZE"));
            noOfBatch = Integer.parseInt(properties.getProperty("HOUSE_KEEP_NO_OF_BATCH"));

            iasMsgHousekeepRetentionMonths = Integer
                    .parseInt(properties
                            .getProperty(CmcAppPropertyNames.IAS_MSG_HOUSEKEEP_RETENTION_MONTHS_PROPERTY_NAME));
            iasToDoItemHousekeepRetentionMonths = Integer.parseInt(
                    properties.getProperty(CmcAppPropertyNames.IAS_TODO_ITEM_HOUSEKEEP_RETENTION_MONTHS_PROPERTY_NAME));
            iasApplicationHousekeepRetentionMonths = Integer.parseInt(
                    properties
                            .getProperty(CmcAppPropertyNames.IAS_APPLICATION_HOUSEKEEP_RETENTION_MONTHS_PROPERTY_NAME));

            HouseKeepingRecordDAO dao = new HouseKeepingRecordDAO();

            logger.info("deleteMainTableRecord - Start delete Ias Message");
            int delChildCont = 0;
            int delParentCont = 0;
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
            logger.info("deleteMainTableRecord - Delete " + delChildCont + " record(s) in IAS_USER_MESSAGE");
            logger.info("deleteMainTableRecord - Delete " + delParentCont + " record(s) in IAS_MESSAGE");
            logger.info("deleteMainTableRecord - End delete Ias Message");

            logger.info("deleteMainTableRecord - Start delete Ias ToDo Item");
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
            logger.info("deleteMainTableRecord - - Delete " + delChildCont + " record(s) in IAS_USER_TO_DO_ITEM");
            logger.info("deleteMainTableRecord - Delete " + delParentCont + " record(s) in IAS_TO_DO_ITEM");
            logger.info("deleteMainTableRecord - End delete Ias ToDo Item");

            logger.info("deleteMainTableRecord - Start delete Ias Appliction");
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
            logger.info("deleteMainTableRecord - Delete " + delChildCont + " record(s) in IAS_USER_APPLICATION");
            logger.info("deleteMainTableRecord - Delete " + delParentCont + " record(s) in IAS_APPLICATION");
            logger.info("deleteMainTableRecord - End delete Ias Appliction");

        } catch (Exception e) {
            logger.error("deleteMainTableRecord - general exception, curProcessingInfo: " + curProcessingInfo, e);
            throw new EJBException(e);
        } finally {
            if (conn != null) {
                conn.close();
            }
        }

        logger.info("deleteMainTableRecord - END");
    }
}
