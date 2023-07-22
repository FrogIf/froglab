package sch.frog.kit.core.fun.lang;

import sch.frog.kit.core.exception.ExecuteException;
import sch.frog.kit.core.execute.ISession;
import sch.frog.kit.core.fun.FunctionDefine;
import sch.frog.kit.core.value.Number;
import sch.frog.kit.core.value.VList;
import sch.frog.kit.core.value.Value;
import sch.frog.kit.core.value.ValueType;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LangFunctionController {

    @FunctionDefine(name = "print")
    public Value print(Value arg, ISession session){
        // TODO log4j
        session.getOutput().write(arg.toString());
        return Value.VOID;
    }

    @FunctionDefine(name = "begin")
    public Value begin(Value[] args){
        if(args != null && args.length > 0){
            return args[args.length - 1];
        }
        return Value.VOID;
    }

    @FunctionDefine(name = "uuid")
    public Value uuid(){
        return new Value(java.util.UUID.randomUUID().toString());
    }

    @FunctionDefine(name = "uuidi")
    public Value uuidi(){
        return new Value(java.util.UUID.randomUUID().toString().replaceAll("-", ""));
    }

    @FunctionDefine(name = "upper")
    public Value upper(Value arg){
        if(arg.getType() != ValueType.STRING){
            throw new ExecuteException("upper function argument's type must string");
        }
        return new Value(arg.to(String.class).toUpperCase());
    }

    @FunctionDefine(name = "lower")
    public Value lower(Value arg){
        if(arg.getType() != ValueType.STRING){
            throw new ExecuteException("lower function argument's type must string");
        }
        return new Value(arg.to(String.class).toLowerCase());
    }

    @FunctionDefine(name = "now")
    public Value now(){
        return new Value(new Number(String.valueOf(System.currentTimeMillis())));
    }

    @FunctionDefine(name = "date")
    public Value date(Value[] args){
        if(args.length == 0){
            return new Value(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        }
        Value first = args[0];
        if(first.getType() != ValueType.NUMBER){
            throw new ExecuteException("first argument type expect NUMBER but " + first.getType().name());
        }
        long timeMillis = first.to(long.class);
        Date date = new Date();
        if(args.length == 1){
            date.setTime(timeMillis);
            String result = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
            return new Value(result);
        }else{
            date.setTime(timeMillis);
            String result = new SimpleDateFormat(args[1].toString()).format(date);
            return new Value(result);
        }
    }

    @FunctionDefine(name = "unicode")
    public Value unicode(Value arg){
        if(arg.getType() != ValueType.STRING){
            throw new ExecuteException("unicode function argument's type must string");
        }
        return new Value(encodeUnicode(arg.to(String.class)));
    }

    private String encodeUnicode(String origin){
        int len = origin.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char ch = origin.charAt(i);
            if (ch <= 127) {
                sb.append(ch);
            } else {
                String hex = Integer.toHexString(ch);
                sb.append("\\u");
                if (hex.length() < 4) {
                    sb.append("0000", hex.length(), 4);
                }
                sb.append(hex);
            }
        }
        return sb.toString();
    }

    @FunctionDefine(name = "unicodei")
    public Value unicodei(Value arg){
        if(arg.getType() != ValueType.STRING){
            throw new ExecuteException("invUnicode function argument's type must string");
        }
        return new Value(decodeUnicode(arg.to(String.class)));
    }

    private String decodeUnicode(String origin){
        int len = origin.length();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            char ch = origin.charAt(i);
            if (ch == '\\' && i + 5 < len && origin.charAt(i + 1) == 'u') {
                String hex = origin.substring(i + 2, i + 6);
                sb.append((char) Integer.parseInt(hex, 16));
                i += 5;
            } else {
                sb.append(ch);
            }
        }
        return sb.toString();
    }

    @FunctionDefine(name = "add")
    public Value add(Value[] args){
        if(args == null){
            throw new ExecuteException("add function at least one argument");
        }
        BigDecimal d = new BigDecimal(0);
        if(args.length == 1){
            Value val = args[0];
            if(val.getType() == ValueType.LIST){
                VList list = val.to(VList.class);
                for (Value v : list) {
                    if(v.getType() != ValueType.NUMBER){
                        throw new ExecuteException("list element must number type for add");
                    }
                    d = d.add(v.to(BigDecimal.class));
                }
            }else if(val.getType() == ValueType.NUMBER){
                return val;
            }else{
                throw new ExecuteException("add function's argument type must number or list, but " + val.getType());
            }
        }else{
            for (Value v : args) {
                if(v.getType() != ValueType.NUMBER){
                    throw new ExecuteException("list element must number type for add");
                }
                d = d.add(v.to(BigDecimal.class));
            }
        }
        return Value.of(d);
    }

    @FunctionDefine(name = "sub")
    public Value sub(Value[] args){
        if(args == null){
            throw new ExecuteException("sub function at least one argument");
        }
        BigDecimal d = new BigDecimal(0);
        boolean first = true;
        if(args.length == 1){
            Value val = args[0];
            if(val.getType() == ValueType.LIST){
                VList list = val.to(VList.class);
                for (Value v : list) {
                    if(v.getType() != ValueType.NUMBER){
                        throw new ExecuteException("list element must number type for add");
                    }
                    if(first){
                        d = d.add(v.to(BigDecimal.class));
                        first = false;
                    }else{
                        d = d.subtract(v.to(BigDecimal.class));
                    }
                }
            }else if(val.getType() == ValueType.NUMBER){
                return val;
            }else{
                throw new ExecuteException("add function's argument type must number or list, but " + val.getType());
            }
        }else{
            for (Value v : args) {
                if(v.getType() != ValueType.NUMBER){
                    throw new ExecuteException("list element must number type for add");
                }
                if(first){
                    d = d.add(v.to(BigDecimal.class));
                    first = false;
                }else{
                    d = d.subtract(v.to(BigDecimal.class));
                }
            }
        }
        return Value.of(d);
    }

    @FunctionDefine(name = "mul")
    public Value mul(Value[] args){
        if(args == null){
            throw new ExecuteException("add function at least one argument");
        }
        BigDecimal d = new BigDecimal(1);
        if(args.length == 1){
            Value val = args[0];
            if(val.getType() == ValueType.LIST){
                VList list = val.to(VList.class);
                for (Value v : list) {
                    if(v.getType() != ValueType.NUMBER){
                        throw new ExecuteException("list element must number type for add");
                    }
                    d = d.multiply(v.to(BigDecimal.class));
                }
            }else if(val.getType() == ValueType.NUMBER){
                return val;
            }else{
                throw new ExecuteException("add function's argument type must number or list, but " + val.getType());
            }
        }else{
            for (Value v : args) {
                if(v.getType() != ValueType.NUMBER){
                    throw new ExecuteException("list element must number type for add");
                }
                d = d.multiply(v.to(BigDecimal.class));
            }
        }
        return Value.of(d);
    }

    @FunctionDefine(name = "div")
    public Value div(Value[] args){
        if(args == null){
            throw new ExecuteException("sub function at least one argument");
        }
        BigDecimal d = new BigDecimal(1);
        boolean first = true;
        if(args.length == 1){
            Value val = args[0];
            if(val.getType() == ValueType.LIST){
                VList list = val.to(VList.class);
                for (Value v : list) {
                    if(v.getType() != ValueType.NUMBER){
                        throw new ExecuteException("list element must number type for add");
                    }
                    if(first){
                        d = d.multiply(v.to(BigDecimal.class));
                        first = false;
                    }else{
                        d = d.divide(v.to(BigDecimal.class));   // TODO 改成精度无损
                    }
                }
            }else if(val.getType() == ValueType.NUMBER){
                return val;
            }else{
                throw new ExecuteException("add function's argument type must number or list, but " + val.getType());
            }
        }else{
            for (Value v : args) {
                if(v.getType() != ValueType.NUMBER){
                    throw new ExecuteException("list element must number type for add");
                }
                if(first){
                    d = d.multiply(v.to(BigDecimal.class));
                    first = false;
                }else{
                    d = d.divide(v.to(BigDecimal.class));
                }
            }
        }
        return Value.of(d);
    }

}
