package sch.frog.kit.lang.fun.str;

import sch.frog.kit.lang.exception.ExecuteException;
import sch.frog.kit.lang.fun.IFunction;
import sch.frog.kit.lang.semantic.IExecuteContext;
import sch.frog.kit.lang.value.Value;
import sch.frog.kit.lang.value.ValueType;

public class StrFormatFunction implements IFunction {

    @Override
    public String name() {
        return "strFormat";
    }

    @Override
    public String description() {
        return "格式化字符串\nstrFormat(content, arg1, arg2, ... , argN)\ncontent中使用'{}'作为占位符";
    }

    @Override
    public Value execute(Value[] args, IExecuteContext context) throws ExecuteException {
        if(args.length == 0){
            return Value.of("");
        }
        Value textVal = args[0];
        if(textVal.getType() != ValueType.STRING){
            throw new ExecuteException("print first argument must string, but " + textVal.getType());
        }
        if(args.length == 1){
            return textVal;
        }else{
            String text = textVal.cast(String.class);
            StringBuilder sb = new StringBuilder();
            boolean escape = false;
            int index = 1;
            for(int i = 0, len = text.length(); i < len; i++){
                char ch = text.charAt(i);
                if(ch == '\\'){
                    escape = true;
                }else{
                    if(escape){
                        if(ch == '{' || ch == '}'){
                            sb.append(ch);
                        }else{
                            sb.append('\\');
                        }
                    }else{
                        if(ch == '{' && i + 1 < len && text.charAt(i + 1) == '}'){
                            if(args.length > index){
                                sb.append(args[index++]);
                            }else{
                                sb.append("{}");
                            }
                            i++;
                        }else{
                            sb.append(ch);
                        }
                    }
                    escape = false;
                }
            }
            if(escape){ sb.append('\\'); }
            return Value.of(sb.toString());
        }
    }
}
