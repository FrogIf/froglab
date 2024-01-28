package sch.frog.kit.win.extfun.code;

import sch.frog.kit.lang.fun.IFunction;
import sch.frog.kit.lang.exception.ExecuteException;
import sch.frog.kit.lang.semantic.IExecuteContext;
import sch.frog.kit.lang.value.Value;
import sch.frog.kit.lang.value.ValueType;

import java.nio.charset.StandardCharsets;

public class EnHexFunction implements IFunction {
    @Override
    public String name() {
        return "en_hex";
    }

    @Override
    public String description() {
        return "将字符串转为hex";
    }

    @Override
    public Value execute(Value[] args, IExecuteContext context) throws ExecuteException {
        if(args.length != 1){
            throw new ExecuteException("en_hex expect 1 arguments but " + args.length);
        }
        Value arg = args[0];
        if(arg.getType() != ValueType.STRING){
            throw new ExecuteException("en_hex argument type must string, but " + arg.getType());
        }
        byte[] bytes = arg.cast(String.class).getBytes(StandardCharsets.UTF_8);
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
            sb.append(' ');
        }
        return Value.of(sb.toString());
    }
}
