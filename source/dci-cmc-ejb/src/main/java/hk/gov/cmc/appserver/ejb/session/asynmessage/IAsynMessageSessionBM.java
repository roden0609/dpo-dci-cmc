package hk.gov.cmc.appserver.ejb.session.asynmessage;

import jakarta.ejb.EJBException;

public interface IAsynMessageSessionBM {

    public void sendMsgResponseByAsyn() throws EJBException;

}
