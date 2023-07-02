package sch.frog.kit.core;

import sch.frog.kit.core.exception.ExecuteException;

public interface IFunction {

    String name();

    String description();

    Value execute(Value[] args, ISession session) throws ExecuteException;

}
