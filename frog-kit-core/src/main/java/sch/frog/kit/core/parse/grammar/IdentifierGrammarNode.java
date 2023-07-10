package sch.frog.kit.core.parse.grammar;

import sch.frog.kit.core.exception.ExecuteException;
import sch.frog.kit.core.exception.GrammarException;
import sch.frog.kit.core.execute.AppContext;
import sch.frog.kit.core.execute.ISession;
import sch.frog.kit.core.fun.IFunction;
import sch.frog.kit.core.parse.lexical.Token;
import sch.frog.kit.core.parse.lexical.TokenType;
import sch.frog.kit.core.value.Locator;
import sch.frog.kit.core.value.Value;
import sch.frog.kit.core.value.ValueType;

import java.util.ArrayList;

public class IdentifierGrammarNode extends AbstractGrammarNode{

    // 后继节点
    private final ArrayList<SucceedBox> succeed = new ArrayList<>();

    private Type type = Type.VARIABLE;

    private NodeBuilder nodeBuilder = null;

    public IdentifierGrammarNode(Token token){
        super(token);
        if(token.type() != TokenType.IDENTIFIER){
            throw new IllegalArgumentException("identifier grammar node's token type must be identifier, but " + token.type());
        }
    }

    // 0 - 初始化; 1 - 构建中; 2 - 构建完成
    private int status = 0;

    @Override
    public boolean add(Token token) throws GrammarException {
        if(status == 2){ return false; }

        if(status == 0){ // 初始化
            if(token.type() == TokenType.STRUCT){
                if("(".equals(token.literal())){
                    type = Type.FUNCTION;
                    nodeBuilder = new FunctionBuilder();
                    nodeBuilder.add(token);
                    status = 1;
                    return true;
                }else if(".".equals(token.literal()) || "[".equals(token.literal())){
                    type = Type.VARIABLE;
                    status = 1;
                    nodeBuilder = new VariableBuilder();
                    nodeBuilder.add(token);
                    return true;
                }
            }
            status = 2;
            return false;
        }else if(status == 1){ // 构建中
            boolean success = nodeBuilder.add(token);
            if(!success){
                status = 2;
            }
            return success;
        }
        return false;
    }

    @Override
    public void grammarCheck() throws GrammarException {
        if(type == Type.FUNCTION){
            if(!((FunctionBuilder)nodeBuilder).closed){
                throw new GrammarException("function " + this.name() + " is not closed", token.pos());
            }
        }else{
            if(nodeBuilder != null){
                if(((VariableBuilder)nodeBuilder).waitProp || ((VariableBuilder)nodeBuilder).arrayIndex != null){
                    throw new GrammarException("variable link is not finish for : " + this.name(), token.pos());
                }
            }
        }

        for (SucceedBox succeedBox : succeed) {
            succeedBox.node.grammarCheck();
        }
    }

    @Override
    public Value evaluate(ISession session) {
        if(type == Type.FUNCTION){
            Value[] args = new Value[succeed.size()];
            for (int i = 0; i < args.length; i++){
                args[i] = succeed.get(i).node.evaluate(session);
            }
            AppContext appContext = session.getAppContext();
            IFunction function = appContext.getFunction(this.token.literal());
            if(function == null){
                throw new ExecuteException("no function named " + this.token.literal() + " define");
            }
            return function.execute(args, session);
        }else{
            String varName = this.token.literal();
            if(!succeed.isEmpty()){
                if(!session.exist(varName)){
                    throw new ExecuteException("variable " + varName + " is not define");
                }

                Locator next = null;
                for(int i = succeed.size() - 1; i >= 0; i--){
                    SucceedBox box = succeed.get(i);
                    if(box.type == 1){ // object prop
                        if(!(box.node instanceof IdentifierGrammarNode)){
                            throw new ExecuteException("prop name must identifier for obj : " + this.name());
                        }
                        Locator tmp = next;
                        next = new Locator(box.node.name(), tmp);
                    }else if(box.type == 2){ // array index
                        Value val = box.node.evaluate(session);
                        if(val.getType() != ValueType.NUMBER){
                            throw new ExecuteException("index must number for list : " + this.name());
                        }
                        Integer index = val.to(Integer.class);
                        Locator tmp = next;
                        next = new Locator(index, tmp);
                    }else{
                        throw new ExecuteException("identifier arguments type is not right for : " + this.name());
                    }
                }
                return new Value(new Locator(varName, next));
            }else{
                return new Value(new Locator(varName));
            }
        }
    }

    private enum Type{
        VARIABLE,
        FUNCTION;
    }

    private interface NodeBuilder{
        boolean add(Token token) throws GrammarException;
    }

    /*
     * aa.bb.cc[0]
     * aaa[0][1][2].cc
     */
    private class VariableBuilder implements NodeBuilder {

        private ArrayIndexGrammarNode arrayIndex = null;

        private boolean waitProp = false;  // 对象, 等待属性名

        public boolean add(Token token) throws GrammarException {
            if(waitProp){
                if(token.type() != TokenType.IDENTIFIER){
                    throw new GrammarException(token);
                }
                IdentifierGrammarNode.this.succeed.add(new SucceedBox(new IdentifierGrammarNode(token), 1));
                this.waitProp = false;
                return true;
            }else if(arrayIndex != null){
                if(arrayIndex.add(token)){
                    if(arrayIndex.closed){
                        IdentifierGrammarNode.this.succeed.add(new SucceedBox(arrayIndex, 2));
                        arrayIndex = null;
                    }
                    return true;
                }else{
                    throw new GrammarException(token);
                }
            }else if(".".equals(token.literal())){
                waitProp = true;
                return true;
            }else if("[".equals(token.literal())){
                arrayIndex = new ArrayIndexGrammarNode(token);
                return true;
            }
            return false;
        }
    }

    private class FunctionBuilder implements NodeBuilder{
        private boolean start = false;
        private boolean closed = false;
        private IGrammarNode cursor = null;
        public boolean add(Token token) throws GrammarException {
            if(closed){ return false; }
            if(!start){
                if("(".equals(token.literal())){
                    this.start = true;
                    return true;
                }else{
                    throw new GrammarException(token);
                }
            }
            if(cursor != null){
                if(cursor.add(token)){ return true; }
            }
            if(",".equals(token.literal())){
                if(cursor == null){ throw new GrammarException(token); }
                cursor = null;
                return true;
            }else if(")".equals(token.literal())){
                this.closed = true;
                IdentifierGrammarNode.this.status = 2;
                return true;
            }else if(this.cursor == null){
                IGrammarNode node = GeneralGrammarNodeBuilder.buildForValuable(token);
                if(node == null){
                    throw new GrammarException(token);
                }
                this.cursor = node;
                IdentifierGrammarNode.this.succeed.add(new SucceedBox(node, 0));
                return true;
            }
            return false;
        }
    }

    private static class SucceedBox{
        IGrammarNode node;
        int type; // 0 - function argument, 1 - object prop, 2 - list index

        public SucceedBox(IGrammarNode node, int type) {
            this.node = node;
            this.type = type;
        }
    }

    private static class ArrayIndexGrammarNode extends AbstractGrammarNode{

        private IGrammarNode indexNode;

        private boolean closed = false;

        public ArrayIndexGrammarNode(Token token) {
            super(token);
        }

        @Override
        public boolean add(Token token) throws GrammarException {
            if(closed){ return false; }
            if(indexNode != null){
                boolean success = indexNode.add(token);
                if(!success){
                    if("]".equals(token.literal())){
                        this.closed = true;
                        success = true;
                    }
                }
                return success;
            }else{
                indexNode = GeneralGrammarNodeBuilder.buildForValuable(token);
                if(indexNode == null){
                    throw new GrammarException(token);
                }
                return true;
            }
        }

        @Override
        public void grammarCheck() throws GrammarException {
            if(!closed){
                throw new GrammarException(super.token);
            }
        }

        @Override
        public Value evaluate(ISession session) {
            return indexNode.evaluate(session);
        }
    }


}
