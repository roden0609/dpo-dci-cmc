package hk.gov.cmc.dao.maintainmessage.todoitem;

import java.sql.Date;
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
import hk.gov.cmc.persistence.ias.todoitem.IasToDoItem_;
import hk.gov.cmc.persistence.ias.todoitem.IasUserToDoItem_;
import hk.gov.cmc.utils.common.EncUtils;

public class IasToDoItemDAO {

    private static Log logger = LogFactory.getLog(IasToDoItemDAO.class);

    public IasToDoItemDAO() {
    }

    public void createIasToDoItem(String portalId, String iasToDoItemId, String templateId, String templateVersion,
            String mergedIasToDoItemTitleEn, String mergedIasToDoItemTitleTc, String mergedIasToDoItemTitleSc,
            String mergedIasToDoItemDetailEn, String mergedIasToDoItemDetailTc, String mergedIasToDoItemDetailSc,
            String iasEsAppSuffixEn, String iasEsAppSuffixTc, String iasEsAppSuffixSc,
            String encKeyStoreId, HPFW_Connection conn) throws Exception {
        logger.debug("createIasToDoItem - portalId: " + portalId + ", iasToDoItemId: " + iasToDoItemId
                + ", templateId: " + templateId + ", templateVersion: " + templateVersion + ", encKeyStoreId: "
                + encKeyStoreId);
        logger.debug("createIasToDoItem mergedIasToDoItemTitleEn: " + mergedIasToDoItemTitleEn
                + ", mergedIasToDoItemTitleTc: " + mergedIasToDoItemTitleTc + ", mergedIasToDoItemTitleSc: "
                + mergedIasToDoItemTitleSc);
        logger.debug("createIasToDoItem mergedIasToDoItemDetailEn: " + mergedIasToDoItemDetailEn
                + ", mergedIasToDoItemDetailTc: " + mergedIasToDoItemDetailTc + ", mergedIasToDoItemDetailSc: "
                + mergedIasToDoItemDetailSc);
        logger.debug("createIasToDoItem iasEsAppSuffixEn: " + iasEsAppSuffixEn + ", iasEsAppSuffixTc: "
                + iasEsAppSuffixTc + ", iasEsAppSuffixSc: " + iasEsAppSuffixSc);

        IasToDoItem_ iasToDoItem = new IasToDoItem_();
        iasToDoItem.setIasToDoItemId(iasToDoItemId);
        iasToDoItem.setPortalId(portalId);
        iasToDoItem.setTemplateId(templateId);
        iasToDoItem.setTemplateVersion(templateVersion);
        iasToDoItem.setTitleEn(mergedIasToDoItemTitleEn);
        iasToDoItem.setTitleTc(mergedIasToDoItemTitleTc);
        iasToDoItem.setTitleSc(mergedIasToDoItemTitleSc);

        String encryptedMergedIasToDoItemDetailEn = EncUtils.encrypt(mergedIasToDoItemDetailEn);
        String encryptedMergedIasToDoItemDetailTc = EncUtils.encrypt(mergedIasToDoItemDetailTc);
        String encryptedMergedIasToDoItemDetailSc = EncUtils.encrypt(mergedIasToDoItemDetailSc);
        iasToDoItem.setEncInd(CmcAppConstants.ENC_IND_YES);
        iasToDoItem.setEncKeyStoreId(encKeyStoreId);

        iasToDoItem.setDetailEn(encryptedMergedIasToDoItemDetailEn);
        iasToDoItem.setDetailTc(encryptedMergedIasToDoItemDetailTc);
        iasToDoItem.setDetailSc(encryptedMergedIasToDoItemDetailSc);
        iasToDoItem.setIasEsAppSuffixEn(iasEsAppSuffixEn);
        iasToDoItem.setIasEsAppSuffixTc(iasEsAppSuffixTc);
        iasToDoItem.setIasEsAppSuffixSc(iasEsAppSuffixSc);

        conn.begin(null, conn.getLastUpTime(), HPFW_Connection.DIRECT);
        iasToDoItem.insert(conn);
        conn.begin(null, conn.getLastUpTime(), HPFW_Connection.DIRECT_WITH_HISTORY);
    }

    public boolean createIasUserToDoItemByOpenId(
            HPFW_Connection conn, String serviceProviderId,
            String clientId, String openId, String iasToDoItemId,
            String tranId, Date itemDate,
            String operationType, String iasUserNotiId) throws Exception {
        logger.debug("createIasUserToDoItemByOpenId - serviceProviderId: " + serviceProviderId
                + ", clientId: " + clientId + ", openId: " + openId + ", iasToDoItemId: " + iasToDoItemId
                + ", tranId: " + tranId + ", itemDate: " + itemDate
                + ", operationType: " + operationType + ", iasUserNotiId: " + iasUserNotiId);

        IasUserToDoItem_ iasUserToDoItem = new IasUserToDoItem_();
        iasUserToDoItem.setClientId(clientId);
        iasUserToDoItem.setRecipientId(openId);
        iasUserToDoItem.setIasToDoItemId(iasToDoItemId);
        iasUserToDoItem.setRecipientIdType(RecipientIDTypeConstant.OPEN_ID);
        iasUserToDoItem.setTranId(tranId);
        iasUserToDoItem.setItemDate(itemDate);
        iasUserToDoItem.setOperationType(operationType);
        iasUserToDoItem.setCompleteInd(StatusConstants.COMPLETE_IND_IN_COMPLETED);
        iasUserToDoItem.setReadInd(CmcAppConstants.READ_IND_UNREAD);
        iasUserToDoItem.setDeleteInd(StatusConstants.DELETE_IND_NOT_DELETED);
        iasUserToDoItem.setIasDeleteNotiStatus(IntegrationConstants.IAS_NOTI_STATUS_NEW);
        iasUserToDoItem.setIasNotiStatus(IntegrationConstants.IAS_NOTI_STATUS_NEW);
        iasUserToDoItem.setNotiId(iasUserNotiId);
        iasUserToDoItem.insert(conn);

        return true;
    }

    public void createIasUserToDoItemByHKID(
            HPFW_Connection conn, String serviceProviderId,
            String clientId, String hkidHashed, String iasToDoItemId,
            String tranId, Date itemDate,
            String operationType, String iasUserNotiId, String hkidEncrypted) throws Exception {
        logger.debug("createIasUserToDoItemByHKID - serviceProviderId: " + serviceProviderId
                + ", clientId: " + clientId + ", hkidHashed: " + hkidHashed + ", hkidEncrypted: " + hkidEncrypted
                + ", iasToDoItemId: " + iasToDoItemId
                + ", tranId: " + tranId + ", itemDate: " + itemDate
                + ", operationType: " + operationType + ", iasUserNotiId: " + iasUserNotiId);

        IasUserToDoItem_ iasUserToDoItem = new IasUserToDoItem_();
        iasUserToDoItem.setClientId(clientId);
        iasUserToDoItem.setRecipientId(hkidHashed);
        iasUserToDoItem.setHkidEncrypted(hkidEncrypted);
        iasUserToDoItem.setIasToDoItemId(iasToDoItemId);
        iasUserToDoItem.setRecipientIdType(RecipientIDTypeConstant.HKID);
        iasUserToDoItem.setTranId(tranId);
        iasUserToDoItem.setItemDate(itemDate);
        iasUserToDoItem.setOperationType(operationType);
        iasUserToDoItem.setCompleteInd(StatusConstants.COMPLETE_IND_IN_COMPLETED);
        iasUserToDoItem.setReadInd(CmcAppConstants.READ_IND_UNREAD);
        iasUserToDoItem.setDeleteInd(StatusConstants.DELETE_IND_NOT_DELETED);
        iasUserToDoItem.setIasDeleteNotiStatus(IntegrationConstants.IAS_NOTI_STATUS_NEW);
        iasUserToDoItem.setIasNotiStatus(IntegrationConstants.IAS_NOTI_STATUS_NEW);
        iasUserToDoItem.setNotiId(iasUserNotiId);
        iasUserToDoItem.insert(conn);

    }

    public List<IasUserToDoItem_> getIasUserToDoItemList(HPFW_Connection conn, String clientId, String repcipientId,
            String tranId) throws Exception {
        logger.debug("getIasUserToDoItemList - clientId: " + clientId + ", repcipientId: " + repcipientId + ", tranId: "
                + tranId);

        ArrayList<Parameter> paraL = new ArrayList<Parameter>();
        paraL.add(new Parameter(Parameter.String, clientId));
        paraL.add(new Parameter(Parameter.String, repcipientId));
        paraL.add(new Parameter(Parameter.String, tranId));

        List<IasUserToDoItem_> iasUserToDoItemList = IasUserToDoItem_.getResultList(
                conn, "where CLIENT_ID = ? and RECIPIENT_ID = ? and TRAN_ID = ? order by CREATE_DT desc", paraL);

        return iasUserToDoItemList;
    }

    public void markDeleteIasUserToDoItemByCorrTranId(HPFW_Connection conn, String clientId, String recipientId,
            String tranId, String corrTranId) throws Exception {
        logger.debug("markDeleteIasUserToDoItem - clientId: " + clientId + ", recipientId: " + recipientId
                + ", tranId: " + tranId + ", corrTranId: " + corrTranId);

        ArrayList<Parameter> paraSetList = new ArrayList<Parameter>();
        ArrayList<Parameter> paraWhereList = new ArrayList<Parameter>();

        paraSetList.add(new Parameter(Parameter.String, StatusConstants.DELETE_IND_PENDING));
        paraSetList.add(new Parameter(Parameter.String, tranId));
        paraWhereList.add(new Parameter(Parameter.String, clientId));
        paraWhereList.add(new Parameter(Parameter.String, recipientId));
        paraWhereList.add(new Parameter(Parameter.String, corrTranId));

        IasUserToDoItem_.update(conn,
                "set DELETE_IND = ?, ACTION_TRAN_ID = ?", paraSetList,
                "where CLIENT_ID = ? and RECIPIENT_ID = ? and TRAN_ID = ?", paraWhereList);
    }

    public void markDeleteIasUserToDoItemByTranId(HPFW_Connection conn, String clientId, String recipientId,
            String tranId) throws Exception {
        logger.debug("markDeleteIasUserToDoItem - clientId: " + clientId + ", recipientId: " + recipientId
                + ", tranId: " + tranId);

        ArrayList<Parameter> paraSetList = new ArrayList<Parameter>();
        ArrayList<Parameter> paraWhereList = new ArrayList<Parameter>();

        paraSetList.add(new Parameter(Parameter.String, StatusConstants.DELETE_IND_PENDING));
        paraWhereList.add(new Parameter(Parameter.String, clientId));
        paraWhereList.add(new Parameter(Parameter.String, recipientId));
        paraWhereList.add(new Parameter(Parameter.String, tranId));

        IasUserToDoItem_.update(conn,
                "set DELETE_IND = ?", paraSetList,
                "where CLIENT_ID = ? and RECIPIENT_ID = ? and TRAN_ID = ?", paraWhereList);
    }

    public void completeIasUserToDoItem(HPFW_Connection conn, String clientId, String recipientId, String tranId,
            String corrTranId) throws Exception {
        logger.debug("completeIasUserToDoItem - clientId: " + clientId + ", recipientId: " + recipientId + ", tranId: "
                + tranId + ", corrTranId: " + corrTranId);

        ArrayList<Parameter> paraSetList = new ArrayList<Parameter>();
        ArrayList<Parameter> paraWhereList = new ArrayList<Parameter>();

        paraSetList.add(new Parameter(Parameter.String, StatusConstants.COMPLETE_IND_COMPLETED));
        paraSetList.add(new Parameter(Parameter.String, tranId));
        paraSetList.add(new Parameter(Parameter.String, StatusConstants.COMPLETE_BY_RS));
        paraSetList.add(new Parameter(Parameter.Timestamp, new Timestamp(new java.util.Date().getTime())));
        paraWhereList.add(new Parameter(Parameter.String, recipientId));
        paraWhereList.add(new Parameter(Parameter.String, corrTranId));
        paraWhereList.add(new Parameter(Parameter.String, StatusConstants.COMPLETE_IND_IN_COMPLETED)); // if it is not mark completed yet
        IasUserToDoItem_.update(conn,
                "set COMPLETE_IND = ?, ACTION_TRAN_ID = ?, COMPLETE_BY = ?, COMPLETE_DT = ?", paraSetList,
                "where RECIPIENT_ID = ? and TRAN_ID = ? and COMPLETE_IND = ? ", paraWhereList);
    }

    public int getNumberOfNotDeletedIasToDoItemByNotiIdAndCreateDt(HPFW_Connection conn, String notiId,
            Timestamp createDt) throws Exception {
        logger.debug("getNumberOfNotDeletedIasToDoItemByNotiIdAndCreateDt - START");

        ResultSet rs = null;
        int cnt = 0;
        try {
            ArrayList<Parameter> paraList = new ArrayList<Parameter>();
            paraList.add(new Parameter(Parameter.String, notiId));
            paraList.add(new Parameter(Parameter.Timestamp, createDt));
            rs = conn.getResultSet(
                    "select count(*) CNT from ias_user_to_do_item where NOTI_ID = ? and CREATE_DT < ? and DELETE_IND != 'Y'",
                    paraList);

            while (rs.next()) {
                cnt = Integer.parseInt(rs.getString("CNT"));
            }
            logger.debug("getNumberOfNotDeletedIasToDoItemByNotiIdAndCreateDt - END");

            return cnt;
        } catch (Exception ex) {
            logger.error("General exception caught in getNumberOfNotDeletedIasToDoItemByNotiIdAndCreateDt", ex);
            throw ex;
        } finally {
            try {
                if (rs != null)
                    HPFW_Connection.close(rs);
            } catch (Exception ex) {
                logger.error(
                        "General exception caught in getNumberOfNotDeletedIasToDoItemByNotiIdAndCreateDt - rs.close();",
                        ex);
            }
        }
    }
}
