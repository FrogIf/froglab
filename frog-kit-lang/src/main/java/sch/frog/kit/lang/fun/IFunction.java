package sch.frog.kit.lang.fun;

import sch.frog.kit.lang.exception.ExecuteException;
import sch.frog.kit.lang.semantic.IExecuteContext;
import sch.frog.kit.lang.value.Value;

public interface IFunction {

    String name();

    String description();

    Value execute(Value[] args, IExecuteContext context) throws ExecuteException;

}
