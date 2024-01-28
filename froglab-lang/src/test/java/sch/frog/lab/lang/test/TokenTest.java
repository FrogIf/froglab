package sch.frog.lab.lang.test;

import sch.frog.lab.lang.io.StringScriptStream;
import sch.frog.lab.lang.lexical.GeneralTokenStream;
import sch.frog.lab.lang.lexical.LexicalAnalyzer;
import sch.frog.lab.lang.lexical.Token;

public class TokenTest {

    public static void main(String[] args){
        LexicalAnalyzer a = new LexicalAnalyzer();
        GeneralTokenStream parse = a.parse(new StringScriptStream("_system.println(\"aaa\");"));
        do {
            System.out.println(parse.current());
            parse.next();
        }while(parse.current() != Token.EOF);
    }

}
