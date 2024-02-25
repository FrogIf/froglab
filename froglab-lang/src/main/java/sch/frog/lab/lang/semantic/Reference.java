package sch.frog.lab.lang.semantic;

import sch.frog.lab.lang.value.Value;

public class Reference {

    private final Value value;

    private final IAssigner assigner;

    public Reference(Value value, IAssigner assigner) {
        this.value = value;
        this.assigner = assigner;
    }

    public Reference(Value value){
        this(value, null);
    }

    public Value value() {
        return value;
    }

    public IAssigner assigner() {
        return assigner;
    }

    public boolean assignable(){
        return assigner != null;
    }
}
