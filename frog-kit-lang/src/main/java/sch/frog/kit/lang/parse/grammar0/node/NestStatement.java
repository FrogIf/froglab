package sch.frog.kit.lang.parse.grammar0.node;

import sch.frog.kit.lang.parse.grammar0.IAstNode;
import sch.frog.kit.lang.parse.grammar0.IStatement;

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
}
