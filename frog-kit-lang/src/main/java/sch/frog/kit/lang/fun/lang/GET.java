package sch.frog.kit.lang.fun.lang;

import sch.frog.kit.lang.exception.ExecuteException;
import sch.frog.kit.lang.execute.IRuntimeContext;
import sch.frog.kit.lang.fun.AbstractFunction;
import sch.frog.kit.lang.value.Locator;
import sch.frog.kit.lang.value.Value;
import sch.frog.kit.lang.value.ValueType;

public class GET extends AbstractFunction {

    @Override
    public String name() {
        return "get";
    }

    @Override
    public String description() {
        return "Value get(string key)";
    }

    @Override
    public Value execute(Value[] args, IRuntimeContext context) {
        if(args.length != 1){
            throw new ExecuteException("set function must has 1 arguments");
        }
        Value arg = args[0];
        if(arg.getType() != ValueType.SYMBOL){
            throw new ExecuteException("set function first argument must string type");
        }
        Locator locator = arg.cast(Locator.class);
        return context.getVariable(locator.getKey());
    }


}
