package sch.frog.kit.lang.parse.grammar;

import sch.frog.kit.lang.execute.IRuntimeContext;
import sch.frog.kit.lang.parse.lexical.Token;
import sch.frog.kit.lang.parse.lexical.TokenType;
import sch.frog.kit.lang.value.Locator;
import sch.frog.kit.lang.value.Value;

/**
 * 标识符, 后面可以紧跟: . ( [
 */
public class IdentifierGrammarNode extends AbstractGrammarNode{

    private AbstractLeftAssociativeGrammarNode succeed = null;

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
                    succeed = new ArgumentListGrammarNode(token);
                    status = 1;
                    return true;
                }else if(".".equals(token.literal())){
                    status = 1;
                    succeed = new PropertyGrammarNode(token);
                    return true;
                }else if("[".equals(token.literal())){
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
    public Value evaluate(IRuntimeContext context) {
        if(succeed == null){
            return new Value(new Locator(this.token.literal()));
        }

        Value result = context.getVariable(token.literal());
        return succeed.succeedEvaluate(result, context);
    }

}
