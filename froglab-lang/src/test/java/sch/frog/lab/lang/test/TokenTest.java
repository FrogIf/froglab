package sch.frog.lab.lang.test;

import sch.frog.lab.lang.io.StringScriptStream;
import sch.frog.lab.lang.lexical.GeneralTokenStream;
import sch.frog.lab.lang.lexical.LexicalAnalyzer;
import sch.frog.lab.lang.lexical.Token;

import java.util.Scanner;

public class TokenTest {

    public static void main(String[] args){
        LexicalAnalyzer a = new LexicalAnalyzer();

        Scanner sc = new Scanner(System.in);

        while(true){
            String expression = sc.nextLine();
            if("exit".equals(expression)){ break; }

            GeneralTokenStream parse = a.parse(new StringScriptStream(expression));
            do {
                System.out.println(parse.current());
                parse.next();
            }while(parse.current() != Token.EOF);
        }
    }

}
