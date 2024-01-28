package sch.frog.lab.lang.fun.general;

import sch.frog.lab.lang.fun.IFunction;
import sch.frog.lab.lang.exception.ExecuteException;
import sch.frog.lab.lang.semantic.IExecuteContext;
import sch.frog.lab.lang.value.VList;
import sch.frog.lab.lang.value.Value;
import sch.frog.lab.lang.value.ValueType;

public class LengthFunction implements IFunction {


    @Override
    public String name() {
        return "length";
    }

    @Override
    public String description() {
        return "获取长度, 对于数组返回数组元素个数, 对于字符串返回字符串字符个数\nlength(obj)";
    }

    @Override
    public Value execute(Value[] args, IExecuteContext context) throws ExecuteException {
        if(args.length != 1){
            throw new ExecuteException("length function expect 1 arguments, but " + args.length);
        }
        Value val = args[0];
        if(val.getType() == ValueType.STRING){
            return Value.of(val.cast(String.class).length());
        }else if(val.getType() == ValueType.LIST){
            return Value.of(val.cast(VList.class).size());
        }else{
            throw new ExecuteException("unsupported value type : " + val.getType() + " for length function");
        }
    }
}
