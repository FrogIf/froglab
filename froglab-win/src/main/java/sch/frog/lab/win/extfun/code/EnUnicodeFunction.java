package sch.frog.lab.win.extfun.code;

import sch.frog.lab.lang.fun.IFunction;
import sch.frog.lab.lang.exception.ExecuteException;
import sch.frog.lab.lang.semantic.IExecuteContext;
import sch.frog.lab.lang.value.Value;
import sch.frog.lab.lang.value.ValueType;

public class EnUnicodeFunction implements IFunction {
    @Override
    public String name() {
        return "de_unicode";
    }

    @Override
    public String description() {
        return "将unicode码转为字符串";
    }

    @Override
    public Value execute(Value[] args, IExecuteContext context) throws ExecuteException {
        if(args.length != 1){
            throw new ExecuteException("de_unicode function expect 1 arguments but " + args.length);
        }
        Value arg = args[0];
        if(arg.getType() != ValueType.STRING){
            throw new ExecuteException("de_unicode function argument's type must string");
        }
        return Value.of(decodeUnicode(arg.cast(String.class)));
    }

    private String decodeUnicode(String origin){
        int len = origin.length();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            char ch = origin.charAt(i);
            if (ch == '\\' && i + 5 < len && origin.charAt(i + 1) == 'u') {
                String hex = origin.substring(i + 2, i + 6);
                sb.append((char) Integer.parseInt(hex, 16));
                i += 5;
            } else {
                sb.append(ch);
            }
        }
        return sb.toString();
    }
}
