package sch.frog.kit.core.parse;

import sch.frog.kit.core.ISession;
import sch.frog.kit.core.exception.IncorrectExpressionException;
import sch.frog.kit.core.exception.WordDuplicateError;

import java.util.ArrayList;
import java.util.List;

public class TokenParser {

    private final WordNode<String> functionWord = new WordNode<>();

    private final WordNode<TokenType> specialWord = new WordNode<>();

    public TokenParser() {
        init();
    }

    private void init(){
        specialWord.add("true", TokenType.BOOL);
        specialWord.add("false", TokenType.BOOL);
        specialWord.add("null", TokenType.NULL);
    }

    /**
     * 注册函数
     */
    public void registerFunction(String funName){
        functionWord.add(funName, null);
    }

    /*
     * uuid()
     * now()
     * add(now(), 1000)
     * date(now(), 'yyyy-MM-dd HH:mm:ss')
     * concat('a', 'b', 'c', 'd')
     * open('color'), open('json')
     */
    public List<Token> getToken(String expression, ISession session) throws IncorrectExpressionException {
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
            }else if(ch == '"' || ch == '\''){  // 匹配字符串
                String str = matchString(expression, i, ch);
                if(str != null){
                    token = new Token(str, TokenType.STRING, i);
                }
            }else if(ch == '['){  // 匹配array
                String arr = matchArray(expression, i);
                if(arr != null){
                    token = new Token(arr, TokenType.ARRAY, i);
                }
            }else if(ch == '{'){  // 匹配obj
                String obj = matchObject(expression, i);
                if(obj != null){
                    token = new Token(obj, TokenType.OBJECT, i);
                }
            }else {
                // 匹配数字
                String num = matchNumber(i, expression);
                if(num != null){
                    token = new Token(num, TokenType.NUMBER, i);
                }

                if(token == null){
                    Word<TokenType> specialWord = this.specialWord.match(i, expression);
                    // 匹配函数
                    Word<String> funWord = functionWord.match(i, expression);

                    // 从bool匹配和函数匹配中找到最长匹配
                    if(specialWord != null && funWord != null){
                        if(specialWord.length() > funWord.length()){
                            token = new Token(specialWord.literal, specialWord.data, i);
                        }else{
                            token = new Token(funWord.literal, TokenType.FUNCTION, i);
                        }
                    }else if(specialWord != null){
                        token = new Token(specialWord.literal, specialWord.data, i);
                    }else if(funWord != null){
                        token = new Token(funWord.literal, TokenType.FUNCTION, i);
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

    private String matchArray(String str, int i){
        // TODO 解析数组
        char ch = str.charAt(i);
        if(ch != '['){
            return null;
        }
        StringBuilder result = new StringBuilder();
        result.append('[');
        i++;

        return result.toString();
    }

    private String matchObject(String str, int i){
        // TODO 解析对象
        return null;
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

        if((ch == 'e' || ch == 'E') && i + 1 < len){
            i++;
            char p = str.charAt(i);
            if(isNotDigit(p)){
                if(i + 1 < len && (p == '+' || p == '-')){
                    i++;
                    char p2 = str.charAt(i);
                    if(!isNotDigit(p2)){
                        number.append(ch).append(p);
                    }else{
                        return number.toString();
                    }
                }else{
                    return number.toString();
                }
            }else{
                number.append(ch);
            }

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

    private static class WordNode<D>{
        private char ch;

        private Word<D> word;

        private final ArrayList<WordNode<D>> children = new ArrayList<>();

        private void add(String word, D data){
            int index = 0;
            ArrayList<WordNode<D>> cursorChildren = children;
            WordNode<D> targetNode = null;
            int len = word.length();
            while(true){
                char ch = word.charAt(index);
                for (WordNode<D> child : cursorChildren) {
                    if(child.ch == ch){
                        targetNode = child;
                        break;
                    }
                }
                if(targetNode == null){
                    targetNode = new WordNode<D>();
                    targetNode.ch = ch;
                    cursorChildren.add(targetNode);
                }
                cursorChildren = targetNode.children;
                index++;
                if(index == len){
                    if(targetNode.word != null){
                        throw new WordDuplicateError(word);
                    }
                    targetNode.word = new Word<>(word, data);
                    break;
                }
                targetNode = null;
            }
        }

        public Word<D> get(String literal){
            Word<D> word = match(0, literal);
            if(word != null && word.literal.equals(literal)){
                return word;
            }
            return null;
        }

        public Word<D> match(int start, String expression){
            if(start >= expression.length()){ return null; }
            char c = expression.charAt(start);
            WordNode<D> cursor = null;
            for (WordNode<D> n : children) {
                if(n.ch == c){
                    cursor = n;
                    break;
                }
            }
            if(cursor == null){
                return null;
            }else{
                Word<D> word = cursor.match(start + 1, expression);
                if(word == null){
                    return cursor.word;
                }else{
                    return word;
                }
            }
        }
    }

    private static class Word<D>{

        private final String literal;

        private final D data;

        public Word(String literal, D data) {
            this.literal = literal;
            this.data = data;
        }

        public int length(){
            return this.literal.length();
        }
    }

}
