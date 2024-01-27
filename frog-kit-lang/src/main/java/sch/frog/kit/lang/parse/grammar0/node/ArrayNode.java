package sch.frog.kit.lang.parse.grammar0.node;

import sch.frog.kit.lang.parse.exception.ExecuteException;
import sch.frog.kit.lang.parse.grammar0.IAstNode;
import sch.frog.kit.lang.parse.grammar0.IExpression;
import sch.frog.kit.lang.parse.semantic.IExecuteContext;
import sch.frog.kit.lang.value.VList;
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
        VList list = new VList();
        for (IExpression exp : arr) {
            list.add(exp.evaluate(context));
        }
        return Value.of(list);
    }
}
