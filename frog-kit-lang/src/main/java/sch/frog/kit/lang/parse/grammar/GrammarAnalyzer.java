package sch.frog.kit.lang.parse.grammar;

import sch.frog.kit.lang.exception.GrammarException;
import sch.frog.kit.lang.parse.lexical.Token;
import sch.frog.kit.lang.parse.lexical.TokenType;

import java.util.Iterator;
import java.util.List;

/**
 * 语法分析
 */
public class GrammarAnalyzer {

    public IGrammarNode getGrammarTree(List<Token> tokens) throws GrammarException {
        IGrammarNode rootNode = null;
        Iterator<Token> tokenItr = tokens.iterator();
        Token root = null;
        while(tokenItr.hasNext()){
            root = tokenItr.next();
            if(root.type() != TokenType.COMMENT){ break; }
            else{ root = null; }
        }
        if(root == null){ return new EmptyGrammarNode(); }

        TokenType type = root.type();
        if(type == TokenType.STRUCT){
            rootNode = GrammarNodeBuilder.buildForObject(root);
        }else if(type == TokenType.IDENTIFIER){
            rootNode = new IdentifierGrammarNode(root);
        }else{
            rootNode = GrammarNodeBuilder.buildForConstant(root);
        }
        if(rootNode == null){
            throw new GrammarException(root);
        }
        while(tokenItr.hasNext()){
            Token t = tokenItr.next();
            if(t.type() == TokenType.COMMENT){ continue; }  // 跳过注释
            if(!rootNode.add(t)){
                throw new GrammarException(t);
            }
        }

        rootNode.grammarCheck();
        return rootNode;
    }

}
