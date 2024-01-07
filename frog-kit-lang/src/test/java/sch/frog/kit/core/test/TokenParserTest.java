package sch.frog.kit.core.test;

import sch.frog.kit.lang.parse.io.StringScriptStream;
import sch.frog.kit.lang.parse.lexical.GeneralTokenStream;
import sch.frog.kit.lang.parse.lexical.LexicalAnalyzer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class TokenParserTest {

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

        LexicalAnalyzer analyzer = new LexicalAnalyzer();

        String script = readScript("script-test.fr");
        GeneralTokenStream ts = analyzer.parse(new StringScriptStream(script));
        print(ts);
        System.out.println();

        while(true){
            String expression = sc.nextLine();
            if("exit".equals(expression)){ break; }
            GeneralTokenStream tokenStream = analyzer.parse(new StringScriptStream(expression));
            print(tokenStream);
            System.out.println();
        }
    }

    private static void print(GeneralTokenStream tokenStream){
        do{
            System.out.print(tokenStream.current() + ", ");
        }while (tokenStream.next() != null);
    }

    private static String readScript(String path) throws IOException {
        File file = new File(path);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while((line = reader.readLine()) != null){
            sb.append(line).append('\n');
        }
        return sb.toString();
    }

}
