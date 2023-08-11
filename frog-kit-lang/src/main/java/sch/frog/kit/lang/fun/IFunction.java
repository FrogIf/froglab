package sch.frog.kit.lang.fun;

import sch.frog.kit.lang.execute.IRuntimeContext;
import sch.frog.kit.lang.parse.grammar.IGrammarNode;
import sch.frog.kit.lang.parse.grammar.IProcess;
import sch.frog.kit.lang.value.Value;

public interface IFunction extends IProcess {

    String name();

    String description();

    Value execute(Value[] args, IRuntimeContext context);

    default Value process(IGrammarNode[] children, IRuntimeContext context){
        if(children == null || children.length == 0){
            return execute(new Value[0], context);
        }else{
            Value[] args = new Value[children.length];
            for(int i = 0; i < children.length; i++){
                args[i] = children[i].evaluate(context);
            }
            return execute(args, context);
        }
    }

}