package sch.frog.kit.core.parse.lexical;

import sch.frog.kit.core.common.SearchMap;
import sch.frog.kit.core.exception.IncorrectExpressionException;

import java.util.ArrayList;
import java.util.List;

/**
 * 词法分析
 */
public class LexicalAnalyzer {

    private final SearchMap<TokenType> specialWord = new SearchMap<>();

    public LexicalAnalyzer() {
        init();
    }

    private void init(){
        specialWord.put("true", TokenType.BOOL);
        specialWord.put("false", TokenType.BOOL);
        specialWord.put("null", TokenType.NULL);
    }

    /*
     * uuid()
     * now()
     * add(now(), 1000)
     * date(now(), 'yyyy-MM-dd HH:mm:ss')
     * concat('a', 'b', 'c', 'd')
     * open('color'), open('json')
     */
    public List<Token> getToken(String expression) throws IncorrectExpressionException {
        int len = expression.length();
        ArrayList<Token> tokens = new ArrayList<>();
        for(int i = 0; i < len; ){
            Token token = null;
            char ch = expression.charAt(i);
            if(isWhitespace(ch)){
                i++;
                continue;
            }

            if(ch == '('){  // 匹配结构
                token = new Token("(", TokenType.STRUCT, i);
            }else if(ch == ')') { // 匹配结构
                token = new Token(")", TokenType.STRUCT, i);
            }else if(ch == ',') {
                token = new Token(",", TokenType.STRUCT, i);
            }else if(ch == '.') {
                token = new Token(".", TokenType.STRUCT, i);
            }else if(ch == '"' || ch == '\''){  // 匹配字符串
                String str = matchString(expression, i, ch);
                if(str != null){
                    token = new Token(str, TokenType.STRING, i);
                }
            }else if(ch == ':'){
                token = new Token(":", TokenType.STRUCT, i);
            }else if(ch == '['){  // 匹配array
                token = new Token("[", TokenType.STRUCT, i);
            }else if(ch == ']'){
                token = new Token("]", TokenType.STRUCT, i);
            }else if(ch == '{'){  // 匹配obj
                token = new Token("{", TokenType.STRUCT, i);
            }else if(ch == '}'){
                token = new Token("}", TokenType.STRUCT, i);
            }else {
                // 匹配数字
                String num = matchNumber(i, expression);
                if(num != null){
                    token = new Token(num, TokenType.NUMBER, i);
                }

                if(token == null){  // 匹配标识符
                    SearchMap.Entry<TokenType> specialWord = this.specialWord.match(expression, i);

                    String identifier = matchIdentifier(expression, i);

                    // 从bool匹配和函数匹配中找到最长匹配
                    if(specialWord != null && identifier != null){
                        if(identifier.length() > specialWord.getKey().length()){
                            token = new Token(identifier, TokenType.IDENTIFIER, i);
                        }else{
                            token = new Token(specialWord.getKey(), specialWord.getValue(), i);
                        }
                    }else if(specialWord != null){
                        token = new Token(specialWord.getKey(), specialWord.getValue(), i);
                    }else if(identifier != null){
                        token = new Token(identifier, TokenType.IDENTIFIER, i);
                    }
                }
            }
            if(token == null){
                throw new IncorrectExpressionException("expression parse failed for : " + expression + " at " + i);
            }
            tokens.add(token);
            i += token.literal().length();
        }
        return tokens;
    }

    private boolean isWhitespace(char ch) {
        return ch == ' ' || ch == '\t' || ch == '\r' || ch == '\n';
    }

    private String matchIdentifier(String expression, int start){
        if(!isLetter(expression.charAt(start))){ return null; }
        StringBuilder identifier = new StringBuilder();
        for(int i = start, len = expression.length(); i < len; i++){
            char ch = expression.charAt(i);
            if(!isLetter(ch) && !isDigit(ch)){
                break;
            }
            identifier.append(ch);
        }
        return identifier.toString();
    }

    private String matchString(String str, int i, char edge){
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

    private String matchNumber(int start, String str){
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

    private boolean isNotDigit(char ch) {
        return ch < '0' || ch > '9';
    }

    private boolean isDigit(char ch){
        return ch >= '0' && ch <= '9';
    }

    private boolean isLetter(char ch){
        return (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z');
    }

}
