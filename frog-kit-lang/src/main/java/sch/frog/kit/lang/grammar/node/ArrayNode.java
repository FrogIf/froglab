package sch.frog.kit.lang.grammar.node;

import sch.frog.kit.lang.exception.ExecuteException;
import sch.frog.kit.lang.grammar.IAstNode;
import sch.frog.kit.lang.grammar.IExpression;
import sch.frog.kit.lang.semantic.IExecuteContext;
import sch.frog.kit.lang.value.VList;
import sch.frog.kit.lang.value.VListImpl;
import sch.frog.kit.lang.value.Value;

import java.util.Arrays;
import java.util.List;

public class ArrayNode implements IExpression {

    private final IExpression[] arr;

    public ArrayNode(IExpression[] arr) {
        this.arr = arr;
    }

    @Override
    public String literal() {
        return "#[]";
    }

    @Override
    public List<IAstNode> getChildren() {
        return Arrays.asList(arr);
    }

    @Override
    public Value evaluate(IExecuteContext context) throws ExecuteException {
        VList list = new VListImpl();
        for (IExpression exp : arr) {
            list.add(exp.evaluate(context));
        }
        return Value.of(list);
    }
}
