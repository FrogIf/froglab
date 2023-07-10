package sch.frog.kit.core.fun.lang;

import sch.frog.kit.core.exception.ExecuteException;
import sch.frog.kit.core.execute.ISession;
import sch.frog.kit.core.fun.AbstractFunction;
import sch.frog.kit.core.json.JsonArray;
import sch.frog.kit.core.json.JsonObject;
import sch.frog.kit.core.value.Locator;
import sch.frog.kit.core.value.Value;
import sch.frog.kit.core.value.ValueType;

public class SET extends AbstractFunction {
    @Override
    public String name() {
        return "set";
    }

    @Override
    public String description() {
        return null;
    }

    @Override
    public Value execute(Value[] args, ISession session) {
        if(args.length != 2){
            throw new ExecuteException("set function must has 2 arguments");
        }
        Value arg = args[0];
        if(arg.getType() != ValueType.SYMBOL){
            throw new ExecuteException("set function first argument must identifier type");
        }
        Locator locator = arg.to(Locator.class);
        if(locator.isIndex()){
            throw new ExecuteException("index can't as variable name");
        }
        if(!session.exist(locator.getKey())){
            throw new ExecuteException("variable " + locator.getKey() + " is not exist");
        }
        Locator cursor = locator.next();
        if(cursor == null){
            session.setValue(locator.getKey(), args[1]);
        }else{
            Value cursorVal = session.getVariable(locator.getKey());
            if(cursorVal == null){
                throw new NullPointerException("variable : " + locator.getKey() + "is null");
            }
            StringBuilder path = new StringBuilder();
            while(cursor != null){
                Locator next = cursor.next();
                if(cursor.isIndex()){
                    int index = cursor.getIndex();
                    path.append(".#").append(index);
                    JsonArray array = cursorVal.to(JsonArray.class);
                    if(array == null){
                        throw new ExecuteException(path + " is undefine");
                    }
                    if(array.size() <= index){
                        throw new IndexOutOfBoundsException(path + " size is " + array.size() + ", but index is " + index);
                    }
                    if(next == null){
                        array.set(index, args[1]);
                    }else{
                        cursorVal = Value.of(array.get(index));
                    }
                }else{
                    String key = cursor.getKey();
                    path.append(".@").append(key);
                    JsonObject jsonObject = cursorVal.to(JsonObject.class);
                    if(jsonObject == null){
                        throw new ExecuteException(path + " is null");
                    }
                    if(!jsonObject.containsKey(key)){
                        throw new ExecuteException(key + " not exist");
                    }
                    if(next == null){
                        jsonObject.put(key, args[1]);
                    }else{
                        cursorVal = Value.of(jsonObject.get(next.getKey()));
                    }
                }
                cursor = next;
            }
        }
        return Value.VOID;
    }
}
