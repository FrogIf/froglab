package sch.frog.kit.test;

import sch.frog.kit.core.exception.IncorrectExpressionException;
import sch.frog.kit.core.parse.Token;
import sch.frog.kit.core.parse.TokenParser;

import java.util.List;

public class TokenParserTest {


    public static void main(String[] args) throws IncorrectExpressionException {
        TokenParser tokenParser = new TokenParser();
        List<Token> tokens = tokenParser.getToken("dateFormat(now(), 'yyyy-MM-dd HH:mm:ss', ['aaaa', \"bbbb\", 123.356, {aaa:123}])");
//        List<Token> tokens = tokenParser.getToken("['aaaa', \"bbbb\", 123.356, {'aaa':123}]");

        for (Token token : tokens) {
            System.out.println(token.literal());
        }
    }

}
