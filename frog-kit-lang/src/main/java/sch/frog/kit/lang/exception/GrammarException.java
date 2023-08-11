package sch.frog.kit.lang.exception;

import sch.frog.kit.lang.parse.lexical.Token;

public class GrammarException extends Exception{

    private final int pos;

    public GrammarException(String message, int pos) {
        super(message);
        this.pos = pos;
    }

    public GrammarException(Token token){
        super("grammar incorrect near : " + token.literal() + " at " + token.pos());
        this.pos = token.pos();
    }

    public int pos(){
        return pos;
    }
}
