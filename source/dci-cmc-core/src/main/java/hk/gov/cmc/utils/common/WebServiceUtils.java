package hk.gov.cmc.utils.common;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

import jakarta.xml.soap.SOAPMessage;

public class WebServiceUtils {

    public static String soapMessageToString(SOAPMessage msg) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            msg.writeTo(baos);
            return baos.toString(StandardCharsets.UTF_8);
        } catch (Exception e) {
            return "[Failed to dump SOAPMessage] " + e.getMessage();
        }
    }
}
