package sch.frog.kit.win.extfun;

import sch.frog.kit.lang.fun.IFunction;

import java.util.List;

public class FunctionPackage {

    private String name;

    private List<IFunction> functions;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<IFunction> getFunctions() {
        return functions;
    }

    public void setFunctions(List<IFunction> functions) {
        this.functions = functions;
    }
}
