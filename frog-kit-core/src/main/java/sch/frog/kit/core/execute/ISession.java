package sch.frog.kit.core.execute;

import sch.frog.kit.core.value.Value;

public interface ISession {

    IOutput getOutput();

    AppContext getAppContext();

    Value getVariable(String key);

    void setValue(String key, Value value);

    void addValue(String key, Value value);

}
