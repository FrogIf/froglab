package sch.frog.lab.lang.grammar.node;

import sch.frog.lab.lang.exception.ExecuteException;
import sch.frog.lab.lang.grammar.IAstNode;
import sch.frog.lab.lang.grammar.IExpression;
import sch.frog.lab.lang.semantic.IAssigner;
import sch.frog.lab.lang.semantic.IExecuteContext;
import sch.frog.lab.lang.semantic.Reference;
import sch.frog.lab.lang.value.Value;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ArrayExpression implements IExpression {

    private final ArrayNode arrayObj;

    private final IdentifierNode arrayIdentifier;

    private final AbstractCaller arrayCaller;

    public ArrayExpression(ArrayNode arrayObj, IdentifierNode arrayIdentifier, AbstractCaller arrayCaller) {
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
    public Reference evaluate(IExecuteContext context) throws ExecuteException {
        Value val = null;
        IAssigner assigner = null;
        if(this.arrayIdentifier != null){
            Reference ref = this.arrayIdentifier.evaluate(context);
            assigner = ref.assigner();
            val = ref.value();
        }else{
            val = arrayObj.evaluate(context).value();
        }
        if(this.arrayCaller == null){
            return new Reference(val, assigner);
        }
        return arrayCaller.evaluate(val, context);
    }
}
