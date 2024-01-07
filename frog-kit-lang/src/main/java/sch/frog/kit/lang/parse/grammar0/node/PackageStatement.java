package sch.frog.kit.lang.parse.grammar0.node;

import sch.frog.kit.lang.parse.grammar0.IAstNode;
import sch.frog.kit.lang.parse.grammar0.IStatement;

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
