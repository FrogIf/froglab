package sch.frog.kit.lang.execute;

import sch.frog.kit.lang.AppContext;
import sch.frog.kit.lang.fun.IFunction;
import sch.frog.kit.lang.value.Value;

import java.util.Collection;

public interface ISession {

    IOutput getOutput();

    AppContext getAppContext();

    Value getVariable(String key);

    void setVariable(String key, Value value);

    void addVariable(String key, Value value);

    void setOutput(IOutput output);

    Collection<IFunction> getFunctions();

    Collection<IFunction> getFunctions(String packName);

}
