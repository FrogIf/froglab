package sch.frog.kit.lang.grammar.node;

import sch.frog.kit.lang.exception.ExecuteException;
import sch.frog.kit.lang.semantic.IExecuteContext;
import sch.frog.kit.lang.value.VMap;
import sch.frog.kit.lang.value.Value;

public class ObjectCaller extends AbstractCaller{

    public ObjectCaller(IdentifierNode cursor, AbstractCaller next) {
        super(cursor, next);
    }

    @Override
    public String literal() {
        return ".";
    }

    @Override
    public Value evaluate(Value upValue, IExecuteContext context) throws ExecuteException {
        if(upValue == null){
            throw new ExecuteException(ExecuteException.CODE_NULL_POINTER, "value is null");
        }
        VMap vMap = upValue.cast(VMap.class);
        Value val = vMap.get(((IdentifierNode)this.cursor).identifier());
        if(val == null){ val = Value.NULL; }
        if(this.next == null){ return val; }

        return this.next.evaluate(val, context);
    }
}
