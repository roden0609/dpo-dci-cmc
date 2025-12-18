package hk.gov.cmc.utils;

import static hk.gov.ogcio.egis.rm.keyservice.appserver.AppPropertyNames.PROPERTY_NAME_KEY_OPERATIONS_EJB_REMOTE_JNDI_NAME;
import static hk.gov.ogcio.egis.rm.keyservice.common.Constants.EGIS_RM_KEYSERVICE_MODULE_NAME;

import java.io.BufferedReader;
import java.io.FileReader;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Properties;

import javax.naming.InitialContext;

import org.apache.wss4j.dom.util.EncryptionUtils;

import hk.gov.gcis.rm.common.utils.PropertiesUtils;
import hk.gov.gcis.rm.keyservice.appserver.ejb.session.IKeyOperations;
import hk.gov.ogcio.egis.rm.keyservice.appserver.ejb.session.IKeyServiceManager;
import hk.gov.ogcio.egis.rm.keyservice.appserver.ejb.session.IPasswordOperations;
import hk.gov.ogcio.mars_cmc.framework.common.CommonConstants;

public class KMUUtils {
    private static final String LDAP_PASSWORD_KEY_SERVICE_ID_PROPERTY_NAME = "LDAP_PASSWORD_KEY_SERVICE_ID";
    private static final String LDAP_PASSWORD_KEY_SERVICE_USAGE_TYPE_PROPERTY_NAME = "LDAP_PASSWORD_KEY_SERVICE_USAGE_TYPE";
    private static final String DB_PASSWORD_KEY_SERVICE_ID_PROPERTY_NAME = "DB_PASSWORD_KEY_SERVICE_ID";
    private static final String DB_PASSWORD_KEY_SERVICE_USAGE_TYPE_PROPERTY_NAME = "DB_PASSWORD_KEY_SERVICE_USAGE_TYPE";
    private static final String MY_ID_DECRYPT_KEY_FRIENDLY_ALIAS_PROPERTY_NAME = "MY_ID_DECRYPT_KEY_FRIENDLY_ALIAS";

    public static String getLDAPPasswordFromKMU(Properties properties) throws Exception {
        return getPasswordFromKMU(properties, PropertiesUtils.getMandatoryProperty(properties,
                LDAP_PASSWORD_KEY_SERVICE_ID_PROPERTY_NAME),
                PropertiesUtils.getMandatoryProperty(properties,
                        LDAP_PASSWORD_KEY_SERVICE_USAGE_TYPE_PROPERTY_NAME));
    }

    public static String getDBPasswordFromKMU(Properties properties) throws Exception {
        return getPasswordFromKMU(properties, PropertiesUtils.getMandatoryProperty(properties,
                DB_PASSWORD_KEY_SERVICE_ID_PROPERTY_NAME),
                PropertiesUtils.getMandatoryProperty(properties,
                        DB_PASSWORD_KEY_SERVICE_USAGE_TYPE_PROPERTY_NAME));
    }

    public static String getPasswordFromKMU(Properties properties, String id, String usageType) throws Exception {

        String result = null;
        String pwdOpsJNDIName = properties.getProperty(PROPERTY_NAME_KEY_OPERATIONS_EJB_REMOTE_JNDI_NAME,
                CommonConstants.GLOBAL_CONTEXT_NAME + CommonConstants.CONTEXT_NAME_SEPARATOR
                        + EGIS_RM_KEYSERVICE_MODULE_NAME + CommonConstants.CONTEXT_NAME_SEPARATOR
                        + EGIS_RM_KEYSERVICE_MODULE_NAME + CommonConstants.DEFAULT_EJB_MODULE_NAME_SUFFIX
                        + CommonConstants.CONTEXT_NAME_SEPARATOR
                        + "PasswordOperations!hk.gov.ogcio.egis.rm.keyservice.appserver.ejb.session.IPasswordOperations");

        InitialContext context = new InitialContext(properties);
        IPasswordOperations pwdOps = (IPasswordOperations) context.lookup(pwdOpsJNDIName);

        char[] resultChar = pwdOps.retrieve(id, usageType);

        if (resultChar != null) {
            result = new String(resultChar);
        }
        return result;
    }

    public static boolean isKMUInitialized(Properties properties) throws Exception {

        String keyServiceJNDIName = properties.getProperty(PROPERTY_NAME_KEY_OPERATIONS_EJB_REMOTE_JNDI_NAME,
                CommonConstants.GLOBAL_CONTEXT_NAME + CommonConstants.CONTEXT_NAME_SEPARATOR
                        + EGIS_RM_KEYSERVICE_MODULE_NAME + CommonConstants.CONTEXT_NAME_SEPARATOR
                        + EGIS_RM_KEYSERVICE_MODULE_NAME + CommonConstants.DEFAULT_EJB_MODULE_NAME_SUFFIX
                        + CommonConstants.CONTEXT_NAME_SEPARATOR
                        + "KeyServiceManager!hk.gov.ogcio.egis.rm.keyservice.appserver.ejb.session.IKeyServiceManager");

        InitialContext context = new InitialContext(properties);
        IKeyServiceManager keyService = (IKeyServiceManager) context.lookup(keyServiceJNDIName);

        return keyService.isInitialized();

    }

    public static String encryptByCert(Properties properties, String rawData, String friendlyAlias) throws Exception {
        if (rawData == null)
            return null;

        String keyOpsJNDIName = properties.getProperty(PROPERTY_NAME_KEY_OPERATIONS_EJB_REMOTE_JNDI_NAME,
                CommonConstants.GLOBAL_CONTEXT_NAME + CommonConstants.CONTEXT_NAME_SEPARATOR
                        + EGIS_RM_KEYSERVICE_MODULE_NAME + CommonConstants.CONTEXT_NAME_SEPARATOR
                        + EGIS_RM_KEYSERVICE_MODULE_NAME + CommonConstants.DEFAULT_EJB_MODULE_NAME_SUFFIX
                        + CommonConstants.CONTEXT_NAME_SEPARATOR
                        + "KeyOperations!hk.gov.ogcio.egis.rm.keyservice.appserver.ejb.session.IKeyOperations");

        InitialContext context = new InitialContext(properties);
        IKeyOperations keyOps = (IKeyOperations) context.lookup(keyOpsJNDIName);
        Certificate certificate = keyOps.retrieveCert(friendlyAlias, new Date());

        return EncryptionUtils.asymetricEncryptToBase64Encode(certificate, rawData);

    }

    public static Certificate getCertFromKMU(Properties properties, String friendlyAlias) throws Exception {

        String keyOpsJNDIName = properties.getProperty(PROPERTY_NAME_KEY_OPERATIONS_EJB_REMOTE_JNDI_NAME,
                CommonConstants.GLOBAL_CONTEXT_NAME + CommonConstants.CONTEXT_NAME_SEPARATOR
                        + EGIS_RM_KEYSERVICE_MODULE_NAME + CommonConstants.CONTEXT_NAME_SEPARATOR
                        + EGIS_RM_KEYSERVICE_MODULE_NAME + CommonConstants.DEFAULT_EJB_MODULE_NAME_SUFFIX
                        + CommonConstants.CONTEXT_NAME_SEPARATOR
                        + "KeyOperations!hk.gov.ogcio.egis.rm.keyservice.appserver.ejb.session.IKeyOperations");

        InitialContext context = new InitialContext(properties);
        IKeyOperations keyOps = (IKeyOperations) context.lookup(keyOpsJNDIName);
        Certificate certificate = keyOps.retrieveCert(friendlyAlias, new Date());

        return certificate;
    }

    public static String decryptByKey(Properties properties, String encryptString, String friendlyAlias)
            throws Exception {
        if (encryptString == null)
            return null;

        String keyOpsJNDIName = properties.getProperty(PROPERTY_NAME_KEY_OPERATIONS_EJB_REMOTE_JNDI_NAME,
                CommonConstants.GLOBAL_CONTEXT_NAME + CommonConstants.CONTEXT_NAME_SEPARATOR
                        + EGIS_RM_KEYSERVICE_MODULE_NAME + CommonConstants.CONTEXT_NAME_SEPARATOR
                        + EGIS_RM_KEYSERVICE_MODULE_NAME + CommonConstants.DEFAULT_EJB_MODULE_NAME_SUFFIX
                        + CommonConstants.CONTEXT_NAME_SEPARATOR
                        + "KeyOperations!hk.gov.ogcio.egis.rm.keyservice.appserver.ejb.session.IKeyOperations");

        InitialContext context = new InitialContext(properties);
        IKeyOperations keyOps = (IKeyOperations) context.lookup(keyOpsJNDIName);

        PrivateKey privateKey = (PrivateKey) keyOps.retrieveKey(friendlyAlias, new Date());

        return EncryptionUtils.asymetricDecryptFromBase64Encode(privateKey, encryptString);
    }

    public static String decryptByKey(Properties properties, String encryptString) throws Exception {
        if (encryptString == null)
            return null;
        PrivateKey privateKey = getMyIdDecryptKey(properties);
        return EncryptionUtils.asymetricDecryptFromBase64Encode(privateKey, encryptString);
    }

    public static PrivateKey getMyIdDecryptKey(Properties properties, String friendlyAlias) throws Exception {

        InitialContext context = null;
        try {
            String keyOpsJNDIName = properties.getProperty(PROPERTY_NAME_KEY_OPERATIONS_EJB_REMOTE_JNDI_NAME,
                    CommonConstants.GLOBAL_CONTEXT_NAME + CommonConstants.CONTEXT_NAME_SEPARATOR
                            + EGIS_RM_KEYSERVICE_MODULE_NAME + CommonConstants.CONTEXT_NAME_SEPARATOR
                            + EGIS_RM_KEYSERVICE_MODULE_NAME + CommonConstants.DEFAULT_EJB_MODULE_NAME_SUFFIX
                            + CommonConstants.CONTEXT_NAME_SEPARATOR
                            + "KeyOperations!hk.gov.ogcio.egis.rm.keyservice.appserver.ejb.session.IKeyOperations");

            context = new InitialContext(properties);
            IKeyOperations keyOps = (IKeyOperations) context.lookup(keyOpsJNDIName);
            PrivateKey privateKey = (PrivateKey) keyOps.retrieveKey(friendlyAlias, new java.util.Date());
            return privateKey;
        } finally {
            try {
                if (context != null) {
                    context.close();
                }
            } catch (Exception ignore) {
            }
        }

    }

    public static PrivateKey getMyIdDecryptKey(Properties properties) throws Exception {
        String friendlyAlias = PropertiesUtils.getMandatoryProperty(properties,
                MY_ID_DECRYPT_KEY_FRIENDLY_ALIAS_PROPERTY_NAME);

        String keyOpsJNDIName = properties.getProperty(PROPERTY_NAME_KEY_OPERATIONS_EJB_REMOTE_JNDI_NAME,
                CommonConstants.GLOBAL_CONTEXT_NAME + CommonConstants.CONTEXT_NAME_SEPARATOR
                        + EGIS_RM_KEYSERVICE_MODULE_NAME + CommonConstants.CONTEXT_NAME_SEPARATOR
                        + EGIS_RM_KEYSERVICE_MODULE_NAME + CommonConstants.DEFAULT_EJB_MODULE_NAME_SUFFIX
                        + CommonConstants.CONTEXT_NAME_SEPARATOR
                        + "KeyOperations!hk.gov.ogcio.egis.rm.keyservice.appserver.ejb.session.IKeyOperations");

        InitialContext context = new InitialContext(properties);
        IKeyOperations keyOps = (IKeyOperations) context.lookup(keyOpsJNDIName);
        PrivateKey privateKey = (PrivateKey) keyOps.retrieveKey(friendlyAlias, new Date());

        return privateKey;
    }

    public static Collection<String> readMyIds(String file) throws Exception {
        ArrayList<String> myIdList = new ArrayList<String>();
        BufferedReader br = new BufferedReader(new FileReader(file));
        String myId = null;
        while ((myId = br.readLine()) != null) {
            myIdList.add(myId);
        }
        return myIdList;
    }

    public static PrivateKey getPrivateKeyByFriendlyAlias(Properties properties, String friendlyAlias)
            throws Exception {

        InitialContext context = null;
        try {

            if (properties == null)
                throw new Exception("getMyIdDecryptKey properties is NULL");

            String keyOpsJNDIName = properties.getProperty(PROPERTY_NAME_KEY_OPERATIONS_EJB_REMOTE_JNDI_NAME,
                    CommonConstants.GLOBAL_CONTEXT_NAME + CommonConstants.CONTEXT_NAME_SEPARATOR
                            + EGIS_RM_KEYSERVICE_MODULE_NAME + CommonConstants.CONTEXT_NAME_SEPARATOR
                            + EGIS_RM_KEYSERVICE_MODULE_NAME + CommonConstants.DEFAULT_EJB_MODULE_NAME_SUFFIX
                            + CommonConstants.CONTEXT_NAME_SEPARATOR
                            + "KeyOperations!hk.gov.ogcio.egis.rm.keyservice.appserver.ejb.session.IKeyOperations");

            context = new InitialContext(properties);
            IKeyOperations keyOps = (IKeyOperations) context.lookup(keyOpsJNDIName);
            PrivateKey privateKey = (PrivateKey) keyOps.retrieveKey(friendlyAlias, new java.util.Date());
            return privateKey;
        } finally {
            try {
                if (context != null) {
                    context.close();
                }
            } catch (Exception ignore) {
            }
        }

    }
}
