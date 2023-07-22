package sch.frog.kit.core.value;

import sch.frog.kit.core.exception.ValueCastException;
import sch.frog.kit.core.fun.IFunction;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class Value {

    public static final Value VOID = new Value(ValueType.VOID);

    public static final Value NULL = new Value(ValueType.NULL);

    private static final Map<Class<?>, TypeConvertor> map = new HashMap<>();

    static {
        map.put(int.class, val -> {
            if(val.type != ValueType.NUMBER){ throw new ValueCastException(val.type, int.class); }
            return Integer.parseInt(val.val.toString());
        });
        map.put(Integer.class, val -> {
            if(val.type != ValueType.NUMBER){ throw new ValueCastException(val.type, Integer.class); }
            return Integer.parseInt(val.val.toString());
        });
        map.put(long.class, val -> {
            if(val.type != ValueType.NUMBER){ throw new ValueCastException(val.type, long.class); }
            return Long.parseLong(val.val.toString());
        });
        map.put(Long.class, val -> {
            if(val.type != ValueType.NUMBER){ throw new ValueCastException(val.type, Long.class); }
            return Long.parseLong(val.val.toString());
        });
        map.put(double.class, val -> {
            if(val.type != ValueType.NUMBER){ throw new ValueCastException(val.type, double.class); }
            return Double.parseDouble(val.val.toString());
        });
        map.put(Double.class, val -> {
            if(val.type != ValueType.NUMBER){ throw new ValueCastException(val.type, Double.class); }
            return Double.parseDouble(val.val.toString());
        });
        map.put(float.class, val -> {
            if(val.type != ValueType.NUMBER){ throw new ValueCastException(val.type, float.class); }
            return Float.parseFloat(val.val.toString());
        });
        map.put(Float.class, val -> {
            if(val.type != ValueType.NUMBER){ throw new ValueCastException(val.type, Float.class); }
            return Float.parseFloat(val.val.toString());
        });
        map.put(BigDecimal.class, val -> {
            if(val.type != ValueType.NUMBER){ throw new ValueCastException(val.type, BigDecimal.class); }
            return new BigDecimal(val.val.toString());
        });
        map.put(BigInteger.class, val -> {
            if(val.type != ValueType.NUMBER){ throw new ValueCastException(val.type, BigInteger.class); }
            return new BigInteger(val.val.toString());
        });
        map.put(boolean.class, val -> {
            if(val.type != ValueType.BOOL){ throw new ValueCastException(val.type, boolean.class); }
            return val.val;
        });
        map.put(Boolean.class, val -> {
            if(val.type != ValueType.BOOL){ throw new ValueCastException(val.type, Boolean.class); }
            return val.val;
        });
        map.put(String.class, val -> val.val.toString());
        map.put(VMap.class, val -> {
            if(val.type != ValueType.OBJECT){ throw new ValueCastException(val.type, VMap.class); }
            return val.val;
        });
        map.put(VList.class, val -> {
            if(val.type != ValueType.LIST){ throw new ValueCastException(val.type, VList.class); }
            return val.val;
        });
        map.put(Locator.class, val -> {
            if(val.type != ValueType.SYMBOL){ throw new ValueCastException(val.type, Locator.class); }
            return val.val;
        });
        map.put(IFunction.class, val -> {
            if(val.type != ValueType.FUNCTION){ throw new ValueCastException(val.type, IFunction.class); }
            return val.val;
        });

    }

    private final ValueType type;

    private final Object val;

    public Value(boolean val)
    {
        this.type = ValueType.BOOL;
        this.val = val;
    }

    public Value(String str){
        this.type = str == null ? ValueType.NULL : ValueType.STRING;
        this.val = str;
    }

    public Value(Number num){
        this.type = num == null ? ValueType.NULL : ValueType.NUMBER;
        this.val = num == null ? null : num.getNumber();
    }

    public Value(VMap map){
        this.type = map == null ? ValueType.NULL : ValueType.OBJECT;
        this.val = map;
    }

    public Value(VList list){
        this.type = list == null ? ValueType.NULL : ValueType.LIST;
        this.val = list;
    }

    public Value(IFunction function){
        this.type = function == null ? ValueType.NULL : ValueType.FUNCTION;
        this.val = function;
    }

    public Value(Locator locator){
        this.type = locator == null ? ValueType.NULL : ValueType.SYMBOL;
        this.val = locator;
    }

    private Value(ValueType valueType){
        this.type = valueType;
        this.val = null;
    }

    public ValueType getType() {
        return type;
    }

    public <T> T to(Class<T> clazz){
        if(this.type == ValueType.VOID){
            throw new ValueCastException(this.type, clazz);
        }else if(this.type == ValueType.NULL){
            return null;
        }
        TypeConvertor convertor = map.get(clazz);
        if(convertor == null){
            throw new ValueCastException(this.type, clazz);
        }
        return (T) convertor.convert(this);
    }

    public static Value of(Object obj){
        if(obj == null){
            return NULL;
        }else if(obj instanceof Value){
            return (Value) obj;
        }else if(obj instanceof String){
            return new Value(obj.toString());
        }else if(obj instanceof Boolean){
            return new Value((Boolean) obj);
        }else if(obj instanceof Integer || obj instanceof Double || obj instanceof Float || obj instanceof Long || obj instanceof Short){
            return new Value(new Number(String.valueOf(obj)));
        }else if(obj instanceof BigDecimal){
            return new Value(new Number(((BigDecimal) obj).toPlainString()));
        }else if(obj instanceof BigInteger){
            return new Value(new Number(obj.toString()));
        }else if(obj instanceof VList){
            return new Value((VList) obj);
        }else if(obj instanceof VMap){
            return new Value((VMap) obj);
        }else if(obj instanceof Iterable){
            return new Value(VList.load((Iterable<?>) obj));
        }else if(obj instanceof Locator){
            return new Value((Locator)obj);
        }else if(obj instanceof IFunction){
            return new Value((IFunction) obj);
        }else{
            return new Value(VMap.load(obj));
        }
    }

    @Override
    public String toString() {
        return String.valueOf(val);
    }

    private interface TypeConvertor{
        Object convert(Value val);
    }

}
