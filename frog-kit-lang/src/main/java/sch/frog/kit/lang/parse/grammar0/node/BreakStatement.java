package sch.frog.kit.lang.parse.grammar0.node;

import sch.frog.kit.lang.parse.grammar0.IAstNode;
import sch.frog.kit.lang.parse.grammar0.IStatement;

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
