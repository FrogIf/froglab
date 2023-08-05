package sch.frog.kit.win.extfun;

import org.apache.commons.codec.digest.DigestUtils;
import sch.frog.kit.core.exception.ExecuteException;
import sch.frog.kit.core.execute.IRuntimeContext;
import sch.frog.kit.core.fun.AbstractGeneralFunction;
import sch.frog.kit.core.value.Value;
import sch.frog.kit.core.value.ValueType;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

public class Md5Function extends AbstractGeneralFunction {
    @Override
    public String name() {
        return "md5";
    }

    @Override
    public String description() {
        return null;
    }

    @Override
    protected Value doExec(Value[] args, IRuntimeContext context) {
        if(args.length != 1){
            throw new ExecuteException("md5 function expect 1 argument, but " + args.length);
        }
        Value arg = args[0];
        if(arg.getType() != ValueType.STRING){
            throw new ExecuteException("md5 function expect string argument, but " + arg.getType());
        }
        String path = arg.to(String.class);
        File file = new File(path);
        if(!file.exists()){
            throw new ExecuteException("file not found for path :" + path);
        }
        try(
                BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file))
        ){
            String md5 = DigestUtils.md5Hex(inputStream);
            return new Value(md5);
        }catch (Exception e){
            throw new ExecuteException(e.getMessage());
        }
    }
}
