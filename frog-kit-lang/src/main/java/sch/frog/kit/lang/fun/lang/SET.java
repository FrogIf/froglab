package sch.frog.kit.lang.fun.lang;

import sch.frog.kit.lang.exception.ExecuteException;
import sch.frog.kit.lang.execute.IRuntimeContext;
import sch.frog.kit.lang.fun.AbstractFunction;
import sch.frog.kit.lang.value.Locator;
import sch.frog.kit.lang.value.Value;
import sch.frog.kit.lang.value.ValueType;

public class SET extends AbstractFunction {
    @Override
    public String name() {
        return "set";
    }

    @Override
    public String description() {
        return null;
    }

    @Override
    public Value execute(Value[] args, IRuntimeContext context) {
        if(args.length != 2){
            throw new ExecuteException("set function must has 2 arguments");
        }
        Value arg = args[0];
        if(arg.getType() != ValueType.SYMBOL){
            throw new ExecuteException("set function first argument must identifier type");
        }
        Locator locator = arg.cast(Locator.class);
        if(locator.isIndex()){
            throw new ExecuteException("index can't as variable name");
        }
        if(context.getVariable(locator.getKey()) == null){
            throw new ExecuteException("variable " + locator.getKey() + " is not exist");
        }
        context.setVariable(locator.getKey(), args[1]);
        return Value.VOID;
    }
}
