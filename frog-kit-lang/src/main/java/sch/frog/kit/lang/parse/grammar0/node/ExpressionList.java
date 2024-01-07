package sch.frog.kit.lang.parse.grammar0.node;

import sch.frog.kit.lang.parse.grammar0.IAstNode;
import sch.frog.kit.lang.parse.grammar0.IExpression;

import java.util.ArrayList;
import java.util.List;

public class ExpressionList implements IAstNode {

    private final List<IExpression> expressions = new ArrayList<>();

    public ExpressionList(List<IExpression> expressions){
        this.expressions.addAll(expressions);
    }

    @Override
    public String literal() {
        return "(,)";
    }

    @Override
    public List<IAstNode> getChildren() {
        return new ArrayList<>(expressions);
    }
}
