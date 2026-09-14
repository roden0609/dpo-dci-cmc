package hk.gov.cmc.utils.soap;

import java.io.ByteArrayOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

import jakarta.xml.soap.SOAPMessage;

public class CmcSoapUtils {
    public static String soapMessageToString(SOAPMessage message) throws Exception {

        String result = "";

        if (message == null)
            return null;
        ByteArrayOutputStream bas = new ByteArrayOutputStream(4096);
        message.writeTo(bas);
        result = bas.toString("UTF-8");

        return result;
    }

    public static String documentToString(Document document) throws Exception {

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.setOutputProperty(OutputKeys.INDENT, "no");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        transformer.transform(new DOMSource(document), new StreamResult(outputStream));

        return outputStream.toString("UTF-8");
    }

    public static Document toDocument(org.w3c.dom.Node node) throws Exception {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();
        document.appendChild(document.importNode(node, true));

        return document;
    }
}
