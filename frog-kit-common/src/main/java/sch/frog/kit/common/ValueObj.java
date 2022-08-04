package sch.frog.kit.common;

public class ValueObj {

    private final String name;

    private final String value;

    public ValueObj(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
