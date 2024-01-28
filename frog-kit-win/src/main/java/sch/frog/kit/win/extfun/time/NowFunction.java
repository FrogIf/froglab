package sch.frog.kit.win.extfun.time;

import sch.frog.kit.lang.fun.IFunction;
import sch.frog.kit.lang.exception.ExecuteException;
import sch.frog.kit.lang.semantic.IExecuteContext;
import sch.frog.kit.lang.value.Value;

public class NowFunction implements IFunction {
    @Override
    public String name() {
        return "now";
    }

    @Override
    public String description() {
        return "返回当前时间戳,空参";
    }

    @Override
    public Value execute(Value[] args, IExecuteContext context) throws ExecuteException {
        if(args.length != 0){
            throw new ExecuteException("now function must 0 arguments, but " + args.length);
        }
        return Value.of(System.currentTimeMillis());
    }
}
