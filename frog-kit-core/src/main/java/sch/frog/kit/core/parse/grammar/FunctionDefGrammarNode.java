package sch.frog.kit.core.parse.grammar;

import sch.frog.kit.core.exception.ExecuteException;
import sch.frog.kit.core.exception.GrammarException;
import sch.frog.kit.core.execute.ISession;
import sch.frog.kit.core.execute.SessionWrapper;
import sch.frog.kit.core.fun.AbstractFunction;
import sch.frog.kit.core.parse.lexical.Token;
import sch.frog.kit.core.parse.lexical.TokenType;
import sch.frog.kit.core.value.Value;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
(a, b) -> if(lt(a,b), A, B)
 */

public class FunctionDefGrammarNode extends ObjectGrammarNode {
    // 0 - 参数构建中; 1 - 等待=>分隔; 2 - 函数体构建中, 3 - 构建结束
    private int status = 0;

    private final List<String> arguments = new ArrayList<>();

    private IdentifierGrammarNode body = null;

    private final ArgumentBuilder argBuilder = new ArgumentBuilder();

    public FunctionDefGrammarNode(Token token) {
        super(token);
        if(token.type() != TokenType.STRUCT || !"(".equals(token.literal())){
            throw new IllegalArgumentException("function must start with (");
        }
    }

    @Override
    public boolean add(Token token) throws GrammarException {
        if(status == 3){ return false; }

        if(status == 0){
            if(!argBuilder.add(token)){
                throw new GrammarException(token);
            }
            if(argBuilder.finished){
                status = 1;
            }
            return true;
        }else if(status == 1){
            if(token.type() == TokenType.STRUCT && "=>".equals(token.literal())){
                status = 2;
                return true;
            }
            throw new GrammarException(token);
        }else if(status == 2){
            if(!buildBody(token)){
                status = 3;
                return false;
            }else{
                return true;
            }
        }
        return false;
    }

    public boolean buildBody(Token token) throws GrammarException{
        if(this.body == null){
            if(token.type() == TokenType.IDENTIFIER){
                this.body = new IdentifierGrammarNode(token);
                return true;
            }
            throw new GrammarException(token);
        }else{
            return this.body.add(token);
        }
    }

    @Override
    public void grammarCheck() throws GrammarException {
        if(!argBuilder.finished){ throw new GrammarException("argument define not finish", token.pos()); }
        if(this.body == null){
            throw new GrammarException("function body is not define", token.pos());
        }
        this.body.grammarCheck();
    }

    @Override
    public Value evaluate(ISession session) {
        return new Value(new BodyFunction(this.arguments, this.body));
    }

    private static class BodyFunction extends AbstractFunction{

        private final IdentifierGrammarNode body;

        private final List<String> arguments;

        public BodyFunction(List<String> arguments, IdentifierGrammarNode body) {
            this.body = body;
            this.arguments = arguments;
        }

        @Override
        public String name() {
            return null;
        }

        @Override
        public String description() {
            return null;
        }

        @Override
        public Value execute(Value[] args, ISession session) {
            session = new InnerSession(session);
            if(args.length != arguments.size()){
                throw new ExecuteException("real args count except " + arguments.size() + ", but " + args.length);
            }
            for(int i = 0; i < arguments.size(); i++){
                session.addValue(arguments.get(i), args[i]);
            }
            return this.body.evaluate(session);
        }
    }

    private static class InnerSession extends SessionWrapper {

        private final HashMap<String, Value> innerVariable = new HashMap<>();

        public InnerSession(ISession session) {
            super(session);
        }

        @Override
        public Value getVariable(String key) {
            Value val = innerVariable.get(key);
            if(val == null){
                return super.getVariable(key);
            }else{
                return val;
            }
        }

        @Override
        public void setValue(String key, Value value) {
            if(innerVariable.containsKey(key)){
                innerVariable.put(key, value);
            }else{
                session.setValue(key, value);
            }
        }

        @Override
        public void addValue(String key, Value value) {
            if(innerVariable.containsKey(key)){
                throw new ExecuteException(key + " has defined");
            }
            innerVariable.put(key, value);
        }
    }

    private class ArgumentBuilder{
        private boolean finished = false;

        private boolean waitEle = true;

        public boolean add(Token token) throws GrammarException{
            if(finished){ return false; }
            if(")".equals(token.literal())){
                finished = true;
                return true;
            }
            if(",".equals(token.literal())){
                if(waitEle){ throw new GrammarException(token); }
                waitEle = true;
                return true;
            }
            if(waitEle){
                if(token.type() != TokenType.IDENTIFIER){
                    throw new GrammarException(token);
                }
                FunctionDefGrammarNode.this.arguments.add(token.literal());
                waitEle = false;
                return true;
            }
            throw new GrammarException(token);
        }
    }
}
