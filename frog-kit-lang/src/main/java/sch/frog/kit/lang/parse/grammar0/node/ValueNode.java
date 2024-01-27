package sch.frog.kit.lang.parse.grammar0.node;

import sch.frog.kit.lang.parse.exception.ExecuteException;
import sch.frog.kit.lang.parse.grammar0.IAstNode;
import sch.frog.kit.lang.parse.grammar0.IExpression;
import sch.frog.kit.lang.parse.lexical.Token;
import sch.frog.kit.lang.parse.semantic.IExecuteContext;
import sch.frog.kit.lang.value.Value;

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
