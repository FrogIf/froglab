package sch.frog.lab.lang.value;

public interface VList {
    boolean add(Value val);

    int size();

    Value get(int index);

    Value set(int i, Value val);
}
