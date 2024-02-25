package sch.frog.lab.lang.grammar.node;

import sch.frog.lab.lang.exception.ExecuteException;
import sch.frog.lab.lang.grammar.IExpression;
import sch.frog.lab.lang.grammar.IAstNode;
import sch.frog.lab.lang.semantic.IExecuteContext;
import sch.frog.lab.lang.semantic.InnerExecuteContext;
import sch.frog.lab.lang.semantic.Result;
import sch.frog.lab.lang.semantic.ResultType;
import sch.frog.lab.lang.value.Value;

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

    public Result execute(IExecuteContext context) throws ExecuteException {
        InnerExecuteContext innerContext = new InnerExecuteContext(context);
        if(forInitializer != null){
            VariableStatement variableStatement = forInitializer.getVariableStatement();
            VariableBody variableBody = forInitializer.getVariableBody();
            if(variableStatement != null){
                context.executor().executeVariableStatement(variableStatement, innerContext);
            }else if(variableBody != null){
                String variableName = variableBody.getVariableName();
                IExpression expressionBody = variableBody.expressionBody();
                if(expressionBody == null){
                    throw new ExecuteException("variable not assign");
                }
                Value value = expressionBody.evaluate(context);
                context.setVariable(variableName, value);
            }
        }

        while(condition.evaluate(innerContext).cast(boolean.class)){
            Result result = nestStatement.execute(innerContext);
            if(result.type() == ResultType.BREAK){
                return new Result(result.value(), ResultType.NORMAL);
            }else if(result.type() == ResultType.RETURN){
                return result;
            }

            if(afterOnce != null){
                afterOnce.evaluate(innerContext);
            }
        }

        return Result.VOID;
    }
}
