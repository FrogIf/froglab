package sch.frog.lab.win.extfun.code;

import sch.frog.lab.lang.fun.IFunction;
import sch.frog.lab.lang.exception.ExecuteException;
import sch.frog.lab.lang.semantic.IExecuteContext;
import sch.frog.lab.lang.value.Value;
import sch.frog.lab.lang.value.ValueType;

import java.nio.charset.StandardCharsets;

public class DeHexFunction implements IFunction {
    @Override
    public String name() {
        return "de_hex";
    }

    @Override
    public String description() {
        return "将hex转为字符串";
    }

    @Override
    public Value execute(Value[] args, IExecuteContext context) throws ExecuteException {
        if(args.length != 1){
            throw new ExecuteException("de_hex expect 1 arguments but " + args.length);
        }
        Value arg = args[0];
        if(arg.getType() != ValueType.STRING){
            throw new ExecuteException("de_hex argument type must string, but " + arg.getType());
        }
        String content = arg.cast(String.class);
        content = content.replaceAll(" ", "");
        int len = content.length();
        byte[] bytes = new byte[len >> 1];
        for(int j = 0, i = 0; j < bytes.length; i += 2, j++){
            bytes[j] = (byte)Integer.parseInt(content.substring(i, i + 2), 16);
        }
        return Value.of(new String(bytes, StandardCharsets.UTF_8));
    }
}
