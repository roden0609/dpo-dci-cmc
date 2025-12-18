
package hk.gov.cmc.eid.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class GetNotificationOpenIdRequestBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private ArrayList<EServiceOpenIdsBean> eServiceOpenIdsBeanList;

    public GetNotificationOpenIdRequestBean() {
    }

    public void setEServiceOpenIDs(ArrayList<EServiceOpenIdsBean> eServiceOpenIds) {
        this.eServiceOpenIdsBeanList = eServiceOpenIds;
    }

    public ArrayList<EServiceOpenIdsBean> getEServiceOpenIDs() {
        return this.eServiceOpenIdsBeanList;
    }

    @Override
    public String toString() {
        return "GetNotificationOpenIdRequestBean [eServiceOpenIdsBeanList=" + eServiceOpenIdsBeanList + "]";
    }

}
