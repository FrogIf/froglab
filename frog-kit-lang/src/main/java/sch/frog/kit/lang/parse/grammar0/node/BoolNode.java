package sch.frog.kit.lang.parse.grammar0.node;

import sch.frog.kit.lang.parse.lexical.Token;
import sch.frog.kit.lang.parse.lexical.TokenType;
import sch.frog.kit.lang.value.Value;

public class BoolNode extends ValuableNode {

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
