package sch.frog.kit;

import javafx.application.Application;

public class FrogKitLauncher {

    public static void main(String[] args){
        try{
            Application.launch(FrogKitApplication.class, args);
        }catch (Throwable t){
            t.printStackTrace();
            System.exit(1);
        }
    }

}
