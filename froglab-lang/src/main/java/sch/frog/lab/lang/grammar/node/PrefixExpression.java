package sch.frog.lab.lang.grammar.node;

import sch.frog.lab.lang.exception.ExecuteException;
import sch.frog.lab.lang.grammar.IAstNode;
import sch.frog.lab.lang.grammar.IExpression;
import sch.frog.lab.lang.lexical.Token;
import sch.frog.lab.lang.semantic.IExecuteContext;
import sch.frog.lab.lang.semantic.Operator;
import sch.frog.lab.lang.value.Value;

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
