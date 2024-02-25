package sch.frog.lab.lang.grammar.node;

import sch.frog.lab.lang.lexical.Token;
import sch.frog.lab.lang.lexical.TokenType;
import sch.frog.lab.lang.value.Value;

public class StringNode extends ConstantNode {

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
