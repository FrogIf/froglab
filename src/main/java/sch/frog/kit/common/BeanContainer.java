package sch.frog.kit.common;

import java.util.HashMap;

public class BeanContainer {

    private static final HashMap<String, Object> container = new HashMap<>();

    public static <T> T get(Class<T> clazz){
        String name = clazz.getName();
        Object o = container.get(name);
        return (T) o;
    }

    public static <T> T get(String name){
        return (T)container.get(name);
    }

    public static void add(String name, Object obj){
        container.put(name, obj);
    }

    public static void add(Object obj){
        Class<?> clazz = obj.getClass();
        if(clazz.isAssignableFrom(Iterable.class)){
            throw new UnsupportedOperationException(clazz.getName() + " can't add by class info");
        }
        container.put(obj.getClass().getName(), obj);
    }

}
