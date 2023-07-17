package sch.frog.kit.core.execute;

import sch.frog.kit.core.fun.IFunction;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class AppContext {

    private final Map<String, IFunction> functionMap = new HashMap<>();

    public IFunction getFunction(String name){
        return functionMap.get(name);
    }

    public void addFunction(IFunction function){
        functionMap.putIfAbsent(function.name(), function);
    }

    public Collection<IFunction> getFunctions(){
        return functionMap.values();
    }

    public boolean existFun(String key) {
        return functionMap.containsKey(key);
    }
}
