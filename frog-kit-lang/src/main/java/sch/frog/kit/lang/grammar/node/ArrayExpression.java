package sch.frog.kit.lang.grammar.node;

import sch.frog.kit.lang.exception.ExecuteException;
import sch.frog.kit.lang.grammar.IAstNode;
import sch.frog.kit.lang.grammar.IExpression;
import sch.frog.kit.lang.semantic.IExecuteContext;
import sch.frog.kit.lang.value.Value;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ArrayExpression implements IExpression {

    private final ArrayNode arrayObj;

    private final IdentifierNode arrayIdentifier;

    private final ArrayCaller arrayCaller;

    public ArrayExpression(ArrayNode arrayObj, IdentifierNode arrayIdentifier, ArrayCaller arrayCaller) {
        this.arrayObj = arrayObj;
        this.arrayIdentifier = arrayIdentifier;
        this.arrayCaller = arrayCaller;
        if(arrayObj == null && arrayIdentifier == null){
            throw new IllegalArgumentException("array obj and array identifier both null");
        }
        if(arrayObj != null && arrayIdentifier != null){
            throw new IllegalArgumentException("array obj and array identifier both not null");
        }
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
            if(this.arrayIdentifier == null){
                return Collections.singletonList(arrayObj);
            }else{
                return Collections.singletonList(arrayIdentifier);
            }
        }else{
            if(this.arrayIdentifier == null){
                return Arrays.asList(arrayObj, arrayCaller);
            }else{
                return Arrays.asList(arrayIdentifier, arrayCaller);
            }
        }
    }

    @Override
    public Value evaluate(IExecuteContext context) throws ExecuteException {
        Value val = null;
        if(this.arrayIdentifier != null){
            val = this.arrayIdentifier.evaluate(context);
        }else{
            val = arrayObj.evaluate(context);
        }
        if(this.arrayCaller == null){
            return val;
        }
        return arrayCaller.evaluate(val, context);
    }
}
