package sch.frog.kit.lang.parse.grammar0.node;

public class ObjectCaller extends AbstractCaller{

    public ObjectCaller(IdentifierNode cursor, AbstractCaller next) {
        super(cursor, next);
    }

    @Override
    public String literal() {
        return ".";
    }
}
