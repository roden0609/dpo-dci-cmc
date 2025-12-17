

package hk.gov.cmc.eid.bean;


import java.io.Serializable;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class NotificationIdsBean implements Serializable
{

    
    private static final long serialVersionUID = 1L;

    private ArrayList<NotificationIdItem> notificationIDs;

    private static final Log log = LogFactory.getLog(NotificationIdsBean.class);

    public NotificationIdsBean() {}

    public NotificationIdsBean(String content) {
        log.info("NotificationIdsBean content: " + content);
        ArrayList<NotificationIdItem> notiArray = new ArrayList<NotificationIdItem>();
        JsonObject j = new JsonParser().parse(content).getAsJsonObject();
        JsonArray jArray = j.getAsJsonArray("notificationIDs");

        for (int i=0; i < jArray.size(); i++) {
            JsonObject x = jArray.get(i).getAsJsonObject();
            String status = getJsonString(x, "status");
            String clientID = getJsonString(x, "clientID");
            String openID = getJsonString(x, "openID");
            String notificationID = getJsonString(x, "notificationID");
            NotificationIdItem n = new NotificationIdItem(status, clientID, openID, notificationID);
            notiArray.add(n);
        }
        this.notificationIDs = notiArray;
    }

    public ArrayList<NotificationIdItem> getNotificationIDs() {
        return this.notificationIDs;
    }

    public void setNotificationIDs(ArrayList<NotificationIdItem> n) {
        this.notificationIDs = n;
    }

    private static String getJsonString(JsonObject jsonobj, String tagname) {
        String resultStr;
        Object tag = jsonobj.get(tagname);
        if (tag != null) {
            resultStr = (String)tag.toString().replace("\"", "");
        } else {
            resultStr = null;
        }
        return resultStr;
    }

    public void printBean() {
        int i= 0;
        for ( NotificationIdItem t : this.notificationIDs) {
            System.out.println("notificationIDs Item#" + i + ":" +
                               "ClientId=" + t.getClientID() +
                               ",OpenId=" + t.getOpenID() +
                               ",NotificationID=" + t.getNotificationID() +
                               ",Status=" + t.getStatus());
          i++;
        }
    }

}
