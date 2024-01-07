package sch.frog.kit.lang.parse.grammar0.node;

import sch.frog.kit.lang.parse.grammar0.IAstNode;
import sch.frog.kit.lang.parse.grammar0.IExpression;

import java.util.Arrays;
import java.util.List;

public class FunctionRefCallExpression implements IExpression {

    private final IdentifierNode funVar;

    private final FunctionCaller caller;

    public FunctionRefCallExpression(IdentifierNode funVar, FunctionCaller caller) {
        this.funVar = funVar;
        this.caller = caller;
        if(this.funVar == null){
            throw new IllegalArgumentException("function identifier is null");
        }
        if(this.caller == null){
            throw new IllegalArgumentException("function caller is null");
        }
    }

    public IdentifierNode getFunVar() {
        return funVar;
    }

    public FunctionCaller getCaller() {
        return caller;
    }

    @Override
    public String literal() {
        return "#fun_ref_call";
    }

    @Override
    public List<IAstNode> getChildren() {
        return Arrays.asList(funVar, caller);
    }
}
