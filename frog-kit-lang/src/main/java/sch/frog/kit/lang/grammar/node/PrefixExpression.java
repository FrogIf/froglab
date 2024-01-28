package sch.frog.kit.lang.grammar.node;

import sch.frog.kit.lang.exception.ExecuteException;
import sch.frog.kit.lang.grammar.IAstNode;
import sch.frog.kit.lang.grammar.IExpression;
import sch.frog.kit.lang.lexical.Token;
import sch.frog.kit.lang.semantic.IExecuteContext;
import sch.frog.kit.lang.semantic.Operator;
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
