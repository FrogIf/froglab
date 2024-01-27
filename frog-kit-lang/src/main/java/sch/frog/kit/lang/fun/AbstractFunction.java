package sch.frog.kit.lang.fun;

import sch.frog.kit.lang.parse.exception.ExecuteException;
import sch.frog.kit.lang.parse.semantic.IExecuteContext;
import sch.frog.kit.lang.value.Value;

public abstract class AbstractFunction implements IFunction{

    @Override
    public Value execute(Value[] args, IExecuteContext context) throws ExecuteException {
        return null;
    }
}
