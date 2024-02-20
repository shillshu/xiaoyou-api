package cn.sibetech.core.util;

public class StringUtil {
    public static boolean isNumber(String str) {
        String reg = "^[0-9]+(.[0-9]+)?$";
        return str.matches(reg);
    }
}
