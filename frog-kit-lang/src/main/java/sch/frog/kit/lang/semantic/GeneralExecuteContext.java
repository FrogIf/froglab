package sch.frog.kit.lang.semantic;

import sch.frog.kit.lang.constant.VariableConstant;
import sch.frog.kit.lang.exception.ExecuteError;
import sch.frog.kit.lang.exception.ExecuteException;
import sch.frog.kit.lang.fun.IFunction;
import sch.frog.kit.lang.fun.general.LengthFunction;
import sch.frog.kit.lang.fun.general.UUIDFunction;
import sch.frog.kit.lang.fun.str.IndexOfFunction;
import sch.frog.kit.lang.fun.str.LowerFunction;
import sch.frog.kit.lang.fun.str.ReplaceFunction;
import sch.frog.kit.lang.fun.str.StrFormatFunction;
import sch.frog.kit.lang.fun.str.UpperFunction;
import sch.frog.kit.lang.handle.SystemOutputHandle;
import sch.frog.kit.lang.value.Value;

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
            new UpperFunction()
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
