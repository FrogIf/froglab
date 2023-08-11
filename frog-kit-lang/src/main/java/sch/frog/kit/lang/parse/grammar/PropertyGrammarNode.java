package sch.frog.kit.lang.parse.grammar;

import sch.frog.kit.lang.exception.ExecuteException;
import sch.frog.kit.lang.exception.GrammarException;
import sch.frog.kit.lang.execute.IRuntimeContext;
import sch.frog.kit.lang.parse.lexical.Token;
import sch.frog.kit.lang.parse.lexical.TokenType;
import sch.frog.kit.lang.value.VMap;
import sch.frog.kit.lang.value.Value;
import sch.frog.kit.lang.value.ValueType;

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
    public Value succeedEvaluate(Value value, IRuntimeContext context){
        if(value.getType() != ValueType.OBJECT){
            throw new ExecuteException(value + " is not object but " + value.getType());
        }
        Value result = value.cast(VMap.class).get(this.propName);
        if(succeed == null){
            if(result == null){
                return Value.NULL;
            }
            return result;
        }else{
            return succeed.succeedEvaluate(result, context);
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
            succeed = GrammarNodeBuilder.buildForRef(token);
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
