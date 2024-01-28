package sch.frog.lab.lang.handle;

import sch.frog.lab.lang.exception.ExecuteException;
import sch.frog.lab.lang.fun.AbstractFunction;
import sch.frog.lab.lang.fun.IFunction;
import sch.frog.lab.lang.semantic.IExecuteContext;
import sch.frog.lab.lang.value.Handle;
import sch.frog.lab.lang.value.Value;

import java.util.HashMap;

public class SystemOutputHandle extends Handle {

    private final HashMap<String, IFunction> funMap = new HashMap<>();

    public SystemOutputHandle(){
        this(new PrintStream() {
            @Override
            public void print(String str) {
                System.out.print(str);
            }

            @Override
            public void println(String str) {
                System.out.println(str);
            }
        });
    }

    public SystemOutputHandle(final PrintStream printStream) {
        if(printStream == null){
            throw new IllegalArgumentException("printStream is null");
        }
        IFunction[] funArr = new IFunction[]{
                new AbstractFunction("print") {
                    @Override
                    public Value execute(Value[] args, IExecuteContext context) throws ExecuteException {
                        if (args.length != 1) {
                            throw new ExecuteException("print expect 1 arguments, but " + args.length);
                        }
                        printStream.print(args[0].toString());
                        return Value.VOID;
                    }
                },
                new AbstractFunction("println") {
                    @Override
                    public Value execute(Value[] args, IExecuteContext context) throws ExecuteException {
                        if (args.length != 1) {
                            throw new ExecuteException("println expect 1 arguments, but " + args.length);
                        }
                        printStream.println(args[0].toString());
                        return Value.VOID;
                    }
                }
        };
        for (IFunction fun : funArr) {
            funMap.put(fun.name(), fun);
        }
    }

    @Override
    public String[] keys() {
        return new String[]{
                "print", "println"
        };
    }

    @Override
    public Value get(String key) {
        return Value.of(funMap.get(key));
    }

    public interface PrintStream{
        void print(String str);

        void println(String str);
    }
}
