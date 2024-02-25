package sch.frog.lab.lang.semantic;

import sch.frog.lab.lang.exception.ExecuteException;
import sch.frog.lab.lang.grammar.IExpression;
import sch.frog.lab.lang.grammar.IStatement;
import sch.frog.lab.lang.grammar.node.BreakStatement;
import sch.frog.lab.lang.grammar.node.ContinueStatement;
import sch.frog.lab.lang.grammar.node.DoWhileStatement;
import sch.frog.lab.lang.grammar.node.ForStatement;
import sch.frog.lab.lang.grammar.node.FunctionStatement;
import sch.frog.lab.lang.grammar.node.IfStatement;
import sch.frog.lab.lang.grammar.node.ReturnStatement;
import sch.frog.lab.lang.grammar.node.StatementBlock;
import sch.frog.lab.lang.grammar.node.Statements;
import sch.frog.lab.lang.grammar.node.VariableBody;
import sch.frog.lab.lang.grammar.node.VariableStatement;
import sch.frog.lab.lang.grammar.node.WhileStatement;
import sch.frog.lab.lang.value.Value;

import java.util.List;

public class Executor {

    public Result execute(Statements statements, IExecuteContext context) throws ExecuteException {
        List<IStatement> statementList = statements.getStatements();
        if(statementList.isEmpty()){ return Result.VOID; }

        Result result = Result.VOID;
        for (IStatement statement : statementList) {
            result = executeStatement(statement, context);
            if (result.type() != ResultType.NORMAL){
                break;
            }
        }
        return result;
    }

    private Result executeStatement(IStatement statement, IExecuteContext context) throws ExecuteException {
        if(statement instanceof IExpression){
            return new Result(((IExpression)statement).evaluate(context));
        }else if(statement instanceof StatementBlock){
            return execute(((StatementBlock)statement).getStatements(), context);
        }else if(statement instanceof VariableStatement){
            executeVariableStatement((VariableStatement) statement, context);
            return Result.VOID;
        }else if(statement instanceof ReturnStatement){
            ReturnStatement returnStatement = (ReturnStatement) statement;
            IExpression exp = returnStatement.getExp();
            return new Result(exp == null ? new Reference(Value.VOID) : exp.evaluate(context), ResultType.RETURN);
        }else if(statement instanceof BreakStatement){
            return new Result(new Reference(Value.VOID), ResultType.BREAK);
        }else if(statement instanceof ContinueStatement){
            return new Result(new Reference(Value.VOID), ResultType.CONTINUE);
        }else if(statement instanceof IfStatement){
            return ((IfStatement) statement).execute(context);
        }else if(statement instanceof WhileStatement){
            return ((WhileStatement) statement).execute(context);
        }else if(statement instanceof DoWhileStatement){
            return ((DoWhileStatement) statement).execute(context);
        }else if(statement instanceof ForStatement){
            return ((ForStatement) statement).execute(context);
        }else if(statement instanceof FunctionStatement){
            return ((FunctionStatement) statement).execute(context);
        }
        return Result.VOID;
    }

    public void executeVariableStatement(VariableStatement variableStatement, IExecuteContext context) throws ExecuteException {
        List<VariableBody> bodyList = variableStatement.getBodyList();
        for (VariableBody varBody : bodyList) {
            String name = varBody.name();
            IExpression body = varBody.expressionBody();
            Value value = body == null ? Value.UNDEFINE : body.evaluate(context).value();
            if(variableStatement.isGlobalVar()){
                context.defGlobalVariable(name, value);
            }else{
                context.defLocalVariable(name, value);
            }
        }
    }

}
