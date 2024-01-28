package sch.frog.kit.win.extfun.code;

import org.apache.commons.codec.digest.DigestUtils;
import sch.frog.kit.lang.exception.ExecuteException;
import sch.frog.kit.lang.fun.IFunction;
import sch.frog.kit.lang.semantic.IExecuteContext;
import sch.frog.kit.lang.value.Value;
import sch.frog.kit.lang.value.ValueType;

public class Md5Function implements IFunction {

    @Override
    public String name() {
        return "md5";
    }

    @Override
    public String description() {
        return "计算指定内容的md5值";
    }

    @Override
    public Value execute(Value[] args, IExecuteContext context) throws ExecuteException {
        if(args.length != 1){
            throw new ExecuteException("md5 function expect 1 argument, but " + args.length);
        }
        Value arg = args[0];
        if(arg.getType() != ValueType.STRING){
            throw new ExecuteException("md5 function expect string argument, but " + arg.getType());
        }
        String text = arg.cast(String.class);
        String md5 = DigestUtils.md5Hex(text);
        return new Value(md5);
    }
}
