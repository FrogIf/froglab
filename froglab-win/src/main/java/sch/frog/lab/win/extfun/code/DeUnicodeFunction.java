package sch.frog.lab.win.extfun.code;

import sch.frog.lab.lang.fun.IFunction;
import sch.frog.lab.lang.exception.ExecuteException;
import sch.frog.lab.lang.semantic.IExecuteContext;
import sch.frog.lab.lang.value.Value;
import sch.frog.lab.lang.value.ValueType;

public class DeUnicodeFunction implements IFunction {
    @Override
    public String name() {
        return "en_unicode";
    }

    @Override
    public String description() {
        return "将指定字符串转为unicode码";
    }

    @Override
    public Value execute(Value[] args, IExecuteContext context) throws ExecuteException {
        if(args.length != 1){
            throw new ExecuteException("en_unicode function expect 1 arguments but " + args.length);
        }
        Value arg = args[0];
        if(arg.getType() != ValueType.STRING){
            throw new ExecuteException("en_unicode function argument's type must string");
        }
        return Value.of(encodeUnicode(arg.cast(String.class)));
    }

    private String encodeUnicode(String origin){
        int len = origin.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char ch = origin.charAt(i);
            if (ch <= 127) {
                sb.append(ch);
            } else {
                String hex = Integer.toHexString(ch);
                sb.append("\\u");
                if (hex.length() < 4) {
                    sb.append("0000", hex.length(), 4);
                }
                sb.append(hex);
            }
        }
        return sb.toString();
    }
}
