package sch.frog.kit.lang.grammar.node;

import sch.frog.kit.lang.grammar.IStatement;
import sch.frog.kit.lang.grammar.IAstNode;

import java.util.ArrayList;
import java.util.List;

public class ImportStatement implements IStatement {

    private final List<IdentifierNode> path = new ArrayList<>();

    public ImportStatement(List<IdentifierNode> path) {
        this.path.addAll(path);
    }

    @Override
    public String literal() {
        return "import";
    }

    @Override
    public List<IAstNode> getChildren() {
        return new ArrayList<>(path);
    }
}
