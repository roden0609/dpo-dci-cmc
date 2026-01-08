package hk.gov.cmc.utils.common;

import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.Source;
import java.io.StringReader;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

public class XsltUtil {
    public static String xml2html(String xslContent, String metaData) throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {

            Source xmlSource = new StreamSource(new StringReader(removeBom(metaData)));
            Source xsltSource = new StreamSource(new StringReader(removeBom(xslContent)));

            ErrorListener errorListener = new LoggingErrorListener();

            TransformerFactory tFactory = TransformerFactory.newInstance();
            tFactory.setErrorListener(errorListener);

            Transformer transformer = tFactory.newTransformer(xsltSource);
            transformer.setErrorListener(errorListener);

            byteArrayOutputStream = new ByteArrayOutputStream();

            transformer.transform(xmlSource, new StreamResult(byteArrayOutputStream));

            byteArrayOutputStream.flush();

            String resultStr = "";

            byte[] resultByteArray = byteArrayOutputStream.toByteArray();

            if (resultByteArray != null)
                resultStr = new String(resultByteArray);

            return resultStr;

        } catch (RuntimeException e) {
            // e.printStackTrace();
            throw e;
        } catch (Exception e) {
            // e.printStackTrace();
            throw e;
        } finally {
            try {
                if (byteArrayOutputStream != null)
                    byteArrayOutputStream.close();
            } catch (IOException e) {
            }
        }
    }

    public static String xml2htmlwithMergedParam(String xslContent, String metaData, Map<String, String> mergedParamMap)
            throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            Source xmlSource = new StreamSource(new StringReader(removeBom(metaData)));
            Source xsltSource = new StreamSource(new StringReader(removeBom(xslContent)));

            ErrorListener errorListener = new LoggingErrorListener();

            TransformerFactory tFactory = TransformerFactory.newInstance();
            tFactory.setErrorListener(errorListener);

            Transformer transformer = tFactory.newTransformer(xsltSource);
            transformer.setErrorListener(errorListener);

            byteArrayOutputStream = new ByteArrayOutputStream();

            // For merging additional parameters received
            if (mergedParamMap.size() > 0) {
                for (Map.Entry<String, String> entry : mergedParamMap.entrySet()) {
                    transformer.setParameter(entry.getKey(), entry.getValue());
                }
            }

            transformer.transform(xmlSource, new StreamResult(byteArrayOutputStream));

            byteArrayOutputStream.flush();

            String resultStr = "";

            byte[] resultByteArray = byteArrayOutputStream.toByteArray();

            if (resultByteArray != null)
                resultStr = new String(resultByteArray);

            return resultStr;

        } catch (RuntimeException e) {
            // e.printStackTrace();
            throw e;
        } catch (Exception e) {
            // e.printStackTrace();
            throw e;
        } finally {
            try {
                if (byteArrayOutputStream != null)
                    byteArrayOutputStream.close();
            } catch (IOException e) {
            }
        }
    }

    public static String replaceNbsp(String inString) {
        return StringUtils.replace(inString.toLowerCase(), "&nbsp;", "&#160;");
    }

    public static String removeBom(String in) {

        String resultStr = in;

        if (in != null && in.length() > 0) {
            Character c = in.charAt(0);
            if (c == '\ufeff') {
                resultStr = in.substring(1, in.length());
            }
        }

        return resultStr;
    }

    public static String getCDataStr(String inStr) {
        if (inStr == null)
            return "";

        if (inStr.indexOf("]]>") != -1)
            return "<![CDATA[" + inStr.replaceAll("]]>", "]]]]><![CDATA[>") + "]]>";
        else
            return "<![CDATA[" + inStr + "]]>";
    }

}
