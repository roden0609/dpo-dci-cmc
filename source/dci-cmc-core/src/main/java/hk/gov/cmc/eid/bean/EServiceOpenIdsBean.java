

package hk.gov.cmc.eid.bean;


import java.io.Serializable;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class EServiceOpenIdsBean implements Serializable
{

    
    private static final long serialVersionUID = 1L;

    private ArrayList<EServiceOpenIdItem> eServiceOpenIDs;

    public EServiceOpenIdsBean() {}

    public EServiceOpenIdsBean(String content) {

       JsonArray jarray = new JsonParser().parse(content).getAsJsonArray();
       ArrayList<EServiceOpenIdItem> openIdArray = new ArrayList<EServiceOpenIdItem>();
       for (int i=0; i < jarray.size(); i++) {
        JsonObject x = jarray.get(i).getAsJsonObject();
        String clientID = getJsonString(x, "clientID");
        String openID = getJsonString(x, "openID");
        EServiceOpenIdItem n = new EServiceOpenIdItem(clientID, openID);
        openIdArray.add(n);
       }
       this.eServiceOpenIDs = openIdArray;
    }

    public ArrayList<EServiceOpenIdItem> getEServiceOpenIDs() {
        return this.eServiceOpenIDs;
    }

    public void setEServiceOpenIDs(ArrayList<EServiceOpenIdItem> n) {
        this.eServiceOpenIDs = n;
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
        for ( EServiceOpenIdItem t : this.eServiceOpenIDs) {
            System.out.println("eServiceOpenIDs Item#" + i + ":" +
                               "ClientId=" + t.getClientID() +
                               ",OpenId=" + t.getOpenID() );
          i++;
        }
    }

}
