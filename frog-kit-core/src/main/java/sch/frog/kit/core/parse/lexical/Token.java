package sch.frog.kit.core.parse.lexical;

public class Token {

    private final int pos;

    private final String literal;

    private final TokenType type;

    public Token(String literal, TokenType type, int pos) {
        this.literal = literal;
        this.type = type;
        this.pos = pos;
    }

    public String literal() {
        return literal;
    }


    public TokenType type() {
        return type;
    }

    public int pos(){
        return this.pos;
    }

    @Override
    public String toString() {
        return literal;
    }


}
