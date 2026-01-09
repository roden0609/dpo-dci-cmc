package hk.gov.cmc.dao.maintainmessage.application;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import hk.gov.cmc.common.CmcAppConstants;
import hk.gov.cmc.common.IntegrationConstants;
import hk.gov.cmc.common.RecipientIDTypeConstant;
import hk.gov.cmc.common.StatusConstants;
import hk.gov.cmc.persistence.connection.hpfw.HPFW_Connection;
import hk.gov.cmc.persistence.connection.hpfw.Parameter;
import hk.gov.cmc.persistence.ias.application.IasApplication_;
import hk.gov.cmc.persistence.ias.application.IasUserApplication_;
import hk.gov.cmc.utils.common.EncUtils;

public class IasApplicationDAO {

    private static Log logger = LogFactory.getLog(IasApplicationDAO.class);

    public IasApplicationDAO() {
    }

    public void createIasApplication(String portalId, String iasApplicationId, String templateId, String templateVersion, 
        String mergedIasApplicationTitleEn, String mergedIasApplicationTitleTc, String mergedIasApplicationTitleSc, 
        String mergedIasApplicationDetailEn, String mergedIasApplicationDetailTc, String mergedIasApplicationDetailSc, 
        String iasEsAppSuffixEn, String iasEsAppSuffixTc, String iasEsAppSuffixSc,
        String encKeyStoreId, String iasEsAppSuffixTagName, HPFW_Connection conn
    ) throws Exception {
        logger.debug("createIasApplication - portalId: " + portalId + ", iasApplicationId: " + iasApplicationId 
            + ", templateId: " + templateId + ", templateVersion: " + templateVersion
            + ", encKeyStoreId: " + encKeyStoreId + ", iasEsAppSuffixTagName: " + iasEsAppSuffixTagName
        );
        logger.debug("createIasApplication mergedIasApplicationTitleEn: " + mergedIasApplicationTitleEn  + ", mergedIasApplicationTitleTc: " + mergedIasApplicationTitleTc + ", mergedIasApplicationTitleSc: " + mergedIasApplicationTitleSc);
        logger.debug("createIasApplication mergedIasApplicationDetailEn: " + mergedIasApplicationDetailEn  + ", mergedIasApplicationDetailTc: " + mergedIasApplicationDetailTc  + ", mergedIasApplicationDetailSc: " + mergedIasApplicationDetailSc);
        logger.debug("createIasApplication iasEsAppSuffixEn: " + iasEsAppSuffixEn + ", iasEsAppSuffixTc: " + iasEsAppSuffixTc + ", iasEsAppSuffixSc: " + iasEsAppSuffixSc);

        IasApplication_ iasApplication = new IasApplication_();
        iasApplication.setIasApplicationId(iasApplicationId);
        iasApplication.setPortalId(portalId);
        iasApplication.setTemplateId(templateId);
        iasApplication.setTemplateVersion(templateVersion);
        iasApplication.setTitleEn(mergedIasApplicationTitleEn);
        iasApplication.setTitleTc(mergedIasApplicationTitleTc);
        iasApplication.setTitleSc(mergedIasApplicationTitleSc);

        String encryptedMergedIasApplicationDetailEn = EncUtils.encrypt(mergedIasApplicationDetailEn);
        String encryptedMergedIasApplicationDetailTc = EncUtils.encrypt(mergedIasApplicationDetailTc);
        String encryptedMergedIasApplicationDetailSc = EncUtils.encrypt(mergedIasApplicationDetailSc);
        iasApplication.setEncInd(CmcAppConstants.ENC_IND_YES);
        iasApplication.setEncKeyStoreId(encKeyStoreId);

        iasApplication.setDetailEn(encryptedMergedIasApplicationDetailEn);
        iasApplication.setDetailTc(encryptedMergedIasApplicationDetailTc);
        iasApplication.setDetailSc(encryptedMergedIasApplicationDetailSc);

        iasApplication.setIasEsAppSuffixEn(iasEsAppSuffixEn);
        iasApplication.setIasEsAppSuffixTc(iasEsAppSuffixTc);
        iasApplication.setIasEsAppSuffixSc(iasEsAppSuffixSc);

        conn.begin(null, conn.getLastUpTime(), HPFW_Connection.DIRECT);
        iasApplication.insert(conn);
        conn.begin(null, conn.getLastUpTime(), HPFW_Connection.DIRECT_WITH_HISTORY);
    }

    public boolean createIasUserApplicationByOpenId(
        HPFW_Connection conn, String serviceProviderId,
        String clientId, String openId, String iasApplicationId, String tranId,
        String appRefNum, String appStatus, Timestamp statusUpdateDate, String contactNum,
        String contactEmail, String miscInfo, String operationType, String iasUserNotiId
    ) throws Exception {
        logger.debug("createIasUserApplicationByOpenId - serviceProviderId: " + serviceProviderId
            + ", clientId: " + clientId + ", openId: " + openId + ", iasApplicationId: " + iasApplicationId + ", tranId: " + tranId + ", appRefNum: " + appRefNum 
            + ", appStatus: " + appStatus + ", statusUpdateDate: " + statusUpdateDate + ", contactNum: " + contactNum + ", contactEmail: " + contactEmail 
            + ", miscInfo: " + miscInfo + ", operationType: " + operationType + ", iasUserNotiId: " + iasUserNotiId);

        IasUserApplication_ iasUserApplication = new IasUserApplication_();
        iasUserApplication.setClientId(clientId);
        iasUserApplication.setRecipientId(openId);
        iasUserApplication.setIasApplicationId(iasApplicationId);
        iasUserApplication.setRecipientIdType(RecipientIDTypeConstant.OPEN_ID);
        iasUserApplication.setTranId(tranId);
        iasUserApplication.setAppRefNum(appRefNum);
        iasUserApplication.setAppStatus(appStatus);
        iasUserApplication.setAppStatusUpdateDate(statusUpdateDate);
        iasUserApplication.setContactNum(contactNum);
        iasUserApplication.setContactEmail(contactEmail);
        iasUserApplication.setMiscInfo(miscInfo);
        iasUserApplication.setOperationType(operationType);
        iasUserApplication.setDeleteInd(StatusConstants.DELETE_IND_NOT_DELETED);
        iasUserApplication.setIasDeleteNotiStatus(IntegrationConstants.IAS_NOTI_STATUS_NEW);
        iasUserApplication.setIasNotiStatus(IntegrationConstants.IAS_NOTI_STATUS_NEW);
        iasUserApplication.setNotiId(iasUserNotiId);
        iasUserApplication.insert(conn);

        return true;
    }

    public void createIasUserApplicationByHKID(
        HPFW_Connection conn, String serviceProviderId,
        String clientId, String hkidHashed, String iasApplicationId, String tranId,
        String appRefNum, String appStatus, Timestamp statusUpdateDate, String contactNum, String contactEmail, 
        String miscInfo, String operationType, String iasUserNotiId, String hkidEncrypted
    ) throws Exception {
        logger.debug("createIasUserApplicationByHKID - serviceProviderId: " + serviceProviderId
            + ", clientId: " + clientId + ", hkidHashed: " + hkidHashed + ", hkidEncrypted: " + hkidEncrypted + ", iasApplicationId: " + iasApplicationId 
            + ", tranId: " + tranId + ", appRefNum: " + appRefNum + ", appStatus: " + appStatus + ", statusUpdateDate: " + statusUpdateDate + ", contactNum: " + contactNum 
            + ", contactEmail: " + contactEmail + ", miscInfo: " + miscInfo + ", operationType: " + operationType + ", iasUserNotiId: " + iasUserNotiId);

        IasUserApplication_ iasUserApplication = new IasUserApplication_();
        iasUserApplication.setClientId(clientId);
        iasUserApplication.setRecipientId(hkidHashed);
        iasUserApplication.setHkidEncrypted(hkidEncrypted);
        iasUserApplication.setIasApplicationId(iasApplicationId);
        iasUserApplication.setRecipientIdType(RecipientIDTypeConstant.HKID);
        iasUserApplication.setTranId(tranId);
        iasUserApplication.setAppRefNum(appRefNum);
        iasUserApplication.setAppStatus(appStatus);
        iasUserApplication.setAppStatusUpdateDate(statusUpdateDate);
        iasUserApplication.setContactNum(contactNum);
        iasUserApplication.setContactEmail(contactEmail);
        iasUserApplication.setMiscInfo(miscInfo);
        iasUserApplication.setOperationType(operationType);
        iasUserApplication.setDeleteInd(StatusConstants.DELETE_IND_NOT_DELETED);
        iasUserApplication.setIasDeleteNotiStatus(IntegrationConstants.IAS_NOTI_STATUS_NEW);
        iasUserApplication.setIasNotiStatus(IntegrationConstants.IAS_NOTI_STATUS_NEW);
        iasUserApplication.setNotiId(iasUserNotiId);
        iasUserApplication.insert(conn);

    }

    public void markDeleteIasUserApplicationByCorrTranId(HPFW_Connection conn, String clientId, String recipientId, String tranId, String corrTranId) throws Exception {
        logger.debug("markDeleteIasUserApplication - clientId: " + clientId + ", recipientId: " + recipientId + ", tranId: " + tranId + ", corrTranId: " + corrTranId);

        ArrayList<Parameter> paraSetList = new ArrayList<Parameter>();
        ArrayList<Parameter> paraWhereList = new ArrayList<Parameter>();

        paraSetList.add(new Parameter(Parameter.String, StatusConstants.DELETE_IND_PENDING));
        paraSetList.add(new Parameter(Parameter.String, tranId));
        paraWhereList.add(new Parameter(Parameter.String, clientId));
        paraWhereList.add(new Parameter(Parameter.String, recipientId));
        paraWhereList.add(new Parameter(Parameter.String, corrTranId));

        IasUserApplication_.update(conn, 
            "set DELETE_IND = ?, ACTION_TRAN_ID = ?", paraSetList, 
            "where CLIENT_ID = ? and RECIPIENT_ID = ? and TRAN_ID = ?", paraWhereList);
    }

    public void deleteLatestIasUserApplication(String clientId, String recipientId, String appRefNum, HPFW_Connection conn) throws Exception {
        logger.debug("deleteLatestIasUserApplication - clientId: " + clientId + ", recipientId: " + recipientId + ", appRefNum: " + appRefNum);

        ArrayList<Parameter> paraSetList = new ArrayList<Parameter>();
        ArrayList<Parameter> paraWhereList = new ArrayList<Parameter>();

        paraSetList.add(new Parameter(Parameter.String, StatusConstants.DELETE_IND_DELETED));
        paraWhereList.add(new Parameter(Parameter.String, clientId));
        paraWhereList.add(new Parameter(Parameter.String, recipientId));
        paraWhereList.add(new Parameter(Parameter.String, appRefNum));

        IasUserApplication_.update(conn, 
            "set DELETE_IND = ?", paraSetList, 
            "where CLIENT_ID = ? and RECIPIENT_ID = ? and APP_REF_NUM = ? and DELETE_IND != 'Y' order by CREATE_DT desc limit 1", paraWhereList);
    }

    public void markLatestIasUserApplicationPendingDelete(String clientId, String recipientId, String appRefNum, HPFW_Connection conn) throws Exception {
        logger.debug("markLatestIasUserApplicationPendingDelete - clientId: " + clientId + ", recipientId: " + recipientId + ", appRefNum: " + appRefNum);

        ArrayList<Parameter> paraSetList = new ArrayList<Parameter>();
        ArrayList<Parameter> paraWhereList = new ArrayList<Parameter>();

        paraSetList.add(new Parameter(Parameter.String, StatusConstants.DELETE_IND_PENDING));
        paraWhereList.add(new Parameter(Parameter.String, clientId));
        paraWhereList.add(new Parameter(Parameter.String, recipientId));
        paraWhereList.add(new Parameter(Parameter.String, appRefNum));

        IasUserApplication_.update(conn, 
            "set DELETE_IND = ?", paraSetList, 
            "where CLIENT_ID = ? and RECIPIENT_ID = ? and APP_REF_NUM = ? and DELETE_IND != 'Y' order by CREATE_DT desc limit 1", paraWhereList);
    }

    public void updateLatestIasUserApplication(String clientId, String recipientId, String appRefNum, String appStatus, Timestamp statusUpdateDate, HPFW_Connection conn) throws Exception {
        logger.debug("updateLatestIasUserApplication - clientId: " + clientId 
            + ", recipientId: " + recipientId + ", appRefNum: " + appRefNum + ", appStatus: " + appStatus + ", statusUpdateDate: " + statusUpdateDate);

        ArrayList<Parameter> paraSetList = new ArrayList<Parameter>();
        ArrayList<Parameter> paraWhereList = new ArrayList<Parameter>();

        paraSetList.add(new Parameter(Parameter.String, appStatus));
        paraSetList.add(new Parameter(Parameter.Timestamp, statusUpdateDate));
        paraWhereList.add(new Parameter(Parameter.String, clientId));
        paraWhereList.add(new Parameter(Parameter.String, recipientId));
        paraWhereList.add(new Parameter(Parameter.String, appRefNum));

        IasUserApplication_.update(conn, 
            "set STATUS = ?, STATUS_UPDATE_DATE = ?", paraSetList, 
            "where CLIENT_ID = ? and RECIPIENT_ID = ? and APP_REF_NUM = ? and DELETE_IND != 'Y' order by CREATE_DT desc limit 1", paraWhereList);
    }

    public List<IasUserApplication_> getIasUserApplicationList(HPFW_Connection conn, String clientId, String repcipientId, String tranId) throws Exception {
        logger.debug("getIasUserApplicationList - clientId: " + clientId + ", repcipientId: " + repcipientId + ", tranId: " + tranId);

        ArrayList<Parameter> paraL = new ArrayList<Parameter>();
        paraL.add(new Parameter(Parameter.String, clientId));
        paraL.add(new Parameter(Parameter.String, repcipientId));
        paraL.add(new Parameter(Parameter.String, tranId));

        List<IasUserApplication_> iasUserApplicationList = IasUserApplication_.getResultList(
            conn, "where CLIENT_ID = ? and RECIPIENT_ID = ? and TRAN_ID = ? order by CREATE_DT desc", paraL);

        return iasUserApplicationList;
    }

    public int getNumberOfNotDeletedIasApplicationByNotiIdAndCreateDt(HPFW_Connection conn, String notiId, Timestamp createDt) throws Exception {
        logger.debug("getNumberOfNotDeletedIasApplicationByNotiIdAndCreateDt - START");

        ResultSet rs = null;
        int cnt = 0;
        try {
            ArrayList<Parameter> paraList = new ArrayList<Parameter>() ;
            paraList.add(new Parameter(Parameter.String, notiId));
            paraList.add(new Parameter(Parameter.Timestamp, createDt));
            rs = conn.getResultSet("select count(*) CNT from ias_user_application where NOTI_ID = ? and CREATE_DT < ? and DELETE_IND != 'Y'", paraList);

            while (rs.next()) {
                cnt = Integer.parseInt(rs.getString("CNT"));
            }
            logger.debug("getNumberOfNotDeletedIasApplicationByNotiIdAndCreateDt - END");

            return cnt;
        } catch (Exception ex) {
            logger.error("General exception caught in getNumberOfNotDeletedIasApplicationByNotiIdAndCreateDt", ex);
            throw ex;
        } finally {
            try {
                if (rs != null)
                    HPFW_Connection.close(rs);
            } catch (Exception ex) {
                logger.error("General exception caught in getNumberOfNotDeletedIasApplicationByNotiIdAndCreateDt - rs.close();", ex);
            }
        }
    }
}
