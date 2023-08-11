package sch.frog.kit.lang.parse.grammar;

import sch.frog.kit.lang.exception.ExecuteException;
import sch.frog.kit.lang.exception.GrammarException;
import sch.frog.kit.lang.execute.IRuntimeContext;
import sch.frog.kit.lang.fun.IFunction;
import sch.frog.kit.lang.parse.lexical.Token;
import sch.frog.kit.lang.value.Value;
import sch.frog.kit.lang.value.ValueType;

import java.util.ArrayList;

public class ArgumentListGrammarNode extends AbstractLeftAssociativeGrammarNode {

    private final ArrayList<IGrammarNode> list = new ArrayList<>();

    private AbstractLeftAssociativeGrammarNode succeed;

    private boolean selfFinish = false;

    private boolean closed = false;

    public ArgumentListGrammarNode(Token token) {
        super(token);
        if(!"(".equals(token.literal())){
            throw new IllegalArgumentException("argument list must start with (");
        }
    }

    private IGrammarNode cursor = null;

    @Override
    public boolean add(Token token) throws GrammarException {
        if(closed){ return false; }
        if(selfFinish){
            boolean success;
            if(succeed == null){
                success = (succeed = GrammarNodeBuilder.buildForRef(token)) != null;
            }else{
                success = succeed.add(token);
            }
            this.closed = !success;
            return success;
        }else{
            if(cursor != null && cursor.add(token)){
                return true;
            }
            if(",".equals(token.literal())){    // 分割, 下一个
                if(cursor == null && list.size() == 0){ throw new GrammarException(token); }
                cursor = null;
                return true;
            }else if(")".equals(token.literal())){ // 闭合
                this.selfFinish = true;
                return true;
            }else if(this.cursor == null){
                IGrammarNode node = GrammarNodeBuilder.buildForValuable(token);
                if(node == null){ throw new GrammarException(token); }
                this.cursor = node;
                list.add(node);
                return true;
            }
            this.closed = true;
            return false;
        }
    }

    @Override
    public void grammarCheck() throws GrammarException {
        if(!this.selfFinish){
            throw new GrammarException(token);
        }
        for (IGrammarNode node : list) {
            node.grammarCheck();
        }
    }

    @Override
    public Value succeedEvaluate(Value value, IRuntimeContext context){
        if(value.getType() != ValueType.FUNCTION){
            throw new ExecuteException(value + " is not a function");
        }
        IFunction fun = value.cast(IFunction.class);
        Value result = fun.process(list.toArray(IGrammarNode[]::new), context);
        if(succeed == null){
            return result;
        }else{
            return succeed.succeedEvaluate(result, context);
        }
    }
}
