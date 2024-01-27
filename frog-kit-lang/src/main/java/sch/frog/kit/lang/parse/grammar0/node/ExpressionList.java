package sch.frog.kit.lang.parse.grammar0.node;

import sch.frog.kit.lang.parse.exception.ExecuteException;
import sch.frog.kit.lang.parse.grammar0.IAstNode;
import sch.frog.kit.lang.parse.grammar0.IExpression;
import sch.frog.kit.lang.parse.semantic.IExecuteContext;
import sch.frog.kit.lang.value.Value;

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
