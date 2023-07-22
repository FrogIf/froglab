package sch.frog.kit.core.parse.grammar;

import sch.frog.kit.core.exception.GrammarException;
import sch.frog.kit.core.execute.ISession;
import sch.frog.kit.core.parse.lexical.Token;
import sch.frog.kit.core.parse.lexical.TokenType;
import sch.frog.kit.core.value.Locator;
import sch.frog.kit.core.value.Value;

/**
 * 标识符, 后面可以紧跟: . ( [
 */
public class IdentifierGrammarNode extends AbstractGrammarNode{

    private AbstractLeftAssociativeGrammarNode succeed = null;

    private Type type = Type.VARIABLE;

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
                    succeed = new ArgumentListGrammarNode(token);
                    status = 1;
                    return true;
                }else if(".".equals(token.literal())){
                    type = Type.VARIABLE;
                    status = 1;
                    succeed = new PropertyGrammarNode(token);
                    return true;
                }else if("[".equals(token.literal())){
                    type = Type.VARIABLE;
                    status = 1;
                    succeed = new ArrayIndexGrammarNode(token);
                    return true;
                }
            }
            status = 2;
            return false;
        }else if(status == 1){ // 构建中
            boolean success = succeed.add(token);
            if(!success){
                status = 2;
            }
            return success;
        }
        return false;
    }

    @Override
    public void grammarCheck() throws GrammarException {
        if(this.succeed != null){
            this.succeed.grammarCheck();
        }
    }

    @Override
    public Value evaluate(ISession session) {
        if(succeed == null){
            return new Value(new Locator(this.token.literal()));
        }
        Value result = session.getVariable(token.literal());
        return succeed.succeedEvaluate(result, session);
    }

    private enum Type{
        VARIABLE,
        FUNCTION;
    }

}