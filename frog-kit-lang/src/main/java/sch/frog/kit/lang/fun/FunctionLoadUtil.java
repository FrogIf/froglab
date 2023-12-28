package sch.frog.kit.lang.fun;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class FunctionLoadUtil {

    public static List<AbstractGeneralFunction> load(Object instance){
        ArrayList<AbstractGeneralFunction> list = new ArrayList<>();
        Class<?> clazz = instance.getClass();
        Method[] methods = clazz.getMethods();
        for (Method m : methods) {
            FunDef funDefine = m.getAnnotation(FunDef.class);
            if(funDefine != null){
                list.add(new GeneralFunctionWrapper(new FunctionInfo(funDefine), instance, m));
            }
        }
        return list;
    }

}
