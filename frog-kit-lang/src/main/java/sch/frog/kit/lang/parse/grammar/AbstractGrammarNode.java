package sch.frog.kit.lang.parse.grammar;


import sch.frog.kit.lang.parse.lexical.Token;

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
