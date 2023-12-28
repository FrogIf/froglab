package sch.frog.kit.lang.fun;

public class FunctionInfo {

    private final String name;

    private final String description;

    public FunctionInfo(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public FunctionInfo(FunDef funDef){
        this(funDef.name(), funDef.description());
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

}
