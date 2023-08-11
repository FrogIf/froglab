package sch.frog.kit.win;

import javafx.application.Application;

public class FrogKitBootstrap {

    public static void main(String[] args){
        try{
            Application.launch(FrogKitApplication.class, args);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
