package sch.frog.lab.lang.grammar.node;

import sch.frog.lab.lang.grammar.IAstNode;
import sch.frog.lab.lang.grammar.IExpression;

import java.util.Arrays;
import java.util.List;

public class IfEntry implements IAstNode {

    private final IExpression condition;

    private final NestStatement nestStatement;

    public IfEntry(IExpression condition, NestStatement nestStatement) {
        this.condition = condition;
        this.nestStatement = nestStatement;
    }

    public IExpression getCondition() {
        return condition;
    }

    public NestStatement getNestStatement() {
        return nestStatement;
    }

    @Override
    public String literal() {
        return "#if-entry";
    }

    @Override
    public List<IAstNode> getChildren() {
        return Arrays.asList(condition, nestStatement);
    }
}
