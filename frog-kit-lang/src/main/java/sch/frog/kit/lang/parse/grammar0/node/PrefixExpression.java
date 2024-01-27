package sch.frog.kit.lang.parse.grammar0.node;

import sch.frog.kit.lang.parse.exception.ExecuteException;
import sch.frog.kit.lang.parse.grammar0.IAstNode;
import sch.frog.kit.lang.parse.grammar0.IExpression;
import sch.frog.kit.lang.parse.lexical.Token;
import sch.frog.kit.lang.parse.semantic.Executor;
import sch.frog.kit.lang.parse.semantic.IExecuteContext;
import sch.frog.kit.lang.parse.semantic.Operator;
import sch.frog.kit.lang.value.Value;

import java.util.Collections;
import java.util.List;

public class PrefixExpression implements IExpression {

    private final Token prefix;

    private final IExpression right;

    public PrefixExpression(Token prefix, IExpression right) {
        this.prefix = prefix;
        this.right = right;
    }

    public Token getPrefix() {
        return prefix;
    }

    public IExpression getRight() {
        return right;
    }

    @Override
    public String literal() {
        return this.prefix.literal();
    }

    @Override
    public List<IAstNode> getChildren() {
        return Collections.singletonList(this.right);
    }

    @Override
    public Value evaluate(IExecuteContext context) throws ExecuteException {
        return Operator.prefixEvaluate(prefix.literal(), right, context);
    }
}
