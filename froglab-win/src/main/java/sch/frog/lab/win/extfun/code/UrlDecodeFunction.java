package sch.frog.lab.win.extfun.code;

import sch.frog.lab.lang.fun.IFunction;
import sch.frog.lab.lang.exception.ExecuteException;
import sch.frog.lab.lang.semantic.IExecuteContext;
import sch.frog.lab.lang.value.Value;
import sch.frog.lab.lang.value.ValueType;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class UrlDecodeFunction implements IFunction {
    @Override
    public String name() {
        return "url_decode";
    }

    @Override
    public String description() {
        return "将url编码后的字符串还原为普通字符串";
    }

    @Override
    public Value execute(Value[] args, IExecuteContext context) throws ExecuteException {
        if(args.length == 1){
            throw new ExecuteException("url_decode expect 1 arguments, but " + args.length);
        }
        Value arg = args[0];
        if(arg.getType() != ValueType.STRING){
            throw new ExecuteException("url_decode argument type must string, but " + arg.getType());
        }
        return Value.of(URLDecoder.decode(arg.cast(String.class), StandardCharsets.UTF_8));
    }
}
