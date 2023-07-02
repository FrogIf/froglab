package sch.frog.kit.core.util;

public class NumberUtil {

    /**
     * 判断是否是数字
     */
    public static boolean isNumber(String origin){
        return true;
    }

    /**
     * 判断是否是小数
     */
    public static boolean isDecimal(String origin){
        if(!isNumber(origin)){ return false; }
        return origin.contains(".");
    }

    /**
     * 判断是否是整数
     */
    public static boolean isInteger(String origin){
        if(!isNumber(origin)){ return false; }
        return !isDecimal(origin);
    }

}
