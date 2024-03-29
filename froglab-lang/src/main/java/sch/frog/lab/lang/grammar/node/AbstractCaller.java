package sch.frog.lab.lang.grammar.node;

import sch.frog.lab.lang.exception.ExecuteException;
import sch.frog.lab.lang.grammar.IAstNode;
import sch.frog.lab.lang.semantic.IExecuteContext;
import sch.frog.lab.lang.semantic.Reference;
import sch.frog.lab.lang.value.Value;

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

    public abstract Reference evaluate(Value upValue, IExecuteContext context) throws ExecuteException;
}
