package sch.frog.kit.core.execute;

public class GeneralOutput implements IOutput{
    @Override
    public void write(String str) {
        System.out.println(str);
    }
}
