package sch.frog.lab.lang.util;

import io.github.frogif.calculator.number.impl.IntegerNumber;
import io.github.frogif.calculator.number.impl.RationalNumber;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReflectUtil{

    /**
     * 获取类的所有字段(包括父类)
     */
    public static Field[] getFields(Class<?> clazz){
        ArrayList<Field> fields = new ArrayList<>();
        while(clazz != null){
            Field[] declaredFields = clazz.getDeclaredFields();
            fields.addAll(Arrays.asList(declaredFields));
            clazz = clazz.getSuperclass();
        }
        return fields.toArray(new Field[0]);
    }

    /**
     * 执行方法
     * @param obj 执行对象
     * @param method 方法名
     * @param args 参数
     * @return 执行结果, 如果是void, 则返回null
     * @throws InvocationTargetException 执行方法失败
     * @throws NoSuchMethodException 没有这样的方法
     */
    public static Object invoke(Object obj, String method, Object[] args) throws InvocationTargetException,
            NoSuchMethodException {
        return invoke(obj.getClass(), obj, method, args);
    }

    /**
     * 执行静态方法
     * @param clazz 执行对象
     * @param method 方法名
     * @param args 参数
     * @return 执行结果, 如果是void, 则返回null
     * @throws InvocationTargetException 执行方法失败
     * @throws NoSuchMethodException 没有这样的方法
     */
    public static Object invokeStatic(Class<?> clazz, String method, Object[] args) throws InvocationTargetException,
            NoSuchMethodException {
        return invoke(clazz, null, method, args);
    }

    /**
     * 执行方法
     * @param obj 执行实例
     * @param method 执行方法
     * @param args 执行参数
     * @return
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     */
    private static Object invoke(Class<?> clazz, Object obj, String method, Object[] args) throws NoSuchMethodException, InvocationTargetException {
        Method[] methods = clazz.getMethods();

        ArrayList<Method> candidateMethodList = new ArrayList<>();
        for (Method m : methods) {
            if(method.equals(m.getName()) && m.getParameterCount() == args.length){
                candidateMethodList.add(m);
            }
        }
        if(candidateMethodList.isEmpty()){
            throw new NoSuchMethodException("no method found for : " + method);
        }

        Object result;
        if(candidateMethodList.size() == 1){ // 只有一个备选方法
            try {
                result = candidateMethodList.get(0).invoke(obj, args);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }else{ // 存在多个重载方法
            Class<?>[] realArgTypes = new Class<?>[args.length]; // 实参类型数组
            for(int i = 0; i < args.length; i++){
                Object arg = args[i];
                if(arg != null){
                    realArgTypes[i] = arg.getClass();
                }else{
                    realArgTypes[i] = null;
                }
            }

            ParameterTreeNode tree = new ParameterTreeNode(); // 构建重载方法参数树
            for (Method m : candidateMethodList) {
                tree.add(m.getParameterTypes());
            }
            Class<?>[] path = tree.match(realArgTypes); // 获取到匹配的参数列表
            if(path == null){
                throw new NoSuchMethodException("no method found for : " + method);
            }
            Method m = clazz.getMethod(method, path);
            try {
                for(int i = 0; i < realArgTypes.length; i++){
                    if(realArgTypes[i] != path[i]){
                        Map<Class<?>, Convertor> map = originTypeToTargetTypeToConvertor.get(realArgTypes[i]);
                        if(map != null){
                            Convertor convertor = map.get(path[i]);
                            if(convertor != null){
                                args[i] = convertor.convert(args[i]);
                            }
                        }
                    }
                }
                result = m.invoke(obj, args);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        return result;
    }

    /**
     * 根的clazz为null
     */
    private static class ParameterTreeNode {
        private Class<?> clazz;
        private List<ParameterTreeNode> children;

        public void add(Class<?>[] formalArgs){
            this.add(formalArgs, 0);
        }

        public void add(Class<?>[] formalArgs, int index){
            if(index == formalArgs.length){ return; }
            if(this.children == null){
                this.children = new ArrayList<>();
            }
            ParameterTreeNode node = new ParameterTreeNode();
            this.children.add(node);
            node.clazz = formalArgs[index];
            node.add(formalArgs, index + 1);
        }

        public Class<?>[] match(Class<?>[] realArgs){
            Class<?>[] path = new Class<?>[realArgs.length];
            match(realArgs, path, 0);
            if(isFinish(path)){ return path; }
            else { return null; }
        }

        private boolean isFinish(Class<?>[] path){
            return path[path.length - 1] != null;
        }

        private void match(Class<?>[] realArgs, Class<?>[] path, int index){
            if(index == path.length){ return; }
            this.children.sort((o1, o2) -> {
                Class<?> c1 = o1.clazz;
                Class<?> c2 = o2.clazz;
                if(c1 == c2){ return 0; }
                if(c1.isAssignableFrom(c2)){ return 1; }
                else { return -1; }
            });
            if(realArgs[index] == null){    // 说明实参中这个参数传的null, 参数类型不确定
                for (ParameterTreeNode child : this.children) {
                    if(child.clazz.isPrimitive()){  // 如果是null, 则一定不会是基本数据类型
                        continue;
                    }
                    path[index] = child.clazz;
                    child.match(realArgs, path, index + 1);
                    if(isFinish(path)){
                        return;
                    }
                }
            }else{
                Class<?> realArgType = realArgs[index];
                for (ParameterTreeNode child : this.children) { // 直接按类型匹配
                    if(child.clazz == realArgType){
                        path[index] = child.clazz;
                        child.match(realArgs, path, index + 1);
                        if(isFinish(path)){
                            return;
                        }
                    }
                }
                for (ParameterTreeNode child : this.children) { // 按照继承关系进行模糊匹配
                    if(child.clazz.isAssignableFrom(realArgType)){
                        path[index] = child.clazz;
                        child.match(realArgs, path, index + 1);
                        if(isFinish(path)){
                            return;
                        }
                    }
                }

                if(realArgType.isPrimitive()){  // 将实参的基础类型转换为包装类进行匹配
                    Class<?> primaryType = primaryTypeToBoxType.get(realArgType);
                    if(primaryType != null){
                        for (ParameterTreeNode child : this.children){
                            if(child.clazz == primaryType){
                                path[index] = child.clazz;
                                child.match(realArgs, path, index + 1);
                                if(isFinish(path)){
                                    return;
                                }
                            }
                        }
                    }
                }else {
                    Class<?> boxType = boxTypeToPrimaryType.get(realArgType);
                    if(boxType != null){
                        for (ParameterTreeNode child : this.children){
                            if(child.clazz == boxType){
                                path[index] = child.clazz;
                                child.match(realArgs, path, index + 1);
                                if(isFinish(path)){
                                    return;
                                }
                            }
                        }
                    }
                }

                Class<?>[] classes = extendTypeMap.get(realArgType);
                if(classes != null){
                    for (Class<?> clz : classes) {
                        for (ParameterTreeNode child : this.children){
                            if(child.clazz == clz){
                                path[index] = child.clazz;
                                child.match(realArgs, path, index + 1);
                                if(isFinish(path)){
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // boolean, char, byte, short, int, long, float, double
    private final static Map<Class<?>, Class<?>> boxTypeToPrimaryType = new HashMap<>();
    private final static Map<Class<?>, Class<?>> primaryTypeToBoxType = new HashMap<>();
    private final static Map<Class<?>, Class<?>[]> extendTypeMap = new HashMap<>(); // 扩展的匹配方式
    private final static Map<Class<?>, Map<Class<?>, Convertor>> originTypeToTargetTypeToConvertor = new HashMap<>();

    static{
        boxTypeToPrimaryType.put(Boolean.class, boolean.class);
        boxTypeToPrimaryType.put(Byte.class, byte.class);
        boxTypeToPrimaryType.put(Short.class, short.class);
        boxTypeToPrimaryType.put(Character.class, char.class);
        boxTypeToPrimaryType.put(Integer.class, int.class);
        boxTypeToPrimaryType.put(Long.class, long.class);
        boxTypeToPrimaryType.put(Float.class, float.class);
        boxTypeToPrimaryType.put(Double.class, double.class);

        primaryTypeToBoxType.put(boolean.class, Boolean.class);
        primaryTypeToBoxType.put(byte.class, Byte.class);
        primaryTypeToBoxType.put(short.class, Short.class);
        primaryTypeToBoxType.put(char.class, Character.class);
        primaryTypeToBoxType.put(int.class, Integer.class);
        primaryTypeToBoxType.put(long.class, Long.class);
        primaryTypeToBoxType.put(float.class, Float.class);
        primaryTypeToBoxType.put(double.class, Double.class);

        extendTypeMap.put(RationalNumber.class, new Class<?>[]{ double.class, float.class, Double.class, Float.class, long.class, int.class, Long.class, Integer.class}); // 顺序不能改, 需要优先匹配浮点型
        extendTypeMap.put(IntegerNumber.class, new Class<?>[]{ long.class, int.class, Long.class, Integer.class });

        HashMap<Class<?>, Convertor> rationalMap = new HashMap<>();
        rationalMap.put(double.class, o -> ((RationalNumber)o).doubleValue());
        rationalMap.put(Double.class, o -> ((RationalNumber)o).doubleValue());
        rationalMap.put(float.class, o -> ((RationalNumber)o).floatValue());
        rationalMap.put(Float.class, o -> ((RationalNumber)o).floatValue());
        rationalMap.put(int.class, o -> ((RationalNumber)o).toInteger().intValue());
        rationalMap.put(Integer.class, o -> ((RationalNumber)o).toInteger().intValue());
        rationalMap.put(long.class, o -> ((RationalNumber)o).toInteger().longValue());
        rationalMap.put(Long.class, o -> ((RationalNumber)o).toInteger().longValue());
        originTypeToTargetTypeToConvertor.put(RationalNumber.class, rationalMap);

        HashMap<Class<?>, Convertor> integerMap = new HashMap<>();
        integerMap.put(long.class, o -> ((IntegerNumber)o).longValue());
        integerMap.put(Long.class, o -> ((IntegerNumber)o).longValue());
        integerMap.put(int.class, o -> ((IntegerNumber)o).intValue());
        integerMap.put(Integer.class, o -> ((IntegerNumber)o).intValue());
        originTypeToTargetTypeToConvertor.put(IntegerNumber.class, integerMap);
    }

    private interface Convertor{
        Object convert(Object origin);
    }

    public static void a(int a, int b){
        System.out.println("base a, b");
    }

    public static void a(Integer a, Integer b){
        System.out.println("box a, b");
    }

}
