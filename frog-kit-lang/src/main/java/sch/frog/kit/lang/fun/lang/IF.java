package sch.frog.kit.lang.fun.lang;

import sch.frog.kit.lang.exception.ExecuteException;
import sch.frog.kit.lang.execute.IRuntimeContext;
import sch.frog.kit.lang.fun.IFunction;
import sch.frog.kit.lang.parse.grammar.IGrammarNode;
import sch.frog.kit.lang.value.Value;
import sch.frog.kit.lang.value.ValueType;

public class IF implements IFunction {

    @Override
    public String name() {
        return "if";
    }

    @Override
    public String description() {
        return null;
    }

    @Override
    public Value process(IGrammarNode[] children, IRuntimeContext context) {
        if(children.length < 2 || children.length > 3){
            throw new ExecuteException("if must have 2 or 3 arguments, but " + children.length);
        }
        Value judge = children[0].evaluate(context);
        if(judge.getType() != ValueType.BOOL){
            throw new ExecuteException("if first argument must bool type, but " + judge.getType());
        }
        Boolean bo = judge.cast(boolean.class);
        if(bo){
            return children[1].evaluate(context);
        }else if(children.length == 3){
            return children[2].evaluate(context);
        }else{
            return Value.VOID;
        }
    }

    @Override
    public Value execute(Value[] args, IRuntimeContext context) {
        throw new UnsupportedOperationException("if can't execute");
    }

}
