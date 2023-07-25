package sch.frog.kit.core.fun;

import sch.frog.kit.core.execute.IRuntimeContext;
import sch.frog.kit.core.value.Locator;
import sch.frog.kit.core.value.Value;
import sch.frog.kit.core.value.ValueType;

public abstract class AbstractGeneralFunction extends AbstractFunction{

    @Override
    public Value execute(Value[] args, IRuntimeContext context) {
        if(args != null){   // 参数转换
            for(int i = 0; i < args.length; i++){
                Value val = args[i];
                if(val != null){
                    if(val.getType() == ValueType.SYMBOL){
                        args[i] = context.getVariable(val.to(Locator.class).getKey());
                    }
                }
            }
        }
        return doExec(args, context);
    }

    protected abstract Value doExec(Value[] args, IRuntimeContext context);

}
