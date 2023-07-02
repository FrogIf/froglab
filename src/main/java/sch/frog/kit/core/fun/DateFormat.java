package sch.frog.kit.core.fun;

import sch.frog.kit.core.AbstractFunction;
import sch.frog.kit.core.ISession;
import sch.frog.kit.core.Value;
import sch.frog.kit.core.ValueType;
import sch.frog.kit.core.exception.ExecuteException;
import sch.frog.kit.core.util.NumberUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormat extends AbstractFunction {


    @Override
    public String name() {
        return "formatDate";
    }

    @Override
    public String description() {
        return null;
    }

    @Override
    public Value execute(Value[] args, ISession session) throws ExecuteException {
        Value first = args[0];
        if(first.getType() != ValueType.NUMBER){
            throw new ExecuteException("first argument type expect NUMBER but " + first.getType().name());
        }
        if(!NumberUtil.isInteger(first.getOriginValue())){
            throw new ExecuteException("first argument is not a integer --> " + first.getOriginValue());
        }
        long timeMillis = Long.parseLong(first.getOriginValue());
        Date date = new Date();
        if(args.length == 1){
            date.setTime(timeMillis);
            String result = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
            return new Value(ValueType.STRING, result);
        }else{
            date.setTime(timeMillis);
            String result = new SimpleDateFormat(args[1].toString()).format(date);
            return new Value(ValueType.STRING, result);
        }
    }
}
