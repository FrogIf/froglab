package sch.frog.kit.lang;

import sch.frog.kit.lang.exception.GrammarException;
import sch.frog.kit.lang.exception.IncorrectExpressionException;
import sch.frog.kit.lang.execute.GeneralRuntimeContext;
import sch.frog.kit.lang.execute.GeneralSession;
import sch.frog.kit.lang.execute.ISession;
import sch.frog.kit.lang.ext.ExternalFunctionLoadUtil;
import sch.frog.kit.lang.fun.AbstractGeneralFunction;
import sch.frog.kit.lang.fun.FunctionLoadUtil;
import sch.frog.kit.lang.fun.IFunction;
import sch.frog.kit.lang.fun.lang.COND;
import sch.frog.kit.lang.fun.lang.Define;
import sch.frog.kit.lang.fun.lang.GET;
import sch.frog.kit.lang.fun.lang.IF;
import sch.frog.kit.lang.fun.lang.LET;
import sch.frog.kit.lang.fun.lang.LangFunctionController;
import sch.frog.kit.lang.fun.lang.SET;
import sch.frog.kit.lang.fun.lang.WHILE;
import sch.frog.kit.lang.parse.grammar.GrammarAnalyzer;
import sch.frog.kit.lang.parse.grammar.IGrammarNode;
import sch.frog.kit.lang.parse.lexical.LexicalAnalyzer;
import sch.frog.kit.lang.parse.lexical.Token;
import sch.frog.kit.lang.value.VMap;
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
        FrogLangApp app = new FrogLangApp();
        if(funs != null){
            for (IFunction fun : funs) {
                app.context.addVariable(fun.name(), new Value(fun));
            }
        }
        return app;
    }

    public static FrogLangApp getInstance(List<String> externalPaths, IFunction[] funs){
        FrogLangApp app = new FrogLangApp();
        for (String externalPath : externalPaths) {
            try {
                Map<String, List<IFunction>> funPak = ExternalFunctionLoadUtil.load(externalPath);
                for (Map.Entry<String, List<IFunction>> entry : funPak.entrySet()) {
                    VMap funMap = new VMap();
                    List<IFunction> funList = entry.getValue();
                    for (IFunction fun : funList) {
                        funMap.put(fun.name(), new Value(fun));
                    }
                    app.context.addVariable(entry.getKey(), new Value(funMap));
                }
            } catch (Exception e) {
                throw new Error(e);
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
        List<Token> tokens = lexicalAnalyzer.getToken(expression);
        IGrammarNode exp = grammarAnalyzer.getGrammarTree(tokens);
        return exp.evaluate(new GeneralRuntimeContext(session.getAppContext(), session));
    }

    public List<Token> tokens(String expression) throws IncorrectExpressionException {
        return lexicalAnalyzer.getToken(expression);
    }


}
