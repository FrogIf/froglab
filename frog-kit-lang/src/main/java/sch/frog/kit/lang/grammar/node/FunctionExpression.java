package sch.frog.kit.lang.grammar.node;

import sch.frog.kit.lang.exception.ExecuteException;
import sch.frog.kit.lang.grammar.IAstNode;
import sch.frog.kit.lang.grammar.IExpression;
import sch.frog.kit.lang.semantic.IExecuteContext;
import sch.frog.kit.lang.value.Value;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FunctionExpression implements IExpression {

    private final FunctionDefineExpression functionDefine;

    private final FunctionCaller functionCaller;

    public FunctionExpression(FunctionDefineExpression functionDefine, FunctionCaller functionCaller) {
        if(functionDefine == null){
            throw new IllegalArgumentException("functionDefine must not null");
        }
        this.functionDefine = functionDefine;
        this.functionCaller = functionCaller;
    }

    public FunctionDefineExpression getFunctionDefine() {
        return functionDefine;
    }

    public FunctionCaller getFunctionCaller() {
        return functionCaller;
    }

    @Override
    public String literal() {
        return "#function_exp";
    }

    @Override
    public List<IAstNode> getChildren() {
        if(functionCaller == null){
            return Collections.singletonList(functionDefine);
        }else{
            return Arrays.asList(functionDefine, functionCaller);
        }
    }

    @Override
    public Value evaluate(IExecuteContext context) throws ExecuteException {
        Value result = functionDefine.evaluate(context);
        if(functionCaller != null){
            return functionCaller.evaluate(result, context);
        }else{
            return result;
        }
    }
}
