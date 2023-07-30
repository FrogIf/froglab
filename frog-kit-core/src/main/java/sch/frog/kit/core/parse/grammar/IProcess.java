package sch.frog.kit.core.parse.grammar;

import sch.frog.kit.core.execute.IRuntimeContext;
import sch.frog.kit.core.value.Value;

public interface IProcess {

    Value process(IGrammarNode[] children, IRuntimeContext context);

}
