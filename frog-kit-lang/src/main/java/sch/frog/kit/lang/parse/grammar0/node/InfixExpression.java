package sch.frog.kit.lang.parse.grammar0.node;

import sch.frog.kit.lang.parse.grammar0.IAstNode;
import sch.frog.kit.lang.parse.grammar0.IExpression;
import sch.frog.kit.lang.parse.lexical.Token;

import java.util.Arrays;
import java.util.List;

public class InfixExpression implements IExpression {

    private final IExpression left;

    private final Token infix;

    private final IExpression right;

    public InfixExpression(IExpression left, Token infix, IExpression right) {
        this.left = left;
        this.infix = infix;
        this.right = right;
    }

    @Override
    public String literal() {
        return this.infix.literal();
    }

    @Override
    public List<IAstNode> getChildren() {
        return Arrays.asList(this.left, this.right);
    }
}
