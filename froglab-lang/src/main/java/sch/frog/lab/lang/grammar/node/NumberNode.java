package sch.frog.lab.lang.grammar.node;

import io.github.frogif.calculator.number.impl.RationalNumber;
import sch.frog.lab.lang.lexical.Token;
import sch.frog.lab.lang.lexical.TokenType;
import sch.frog.lab.lang.value.Value;

public class NumberNode extends ConstantNode {

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
