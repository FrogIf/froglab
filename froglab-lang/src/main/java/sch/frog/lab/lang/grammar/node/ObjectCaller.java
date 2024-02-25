package sch.frog.lab.lang.grammar.node;

import sch.frog.lab.lang.exception.ExecuteException;
import sch.frog.lab.lang.fun.IFunction;
import sch.frog.lab.lang.semantic.IAssigner;
import sch.frog.lab.lang.semantic.IExecuteContext;
import sch.frog.lab.lang.semantic.Reference;
import sch.frog.lab.lang.value.VList;
import sch.frog.lab.lang.value.VMap;
import sch.frog.lab.lang.value.Value;
import sch.frog.lab.lang.value.ValueType;

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
        Value val = Value.UNDEFINE;
        String key = ((IdentifierNode) this.cursor).identifier();
        IAssigner assigner = null;
        if(upValue.getType() == ValueType.LIST){
            VList list = upValue.cast(VList.class);
            IFunction fun = list.getFunction(key);
            val = Value.of(fun);
        }else{
            VMap vMap = upValue.cast(VMap.class);
            if(vMap.existKey(key)){
                val = vMap.get(key);
            }else{
                if(this.next instanceof FunctionCaller){
                    IFunction fun = vMap.getFunction(key);
                    val = Value.of(fun);
                }
            }
            assigner = v -> vMap.put(key, v);
        }
        if(this.next == null){
            return new Reference(val, assigner);
        }else{
            return this.next.evaluate(val, context);
        }
    }
}
