package sch.frog.kit.lang.parse.lexical;

import sch.frog.kit.lang.parse.io.IScriptStream;

public class GeneralTokenStream implements ITokenStream{

    private Token current;

    private Token peek;

    private final IScriptStream scriptStream;

    private final LexicalAnalyzer lexicalAnalyzer;

    public GeneralTokenStream(IScriptStream scriptStream, LexicalAnalyzer lexicalAnalyzer) {
        this.scriptStream = scriptStream;
        this.lexicalAnalyzer = lexicalAnalyzer;
        /*
         * 双指针(peek, current)
         * 第一次next, peek指针指向开始token
         * 第二次next, current指针指向开始token
         */
        this.next();
        this.next();
    }

    @Override
    public Token current() {
        return current;
    }

    @Override
    public Token peek() {
        return peek;
    }

    @Override
    public Token next() {
        current = peek;
        if(current == null){ current = Token.EOF; }

        peek = lexicalAnalyzer.read(scriptStream);
        if(peek == null){ peek = Token.EOF; }

        return current;
    }
}
