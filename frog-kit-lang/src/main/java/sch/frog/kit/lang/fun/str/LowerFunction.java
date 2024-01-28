package sch.frog.kit.lang.fun.str;

import sch.frog.kit.lang.fun.IFunction;
import sch.frog.kit.lang.exception.ExecuteException;
import sch.frog.kit.lang.semantic.IExecuteContext;
import sch.frog.kit.lang.value.Value;
import sch.frog.kit.lang.value.ValueType;

/**
 * 字符串转小写
 */
public class LowerFunction implements IFunction {
    
    @Override
    public String name() {
        return "lower";
    }

    @Override
    public String description() {
        return "字符串转小写, 接收一个字符串参数\nlower(str)";
    }

    @Override
    public Value execute(Value[] args, IExecuteContext context) throws ExecuteException {
        if(args.length != 1){
            throw new ExecuteException("lower function expect 1 arguments, but " + args.length);
        }
        Value arg = args[0];
        if(arg.getType() != ValueType.STRING){
            throw new ExecuteException("lower function argument's type must string");
        }
        return Value.of(arg.cast(String.class).toLowerCase());
    }
}
