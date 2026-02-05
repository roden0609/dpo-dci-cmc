package hk.gov.cmc.processor.report;

import java.util.List;
import java.util.Map;

import hk.gov.cmc.persistence.report.AdmRptInfo_;

public interface IReportImplement {

    public static final String CONSTANTS_BATCH_REPORT_REQUEST_USER = "OGIO";

    public static final String MAP_PARAM_KEY_TEAM_CODE = "TEAMCODE";
    public static final String MAP_PARAM_KEY_SP_ID = "SPID";
    public static final String MAP_PARAM_KEY_USER_ID = "USERID";
    public static final String MAP_PARAM_KEY_IS_ADHOC = "ISADHOC";
    public static final String MAP_PARAM_KEY_IS_BATCH_REGEN = "ISBATCHREGEN";
    public static final String MAP_PARAM_KEY_SINGLE_TEAM_OR_SP = "SINGLETEAMSP";
    public static final String MAP_PARAM_KEY_ADHOC_SEQ_ID = "ADHOCSEQID";
    public static final String MAP_PARAM_KEY_TEAM_OR_SP_SIZE = "TEAMORSPSIZE";
    public static final String MAP_PARAM_KEY_CURRENT_INDEX = "CURRENTINDEX";

    public static final String MAP_PARAM_VALUE_IS_ADMIN = "ALL";
    public static final String MAP_PARAM_VALUE_YES = "Y";
    public static final String MAP_PARAM_VALUE_NO = "N";

    public static final String REPORT_DISPLAY_PRINT_BY = "Printed By: ";
    public static final String REPORT_DISPLAY_PRINT_DATE = "Print Date: ";
    public static final String REPORT_DISPLAY_AS_AT = "As at: ";
    public static final String REPORT_DISPLAY_DASHLINE = "---------------------------------------------------------------------------------------------------------------------------------------------------------------";
    public static final String REPORT_DISPLAY_END_OF_REPORT = "- End of Report -";

    /**
     * Generate report implementation
     * 
     * @param admRptInfo - AdmRptInfo_ data-bean
     * @param paramMap   - Generate report extra information
     * @return
     * @throws Exception
     */
    public List<ReportResult> generateReport(final AdmRptInfo_ admRptInfo, final Map<String, String> paramMap)
            throws Exception;

}
