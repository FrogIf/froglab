package sch.frog.lab.lang.semantic;

import sch.frog.lab.lang.constant.VariableConstant;
import sch.frog.lab.lang.exception.ExecuteError;
import sch.frog.lab.lang.exception.ExecuteException;
import sch.frog.lab.lang.fun.IFunction;
import sch.frog.lab.lang.fun.general.IsBoolFunction;
import sch.frog.lab.lang.fun.general.IsFunFunction;
import sch.frog.lab.lang.fun.general.IsListFunction;
import sch.frog.lab.lang.fun.general.IsNumberFunction;
import sch.frog.lab.lang.fun.general.IsObjectFunction;
import sch.frog.lab.lang.fun.general.IsStringFunction;
import sch.frog.lab.lang.fun.general.IsUndefineFunction;
import sch.frog.lab.lang.fun.general.LengthFunction;
import sch.frog.lab.lang.fun.general.UUIDFunction;
import sch.frog.lab.lang.fun.str.IndexOfFunction;
import sch.frog.lab.lang.fun.str.LowerFunction;
import sch.frog.lab.lang.fun.str.ReplaceFunction;
import sch.frog.lab.lang.fun.str.StrFormatFunction;
import sch.frog.lab.lang.fun.str.UpperFunction;
import sch.frog.lab.lang.handle.SystemOutputHandle;
import sch.frog.lab.lang.value.Value;

import java.util.HashMap;
import java.util.Map;

public class GeneralExecuteContext extends InnerExecuteContext{

    private static final IFunction[] functionArr = new IFunction[]{
            new LengthFunction(),
            new StrFormatFunction(),
            new UUIDFunction(),
            new IndexOfFunction(),
            new LowerFunction(),
            new ReplaceFunction(),
            new UpperFunction(),
            new IsBoolFunction(),
            new IsFunFunction(),
            new IsListFunction(),
            new IsNumberFunction(),
            new IsObjectFunction(),
            new IsStringFunction(),
            new IsUndefineFunction()
    };

    private static final HashMap<String, Value> variableMap = new HashMap<>();

    static {
        variableMap.put(VariableConstant.OUTPUT_STREAM_VAR_NAME, Value.of(new SystemOutputHandle()));
    }

    public GeneralExecuteContext(Executor executor, Map<String, Value> extendVariableMap) {
        super(executor);
        for (IFunction f : functionArr) {
            try {
                defGlobalVariable(f.name(), Value.of(f));
            } catch (ExecuteException e) {
                throw new ExecuteError(e.getMessage());
            }
        }
        for (Map.Entry<String, Value> entry : variableMap.entrySet()) {
            try {
                defGlobalVariable(entry.getKey(), entry.getValue());
            } catch (ExecuteException e) {
                throw new ExecuteError(e.getMessage());
            }
        }
        if(extendVariableMap != null){
            for (Map.Entry<String, Value> entry : extendVariableMap.entrySet()) {
                try {
                    defGlobalVariable(entry.getKey(), entry.getValue());
                } catch (ExecuteException e) {
                    throw new ExecuteError(e.getMessage());
                }
            }
        }
    }

}
