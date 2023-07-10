package sch.frog.kit.core.fun;

import sch.frog.kit.core.execute.ISession;
import sch.frog.kit.core.value.Value;

public interface IFunction {

    String name();

    String description();

    Value execute(Value[] args, ISession session);

}
