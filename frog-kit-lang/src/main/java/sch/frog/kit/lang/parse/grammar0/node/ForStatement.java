package sch.frog.kit.lang.parse.grammar0.node;

import sch.frog.kit.lang.parse.grammar0.IAstNode;
import sch.frog.kit.lang.parse.grammar0.IExpression;

import java.util.ArrayList;
import java.util.List;

public class ForStatement implements IterationStatement{

    private final ForInitializer forInitializer;

    private final IExpression condition;

    private final ExpressionList afterOnce;

    private final NestStatement nestStatement;

    public ForStatement(ForInitializer forInitializer, IExpression condition, ExpressionList afterOnce, NestStatement nestStatement) {
        this.forInitializer = forInitializer;
        this.condition = condition;
        this.afterOnce = afterOnce;
        this.nestStatement = nestStatement;
    }

    public ForInitializer getForInitializer() {
        return forInitializer;
    }

    public IExpression getCondition() {
        return condition;
    }

    public ExpressionList getAfterOnce() {
        return afterOnce;
    }

    public NestStatement getNestStatement() {
        return nestStatement;
    }

    @Override
    public String literal() {
        return "for";
    }

    @Override
    public List<IAstNode> getChildren() {
        ArrayList<IAstNode> nodes = new ArrayList<>();
        if(forInitializer != null){
            nodes.add(forInitializer);
        }
        if(condition != null){
            nodes.add(condition);
        }
        if(afterOnce != null){
            nodes.add(afterOnce);
        }
        nodes.add(nestStatement);
        return nodes;
    }
}
