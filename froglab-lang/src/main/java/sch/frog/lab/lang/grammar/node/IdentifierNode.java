package sch.frog.lab.lang.grammar.node;

import sch.frog.lab.lang.exception.ExecuteException;
import sch.frog.lab.lang.grammar.IAstNode;
import sch.frog.lab.lang.grammar.IExpression;
import sch.frog.lab.lang.lexical.Token;
import sch.frog.lab.lang.lexical.TokenType;
import sch.frog.lab.lang.semantic.IExecuteContext;
import sch.frog.lab.lang.semantic.Reference;
import sch.frog.lab.lang.value.Value;

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
    public Reference evaluate(IExecuteContext context) throws ExecuteException {
        Value val = context.getVariableValue(this.identifier);
        return new Reference(val, v -> context.setVariable(this.identifier, v));
    }
}
