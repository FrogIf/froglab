package sch.frog.kit.lang.value;

import io.github.frogif.calculator.number.impl.IntegerNumber;
import io.github.frogif.calculator.number.impl.NumberRoundingMode;
import io.github.frogif.calculator.number.impl.RationalNumber;
import sch.frog.kit.lang.exception.ValueCastException;
import sch.frog.kit.lang.fun.IFunction;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

public class Value {

    public static final Value VOID = new Value(ValueType.VOID);

    public static final Value NULL = new Value(ValueType.NULL);

    private static final Map<Class<?>, TypeConvertor> javaTypeConvertorMap = new HashMap<>();

    static {
        javaTypeConvertorMap.put(int.class, val -> {
            if(val.type != ValueType.NUMBER){ throw new ValueCastException(val.type, int.class); }
            RationalNumber num = (RationalNumber) val.val;
            IntegerNumber intNum = num.toInteger();
            if(intNum == null){ throw new ValueCastException(num + " can't cast to int"); }
            return Integer.parseInt(intNum.toString());
        });
        javaTypeConvertorMap.put(Integer.class, val -> {
            if(val.type != ValueType.NUMBER){ throw new ValueCastException(val.type, Integer.class); }
            RationalNumber num = (RationalNumber) val.val;
            IntegerNumber intNum = num.toInteger();
            if(intNum == null){ throw new ValueCastException(num + " can't cast to integer"); }
            return Integer.parseInt(intNum.toString());
        });
        javaTypeConvertorMap.put(long.class, val -> {
            if(val.type != ValueType.NUMBER){ throw new ValueCastException(val.type, long.class); }
            RationalNumber num = (RationalNumber) val.val;
            IntegerNumber intNum = num.toInteger();
            if(intNum == null){ throw new ValueCastException(num + " can't cast to long"); }
            return Long.parseLong(intNum.toString());
        });
        javaTypeConvertorMap.put(Long.class, val -> {
            if(val.type != ValueType.NUMBER){ throw new ValueCastException(val.type, Long.class); }
            RationalNumber num = (RationalNumber) val.val;
            IntegerNumber intNum = num.toInteger();
            if(intNum == null){ throw new ValueCastException(num + " can't cast to Long"); }
            return Long.parseLong(intNum.toString());
        });
        javaTypeConvertorMap.put(double.class, val -> {
            if(val.type != ValueType.NUMBER){ throw new ValueCastException(val.type, double.class); }
            RationalNumber num = (RationalNumber) val.val;
            String plainString = num.toPlainString(10, NumberRoundingMode.HALF_UP);
            return Double.parseDouble(plainString);
        });
        javaTypeConvertorMap.put(Double.class, val -> {
            if(val.type != ValueType.NUMBER){ throw new ValueCastException(val.type, Double.class); }
            RationalNumber num = (RationalNumber) val.val;
            String plainString = num.toPlainString(10, NumberRoundingMode.HALF_UP);
            return Double.parseDouble(plainString);
        });
        javaTypeConvertorMap.put(float.class, val -> {
            if(val.type != ValueType.NUMBER){ throw new ValueCastException(val.type, float.class); }
            RationalNumber num = (RationalNumber) val.val;
            String plainString = num.toPlainString(10, NumberRoundingMode.HALF_UP);
            return Float.parseFloat(plainString);
        });
        javaTypeConvertorMap.put(Float.class, val -> {
            if(val.type != ValueType.NUMBER){ throw new ValueCastException(val.type, Float.class); }
            RationalNumber num = (RationalNumber) val.val;
            String plainString = num.toPlainString(10, NumberRoundingMode.HALF_UP);
            return Float.parseFloat(plainString);
        });
        javaTypeConvertorMap.put(IntegerNumber.class, val -> {
            if(val.type != ValueType.NUMBER){ throw new ValueCastException(val.type, RationalNumber.class); }
            IntegerNumber intNum = ((RationalNumber) val.val).toInteger();
            if(intNum == null){ throw new ValueCastException(val.val + " can't cast to integerNumber"); }
            return intNum;
        });
        javaTypeConvertorMap.put(RationalNumber.class, val -> {
            if(val.type != ValueType.NUMBER){ throw new ValueCastException(val.type, RationalNumber.class); }
            return val.val;
        });
        javaTypeConvertorMap.put(BigDecimal.class, val -> {
            if(val.type != ValueType.NUMBER){ throw new ValueCastException(val.type, BigDecimal.class); }
            RationalNumber rationalNumber = (RationalNumber) val.val;
            BigDecimal num = new BigDecimal(rationalNumber.getNumerator().toPlainString());
            BigDecimal den = new BigDecimal(rationalNumber.getDenominator().toPlainString());
            return num.divide(den, 10, RoundingMode.HALF_UP);
        });
        javaTypeConvertorMap.put(BigInteger.class, val -> {
            if(val.type != ValueType.NUMBER){ throw new ValueCastException(val.type, BigInteger.class); }
            IntegerNumber intNum = ((RationalNumber) val.val).toInteger();
            if(intNum == null){ throw new ValueCastException(val.val + " can't cast to integerNumber"); }
            return new BigInteger(intNum.toPlainString());
        });
        javaTypeConvertorMap.put(boolean.class, val -> {
            if(val.type != ValueType.BOOL){ throw new ValueCastException(val.type, boolean.class); }
            return val.val;
        });
        javaTypeConvertorMap.put(Boolean.class, val -> {
            if(val.type != ValueType.BOOL){ throw new ValueCastException(val.type, Boolean.class); }
            return val.val;
        });
        javaTypeConvertorMap.put(String.class, val -> {
            if(val.type != ValueType.STRING){ throw new ValueCastException(val.type, String.class); }
            return val.val;
        });
        javaTypeConvertorMap.put(VMap.class, val -> {
            if(val.type != ValueType.OBJECT){ throw new ValueCastException(val.type, VMap.class); }
            return val.val;
        });
        javaTypeConvertorMap.put(VList.class, val -> {
            if(val.type != ValueType.LIST){ throw new ValueCastException(val.type, VList.class); }
            return val.val;
        });
        javaTypeConvertorMap.put(Locator.class, val -> {
            if(val.type != ValueType.SYMBOL){ throw new ValueCastException(val.type, Locator.class); }
            return val.val;
        });
        javaTypeConvertorMap.put(IFunction.class, val -> {
            if(val.type != ValueType.FUNCTION){ throw new ValueCastException(val.type, IFunction.class); }
            return val.val;
        });
//        map.put(Object.class, val -> val.val);

    }

    private final ValueType type;

    private final Object val;

    public Value(RationalNumber rationalNumber){
        this.val = rationalNumber;
        this.type = ValueType.NUMBER;
    }

    public Value(boolean val)
    {
        this.type = ValueType.BOOL;
        this.val = val;
    }

    public Value(String str){
        this.type = str == null ? ValueType.NULL : ValueType.STRING;
        this.val = str;
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

    public <T> T cast(Class<T> clazz){
        if(this.type == ValueType.VOID){
            throw new ValueCastException(this.type, clazz);
        }else if(this.type == ValueType.NULL){
            return null;
        }
        TypeConvertor convertor = javaTypeConvertorMap.get(clazz);
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
            return new Value(new RationalNumber(String.valueOf(obj)));
        }else if(obj instanceof BigDecimal){
            return new Value(new RationalNumber(((BigDecimal) obj).toPlainString()));
        }else if(obj instanceof BigInteger){
            return new Value(new RationalNumber(obj.toString()));
        }else if(obj instanceof RationalNumber){
            return new Value((RationalNumber)obj);
        }else if(obj instanceof IntegerNumber){
            return new Value(new RationalNumber((IntegerNumber)obj));
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
        if(val == null){ return null; }
        if(val instanceof RationalNumber){
            IntegerNumber intNum = ((RationalNumber) val).toInteger();
            if(intNum != null){
                return intNum.toPlainString();
            }else{
                String decimal = ((RationalNumber) val).toPlainString(10, NumberRoundingMode.HALF_UP);
                for(int i = decimal.length() - 1; i > 0; i--){
                    char ch = decimal.charAt(i);
                    if(ch != '0'){
                        return decimal.substring(0, i + 1);
                    }
                }
                throw new IllegalStateException(decimal + " format incorrect");
            }
        }
        return String.valueOf(val);
    }

    private interface TypeConvertor{
        Object convert(Value val);
    }

}
