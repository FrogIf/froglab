package sch.frog.lab.lang.grammar.node;

import sch.frog.lab.lang.lexical.Token;
import sch.frog.lab.lang.lexical.TokenType;
import sch.frog.lab.lang.value.Value;

public class NullNode extends ConstantNode {

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
