package sch.frog.kit.win.extfun.time;

import sch.frog.kit.lang.fun.IFunction;
import sch.frog.kit.lang.exception.ExecuteException;
import sch.frog.kit.lang.semantic.IExecuteContext;
import sch.frog.kit.lang.value.Value;
import sch.frog.kit.lang.value.ValueType;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFunction implements IFunction {
    @Override
    public String name() {
        return "date";
    }

    @Override
    public String description() {
        return "将时间戳格式化输出\n接受0-2个参数\n0个参数则对当前时间格式化为yyyy-MM-dd HH:mm:ss输出\n1个参数对输入的时间戳按照yyyy-MM-dd HH:mm:ss进行输出\n2个参数, 则第二个参数是格式化的样式";
    }

    @Override
    public Value execute(Value[] args, IExecuteContext context) throws ExecuteException {
        if(args.length > 3){
            throw new ExecuteException("date function at most 3 arguments, but " + args.length);
        }
        if(args.length == 0){
            return Value.of(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        }
        Value first = args[0];
        if(first.getType() != ValueType.NUMBER){
            throw new ExecuteException("1 argument type expect NUMBER but " + first.getType());
        }
        long timeMillis = first.cast(long.class);
        Date date = new Date();
        if(args.length == 1){
            date.setTime(timeMillis);
            return Value.of(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
        }else{
            date.setTime(timeMillis);
            Value pattern = args[1];
            if(pattern.getType() != ValueType.STRING){
                throw new ExecuteException("2 argument type expect string but " + pattern.getType());
            }
            return Value.of(new SimpleDateFormat(pattern.cast(String.class)).format(date));
        }
    }
}
