package sch.frog.kit.core.fun.lang;

import sch.frog.kit.core.exception.ExecuteException;
import sch.frog.kit.core.execute.ISession;
import sch.frog.kit.core.fun.AbstractGeneralFunction;
import sch.frog.kit.core.value.Value;

public class PRINT extends AbstractGeneralFunction {

    @Override
    public String name() {
        return "print";
    }

    @Override
    public String description() {
        return null;
    }

    @Override
    protected Value doExec(Value[] args, ISession session) {
        if(args.length != 1){
            throw new ExecuteException("print function only support one argument");
        }
        session.getOutput().write(args[0].toString());
        return Value.VOID;
    }
}
