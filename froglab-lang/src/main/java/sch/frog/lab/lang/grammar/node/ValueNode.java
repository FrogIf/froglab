package sch.frog.lab.lang.grammar.node;

import sch.frog.lab.lang.exception.ExecuteException;
import sch.frog.lab.lang.grammar.IAstNode;
import sch.frog.lab.lang.grammar.IExpression;
import sch.frog.lab.lang.lexical.Token;
import sch.frog.lab.lang.semantic.IExecuteContext;
import sch.frog.lab.lang.value.Value;

import java.util.List;

public abstract class ValueNode implements IExpression {

    protected final Token token;

    public ValueNode(Token token) {
        this.token = token;
    }

    @Override
    public String literal() {
        return token.literal();
    }

    @Override
    public List<IAstNode> getChildren() {
        return null;
    }

    public abstract Value evaluate();

    @Override
    public Value evaluate(IExecuteContext context) throws ExecuteException {
        return evaluate();
    }
}
