package sch.frog.lab.lang.lexical;

public class TokenUtil {

    public static boolean isConstant(TokenType tokenType){
        return tokenType == TokenType.BOOL || tokenType == TokenType.NULL || tokenType == TokenType.NUMBER || tokenType == TokenType.STRING;
    }

}
