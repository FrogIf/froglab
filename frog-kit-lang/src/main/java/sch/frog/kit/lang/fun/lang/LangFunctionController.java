package sch.frog.kit.lang.fun.lang;

public class LangFunctionController {

//    @FunDef(name = "funs")
//    public String funs(Value[] args, IRuntimeContext context){
//        ISession session = context.getSession();
//        StringBuilder sb = new StringBuilder();
//        if(args.length == 0){
//            Collection<IFunction> functions = session.getFunctions();
//            boolean start = true;
//            for (IFunction function : functions) {
//                if(!start){
//                    sb.append(',');
//                }
//                sb.append(function.name());
//                start = false;
//            }
//        }else{
//            boolean start = true;
//            for (Value arg : args) {
//                if(arg.getType() != ValueType.STRING){ throw new ExecuteException("funs arguments type must string"); }
//                Collection<IFunction> functions = session.getFunctions(arg.cast(String.class));
//                for (IFunction function : functions) {
//                    if(!start){
//                        sb.append(',');
//                    }
//                    sb.append(function.name());
//                    start = false;
//                }
//            }
//        }
//        return sb.toString();
//    }

}
