package sch.frog.lab.win.extfun;

import sch.frog.lab.lang.fun.IFunction;

import java.util.List;

public class FunPackage {

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
