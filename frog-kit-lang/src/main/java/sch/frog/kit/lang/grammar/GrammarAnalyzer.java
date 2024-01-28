package sch.frog.kit.lang.grammar;

import sch.frog.kit.lang.exception.GrammarException;
import sch.frog.kit.lang.grammar.node.AbstractCaller;
import sch.frog.kit.lang.grammar.node.ArrayCaller;
import sch.frog.kit.lang.grammar.node.ArrayExpression;
import sch.frog.kit.lang.grammar.node.ArrayIndex;
import sch.frog.kit.lang.grammar.node.ArrayNode;
import sch.frog.kit.lang.grammar.node.BoolNode;
import sch.frog.kit.lang.grammar.node.BreakStatement;
import sch.frog.kit.lang.grammar.node.ContinueStatement;
import sch.frog.kit.lang.grammar.node.DoWhileStatement;
import sch.frog.kit.lang.grammar.node.ElseEntry;
import sch.frog.kit.lang.grammar.node.ElseIfEntry;
import sch.frog.kit.lang.grammar.node.ExpressionGroup;
import sch.frog.kit.lang.grammar.node.ExpressionList;
import sch.frog.kit.lang.grammar.node.ForInitializer;
import sch.frog.kit.lang.grammar.node.ForStatement;
import sch.frog.kit.lang.grammar.node.FunctionCaller;
import sch.frog.kit.lang.grammar.node.FunctionDefineExpression;
import sch.frog.kit.lang.grammar.node.FunctionExpression;
import sch.frog.kit.lang.grammar.node.FunctionFormalArgumentExpression;
import sch.frog.kit.lang.grammar.node.FunctionRefCallExpression;
import sch.frog.kit.lang.grammar.node.IdentifierNode;
import sch.frog.kit.lang.grammar.node.IfEntry;
import sch.frog.kit.lang.grammar.node.IfStatement;
import sch.frog.kit.lang.grammar.node.ImportStatement;
import sch.frog.kit.lang.grammar.node.InfixExpression;
import sch.frog.kit.lang.grammar.node.IterationStatement;
import sch.frog.kit.lang.grammar.node.NestStatement;
import sch.frog.kit.lang.grammar.node.NullNode;
import sch.frog.kit.lang.grammar.node.NumberNode;
import sch.frog.kit.lang.grammar.node.ObjectCaller;
import sch.frog.kit.lang.grammar.node.ObjectExpression;
import sch.frog.kit.lang.grammar.node.ObjectNode;
import sch.frog.kit.lang.grammar.node.PackageStatement;
import sch.frog.kit.lang.grammar.node.PrefixExpression;
import sch.frog.kit.lang.grammar.node.ReturnStatement;
import sch.frog.kit.lang.grammar.node.StatementBlock;
import sch.frog.kit.lang.grammar.node.Statements;
import sch.frog.kit.lang.grammar.node.StringNode;
import sch.frog.kit.lang.grammar.node.VariableBody;
import sch.frog.kit.lang.grammar.node.VariableStatement;
import sch.frog.kit.lang.grammar.node.WhileStatement;
import sch.frog.kit.lang.lexical.ITokenStream;
import sch.frog.kit.lang.lexical.Token;
import sch.frog.kit.lang.lexical.TokenConstant;
import sch.frog.kit.lang.lexical.TokenType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class GrammarAnalyzer {

    // 优先级
    private static final HashMap<String, Integer> precedenceMap = new HashMap<>();

    static {
        precedenceMap.put(TokenConstant.ASSIGN, -1);
        precedenceMap.put(TokenConstant.OR, 0);
        precedenceMap.put(TokenConstant.AND, 0);
        precedenceMap.put(TokenConstant.SHORT_CIRCLE_AND, 0);
        precedenceMap.put(TokenConstant.SHORT_CIRCLE_OR, 0);
        precedenceMap.put(TokenConstant.EQ, 1);
        precedenceMap.put(TokenConstant.NOT_EQ, 1);
        precedenceMap.put(TokenConstant.GT, 2);
        precedenceMap.put(TokenConstant.GTE, 2);
        precedenceMap.put(TokenConstant.LT, 2);
        precedenceMap.put(TokenConstant.LTE, 2);
        precedenceMap.put(TokenConstant.PLUS, 3);
        precedenceMap.put(TokenConstant.MINUS, 3);
        precedenceMap.put(TokenConstant.STAR, 4);
        precedenceMap.put(TokenConstant.SLASH, 4);
    }

    public Statements parse(ITokenStream tokenStream) throws GrammarException {
        return parseStatements(tokenStream);
    }

    private Statements parseStatements(ITokenStream tokenStream) throws GrammarException {
        List<IStatement> statements = new LinkedList<>();
        while (tokenStream.current() != Token.EOF){
            IStatement statement = parseStatement(tokenStream);
            if(statement == null){ break; }

            statements.add(statement);
            if(!TokenConstant.SEMICOLON.equals(tokenStream.current().literal())){
                break;
            }
            tokenStream.next();
        }
        return new Statements(statements);
    }

    private IStatement parseStatement(ITokenStream tokenStream) throws GrammarException {
        Token current = tokenStream.current();
        String literal = current.literal();
        switch (literal){
            case TokenConstant.VAR:
            case TokenConstant.LET:
                return parseVariableStatement(tokenStream);
            case TokenConstant.RETURN:
                return parseReturnStatement(tokenStream);
            case TokenConstant.BREAK:
                return parseBreakStatement(tokenStream);
            case TokenConstant.CONTINUE:
                return parseContinueStatement(tokenStream);
            case TokenConstant.PACKAGE:
                return parsePackageStatement(tokenStream);
            case TokenConstant.IMPORT:
                return parseImportStatement(tokenStream);
            case TokenConstant.IF:
                return parseIfStatement(tokenStream);
            case TokenConstant.WHILE:
            case TokenConstant.DO:
            case TokenConstant.FOR:
                return parseIterationStatement(tokenStream);
            default:
                return parseExpression(tokenStream, Integer.MIN_VALUE);
        }
    }

    private IterationStatement parseIterationStatement(ITokenStream tokenStream) throws GrammarException {
        String literal = tokenStream.current().literal();
        switch (literal){
            case TokenConstant.FOR:
                return forStatement(tokenStream);
            case TokenConstant.WHILE:
                return whileStatement(tokenStream);
            case TokenConstant.DO:
                return doWhileStatement(tokenStream);
        }
        throw buildException(tokenStream.current(), "unexpected execute for parse iteration statement");
    }

    private DoWhileStatement doWhileStatement(ITokenStream tokenStream) throws GrammarException {
        tokenStream.next(); // skip do
        NestStatement nestStatement = nestStatement(tokenStream);
        Token current = tokenStream.current();
        if(!TokenConstant.WHILE.equals(current.literal())){
            throw buildException(current, "do-while statement expect while word");
        }
        current = tokenStream.next();
        if(!TokenConstant.LPAREN.equals(current.literal())){
            throw buildException(current, "do-while statement expect: " + TokenConstant.LPAREN);
        }
        tokenStream.next();
        IExpression condition = parseExpression(tokenStream, Integer.MIN_VALUE);
        if(condition == null){
            throw buildException(current, "do-while statement condition is null");
        }
        current = tokenStream.current();
        if(!TokenConstant.RPAREN.equals(current.literal())){
            throw buildException(current, "do-while statement expect: " + TokenConstant.RPAREN);
        }
        tokenStream.next();

        return new DoWhileStatement(nestStatement, condition);
    }

    private WhileStatement whileStatement(ITokenStream tokenStream) throws GrammarException {
        Token current = tokenStream.next();// skip while
        if(!TokenConstant.LPAREN.equals(current.literal())){
            throw buildException(current, "while statement incorrect");
        }
        tokenStream.next();
        IExpression condition = parseExpression(tokenStream, Integer.MIN_VALUE);
        if(condition == null){
            throw buildException(current, "while statement condition is null");
        }
        current = tokenStream.current();
        if(!TokenConstant.RPAREN.equals(current.literal())){
            throw buildException(current, "while statement incorrect");
        }
        tokenStream.next();

        NestStatement nestStatement = nestStatement(tokenStream);
        return new WhileStatement(condition, nestStatement);
    }

    private ForStatement forStatement(ITokenStream tokenStream) throws GrammarException {
        Token current = tokenStream.next();
        if(!TokenConstant.LPAREN.equals(current.literal())){
            throw buildException(current, "for statement format is incorrect");
        }
        current = tokenStream.next();

        ForInitializer forInitializer = null;
        if(!TokenConstant.SEMICOLON.equals(current.literal())){
            forInitializer = forInitializer(tokenStream);
            current = tokenStream.current();
            if(!TokenConstant.SEMICOLON.equals(current.literal())){
                throw buildException(current, "for statement is incorrect");
            }
        }
        current = tokenStream.next();

        IExpression condition = null;
        if(!TokenConstant.SEMICOLON.equals(current.literal())){
            condition = parseExpression(tokenStream, Integer.MIN_VALUE);
            if(condition == null){
                throw buildException(current, "for statement condition incorrect");
            }
            current = tokenStream.current();
            if(!TokenConstant.SEMICOLON.equals(current.literal())){
                throw buildException(current, "for statement is incorrect");
            }
        }
        tokenStream.next();

        ExpressionList expressions = new ExpressionList(expressionList(tokenStream));
        current = tokenStream.current();
        if(!TokenConstant.RPAREN.equals(current.literal())){
            throw buildException(current, "for statement is incorrect");
        }
        tokenStream.next();

        NestStatement nestStatement = nestStatement(tokenStream);

        return new ForStatement(forInitializer, condition, expressions, nestStatement);
    }

    private ForInitializer forInitializer(ITokenStream tokenStream) throws GrammarException {
        Token current = tokenStream.current();
        if(TokenConstant.VAR.equals(current.literal()) || TokenConstant.LET.equals(current.literal())){
            return new ForInitializer(parseVariableStatement(tokenStream));
        }else if(current.type() == TokenType.IDENTIFIER){
            return new ForInitializer(variableBody(tokenStream));
        }else{
            throw buildException(current, "for initializer start incorrect");
        }
    }

    private IfStatement parseIfStatement(ITokenStream tokenStream) throws GrammarException {
        IfEntry ifEntry = ifEntry(tokenStream);

        Token current = tokenStream.current();
        ArrayList<ElseIfEntry> elseIfList = new ArrayList<>();
        ElseEntry elseEntry = null;
        if(TokenConstant.ELSE.equals(current.literal())){
            while(TokenConstant.ELSE.equals(current.literal()) && TokenConstant.IF.equals(tokenStream.peek().literal())){
                elseIfList.add(elseIfEntry(tokenStream));
                current = tokenStream.current();
            }

            if(TokenConstant.ELSE.equals(current.literal())){
                elseEntry = elseEntry(tokenStream);
            }
        }
        return new IfStatement(ifEntry, elseIfList, elseEntry);
    }

    private ElseEntry elseEntry(ITokenStream tokenStream) throws GrammarException {
        Token current = tokenStream.next(); // skip else
        if(current == Token.EOF){
            throw buildException(current, "if statement format is incorrect");
        }
        NestStatement elseNest = nestStatement(tokenStream);
        return new ElseEntry(elseNest);
    }

    private ElseIfEntry elseIfEntry(ITokenStream tokenStream) throws GrammarException {
        tokenStream.next(); // else
        IfEntry ifEntry = ifEntry(tokenStream);
        return new ElseIfEntry(ifEntry);
    }

    private IfEntry ifEntry(ITokenStream tokenStream) throws GrammarException {
        Token current = tokenStream.next();
        if(!TokenConstant.LPAREN.equals(current.literal())){
            throw buildException(current, "if condition format is incorrect");
        }
        current = tokenStream.next(); // skip (
        if(current == Token.EOF){
            throw buildException(current, "if condition format is incorrect");
        }

        IExpression ifCondition = parseExpression(tokenStream, Integer.MIN_VALUE);
        if(!TokenConstant.RPAREN.equals(tokenStream.current().literal())){
            throw buildException(tokenStream.current(), "if condition format is incorrect");
        }
        tokenStream.next();

        NestStatement ifNest = nestStatement(tokenStream);
        return new IfEntry(ifCondition, ifNest);
    }

    private NestStatement nestStatement(ITokenStream tokenStream) throws GrammarException {
        Token current = tokenStream.current();
        if(TokenConstant.LBRACE.equals(current.literal())){
            StatementBlock statementBlock = parseStatementBlock(tokenStream);
            return new NestStatement(statementBlock);
        }else{
            IStatement statement = parseStatement(tokenStream);
            return new NestStatement(statement);
        }
    }

    private ImportStatement parseImportStatement(ITokenStream tokenStream) throws GrammarException {
        Token current = tokenStream.next();
        if(current.type() != TokenType.IDENTIFIER){
            throw buildException(current, "package not assign");
        }
        LinkedList<IdentifierNode> path = new LinkedList<>();
        path.add(new IdentifierNode(current));

        while(TokenConstant.DOT.equals(tokenStream.next().literal())){
            current = tokenStream.next();
            if(current.type() != TokenType.IDENTIFIER){
                throw buildException(current, "package format is incorrect");
            }
            path.add(new IdentifierNode(current));
        }
        return new ImportStatement(path);
    }

    private PackageStatement parsePackageStatement(ITokenStream tokenStream) throws GrammarException {
        Token current = tokenStream.next();
        if(current.type() != TokenType.IDENTIFIER){
            throw buildException(current, "package not assign");
        }
        LinkedList<IdentifierNode> path = new LinkedList<>();
        path.add(new IdentifierNode(current));

        while(TokenConstant.DOT.equals(tokenStream.next().literal())){
            current = tokenStream.next();
            if(current.type() != TokenType.IDENTIFIER){
                throw buildException(current, "package format is incorrect");
            }
            path.add(new IdentifierNode(current));
        }
        return new PackageStatement(path);
    }

    private BreakStatement parseBreakStatement(ITokenStream tokenStream){
        tokenStream.next();
        return new BreakStatement();
    }
    private ContinueStatement parseContinueStatement(ITokenStream tokenStream){
        tokenStream.next();
        return new ContinueStatement();
    }

    private ReturnStatement parseReturnStatement(ITokenStream tokenStream) throws GrammarException {
        Token next = tokenStream.next();
        IExpression exp = null;
        if(next != Token.EOF && !TokenConstant.SEMICOLON.equals(next.literal())){
            exp = parseExpression(tokenStream, Integer.MIN_VALUE);
        }
        return new ReturnStatement(exp);
    }

    private VariableStatement parseVariableStatement(ITokenStream tokenStream) throws GrammarException {
        String scopeMark = tokenStream.current().literal();

        Token first = tokenStream.next();
        if(first.type() != TokenType.IDENTIFIER){
            throw buildException(first, "variable statement must have variable name");
        }

        ArrayList<VariableBody> vList = new ArrayList<>();
        while(tokenStream.current().type() == TokenType.IDENTIFIER){
            VariableBody v = variableBody(tokenStream);
            vList.add(v);
            if(!TokenConstant.COMMA.equals(tokenStream.current().literal())){ break; }
            tokenStream.next();
        }

        return new VariableStatement(scopeMark, vList);
    }

    private VariableBody variableBody(ITokenStream tokenStream) throws GrammarException {
        Token ident = tokenStream.current();
        IdentifierNode variableName = new IdentifierNode(ident);

        Token assign = tokenStream.next();
        IExpression body = null;
        if(TokenConstant.ASSIGN.equals(assign.literal())){
            tokenStream.next();
            body = parseExpression(tokenStream, Integer.MIN_VALUE);
            if(body == null){
                throw buildException(assign, "variable assign is not close");
            }
        }
        return new VariableBody(variableName, body);
    }

    private IExpression parseExpression(ITokenStream tokenStream, int precedence) throws GrammarException {
        IExpression exp = parsePrefixExpression(tokenStream);
        if (exp == null) {
            return null;
        }

        Token operatorToken = tokenStream.current();
        Integer p;
        while (operatorToken != Token.EOF && (p = precedenceMap.get(operatorToken.literal())) != null && p > precedence) {
            tokenStream.next();
            IExpression right = parseExpression(tokenStream, p);
            if (right == null) {
                throw buildException(tokenStream.current(), "expression format is incorrect");
            } else {
                exp = new InfixExpression(exp, operatorToken, right);
                operatorToken = tokenStream.current();
            }
        }
        return exp;
    }

    private IExpression parsePrefixExpression(ITokenStream tokenStream) throws GrammarException {
        Token token = tokenStream.current();
        String literal = token.literal();
        switch (literal) {
            case TokenConstant.MINUS:
            case TokenConstant.REFERENCE:
            case TokenConstant.PLUS:
            case TokenConstant.BANG:
                tokenStream.next();
                IExpression exp = plainExpression(tokenStream);
                if(exp == null){ throw buildException(tokenStream.current(), "prefix expression not succeed"); }
                return new PrefixExpression(token, exp);
            default:
                return plainExpression(tokenStream);
        }
    }

    private IExpression plainExpression(ITokenStream tokenStream) throws GrammarException {
        Token current = tokenStream.current();
        Token peek = tokenStream.peek();

        TokenType t = current.type();
        if (TokenConstant.LBRACKET.equals(current.literal()) || (TokenConstant.LBRACKET.equals(peek.literal()))) {
            return arrayExpression(tokenStream);
        } else if ("{".equals(current.literal()) || (TokenConstant.DOT.equals(peek.literal()))) {
            return objectExpression(tokenStream);
        } else if ("(".equals(current.literal()) || (current.type() == TokenType.IDENTIFIER && TokenConstant.LPAREN.equals(peek.literal()))) {
            return parseFunctionOrGroupExpression(tokenStream);
        } else if (t == TokenType.BOOL || t == TokenType.IDENTIFIER || t == TokenType.NUMBER || t == TokenType.STRING || t == TokenType.NULL) {
            return constantExpression(tokenStream);
        }
        return null;
    }

    private IExpression parseFunctionOrGroupExpression(ITokenStream tokenStream) throws GrammarException {
        if(TokenConstant.LPAREN.equals(tokenStream.current().literal())){
            Token next = tokenStream.next();
            if(next == Token.EOF){ throw buildException(next, "group is not close"); }

            List<IExpression> expressions = Collections.emptyList();
            if(!TokenConstant.RPAREN.equals(next.literal())){
                expressions = expressionList(tokenStream);
            }

            if(!TokenConstant.RPAREN.equals(tokenStream.current().literal())){
                throw buildException(tokenStream.current(), "group is not close");
            }
            next = tokenStream.next();

            if(TokenConstant.ARROW.equals(next.literal())){ // 函数定义
                ArrayList<IdentifierNode> formalArgumentList = new ArrayList<>(expressions.size());
                for (IExpression exp : expressions) {
                    if(!(exp instanceof IdentifierNode)){
                        throw buildException(tokenStream.current(), "function define must formal argument");
                    }
                    formalArgumentList.add((IdentifierNode) exp);
                }

                return functionExpression(new FunctionFormalArgumentExpression(formalArgumentList), tokenStream);
            }else{ // 表达式组
                if(expressions.size() != 1){
                    throw buildException(next, "expression group must have only one expression, but " + expressions.size());
                }
                return new ExpressionGroup(expressions.get(0));
            }
        }else{
            return functionRefCallExpression(tokenStream);
        }
    }

    private FunctionExpression functionExpression(FunctionFormalArgumentExpression formalArgumentExpression, ITokenStream tokenStream) throws GrammarException {
        Token bodyStart = tokenStream.next();
        if(bodyStart == Token.EOF){
            throw buildException(bodyStart, "function body is empty");
        }
        StatementBlock statementBlock;
        if(TokenConstant.LBRACE.equals(bodyStart.literal())){
            statementBlock = parseStatementBlock(tokenStream);
        }else{
            IStatement statement = parseStatement(tokenStream);
            statementBlock = new StatementBlock(new Statements(Collections.singleton(statement)));
        }
        FunctionDefineExpression funDefine = new FunctionDefineExpression(formalArgumentExpression, statementBlock);

        FunctionCaller functionCaller = null;
        if(TokenConstant.LPAREN.equals(tokenStream.current().literal())){
            functionCaller = functionCaller(tokenStream);
        }
        return new FunctionExpression(funDefine, functionCaller);
    }

    private StatementBlock parseStatementBlock(ITokenStream tokenStream) throws GrammarException {
        Token next = tokenStream.next();
        if(next == Token.EOF){ throw buildException(next, "statement block is not close"); }
        if(TokenConstant.RBRACE.equals(next.literal())){
            tokenStream.next();
            return new StatementBlock(new Statements(Collections.emptyList()));
        }
        Statements statements = parseStatements(tokenStream);
        if(!TokenConstant.RBRACE.equals(tokenStream.current().literal())){ throw buildException(tokenStream.current(), "statement block is not close"); }

        tokenStream.next();
        return new StatementBlock(statements);
    }

    private FunctionRefCallExpression functionRefCallExpression(ITokenStream tokenStream) throws GrammarException {
        Token current = tokenStream.current();
        IdentifierNode funVar = new IdentifierNode(current);
        current = tokenStream.next();
        if(!TokenConstant.LPAREN.equals(current.literal())){
            throw buildException(current, "function ref call can't find caller");
        }
        FunctionCaller functionCaller = functionCaller(tokenStream);
        return new FunctionRefCallExpression(funVar, functionCaller);
    }

    private FunctionCaller functionCaller(ITokenStream tokenStream) throws GrammarException {
        Token current = tokenStream.next();
        if(current == Token.EOF){ throw buildException(current, "function caller is not close"); }
        if(TokenConstant.RPAREN.equals(current.literal())){
            tokenStream.next();
            return new FunctionCaller(new ExpressionList(Collections.emptyList()), parseCaller(tokenStream));
        }

        List<IExpression> expList = expressionList(tokenStream);
        current = tokenStream.current();
        if(!TokenConstant.RPAREN.equals(current.literal())){
            throw buildException(current, "function caller is not close");
        }
        tokenStream.next();
        return new FunctionCaller(new ExpressionList(expList), parseCaller(tokenStream));
    }

    private List<IExpression> expressionList(ITokenStream tokenStream) throws GrammarException {
        LinkedList<IExpression> list = new LinkedList<>();
        while(true){
            IExpression first = parseExpression(tokenStream, Integer.MIN_VALUE);
            list.add(first);

            Token current = tokenStream.current();
            if(!TokenConstant.COMMA.equals(current.literal())){ break; }
            tokenStream.next();
        }
        return list;
    }

    private IExpression constantExpression(ITokenStream tokenStream) {
        Token current = tokenStream.current();
        TokenType t = current.type();
        switch (t) {
            case NUMBER:
                tokenStream.next();
                return new NumberNode(current);
            case STRING:
                tokenStream.next();
                return new StringNode(current);
            case BOOL:
                tokenStream.next();
                return new BoolNode(current);
            case IDENTIFIER:
                tokenStream.next();
                return new IdentifierNode(current);
            case NULL:
                tokenStream.next();
                return new NullNode(current);
            default:
                return null;
        }
    }

    private ObjectExpression objectExpression(ITokenStream tokenStream) throws GrammarException {
        Token current = tokenStream.current();
        ObjectNode objectObj = null;
        IdentifierNode iden = null;
        if(TokenConstant.LBRACE.equals(current.literal())){
            objectObj = parseObject(tokenStream);
        }else if(current.type() == TokenType.IDENTIFIER){
            iden = new IdentifierNode(current);
            tokenStream.next();
        }else{
            throw buildException(current, "object start prefix is incorrect");
        }

        ObjectCaller objectCaller = null;
        current = tokenStream.current();
        if(TokenConstant.DOT.equals(current.literal())){
            objectCaller = objectCaller(tokenStream);
        }

        return new ObjectExpression(iden, objectObj, objectCaller);
    }

    private ObjectCaller objectCaller(ITokenStream tokenStream) throws GrammarException {
        Token key = tokenStream.next();
        if(key.type() != TokenType.IDENTIFIER){
            throw buildException(key, "object caller is must identifier");
        }
        Token current = tokenStream.next();
        AbstractCaller caller = isCaller(current) ? parseCaller(tokenStream) : null;
        return new ObjectCaller(new IdentifierNode(key), caller);
    }

    private ObjectNode parseObject(ITokenStream tokenStream) throws GrammarException {
        Token current;
        List<ObjectNode.ObjectEntry> entries = new LinkedList<>();
        do{
            current = tokenStream.next();
            if(current.type() != TokenType.IDENTIFIER){
                throw buildException(current, "object key is not identifier");
            }
            Token key = current;
            if(!TokenConstant.COLON.equals(tokenStream.next().literal())){
                throw buildException(tokenStream.current(), "object format is incorrect");
            }
            tokenStream.next();
            IExpression value = parseExpression(tokenStream, Integer.MIN_VALUE);
            entries.add(new ObjectNode.ObjectEntry(new IdentifierNode(key), value));
        }while(TokenConstant.COMMA.equals(tokenStream.current().literal()));

        if(!TokenConstant.RBRACE.equals(tokenStream.current().literal())){
            throw buildException(tokenStream.current(), "object expect end with " + TokenConstant.RBRACE + " but " + tokenStream.current().literal());
        }
        tokenStream.next();
        return new ObjectNode(entries);
    }

    private ArrayExpression arrayExpression(ITokenStream tokenStream) throws GrammarException {
        Token current = tokenStream.current();
        ArrayNode arrayObj = null;
        IdentifierNode arrayIdentifier = null;
        if (TokenConstant.LBRACKET.equals(current.literal())) {
            arrayObj = parseArray(tokenStream);
        } else if (current.type() == TokenType.IDENTIFIER) {
            arrayIdentifier = new IdentifierNode(current);
            tokenStream.next();
        } else {
            throw buildException(current, "array format is not right");
        }

        ArrayCaller arrayCaller = null;
        current = tokenStream.current();
        if(TokenConstant.LBRACKET.equals(current.literal())){
            arrayCaller = arrayCaller(tokenStream);
        }
        return new ArrayExpression(arrayObj, arrayIdentifier, arrayCaller);
    }

    private boolean isCaller(Token token){
        String literal = token.literal();
        return TokenConstant.LBRACKET.equals(literal) || TokenConstant.LPAREN.equals(literal) || TokenConstant.DOT.equals(literal);
    }

    private AbstractCaller parseCaller(ITokenStream tokenStream) throws GrammarException {
        Token current = tokenStream.current();
        AbstractCaller caller = null;
        if(isCaller(current)){
            String literal = current.literal();
            switch (literal){
                case TokenConstant.LBRACKET: // array caller
                    caller = arrayCaller(tokenStream);
                    break;
                case TokenConstant.LPAREN: // function caller
                    caller = functionCaller(tokenStream);
                    break;
                case TokenConstant.DOT: // object caller
                    caller = objectCaller(tokenStream);
                    break;
            }
        }
        return caller;
    }

    private ArrayCaller arrayCaller(ITokenStream tokenStream) throws GrammarException {
        ArrayIndex arrayIndex = arrayIndex(tokenStream);
        Token current = tokenStream.current();
        AbstractCaller caller = isCaller(current) ? parseCaller(tokenStream) : null;
        return new ArrayCaller(arrayIndex, caller);
    }

    private ArrayIndex arrayIndex(ITokenStream tokenStream) throws GrammarException {
        tokenStream.next();
        IExpression index = parseExpression(tokenStream, Integer.MIN_VALUE);
        if(!TokenConstant.RBRACKET.equals(tokenStream.current().literal())){
            throw buildException(tokenStream.current(), "array index is not end with " + TokenConstant.RBRACKET);
        }
        tokenStream.next();
        return new ArrayIndex(index);
    }

    private ArrayNode parseArray(ITokenStream tokenStream) throws GrammarException {
        ArrayList<IExpression> expArr = new ArrayList<>();
        do {
            tokenStream.next();
            IExpression exp = parseExpression(tokenStream, Integer.MIN_VALUE);
            if (exp == null) {
                throw buildException(tokenStream.current(), "array format is incorrect");
            }
            expArr.add(exp);
        } while (TokenConstant.COMMA.equals(tokenStream.current().literal()));

        ArrayNode arrExp = new ArrayNode(expArr.toArray(new IExpression[0]));
        if (!TokenConstant.RBRACKET.equals(tokenStream.current().literal())) {
            throw buildException(tokenStream.current(), "array not end with " + TokenConstant.RBRACKET);
        }
        tokenStream.next();
        return arrExp;
    }

    private GrammarException buildException(Token token, String message) {
        int pos = token == null ? -1 : token.pos();
        return new GrammarException(pos, message);
    }

}
