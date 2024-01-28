package sch.frog.kit.lang.grammar.node;

import sch.frog.kit.lang.grammar.IAstNode;

import java.util.ArrayList;
import java.util.List;

public class FunctionFormalArgumentExpression implements IAstNode {

    private final List<IdentifierNode> formalArguments = new ArrayList<>();

    public FunctionFormalArgumentExpression(List<IdentifierNode> formalArguments) {
        this.formalArguments.addAll(formalArguments);
    }

    public List<IdentifierNode> getFormalArguments() {
        return formalArguments;
    }

    @Override
    public String literal() {
        return "(#formal_arguments)";
    }

    @Override
    public List<IAstNode> getChildren() {
        return new ArrayList<>(formalArguments);
    }
}
