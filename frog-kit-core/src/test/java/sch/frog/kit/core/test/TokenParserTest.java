package sch.frog.kit.core.test;

import sch.frog.kit.core.FrogLangApp;
import sch.frog.kit.core.exception.ExecuteException;
import sch.frog.kit.core.exception.GrammarException;
import sch.frog.kit.core.exception.IncorrectExpressionException;
import sch.frog.kit.core.execute.ISession;
import sch.frog.kit.core.value.Value;
import sch.frog.kit.core.value.ValueType;

import java.util.Scanner;

public class TokenParserTest {


    public static void main(String[] args) throws IncorrectExpressionException, GrammarException, ExecuteException {
        Scanner sc = new Scanner(System.in);

        FrogLangApp instance = FrogLangApp.getInstance();
        ISession session = instance.generateSession();
        while(true){
            String expression = sc.nextLine();
            if("exit".equals(expression)){ break; }

            try{
                Value val = instance.execute(expression, session);
                if(val.getType() != ValueType.VOID){
                    System.out.println(val);
                }else{
                    System.out.println();
                }
            }catch (Exception e){
                e.printStackTrace();
//                System.out.println(e.getMessage());
            }
        }

    }

}
