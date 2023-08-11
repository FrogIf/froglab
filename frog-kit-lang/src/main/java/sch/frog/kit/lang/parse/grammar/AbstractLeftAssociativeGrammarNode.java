package sch.frog.kit.lang.parse.grammar;

import sch.frog.kit.lang.execute.IRuntimeContext;
import sch.frog.kit.lang.parse.lexical.Token;
import sch.frog.kit.lang.value.Value;

public abstract class AbstractLeftAssociativeGrammarNode extends AbstractGrammarNode{

    public AbstractLeftAssociativeGrammarNode(Token token) {
        super(token);
    }

    public abstract Value succeedEvaluate(Value value, IRuntimeContext context);

    @Override
    public final Value evaluate(IRuntimeContext context) {
        throw new UnsupportedOperationException("can't call evaluate function for this class " + this.getClass().getName());
    }
}
