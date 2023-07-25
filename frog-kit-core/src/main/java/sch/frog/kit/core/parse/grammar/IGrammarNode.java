package sch.frog.kit.core.parse.grammar;

import sch.frog.kit.core.exception.GrammarException;
import sch.frog.kit.core.execute.IRuntimeContext;
import sch.frog.kit.core.parse.lexical.Token;
import sch.frog.kit.core.value.Value;

public interface IGrammarNode {
    String name();

    boolean add(Token token) throws GrammarException;

    void grammarCheck() throws GrammarException;

    Value evaluate(IRuntimeContext context);

}
