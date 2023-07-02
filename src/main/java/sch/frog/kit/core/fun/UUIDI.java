package sch.frog.kit.core.fun;

import sch.frog.kit.core.AbstractFunction;
import sch.frog.kit.core.ISession;
import sch.frog.kit.core.Value;
import sch.frog.kit.core.ValueType;

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
        return new Value(ValueType.STRING, java.util.UUID.randomUUID().toString().replaceAll("-", ""));
    }
}
