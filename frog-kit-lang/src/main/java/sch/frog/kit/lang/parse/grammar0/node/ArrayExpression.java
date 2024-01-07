package sch.frog.kit.lang.parse.grammar0.node;

import sch.frog.kit.lang.parse.grammar0.IAstNode;
import sch.frog.kit.lang.parse.grammar0.IExpression;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ArrayExpression implements IExpression {

    private final IExpression arrayObj;

    private final ArrayCaller arrayCaller;

    public ArrayExpression(IExpression arrayObj, ArrayCaller arrayCaller) {
        this.arrayObj = arrayObj;
        this.arrayCaller = arrayCaller;
    }

    public IExpression getArrayObj() {
        return arrayObj;
    }

    public ArrayCaller getArrayCaller() {
        return arrayCaller;
    }

    @Override
    public String literal() {
        return "array_exp";
    }

    @Override
    public List<IAstNode> getChildren() {
        if(arrayCaller == null){
            return Collections.singletonList(arrayObj);
        }else{
            return Arrays.asList(arrayObj, arrayCaller);
        }
    }
}
