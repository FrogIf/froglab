package sch.frog.lab.lang.semantic;

import sch.frog.lab.lang.value.Value;

public class Result {

    public static final Result VOID = new Result(Value.VOID);

    private final Value value;

    private final ResultType type;

    public Result(Value value, ResultType type) {
        this.value = value;
        this.type = type;
    }

    public Result(Value value){
        this.value = value;
        this.type = ResultType.NORMAL;
    }

    public Value value() {
        return value;
    }

    public ResultType type() {
        return type;
    }

}
