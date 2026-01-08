package hk.gov.cmc.utils.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    static public String array2String(String[] arr, boolean withSingleQuote, int from, int to) {
        if (arr == null)
            return "";
        String result = "";
        for (int i = from; i < to; i++) {
            result += withSingleQuote ? ("'" + arr[i] + "',") : arr[i] + ",";
        }
        return result.substring(0, result.length() - 1);
    }

    static public String array2String(String[] arr, boolean withSingleQuote) {
        return array2String(arr, withSingleQuote, 0, arr.length);
    }

    static public boolean checkAllUpperCase(String check) {
        return check.equals(check.toUpperCase());
    }

    static public String replace(String inString, String from, String to) {
        if (inString == null || from == null || inString.length() < 1 || from.length() < 1)
            return inString;
        String result = inString;
        Pattern p = Pattern.compile(from);
        Matcher matcher = p.matcher(result);
        result = matcher.replaceAll(to);
        return result;
    }

    static public boolean validatePhoneNum(String phoneNum) {
        boolean result = false;
        String phoneNumCheckRegExpr = "^[0-9]{4,20}$";

        // validate phone number
        if (phoneNum != null && phoneNum.matches(phoneNumCheckRegExpr)) {
            result = true;
        }

        return result;
    }

}
