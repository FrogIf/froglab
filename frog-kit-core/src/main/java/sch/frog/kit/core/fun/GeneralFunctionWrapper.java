package sch.frog.kit.core.fun;

import sch.frog.kit.core.exception.ExecuteException;
import sch.frog.kit.core.execute.ISession;
import sch.frog.kit.core.value.Value;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class GeneralFunctionWrapper extends AbstractGeneralFunction{

    private final String name;

    private final String description;

    private final Object instance;

    private final Method method;

    private final int argCount;

    private int valueCount = 0; // 0 - 无参函数, >0 多个参数的函数, -1 - Value数组

    private int[] valueIndexMark = null; // 0 - 未定义; 1 - session; 2 - Value; 3 - Value[]

    public GeneralFunctionWrapper(FunctionDefine define, Object instance, Method method){
        this.name = define.name();
        this.description = define.description();
        this.instance = instance;
        this.method = method;
        this.argCount = method.getParameterCount();
        if(!Value.class.isAssignableFrom(method.getReturnType())){
            throw new IllegalArgumentException(method.getName() + " return type must Value.class");
        }
        valueIndexMark = new int[this.argCount];
        boolean findValueArr = false;
        boolean findValue = false;
        Class<?>[] types = method.getParameterTypes();
        for(int i = 0; i < types.length; i++){
            Class<?> type = types[i];
            if(ISession.class.isAssignableFrom(type)){
                valueIndexMark[i] = 1;
            }else if(Value.class.isAssignableFrom(type)){
                if(findValueArr){
                    throw new IllegalArgumentException("value arr must have one");
                }
                findValue = true;
                valueCount++;
                valueIndexMark[i] = 2;
            }else if(Value[].class == type){
                if(findValueArr){
                    throw new IllegalArgumentException("value arr must have one");
                }
                if(findValue){
                    throw new IllegalArgumentException("value arr and value confuse");
                }
                findValueArr = true;
                valueIndexMark[i] = 3;
                this.valueCount = -1;
            }
        }
    }

    @Override
    protected Value doExec(Value[] args, ISession session) {
        Object[] arguments = new Object[argCount];
        if(this.valueCount != -1){
            if(args.length != this.valueCount){
                throw new ExecuteException(this.name + "must have " + this.valueCount + " arguments");
            }
        }
        int a = 0;
        for (int i = 0; i < this.argCount; i++){
            int mark = valueIndexMark[i];
            if(mark == 1){
                arguments[i] = session;
            }else if(mark == 2){
                arguments[i] = args[a++];
            }else if(mark == 3){
                arguments[i] = args;
            }
        }
        try {
            return (Value) method.invoke(instance, arguments);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e){
            Throwable t = e.getTargetException();
            if(t instanceof ExecuteException){
                throw (ExecuteException)t;
            }
            throw new RuntimeException(t);
        }
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public String description() {
        return this.description;
    }
}
