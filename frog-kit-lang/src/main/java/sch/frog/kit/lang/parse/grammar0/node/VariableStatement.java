package sch.frog.kit.lang.parse.grammar0.node;

import sch.frog.kit.lang.parse.grammar0.IAstNode;
import sch.frog.kit.lang.parse.grammar0.IStatement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class VariableStatement implements IStatement {

    private final String scopeMark;

    private final List<VariableBody> bodyList;

    public VariableStatement(String scopeMark, Collection<VariableBody> bodyList) {
        this.scopeMark = scopeMark;
        this.bodyList = new ArrayList<>(bodyList);
    }

    public String getScopeMark() {
        return scopeMark;
    }

    public List<VariableBody> getBodyList() {
        return bodyList;
    }

    @Override
    public String literal() {
        return "#variable-" + scopeMark;
    }

    @Override
    public List<IAstNode> getChildren() {
        return new ArrayList<>(bodyList);
    }
}
