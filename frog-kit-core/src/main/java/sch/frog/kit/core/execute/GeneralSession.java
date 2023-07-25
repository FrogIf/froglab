package sch.frog.kit.core.execute;

import sch.frog.kit.core.AppContext;
import sch.frog.kit.core.exception.ExecuteException;
import sch.frog.kit.core.fun.IFunction;
import sch.frog.kit.core.value.VMap;
import sch.frog.kit.core.value.Value;
import sch.frog.kit.core.value.ValueType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class GeneralSession implements ISession{

    private final AppContext context;

    private IOutput output;

    private final Map<String, Value> variableMap = new HashMap<>();

    public GeneralSession(AppContext context) {
        this(context, new GeneralOutput());
    }

    public GeneralSession(AppContext context, IOutput output){
        this.context = context;
        this.output = output;
    }

    @Override
    public IOutput getOutput(){
        return output;
    }

    @Override
    public AppContext getAppContext() {
        return this.context;
    }

    @Override
    public Value getVariable(String key) {
        if(variableMap.containsKey(key)){
            return variableMap.get(key);
        }else{
            IFunction fun = context.getFunction(key);
            if(fun != null){
                return new Value(fun);
            }
        }
        return context.getVariable(key);
    }

    @Override
    public void setVariable(String key, Value value){
        if(value == null){ throw new ExecuteException("value can't be null"); }
        if(!variableMap.containsKey(key)){
            throw new ExecuteException("variable " + key + " not exist");
        }
        variableMap.put(key, value);
    }

    @Override
    public void addVariable(String key, Value value) {
        if(value == null){ throw new ExecuteException("value can't be null"); }
        if(this.variableMap.containsKey(key)){
            throw new ExecuteException(key + " has defined");
        }
        if(this.context.existVariable(key)){
            throw new ExecuteException(key + " has defined in app context");
        }
        variableMap.put(key, value);
    }

    @Override
    public void setOutput(IOutput output) {
        this.output = output;
    }

    @Override
    public Collection<IFunction> getFunctions() {
        Collection<IFunction> functions = context.getFunctions();
        ArrayList<IFunction> result = new ArrayList<>(functions);
        for (Value value : variableMap.values()) {
            if(value.getType() == ValueType.FUNCTION){
                result.add(value.to(IFunction.class));
            }
        }
        return result;
    }

    @Override
    public Collection<IFunction> getFunctions(String packName) {
        Collection<IFunction> functions = context.getFunctions(packName);
        ArrayList<IFunction> result = new ArrayList<>(functions);
        Value val = variableMap.get(packName);
        if(val == null){ return result; }
        if(val.getType() == ValueType.OBJECT){
            VMap obj = val.to(VMap.class);
            for (Value value : obj.values()) {
                if(value.getType() == ValueType.FUNCTION){
                    result.add(value.to(IFunction.class));
                }
            }
        }
        return result;
    }
}
