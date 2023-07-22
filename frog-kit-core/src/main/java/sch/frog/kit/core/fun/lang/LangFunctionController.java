package sch.frog.kit.core.fun.lang;

import sch.frog.kit.core.exception.ExecuteException;
import sch.frog.kit.core.execute.ISession;
import sch.frog.kit.core.fun.FunctionDefine;
import sch.frog.kit.core.value.Number;
import sch.frog.kit.core.value.VList;
import sch.frog.kit.core.value.Value;
import sch.frog.kit.core.value.ValueType;

import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

public class LangFunctionController {

    @FunctionDefine(name = "print")
    public Value print(Value[] args, ISession session){
        if(args.length == 0){
            throw new ExecuteException("print at least 1 argument");
        }
        Value log = args[0];
        if(log.getType() != ValueType.STRING){
            throw new ExecuteException("print first argument must string, but " + log.getType());
        }
        if(args.length == 1){
            session.getOutput().write(log.toString());
        }else{
            String logExp = log.to(String.class);
            StringBuilder sb = new StringBuilder();
            boolean escape = false;
            int index = 1;
            for(int i = 0, len = logExp.length(); i < len; i++){
                char ch = logExp.charAt(i);
                if(ch == '\\'){
                    escape = true;
                }else{
                    if(escape){
                        if(ch == '{' || ch == '}'){
                            sb.append(ch);
                        }else{
                            sb.append('\\');
                        }
                    }else{
                        if(ch == '{' && i + 1 < len && logExp.charAt(i + 1) == '}'){
                            if(args.length > index){
                                sb.append(args[index++]);
                            }else{
                                sb.append("{}");
                            }
                            i++;
                        }else{
                            sb.append(ch);
                        }
                    }
                    escape = false;
                }
            }
            if(escape){ sb.append('\\'); }
            session.getOutput().write(sb.toString());
        }
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

    @FunctionDefine(name = "timestamp")
    public Value timestamp(Value[] args){
        if(args.length < 1){
            throw new ExecuteException("timestamp at least have 1 argument");
        }
        if(args.length > 2){
            throw new ExecuteException("timestamp at most have 2 argument");
        }
        Value date = args[0];
        if(date.getType() != ValueType.STRING){
            throw new ExecuteException("timestamp first argument must string, but " + date.getType());
        }
        String dateStr = date.to(String.class);
        String pattern = "yyyy-MM-dd HH:mm:ss";
        if(args.length == 2){
            Value patternVal = args[1];
            if(patternVal.getType() != ValueType.STRING){
                throw new ExecuteException("timestamp second argument must string, but " + date.getType());
            }
            pattern = patternVal.to(String.class);
        }
        Number num;
        try {
            num = new Number(new SimpleDateFormat(pattern).parse(dateStr).getTime());
        } catch (ParseException e) {
            throw new ExecuteException(dateStr + " convert to timestamp failed with pattern " + pattern);
        }
        return new Value(num);
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

    @FunctionDefine(name = "base64")
    public Value base64(Value arg){
        if(arg.getType() != ValueType.STRING){
            throw new ExecuteException("base64 argument type must string, but " + arg.getType());
        }
        String s = Base64.getEncoder().encodeToString(arg.to(String.class).getBytes(StandardCharsets.UTF_8));
        return new Value(s);
    }

    @FunctionDefine(name = "base64i")
    public Value base64i(Value arg){
        if(arg.getType() != ValueType.STRING){
            throw new ExecuteException("base64i argument type must string, but " + arg.getType());
        }
        String s = new String(Base64.getDecoder().decode(arg.to(String.class).getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
        return new Value(s);
    }

    @FunctionDefine(name = "hex")
    public Value hex(Value arg){
        if(arg.getType() != ValueType.STRING){
            throw new ExecuteException("hex argument type must string, but " + arg.getType());
        }
        byte[] bytes = arg.to(String.class).getBytes(StandardCharsets.UTF_8);
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
            sb.append(' ');
        }
        return new Value(sb.toString());
    }

    @FunctionDefine(name = "hexi")
    public Value hexi(Value arg){
        if(arg.getType() != ValueType.STRING){
            throw new ExecuteException("hexi argument type must string, but " + arg.getType());
        }
        String content = arg.to(String.class);
        content = content.replaceAll(" ", "");
        int len = content.length();
        byte[] bytes = new byte[len >> 1];
        for(int j = 0, i = 0; j < bytes.length; i += 2, j++){
            bytes[j] = (byte)Integer.parseInt(content.substring(i, i + 2), 16);
        }
        return new Value(new String(bytes, StandardCharsets.UTF_8));
    }

    @FunctionDefine(name = "url_encode")
    public Value urlEncode(Value arg){
        if(arg.getType() != ValueType.STRING){
            throw new ExecuteException("url_encode argument type must string, but " + arg.getType());
        }
        return new Value(URLEncoder.encode(arg.to(String.class), StandardCharsets.UTF_8));
    }

    @FunctionDefine(name = "url_decode")
    public Value urlDecode(Value arg){
        if(arg.getType() != ValueType.STRING){
            throw new ExecuteException("url_decode argument type must string, but " + arg.getType());
        }
        return new Value(URLDecoder.decode(arg.to(String.class), StandardCharsets.UTF_8));
    }

}
