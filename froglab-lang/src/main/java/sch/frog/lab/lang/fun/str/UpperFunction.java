package sch.frog.lab.lang.fun.str;

import sch.frog.lab.lang.fun.IFunction;
import sch.frog.lab.lang.exception.ExecuteException;
import sch.frog.lab.lang.semantic.IExecuteContext;
import sch.frog.lab.lang.value.Value;
import sch.frog.lab.lang.value.ValueType;

/**
 * 字符串转大写
 */
public class UpperFunction implements IFunction {

    @Override
    public String name() {
        return "upper";
    }

    @Override
    public String description() {
        return "字符串转大写, 接收一个字符串参数\nupper(str)";
    }

    @Override
    public Value execute(Value[] args, IExecuteContext context) throws ExecuteException {
        if(args.length != 1){
            throw new ExecuteException("upper function expect 1 arguments, but " + args.length);
        }
        Value arg = args[0];
        if(arg.getType() != ValueType.STRING){
            throw new ExecuteException("upper function argument's type must string");
        }
        return Value.of(arg.cast(String.class).toUpperCase());
    }
}
