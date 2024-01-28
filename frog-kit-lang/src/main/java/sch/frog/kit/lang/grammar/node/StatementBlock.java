package sch.frog.kit.lang.grammar.node;

import sch.frog.kit.lang.grammar.IAstNode;
import sch.frog.kit.lang.grammar.IStatement;

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
