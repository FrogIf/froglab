package sch.frog.lab.lang.grammar.node;

import sch.frog.lab.lang.exception.ExecuteException;
import sch.frog.lab.lang.fun.IFunction;
import sch.frog.lab.lang.semantic.IExecuteContext;
import sch.frog.lab.lang.semantic.Reference;
import sch.frog.lab.lang.value.Value;

public class FunctionCaller extends AbstractCaller{

    public FunctionCaller(ExpressionList cursor, AbstractCaller next) {
        super(cursor, next);
    }

    @Override
    public String literal() {
        return "(#function_caller)";
    }

    @Override
    public Reference evaluate(Value upValue, IExecuteContext context) throws ExecuteException {
        if(upValue == null){
            throw new ExecuteException(ExecuteException.CODE_NULL_POINTER, "value is null");
        }
        IFunction fun = upValue.cast(IFunction.class);
        Value[] args = ((ExpressionList) cursor).evaluate(context);
        Value result = fun.execute(args, context);
        if(next == null){ return new Reference(result); }
        return next.evaluate(result, context);
    }
}
