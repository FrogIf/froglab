package sch.frog.lab.lang.grammar.node;

import sch.frog.lab.lang.grammar.IAstNode;
import sch.frog.lab.lang.lexical.Token;
import sch.frog.lab.lang.lexical.TokenType;

import java.util.List;

public class EntryKey implements IAstNode {

    private final Token token;

    private final String key;

    public EntryKey(Token token) {
        if(token.type() == TokenType.STRING){
            String t = token.literal();
            this.key = t.substring(1, t.length() - 1);
        }else if(token.type() == TokenType.IDENTIFIER){
            this.key = token.literal();
        }else{
            throw new IllegalArgumentException("token type must string or identifier");
        }
        this.token = token;
    }

    public String key(){
        return key;
    }

    @Override
    public String literal() {
        return token.literal();
    }

    @Override
    public List<IAstNode> getChildren() {
        return null;
    }
}
