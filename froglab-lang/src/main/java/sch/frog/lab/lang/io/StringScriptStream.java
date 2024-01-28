package sch.frog.lab.lang.io;

public class StringScriptStream implements IScriptStream {

    private final String origin;

    private int index = 0;

    private final int edge;

    public StringScriptStream(String origin) {
        this.origin = origin;
        this.edge = origin.length() - 1;
    }

    @Override
    public char current() {
        if(index > edge){ return EOF; }
        return origin.charAt(index);
    }

    @Override
    public int pos() {
        return index;
    }

    @Override
    public char peek() {
        if(index + 1 > edge){
            return EOF;
        }
        return origin.charAt(index + 1);
    }

    @Override
    public char next() {
        index++;
        return current();
    }
}
