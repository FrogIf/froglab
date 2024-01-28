package sch.frog.lab.lang.fun.str;

import io.github.frogif.calculator.number.impl.IntegerNumber;
import sch.frog.lab.lang.fun.IFunction;
import sch.frog.lab.lang.exception.ExecuteException;
import sch.frog.lab.lang.semantic.IExecuteContext;
import sch.frog.lab.lang.value.Value;
import sch.frog.lab.lang.value.ValueType;

public class IndexOfFunction implements IFunction {
    @Override
    public String name() {
        return "indexOf";
    }

    @Override
    public String description() {
        return "获取字符串中从指定位置开始匹配的第一个位置\nindexOf(text, searchStr, [fromIndex])";
    }

    @Override
    public Value execute(Value[] args, IExecuteContext context) throws ExecuteException {
        if(args.length != 3 && args.length != 2){
            throw new ExecuteException("indexOf function expect 2 or 3 arguments, but " + args.length);
        }
        for (int i = 0; i < 2; i++){
            Value arg = args[i];
            if(arg.getType() != ValueType.STRING){
                throw new ExecuteException("indexOf function " + (i + 1) + " argument expect string type, but " + arg.getType());
            }
        }
        String text = args[0].cast(String.class);
        String searchStr = args[1].cast(String.class);
        int start = 0;
        if(args.length == 3){
            start = args[2].cast(IntegerNumber.class).intValue();
        }

        return Value.of(text.indexOf(searchStr, start));
    }
}
