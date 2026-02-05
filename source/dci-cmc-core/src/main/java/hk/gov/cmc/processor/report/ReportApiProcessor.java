package hk.gov.cmc.processor.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import hk.gov.cmc.common.ReportConstants;
import hk.gov.cmc.dao.report.ReportApiDAO;
import hk.gov.cmc.persistence.connection.hpfw.HPFW_Connection;
import hk.gov.cmc.persistence.report.AdmRptAdhocReq_;
import hk.gov.cmc.persistence.report.AdmRptInfo_;

public class ReportApiProcessor {

    private static final Log logger = LogFactory.getLog(ReportApiProcessor.class);

    public static final String ADHOC_RESULT_SUCCESS = "S";
    public static final String ADHOC_RESULT_PARTIAL_SUCCESS = "P";
    public static final String ADHOC_RESULT_ALL_FAIL = "F";

    public static String executeBatchRegen(String projectId) throws Exception {
        if (logger.isDebugEnabled())
            logger.debug("executeBatchRegen() begin");

        String resultCode = ADHOC_RESULT_SUCCESS;
        int totalCount = 0;
        int successCount = 0;
        HPFW_Connection hpfwConn = null;
        ReportApiDAO dao = null;
        try {
            hpfwConn = HPFW_Connection.getHPFW_Connection();
            dao = new ReportApiDAO();

            List<AdmRptAdhocReq_> admRptAdhocReqList = dao.retrieveAdmRptAdhocReqByReqTypeProjectId(hpfwConn,
                    ReportConstants.RPT_ADHOC_REQ_REQTYPE_BATCH, projectId);
            if (admRptAdhocReqList == null || admRptAdhocReqList.isEmpty()) {
                logger.info("No outstanding adhoc report found.");
                return resultCode;
            }
            totalCount = admRptAdhocReqList.size();

            for (AdmRptAdhocReq_ admRptAdhocReq : admRptAdhocReqList) {
                try {
                    hpfwConn.setAutoCommit(false);
                    hpfwConn.begin(admRptAdhocReq.getUserId(), null, HPFW_Connection.DIRECT);

                    if (logger.isDebugEnabled()) {
                        logger.debug("AdmRptAdhocReq_ SeqId[" + admRptAdhocReq.getSeqId() + "],RptId["
                                + admRptAdhocReq.getRptId() + "],Param[" + admRptAdhocReq.getParam()
                                + "],OutputFileType[" + admRptAdhocReq.getOutputFileType() + "],UserId["
                                + admRptAdhocReq.getUserId() + "]");
                    }
                    Map<String, String> paramMap = convertParamString2Map(admRptAdhocReq.getParam());
                    paramMap.put(IReportImplement.MAP_PARAM_KEY_IS_ADHOC, IReportImplement.MAP_PARAM_VALUE_NO);
                    paramMap.put(IReportImplement.MAP_PARAM_KEY_IS_BATCH_REGEN, IReportImplement.MAP_PARAM_VALUE_YES);
                    paramMap.put(IReportImplement.MAP_PARAM_KEY_ADHOC_SEQ_ID,
                            String.valueOf(admRptAdhocReq.getSeqId()));
                    boolean batchResult = generateBatchReport(hpfwConn, admRptAdhocReq.getRptId(), paramMap);
                    if (batchResult) {
                        successCount = successCount + 1;
                        admRptAdhocReq.delete(hpfwConn);
                        hpfwConn.commit();
                    } else {
                        dao.updateAdmRptAdhocReqToError(hpfwConn, admRptAdhocReq);
                        hpfwConn.commit();
                    }
                } catch (Exception ex) {
                    logger.error("General exception raised in run AdmRptAdhocReq SeqId[" + admRptAdhocReq.getSeqId()
                            + "],RptId[" + admRptAdhocReq.getRptId() + "]");
                    dao.updateAdmRptAdhocReqToError(hpfwConn, admRptAdhocReq);
                    hpfwConn.commit();
                }
            }

            HPFW_Connection.close(hpfwConn);
            dao = null;

            if (successCount == 0)
                resultCode = ADHOC_RESULT_ALL_FAIL;
            else if (totalCount > successCount)
                resultCode = ADHOC_RESULT_PARTIAL_SUCCESS;
            else
                resultCode = ADHOC_RESULT_SUCCESS;

            return resultCode;
        } catch (Exception ex) {
            logger.error("General exception raised in executeAdhocReport", ex);
            throw ex;
        } finally {
            if (hpfwConn != null)
                HPFW_Connection.close(hpfwConn);
            if (dao != null)
                dao = null;
            if (logger.isDebugEnabled())
                logger.debug("executeAdhocReport() end");
        }
    }

    public static boolean generateBatchReport(String reportId, Map<String, String> paramMap) throws Exception {
        return generateBatchReport(null, reportId, paramMap);
    }

    private static boolean generateBatchReport(HPFW_Connection hpfwConn, String reportId, Map<String, String> paramMap)
            throws Exception {
        if (logger.isDebugEnabled())
            logger.debug("generateBatchReport() begin");

        if (reportId == null || reportId.trim().equals("")) {
            logger.info("reportId is null or empty");
            return false;
        }

        boolean result = true;
        ReportApiDAO dao = null;
        boolean isCMT = true;
        try {
            if (hpfwConn == null) {
                isCMT = false;
                hpfwConn = HPFW_Connection.getHPFW_Connection();
            }
            dao = new ReportApiDAO();

            AdmRptInfo_ admRptInfo = dao.retrieveAdmRptInfo(hpfwConn, reportId);
            if (admRptInfo == null) {
                logger.info("Connot retrieve AdmRptInfo by reportId[" + reportId + "]");
                return false;
            }
            String reportImplementClass = admRptInfo.getImplClass();
            if (reportImplementClass == null || reportImplementClass.trim().equals("")) {
                logger.info("Cannot retrieve reportImplementClass by reportId[" + reportId + "]");
                return false;
            }
            if (admRptInfo.getBatchRptFuncId() == null || admRptInfo.getBatchRptFuncId().trim().equals("")) {
                logger.info("Cannot retrieve batchRptFuncId by reportId[" + reportId + "]");
                return false;
            }
            boolean isTeamBatchRpt = ReportApiDAO.ADM_RPT_INFO_CONSTANTS_VALUE_YES.equals(admRptInfo.getTeamBatchRpt());
            boolean isSpIdBatchRpt = ReportApiDAO.ADM_RPT_INFO_CONSTANTS_VALUE_YES.equals(admRptInfo.getSpidBatchRpt());

            List<String> resultList = null;
            if (isTeamBatchRpt) {
                resultList = dao.getTeamCodeListForBatchReport(hpfwConn, admRptInfo.getBatchRptFuncId());
                // For batch regenerate by signal team code.
                if (paramMap.containsKey(IReportImplement.MAP_PARAM_KEY_SINGLE_TEAM_OR_SP)) {
                    if (!resultList.contains(paramMap.get(IReportImplement.MAP_PARAM_KEY_SINGLE_TEAM_OR_SP))) {
                        logger.info("Team code [" + paramMap.get(IReportImplement.MAP_PARAM_KEY_SINGLE_TEAM_OR_SP)
                                + "] have no user assigned to reportId[" + reportId + "]");
                        return false;
                    }
                    resultList = new ArrayList<String>();
                    resultList.add(paramMap.get(IReportImplement.MAP_PARAM_KEY_SINGLE_TEAM_OR_SP));
                } else
                    resultList.add(0, IReportImplement.MAP_PARAM_VALUE_IS_ADMIN);
            } else if (isSpIdBatchRpt) {
                resultList = dao.getSpIdListForBatchReport(hpfwConn, admRptInfo.getBatchRptFuncId());
                // For batch regenerate by signal spId.
                if (paramMap.containsKey(IReportImplement.MAP_PARAM_KEY_SINGLE_TEAM_OR_SP)) {
                    if (!resultList.contains(paramMap.get(IReportImplement.MAP_PARAM_KEY_SINGLE_TEAM_OR_SP))) {
                        logger.info("SpId [" + paramMap.get(IReportImplement.MAP_PARAM_KEY_SINGLE_TEAM_OR_SP)
                                + "] have no user assigned to reportId[" + reportId + "]");
                        return false;
                    }
                    resultList = new ArrayList<String>();
                    resultList.add(paramMap.get(IReportImplement.MAP_PARAM_KEY_SINGLE_TEAM_OR_SP));
                } else
                    resultList.add(0, IReportImplement.MAP_PARAM_VALUE_IS_ADMIN);
            }

            if (!paramMap.containsKey(IReportImplement.MAP_PARAM_KEY_IS_ADHOC))
                paramMap.put(IReportImplement.MAP_PARAM_KEY_IS_ADHOC, IReportImplement.MAP_PARAM_VALUE_NO);
            if (!paramMap.containsKey(IReportImplement.MAP_PARAM_KEY_IS_BATCH_REGEN))
                paramMap.put(IReportImplement.MAP_PARAM_KEY_IS_BATCH_REGEN, IReportImplement.MAP_PARAM_VALUE_NO);
            String adhocSeqId = "";
            if (paramMap.containsKey(IReportImplement.MAP_PARAM_KEY_ADHOC_SEQ_ID)) {
                adhocSeqId = paramMap.get(IReportImplement.MAP_PARAM_KEY_ADHOC_SEQ_ID);
            }
            IReportImplement reportImplement = null;
            try {
                if (logger.isDebugEnabled())
                    logger.debug("Create IReportImplement instance");
                reportImplement = (IReportImplement) Class.forName(reportImplementClass.trim()).newInstance();

                if (!isCMT) {
                    // Open transaction
                    hpfwConn.setAutoCommit(false);
                    hpfwConn.begin(null, null, HPFW_Connection.DIRECT);
                }

                if (isTeamBatchRpt || isSpIdBatchRpt) {
                    paramMap.put(IReportImplement.MAP_PARAM_KEY_TEAM_OR_SP_SIZE, String.valueOf(resultList.size()));
                    for (int i = 0; i < resultList.size(); i++) {
                        paramMap.put(IReportImplement.MAP_PARAM_KEY_CURRENT_INDEX, String.valueOf(i + 1));
                        String teamCodeOrSpId = resultList.get(i);

                        if (isTeamBatchRpt) {
                            logger.info("Run reportImplement.generateReport by reportId[" + reportId + "], teamCode["
                                    + teamCodeOrSpId + "]");
                            paramMap.put(IReportImplement.MAP_PARAM_KEY_TEAM_CODE, teamCodeOrSpId);
                        } else if (isSpIdBatchRpt) {
                            logger.info("Run reportImplement.generateReport by reportId[" + reportId + "], spId["
                                    + teamCodeOrSpId + "]");
                            paramMap.put(IReportImplement.MAP_PARAM_KEY_SP_ID, teamCodeOrSpId);
                        }

                        if (logger.isDebugEnabled()) {
                            logger.debug("paramMap key and value.");
                            for (String key : paramMap.keySet()) {
                                logger.debug(key + "[" + paramMap.get(key) + "]");
                            }
                        }

                        List<ReportResult> rptResultList = reportImplement.generateReport(admRptInfo, paramMap);
                        if (rptResultList == null || rptResultList.isEmpty()) {
                            if (isTeamBatchRpt)
                                logger.error("Report output is empty, reportId[" + reportId + "], teamCode["
                                        + teamCodeOrSpId + "]");
                            else if (isSpIdBatchRpt)
                                logger.error("Report output is empty, reportId[" + reportId + "], spId["
                                        + teamCodeOrSpId + "]");
                            return false;
                        }
                        for (ReportResult rptResult : rptResultList) {
                            if (!isValidReportResult(rptResult)) {
                                if (isTeamBatchRpt)
                                    logger.error("Report output invalid, reportId[" + reportId + "], teamCode["
                                            + teamCodeOrSpId + "]");
                                else if (isSpIdBatchRpt)
                                    logger.error("Report output invalid, reportId[" + reportId + "], spId["
                                            + teamCodeOrSpId + "]");
                                result = false;
                                break;
                            }
                        }

                        for (ReportResult rptResult : rptResultList) {
                            dao.createAdmRptOutput(
                                    hpfwConn,
                                    reportId,
                                    ReportApiDAO.ADM_RPT_OUTPUT_GEN_MODE_BATCH,
                                    rptResult.getOutputFileType(),
                                    rptResult.getOutputFileByteArray(),
                                    convertMap2ParamString(paramMap),
                                    rptResult.getReportingDate(),
                                    rptResult.getDataStartDate(),
                                    isTeamBatchRpt ? teamCodeOrSpId : null,
                                    isSpIdBatchRpt ? teamCodeOrSpId : null,
                                    adhocSeqId,
                                    null,
                                    isValidReportResult(rptResult) ? ReportApiDAO.ADM_RPT_OUTPUT_STATUS_ACTIVE
                                            : ReportApiDAO.ADM_RPT_OUTPUT_STATUS_ERROR);
                        }
                        hpfwConn.commit();
                    }
                } else {
                    logger.info("Run reportImplement.generateReport by reportId[" + reportId + "]");
                    List<ReportResult> rptResultList = reportImplement.generateReport(admRptInfo, paramMap);
                    if (rptResultList == null || rptResultList.isEmpty()) {
                        logger.error("Report output is empty, reportId[" + reportId + "]");
                        return false;
                    }

                    for (ReportResult rptResult : rptResultList) {
                        if (!isValidReportResult(rptResult)) {
                            logger.error("Report output invalid, reportId[" + reportId + "]");
                            result = false;
                            break;
                        }
                    }
                    for (ReportResult rptResult : rptResultList) {
                        dao.createAdmRptOutput(
                                hpfwConn,
                                reportId,
                                ReportApiDAO.ADM_RPT_OUTPUT_GEN_MODE_BATCH,
                                rptResult.getOutputFileType(),
                                rptResult.getOutputFileByteArray(),
                                convertMap2ParamString(paramMap),
                                rptResult.getReportingDate(),
                                rptResult.getDataStartDate(),
                                null,
                                null,
                                adhocSeqId,
                                null,
                                isValidReportResult(rptResult) ? ReportApiDAO.ADM_RPT_OUTPUT_STATUS_ACTIVE
                                        : ReportApiDAO.ADM_RPT_OUTPUT_STATUS_ERROR);
                    }
                    hpfwConn.commit();
                }
            } catch (ClassNotFoundException ex) {
                logger.error("Cannot create new IReportImplement instance, reportId[" + reportId
                        + "], reportImplementClass[" + reportImplementClass + "]", ex);
                result = false;
            }

            if (!isCMT)
                HPFW_Connection.close(hpfwConn);
            dao = null;

            return result;
        } catch (Exception ex) {
            logger.error("General exception raised in generateBatchReport", ex);
            throw ex;
        } finally {
            if (hpfwConn != null && !isCMT)
                HPFW_Connection.close(hpfwConn);
            if (dao != null)
                dao = null;
            if (logger.isDebugEnabled())
                logger.debug("generateBatchReport() end");
        }
    }

    private static boolean isValidReportResult(ReportResult reportResult) {
        if (reportResult == null) {
            logger.info("reportResult is empty.");
            return false;
        }
        if (!reportResult.isSuccess()) {
            return false;
        }
        if (reportResult.getOutputFileType() == null || reportResult.getOutputFileType().trim().length() == 0) {
            logger.info("outputFileType is null or empty.");
            return false;
        }
        if (reportResult.getOutputFileByteArray() == null || reportResult.getOutputFileByteArray().length == 0) {
            logger.info("outputFile is empty.");
            return false;
        }
        return true;
    }

    private static Map<String, String> convertParamString2Map(String paramString) throws Exception {
        if (logger.isDebugEnabled())
            logger.debug("convertParamString2Map() begin");

        Map<String, String> paramMap = new HashMap<String, String>();

        if (paramString == null || paramString.trim().length() == 0)
            return paramMap;

        try {
            String[] params = paramString.split(ReportConstants.MAP_PARAMETER_AND);
            if (params == null || params.length <= 0)
                return paramMap;
            for (String param : params) {
                String[] para = param.split(ReportConstants.MAP_PARAMETER_EQUALS);
                if (para == null || para.length != 2) {
                    logger.info("invalid parameter key and value [" + param + "], skip this.");
                    continue;
                }
                paramMap.put(para[0], para[1]);
            }
            return paramMap;
        } catch (Exception ex) {
            logger.error("General exception raised in convertParamString2Map", ex);
            throw ex;
        } finally {
            if (logger.isDebugEnabled())
                logger.debug("convertParamString2Map() end");
        }
    }

    private static String convertMap2ParamString(Map<String, String> paraMap) throws Exception {
        if (logger.isDebugEnabled())
            logger.debug("convertMap2ParamString() begin");

        if (paraMap == null || paraMap.isEmpty())
            return null;

        StringBuilder sb = new StringBuilder();
        try {
            for (String key : paraMap.keySet()) {
                if (sb.length() > 0)
                    sb.append(ReportConstants.MAP_PARAMETER_AND);
                sb.append(key).append(ReportConstants.MAP_PARAMETER_EQUALS).append(paraMap.get(key));
            }
            return sb.toString();
        } catch (Exception ex) {
            logger.error("General exception raised in convertMap2ParamString", ex);
            throw ex;
        } finally {
            if (logger.isDebugEnabled())
                logger.debug("convertMap2ParamString() end");
        }
    }

}
