package sch.frog.kit.core.fun.lang;

import sch.frog.kit.core.execute.ISession;
import sch.frog.kit.core.fun.AbstractFunction;
import sch.frog.kit.core.value.Number;
import sch.frog.kit.core.value.Value;

public class NOW extends AbstractFunction {

    @Override
    public String name() {
        return "now";
    }

    @Override
    public String description() {
        return null;
    }

    @Override
    public Value execute(Value[] args, ISession session) {
        return new Value(new Number(String.valueOf(System.currentTimeMillis())));
    }
}
