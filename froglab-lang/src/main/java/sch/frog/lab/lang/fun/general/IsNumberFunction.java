package sch.frog.lab.lang.fun.general;

import sch.frog.lab.lang.exception.ExecuteException;
import sch.frog.lab.lang.fun.IFunction;
import sch.frog.lab.lang.semantic.IExecuteContext;
import sch.frog.lab.lang.value.VList;
import sch.frog.lab.lang.value.Value;
import sch.frog.lab.lang.value.ValueType;

public class IsNumberFunction implements IFunction {
    @Override
    public String name() {
        return "isNumber";
    }

    @Override
    public String description() {
        return "判断一个值是否是数字类型\nisNumber(val)";
    }

    @Override
    public Value execute(Value[] args, IExecuteContext context) throws ExecuteException {
        if(args.length != 1){
            throw new ExecuteException("isNumber function expect 1 arguments, but " + args.length);
        }
        Value val = args[0];
        return Value.of(val.getType() == ValueType.NUMBER);
    }
}
