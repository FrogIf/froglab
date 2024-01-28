package sch.frog.kit.lang;

import sch.frog.kit.lang.exception.ExecuteException;
import sch.frog.kit.lang.exception.GrammarException;
import sch.frog.kit.lang.grammar.GrammarAnalyzer;
import sch.frog.kit.lang.grammar.node.Statements;
import sch.frog.kit.lang.io.IScriptStream;
import sch.frog.kit.lang.io.StringScriptStream;
import sch.frog.kit.lang.lexical.GeneralTokenStream;
import sch.frog.kit.lang.lexical.LexicalAnalyzer;
import sch.frog.kit.lang.semantic.Executor;
import sch.frog.kit.lang.semantic.GeneralExecuteContext;
import sch.frog.kit.lang.semantic.IExecuteContext;
import sch.frog.kit.lang.semantic.Result;
import sch.frog.kit.lang.value.Value;

import java.util.HashMap;
import java.util.Map;

public class LangRunner {

    private final GrammarAnalyzer grammarAnalyzer = new GrammarAnalyzer();
    private final LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer();
    private final Executor executor = new Executor();

    private final Map<String, Value> extendVariableMap = new HashMap<>();

    public LangRunner() {
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
//        System.out.println(AstUtil.generateTree(statement)); // 输出抽象语法树

        // 求值
        Result result = executor.execute(statement, context);

        return result;
    }

    public Result run(String expression, IExecuteContext context) throws GrammarException, ExecuteException {
        return run(new StringScriptStream(expression), context);
    }


}
