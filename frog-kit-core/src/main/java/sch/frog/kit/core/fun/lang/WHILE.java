package sch.frog.kit.core.fun.lang;

import sch.frog.kit.core.exception.ExecuteException;
import sch.frog.kit.core.execute.IRuntimeContext;
import sch.frog.kit.core.fun.IFunction;
import sch.frog.kit.core.parse.grammar.IGrammarNode;
import sch.frog.kit.core.value.Value;

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
    public Value process(IGrammarNode[] children, IRuntimeContext context) {
        if(children.length != 2){
            throw new ExecuteException("while must have 2 arguments, but " + children.length);
        }
        IGrammarNode judge = children[0];
        IGrammarNode loop = children[1];
        Value val = Value.VOID;
        while(judge.evaluate(context).to(Boolean.class)){
            val = loop.evaluate(context);
        }
        return val;
    }
}
