package sch.frog.kit.core.test;

import sch.frog.kit.lang.parse.exception.GrammarException;
import sch.frog.kit.lang.parse.grammar0.GrammarAnalyzer;
import sch.frog.kit.lang.parse.grammar0.node.Statements;
import sch.frog.kit.lang.parse.grammar0.util.AstTreeUtil;
import sch.frog.kit.lang.parse.io.StringScriptStream;
import sch.frog.kit.lang.parse.lexical.GeneralTokenStream;
import sch.frog.kit.lang.parse.lexical.LexicalAnalyzer;

import java.util.Scanner;

public class GrammarTest {

    public static void main(String[] args){
        GrammarAnalyzer grammarAnalyzer = new GrammarAnalyzer();
        Scanner sc = new Scanner(System.in);

        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer();

        while(true){
            String expression = sc.nextLine();
            if("exit".equals(expression)){ break; }
            GeneralTokenStream tokenStream = lexicalAnalyzer.parse(new StringScriptStream(expression));
            try {
                Statements statement = grammarAnalyzer.parse(tokenStream);
                System.out.println(AstTreeUtil.generateTree(statement));
            } catch (GrammarException e) {
                System.out.println("message : " + e.getMessage() + ", pos : " + e.getPos());
            }
            System.out.println();
        }

    }

    private interface Test{
        void t();
    }

}
