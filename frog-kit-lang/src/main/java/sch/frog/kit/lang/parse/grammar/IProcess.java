package sch.frog.kit.lang.parse.grammar;

import sch.frog.kit.lang.execute.IRuntimeContext;
import sch.frog.kit.lang.value.Value;

public interface IProcess {

    Value process(IGrammarNode[] children, IRuntimeContext context);

}
