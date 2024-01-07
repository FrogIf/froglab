package sch.frog.kit.lang.parse.grammar0.node;

public class FunctionCaller extends AbstractCaller{

    public FunctionCaller(ExpressionList cursor, AbstractCaller next) {
        super(cursor, next);
    }

    @Override
    public String literal() {
        return "(#function_caller)";
    }
}
