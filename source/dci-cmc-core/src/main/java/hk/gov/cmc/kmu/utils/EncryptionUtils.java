
package hk.gov.cmc.kmu.utils;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
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
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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

}
