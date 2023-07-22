package sch.frog.kit.core.parse.grammar;

import sch.frog.kit.core.parse.lexical.Token;
import sch.frog.kit.core.parse.lexical.TokenType;

public class GeneralGrammarNodeBuilder {

    public static IGrammarNode buildForValuable(Token token){
        TokenType type = token.type();
        IGrammarNode node = null;
        if(type == TokenType.STRUCT){
            node = GeneralGrammarNodeBuilder.buildForObject(token);
        }else if(type == TokenType.IDENTIFIER){
            node = new IdentifierGrammarNode(token);
        }else{
            node = GeneralGrammarNodeBuilder.buildForConstant(token);
        }
        return node;
    }

    public static IGrammarNode buildForObject(Token token){
        if(token.type() == TokenType.STRUCT){
            IGrammarNode node = null;
            String literal = token.literal();
            if("{".equals(literal)){
                node = new ObjectGrammarNode(token);
            }else if("[".equals(literal)){
                node = new ObjectGrammarNode(token);
            }else if("(".equals(literal)){
                node = new FunctionDefGrammarNode(token);
            }
            return node;
        }
        return null;
    }

    public static IGrammarNode buildForConstant(Token token){
        if(token.type() == TokenType.BOOL || token.type() == TokenType.NUMBER || token.type() == TokenType.STRING || token.type() == TokenType.NULL){
            return new ConstantGrammarNode(token);
        }
        return null;
    }
}
