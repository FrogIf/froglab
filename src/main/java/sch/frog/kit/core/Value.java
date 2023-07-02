package sch.frog.kit.core;

public class Value {

    private final ValueType type;

    private final String originValue;

    public Value(ValueType type, String originValue) {
        this.type = type;
        this.originValue = originValue;
    }

    public ValueType getType() {
        return type;
    }

    public String getOriginValue() {
        return originValue;
    }

}
