package sch.frog.lab.lang.fun;

import sch.frog.lab.lang.exception.ExecuteException;
import sch.frog.lab.lang.semantic.IExecuteContext;
import sch.frog.lab.lang.value.Value;

public interface IFunction {

    String name();

    String description();

    Value execute(Value[] args, IExecuteContext context) throws ExecuteException;

}
