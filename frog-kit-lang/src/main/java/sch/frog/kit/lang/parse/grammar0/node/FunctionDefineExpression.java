package sch.frog.kit.lang.parse.grammar0.node;

import sch.frog.kit.lang.parse.exception.ExecuteException;
import sch.frog.kit.lang.parse.grammar0.IAstNode;
import sch.frog.kit.lang.parse.grammar0.IExpression;
import sch.frog.kit.lang.parse.semantic.FunctionDefine;
import sch.frog.kit.lang.parse.semantic.IExecuteContext;
import sch.frog.kit.lang.value.Value;

import java.util.Arrays;
import java.util.List;

public class FunctionDefineExpression implements IExpression {

    private final FunctionFormalArgumentExpression formalArguments;

    private final StatementBlock statementBlock;

    public FunctionDefineExpression(FunctionFormalArgumentExpression formalArguments, StatementBlock statementBlock) {
        this.formalArguments = formalArguments;
        this.statementBlock = statementBlock;
    }

    public FunctionFormalArgumentExpression getFormalArguments() {
        return formalArguments;
    }

    public StatementBlock getStatementBlock() {
        return statementBlock;
    }

    @Override
    public String literal() {
        return "=>";
    }

    @Override
    public List<IAstNode> getChildren() {
        return Arrays.asList(formalArguments, statementBlock);
    }

    @Override
    public Value evaluate(IExecuteContext context) throws ExecuteException {
        List<IdentifierNode> args = formalArguments.getFormalArguments();
        String[] argNameArr = new String[args.size()];
        int i = 0;
        for (IdentifierNode arg : args) {
            argNameArr[i] = arg.identifier();
            i++;
        }
        FunctionDefine functionDefine = new FunctionDefine(argNameArr, statementBlock);
        return Value.of(functionDefine);
    }
}
