package sch.frog.kit.lang.lexical;

public class Token {

    public static final Token EOF = new Token("EOF", TokenType.EOF, -1);

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
        return literal + "(" + type.name() + ")";
    }


}
