package sch.frog.kit.core.fun.lang;

import sch.frog.kit.core.execute.ISession;
import sch.frog.kit.core.fun.AbstractFunction;
import sch.frog.kit.core.value.Value;

public class UUIDI extends AbstractFunction {

    @Override
    public String name() {
        return "uuidi";
    }

    @Override
    public String description() {
        return null;
    }

    @Override
    public Value execute(Value[] args, ISession session) {
        return new Value(java.util.UUID.randomUUID().toString().replaceAll("-", ""));
    }
}
