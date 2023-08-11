package sch.frog.kit.lang.parse.grammar;

import sch.frog.kit.lang.parse.lexical.Token;
import sch.frog.kit.lang.parse.lexical.TokenType;

public class GrammarNodeBuilder {

    // 构建引用: a.b.c  aaa()   aa[0]
    public static AbstractLeftAssociativeGrammarNode buildForRef(Token token){
        AbstractLeftAssociativeGrammarNode node = null;
        if("(".equals(token.literal())){
            node = new ArgumentListGrammarNode(token);
        }else if(".".equals(token.literal())){
            node = new PropertyGrammarNode(token);
        }else if("[".equals(token.literal())){
            node = new ArrayIndexGrammarNode(token);
        }
        return node;
    }

    public static IGrammarNode buildForValuable(Token token){
        TokenType type = token.type();
        IGrammarNode node;
        if(type == TokenType.STRUCT){
            node = GrammarNodeBuilder.buildForObject(token);
        }else if(type == TokenType.IDENTIFIER){
            node = new IdentifierGrammarNode(token);
        }else{
            node = GrammarNodeBuilder.buildForConstant(token);
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
                node = new FunctionObjectGrammarNode(token);
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
