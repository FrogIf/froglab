package sch.frog.kit.lang.grammar.node;

import sch.frog.kit.lang.exception.ExecuteException;
import sch.frog.kit.lang.grammar.IAstNode;
import sch.frog.kit.lang.grammar.IExpression;
import sch.frog.kit.lang.lexical.Token;
import sch.frog.kit.lang.lexical.TokenType;
import sch.frog.kit.lang.semantic.IExecuteContext;
import sch.frog.kit.lang.value.Value;

import java.util.List;

public class IdentifierNode implements IExpression {

    private final String identifier;

    public IdentifierNode(Token token){
        if(token.type() != TokenType.IDENTIFIER){
            throw new IllegalArgumentException("identifier node type expect identifier but " + token.type());
        }
        this.identifier = token.literal();
    }

    @Override
    public String literal() {
        return identifier;
    }

    @Override
    public List<IAstNode> getChildren() {
        return null;
    }

    public String identifier(){
        return identifier;
    }

    @Override
    public Value evaluate(IExecuteContext context) throws ExecuteException {
        return context.getVariableValue(this.identifier);
    }
}
