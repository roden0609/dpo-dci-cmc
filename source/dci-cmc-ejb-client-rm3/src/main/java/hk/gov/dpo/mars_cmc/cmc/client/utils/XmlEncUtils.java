/*
 * Design, Implementation and Support of Common Middleware Components and Reference Applications
 *
 * Utils (XML Encryption)
 *
 * Developed by: Karl Li
 * Reviewed by: Keith HO
 * Tester: Ricky Chung
 */
package hk.gov.dpo.mars_cmc.cmc.client.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.StringWriter;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Properties;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
//import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

//import org.apache.ws.security.WSSecurityException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.security.encryption.EncryptedData;
import org.apache.xml.security.encryption.EncryptedKey;
import org.apache.xml.security.encryption.XMLCipher;
import org.apache.xml.security.keys.KeyInfo;
import org.apache.xml.security.keys.content.X509Data;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Contains methods for XML Encryption related operations.<br/>
 * <br/>
 * Operation mode: Online/ Batch<br/>
 * <br>
 * <h3>Amendment History:</h3>
 * <p>
 * <br/>$Log: XmlEncUtils.java,v $
 * <br/>Revision 1.0  2021/01/18 03:01:02  Karl
 * <br/>synchronize local repository with CVS
 * <br/>
 * </p>
 * <br/>
 * <br/>
 * @version $Revision: 1.0 $<br/>
 * <br/>
 * <br/>
 *
 */
public class XmlEncUtils {

  /**
   * Default XML signature namespace prefix.
   */
  public static final String MAINT_MSG_XML_SIGNATURE_NS_PREFIX = "ds";

  /**
   * Default signature property timestamp prefix.
   */
  public static final String MAINT_MSG_SIGNATURE_PROPERTY_TIMESTAMP_PREFIX = "timestamp_";

  /**
   * Default signature property timestamp reference prefix.
   */
  public static final String MAINT_MSG_SIGNATURE_PROPERTY_TIMESTAMP_REF_PREFIX = "ref_";

  /**
   * Key name separator.
   */
  public static final String KEY_NAME_SEPARATOR = "@";

  /**
   * Property name of MAINT_MSG_XML_DATA_ENCRYPTION_ALGORITHM_URI.
   */
  public static final String MAINT_MSG_XML_DATA_ENCRYPTION_ALGORITHM_URI_PROPERTY_NAME = "MAINT_MSG_XML_DATA_ENCRYPTION_ALGORITHM_URI";

  /**
   * Property name of MAINT_MSG_XML_DATA_ENCRYPTION_ALGORITHM_NAME.
   */
  public static final String MAINT_MSG_XML_DATA_ENCRYPTION_ALGORITHM_NAME_PROPERTY_NAME = "MAINT_MSG_XML_DATA_ENCRYPTION_ALGORITHM_NAME";

  /**
   * Property name of MAINT_MSG_XML_DATA_ENCRYPTION_KEY_SIZE.
   */
  public static final String MAINT_MSG_XML_DATA_ENCRYPTION_KEY_SIZE_PROPERTY_NAME = "MAINT_MSG_XML_DATA_ENCRYPTION_KEY_SIZE";

  /**
   * Property name of MAINT_MSG_XML_KEY_ENCRYPTION_ALGORITHM_URI.
   */
  public static final String MAINT_MSG_XML_KEY_ENCRYPTION_ALGORITHM_URI_PROPERTY_NAME = "MAINT_MSG_XML_KEY_ENCRYPTION_ALGORITHM_URI";

  /**
   * Property name of MAINT_MSG_XML_ENCRYPTION_CERT_FILENAME.
   */
  public static final String MAINT_MSG_XML_ENCRYPTION_CERT_FILENAME_PROPERTY_NAME = "MAINT_MSG_XML_ENCRYPTION_CERT_FILENAME";

// public data member(s) -- BEGIN
public final static Log log = LogFactory.getLog(XmlEncUtils.class);
// public data member(s) -- END

  private XPathFactory factory = XPathFactory.newInstance();
  private XPathExpression encryptedKeyX509PathExpression;

    static {
          org.apache.xml.security.Init.init();
    }

  /**
   * Encrypts the MaintainMessageRequest.
   *
   * @ejb.interface-method  view-type = "both"
   * @ejb.permission role-name="keyservice_admin,keyservice_readonly,keyservice_internal"
   *
   * @param friendlyAlias The friendly alias to a key/ certificate for the encryption
   * @param xmlMessage XML document
   * @param xPath The xPath evaluate to a node of the XML to encrypt. (single node only)
   * @return Original XML document with the given node encrypted.
   * @throws Exception
   */
  public Document encryptXML(Properties runtimeProperties, String friendlyAlias, Document xmlMessage)
		throws Exception {

		String certFileName = runtimeProperties.getProperty(MAINT_MSG_XML_ENCRYPTION_CERT_FILENAME_PROPERTY_NAME);

		String dataEncAlgoName = runtimeProperties.getProperty(MAINT_MSG_XML_DATA_ENCRYPTION_ALGORITHM_NAME_PROPERTY_NAME);
		String dataEncKeySize = runtimeProperties.getProperty(MAINT_MSG_XML_DATA_ENCRYPTION_KEY_SIZE_PROPERTY_NAME);
		String dataEncAlgoURI = runtimeProperties.getProperty(MAINT_MSG_XML_DATA_ENCRYPTION_ALGORITHM_URI_PROPERTY_NAME);
		String keyEncAlgoURI = runtimeProperties.getProperty(MAINT_MSG_XML_KEY_ENCRYPTION_ALGORITHM_URI_PROPERTY_NAME);

		//DEFAULT SET OF ALGORITHMS FOR XML ENCRYPTION IF NOT SPECIFIED.
		if (dataEncAlgoName == null || "".equals(dataEncAlgoName) || dataEncKeySize == null || "".equals(dataEncKeySize)
			|| dataEncAlgoURI == null || "".equals(dataEncAlgoURI) || keyEncAlgoURI == null || "".equals(keyEncAlgoURI)) {
			dataEncAlgoName = "aes";
			dataEncKeySize = "128";
			dataEncAlgoURI = XMLCipher.AES_128;
			keyEncAlgoURI = XMLCipher.RSA_OAEP;
		}

		try {
			//Generate a data encryption key randomly
			Date today = new Date();
			SecretKey dataEncKey = generateDataEncryptionKey(dataEncAlgoName, Integer.parseInt(dataEncKeySize));

			Certificate cert = readCertFromFile(certFileName);

			if (cert == null) {
				throw new Exception("No certificate had been found for File name:" + certFileName);
			}

			if (!"RSA".equals(cert.getPublicKey().getAlgorithm())) {
				log.warn("Unsupported algorithm for encryptXML. algorithm:" + cert.getPublicKey().getAlgorithm());
				throw new Exception("Unsupported algorithm for encryptXML. algorithm:" + cert.getPublicKey().getAlgorithm());
			}

			StringWriter sw2 = new StringWriter();
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

			transformer.transform(new DOMSource(xmlMessage), new StreamResult(sw2));

			Key keyEnc_dataEncKey = cert.getPublicKey();

			//This step encryptes the randomly generated data encryption key (dataEncKey)
			//using the given friendlyAlias
			XMLCipher keyCipher = XMLCipher.getInstance(keyEncAlgoURI);
			keyCipher.init(XMLCipher.WRAP_MODE, keyEnc_dataEncKey);
			EncryptedKey encryptedKey = keyCipher.encryptKey(xmlMessage, dataEncKey);

			//Enable this section for key info inside encrypted key
			//Put information into the encrypted key
			KeyInfo keyInfoForEncryptedKey = new KeyInfo(xmlMessage);
			//Give name to the encrypted key
			keyInfoForEncryptedKey.addKeyName("SessionKey");

			//Give cert to the encrypted key
			X509Certificate x509Cert = (X509Certificate)cert;
			X509Data x509data = new X509Data(xmlMessage);
			x509data.addCertificate(x509Cert);
			keyInfoForEncryptedKey.add(x509data);
			encryptedKey.setKeyInfo(keyInfoForEncryptedKey);

			XMLCipher xmlCipher = XMLCipher.getInstance(dataEncAlgoURI);
			xmlCipher.init(XMLCipher.ENCRYPT_MODE, dataEncKey);

			//Setting keyinfo inside the encrypted data being prepared.
			EncryptedData encryptedData = xmlCipher.getEncryptedData();
			KeyInfo keyInfo = new KeyInfo(xmlMessage);
			keyInfo.add(encryptedKey);
			keyInfo.addKeyName(friendlyAlias + KEY_NAME_SEPARATOR + today.getTime());

			encryptedData.setKeyInfo(keyInfo);

			//Retrieve the MaintainMessageRequest element that requires to be encrypted
			Element elementToEncrypt = null;
			elementToEncrypt = (Element) xmlMessage.getElementsByTagName("MaintainMessageRequest").item(0);
			xmlCipher.doFinal(xmlMessage, elementToEncrypt, true);

		} catch (Exception e) {
			throw new Exception(e.getClass().getName(), e);
		}
		return xmlMessage;
	}


	/**
	 * read certificate from file
	 * @param certFileName certificate file name
	 * @return Certificate data structure
	 * @throws FileNotFoundException
	 * @throws CertificateException
	 */
	public static Certificate readCertFromFile(String certFileName) throws Exception {
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
					throw new Exception("Read Cert failed", e);
				}
			}
		}
	}

	private SecretKey generateDataEncryptionKey(String algorithm, int keySize)
		throws NoSuchAlgorithmException {
		KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);
			if (keySize != -1) {
			  keyGenerator.init(keySize);
			} //auto
			return keyGenerator.generateKey();
		}
	}

