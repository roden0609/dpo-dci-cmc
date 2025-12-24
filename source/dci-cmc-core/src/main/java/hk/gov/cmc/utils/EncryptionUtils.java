
package hk.gov.cmc.utils;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.LinkedList;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
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

import hk.gov.cmc.persistence.connection.hpfw.HPFW_Connection;
import hk.gov.cmc.persistence.connection.hpfw.Parameter;
import hk.gov.gcis.rm.common.utils.encoder.BASE64Coder;

public class EncryptionUtils {

    public static final Log log = LogFactory.getLog(EncryptionUtils.class);

    public static String DEFAULT_TRANSFORMATION = "RSA/ECB/Pkcs1Padding";
    public static String DEFAULT_PROVIDER = "BC";

    public static String asymetricEncryptToBase64Encode(Certificate certificate, String data)
            throws NoSuchAlgorithmException, NoSuchPaddingException, NoSuchProviderException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        return asymetricEncryptToBase64Encode(certificate, data, null);
    }

    public static String asymetricEncryptToBase64Encode(Certificate certificate, String data, String provider)
            throws NoSuchAlgorithmException, NoSuchPaddingException, NoSuchProviderException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        Cipher cipher = null;
        if (provider != null) {
            cipher = Cipher.getInstance(DEFAULT_TRANSFORMATION, provider);
        } else {
            cipher = Cipher.getInstance(DEFAULT_TRANSFORMATION);
        }
        cipher.init(Cipher.ENCRYPT_MODE, certificate);
        byte[] cipherData = cipher.doFinal(data.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(cipherData);
    }

    public static String asymetricDecryptFromBase64Encode(PrivateKey privateKey, String data)
            throws NoSuchAlgorithmException, NoSuchPaddingException, NoSuchProviderException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        return asymetricDecryptFromBase64Encode(privateKey, data, null);
    }

    public static String asymetricDecryptFromBase64Encode(PrivateKey privateKey, String data, String provider)
            throws NoSuchAlgorithmException, NoSuchPaddingException, NoSuchProviderException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        byte[] b = Base64.getDecoder().decode(data);
        Cipher cipher = null;
        if (provider != null) {
            cipher = Cipher.getInstance(DEFAULT_TRANSFORMATION, provider);
        } else {
            cipher = Cipher.getInstance(DEFAULT_TRANSFORMATION);
        }

        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] cipherData = cipher.doFinal(b);
        return new String(cipherData, "UTF-8");
    }

    public static Certificate readCertFromFile(String certFileName) throws FileNotFoundException, CertificateException {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(certFileName);
            Certificate cert = CertificateFactory.getInstance("X.509").generateCertificate(fis);
            return cert;
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (Exception e) {

                }
            }
        }
    }

    public static Certificate readCertFromBase64EncodedString(String certString) throws Exception {
        InputStream Is = null;
        try {
            byte[] bytes = Base64.getDecoder().decode(certString);
            Is = new ByteArrayInputStream(bytes);
            Certificate cert = CertificateFactory.getInstance("X.509").generateCertificate(Is);
            return cert;
        } finally {
            if (Is != null) {
                try {
                    Is.close();
                } catch (Exception e) {

                }
            }
        }
    }

    public static PrivateKey readKeyFromFile(String privKeyFile) throws Exception {
        DataInputStream dis = new DataInputStream(new FileInputStream(privKeyFile));
        byte[] privKeyBytes = Files.readAllBytes(Paths.get(privKeyFile));
        dis.read(privKeyBytes);
        dis.close();

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        PKCS8EncodedKeySpec privSpec = new PKCS8EncodedKeySpec(privKeyBytes);
        RSAPrivateKey privKey = (RSAPrivateKey) keyFactory.generatePrivate(privSpec);
        return privKey;
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

        if (!keyExist)
            log.warn("No private key has been found for issuer name [" + issuerName + "], serial no ["
                    + serialNo.toString() + "] in KMU.");

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

}
