package hk.gov.cmc.dao.report;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import hk.gov.cmc.persistence.connection.hpfw.HPFW_Connection;
import hk.gov.cmc.persistence.connection.hpfw.MysqlCommonDBUtils;
import hk.gov.cmc.persistence.connection.hpfw.Parameter;
import hk.gov.cmc.persistence.report.AdmRptAdhocReq_;
import hk.gov.cmc.persistence.report.AdmRptInfo_;

public class ReportApiDAO {

    private static Log logger = LogFactory.getLog(ReportApiDAO.class);
    /**
     * AdmRptInfo table constants value for yes.
     */
    public final static String ADM_RPT_INFO_CONSTANTS_VALUE_YES = "Y";
    /**
     * AdmRptInfo table constants value for no.
     */
    public final static String ADM_RPT_INFO_CONSTANTS_VALUE_NO = "N";
    /**
     * All table status constants value for active.
     */
    public final static String CONSTANTS_STATUS_VALUE_ACTIVE = "A";
    /**
     * AdmRptInfo table value for status - Active.
     */
    public final static String ADM_RPT_OUTPUT_STATUS_ACTIVE = "A";
    /**
     * AdmRptInfo table value for status - Error.
     */
    public final static String ADM_RPT_OUTPUT_STATUS_ERROR = "E";
    /**
     * AdmRptInfo table value for generate mode - Batch.
     */
    public final static String ADM_RPT_OUTPUT_GEN_MODE_BATCH = "B";
    /**
     * AdmRptInfo table value for generate mode - AdHoc.
     */
    public final static String ADM_RPT_OUTPUT_GEN_MODE_ADHOC = "S";
    /**
     * AdmRptAdhocReq table value for status - New.
     */
    public final static String ADM_RPT_ADHOC_REQ_STATUS_NEW = "N";
    /**
     * AdmRptAdhocReq table value for status - Error.
     */
    public final static String ADM_RPT_ADHOC_REQ_STATUS_ERROR = "E";
    /**
     * Get sp id by function Id for batch report
     */
    private final static String SQL_GET_SPID_BY_FUNCID_FOR_BATCH_REPORT = "SELECT DISTINCT " +
            "	usa.sp_id " +
            "FROM adm_user usr " +
            "INNER JOIN adm_user_sp_assignment usa ON (usa.user_id = usr.user_id) " +
            "INNER JOIN adm_user_role_assignment ura ON (ura.user_id = usr.user_id) " +
            "INNER JOIN adm_role_function_assignment rfa ON (rfa.role_id = ura.role_id) " +
            "WHERE " +
            "	usr.acc_status = '" + CONSTANTS_STATUS_VALUE_ACTIVE + "' " +
            "	AND rfa.func_id = ? ";

    /**
     * Get team code by function Id for batch report
     */
    private final static String SQL_GET_TEAMCODE_BY_FUNCID_FOR_BATCH_REPORT = "SELECT DISTINCT " +
            "	tem.team_code " +
            "FROM adm_user usr " +
            "INNER JOIN adm_team tem ON (tem.team_code = usr.team_code) " +
            "INNER JOIN adm_user_role_assignment ura ON (ura.user_id = usr.user_id) " +
            "INNER JOIN adm_role_function_assignment rfa ON (rfa.role_id = ura.role_id) " +
            "WHERE " +
            "	usr.acc_status = '" + CONSTANTS_STATUS_VALUE_ACTIVE + "' " +
            "	AND tem.status = '" + CONSTANTS_STATUS_VALUE_ACTIVE + "' " +
            "	AND rfa.func_id = ? ";

    /**
     * Insert AdmRptOutput record.
     */
    private final static String INSERT_ADM_RPT_OUTPUT = "INSERT INTO adm_rpt_output " +
            "(seq_id, reporting_dt, gen_mode, rpt_id, output_file_type, user_id, param, req_seq_id, status, " +
            "report_file, team_resolution, spid_resolution, data_start_dt, create_by, last_modify_by, create_dt, last_modify_dt) "
            +
            "VALUES " +
            "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

    /**
     * Retrieve AdmUser email by userId
     */
    private final static String RETRIEVE_ADM_USER_EMAILBY_USER_ID = "SELECT email FROM adm_user WHERE user_id = ?";

    /**
     * Retrieve AdmRptInfo by reportId
     * 
     * @param reportId
     * @return
     * @throws Exception
     */
    public AdmRptInfo_ retrieveAdmRptInfo(HPFW_Connection hpfwConn, String reportId) throws Exception {
        if (logger.isDebugEnabled())
            logger.debug("retrieveAdmRptInfo() begin");

        if (reportId == null || reportId.trim().equals("")) {
            logger.info("reportId is null or empty");
            return null;
        }
        if (logger.isDebugEnabled())
            logger.debug("reportId[" + reportId + "]");

        try {
            return new AdmRptInfo_(hpfwConn, reportId);
        } catch (NullPointerException npx) {
            logger.error("Connot retrieve AdmRptInfo_ by reportId[" + reportId + "]");
            return null;
        } catch (Exception ex) {
            logger.error("General exception raised in retrieveAdmRptInfo", ex);
            throw ex;
        } finally {
            if (logger.isDebugEnabled())
                logger.debug("retrieveAdmRptInfo() end");
        }
    }

    /**
     * Get SpId List for batch report
     * 
     * @param hpfwConn
     * @param funcId
     * @return
     * @throws Exception
     */
    public List<String> getSpIdListForBatchReport(HPFW_Connection hpfwConn, String funcId) throws Exception {
        if (logger.isDebugEnabled())
            logger.debug("getSpIdListForBatchReport() begin");

        if (funcId == null || funcId.trim().equals("")) {
            logger.info("funcId is null or empty");
            return null;
        }
        if (logger.isDebugEnabled())
            logger.debug("funcId[" + funcId + "]");

        List<String> resultList = new ArrayList<String>();
        ArrayList<Parameter> paraList = new ArrayList<Parameter>();
        ResultSet rs = null;
        try {
            paraList.add(new Parameter(Parameter.String, funcId));
            rs = hpfwConn.getResultSet(SQL_GET_SPID_BY_FUNCID_FOR_BATCH_REPORT, paraList);
            paraList.clear();
            paraList = null;

            while (rs.next()) {
                String spId = rs.getString("sp_id");
                if (logger.isDebugEnabled())
                    logger.debug("spId[" + spId + "]");
                if (spId != null && spId.trim().length() > 0) {
                    resultList.add(spId);
                }
            }
            HPFW_Connection.close(rs);

            return resultList;
        } catch (Exception ex) {
            logger.error("General exception raised in getSpIdListForBatchReport", ex);
            throw ex;
        } finally {
            if (rs != null)
                HPFW_Connection.close(rs);
            if (paraList != null) {
                paraList.clear();
                paraList = null;
            }
            if (logger.isDebugEnabled())
                logger.debug("getSpIdListForBatchReport() end");
        }
    }

    /**
     * Get teamCode List for batch report
     * 
     * @param hpfwConn
     * @param funcId
     * @return
     * @throws Exception
     */
    public List<String> getTeamCodeListForBatchReport(HPFW_Connection hpfwConn, String funcId) throws Exception {
        if (logger.isDebugEnabled())
            logger.debug("getTeamCodeListForBatchReport() begin");

        if (funcId == null || funcId.trim().equals("")) {
            logger.info("funcId is null or empty");
            return null;
        }
        if (logger.isDebugEnabled())
            logger.debug("funcId[" + funcId + "]");

        List<String> resultList = new ArrayList<String>();
        ArrayList<Parameter> paraList = new ArrayList<Parameter>();
        ResultSet rs = null;
        try {
            paraList.add(new Parameter(Parameter.String, funcId));
            rs = hpfwConn.getResultSet(SQL_GET_TEAMCODE_BY_FUNCID_FOR_BATCH_REPORT, paraList);
            paraList.clear();
            paraList = null;

            while (rs.next()) {
                String teamCode = rs.getString("team_code");
                if (logger.isDebugEnabled())
                    logger.debug("teamCode[" + teamCode + "]");
                if (teamCode != null && teamCode.trim().length() > 0) {
                    resultList.add(teamCode);
                }
            }
            HPFW_Connection.close(rs);

            return resultList;
        } catch (Exception ex) {
            logger.error("General exception raised in getTeamCodeListForBatchReport", ex);
            throw ex;
        } finally {
            if (rs != null)
                HPFW_Connection.close(rs);
            if (paraList != null) {
                paraList.clear();
                paraList = null;
            }
            if (logger.isDebugEnabled())
                logger.debug("getTeamCodeListForBatchReport() end");
        }
    }

    /**
     * Create AdmRptOutput record
     * 
     * @param hpfwConn
     * @param rptId
     * @param genMode
     * @param outputFileType
     * @param reportFile
     * @param param
     * @param reportingDt
     * @param dataStartDt
     * @param teamResolution
     * @param spidResolution
     * @param reqSeqId
     * @param userId
     * @return
     * @throws Exception
     */
    public void createAdmRptOutput(HPFW_Connection hpfwConn, String rptId, String genMode,
            String outputFileType, byte[] reportFileByteArray, String param, Date reportingDt, Date dataStartDt,
            String teamResolution, String spidResolution, String reqSeqId, String userId, String status)
            throws Exception {
        if (logger.isDebugEnabled())
            logger.debug("createAdmRptOutput() begin");

        PreparedStatement pstmt = null;
        try {
            Connection con = hpfwConn.getConnectionPtr();
            pstmt = con.prepareStatement(INSERT_ADM_RPT_OUTPUT);

            pstmt.setString(1, MysqlCommonDBUtils.getNextMachineBaseSequence());

            if (reportingDt != null)
                pstmt.setTimestamp(2, new java.sql.Timestamp(reportingDt.getTime()));
            else
                pstmt.setNull(2, Types.TIMESTAMP);
            pstmt.setString(3, genMode);
            pstmt.setString(4, rptId);
            pstmt.setString(5, outputFileType);
            pstmt.setString(6, userId);
            pstmt.setString(7, param);
            if (reqSeqId != null && reqSeqId.length() > 0)
                pstmt.setString(8, reqSeqId);
            else
                pstmt.setNull(8, Types.NVARCHAR);
            pstmt.setString(9, status);
            if (reportFileByteArray != null)
                pstmt.setBinaryStream(10, new BufferedInputStream(new ByteArrayInputStream(reportFileByteArray)),
                        reportFileByteArray.length);
            else
                pstmt.setNull(10, Types.BLOB);

            pstmt.setString(11, teamResolution);
            pstmt.setString(12, spidResolution);
            if (reportingDt != null)
                pstmt.setDate(13, new java.sql.Date(dataStartDt.getTime()));
            else
                pstmt.setNull(13, Types.DATE);
            pstmt.setString(14, hpfwConn.getLastUpdBy());
            pstmt.setString(15, hpfwConn.getLastUpdBy());
            pstmt.setTimestamp(16, hpfwConn.getLastUpTime());
            pstmt.setTimestamp(17, hpfwConn.getLastUpTime());
            pstmt.executeUpdate();
        } catch (Exception ex) {
            logger.error("General exception raised in createAdmRptOutput", ex);
            throw ex;
        } finally {
            if (logger.isDebugEnabled())
                logger.debug("createAdmRptOutput() end");
        }
    }

    /**
     * Retrieve AdmRptAdhocReq_ by project Id
     * 
     * @param hpfwConn
     * @param reqType
     * @param projectId
     * @return
     * @throws Exception
     */
    public List<AdmRptAdhocReq_> retrieveAdmRptAdhocReqByReqTypeProjectId(HPFW_Connection hpfwConn, String reqType,
            String projectId) throws Exception {
        if (logger.isDebugEnabled())
            logger.debug("retrieveAdmRptAdhocReqByReqTypeProjectId() begin");

        if (reqType == null || reqType.trim().equals("")) {
            logger.info("reqType is null or empty");
            return null;
        }
        if (projectId == null || projectId.trim().equals("")) {
            logger.info("projectId is null or empty");
            return null;
        }
        if (logger.isDebugEnabled())
            logger.debug("projectId[" + projectId + "]");

        List<AdmRptAdhocReq_> admRptAdhocReqList = null;
        ArrayList<Parameter> paramList = new ArrayList<Parameter>();
        try {
            paramList.add(new Parameter(Parameter.String, reqType));
            paramList.add(new Parameter(Parameter.String, projectId));
            String whereCluase = "WHERE req_type = ? AND project_id = ? AND status = '" + ADM_RPT_ADHOC_REQ_STATUS_NEW
                    + "' ORDER BY seq_id";
            admRptAdhocReqList = AdmRptAdhocReq_.getResultList(hpfwConn, whereCluase, paramList);

            paramList.clear();
            paramList = null;

            return admRptAdhocReqList;
        } catch (Exception ex) {
            logger.error("General exception raised in retrieveAdmRptAdhocReqByReqTypeProjectId", ex);
            throw ex;
        } finally {
            if (paramList != null) {
                paramList.clear();
                paramList = null;
            }
            if (logger.isDebugEnabled())
                logger.debug("retrieveAdmRptAdhocReqByReqTypeProjectId() end");
        }
    }

    /**
     * Update AdmRptAdhocReq_ status to error
     * 
     * @param hpfwConn
     * @param admRptAdhocReq
     * @throws Exception
     */
    public void updateAdmRptAdhocReqToError(HPFW_Connection hpfwConn, AdmRptAdhocReq_ admRptAdhocReq) throws Exception {
        if (logger.isDebugEnabled())
            logger.debug("updateAdmRptAdhocReqToError() begin");

        try {
            admRptAdhocReq.setStatus(ADM_RPT_ADHOC_REQ_STATUS_ERROR);
            admRptAdhocReq.update(hpfwConn);
        } catch (Exception ex) {
            logger.error("General exception raised in updateAdmRptAdhocReqToError", ex);
            throw ex;
        } finally {
            if (logger.isDebugEnabled())
                logger.debug("updateAdmRptAdhocReqToError() end");
        }
    }

    /**
     * Retrieve AdmUser email by userId
     * 
     * @param hpfwConn
     * @param userId
     * @return
     * @throws Exception
     */
    public String retrieveAdmUserEmail(HPFW_Connection hpfwConn, String userId) throws Exception {
        if (logger.isDebugEnabled())
            logger.debug("retrieveAdmUserEmail() begin");

        if (userId == null || userId.trim().equals("")) {
            logger.info("userId is null or empty");
            return null;
        }
        if (logger.isDebugEnabled())
            logger.debug("userId[" + userId + "]");

        String email = null;
        ResultSet rs = null;
        ArrayList<Parameter> paramList = new ArrayList<Parameter>();
        try {
            paramList.add(new Parameter(Parameter.String, userId));
            rs = hpfwConn.getResultSet(RETRIEVE_ADM_USER_EMAILBY_USER_ID, paramList);

            if (rs.next()) {
                email = rs.getString("email");
            }

            paramList.clear();
            paramList = null;
            HPFW_Connection.close(rs);

            return email;
        } catch (Exception ex) {
            logger.error("General exception raised in retrieveAdmUserEmail", ex);
            throw ex;
        } finally {
            if (rs != null)
                HPFW_Connection.close(rs);
            if (paramList != null) {
                paramList.clear();
                paramList = null;
            }
            if (logger.isDebugEnabled())
                logger.debug("retrieveAdmUserEmail() end");
        }
    }
}
