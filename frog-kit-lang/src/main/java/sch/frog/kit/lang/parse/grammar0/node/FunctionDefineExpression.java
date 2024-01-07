package sch.frog.kit.lang.parse.grammar0.node;

import sch.frog.kit.lang.parse.grammar0.IAstNode;
import sch.frog.kit.lang.parse.grammar0.IExpression;

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

}
