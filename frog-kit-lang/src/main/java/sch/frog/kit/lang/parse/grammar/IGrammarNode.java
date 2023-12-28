package sch.frog.kit.lang.parse.grammar;

import sch.frog.kit.lang.execute.IRuntimeContext;
import sch.frog.kit.lang.parse.lexical.Token;
import sch.frog.kit.lang.value.Value;

public interface IGrammarNode {
    String name();

    boolean add(Token token) throws GrammarException;

    void grammarCheck() throws GrammarException;

    Value evaluate(IRuntimeContext context);

}
