package sch.frog.lab.win;

import javafx.application.Application;

public class FroglabBootstrap {

    public static void main(String[] args){
        try{
            Application.launch(FroglabApplication.class, args);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
