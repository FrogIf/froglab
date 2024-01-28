package sch.frog.lab.lang.grammar.node;

import sch.frog.lab.lang.exception.ExecuteException;
import sch.frog.lab.lang.grammar.IAstNode;
import sch.frog.lab.lang.grammar.IStatement;
import sch.frog.lab.lang.semantic.IExecuteContext;
import sch.frog.lab.lang.semantic.Result;

import java.util.Collections;
import java.util.List;

public class NestStatement implements IAstNode {

    private final StatementBlock statementBlock;

    private final IStatement statement;

    public NestStatement(StatementBlock statementBlock) {
        this.statementBlock = statementBlock;
        this.statement = null;
    }

    public NestStatement(IStatement statement) {
        this.statement = statement;
        this.statementBlock = null;
    }

    @Override
    public String literal() {
        return "#nest-statement";
    }

    @Override
    public List<IAstNode> getChildren() {
        if(statement != null){
            return Collections.singletonList(statement);
        }else if(statementBlock != null){
            return Collections.singletonList(statementBlock);
        }
        return null;
    }

    public Result execute(IExecuteContext context) throws ExecuteException {
        if(statement != null){
            return context.executor().execute(new Statements(Collections.singleton(statement)), context);
        }else if(statementBlock != null){
            return context.executor().execute(new Statements(Collections.singleton(statementBlock)), context);
        }
        throw new ExecuteException("nest statement is empty");
    }
}
