package hk.gov.cmc.appserver.ejb.session.retrievecmctemplate;

import hk.gov.cmc.jaxb.retrievecmctemplate.RetrieveCmcTemplateRequest;
import hk.gov.cmc.jaxb.retrievecmctemplate.RetrieveCmcTemplateResponse;
import jakarta.ejb.EJBException;

public interface IRetrieveCmcTemplateSessionBM {

    public RetrieveCmcTemplateResponse processMessage(String appId,
            RetrieveCmcTemplateRequest retrieveCmcTemplateRequest) throws EJBException;

}
