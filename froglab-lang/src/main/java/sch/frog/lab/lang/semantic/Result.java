package sch.frog.lab.lang.semantic;

import sch.frog.lab.lang.value.Value;

public class Result {

    public static final Result VOID = new Result(new Reference(Value.VOID));

    private final Reference reference;

    private final ResultType type;

    public Result(Reference reference, ResultType type) {
        this.reference = reference;
        this.type = type;
    }

    public Result(Reference reference){
        this.reference = reference;
        this.type = ResultType.NORMAL;
    }

    public Value value() {
        return reference.value();
    }

    public Reference reference(){
        return reference;
    }

    public ResultType type() {
        return type;
    }

}
