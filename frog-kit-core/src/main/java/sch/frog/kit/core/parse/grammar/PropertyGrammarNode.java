package sch.frog.kit.core.parse.grammar;

import sch.frog.kit.core.exception.ExecuteException;
import sch.frog.kit.core.exception.GrammarException;
import sch.frog.kit.core.execute.ISession;
import sch.frog.kit.core.parse.lexical.Token;
import sch.frog.kit.core.parse.lexical.TokenType;
import sch.frog.kit.core.value.VMap;
import sch.frog.kit.core.value.Value;
import sch.frog.kit.core.value.ValueType;

public class PropertyGrammarNode extends AbstractLeftAssociativeGrammarNode {

    private String propName;

    private AbstractLeftAssociativeGrammarNode succeed;

    private boolean closed = false;

    public PropertyGrammarNode(Token token) {
        super(token);
        if(!".".equals(token.literal())){
            throw new IllegalArgumentException("property must start with .");
        }
    }

    @Override
    public Value succeedEvaluate(Value value, ISession session){
        if(value.getType() != ValueType.OBJECT){
            throw new ExecuteException(value + " is not object but " + value.getType());
        }
        Value result = value.to(VMap.class).get(this.propName);
        if(succeed == null){
            if(result == null){
                return Value.NULL;
            }
            return result;
        }else{
            return succeed.succeedEvaluate(result, session);
        }
    }

    @Override
    public boolean add(Token token) throws GrammarException {
        if(this.closed){ return false; }
        if(propName == null){
            if(token.type() != TokenType.IDENTIFIER){
                throw new GrammarException(token);
            }
            propName = token.literal();
            return true;
        }else if(succeed != null){
            this.closed = !succeed.add(token);
        }else{
            succeed = GeneralGrammarNodeBuilder.buildForRef(token);
            this.closed = succeed == null;
        }
        return !this.closed;
    }

    @Override
    public void grammarCheck() throws GrammarException {
        if(this.propName == null){
            throw new GrammarException(token);
        }
        if(this.succeed != null){
            this.succeed.grammarCheck();
        }
    }
}
