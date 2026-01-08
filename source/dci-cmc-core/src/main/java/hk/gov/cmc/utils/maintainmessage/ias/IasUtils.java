package hk.gov.cmc.utils.maintainmessage.ias;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import hk.gov.cmc.eid.bean.NotificationBean;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;

public class IasUtils {

    private static Log logger = LogFactory.getLog(IasUtils.class);

    private static int runningNumber = 0;
    private static String preRunningTime = "";
    private static final DateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

    public static synchronized String getNextIasMsgId(String serverId) {
        String sequenceString = "";
        String currRunningTime = sdf.format(new Date());

        if ("".equals(preRunningTime)) {
            preRunningTime = currRunningTime;
        }

        if (preRunningTime.equals(currRunningTime)) {
            if (runningNumber >= 999) {
                wait(1000);
                currRunningTime = sdf.format(new Date());
            } else {
                runningNumber = runningNumber + 1;
                sequenceString = currRunningTime + StringUtils.leftPad(String.valueOf(runningNumber), 3, "0")
                        + serverId;
            }
        }

        if (!preRunningTime.equals(currRunningTime)) {
            runningNumber = 1;
            sequenceString = currRunningTime + StringUtils.leftPad(String.valueOf(runningNumber), 3, "0") + serverId;
            preRunningTime = currRunningTime;
        }

        return sequenceString;
    }

    public static synchronized String getNextIasToDoItemId(String serverId) {
        String sequenceString = "";
        String currRunningTime = sdf.format(new Date());

        if ("".equals(preRunningTime)) {
            preRunningTime = currRunningTime;
        }

        if (preRunningTime.equals(currRunningTime)) {
            if (runningNumber >= 999) {
                wait(1000);
                currRunningTime = sdf.format(new Date());
            } else {
                runningNumber = runningNumber + 1;
                sequenceString = currRunningTime + StringUtils.leftPad(String.valueOf(runningNumber), 3, "0")
                        + serverId;
            }
        }

        if (!preRunningTime.equals(currRunningTime)) {
            runningNumber = 1;
            sequenceString = currRunningTime + StringUtils.leftPad(String.valueOf(runningNumber), 3, "0") + serverId;
            preRunningTime = currRunningTime;
        }

        return sequenceString;
    }

    public static synchronized String getNextIasApplicationId(String serverId) {
        String sequenceString = "";
        String currRunningTime = sdf.format(new Date());

        if ("".equals(preRunningTime)) {
            preRunningTime = currRunningTime;
        }

        if (preRunningTime.equals(currRunningTime)) {
            if (runningNumber >= 999) {
                wait(1000);
                currRunningTime = sdf.format(new Date());
            } else {
                runningNumber = runningNumber + 1;
                sequenceString = currRunningTime + StringUtils.leftPad(String.valueOf(runningNumber), 3, "0")
                        + serverId;
            }
        }

        if (!preRunningTime.equals(currRunningTime)) {
            runningNumber = 1;
            sequenceString = currRunningTime + StringUtils.leftPad(String.valueOf(runningNumber), 3, "0") + serverId;
            preRunningTime = currRunningTime;
        }

        return sequenceString;
    }

    private static void wait(int mSeconds) {
        try {
            Thread.sleep(mSeconds);
        } catch (InterruptedException e) {
            logger.error(e);
        }
    }

    public static String getNotificationBeanStr(NotificationBean notificationBean) {
        String result = "";

        try {
            JsonbConfig config = new JsonbConfig()
                    .withFormatting(true);

            Jsonb jsonb = JsonbBuilder.create(config);
            return jsonb.toJson(notificationBean);
        } catch (Throwable t) {
            result = t.getMessage();
        }

        return result;
    }
}
