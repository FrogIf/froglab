package sch.frog.lab.lang.grammar.node;

import sch.frog.lab.lang.exception.ExecuteException;
import sch.frog.lab.lang.grammar.IAstNode;
import sch.frog.lab.lang.grammar.IExpression;
import sch.frog.lab.lang.semantic.IExecuteContext;
import sch.frog.lab.lang.semantic.Reference;
import sch.frog.lab.lang.value.VList;
import sch.frog.lab.lang.value.VListImpl;
import sch.frog.lab.lang.value.Value;

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
    public Reference evaluate(IExecuteContext context) throws ExecuteException {
        VList list = new VListImpl();
        for (IExpression exp : arr) {
            list.add(exp.evaluate(context).value());
        }
        return new Reference(Value.of(list));
    }
}
