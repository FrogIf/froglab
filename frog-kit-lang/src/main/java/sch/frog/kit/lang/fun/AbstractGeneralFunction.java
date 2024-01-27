package sch.frog.kit.lang.fun;

import sch.frog.kit.lang.execute.IRuntimeContext;
import sch.frog.kit.lang.parse.semantic.IExecuteContext;
import sch.frog.kit.lang.value.Locator;
import sch.frog.kit.lang.value.Value;
import sch.frog.kit.lang.value.ValueType;

public abstract class AbstractGeneralFunction extends AbstractFunction{

    @Override
    public Value execute(Value[] args, IRuntimeContext context) {
        if(args != null){   // 参数转换
            for(int i = 0; i < args.length; i++){
                Value val = args[i];
                if(val != null){
                    if(val.getType() == ValueType.SYMBOL){
                        args[i] = context.getVariable(val.cast(Locator.class).getKey());
                    }
                }
            }
        }
        return doExec(args, context);
    }

    protected abstract Value doExec(Value[] args, IRuntimeContext context);

    @Override
    public Value execute(Value[] args, IExecuteContext context) {
        return null;
    }
}
