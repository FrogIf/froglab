package sch.frog.kit.core.exception;

import sch.frog.kit.core.parse.lexical.Token;

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
