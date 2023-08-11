package sch.frog.kit.lang.util;

public class StringUtils {

    public static String trimLeft(String origin, int end){
        int i = 0;
        for(int len = origin.length(); i < len && i < end; i++){
            char ch = origin.charAt(i);
            if(!isWhitespace(ch)){
                break;
            }
        }
        return origin.substring(i);
    }


    public static boolean isWhitespace(char ch) {
        return ch == ' ' || ch == '\t' || ch == '\r' || ch == '\n';
    }


}
