package sch.frog.kit.lang.fun;

import sch.frog.kit.lang.exception.ExecuteException;
import sch.frog.kit.lang.semantic.IExecuteContext;
import sch.frog.kit.lang.value.Value;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class GeneralFunction implements IFunction{

    private final String name;

    private final String description;

    private final Object instance;

    private final Method method;

    private final int argCount;

    private int valueCount = 0; // 0 - 无参函数, >0 多个参数的函数, -1 - Value数组

    private int[] valueIndexMark = null; // 0 - 未定义; 1 - context; 2 - Value; 3 - Value[]

    private final Class<?>[] argumentTypeArr;

    private final boolean returnVoid;

    public GeneralFunction(String name, String description, Object instance, Method method){
        this.name = name;
        this.description = description;
        this.instance = instance;
        this.method = method;
        this.argCount = method.getParameterCount();
        valueIndexMark = new int[this.argCount];
        boolean findValueArr = false;
        boolean findValue = false;
        returnVoid = method.getReturnType() == void.class;
        Class<?>[] types = method.getParameterTypes();
        argumentTypeArr = new Class<?>[types.length];
        for(int i = 0; i < types.length; i++){
            Class<?> type = types[i];
            argumentTypeArr[i] = type;
            if(IExecuteContext.class.isAssignableFrom(type)){
                valueIndexMark[i] = 1;
            }else if(type.isArray()){
                if(findValueArr){
                    throw new IllegalArgumentException("value arr must have one");
                }
                if(findValue){
                    throw new IllegalArgumentException("value arr and value confuse");
                }
                findValueArr = true;
                valueIndexMark[i] = 3;
                this.valueCount = -1;
            }else{
                if(findValueArr){
                    throw new IllegalArgumentException("value arr must have one");
                }
                findValue = true;
                valueCount++;
                valueIndexMark[i] = 2;
            }
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

    @Override
    public Value execute(Value[] args, IExecuteContext context) throws ExecuteException {
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
                arguments[i] = context;
            }else if(mark == 2){ // value
                if(argumentTypeArr[i] == Value.class){
                    arguments[i] = args[a++];
                }else{
                    arguments[i] = args[a++].cast(argumentTypeArr[i]);
                }
            }else if(mark == 3){ // array
                Class<?> componentType = argumentTypeArr[i].getComponentType();
                int len = args.length - a;
                Object arr = Array.newInstance(componentType, len);
                arguments[i] = arr;
                int offset = a;
                for(int j = offset; j < len; j++){
                    Object v;
                    if(componentType == Value.class){
                        v = args[j];
                    }else{
                        v = args[j].cast(componentType);
                    }
                    Array.set(arr, j - offset, v);
                    a++;
                }
            }
        }
        try {
            Object result = method.invoke(instance, arguments);
            return returnVoid ? Value.VOID : Value.of(result);
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
}
