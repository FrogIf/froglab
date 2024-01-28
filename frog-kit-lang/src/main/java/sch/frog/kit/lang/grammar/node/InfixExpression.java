package sch.frog.kit.lang.grammar.node;

import sch.frog.kit.lang.exception.ExecuteException;
import sch.frog.kit.lang.grammar.IExpression;
import sch.frog.kit.lang.grammar.IAstNode;
import sch.frog.kit.lang.lexical.Token;
import sch.frog.kit.lang.semantic.IExecuteContext;
import sch.frog.kit.lang.semantic.Operator;
import sch.frog.kit.lang.value.Value;

import java.util.Arrays;
import java.util.List;

public class InfixExpression implements IExpression {

    private final IExpression left;

    private final Token infix;

    private final IExpression right;

    public InfixExpression(IExpression left, Token infix, IExpression right) {
        this.left = left;
        this.infix = infix;
        this.right = right;
    }

    @Override
    public String literal() {
        return this.infix.literal();
    }

    @Override
    public List<IAstNode> getChildren() {
        return Arrays.asList(this.left, this.right);
    }

    @Override
    public Value evaluate(IExecuteContext context) throws ExecuteException {
        return Operator.infixEvaluate(left, infix.literal(), right, context);
    }
}
