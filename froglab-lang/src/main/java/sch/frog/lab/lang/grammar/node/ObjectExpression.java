package sch.frog.lab.lang.grammar.node;

import sch.frog.lab.lang.exception.ExecuteException;
import sch.frog.lab.lang.grammar.IAstNode;
import sch.frog.lab.lang.grammar.IExpression;
import sch.frog.lab.lang.semantic.IExecuteContext;
import sch.frog.lab.lang.value.Value;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ObjectExpression implements IExpression {

    private final IdentifierNode objIdentifier;

    private final ObjectNode objectNode;

    private final ObjectCaller objectCaller;

    public ObjectExpression(IdentifierNode objIdentifier, ObjectNode objectNode, ObjectCaller objectCaller) {
        this.objIdentifier = objIdentifier;
        this.objectNode = objectNode;
        this.objectCaller = objectCaller;
        if(objIdentifier == null && objectNode == null){
            throw new IllegalArgumentException("object and identifier both null");
        }
        if(objIdentifier != null && objectNode != null){
            throw new IllegalArgumentException("object and identifier both not null");
        }
    }

    @Override
    public String literal() {
        return "object_exp";
    }

    @Override
    public List<IAstNode> getChildren() {
        if(objectCaller == null){
            if(objIdentifier == null){
                return Collections.singletonList(objectNode);
            }else{
                return Collections.singletonList(objIdentifier);
            }
        }else{
            if(objIdentifier == null){
                return Arrays.asList(objectNode, objectCaller);
            }else{
                return Arrays.asList(objIdentifier, objectCaller);
            }
        }
    }

    @Override
    public Value evaluate(IExecuteContext context) throws ExecuteException {
        Value val = null;
        if(objIdentifier != null){
            val = objIdentifier.evaluate(context);
        }else{
            val = objectNode.evaluate(context);
        }

        if(objectCaller == null){ return val; }

        return objectCaller.evaluate(val, context);
    }
}