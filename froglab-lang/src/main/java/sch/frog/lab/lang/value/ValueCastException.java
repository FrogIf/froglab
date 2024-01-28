package sch.frog.lab.lang.value;

public class ValueCastException extends ClassCastException{

    public ValueCastException(ValueType type, Class<?> targetType) {
        super("cast value failed, value type is " + type.name() + ", can't cast to " + targetType.getName());
    }

    public ValueCastException(String message){
        super(message);
    }
}
