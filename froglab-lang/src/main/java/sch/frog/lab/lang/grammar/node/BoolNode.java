package sch.frog.lab.lang.grammar.node;

import sch.frog.lab.lang.lexical.Token;
import sch.frog.lab.lang.lexical.TokenType;
import sch.frog.lab.lang.value.Value;

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
