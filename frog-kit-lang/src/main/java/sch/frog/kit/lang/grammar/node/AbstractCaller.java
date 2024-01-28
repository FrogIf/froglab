package sch.frog.kit.lang.grammar.node;

import sch.frog.kit.lang.exception.ExecuteException;
import sch.frog.kit.lang.grammar.IAstNode;
import sch.frog.kit.lang.semantic.IExecuteContext;
import sch.frog.kit.lang.value.Value;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class AbstractCaller implements IAstNode {

    protected final IAstNode cursor;

    protected final AbstractCaller next;

    public AbstractCaller(IAstNode cursor, AbstractCaller next) {
        this.cursor = cursor;
        this.next = next;
    }

    public IAstNode cursor(){ return cursor; }

    public AbstractCaller next(){ return next; }

    @Override
    public List<IAstNode> getChildren() {
        if(next == null){
            return Collections.singletonList(cursor);
        }else{
            return Arrays.asList(cursor, next);
        }
    }

    public abstract Value evaluate(Value upValue, IExecuteContext context) throws ExecuteException;
}
