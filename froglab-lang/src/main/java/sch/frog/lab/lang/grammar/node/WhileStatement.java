package sch.frog.lab.lang.grammar.node;

import sch.frog.lab.lang.exception.ExecuteException;
import sch.frog.lab.lang.grammar.IExpression;
import sch.frog.lab.lang.grammar.IAstNode;
import sch.frog.lab.lang.semantic.IExecuteContext;
import sch.frog.lab.lang.semantic.Result;
import sch.frog.lab.lang.semantic.ResultType;

import java.util.Arrays;
import java.util.List;

public class WhileStatement implements IterationStatement{

    private final IExpression condition;

    private final NestStatement nestStatement;

    public WhileStatement(IExpression condition, NestStatement nestStatement) {
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
        return "while";
    }

    @Override
    public List<IAstNode> getChildren() {
        return Arrays.asList(condition, nestStatement);
    }

    public Result execute(IExecuteContext context) throws ExecuteException {
        while (condition.evaluate(context).cast(boolean.class)){
            Result result = nestStatement.execute(context);
            if(result.type() == ResultType.BREAK){
                return new Result(result.value(), ResultType.NORMAL);
            }else if(result.type() == ResultType.RETURN){
                return result;
            }
        }
        return Result.VOID;
    }
}
