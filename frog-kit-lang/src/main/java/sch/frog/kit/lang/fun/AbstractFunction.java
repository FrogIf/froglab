package sch.frog.kit.lang.fun;

public abstract class AbstractFunction implements IFunction{

    private final String name;

    public AbstractFunction(String name) {
        this.name = name;
    }

    public AbstractFunction(){
        this.name = "#anonymous";
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public String description() {
        return "";
    }
}
