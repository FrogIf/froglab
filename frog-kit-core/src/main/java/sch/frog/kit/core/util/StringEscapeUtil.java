package sch.frog.kit.core.util;

public class StringEscapeUtil {

    public static String fromString(String str){
        if(str.startsWith("'")){
            return fromStringSingleQuotation(str);
        }else if(str.startsWith("\"")){
            return fromStringDoubleQuotation(str);
        }else{
            throw new IllegalArgumentException("unrecognized str " + str);
        }
    }

    public static String fromStringSingleQuotation(String string) {
        int len = string.length();
        StringBuilder sb = new StringBuilder(len);
        for(int i = 1; i < len - 1; ){
            char ch = string.charAt(i);
            if(ch == '\\'){
                if(match(string, "\\'", i)){
                    sb.append('\'');
                    i += 2;
                }else if(match(string, "\\\\", i)){
                    sb.append('\\');
                    i += 2;
                }else if(match(string, "\\n", i) || match(string, "\\r", i)){
                    i += 2;
                }else if(match(string, "\\t", i)){
                    sb.append('\t');
                    i += 2;
                }else{
                    sb.append(ch);
                    i++;
                }
            }else{
                sb.append(ch);
                i++;
            }
        }
        return sb.toString();
    }

    public static String fromStringDoubleQuotation(String string) {
        int len = string.length();
        StringBuilder sb = new StringBuilder(len);
        for(int i = 1; i < len - 1; ){
            char ch = string.charAt(i);
            if(ch == '\\'){
                if(match(string, "\\\"", i)){
                    sb.append('"');
                    i += 2;
                }else if(match(string, "\\\\", i)){
                    sb.append('\\');
                    i += 2;
                }else if(match(string, "\\n", i) || match(string, "\\r", i)){
                    i += 2;
                }else if(match(string, "\\t", i)){
                    sb.append('\t');
                    i += 2;
                }else{
                    sb.append(ch);
                    i++;
                }
            }else{
                sb.append(ch);
                i++;
            }
        }
        return sb.toString();
    }

    private static boolean match(String str, String match, int start) {
        if (str.length() < match.length() + start) {
            return false;
        }
        for (int i = 0, len = match.length(); i < len; i++) {
            if (str.charAt(i + start) != match.charAt(i)) {
                return false;
            }
        }
        return true;
    }

}
