package sch.frog.kit.core.fun.lang;

import sch.frog.kit.core.exception.ExecuteException;
import sch.frog.kit.core.execute.ISession;
import sch.frog.kit.core.fun.AbstractFunction;
import sch.frog.kit.core.value.Locator;
import sch.frog.kit.core.value.Value;
import sch.frog.kit.core.value.ValueType;

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
    public Value execute(Value[] args, ISession session) {
        if(args.length != 1){
            throw new ExecuteException("set function must has 1 arguments");
        }
        Value arg = args[0];
        if(arg.getType() != ValueType.SYMBOL){
            throw new ExecuteException("set function first argument must string type");
        }
        Locator locator = arg.to(Locator.class);
        return locator.get(session);
    }


}
