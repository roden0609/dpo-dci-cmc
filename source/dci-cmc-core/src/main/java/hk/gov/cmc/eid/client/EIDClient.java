package hk.gov.cmc.eid.client;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.SSLContext;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import hk.gov.cmc.eid.bean.EIDEncryptionContentBean;
import hk.gov.cmc.eid.bean.EIDResponseBean;
import hk.gov.cmc.eid.bean.EServiceOpenIdsBean;
import hk.gov.cmc.eid.bean.NotificationBean;
import hk.gov.cmc.eid.bean.TxIdBean;
import hk.gov.cmc.eid.bean.pushNotification.request.PushNotificationBean;
import hk.gov.cmc.eid.bean.switchNotificationID.request.EServiceHkidsBean;
import hk.gov.cmc.eid.common.EIDConstants;
import hk.gov.cmc.kmu.utils.KMUUtils;
import hk.gov.gcis.rm.common.utils.encoder.EncoderUtils;

public class EIDClient {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    public static final Log log = LogFactory.getLog(EIDClient.class);
    public static String clientID = "";
    public static String clientSecret = null;

    private static final String DEFAULT_TRANSFORMATION = "AES/GCM/NoPadding";

    private static int ivDefaultLength = 12;
    public static final String successCode = "D00000";

    public static final String ENCRYPT_DECRPT_ERROR_CODE_D30001 = "D30001";
    public static final String ENCRYPT_DECRPT_ERROR_CODE_D30002 = "D30002";
    public static final String ENCRYPT_DECRPT_ERROR_CODE_D30003 = "D30003";
    public static final String ENCRYPT_DECRPT_ERROR_CODE_D30004 = "D30004";
    public static final String signatureMethod = "HmacSHA256";
    private static final String HASH_ALGORITHM = "SHA-256";

    private static String symmetricEncryptionKeyAPIURL = null;

    private static String revokeSymmetricEncryptionKeyAPIURL = null;

    private static String symmetricEncryptionKey = null;
    private static Timestamp symmetricEncryptionKeyExpiryTime = null;
    private static final String INITIALIZATION_MUTEX = "EID_INIT_MUTEX";

    private static Properties runtimeProperty = null;

    private static int postCallTimeout = 30;

    private static String eIDProxyURL = null;
    private static Integer eIDProxyPort = null;

    public static String enableEID = "false";

    public static String mobileAppContextURL = null;
    public static String mobileAppContextProfile = "profile";
    public static String mobileAppContextEme = "eme";
    public static String mobileAppContextReauth = "re-auth";
    private static SSLConnectionSocketFactory sslsf = null;

    public static String encryptToBase64(String encryptedTextStr, byte[] rawKey) throws Exception {
        log.debug("encryptToBase64 encryptedTextStr = " + encryptedTextStr);
        if (encryptedTextStr == null || encryptedTextStr.length() <= 0 || rawKey == null)
            return encryptedTextStr;

        byte[] plainText = encryptedTextStr.getBytes();

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
        String result = new String(Base64.encodeBase64(cipherMessage));
        log.debug("encryptToBase64 result = " + result);
        return result;
    }

    public static byte[] genIV() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] iv = new byte[ivDefaultLength];
        secureRandom.nextBytes(iv);
        return iv;
    }

    public static String decryptFromBase64(String encryptedTextStr, byte[] rawKey)
            throws Exception {
        if (encryptedTextStr == null || encryptedTextStr.length() <= 0 || rawKey == null)
            return null;

        rawKey = Base64.decodeBase64(rawKey);
        byte[] cipherMessage = Base64.decodeBase64(encryptedTextStr.getBytes());
        ByteBuffer byteBuffer = ByteBuffer.wrap(cipherMessage);
        int ivLength = byteBuffer.getInt();

        if (ivLength != 12) {
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

    public static String issuePostCallToEID(String targetURL,
            Map<String, String> headerParms, String reqBodyJsonString, int timeoutSeconds)
            throws Exception {
        log.debug("issuePostCallToEID - targetURL: " + targetURL + ", timeoutSeconds: " + timeoutSeconds);
        CloseableHttpClient httpClient = null;
        HttpPost postMethod = null;

        String jsonResponse = null;
        try {

            httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
            postMethod = new HttpPost(targetURL);
            RequestConfig requestConfig = RequestConfig
                    .custom()
                    .setProxy(new HttpHost(eIDProxyURL, eIDProxyPort.intValue()))
                    .setConnectTimeout(1000 * timeoutSeconds).setConnectionRequestTimeout(1000 * timeoutSeconds)
                    .setSocketTimeout(1000 * timeoutSeconds)
                    .build();

            postMethod.setConfig(requestConfig);

            postMethod.addHeader("Content-type", "application/json; charset=utf-8");
            postMethod.addHeader("Accept", "application/json; charset=utf-8");

            if (headerParms != null && headerParms.size() > 0) {
                for (String key : headerParms.keySet()) {
                    log.debug("issuePostCallToEID headerParms:" + key + ":" + headerParms.get(key));
                    postMethod.addHeader(key, headerParms.get(key));
                }
            }

            if (reqBodyJsonString != null && !reqBodyJsonString.isEmpty()) {
                StringEntity entity = new StringEntity(reqBodyJsonString, ContentType.APPLICATION_JSON);
                postMethod.setEntity(entity);
            }

            CloseableHttpResponse response = null;
            try {
                response = httpClient.execute(postMethod);
            } catch (Exception e) {
                log.warn("issuePostCallToEID httpClient.execute Exception e: " + e);
                log.warn("issuePostCallToEID retry to targetURL: " + targetURL);
                response = httpClient.execute(postMethod);
            }

            InputStream inputStream = null;
            ByteArrayOutputStream outputStream = null;
            log.debug("eID response body statusCode:" + response.getStatusLine());

            try {
                int statusCode = response.getStatusLine().getStatusCode();
                if ((HttpStatus.SC_SERVICE_UNAVAILABLE == statusCode) ||
                        (HttpStatus.SC_BAD_GATEWAY == statusCode) ||
                        (HttpStatus.SC_GATEWAY_TIMEOUT == statusCode) ||
                        (HttpStatus.SC_INTERNAL_SERVER_ERROR == statusCode) ||
                        (429 == statusCode)) {
                    log.warn("Calling iAM Smart system failed. StatusCode=" + response.getStatusLine().getStatusCode());
                } else {
                    inputStream = response.getEntity().getContent();
                    outputStream = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, length);
                    }
                    jsonResponse = outputStream.toString("UTF-8");
                    log.debug("eID response body:" + jsonResponse);
                }
            } finally {
                response.close();
                if (inputStream != null)
                    inputStream.close();
                if (outputStream != null)
                    outputStream.close();
            }
        } catch (Exception e) {
            if (e.getMessage().indexOf("java.net.SocketTimeoutException") > -1) {
                log.warn("issuePostCallToEID failed:" + e.getMessage());
            } else {
                log.error(e.getMessage(), e);
                throw e;
            }
        } finally {
            postMethod.releaseConnection();
            postMethod = null;
            if (httpClient != null)
                httpClient.close();
        }
        log.debug("issuePostCallToEID end");
        return jsonResponse;
    }

    public static String genSignatureHmacSHA256(String timestamp, String nonce, String reqBody) throws Exception {
        String base64HashString = null;
        if (clientSecret == null) {
            clientSecret = KMUUtils.getPasswordFromKMU(runtimeProperty,
                    EIDConstants.EID_CLIENT_SECRET_ID_PROPERTY_NAME,
                    EIDConstants.EID_CLIENT_SECRET_USAGE_TYPE_PROPERTY_NAME);
        }

        log.debug("genSignatureHmacSHA256: clientSecret=" + clientSecret);
        if (clientSecret != null && clientID != null) {
            String message = clientID + signatureMethod + timestamp + nonce + reqBody;
            Mac sha256_HMAC = Mac.getInstance(signatureMethod);
            SecretKeySpec secretKey = new SecretKeySpec(clientSecret.getBytes(), signatureMethod);
            sha256_HMAC.init(secretKey);
            sha256_HMAC.update(message.getBytes("UTF-8"));
            base64HashString = Base64.encodeBase64String(sha256_HMAC.doFinal()).replace("\r\n", "");
            log.debug("genSignatureHmacSHA256 base64HashString before:" + base64HashString);
            base64HashString = encodeURLValue(base64HashString);
            log.debug("genSignatureHmacSHA256 base64HashString after:" + base64HashString);
        }
        return base64HashString;
    }

    public static String decryptSymmetricContentKeyFromBase64Encode(String encryptedBase64RawData) throws Exception {
        log.debug("decryptSymmetricContentKeyFromBase64Encode" + ":" + encryptedBase64RawData);

        PrivateKey privateKey = KMUUtils.getPrivateKeyByFriendlyAlias(runtimeProperty,
                runtimeProperty.getProperty(EIDConstants.EID_ENC_DEC_PRIVATE_KEY_FRIENDLY_ALIAS_PROPERTY_NAME));

        if (privateKey instanceof RSAPrivateKey) {
            log.debug("decryptSymmetricContentKeyFromBase64Encode - private key getModulus:"
                    + ((RSAPrivateKey) privateKey).getModulus());
        }

        if (encryptedBase64RawData != null && encryptedBase64RawData.length() > 0) {
            byte[] b = Base64.decodeBase64(encryptedBase64RawData);
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] cipherData = cipher.doFinal(b);

            return new String(Base64.encodeBase64(cipherData), "UTF-8");
        } else
            return null;
    }

    public static String genStateByUsingUUID() {
        return UUID.randomUUID().toString();
    }

    public static String encodeURLValue(String value) throws RuntimeException {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex.getCause());
        }
    }

    public static String decodeURLValue(String value) throws RuntimeException {
        try {
            return URLDecoder.decode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex.getCause());
        }
    }

    public static boolean doGetSymmetricEncryptionKey(String targetURL, String reqTimeStamp, String reqNonce)
            throws Exception {
        log.debug("doGetSymmetricEncryptionKey start" + ":" + targetURL + ":" + reqTimeStamp + ":" + reqNonce + ":"
                + clientID);

        boolean result = false;

        if (targetURL == null || targetURL.isEmpty() ||
                clientID == null || clientID.isEmpty() ||
                reqTimeStamp == null || reqTimeStamp.isEmpty() ||
                reqNonce == null || reqNonce.isEmpty()) {
            return result;
        }

        String reqSignature = genSignatureHmacSHA256(reqTimeStamp, reqNonce, "");

        Map<String, String> headerParms = new HashMap<String, String>();
        headerParms.put("clientID", clientID);
        headerParms.put("signatureMethod", signatureMethod);
        headerParms.put("signature", reqSignature);
        headerParms.put("timestamp", reqTimeStamp);
        headerParms.put("nonce", reqNonce);
        headerParms.put("user-agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");

        String returnResult = issuePostCallToEID(targetURL, headerParms, "", postCallTimeout);
        log.debug("doGetSymmetricEncryptionKey after issuePostCallToEID" + ":" + returnResult);
        if (returnResult != null) {
            JsonNode jsonObject = objectMapper.readTree(returnResult);
            String txnId = jsonObject.path("txID").asText();
            String responseCode = jsonObject.path("code").asText();
            String message = jsonObject.path("message").asText();
            log.debug("doGetSymmetricEncryptionKey code" + ":" + responseCode);

            String content = jsonObject.path("content").isMissingNode() ? null : jsonObject.get("content").toString();
            log.debug("doGetSymmetricEncryptionKey content" + ":" + content);

            if (successCode.equalsIgnoreCase(responseCode) && content != null && content.length() > 0) {
                JsonNode contentNode = jsonObject.path("content");
                String secretKey = contentNode.path("secretKey").asText();
                long expiresIn = contentNode.path("expiresIn").asLong();
                long issueAt = contentNode.path("issueAt").asLong();

                Timestamp newExpiryTime = new Timestamp(issueAt + expiresIn);

                log.debug("doGetSymmetricEncryptionKey secretKey" + ":" + secretKey);
                log.debug("doGetSymmetricEncryptionKey expiresIn" + ":" + newExpiryTime);

                if (secretKey != null && secretKey.length() > 0) {
                    symmetricEncryptionKeyExpiryTime = newExpiryTime;
                    log.debug("doGetSymmetricEncryptionKey symmetricEncryptionKeyExpiryTime" + ":" + newExpiryTime);
                    symmetricEncryptionKey = decryptSymmetricContentKeyFromBase64Encode(secretKey);
                    log.debug("doGetSymmetricEncryptionKey symmetricEncryptionKey end " + ":" + symmetricEncryptionKey);
                    result = true;
                }
            } else {
                log.debug("response error code" + ":" + responseCode);
                log.debug("response end error msg" + ":" + message);
            }
        }

        return result;

    }

    public static EIDResponseBean doRequestGetNotificationIDs(String targetURL, EServiceOpenIdsBean eSerivceOpenIdsBean)
            throws Exception {
        String reqTimeStamp = EIDClient.doGenCurrentTimeInMilliSecond();
        String reqNonce = EIDClient.genStateByUsingUUID();
        EIDResponseBean returnBean = doRequestGetNotificationIDs(targetURL, reqTimeStamp, reqNonce,
                eSerivceOpenIdsBean);

        if (returnBean != null) {
            log.debug("doRequestGetNotificationIDs returnBean.getCode()=" + returnBean.getCode());
            if (ENCRYPT_DECRPT_ERROR_CODE_D30001.equalsIgnoreCase(returnBean.getCode())
                    || ENCRYPT_DECRPT_ERROR_CODE_D30002.equalsIgnoreCase(returnBean.getCode())
                    || ENCRYPT_DECRPT_ERROR_CODE_D30003.equalsIgnoreCase(returnBean.getCode())
                    || ENCRYPT_DECRPT_ERROR_CODE_D30004.equalsIgnoreCase(returnBean.getCode())) {
                renewSymmetricEncryptionKey(true);

                reqTimeStamp = EIDClient.doGenCurrentTimeInMilliSecond();
                reqNonce = EIDClient.genStateByUsingUUID();
                returnBean = doRequestGetNotificationIDs(targetURL, reqTimeStamp, reqNonce, eSerivceOpenIdsBean);
            }
        }

        return returnBean;
    }

    public static EIDResponseBean doRequestSwitchNotificationIDsByHKIDs(
            String targetURL, EServiceHkidsBean eSerivceHkidssBean) throws Exception {

        String reqTimeStamp = EIDClient.doGenCurrentTimeInMilliSecond();
        String reqNonce = EIDClient.genStateByUsingUUID();
        EIDResponseBean returnBean = doRequestSwitchNotificationIDsByHKIDs(targetURL, reqTimeStamp, reqNonce,
                eSerivceHkidssBean);

        if (returnBean != null) {
            log.debug("doRequestSwitchNotificationIDsByHKIDs returnBean.getCode()=" + returnBean.getCode());
            if (ENCRYPT_DECRPT_ERROR_CODE_D30001.equalsIgnoreCase(returnBean.getCode())
                    || ENCRYPT_DECRPT_ERROR_CODE_D30002.equalsIgnoreCase(returnBean.getCode())
                    || ENCRYPT_DECRPT_ERROR_CODE_D30003.equalsIgnoreCase(returnBean.getCode())
                    || ENCRYPT_DECRPT_ERROR_CODE_D30004.equalsIgnoreCase(returnBean.getCode())) {
                renewSymmetricEncryptionKey(true);

                reqTimeStamp = EIDClient.doGenCurrentTimeInMilliSecond();
                reqNonce = EIDClient.genStateByUsingUUID();
                returnBean = doRequestSwitchNotificationIDsByHKIDs(targetURL, reqTimeStamp, reqNonce,
                        eSerivceHkidssBean);
            }
        }

        return returnBean;
    }

    private static EIDResponseBean doRequestGetNotificationIDs(
            String targetURL, String reqTimeStamp, String reqNonce, EServiceOpenIdsBean eSerivceOpenIdsBean)
            throws Exception {
        log.debug("doRequestGetNotificationIDs targetURL=" + targetURL + ", clientID=" + clientID
                + ", reqTimeStamp=" + reqTimeStamp + ", reqNonce=" + reqNonce);

        if (targetURL == null || targetURL.isEmpty() ||
                clientID == null || clientID.isEmpty() ||
                reqTimeStamp == null || reqTimeStamp.isEmpty() ||
                reqNonce == null || reqNonce.isEmpty()) {
            return null;
        }

        renewSymmetricEncryptionKey(false);

        EIDResponseBean responseBean = null;
        String reqBodyStr = objectMapper.writeValueAsString(eSerivceOpenIdsBean);

        log.debug("doRequestGetNotificationIDs reqBodyStr=" + reqBodyStr);

        Map<String, String> headerParms = new HashMap<String, String>();
        headerParms.put("clientID", clientID);
        headerParms.put("signatureMethod", signatureMethod);

        headerParms.put("timestamp", reqTimeStamp);
        headerParms.put("nonce", reqNonce);

        if (symmetricEncryptionKey != null) {
            String encryptedRequestBody = encryptToBase64(reqBodyStr,
                    Base64.decodeBase64(symmetricEncryptionKey.getBytes("UTF-8")));
            if (encryptedRequestBody != null && encryptedRequestBody.length() > 0) {
                EIDEncryptionContentBean EIDEncryptContentBean = new EIDEncryptionContentBean();
                EIDEncryptContentBean.setContent(encryptedRequestBody);
                reqBodyStr = reqBodyStr = objectMapper.writeValueAsString(EIDEncryptContentBean);
                log.debug("doRequestGetNotificationIDs encrypted reqBodyStr=" + reqBodyStr);
            }

            String reqSignature = genSignatureHmacSHA256(reqTimeStamp, reqNonce, reqBodyStr);
            headerParms.put("signature", reqSignature);

            String returnResult = issuePostCallToEID(targetURL, headerParms, reqBodyStr, postCallTimeout);

            if (returnResult != null) {
                JsonNode jsonObject = objectMapper.readTree(returnResult);
                String txnID = jsonObject.get("txID").asText();
                String responseCode = jsonObject.get("code").asText();
                String message = jsonObject.get("message").asText();

                responseBean = new EIDResponseBean();

                if (jsonObject.get("content") == null) {
                    log.debug("doRequestGetNotificationIDs content is null");
                } else {

                    JsonNode contentNode = jsonObject.get("content");
                    String content = (contentNode != null) ? contentNode.toString() : null;

                    if (content != null && content.length() > 0) {
                        content = decryptFromBase64(content, (symmetricEncryptionKey.getBytes("UTF-8")));
                        responseBean.setContent(content);
                    }
                }

                responseBean.setTxID(txnID);
                responseBean.setCode(responseCode);
                responseBean.setMessage(message);
            }
        }
        return responseBean;

    }

    private static EIDResponseBean doRequestSwitchNotificationIDsByHKIDs(
            String targetURL, String reqTimeStamp, String reqNonce, EServiceHkidsBean eServiceHkidsBean)
            throws Exception {
        log.debug("doRequestSwitchNotificationIDsByHKIDs targetURL=" + targetURL + ", clientID=" + clientID
                + ", reqTimeStamp=" + reqTimeStamp + ", reqNonce=" + reqNonce);

        if (targetURL == null || targetURL.isEmpty() ||
                clientID == null || clientID.isEmpty() ||
                reqTimeStamp == null || reqTimeStamp.isEmpty() ||
                reqNonce == null || reqNonce.isEmpty()) {
            return null;
        }

        renewSymmetricEncryptionKey(false);

        EIDResponseBean responseBean = null;
        String reqBodyStr = objectMapper.writeValueAsString(eServiceHkidsBean);

        log.debug("doRequestSwitchNotificationIDsByHKIDs reqBodyStr=" + reqBodyStr);

        Map<String, String> headerParms = new HashMap<String, String>();
        headerParms.put("clientID", clientID);
        headerParms.put("signatureMethod", signatureMethod);

        headerParms.put("timestamp", reqTimeStamp);
        headerParms.put("nonce", reqNonce);

        if (symmetricEncryptionKey != null) {
            String encryptedRequestBody = encryptToBase64(reqBodyStr,
                    Base64.decodeBase64(symmetricEncryptionKey.getBytes("UTF-8")));
            if (encryptedRequestBody != null && encryptedRequestBody.length() > 0) {
                EIDEncryptionContentBean EIDEncryptContentBean = new EIDEncryptionContentBean();
                EIDEncryptContentBean.setContent(encryptedRequestBody);
                reqBodyStr = objectMapper.writeValueAsString(EIDEncryptContentBean);
                log.debug("doRequestSwitchNotificationIDsByHKIDs encrypted reqBodyStr=" + reqBodyStr);
            }

            String reqSignature = genSignatureHmacSHA256(reqTimeStamp, reqNonce, reqBodyStr);
            headerParms.put("signature", reqSignature);

            String returnResult = issuePostCallToEID(targetURL, headerParms, reqBodyStr, postCallTimeout);

            log.debug("doRequestSwitchNotificationIDsByHKIDs returnResult=" + returnResult);
            if (returnResult != null) {
                JsonNode jsonObject = objectMapper.readTree(returnResult);
                String txnID = jsonObject.get("txID").asText();
                String responseCode = jsonObject.get("code").asText();
                String message = jsonObject.get("message").asText();

                responseBean = new EIDResponseBean();

                if (jsonObject.get("content") == null) {
                    log.debug("doRequestSwitchNotificationIDsByHKIDs content is null");
                } else {
                    JsonNode contentNode = jsonObject.get("content");
                    String content = (contentNode != null) ? contentNode.toString() : null;

                    if (content != null && content.length() > 0) {
                        content = decryptFromBase64(content, (symmetricEncryptionKey.getBytes("UTF-8")));
                        responseBean.setContent(content);
                    }
                }

                responseBean.setTxID(txnID);
                responseBean.setCode(responseCode);
                responseBean.setMessage(message);
            }
        }
        return responseBean;

    }

    public static EIDResponseBean doRequestQueryNotificationDeliveryStatus(String targetURL, TxIdBean txIdBean)
            throws Exception {
        String reqTimeStamp = EIDClient.doGenCurrentTimeInMilliSecond();
        String reqNonce = EIDClient.genStateByUsingUUID();
        EIDResponseBean returnBean = doRequestQueryNotificationDeliveryStatus(targetURL, reqTimeStamp, reqNonce,
                txIdBean);

        if (returnBean != null) {
            log.debug("doRequestQueryNotificationDeliveryStatus returnBean.getCode()=" + returnBean.getCode());
            if (ENCRYPT_DECRPT_ERROR_CODE_D30001.equalsIgnoreCase(returnBean.getCode())
                    || ENCRYPT_DECRPT_ERROR_CODE_D30002.equalsIgnoreCase(returnBean.getCode())
                    || ENCRYPT_DECRPT_ERROR_CODE_D30003.equalsIgnoreCase(returnBean.getCode())
                    || ENCRYPT_DECRPT_ERROR_CODE_D30004.equalsIgnoreCase(returnBean.getCode())) {
                log.debug("renewSymmetricEncryptionKey 2 start");
                renewSymmetricEncryptionKey(true);

                reqTimeStamp = EIDClient.doGenCurrentTimeInMilliSecond();
                reqNonce = EIDClient.genStateByUsingUUID();
                returnBean = doRequestQueryNotificationDeliveryStatus(targetURL, reqTimeStamp, reqNonce, txIdBean);
            }
        }

        return returnBean;
    }

    private static EIDResponseBean doRequestQueryNotificationDeliveryStatus(
            String targetURL, String reqTimeStamp, String reqNonce, TxIdBean txIdBean) throws Exception {
        log.debug("doRequestQueryNotificationDeliveryStatus targetURL=" + targetURL + ", clientID=" + clientID
                + ", reqTimeStamp=" + reqTimeStamp + ", reqNonce=" + reqNonce);

        if (targetURL == null || targetURL.isEmpty() ||
                clientID == null || clientID.isEmpty() ||
                reqTimeStamp == null || reqTimeStamp.isEmpty() ||
                reqNonce == null || reqNonce.isEmpty()) {
            return null;
        }

        renewSymmetricEncryptionKey(false);

        EIDResponseBean responseBean = null;
        String reqBodyStr = objectMapper.writeValueAsString(txIdBean);

        log.debug("doRequestQueryNotificationDeliveryStatus reqBodyStr=" + reqBodyStr);

        Map<String, String> headerParms = new HashMap<String, String>();
        headerParms.put("clientID", clientID);
        headerParms.put("signatureMethod", signatureMethod);

        headerParms.put("timestamp", reqTimeStamp);
        headerParms.put("nonce", reqNonce);

        if (symmetricEncryptionKey != null) {
            String encryptedRequestBody = encryptToBase64(reqBodyStr,
                    Base64.decodeBase64(symmetricEncryptionKey.getBytes("UTF-8")));
            if (encryptedRequestBody != null && encryptedRequestBody.length() > 0) {
                EIDEncryptionContentBean EIDEncryptContentBean = new EIDEncryptionContentBean();
                EIDEncryptContentBean.setContent(encryptedRequestBody);
                reqBodyStr = objectMapper.writeValueAsString(EIDEncryptContentBean);
                log.debug("doRequestQueryNotificationDeliveryStatus encrypted reqBodyStr=" + reqBodyStr);
            }

            String reqSignature = genSignatureHmacSHA256(reqTimeStamp, reqNonce, reqBodyStr);
            headerParms.put("signature", reqSignature);

            String returnResult = issuePostCallToEID(targetURL, headerParms, reqBodyStr, postCallTimeout);

            if (returnResult != null) {
                JsonNode jsonObject = objectMapper.readTree(returnResult);
                String txnID = jsonObject.get("txID").asText();
                String responseCode = jsonObject.get("code").asText();
                String message = jsonObject.get("message").asText();

                responseBean = new EIDResponseBean();

                if (jsonObject.get("content") == null) {
                    log.debug("doRequestQueryNotificationDeliveryStatus content is null");
                } else {

                    JsonNode contentNode = jsonObject.get("content");
                    String content = (contentNode != null) ? contentNode.toString() : null;

                    if (content != null && content.length() > 0) {
                        content = decryptFromBase64(content, (symmetricEncryptionKey.getBytes("UTF-8")));
                        responseBean.setContent(content);
                    }
                }

                responseBean.setTxID(txnID);
                responseBean.setCode(responseCode);
                responseBean.setMessage(message);
            }
        }
        return responseBean;

    }

    public static EIDResponseBean doRequestSendNotificationMessages(String targetURL, NotificationBean notificationBean)
            throws Exception {
        String reqTimeStamp = EIDClient.doGenCurrentTimeInMilliSecond();
        String reqNonce = EIDClient.genStateByUsingUUID();
        log.debug("doRequestSendNotificationMessages pass 2, reqTimeStamp=" + reqTimeStamp +
                ",reqNonce=" + reqNonce);
        EIDResponseBean returnBean = doRequestSendNotificationMessages(targetURL, reqTimeStamp, reqNonce,
                notificationBean);

        if (returnBean != null) {
            log.debug("doRequestSendNotificationMessages returnBean.getCode()=" + returnBean.getCode());
            if (ENCRYPT_DECRPT_ERROR_CODE_D30001.equalsIgnoreCase(returnBean.getCode())
                    || ENCRYPT_DECRPT_ERROR_CODE_D30002.equalsIgnoreCase(returnBean.getCode())
                    || ENCRYPT_DECRPT_ERROR_CODE_D30003.equalsIgnoreCase(returnBean.getCode())
                    || ENCRYPT_DECRPT_ERROR_CODE_D30004.equalsIgnoreCase(returnBean.getCode())) {
                log.debug("renewSymmetricEncryptionKey 2 start");
                renewSymmetricEncryptionKey(true);

                reqTimeStamp = EIDClient.doGenCurrentTimeInMilliSecond();
                reqNonce = EIDClient.genStateByUsingUUID();
                returnBean = doRequestSendNotificationMessages(targetURL, reqTimeStamp, reqNonce, notificationBean);
            }
        }

        return returnBean;
    }

    private static EIDResponseBean doRequestSendNotificationMessages(String targetURL, String reqTimeStamp,
            String reqNonce,
            NotificationBean notificationBean) throws Exception {

        log.debug("doRequestSendNotificationMessages targetURL=" + targetURL + ", clientID=" + clientID
                + ", reqTimeStamp=" + reqTimeStamp + ", reqNonce=" + reqNonce);
        if (targetURL == null || targetURL.isEmpty() ||
                clientID == null || clientID.isEmpty() ||
                reqTimeStamp == null || reqTimeStamp.isEmpty() ||
                reqNonce == null || reqNonce.isEmpty()) {
            return null;
        }

        renewSymmetricEncryptionKey(false);

        EIDResponseBean responseBean = null;
        String reqBodyStr = objectMapper.writeValueAsString(notificationBean);

        log.debug("doRequestSendNotificationMessages raw reqBodyStr=" + reqBodyStr);

        Map<String, String> headerParms = new HashMap<String, String>();
        headerParms.put("clientID", clientID);
        headerParms.put("signatureMethod", signatureMethod);

        headerParms.put("timestamp", reqTimeStamp);
        headerParms.put("nonce", reqNonce);

        if (symmetricEncryptionKey != null) {
            String encryptedRequestBody = encryptToBase64(reqBodyStr,
                    Base64.decodeBase64(symmetricEncryptionKey.getBytes("UTF-8")));
            if (encryptedRequestBody != null && encryptedRequestBody.length() > 0) {
                EIDEncryptionContentBean EIDEncryptContentBean = new EIDEncryptionContentBean();
                EIDEncryptContentBean.setContent(encryptedRequestBody);
                reqBodyStr = objectMapper.writeValueAsString(EIDEncryptContentBean);
                log.debug("doRequestSendNotificationMessages encrypted reqBodyStr=" + reqBodyStr);
            }

            String reqSignature = genSignatureHmacSHA256(reqTimeStamp, reqNonce, reqBodyStr);
            headerParms.put("signature", reqSignature);

            String returnResult = issuePostCallToEID(targetURL, headerParms, reqBodyStr, postCallTimeout);

            if (returnResult != null) {
                JsonNode jsonObject = objectMapper.readTree(returnResult);
                String txnID = jsonObject.get("txID").asText();
                String responseCode = jsonObject.get("code").asText();
                String message = jsonObject.get("message").asText();

                responseBean = new EIDResponseBean();

                if (jsonObject.get("content") == null) {
                    log.debug("doRequestSendNotificationMessages content is null");
                } else {

                    JsonNode contentNode = jsonObject.get("content");
                    String content = (contentNode != null) ? contentNode.toString() : null;

                    if (content != null && content.length() > 0) {
                        content = decryptFromBase64(content, (symmetricEncryptionKey.getBytes("UTF-8")));
                        responseBean.setContent(content);
                    }
                }

                responseBean.setTxID(txnID);
                responseBean.setCode(responseCode);
                responseBean.setMessage(message);
            }
        }
        return responseBean;

    }

    public static EIDResponseBean doRequestPushNotificationMessages(String targetURL,
            PushNotificationBean pushNotificationBean) throws Exception {
        String reqTimeStamp = EIDClient.doGenCurrentTimeInMilliSecond();
        String reqNonce = EIDClient.genStateByUsingUUID();
        log.debug("doRequestPushNotificationMessages pass 2, reqTimeStamp=" + reqTimeStamp + ",reqNonce=" + reqNonce);
        EIDResponseBean returnBean = doRequestPushNotificationMessages(targetURL, reqTimeStamp, reqNonce,
                pushNotificationBean);

        if (returnBean != null) {
            log.debug("doRequestPushNotificationMessages returnBean.getCode()=" + returnBean.getCode());
            if (ENCRYPT_DECRPT_ERROR_CODE_D30001.equalsIgnoreCase(returnBean.getCode()) ||
                    ENCRYPT_DECRPT_ERROR_CODE_D30002.equalsIgnoreCase(returnBean.getCode()) ||
                    ENCRYPT_DECRPT_ERROR_CODE_D30003.equalsIgnoreCase(returnBean.getCode()) ||
                    ENCRYPT_DECRPT_ERROR_CODE_D30004.equalsIgnoreCase(returnBean.getCode())) {
                log.debug("renewSymmetricEncryptionKey 2 start");
                renewSymmetricEncryptionKey(true);

                reqTimeStamp = EIDClient.doGenCurrentTimeInMilliSecond();
                reqNonce = EIDClient.genStateByUsingUUID();
                returnBean = doRequestPushNotificationMessages(targetURL, reqTimeStamp, reqNonce, pushNotificationBean);
            }
        }

        return returnBean;
    }

    private static EIDResponseBean doRequestPushNotificationMessages(
            String targetURL, String reqTimeStamp, String reqNonce, PushNotificationBean pushNotificationBean)
            throws Exception {

        log.debug("doRequestPushNotificationMessages targetURL=" + targetURL + ", clientID=" + clientID
                + ", reqTimeStamp=" + reqTimeStamp + ", reqNonce=" + reqNonce);
        if (targetURL == null || targetURL.isEmpty() ||
                clientID == null || clientID.isEmpty() ||
                reqTimeStamp == null || reqTimeStamp.isEmpty() ||
                reqNonce == null || reqNonce.isEmpty()) {
            return null;
        }

        renewSymmetricEncryptionKey(false);

        EIDResponseBean responseBean = null;
        String reqBodyStr = objectMapper.writeValueAsString(pushNotificationBean);

        log.debug("doRequestPushNotificationMessages raw reqBodyStr=" + reqBodyStr);

        Map<String, String> headerParms = new HashMap<String, String>();
        headerParms.put("clientID", clientID);
        headerParms.put("signatureMethod", signatureMethod);

        headerParms.put("timestamp", reqTimeStamp);
        headerParms.put("nonce", reqNonce);

        if (symmetricEncryptionKey != null) {
            String encryptedRequestBody = encryptToBase64(reqBodyStr,
                    Base64.decodeBase64(symmetricEncryptionKey.getBytes("UTF-8")));
            if (encryptedRequestBody != null && encryptedRequestBody.length() > 0) {
                EIDEncryptionContentBean eIDEncryptContentBean = new EIDEncryptionContentBean();
                eIDEncryptContentBean.setContent(encryptedRequestBody);
                reqBodyStr = objectMapper.writeValueAsString(eIDEncryptContentBean);
                log.debug("doRequestPushNotificationMessages encrypted reqBodyStr=" + reqBodyStr);
            }

            String reqSignature = genSignatureHmacSHA256(reqTimeStamp, reqNonce, reqBodyStr);
            headerParms.put("signature", reqSignature);

            String returnResult = issuePostCallToEID(targetURL, headerParms, reqBodyStr, postCallTimeout);
            log.debug("doRequestPushNotificationMessages returnResult=" + returnResult);

            if (returnResult != null) {
                JsonNode jsonObject = objectMapper.readTree(returnResult);
                String txnID = jsonObject.get("txID").asText();
                String responseCode = jsonObject.get("code").asText();
                String message = jsonObject.get("message").asText();

                responseBean = new EIDResponseBean();

                if (jsonObject.get("content") == null) {
                    log.debug("doRequestPushNotificationMessages content is null");
                } else {
                    JsonNode contentNode = jsonObject.get("content");
                    String content = (contentNode != null) ? contentNode.toString() : null;

                    if (content != null && content.length() > 0) {
                        content = decryptFromBase64(content, (symmetricEncryptionKey.getBytes("UTF-8")));
                        log.debug("doRequestPushNotificationMessages decrypted content=" + content);
                        responseBean.setContent(content);
                    }
                }

                responseBean.setTxID(txnID);
                responseBean.setCode(responseCode);
                responseBean.setMessage(message);
            }
        }
        return responseBean;

    }

    public static String doGenCurrentTimeInMilliSecond() {
        Date date = new Date();
        return date.getTime() + "";
    }

    public static boolean initialize(Properties inRuntimeProperty) throws Exception {
        boolean returnVal = true;

        try {
            if (inRuntimeProperty != null) {
                runtimeProperty = inRuntimeProperty;
                if (symmetricEncryptionKeyAPIURL == null || symmetricEncryptionKeyAPIURL.length() <= 0) {
                    symmetricEncryptionKeyAPIURL = runtimeProperty
                            .getProperty(EIDConstants.EID_SYM_ENC_KEY_REQUEST_URL_PROPERTY_NAME);

                    if (symmetricEncryptionKeyAPIURL == null || symmetricEncryptionKeyAPIURL.length() <= 0) {
                        throw new Exception("Please setup eID symmetric content key API URL.");
                    }
                }

                if (clientID == null || clientID.length() <= 0) {
                    clientID = runtimeProperty.getProperty(EIDConstants.EID_CLIENT_ID_PROPERTY_NAME,
                            EIDConstants.EID_CLIENT_ID_DEFAULT_VALUE).trim();
                    if (clientID == null || clientID.length() <= 0) {
                        throw new Exception("invalid eID client ID.");
                    }
                }

                if (clientSecret == null) {
                    clientSecret = KMUUtils.getPasswordFromKMU(runtimeProperty,
                            runtimeProperty.getProperty(EIDConstants.EID_CLIENT_SECRET_ID_PROPERTY_NAME),
                            runtimeProperty.getProperty(EIDConstants.EID_CLIENT_SECRET_USAGE_TYPE_PROPERTY_NAME));
                    if (clientSecret == null || clientSecret.length() <= 0) {
                        throw new Exception("Fail to retrieve eID onboard clientSecret from kmu");
                    }
                }

                if (revokeSymmetricEncryptionKeyAPIURL == null || revokeSymmetricEncryptionKeyAPIURL.length() <= 0) {
                    revokeSymmetricEncryptionKeyAPIURL = runtimeProperty
                            .getProperty(EIDConstants.EID_REVOKE_SYM_ENC_KEY_REQUEST_URL_PROPERTY_NAME);
                    if (revokeSymmetricEncryptionKeyAPIURL == null
                            || revokeSymmetricEncryptionKeyAPIURL.length() <= 0) {
                        throw new Exception("Please setup eID revoke symmetric content key API URL.");
                    }
                }

                if (eIDProxyURL == null) {
                    eIDProxyURL = runtimeProperty.getProperty(EIDConstants.EID_PROXY_SERVER_PROPERTY_NAME);
                    if (eIDProxyURL == null || eIDProxyURL.length() <= 0) {
                        throw new Exception("Fail to retrieve eID onboard eIDProxyURL");
                    }
                }

                if (eIDProxyPort == null) {
                    String eIDProxyPortString = runtimeProperty
                            .getProperty(EIDConstants.EID_PROXY_PORT_PROPERTY_NAME);

                    if (eIDProxyPortString == null || eIDProxyPortString.length() <= 0) {
                        throw new Exception("Fail to retrieve eID onboard eIDProxyPort");
                    } else {
                        eIDProxyPort = Integer.parseInt(eIDProxyPortString);
                    }
                }

                if (mobileAppContextURL == null) {
                    mobileAppContextURL = runtimeProperty
                            .getProperty(EIDConstants.EID_MOBILEAPP_CONTEXT_URL_PROPERTY_NAME);
                }

                enableEID = runtimeProperty.getProperty(EIDConstants.ENABLE_EID_PROPERTY_NAME);

                if (sslsf == null) {
                    String keystoreFilePath = runtimeProperty.getProperty(EIDConstants.EID_SSL_TRUST_STORE_PATH);
                    getSSLContext(keystoreFilePath);
                }
                if (("true".equalsIgnoreCase(enableEID)) || ("super".equalsIgnoreCase(enableEID))) {
                    log.debug("EIDUtils:before call renewSysmmetricEncryptionKey(false)");
                    renewSymmetricEncryptionKey(false);
                }
            }

        } catch (Exception e) {
            log.error("initialize exception:" + e);
            returnVal = false;

        }
        return returnVal;
    }

    public static void initForWebServiceEIDCallBack(Properties inRuntimeProperty) throws Exception {
        log.debug("initForWebServiceEIDCallBack:" + inRuntimeProperty);
        if (inRuntimeProperty != null) {
            runtimeProperty = inRuntimeProperty;

        }
    }

    protected static void renewSymmetricEncryptionKey(boolean forceUpdate) throws Exception {
        synchronized (INITIALIZATION_MUTEX) {
            log.debug("** renewSymmetricEncryptionKey - start ");
            Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
            String reqNonce = genStateByUsingUUID();
            boolean getSuccess = false;
            for (int i = 0; (i < 4 && (getSuccess == false)); i++) {
                if (symmetricEncryptionKey == null || symmetricEncryptionKeyExpiryTime == null
                        || (symmetricEncryptionKeyExpiryTime != null
                                && symmetricEncryptionKeyExpiryTime.before(currentTimestamp))
                        || forceUpdate) {
                    getSuccess = doGetSymmetricEncryptionKey(symmetricEncryptionKeyAPIURL,
                            (currentTimestamp.getTime() + ""), reqNonce);
                }
            }
            log.debug("** renewSymmetricEncryptionKey - end ");
        }
    }

    public static void renewEnableIEID() throws Exception {

        enableEID = runtimeProperty.getProperty(EIDConstants.ENABLE_EID_PROPERTY_NAME);
    }

    private static void getSSLContext(String keystoreFilePath) throws Exception {
        try {

            SSLContext sslcontext = SSLContexts.custom()
                    .loadTrustMaterial(new File(keystoreFilePath), null, new TrustSelfSignedStrategy())
                    .build();

            sslsf = new SSLConnectionSocketFactory(
                    sslcontext,
                    new String[] { "TLSv1.2" },
                    null,
                    SSLConnectionSocketFactory.getDefaultHostnameVerifier());
        } catch (Exception e) {
            log.error("getSSLContext exception:" + e);
            throw e;
        }
    }

    public static String hashBySHA256(String value) throws NoSuchAlgorithmException {
        return new String(
                EncoderUtils.base64Encode(MessageDigest.getInstance(HASH_ALGORITHM).digest(value.getBytes())));
    }

    public static String genURLStringToEID(
            String eIDApiUrl,
            String responseType, String source,
            String redirectURI,
            String scope, String sessionLanguage,
            String state) {
        String returnURL = eIDApiUrl;
        Map<String, String> parameterMap = new HashMap<String, String>();
        parameterMap.put("clientID", EIDClient.clientID);
        parameterMap.put("responseType", responseType);
        parameterMap.put("source", source);
        parameterMap.put("redirectURI", redirectURI);
        parameterMap.put("scope", scope);
        parameterMap.put("lang", sessionLanguage);

        parameterMap.put("state", state);
        parameterMap.put("brokerPage", "true");

        int idx = 0;
        for (String key : parameterMap.keySet()) {
            String value = parameterMap.get(key);

            if (value != null && (!value.isEmpty())) {
                if (0 == idx) {
                    returnURL += "?" + key + "=" + EIDClient.encodeURLValue(value);
                } else {
                    returnURL += "&" + key + "=" + EIDClient.encodeURLValue(value);
                }
            }
            idx++;
        }
        return returnURL;
    }

    public static String openIAMSmartAppUrlFormation(String type, String ticketID) {
        String openEIDAppURL = null;
        if (type == null || type.length() <= 0
                || ticketID == null || ticketID.length() <= 0
                || mobileAppContextURL == null || mobileAppContextURL.length() <= 0) {
            return null;
        }

        if (mobileAppContextURL != null && mobileAppContextURL.length() > 0) {
            if (mobileAppContextProfile.equalsIgnoreCase(type)) {
                openEIDAppURL = mobileAppContextURL + "://" + mobileAppContextProfile + "?" + "ticketID="
                        + encodeURLValue(ticketID);
            } else if (mobileAppContextEme.equalsIgnoreCase(type)) {
                openEIDAppURL = mobileAppContextURL + "://" + mobileAppContextEme + "?" + "ticketID="
                        + encodeURLValue(ticketID);
            } else if (mobileAppContextReauth.equalsIgnoreCase(type)) {
                openEIDAppURL = mobileAppContextURL + "://" + mobileAppContextReauth + "?" + "ticketID="
                        + encodeURLValue(ticketID);
            }
        }
        return openEIDAppURL;
    }

    public static void doRevokeSymmetricEncryptionKey(String targetURL, String reqTimeStamp, String reqNonce)
            throws Exception {
        log.debug("--doRevokeSymmetricEncryptionKey start");
        if (targetURL == null || targetURL.isEmpty() ||
                clientID == null || clientID.isEmpty() ||
                reqTimeStamp == null || reqTimeStamp.isEmpty() ||
                reqNonce == null || reqNonce.isEmpty()) {
            return;
        }

        String reqSignature = genSignatureHmacSHA256(reqTimeStamp, reqNonce, "");

        Map<String, String> headerParms = new HashMap<String, String>();
        headerParms.put("clientID", clientID);
        headerParms.put("signatureMethod", signatureMethod);
        headerParms.put("signature", reqSignature);
        headerParms.put("timestamp", reqTimeStamp);
        headerParms.put("nonce", reqNonce);

        String returnResult = issuePostCallToEID(targetURL, headerParms, "", postCallTimeout);
        log.debug("doRevokeSymmetricEncryptionKey returnResult" + ":" + returnResult);
        if (returnResult != null) {
            JsonNode jsonObject = objectMapper.readTree(returnResult);
            String txnId = jsonObject.path("txID").asText();
            String responseCode = jsonObject.path("code").asText();
            String message = jsonObject.path("message").asText();

            log.debug("revoke response message: " + message);
        }
        log.debug("--doRevokeSymmetricEncryptionKey end");
    }

    public static void revokeSymmetricEncryptionKey() throws Exception {
        synchronized (INITIALIZATION_MUTEX) {
            log.debug("revokeSymmetricEncryptionKey() - start");
            Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
            String reqNonce = genStateByUsingUUID();
            doRevokeSymmetricEncryptionKey(revokeSymmetricEncryptionKeyAPIURL, (currentTimestamp.getTime() + ""),
                    reqNonce);
            log.debug("revokeSymmetricEncryptionKey() - end");
        }
    }

}
