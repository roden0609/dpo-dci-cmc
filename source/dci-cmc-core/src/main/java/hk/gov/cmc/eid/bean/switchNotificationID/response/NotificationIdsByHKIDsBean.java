package hk.gov.cmc.eid.bean.switchNotificationID.response;

import java.io.Serializable;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class NotificationIdsByHKIDsBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private ArrayList<NotificationIdByHKIDItem> notificationIDs;

    private static final Log log = LogFactory.getLog(NotificationIdsByHKIDsBean.class);

    public NotificationIdsByHKIDsBean() {
    }

    public NotificationIdsByHKIDsBean(String content) {
        log.debug("NotificationIdsByHKIDsBean content: " + content);
        ArrayList<NotificationIdByHKIDItem> notiArray = new ArrayList<NotificationIdByHKIDItem>();
        JsonObject j = new JsonParser().parse(content).getAsJsonObject();
        JsonArray jArray = j.getAsJsonArray("notificationIDs");

        for (int i = 0; i < jArray.size(); i++) {
            JsonObject x = jArray.get(i).getAsJsonObject();
            String status = getJsonString(x, "status");
            String clientID = getJsonString(x, "clientID");
            String HKID = getJsonString(x, "HKID");
            String notificationID = getJsonString(x, "notificationID");
            NotificationIdByHKIDItem n = new NotificationIdByHKIDItem(status, clientID, HKID, notificationID);
            notiArray.add(n);
        }
        this.notificationIDs = notiArray;
    }

    public ArrayList<NotificationIdByHKIDItem> getNotificationIDs() {
        return this.notificationIDs;
    }

    public void setNotificationIDs(ArrayList<NotificationIdByHKIDItem> n) {
        this.notificationIDs = n;
    }

    private static String getJsonString(JsonObject jsonobj, String tagname) {
        String resultStr;
        Object tag = jsonobj.get(tagname);
        if (tag != null) {
            resultStr = (String) tag.toString().replace("\"", "");
        } else {
            resultStr = null;
        }
        return resultStr;
    }

}
