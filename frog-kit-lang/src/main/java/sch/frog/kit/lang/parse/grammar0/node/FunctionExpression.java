package sch.frog.kit.lang.parse.grammar0.node;

import sch.frog.kit.lang.parse.grammar0.IAstNode;
import sch.frog.kit.lang.parse.grammar0.IExpression;

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
}
