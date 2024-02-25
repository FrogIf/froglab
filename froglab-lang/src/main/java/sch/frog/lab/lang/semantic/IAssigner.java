package sch.frog.lab.lang.semantic;

import sch.frog.lab.lang.exception.ExecuteException;
import sch.frog.lab.lang.value.Value;

public interface IAssigner {

    void assign(Value val) throws ExecuteException;

}
