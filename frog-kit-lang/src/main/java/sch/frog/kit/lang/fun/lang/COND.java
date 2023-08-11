package sch.frog.kit.lang.fun.lang;

import sch.frog.kit.lang.exception.ExecuteException;
import sch.frog.kit.lang.execute.IRuntimeContext;
import sch.frog.kit.lang.fun.IFunction;
import sch.frog.kit.lang.parse.grammar.IGrammarNode;
import sch.frog.kit.lang.value.Value;
import sch.frog.kit.lang.value.ValueType;

// cond(j1, s1, j2, s2, j3, s3)
public class COND implements IFunction {
    @Override
    public String name() {
        return "cond";
    }

    @Override
    public String description() {
        return null;
    }

    @Override
    public Value execute(Value[] args, IRuntimeContext context) {
        throw new UnsupportedOperationException("cond can't execute");
    }

    @Override
    public Value process(IGrammarNode[] children, IRuntimeContext context) {
        int len = children.length;
        if(len < 2){
            throw new ExecuteException("cond at lease have 2 arguments, but " + children.length);
        }
        IGrammarNode defaultNode = null;
        if(len % 2 == 1){   // 如果是奇数, 最后一个是default
            defaultNode = children[len - 1];
            len--;
        }
        for(int i = 0; i < len; i += 2){
            Value judge = children[i].evaluate(context);
            if(judge.getType() != ValueType.BOOL){
                throw new ExecuteException("judge argument must bool type, but " + judge.getType() + " at argument index " + i);
            }
            if(judge.cast(boolean.class)){
                return children[i + 1].evaluate(context);
            }
        }

        if(defaultNode != null){
            return defaultNode.evaluate(context);
        }
        return Value.VOID;
    }
}
