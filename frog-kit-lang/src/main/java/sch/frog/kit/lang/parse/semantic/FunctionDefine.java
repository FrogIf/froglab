package sch.frog.kit.lang.parse.semantic;

import sch.frog.kit.lang.execute.IRuntimeContext;
import sch.frog.kit.lang.fun.IFunction;
import sch.frog.kit.lang.parse.exception.ExecuteException;
import sch.frog.kit.lang.parse.grammar0.node.StatementBlock;
import sch.frog.kit.lang.parse.grammar0.node.Statements;
import sch.frog.kit.lang.value.Value;

import java.util.Collections;

public class FunctionDefine implements IFunction {

    private final String[] fArgs;

    private final Statements funBody;

    public FunctionDefine(String[] fArgs, StatementBlock statementBlock) {
        this.fArgs = fArgs;
        this.funBody = new Statements(Collections.singleton(statementBlock));
    }

    @Override
    public String name() {
        return null;
    }

    @Override
    public String description() {
        return null;
    }

    @Override
    public Value execute(Value[] args, IRuntimeContext context) {
        return null;
    }

    @Override
    public Value execute(Value[] args, IExecuteContext context) throws ExecuteException {
        InnerExecuteContext nestContext = new InnerExecuteContext(context);
        if(args.length != fArgs.length){ throw new ExecuteException("input argument incorrect, except size : " + fArgs.length + ", but : " + args.length); }
        for(int i = 0; i < args.length; i++){
            nestContext.defLocalVariable(fArgs[i], args[i]);
        }
        Result result = context.executor().execute(funBody, nestContext);
        if(result.type() == ResultType.NORMAL){
            return result.value();
        }else if(result.type() == ResultType.RETURN){
            return result.value();
        }else{
            throw new ExecuteException("function can't return type : " + result.type());
        }
    }
}
