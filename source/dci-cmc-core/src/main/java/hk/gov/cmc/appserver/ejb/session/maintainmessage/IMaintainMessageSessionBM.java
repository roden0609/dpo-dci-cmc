package hk.gov.cmc.appserver.ejb.session.maintainmessage;

import hk.gov.cmc.jaxb.maintainmessage.MaintainMessageRequest;
import hk.gov.cmc.jaxb.maintainmessage.MaintainMessageResponse;
import jakarta.ejb.EJBException;

public interface IMaintainMessageSessionBM {
    public MaintainMessageResponse processMessage(String appId, MaintainMessageRequest maintMsgReq) throws EJBException;

    public void processMessageByBatchPull(String getAsynAppIdStr, String singlePullCallLimitStr) throws EJBException;
}
