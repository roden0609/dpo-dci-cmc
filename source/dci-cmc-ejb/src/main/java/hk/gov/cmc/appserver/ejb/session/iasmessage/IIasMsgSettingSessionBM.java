package hk.gov.cmc.appserver.ejb.session.iasmessage;

import jakarta.ejb.EJBException;

public interface IIasMsgSettingSessionBM {
    public void processMessageByBatchPull(String getAsynAppIdStr, String singlePullCallLimitStr) throws EJBException;
}
