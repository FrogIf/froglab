package sch.frog.lab.lang.grammar.node;

import sch.frog.lab.lang.grammar.IAstNode;

import java.util.Collections;
import java.util.List;

public class ForInitializer implements IAstNode {

    private final VariableBody variableBody;

    private final VariableStatement variableStatement;

    public ForInitializer(VariableBody variableBody) {
        this.variableBody = variableBody;
        this.variableStatement = null;
    }

    public ForInitializer(VariableStatement variableStatement) {
        this.variableBody = null;
        this.variableStatement = variableStatement;
    }

    public VariableBody getVariableBody() {
        return variableBody;
    }

    public VariableStatement getVariableStatement() {
        return variableStatement;
    }

    @Override
    public String literal() {
        return "#for-initializer";
    }

    @Override
    public List<IAstNode> getChildren() {
        if(this.variableBody != null){
            return Collections.singletonList(variableBody);
        }else if(this.variableStatement != null){
            return Collections.singletonList(variableStatement);
        }else{
            return null;
        }
    }
}
