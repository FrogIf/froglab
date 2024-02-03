package sch.frog.lab.win.extfun.win;

import sch.frog.lab.lang.exception.ExecuteException;
import sch.frog.lab.lang.fun.IFunction;
import sch.frog.lab.lang.semantic.IExecuteContext;
import sch.frog.lab.lang.value.Value;
import sch.frog.lab.win.util.StringUtils;

import java.util.List;

public class FunListFunction implements IFunction {

    private final String document;

    private static final String RETRACT = "\t";

    public FunListFunction(List<FunWrapper> list) {
        StringBuilder sb = new StringBuilder();
        for (FunWrapper fw : list) {
            IFunction f = fw.fun;
            sb.append("▶ name: ");
            if(StringUtils.isNotBlank(fw.pak)){
                sb.append(fw.pak).append('.');
            }
            sb.append(f.name()).append('\n');
            String description = f.description();
            if(description != null && !"".equals(description = description.trim())){
                sb.append(RETRACT);
                for(int i = 0, len = description.length(); i < len; i++){
                    char ch = description.charAt(i);
                    if(ch == '\n'){
                        sb.append(ch).append(RETRACT);
                    }else{
                        sb.append(ch);
                    }
                }
            }
            sb.append('\n').append('\n');
        }
        document = sb.toString();
    }

    @Override
    public String name() {
        return "funs";
    }

    @Override
    public String description() {
        return "获取函数列表";
    }

    @Override
    public Value execute(Value[] args, IExecuteContext context) throws ExecuteException {
        return Value.of(document);
    }

    public static class FunWrapper {
        private IFunction fun;
        private String pak;

        public FunWrapper(IFunction fun, String pak) {
            this.fun = fun;
            this.pak = pak;
        }
    }
}
