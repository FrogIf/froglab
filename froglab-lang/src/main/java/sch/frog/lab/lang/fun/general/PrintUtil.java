package sch.frog.lab.lang.fun.general;

import sch.frog.lab.lang.exception.ExecuteException;
import sch.frog.lab.lang.semantic.IExecuteContext;
import sch.frog.lab.lang.value.Value;
import sch.frog.lab.lang.value.ValueType;

public class PrintUtil {

    /**
     * 打印输出
     * @param args 参数列表
     * @param context 上下文
     * @param newline 是否换行
     */
    public static void print(Value[] args, IExecuteContext context, boolean newline) throws ExecuteException {
        if(args.length == 0){
            throw new ExecuteException("print at least 1 argument");
        }
        Value log = args[0];
        if(log.getType() != ValueType.STRING){
            throw new ExecuteException("print first argument must string, but " + log.getType());
        }
        if(args.length == 1){
            System.out.println(log + (newline ? "\n" : ""));
//            context.getOutput().write(log + (newline ? "\n" : ""));
        }else{
            String logExp = log.cast(String.class);
            StringBuilder sb = new StringBuilder();
            boolean escape = false;
            int index = 1;
            for(int i = 0, len = logExp.length(); i < len; i++){
                char ch = logExp.charAt(i);
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
                        if(ch == '{' && i + 1 < len && logExp.charAt(i + 1) == '}'){
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
            System.out.println(sb + (newline ? "\n" : ""));
        }
    }

}
