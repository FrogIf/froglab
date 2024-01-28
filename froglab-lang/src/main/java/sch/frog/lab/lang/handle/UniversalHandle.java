package sch.frog.lab.lang.handle;

import sch.frog.lab.lang.exception.ExecuteException;
import sch.frog.lab.lang.fun.IFunction;
import sch.frog.lab.lang.semantic.IExecuteContext;
import sch.frog.lab.lang.util.ReflectUtil;
import sch.frog.lab.lang.value.Handle;
import sch.frog.lab.lang.value.Value;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 通用句柄
 * 连接脚本语言内部和java的桥梁
 */
public class UniversalHandle extends Handle {

    private final Object instance;

    private final Class<?> clazz;

    private final HashMap<String, List<Method>> methodMap = new HashMap<>();

    public UniversalHandle(Object instance) {
        this.instance = instance;
        this.clazz = instance.getClass();

        Method[] methods = this.clazz.getMethods();
        for (Method method : methods) {
            List<Method> list = methodMap.computeIfAbsent(method.getName(), n -> new ArrayList<>());
            list.add(method);
        }
    }

    @Override
    public String[] keys() {
        // TODO keys
        return new String[0];
    }

    @Override
    public Value get(String key) {
        List<Method> methods = methodMap.get(key);
        if(methods == null){
            try {
                Field field = this.clazz.getField(key);
                Object val = field.get(instance);
                return Value.of(val);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                return null;
            }
        }else{
            return Value.of(new IFunction(){

                @Override
                public String name() {
                    return key;
                }

                @Override
                public String description() {
                    return "#handle::" + clazz.getName() + "@" + key;
                }

                @Override
                public Value execute(Value[] args, IExecuteContext context) throws ExecuteException {
                    Object[] realArgs = new Object[args.length];
                    for(int i = 0; i < realArgs.length; i++){
                        realArgs[i] = args[i].getValueObject();
                    }
                    try {
                        return Value.of(ReflectUtil.invoke(instance, key, realArgs));
                    } catch (InvocationTargetException e) {
                        throw new ExecuteException(e.getMessage());
                    } catch (NoSuchMethodException e) {
                        throw new ExecuteException("no such method");
                    }
                }

                @Override
                public String toString(){
                    return description();
                }
            });
        }
    }

    public Object target(){
        return instance;
    }

    @Override
    public String toString() {
        return "UniversalHandle{" +
                "instance=" + instance +
                ", clazz=" + clazz +
                ", methodMap=" + methodMap +
                '}';
    }
}
