package sch.frog.kit.core.parse.common;

public class TokenRuleUtil {

    public static String matchIdentifier(String expression, int start){
        if(!isLetter(expression.charAt(start))){ return null; }
        StringBuilder identifier = new StringBuilder();
        for(int i = start, len = expression.length(); i < len; i++){
            char ch = expression.charAt(i);
            if(!isLetter(ch) && !isDigit(ch) && ch != '_'){
                break;
            }
            identifier.append(ch);
        }
        return identifier.toString();
    }

    public static String matchString(String str, int i, char edge){
        boolean match = false;
        char ch = str.charAt(i);
        if (ch != edge) { return null; }
        StringBuilder result = new StringBuilder();
        result.append(edge);
        i++;
        int len = str.length();
        int escape = 0;
        while (i < len) {
            ch = str.charAt(i);
            if (ch == edge && (escape & 1) == 0) {
                match = true;
                result.append(edge);
                break;
            }else{
                result.append(ch);
                i++;
            }
            if(ch == '\\'){
                escape++;
            }else{
                escape = 0;
            }
        }
        if(match){
            return result.toString();
        }else{
            return null;
        }
    }

    public static String matchNumber(int start, String str){
        StringBuilder number = new StringBuilder();
        int i = start;
        int len = str.length();
        char ch = str.charAt(i);
        if(ch == '-' && i + 1 < len){  // 匹配正负号
            number.append(ch);
            i++;
        }

        ch = str.charAt(i);
        if(isNotDigit(ch)){
            return null;
        }

        number.append(ch);
        i++;
        if(ch != '0'){  // 匹配整数部分
            for(; i < len; i++){
                ch = str.charAt(i);
                if(isNotDigit(ch)){ break; }
                number.append(ch);
            }
        }else if(i < len){
            ch = str.charAt(i);
        }

        if(ch == '.' && i + 1 < len){
            i++;
            ch = str.charAt(i); // 向后预看一位
            if(isNotDigit(ch)){
                return number.toString();
            }
            number.append('.');
            for(; i < len; i++){
                ch = str.charAt(i);
                if(isNotDigit(ch)){ break; }
                number.append(ch);
            }
        }

        return number.toString();
    }

    public static String matchComment(int start, String str){
        int len = str.length();
        if(str.charAt(start) == '/' && start < len - 1){
            if(str.charAt(start + 1) == '/'){
                start += 2;
                StringBuilder sb = new StringBuilder("//");
                for(; start < len; start++){
                    char ch = str.charAt(start);
                    if(ch == '\n'){
                        break;
                    }
                    sb.append(ch);
                }
                return sb.toString();
            }else if(str.charAt(start + 1) == '*'){
                start += 2;
                StringBuilder sb = new StringBuilder("/*");
                boolean isStar = false;
                for(; start < len; start++){
                    char ch = str.charAt(start);
                    if(ch == '/' && isStar){
                        sb.append('/');
                        break;
                    }
                    isStar = ch == '*';
                    sb.append(ch);
                }
                return sb.toString();
            }
        }
        return null;
    }

    public static boolean isLetter(char ch){
        return (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z');
    }

    public static boolean isDigit(char ch){
        return ch >= '0' && ch <= '9';
    }

    public static boolean isNotDigit(char ch) {
        return ch < '0' || ch > '9';
    }

    public static boolean isWhitespace(char ch) {
        return ch == ' ' || ch == '\t' || ch == '\r' || ch == '\n';
    }

    public static boolean isIdentifier(String str){
        if(!isLetter(str.charAt(0))){ return false; }
        for(int i = 1, len = str.length(); i < len; i++){
            char c = str.charAt(i);
            if(!isLetter(c) && !isDigit(c)){
                return false;
            }
        }
        return true;
    }
}
