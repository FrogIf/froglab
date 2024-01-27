package sch.frog.kit.lang.fun.lang;

import sch.frog.kit.lang.exception.ExecuteException;
import sch.frog.kit.lang.execute.IRuntimeContext;
import sch.frog.kit.lang.fun.IFunction;
import sch.frog.kit.lang.parse.grammar.IGrammarNode;
import sch.frog.kit.lang.parse.semantic.IExecuteContext;
import sch.frog.kit.lang.value.Value;

public class WHILE implements IFunction {
    @Override
    public String name() {
        return "while";
    }

    @Override
    public String description() {
        return null;
    }

    @Override
    public Value execute(Value[] args, IRuntimeContext context) {
        throw new UnsupportedOperationException("while can't execute");
    }

    @Override
    public Value execute(Value[] args, IExecuteContext context) throws sch.frog.kit.lang.parse.exception.ExecuteException {
        return null;
    }

    @Override
    public Value process(IGrammarNode[] children, IRuntimeContext context) {
        if(children.length != 2){
            throw new ExecuteException("while must have 2 arguments, but " + children.length);
        }
        IGrammarNode judge = children[0];
        IGrammarNode loop = children[1];
        Value val = Value.VOID;
        while(judge.evaluate(context).cast(Boolean.class)){
            val = loop.evaluate(context);
        }
        return val;
    }
}
