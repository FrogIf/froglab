package sch.frog.kit;

import sch.frog.kit.common.CustomViewControl;

public class MainControllerOperator {

    private static MainController mainController;

    static void init(MainController mainController){
        MainControllerOperator.mainController = mainController;
    }

    public static void addTab(CustomViewControl view, String title, boolean closable){
        mainController.addView(view, title, closable);
    }

}
