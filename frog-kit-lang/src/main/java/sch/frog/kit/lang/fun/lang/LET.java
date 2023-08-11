package sch.frog.kit.lang.fun.lang;

import sch.frog.kit.lang.exception.ExecuteException;
import sch.frog.kit.lang.execute.IRuntimeContext;
import sch.frog.kit.lang.fun.AbstractFunction;
import sch.frog.kit.lang.value.Locator;
import sch.frog.kit.lang.value.Value;
import sch.frog.kit.lang.value.ValueType;

public class LET extends AbstractFunction {
    @Override
    public String name() {
        return "let";
    }

    @Override
    public String description() {
        return null;
    }

    @Override
    public Value execute(Value[] args, IRuntimeContext context) {
        if(args.length < 1 || args.length > 2){
            throw new ExecuteException("define function must have 1 or 2 arguments");
        }
        Value arg = args[0];
        if(arg.getType() != ValueType.SYMBOL){
            throw new ExecuteException("define function's arguments type must identifier");
        }
        Locator locator = arg.cast(Locator.class);
        if(locator.isIndex()){
            throw new ExecuteException("index can't as variable name");
        }
        Value v = args.length == 1 ? Value.NULL : args[1];
        context.addLocalVariable(locator.getKey(), v);
        return v;
    }
}
