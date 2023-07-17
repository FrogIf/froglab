package sch.frog.kit.core.parse.grammar;

import sch.frog.kit.core.exception.GrammarException;
import sch.frog.kit.core.parse.lexical.Token;
import sch.frog.kit.core.parse.lexical.TokenType;

import java.util.Iterator;
import java.util.List;

/**
 * 语法分析
 */
public class GrammarAnalyzer {

    public IGrammarNode getGrammarTree(List<Token> tokens) throws GrammarException {
        IGrammarNode rootNode = null;
        Iterator<Token> tokenItr = tokens.iterator();
        Token root = tokenItr.next();
        TokenType type = root.type();
        if(type == TokenType.STRUCT){
            rootNode = GeneralGrammarNodeBuilder.buildForObject(root);
        }else if(type == TokenType.IDENTIFIER){
            rootNode = new IdentifierGrammarNode(root);
        }else{
            rootNode = GeneralGrammarNodeBuilder.buildForConstant(root);
        }
        if(rootNode == null){
            throw new GrammarException(root);
        }
        while(tokenItr.hasNext()){
            Token t = tokenItr.next();
            if(!rootNode.add(t)){
                throw new GrammarException(t);
            }
        }

        rootNode.grammarCheck();
        return rootNode;
    }

}
