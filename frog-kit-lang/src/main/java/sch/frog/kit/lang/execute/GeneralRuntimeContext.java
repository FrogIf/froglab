package sch.frog.kit.lang.execute;

import sch.frog.kit.lang.AppContext;
import sch.frog.kit.lang.exception.ExecuteException;
import sch.frog.kit.lang.value.Value;

import java.util.HashMap;
import java.util.Map;

public class GeneralRuntimeContext implements IRuntimeContext {

    private final Map<String, Value> variableMap = new HashMap<>();

    private final ISession session;

    private final AppContext appContext;

    private final IRuntimeContext parent;

    public GeneralRuntimeContext(AppContext appContext, ISession session) {
        this.session = session;
        this.appContext = appContext;
        this.parent = null;
    }

    public GeneralRuntimeContext(IRuntimeContext parent) {
        this.session = parent.getSession();
        this.appContext = parent.getAppContext();
        this.parent = parent;
    }

    @Override
    public ISession getSession() {
        return session;
    }

    @Override
    public AppContext getAppContext() {
        return appContext;
    }

    @Override
    public IOutput getOutput() {
        return session.getOutput();
    }

    @Override
    public Value getVariable(String name) {
        Value val = this.variableMap.get(name);
        if(val == null){
            if(parent != null){
                return parent.getVariable(name);
            }else{
                return session.getVariable(name);
            }
        } else{
            return val;
        }
    }

    @Override
    public Value getSessionVariable(String name) {
        return session.getVariable(name);
    }

    @Override
    public void setSessionVariable(String name, Value val) {
        session.setVariable(name, val);
    }

    @Override
    public void setVariable(String name, Value val) {
        if(val == null){ throw new ExecuteException("value can't be null"); }
        if(this.variableMap.containsKey(name)){
            this.variableMap.put(name, val);
        } else if (parent != null) {
            parent.setVariable(name, val);
        } else{
            session.setVariable(name, val);
        }
    }

    @Override
    public void addSessionVariable(String name, Value val) {
        session.addVariable(name, val);
    }

    @Override
    public void addLocalVariable(String name, Value val) {
        if(val == null){ throw new ExecuteException("value can't be null"); }
        if(this.variableMap.containsKey(name)){
            throw new ExecuteException(name + " has defined");
        }
        variableMap.put(name, val);
    }
}
