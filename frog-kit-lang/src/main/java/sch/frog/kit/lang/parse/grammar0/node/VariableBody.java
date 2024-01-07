package sch.frog.kit.lang.parse.grammar0.node;

import sch.frog.kit.lang.parse.grammar0.IAstNode;
import sch.frog.kit.lang.parse.grammar0.IExpression;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class VariableBody implements IAstNode {

    private final IdentifierNode variableName;

    private final IExpression body;

    public VariableBody(IdentifierNode variableName, IExpression body) {
        this.variableName = variableName;
        this.body = body;
    }

    @Override
    public String literal() {
        if(body == null){
            return "#variable-declare";
        }else{
            return "#variable-define";
        }
    }

    @Override
    public List<IAstNode> getChildren() {
        if(body == null){
            return Collections.singletonList(variableName);
        }else{
            return Arrays.asList(variableName, body);
        }
    }
}
