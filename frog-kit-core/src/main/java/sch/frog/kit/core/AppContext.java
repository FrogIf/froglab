package sch.frog.kit.core;

import sch.frog.kit.core.fun.IFunction;
import sch.frog.kit.core.value.VMap;
import sch.frog.kit.core.value.Value;
import sch.frog.kit.core.value.ValueType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class AppContext {

    private final ConcurrentHashMap<String, Value> variableMap = new ConcurrentHashMap<>();

    public IFunction getFunction(String name){
        Value val = variableMap.get(name);
        if(val != null && val.getType() == ValueType.FUNCTION){
            return val.to(IFunction.class);
        }
        return null;
    }

    void addFunction(IFunction function){
        variableMap.putIfAbsent(function.name(), Value.of(function));
    }

    void addVariable(String key, Value value){
        if(variableMap.containsKey(key)){
            throw new IllegalStateException(key + " has exist");
        }
        variableMap.put(key, value);
    }

    public Collection<IFunction> getFunctions(){
        ArrayList<IFunction> functions = new ArrayList<>();
        for (Value value : variableMap.values()) {
            if(value.getType() == ValueType.FUNCTION){
                functions.add(value.to(IFunction.class));
            }
        }
        return functions;
    }

    public Collection<IFunction> getFunctions(String packName){
        ArrayList<IFunction> functions = new ArrayList<>();
        Value val = variableMap.get(packName);
        if(val == null){ return functions; }
        if(val.getType() == ValueType.OBJECT){
            VMap obj = val.to(VMap.class);
            for (Value value : obj.values()) {
                if(value.getType() == ValueType.FUNCTION){
                    functions.add(value.to(IFunction.class));
                }
            }
        }
        return functions;
    }

    public Value getVariable(String key){
        return variableMap.get(key);
    }

    public boolean existVariable(String key) {
        return variableMap.containsKey(key);
    }
}
