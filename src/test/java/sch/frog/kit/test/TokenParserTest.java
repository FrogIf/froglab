package sch.frog.kit.test;

import sch.frog.kit.core.exception.IncorrectExpressionException;
import sch.frog.kit.core.parse.Token;
import sch.frog.kit.core.parse.TokenParser;

import java.util.List;

public class TokenParserTest {


    public static void main(String[] args) throws IncorrectExpressionException {
        TokenParser tokenParser = new TokenParser();
        tokenParser.registerFunction("uuid");
        tokenParser.registerFunction("uuidi");
        tokenParser.registerFunction("dateFormat");
        tokenParser.registerFunction("now");
        List<Token> tokens = tokenParser.getToken("dateFormat(now(), 'yyyy-MM-dd HH:mm:ss')", null);

        for (Token token : tokens) {
            System.out.println(token.literal());
        }
    }

}
