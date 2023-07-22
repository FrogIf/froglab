package sch.frog.kit.core.fun;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class FunctionLoadUtil {

    public static List<AbstractGeneralFunction> load(Object instance){
        ArrayList<AbstractGeneralFunction> list = new ArrayList<>();
        Class<?> clazz = instance.getClass();
        Method[] methods = clazz.getMethods();
        for (Method m : methods) {
            FunctionDefine funDefine = m.getAnnotation(FunctionDefine.class);
            if(funDefine != null){
                list.add(new GeneralFunctionWrapper(funDefine, instance, m));
            }
        }
        return list;
    }

}
