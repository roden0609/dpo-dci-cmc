package hk.gov.cmc.appserver.ejb.session.retrievecmctemplate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import hk.gov.cmc.jaxb.retrievecmctemplate.RetrieveCmcTemplateRequest;
import hk.gov.cmc.jaxb.retrievecmctemplate.RetrieveCmcTemplateResponse;
import hk.gov.cmc.mapper.retrievecmctemplate.RetrieveCmcTemplateRequestMapper;
import hk.gov.cmc.mapper.retrievecmctemplate.RetrieveCmcTemplateResponseMapper;
import hk.gov.cmc.persistence.connection.hpfw.HPFW_Connection;
import hk.gov.cmc.processor.retrievecmctemplate.RetrieveCmcTemplateProcessor;
import hk.gov.gcis.rm.common.javaee.ejb.EJBBase;
import jakarta.ejb.EJBException;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionManagement;
import jakarta.ejb.TransactionManagementType;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class RetrieveCmcTemplateSessionEJB extends EJBBase
        implements IRetrieveCmcTemplateSessionBMRemote, IRetrieveCmcTemplateSessionBMLocal {

    private static Log logger = LogFactory.getLog(RetrieveCmcTemplateSessionEJB.class);

    public RetrieveCmcTemplateResponse processMessage(String sendAppId,
            RetrieveCmcTemplateRequest retrieveCmcTemplateRequest) throws EJBException {
        logger.info("[RetrieveCmcTemplateSessionEJB]processMessage - START");

        RetrieveCmcTemplateResponse retrieveCmcTemplateResponse = null;

        HPFW_Connection conn = null;

        try {
            conn = HPFW_Connection.getHPFW_Connection(false);

            conn.begin(sendAppId, null, HPFW_Connection.DIRECT_WITH_HISTORY);

            logger.info("[RetrieveCmcTemplateSessionEJB]processMessage sendAppId: " + sendAppId
                    + ", retrieveCmcTemplateRequest TemplateId: "
                    + (retrieveCmcTemplateRequest != null ? retrieveCmcTemplateRequest.getTemplateId()
                            : "retrieveCmcTemplateRequest == null")
                    + ", retrieveCmcTemplateRequest TemplateVersion: "
                    + (retrieveCmcTemplateRequest != null ? retrieveCmcTemplateRequest.getTemplateVersion()
                            : "retrieveCmcTemplateRequest == null"));

            RetrieveCmcTemplateProcessor retrieveCmcTemplateProcessor = new RetrieveCmcTemplateProcessor();
            hk.gov.cmc.model.retrievecmctemplate.request.RetrieveCmcTemplateRequest retrieveCmcTemplateReqDomain = RetrieveCmcTemplateRequestMapper
                    .fromJaxb(retrieveCmcTemplateRequest);
            hk.gov.cmc.model.retrievecmctemplate.response.RetrieveCmcTemplateResponse retrieveCmcTemplateRespDomain = retrieveCmcTemplateProcessor
                    .processMessage(conn, sendAppId, retrieveCmcTemplateReqDomain);
            retrieveCmcTemplateResponse = RetrieveCmcTemplateResponseMapper.toJaxb(retrieveCmcTemplateRespDomain);

            conn.commit();

        } catch (RuntimeException e) {
            logger.error("Runtime exception raised in processMessage", e);
            throw e;
        } catch (Exception e) {
            logger.error("General exception raised in processMessage", e);
            throw new EJBException(e);
        } finally {
            try {
                HPFW_Connection.close(conn);
            } catch (Exception ex) {
                logger.error("General exception caught in processMessage - conn.close();", ex);
            }
        }

        logger.info("[RetrieveCmcTemplateSessionEJB]processMessage - END");

        return retrieveCmcTemplateResponse;
    }
}
