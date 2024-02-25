package sch.frog.lab.lang.grammar.node;

import sch.frog.lab.lang.exception.ExecuteException;
import sch.frog.lab.lang.grammar.IAstNode;
import sch.frog.lab.lang.grammar.IExpression;
import sch.frog.lab.lang.semantic.IExecuteContext;
import sch.frog.lab.lang.semantic.Reference;
import sch.frog.lab.lang.value.Value;

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
    public Reference evaluate(IExecuteContext context) throws ExecuteException {
        if(expression != null){
            return expression.evaluate(context);
        }
        return new Reference(Value.VOID);
    }
}
