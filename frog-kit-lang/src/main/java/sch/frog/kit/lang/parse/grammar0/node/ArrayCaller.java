package sch.frog.kit.lang.parse.grammar0.node;

public class ArrayCaller extends AbstractCaller {

    public ArrayCaller(ArrayIndex cursor, AbstractCaller next) {
        super(cursor, next);
    }

    @Override
    public String literal() {
        return "[#call]";
    }
}
