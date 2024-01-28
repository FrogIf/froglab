package sch.frog.kit.lang.grammar;

import sch.frog.kit.lang.exception.ExecuteException;
import sch.frog.kit.lang.semantic.IExecuteContext;
import sch.frog.kit.lang.value.Value;

public interface IExpression extends IStatement{

    Value evaluate(IExecuteContext context) throws ExecuteException;

}
