package sch.frog.kit.core.fun.lang;

import sch.frog.kit.core.exception.ExecuteException;
import sch.frog.kit.core.execute.ISession;
import sch.frog.kit.core.fun.AbstractGeneralFunction;
import sch.frog.kit.core.value.Value;
import sch.frog.kit.core.value.ValueType;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DATE extends AbstractGeneralFunction {

    @Override
    public String name() {
        return "date";
    }

    @Override
    public String description() {
        return null;
    }

    @Override
    protected Value doExec(Value[] args, ISession session) {
        if(args.length == 0){
            return new Value(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        }
        Value first = args[0];
        if(first.getType() != ValueType.NUMBER){
            throw new ExecuteException("first argument type expect NUMBER but " + first.getType().name());
        }
        long timeMillis = first.to(long.class);
        Date date = new Date();
        if(args.length == 1){
            date.setTime(timeMillis);
            String result = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
            return new Value(result);
        }else{
            date.setTime(timeMillis);
            String result = new SimpleDateFormat(args[1].toString()).format(date);
            return new Value(result);
        }
    }
}
