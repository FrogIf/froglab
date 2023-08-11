package sch.frog.kit.lang.parse.grammar;

import sch.frog.kit.lang.exception.GrammarException;
import sch.frog.kit.lang.execute.IRuntimeContext;
import sch.frog.kit.lang.parse.lexical.Token;
import sch.frog.kit.lang.value.Value;

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
