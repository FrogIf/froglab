package sch.frog.kit.util;

public class StringUtil {

    public static boolean isBlank(String str){
        return str == null || str.isBlank();
    }

    public static boolean isNotBlank(String str){
        return str != null && !str.isBlank();
    }
}
