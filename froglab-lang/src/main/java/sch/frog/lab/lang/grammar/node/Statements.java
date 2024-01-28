package sch.frog.lab.lang.grammar.node;

import sch.frog.lab.lang.grammar.IAstNode;
import sch.frog.lab.lang.grammar.IStatement;

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
