package sch.frog.kit.core.parse.grammar;

import sch.frog.kit.core.exception.ExecuteException;
import sch.frog.kit.core.exception.GrammarException;
import sch.frog.kit.core.execute.ISession;
import sch.frog.kit.core.parse.lexical.Token;
import sch.frog.kit.core.value.VList;
import sch.frog.kit.core.value.Value;
import sch.frog.kit.core.value.ValueType;

// aaa[0]()
public class ArrayIndexGrammarNode extends AbstractLeftAssociativeGrammarNode {

    private IGrammarNode indexNode;

    private AbstractLeftAssociativeGrammarNode succeed;   // 后继节点

    private boolean selfFinish = false;

    private boolean close = false;

    public ArrayIndexGrammarNode(Token token) {
        super(token);
        if(!"[".equals(token.literal())){
            throw new IllegalArgumentException("array index must start with [");
        }
    }

    @Override
    public Value succeedEvaluate(Value value, ISession session){
        if(value.getType() != ValueType.LIST){
            throw new ExecuteException(value + " is not list but " + value.getType());
        }
        Value index = indexNode.evaluate(session);
        if(index.getType()!= ValueType.NUMBER){
            throw new ExecuteException(indexNode.name() + " result is not number but " + index.getType());
        }
        Integer i = index.to(int.class);
        Value result = value.to(VList.class).get(i);
        if(succeed == null){
            return result;
        }else{
            return succeed.succeedEvaluate(result, session);
        }
    }

    @Override
    public boolean add(Token token) throws GrammarException {
        if(this.close){ return false; }
        if(selfFinish){
            if(succeed == null){
                if("[".equals(token.literal())){
                    succeed = new ArrayIndexGrammarNode(token);
                }else if("(".equals(token.literal())){
                    succeed = new ArgumentListGrammarNode(token);
                }else if(".".equals(token.literal())){
                    succeed = new PropertyGrammarNode(token);
                }else{
                    throw new GrammarException(token);
                }
                return true;
            }else{
                return succeed.add(token);
            }
        }
        if(indexNode != null){
            boolean success = indexNode.add(token);
            if(!success){
                if("]".equals(token.literal())){
                    this.selfFinish = true;
                    success = true;
                }else if(")".equals(token.literal())){
                    this.selfFinish = true;
                    success = true;
                    this.close = true;
                }
            }
            return success;
        }else{
            indexNode = GeneralGrammarNodeBuilder.buildForValuable(token);
            if(indexNode == null){
                throw new GrammarException(token);
            }
            return true;
        }
    }

    @Override
    public void grammarCheck() throws GrammarException {
        if(!selfFinish){
            throw new GrammarException(super.token);
        }
        if(succeed != null){
            succeed.grammarCheck();
        }
    }
}
