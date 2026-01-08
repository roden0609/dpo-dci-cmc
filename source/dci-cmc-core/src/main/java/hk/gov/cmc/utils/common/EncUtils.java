package hk.gov.cmc.utils.common;

import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Properties;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.security.utils.EncryptionConstants;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import hk.gov.cmc.common.CmcAppConstants;
import hk.gov.cmc.common.CmcAppPropertyNames;
import hk.gov.cmc.config.CmcEnvProperties;
import hk.gov.cmc.kmu.client.KMUUtils;
import hk.gov.cmc.persistence.connection.hpfw.HPFW_Connection;
import hk.gov.cmc.persistence.connection.hpfw.Parameter;
import hk.gov.gcis.rm.common.utils.encoder.BASE64Coder;

public class EncUtils {

    private static final String DEFAULT_TRANSFORMATION = "AES/GCM/NoPadding";
    private static final String PWD_USAGE_TYPE_CMC_MESSAGE_ENCRYPTION_KEY = "CMC_MESSAGE_ENCRYPT_KEY";
    private static int ivDefaultLength = 12;
    private static HashMap<String, byte[]> m_encKeyHashmap = new HashMap<>();
    private XPathFactory factory = XPathFactory.newInstance();
    private XPathExpression encryptedKeyX509PathExpression;

    private final static CmcEnvProperties cmcEnvProperties = new CmcEnvProperties();
    private final static Log logger = LogFactory.getLog(EncUtils.class);

    public static String encrypt(String inStr) throws Exception {
        Properties properties = cmcEnvProperties.getProperties();
        String encKeyStoreId = properties
                .getProperty(CmcAppPropertyNames.CMC_MESSAGE_ENCRYPT_KEY_STORE_ID_PROPERTY_NAME);

        byte[] encKey = m_encKeyHashmap.get(encKeyStoreId);
        if (encKey == null) {
            encKey = BASE64Coder.decode(
                    KMUUtils.getPasswordFromKMU(properties, encKeyStoreId, PWD_USAGE_TYPE_CMC_MESSAGE_ENCRYPTION_KEY)
                            .getBytes());
            m_encKeyHashmap.put(encKeyStoreId, encKey);
        }

        String encryptedStr = encryptToBase64(inStr, encKey);

        return encryptedStr;
    }

    public static String decryptWithEncIndCheck(String inStr, String encKeyStoreId, String encInd) throws Exception {
        String result = "";
        if (CmcAppConstants.ENC_IND_YES.equals(encInd)) {
            result = decrypt(inStr, encKeyStoreId);
        } else {
            result = inStr;
        }

        return result;
    }

    public static String decrypt(String encryptedStr, String encKeyStoreId) throws Exception {
        Properties properties = cmcEnvProperties.getProperties();
        byte[] encKey = m_encKeyHashmap.get(encKeyStoreId);
        if (encKey == null) {
            encKey = BASE64Coder.decode(
                    KMUUtils.getPasswordFromKMU(properties, encKeyStoreId, PWD_USAGE_TYPE_CMC_MESSAGE_ENCRYPTION_KEY)
                            .getBytes());
            m_encKeyHashmap.put(encKeyStoreId, encKey);
        }

        String decryptedStr = decryptFromBase64(encryptedStr, encKey);

        return decryptedStr;
    }

    public static String encrypt(String inStr, String encKeyStoreId, String usageType) throws Exception {
        Properties properties = cmcEnvProperties.getProperties();
        byte[] encKey = m_encKeyHashmap.get(encKeyStoreId);
        if (encKey == null) {
            encKey = BASE64Coder.decode(KMUUtils.getPasswordFromKMU(properties, encKeyStoreId, usageType).getBytes());
            m_encKeyHashmap.put(encKeyStoreId, encKey);
        }
        String encryptedStr = encryptToBase64(inStr, encKey);
        return encryptedStr;
    }

    public static String decrypt(String encryptedStr, String encKeyStoreId, String usageType) throws Exception {
        Properties properties = cmcEnvProperties.getProperties();
        byte[] encKey = m_encKeyHashmap.get(encKeyStoreId);
        if (encKey == null) {
            encKey = BASE64Coder.decode(KMUUtils.getPasswordFromKMU(properties, encKeyStoreId, usageType).getBytes());
            m_encKeyHashmap.put(encKeyStoreId, encKey);
        }
        String decryptedStr = decryptFromBase64(encryptedStr, encKey);
        return decryptedStr;
    }

    public static String encryptToBase64(String inTextStr, byte[] rawKey) throws Exception {
        logger.debug("EncUtils encryptToBase64 inTextStr = " + inTextStr);
        if (inTextStr == null || inTextStr.length() <= 0 || rawKey == null)
            return inTextStr;

        byte[] plainText = inTextStr.getBytes();

        SecureRandom secureRandom = new SecureRandom();

        byte[] iv = new byte[ivDefaultLength];
        secureRandom.nextBytes(iv);

        final Cipher cipher = Cipher.getInstance(DEFAULT_TRANSFORMATION);
        GCMParameterSpec parameterSpec = new GCMParameterSpec(128, iv);
        SecretKeySpec secretKey = new SecretKeySpec(rawKey, "AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);
        byte[] cipherText = cipher.doFinal(plainText);

        ByteBuffer byteBuffer = ByteBuffer.allocate(4 + iv.length + cipherText.length);
        byteBuffer.putInt(iv.length);
        byteBuffer.put(iv);
        byteBuffer.put(cipherText);
        byte[] cipherMessage = byteBuffer.array();
        String result = new String(BASE64Coder.encode(cipherMessage));
        logger.debug("EncUtils encryptToBase64 result = " + result);
        return result;
    }

    public static byte[] genIV() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] iv = new byte[ivDefaultLength];
        secureRandom.nextBytes(iv);
        return iv;
    }

    public static String decryptFromBase64(String encryptedTextStr, byte[] rawKey) throws Exception {
        if (encryptedTextStr == null || encryptedTextStr.length() <= 0 || rawKey == null)
            return null;

        // rawKey = BASE64Coder.decode(rawKey);
        byte[] cipherMessage = BASE64Coder.decode(encryptedTextStr.getBytes());
        ByteBuffer byteBuffer = ByteBuffer.wrap(cipherMessage);
        int ivLength = byteBuffer.getInt();

        if (ivLength != 12) { // check input parameter
            throw new IllegalArgumentException("invalid iv length");
        }

        byte[] iv = new byte[ivLength];
        byteBuffer.get(iv);

        byte[] cipherText = new byte[byteBuffer.remaining()];
        byteBuffer.get(cipherText);
        final Cipher cipher = Cipher.getInstance(DEFAULT_TRANSFORMATION);

        GCMParameterSpec parameterSpec = new GCMParameterSpec(128, iv);
        SecretKeySpec secretKeySpec = new SecretKeySpec(rawKey, "AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, parameterSpec);
        byte[] plainText = cipher.doFinal(cipherText);
        String result = new String(plainText, StandardCharsets.UTF_8);
        return result;

    }

    public boolean keyExistInKMU(HPFW_Connection conn, Document xmlMessage)
            throws Exception {

        // Look for EncryptedKey/KeyInfo/X509Data/X509Certificate
        XPath xpath = factory.newXPath();
        xpath.setNamespaceContext(new XmlNSContextImpl());

        if (encryptedKeyX509PathExpression == null) {
            encryptedKeyX509PathExpression = xpath
                    .compile("//xenc:EncryptedKey/ds:KeyInfo/ds:X509Data/ds:X509Certificate");
        }
        NodeList encryptingCerts = (NodeList) encryptedKeyX509PathExpression.evaluate(xmlMessage,
                XPathConstants.NODESET);

        CertificateFactory certFact = CertificateFactory.getInstance("X509");
        for (int i = 0; i < encryptingCerts.getLength(); i++) {
            String cert = encryptingCerts.item(i).getTextContent();
            // resolve cert to issuer name serial
            X509Certificate x509Cert = (X509Certificate) certFact
                    .generateCertificate(new ByteArrayInputStream(BASE64Coder.decode(cert.getBytes())));
            String issuerName = x509Cert.getIssuerX500Principal().getName();
            BigInteger serialNo = x509Cert.getSerialNumber();
            return loadKey(conn, issuerName, serialNo);
        }

        // Key not found
        return false;

    }

    private boolean loadKey(HPFW_Connection conn, String issuerName, BigInteger serialNo)
            throws Exception {

        boolean keyExist = false;

        String SELECT_FIND_BY_ISSUER_SERIAL = "select friendly_alias "
                + " from key_store_index ksi where "
                + " serial_no = ? "
                + " and status = 'A'";

        ArrayList<Parameter> paraList = new ArrayList<>();
        paraList.add(new Parameter(Parameter.String, serialNo.toString()));

        ResultSet rs = conn.getResultSet(SELECT_FIND_BY_ISSUER_SERIAL, paraList);

        while (rs.next()) {
            keyExist = true;
        }

        if (!keyExist)
            logger.warn("No private key has been found for issuer name [" + issuerName + "], serial no ["
                    + serialNo.toString() + "] in KMU.");

        return keyExist;

    }

    private class XmlNSContextImpl implements NamespaceContext {

        public String getNamespaceURI(String arg0) {
            if ("ds".equals(arg0)) {
                return org.apache.xml.security.utils.Constants.SignatureSpecNS;
            } else if ("xenc".equals(arg0)) {
                return EncryptionConstants.EncryptionSpecNS;
            } else {
                return null;
            }
        }

        public String getPrefix(String arg0) {
            if (org.apache.xml.security.utils.Constants.SignatureSpecNS.equals(arg0)) {
                return "ds";
            } else if (EncryptionConstants.EncryptionSpecNS.equals(arg0)) {
                return "xenc";
            } else {
                return null;
            }
        }

        public Iterator<String> getPrefixes(String arg0) {
            LinkedList<String> ll = new LinkedList<String>();
            if (org.apache.xml.security.utils.Constants.SignatureSpecNS.equals(arg0)) {
                ll.add("ds");
                return ll.iterator();
            } else if (EncryptionConstants.EncryptionSpecNS.equals(arg0)) {
                ll.add("xenc");
                return ll.iterator();
            } else {
                return ll.iterator();
            }
        }
    }

}
