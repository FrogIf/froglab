package sch.frog.kit.core.fun.lang;

import sch.frog.calculator.cell.CellCalculator;
import sch.frog.calculator.number.ComplexNumber;
import sch.frog.calculator.number.NumberRoundingMode;
import sch.frog.calculator.number.RationalNumber;
import sch.frog.kit.core.exception.ExecuteException;
import sch.frog.kit.core.execute.IRuntimeContext;
import sch.frog.kit.core.execute.ISession;
import sch.frog.kit.core.fun.FunctionDefine;
import sch.frog.kit.core.fun.IFunction;
import sch.frog.kit.core.value.VList;
import sch.frog.kit.core.value.Value;
import sch.frog.kit.core.value.ValueType;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;

public class LangFunctionController {

    @FunctionDefine(name = "print")
    public void print(Value[] args, IRuntimeContext context){
        writeToOutput(args, context, false);
    }

    @FunctionDefine(name = "println")
    public void println(Value[] args, IRuntimeContext context){
        writeToOutput(args, context, true);
    }

    private void writeToOutput(Value[] args, IRuntimeContext context, boolean newline){
        if(args.length == 0){
            throw new ExecuteException("print at least 1 argument");
        }
        Value log = args[0];
        if(log.getType() != ValueType.STRING){
            throw new ExecuteException("print first argument must string, but " + log.getType());
        }
        if(args.length == 1){
            context.getOutput().write(log + (newline ? "\n" : ""));
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
            context.getOutput().write(sb + (newline ? "\n" : ""));
        }
    }

    @FunctionDefine(name = "exec")
    public Value exec(Value[] args){
        if(args != null && args.length > 0){
            return args[args.length - 1];
        }
        return Value.VOID;
    }

    @FunctionDefine(name = "lambda")
    public Value lambda(Value arg){
        if(arg.getType() != ValueType.FUNCTION){
            throw new ExecuteException("lambda argument type must function, but " + arg.getType());
        }
        return arg;
    }

    @FunctionDefine(name = "uuid")
    public String uuid(){
        return java.util.UUID.randomUUID().toString();
    }

    @FunctionDefine(name = "uuidi")
    public String uuidi(){
        return java.util.UUID.randomUUID().toString().replaceAll("-", "");
    }

    @FunctionDefine(name = "upper")
    public String upper(Value arg){
        if(arg.getType() != ValueType.STRING){
            throw new ExecuteException("upper function argument's type must string");
        }
        return arg.to(String.class).toUpperCase();
    }

    @FunctionDefine(name = "lower")
    public String lower(Value arg){
        if(arg.getType() != ValueType.STRING){
            throw new ExecuteException("lower function argument's type must string");
        }
        return arg.to(String.class).toLowerCase();
    }

    @FunctionDefine(name = "now")
    public RationalNumber now(){
        return new RationalNumber(String.valueOf(System.currentTimeMillis()));
    }

    @FunctionDefine(name = "timestamp")
    public RationalNumber timestamp(Value[] args){
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
        RationalNumber num;
        try {
            num = new RationalNumber(String.valueOf(new SimpleDateFormat(pattern).parse(dateStr).getTime()));
        } catch (ParseException e) {
            throw new ExecuteException(dateStr + " convert to timestamp failed with pattern " + pattern);
        }
        return num;
    }

    @FunctionDefine(name = "date")
    public String date(Value[] args){
        if(args.length == 0){
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        }
        Value first = args[0];
        if(first.getType() != ValueType.NUMBER){
            throw new ExecuteException("first argument type expect NUMBER but " + first.getType().name());
        }
        long timeMillis = first.to(long.class);
        Date date = new Date();
        if(args.length == 1){
            date.setTime(timeMillis);
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
        }else{
            date.setTime(timeMillis);
            return new SimpleDateFormat(args[1].toString()).format(date);
        }
    }

    @FunctionDefine(name = "add")
    public RationalNumber add(Value[] args){
        if(args == null){
            throw new ExecuteException("add function at least one argument");
        }
        RationalNumber d = RationalNumber.ZERO;
        if(args.length == 1){
            Value val = args[0];
            if(val.getType() == ValueType.LIST){
                VList list = val.to(VList.class);
                for (Value v : list) {
                    if(v.getType() != ValueType.NUMBER){
                        throw new ExecuteException("list element must number type for add");
                    }
                    d = d.add(v.to(RationalNumber.class));
                }
            }else if(val.getType() == ValueType.NUMBER){
                return val.to(RationalNumber.class);
            }else{
                throw new ExecuteException("add function's argument type must number or list, but " + val.getType());
            }
        }else{
            for (Value v : args) {
                if(v.getType() != ValueType.NUMBER){
                    throw new ExecuteException("list element must number type for add");
                }
                d = d.add(v.to(RationalNumber.class));
            }
        }
        return d;
    }

    @FunctionDefine(name = "sub")
    public RationalNumber sub(Value[] args){
        if(args == null){
            throw new ExecuteException("sub function at least one argument");
        }
        RationalNumber d = RationalNumber.ZERO;
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
                        d = d.add(v.to(RationalNumber.class));
                        first = false;
                    }else{
                        d = d.sub(v.to(RationalNumber.class));
                    }
                }
            }else if(val.getType() == ValueType.NUMBER){
                return val.to(RationalNumber.class);
            }else{
                throw new ExecuteException("add function's argument type must number or list, but " + val.getType());
            }
        }else{
            for (Value v : args) {
                if(v.getType() != ValueType.NUMBER){
                    throw new ExecuteException("list element must number type for add");
                }
                if(first){
                    d = d.add(v.to(RationalNumber.class));
                    first = false;
                }else{
                    d = d.sub(v.to(RationalNumber.class));
                }
            }
        }
        return d;
    }

    @FunctionDefine(name = "mul")
    public RationalNumber mul(Value[] args){
        if(args == null){
            throw new ExecuteException("add function at least one argument");
        }
        RationalNumber d = RationalNumber.ONE;
        if(args.length == 1){
            Value val = args[0];
            if(val.getType() == ValueType.LIST){
                VList list = val.to(VList.class);
                for (Value v : list) {
                    if(v.getType() != ValueType.NUMBER){
                        throw new ExecuteException("list element must number type for add");
                    }
                    d = d.mult(v.to(RationalNumber.class));
                }
            }else if(val.getType() == ValueType.NUMBER){
                return val.to(RationalNumber.class);
            }else{
                throw new ExecuteException("add function's argument type must number or list, but " + val.getType());
            }
        }else{
            for (Value v : args) {
                if(v.getType() != ValueType.NUMBER){
                    throw new ExecuteException("list element must number type for add");
                }
                d = d.mult(v.to(RationalNumber.class));
            }
        }
        return d;
    }

    @FunctionDefine(name = "div")
    public RationalNumber div(Value[] args){
        if(args == null){
            throw new ExecuteException("sub function at least one argument");
        }
        RationalNumber d = RationalNumber.ONE;
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
                        d = d.mult(v.to(RationalNumber.class));
                        first = false;
                    }else{
                        d = d.div(v.to(RationalNumber.class));   // TODO 改成精度无损
                    }
                }
            }else if(val.getType() == ValueType.NUMBER){
                return val.to(RationalNumber.class);
            }else{
                throw new ExecuteException("add function's argument type must number or list, but " + val.getType());
            }
        }else{
            for (Value v : args) {
                if(v.getType() != ValueType.NUMBER){
                    throw new ExecuteException("list element must number type for add");
                }
                if(first){
                    d = d.mult(v.to(RationalNumber.class));
                    first = false;
                }else{
                    d = d.div(v.to(RationalNumber.class));
                }
            }
        }
        return d;
    }

    @FunctionDefine(name = "unicode")
    public String unicode(Value arg){
        if(arg.getType() != ValueType.STRING){
            throw new ExecuteException("unicode function argument's type must string");
        }
        return encodeUnicode(arg.to(String.class));
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
    public String unicodei(Value arg){
        if(arg.getType() != ValueType.STRING){
            throw new ExecuteException("invUnicode function argument's type must string");
        }
        return decodeUnicode(arg.to(String.class));
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
    public String base64(Value arg){
        if(arg.getType() != ValueType.STRING){
            throw new ExecuteException("base64 argument type must string, but " + arg.getType());
        }
        return Base64.getEncoder().encodeToString(arg.to(String.class).getBytes(StandardCharsets.UTF_8));
    }

    @FunctionDefine(name = "base64i")
    public String base64i(Value arg){
        if(arg.getType() != ValueType.STRING){
            throw new ExecuteException("base64i argument type must string, but " + arg.getType());
        }
        return new String(Base64.getDecoder().decode(arg.to(String.class).getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
    }

    @FunctionDefine(name = "hex")
    public String hex(Value arg){
        if(arg.getType() != ValueType.STRING){
            throw new ExecuteException("hex argument type must string, but " + arg.getType());
        }
        byte[] bytes = arg.to(String.class).getBytes(StandardCharsets.UTF_8);
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
            sb.append(' ');
        }
        return sb.toString();
    }

    @FunctionDefine(name = "hexi")
    public String hexi(Value arg){
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
        return new String(bytes, StandardCharsets.UTF_8);
    }

    @FunctionDefine(name = "url_encode")
    public String urlEncode(Value arg){
        if(arg.getType() != ValueType.STRING){
            throw new ExecuteException("url_encode argument type must string, but " + arg.getType());
        }
        return URLEncoder.encode(arg.to(String.class), StandardCharsets.UTF_8);
    }

    @FunctionDefine(name = "url_decode")
    public String urlDecode(Value arg){
        if(arg.getType() != ValueType.STRING){
            throw new ExecuteException("url_decode argument type must string, but " + arg.getType());
        }
        return URLDecoder.decode(arg.to(String.class), StandardCharsets.UTF_8);
    }

    private final CellCalculator cellCalculator = new CellCalculator();

    @FunctionDefine(name = "calc")
    public RationalNumber calc(Value expression){
        if(expression.getType() != ValueType.STRING){
            throw new ExecuteException("calc must input string expression");
        }
        ComplexNumber number = cellCalculator.calculate(expression.to(String.class));
        RationalNumber rational = number.toRational();
        if(rational == null){
            throw new ExecuteException("result is not rational number, system unsupported.");
        }
        return rational;
    }

    @FunctionDefine(name = "numScale")
    public String numScale(Value num, Value mode, Value scale){
        if(num.getType() != ValueType.NUMBER){
            throw new ExecuteException("numScale first argument must number, but " + num.getType());
        }
        if(mode.getType() != ValueType.STRING){
            throw new ExecuteException("numScale second argument must string, but " + num.getType());
        }
        if(scale.getType() != ValueType.NUMBER){
            throw new ExecuteException("numScale third argument must number, but " + num.getType());
        }
        return num.to(RationalNumber.class).toPlainString(scale.to(int.class), NumberRoundingMode.valueOf(mode.to(String.class)));
    }

    @FunctionDefine(name = "funs")
    public String funs(Value[] args, IRuntimeContext context){
        ISession session = context.getSession();
        StringBuilder sb = new StringBuilder();
        if(args.length == 0){
            Collection<IFunction> functions = session.getFunctions();
            boolean start = true;
            for (IFunction function : functions) {
                if(!start){
                    sb.append(',');
                }
                sb.append(function.name());
                start = false;
            }
        }else{
            boolean start = true;
            for (Value arg : args) {
                if(arg.getType() != ValueType.STRING){ throw new ExecuteException("funs arguments type must string"); }
                Collection<IFunction> functions = session.getFunctions(arg.to(String.class));
                for (IFunction function : functions) {
                    if(!start){
                        sb.append(',');
                    }
                    sb.append(function.name());
                    start = false;
                }
            }
        }
        return sb.toString();
    }

}
