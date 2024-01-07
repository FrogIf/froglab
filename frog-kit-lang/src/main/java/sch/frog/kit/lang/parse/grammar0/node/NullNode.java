package sch.frog.kit.lang.parse.grammar0.node;

import sch.frog.kit.lang.parse.lexical.Token;
import sch.frog.kit.lang.parse.lexical.TokenType;
import sch.frog.kit.lang.value.Value;

public class NullNode extends ValuableNode {

    public NullNode(Token token){
        super(token);
        if(token.type() != TokenType.NULL){
            throw new IllegalArgumentException("null node type expect null but " + token.type());
        }
    }

    @Override
    public Value evaluate() {
        return Value.NULL;
    }
}
