package sch.frog.kit.core;

import sch.frog.kit.core.fun.IFunction;
import sch.frog.kit.core.value.Value;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class AppContext {

    private final ConcurrentHashMap<String, IFunction> functionMap = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<String, Value> variableMap = new ConcurrentHashMap<>();

    public IFunction getFunction(String name){
        return functionMap.get(name);
    }

    void addFunction(IFunction function){
        functionMap.putIfAbsent(function.name(), function);
    }

    void addVariable(String key, Value value){
        if(variableMap.containsKey(key)){
            throw new IllegalStateException(key + " has exist");
        }
        variableMap.put(key, value);
    }

    public Collection<IFunction> getFunctions(){
        return functionMap.values();
    }

    public boolean existFun(String key) {
        return functionMap.containsKey(key);
    }

    public Value getVariable(String key){
        return variableMap.get(key);
    }
}
