package sch.frog.kit.lang.execute;

import sch.frog.kit.lang.AppContext;
import sch.frog.kit.lang.value.Value;

public interface IRuntimeContext {

    ISession getSession();

    AppContext getAppContext();

    IOutput getOutput();

    Value getVariable(String name);

    Value getSessionVariable(String name);

    void addSessionVariable(String name, Value val);

    void addLocalVariable(String name, Value val);

    void setSessionVariable(String name, Value val);

    void setVariable(String name, Value val);

}
