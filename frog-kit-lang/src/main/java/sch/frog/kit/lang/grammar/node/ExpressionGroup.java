package sch.frog.kit.lang.grammar.node;

import sch.frog.kit.lang.exception.ExecuteException;
import sch.frog.kit.lang.grammar.IAstNode;
import sch.frog.kit.lang.grammar.IExpression;
import sch.frog.kit.lang.semantic.IExecuteContext;
import sch.frog.kit.lang.value.Value;

import java.util.Collections;
import java.util.List;

public class ExpressionGroup implements IExpression {

    private final IExpression expression;

    public ExpressionGroup(IExpression expression) {
        this.expression = expression;
    }

    @Override
    public String literal() {
        return "()";
    }

    @Override
    public List<IAstNode> getChildren() {
        return Collections.singletonList(expression);
    }

    @Override
    public Value evaluate(IExecuteContext context) throws ExecuteException {
        if(expression != null){
            return expression.evaluate(context);
        }
        return Value.VOID;
    }
}
