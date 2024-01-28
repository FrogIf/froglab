package sch.frog.lab.win.extfun.code;

import sch.frog.lab.lang.fun.IFunction;
import sch.frog.lab.lang.exception.ExecuteException;
import sch.frog.lab.lang.semantic.IExecuteContext;
import sch.frog.lab.lang.value.Value;
import sch.frog.lab.lang.value.ValueType;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class UrlEncodeFunction implements IFunction {
    @Override
    public String name() {
        return "url_encode";
    }

    @Override
    public String description() {
        return "将字符串按照url规则进行编码";
    }

    @Override
    public Value execute(Value[] args, IExecuteContext context) throws ExecuteException {
        if(args.length == 1){
            throw new ExecuteException("url_encode expect 1 arguments, but " + args.length);
        }
        Value arg = args[0];
        if(arg.getType() != ValueType.STRING){
            throw new ExecuteException("url_encode argument type must string, but " + arg.getType());
        }
        return Value.of(URLEncoder.encode(arg.cast(String.class), StandardCharsets.UTF_8));
    }
}
