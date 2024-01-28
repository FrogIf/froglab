package sch.frog.kit.lang.grammar.node;

import sch.frog.kit.lang.grammar.IAstNode;
import sch.frog.kit.lang.grammar.IStatement;

import java.util.ArrayList;
import java.util.List;

public class PackageStatement implements IStatement {

    private final List<IdentifierNode> path = new ArrayList<>();

    public PackageStatement(List<IdentifierNode> path) {
        this.path.addAll(path);
    }

    @Override
    public String literal() {
        return "package";
    }

    @Override
    public List<IAstNode> getChildren() {
        return new ArrayList<>(path);
    }
}
