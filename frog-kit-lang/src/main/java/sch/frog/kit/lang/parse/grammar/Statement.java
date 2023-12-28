package sch.frog.kit.lang.parse.grammar;

import java.util.List;

public class Statement implements IAstNode{

    private IExpression expression;

    public Statement(IExpression expression) {
        this.expression = expression;
    }

    @Override
    public List<IAstNode> getChildren() {
        return null;
    }
}
