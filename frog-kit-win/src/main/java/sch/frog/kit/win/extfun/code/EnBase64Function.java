package sch.frog.kit.win.extfun.code;

import sch.frog.kit.lang.fun.IFunction;
import sch.frog.kit.lang.exception.ExecuteException;
import sch.frog.kit.lang.semantic.IExecuteContext;
import sch.frog.kit.lang.value.Value;
import sch.frog.kit.lang.value.ValueType;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class EnBase64Function implements IFunction {
    @Override
    public String name() {
        return "en_base64";
    }

    @Override
    public String description() {
        return "将字符串转为base64编码";
    }

    @Override
    public Value execute(Value[] args, IExecuteContext context) throws ExecuteException {
        if(args.length != 1){
            throw new ExecuteException("en_base64 function expect 1 arguments but " + args.length);
        }
        Value arg = args[0];
        if(arg.getType() != ValueType.STRING){
            throw new ExecuteException("en_base64 function argument's type must string");
        }
        return Value.of(Base64.getEncoder().encodeToString(arg.cast(String.class).getBytes(StandardCharsets.UTF_8)));
    }
}
