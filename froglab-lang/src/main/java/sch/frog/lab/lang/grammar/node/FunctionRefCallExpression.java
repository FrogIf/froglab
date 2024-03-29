package sch.frog.lab.lang.grammar.node;

import sch.frog.lab.lang.exception.ExecuteException;
import sch.frog.lab.lang.grammar.IAstNode;
import sch.frog.lab.lang.grammar.IExpression;
import sch.frog.lab.lang.semantic.IExecuteContext;
import sch.frog.lab.lang.semantic.Reference;
import sch.frog.lab.lang.value.Value;

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

    @Override
    public Reference evaluate(IExecuteContext context) throws ExecuteException {
        Value funVal = context.getVariableValue(funVar.identifier());
        return caller.evaluate(funVal, context);
    }
}
