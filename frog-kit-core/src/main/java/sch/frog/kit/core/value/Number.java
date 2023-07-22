package sch.frog.kit.core.value;

public class Number {

    private final String number;

    public Number(String number) {
        this.number = number;
    }

    public Number(long num){
        this.number = Long.toString(num);
    }

    public String getNumber(){
        return this.number;
    }

    @Override
    public String toString() {
        return this.number;
    }
}
