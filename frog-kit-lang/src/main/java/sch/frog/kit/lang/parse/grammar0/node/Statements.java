package sch.frog.kit.lang.parse.grammar0.node;

import sch.frog.kit.lang.parse.grammar0.IAstNode;
import sch.frog.kit.lang.parse.grammar0.IStatement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Statements implements IAstNode {

    private final List<IStatement> statements = new ArrayList<>();

    public Statements(Collection<IStatement> statements){
        this.statements.addAll(statements);
    }

    public List<IStatement> getStatements() {
        return statements;
    }

    @Override
    public String literal() {
        return "#statements";
    }

    @Override
    public List<IAstNode> getChildren() {
        return new ArrayList<>(statements);
    }
}
