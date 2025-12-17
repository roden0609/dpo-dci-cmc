
package hk.gov.cmc.eid.bean;



import java.io.Serializable;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class NotificationBean implements Serializable
{

    
    private static final long serialVersionUID = 1L;

    private ArrayList<NotificationItem> notifications;

    public NotificationBean() {}

    public NotificationBean(String content) {
       JsonArray jarray = new JsonParser().parse(content).getAsJsonArray();
       ArrayList<NotificationItem> notiArray = new ArrayList<NotificationItem>();
       for (int i=0; i < jarray.size(); i++) {
           JsonObject x = jarray.get(i).getAsJsonObject();
           JsonArray notiIds = x.get("notificationIDs").getAsJsonArray();
           String messageID = x.get("messageID").getAsString();
           String spID = x.get("spID").getAsString();
           String enMessage = x.get("enMessage").getAsString();
           String tcMessage = x.get("tcMessage").getAsString();
           String scMessage = x.get("scMessage").getAsString();

           ArrayList<String> notiIdsList = new ArrayList<String>();

           if (notiIds != null) {
              for (int z=0; z<notiIds.size();z++){
                notiIdsList.add(notiIds.get(z).getAsString());
              }
           }

           NotificationItem n = new NotificationItem(notiIdsList, messageID, spID, enMessage, tcMessage,scMessage );
           notiArray.add(n);
       }
       this.notifications = notiArray;
    }

    public ArrayList<NotificationItem> getNotifications() {
        return this.notifications;
    }

    public void setNotifications(ArrayList<NotificationItem> n) {
        this.notifications = n;
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
        for ( NotificationItem t : this.notifications) {
            System.out.println("notifications Item#" + i + ":" +
                               "notificationIDs=" + t.getNotificationIDs().toString() + "," +
                               "messageID=" + t.getMessageID() + "," +
                               "spID=" + t.getSpID() + "," +
                               "enMessage=" + t.getEnMessage() + "," +
                               "tcMessage=" + t.getTcMessage() + "," +
                               "scMessage=" + t.getScMessage() );
          i++;
        }
    }

}
