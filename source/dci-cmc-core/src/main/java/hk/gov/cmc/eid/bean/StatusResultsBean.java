


package hk.gov.cmc.eid.bean;


import java.io.Serializable;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class StatusResultsBean implements Serializable
{

    
    private static final long serialVersionUID = 1L;

    private ArrayList<StatusResultItem> statusResults;

    public StatusResultsBean() {}

    public StatusResultsBean(String content) {
        ArrayList<StatusResultItem> statusArray = new ArrayList<StatusResultItem>();
        JsonObject j = new JsonParser().parse(content).getAsJsonObject();
        JsonArray jArray = j.getAsJsonArray("statusResults");

        for (int i=0; i < jArray.size(); i++) {
            JsonObject x = jArray.get(i).getAsJsonObject();
            String status = getJsonString(x, "status");
            String messageID = getJsonString(x, "messageID");
            String notificationID = getJsonString(x, "notificationID");
            StatusResultItem n = new StatusResultItem(notificationID, messageID, status );
            statusArray.add(n);
        }
        this.statusResults = statusArray;
    }

    public ArrayList<StatusResultItem> getStatusResults() {
        return this.statusResults;
    }

    public void setStatusResults(ArrayList<StatusResultItem> n) {
        this.statusResults = n;
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
        for ( StatusResultItem t : this.statusResults) {
            System.out.println("statusResult Item#" + i + ":" +
                               "notificationID=" + t.getNotificationID() +
                               ",messageID=" + t.getMessageID() +
                               ",status=" + t.getStatus() );
          i++;
        }
    }

}
