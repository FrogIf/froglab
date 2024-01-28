package sch.frog.lab.lang.grammar;

import sch.frog.lab.lang.exception.ExecuteException;
import sch.frog.lab.lang.semantic.IExecuteContext;
import sch.frog.lab.lang.value.Value;

public interface IExpression extends IStatement{

    Value evaluate(IExecuteContext context) throws ExecuteException;

}
