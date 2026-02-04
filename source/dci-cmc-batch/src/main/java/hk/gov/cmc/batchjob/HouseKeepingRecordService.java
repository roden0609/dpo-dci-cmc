package hk.gov.cmc.batchjob;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.naming.InitialContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import hk.gov.cmc.appserver.ejb.session.housekeep.IHouseKeepingRecordSessionBM;
import hk.gov.gcis.rm.common.utils.PropertiesUtils;

public class HouseKeepingRecordService {

    private static Log logger = LogFactory.getLog(HouseKeepingRecordService.class);
    private Properties properties = null;

    public static void main(String[] args) throws Exception {
        try {
            if (!(args.length == 2 || args.length == 3)) {
                logger.info("usage: HouseKeepingRecordService <property file> <mode> <input_date>(optional)");
                System.exit(-1);
            }

            String inputDate = "";

            if (args.length == 2) {
                // If input_date is not entered, use today as input_date
                Date date = new Date();
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                inputDate = sdf1.format(date);
                logger.info("input date is not entered. This value will be set as today date:" + inputDate);
            } else if (args.length == 3) {
                // If input_date is entered, use entered value as input_date and perform validation
                inputDate = args[2];
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    sdf1.parse(inputDate);
                } catch (Exception e) {
                    logger.info("input date format should be 'YYYY-MM-DD'");
                    return;
                }
                logger.info("input date:" + inputDate);
            } else
                ;

            HouseKeepingRecordService service = new HouseKeepingRecordService(args[0]);
            if (args[1].equals("1")) {
                service.markHouseKeepIndicator(inputDate);
            } else if (args[1].equals("2")) {
                service.removeHistoricalRecord(inputDate);
            } else if (args[1].equals("3")) {
                service.deleteMainTableRecord(inputDate);
            }

        } catch (Exception ex) {
            // log warn instead of error if server side processing time exceed InvokerLocator timeout threshold
            if (ex.getCause() != null && ex.getCause().getMessage() != null
                    && (ex.getCause().getMessage()).indexOf("Socket timed out") > -1) {
                logger.warn("General Exception is caught: ", ex);
            } else {
                logger.error("General Exception is caught: ", ex);
            }
            throw ex;
        }
    }

    public HouseKeepingRecordService(String propfile) throws IOException {
        properties = PropertiesUtils.loadPropertiesFile(propfile);
    }

    public void markHouseKeepIndicator(String inputDate) throws Exception {
        InitialContext ctx = new InitialContext(properties);
        String houseKeepingRecordSessionEjbJndiName = properties
                .getProperty("HOUSE_KEEPING_RECORD_SESSION_EJB_JNDI_NAME");
        IHouseKeepingRecordSessionBM houseKeepingRecordSessionBM = (IHouseKeepingRecordSessionBM) ctx
                .lookup(houseKeepingRecordSessionEjbJndiName);
        houseKeepingRecordSessionBM.markHouseKeepIndicator(inputDate);
    }

    public void removeHistoricalRecord(String inputDate) throws Exception {
        InitialContext ctx = new InitialContext(properties);
        String houseKeepingRecordSessionEjbJndiName = properties
                .getProperty("HOUSE_KEEPING_RECORD_SESSION_EJB_JNDI_NAME");
        IHouseKeepingRecordSessionBM houseKeepingRecordSessionBM = (IHouseKeepingRecordSessionBM) ctx
                .lookup(houseKeepingRecordSessionEjbJndiName);
        houseKeepingRecordSessionBM.removeHistoricalRecord(inputDate);
    }

    public void deleteMainTableRecord(String inputDate) throws Exception {
        InitialContext ctx = new InitialContext(properties);
        String houseKeepingRecordSessionEjbJndiName = properties
                .getProperty("HOUSE_KEEPING_RECORD_SESSION_EJB_JNDI_NAME");
        IHouseKeepingRecordSessionBM houseKeepingRecordSessionBM = (IHouseKeepingRecordSessionBM) ctx
                .lookup(houseKeepingRecordSessionEjbJndiName);
        houseKeepingRecordSessionBM.deleteMainTableRecord(inputDate);

    }
}
