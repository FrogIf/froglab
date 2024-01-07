package sch.frog.kit.lang.parse.grammar0.node;

import sch.frog.kit.lang.parse.grammar0.IAstNode;
import sch.frog.kit.lang.parse.grammar0.IExpression;

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
}
