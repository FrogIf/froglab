package sch.frog.kit.util;

public class NumberUtil {

    /**
     * judge string is integer
     * @return
     */
    public static boolean isInteger(String str){
        int len = str.length();
        if(len == 0){ return false; }
        for(int i = 0; i < len; i++){
            char c = str.charAt(i);
            if(c < '0' || c > '9'){
                return false;
            }
        }
        return true;
    }
}
