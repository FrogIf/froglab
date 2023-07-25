package sch.frog.kit.core.parse.lexical;

import sch.frog.kit.core.common.SearchMap;
import sch.frog.kit.core.exception.IncorrectExpressionException;
import sch.frog.kit.core.parse.common.TokenRuleUtil;

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
        specialWord.put("=>", TokenType.STRUCT);
    }

    public List<Token> getToken(String expression) throws IncorrectExpressionException {
        int len = expression.length();
        ArrayList<Token> tokens = new ArrayList<>();
        for(int i = 0; i < len; ){
            Token token = null;
            char ch = expression.charAt(i);
            if(TokenRuleUtil.isWhitespace(ch)){
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
                String str = TokenRuleUtil.matchString(expression, i, ch);
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
            }else if(ch == '/'){
                String comment = TokenRuleUtil.matchComment(i, expression);
                if(comment == null){
                    throw new IncorrectExpressionException("comment incorrect", i);
                }
                token = new Token(comment, TokenType.COMMENT, i);
            }else {
                // 匹配数字
                String num = TokenRuleUtil.matchNumber(i, expression);
                if(num != null){
                    token = new Token(num, TokenType.NUMBER, i);
                }

                if(token == null){  // 匹配标识符
                    SearchMap.Entry<TokenType> specialWord = this.specialWord.match(expression, i);

                    String identifier = TokenRuleUtil.matchIdentifier(expression, i);

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
                throw new IncorrectExpressionException("expression parse failed for : " + expression + " at " + i, i);
            }
            tokens.add(token);
            i += token.literal().length();
        }
        return tokens;
    }

}
