package sch.frog.lab.lang.fun.general;

import sch.frog.lab.lang.fun.IFunction;
import sch.frog.lab.lang.exception.ExecuteException;
import sch.frog.lab.lang.semantic.IExecuteContext;
import sch.frog.lab.lang.value.Value;

public class UUIDFunction implements IFunction {
    @Override
    public String name() {
        return "uuid";
    }

    @Override
    public String description() {
        return "生成uuid,无参";
    }

    @Override
    public Value execute(Value[] args, IExecuteContext context) throws ExecuteException {
        return new Value(java.util.UUID.randomUUID().toString());
    }
}
