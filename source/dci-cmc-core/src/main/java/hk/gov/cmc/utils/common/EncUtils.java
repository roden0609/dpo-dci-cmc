package hk.gov.cmc.utils.common;

import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

    public static boolean isKeyExistInKMU(HPFW_Connection conn, Document xmlMessage)
            throws Exception {

        if (xmlMessage == null) {
            return false;
        }

        XPath xpath = XPathFactory.newInstance().newXPath();
        xpath.setNamespaceContext(new XmlNSContextImpl());

        XPathExpression localExpr = xpath.compile(
                "//xenc:EncryptedKey/ds:KeyInfo/ds:X509Data/ds:X509Certificate");

        NodeList certNodes = (NodeList) localExpr.evaluate(
                xmlMessage, XPathConstants.NODESET);

        if (certNodes == null || certNodes.getLength() == 0) {
            return false;
        }

        CertificateFactory certFactory = CertificateFactory.getInstance("X.509");

        for (int i = 0; i < certNodes.getLength(); i++) {
            String base64Cert = certNodes.item(i).getTextContent();
            if (base64Cert == null || base64Cert.isBlank()) {
                continue;
            }

            byte[] certBytes = BASE64Coder.decode(
                    base64Cert.getBytes(StandardCharsets.UTF_8));

            try (ByteArrayInputStream bis = new ByteArrayInputStream(certBytes)) {
                X509Certificate cert = (X509Certificate) certFactory.generateCertificate(bis);

                String issuerName = cert.getIssuerX500Principal().getName();
                BigInteger serialNo = cert.getSerialNumber();

                return isKeyExistInKMU(conn, issuerName, serialNo);
            }
        }

        return false;
    }

    public static boolean isKeyExistInKMU(HPFW_Connection conn, String issuerName, BigInteger serialNo)
            throws Exception {

        boolean keyExist = false;

        String SELECT_FIND_BY_ISSUER_SERIAL = "select friendly_alias "
                + " from key_store_index ksi where "
                + " serial_no = ? "
                + " and status = 'A'";

        ArrayList<Parameter> paraList = new ArrayList<Parameter>();
        paraList.add(new Parameter(Parameter.String, serialNo.toString()));

        ResultSet rs = conn.getResultSet(SELECT_FIND_BY_ISSUER_SERIAL, paraList);

        while (rs.next()) {
            keyExist = true;
        }

        if (!keyExist) {
            logger.warn("No private key has been found for issuer name [" + issuerName + "], serial no ["
                    + serialNo.toString() + "] in KMU.");
        } else {
            logger.info("Private key has been found for issuer name [" + issuerName + "], serial no ["
                    + serialNo.toString() + "] in KMU.");
        }

        return keyExist;

    }

    private static class XmlNSContextImpl implements NamespaceContext {
        @Override
        public String getNamespaceURI(String prefix) {
            if ("ds".equals(prefix)) {
                return org.apache.xml.security.utils.Constants.SignatureSpecNS;
            } else if ("xenc".equals(prefix)) {
                return EncryptionConstants.EncryptionSpecNS;
            }
            return null;
        }

        @Override
        public String getPrefix(String namespaceURI) {
            if (org.apache.xml.security.utils.Constants.SignatureSpecNS.equals(namespaceURI)) {
                return "ds";
            } else if (EncryptionConstants.EncryptionSpecNS.equals(namespaceURI)) {
                return "xenc";
            }
            return null;
        }

        @Override
        public Iterator<String> getPrefixes(String namespaceURI) {
            LinkedList<String> list = new LinkedList<>();
            String prefix = getPrefix(namespaceURI);
            if (prefix != null) {
                list.add(prefix);
            }
            return list.iterator();
        }
    }

    public static String hashString(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashedBytes = md.digest(input.getBytes());
        String hash = bytesToHex(hashedBytes);
        return hash;
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }

}
