package hk.gov.cmc.utils.maintainmessage.param;

import java.util.List;

import org.apache.commons.text.StringEscapeUtils;

import hk.gov.cmc.config.CmcSystemParam;
import hk.gov.cmc.model.maintainmessage.param.MessageParam;

public class ParamUtils {
    public static void updateStaticSystemParam(List<MessageParam> paramList) throws Exception {
        for (MessageParam tempBean : paramList) {
            String tempStaticParaEn = tempBean.getStaticParaEn();
            String tempStaticParaTc = tempBean.getStaticParaTc();
            String tempStaticParaSc = tempBean.getStaticParaSc();
            String tempPara = "";

            if (tempStaticParaEn != null && !"".equals(tempStaticParaEn)) {
                tempPara = CmcSystemParam.getPara(tempStaticParaEn);
                tempBean.setStaticParaEn(StringEscapeUtils.unescapeHtml4(tempPara));
            }

            if (tempStaticParaTc != null && !"".equals(tempStaticParaTc)) {
                tempPara = CmcSystemParam.getPara(tempStaticParaTc);
                tempBean.setStaticParaTc(StringEscapeUtils.unescapeHtml4(tempPara));
            }

            if (tempStaticParaSc != null && !"".equals(tempStaticParaSc)) {
                tempPara = CmcSystemParam.getPara(tempStaticParaSc);
                tempBean.setStaticParaSc(StringEscapeUtils.unescapeHtml4(tempPara));
            }
        }
    }
}
