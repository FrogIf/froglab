package sch.frog.kit.lang.parse.grammar0.node;

import sch.frog.kit.lang.parse.lexical.Token;
import sch.frog.kit.lang.parse.lexical.TokenType;
import sch.frog.kit.lang.value.Value;

public class StringNode extends ValueNode {

    public StringNode(Token token){
        super(token);
        if(token.type() != TokenType.STRING){
            throw new IllegalArgumentException("string node type expect string but " + token.type());
        }
    }

    @Override
    public Value evaluate() {
        return new Value(token.literal());
    }
}
