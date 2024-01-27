package sch.frog.kit.lang.parse.grammar0.node;

import sch.frog.kit.lang.parse.exception.ExecuteException;
import sch.frog.kit.lang.parse.grammar0.IAstNode;
import sch.frog.kit.lang.parse.grammar0.IExpression;
import sch.frog.kit.lang.parse.grammar0.IStatement;
import sch.frog.kit.lang.parse.semantic.IExecuteContext;
import sch.frog.kit.lang.parse.semantic.Result;
import sch.frog.kit.lang.value.Value;

import java.util.ArrayList;
import java.util.List;

public class IfStatement implements IStatement {

    private final IfEntry ifEntry;

    private final List<ElseIfEntry> elseIfList = new ArrayList<>();

    private final ElseEntry elseEntry;

    public IfStatement(IfEntry ifEntry, List<ElseIfEntry> elseIfList, ElseEntry elseEntry) {
        this.ifEntry = ifEntry;
        this.elseIfList.addAll(elseIfList);
        this.elseEntry = elseEntry;
    }

    @Override
    public String literal() {
        return "#if_statement";
    }

    @Override
    public List<IAstNode> getChildren() {
        ArrayList<IAstNode> list = new ArrayList<>();
        list.add(ifEntry);
        if(!elseIfList.isEmpty()){
            list.addAll(elseIfList);
        }
        if(elseEntry != null){
            list.add(elseEntry);
        }
        return list;
    }

    public Result execute(IExecuteContext context) throws ExecuteException {
        IExpression condition = ifEntry.getCondition();
        Value val = condition.evaluate(context);
        if(val.cast(boolean.class)){
            NestStatement nestStatement = ifEntry.getNestStatement();
            return nestStatement.execute(context);
        }
        if(!elseIfList.isEmpty()){
            for (ElseIfEntry entry : elseIfList) {
                condition = entry.getCondition();
                if(condition.evaluate(context).cast(boolean.class)){
                    return entry.getIfNest().execute(context);
                }
            }
        }

        if(elseEntry != null){
            return elseEntry.getIfNest().execute(context);
        }

        return Result.VOID;
    }
}
