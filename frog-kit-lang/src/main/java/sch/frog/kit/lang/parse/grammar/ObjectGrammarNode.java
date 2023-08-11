package sch.frog.kit.lang.parse.grammar;

import sch.frog.kit.lang.exception.GrammarException;
import sch.frog.kit.lang.execute.IRuntimeContext;
import sch.frog.kit.lang.parse.common.TokenRuleUtil;
import sch.frog.kit.lang.parse.lexical.Token;
import sch.frog.kit.lang.parse.lexical.TokenType;
import sch.frog.kit.lang.parse.lexical.TokenUtil;
import sch.frog.kit.lang.util.StringEscapeUtil;
import sch.frog.kit.lang.value.VList;
import sch.frog.kit.lang.value.VMap;
import sch.frog.kit.lang.value.Value;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class ObjectGrammarNode extends AbstractGrammarNode{

    private LinkedHashMap<String, IGrammarNode> map = null;

    private ArrayList<IGrammarNode> list = null;

    private final GrammarBuilder builder;

    public ObjectGrammarNode(Token token) {
        super(token);
        if("[".equals(token.literal())){
            list = new ArrayList<>();
            builder = new ListBuilder();
        }else if("{".equals(token.literal())){
            map = new LinkedHashMap<>();
            builder = new ObjectBuilder();
        }else {
            builder = null;
        }
    }

    @Override
    public boolean add(Token token) throws GrammarException {
        return builder.add(token);
    }

    @Override
    public void grammarCheck() throws GrammarException {
        if(!builder.closed()){
            throw new GrammarException("object is not closed for : " + this.name(), token.pos());
        }
        if(map != null){
            for (IGrammarNode value : map.values()) {
                value.grammarCheck();
            }
        }else if(list != null){
            for (IGrammarNode node : list) {
                node.grammarCheck();
            }
        }
    }

    @Override
    public Value evaluate(IRuntimeContext context) {
        if(map != null){
            VMap obj = new VMap();
            for (Map.Entry<String, IGrammarNode> entry : map.entrySet()) {
                obj.put(entry.getKey(), entry.getValue().evaluate(context));
            }
            return new Value(obj);
        }else{
            VList l = new VList();
            for (IGrammarNode node : list) {
                l.add(node.evaluate(context));
            }
            return new Value(l);
        }
    }

    private interface GrammarBuilder{
        boolean add(Token token) throws GrammarException;

        boolean closed();
    }

    private class ListBuilder implements GrammarBuilder{

        private IGrammarNode activeNode;

        private boolean closed = false;

        @Override
        public boolean add(Token token) throws GrammarException {
            if(closed){ return false; }
            if(activeNode != null){
                if(!activeNode.add(token)){
                    if(",".equals(token.literal())){
                        activeNode = null;
                    }else if("]".equals(token.literal())){
                        this.closed = true;
                    }else{
                        throw new GrammarException(token);
                    }
                }
                return true;
            }else{
                if("]".equals(token.literal())){
                    if(!ObjectGrammarNode.this.list.isEmpty()){
                        // 异常: [aaa,]
                        throw new GrammarException(token);
                    }
                    this.closed = true;
                    return true;
                }else if(token.type() == TokenType.STRUCT){
                    this.activeNode = GrammarNodeBuilder.buildForObject(token);
                    if(this.activeNode == null){
                        throw new GrammarException(token);
                    }
                }else if(token.type() == TokenType.IDENTIFIER){
                    this.activeNode = new IdentifierGrammarNode(token);
                }else if(TokenUtil.isConstant(token.type())){
                    this.activeNode = GrammarNodeBuilder.buildForConstant(token);
                }else{
                    throw new GrammarException(token);
                }
                if(this.activeNode == null){
                    throw new GrammarException(token);
                }else{
                    ObjectGrammarNode.this.list.add(this.activeNode);
                    return true;
                }
            }
        }

        @Override
        public boolean closed() {
            return this.closed;
        }
    }

    private class ObjectBuilder implements GrammarBuilder{
        /*
         * 等待状态
         * 0 - key
         * 1 - :
         * 2 - value
         */
        private int objStatus = 0;

        private boolean closed = false;

        private String activeKey = null;

        private IGrammarNode valueNode = null;

        @Override
        public boolean add(Token token) throws GrammarException{
            if(this.closed){ return false; }
            if(objStatus == 0){ // key
                if(token.type() == TokenType.STRING){
                    this.activeKey = StringEscapeUtil.fromString(token.literal());
                }else if(token.type() == TokenType.IDENTIFIER){
                    this.activeKey = token.literal();
                }else{
                    throw new GrammarException(token);
                }
                if(!TokenRuleUtil.isIdentifier(this.activeKey)){
                    throw new GrammarException(token);
                }
                objStatus = 1;
                return true;
            }else if(objStatus == 1){ // :
                if(!":".equals(token.literal())){
                    throw new GrammarException(token);
                }
                objStatus = 2;
                valueNode = null;
                return true;
            }else if(objStatus == 2){ // value
                if(valueNode == null){
                    if(token.type() == TokenType.IDENTIFIER){
                        valueNode = new IdentifierGrammarNode(token);
                    }else if(TokenUtil.isConstant(token.type())){
                        valueNode = GrammarNodeBuilder.buildForConstant(token);
                    }else if(token.type() == TokenType.STRUCT){
                        valueNode = GrammarNodeBuilder.buildForObject(token);
                    }
                    if(valueNode == null){
                        throw new GrammarException(token);
                    }
                }else{
                    if(!valueNode.add(token)){
                        if(",".equals(token.literal())){
                            objStatus = 0;
                            ObjectGrammarNode.this.map.put(activeKey, valueNode);
                            return true;
                        }else if("}".equals(token.literal())){
                            ObjectGrammarNode.this.map.put(activeKey, valueNode);
                            this.closed = true;
                            return true;
                        }
                        return false;
                    }
                }
                return true;
            }
            throw new GrammarException(token);
        }

        @Override
        public boolean closed() {
            return this.closed;
        }
    }
}