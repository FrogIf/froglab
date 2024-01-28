package sch.frog.kit.lang.grammar.node;

import sch.frog.kit.lang.grammar.IAstNode;
import sch.frog.kit.lang.grammar.IExpression;
import sch.frog.kit.lang.grammar.IStatement;

import java.util.Collections;
import java.util.List;

public class ReturnStatement implements IStatement {

    private final IExpression exp;

    public ReturnStatement(IExpression exp) {
        this.exp = exp;
    }

    public IExpression getExp() {
        return exp;
    }

    @Override
    public String literal() {
        return "return";
    }

    @Override
    public List<IAstNode> getChildren() {
        if(exp == null){ return Collections.emptyList(); }
        else{ return Collections.singletonList(exp); }
    }
}
