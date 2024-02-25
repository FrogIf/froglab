package sch.frog.lab.lang.grammar.node;

import sch.frog.lab.lang.exception.ExecuteException;
import sch.frog.lab.lang.grammar.IAstNode;
import sch.frog.lab.lang.grammar.IExpression;
import sch.frog.lab.lang.lexical.Token;
import sch.frog.lab.lang.semantic.IExecuteContext;
import sch.frog.lab.lang.semantic.Operator;
import sch.frog.lab.lang.semantic.Reference;

import java.util.Collections;
import java.util.List;

public class SuffixExpression implements IExpression {

    private final Token suffix;

    private final IExpression left;

    public SuffixExpression(IExpression left, Token suffix) {
        this.suffix = suffix;
        this.left = left;
    }

    @Override
    public String literal() {
        return "#suffix->" + suffix.literal();
    }

    @Override
    public List<IAstNode> getChildren() {
        return Collections.singletonList(left);
    }

    @Override
    public Reference evaluate(IExecuteContext context) throws ExecuteException {
        return new Reference(Operator.suffixEvaluate(left, suffix.literal(), context));
    }
}
