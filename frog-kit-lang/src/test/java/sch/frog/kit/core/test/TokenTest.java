package sch.frog.kit.core.test;

import sch.frog.kit.lang.io.StringScriptStream;
import sch.frog.kit.lang.lexical.GeneralTokenStream;
import sch.frog.kit.lang.lexical.LexicalAnalyzer;
import sch.frog.kit.lang.lexical.Token;

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
