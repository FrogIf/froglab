package sch.frog.kit.core.fun.lang;

import sch.frog.kit.core.execute.ISession;
import sch.frog.kit.core.fun.AbstractFunction;
import sch.frog.kit.core.value.Value;

public class Exec extends AbstractFunction {
    @Override
    public String name() {
        return "exec";
    }

    @Override
    public String description() {
        return null;
    }

    @Override
    public Value execute(Value[] args, ISession session) {
        return Value.VOID;
    }
}
