package sch.frog.kit.core.value;

import sch.frog.kit.core.exception.ExecuteException;
import sch.frog.kit.core.execute.ISession;

public class Locator {

    private final String key;

    private final int index;

    private Locator next;

    public Locator(String str) {
        this.key = str;
        this.index = -1;
    }

    public Locator(int index){
        this.index = index;
        this.key = null;
    }

    public Locator(String str, Locator next){
        this(str);
        this.next = next;
    }

    public Locator(int index, Locator next){
        this(index);
        this.next = next;
    }

    public String getKey(){
        return this.key;
    }

    public int getIndex(){
        return this.index;
    }

    public boolean isIndex(){
        return this.index >= 0;
    }

    public Locator next(){
        return this.next;
    }

    @Override
    public String toString() {
        if(this.index >= 0){
            return "#" + this.index + (this.next == null ? "" : ("." + this.next));
        }else{
            return "@" + this.key + (this.next == null ? "" : ("." + this.next));
        }
    }

    public Value get(ISession session){
        if(this.isIndex()){
            throw new ExecuteException("index can't as variable name");
        }
        if(session.getVariable(this.getKey()) == null){
            throw new ExecuteException("variable " + this.getKey() + " is not exist");
        }
        Value cursorVal = session.getVariable(this.getKey());
        if(cursorVal == null){
            throw new NullPointerException("variable : " + this.getKey() + "is null");
        }
        Locator next = this.next();
        StringBuilder path = new StringBuilder(this.getKey());
        while(next != null){
            if(next.isIndex()){
                int index = next.getIndex();
                path.append(".#").append(index);
                VList array = cursorVal.to(VList.class);
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
                VMap jsonObject = cursorVal.to(VMap.class);
                if(jsonObject == null){
                    throw new ExecuteException(path + " is null");
                }
                cursorVal = Value.of(jsonObject.get(next.getKey()));
            }
            next = next.next();
        }
        return cursorVal;
    }

    public void set(Value target, Value val){
        Locator cursor = this;
        Value cursorVal = target;
        StringBuilder path = new StringBuilder();
        while(cursor != null){
            Locator next = cursor.next();
            if(cursor.isIndex()){
                int index = cursor.getIndex();
                path.append(".#").append(index);
                VList array = cursorVal.to(VList.class);
                if(array == null){
                    throw new ExecuteException(path + " is undefine");
                }
                if(array.size() <= index){
                    throw new IndexOutOfBoundsException(path + " size is " + array.size() + ", but index is " + index);
                }
                if(next == null){
                    array.set(index, val);
                }else{
                    cursorVal = Value.of(array.get(index));
                }
            }else{
                String key = cursor.getKey();
                path.append(".@").append(key);
                VMap jsonObject = cursorVal.to(VMap.class);
                if(jsonObject == null){
                    throw new ExecuteException(path + " is null");
                }
                if(!jsonObject.containsKey(key)){
                    throw new ExecuteException(key + " not exist");
                }
                if(next == null){
                    jsonObject.put(key, val);
                }else{
                    cursorVal = Value.of(jsonObject.get(next.getKey()));
                }
            }
            cursor = next;
        }
    }
}
