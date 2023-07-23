package sch.frog.kit.core.parse.grammar;

import sch.frog.kit.core.exception.ExecuteException;
import sch.frog.kit.core.exception.GrammarException;
import sch.frog.kit.core.execute.ISession;
import sch.frog.kit.core.fun.IFunction;
import sch.frog.kit.core.parse.lexical.Token;
import sch.frog.kit.core.value.Value;
import sch.frog.kit.core.value.ValueType;

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
    public Value succeedEvaluate(Value value, ISession session){
        if(value.getType() != ValueType.FUNCTION){
            throw new ExecuteException(value.toString() + " is not a function");
        }
        IFunction fun = value.to(IFunction.class);
        Value[] args = new Value[list.size()];
        for (int i = 0; i < args.length; i++){
            args[i] = list.get(i).evaluate(session);
        }
        Value result = fun.execute(args, session);
        if(succeed == null){
            return result;
        }else{
            return succeed.succeedEvaluate(result, session);
        }
    }
}
