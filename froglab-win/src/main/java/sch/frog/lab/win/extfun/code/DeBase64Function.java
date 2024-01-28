package sch.frog.lab.win.extfun.code;

import sch.frog.lab.lang.fun.IFunction;
import sch.frog.lab.lang.exception.ExecuteException;
import sch.frog.lab.lang.semantic.IExecuteContext;
import sch.frog.lab.lang.value.Value;
import sch.frog.lab.lang.value.ValueType;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class DeBase64Function implements IFunction {
    @Override
    public String name() {
        return "de_base64";
    }

    @Override
    public String description() {
        return "将base64转为字符串";
    }

    @Override
    public Value execute(Value[] args, IExecuteContext context) throws ExecuteException {
        if(args.length != 1){
            throw new ExecuteException("de_base64 function expect 1 arguments but " + args.length);
        }
        Value arg = args[0];
        if(arg.getType() != ValueType.STRING){
            throw new ExecuteException("de_base64 function argument's type must string");
        }
        return Value.of(new String(Base64.getDecoder().decode(arg.cast(String.class).getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8));
    }
}
