package sch.frog.kit.lang.parse.grammar0.node;

import sch.frog.kit.lang.parse.grammar0.IAstNode;
import sch.frog.kit.lang.parse.grammar0.IExpression;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ObjectExpression implements IExpression {

    private final IExpression objectObj;

    private final ObjectCaller objectCaller;

    public ObjectExpression(IExpression objectObj, ObjectCaller objectCaller) {
        this.objectObj = objectObj;
        this.objectCaller = objectCaller;
    }

    public IExpression getObjectObj() {
        return objectObj;
    }

    public ObjectCaller getObjectCaller() {
        return objectCaller;
    }

    @Override
    public String literal() {
        return "object_exp";
    }

    @Override
    public List<IAstNode> getChildren() {
        if(objectCaller == null){
            return Collections.singletonList(objectObj);
        }else{
            return Arrays.asList(objectObj, objectCaller);
        }
    }
}
