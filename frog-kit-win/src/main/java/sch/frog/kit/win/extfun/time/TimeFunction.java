package sch.frog.kit.win.extfun.time;

import io.github.frogif.calculator.number.impl.RationalNumber;
import sch.frog.kit.lang.fun.IFunction;
import sch.frog.kit.lang.exception.ExecuteException;
import sch.frog.kit.lang.semantic.IExecuteContext;
import sch.frog.kit.lang.value.Value;
import sch.frog.kit.lang.value.ValueType;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class TimeFunction implements IFunction {
    @Override
    public String name() {
        return "time";
    }

    @Override
    public String description() {
        return "将时间转化为时间戳\n接受0-2个参数\n0个参数时, 和now函数相同\n1个参数时,对给定的字符串按照yyyy-MM-dd HH:mm:ss进行解析\n2个参数时,对跟定的字符串按照第二个参数指定的模式进行解析";
    }

    @Override
    public Value execute(Value[] args, IExecuteContext context) throws ExecuteException {
        if(args.length > 2){
            throw new ExecuteException("timestamp at most have 2 argument, but " + args.length);
        }
        if(args.length == 0){
            return Value.of(System.currentTimeMillis());
        }
        Value date = args[0];
        if(date.getType() != ValueType.STRING){
            throw new ExecuteException("time first argument must string, but " + date.getType());
        }
        String dateStr = date.cast(String.class);
        String pattern = "yyyy-MM-dd HH:mm:ss";
        if(args.length == 2){
            Value patternVal = args[1];
            if(patternVal.getType() != ValueType.STRING){
                throw new ExecuteException("time second argument must string, but " + date.getType());
            }
            pattern = patternVal.cast(String.class);
        }
        RationalNumber num;
        try {
            num = new RationalNumber(String.valueOf(new SimpleDateFormat(pattern).parse(dateStr).getTime()));
        } catch (ParseException e) {
            throw new ExecuteException(dateStr + " convert to timestamp failed with pattern " + pattern);
        }
        return Value.of(num);
    }
}
