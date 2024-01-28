package sch.frog.lab.lang.fun.str;

import sch.frog.lab.lang.fun.IFunction;
import sch.frog.lab.lang.exception.ExecuteException;
import sch.frog.lab.lang.semantic.IExecuteContext;
import sch.frog.lab.lang.value.Value;
import sch.frog.lab.lang.value.ValueType;

public class ReplaceFunction implements IFunction {
    @Override
    public String name() {
        return "replace";
    }

    @Override
    public String description() {
        return "字符串中替换字符\nreplace(text, searchStr, replacement)\nsearchStr是需要匹配的字符传,replacement是替换的字符串";
    }

    @Override
    public Value execute(Value[] args, IExecuteContext context) throws ExecuteException {
        if(args.length != 3){
            throw new ExecuteException("replace function expect 3 arguments, but " + args.length);
        }
        for (int i = 0; i < args.length; i++){
            Value arg = args[i];
            if(arg.getType() != ValueType.STRING){
                throw new ExecuteException("replace function " + (i + 1) + " argument expect string type, but " + arg.getType());
            }
        }
        String text = args[0].cast(String.class);
        String searchStr = args[1].cast(String.class);
        String replacement = args[2].cast(String.class);

        return new Value(replace(text, searchStr, replacement));
    }

    private static String replace(String text, String searchStr, String replacement){
        int replLength = searchStr.length();
        if(replLength == 0 || text.isEmpty()){ return text; }

        int start = 0;
        int end = text.indexOf(searchStr, start);
        if(end == -1){ return text; }

        int increase = Math.max(replacement.length() - replLength, 0);
        increase *= 16;  // 预估替换后的字符串可能增长的长度

        StringBuilder buf = new StringBuilder(text.length() + increase);
        for(; end != -1; end = text.indexOf(searchStr, start)) {
            buf.append(text, start, end).append(replacement);
            start = end + replLength;
        }

        buf.append(text, start, text.length());
        return buf.toString();
    }
}
