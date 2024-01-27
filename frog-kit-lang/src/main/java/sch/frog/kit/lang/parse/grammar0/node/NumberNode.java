package sch.frog.kit.lang.parse.grammar0.node;

import io.github.frogif.calculator.number.impl.RationalNumber;
import sch.frog.kit.lang.parse.lexical.Token;
import sch.frog.kit.lang.parse.lexical.TokenType;
import sch.frog.kit.lang.value.Value;

public class NumberNode extends ValueNode {

    public NumberNode(Token token){
        super(token);
        if(token.type() != TokenType.NUMBER){
            throw new IllegalArgumentException("number node type expect number but " + token.type());
        }
    }

    @Override
    public Value evaluate() {
        // TODO 进制
        return new Value(new RationalNumber(token.literal()));
    }
}
