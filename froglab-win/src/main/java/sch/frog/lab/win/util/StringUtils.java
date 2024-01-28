package sch.frog.lab.win.util;

public class StringUtils {

    public static boolean isNotBlank(String str){
        return str != null && !str.isBlank();
    }

    public static boolean isBlank(String str){
        return str == null || str.isBlank();
    }

}
