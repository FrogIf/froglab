package sch.frog.kit.lang.parse.grammar0.node;

import sch.frog.kit.lang.parse.grammar0.IAstNode;
import sch.frog.kit.lang.parse.grammar0.IExpression;

import java.util.Collections;
import java.util.List;

public class ExpressionGroup implements IExpression {

    private final IExpression expression;

    public ExpressionGroup(IExpression expression) {
        this.expression = expression;
    }

    @Override
    public String literal() {
        return "()";
    }

    @Override
    public List<IAstNode> getChildren() {
        return Collections.singletonList(expression);
    }
}
