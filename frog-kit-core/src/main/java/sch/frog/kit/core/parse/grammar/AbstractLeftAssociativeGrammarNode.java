package sch.frog.kit.core.parse.grammar;

import sch.frog.kit.core.execute.IRuntimeContext;
import sch.frog.kit.core.execute.ISession;
import sch.frog.kit.core.parse.lexical.Token;
import sch.frog.kit.core.value.Value;

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
