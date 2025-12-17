package hk.gov.cmc.config;

import hk.gov.cmc.persistence.connection.hpfw.HPFW_Connection;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Queue;
import java.util.Stack;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CmcSystemParam extends TimerTask {

    private static final String SQL_SELECT_CMC_SYSTEM_PARAM = "select PARA_NAME, PARA_VALUE, EFFECTIVE_DATE from CMC_SYSTEM_PARAM order by PARA_NAME, EFFECTIVE_DATE ASC";
    private static Log log = LogFactory.getLog(CmcSystemParam.class);
    private static ConcurrentHashMap<String, Stack<ParaValue>> pastParaCache = new ConcurrentHashMap<String, Stack<ParaValue>>();
    private static ConcurrentHashMap<String, Queue<ParaValue>> futureParaCache = new ConcurrentHashMap<String, Queue<ParaValue>>();
    private static String MUTEX = "MUTEX";

    static class ParaValue {
        String value;
        long effectTime;

        ParaValue(String value, long effectTime) {
            this.value = value;
            this.effectTime = effectTime;
        }
    }

    @Override
    public void run() {
        reload();
    }

    @Override
    protected void finalize() throws Throwable {
        pastParaCache.clear();
        pastParaCache = null;
    }

    public static void reload() {
        log.info("reloading CmcSystemParam start");

        ConcurrentHashMap<String, Stack<ParaValue>> newPastParaCache = new ConcurrentHashMap<String, Stack<ParaValue>>();
        ConcurrentHashMap<String, Queue<ParaValue>> newFutureParaCache = new ConcurrentHashMap<String, Queue<ParaValue>>();
        HPFW_Connection conn = null;
        ResultSet rs = null;
        long now = System.currentTimeMillis();
        try {
            conn = HPFW_Connection.getHPFW_Connection();
            rs = conn.getResultSet(SQL_SELECT_CMC_SYSTEM_PARAM, null);
            while (rs.next()) {
                String key = rs.getString(1);
                String value = rs.getString(2);
                Timestamp effectTime = rs.getTimestamp(3);

                Queue<ParaValue> futureQueue = newFutureParaCache.get(key);
                if (futureQueue == null) {
                    futureQueue = new ConcurrentLinkedQueue<ParaValue>();
                    newFutureParaCache.put(key, futureQueue);
                }

                Stack<ParaValue> passStack = newPastParaCache.get(key);
                if (passStack == null) {
                    passStack = new Stack<ParaValue>();
                    newPastParaCache.put(key, passStack);
                }

                if (now < effectTime.getTime()) {
                    futureQueue.add(new ParaValue(value, effectTime.getTime()));
                } else {
                    passStack.add(new ParaValue(value, effectTime.getTime()));
                }
            }
            synchronized (MUTEX) {
                pastParaCache = newPastParaCache;
                futureParaCache = newFutureParaCache;
            }
        } catch (Exception e) {
            log.error("reloading CmcSystemParam error:", e);
        } finally {
            if (rs != null)
                try {
                    rs.close();
                } catch (Exception ignored) {
                }
            HPFW_Connection.close(conn);
            log.info("reloading CmcSystemParam end");
        }
    }

    public static int getIntPara(String key, int defaultValue) {
        String para = getPara(key);
        if (para == null || para.trim().equals("")) {
            return defaultValue;
        } else {
            try {
                return Integer.parseInt(para);
            } catch (NumberFormatException ne) {
                return defaultValue;
            }
        }
    }

    public static int getIntPara(String key) {
        return Integer.parseInt(getPara(key));
    }

    public static String getPara(String key, String defaultValue) {
        String para = getPara(key);
        if (para == null || para.trim().equals("")) {
            return defaultValue;
        } else {
            return para;
        }
    }

    public static String getPara(String key) {
        long now = System.currentTimeMillis();
        synchronized (MUTEX) {
            Stack<ParaValue> pastStack = pastParaCache.get(key);
            if (pastStack == null)
                return null;

            Queue<ParaValue> futureQueue = futureParaCache.get(key);
            ParaValue next = futureQueue.peek();
            while (true) {
                if (next != null && next.effectTime <= now) {
                    pastStack.add(futureQueue.poll());
                    next = futureQueue.peek();
                } else {
                    break;
                }
            }
            ParaValue current = pastStack.peek();
            return current == null ? null : current.value;
        }
    }
}
