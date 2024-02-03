package sch.frog.lab.fundemo;

import sch.frog.lab.lang.ext.fun.FunctionDefine;
import sch.frog.lab.lang.ext.fun.FunctionPackage;

@FunctionPackage(name = "demo")
public class DemoPackage {

    @FunctionDefine(name = "add", description = "演示, 相加")
    public int add(int a, int b){
        return a + b;
    }

    @FunctionDefine(name = "sub", description = "演示, 相减")
    public int sub(int a, int b){
        return a - b;
    }

}
