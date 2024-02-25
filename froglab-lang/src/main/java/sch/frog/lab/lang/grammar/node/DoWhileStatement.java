package sch.frog.lab.lang.grammar.node;

import sch.frog.lab.lang.exception.ExecuteException;
import sch.frog.lab.lang.grammar.IAstNode;
import sch.frog.lab.lang.grammar.IExpression;
import sch.frog.lab.lang.semantic.IExecuteContext;
import sch.frog.lab.lang.semantic.Result;
import sch.frog.lab.lang.semantic.ResultType;

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
                return new Result(result.reference(), ResultType.NORMAL);
            }else if(result.type() == ResultType.RETURN){
                return result;
            }
        } while (condition.evaluate(context).value().cast(boolean.class));
        return Result.VOID;
    }
}
