package sch.frog.kit.lang.grammar.node;

import sch.frog.kit.lang.grammar.IAstNode;
import sch.frog.kit.lang.grammar.IStatement;

import java.util.Collections;
import java.util.List;

public class BreakStatement implements IStatement {
    @Override
    public String literal() {
        return "break";
    }

    @Override
    public List<IAstNode> getChildren() {
        return Collections.emptyList();
    }
}
