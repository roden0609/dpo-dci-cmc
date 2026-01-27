package hk.gov.cmc.persistence.maintainmessage;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.lang.Long;
import java.lang.String;
import java.lang.NullPointerException;
import java.util.ArrayList;

import hk.gov.cmc.persistence.connection.hpfw.CommonDBUtils;
import hk.gov.cmc.persistence.connection.hpfw.HPFW_Connection;
import hk.gov.cmc.persistence.connection.hpfw.Parameter;

public class CmcServiceProvider_ implements Serializable {
    private static final long serialVersionUID = 1L;
    private boolean initialized = false;
    private final static String thisTableName = "CMC_SERVICE_PROVIDER";
    private final static String thisTableHistName = "_H";
    private boolean forUpdate = false;
    private String serviceProviderId = null;
    private String serviceProviderName = null;
    private String description = null;
    private String status = null;
    private String friendlyAlias = null;
    private String appId = null;
    private String reqLinkupInd = null;
    private Timestamp createDt = null;
    private Timestamp lastModifyDt = null;
    private String createBy = null;
    private String lastModifyBy = null;
    private String serviceProviderNameEn = null;
    private String serviceProviderNameTc = null;
    private String serviceProviderNameSc = null;
    private String bcpAppId = null;
    private String bcpAppBranchCode = null;
    private String bcptfInd = null;
    private String bcptfRecipientAppId = null;
    private String bcptfRecipientAppType = null;
    private String bcptfSoapAction = null;
    private String bcptfEndPoint = null;
    private String billAppType = null;
    private String billAppNameEn = null;
    private String billAppNameSc = null;
    private String billAppNameTc = null;
    private String merchantNameEn = null;
    private String merchantNameTc = null;
    private String merchantNameSc = null;
    private String mobileInd = null;
    private String emailInd = null;
    private Integer mobileMsgExpiryTime = null;
    private String billDisplayType = null;
    private String commonBillDescEn = null;
    private String commonBillDescTc = null;
    private String commonBillDescSc = null;
    private String bcpEchqEserId = null;
    private String bcpBillTypeId = null;
    private String bcpCollectionCode = null;
    private String clientId = null;
    private String iasAutoCreateInd = null;
    private String iasShowOpt = null;
    private String iasOptSpId = null;

    private boolean dirty_serviceProviderId = false;
    private boolean dirty_serviceProviderName = false;
    private boolean dirty_description = false;
    private boolean dirty_status = false;
    private boolean dirty_friendlyAlias = false;
    private boolean dirty_appId = false;
    private boolean dirty_reqLinkupInd = false;
    private boolean dirty_createDt = false;
    private boolean dirty_lastModifyDt = false;
    private boolean dirty_createBy = false;
    private boolean dirty_lastModifyBy = false;
    private boolean dirty_serviceProviderNameEn = false;
    private boolean dirty_serviceProviderNameTc = false;
    private boolean dirty_serviceProviderNameSc = false;
    private boolean dirty_bcpAppId = false;
    private boolean dirty_bcpAppBranchCode = false;
    private boolean dirty_bcptfInd = false;
    private boolean dirty_bcptfRecipientAppId = false;
    private boolean dirty_bcptfRecipientAppType = false;
    private boolean dirty_bcptfSoapAction = false;
    private boolean dirty_bcptfEndPoint = false;
    private boolean dirty_billAppType = false;
    private boolean dirty_billAppNameEn = false;
    private boolean dirty_billAppNameSc = false;
    private boolean dirty_billAppNameTc = false;
    private boolean dirty_merchantNameEn = false;
    private boolean dirty_merchantNameTc = false;
    private boolean dirty_merchantNameSc = false;
    private boolean dirty_mobileInd = false;
    private boolean dirty_emailInd = false;
    private boolean dirty_mobileMsgExpiryTime = false;
    private boolean dirty_billDisplayType = false;
    private boolean dirty_commonBillDescEn = false;
    private boolean dirty_commonBillDescTc = false;
    private boolean dirty_commonBillDescSc = false;
    private boolean dirty_bcpEchqEserId = false;
    private boolean dirty_bcpBillTypeId = false;
    private boolean dirty_bcpCollectionCode = false;
    private boolean dirty_clientId = false;
    private boolean dirty_iasAutoCreateInd = false;
    private boolean dirty_iasShowOpt = false;
    private boolean dirty_iasOptSpId = false;

    /**
     * CmcServiceProvider_ Contructor
     */
    public CmcServiceProvider_() {
        super();
    }

    /**
     * CmcServiceProvider_ Constructor with specify PK
     */
    public CmcServiceProvider_(HPFW_Connection countCon, String inserviceProviderId)
            throws SQLException, NullPointerException {
        this();
        init(countCon, inserviceProviderId, false);
    }

    public CmcServiceProvider_(HPFW_Connection countCon, String inserviceProviderId, boolean forUpdate)
            throws SQLException, NullPointerException {
        this();
        init(countCon, inserviceProviderId, forUpdate);
    }

    static public void delete(HPFW_Connection countCon, String whereCluase, ArrayList<Parameter> paraL)
            throws SQLException {
        if (HPFW_Connection.DIRECT.equals(countCon.getUpdateMode())
                || HPFW_Connection.DIRECT_WITH_HISTORY.equals(countCon.getUpdateMode())) {
            String sql = "delete from ";
            sql += thisTableName + " ";
            sql += whereCluase;
            countCon.executeStatement(sql, paraL);
        }

        if (HPFW_Connection.HISTORY_ONLY.equals(countCon.getUpdateMode())
                || HPFW_Connection.DIRECT_WITH_HISTORY.equals(countCon.getUpdateMode())) {
            PreparedStatement stmt = null;
            ResultSet rs = null;

            try {
                String sql = "select service_provider_id from " + thisTableName + " " + whereCluase;
                stmt = countCon.getConnectionPtr().prepareStatement(sql);
                CommonDBUtils.setStatement(stmt, paraL);
                rs = stmt.executeQuery();

                while (rs.next()) {
                    String histsql = "insert into ";
                    histsql += thisTableName + thisTableHistName;
                    histsql += " (HIST_SEQ,HIST_action,HIST_update_type,HIST_status,HIST_server_id,last_modify_by,last_modify_dt,create_by,create_dt,service_provider_id";
                    histsql += ") values (?,?,?,?,?,?,?,?,?,?";
                    // perpare where ...
                    histsql += ",?";
                    histsql += ")";
                    ArrayList<Parameter> histParaList = new ArrayList<Parameter>();
                    histParaList.add(new Parameter(Parameter.String,
                            CommonDBUtils.getSequence(countCon, thisTableName + thisTableHistName)));
                    histParaList.add(new Parameter(Parameter.String, HPFW_Connection.DELETE));
                    histParaList.add(new Parameter(Parameter.String,
                            countCon.getUpdateMode().equals(HPFW_Connection.HISTORY_ONLY) ? HPFW_Connection.DUAL
                                    : HPFW_Connection.REMOTE));
                    histParaList.add(new Parameter(Parameter.String, HPFW_Connection.PENDING));
                    histParaList.add(new Parameter(Parameter.String, CommonDBUtils.getSERVER_ID()));
                    histParaList.add(new Parameter(Parameter.String, countCon.getLastUpdBy()));
                    histParaList.add(new Parameter(Parameter.Timestamp, countCon.getLastUpTime()));
                    histParaList.add(new Parameter(Parameter.String, countCon.getLastUpdBy()));
                    histParaList.add(new Parameter(Parameter.Timestamp, countCon.getLastUpTime()));
                    // perpare where ...
                    histParaList.add(new Parameter(Parameter.String, rs.getString(1)));
                    countCon.executeStatement(histsql, histParaList, true);
                }
            } catch (SQLException e) {
                throw e;
            } finally {
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (Exception ignore) {
                    }
                    rs = null;
                }
                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (Exception ignore) {
                    }
                    stmt = null;
                }
            }
        }
    }

    public void delete(HPFW_Connection countCon) throws SQLException {
        if (HPFW_Connection.DIRECT.equals(countCon.getUpdateMode())
                || HPFW_Connection.DIRECT_WITH_HISTORY.equals(countCon.getUpdateMode())) {
            String sql = "delete from ";
            sql += thisTableName;
            sql += " where 1=1 and service_provider_id = ? ";

            ArrayList<Parameter> paraList = new ArrayList<Parameter>();

            // perpare where ...
            paraList.add(new Parameter(Parameter.String, this.serviceProviderId));
            countCon.executeStatement(sql, paraList);
        }

        if (HPFW_Connection.HISTORY_ONLY.equals(countCon.getUpdateMode())
                || HPFW_Connection.DIRECT_WITH_HISTORY.equals(countCon.getUpdateMode())) {
            String histsql = "insert into ";
            histsql += thisTableName + thisTableHistName;
            histsql += " (HIST_SEQ,HIST_action,HIST_update_type,HIST_status,HIST_server_id,last_modify_by,last_modify_dt,create_by,create_dt";
            histsql += ",ServiceProviderId";
            histsql += ") values (?,?,?,?,?,?,?,?,?";
            // perpare where ...
            histsql += ",?";
            histsql += ")";
            ArrayList<Parameter> histParaList = new ArrayList<Parameter>();
            histParaList.add(new Parameter(Parameter.String,
                    CommonDBUtils.getSequence(countCon, thisTableName + thisTableHistName)));
            histParaList.add(new Parameter(Parameter.String, HPFW_Connection.DELETE));
            histParaList.add(new Parameter(Parameter.String,
                    countCon.getUpdateMode().equals(HPFW_Connection.HISTORY_ONLY) ? HPFW_Connection.DUAL
                            : HPFW_Connection.REMOTE));
            histParaList.add(new Parameter(Parameter.String, HPFW_Connection.PENDING));
            histParaList.add(new Parameter(Parameter.String, CommonDBUtils.getSERVER_ID()));
            histParaList.add(new Parameter(Parameter.String, countCon.getLastUpdBy()));
            histParaList.add(new Parameter(Parameter.Timestamp, countCon.getLastUpTime()));
            histParaList.add(new Parameter(Parameter.String, countCon.getLastUpdBy()));
            histParaList.add(new Parameter(Parameter.Timestamp, countCon.getLastUpTime()));
            // perpare where ...
            histParaList.add(new Parameter(Parameter.String, this.serviceProviderId));
            countCon.executeStatement(histsql, histParaList, true);
        }
    }

    public void insert(HPFW_Connection countCon) throws SQLException {
        if (HPFW_Connection.DIRECT.equals(countCon.getUpdateMode())
                || HPFW_Connection.DIRECT_WITH_HISTORY.equals(countCon.getUpdateMode())) {
            ArrayList<Parameter> paraList = new ArrayList<Parameter>();

            String sql = "insert into ";
            sql += thisTableName;
            sql += " (";

            // perpare set ...
            if (serviceProviderId != null)
                sql += "service_provider_id,";
            if (serviceProviderName != null)
                sql += "service_provider_name,";
            if (description != null)
                sql += "description,";
            if (status != null)
                sql += "status,";
            if (friendlyAlias != null)
                sql += "friendly_alias,";
            if (appId != null)
                sql += "app_id,";
            if (reqLinkupInd != null)
                sql += "req_linkup_ind,";
            if (serviceProviderNameEn != null)
                sql += "service_provider_name_en,";
            if (serviceProviderNameTc != null)
                sql += "service_provider_name_tc,";
            if (serviceProviderNameSc != null)
                sql += "service_provider_name_sc,";
            if (bcpAppId != null)
                sql += "bcp_app_id,";
            if (bcpAppBranchCode != null)
                sql += "bcp_app_branch_code,";
            if (bcptfInd != null)
                sql += "bcptf_ind,";
            if (bcptfRecipientAppId != null)
                sql += "bcptf_recipient_app_id,";
            if (bcptfRecipientAppType != null)
                sql += "bcptf_recipient_app_type,";
            if (bcptfSoapAction != null)
                sql += "bcptf_soap_action,";
            if (bcptfEndPoint != null)
                sql += "bcptf_end_point,";
            if (billAppType != null)
                sql += "bill_app_type,";
            if (billAppNameEn != null)
                sql += "bill_app_name_en,";
            if (billAppNameSc != null)
                sql += "bill_app_name_sc,";
            if (billAppNameTc != null)
                sql += "bill_app_name_tc,";
            if (merchantNameEn != null)
                sql += "merchant_name_en,";
            if (merchantNameTc != null)
                sql += "merchant_name_tc,";
            if (merchantNameSc != null)
                sql += "merchant_name_sc,";
            if (mobileInd != null)
                sql += "mobile_ind,";
            if (emailInd != null)
                sql += "email_ind,";
            if (mobileMsgExpiryTime != null)
                sql += "mobile_msg_expiry_time,";
            if (billDisplayType != null)
                sql += "bill_display_type,";
            if (commonBillDescEn != null)
                sql += "common_bill_desc_en,";
            if (commonBillDescTc != null)
                sql += "common_bill_desc_tc,";
            if (commonBillDescSc != null)
                sql += "common_bill_desc_sc,";
            if (bcpEchqEserId != null)
                sql += "bcp_echq_eser_id,";
            if (bcpBillTypeId != null)
                sql += "bcp_bill_type_id,";
            if (bcpCollectionCode != null)
                sql += "bcp_collection_code,";
            if (clientId != null)
                sql += "client_id,";
            if (iasAutoCreateInd != null)
                sql += "ias_auto_create_ind,";
            if (iasShowOpt != null)
                sql += "ias_show_opt,";
            if (iasOptSpId != null)
                sql += "ias_opt_sp_id,";
            sql += "create_by,last_modify_by,create_dt,last_modify_dt) values (";

            // perpare set ...
            if (serviceProviderId != null)
                sql += "?,";
            if (serviceProviderName != null)
                sql += "?,";
            if (description != null)
                sql += "?,";
            if (status != null)
                sql += "?,";
            if (friendlyAlias != null)
                sql += "?,";
            if (appId != null)
                sql += "?,";
            if (reqLinkupInd != null)
                sql += "?,";
            if (serviceProviderNameEn != null)
                sql += "?,";
            if (serviceProviderNameTc != null)
                sql += "?,";
            if (serviceProviderNameSc != null)
                sql += "?,";
            if (bcpAppId != null)
                sql += "?,";
            if (bcpAppBranchCode != null)
                sql += "?,";
            if (bcptfInd != null)
                sql += "?,";
            if (bcptfRecipientAppId != null)
                sql += "?,";
            if (bcptfRecipientAppType != null)
                sql += "?,";
            if (bcptfSoapAction != null)
                sql += "?,";
            if (bcptfEndPoint != null)
                sql += "?,";
            if (billAppType != null)
                sql += "?,";
            if (billAppNameEn != null)
                sql += "?,";
            if (billAppNameSc != null)
                sql += "?,";
            if (billAppNameTc != null)
                sql += "?,";
            if (merchantNameEn != null)
                sql += "?,";
            if (merchantNameTc != null)
                sql += "?,";
            if (merchantNameSc != null)
                sql += "?,";
            if (mobileInd != null)
                sql += "?,";
            if (emailInd != null)
                sql += "?,";
            if (mobileMsgExpiryTime != null)
                sql += "?,";
            if (billDisplayType != null)
                sql += "?,";
            if (commonBillDescEn != null)
                sql += "?,";
            if (commonBillDescTc != null)
                sql += "?,";
            if (commonBillDescSc != null)
                sql += "?,";
            if (bcpEchqEserId != null)
                sql += "?,";
            if (bcpBillTypeId != null)
                sql += "?,";
            if (bcpCollectionCode != null)
                sql += "?,";
            if (clientId != null)
                sql += "?,";
            if (iasAutoCreateInd != null)
                sql += "?,";
            if (iasShowOpt != null)
                sql += "?,";
            if (iasOptSpId != null)
                sql += "?,";
            sql += "?,?,?,?) ";

            // perpare set ...
            if (serviceProviderId != null)
                paraList.add(new Parameter(Parameter.String, this.serviceProviderId));
            if (serviceProviderName != null)
                paraList.add(new Parameter(Parameter.String, this.serviceProviderName));
            if (description != null)
                paraList.add(new Parameter(Parameter.String, this.description));
            if (status != null)
                paraList.add(new Parameter(Parameter.String, this.status));
            if (friendlyAlias != null)
                paraList.add(new Parameter(Parameter.String, this.friendlyAlias));
            if (appId != null)
                paraList.add(new Parameter(Parameter.String, this.appId));
            if (reqLinkupInd != null)
                paraList.add(new Parameter(Parameter.String, this.reqLinkupInd));
            if (serviceProviderNameEn != null)
                paraList.add(new Parameter(Parameter.String, this.serviceProviderNameEn));
            if (serviceProviderNameTc != null)
                paraList.add(new Parameter(Parameter.String, this.serviceProviderNameTc));
            if (serviceProviderNameSc != null)
                paraList.add(new Parameter(Parameter.String, this.serviceProviderNameSc));
            if (bcpAppId != null)
                paraList.add(new Parameter(Parameter.String, this.bcpAppId));
            if (bcpAppBranchCode != null)
                paraList.add(new Parameter(Parameter.String, this.bcpAppBranchCode));
            if (bcptfInd != null)
                paraList.add(new Parameter(Parameter.String, this.bcptfInd));
            if (bcptfRecipientAppId != null)
                paraList.add(new Parameter(Parameter.String, this.bcptfRecipientAppId));
            if (bcptfRecipientAppType != null)
                paraList.add(new Parameter(Parameter.String, this.bcptfRecipientAppType));
            if (bcptfSoapAction != null)
                paraList.add(new Parameter(Parameter.String, this.bcptfSoapAction));
            if (bcptfEndPoint != null)
                paraList.add(new Parameter(Parameter.String, this.bcptfEndPoint));
            if (billAppType != null)
                paraList.add(new Parameter(Parameter.String, this.billAppType));
            if (billAppNameEn != null)
                paraList.add(new Parameter(Parameter.String, this.billAppNameEn));
            if (billAppNameSc != null)
                paraList.add(new Parameter(Parameter.String, this.billAppNameSc));
            if (billAppNameTc != null)
                paraList.add(new Parameter(Parameter.String, this.billAppNameTc));
            if (merchantNameEn != null)
                paraList.add(new Parameter(Parameter.String, this.merchantNameEn));
            if (merchantNameTc != null)
                paraList.add(new Parameter(Parameter.String, this.merchantNameTc));
            if (merchantNameSc != null)
                paraList.add(new Parameter(Parameter.String, this.merchantNameSc));
            if (mobileInd != null)
                paraList.add(new Parameter(Parameter.String, this.mobileInd));
            if (emailInd != null)
                paraList.add(new Parameter(Parameter.String, this.emailInd));
            if (mobileMsgExpiryTime != null)
                paraList.add(new Parameter(Parameter.Integer, this.mobileMsgExpiryTime));
            if (billDisplayType != null)
                paraList.add(new Parameter(Parameter.String, this.billDisplayType));
            if (commonBillDescEn != null)
                paraList.add(new Parameter(Parameter.String, this.commonBillDescEn));
            if (commonBillDescTc != null)
                paraList.add(new Parameter(Parameter.String, this.commonBillDescTc));
            if (commonBillDescSc != null)
                paraList.add(new Parameter(Parameter.String, this.commonBillDescSc));
            if (bcpEchqEserId != null)
                paraList.add(new Parameter(Parameter.String, this.bcpEchqEserId));
            if (bcpBillTypeId != null)
                paraList.add(new Parameter(Parameter.String, this.bcpBillTypeId));
            if (bcpCollectionCode != null)
                paraList.add(new Parameter(Parameter.String, this.bcpCollectionCode));
            if (clientId != null)
                paraList.add(new Parameter(Parameter.String, this.clientId));
            if (iasAutoCreateInd != null)
                paraList.add(new Parameter(Parameter.String, this.iasAutoCreateInd));
            if (iasShowOpt != null)
                paraList.add(new Parameter(Parameter.String, this.iasShowOpt));
            if (iasOptSpId != null)
                paraList.add(new Parameter(Parameter.String, this.iasOptSpId));
            paraList.add(new Parameter(Parameter.String, countCon.getLastUpdBy()));
            paraList.add(new Parameter(Parameter.String, countCon.getLastUpdBy()));
            paraList.add(new Parameter(Parameter.Timestamp, countCon.getLastUpTime()));
            paraList.add(new Parameter(Parameter.Timestamp, countCon.getLastUpTime()));
            countCon.executeStatement(sql, paraList);
        }

        if (HPFW_Connection.HISTORY_ONLY.equals(countCon.getUpdateMode())
                || HPFW_Connection.DIRECT_WITH_HISTORY.equals(countCon.getUpdateMode())) {
            ArrayList<Parameter> histParaList = new ArrayList<Parameter>();

            String hist_sql = "insert into ";
            hist_sql += thisTableName + thisTableHistName;
            hist_sql += " (HIST_SEQ,HIST_action,HIST_update_type,HIST_status,HIST_server_id,";

            // perpare set ...
            if (serviceProviderId != null)
                hist_sql += "service_provider_id,";
            if (serviceProviderName != null)
                hist_sql += "service_provider_name,";
            if (description != null)
                hist_sql += "description,";
            if (status != null)
                hist_sql += "status,";
            if (friendlyAlias != null)
                hist_sql += "friendly_alias,";
            if (appId != null)
                hist_sql += "app_id,";
            if (reqLinkupInd != null)
                hist_sql += "req_linkup_ind,";
            if (serviceProviderNameEn != null)
                hist_sql += "service_provider_name_en,";
            if (serviceProviderNameTc != null)
                hist_sql += "service_provider_name_tc,";
            if (serviceProviderNameSc != null)
                hist_sql += "service_provider_name_sc,";
            if (bcpAppId != null)
                hist_sql += "bcp_app_id,";
            if (bcpAppBranchCode != null)
                hist_sql += "bcp_app_branch_code,";
            if (bcptfInd != null)
                hist_sql += "bcptf_ind,";
            if (bcptfRecipientAppId != null)
                hist_sql += "bcptf_recipient_app_id,";
            if (bcptfRecipientAppType != null)
                hist_sql += "bcptf_recipient_app_type,";
            if (bcptfSoapAction != null)
                hist_sql += "bcptf_soap_action,";
            if (bcptfEndPoint != null)
                hist_sql += "bcptf_end_point,";
            if (billAppType != null)
                hist_sql += "bill_app_type,";
            if (billAppNameEn != null)
                hist_sql += "bill_app_name_en,";
            if (billAppNameSc != null)
                hist_sql += "bill_app_name_sc,";
            if (billAppNameTc != null)
                hist_sql += "bill_app_name_tc,";
            if (merchantNameEn != null)
                hist_sql += "merchant_name_en,";
            if (merchantNameTc != null)
                hist_sql += "merchant_name_tc,";
            if (merchantNameSc != null)
                hist_sql += "merchant_name_sc,";
            if (mobileInd != null)
                hist_sql += "mobile_ind,";
            if (emailInd != null)
                hist_sql += "email_ind,";
            if (mobileMsgExpiryTime != null)
                hist_sql += "mobile_msg_expiry_time,";
            if (billDisplayType != null)
                hist_sql += "bill_display_type,";
            if (commonBillDescEn != null)
                hist_sql += "common_bill_desc_en,";
            if (commonBillDescTc != null)
                hist_sql += "common_bill_desc_tc,";
            if (commonBillDescSc != null)
                hist_sql += "common_bill_desc_sc,";
            if (bcpEchqEserId != null)
                hist_sql += "bcp_echq_eser_id,";
            if (bcpBillTypeId != null)
                hist_sql += "bcp_bill_type_id,";
            if (bcpCollectionCode != null)
                hist_sql += "bcp_collection_code,";
            if (clientId != null)
                hist_sql += "client_id,";
            if (iasAutoCreateInd != null)
                hist_sql += "ias_auto_create_ind,";
            if (iasShowOpt != null)
                hist_sql += "ias_show_opt,";
            if (iasOptSpId != null)
                hist_sql += "ias_opt_sp_id,";
            hist_sql += "create_by,last_modify_by,create_dt,last_modify_dt) values (?,?,?,?,?,";

            // perpare set ...
            if (serviceProviderId != null)
                hist_sql += "?,";
            if (serviceProviderName != null)
                hist_sql += "?,";
            if (description != null)
                hist_sql += "?,";
            if (status != null)
                hist_sql += "?,";
            if (friendlyAlias != null)
                hist_sql += "?,";
            if (appId != null)
                hist_sql += "?,";
            if (reqLinkupInd != null)
                hist_sql += "?,";
            if (serviceProviderNameEn != null)
                hist_sql += "?,";
            if (serviceProviderNameTc != null)
                hist_sql += "?,";
            if (serviceProviderNameSc != null)
                hist_sql += "?,";
            if (bcpAppId != null)
                hist_sql += "?,";
            if (bcpAppBranchCode != null)
                hist_sql += "?,";
            if (bcptfInd != null)
                hist_sql += "?,";
            if (bcptfRecipientAppId != null)
                hist_sql += "?,";
            if (bcptfRecipientAppType != null)
                hist_sql += "?,";
            if (bcptfSoapAction != null)
                hist_sql += "?,";
            if (bcptfEndPoint != null)
                hist_sql += "?,";
            if (billAppType != null)
                hist_sql += "?,";
            if (billAppNameEn != null)
                hist_sql += "?,";
            if (billAppNameSc != null)
                hist_sql += "?,";
            if (billAppNameTc != null)
                hist_sql += "?,";
            if (merchantNameEn != null)
                hist_sql += "?,";
            if (merchantNameTc != null)
                hist_sql += "?,";
            if (merchantNameSc != null)
                hist_sql += "?,";
            if (mobileInd != null)
                hist_sql += "?,";
            if (emailInd != null)
                hist_sql += "?,";
            if (mobileMsgExpiryTime != null)
                hist_sql += "?,";
            if (billDisplayType != null)
                hist_sql += "?,";
            if (commonBillDescEn != null)
                hist_sql += "?,";
            if (commonBillDescTc != null)
                hist_sql += "?,";
            if (commonBillDescSc != null)
                hist_sql += "?,";
            if (bcpEchqEserId != null)
                hist_sql += "?,";
            if (bcpBillTypeId != null)
                hist_sql += "?,";
            if (bcpCollectionCode != null)
                hist_sql += "?,";
            if (clientId != null)
                hist_sql += "?,";
            if (iasAutoCreateInd != null)
                hist_sql += "?,";
            if (iasShowOpt != null)
                hist_sql += "?,";
            if (iasOptSpId != null)
                hist_sql += "?,";
            hist_sql += "?,?,?,?) ";
            histParaList.add(new Parameter(Parameter.String,
                    CommonDBUtils.getSequence(countCon, thisTableName + thisTableHistName)));
            histParaList.add(new Parameter(Parameter.String, HPFW_Connection.INSERT));
            histParaList.add(new Parameter(Parameter.String,
                    countCon.getUpdateMode().equals(HPFW_Connection.HISTORY_ONLY) ? HPFW_Connection.DUAL
                            : HPFW_Connection.REMOTE));
            histParaList.add(new Parameter(Parameter.String, HPFW_Connection.PENDING));
            histParaList.add(new Parameter(Parameter.String, CommonDBUtils.getSERVER_ID()));

            // perpare set ...
            if (serviceProviderId != null)
                histParaList.add(new Parameter(Parameter.String, this.serviceProviderId));
            if (serviceProviderName != null)
                histParaList.add(new Parameter(Parameter.String, this.serviceProviderName));
            if (description != null)
                histParaList.add(new Parameter(Parameter.String, this.description));
            if (status != null)
                histParaList.add(new Parameter(Parameter.String, this.status));
            if (friendlyAlias != null)
                histParaList.add(new Parameter(Parameter.String, this.friendlyAlias));
            if (appId != null)
                histParaList.add(new Parameter(Parameter.String, this.appId));
            if (reqLinkupInd != null)
                histParaList.add(new Parameter(Parameter.String, this.reqLinkupInd));
            if (serviceProviderNameEn != null)
                histParaList.add(new Parameter(Parameter.String, this.serviceProviderNameEn));
            if (serviceProviderNameTc != null)
                histParaList.add(new Parameter(Parameter.String, this.serviceProviderNameTc));
            if (serviceProviderNameSc != null)
                histParaList.add(new Parameter(Parameter.String, this.serviceProviderNameSc));
            if (bcpAppId != null)
                histParaList.add(new Parameter(Parameter.String, this.bcpAppId));
            if (bcpAppBranchCode != null)
                histParaList.add(new Parameter(Parameter.String, this.bcpAppBranchCode));
            if (bcptfInd != null)
                histParaList.add(new Parameter(Parameter.String, this.bcptfInd));
            if (bcptfRecipientAppId != null)
                histParaList.add(new Parameter(Parameter.String, this.bcptfRecipientAppId));
            if (bcptfRecipientAppType != null)
                histParaList.add(new Parameter(Parameter.String, this.bcptfRecipientAppType));
            if (bcptfSoapAction != null)
                histParaList.add(new Parameter(Parameter.String, this.bcptfSoapAction));
            if (bcptfEndPoint != null)
                histParaList.add(new Parameter(Parameter.String, this.bcptfEndPoint));
            if (billAppType != null)
                histParaList.add(new Parameter(Parameter.String, this.billAppType));
            if (billAppNameEn != null)
                histParaList.add(new Parameter(Parameter.String, this.billAppNameEn));
            if (billAppNameSc != null)
                histParaList.add(new Parameter(Parameter.String, this.billAppNameSc));
            if (billAppNameTc != null)
                histParaList.add(new Parameter(Parameter.String, this.billAppNameTc));
            if (merchantNameEn != null)
                histParaList.add(new Parameter(Parameter.String, this.merchantNameEn));
            if (merchantNameTc != null)
                histParaList.add(new Parameter(Parameter.String, this.merchantNameTc));
            if (merchantNameSc != null)
                histParaList.add(new Parameter(Parameter.String, this.merchantNameSc));
            if (mobileInd != null)
                histParaList.add(new Parameter(Parameter.String, this.mobileInd));
            if (emailInd != null)
                histParaList.add(new Parameter(Parameter.String, this.emailInd));
            if (mobileMsgExpiryTime != null)
                histParaList.add(new Parameter(Parameter.Integer, this.mobileMsgExpiryTime));
            if (billDisplayType != null)
                histParaList.add(new Parameter(Parameter.String, this.billDisplayType));
            if (commonBillDescEn != null)
                histParaList.add(new Parameter(Parameter.String, this.commonBillDescEn));
            if (commonBillDescTc != null)
                histParaList.add(new Parameter(Parameter.String, this.commonBillDescTc));
            if (commonBillDescSc != null)
                histParaList.add(new Parameter(Parameter.String, this.commonBillDescSc));
            if (bcpEchqEserId != null)
                histParaList.add(new Parameter(Parameter.String, this.bcpEchqEserId));
            if (bcpBillTypeId != null)
                histParaList.add(new Parameter(Parameter.String, this.bcpBillTypeId));
            if (bcpCollectionCode != null)
                histParaList.add(new Parameter(Parameter.String, this.bcpCollectionCode));
            if (clientId != null)
                histParaList.add(new Parameter(Parameter.String, this.clientId));
            if (iasAutoCreateInd != null)
                histParaList.add(new Parameter(Parameter.String, this.iasAutoCreateInd));
            if (iasShowOpt != null)
                histParaList.add(new Parameter(Parameter.String, this.iasShowOpt));
            if (iasOptSpId != null)
                histParaList.add(new Parameter(Parameter.String, this.iasOptSpId));
            histParaList.add(new Parameter(Parameter.String, countCon.getLastUpdBy()));
            histParaList.add(new Parameter(Parameter.String, countCon.getLastUpdBy()));
            histParaList.add(new Parameter(Parameter.Timestamp, countCon.getLastUpTime()));
            histParaList.add(new Parameter(Parameter.Timestamp, countCon.getLastUpTime()));
            countCon.executeStatement(hist_sql, histParaList, false);
        }
    }

    public static void update(HPFW_Connection countCon, String setClause, ArrayList<Parameter> paraL,
            String whereClause, ArrayList<Parameter> whereParaL)
            throws SQLException {
        // e.g. whereClause = "set data1 = ?, data2 = ? " ; => "data1,data2"
        String selectString = setClause.toLowerCase();
        selectString = selectString.replaceAll("set", "");
        selectString = selectString.replaceAll("=", "");
        selectString = selectString.replaceAll(" ", "");
        selectString = selectString.replaceAll("\\?", "");
        String[] selectStringArray = selectString.split(",");

        if (HPFW_Connection.DIRECT.equals(countCon.getUpdateMode())
                || HPFW_Connection.DIRECT_WITH_HISTORY.equals(countCon.getUpdateMode())) {
            ArrayList<Parameter> tempList = new ArrayList<Parameter>();
            String sql = "update ";
            sql += thisTableName + " ";
            sql += setClause + " ,last_modify_by = ?,last_modify_dt = ? ";
            sql += whereClause + " ";
            tempList.addAll(paraL);
            tempList.add(new Parameter(Parameter.String, countCon.getLastUpdBy()));
            tempList.add(new Parameter(Parameter.Timestamp, countCon.getLastUpTime()));
            tempList.addAll(whereParaL);
            countCon.executeStatement(sql, tempList);
        }

        if (HPFW_Connection.HISTORY_ONLY.equals(countCon.getUpdateMode())
                || HPFW_Connection.DIRECT_WITH_HISTORY.equals(countCon.getUpdateMode())) {
            PreparedStatement stmt = null;
            ResultSet rs = null;
            try {
                String sql = "select service_provider_id from " + thisTableName + " " + whereClause;
                stmt = countCon.getConnectionPtr().prepareStatement(sql);
                CommonDBUtils.setStatement(stmt, whereParaL);
                rs = stmt.executeQuery();

                while (rs.next()) {
                    String histsql = "insert into ";
                    histsql += thisTableName + thisTableHistName;
                    histsql += " (HIST_SEQ,HIST_action,HIST_update_type,HIST_status,HIST_server_id,last_modify_by,last_modify_dt,create_by,create_dt,service_provider_id,"
                            + selectString;
                    histsql += ") values (?,?,?,?,?,?,?,?,?,?";
                    for (int i = 0; i < selectStringArray.length; i++)
                        histsql += ",?";
                    histsql += ")";

                    ArrayList<Parameter> histParaList = new ArrayList<Parameter>();
                    histParaList.add(new Parameter(Parameter.String,
                            CommonDBUtils.getSequence(countCon, thisTableName + thisTableHistName)));
                    histParaList.add(new Parameter(Parameter.String, HPFW_Connection.UPDATE));
                    histParaList.add(new Parameter(Parameter.String,
                            countCon.getUpdateMode().equals(HPFW_Connection.HISTORY_ONLY) ? HPFW_Connection.DUAL
                                    : HPFW_Connection.REMOTE));
                    histParaList.add(new Parameter(Parameter.String, HPFW_Connection.PENDING));
                    histParaList.add(new Parameter(Parameter.String, CommonDBUtils.getSERVER_ID()));
                    histParaList.add(new Parameter(Parameter.String, countCon.getLastUpdBy()));
                    histParaList.add(new Parameter(Parameter.Timestamp, countCon.getLastUpTime()));
                    histParaList.add(new Parameter(Parameter.String, countCon.getLastUpdBy()));
                    histParaList.add(new Parameter(Parameter.Timestamp, countCon.getLastUpTime()));
                    // perpare where ...
                    histParaList.add(new Parameter(Parameter.String, rs.getString(1)));
                    histParaList.addAll(paraL);
                    countCon.executeStatement(histsql, histParaList, true);

                }
            } catch (SQLException e) {
                throw e;
            } finally {
                if (paraL != null) {
                    paraL.clear();
                    paraL = null;
                }
                if (whereParaL != null) {
                    whereParaL.clear();
                    whereParaL = null;
                }

                if (rs != null) {
                    try {
                        rs.close();
                    } catch (Exception ignore) {
                    }
                    rs = null;
                }
                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (Exception ignore) {
                    }
                    stmt = null;
                }
            }
        }
    }

    public void update(HPFW_Connection countCon) throws SQLException {
        if (HPFW_Connection.DIRECT.equals(countCon.getUpdateMode())
                || HPFW_Connection.DIRECT_WITH_HISTORY.equals(countCon.getUpdateMode())) {
            ArrayList<Parameter> paraList = new ArrayList<Parameter>();

            String sql = "update ";
            sql += thisTableName;
            sql += " set ";
            // perpare set ...
            if (dirty_serviceProviderName)
                sql += "service_provider_name = ?,";
            if (dirty_description)
                sql += "description = ?,";
            if (dirty_status)
                sql += "status = ?,";
            if (dirty_friendlyAlias)
                sql += "friendly_alias = ?,";
            if (dirty_appId)
                sql += "app_id = ?,";
            if (dirty_reqLinkupInd)
                sql += "req_linkup_ind = ?,";
            if (dirty_serviceProviderNameEn)
                sql += "service_provider_name_en = ?,";
            if (dirty_serviceProviderNameTc)
                sql += "service_provider_name_tc = ?,";
            if (dirty_serviceProviderNameSc)
                sql += "service_provider_name_sc = ?,";
            if (dirty_bcpAppId)
                sql += "bcp_app_id = ?,";
            if (dirty_bcpAppBranchCode)
                sql += "bcp_app_branch_code = ?,";
            if (dirty_bcptfInd)
                sql += "bcptf_ind = ?,";
            if (dirty_bcptfRecipientAppId)
                sql += "bcptf_recipient_app_id = ?,";
            if (dirty_bcptfRecipientAppType)
                sql += "bcptf_recipient_app_type = ?,";
            if (dirty_bcptfSoapAction)
                sql += "bcptf_soap_action = ?,";
            if (dirty_bcptfEndPoint)
                sql += "bcptf_end_point = ?,";
            if (dirty_billAppType)
                sql += "bill_app_type = ?,";
            if (dirty_billAppNameEn)
                sql += "bill_app_name_en = ?,";
            if (dirty_billAppNameSc)
                sql += "bill_app_name_sc = ?,";
            if (dirty_billAppNameTc)
                sql += "bill_app_name_tc = ?,";
            if (dirty_merchantNameEn)
                sql += "merchant_name_en = ?,";
            if (dirty_merchantNameTc)
                sql += "merchant_name_tc = ?,";
            if (dirty_merchantNameSc)
                sql += "merchant_name_sc = ?,";
            if (dirty_mobileInd)
                sql += "mobile_ind = ?,";
            if (dirty_emailInd)
                sql += "email_ind = ?,";
            if (dirty_mobileMsgExpiryTime)
                sql += "mobile_msg_expiry_time = ?,";
            if (dirty_billDisplayType)
                sql += "bill_display_type = ?,";
            if (dirty_commonBillDescEn)
                sql += "common_bill_desc_en = ?,";
            if (dirty_commonBillDescTc)
                sql += "common_bill_desc_tc = ?,";
            if (dirty_commonBillDescSc)
                sql += "common_bill_desc_sc = ?,";
            if (dirty_bcpEchqEserId)
                sql += "bcp_echq_eser_id = ?,";
            if (dirty_bcpBillTypeId)
                sql += "bcp_bill_type_id = ?,";
            if (dirty_bcpCollectionCode)
                sql += "bcp_collection_code = ?,";
            if (dirty_clientId)
                sql += "client_id = ?,";
            if (dirty_iasAutoCreateInd)
                sql += "ias_auto_create_ind = ?,";
            if (dirty_iasShowOpt)
                sql += "ias_show_opt = ?,";
            if (dirty_iasOptSpId)
                sql += "ias_opt_sp_id = ?,";

            sql += " last_modify_by = ?,last_modify_dt = ? where 1=1 and service_provider_id = ? ";
            // perpare set ...
            if (dirty_serviceProviderName)
                paraList.add(new Parameter(Parameter.String, this.serviceProviderName));
            if (dirty_description)
                paraList.add(new Parameter(Parameter.String, this.description));
            if (dirty_status)
                paraList.add(new Parameter(Parameter.String, this.status));
            if (dirty_friendlyAlias)
                paraList.add(new Parameter(Parameter.String, this.friendlyAlias));
            if (dirty_appId)
                paraList.add(new Parameter(Parameter.String, this.appId));
            if (dirty_reqLinkupInd)
                paraList.add(new Parameter(Parameter.String, this.reqLinkupInd));
            if (dirty_serviceProviderNameEn)
                paraList.add(new Parameter(Parameter.String, this.serviceProviderNameEn));
            if (dirty_serviceProviderNameTc)
                paraList.add(new Parameter(Parameter.String, this.serviceProviderNameTc));
            if (dirty_serviceProviderNameSc)
                paraList.add(new Parameter(Parameter.String, this.serviceProviderNameSc));
            if (dirty_bcpAppId)
                paraList.add(new Parameter(Parameter.String, this.bcpAppId));
            if (dirty_bcpAppBranchCode)
                paraList.add(new Parameter(Parameter.String, this.bcpAppBranchCode));
            if (dirty_bcptfInd)
                paraList.add(new Parameter(Parameter.String, this.bcptfInd));
            if (dirty_bcptfRecipientAppId)
                paraList.add(new Parameter(Parameter.String, this.bcptfRecipientAppId));
            if (dirty_bcptfRecipientAppType)
                paraList.add(new Parameter(Parameter.String, this.bcptfRecipientAppType));
            if (dirty_bcptfSoapAction)
                paraList.add(new Parameter(Parameter.String, this.bcptfSoapAction));
            if (dirty_bcptfEndPoint)
                paraList.add(new Parameter(Parameter.String, this.bcptfEndPoint));
            if (dirty_billAppType)
                paraList.add(new Parameter(Parameter.String, this.billAppType));
            if (dirty_billAppNameEn)
                paraList.add(new Parameter(Parameter.String, this.billAppNameEn));
            if (dirty_billAppNameSc)
                paraList.add(new Parameter(Parameter.String, this.billAppNameSc));
            if (dirty_billAppNameTc)
                paraList.add(new Parameter(Parameter.String, this.billAppNameTc));
            if (dirty_merchantNameEn)
                paraList.add(new Parameter(Parameter.String, this.merchantNameEn));
            if (dirty_merchantNameTc)
                paraList.add(new Parameter(Parameter.String, this.merchantNameTc));
            if (dirty_merchantNameSc)
                paraList.add(new Parameter(Parameter.String, this.merchantNameSc));
            if (dirty_mobileInd)
                paraList.add(new Parameter(Parameter.String, this.mobileInd));
            if (dirty_emailInd)
                paraList.add(new Parameter(Parameter.String, this.emailInd));
            if (dirty_mobileMsgExpiryTime)
                paraList.add(new Parameter(Parameter.Integer, this.mobileMsgExpiryTime));
            if (dirty_billDisplayType)
                paraList.add(new Parameter(Parameter.String, this.billDisplayType));
            if (dirty_commonBillDescEn)
                paraList.add(new Parameter(Parameter.String, this.commonBillDescEn));
            if (dirty_commonBillDescTc)
                paraList.add(new Parameter(Parameter.String, this.commonBillDescTc));
            if (dirty_commonBillDescSc)
                paraList.add(new Parameter(Parameter.String, this.commonBillDescSc));
            if (dirty_bcpEchqEserId)
                paraList.add(new Parameter(Parameter.String, this.bcpEchqEserId));
            if (dirty_bcpBillTypeId)
                paraList.add(new Parameter(Parameter.String, this.bcpBillTypeId));
            if (dirty_bcpCollectionCode)
                paraList.add(new Parameter(Parameter.String, this.bcpCollectionCode));
            if (dirty_clientId)
                paraList.add(new Parameter(Parameter.String, this.clientId));
            if (dirty_iasAutoCreateInd)
                paraList.add(new Parameter(Parameter.String, this.iasAutoCreateInd));
            if (dirty_iasShowOpt)
                paraList.add(new Parameter(Parameter.String, this.iasShowOpt));
            if (dirty_iasOptSpId)
                paraList.add(new Parameter(Parameter.String, this.iasOptSpId));
            paraList.add(new Parameter(Parameter.String, countCon.getLastUpdBy()));
            paraList.add(new Parameter(Parameter.Timestamp, countCon.getLastUpTime()));
            // perpare where ...
            paraList.add(new Parameter(Parameter.String, this.serviceProviderId));
            countCon.executeStatement(sql, paraList);
        }

        if (HPFW_Connection.HISTORY_ONLY.equals(countCon.getUpdateMode())
                || HPFW_Connection.DIRECT_WITH_HISTORY.equals(countCon.getUpdateMode())) {
            ArrayList<Parameter> histParaList = new ArrayList<Parameter>();

            String hist_sql = "insert into ";
            hist_sql += thisTableName + thisTableHistName;
            hist_sql += " (HIST_SEQ,HIST_action,HIST_update_type,HIST_status,HIST_server_id,";

            // perpare set ...
            hist_sql += "service_provider_id,";
            if (dirty_serviceProviderName)
                hist_sql += "service_provider_name,";
            if (dirty_description)
                hist_sql += "description,";
            if (dirty_status)
                hist_sql += "status,";
            if (dirty_friendlyAlias)
                hist_sql += "friendly_alias,";
            if (dirty_appId)
                hist_sql += "app_id,";
            if (dirty_reqLinkupInd)
                hist_sql += "req_linkup_ind,";
            if (dirty_serviceProviderNameEn)
                hist_sql += "service_provider_name_en,";
            if (dirty_serviceProviderNameTc)
                hist_sql += "service_provider_name_tc,";
            if (dirty_serviceProviderNameSc)
                hist_sql += "service_provider_name_sc,";
            if (dirty_bcpAppId)
                hist_sql += "bcp_app_id,";
            if (dirty_bcpAppBranchCode)
                hist_sql += "bcp_app_branch_code,";
            if (dirty_bcptfInd)
                hist_sql += "bcptf_ind,";
            if (dirty_bcptfRecipientAppId)
                hist_sql += "bcptf_recipient_app_id,";
            if (dirty_bcptfRecipientAppType)
                hist_sql += "bcptf_recipient_app_type,";
            if (dirty_bcptfSoapAction)
                hist_sql += "bcptf_soap_action,";
            if (dirty_bcptfEndPoint)
                hist_sql += "bcptf_end_point,";
            if (dirty_billAppType)
                hist_sql += "bill_app_type,";
            if (dirty_billAppNameEn)
                hist_sql += "bill_app_name_en,";
            if (dirty_billAppNameSc)
                hist_sql += "bill_app_name_sc,";
            if (dirty_billAppNameTc)
                hist_sql += "bill_app_name_tc,";
            if (dirty_merchantNameEn)
                hist_sql += "merchant_name_en,";
            if (dirty_merchantNameTc)
                hist_sql += "merchant_name_tc,";
            if (dirty_merchantNameSc)
                hist_sql += "merchant_name_sc,";
            if (dirty_mobileInd)
                hist_sql += "mobile_ind,";
            if (dirty_emailInd)
                hist_sql += "email_ind,";
            if (dirty_mobileMsgExpiryTime)
                hist_sql += "mobile_msg_expiry_time,";
            if (dirty_billDisplayType)
                hist_sql += "bill_display_type,";
            if (dirty_commonBillDescEn)
                hist_sql += "common_bill_desc_en,";
            if (dirty_commonBillDescTc)
                hist_sql += "common_bill_desc_tc,";
            if (dirty_commonBillDescSc)
                hist_sql += "common_bill_desc_sc,";
            if (dirty_bcpEchqEserId)
                hist_sql += "bcp_echq_eser_id,";
            if (dirty_bcpBillTypeId)
                hist_sql += "bcp_bill_type_id,";
            if (dirty_bcpCollectionCode)
                hist_sql += "bcp_collection_code,";
            if (dirty_clientId)
                hist_sql += "client_id,";
            if (dirty_iasAutoCreateInd)
                hist_sql += "ias_auto_create_ind,";
            if (dirty_iasShowOpt)
                hist_sql += "ias_show_opt,";
            if (dirty_iasOptSpId)
                hist_sql += "ias_opt_sp_id,";
            hist_sql += "last_modify_by,last_modify_dt,create_by,create_dt) values (?,?,?,?,?,";

            // perpare set ...
            hist_sql += "?,";
            if (dirty_serviceProviderName)
                hist_sql += "?,";
            if (dirty_description)
                hist_sql += "?,";
            if (dirty_status)
                hist_sql += "?,";
            if (dirty_friendlyAlias)
                hist_sql += "?,";
            if (dirty_appId)
                hist_sql += "?,";
            if (dirty_reqLinkupInd)
                hist_sql += "?,";
            if (dirty_serviceProviderNameEn)
                hist_sql += "?,";
            if (dirty_serviceProviderNameTc)
                hist_sql += "?,";
            if (dirty_serviceProviderNameSc)
                hist_sql += "?,";
            if (dirty_bcpAppId)
                hist_sql += "?,";
            if (dirty_bcpAppBranchCode)
                hist_sql += "?,";
            if (dirty_bcptfInd)
                hist_sql += "?,";
            if (dirty_bcptfRecipientAppId)
                hist_sql += "?,";
            if (dirty_bcptfRecipientAppType)
                hist_sql += "?,";
            if (dirty_bcptfSoapAction)
                hist_sql += "?,";
            if (dirty_bcptfEndPoint)
                hist_sql += "?,";
            if (dirty_billAppType)
                hist_sql += "?,";
            if (dirty_billAppNameEn)
                hist_sql += "?,";
            if (dirty_billAppNameSc)
                hist_sql += "?,";
            if (dirty_billAppNameTc)
                hist_sql += "?,";
            if (dirty_merchantNameEn)
                hist_sql += "?,";
            if (dirty_merchantNameTc)
                hist_sql += "?,";
            if (dirty_merchantNameSc)
                hist_sql += "?,";
            if (dirty_mobileInd)
                hist_sql += "?,";
            if (dirty_emailInd)
                hist_sql += "?,";
            if (dirty_mobileMsgExpiryTime)
                hist_sql += "?,";
            if (dirty_billDisplayType)
                hist_sql += "?,";
            if (dirty_commonBillDescEn)
                hist_sql += "?,";
            if (dirty_commonBillDescTc)
                hist_sql += "?,";
            if (dirty_commonBillDescSc)
                hist_sql += "?,";
            if (dirty_bcpEchqEserId)
                hist_sql += "?,";
            if (dirty_bcpBillTypeId)
                hist_sql += "?,";
            if (dirty_bcpCollectionCode)
                hist_sql += "?,";
            if (dirty_clientId)
                hist_sql += "?,";
            if (dirty_iasAutoCreateInd)
                hist_sql += "?,";
            if (dirty_iasShowOpt)
                hist_sql += "?,";
            if (dirty_iasOptSpId)
                hist_sql += "?,";
            hist_sql += "?,?,?,?) ";
            histParaList.add(new Parameter(Parameter.String,
                    CommonDBUtils.getSequence(countCon, thisTableName + thisTableHistName)));
            histParaList.add(new Parameter(Parameter.String, HPFW_Connection.UPDATE));
            histParaList.add(new Parameter(Parameter.String,
                    countCon.getUpdateMode().equals(HPFW_Connection.HISTORY_ONLY) ? HPFW_Connection.DUAL
                            : HPFW_Connection.REMOTE));
            histParaList.add(new Parameter(Parameter.String, HPFW_Connection.PENDING));
            histParaList.add(new Parameter(Parameter.String, CommonDBUtils.getSERVER_ID()));

            // perpare set ...
            histParaList.add(new Parameter(Parameter.String, this.serviceProviderId));
            if (dirty_serviceProviderName)
                histParaList.add(new Parameter(Parameter.String, this.serviceProviderName));
            if (dirty_description)
                histParaList.add(new Parameter(Parameter.String, this.description));
            if (dirty_status)
                histParaList.add(new Parameter(Parameter.String, this.status));
            if (dirty_friendlyAlias)
                histParaList.add(new Parameter(Parameter.String, this.friendlyAlias));
            if (dirty_appId)
                histParaList.add(new Parameter(Parameter.String, this.appId));
            if (dirty_reqLinkupInd)
                histParaList.add(new Parameter(Parameter.String, this.reqLinkupInd));
            if (dirty_serviceProviderNameEn)
                histParaList.add(new Parameter(Parameter.String, this.serviceProviderNameEn));
            if (dirty_serviceProviderNameTc)
                histParaList.add(new Parameter(Parameter.String, this.serviceProviderNameTc));
            if (dirty_serviceProviderNameSc)
                histParaList.add(new Parameter(Parameter.String, this.serviceProviderNameSc));
            if (dirty_bcpAppId)
                histParaList.add(new Parameter(Parameter.String, this.bcpAppId));
            if (dirty_bcpAppBranchCode)
                histParaList.add(new Parameter(Parameter.String, this.bcpAppBranchCode));
            if (dirty_bcptfInd)
                histParaList.add(new Parameter(Parameter.String, this.bcptfInd));
            if (dirty_bcptfRecipientAppId)
                histParaList.add(new Parameter(Parameter.String, this.bcptfRecipientAppId));
            if (dirty_bcptfRecipientAppType)
                histParaList.add(new Parameter(Parameter.String, this.bcptfRecipientAppType));
            if (dirty_bcptfSoapAction)
                histParaList.add(new Parameter(Parameter.String, this.bcptfSoapAction));
            if (dirty_bcptfEndPoint)
                histParaList.add(new Parameter(Parameter.String, this.bcptfEndPoint));
            if (dirty_billAppType)
                histParaList.add(new Parameter(Parameter.String, this.billAppType));
            if (dirty_billAppNameEn)
                histParaList.add(new Parameter(Parameter.String, this.billAppNameEn));
            if (dirty_billAppNameSc)
                histParaList.add(new Parameter(Parameter.String, this.billAppNameSc));
            if (dirty_billAppNameTc)
                histParaList.add(new Parameter(Parameter.String, this.billAppNameTc));
            if (dirty_merchantNameEn)
                histParaList.add(new Parameter(Parameter.String, this.merchantNameEn));
            if (dirty_merchantNameTc)
                histParaList.add(new Parameter(Parameter.String, this.merchantNameTc));
            if (dirty_merchantNameSc)
                histParaList.add(new Parameter(Parameter.String, this.merchantNameSc));
            if (dirty_mobileInd)
                histParaList.add(new Parameter(Parameter.String, this.mobileInd));
            if (dirty_emailInd)
                histParaList.add(new Parameter(Parameter.String, this.emailInd));
            if (dirty_mobileMsgExpiryTime)
                histParaList.add(new Parameter(Parameter.Integer, this.mobileMsgExpiryTime));
            if (dirty_billDisplayType)
                histParaList.add(new Parameter(Parameter.String, this.billDisplayType));
            if (dirty_commonBillDescEn)
                histParaList.add(new Parameter(Parameter.String, this.commonBillDescEn));
            if (dirty_commonBillDescTc)
                histParaList.add(new Parameter(Parameter.String, this.commonBillDescTc));
            if (dirty_commonBillDescSc)
                histParaList.add(new Parameter(Parameter.String, this.commonBillDescSc));
            if (dirty_bcpEchqEserId)
                histParaList.add(new Parameter(Parameter.String, this.bcpEchqEserId));
            if (dirty_bcpBillTypeId)
                histParaList.add(new Parameter(Parameter.String, this.bcpBillTypeId));
            if (dirty_bcpCollectionCode)
                histParaList.add(new Parameter(Parameter.String, this.bcpCollectionCode));
            if (dirty_clientId)
                histParaList.add(new Parameter(Parameter.String, this.clientId));
            if (dirty_iasAutoCreateInd)
                histParaList.add(new Parameter(Parameter.String, this.iasAutoCreateInd));
            if (dirty_iasShowOpt)
                histParaList.add(new Parameter(Parameter.String, this.iasShowOpt));
            if (dirty_iasOptSpId)
                histParaList.add(new Parameter(Parameter.String, this.iasOptSpId));
            histParaList.add(new Parameter(Parameter.String, countCon.getLastUpdBy()));
            histParaList.add(new Parameter(Parameter.Timestamp, countCon.getLastUpTime()));
            histParaList.add(new Parameter(Parameter.String, countCon.getLastUpdBy()));
            histParaList.add(new Parameter(Parameter.Timestamp, countCon.getLastUpTime()));
            countCon.executeStatement(hist_sql, histParaList, true);
        }
        this.forUpdate = false;
    }

    public static ArrayList<CmcServiceProvider_> getResultList(HPFW_Connection countCon, String whereCluase,
            ArrayList<Parameter> paraL) throws SQLException {
        boolean needClose = countCon == null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<CmcServiceProvider_> result = new ArrayList<CmcServiceProvider_>();
        String sql = "select " + thisTableName + ".* from ";
        try {
            sql += thisTableName + " ";
            sql += whereCluase;
            if (countCon == null)
                countCon = HPFW_Connection.getHPFW_Connection();
            stmt = countCon.getConnectionPtr().prepareStatement(sql);
            if (paraL != null)
                CommonDBUtils.setStatement(stmt, paraL);
            rs = stmt.executeQuery();

            while (rs.next()) {
                CmcServiceProvider_ obj = new CmcServiceProvider_();
                obj.serviceProviderId = rs.getString("service_provider_id"); // String
                obj.serviceProviderName = rs.getString("service_provider_name"); // String
                obj.description = rs.getString("description"); // String
                obj.status = rs.getString("status"); // String
                obj.friendlyAlias = rs.getString("friendly_alias"); // String
                obj.appId = rs.getString("app_id"); // String
                obj.reqLinkupInd = rs.getString("req_linkup_ind"); // String
                obj.createDt = rs.getTimestamp("create_dt"); // Timestamp
                obj.lastModifyDt = rs.getTimestamp("last_modify_dt"); // Timestamp
                obj.createBy = rs.getString("create_by"); // String
                obj.lastModifyBy = rs.getString("last_modify_by"); // String
                obj.serviceProviderNameEn = rs.getString("service_provider_name_en"); // String
                obj.serviceProviderNameTc = rs.getString("service_provider_name_tc"); // String
                obj.serviceProviderNameSc = rs.getString("service_provider_name_sc"); // String
                obj.bcpAppId = rs.getString("bcp_app_id"); // String
                obj.bcpAppBranchCode = rs.getString("bcp_app_branch_code"); // String
                obj.bcptfInd = rs.getString("bcptf_ind"); // String
                obj.bcptfRecipientAppId = rs.getString("bcptf_recipient_app_id"); // String
                obj.bcptfRecipientAppType = rs.getString("bcptf_recipient_app_type"); // String
                obj.bcptfSoapAction = rs.getString("bcptf_soap_action"); // String
                obj.bcptfEndPoint = rs.getString("bcptf_end_point"); // String
                obj.billAppType = rs.getString("bill_app_type"); // String
                obj.billAppNameEn = rs.getString("bill_app_name_en"); // String
                obj.billAppNameSc = rs.getString("bill_app_name_sc"); // String
                obj.billAppNameTc = rs.getString("bill_app_name_tc"); // String
                obj.merchantNameEn = rs.getString("merchant_name_en"); // String
                obj.merchantNameTc = rs.getString("merchant_name_tc"); // String
                obj.merchantNameSc = rs.getString("merchant_name_sc"); // String
                obj.mobileInd = rs.getString("mobile_ind"); // String
                obj.emailInd = rs.getString("email_ind"); // String
                obj.mobileMsgExpiryTime = new Integer(rs.getInt("mobile_msg_expiry_time")); // Integer
                obj.billDisplayType = rs.getString("bill_display_type"); // String
                obj.commonBillDescEn = rs.getString("common_bill_desc_en"); // String
                obj.commonBillDescTc = rs.getString("common_bill_desc_tc"); // String
                obj.commonBillDescSc = rs.getString("common_bill_desc_sc"); // String
                obj.bcpEchqEserId = rs.getString("bcp_echq_eser_id"); // String
                obj.bcpBillTypeId = rs.getString("bcp_bill_type_id"); // String
                obj.bcpCollectionCode = rs.getString("bcp_collection_code"); // String
                obj.clientId = rs.getString("client_id"); // String
                obj.iasAutoCreateInd = rs.getString("ias_auto_create_ind"); // String
                obj.iasShowOpt = rs.getString("ias_show_opt"); // String
                obj.iasOptSpId = rs.getString("ias_opt_sp_id"); // String
                obj.initialized = true;
                result.add(obj);
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception ignore) {
                }
                rs = null;
            }

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception ignore) {
                }
                stmt = null;
            }

            if (needClose && countCon != null) {
                try {
                    HPFW_Connection.close(countCon);
                    countCon = null;
                } catch (Exception ignore) {
                }
            }
        }
        return result;
    }

    public void setinitialized(boolean initialized) {
        this.initialized = initialized;
    }

    public void init(HPFW_Connection countCon, final String inserviceProviderId, boolean forUpdate)
            throws SQLException, NullPointerException {
        this.forUpdate = forUpdate;
        boolean needClose = countCon == null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql = "select * from " + thisTableName + " where 1=1 and service_provider_id = ? ";
            if (forUpdate)
                sql += "for update";
            if (countCon == null)
                countCon = HPFW_Connection.getHPFW_Connection();
            stmt = countCon.getConnectionPtr().prepareStatement(sql);
            stmt.setString(1, inserviceProviderId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                serviceProviderId = rs.getString("service_provider_id"); // String
                serviceProviderName = rs.getString("service_provider_name"); // String
                description = rs.getString("description"); // String
                status = rs.getString("status"); // String
                friendlyAlias = rs.getString("friendly_alias"); // String
                appId = rs.getString("app_id"); // String
                reqLinkupInd = rs.getString("req_linkup_ind"); // String
                createDt = rs.getTimestamp("create_dt"); // Timestamp
                lastModifyDt = rs.getTimestamp("last_modify_dt"); // Timestamp
                createBy = rs.getString("create_by"); // String
                lastModifyBy = rs.getString("last_modify_by"); // String
                serviceProviderNameEn = rs.getString("service_provider_name_en"); // String
                serviceProviderNameTc = rs.getString("service_provider_name_tc"); // String
                serviceProviderNameSc = rs.getString("service_provider_name_sc"); // String
                bcpAppId = rs.getString("bcp_app_id"); // String
                bcpAppBranchCode = rs.getString("bcp_app_branch_code"); // String
                bcptfInd = rs.getString("bcptf_ind"); // String
                bcptfRecipientAppId = rs.getString("bcptf_recipient_app_id"); // String
                bcptfRecipientAppType = rs.getString("bcptf_recipient_app_type"); // String
                bcptfSoapAction = rs.getString("bcptf_soap_action"); // String
                bcptfEndPoint = rs.getString("bcptf_end_point"); // String
                billAppType = rs.getString("bill_app_type"); // String
                billAppNameEn = rs.getString("bill_app_name_en"); // String
                billAppNameSc = rs.getString("bill_app_name_sc"); // String
                billAppNameTc = rs.getString("bill_app_name_tc"); // String
                merchantNameEn = rs.getString("merchant_name_en"); // String
                merchantNameTc = rs.getString("merchant_name_tc"); // String
                merchantNameSc = rs.getString("merchant_name_sc"); // String
                mobileInd = rs.getString("mobile_ind"); // String
                emailInd = rs.getString("email_ind"); // String
                mobileMsgExpiryTime = new Integer(rs.getInt("mobile_msg_expiry_time")); // Integer
                billDisplayType = rs.getString("bill_display_type"); // String
                commonBillDescEn = rs.getString("common_bill_desc_en"); // String
                commonBillDescTc = rs.getString("common_bill_desc_tc"); // String
                commonBillDescSc = rs.getString("common_bill_desc_sc"); // String
                bcpEchqEserId = rs.getString("bcp_echq_eser_id"); // String
                bcpBillTypeId = rs.getString("bcp_bill_type_id"); // String
                bcpCollectionCode = rs.getString("bcp_collection_code"); // String
                clientId = rs.getString("client_id"); // String
                iasAutoCreateInd = rs.getString("ias_auto_create_ind"); // String
                iasShowOpt = rs.getString("ias_show_opt"); // String
                iasOptSpId = rs.getString("ias_opt_sp_id"); // String
                initialized = true;
            } else {
                throw new java.lang.NullPointerException();
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception ignore) {
                }
                rs = null;
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception ignore) {
                }
                stmt = null;
            }
            if (needClose && countCon != null) {
                try {
                    HPFW_Connection.close(countCon);
                    countCon = null;
                } catch (Exception ignore) {
                }
            }
        }
    }
    // }INIT

    /**
     * CmcServiceProvider_ Destroyer
     */
    protected void finalize() throws Throwable {
        serviceProviderId = null;
        serviceProviderName = null;
        description = null;
        status = null;
        friendlyAlias = null;
        appId = null;
        reqLinkupInd = null;
        createDt = null;
        lastModifyDt = null;
        createBy = null;
        lastModifyBy = null;
        serviceProviderNameEn = null;
        serviceProviderNameTc = null;
        serviceProviderNameSc = null;
        bcpAppId = null;
        bcpAppBranchCode = null;
        bcptfInd = null;
        bcptfRecipientAppId = null;
        bcptfRecipientAppType = null;
        bcptfSoapAction = null;
        bcptfEndPoint = null;
        billAppType = null;
        billAppNameEn = null;
        billAppNameSc = null;
        billAppNameTc = null;
        merchantNameEn = null;
        merchantNameTc = null;
        merchantNameSc = null;
        mobileInd = null;
        emailInd = null;
        mobileMsgExpiryTime = null;
        billDisplayType = null;
        commonBillDescEn = null;
        commonBillDescTc = null;
        commonBillDescSc = null;
        bcpEchqEserId = null;
        bcpBillTypeId = null;
        bcpCollectionCode = null;
        clientId = null;
        iasAutoCreateInd = null;
        iasShowOpt = null;
        iasOptSpId = null;
    }

    /**
     * Get service_provider_id
     */
    public String getServiceProviderId() {
        return serviceProviderId == null ? "" : serviceProviderId;
    }

    /**
     * Set service_provider_id
     */
    public void setServiceProviderId(final String inServiceProviderId) {
        serviceProviderId = inServiceProviderId;
        dirty_serviceProviderId = true;
    }

    /**
     * Get service_provider_name
     */
    public String getServiceProviderName() {
        return serviceProviderName == null ? "" : serviceProviderName;
    }

    /**
     * Set service_provider_name
     */
    public void setServiceProviderName(final String inServiceProviderName) {
        serviceProviderName = inServiceProviderName;
        dirty_serviceProviderName = true;
    }

    /**
     * Get description
     */
    public String getDescription() {
        return description == null ? "" : description;
    }

    /**
     * Set description
     */
    public void setDescription(final String inDescription) {
        description = inDescription;
        dirty_description = true;
    }

    /**
     * Get status
     */
    public String getStatus() {
        return status == null ? "" : status;
    }

    /**
     * Set status
     */
    public void setStatus(final String inStatus) {
        status = inStatus;
        dirty_status = true;
    }

    /**
     * Get friendly_alias
     */
    public String getFriendlyAlias() {
        return friendlyAlias == null ? "" : friendlyAlias;
    }

    /**
     * Set friendly_alias
     */
    public void setFriendlyAlias(final String inFriendlyAlias) {
        friendlyAlias = inFriendlyAlias;
        dirty_friendlyAlias = true;
    }

    /**
     * Get app_id
     */
    public String getAppId() {
        return appId == null ? "" : appId;
    }

    /**
     * Set app_id
     */
    public void setAppId(final String inAppId) {
        appId = inAppId;
        dirty_appId = true;
    }

    /**
     * Get req_linkup_ind
     */
    public String getReqLinkupInd() {
        return reqLinkupInd == null ? "" : reqLinkupInd;
    }

    /**
     * Set req_linkup_ind
     */
    public void setReqLinkupInd(final String inReqLinkupInd) {
        reqLinkupInd = inReqLinkupInd;
        dirty_reqLinkupInd = true;
    }

    /**
     * Get create_dt
     */
    public Timestamp getCreateDt() {
        return createDt;
    }

    /**
     * Set create_dt
     */
    public void setCreateDt(final Timestamp inCreateDt) {
        createDt = inCreateDt;
        dirty_createDt = true;
    }

    /**
     * Get last_modify_dt
     */
    public Timestamp getLastModifyDt() {
        return lastModifyDt;
    }

    /**
     * Set last_modify_dt
     */
    public void setLastModifyDt(final Timestamp inLastModifyDt) {
        lastModifyDt = inLastModifyDt;
        dirty_lastModifyDt = true;
    }

    /**
     * Get create_by
     */
    public String getCreateBy() {
        return createBy == null ? "" : createBy;
    }

    /**
     * Set create_by
     */
    public void setCreateBy(final String inCreateBy) {
        createBy = inCreateBy;
        dirty_createBy = true;
    }

    /**
     * Get last_modify_by
     */
    public String getLastModifyBy() {
        return lastModifyBy == null ? "" : lastModifyBy;
    }

    /**
     * Set last_modify_by
     */
    public void setLastModifyBy(final String inLastModifyBy) {
        lastModifyBy = inLastModifyBy;
        dirty_lastModifyBy = true;
    }

    /**
     * Get service_provider_name_en
     */
    public String getServiceProviderNameEn() {
        return serviceProviderNameEn == null ? "" : serviceProviderNameEn;
    }

    /**
     * Set service_provider_name_en
     */
    public void setServiceProviderNameEn(final String inServiceProviderNameEn) {
        serviceProviderNameEn = inServiceProviderNameEn;
        dirty_serviceProviderNameEn = true;
    }

    /**
     * Get service_provider_name_tc
     */
    public String getServiceProviderNameTc() {
        return serviceProviderNameTc == null ? "" : serviceProviderNameTc;
    }

    /**
     * Set service_provider_name_tc
     */
    public void setServiceProviderNameTc(final String inServiceProviderNameTc) {
        serviceProviderNameTc = inServiceProviderNameTc;
        dirty_serviceProviderNameTc = true;
    }

    /**
     * Get service_provider_name_sc
     */
    public String getServiceProviderNameSc() {
        return serviceProviderNameSc == null ? "" : serviceProviderNameSc;
    }

    /**
     * Set service_provider_name_sc
     */
    public void setServiceProviderNameSc(final String inServiceProviderNameSc) {
        serviceProviderNameSc = inServiceProviderNameSc;
        dirty_serviceProviderNameSc = true;
    }

    /**
     * Get bcp_app_id
     */
    public String getBcpAppId() {
        return bcpAppId == null ? "" : bcpAppId;
    }

    /**
     * Set bcp_app_id
     */
    public void setBcpAppId(final String inBcpAppId) {
        bcpAppId = inBcpAppId;
        dirty_bcpAppId = true;
    }

    /**
     * Get bcp_app_branch_code
     */
    public String getBcpAppBranchCode() {
        return bcpAppBranchCode == null ? "" : bcpAppBranchCode;
    }

    /**
     * Set bcp_app_branch_code
     */
    public void setBcpAppBranchCode(final String inBcpAppBranchCode) {
        bcpAppBranchCode = inBcpAppBranchCode;
        dirty_bcpAppBranchCode = true;
    }

    /**
     * Get bcptf_ind
     */
    public String getBcptfInd() {
        return bcptfInd == null ? "" : bcptfInd;
    }

    /**
     * Set bcptf_ind
     */
    public void setBcptfInd(final String inBcptfInd) {
        bcptfInd = inBcptfInd;
        dirty_bcptfInd = true;
    }

    /**
     * Get bcptf_recipient_app_id
     */
    public String getBcptfRecipientAppId() {
        return bcptfRecipientAppId == null ? "" : bcptfRecipientAppId;
    }

    /**
     * Set bcptf_recipient_app_id
     */
    public void setBcptfRecipientAppId(final String inBcptfRecipientAppId) {
        bcptfRecipientAppId = inBcptfRecipientAppId;
        dirty_bcptfRecipientAppId = true;
    }

    /**
     * Get bcptf_recipient_app_type
     */
    public String getBcptfRecipientAppType() {
        return bcptfRecipientAppType == null ? "" : bcptfRecipientAppType;
    }

    /**
     * Set bcptf_recipient_app_type
     */
    public void setBcptfRecipientAppType(final String inBcptfRecipientAppType) {
        bcptfRecipientAppType = inBcptfRecipientAppType;
        dirty_bcptfRecipientAppType = true;
    }

    /**
     * Get bcptf_soap_action
     */
    public String getBcptfSoapAction() {
        return bcptfSoapAction == null ? "" : bcptfSoapAction;
    }

    /**
     * Set bcptf_soap_action
     */
    public void setBcptfSoapAction(final String inBcptfSoapAction) {
        bcptfSoapAction = inBcptfSoapAction;
        dirty_bcptfSoapAction = true;
    }

    /**
     * Get bcptf_end_point
     */
    public String getBcptfEndPoint() {
        return bcptfEndPoint == null ? "" : bcptfEndPoint;
    }

    /**
     * Set bcptf_end_point
     */
    public void setBcptfEndPoint(final String inBcptfEndPoint) {
        bcptfEndPoint = inBcptfEndPoint;
        dirty_bcptfEndPoint = true;
    }

    /**
     * Get bill_app_type
     */
    public String getBillAppType() {
        return billAppType == null ? "" : billAppType;
    }

    /**
     * Set bill_app_type
     */
    public void setBillAppType(final String inBillAppType) {
        billAppType = inBillAppType;
        dirty_billAppType = true;
    }

    /**
     * Get bill_app_name_en
     */
    public String getBillAppNameEn() {
        return billAppNameEn == null ? "" : billAppNameEn;
    }

    /**
     * Set bill_app_name_en
     */
    public void setBillAppNameEn(final String inBillAppNameEn) {
        billAppNameEn = inBillAppNameEn;
        dirty_billAppNameEn = true;
    }

    /**
     * Get bill_app_name_sc
     */
    public String getBillAppNameSc() {
        return billAppNameSc == null ? "" : billAppNameSc;
    }

    /**
     * Set bill_app_name_sc
     */
    public void setBillAppNameSc(final String inBillAppNameSc) {
        billAppNameSc = inBillAppNameSc;
        dirty_billAppNameSc = true;
    }

    /**
     * Get bill_app_name_tc
     */
    public String getBillAppNameTc() {
        return billAppNameTc == null ? "" : billAppNameTc;
    }

    /**
     * Set bill_app_name_tc
     */
    public void setBillAppNameTc(final String inBillAppNameTc) {
        billAppNameTc = inBillAppNameTc;
        dirty_billAppNameTc = true;
    }

    /**
     * Get merchant_name_en
     */
    public String getMerchantNameEn() {
        return merchantNameEn == null ? "" : merchantNameEn;
    }

    /**
     * Set merchant_name_en
     */
    public void setMerchantNameEn(final String inMerchantNameEn) {
        merchantNameEn = inMerchantNameEn;
        dirty_merchantNameEn = true;
    }

    /**
     * Get merchant_name_tc
     */
    public String getMerchantNameTc() {
        return merchantNameTc == null ? "" : merchantNameTc;
    }

    /**
     * Set merchant_name_tc
     */
    public void setMerchantNameTc(final String inMerchantNameTc) {
        merchantNameTc = inMerchantNameTc;
        dirty_merchantNameTc = true;
    }

    /**
     * Get merchant_name_sc
     */
    public String getMerchantNameSc() {
        return merchantNameSc == null ? "" : merchantNameSc;
    }

    /**
     * Set merchant_name_sc
     */
    public void setMerchantNameSc(final String inMerchantNameSc) {
        merchantNameSc = inMerchantNameSc;
        dirty_merchantNameSc = true;
    }

    /**
     * Get mobile_ind
     */
    public String getMobileInd() {
        return mobileInd == null ? "" : mobileInd;
    }

    /**
     * Set mobile_ind
     */
    public void setMobileInd(final String inMobileInd) {
        mobileInd = inMobileInd;
        dirty_mobileInd = true;
    }

    /**
     * Get email_ind
     */
    public String getEmailInd() {
        return emailInd == null ? "" : emailInd;
    }

    /**
     * Set email_ind
     */
    public void setEmailInd(final String inEmailInd) {
        emailInd = inEmailInd;
        dirty_emailInd = true;
    }

    /**
     * Get mobile_msg_expiry_time
     */
    public Integer getMobileMsgExpiryTime() {
        return mobileMsgExpiryTime;
    }

    /**
     * Set mobile_msg_expiry_time
     */
    public void setMobileMsgExpiryTime(final Integer inMobileMsgExpiryTime) {
        mobileMsgExpiryTime = inMobileMsgExpiryTime;
        dirty_mobileMsgExpiryTime = true;
    }

    /**
     * Get bill_display_type
     */
    public String getBillDisplayType() {
        return billDisplayType == null ? "" : billDisplayType;
    }

    /**
     * Set bill_display_type
     */
    public void setBillDisplayType(final String inBillDisplayType) {
        billDisplayType = inBillDisplayType;
        dirty_billDisplayType = true;
    }

    /**
     * Get common_bill_desc_en
     */
    public String getCommonBillDescEn() {
        return commonBillDescEn == null ? "" : commonBillDescEn;
    }

    /**
     * Set common_bill_desc_en
     */
    public void setCommonBillDescEn(final String inCommonBillDescEn) {
        commonBillDescEn = inCommonBillDescEn;
        dirty_commonBillDescEn = true;
    }

    /**
     * Get common_bill_desc_tc
     */
    public String getCommonBillDescTc() {
        return commonBillDescTc == null ? "" : commonBillDescTc;
    }

    /**
     * Set common_bill_desc_tc
     */
    public void setCommonBillDescTc(final String inCommonBillDescTc) {
        commonBillDescTc = inCommonBillDescTc;
        dirty_commonBillDescTc = true;
    }

    /**
     * Get common_bill_desc_sc
     */
    public String getCommonBillDescSc() {
        return commonBillDescSc == null ? "" : commonBillDescSc;
    }

    /**
     * Set common_bill_desc_sc
     */
    public void setCommonBillDescSc(final String inCommonBillDescSc) {
        commonBillDescSc = inCommonBillDescSc;
        dirty_commonBillDescSc = true;
    }

    /**
     * Get bcp_echq_eser_id
     */
    public String getBcpEchqEserId() {
        return bcpEchqEserId == null ? "" : bcpEchqEserId;
    }

    /**
     * Set bcp_echq_eser_id
     */
    public void setBcpEchqEserId(final String inBcpEchqEserId) {
        bcpEchqEserId = inBcpEchqEserId;
        dirty_bcpEchqEserId = true;
    }

    /**
     * Get bcp_bill_type_id
     */
    public String getBcpBillTypeId() {
        return bcpBillTypeId == null ? "" : bcpBillTypeId;
    }

    /**
     * Set bcp_bill_type_id
     */
    public void setBcpBillTypeId(final String inBcpBillTypeId) {
        bcpBillTypeId = inBcpBillTypeId;
        dirty_bcpBillTypeId = true;
    }

    /**
     * Get bcp_collection_code
     */
    public String getBcpCollectionCode() {
        return bcpCollectionCode == null ? "" : bcpCollectionCode;
    }

    /**
     * Set bcp_collection_code
     */
    public void setBcpCollectionCode(final String inBcpCollectionCode) {
        bcpCollectionCode = inBcpCollectionCode;
        dirty_bcpCollectionCode = true;
    }

    /**
     * Get client_id
     */
    public String getClientId() {
        return clientId == null ? "" : clientId;
    }

    /**
     * Set client_id
     */
    public void setClientId(final String inClientId) {
        clientId = inClientId;
        dirty_clientId = true;
    }

    /**
     * Get ias_auto_create_ind
     */
    public String getIasAutoCreateInd() {
        return iasAutoCreateInd == null ? "" : iasAutoCreateInd;
    }

    /**
     * Set ias_auto_create_ind
     */
    public void setIasAutoCreateInd(final String inIasAutoCreateInd) {
        iasAutoCreateInd = inIasAutoCreateInd;
        dirty_iasAutoCreateInd = true;
    }

    /**
     * Get ias_show_opt
     */
    public String getIasShowOpt() {
        return iasShowOpt == null ? "" : iasShowOpt;
    }

    /**
     * Set ias_show_opt
     */
    public void setIasShowOpt(final String inIasShowOpt) {
        iasShowOpt = inIasShowOpt;
        dirty_iasShowOpt = true;
    }

    /**
     * Get ias_opt_sp_id
     */
    public String getIasOptSpId() {
        return iasOptSpId == null ? "" : iasOptSpId;
    }

    /**
     * Set ias_opt_sp_id
     */
    public void setIasOptSpId(final String inIasOptSpId) {
        iasOptSpId = inIasOptSpId;
        dirty_iasOptSpId = true;
    }
}
