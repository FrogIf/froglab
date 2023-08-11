package sch.frog.kit.lang.execute;

public class GeneralOutput implements IOutput{
    @Override
    public void write(String str) {
        System.out.println(str);
    }
}
