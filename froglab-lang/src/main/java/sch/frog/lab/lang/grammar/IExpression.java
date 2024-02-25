package sch.frog.lab.lang.grammar;

import sch.frog.lab.lang.exception.ExecuteException;
import sch.frog.lab.lang.semantic.IExecuteContext;
import sch.frog.lab.lang.semantic.Reference;
import sch.frog.lab.lang.value.Value;

public interface IExpression extends IStatement{

    Reference evaluate(IExecuteContext context) throws ExecuteException;

}
