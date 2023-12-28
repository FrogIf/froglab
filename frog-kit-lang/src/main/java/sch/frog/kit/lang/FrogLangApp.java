package sch.frog.kit.lang;

import sch.frog.kit.lang.exception.IncorrectExpressionException;
import sch.frog.kit.lang.execute.GeneralSession;
import sch.frog.kit.lang.execute.ISession;
import sch.frog.kit.lang.fun.AbstractGeneralFunction;
import sch.frog.kit.lang.fun.FunctionLoadUtil;
import sch.frog.kit.lang.fun.IFunction;
import sch.frog.kit.lang.fun.lang.*;
import sch.frog.kit.lang.parse.grammar.GrammarAnalyzer;
import sch.frog.kit.lang.parse.grammar.GrammarException;
import sch.frog.kit.lang.parse.lexical.LexicalAnalyzer;
import sch.frog.kit.lang.parse.lexical.Token;
import sch.frog.kit.lang.value.Value;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class FrogLangApp {

    private static final FrogLangApp instance = new FrogLangApp();

    public static FrogLangApp getInstance(){
        return instance;
    }

    public static FrogLangApp getInstance(IFunction[] funs){
        return getInstance(funs, null);
    }

    public static FrogLangApp getInstance(IFunction[] funs, Map<String, Value> variables){
        FrogLangApp app = new FrogLangApp();
        if(variables != null){
            for (Map.Entry<String, Value> entry : variables.entrySet()) {
                app.context.addVariable(entry.getKey(), entry.getValue());
            }
        }
        if(funs != null){
            for (IFunction fun : funs) {
                app.context.addVariable(fun.name(), new Value(fun));
            }
        }
        return app;
    }

    private final LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer();
    private final GrammarAnalyzer grammarAnalyzer = new GrammarAnalyzer();

    private final AppContext context = new AppContext();

    public FrogLangApp(){
        init();
    }

    private void init(){
        context.addFunction(new SET());
        context.addFunction(new GET());
        context.addFunction(new Define());
        context.addFunction(new LET());
        context.addFunction(new IF());
        context.addFunction(new COND());
        context.addFunction(new WHILE());
        List<AbstractGeneralFunction> funList = FunctionLoadUtil.load(new LangFunctionController());
        for (AbstractGeneralFunction fun : funList) {
            context.addFunction(fun);
        }
    }

    public void addFunction(IFunction function){
        context.addFunction(function);
    }

    public Collection<IFunction> getFunctions(){
        return context.getFunctions();
    }

    public ISession generateSession(){
        return new GeneralSession(context);
    }

    public Value execute(String expression, ISession session) throws IncorrectExpressionException, GrammarException {
//        List<Token> tokens = lexicalAnalyzer.getToken(expression);
//        IGrammarNode exp = grammarAnalyzer.getGrammarTree(tokens);
//        return exp.evaluate(new GeneralRuntimeContext(session.getAppContext(), session));
        return null;
    }

    public List<Token> tokens(String expression) throws IncorrectExpressionException {
//        return lexicalAnalyzer.getToken(expression);
        return null;
    }


}
