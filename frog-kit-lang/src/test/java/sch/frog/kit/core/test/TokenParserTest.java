package sch.frog.kit.core.test;

import sch.frog.kit.lang.exception.IncorrectExpressionException;
import sch.frog.kit.lang.parse.lexical.LexicalAnalyzer;
import sch.frog.kit.lang.parse.lexical.Token;

import java.util.List;
import java.util.Scanner;

public class TokenParserTest {


    public static void main(String[] args) throws IncorrectExpressionException {
        Scanner sc = new Scanner(System.in);

        LexicalAnalyzer analyzer = new LexicalAnalyzer();

        while(true){
            String expression = sc.nextLine();
            if("exit".equals(expression)){ break; }
            List<Token> token = analyzer.getToken(expression);
            System.out.println(token);
        }

    }

}
