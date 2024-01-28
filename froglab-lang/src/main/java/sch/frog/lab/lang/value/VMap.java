package sch.frog.lab.lang.value;

public interface VMap {

    Value put(String key, Value value);

    Value get(String key);

}
