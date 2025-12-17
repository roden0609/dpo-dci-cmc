package hk.gov.cmc.persistence.connection.hpfw;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.Calendar;

public class JobThread extends Thread {

    private static Log logger = LogFactory.getLog(JobThread.class);

    private static long sleep_time = 60000; // 60000 sleep 1 minutes for each execution
    public String jobName = "";
    public String mode = "";
    private boolean threadEnabled = false;
    private Calendar lastSyncTime = null;

    public JobThread() {
        super();
    }

    public void init(String jobName, String mode) {
        this.jobName = jobName;
        this.mode = mode;
    }

    public void run() {
        try {

            while (true) {
                sleep(sleep_time);

                try {

                    if (HistSynJob.isEnabled()) {
                        if (!threadEnabled) {
                            logger.info("Sync JobThread is ENABLED. jobName[" + jobName + "]");
                            threadEnabled = true;
                        }
                        HistSynJob.syn(jobName, mode);
                    } else {
                        if (threadEnabled) {
                            logger.info("Sync JobThread is DISABLED. jobName[" + jobName + "]");
                            threadEnabled = false;
                        }
                    }

                    Calendar currentTime = Calendar.getInstance();
                    // if lastSyncTime is over 30 minutes
                    if ((lastSyncTime == null)
                            || (currentTime.getTimeInMillis() - lastSyncTime.getTimeInMillis() > (30 * 60 * 1000))) {
                        logger.info("Sync JobThread is running. threadEnabled[" + threadEnabled + "], jobName["
                                + jobName + "]");
                        lastSyncTime = currentTime;
                    }
                } catch (Exception ex) {
                    logger.warn("Sync JobThread has General Exception jobName[" + jobName + "]", ex);
                }
            }
        } catch (InterruptedException e) {
            logger.error("Sync JobThread is DEAD. InterruptedException jobName[" + jobName + "]", e);
        } catch (Throwable t) {
            logger.error("Sync JobThread is DEAD. Throwable jobName[" + jobName + "]", t);
        }
    }
}
