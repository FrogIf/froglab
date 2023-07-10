package sch.frog.kit.core.parse.grammar;

import sch.frog.kit.core.exception.GrammarException;
import sch.frog.kit.core.execute.ISession;
import sch.frog.kit.core.parse.lexical.Token;
import sch.frog.kit.core.parse.lexical.TokenType;
import sch.frog.kit.core.util.StringEscapeUtil;
import sch.frog.kit.core.value.Number;
import sch.frog.kit.core.value.Value;

public class ConstantGrammarNode extends AbstractGrammarNode {

    private final Value value;

    public ConstantGrammarNode(Token token) {
        super(token);
        String name = token.literal();
        TokenType tokenType = token.type();
        if(TokenType.BOOL == tokenType){
            value = new Value(Boolean.parseBoolean(name));
        }else if(TokenType.NUMBER == tokenType){
            value = new Value(new Number(name));
        }else if(TokenType.NULL == tokenType){
            value = null;
        }else if(TokenType.STRING == tokenType){
            value = new Value(StringEscapeUtil.fromString(token.literal()));
        }else{
            throw new IllegalArgumentException("type : " + tokenType.name() + " is not constant node");
        }
    }

    @Override
    public boolean add(Token token) throws GrammarException {
        return false;
    }

    @Override
    public void grammarCheck() throws GrammarException {
        // do nothing
    }

    @Override
    public Value evaluate(ISession session) {
        return value;
    }
}
