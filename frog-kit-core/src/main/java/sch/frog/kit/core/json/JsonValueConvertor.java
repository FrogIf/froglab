package sch.frog.kit.core.json;

import sch.frog.kit.core.fun.IFunction;
import sch.frog.kit.core.value.Locator;
import sch.frog.kit.core.value.Value;
import sch.frog.kit.core.value.ValueType;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class JsonValueConvertor {

    private static Map<Class<?>, IConvertor> convertorHolder = new HashMap<>();

    static{
        convertorHolder.put(CharSequence.class, obj -> new StringJsonValue(obj.toString()));
        convertorHolder.put(Boolean.class, obj -> new ConstJsonValue(Boolean.TRUE.equals(obj) ? "true" : "false"));
        convertorHolder.put(Integer.class, obj -> new NumberJsonValue(obj.toString()));
        convertorHolder.put(Double.class, obj -> new NumberJsonValue(obj.toString()));
        convertorHolder.put(Short.class, obj -> new NumberJsonValue(obj.toString()));
        convertorHolder.put(Byte.class, obj -> new NumberJsonValue(obj.toString()));
        convertorHolder.put(Float.class, obj -> new NumberJsonValue(obj.toString()));
        convertorHolder.put(Long.class, obj -> new NumberJsonValue(obj.toString()));
        convertorHolder.put(BigInteger.class, obj -> new NumberJsonValue(obj.toString()));
        convertorHolder.put(BigDecimal.class, obj -> new NumberJsonValue(obj.toString()));
        convertorHolder.put(JsonObject.class, obj -> new ObjectJsonValue((JsonObject) obj));
        convertorHolder.put(JsonArray.class, obj -> new ArrayJsonValue((JsonArray) obj));
        convertorHolder.put(Iterable.class, obj -> new ArrayJsonValue(JsonArray.load((Iterable<?>) obj)));
        convertorHolder.put(Map.class, obj -> new ObjectJsonValue(JsonObject.load(obj)));
        convertorHolder.put(Value.class, obj -> {
            Value val = (Value) obj;
            ValueType type = val.getType();
            if(type == ValueType.NUMBER){
                return new NumberJsonValue(val.toString());
            }else if(type == ValueType.STRING){
                return new StringJsonValue(val.to(String.class));
            }else if(type == ValueType.LIST){
                return new ArrayJsonValue(val.to(JsonArray.class));
            }else if(type == ValueType.OBJECT){
                return new ObjectJsonValue(val.to(JsonObject.class));
            }else if(type == ValueType.BOOL){
                return new ConstJsonValue(Boolean.TRUE.equals(val.to(Boolean.class)) ? "true" : "false");
            }else if(type == ValueType.SYMBOL){
                return new SymbolJsonValue(val.to(Locator.class));
            }else{
                return new StringJsonValue(val.toString());
            }
        });
    }

    public static JsonValue<?> convert(Object obj){
        if(obj == null){
            return new ConstJsonValue("null");
        }
        Class<?> clazz = obj.getClass();
        IConvertor convertor = convertorHolder.get(clazz);
        if(convertor == null){
            for (Map.Entry<Class<?>, IConvertor> entry : convertorHolder.entrySet()) {
                if(entry.getKey().isAssignableFrom(clazz)){
                    convertor = entry.getValue();
                }
            }
        }
        if(convertor != null){
            return convertor.convert(obj);
        }else{
            return new ObjectJsonValue(JsonObject.load(obj));
        }
    }

    private interface IConvertor{
        JsonValue convert(Object obj);
    }

}
