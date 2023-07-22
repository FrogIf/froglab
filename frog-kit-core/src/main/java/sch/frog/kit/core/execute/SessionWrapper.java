package sch.frog.kit.core.execute;

import sch.frog.kit.core.AppContext;
import sch.frog.kit.core.value.Value;

public class SessionWrapper implements ISession{

    protected final ISession session;

    public SessionWrapper(ISession session) {
        this.session = session;
    }

    @Override
    public IOutput getOutput() {
        return session.getOutput();
    }

    @Override
    public AppContext getAppContext() {
        return session.getAppContext();
    }

    @Override
    public Value getVariable(String key) {
        return session.getVariable(key);
    }

    @Override
    public void setValue(String key, Value value) {
        session.setValue(key, value);
    }

    @Override
    public void addValue(String key, Value value) {
        session.addValue(key, value);
    }

    @Override
    public void setOutput(IOutput output) {
        session.setOutput(output);
    }

}
