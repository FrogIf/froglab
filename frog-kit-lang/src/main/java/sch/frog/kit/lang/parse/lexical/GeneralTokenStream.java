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
        if(peek != null){
            current = peek;
        }
        peek = lexicalAnalyzer.read(scriptStream);
        return current;
    }
}
