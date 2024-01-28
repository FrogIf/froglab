package sch.frog.kit.lang.grammar.node;

import sch.frog.kit.lang.grammar.IAstNode;
import sch.frog.kit.lang.grammar.IExpression;

import java.util.Collections;
import java.util.List;

public class ElseIfEntry implements IAstNode {

    private final IfEntry ifEntry;

    public ElseIfEntry(IfEntry ifEntry) {
        this.ifEntry = ifEntry;
    }

    public IExpression getCondition() {
        return ifEntry.getCondition();
    }

    public NestStatement getIfNest() {
        return ifEntry.getNestStatement();
    }

    @Override
    public String literal() {
        return "#else-if-entry";
    }

    @Override
    public List<IAstNode> getChildren() {
        return Collections.singletonList(ifEntry);
    }
}
