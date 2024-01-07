package sch.frog.kit.lang.parse.grammar0.node;

import sch.frog.kit.lang.parse.grammar0.IAstNode;
import sch.frog.kit.lang.parse.grammar0.IExpression;

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
}
