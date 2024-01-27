package sch.frog.kit.lang.parse.grammar0.node;

import sch.frog.kit.lang.parse.exception.ExecuteException;
import sch.frog.kit.lang.parse.grammar0.IAstNode;
import sch.frog.kit.lang.parse.grammar0.IExpression;
import sch.frog.kit.lang.parse.semantic.IExecuteContext;
import sch.frog.kit.lang.parse.semantic.Result;
import sch.frog.kit.lang.parse.semantic.ResultType;

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

    public Result execute(IExecuteContext context) throws ExecuteException {
        do{
            Result result = nestStatement.execute(context);
            if(result.type() == ResultType.BREAK){
                return new Result(result.value(), ResultType.NORMAL);
            }else if(result.type() == ResultType.RETURN){
                return result;
            }
        } while (condition.evaluate(context).cast(boolean.class));
        return Result.VOID;
    }
}
