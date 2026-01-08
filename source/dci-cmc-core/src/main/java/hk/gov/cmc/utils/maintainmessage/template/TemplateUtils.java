package hk.gov.cmc.utils.maintainmessage.template;

import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import hk.gov.cmc.common.CmcAppConstants;
import hk.gov.cmc.model.maintainmessage.param.MessageParam;
import hk.gov.cmc.utils.common.XsltUtil;

public class TemplateUtils {
    private static Log logger = LogFactory.getLog(TemplateUtils.class);

    public static String getIasMergedContent(
            String templateStringEn, String templateStringTc, String templateStringSc,
            String dataEn, String dataTc, String dataSc, String lang, List<MessageParam> paraList) throws Exception {
        String result = "";

        if (lang.equals(CmcAppConstants.LANGUAGE_SC)) {

            if (templateStringSc != null && templateStringSc.length() > 0) {
                result = XsltUtil.xml2htmlwithMergedParam(templateStringSc, dataSc,
                        getIasMergedParamMap(paraList, CmcAppConstants.LANGUAGE_SC));
            } else if (templateStringTc != null && templateStringTc.length() > 0) {
                result = XsltUtil.xml2htmlwithMergedParam(templateStringTc, dataTc,
                        getIasMergedParamMap(paraList, CmcAppConstants.LANGUAGE_TC));
            } else {
                result = XsltUtil.xml2htmlwithMergedParam(templateStringEn, dataEn,
                        getIasMergedParamMap(paraList, CmcAppConstants.LANGUAGE_EN));
            }

        } else if (lang.equals(CmcAppConstants.LANGUAGE_TC)) {

            if (templateStringTc != null && templateStringTc.length() > 0) {
                result = XsltUtil.xml2htmlwithMergedParam(templateStringTc, dataTc,
                        getIasMergedParamMap(paraList, CmcAppConstants.LANGUAGE_TC));
            } else {
                result = XsltUtil.xml2htmlwithMergedParam(templateStringEn, dataEn,
                        getIasMergedParamMap(paraList, CmcAppConstants.LANGUAGE_EN));
            }

        } else {
            result = XsltUtil.xml2htmlwithMergedParam(templateStringEn, dataEn,
                    getIasMergedParamMap(paraList, CmcAppConstants.LANGUAGE_EN));
        }

        return result;
    }

    public static Map<String, String> getIasMergedParamMap(List<MessageParam> paraList, String lang) throws Exception {
        Map<String, String> mergedParamMap = new HashMap<String, String>();

        for (MessageParam tempParam : paraList) {
            if (MessageParam.MESSAGE_PARAM_TYPE_STATIC.equals(tempParam.getParaSource())) {
                if (CmcAppConstants.LANGUAGE_EN.equals(lang)) {
                    mergedParamMap.put(tempParam.getParaName(), tempParam.getStaticParaEn());
                } else if (CmcAppConstants.LANGUAGE_TC.equals(lang)) {
                    mergedParamMap.put(tempParam.getParaName(), tempParam.getStaticParaTc());
                } else {
                    mergedParamMap.put(tempParam.getParaName(), tempParam.getStaticParaSc());
                }
            }
        }

        return mergedParamMap;
    }

    public static String getNodeValueFromMetaData(String metaData, String tagName) throws Exception {
        String result = null;
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        InputSource src = new InputSource();
        src.setCharacterStream(new StringReader(metaData));

        Document doc = builder.parse(src);
        NodeList nodeList = doc.getElementsByTagName(tagName);

        if ((nodeList != null) && (nodeList.getLength() > 0)) {
            result = doc.getElementsByTagName(tagName).item(0).getTextContent();
        }

        return result;
    }
}
