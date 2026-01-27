package hk.gov.cmc.dao.maintainmessage.message;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import hk.gov.cmc.common.CmcAppConstants;
import hk.gov.cmc.common.IntegrationConstants;
import hk.gov.cmc.common.StatusConstants;
import hk.gov.cmc.persistence.connection.hpfw.HPFW_Connection;
import hk.gov.cmc.persistence.maintainmessage.message.IasMessage_;
import hk.gov.cmc.persistence.maintainmessage.message.IasUserMessage_;
import hk.gov.cmc.utils.common.EncUtils;

public class IasMessageDAO {

    private static Log logger = LogFactory.getLog(IasMessageDAO.class);

    public IasMessageDAO() {
    }

    public void createIasMessage(String portalId, String iasMsgId, String templateId, String templateVersion,
            String mergedIasMsgSubjectEn, String mergedIasMsgSubjectTc, String mergedIasMsgSubjectSc,
            String mergedIasMsgContentEn, String mergedIasMsgContentTc, String mergedIasMsgContentSc,
            String iasEsAppSuffixEn, String iasEsAppSuffixTc, String iasEsAppSuffixSc,
            String encKeyStoreId, HPFW_Connection conn) throws Exception {
        logger.debug("createIasMessage - portalId: " + portalId + ", iasMsgId: " + iasMsgId
                + ", templateId: " + templateId + ", templateVersion: " + templateVersion + ", encKeyStoreId: "
                + encKeyStoreId);
        logger.debug("createIasMessage mergedIasMsgSubjectEn: " + mergedIasMsgSubjectEn
                + ", mergedIasMsgSubjectTc: " + mergedIasMsgSubjectTc + ", mergedIasMsgSubjectSc: "
                + mergedIasMsgSubjectSc);
        logger.debug("createIasMessage mergedIasMsgContentEn: " + mergedIasMsgContentEn
                + ", mergedIasMsgContentTc: " + mergedIasMsgContentTc + ", mergedIasMsgContentSc: "
                + mergedIasMsgContentSc);
        logger.debug("createIasMessage iasEsAppSuffixEn: " + iasEsAppSuffixEn + ", iasEsAppSuffixTc: "
                + iasEsAppSuffixTc + ", iasEsAppSuffixSc: " + iasEsAppSuffixSc);

        IasMessage_ iasMessage = new IasMessage_();
        iasMessage.setIasMsgId(iasMsgId);
        iasMessage.setPortalId(portalId);
        iasMessage.setTemplateId(templateId);
        iasMessage.setTemplateVersion(templateVersion);
        iasMessage.setSubjectEn(mergedIasMsgSubjectEn);
        iasMessage.setSubjectTc(mergedIasMsgSubjectTc);
        iasMessage.setSubjectSc(mergedIasMsgSubjectSc);

        String encryptedMergedIasMessageContentEn = EncUtils.encrypt(mergedIasMsgContentEn);
        String encryptedMergedIasMessageContentTc = EncUtils.encrypt(mergedIasMsgContentTc);
        String encryptedMergedIasMessageContentSc = EncUtils.encrypt(mergedIasMsgContentSc);
        iasMessage.setEncInd(CmcAppConstants.ENC_IND_YES);
        iasMessage.setEncKeyStoreId(encKeyStoreId);
        iasMessage.setContentEn(encryptedMergedIasMessageContentEn);
        iasMessage.setContentTc(encryptedMergedIasMessageContentTc);
        iasMessage.setContentSc(encryptedMergedIasMessageContentSc);

        iasMessage.setIasEsAppSuffixEn(iasEsAppSuffixEn);
        iasMessage.setIasEsAppSuffixTc(iasEsAppSuffixTc);
        iasMessage.setIasEsAppSuffixSc(iasEsAppSuffixSc);

        conn.begin(null, conn.getLastUpTime(), HPFW_Connection.DIRECT);
        iasMessage.insert(conn);
        conn.begin(null, conn.getLastUpTime(), HPFW_Connection.DIRECT_WITH_HISTORY);
    }

    public boolean createIasUserMessageByOpenId(
            HPFW_Connection conn, String serviceProviderId,
            String clientId, String openId, String iasMsgId,
            String tranId, String iasUserNotiId) throws Exception {
        logger.debug("createIasUserMessageByOpenId - serviceProviderId: " + serviceProviderId
                + ", clientId: " + clientId + ", openId: " + openId + ", iasMsgId: " + iasMsgId
                + ", tranId: " + tranId + ", iasUserNotiId: " + iasUserNotiId);

        IasUserMessage_ iasUserMessage = new IasUserMessage_();
        iasUserMessage.setClientId(clientId);
        iasUserMessage.setOpenId(openId);
        iasUserMessage.setIasMsgId(iasMsgId);
        iasUserMessage.setTranId(tranId);
        iasUserMessage.setReadInd(CmcAppConstants.READ_IND_UNREAD);
        iasUserMessage.setDeleteInd(StatusConstants.DELETE_IND_NOT_DELETED);
        iasUserMessage.setIasNotiStatus(IntegrationConstants.IAS_NOTI_STATUS_NEW);
        iasUserMessage.setNotiId(iasUserNotiId);
        iasUserMessage.insert(conn);

        return true;
    }

}
