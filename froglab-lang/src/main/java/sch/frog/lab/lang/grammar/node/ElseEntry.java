package sch.frog.lab.lang.grammar.node;

import sch.frog.lab.lang.grammar.IAstNode;

import java.util.Collections;
import java.util.List;

public class ElseEntry implements IAstNode {

    private final NestStatement nestStatement;

    public ElseEntry(NestStatement nestStatement) {
        this.nestStatement = nestStatement;
    }

    public NestStatement getIfNest() {
        return nestStatement;
    }

    @Override
    public String literal() {
        return "else";
    }

    @Override
    public List<IAstNode> getChildren() {
        return Collections.singletonList(nestStatement);
    }
}
