package sch.frog.lab.lang.grammar.node;

import sch.frog.lab.lang.exception.ExecuteException;
import sch.frog.lab.lang.grammar.IAstNode;
import sch.frog.lab.lang.grammar.IExpression;
import sch.frog.lab.lang.semantic.IExecuteContext;
import sch.frog.lab.lang.value.Value;

import java.util.Collections;
import java.util.List;

public class ArrayIndex implements IAstNode {

    private final IExpression index;

    public ArrayIndex(IExpression index) {
        this.index = index;
    }

    public IExpression getIndex() {
        return index;
    }

    @Override
    public String literal() {
        return "#[]";
    }

    @Override
    public List<IAstNode> getChildren() {
        return Collections.singletonList(index);
    }

    public Value evaluate(IExecuteContext context) throws ExecuteException {
        return index.evaluate(context);
    }
}
