package sch.frog.lab.lang.value;

import io.github.frogif.calculator.number.impl.IntegerNumber;
import sch.frog.lab.lang.exception.ExecuteException;
import sch.frog.lab.lang.fun.AbstractFunction;
import sch.frog.lab.lang.fun.IFunction;
import sch.frog.lab.lang.semantic.IExecuteContext;

public interface VList {
    boolean add(Value val);

    int size();

    Value get(int index);

    Value set(int i, Value val);

    default IFunction getFunction(String name) throws ExecuteException {
        if("get".equals(name)){
            return new AbstractFunction() {
                @Override
                public Value execute(Value[] args, IExecuteContext context) throws ExecuteException {
                    if(args.length != 1){
                        throw new ExecuteException("get function expect 1 arguments, but " + args.length);
                    }
                    Value val = args[0];
                    return get(val.cast(IntegerNumber.class).intValue());
                }
            };
        }else if("set".equals(name)){
            return new AbstractFunction() {
                @Override
                public Value execute(Value[] args, IExecuteContext context) throws ExecuteException {
                    if(args.length != 2){
                        throw new ExecuteException("set function expect 2 arguments, but " + args.length);
                    }
                    Value key = args[0];
                    Value val = args[1];
                    return set(key.cast(IntegerNumber.class).intValue(), val);
                }
            };
        }else if("length".equals(name)){
            return new AbstractFunction() {
                @Override
                public Value execute(Value[] args, IExecuteContext context) throws ExecuteException {
                    if(args.length != 0){
                        throw new ExecuteException("keys function expect 0 arguments, but " + args.length);
                    }
                    return Value.of(size());
                }
            };
        }else if("add".equals(name)){
            return new AbstractFunction() {
                @Override
                public Value execute(Value[] args, IExecuteContext context) throws ExecuteException {
                    if(args.length != 1){
                        throw new ExecuteException("add function expect 1 arguments, but " + args.length);
                    }
                    Value val = args[0];
                    return Value.of(add(val));
                }
            };
        }
        throw new ExecuteException(name + " function is undefine");
    }
}
