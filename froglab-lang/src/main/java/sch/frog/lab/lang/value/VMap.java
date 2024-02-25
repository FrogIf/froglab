package sch.frog.lab.lang.value;

import sch.frog.lab.lang.exception.ExecuteException;
import sch.frog.lab.lang.fun.AbstractFunction;
import sch.frog.lab.lang.fun.IFunction;
import sch.frog.lab.lang.semantic.IExecuteContext;

public interface VMap {

    Value put(String key, Value value);

    Value get(String key);

    Value keyList();

    boolean existKey(String key);

    default IFunction getFunction(String name) throws ExecuteException {
        if("get".equals(name)){
            return new AbstractFunction() {
                @Override
                public Value execute(Value[] args, IExecuteContext context) throws ExecuteException {
                    if(args.length != 1){
                        throw new ExecuteException("get function expect 1 arguments, but " + args.length);
                    }
                    Value val = args[0];
                    return get(val.cast(String.class));
                }
            };
        }else if("put".equals(name)){
            return new AbstractFunction() {
                @Override
                public Value execute(Value[] args, IExecuteContext context) throws ExecuteException {
                    if(args.length != 2){
                        throw new ExecuteException("put function expect 2 arguments, but " + args.length);
                    }
                    Value key = args[0];
                    Value val = args[1];
                    return put(key.cast(String.class), val);
                }
            };
        }else if("keys".equals(name)){
            return new AbstractFunction() {
                @Override
                public Value execute(Value[] args, IExecuteContext context) throws ExecuteException {
                    if(args.length != 0){
                        throw new ExecuteException("keys function expect 0 arguments, but " + args.length);
                    }
                    return keyList();
                }
            };
        }
        throw new ExecuteException(name + " function is undefine");
    }
}
