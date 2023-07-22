package sch.frog.kit.core.execute;

import sch.frog.kit.core.exception.ExecuteException;
import sch.frog.kit.core.fun.IFunction;
import sch.frog.kit.core.value.Value;

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
        return null;
    }

    @Override
    public void setValue(String key, Value value){
        if(value == null){ throw new ExecuteException("value can't be null"); }
        if(!variableMap.containsKey(key)){
            throw new ExecuteException("variable " + key + " not exist");
        }
        variableMap.put(key, value);
    }

    @Override
    public void addValue(String key, Value value) {
        if(value == null){ throw new ExecuteException("value can't be null"); }
        if(this.variableMap.containsKey(key)){
            throw new ExecuteException(key + " has defined");
        }
        if(this.context.existFun(key)){
            throw new ExecuteException(key + " has defined as function");
        }
        variableMap.put(key, value);
    }

    @Override
    public void setOutput(IOutput output) {
        this.output = output;
    }
}
