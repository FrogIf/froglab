package sch.frog.kit.lang.parse.grammar0.node;

import sch.frog.kit.lang.parse.grammar0.IAstNode;
import sch.frog.kit.lang.parse.grammar0.IStatement;

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
}
