package sch.frog.kit.lang.parse.grammar0.node;

import sch.frog.kit.lang.parse.lexical.Token;
import sch.frog.kit.lang.parse.lexical.TokenType;
import sch.frog.kit.lang.value.Value;

public class IdentifierNode extends ValuableNode {

    public IdentifierNode(Token token){
        super(token);
        if(token.type() != TokenType.IDENTIFIER){
            throw new IllegalArgumentException("identifier node type expect identifier but " + token.type());
        }
    }

    @Override
    public Value evaluate() {
        // TODO identifier
        return null;
    }
}
