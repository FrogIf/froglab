package sch.frog.kit.core;

import sch.frog.kit.core.exception.GrammarException;
import sch.frog.kit.core.exception.IncorrectExpressionException;
import sch.frog.kit.core.execute.AppContext;
import sch.frog.kit.core.execute.GeneralSession;
import sch.frog.kit.core.execute.ISession;
import sch.frog.kit.core.fun.IFunction;
import sch.frog.kit.core.fun.lang.DATE;
import sch.frog.kit.core.fun.lang.Define;
import sch.frog.kit.core.fun.lang.Exec;
import sch.frog.kit.core.fun.lang.GET;
import sch.frog.kit.core.fun.lang.NOW;
import sch.frog.kit.core.fun.lang.SET;
import sch.frog.kit.core.fun.lang.UUID;
import sch.frog.kit.core.fun.lang.UUIDI;
import sch.frog.kit.core.parse.grammar.GrammarAnalyzer;
import sch.frog.kit.core.parse.grammar.IGrammarNode;
import sch.frog.kit.core.parse.lexical.LexicalAnalyzer;
import sch.frog.kit.core.parse.lexical.Token;
import sch.frog.kit.core.value.Value;

import java.util.Collection;
import java.util.List;

public class FrogLangApp {

    private static final FrogLangApp instance = new FrogLangApp();

    public static FrogLangApp getInstance(){
        return instance;
    }

    private final LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer();
    private final GrammarAnalyzer grammarAnalyzer = new GrammarAnalyzer();

    private final AppContext context = new AppContext();

    public FrogLangApp(){
        init();
    }

    private void init(){
        context.addFunction(new UUID());
        context.addFunction(new UUIDI());
        context.addFunction(new NOW());
        context.addFunction(new DATE());
        context.addFunction(new SET());
        context.addFunction(new GET());
        context.addFunction(new Define());
        context.addFunction(new Exec());
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
        return exp.evaluate(session);
    }


}
