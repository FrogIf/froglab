package sch.frog.kit.lang.parse.grammar;

import io.github.frogif.calculator.number.impl.RationalNumber;
import sch.frog.kit.lang.execute.IRuntimeContext;
import sch.frog.kit.lang.parse.lexical.Token;
import sch.frog.kit.lang.parse.lexical.TokenType;
import sch.frog.kit.lang.util.StringEscapeUtil;
import sch.frog.kit.lang.value.Value;

public class ConstantGrammarNode extends AbstractGrammarNode {

    private final Value value;

    public ConstantGrammarNode(Token token) {
        super(token);
        String name = token.literal();
        TokenType tokenType = token.type();
        if(TokenType.BOOL == tokenType){
            value = new Value(Boolean.parseBoolean(name));
        }else if(TokenType.NUMBER == tokenType){
            value = new Value(new RationalNumber(name));
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
    public Value evaluate(IRuntimeContext context) {
        return value;
    }
}
