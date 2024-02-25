package sch.frog.lab.lang.fun.general;

import sch.frog.lab.lang.exception.ExecuteException;
import sch.frog.lab.lang.fun.IFunction;
import sch.frog.lab.lang.semantic.IExecuteContext;
import sch.frog.lab.lang.value.Value;
import sch.frog.lab.lang.value.ValueType;

public class IsListFunction implements IFunction {
    @Override
    public String name() {
        return "isList";
    }

    @Override
    public String description() {
        return "判断一个值是否是集合类型\nisList(val)";
    }

    @Override
    public Value execute(Value[] args, IExecuteContext context) throws ExecuteException {
        if(args.length != 1){
            throw new ExecuteException("isList function expect 1 arguments, but " + args.length);
        }
        Value val = args[0];
        return Value.of(val.getType() == ValueType.LIST);
    }
}
