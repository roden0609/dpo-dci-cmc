package hk.gov.cmc.appserver.ejb.session.housekeep;

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

    public void markHouseKeepIndicator(String inputDate) throws EJBException {
        logInfo("[BATCH_JOB][HouseKeepingRecord]markHouseKeepIndicator - START, inputDate: " + inputDate);

        MarkHousekeepIndProcessor.markHouseKeepIndicator(inputDate);

        logInfo("[BATCH_JOB][HouseKeepingRecord]markHouseKeepIndicator - END");
    }

    public void removeHistoricalRecord(String inputDate) throws EJBException {
        logInfo("[BATCH_JOB][HouseKeepingRecord]removeHistoricalRecord - START, inputDate: " + inputDate);

        RemoveHistoricalRecordProcessor.removeHistoricalRecord(inputDate);

        logInfo("[BATCH_JOB][HouseKeepingRecord]removeHistoricalRecord - END");
    }

    public void deleteMainTableRecord(String inputDate) throws EJBException {
        logInfo("[BATCH_JOB][HouseKeepingRecord]DeleteMainTableRecord - START " + inputDate);

        DeleteMainTableRecordProcessor.deleteMainTableRecord(inputDate);

        logInfo("[BATCH_JOB][HouseKeepingRecord]DeleteMainTableRecord - END ");
    }

}
