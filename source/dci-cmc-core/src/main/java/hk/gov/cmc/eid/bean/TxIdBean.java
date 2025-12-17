

package hk.gov.cmc.eid.bean;


import java.io.Serializable;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class TxIdBean implements Serializable
{

    
    private static final long serialVersionUID = 1L;

    private String txID;

    public TxIdBean() {}

    public TxIdBean(String content) {

       this.txID = content;
    }

    public String getTxID() {
        return this.txID;
    }

    public void setTxID(String txid) {
        this.txID =txid;
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
        System.out.println("txID=" + this.txID);
    }

}
