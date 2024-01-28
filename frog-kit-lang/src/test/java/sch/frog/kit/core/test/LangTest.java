package sch.frog.kit.core.test;

import sch.frog.kit.lang.LangRunner;
import sch.frog.kit.lang.exception.ExecuteException;
import sch.frog.kit.lang.exception.GrammarException;
import sch.frog.kit.lang.semantic.IExecuteContext;
import sch.frog.kit.lang.semantic.Result;

import java.util.Scanner;

public class LangTest {

    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);

        LangRunner runner = new LangRunner();
        IExecuteContext context = runner.newExecuteContext();
        while(true){
            String expression = sc.nextLine();
            if("exit".equals(expression)){ break; }
            try {
                Result result = runner.run(expression, context);
                System.out.println(result.value());
            } catch (GrammarException e) {
                System.out.println("message : " + e.getMessage() + ", pos : " + e.getPos());
            } catch (ExecuteException e) {
                e.printStackTrace();
                System.out.println("execute failed : " + e.getMessage());
            }
            System.out.println();
        }
    }

}
