package sch.frog.kit.lang.grammar.node;

import sch.frog.kit.lang.grammar.IExpression;
import sch.frog.kit.lang.grammar.IAstNode;

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

    public String name(){
        return variableName.identifier();
    }

    public IExpression expressionBody(){
        return body;
    }

    public String getVariableName() {
        return variableName.identifier();
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
