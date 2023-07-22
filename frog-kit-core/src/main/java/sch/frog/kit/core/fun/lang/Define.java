package sch.frog.kit.core.fun.lang;

import sch.frog.kit.core.exception.ExecuteException;
import sch.frog.kit.core.execute.ISession;
import sch.frog.kit.core.fun.AbstractFunction;
import sch.frog.kit.core.value.Locator;
import sch.frog.kit.core.value.Value;
import sch.frog.kit.core.value.ValueType;

public class Define extends AbstractFunction {

    @Override
    public String name() {
        return "define";
    }

    @Override
    public String description() {
        return null;
    }

    @Override
    public Value execute(Value[] args, ISession session) {
        if(args.length < 1 || args.length > 2){
            throw new ExecuteException("define function must have 1 or 2 arguments");
        }
        Value arg = args[0];
        if(arg.getType() != ValueType.SYMBOL){
            throw new ExecuteException("define function's arguments type must identifier");
        }
        Locator locator = arg.to(Locator.class);
        if(locator.isIndex()){
            throw new ExecuteException("index can't as variable name");
        }
        Value v = args.length == 1 ? Value.NULL : args[1];
        session.addValue(locator.getKey(), v);
        return v;
    }
}