


package hk.gov.cmc.eid.bean;


import java.io.Serializable;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class NotificationResultsBean implements Serializable
{

    
    private static final long serialVersionUID = 1L;

    private ArrayList<NotificationResultItem> notificationResults;

    public NotificationResultsBean() {}

    public NotificationResultsBean(String content) {
        ArrayList<NotificationResultItem> notiArray = new ArrayList<NotificationResultItem>();
        JsonObject j = new JsonParser().parse(content).getAsJsonObject();
        JsonArray jArray = j.getAsJsonArray("notificationResults");

        for (int i=0; i < jArray.size(); i++) {
            JsonObject x = jArray.get(i).getAsJsonObject();
            String status = getJsonString(x, "status");
            String messageID = getJsonString(x, "messageID");
            String notificationID = getJsonString(x, "notificationID");
            NotificationResultItem n = new NotificationResultItem(notificationID, messageID, status );
            notiArray.add(n);
        }
        this.notificationResults = notiArray;
    }

    public ArrayList<NotificationResultItem> getNotificationResults() {
        return this.notificationResults;
    }

    public void setNotificationResults(ArrayList<NotificationResultItem> n) {
        this.notificationResults = n;
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
        for ( NotificationResultItem t : this.notificationResults) {
            System.out.println("notificationResults Item#" + i + ":" +
                               "notificationID=" + t.getNotificationID() +
                               ",messageID=" + t.getMessageID() +
                               ",status=" + t.getStatus() );
          i++;
        }
    }

}
