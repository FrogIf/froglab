package sch.frog.lab.lang.grammar.node;

import sch.frog.lab.lang.exception.ExecuteException;
import sch.frog.lab.lang.grammar.IAstNode;
import sch.frog.lab.lang.grammar.IExpression;
import sch.frog.lab.lang.semantic.IExecuteContext;
import sch.frog.lab.lang.value.Value;

import java.util.ArrayList;
import java.util.List;

public class ExpressionList implements IAstNode {

    private final List<IExpression> expressions = new ArrayList<>();

    public ExpressionList(List<IExpression> expressions){
        this.expressions.addAll(expressions);
    }

    @Override
    public String literal() {
        return "(,)";
    }

    @Override
    public List<IAstNode> getChildren() {
        return new ArrayList<>(expressions);
    }

    public Value[] evaluate(IExecuteContext context) throws ExecuteException {
        Value[] values = new Value[expressions.size()];
        for(int i = 0; i < values.length; i++){
            values[i] = expressions.get(i).evaluate(context);
        }
        return values;
    }
}
