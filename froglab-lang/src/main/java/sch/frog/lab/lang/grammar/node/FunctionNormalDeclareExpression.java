package sch.frog.lab.lang.grammar.node;

import sch.frog.lab.lang.exception.ExecuteException;
import sch.frog.lab.lang.grammar.IAstNode;
import sch.frog.lab.lang.grammar.IExpression;
import sch.frog.lab.lang.semantic.FunctionDefine;
import sch.frog.lab.lang.semantic.IExecuteContext;
import sch.frog.lab.lang.semantic.Reference;
import sch.frog.lab.lang.value.Value;

import java.util.Arrays;
import java.util.List;

public class FunctionNormalDeclareExpression implements IExpression {

    private final FunctionFormalArgumentExpression formalArguments;

    private final StatementBlock statementBlock;

    public FunctionNormalDeclareExpression(FunctionFormalArgumentExpression formalArguments, StatementBlock statementBlock) {
        this.formalArguments = formalArguments;
        this.statementBlock = statementBlock;
    }

    @Override
    public String literal() {
        return "#function-declare";
    }

    @Override
    public List<IAstNode> getChildren() {
        return Arrays.asList(formalArguments, statementBlock);
    }

    @Override
    public Reference evaluate(IExecuteContext context) throws ExecuteException {
        List<IdentifierNode> args = formalArguments.getFormalArguments();
        String[] argNameArr = new String[args.size()];
        int i = 0;
        for (IdentifierNode arg : args) {
            argNameArr[i] = arg.identifier();
            i++;
        }
        FunctionDefine functionDefine = new FunctionDefine(argNameArr, statementBlock, context);
        return new Reference(Value.of(functionDefine));
    }
}
