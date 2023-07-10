package sch.frog.kit.core.execute;

import sch.frog.kit.core.exception.ExecuteException;
import sch.frog.kit.core.value.Value;

import java.util.HashMap;
import java.util.Map;

public class GeneralSession implements ISession{

    private final AppContext context;

    private final Map<String, Value> variableMap = new HashMap<>();

    public GeneralSession(AppContext context) {
        this.context = context;
    }

    @Override
    public AppContext getAppContext() {
        return this.context;
    }

    @Override
    public Value getVariable(String key) {
        return variableMap.get(key);
    }

    @Override
    public boolean exist(String key) {
        return variableMap.containsKey(key);
    }

    @Override
    public void setValue(String key, Value value){
        if(!variableMap.containsKey(key)){
            throw new ExecuteException("variable " + key + " not exist");
        }
        variableMap.put(key, value);
    }

    @Override
    public void addValue(String key, Value value) {
        if(variableMap.containsKey(key)){
            throw new ExecuteException(key + " has defined");
        }
        variableMap.put(key, value);
    }
}
