package sch.frog.kit.core.parse.semantic;

import sch.frog.kit.core.execute.IRuntimeContext;
import sch.frog.kit.core.parse.grammar.IGrammarNode;
import sch.frog.kit.core.parse.grammar.IProcess;

public class Executor {

    private String continuation = "";

    public String getContinuation(){
        continuation = String.valueOf(System.currentTimeMillis());
        return continuation;
    }

    public void execute(IProcess process, IGrammarNode[] grammarNode, IRuntimeContext context, String continuation){
        if(this.continuation.equals(continuation)){
            process.process(grammarNode, context);
        }
    }

}
