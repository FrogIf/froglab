package sch.frog.kit.core.execute;

import sch.frog.kit.core.value.Value;

public interface ISession {

    AppContext getAppContext();

    Value getVariable(String key);

    boolean exist(String key);

    void setValue(String key, Value value);

    void addValue(String key, Value value);
}
