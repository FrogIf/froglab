package sch.frog.lab.lang.grammar.node;

import sch.frog.lab.lang.exception.ExecuteException;
import sch.frog.lab.lang.grammar.IAstNode;
import sch.frog.lab.lang.grammar.IExpression;
import sch.frog.lab.lang.lexical.Token;
import sch.frog.lab.lang.semantic.IExecuteContext;
import sch.frog.lab.lang.semantic.Operator;
import sch.frog.lab.lang.semantic.Reference;

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
    public Reference evaluate(IExecuteContext context) throws ExecuteException {
        return new Reference(Operator.infixEvaluate(left, infix.literal(), right, context));
    }
}
