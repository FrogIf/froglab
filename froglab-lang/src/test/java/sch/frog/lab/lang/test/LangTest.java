package sch.frog.lab.lang.test;

import sch.frog.lab.lang.LangRunner;
import sch.frog.lab.lang.exception.ExecuteException;
import sch.frog.lab.lang.exception.GrammarException;
import sch.frog.lab.lang.semantic.IExecuteContext;
import sch.frog.lab.lang.semantic.Result;

import java.util.Scanner;

public class LangTest {

    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);

        LangRunner runner = new LangRunner(true);
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
