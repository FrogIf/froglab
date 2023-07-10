package sch.frog.kit.core.fun.lang;

import sch.frog.kit.core.exception.ExecuteException;
import sch.frog.kit.core.execute.ISession;
import sch.frog.kit.core.fun.AbstractFunction;
import sch.frog.kit.core.json.JsonArray;
import sch.frog.kit.core.json.JsonObject;
import sch.frog.kit.core.value.Locator;
import sch.frog.kit.core.value.Value;
import sch.frog.kit.core.value.ValueType;

public class GET extends AbstractFunction {

    @Override
    public String name() {
        return "get";
    }

    @Override
    public String description() {
        return "Value get(string key)";
    }

    @Override
    public Value execute(Value[] args, ISession session) {
        if(args.length != 1){
            throw new ExecuteException("set function must has 1 arguments");
        }
        Value arg = args[0];
        if(arg.getType() != ValueType.VARIABLE){
            throw new ExecuteException("set function first argument must string type");
        }
        Locator locator = arg.to(Locator.class);
        if(locator.isIndex()){
            throw new ExecuteException("index can't as variable name");
        }
        if(!session.exist(locator.getKey())){
            throw new ExecuteException("variable " + locator.getKey() + " is not exist");
        }
        Value cursorVal = session.getVariable(locator.getKey());
        if(cursorVal == null){
            throw new NullPointerException("variable : " + locator.getKey() + "is null");
        }
        Locator next = locator.next();
        StringBuilder path = new StringBuilder(locator.getKey());
        while(next != null){
            if(next.isIndex()){
                int index = next.getIndex();
                path.append(".#").append(index);
                JsonArray array = cursorVal.to(JsonArray.class);
                if(array == null){
                    throw new ExecuteException(path + " is null");
                }
                if(array.size() <= index){
                    throw new IndexOutOfBoundsException(path + " size is " + array.size() + ", but index is " + index);
                }
                cursorVal = Value.of(array.get(index));
            }else{
                String key = next.getKey();
                path.append(".@").append(key);
                JsonObject jsonObject = cursorVal.to(JsonObject.class);
                if(jsonObject == null){
                    throw new ExecuteException(path + " is null");
                }
                cursorVal = Value.of(jsonObject.get(next.getKey()));
            }
            next = next.next();
        }
        return cursorVal;
    }
}
