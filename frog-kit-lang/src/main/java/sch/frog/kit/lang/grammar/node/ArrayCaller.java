package sch.frog.kit.lang.grammar.node;

import io.github.frogif.calculator.number.impl.IntegerNumber;
import sch.frog.kit.lang.exception.ExecuteException;
import sch.frog.kit.lang.semantic.IExecuteContext;
import sch.frog.kit.lang.value.VList;
import sch.frog.kit.lang.value.Value;

public class ArrayCaller extends AbstractCaller {

    public ArrayCaller(ArrayIndex cursor, AbstractCaller next) {
        super(cursor, next);
    }

    @Override
    public String literal() {
        return "[#call]";
    }

    @Override
    public Value evaluate(Value upValue, IExecuteContext context) throws ExecuteException {
        if(upValue == null){
            throw new ExecuteException(ExecuteException.CODE_NULL_POINTER, "value is null");
        }
        VList vList = upValue.cast(VList.class);
        Value indexVal = ((ArrayIndex) cursor).evaluate(context);
        IntegerNumber index = indexVal.cast(IntegerNumber.class);
        Value val = vList.get(Integer.parseInt(index.toPlainString()));

        if(val == null){ val = Value.NULL; }
        if(this.next == null){ return val; }

        return this.next.evaluate(val, context);
    }
}
