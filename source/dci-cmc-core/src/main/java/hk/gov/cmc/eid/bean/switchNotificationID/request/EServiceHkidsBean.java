package hk.gov.cmc.eid.bean.switchNotificationID.request;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class EServiceHkidsBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private ArrayList<EServiceHkidItem> eServiceHKIDs;

    public EServiceHkidsBean() {
    }

    public EServiceHkidsBean(String content) {
        JsonArray jarray = new JsonParser().parse(content).getAsJsonArray();
        ArrayList<EServiceHkidItem> hkidArray = new ArrayList<EServiceHkidItem>();
        for (int i = 0; i < jarray.size(); i++) {
            JsonObject x = jarray.get(i).getAsJsonObject();
            String clientID = getJsonString(x, "clientID");
            String hkid = getJsonString(x, "HKID");
            EServiceHkidItem eServiceHkidItem = new EServiceHkidItem(clientID, hkid);
            hkidArray.add(eServiceHkidItem);
        }
        this.eServiceHKIDs = hkidArray;
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

    public void setEServiceHKIDs(ArrayList<EServiceHkidItem> eServiceHKIDs) {
        this.eServiceHKIDs = eServiceHKIDs;
    }

    public ArrayList<EServiceHkidItem> getEServiceHKIDs() {
        return this.eServiceHKIDs;
    }

    @Override
    public String toString() {
        return "EServiceHkidsBean [eServiceHKIDs=" + eServiceHKIDs + "]";
    }

}