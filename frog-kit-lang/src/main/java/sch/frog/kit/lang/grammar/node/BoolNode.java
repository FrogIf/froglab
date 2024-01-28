package sch.frog.kit.lang.grammar.node;

import sch.frog.kit.lang.lexical.Token;
import sch.frog.kit.lang.lexical.TokenType;
import sch.frog.kit.lang.value.Value;

public class BoolNode extends ValueNode {

    public BoolNode(Token token){
        super(token);
        if(token.type() != TokenType.BOOL){
            throw new IllegalArgumentException("bool node type expect bool but " + token.type());
        }
    }

    @Override
    public Value evaluate() {
        return new Value("true".equals(token.literal()));
    }
}
