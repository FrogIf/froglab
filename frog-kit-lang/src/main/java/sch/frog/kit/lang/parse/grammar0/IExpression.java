package sch.frog.kit.lang.parse.grammar0;

import sch.frog.kit.lang.parse.exception.ExecuteException;
import sch.frog.kit.lang.parse.semantic.IExecuteContext;
import sch.frog.kit.lang.value.Value;

public interface IExpression extends IStatement{

    Value evaluate(IExecuteContext context) throws ExecuteException;

}
