package sch.frog.kit.lang.parse.grammar0.node;

import sch.frog.kit.lang.parse.grammar0.IAstNode;
import sch.frog.kit.lang.parse.grammar0.IExpression;

import java.util.Arrays;
import java.util.List;

public class DoWhileStatement implements IterationStatement{

    private final NestStatement nestStatement;

    private final IExpression condition;

    public DoWhileStatement(NestStatement nestStatement, IExpression condition) {
        this.nestStatement = nestStatement;
        this.condition = condition;
    }

    public NestStatement getNestStatement() {
        return nestStatement;
    }

    public IExpression getCondition() {
        return condition;
    }

    @Override
    public String literal() {
        return "do-while";
    }

    @Override
    public List<IAstNode> getChildren() {
        return Arrays.asList(nestStatement, condition);
    }
}
