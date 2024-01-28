package sch.frog.kit.lang.grammar.node;

import sch.frog.kit.lang.lexical.Token;
import sch.frog.kit.lang.lexical.TokenType;
import sch.frog.kit.lang.value.Value;

public class StringNode extends ValueNode {

    private final String stringVal;

    public StringNode(Token token){
        super(token);
        if(token.type() != TokenType.STRING){
            throw new IllegalArgumentException("string node type expect string but " + token.type());
        }
        stringVal = token.literal().substring(1, token.literal().length() - 1);
    }

    @Override
    public Value evaluate() {
        return new Value(stringVal);
    }
}
