package hk.gov.cmc.appserver.ejb.session.batchfw;

import java.util.Map;

public interface IFwBatch {
    public void callBatchUsingReflection(String clsImpl, String methodName, Map<String, Object> params)
            throws Exception;
}
