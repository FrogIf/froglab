package sch.frog.kit.lang.lexical;

import sch.frog.kit.lang.io.IScriptStream;

/**
 * 词法分析
 */
public class LexicalAnalyzer {

    private final TokenSearcher<TokenType> specialWord = new TokenSearcher<>();

    public LexicalAnalyzer() {
        init();
    }

    private void init(){
        specialWord.put(TokenConstant.TRUE, TokenType.BOOL);
        specialWord.put(TokenConstant.FALSE, TokenType.BOOL);
        specialWord.put(TokenConstant.NULL, TokenType.NULL);

        specialWord.put(TokenConstant.ASSIGN, TokenType.ASSIGN);
        specialWord.put(TokenConstant.VAR, TokenType.KEYWORD);
        specialWord.put(TokenConstant.LET, TokenType.KEYWORD);
        specialWord.put(TokenConstant.CONTINUE, TokenType.KEYWORD);
        specialWord.put(TokenConstant.BREAK, TokenType.KEYWORD);
        specialWord.put(TokenConstant.PACKAGE, TokenType.KEYWORD);
        specialWord.put(TokenConstant.IMPORT, TokenType.KEYWORD);
        specialWord.put(TokenConstant.IF, TokenType.KEYWORD);
        specialWord.put(TokenConstant.ELSE, TokenType.KEYWORD);
        specialWord.put(TokenConstant.WHILE, TokenType.KEYWORD);
        specialWord.put(TokenConstant.DO, TokenType.KEYWORD);
        specialWord.put(TokenConstant.FOR, TokenType.KEYWORD);

        specialWord.put(TokenConstant.PLUS, TokenType.OPERATOR);
        specialWord.put(TokenConstant.MINUS, TokenType.OPERATOR);
        specialWord.put(TokenConstant.STAR, TokenType.OPERATOR);
        specialWord.put(TokenConstant.SLASH, TokenType.OPERATOR);
        specialWord.put(TokenConstant.LT, TokenType.OPERATOR);
        specialWord.put(TokenConstant.LTE, TokenType.OPERATOR);
        specialWord.put(TokenConstant.GT, TokenType.OPERATOR);
        specialWord.put(TokenConstant.GTE, TokenType.OPERATOR);
        specialWord.put(TokenConstant.EQ, TokenType.OPERATOR);
        specialWord.put(TokenConstant.NOT_EQ, TokenType.OPERATOR);
        specialWord.put(TokenConstant.AND, TokenType.OPERATOR);
        specialWord.put(TokenConstant.OR, TokenType.OPERATOR);
        specialWord.put(TokenConstant.BANG, TokenType.OPERATOR);
        specialWord.put(TokenConstant.SHORT_CIRCLE_AND, TokenType.OPERATOR);
        specialWord.put(TokenConstant.SHORT_CIRCLE_OR, TokenType.OPERATOR);
        specialWord.put(TokenConstant.REFERENCE, TokenType.OPERATOR);

        specialWord.put(TokenConstant.LPAREN, TokenType.STRUCT);
        specialWord.put(TokenConstant.RPAREN, TokenType.STRUCT);
        specialWord.put(TokenConstant.LBRACKET, TokenType.STRUCT);
        specialWord.put(TokenConstant.RBRACKET, TokenType.STRUCT);
        specialWord.put(TokenConstant.LBRACE, TokenType.STRUCT);
        specialWord.put(TokenConstant.RBRACE, TokenType.STRUCT);
        specialWord.put(TokenConstant.COMMA, TokenType.STRUCT);
        specialWord.put(TokenConstant.DOT, TokenType.STRUCT);
        specialWord.put(TokenConstant.COLON, TokenType.STRUCT);
        specialWord.put(TokenConstant.ARROW, TokenType.STRUCT);
        specialWord.put(TokenConstant.SEMICOLON, TokenType.STRUCT);
    }

    public GeneralTokenStream parse(IScriptStream scriptStream){
        return new GeneralTokenStream(scriptStream, this);
    }

    /**
     * 读取下一个token, 如果已经读到流结束, 则返回null
     */
    public Token read(IScriptStream scriptStream){
        char ch = scriptStream.current();
        while(isWhitespace(ch)){
            ch = scriptStream.next();
        }
        if(ch == IScriptStream.EOF){ return null; }

        Token token = null;
        int i = scriptStream.pos();

        if(ch == '"' || ch == '\''){  // 匹配字符串
            String str = matchString(scriptStream, ch);
            if(str != null){
                token = new Token(str, TokenType.STRING, i);
            }
        }else if(ch == '/'){ // 注释
            String comment = matchComment(scriptStream);
            if(comment != null){
                token = new Token(comment, TokenType.COMMENT, i);
            }
        }else if(isDigit(ch)){
            // 匹配数字
            String num = matchUnsignedNumber(scriptStream);
            if(num != null){
                token = new Token(num, TokenType.NUMBER, i);
            }
        }else{
            if(maybeIdentifierFirst(ch)){
                String identifier = matchIdentifier(scriptStream);
                if(identifier != null){
                    TokenType tokenType = specialWord.get(identifier);
                    token = new Token(identifier, tokenType == null ? TokenType.IDENTIFIER : tokenType, i);
                }
            }else{
                TokenSearcher.Entry<TokenType> specialWord = this.specialWord.match(scriptStream);
                if(specialWord != null){
                    token = new Token(specialWord.getKey(), specialWord.getValue(), i);
                }
            }
        }
        if(token == null){
            token = new Token(String.valueOf(ch), TokenType.ILLEGAL, i);
            scriptStream.next();
        }
        return token;
    }

    private static boolean maybeIdentifierFirst(char ch){
        return isLetter(ch) || ch == '_';
    }

    /**
     * 第一个字符: a - z, A - Z, _
     * 其余字符: a - z, A - Z, 0 - 9, _
     */
    public static String matchIdentifier(IScriptStream scriptStream){
        char ch = scriptStream.current();
        if(!maybeIdentifierFirst(ch)){ return null; }
        StringBuilder identifier = new StringBuilder();
        identifier.append(scriptStream.current());
        while((ch = scriptStream.next()) != IScriptStream.EOF){
            if(!isLetter(ch) && !isDigit(ch) && ch != '_'){
                break;
            }
            identifier.append(ch);
        }
        return identifier.toString();
    }

    public static String matchString(IScriptStream scriptStream, char edge){
        boolean match = false;
        char ch = scriptStream.current();
        if (ch != edge) { return null; }
        StringBuilder result = new StringBuilder();
        result.append(edge);
        int escape = 0;
        while ((ch = scriptStream.next()) != IScriptStream.EOF) {
            if (ch == edge && (escape & 1) == 0) {
                match = true;
                result.append(edge);
                scriptStream.next();
                break;
            }else{
                result.append(ch);
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

    private String matchUnsignedNumber(IScriptStream scriptStream){
        char ch = scriptStream.current();
        if(!isDigit(ch)){ return null; }
        char p;
        if(ch == '0' && (p = scriptStream.peek()) != '.' && p != IScriptStream.EOF){ // 非十进制数
            StringBuilder sb = new StringBuilder();
            /*
             * 二进制, 0b或者0B开头
             * 八进制, 以0开头
             * 十六进制, 0x或者0X开头
             */
            INumberMatcher m;
            if(isDigit(p)){
                m = c -> c >= '0' && c <= '7';
                sb.append(ch).append(scriptStream.next());
            }else if(p == 'b' || p == 'B'){
                m = c -> c == '0' || c == '1';
                sb.append(ch).append(scriptStream.next());
            }else if(p == 'x' || p == 'X'){
                m = c -> (c >= '0' && c <= '9') || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F');
                sb.append(ch).append(scriptStream.next());
            }else{
                m = c -> false;
                sb.append(ch);
            }
            boolean mat;
            while((ch = scriptStream.next()) != IScriptStream.EOF){
                mat = m.judge(ch);
                if(!mat){ break; }
                sb.append(ch);
            }
            return sb.toString();
        }else{
            StringBuilder sb = new StringBuilder();
            sb.append(ch);
            boolean dot = false;
            boolean digit;
            boolean loop = false;
            while((ch = scriptStream.next()) != IScriptStream.EOF){
                digit = isDigit(ch)
                        || (!dot && (dot = ch == '.'))  // 小数点
                        || (dot && !loop && (loop = ch == '_')); // 循环节
                if(!digit){ break; }
                sb.append(ch);
            }
            return sb.toString();
        }
    }

    public static String matchComment(IScriptStream scriptStream){
        if(scriptStream.current() == '/'){
            if(scriptStream.peek() == '/'){
                scriptStream.next();
                StringBuilder sb = new StringBuilder("//");
                char ch;
                while((ch = scriptStream.next()) != IScriptStream.EOF){
                    if(ch == '\n'){
                        scriptStream.next();
                        break;
                    }
                    sb.append(ch);
                }
                return sb.toString();
            }else if(scriptStream.peek() == '*'){
                scriptStream.next();
                StringBuilder sb = new StringBuilder("/*");
                boolean isStar = false;
                char ch;
                while((ch = scriptStream.next()) != IScriptStream.EOF){
                    if(ch == '/' && isStar){
                        sb.append('/');
                        scriptStream.next();
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

    private interface INumberMatcher{
        boolean judge(char ch);
    }

}
