package sch.frog.lab.lang.grammar.node;

import sch.frog.lab.lang.exception.ExecuteException;
import sch.frog.lab.lang.semantic.IExecuteContext;
import sch.frog.lab.lang.semantic.Reference;
import sch.frog.lab.lang.value.VMap;
import sch.frog.lab.lang.value.Value;

public class ObjectCaller extends AbstractCaller{

    public ObjectCaller(IdentifierNode cursor, AbstractCaller next) {
        super(cursor, next);
    }

    @Override
    public String literal() {
        return ".";
    }

    @Override
    public Reference evaluate(Value upValue, IExecuteContext context) throws ExecuteException {
        if(upValue == null){
            throw new ExecuteException(ExecuteException.CODE_NULL_POINTER, "value is null");
        }
        VMap vMap = upValue.cast(VMap.class);
        String key = ((IdentifierNode) this.cursor).identifier();
        Value val = vMap.get(key);
        if(val == null){ val = Value.NULL; }
        if(this.next == null){ return new Reference(val, v -> vMap.put(key, v)); }

        return this.next.evaluate(val, context);
    }
}
