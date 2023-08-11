package sch.frog.kit.lang;

import sch.frog.kit.lang.fun.IFunction;
import sch.frog.kit.lang.value.VMap;
import sch.frog.kit.lang.value.Value;
import sch.frog.kit.lang.value.ValueType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class AppContext {

    private final ConcurrentHashMap<String, Value> variableMap = new ConcurrentHashMap<>();

    public IFunction getFunction(String name){
        Value val = variableMap.get(name);
        if(val != null && val.getType() == ValueType.FUNCTION){
            return val.cast(IFunction.class);
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
                functions.add(value.cast(IFunction.class));
            }
        }
        return functions;
    }

    public Collection<IFunction> getFunctions(String packName){
        ArrayList<IFunction> functions = new ArrayList<>();
        Value val = variableMap.get(packName);
        if(val == null){ return functions; }
        if(val.getType() == ValueType.OBJECT){
            VMap obj = val.cast(VMap.class);
            for (Value value : obj.values()) {
                if(value.getType() == ValueType.FUNCTION){
                    functions.add(value.cast(IFunction.class));
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
