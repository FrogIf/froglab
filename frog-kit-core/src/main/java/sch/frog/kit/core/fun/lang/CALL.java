package sch.frog.kit.core.fun.lang;

import sch.frog.kit.core.exception.ExecuteException;
import sch.frog.kit.core.execute.ISession;
import sch.frog.kit.core.fun.AbstractFunction;
import sch.frog.kit.core.fun.IFunction;
import sch.frog.kit.core.value.Locator;
import sch.frog.kit.core.value.Value;
import sch.frog.kit.core.value.ValueType;

public class CALL extends AbstractFunction {

    @Override
    public String name() {
        return "call";
    }

    @Override
    public String description() {
        return null;
    }

    @Override
    public Value execute(Value[] args, ISession session) {
        if(args.length < 1){
            throw new ExecuteException("at least on parameter");
        }
        Value funName = args[0];
        if(funName.getType() != ValueType.SYMBOL){
            throw new ExecuteException("call method's first argument must symbol type");
        }
        Locator locator = funName.to(Locator.class);
        if(locator.isIndex()){
            throw new ExecuteException("call method's symbol type must key");
        }
        Value val = session.getVariable(locator.getKey());
        IFunction function = null;
        if(val != null && val.getType() == ValueType.FUNCTION){
            function = val.to(IFunction.class);
        }
        if(function == null){
            throw new ExecuteException("function is null");
        }
        Value[] funArgs = new Value[args.length - 1];
        System.arraycopy(args, 1, funArgs, 0, funArgs.length);
        return function.execute(funArgs, session);
    }
}
