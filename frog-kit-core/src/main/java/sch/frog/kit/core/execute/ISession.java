package sch.frog.kit.core.execute;

import sch.frog.kit.core.fun.IFunction;
import sch.frog.kit.core.value.Value;

public interface ISession {

    IOutput getOutput();

    AppContext getAppContext();

    Value getVariable(String key);

    boolean exist(String key);

    IFunction getFunction(String funName);

    void setValue(String key, Value value);

    void addValue(String key, Value value);
}
