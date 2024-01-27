package sch.frog.kit.lang.parse.semantic;

import sch.frog.kit.lang.parse.exception.ExecuteException;
import sch.frog.kit.lang.value.Value;

public interface IExecuteContext {

    Executor executor();

    Value getVariableValue(String name) throws ExecuteException;

    void setVariable(String name, Value value) throws ExecuteException;

    void defGlobalVariable(String name, Value value) throws ExecuteException;

    void defLocalVariable(String name, Value value) throws ExecuteException;

}
