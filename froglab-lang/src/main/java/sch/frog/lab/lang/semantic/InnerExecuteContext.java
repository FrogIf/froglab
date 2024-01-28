package sch.frog.lab.lang.semantic;

import sch.frog.lab.lang.exception.ExecuteError;
import sch.frog.lab.lang.exception.ExecuteException;
import sch.frog.lab.lang.value.Value;

import java.util.HashMap;

public class InnerExecuteContext implements IExecuteContext{

    private final IExecuteContext parent;

    private final HashMap<String, Value> variableMap = new HashMap<>();

    private Executor executor;

    public InnerExecuteContext(IExecuteContext parent) {
        this.parent = parent;
    }

    public InnerExecuteContext(Executor executor) {
        this.executor = executor;
        this.parent = null;
    }

    @Override
    public Executor executor() {
        if(parent != null){ return parent.executor(); }
        return executor;
    }

    @Override
    public Value getVariableValue(String name) throws ExecuteException {
        Value val = variableMap.get(name);
        if(val != null){ return val; }

        if(parent == null){
            throw new ExecuteException("variable : " + name + " is not define");
        }
        return parent.getVariableValue(name);
    }

    @Override
    public void setVariable(String name, Value value) throws ExecuteException {
        if(value == null){ throw new ExecuteError("value can't be null"); }

        if(variableMap.containsKey(name)){
            variableMap.put(name, value);
        }else{
            if(parent == null){
                throw new ExecuteException("variable " + name + " is not define");
            }
            parent.setVariable(name, value);
        }
    }

    @Override
    public void defGlobalVariable(String name, Value value) throws ExecuteException {
        if(value == null){ throw new ExecuteError("value can't be null"); }

        if(parent == null){
            if(variableMap.containsKey(name)){
                throw new ExecuteException("variable : " + name + " already define in this scope");
            }
            variableMap.put(name, value);
        }else{
            parent.defGlobalVariable(name, value);
        }
    }

    @Override
    public void defLocalVariable(String name, Value value) throws ExecuteException {
        if(value == null){ throw new ExecuteError("value can't be null"); }

        if(variableMap.containsKey(name)){
            throw new ExecuteException("variable : " + name + " already define in this scope");
        }
        variableMap.put(name, value);
    }
}
