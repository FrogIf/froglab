package sch.frog.lab.lang.grammar.node;

import io.github.frogif.calculator.number.impl.IntegerNumber;
import sch.frog.lab.lang.exception.ExecuteException;
import sch.frog.lab.lang.semantic.IExecuteContext;
import sch.frog.lab.lang.semantic.Reference;
import sch.frog.lab.lang.value.VList;
import sch.frog.lab.lang.value.Value;

public class ArrayCaller extends AbstractCaller {

    public ArrayCaller(ArrayIndex cursor, AbstractCaller next) {
        super(cursor, next);
    }

    @Override
    public String literal() {
        return "[#call]";
    }

    @Override
    public Reference evaluate(Value upValue, IExecuteContext context) throws ExecuteException {
        if(upValue == null){
            throw new ExecuteException(ExecuteException.CODE_NULL_POINTER, "value is null");
        }
        VList vList = upValue.cast(VList.class);
        Value indexVal = ((ArrayIndex) cursor).evaluate(context);
        IntegerNumber index = indexVal.cast(IntegerNumber.class);
        Value val = vList.get(Integer.parseInt(index.toPlainString()));

        if(val == null){ val = Value.NULL; }
        if(this.next == null){ return new Reference(val, v -> vList.set(index.intValue(), v)); }

        return this.next.evaluate(val, context);
    }
}
