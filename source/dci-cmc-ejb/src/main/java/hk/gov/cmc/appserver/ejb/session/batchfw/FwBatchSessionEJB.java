package hk.gov.cmc.appserver.ejb.session.batchfw;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Properties;

import hk.gov.cmc.config.CmcEnvProperties;
import hk.gov.gcis.rm.common.javaee.ejb.EJBBase;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionManagement;
import jakarta.ejb.TransactionManagementType;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class FwBatchSessionEJB extends EJBBase implements IFwBatchSessionBM, IFwBatchSessionBMLocal {

    private final static CmcEnvProperties cmcEnvProperties = new CmcEnvProperties();

    public void callBatchUsingReflection(String clsImpl, String methodName, Map<String, Object> params)
            throws Exception {

        Properties properties = cmcEnvProperties.getProperties();

        // for (Map.Entry<Object, Object> entry : properties.entrySet()) {
        //     logInfo(entry.getKey() + " = " + entry.getValue());
        // }

        String fwBatchImplClassEnabled = properties.getProperty("FW_BATCH_IMPL_CLASS_" + clsImpl + "_ENABLED");

        if ("Y".equals(fwBatchImplClassEnabled)) {
            Object clsObj = Class.forName(clsImpl).newInstance();
            Method method = clsObj.getClass().getMethod(methodName, new Class[] { Map.class });
            method.invoke(clsObj, new Object[] { params });
        } else {
            logError("The impl class [" + clsImpl + "] is not permitted.");
            throw new Exception("The impl class [" + clsImpl + "] is not permitted.");
        }
    }
}
