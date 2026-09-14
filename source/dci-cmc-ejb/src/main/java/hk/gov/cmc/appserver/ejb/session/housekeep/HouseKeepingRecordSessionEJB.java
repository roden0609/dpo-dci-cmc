package hk.gov.cmc.appserver.ejb.session.housekeep;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import hk.gov.cmc.processor.housekeep.DeleteMainTableRecordProcessor;
import hk.gov.cmc.processor.housekeep.MarkHousekeepIndProcessor;
import hk.gov.cmc.processor.housekeep.RemoveHistoricalRecordProcessor;
import hk.gov.gcis.rm.common.javaee.ejb.EJBBase;
import jakarta.ejb.EJBException;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionManagement;
import jakarta.ejb.TransactionManagementType;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class HouseKeepingRecordSessionEJB extends EJBBase
        implements IHouseKeepingRecordSessionBMLocal, IHouseKeepingRecordSessionBMRemote {

    private static Log logger = LogFactory.getLog(HouseKeepingRecordSessionEJB.class);

    public void markHouseKeepIndicator() throws EJBException {
        logger.info("[BATCH_JOB][HouseKeepingRecord]markHouseKeepIndicator - START");

        MarkHousekeepIndProcessor.markHouseKeepIndicator();

        logger.info("[BATCH_JOB][HouseKeepingRecord]markHouseKeepIndicator - END");
    }

    public void removeHistoricalRecord(String inputDate) throws EJBException {
        logger.info("[BATCH_JOB][HouseKeepingRecord]removeHistoricalRecord - START, inputDate: " + inputDate);

        RemoveHistoricalRecordProcessor.removeHistoricalRecord(inputDate);

        logger.info("[BATCH_JOB][HouseKeepingRecord]removeHistoricalRecord - END");
    }

    public void deleteMainTableRecord() throws EJBException {
        logger.info("[BATCH_JOB][HouseKeepingRecord]DeleteMainTableRecord - START");

        DeleteMainTableRecordProcessor.deleteMainTableRecord();

        logger.info("[BATCH_JOB][HouseKeepingRecord]DeleteMainTableRecord - END ");
    }

}
