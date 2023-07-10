package sch.frog.kit.core.parse.grammar;


import sch.frog.kit.core.parse.lexical.Token;

public abstract class AbstractGrammarNode implements IGrammarNode{

    protected final Token token;

    public AbstractGrammarNode(Token token) {
        this.token = token;
    }

    @Override
    public String name() {
        return this.token.literal();
    }
}
