package sch.frog.kit.core.parse.grammar;

import sch.frog.kit.core.exception.GrammarException;
import sch.frog.kit.core.execute.IRuntimeContext;
import sch.frog.kit.core.parse.lexical.Token;
import sch.frog.kit.core.value.Value;

public class EmptyGrammarNode implements IGrammarNode{
    @Override
    public String name() {
        return "";
    }

    @Override
    public boolean add(Token token) throws GrammarException {
        return false;
    }

    @Override
    public void grammarCheck() throws GrammarException {

    }

    @Override
    public Value evaluate(IRuntimeContext context) {
        return Value.VOID;
    }

}
