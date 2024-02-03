package sch.frog.lab.lang;

import sch.frog.lab.lang.exception.ExecuteException;
import sch.frog.lab.lang.exception.GrammarException;
import sch.frog.lab.lang.grammar.GrammarAnalyzer;
import sch.frog.lab.lang.grammar.node.Statements;
import sch.frog.lab.lang.grammar.util.AstUtil;
import sch.frog.lab.lang.io.IScriptStream;
import sch.frog.lab.lang.io.StringScriptStream;
import sch.frog.lab.lang.lexical.GeneralTokenStream;
import sch.frog.lab.lang.lexical.LexicalAnalyzer;
import sch.frog.lab.lang.semantic.Executor;
import sch.frog.lab.lang.semantic.GeneralExecuteContext;
import sch.frog.lab.lang.semantic.IExecuteContext;
import sch.frog.lab.lang.semantic.Result;
import sch.frog.lab.lang.value.Value;

import java.util.HashMap;
import java.util.Map;

public class LangRunner {

    private final GrammarAnalyzer grammarAnalyzer = new GrammarAnalyzer();
    private final LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer();
    private final Executor executor = new Executor();

    private final Map<String, Value> extendVariableMap = new HashMap<>();

    private boolean debug = false;

    public LangRunner() {
    }

    public LangRunner(boolean debug){
        this.debug = debug;
    }

    public LangRunner(Map<String, Value> extendVariableMap){
        if(extendVariableMap != null){
            this.extendVariableMap.putAll(extendVariableMap);
        }
    }

    public IExecuteContext newExecuteContext(){
        return new GeneralExecuteContext(executor, extendVariableMap);
    }

    public Result run(IScriptStream scriptStream, IExecuteContext context) throws GrammarException, ExecuteException {
        // 词法分析
        GeneralTokenStream tokenStream = lexicalAnalyzer.parse(scriptStream);

        // 语法分析
        Statements statement = grammarAnalyzer.parse(tokenStream);
        if(debug){
            System.out.println(AstUtil.generateTree(statement)); // 输出抽象语法树
        }

        // 求值
        Result result = executor.execute(statement, context);

        return result;
    }

    public Result run(String expression, IExecuteContext context) throws GrammarException, ExecuteException {
        return run(new StringScriptStream(expression), context);
    }


}
