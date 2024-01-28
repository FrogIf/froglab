package sch.frog.lab.lang.grammar.node;

import sch.frog.lab.lang.grammar.IAstNode;
import sch.frog.lab.lang.grammar.IStatement;

import java.util.Collections;
import java.util.List;

public class StatementBlock implements IStatement {

    private final Statements statements;

    public StatementBlock(Statements statements) {
        this.statements = statements;
    }

    @Override
    public String literal() {
        return "{}";
    }

    @Override
    public List<IAstNode> getChildren() {
        return Collections.singletonList(statements);
    }

    public Statements getStatements() {
        return statements;
    }
}
