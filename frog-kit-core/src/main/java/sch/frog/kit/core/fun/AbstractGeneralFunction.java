package sch.frog.kit.core.fun;

import sch.frog.kit.core.execute.ISession;
import sch.frog.kit.core.value.Locator;
import sch.frog.kit.core.value.Value;
import sch.frog.kit.core.value.ValueType;

public abstract class AbstractGeneralFunction extends AbstractFunction{

    @Override
    public Value execute(Value[] args, ISession session) {
        if(args != null){   // 参数转换
            for(int i = 0; i < args.length; i++){
                Value val = args[i];
                if(val != null){
                    if(val.getType() == ValueType.SYMBOL){
                        args[i] = val.to(Locator.class).get(session);
                    }
                }
            }
        }
        return doExec(args, session);
    }

    protected abstract Value doExec(Value[] args, ISession session);

}
