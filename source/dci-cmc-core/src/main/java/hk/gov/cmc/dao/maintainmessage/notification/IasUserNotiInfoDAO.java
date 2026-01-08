package hk.gov.cmc.dao.maintainmessage.notification;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import hk.gov.cmc.common.CmcAppConstants;
import hk.gov.cmc.common.CmcAppPropertyNames;
import hk.gov.cmc.kmu.client.KMUUtils;
import hk.gov.cmc.model.maintainmessage.user.IasUser;
import hk.gov.cmc.persistence.connection.hpfw.HPFW_Connection;
import hk.gov.cmc.persistence.connection.hpfw.Parameter;
import hk.gov.cmc.utils.common.EncryptionUtils;
import hk.gov.gcis.rm.common.javaee.service.dao.DAOBase;

public class IasUserNotiInfoDAO {

    private static Log logger = LogFactory.getLog(IasUserNotiInfoDAO.class);

    public List<IasUser> getIasUserNotiInfoByHKIDHashed(HPFW_Connection conn, String spId, String hkidHashed)
            throws Exception {
        logger.debug("getIasUserNotiInfoByHKIDHashed - START");

        List<IasUser> iasUserList = new ArrayList<IasUser>();
        ResultSet rs = null;

        String sql = "select B.NOTI_ID, B.HKID_HASHED, B.HKID_ENCRYPTED, B.OPT_IN, A.STATUS "
                + "from IAS_ES_NOTI_MAP B LEFT JOIN IAS_USER_NOTI_INFO A ON (B.NOTI_ID = A.NOTI_ID) "
                + "where B.SERVICE_PROVIDER_ID = ? AND B.HKID_HASHED = ?";

        try {
            ArrayList<Parameter> paraList = new ArrayList<Parameter>();

            paraList.add(new Parameter(Parameter.String, spId));
            paraList.add(new Parameter(Parameter.String, hkidHashed));

            rs = conn.getResultSet(sql, paraList);
            IasUser iasUser = null;
            while (rs.next()) {
                iasUser = new IasUser();
                iasUser.setNotiId(rs.getString("NOTI_ID"));
                iasUser.setHkidHashed(rs.getString("HKID_HASHED"));
                iasUser.setHkidEncrypted(rs.getString("HKID_ENCRYPTED"));
                iasUser.setOptIn(rs.getString("OPT_IN"));
                iasUser.setStatus(rs.getString("STATUS"));
                iasUserList.add(iasUser);
            }

            logger.debug("getIasUserNotiInfoByHKIDHashed - END");

            return iasUserList;
        } catch (Exception ex) {
            logger.error("General exception caught in getIasUserNotiInfoByHKIDHashed", ex);
            throw ex;
        } finally {
            try {
                if (rs != null)
                    HPFW_Connection.close(rs);
            } catch (Exception ex) {
                logger.error("General exception caught in getIasUserNotiInfoByHKIDHashed - rs.close();", ex);
            }
        }
    }

    public IasUser getIasUserNotiInfoByOpenId(HPFW_Connection conn, String spId, String openId) throws Exception {
        logger.debug("getIasUserNotiInfoByOpenId - START");

        IasUser iasUser = null;
        ResultSet rs = null;

        String sql = "select B.NOTI_ID, B.OPEN_ID, B.OPT_IN, A.STATUS "
                + "from IAS_ES_NOTI_MAP B LEFT JOIN IAS_USER_NOTI_INFO A ON (B.NOTI_ID = A.NOTI_ID) "
                + "where B.SERVICE_PROVIDER_ID = ? AND B.OPEN_ID = ?";

        try {
            ArrayList<Parameter> paraList = new ArrayList<Parameter>();

            paraList.add(new Parameter(Parameter.String, spId));
            paraList.add(new Parameter(Parameter.String, openId));

            rs = conn.getResultSet(sql, paraList);

            if (rs.next()) {
                iasUser = new IasUser();
                iasUser.setNotiId(rs.getString("NOTI_ID"));
                iasUser.setOpenId(rs.getString("OPEN_ID"));
                iasUser.setOptIn(rs.getString("OPT_IN"));
                iasUser.setStatus(rs.getString("STATUS"));
            }

            logger.debug("getIasUserNotiInfoByOpenId - END");

            return iasUser;
        } catch (Exception ex) {
            logger.error("General exception caught in getIasUserNotiInfoByOpenId", ex);
            throw ex;
        } finally {
            try {
                if (rs != null)
                    HPFW_Connection.close(rs);
            } catch (Exception ex) {
                logger.error("General exception caught in getIasUserNotiInfoByOpenId - rs.close();", ex);
            }
        }
    }

    public IasUser getIasUserNotiInfoByHKID(HPFW_Connection conn, String spId, String hkid, Properties properties)
            throws Exception {
        logger.debug("getIasUserNotiInfoByHKID - START");

        List<IasUser> iasUserList = new ArrayList<IasUser>();
        IasUser iasUser = null;
        ResultSet rs = null;

        String sql = "select B.NOTI_ID, B.HKID_HASHED, B.HKID_ENCRYPTED, A.STATUS "
                + "from IAS_ES_NOTI_MAP B LEFT JOIN IAS_USER_NOTI_INFO A ON (B.NOTI_ID = A.NOTI_ID) "
                + "where B.SERVICE_PROVIDER_ID = ? AND B.HKID_HASHED = ?";

        try {
            ArrayList<Parameter> paraList = new ArrayList<Parameter>();

            paraList.add(new Parameter(Parameter.String, spId));
            paraList.add(new Parameter(Parameter.String, EncryptionUtils.hashString(hkid)));

            rs = conn.getResultSet(sql, paraList);
            IasUser tempIasUser = null;
            while (rs.next()) {
                tempIasUser = new IasUser();
                tempIasUser.setNotiId(rs.getString("NOTI_ID"));
                tempIasUser.setHkidHashed(rs.getString("HKID_HASHED"));
                tempIasUser.setHkidEncrypted(rs.getString("HKID_ENCRYPTED"));
                tempIasUser.setStatus(rs.getString("STATUS"));
                iasUserList.add(tempIasUser);
            }

            if (iasUserList.size() == 1) {
                iasUser = iasUserList.get(0);
            } else {
                // Prevent same hash value but different HKID
                for (IasUser iasUserItem : iasUserList) {
                    String hkidDecrypted = KMUUtils.decryptByKey(properties, iasUserItem.getHkidEncrypted(),
                            CmcAppPropertyNames.CMC_CORE_CERT_FILE_NAME_P12);
                    if (hkid.equals(hkidDecrypted)) {
                        iasUser = iasUserItem;
                        break;
                    }
                }
            }

            logger.debug("getIasUserNotiInfoByHKID - END");

            return iasUser;
        } catch (Exception ex) {
            logger.error("General exception caught in getIasUserNotiInfoByHKID", ex);
            throw ex;
        } finally {
            try {
                if (rs != null)
                    HPFW_Connection.close(rs);
            } catch (Exception ex) {
                logger.error("General exception caught in getIasUserNotiInfoByHKID - rs.close();", ex);
            }
        }
    }
}
