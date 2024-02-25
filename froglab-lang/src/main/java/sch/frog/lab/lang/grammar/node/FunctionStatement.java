package sch.frog.lab.lang.grammar.node;

import sch.frog.lab.lang.exception.ExecuteException;
import sch.frog.lab.lang.grammar.IAstNode;
import sch.frog.lab.lang.grammar.IStatement;
import sch.frog.lab.lang.lexical.Token;
import sch.frog.lab.lang.semantic.FunctionDefine;
import sch.frog.lab.lang.semantic.IExecuteContext;
import sch.frog.lab.lang.semantic.Result;
import sch.frog.lab.lang.value.Value;

import java.util.Arrays;
import java.util.List;

public class FunctionStatement implements IStatement {

    private final Token name;

    private final FunctionFormalArgumentExpression formalArguments;

    private final StatementBlock statementBlock;

    public FunctionStatement(Token name, FunctionFormalArgumentExpression formalArguments, StatementBlock statementBlock) {
        this.name = name;
        this.formalArguments = formalArguments;
        this.statementBlock = statementBlock;
    }

    @Override
    public String literal() {
        return "#function-define:" + name.literal();
    }

    @Override
    public List<IAstNode> getChildren() {
        return Arrays.asList(formalArguments, statementBlock);
    }

    public Result execute(IExecuteContext context) throws ExecuteException {
        List<IdentifierNode> args = formalArguments.getFormalArguments();
        String[] argNameArr = new String[args.size()];
        int i = 0;
        for (IdentifierNode arg : args) {
            argNameArr[i] = arg.identifier();
            i++;
        }
        FunctionDefine functionDefine = new FunctionDefine(argNameArr, statementBlock, context);
        context.defGlobalVariable(name.literal(), Value.of(functionDefine));
        return Result.VOID;
    }
}
